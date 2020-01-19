package com.wei.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class BaseExceptionInfo {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;
    private Integer status;
    private String exception;
    private String message;
    private String path;
    private String error;

}
