package com.hytc.webmanage.web.biz.service;

import com.hytc.webmanage.web.biz.mapper.LoginMapper;
import com.hytc.webmanage.web.biz.mapper.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final LoginMapper loginMapper;

    public boolean login(String userId, String password) {
       UserEntity user =  loginMapper.doLogin(userId,password);
       return user == null;
    }
}
