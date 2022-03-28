package com.hytc.webmanage.common.config;


import com.hytc.webmanage.common.CodeEnum;

/**
 * ログインステータス
 *
 * @author system
 */
public enum LoginStatus implements CodeEnum<LoginStatus> {
    /* オフライン (0) */
    OFFLINE("0"),
    /* ペンディング (1) */
    PENDING("1"),
    /* オンライン (2) */
    ONLINE("2"),
    ;

    /* Enum値 */
    @lombok.Getter
    private String value;

    /* コンストラクタ */
    private LoginStatus(final String value) {
        this.value = value;
    }

    @Override
    public boolean is(String branchCd) {
        return branchCd.equals(this.value);
    }
}
