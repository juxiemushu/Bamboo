package com.wei.order.feign;

import com.wei.order.feign.fallback.factory.ProductFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="product", path = "/product", fallbackFactory = ProductFeignClientFallbackFactory.class)
public interface ProductFeignClient {

    @GetMapping(value = "/info")
    String productInfo();

}
