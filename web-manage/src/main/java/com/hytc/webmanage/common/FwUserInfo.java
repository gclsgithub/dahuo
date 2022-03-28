package com.hytc.webmanage.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * SpringSecurity ログインユーザー基本情報
 */
@RequiredArgsConstructor
@lombok.EqualsAndHashCode
abstract public class FwUserInfo implements UserDetails {

    private static final long serialVersionUID = 6790663817834115904L;

    /** ユーザID */
    @Getter
    final private String userId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authList = new ArrayList<>();
        return authList;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    @Override
    public String getPassword() {
        return "*"; // パスワードは特に用なし
    }

    @Override
    public boolean isAccountNonExpired() {
        // 実際BL側で判断するので、ここは true 固定。
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 実際BL側で判断するので、ここは true 固定。
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 実際BL側で判断するので、ここは true 固定。
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 実際BL側で判断するので、ここは true 固定。
        return true;
    }
}
