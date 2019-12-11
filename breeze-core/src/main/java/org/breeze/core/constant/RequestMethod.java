package org.breeze.core.constant;

/**
 * @Description: http请求方法枚举类
 * @Auther: 黑面阿呆
 * @Date: 2019-11-27 13:47
 * @Version: 1.0.0
 */
public interface RequestMethod {

    /**
     * get请求，常用于查询数据
     */
    String GET = "GET";
    /**
     * post请求，常用于数据新增
     */
    String POST = "POST";
    /**
     * put请求，常用于数据修改
     */
    String PUT = "PUT";
    /**
     * delete请求，常用于数据删除
     */
    String DELETE = "DELETE";
}
