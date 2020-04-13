package org.breeze.admin.dao;

import org.breeze.core.annotation.repository.Repository;
import org.breeze.core.annotation.repository.Select;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;

/**
 * @description: 部门数据层
 * @auther: 黑面阿呆
 * @date: 2020-04-04 20:30
 * @version: 1.0.0
 */
@Repository(tableName = "sys_dept")
public interface DeptDao {

    /**
     * 根据用户id查询部门信息
     *
     * @param userId
     * @param serial
     * @return
     */
    @Select(sql = "select dept_id from breeze.sys_user_dept where user_id = #:userId:#")
    DataList getDeptsByUser(String userId, Serial serial);
}
