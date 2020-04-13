package org.breeze.admin.dao;

import org.breeze.core.annotation.repository.Repository;
import org.breeze.core.annotation.repository.Select;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;

/**
 * @description: 权限数据层
 * @auther: 黑面阿呆
 * @date: 2020-04-04 20:42
 * @version: 1.0.0
 */
@Repository
public interface PermissionDao {

    /**
     * 根据用户id查询权限信息
     *
     * @param userId
     * @param serial
     * @return
     */
    @Select(sql = "select srp.permission from breeze.sys_role_permission srp left join breeze.sys_user_role sur" +
            " on sur.role_code = srp.role_code where sur.user_id = #:userId:#")
    DataList getPermissionsByUser(String userId, Serial serial);
}
