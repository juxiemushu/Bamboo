package com.wei.common.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import com.wei.common.user.entity.BambooUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 *  Mapper 接口
 *
 * @author shoupeng.wei
 * @since 2020-01-19
 */
@Mapper
public interface BambooUserMapper extends BaseMapper<BambooUser> {

    IPage<BambooUser> listUserWithPage(Page<BambooUser> userPage);

}
