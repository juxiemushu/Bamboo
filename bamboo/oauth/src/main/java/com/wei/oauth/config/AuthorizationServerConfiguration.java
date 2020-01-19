package com.wei.oauth.config;

import com.wei.oauth.service.impl.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;

/**
 * 认证服务器配置
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {


    /**
     * 注入权限验证控制器 来支持 password grant type
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 注入userDetailsService，开启refresh_token需要用到
     */
    @Autowired
    private MyUserDetailsService userDetailsService;

    /**
     * 数据源
     */
    @Autowired
    private DataSource dataSource;

    /**
     * 对Jwt签名时，增加一个密钥
     * JwtAccessTokenConverter：对Jwt来进行编码以及解码的类
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        /*
           Token签名用的密钥, 发出去的令牌需要密钥签名，验令牌的时候也需要令牌来验签，如果他人获知了我们的密钥
           就可以用我们的密钥来签发我们的JWT令牌，JWT唯一的安全性就是密钥, 别人用我们的密钥来签发我们的JWT令牌就可以随意进入我们的系统
         */
        converter.setSigningKey("bamboo");
        return converter;
    }

    @Autowired
    private WebResponseExceptionTranslator webResponseExceptionTranslator;

    /**
     * 设置token 由Jwt产生，不使用默认的透明令牌
     */
    @Bean
    public JwtTokenStore jwtTokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //开启密码授权类型
        endpoints.authenticationManager(authenticationManager);
        //配置token存储方式
        endpoints.tokenStore(jwtTokenStore());
        endpoints.accessTokenConverter(accessTokenConverter());
        //自定义登录或者鉴权失败时的返回信息
        endpoints.exceptionTranslator(webResponseExceptionTranslator);
        //要使用refresh_token的话，需要额外配置userDetailsService
        endpoints.userDetailsService(userDetailsService);

    }

}