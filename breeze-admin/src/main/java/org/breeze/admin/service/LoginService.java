package org.breeze.admin.service;

import org.breeze.admin.dao.LoginDao;
import org.breeze.core.annotation.common.AutoAdd;
import org.breeze.core.annotation.service.Service;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;

/**
 * @Description: 用户登录服务
 * @Auther: 黑面阿呆
 * @Date: 2019-12-04 11:23
 * @Version: 1.0.0
 */
@Service
public class LoginService {

    @AutoAdd
    private LoginDao loginDao;

    public DataList getLoginUser(int id, String name, String value, Serial serial) {
        return loginDao.getList(id, name, value, serial);
    }
}
