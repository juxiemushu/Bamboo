package com.wei.common.user.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wei.common.user.entity.BambooUser;
import com.wei.common.user.service.IBambooUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *  前端控制器
 *
 * @author shoupeng.wei
 * @since 2020-01-19
 */
@RestController
@RequestMapping("/user")
public class BambooUserController {

    @Autowired
    private IBambooUserService bambooUserService;

    @RequestMapping(value = "/list")
    @ResponseBody
    public IPage<BambooUser> listUserWithPage(int page, int pageSize) {
        return bambooUserService.listUserWithPage(page, pageSize);
    }

}
