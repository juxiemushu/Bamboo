package com.wei.common.proxy;

public class ActionClient {

    public static void main(String[] args) {
        System.getProperties().put("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");
        Action action = new ActionProxy(new PrintAction()).getProxy();
        action.doSomething("明天");
    }

}
