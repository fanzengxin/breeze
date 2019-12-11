package org.breeze.core.annotation.service;

import java.lang.annotation.*;

/**
 * @Description: 处理类相关注解
 * @Auther: 黑面阿呆
 * @Date: 2019/2/18 21:48
 * @Version: 1.0.0
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
}
