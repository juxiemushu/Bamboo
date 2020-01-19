package com.wei.product.service.impl;

import com.wei.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public String productInfo() throws InterruptedException {

//        long beforeTime = System.currentTimeMillis();
//        Thread.sleep(new Random().nextInt(3000));
//        long time = System.currentTimeMillis() - beforeTime;
//        log.info("休眠时间:" +

        if(true) {
            throw new OutOfMemoryError("运行时异常");
        }

        return "Product";
    }
}
