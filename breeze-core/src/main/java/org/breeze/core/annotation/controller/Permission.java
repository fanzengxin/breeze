package org.breeze.core.annotation.controller;

import java.lang.annotation.*;

/**
 * @Description: 权限注解
 * @Auther: 黑面阿呆
 * @Date: 2019-11-27 15:37
 * @Version: 1.0.0
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {

    /**
     * 接口权限代码
     *
     * @return
     */
    String value() default "";

    /**
     * 接口是否需要登录验证
     *
     * @return
     */
    boolean login() default true;

    /**
     * 接口是否需要签名验证
     *
     * @return
     */
    boolean sign() default true;
}
