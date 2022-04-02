package com.hytc.webmanage.auth.io;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthOutVo {

    private String resultCd;
    private LocalDateTime lastLoginTime;
    private String accessToken;
    private String refreshToken;
}
