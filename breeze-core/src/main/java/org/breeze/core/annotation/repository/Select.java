package org.breeze.core.annotation.repository;

import org.breeze.core.constant.OperationMethod;

import java.lang.annotation.*;

/**
 * @Description: 数据库查询Query注解
 * @Auther: 黑面阿呆
 * @Date: 2019/2/18 21:54
 * @Version: 1.0.0
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Select {

    /**
     * 获取接口API前缀
     *
     * @return
     */
    String sql() default "";

    /**
     * 数据库链接名称
     *
     * @return
     */
    String connection() default "";

    /**
     * 数据库操作方式
     */
    int type() default OperationMethod.FIND;

    /**
     * 是否开启缓存
     *
     * @return
     */
    String cache() default "";

    /**
     * 操作数据表名
     *
     * @return
     */
    String tableName() default "";
}
