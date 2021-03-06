package com.hytc.webmanage.common.util;

import java.util.ArrayList;
import java.util.List;

import com.hytc.webmanage.auth.*;
import com.hytc.webmanage.common.jackson.FwJacksonConverter;
import com.hytc.webmanage.common.resolve.FwMessageResolve;
import com.hytc.webmanage.common.web.FwWebSecurityExpressionHandler;
import com.hytc.webmanage.web.config.MappingMaster;
import com.hytc.webmanage.web.config.filter.ForcedChangePwdFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;


import lombok.RequiredArgsConstructor;

/**
 * httpBasic ??? formLogin ???????????????SpringSecurity
 *
 * @see https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#multiple-httpsecurity
 *
 */
@EnableWebSecurity
public class MultiHttpSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Order(1)
    @Configuration
    @RequiredArgsConstructor()
    static public class FwSecurityConfigWeb extends WebSecurityConfigurerAdapter {

        final private FwAuthenticationProvider authProvider;

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(authProvider);
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            // ????????????URL
            web.ignoring().antMatchers( //
                    "/" // TOP?????????
                    , "/icon/favicon.ico" //
                    , MappingMaster.STATIC.js$wildcard //
                    , MappingMaster.STATIC.css$wildcard //
                    , MappingMaster.STATIC.img$wildcard //
                    , MappingMaster.STATIC.font$wildcard //
                    , MappingMaster.STATIC.mozilla$wildcard
                    , MappingMaster.STATIC.adminlte$wildcard
                    , MappingMaster.STATIC.dataTables$wildcard
            );

            // handler?????????Thymeleaf??????????????????
            final FwWebSecurityExpressionHandler expressionHandler = new FwWebSecurityExpressionHandler();
            expressionHandler.setPermissionEvaluator(new FwWebPermissionEvaluator());
            web.expressionHandler(expressionHandler);
        }

        @Value("${env.login.url:}")
        private String loginUrl;

        /**
         * HttpSecurity?????????
         */
        @Override
        public void configure(HttpSecurity http) throws Exception {
            if(StringUtils.isEmpty(loginUrl)) {
                loginUrl = MappingMaster.AUTH.LOGIN;
            }
            // ????????????????????????
            http.authorizeRequests() //
                    .antMatchers(MappingMaster.AUTH.LOGIN, MappingMaster.AUTH.LOGOUT, MappingMaster.AUTH.TIMEOUT).permitAll() // ????????????????????????????????????????????????????????????????????????
                    .anyRequest().authenticated() // ???????????????URL??????Security ????????????
                    //----------------------------------------
                    .and().sessionManagement() //
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) //
                    // filter ---------------------------------------
                    .and().addFilterAt(authFilter(), UsernamePasswordAuthenticationFilter.class) //
                    .addFilterAfter(new ForcedChangePwdFilter(), SwitchUserFilter.class) //
                    .exceptionHandling().accessDeniedHandler(new FwAccessDeniedHandler()) //
                    // csrf -----------------------------------------
                    .and().csrf().ignoringAntMatchers(MappingMaster.OPEN_MENU) // csrf ??????
                    // cors -----------------------------------------
                    // login ----------------------------------------
                    .and().formLogin() // ???????????????????????????
                    .loginProcessingUrl(MappingMaster.AUTH.AUTH_LOGIN) // ?????????????????????URL
                    .loginPage(loginUrl) // ????????????????????????????????????URL???/login
                    // ???2?????????boolean
                    // true : ?????????????????????????????????top??????????????????
                    // false : (?????????????????????????????????????????????????????????????????????)?????????????????????????????????URL?????????????????????
                    .permitAll() //
                    // logout ----------------------------------------
                    .and().logout() //
                    .logoutUrl(MappingMaster.AUTH.LOGOUT)    // logoutUrl
                    .logoutSuccessUrl(loginUrl) // ???????????????????????????
                    .permitAll() //
            ;

            http.headers().frameOptions().sameOrigin();
        }

        @Autowired
        private FwJacksonConverter jsonConverter;

        /** ???????????????????????? */
        @Autowired
        private FwMessageResolve messageResolve;

        /**
         * I.??????????????????????????????????????????????????????????????????
         * @return
         * @throws Exception
         */
        public UsernamePasswordAuthenticationFilter authFilter() throws Exception {
            UsernamePasswordAuthenticationFilter authFilter = new FwLoginFilter(); // ???
            authFilter.setUsernameParameter("loginId"); // ??? ?????????????????????????????????????????????????????????(Form????????????????????????)
            authFilter.setPasswordParameter("password"); // ??? ?????????????????????????????????????????????????????????(Form????????????????????????)
            authFilter.setAuthenticationManager(super.authenticationManagerBean());
            authFilter.setSessionAuthenticationStrategy(this.sessionAuthenticationStrategy());
            authFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(MappingMaster.AUTH.AUTH_LOGIN, "POST"));
            authFilter.setAuthenticationSuccessHandler(new FwAuthSuccessHandler(jsonConverter));
            authFilter.setAuthenticationFailureHandler(new FwAuthFailureHandler(messageResolve, jsonConverter));
            return authFilter;
        }

        private SessionAuthenticationStrategy sessionAuthenticationStrategy() {
            List<SessionAuthenticationStrategy> delegateStrategies = new ArrayList<>();
            SessionFixationProtectionStrategy sessionFixationProtectionStrategy = new SessionFixationProtectionStrategy();
            sessionFixationProtectionStrategy.setMigrateSessionAttributes(false);
            delegateStrategies.add(sessionFixationProtectionStrategy);
            delegateStrategies.add(new CsrfAuthenticationStrategy(this.csrfTokenRepository()));
            return new CompositeSessionAuthenticationStrategy(delegateStrategies);
        }

        private HttpSessionCsrfTokenRepository csrfTokenRepository() {
            return new HttpSessionCsrfTokenRepository();
        }
    }
}
