package org.breeze.admin.dao;

import org.breeze.core.annotation.repository.Repository;
import org.breeze.core.annotation.repository.Select;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.constant.OperationMethod;
import org.breeze.core.service.dao.BaseDao;

/**
 * @description: 角色数据层
 * @auther: 黑面阿呆
 * @date: 2020-04-04 20:32
 * @version: 1.0.0
 */
@Repository(tableName = "breeze.sys_role")
public interface RoleDao extends BaseDao {

    /**
     * 查询角色列表
     *
     * @param id
     * @param name
     * @param type
     * @param serial
     * @return
     */
    @Select(sql = "select * from breeze.sys_role where 1=1{ and id = #:id:#}{ and role_name like #:%name%:#}" +
            "{ and role_type = #:type:#}", type = OperationMethod.FIND_PAGE)
    DataList list(int page, int pageSize, Long id, String name, String type, Serial serial);

    /**
     * 根据用户id查询角色信息
     *
     * @param userId
     * @param serial
     * @return
     */
    @Select(sql = "select role_code from breeze.sys_user_role where user_id = #:userId:#")
    DataList getRolesByUser(String userId, Serial serial);
}
