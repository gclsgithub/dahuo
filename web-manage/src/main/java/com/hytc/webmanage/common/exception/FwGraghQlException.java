package com.hytc.webmanage.common.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import lombok.Getter;

public class FwGraghQlException extends RuntimeException {
    /** serialVersionUID **/
    private static final long serialVersionUID = 1L;

    @Getter
    private List<String> graphqlErrors = new ArrayList<String>();

    public FwGraghQlException() {
        super();
    }

    public FwGraghQlException(List<String> errors) {
        super(CollectionUtils.isEmpty(errors) ? "" : errors.toString());
        if (!CollectionUtils.isEmpty(errors)) {
            this.graphqlErrors.addAll(errors);
        }
    }

    public FwGraghQlException(String message) {
        super(message);
    }

    public FwGraghQlException(String message, Throwable cause) {
        super(message, cause);
    }

    public FwGraghQlException(Throwable cause) {
        super(cause);
    }

    protected FwGraghQlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
