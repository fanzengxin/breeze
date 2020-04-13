package org.breeze.admin.service;

import org.breeze.admin.dao.UserDao;
import org.breeze.core.annotation.common.AutoAdd;
import org.breeze.core.annotation.service.Service;
import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.bean.login.LoginInfo;
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
     * @param serial
     * @return
     */
    public DataList getPage(int page, int pageSize, Serial serial) {
        return userDao.getPage(page, pageSize, serial);
    }

    /**
     * 新增用户
     *
     * @param data
     * @param loginInfo
     * @param serial
     * @return
     */
    public boolean create(Data data, LoginInfo loginInfo, Serial serial) {
        data.add("create_id", loginInfo.getUid());
        return userDao.save(data);
    }

    /**
     * 修改用户
     *
     * @param update
     * @param loginInfo
     * @param serial
     * @return
     */
    public boolean update(Data update, LoginInfo loginInfo, Serial serial) {
        update.setPrimaryKey("id");
        update.add("update_id", loginInfo.getUid());
        return userDao.update(update);
    }

    /**
     * 删除用户
     *
     * @param id
     * @param serial
     * @return
     */
    public int remove(String id, Serial serial) {
        Data remove = new Data();
        remove.add("id", id, true);
        return userDao.remove(remove);
    }
}
