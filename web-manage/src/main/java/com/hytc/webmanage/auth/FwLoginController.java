package com.hytc.webmanage.auth;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hytc.webmanage.auth.io.CheckFlg;
import com.hytc.webmanage.common.FwUserDetails;
import com.hytc.webmanage.common.io.FwBaseOut;
import com.hytc.webmanage.common.jackson.FwJacksonConverter;
import com.hytc.webmanage.common.resolve.FwMessageResolve;
import com.hytc.webmanage.common.resolve.ResultMessages;
import com.hytc.webmanage.web.common.WebConstants;
import com.hytc.webmanage.web.config.MappingMaster;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FwLoginController {

    private final ServletContext context;

    /** メッセージ多言語 */
    private final FwMessageResolve messageResolve;

    /** JSON 変換 */
    private final FwJacksonConverter jsonConverter;

    /**
     * 通常ログイン<br>
     *
     * @see jp.co.jsto.pl.sys.MaintenanceController#forceLogin()
     * @param model
     * @return
     */
    @GetMapping(MappingMaster.AUTH.LOGIN)
    public String login(final HttpServletRequest request //
        , final @AuthenticationPrincipal FwUserDetails userDetails //
    ) {
        // ログイン状態を記憶の場合
        if (userDetails != null && CheckFlg.YES.is(userDetails.getRemLoginSts())) {
            return WebConstants.REDIRECT + userDetails.getHomeUrl().replace(request.getContextPath(), "");
        }
        return "/auth/login.html";
    }

    @GetMapping(MappingMaster.AUTH.TIMEOUT)
    public void sessionTimeout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (isAjaxRequest(request)) {
            // ajax の場合
            response.setHeader(WebConstants.ERROR, WebConstants.ERROR_YES);
            response.setContentType("text/html; charset=UTF-8");
            if (RequestMethod.GET.name().equals(request.getMethod())) {
                response.setStatus(408);
                response.setHeader("timeoutUrl", context.getContextPath()+ MappingMaster.AUTH.LOGIN);
            } else {
                response.setHeader(WebConstants.ERROR_TYPE, WebConstants.ERROR_JSON);
            }
            // セッションタイムアウトメッセージ表示
            try (PrintWriter writer = response.getWriter()) {
                writer.print(jsonConverter.objToJson(FwBaseOut.ng(new ResultMessages("E_PL_0001", null, messageResolve.resolveMessage("E_PL_0001")))));
                writer.flush();
            } catch (IOException e) {
            }
        } else {
            DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
            redirectStrategy.sendRedirect(request, response, MappingMaster.AUTH.LOGIN);
        }
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("x-requested-with"));
    }

    /** 権限エラー画面 */
    @GetMapping(value = MappingMaster.AUTH.ACCESS_DENIED)
    public String accessDenied(@ModelAttribute("funcId") String funcId, @ModelAttribute("funcSubId") String funcSubId) {
        return "/error/access_denied.html";
    }
}