package org.breeze.admin.dao;

import org.breeze.core.annotation.repository.Repository;
import org.breeze.core.annotation.repository.Select;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.service.dao.BaseDao;

/**
 * @Description: 登录测试类
 * @Auther: 黑面阿呆
 * @Date: 2019-12-10 13:49
 * @Version: 1.0.0
 */
@Repository
public interface LoginDao extends BaseDao {

    @Select(sql = "select * from breeze.sys_test where 1=1{:?(id>2 || name='2') && value<4, and id=:#id}{and name=:#name}{and value=:$value}")
    DataList getList(int id, String name, String value, Serial serial);
}
