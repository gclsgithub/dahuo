package com.hytc.webmanage.auth.io;


import com.hytc.webmanage.common.CodeEnum;

/**
 * チェックフラグ
 *
 * @author system
 */
public enum CheckFlg implements CodeEnum<CheckFlg> {
    /** はい (1) */
    YES(Values.YES),
    /** いいえ (0) */
    NO(Values.NO),
    ;

    @Override
    public boolean is(String branchCd) {
        return branchCd.equals(this.value);
    }

    public static class Values {
        public static final String YES ="1";
        public static final String NO = "0";
    }


    /** Enum値 */
    @lombok.Getter
    private final String value;

    /** コンストラクタ */
    private CheckFlg(final String value) {
        this.value = value;
    }
}
