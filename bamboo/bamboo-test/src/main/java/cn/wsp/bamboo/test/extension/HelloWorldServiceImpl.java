package cn.wsp.bamboo.test.extension;

/**
 * @author WuWei
 * @date 2020/9/2 2:16 下午
 */

public class HelloWorldServiceImpl implements HelloService {

    @Override
    public String sayHello(String word) {
        System.out.println("Hello World!");
        return "hello " + word;
    }

}
