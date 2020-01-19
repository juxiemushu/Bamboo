package com.wei.common.configuration;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Configuration
public class ParameterValidatorConfiguration {

    /**
     *
     * 它是Spring提供的来实现基于方法Method的JSR校验的核心处理器~它能让约束作用在方法入参、返回值上
     *
     * @return
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
        postProcessor.setValidator(validator());
        return postProcessor;
    }

    /**
     * 配置请求参数校验器，对方法的请求参数校验（设置快速返回模式，只有检测到有错，就返回。不会全部检测一遍）
     *
     * @return    校验器
     */
    @Bean
    public Validator validator(){
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class )
                .configure()
                .failFast(true) // true-快速失败返回模式，false-普通模式
//                .addProperty( "hibernate.validator.fail_fast", "true" )
                .buildValidatorFactory();
        return validatorFactory.getValidator();
    }

}
