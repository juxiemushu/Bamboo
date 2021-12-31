package cn.wsp.bamboo.core.extension.annotation;

import java.lang.annotation.*;

/**
 * @author WuWei
 * @date 2020/9/18 4:58 下午
 *
 * 定义一个扩展点
 */

@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface SPI {

    /**
     * 设置当前扩展点的默认实现类对应的别名，Dubbo SPI 中配置扩展点实现时是 name=classpath 的格式，每一个实现类都有至少一个对应的 name，
     * @SPI 注解的 value 属性可以设置一个 name，作为默认实现
     *
     * @return  当前扩展点的默认实现类对应的别名
     */
    String value() default "";

}
