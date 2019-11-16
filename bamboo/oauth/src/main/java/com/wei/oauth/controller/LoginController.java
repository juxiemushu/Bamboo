package com.wei.oauth.controller;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginController {

    @RequestMapping(value = "/login/test")
    public Boolean test(HttpServletRequest request){
        return Boolean.TRUE;
    }

    @RequestMapping(value = "/home/test")
    public Boolean home(){
        return Boolean.FALSE;
    }

    @RequestMapping(value = "/api/test")
    public Boolean apiTest(){
        return Boolean.FALSE;
    }

}
