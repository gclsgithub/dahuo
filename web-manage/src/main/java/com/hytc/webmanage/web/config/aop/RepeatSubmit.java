package com.hytc.webmanage.web.config.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatSubmit {

    /** 機能ごとのキー。例：処理のURL */
    String funcKey();

    /** ユーザーが特定できるキー。例：ユーザーＩＤ */
    String uniqueKey();

    /** Lock 最大秒数（業務処理時間で予想） */
    int lockSeconds() default 10;

}
