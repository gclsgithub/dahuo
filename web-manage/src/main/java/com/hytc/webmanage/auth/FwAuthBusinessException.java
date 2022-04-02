package com.hytc.webmanage.auth;

import com.hytc.webmanage.auth.io.FwBaseOut;
import com.hytc.webmanage.auth.io.UserAuthLoginOut;

import org.springframework.security.core.AuthenticationException;

import lombok.Getter;

public class FwAuthBusinessException extends AuthenticationException {

    private static final long serialVersionUID = -4832969662682875399L;

    @Getter
    final private FwBaseOut out;

    public FwAuthBusinessException(UserAuthLoginOut out) {
        super(null);
        this.out = out;
    }
}
