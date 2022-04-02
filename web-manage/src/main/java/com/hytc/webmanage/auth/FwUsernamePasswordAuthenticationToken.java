package com.hytc.webmanage.auth;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import lombok.Getter;

/**
 * SpringSecurity ユーザーパスワード認証部分のカスタマイズ。
 */
final public class FwUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    // 会社識別子を保持するフィールドを作成する。
    @Getter
    private final FwAuthInfo authInfo;

    // 認証前の情報(リクエストパラメータで指定された情報)を保持するインスタンスを作成する際に使用するコンストラクタを作成する。
    public FwUsernamePasswordAuthenticationToken(Object principal, Object credentials, FwAuthInfo authInfo) {
        super(principal, credentials);
        this.authInfo = authInfo;
    }

    // 認証済みの情報を保持するインスタンスを作成する際に使用するコンストラクタを作成する。
    // 親クラスのコンストラクタの引数に認可情報を渡すことで、認証済みの状態となる。
    public FwUsernamePasswordAuthenticationToken(Object principal, Object credentials, FwAuthInfo authInfo, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.authInfo = authInfo;
    }
}
