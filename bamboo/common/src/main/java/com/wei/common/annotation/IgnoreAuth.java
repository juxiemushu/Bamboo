package com.wei.common.annotation;

import java.lang.annotation.*;

/**
 * 标记注解，使用该注解的方法或类忽略 Token 校验
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreAuth {
}
