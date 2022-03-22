package com.hytc.webmanage.web.config.handler;

import lombok.Data;

@Data
public final class ErrorResponse {
    final private String status = "error";
    private String type;
    private String message;
    private String code;
    private String detail;
}
