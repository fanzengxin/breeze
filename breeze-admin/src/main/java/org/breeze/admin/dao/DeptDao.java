package org.breeze.admin.dao;

import org.breeze.core.annotation.repository.Repository;
import org.breeze.core.annotation.repository.Select;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.service.dao.BaseDao;

/**
 * @description: 部门数据层
 * @auther: 黑面阿呆
 * @date: 2020-04-04 20:30
 * @version: 1.0.0
 */
@Repository(tableName = "breeze.sys_dept")
public interface DeptDao extends BaseDao {

    /**
     * 查询部门信息
     *
     * @param serial
     * @return
     */
    @Select(sql = "select * from breeze.sys_dept where 1=1{ and id=#:id:#} order by sort_no")
    DataList getDeptList(Long id, Serial serial);

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
