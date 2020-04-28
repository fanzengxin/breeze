package org.breeze.core.annotation.service;

import org.breeze.core.config.DataBaseConfig;

import java.lang.annotation.*;

/**
 * @Description:
 * @Auther: 黑面阿呆
 * @Date: 2019/8/20 10:42
 * @Version: 1.0.0
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataBase {

    /**
     * 数据库链接名称
     *
     * @return
     */
    String connection() default DataBaseConfig.DATABASE_DEFAULT_CONFIG;

    /**
     * 是否开启事务
     *
     * @return
     */
    boolean transaction() default false;
}
