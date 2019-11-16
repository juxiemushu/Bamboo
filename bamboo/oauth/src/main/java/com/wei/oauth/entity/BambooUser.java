package com.wei.oauth.entity;

import com.wei.oauth.common.enums.SexType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BambooUser {

    private Long id;
    private String username;
    private String password;
    private SexType sex;

}
