package com.wei.generator.entity;

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
    private String sex;

}
