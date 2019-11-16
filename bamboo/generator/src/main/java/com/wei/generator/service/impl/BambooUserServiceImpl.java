package com.wei.generator.service.impl;

import com.wei.generator.entity.BambooUser;
import com.wei.generator.mapper.BambooUserMapper;
import com.wei.generator.service.BambooUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BambooUserServiceImpl implements BambooUserService {

    private final BambooUserMapper bambooUserMapper;

    public BambooUserServiceImpl(BambooUserMapper bambooUserMapper) {
        this.bambooUserMapper = bambooUserMapper;
    }

    @Override
    public List<BambooUser> findAll() {
        return bambooUserMapper.findAll();
    }

}