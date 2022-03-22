package com.hytc.webmanage;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
/**
 * war の場合、以下を含む
 * @author ko
 *
 */
public class WebManageServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(com.hytc.webmanage.WebManageApplication.class);
    }
}
