package com.wei.common.advice;

import com.wei.common.base.model.FieldErrorInfo;
import com.wei.common.base.model.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

/**
 * 全局异常处理器，通过 @RestControllerAdvice 注解拦截下所有的请求，然后通过 @ExceptionHandler 处理异常
 *
 * 可以通过 @ExceptionHandler 注解定义多个异常处理器，分别处理不同的异常类型，比如自定义异常
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandlerAdvice {

    @ExceptionHandler(value = BindException.class)
    public ResponseResult<?> validationErrorHandler(BindException ex) {
        try {
            String errorMessage = null;
            for (ObjectError error : ex.getAllErrors()) {
                if (error.getDefaultMessage() != null) {
                    if (error instanceof FieldErrorInfo) {
                        errorMessage = getStandardFieldErrorMessage((FieldErrorInfo) error);
                        break;
                    }
                    errorMessage = error.getDefaultMessage();
                    break;
                } else {
                    errorMessage = error.getCode();
                }
            }
            return ResponseResult.fail(errorMessage);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseResult.fail(e.getMessage(), 500);
        }
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseResult<?> exceptionHandler(Exception exception){
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            exception.printStackTrace(pw);
            log.error(sw.toString());

            if(Objects.isNull(exception.getCause())) {
                return ResponseResult.fail(exception.getMessage());
            }else {
                return ResponseResult.fail(exception.getCause().toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseResult.fail(e.getMessage(), 500);
        }
    }

    /**
     * 取得字段校验的标准错误消息.
     *
     * 诸如 NotEmpty 之类的标准错误,可以同过次方法取得错误消息
     *
     * @param fieldError 可以取到 targetBean
     * @return 与当前语言环境相符的错误描述
     */
    protected String getStandardFieldErrorMessage(FieldErrorInfo fieldError) {
        String field = fieldError.getField();
        Class clazz = fieldError.getTargetBean().getClass();
        clazz = findDeclareClass(clazz, field);
        String fieldPromptMessageKey = clazz.getSimpleName() + "." + field;
        String fieldPrompt = fieldPromptMessageKey.toLowerCase();

        String code = fieldError.getCode();
        String msg = "";
        if(code.equalsIgnoreCase(msg) && fieldError.getDefaultMessage() != null){
            msg =  fieldPrompt + " : " + fieldError.getDefaultMessage();
        }
        return msg;
    }

    /**
     * 找到是哪一个父类定义了属性.
     *
     * @param fromClass
     *            从哪一个类开始查找
     * @param fieldName
     *            属性名
     * @return 定义了指定属性的类,找不到的话,返回fromClass
     */
    private Class findDeclareClass(Class fromClass, String fieldName) {
        Class clazz = fromClass;
        while (clazz.getSuperclass() != null) {
            try {
                clazz.getDeclaredField(fieldName);
                return clazz;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return fromClass;
    }

}
