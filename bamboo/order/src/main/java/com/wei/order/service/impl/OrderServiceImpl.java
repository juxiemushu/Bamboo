package com.wei.order.service.impl;

import com.wei.order.feign.ProductFeignClient;
import com.wei.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final ProductFeignClient productFeignClient;

    public OrderServiceImpl(ProductFeignClient productFeignClient) {
        this.productFeignClient = productFeignClient;
    }

    @Override
    public void getOrderInfo() {
        if(true) {
            throw new RuntimeException("测试异常信息");
        }
        String productInfo = productFeignClient.productInfo();
        log.info("----order: {} " ,productInfo);
    }
}
