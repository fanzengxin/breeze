package org.breeze.core.annotation.controller;

import java.lang.annotation.*;

/**
 * @Description: 基于Controller类的注解
 * @Auther: 黑面阿呆
 * @Date: 2019/2/10 0:42
 * @Version: 1.0.0
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {

    /**
     * 获取接口API前缀
     *
     * @return
     */
    String mapper() default "";

    /**
     * 是否单例
     *
     * @return
     */
    boolean singleton() default true;
}
