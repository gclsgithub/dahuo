package com.hytc.webmanage.web.biz.mapper.entity;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class BaseEntity {

    private LocalDateTime createDt;

    private String createPrgId;

    private LocalDateTime updateDt;

    private String updatePrgId;
}
