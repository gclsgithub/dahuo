package com.hytc.webmanage.auth;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hytc.webmanage.common.FwUserDetails;
import com.hytc.webmanage.common.config.LoginStatus;
import com.hytc.webmanage.common.jackson.FwJacksonConverter;
import com.hytc.webmanage.common.util.FwBeanUtils;
import com.hytc.webmanage.common.util.FwDateTimeUtils;
import com.hytc.webmanage.common.web.SessionManager;
import com.hytc.webmanage.web.config.SessionKeyMaster;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * ユーザー認証成功時の処理Hander
 */
@Log4j2
@RequiredArgsConstructor
final public class FwAuthSuccessHandler implements AuthenticationSuccessHandler {

    /** JSON 変換 */
    final private FwJacksonConverter jsonConverter;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {


        FwUserDetails userDetails = (FwUserDetails) authentication.getPrincipal();

        // 前回のパスワード変更日は、3か月前でしたら、強制パスワード変更画面に遷移させる。
        if (userDetails.getPwChangeDt() == null || FwDateTimeUtils.me.isXMonthsPasted(userDetails.getPwChangeDt(), 3)) {
            SessionManager.me.set(request, SessionKeyMaster.FORCE_CHANGE_PWD, true);
            // ログインステータス
            SessionManager.me.set(request, SessionKeyMaster.LOGIN_STATUS, LoginStatus.PENDING);
        }


        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try (PrintWriter pw = response.getWriter()) {
            FwAuthInfo out = FwBeanUtils.me.createCopy(userDetails, FwAuthInfo.class);
            pw.println(jsonConverter.objToJson(out));
            pw.flush();
        } catch (IOException e) {
            log.catching(e);
        }
    }

}
