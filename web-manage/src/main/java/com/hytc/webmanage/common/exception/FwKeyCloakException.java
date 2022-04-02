package com.hytc.webmanage.common.exception;


import lombok.Getter;

public class FwKeyCloakException extends RuntimeException {
    
    @Getter
    final private Exception out;
    
    public FwKeyCloakException(Exception out) {
        this.out = out;
    }
}
