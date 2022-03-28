package com.hytc.webmanage.common.config;

import com.hytc.webmanage.common.CodeEnum;

/**
 * 機能サブID
 * 
 * @author system
 */
public enum FunctionSubId implements CodeEnum<FunctionSubId> {
    /** ダッシュボード (9900) */
    DASHBOARD("9900"), 
    /** お知らせ管理 (1001) */
    NOTICE_MNG("1001"), 
    /** 顧客情報参照 (1002) */
    REF_USER_INFO("1002"), 
    /** フレンド管理 (1003) */
    FRIEND_MNG("1003"), 
    /** トークンマスタ管理 (2001) */
    TOKEN_MASTER_MNG("2001"), 
    /** プライマリ商品管理 (2002) */
    PRIM_PROD_MNG("2002"), 
    /** 電子契約書面管理 (2003) */
    ELEC_CONT_DOC_MNG("2003"), 
    /** トークン発行管理 (3001) */
    TOKEN_ISSUE_MNG("3001"), 
    /** トークン配布管理 (3002) */
    TOKEN_DISTR_MNG("3002"), 
    /** 売買申請管理 (4001) */
    TRADE_APP_MNG("4001"), 
    /** 媒介売買マッチング (4002) */
    MEDI_TRADE_MATCH("4002"), 
    /** 取引管理 (4003) */
    TRADE_MNG("4003"), 
    /** ST移転履歴 (5001) */
    ST_TRANSFER_HIST("5001"), 
    /** 保有ST一覧 (5002) */
    ST_OWNED_LIST("5002"), 
    ;

    /** Enum値 */
    @lombok.Getter
    private String value;

    /** コンストラクタ */
    private FunctionSubId(final String value) {
        this.value = value;
    }

    @Override
    public boolean is(String branchCd) {
        return branchCd.equals(this.value);
    }
}
