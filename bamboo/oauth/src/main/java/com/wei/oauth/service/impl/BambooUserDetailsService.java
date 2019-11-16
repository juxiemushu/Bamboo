package com.wei.oauth.service.impl;

import com.wei.oauth.common.utils.ObjectUtils;
import com.wei.oauth.entity.BambooUser;
import com.wei.oauth.model.BambooUserDetails;
import com.wei.oauth.service.BambooUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class BambooUserDetailsService implements UserDetailsService {

    private final BambooUserService bambooUserService;

    public BambooUserDetailsService(BambooUserService bambooUserService) {
        this.bambooUserService = bambooUserService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BambooUser user = bambooUserService.findByUsername(username);
        if(ObjectUtils.isNull(user)){
            throw new RuntimeException("用户不存在");
        }

        BambooUserDetails userDetails = new BambooUserDetails(user);

        return userDetails;
    }

}
