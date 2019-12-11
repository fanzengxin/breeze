package org.breeze.core.annotation.controller;

import java.lang.annotation.*;

/**
 * @Description: http请求注解
 * @Auther: 黑面阿呆
 * @Date: 2019-11-27 16:58
 * @Version: 1.0.0
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Api {

    /**
     * 映射地址
     *
     * @return
     */
    String value() default "";

    /**
     * 请求方法
     *
     * @return
     */
    String method() default "";

    /**
     * 接口描述
     *
     * @return
     */
    String descripition() default "";
}
