package com.hytc.webmanage.web.common;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import jp.co.jsto.code.LangCd;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class WebCommonUtil {
    
    public static final String DEFAULT_FORMAT = "#,###.00";

    /**
     * 言語コード取得
     *
     * @return
     */
    public static String getLangCd() {
        String code = LangCd.JA.getValue();
        try {
            Locale locale = (Locale) RequestContextHolder.getRequestAttributes().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, RequestAttributes.SCOPE_SESSION);
            if (locale == null) {
                locale = LocaleContextHolder.getLocale();
            }
            switch (locale.getLanguage()) {
            case WebConstants.EN:
                code = LangCd.EN.getValue();
                break;
            case WebConstants.ZH:
                code = LangCd.ZH.getValue();
                break;
            }
        } catch (Exception ex) {
            log.error("言語コード取得に失敗しました。");
        }
        return code;
    }
    
    /**
     * format bigdecimal to String
     * @param bigDecimal original data
     * @param format DecimalFormat
     * @return
     */
    public static String formatBigDecimal2String(BigDecimal bigDecimal,String format){
        if(bigDecimal == null) {
            return "";
        }
        if(format == null || format.isEmpty()) {
            format = DEFAULT_FORMAT;
        }
        DecimalFormat df = new DecimalFormat(format);
        return df.format(bigDecimal.doubleValue());
    }
}
