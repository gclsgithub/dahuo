package com.hytc.webmanage.auth;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * SpringSecurity に渡す情報Bean
 */
@Getter
@Setter
@Accessors(chain = true)
public class FwAuthInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ユーザID */
    private String loginId;

    /** パスワード */
    private String password;

    /** スタート画面 */
    private String startScreen;

    /** ログイン状態を記憶 */
    private String remLoginSts;

    /** ユーザーエージェント */
    private String userAgent;

    /** 送信元 IP アドレス */
    private String sourceIp;
}
