package org.breeze.core.annotation.controller;

import org.breeze.core.constant.ParamFormatCheck;

import java.lang.annotation.*;

/**
 * @Description: 请求参数验证注解
 * @Auther: 黑面阿呆
 * @Date: 2019-12-02 14:57
 * @Version: 1.0.0
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {

    /**
     * 参数名
     * @return
     */
    String name();

    /**
     * 参数描述
     * @return
     */
    String description() default "";

    /**
     * 是否必填
     * @return
     */
    boolean required() default false;

    /**
     * 格式验证，默认字符串
     * @return
     */
    int format() default ParamFormatCheck.String;

    /**
     * 默认值
     * @return
     */
    String defaultValue() default "";

    /**
     * 最大长度
     * @return
     */
    int maxLength() default -1;

    /**
     * 最小长度
     * @return
     */
    int minLength() default -1;

    /**
     * 最大值
     * @return
     */
    long maxValue() default -1L;

    /**
     * 最小值
     * @return
     */
    long minValue() default -1L;
}
