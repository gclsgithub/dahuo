package com.hytc.webmanage.common.io;

import com.hytc.webmanage.common.CodeEnum;


/**
 * 部店コード
 * 
 * @author system
 */
public enum BranchCd implements CodeEnum<BranchCd> {
    /** 一般口座 (0100) */
    NORMAL("0100"), 
    /** 加盟店口座 (0300) */
    MERCHANT("0300"), 
    /** 内部者口座 (0700) */
    INSIDER("0700"), 
    /** 媒介者口座 (0800) */
    MEDIATOR("0800"), 
    /** 営業者口座 (0900) */
    BUSINESS("0900"), 
    ;

    /** Enum値 */
    @lombok.Getter
    private String value;

    /** コンストラクタ */
    private BranchCd(final String value) {
        this.value = value;
    }

    @Override
    public boolean is(String branchCd) {
        return branchCd.equals(this.value);
    }
}
