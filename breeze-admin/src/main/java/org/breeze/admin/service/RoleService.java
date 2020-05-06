package org.breeze.admin.service;

import org.breeze.admin.dao.RoleDao;
import org.breeze.admin.dao.RolePermissionDao;
import org.breeze.core.annotation.common.AutoAdd;
import org.breeze.core.annotation.service.DataBase;
import org.breeze.core.annotation.service.Service;
import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.bean.login.LoginInfo;
import org.breeze.core.utils.string.UtilString;

import java.util.*;

/**
 * @description: 角色服务层
 * @auther: 黑面阿呆
 * @date: 2020-04-04 20:23
 * @version: 1.0.0
 */
@Service
public class RoleService {

    @AutoAdd
    private RoleDao roleDao;

    @AutoAdd
    private RolePermissionDao rolePermissionDao;

    /**
     * 查询角色信息列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @param id       角色id
     * @param name     角色名称
     * @param type     角色类型
     * @param serial   日志序列
     * @return
     */
    public DataList list(int page, int pageSize, Long id, String name, String type, Serial serial) {
        return roleDao.list(page, pageSize, id, name, type, serial);
    }

    /**
     * 根据用户id获取部门信息
     *
     * @param userId
     * @param serial
     * @return
     */
    public List<String> getRoleByUser(String userId, Serial serial) {
        DataList dl = roleDao.getRolesByUser(userId, serial);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < dl.size(); i++) {
            list.add(dl.getData(i).getString("role_code"));
        }
        return list;
    }

    /**
     * 保存菜单数据
     *
     * @param loginInfo
     * @param data
     * @param serial
     * @return
     */
    public boolean create(LoginInfo loginInfo, Data data, Serial serial) {
        data.add("create_id", loginInfo.getUid());
        return roleDao.save(data, serial);
    }

    /**
     * 更新角色数据
     *
     * @param loginInfo
     * @param data
     * @param serial
     * @return
     */
    public boolean update(LoginInfo loginInfo, Data data, Serial serial) {
        data.setPrimaryKey("ID");
        data.add("update_id", loginInfo.getUid());
        return roleDao.update(data, serial);
    }

    /**
     * 删除角色及关联数据
     *
     * @param id
     * @param serial
     * @return
     */
    @DataBase(transaction = true)
    public int delete(String id, Serial serial) {
        Data remove = new Data();
        remove.add("id", id, true);
        DataList dataList = roleDao.find(remove, serial);
        if (dataList.size() == 1) {
            Data rolePermissionRemove = new Data();
            rolePermissionRemove.add("ROLE_CODE", dataList.getData(0).getString("ROLE_CODE"), true);
            rolePermissionDao.remove(rolePermissionRemove, serial);
            return roleDao.remove(remove, serial);
        }
        return 0;
    }

    /**
     * 保存角色权限
     *
     * @param roleCode
     * @param permissions
     * @param serial
     * @return
     */
    @DataBase(transaction = true)
    public int savePermission(String roleCode, String permissions, Serial serial) {
        Data find = new Data();
        find.add("role_code", roleCode, true);
        rolePermissionDao.remove(find, serial);
        Set<String> permissionSet = new HashSet<>(Arrays.asList(permissions.split(",")));
        DataList btSave = new DataList();
        for (String permission : permissionSet) {
            if (UtilString.isNullOrEmpty(permission)) {
                continue;
            }
            Data data = new Data();
            data.add("role_code", roleCode);
            data.add("permission", permission);
            btSave.add(data);
        }
        if (btSave.size() > 0) {
            return rolePermissionDao.batchSave(btSave, serial);
        } else {
            return 0;
        }
    }
}
