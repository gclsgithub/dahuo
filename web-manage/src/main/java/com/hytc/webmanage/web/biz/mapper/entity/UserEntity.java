package com.hytc.webmanage.web.biz.mapper.entity;

import lombok.Data;

@Data
public class UserEntity extends BaseEntity {
    private long id;
    private String studentId;
    private String password;
    private String name;
    private int age;
    private String nickName;
    private String mail;
    private String isActive;
    private String isOn;
    private String myQrCode;
    private String myPoint;

}
