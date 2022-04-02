package com.hytc.webmanage.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.hytc.webmanage.common.exception.BusinessException;
import com.hytc.webmanage.common.exception.MessageCodeMaster;
import com.hytc.webmanage.common.singlecheck.*;
import lombok.extern.log4j.Log4j2;

@Log4j2
public enum FwSingleCheckUtils {
    me;

    private final String STRING_CLASS = "class java.lang.String";

    private final String BIGDECIMAL_CLASS = "class java.math.BigDecimal";

    private final String DTO_PACKAGE = "jp.co.jsto.biz";

    private final String DTO_PACKAGE_TEST2 = "class jp.co.gt.fw";

    private final String DTO_PACKAGE_List = "java.util.List";

    /**
     * check the single column
     *
     * @param t
     * @param clazz
     * @return file content Str
     */
    public final <T> void checkDtoFile(T t, String clazz) {

        List<BusinessException> exceptionList = new ArrayList<>();

        Class beanClass = t.getClass();

        try {
            Object obj = beanClass.getDeclaredConstructor().newInstance();

            Field[] fields = beanClass.getDeclaredFields();

            Arrays.stream(fields).forEach(field -> {

                field.setAccessible(true);

                String type = field.getGenericType().toString();

                field.setAccessible(true);
                var fieldName = field.getName();
                var name = fieldName.substring(0, 1).toUpperCase(Locale.ROOT) + fieldName.substring(1);

                if (field.isAnnotationPresent(CmMust.class) || field.isAnnotationPresent(CmMaxLength.class) || field.isAnnotationPresent(CmLength.class) || field.isAnnotationPresent(CmLBcSubLength.class)
                    || field.isAnnotationPresent(CmEnumClsCheck.class)) {

                    var cmMust = field.getAnnotation(CmMust.class);
                    var cmMaxLength = field.getAnnotation(CmMaxLength.class);
                    var cmLength = field.getAnnotation(CmLength.class);
                    var cmLBcSubLength = field.getAnnotation(CmLBcSubLength.class);
                    var cmEnumClsCheck = field.getAnnotation(CmEnumClsCheck.class);

                    if (type.toString().startsWith(DTO_PACKAGE) || type.toString().startsWith(DTO_PACKAGE_TEST2)) {
                        try {
                            var value = field.get(t);
                            FwSingleCheckUtils.me.checkDtoFile(value, clazz);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (BusinessException ex) {
                            exceptionList.addAll(ex.getExceptionList());
                        }
                    }

                    // LIST の時
                    if (type.toString().startsWith(DTO_PACKAGE_List)) {
                        try {
                            var value = (List) field.get(t);
                            value.forEach(item -> {
                                FwSingleCheckUtils.me.checkDtoFile(item, clazz);
                            });
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (BusinessException ex) {
                            exceptionList.addAll(ex.getExceptionList());
                        }
                    }

                    // 必須チェック
                    if (cmMust != null && (Arrays.asList(cmMust.runclass()).contains(clazz) || cmMust.runclass().length == 0)) {
                        try {
                            var value = field.get(t);
                            if (value == null || "".equals(value)) {
                                exceptionList.add(new BusinessException(MessageCodeMaster.W_0001, fieldName));
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        try {
                            var value = field.get(t);
                            if (value != null) {
                                // 最大桁数
                                if (cmMaxLength != null && (Arrays.asList(cmMaxLength.runclass()).contains(clazz) || cmMaxLength.runclass().length == 0)) {
                                    // STRING
                                    if (STRING_CLASS.equals(type)) {
                                        try {
                                            var strValue = (String) field.get(t);
                                            if (cmMaxLength.maxlength() < strValue.length()) {
                                                exceptionList.add(new BusinessException(MessageCodeMaster.W_0002, fieldName, String.valueOf(cmMaxLength.maxlength())));
                                            }
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        }
                                    } else if (BIGDECIMAL_CLASS.equals(type)) {
                                        var bcValue = (BigDecimal) field.get(t);
                                        if (cmMaxLength.maxlength() < bcValue.toString().length()) {
                                            exceptionList.add(new BusinessException(MessageCodeMaster.W_0002, fieldName, String.valueOf(cmMaxLength.maxlength())));
                                        }
                                    }
                                }

                                // 整数桁
                                if (cmLength != null && (Arrays.asList(cmLength.runclass()).contains(clazz) || cmLength.runclass().length == 0)) {
                                    if (STRING_CLASS.equals(type)) {
                                        var strValue = (String) field.get(t);
                                        if (strValue != null) {
                                            if (cmLength.length() != strValue.length()) {
                                                exceptionList.add(new BusinessException(MessageCodeMaster.W_0002, fieldName, String.valueOf(cmLength.length())));
                                            }
                                        }
                                    }
                                }

                                // 小数桁
                                if (cmLBcSubLength != null && (Arrays.asList(cmLBcSubLength.runclass()).contains(clazz) || cmLBcSubLength.runclass().length == 0)) {
                                    if (BIGDECIMAL_CLASS.equals(type)) {
                                        var fieldObj = (BigDecimal) field.get(t);
                                        if (fieldObj != null) {
                                            var strBc = fieldObj.remainder(BigDecimal.ONE).movePointRight(fieldObj.scale()).abs().toString();
                                            if (strBc.length() > cmLBcSubLength.length()) {
                                                exceptionList.add(new BusinessException(MessageCodeMaster.W_0002, fieldName, String.valueOf(cmLBcSubLength.length())));
                                            }
                                        }
                                    }
                                }

                                // 範囲チェック
                                if (cmEnumClsCheck != null && (Arrays.asList(cmEnumClsCheck.runclass()).contains(clazz) || cmEnumClsCheck.runclass().length == 0)) {

                                    boolean exceptionFlag = true;
                                    var codeClass = cmEnumClsCheck.clsClass();

                                    var fileCodeValue = (String) field.get(t);

                                    var enumObjs = codeClass.getEnumConstants();

                                    try {
                                        var coingetValue = codeClass.getMethod("getValue");
                                        for (Object enumobj : enumObjs) {
                                            var code = coingetValue.invoke(enumobj).toString();
                                            if (code.equals(fileCodeValue)) {
                                                exceptionFlag = false;
                                            }
                                        }
                                    } catch (NoSuchMethodException e) {
                                        e.printStackTrace();
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    }

                                    if (exceptionFlag) {
                                        exceptionList.add(new BusinessException(MessageCodeMaster.W_0027, fileCodeValue));
                                    }
                                }
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        if (exceptionList.size() != 0) {
            var outException = new BusinessException("");
            outException.getExceptionList().addAll(exceptionList);
            throw outException;
        }
    }
}
