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
@Repository(tableName = "sys_user")
public interface UserDao extends BaseDao {

    /**
     * 分页查询用户列表
     *
     * @param page
     * @param pageSize
     * @param serial
     * @return
     */
    @Select(sql = "select * from breeze.sys_user where 1=1", type = OperationMethod.FIND_PAGE)
    DataList getPage(int page, int pageSize, Serial serial);
}
