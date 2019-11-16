package com.wei.oauth.service.impl;

import com.wei.oauth.entity.BambooUser;
import com.wei.oauth.mapper.BambooUserMapper;
import com.wei.oauth.service.BambooUserService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BambooUserServiceImpl implements BambooUserService {

    private final BambooUserMapper bambooUserMapper;

    public BambooUserServiceImpl(BambooUserMapper bambooUserMapper) {
        this.bambooUserMapper = bambooUserMapper;
    }

    @Override
    public List<BambooUser> findAll() {
        return bambooUserMapper.findAll();
    }

    @Override
    public BambooUser findByUsername(String username) {
        return bambooUserMapper.findByUsername(username);
    }
}
