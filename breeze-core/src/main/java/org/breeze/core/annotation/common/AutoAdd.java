package org.breeze.core.annotation.common;

import java.lang.annotation.*;

/**
 * @Description: 自动注入注解
 * @Auther: 黑面阿呆
 * @Date: 2019/2/18 21:51
 * @Version: 1.0.0
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoAdd {
}
