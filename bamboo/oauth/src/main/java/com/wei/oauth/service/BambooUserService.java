package com.wei.oauth.service;

import com.wei.oauth.entity.BambooUser;

import java.util.List;

public interface BambooUserService {

    List<BambooUser> findAll();

    BambooUser findByUsername(String username);

}
