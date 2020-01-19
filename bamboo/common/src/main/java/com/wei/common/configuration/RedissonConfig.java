package com.wei.common.configuration;

import com.wei.common.redisson.DistributedLocker;
import com.wei.common.redisson.RedissonDistributedLocker;
import com.wei.common.utils.RedissLockUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonSingle() throws IOException {
        Config config = Config.fromYAML(
                RedissonConfig.class.getClassLoader().getResource("redisson-config.yml"));
        return Redisson.create(config);
    }

    @Bean
    public DistributedLocker distributedLocker() throws IOException {
        RedissonDistributedLocker locker = new RedissonDistributedLocker();
        RedissonClient redissonClient = this.redissonSingle();
        locker.setRedissonClient(redissonClient);
        RedissLockUtils.setLocker(locker);
        return locker;
    }

}
