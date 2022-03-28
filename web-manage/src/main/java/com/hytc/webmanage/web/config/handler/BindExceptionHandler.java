package com.hytc.webmanage.web.config.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hytc.webmanage.common.FwUserDetails;
import com.hytc.webmanage.common.exception.FwWebBusinessException;
import com.hytc.webmanage.common.io.FwBaseOut;
import com.hytc.webmanage.common.resolve.FwMessageResolve;
import com.hytc.webmanage.common.resolve.ResultMessages;
import com.hytc.webmanage.common.web.FwBaseForm;
import com.hytc.webmanage.web.common.WebConstants;
import com.hytc.webmanage.web.config.MappingMaster;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestControllerAdvice
@RequiredArgsConstructor
public class BindExceptionHandler {

    final private FwMessageResolve messageResolve;

    /** @Validated 入力データ検証エラー時 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public FwBaseOut handleBindException(final MethodArgumentNotValidException ex, final HttpServletResponse response) {
        log.warn(ex);
        response.setHeader(WebConstants.ERROR, WebConstants.ERROR_YES);
        response.setHeader(WebConstants.ERROR_TYPE, WebConstants.ERROR_JSON);
        FwBaseOut out = FwBaseOut.ng();
        this.bindError(out, ex.getBindingResult());
        return out;
    }

    /** @Validated 入力データ検証エラー時 */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    public FwBaseOut handleBindException(final BindException ex, final HandlerMethod handlerMethod, final HttpServletResponse response) {
        log.warn(ex);
        response.setHeader(WebConstants.ERROR, WebConstants.ERROR_YES);
        response.setHeader(WebConstants.ERROR_TYPE, WebConstants.ERROR_JSON);
        FwBaseOut out = FwBaseOut.ng();
        this.bindError(out, ex.getBindingResult());
        return out;
    }

    private void bindError(FwBaseOut out, BindingResult bindingResult) {
        for (FieldError error : bindingResult.getFieldErrors()) {
            out.getErrors().add(ResultMessages.fromText(messageResolve.resolveMessage(error.getDefaultMessage(), error.getField(), error.getRejectedValue())));
        }
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.OK)
    public FwBaseOut handleException(AccessDeniedException ex //
        , final HttpServletRequest request //
        , final HttpServletResponse response //
        , final RedirectAttributes redirectAttributes //
        , final @AuthenticationPrincipal FwUserDetails userDetails //
        ) throws IOException, InstantiationException, IllegalAccessException {
        log.warn(ex);
        // GETメソッド権限なし
        if (RequestMethod.GET.name().equals(request.getMethod())) {
            String uri = request.getRequestURI();
            String contextPath = request.getContextPath();
            DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
            if (uri.equals(userDetails.getHomeUrl()) == false) {
                redirectStrategy.sendRedirect(request, response, userDetails.getHomeUrl().replace(contextPath, ""));
                return null;
            }

            // メニュー情報を引き渡すできるように編集
            Class<?> cls = WebConstants.MAPPING_FORM.get(uri.replace(contextPath, ""));
            Object obj = cls.newInstance();
            String param = "";
            if (obj instanceof FwBaseForm) {
                FwBaseForm form = (FwBaseForm) obj;
                param = String.format(WebConstants.MENU_FOMART_PARAM, form.funcId(), form.funcSubId());
            }
            redirectStrategy.sendRedirect(request, response, MappingMaster.AUTH.ACCESS_DENIED + param);
            return null;
        }
        response.setHeader(WebConstants.ERROR, WebConstants.ERROR_YES);
        response.setHeader(WebConstants.ERROR_TYPE, WebConstants.ERROR_JSON);
        FwBaseOut out = FwBaseOut.ng(new ResultMessages("ERR_0023", null, messageResolve.resolveMessage("accessDenied")));
        return out;
    }

    @ExceptionHandler(FwWebBusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public FwBaseOut handleException(FwWebBusinessException ex, final HttpServletResponse response) {
        log.warn(ex);
        response.setHeader(WebConstants.ERROR, WebConstants.ERROR_YES);
        response.setHeader(WebConstants.ERROR_TYPE, WebConstants.ERROR_JSON);
        FwBaseOut out = ex.getOut();
        out.backErrorConvert(messageResolve);
        return out;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public FwBaseOut handleException(Exception ex, final HttpServletResponse response) {
        log.catching(ex);
        response.setHeader(WebConstants.ERROR, WebConstants.ERROR_YES);
        response.setHeader(WebConstants.ERROR_TYPE, WebConstants.ERROR_JSON);
        // 予期せぬエラーです。
        String errorCd = "E_BL_9000";
        FwBaseOut out = FwBaseOut.ng(new ResultMessages(errorCd, null, messageResolve.resolveMessage(errorCd)));
        return out;
    }
}
