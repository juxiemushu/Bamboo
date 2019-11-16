package com.wei.oauth.service.impl;

import com.wei.oauth.common.utils.ObjectUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class BambooAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private BambooUserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if(ObjectUtils.isNull(userDetails)){
            throw new BadCredentialsException("用户不存在");
        }

        if(!password.equals(userDetails.getPassword())){
            throw new BadCredentialsException("密码校验失败");
        }

        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

}
