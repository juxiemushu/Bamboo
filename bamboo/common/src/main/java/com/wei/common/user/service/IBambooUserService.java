package com.wei.common.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.common.user.entity.BambooUser;

/**
 *  服务类
 *
 * @author shoupeng.wei
 * @since 2020-01-19
 */
public interface IBambooUserService extends IService<BambooUser> {

    IPage<BambooUser> listUserWithPage(int page, int pageSize);

}
