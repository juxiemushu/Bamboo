package com.wei.common.proxy;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class ActionCglibProxy implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("执行前: 预处理操作");
        Object result = methodProxy.invokeSuper(o, objects);
        System.out.println("执行后: 后置处理");
        return result;
    }
}
