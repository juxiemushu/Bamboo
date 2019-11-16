package com.wei.common.utils;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {

    private Long userId;
    private String userName;
    private String userNumber;
    private Date birthDay;
    private Integer age;
    private int sex;
    private BigDecimal assets;
    private Boolean isActive;
    private Timestamp createDate;

}
