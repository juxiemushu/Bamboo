package cn.wsp.bamboo.core.extension.annotation;

import java.lang.annotation.*;

/**
 * @author WuWei
 * @date 2020/9/21 11:54 下午
 *
 * 在扩展点实现类上使用，设置实现类的别名
 *
 * 非必须，实现类上使用该注解后，配置文件中可以只配置实现类的路径
 */

@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SPIInstance {

    /**
     * 扩展点服务实现类
     *
     * @return  实例别名
     */
    String alisName();

}
