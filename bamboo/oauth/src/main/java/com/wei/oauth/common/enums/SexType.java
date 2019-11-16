package com.wei.oauth.common.enums;

public enum  SexType implements BaseEnum {

    MEN("M", "男"), WOMEN("W", "女");

    private String code;
    private String description;

    SexType(String code, String description){
        this.code = code;
        this.description = description;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
