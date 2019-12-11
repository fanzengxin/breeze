package org.breeze.core.annotation.controller;

import java.lang.annotation.*;

/**
 * @Description: 请求参数注解集
 * @Auther: 黑面阿呆
 * @Date: 2019-11-29 09:37
 * @Version: 1.0.0
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Params {

    Param[] value();
}
