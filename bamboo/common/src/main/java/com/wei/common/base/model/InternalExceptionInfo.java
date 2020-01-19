package com.wei.common.base.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InternalExceptionInfo {

    private String timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;

}
