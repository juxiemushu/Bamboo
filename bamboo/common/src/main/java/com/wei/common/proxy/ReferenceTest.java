package com.wei.common.proxy;

import org.junit.Test;

public class ReferenceTest {

    @Test
    public void test(){
        Integer flag = new Integer(1);
        System.out.println("-----------" + flag);
        this.testRefen(flag);
        System.out.println("-----------" + flag);
        this.testRefen(flag);
    }

    public void testRefen(Integer flag){
        flag = flag + 1;
    }

}
