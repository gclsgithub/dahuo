package com.hytc.webmanage.web.config.i18n;

import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

import org.springframework.context.support.ResourceBundleMessageSource;

import lombok.extern.log4j.Log4j2;

/**
 * MessageSource拡張クラス<br>
 * 多言語ファイル定義
 *
 */
@Log4j2
public class FwMessageSource extends ResourceBundleMessageSource {

    public FwMessageSource() {
        log.info("init FwMessageSource");
        super.setBasenames("i18n/WebMessages", "i18n/ValidationMessages");
        super.setDefaultEncoding(StandardCharsets.UTF_8.name());
    }

    @Override
    protected String getStringOrNull(ResourceBundle bundle, String key) {
        return super.getStringOrNull(bundle, key);
    }
}
