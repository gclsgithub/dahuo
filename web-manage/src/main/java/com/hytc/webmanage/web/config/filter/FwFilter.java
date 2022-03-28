package com.hytc.webmanage.web.config.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hytc.webmanage.common.config.LoginStatus;
import com.hytc.webmanage.common.web.SessionManager;
import com.hytc.webmanage.web.config.MappingMaster;
import com.hytc.webmanage.web.config.MappingMaster.STATIC;
import com.hytc.webmanage.web.config.MappingMaster.AUTH;
import com.hytc.webmanage.web.config.SessionKeyMaster;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.web.filter.OncePerRequestFilter;


import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class FwFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        final String servletPath = request.getServletPath();
        if (servletPath.startsWith(STATIC.css$startsWith)) {
            return true;
        }
        if (servletPath.startsWith(STATIC.js$startsWith)) {
            return true;
        }
        if (servletPath.startsWith(STATIC.img$startsWith)) {
            return true;
        }
        if (servletPath.startsWith(STATIC.adminlte$startsWith)) {
            return true;
        }
        if (servletPath.startsWith(STATIC.dataTables$wildcard)) {
            return true;
        }
        if (servletPath.equals(AUTH.LOGIN)) {
            return true;
        }
        if (servletPath.equals(AUTH.LOGOUT)) {
            return true;
        }
        // メニュー処理
        if (servletPath.equals(MappingMaster.OPEN_MENU)) {
            return true;
        }
        log.debug(() -> servletPath);
        return shouldNotFilter(servletPath);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // ペンディング
        if (LoginStatus.PENDING.equals(SessionManager.me.get(request, SessionKeyMaster.LOGIN_STATUS, LoginStatus.class, LoginStatus.ONLINE))) {
            log.info("FwFilter: {}", this.getClass().getSimpleName());
            pendingLoginFilter(request, response, filterChain);
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * ログイン画面遷移
     *
     * @param request
     * @param response
     * @throws IOException
     */
    protected void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
        redirectStrategy.sendRedirect(request, response, AUTH.LOGIN);
    }

    /**
     *
     * @param servletPath
     * @return
     */
    protected abstract boolean shouldNotFilter(final String servletPath);

    /**
     * チェック処理
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    protected abstract void pendingLoginFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException;
}
