package org.breeze.admin.dao;

import org.breeze.core.annotation.repository.Repository;
import org.breeze.core.service.dao.BaseDao;

/**
 * @description: 用户角色关联
 * @auther: 黑面阿呆
 * @date: 2020-04-28 10:43
 * @version: 1.0.0
 */
@Repository(tableName = "sys_user_role", createTime = false, updateTime = false)
public interface UserRoleDao extends BaseDao {
}
