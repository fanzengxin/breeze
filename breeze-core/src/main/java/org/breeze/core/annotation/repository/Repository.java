package org.breeze.core.annotation.repository;

import org.breeze.core.config.DataBaseConfig;

import java.lang.annotation.*;

/**
 * @Description:
 * @Auther: 黑面阿呆
 * @Date: 2019/2/18 21:52
 * @Version: 1.0.0
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Repository {

    /**
     * 数据表名称
     *
     * @return
     */
    String tableName() default "";

    /**
     * 数据库链接名称
     *
     * @return
     */
    String connection() default DataBaseConfig.DATABASE_DEFAULT_CONFIG;

    /**
     * 自动添加创建时间
     *
     * @return
     */
    boolean createTime() default true;

    /**
     * 自动添加修改时间
     *
     * @return
     */
    boolean updateTime() default true;
}
