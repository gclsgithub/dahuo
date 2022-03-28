package com.hytc.webmanage.common.util;

public enum FwDateTimeFormat {
    yyyyMMdd("yyyyMMdd"), //
    yyyyMMddHHmm_SLASH("yyyy/MM/dd HH:mm"), //
    yyyyMMdd_SLASH("yyyy/MM/dd"), //
    yyyyMMddHHmmss("yyyyMMddHHmmss"), //
    yyyyMMddHHmm_HYPHEN("yyyy-MM-dd HH:mm"), //
    yyyyMMddHHmm_T_HYPHEN("yyyy-MM-dd'T'HH:mm"), //
    ;

    private String value;

    private FwDateTimeFormat(String value) {
        this.value = value;
    }

    final public String getValue() {
        return this.value;
    }
}
