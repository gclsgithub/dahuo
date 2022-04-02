package com.hytc.webmanage.web.biz.mapper;

import com.hytc.webmanage.web.biz.mapper.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginMapper {

    @Select("SELECT * FROM M_USER WHERE student_id=#{userId} and password=#{password} and is_active=1 and is_on=1")
    UserEntity doLogin(String userId, String password);
}
