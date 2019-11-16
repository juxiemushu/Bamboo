package com.wei.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
public class BeanPropertyCopyTest {


    @Test
    public void copyByApacheBeanUtils() throws InvocationTargetException, IllegalAccessException {
        UserInfo userInfo = this.initProperty();
        User user = User.builder().build();

        Long startTime = System.currentTimeMillis();
        for(int i=0; i< 1; i++){
            org.apache.commons.beanutils.BeanUtils.copyProperties(user, userInfo);
        }
        Long endTIme = System.currentTimeMillis();
        log.info("============================");
        log.info("" + (endTIme - startTime));
        log.info("{}", user.toString());

    }

    @Test
    public void copyByPropertyUtils() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        UserInfo userInfo = this.initProperty();
        User user = User.builder().build();

        Long startTime = System.currentTimeMillis();
        for(int i=0; i< 1; i++){
            org.apache.commons.beanutils.PropertyUtils.copyProperties(user, userInfo);
        }
        Long endTIme = System.currentTimeMillis();
        log.info("============================");
        log.info("" + (endTIme - startTime));
        log.info("{}", user.toString());
    }

    @Test
    public void copyBySpringBeanUtils(){
        UserInfo userInfo = this.initProperty();
        User user = User.builder().build();

        Long startTime = System.currentTimeMillis();
        for(int i=0; i< 1; i++){
            org.springframework.beans.BeanUtils.copyProperties(userInfo, user, "userName", "userNumber");
        }
        Long endTIme = System.currentTimeMillis();
        log.info("============================");
        log.info("" + (endTIme - startTime));
        log.info("{}", user.toString());
    }

    @Test
    public void copyByBeanCopier(){
        UserInfo userInfo = this.initProperty();
        User user = User.builder().build();

        org.springframework.cglib.beans.BeanCopier copier = BeanCopier.create(UserInfo.class, User.class, false);
        Long startTime = System.currentTimeMillis();
        for(int i=0; i< 1; i++){
            copier.copy(userInfo, user, null);
        }
        Long endTIme = System.currentTimeMillis();
        log.info("============================");
        log.info("" + (endTIme - startTime));
        log.info("{}", user.toString());
    }

    @Test
    public void copyBySetterAndGetter(){
        UserInfo userInfo = this.initProperty();
        User user = User.builder().build();

        Long startTime = System.currentTimeMillis();
        for(int i=0; i< 1; i++){
            user.setUserId(userInfo.getUserId());
            user.setUserName(userInfo.getUserName());
            user.setUserNumber(userInfo.getUserNumber());
            user.setBirthDay(userInfo.getBirthDay());
            user.setCreateDate(userInfo.getCreateDate());
//            user.setAge(userInfo.getAge());
            user.setSex(userInfo.getSex());
            user.setIsActive(userInfo.getIsActive());
            user.setAssets(userInfo.getAssets());
        }
        Long endTIme = System.currentTimeMillis();
        log.info("============================");
        log.info("" + (endTIme - startTime));
        log.info("{}", user.toString());
    }

    private UserInfo initProperty(){
        UserInfo userInfo = UserInfo.builder()
                .userId(1L)
                .userName("初一")
                .userNumber("UN0000001")
                .birthDay(new Date())
                .age(21)
                .sex(1)
                .assets(new BigDecimal("10000.25"))
                .createDate(Timestamp.valueOf(LocalDateTime.now()))
                .isActive(Boolean.TRUE)
                .build();
        return userInfo;
    }

}
