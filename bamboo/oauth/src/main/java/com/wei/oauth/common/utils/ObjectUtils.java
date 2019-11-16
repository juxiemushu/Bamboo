package com.wei.oauth.common.utils;

public class ObjectUtils {

    public static Boolean isNull(Object obj){
        return null == obj;
    }

    public static Boolean isNotNull(Object obj){
        return !ObjectUtils.isNull(obj);
    }



}
