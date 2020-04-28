package org.breeze.auth.dao;

import org.breeze.core.annotation.repository.Repository;
import org.breeze.core.annotation.repository.Select;
import org.breeze.core.bean.data.Data;
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

    @Select(sql = "select * from breeze.sys_user where user_id = #:username:#")
    Data getUserInfo(String username, Serial serial);
}
