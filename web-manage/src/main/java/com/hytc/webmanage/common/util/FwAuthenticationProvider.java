package com.hytc.webmanage.common.util;

import javax.servlet.ServletContext;

import com.hytc.webmanage.auth.FwAuthBusinessException;
import com.hytc.webmanage.auth.FwAuthInfo;
import com.hytc.webmanage.auth.FwAuthService;
import com.hytc.webmanage.auth.FwUsernamePasswordAuthenticationToken;
import com.hytc.webmanage.auth.io.UserAuthLoginIn;
import com.hytc.webmanage.auth.io.UserAuthLoginOut;
import com.hytc.webmanage.common.FwUserDetails;
import com.hytc.webmanage.common.UserAuthInfo;
import com.hytc.webmanage.common.codeEnum.AuthorityCls;
import com.hytc.webmanage.common.io.FwBaseOut;
import com.hytc.webmanage.common.resolve.ResultMessages;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;

/**
 * SpringSecurity ユーザーパスワード認証部分のカスタマイズ。
 */
@Log4j2
@Component
@RequiredArgsConstructor
final public class FwAuthenticationProvider implements AuthenticationProvider {

    private final FwAuthService authService;

    private final ServletContext context;

    @Value("${application.home.url}")
    private String homeUrl;

    @Value("${application.logic.companyCd}")
    private String companyCd;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        // Auto-generated method stub
        FwUsernamePasswordAuthenticationToken authRequest = (FwUsernamePasswordAuthenticationToken) auth;

        // BL 認証処理呼び出し
        final FwAuthInfo authInfo = authRequest.getAuthInfo();
        final String userId = authInfo.getLoginId();

        // ログイン非空チェック
        if (StringUtils.isBlank(userId)) {
            UserAuthLoginOut errOut = new UserAuthLoginOut();
            throw new FwAuthBusinessException(errOut);
        }
        // パスワード非空チェック
        if (StringUtils.isBlank(authInfo.getPassword())) {
            UserAuthLoginOut errOut = new UserAuthLoginOut();
            log.info("{} is auth failure.", userId);
            throw new FwAuthBusinessException(errOut);
        }

        final UserAuthLoginOut authOut = authLogin(authInfo);
        if (authOut.isNotOK()) {
            log.info("{} is auth failure.", authOut.getUserId());
            throw new FwAuthBusinessException(authOut);
        }
        log.info("{} is auth success.", authOut.getUserId());

        final FwUserDetails userDetails = new FwUserDetails(userId);
        BeanUtils.copyProperties(authOut, userDetails);
        // ログイン状態を記憶
        userDetails.setRemLoginSts(authInfo.getRemLoginSts());
        // ホーム遷移設定
        String homeUrl = this.homeUrl;
        if (StringUtils.isBlank(authInfo.getStartScreen()) == false) {
            homeUrl = authInfo.getStartScreen();
        }
        // デフォルトホーム設定権限チェック
        boolean homeUrlFlg = false;
        for (UserAuthInfo info : authOut.getAuthInfoList()) {
            // ディフォルトホーム設定に権限があり
            if (homeUrl.equals(info.getExePath()) && AuthorityCls.NO_AUTHORITY.is(info.getAuthorityCls()) == false) {
                homeUrlFlg = true;
                break;
            }
        }
        // デフォルトホーム設定権限なしの場合
        if (homeUrlFlg == false) {
            for (UserAuthInfo info : authOut.getAuthInfoList()) {
                // ディフォルトホーム設定に権限があり
                if (AuthorityCls.NO_AUTHORITY.is(info.getAuthorityCls()) == false) {
                    homeUrl = info.getExePath();
                    break;
                }
            }
        }
        // ホーム遷移設定
        userDetails.setHomeUrl(context.getContextPath() + homeUrl);
        return new UsernamePasswordAuthenticationToken(userDetails, authRequest.getCredentials(), userDetails.getAuthorities());
    }

    /**
     * ユーザ認証を行う
     *
     * @param authInfo
     * @param
     * @return FwAuthLoginOut
     */
    private UserAuthLoginOut authLogin(final FwAuthInfo authInfo) {
        final UserAuthLoginOut authOut;
        try {
            // 顧客ログイン認証
            final UserAuthLoginIn authIn = new UserAuthLoginIn();
            authIn.setCompanyCd(companyCd);
            authIn.setUserSeqId(BigDecimal.valueOf(Integer.valueOf(authInfo.getLoginId())));

            authIn
            .setPassword(authInfo.getPassword())
            .setUserAgent(authInfo.getUserAgent())
            .setSourceIp(authInfo.getSourceIp())
            ;
            authOut = authService.authLogin(authIn);
        } catch (Exception ex) {
            log.error(ex);
            throw new AuthenticationServiceException(ex.getLocalizedMessage());
        }
        return authOut;
    }

    @Override
    public boolean supports(Class<?> type) {
        return FwUsernamePasswordAuthenticationToken.class.isAssignableFrom(type);
    }
}
