package com.wei.common.service;


import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.cglib.proxy.Enhancer;

public class ActionCglibClient {

    public static void main(String[] args){
//        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "F:\\personal\\Bamboo");
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Action.class);
        enhancer.setCallback(new ActionCglibProxy());
        PrintAction proxy= (PrintAction) enhancer.create();
        proxy.doSomething("明日");

    }

}
