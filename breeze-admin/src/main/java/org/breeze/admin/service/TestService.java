package org.breeze.admin.service;

import org.breeze.admin.dao.TestDao;
import org.breeze.core.annotation.common.AutoAdd;
import org.breeze.core.annotation.service.Service;
import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.bean.login.LoginInfo;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;

/**
 * @description: 测试
 * @author: 系统管理员
 * @date: 2020-05-11 17:14
 * @version: 1.0.0
 */
@Service
public class TestService {

    private static final Log log = LogFactory.getLog(TestService.class);

    @AutoAdd
    private TestDao testDao;

    /**
     * 分页查询测试列表
     *
     * @param page
     * @param pageSize 
	 * @nama		姓名
	 * @gender		性别
     * @param serial
     * @return
     */
    public DataList getPage(int page, int pageSize, String nama, String gender, Serial serial) {
        return testDao.getPage(page, pageSize, nama, gender, serial);
    }

    /**
     * 获取单条测试信息
     *
     * @param id
     * @param serial
     * @return
     */
    public DataList get(String id, Serial serial) {
        Data find = new Data();
        find.add("id", id, true);
        return testDao.find(find, serial);
    }

    /**
     * 新增测试
     *
     * @param create
     * @param loginInfo
     * @param serial
     * @return
     */
    public boolean create(Data create, LoginInfo loginInfo, Serial serial) {
        create.add("create_id", loginInfo.getUid());
        return testDao.save(create, serial);
    }

    /**
     * 修改测试
     *
     * @param update
     * @param loginInfo
     * @param serial
     * @return
     */
    public boolean update(Data update, LoginInfo loginInfo, Serial serial) {
        update.setPrimaryKey("id");
        update.add("update_id", loginInfo.getUid());
        return testDao.update(update, serial);
    }

    /**
     * 删除测试
     *
     * @param id
     * @param serial
     * @return
     */
    public int remove(String id, Serial serial) {
        Data remove = new Data();
        remove.add("id", id, true);
        return testDao.remove(remove, serial);
    }
}
