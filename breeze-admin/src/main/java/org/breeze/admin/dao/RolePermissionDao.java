package org.breeze.admin.dao;

import org.breeze.core.annotation.repository.Repository;
import org.breeze.core.service.dao.BaseDao;

/**
 * @description: 角色权限数据
 * @auther: 黑面阿呆
 * @date: 2020-04-15 18:26
 * @version: 1.0.0
 */
@Repository(tableName = "sys_role_permission", createTime = false)
public interface RolePermissionDao extends BaseDao {
}
