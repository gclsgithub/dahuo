package com.hytc.webmanage.common.codeEnum;


import com.hytc.webmanage.common.CodeEnum;

/**
 * 掲載顧客区分
 * 
 * @author system
 */
public enum OpenTargetCls implements CodeEnum<OpenTargetCls> {
    /** 全顧客 (0100) */
    ALL_CUSTOMER("0100"), 
    /** 顧客別 (0200) */
    CUSTOMER_SEP("0200"), 
    ;

    /** Enum値 */
    @lombok.Getter
    private String value;

    /** コンストラクタ */
    private OpenTargetCls(final String value) {
        this.value = value;
    }

    @Override
    public boolean is(String branchCd) {
        return branchCd.equals(this.value);
    }
}
