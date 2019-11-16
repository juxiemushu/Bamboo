package com.wei.oauth.controller;

import com.wei.oauth.entity.BambooUser;
import com.wei.oauth.service.BambooUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/bamboo/user")
public class BambooUserController {

    @Autowired
    private BambooUserService bambooUserService;

    @GetMapping
    public List<BambooUser> findAll(){
        return bambooUserService.findAll();
    }

}
