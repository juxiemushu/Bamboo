package com.wei.common.service;

public class PrintAction implements Action {

    @Override
    public void doSomething(String info) {
        System.out.println("打印操作: 输出一些信息-" + info);
    }

}
