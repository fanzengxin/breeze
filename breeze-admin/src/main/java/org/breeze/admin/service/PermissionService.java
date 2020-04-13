package org.breeze.admin.service;

import org.breeze.admin.dao.PermissionDao;
import org.breeze.core.annotation.common.AutoAdd;
import org.breeze.core.annotation.service.Service;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 权限服务层
 * @auther: 黑面阿呆
 * @date: 2020-04-04 20:46
 * @version: 1.0.0
 */
@Service
public class PermissionService {

    @AutoAdd
    private PermissionDao permissionDao;

    /**
     * 根据用户id获取部门信息
     *
     * @param userId
     * @param serial
     * @return
     */
    public List<String> getPermissionsByUser(String userId, Serial serial) {
        DataList dl = permissionDao.getPermissionsByUser(userId, serial);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < dl.size(); i++) {
            list.add(dl.getData(i).getString("permission"));
        }
        return list;
    }
}
