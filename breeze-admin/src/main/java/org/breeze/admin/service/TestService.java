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
 * @Description: 测试
 * @Author: 系统管理员
 * @Date: 2020-05-09 18:14
 * @Version: 1.0.0
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
	 * @age		年龄
     * @param serial
     * @return
     */
    public DataList getPage(int page, int pageSize, String nama, String gender, String age, Serial serial) {
        return testDao.getPage(page, pageSize, nama, gender, age, serial);
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
