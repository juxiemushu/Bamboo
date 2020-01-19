package com.wei.common.base.model;

import org.springframework.validation.FieldError;

public class FieldErrorInfo extends FieldError {

    private Object targetBean;

    public FieldErrorInfo(String objectName, String field, Object rejectedValue, boolean bindingFailure,
                              String[] codes, Object[] arguments, String defaultMessage) {
        super(objectName, field, rejectedValue, bindingFailure, codes, arguments, defaultMessage);
    }

    public Object getTargetBean() {
        return targetBean;
    }

    public void setTargetBean(Object targetBean) {
        this.targetBean = targetBean;
    }

}
