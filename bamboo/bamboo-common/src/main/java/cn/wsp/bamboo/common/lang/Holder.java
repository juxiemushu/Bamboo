package cn.wsp.bamboo.common.lang;

/**
 * @author WuWei
 * @date 2021/12/31 10:56 上午
 */

public class Holder<T> {

    private volatile T value;

    public T get() {
        return this.value;
    }

    public void set(T value) {
        this.value = value;
    }

}
