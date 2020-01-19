package com.wei.common.exceptions;

/**
 * 自定义FeignClient 异常，当 FeignClient 调用接口错误后，抛出该自定义异常
 */
public class CustomerFeignClientException extends RuntimeException {

    private int errorCode;

    public CustomerFeignClientException() {
        this.errorCode = 0;
    }

    public CustomerFeignClientException(String message) {
        super(message);
        this.errorCode = 0;
    }

    public CustomerFeignClientException(int errorCode) {
        this.errorCode = errorCode;
    }

    public CustomerFeignClientException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int errorCode() {
        return this.errorCode;
    }

}


