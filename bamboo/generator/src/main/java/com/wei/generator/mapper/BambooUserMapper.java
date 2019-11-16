package com.wei.generator.mapper;

import com.wei.generator.entity.BambooUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BambooUserMapper {

    List<BambooUser> findAll();

}
