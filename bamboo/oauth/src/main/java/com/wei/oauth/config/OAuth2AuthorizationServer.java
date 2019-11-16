package com.wei.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 授权服务器
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    /**
     * 注入AuthenticationManager ，密码模式用到
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 生成Token的转换器，而token令牌默认是有签名的，且资源服务器需要验证这个签名
     *
     * 加密及验签包括两种方式：
     * 对称加密、非对称加密（公钥密钥）
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //对称加密
        converter.setSigningKey("test-secret");

//        //非对称加密，需要我们生成秘钥和公钥，然后将私钥证书导入授权服务器，公钥证书导入资源服务器
//        KeyStoreKeyFactory keyStoreKeyFactory =
//                new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"), "mypass".toCharArray());
//        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("mytest"));
        return converter;
    }

    /**
     * 设置token 由Jwt产生，不使用默认的透明令牌
     */
    @Bean
    public JwtTokenStore jwtTokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                .tokenStore(jwtTokenStore())
                .accessTokenConverter(accessTokenConverter());
    }

    /**
     * 配置客户端详情服务，可以直接写死客户端(inMemory)，也可以从数据库读取(jdbc)
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("clientapp")
                .secret("123")
                .scopes("read")
                //设置支持[密码模式、授权码模式、token刷新]
                .authorizedGrantTypes(
                        "password",
                        "authorization_code",
                        "refresh_token");

    }


}
