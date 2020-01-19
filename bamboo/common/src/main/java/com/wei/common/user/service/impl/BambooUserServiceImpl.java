package com.wei.common.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.common.user.entity.BambooUser;
import com.wei.common.user.mapper.BambooUserMapper;
import com.wei.common.user.service.IBambooUserService;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 *
 * @author shoupeng.wei
 * @since 2020-01-19
 */
@Service
public class BambooUserServiceImpl extends ServiceImpl<BambooUserMapper, BambooUser> implements IBambooUserService {

    private final BambooUserMapper bambooUserMapper;

    public BambooUserServiceImpl(BambooUserMapper bambooUserMapper) {
        this.bambooUserMapper = bambooUserMapper;
    }

    @Override
    public IPage<BambooUser> listUserWithPage(int page, int pageSize) {
        Page<BambooUser> userPage = new Page<>(page, pageSize);

        return bambooUserMapper.listUserWithPage(userPage);
    }
}
