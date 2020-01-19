package com.wei.common.advice;

import com.alibaba.fastjson.JSON;
import com.wei.common.annotation.IgnoreResponseAdvice;
import com.wei.common.base.model.ResponseResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * 响应结果统一处理器
 */
@RestControllerAdvice
public class GlobalResponseResultAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 判断是否要对拦截的请求的响应内容进行封装
     *
     * @param methodParameter   方法参数
     * @param aClass    类信息
     * @return  是否对请求结果进行处理
     */
    @Override
    @SuppressWarnings("all")
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // 忽略使用 @IgnoreResponseAdvice 注解的方法或类，直接返回
        if(methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)
                || methodParameter.getMethod().isAnnotationPresent(IgnoreResponseAdvice.class)){
            return false;
        }

        return true;
    }

    /**
     * 在响应内容返回之前进行封装，将返回内容封装成统一的返回对象
     *
     * @param result    方法返回结果
     * @param methodParameter   方法参数
     * @param mediaType 响应类型
     * @param aClass    类信息
     * @param serverHttpRequest     请求对象
     * @param serverHttpResponse    响应对象
     * @return  返回的数据对象
     */
    @Nullable
    @Override
    @SuppressWarnings("all")
    public Object beforeBodyWrite(Object result, MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (Objects.isNull(result)) {
            return ResponseResult.success();
        }

        if (result instanceof ResponseResult) {
            return result;
        }

        // String 需要做特殊处理
        if (result instanceof String) {
            return JSON.toJSON(ResponseResult.success(result)).toString();
        }
        return ResponseResult.success(result);
    }

}
