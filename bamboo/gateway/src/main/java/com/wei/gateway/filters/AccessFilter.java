package com.wei.gateway.filters;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Component
public class AccessFilter implements GlobalFilter, Ordered {

    private AntPathMatcher pathMatcher = new AntPathMatcher();

//    @Resource
//    private RedisTemplate<String, Object> redisTemplate ;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String accessToken = this.getAccessToken(exchange.getRequest());

        if(pathMatcher.match("/**/v2/api-docs/**",exchange.getRequest().getPath().value())){
            return chain.filter(exchange);
        }

        if(!pathMatcher.match("/api-auth/**",exchange.getRequest().getPath().value())){
            if (accessToken == null) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                ServerHttpResponse response = exchange.getResponse();
                JSONObject message = new JSONObject();
                message.put("fail_code", 401);
                message.put("fail_msg", "未认证通过！");
                byte[] bits = message.toJSONString().getBytes(StandardCharsets.UTF_8);
                DataBuffer buffer = response.bufferFactory().wrap(bits);
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                // 指定编码，否则在浏览器中会中文乱码
                response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
                return response.writeWith(Mono.just(buffer));
            } else {
                //这里需要验证 Token 是否正确
//                try {
//                    Map<String, Object> params =  (Map<String, Object>) redisTemplate.opsForValue().get("token:" + accessToken) ;
//                    if(params.isEmpty()){
//                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                        return exchange.getResponse().setComplete();
//                    }
//                } catch (Exception e) {
//                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                    return exchange.getResponse().setComplete();
//                }
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -500;
    }

    private String getAccessToken(ServerHttpRequest request) {
        List<String> authorizations = request.getHeaders().get("Authorization");
        String authToken = null;

        if (!CollectionUtils.isEmpty(authorizations)) {
            authToken = authorizations.get(0).substring("Bearer".length()).trim();
        }

        if (StringUtils.isBlank(authToken)) {
            authorizations = request.getQueryParams().get("access_token");
            if (!CollectionUtils.isEmpty(authorizations)) {
                authToken = authorizations.get(0);
            }
        }

        return authToken;
    }

}
