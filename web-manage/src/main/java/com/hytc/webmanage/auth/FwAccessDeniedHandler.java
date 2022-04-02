package com.hytc.webmanage.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hytc.webmanage.web.config.MappingMaster;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class FwAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if (response.isCommitted()) {
            return;
        }
        log.warn(accessDeniedException);
        if (accessDeniedException instanceof MissingCsrfTokenException) {
            DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
            redirectStrategy.sendRedirect(request, response, MappingMaster.AUTH.TIMEOUT);
        } else if (accessDeniedException instanceof InvalidCsrfTokenException) {
            DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
            redirectStrategy.sendRedirect(request, response, MappingMaster.AUTH.TIMEOUT);
        } else {
            response.sendRedirect(request.getServletContext().getContextPath() + MappingMaster.AUTH.LOGIN);
        }
    }

}
