package com.hytc.webmanage.web.config.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FwCacheable {

    String value() default "";

    String key() default "";

    // ジェネリッククラスタイプ
    Class<?> type() default Exception.class;
}
