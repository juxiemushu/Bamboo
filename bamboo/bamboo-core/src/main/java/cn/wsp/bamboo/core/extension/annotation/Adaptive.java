package cn.wsp.bamboo.core.extension.annotation;

import java.lang.annotation.*;

/**
 * @author WuWei
 * @date 2020/10/12 9:03 下午
 *
 *
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Adaptive {

    /**
     *
     *
     * @return
     */
    String[] value() default {};

}
