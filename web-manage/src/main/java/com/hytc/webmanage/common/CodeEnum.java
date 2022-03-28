package com.hytc.webmanage.common;


import java.util.Arrays;
import java.util.Objects;

public interface CodeEnum<E extends Enum<E>> {

    String getValue();

    public default E toEnum(){ return (E)this;}

    /* [staticメソッド]　指定されたCodeEnumを実装したEnumの、指定されたコード値の列挙子を返却する */
    public static <E extends Enum<E>> E of(Class<? extends CodeEnum<E>> clazz, String value) {
        return Arrays.stream(clazz.getEnumConstants()) //
                .filter(e -> Objects.equals(e.getValue(), value)) //
                .map(CodeEnum::toEnum) //
                .findFirst() //
                .orElse(null);
    }

    /* Stringの値は、Enumの値かどうかをチェック */
    public static boolean anyMatch(Class<? extends CodeEnum<?>> clazz, String value) {
        return Arrays.stream(clazz.getEnumConstants()) //
                .anyMatch(e -> e.getValue().equals(value)) //
        ;
    }

    boolean is(String branchCd);
}
