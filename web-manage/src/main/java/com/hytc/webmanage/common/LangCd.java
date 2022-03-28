package com.hytc.webmanage.common;

public enum LangCd implements CodeEnum{
   /** 日本語 (1) */
    JA(Values.JA),
    /** 英語 (2) */
    EN(Values.EN),
    /** 中国語 (3) */
    ZH(Values.ZH),
    ;

    @Override
    public boolean is(String branchCd) {
        return branchCd.equals(this.value);
    }

    public static interface Values {
        public static final String JA = "1";
        public static final String EN = "2";
        public static final String ZH = "3";
    }

    /* Enum値 */
    @lombok.Getter
    private String value;

    /* コンストラクタ */
    private LangCd(final String value) {
        this.value = value;
    }
}
