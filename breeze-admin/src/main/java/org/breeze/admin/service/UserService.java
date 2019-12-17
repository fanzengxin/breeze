package org.breeze.admin.service;

import org.breeze.admin.dao.LoginDao;
import org.breeze.admin.dao.UserDao;
import org.breeze.core.annotation.common.AutoAdd;
import org.breeze.core.annotation.service.Service;
import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;

/**
 * @Description: 用户管理
 * @Auther: 黑面阿呆
 * @Date: 2019-12-17 17:14
 * @Version: 1.0.0
 */
@Service
public class UserService {

    private static final Log log = LogFactory.getLog(UserService.class);

    @AutoAdd
    private UserDao userDao;

    /**
     * 分页查询用户列表
     *
     * @param page
     * @param pageSize
     * @param param1
     * @param param2
     * @param serial
     * @return
     */
    public DataList getPage(int page, int pageSize, String param1, String param2, Serial serial) {
        return userDao.getPage(page, pageSize, param1, param2, serial);
    }

    public boolean create(Data data) {
        return userDao.save(data);
    }

    public boolean update(Data data) {
        return userDao.update(data);
    }

    public int remove(Data data) {
        return userDao.remove(data);
    }
}
