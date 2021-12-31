package cn.wsp.bamboo.test.extension;

/**
 * @author WuWei
 * @date 2020/9/2 2:17 下午
 */

public class HelloCatServiceImpl implements HelloService {

    @Override
    public String sayHello(String word) {
        System.out.println("Hello Cat!");
        return "hello " + word;
    }

}
