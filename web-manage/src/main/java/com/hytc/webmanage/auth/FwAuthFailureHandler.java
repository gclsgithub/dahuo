package com.hytc.webmanage.auth;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hytc.webmanage.common.io.FwBaseOut;
import com.hytc.webmanage.common.jackson.FwJacksonConverter;
import com.hytc.webmanage.common.resolve.FwMessageResolve;
import com.hytc.webmanage.common.resolve.ResultMessages;
import com.hytc.webmanage.web.common.WebConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * ユーザー認証失敗時の処理Hander
 */
@Log4j2
@RequiredArgsConstructor
final public class FwAuthFailureHandler implements AuthenticationFailureHandler {

    /** メッセージ多言語 */
    final private FwMessageResolve messageResolve;

    /** JSON 変換 */
    final private FwJacksonConverter jsonConverter;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try (PrintWriter pw = response.getWriter()) {
            response.setHeader(WebConstants.ERROR, WebConstants.ERROR_YES);
            response.setHeader(WebConstants.ERROR_TYPE, WebConstants.ERROR_JSON);

            ResultMessages messages = ResultMessages.error();
            if (exception instanceof FwAuthBusinessException) {
                List<ResultMessages> resultMessages = ((FwAuthBusinessException) exception).getOut().getErrors();
                for (ResultMessages error : resultMessages) {
                    error.setText(messageResolve.resolveMessage(error));
                    messages.add(error);
                }
            } else if (exception instanceof BadCredentialsException) {
                // E.ユーザー存在しない。或いはパスワード不一致。
                messages.add(ResultMessages.fromText(messageResolve.resolveMessage("E_BL_0058")));
            } else {
                // E.予期せぬエラーです
                messages.add(ResultMessages.fromText(messageResolve.resolveMessage("E_BL_9000")));
            }
            pw.println(jsonConverter.objToJson(FwBaseOut.ng(messages)));
            pw.flush();
        } catch (IOException e) {
            log.catching(e);
        }
    }
}
