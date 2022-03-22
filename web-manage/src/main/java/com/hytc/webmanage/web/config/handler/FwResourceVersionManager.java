package com.hytc.webmanage.web.config.handler;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class FwResourceVersionManager {

    @Value("${fw.resource.version:#{null}}")
    private Optional<String> ver;

    private static final String ts = Long.toString(System.currentTimeMillis());

    @ModelAttribute("resver")
    public String ver() {
        return this.ver.orElse(ts);
    }
}
