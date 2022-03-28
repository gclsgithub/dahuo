package com.hytc.webmanage.web.config.filter;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hytc.webmanage.common.web.SessionManager;
import com.hytc.webmanage.web.config.MappingMaster;
import com.hytc.webmanage.web.config.SessionKeyMaster;
import org.springframework.security.web.DefaultRedirectStrategy;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ForcedChangePwdFilter extends FwFilter {

    public ForcedChangePwdFilter() {
    }

    protected boolean shouldNotFilter(final String servletPath) {
        // パスワード強制変更画面遷移
        if (servletPath.equals(MappingMaster.FORCE_CHANGE_PWD)) {
            return true;
        }
        // パスワード変更処理
        if (servletPath.equals(MappingMaster.DO_CHANGE_PWD)) {
            return true;
        }
        return false;
    }

    @Override
    protected void pendingLoginFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("ForcedChangePwdFilter before!!");
        final String servletPath = request.getServletPath();

        /** /forceChangePwd */
        Boolean changepw = SessionManager.me.get(request, SessionKeyMaster.FORCE_CHANGE_PWD, Boolean.class, Boolean.FALSE);
        if (changepw.booleanValue()) {
            if (Objects.equals(servletPath, MappingMaster.FORCE_CHANGE_PWD)) {
                return;
            }
            DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
            redirectStrategy.sendRedirect(request, response, MappingMaster.FORCE_CHANGE_PWD);
            return;
        }

        filterChain.doFilter(request, response);
        log.debug("ForcedChangePwdFilter after!!");
    }
}