package com.wei.order.feign.fallback.factory;

import com.sun.jdi.InternalException;
import com.wei.order.feign.ProductFeignClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ProductFeignClientFallbackFactory implements FallbackFactory<ProductFeignClient> {

    @Override
    public ProductFeignClient create(Throwable throwable) {

        System.out.println("异常信息：" + throwable.getMessage());

        return null;
    }
}
