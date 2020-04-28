package org.breeze.admin.dao;

import org.breeze.core.annotation.repository.Repository;
import org.breeze.core.annotation.repository.Select;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.constant.OperationMethod;
import org.breeze.core.service.dao.BaseDao;

/**
 * @Description: 用户管理
 * @Auther: 黑面阿呆
 * @Date: 2019-12-17 17:15
 * @Version: 1.0.0
 */
@Repository(tableName = "breeze.sys_user")
public interface UserDao extends BaseDao {

    /**
     * 分页查询用户列表
     *
     * @param page
     * @param pageSize
     * @param userId
     * @param username
     * @param deptId
     * @param serial
     * @return
     */
    @Select(sql = "select su.id, su.user_id, su.user_name, su.gender, su.phone, su.email, su.status, su.create_time from breeze.sys_user su " +
            "left join breeze.sys_user_dept sud on su.id=sud.user_id " +
            "left join breeze.sys_dept sd on sd.id=sud.dept_id " +
            "where 1=1 {?:deptId != null:? and (sd.id=#:deptId:# or sd.parent_ids like #:%childrenDept%:#)}" +
            "{ and su.user_id = #:%userId%:#}{ and su.user_name like #:%username%:#} order by su.create_time desc",
            type = OperationMethod.FIND_PAGE)
    DataList getPage(int page, int pageSize, String userId, String username, String deptId, String childrenDept, Serial serial);
}
