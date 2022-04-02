package com.hytc.webmanage.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hytc.webmanage.web.common.WebConstants;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * SpringSecurity ユーザーパスワード認証部分のカスタマイズ。
 */
final public class FwLoginFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if (!request.getMethod().equalsIgnoreCase("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        // リクエストパラメータから取得した認証情報(ユーザー名、パスワード、会社識別子)より、SFUsernamePasswordAuthenticationTokenのインスタンスを生成する。
        // Obtain UserName, Password, CompanyId
        String username = super.obtainUsername(request);
        String password = super.obtainPassword(request);
        String startScreen = request.getParameter(WebConstants.START_SCREEN);
        String remLoginSts = request.getParameter(WebConstants.REM_LOGIN_STS);

        // ユーザー名やパスワードが未入力の場合の動きを検討。
        if (username == null) {
            username = "";
        } else {
            username = username.trim();
        }
        if (password == null) {
            password = "";
        }
        // カスタマイズログイン情報
        FwAuthInfo authInfo = new FwAuthInfo();
        authInfo
        .setLoginId(username)
        .setPassword(password)
        .setStartScreen(startScreen)
        .setRemLoginSts(remLoginSts)
        .setUserAgent(request.getHeader(WebConstants.USER_AGENT))
        .setSourceIp(request.getRemoteAddr())
        ;
        FwUsernamePasswordAuthenticationToken authRequest = new FwUsernamePasswordAuthenticationToken(username, password, authInfo);

        // Allow subclasses to set the "details" property
        super.setDetails(request, authRequest);

        // リクエストパラメータで指定された認証情報(CustomUsernamePasswordAuthenticationTokenのインスタンス)を指定して、org.springframework.security.authentication.AuthenticationManagerのauthenticateメソッドを呼び出す。
        // AuthenticationManagerのメソッドを呼び出すと、AuthenticationProviderの認証処理が呼び出される。
        return this.getAuthenticationManager().authenticate(authRequest);
    }

}
