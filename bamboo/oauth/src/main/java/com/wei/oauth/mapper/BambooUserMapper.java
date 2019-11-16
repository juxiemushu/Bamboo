package com.wei.oauth.mapper;

import com.wei.oauth.entity.BambooUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BambooUserMapper {

    List<BambooUser> findAll();

    BambooUser findByUsername(String username);

}
