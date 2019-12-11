package com.wei.common.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ActionProxy implements InvocationHandler {

    private Action target;

    public ActionProxy(Action target){
        this.target = target;
    }

    public Action getProxy(){
        return (Action) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        System.out.println("输出之前: 预处理操作");
        Object result = method.invoke(target, args);
        System.out.println("输出之后: 后置处理");
        return result;
    }
}
