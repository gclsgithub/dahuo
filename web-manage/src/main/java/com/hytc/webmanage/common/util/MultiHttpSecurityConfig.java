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
 * httpBasic と formLogin 同時対応のSpringSecurity
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
            // 除外するURL
            web.ignoring().antMatchers( //
                    "/" // TOPページ
                    , "/icon/favicon.ico" //
                    , MappingMaster.STATIC.js$wildcard //
                    , MappingMaster.STATIC.css$wildcard //
                    , MappingMaster.STATIC.img$wildcard //
                    , MappingMaster.STATIC.font$wildcard //
                    , MappingMaster.STATIC.mozilla$wildcard
                    , MappingMaster.STATIC.adminlte$wildcard
                    , MappingMaster.STATIC.dataTables$wildcard
            );

            // handler追加。Thymeleafから利用可能
            final FwWebSecurityExpressionHandler expressionHandler = new FwWebSecurityExpressionHandler();
            expressionHandler.setPermissionEvaluator(new FwWebPermissionEvaluator());
            web.expressionHandler(expressionHandler);
        }

        @Value("${env.login.url:}")
        private String loginUrl;

        /**
         * HttpSecurityの設定
         */
        @Override
        public void configure(HttpSecurity http) throws Exception {
            if(StringUtils.isEmpty(loginUrl)) {
                loginUrl = MappingMaster.AUTH.LOGIN;
            }
            // フォームログイン
            http.authorizeRequests() //
                    .antMatchers(MappingMaster.AUTH.LOGIN, MappingMaster.AUTH.LOGOUT, MappingMaster.AUTH.TIMEOUT).permitAll() // ログイン・ログアウト・タイムアウトの処理を許す。
                    .anyRequest().authenticated() // 上記以外のURLは、Security 範囲内。
                    //----------------------------------------
                    .and().sessionManagement() //
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) //
                    // filter ---------------------------------------
                    .and().addFilterAt(authFilter(), UsernamePasswordAuthenticationFilter.class) //
                    .addFilterAfter(new ForcedChangePwdFilter(), SwitchUserFilter.class) //
                    .exceptionHandling().accessDeniedHandler(new FwAccessDeniedHandler()) //
                    // csrf -----------------------------------------
                    .and().csrf().ignoringAntMatchers(MappingMaster.OPEN_MENU) // csrf 有効
                    // cors -----------------------------------------
                    // login ----------------------------------------
                    .and().formLogin() // フォーム認証を行う
                    .loginProcessingUrl(MappingMaster.AUTH.AUTH_LOGIN) // ログイン処理のURL
                    .loginPage(loginUrl) // ログインページを表示するURLは/login
                    // 第2引数のboolean
                    // true : ログイン画面した後必ずtopにとばされる
                    // false : (認証されてなくて一度ログイン画面に飛ばされても)ログインしたら指定したURLに飛んでくれる
                    .permitAll() //
                    // logout ----------------------------------------
                    .and().logout() //
                    .logoutUrl(MappingMaster.AUTH.LOGOUT)    // logoutUrl
                    .logoutSuccessUrl(loginUrl) // ログアウト後の行先
                    .permitAll() //
            ;

            http.headers().frameOptions().sameOrigin();
        }

        @Autowired
        private FwJacksonConverter jsonConverter;

        /** メッセージ多言語 */
        @Autowired
        private FwMessageResolve messageResolve;

        /**
         * I.ログインチェックロジックの前に動く処理を設定
         * @return
         * @throws Exception
         */
        public UsernamePasswordAuthenticationFilter authFilter() throws Exception {
            UsernamePasswordAuthenticationFilter authFilter = new FwLoginFilter(); // ★
            authFilter.setUsernameParameter("loginId"); // ★ フォームからこのキーでユーザー名を取得(Formの設定を合わせる)
            authFilter.setPasswordParameter("password"); // ★ フォームからこのキーでパスワードを取得(Formの設定を合わせる)
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
