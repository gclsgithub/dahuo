package com.hytc.webmanage.common.codeEnum;


import com.hytc.webmanage.common.CodeEnum;

/**
 * 権限区分
 * 
 * @author system
 */
public enum AuthorityCls implements CodeEnum<AuthorityCls> {
    /** 権限なし (0000) */
    NO_AUTHORITY("0000"), 
    /** 参照 (0100) */
    REFERENCE("0100"), 
    /** 更新 (0200) */
    UPDATE("0200"), 
    /** 承認 (0300) */
    APPROVAL("0300"), 
    ;

    /** Enum値 */
    @lombok.Getter
    private String value;

    /** コンストラクタ */
    private AuthorityCls(final String value) {
        this.value = value;
    }

    @Override
    public boolean is(String branchCd) {
        return branchCd.equals(this.value);
    }
}
