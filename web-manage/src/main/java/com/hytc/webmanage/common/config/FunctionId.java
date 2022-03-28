package com.hytc.webmanage.common.config;

import com.hytc.webmanage.common.CodeEnum;

public enum FunctionId implements CodeEnum<FunctionId> {
    /** ログイン処理 (S0001) */
    TOKEN("S0001"),
    /** プライマリ商品情報処理 (S0002) */
    TOKEN_PRODUCT("S0002"),
    /** STマスタ情報処理 (S0003) */
    TOKEN_STMST("S0003"),

    /** トークン発行管理処理 (S0004) */
    TOKEN_ISSUANCE("S0004"),

    /** トークン発行管理発行可能チェック処理 (S0005) */
    TOKEN_ISSUANCE_CHECK("S0005"),
    /** トークン配布管理処理 (S0006) */
    TOKEN_DISTRIBUTE("S0006"),

    /** 電子契約書管理処理 (S0007) */
    TOKEN_CONTRACT("S0007"),

    /** 売買申請処理 (S0008) */
    TRAND_APPLY("S0008"),

    /** 取引管理情報処理 (S0009) */
    TRAND_TRANSACTION("S0009"),

    /** ST移転履歴情報取得処理 (S0010) */
    BLOCKCHAIN_TRANSFER("S0010"),
    /** 保有ST情報取得処理 (S0011) */
    BLOCKCHAIN_BALANCE("S0011"),
    /** 管理者パスワード更新処理 (S0012) */
    SYS_PWDUPD("S0012"),
    /** お知らせ情報処理 (S0013) */
    CUSTOMER_NOTICE("S0013"),

    /** 顧客情報処理 (S0014) */
    CUSTOMER_CUSTOMER("S0014"),

    /** フレンド管理情報処理 (S0015) */
    CUSTOMER_FRIEND("S0015"),

    /** 媒介マッチング情報処理 (S0016) */
    TRAND_MATCHING("S0016"),

    /** ダッシュボード処理 (S0017) */
    TRAND_DASHBOARD("S0017"),
    /** メールテンプレート情報処理 (S0018) */
    SYS_EMAILTEMP("S0018"),

    /** トークン媒介委託情報処理 (S0019) */
    TOKEN_MEDIATION_SALE("S0019"),

    /** トークン委託企業情報取得処理 (S0020) */
    TOKEN_MEDIATION_COMPANY("S0020"),
    /** 媒介トークン情報処理 (S0021) */
    TOKEN_MEDIATION("S0021"),

    /** 譲受者決済通知受処理 (S0022) */
    TRAND_SETTLEMENT("S0022"),
    /** EUCデータ取得処理 (S0023) */
    EUC_INFO("S0023"),
    /** ログイン処理 (Ｉ0001) */
    LOGIN_AUTHORITY("Ｉ0001"),
    /** 認証連携管理情報取得処理 (Ｉ0002) */
    LOGIN_AUTHORITY_INFO("Ｉ0002"),
    /** 投資家お知らせ情報取得処理 (Ｉ0003) */
    NOTICE("Ｉ0003"),
    /** お知らせ管理情報情報閲覧取得処理 (Ｉ0004) */
    NOTICE_READED("Ｉ0004"),

    ADMIN_LOGIN_INFO("A0001");

    /* Enum値 */
    @lombok.Getter
    private String value;

    /* コンストラクタ */
    private FunctionId(final String value) {
        this.value = value;
    }

    @Override
    public boolean is(String branchCd) {
        return branchCd.equals(this.value);
    }
}
