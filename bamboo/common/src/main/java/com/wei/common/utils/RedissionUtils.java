package com.wei.common.utils;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
public class RedissionUtils {

    private static RedissonClient redissonClient;

    private RedissionUtils(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public static void set(String key, String value) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(value);
    }

}
