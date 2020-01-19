package com.wei.common.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wei.common.base.model.InternalExceptionInfo;
import com.wei.common.exceptions.CustomerFeignClientException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

/**
 * FeignClient 的自定义配置类
 *
 */
@Slf4j
@Configuration
public class FeignClientConfiguration {

    /**
     * Feign 异常处理，FeignClient 调用接口异常后会触发熔断机制，抛出信息的异常，覆盖旧的异常信息。为了异常消息的传递，需要自定义解码器
     *
     * @return  异常解码器
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignClientErrorDecoder();
    }

    static class FeignClientErrorDecoder implements ErrorDecoder {

        private ObjectMapper objectMapper = new ObjectMapper();

        /**
         * FeignClient 的异常处理类，可以将接口提供方抛出的异常传递给消费者。否则，Feign 触发熔断机制，抛出新的异常会覆盖生产者抛出的异常信息
         * response 是异常信息，相比于 SpringBoot 1.x，缺少了 exception 字段，该字段原本存储的是生产者抛出的异常类类型，所以现在无法获取具体的异常类
         *
         * @param methodKey 方法名
         * @param response  响应信息，包含异常消息
         * @return  自定义逻辑处理后的异常类
         */
        @Override
        public Exception decode(String methodKey, Response response) {
            try {
                if (response.body() != null) {
                    String body = Util.toString(response.body().asReader());
                    log.error("FeignClient 调用报错: {}, 异常信息: {}", methodKey, body);

                    InternalExceptionInfo exceptionInfo =
                            this.objectMapper.readValue(body.getBytes(StandardCharsets.UTF_8.name()), InternalExceptionInfo.class);

                    String message = exceptionInfo.getMessage();
                    return new CustomerFeignClientException(message);
                }
            } catch (Exception e) {
                log.error("FeignClient 异常解码器解析异常({})时报错, 异常信息: {}", methodKey, e.getMessage());
                return new CustomerFeignClientException(e.getMessage());
            }

            log.error("系统异常({}),请联系管理员", methodKey);
            return new CustomerFeignClientException("系统异常({}),请联系管理员");
        }
    }

}
