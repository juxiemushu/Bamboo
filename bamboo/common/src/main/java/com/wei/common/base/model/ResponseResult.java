package com.wei.common.base.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = -2951586693380989698L;

    private Boolean success;
    private String message;
    private T data;
    private Integer code;

    public static ResponseResult success() {
        return ResponseResult.builder()
                .success(Boolean.TRUE)
                .code(200)
                .build();
    }

    public static ResponseResult success(Object data) {
        return ResponseResult.builder()
                .success(Boolean.TRUE)
                .code(200)
                .data(data)
                .build();
    }

    public static ResponseResult failData(Object message) {
        return ResponseResult.builder()
                .success(Boolean.FALSE)
                .data(message)
                .build();
    }

    public static ResponseResult fail(String message) {
        return ResponseResult.builder()
                .success(Boolean.FALSE)
                .message(message)
                .build();
    }

    public static ResponseResult fail(String message, Integer code) {
        return ResponseResult.builder()
                .success(Boolean.FALSE)
                .code(code)
                .message(message)
                .build();
    }

}
