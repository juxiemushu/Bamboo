package com.wei.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/base")
public class BaseController {

    @GetMapping(value = "/route")
    public String routingTest(){
        return "Hello";
    }

    @GetMapping(value = "/default")
    public String defaultRoute(){
        return "hello 小明";
    }

}
