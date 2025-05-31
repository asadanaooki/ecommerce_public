package com.example;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginUser implements UserDetails {
    private final String userId;
    
    private final String email;
    
    private final String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public String getUsername() {
        // TODO 自動生成されたメソッド・スタブ
        return email;
    }

}
