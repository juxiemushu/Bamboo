package com.wei.oauth.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

public class BambooJwtTokenStore extends JwtTokenStore {

    @Autowired
    private BambooJwtAccessTokenConverter jwtAccessTokenConverter;

    /**
     * Create a JwtTokenStore with this token enhancer (should be shared with the DefaultTokenServices if used).
     *
     * @param jwtAccessTokenConverter
     */
    public BambooJwtTokenStore(BambooJwtAccessTokenConverter jwtAccessTokenConverter) {
        super(jwtAccessTokenConverter);
    }


}
