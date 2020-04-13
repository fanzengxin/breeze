package org.breeze.admin.service;

import org.breeze.admin.dao.DeptDao;
import org.breeze.core.annotation.common.AutoAdd;
import org.breeze.core.annotation.service.Service;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 部门服务层
 * @auther: 黑面阿呆
 * @date: 2020-04-04 20:23
 * @version: 1.0.0
 */
@Service
public class DeptService {

    @AutoAdd
    private DeptDao deptDao;

    /**
     * 根据用户id获取部门信息
     *
     * @param userId
     * @param serial
     * @return
     */
    public List<String> getDeptByUser(String userId, Serial serial) {
        DataList dl = deptDao.getDeptsByUser(userId, serial);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < dl.size(); i++) {
            list.add(dl.getData(i).getString("dept_id"));
        }
        return list;
    }
}
