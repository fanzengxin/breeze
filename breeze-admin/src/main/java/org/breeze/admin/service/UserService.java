package org.breeze.admin.service;

import com.alibaba.fastjson.JSONArray;
import org.breeze.admin.dao.UserDao;
import org.breeze.admin.dao.UserDeptDao;
import org.breeze.admin.dao.UserRoleDao;
import org.breeze.core.annotation.common.AutoAdd;
import org.breeze.core.annotation.service.DataBase;
import org.breeze.core.annotation.service.Service;
import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.bean.login.LoginInfo;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;
import org.breeze.core.utils.string.UtilString;

import java.util.List;

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

    @AutoAdd
    private UserRoleDao userRoleDao;

    @AutoAdd
    private UserDeptDao userDeptDao;

    @AutoAdd
    private RoleService roleService;

    @AutoAdd
    private DeptService deptService;

    /**
     * 分页查询用户列表
     *
     * @param page
     * @param pageSize
     * @param userId       用户名
     * @param username     姓名
     * @param deptId       部门id
     * @param childrenDept 是否包含子部门
     * @param serial
     * @return
     */
    public DataList getPage(int page, int pageSize, String userId, String username, String deptId, String childrenDept, Serial serial) {
        return userDao.getPage(page, pageSize, userId, username, deptId, childrenDept, serial);
    }

    /**
     * 根据id获取用户信息
     *
     * @param id
     * @param serial
     * @return
     */
    public Data getUserInfo(String id, Serial serial) {
        Data find = new Data();
        find.add("id", id, true);
        DataList dataList = userDao.find(find, serial);
        if (dataList != null && dataList.size() > 0) {
            Data user = dataList.getData(0);
            // 查询关联角色
            List<String> roles = roleService.getRoleByUser(id, serial);
            user.add("roles", roles);
            // 查询关联部门
            List<String> depts = deptService.getDeptByUser(id, serial);
            user.add("depts", depts);
            user.remove("password");
            user.remove("salt");
            return user;
        } else {
            log.logInfo("用户信息不存在:{}", serial, id);
            return new Data();
        }
    }

    /**
     * 新增用户
     *
     * @param save
     * @param loginInfo
     * @param serial
     * @return
     */
    @DataBase(transaction = true)
    public boolean create(Data save, LoginInfo loginInfo, Serial serial) {
        // 获取用户部门信息
        String deptId = save.getString("depts");
        save.remove("depts");
        // 获取用户角色信息
        JSONArray roles = save.getJsonArray("roles");
        save.remove("roles");
        save.add("create_id", loginInfo.getUid());
        boolean result = userDao.save(save, serial);
        Data find = new Data();
        find.add("user_id", save.getString("user_id"), true);
        DataList dl = userDao.find(find, serial);
        if (dl == null || dl.size() == 0) {
            log.logError("获取保存的用户数据失败：user_id={}", serial, save.getString("user_id"));
            return false;
        }
        String id = dl.getData(0).getString("id");
        if (UtilString.isNotEmpty(deptId)) {
            // 创建新部门关联
            Data userDeptAdd = new Data();
            userDeptAdd.add("dept_id", deptId);
            userDeptAdd.add("user_id", id);
            userDeptDao.save(userDeptAdd, serial);
        }
        if (roles.size() > 0) {
            // 创建新角色关联
            DataList userRoleBatchAdd = new DataList();
            for (int i = 0; i < roles.size(); i++) {
                Data userRoleAdd = new Data();
                userRoleAdd.add("role_code", roles.getString(i));
                userRoleAdd.add("user_id", id);
                userRoleBatchAdd.add(userRoleAdd);
            }
            userRoleDao.batchSave(userRoleBatchAdd, serial);
        }
        return result;
    }

    /**
     * 修改用户
     *
     * @param update
     * @param loginInfo
     * @param serial
     * @return
     */
    @DataBase(transaction = true)
    public boolean update(Data update, LoginInfo loginInfo, Serial serial) {
        // 获取用户部门信息
        String deptId = update.getString("depts");
        // 删除旧部门关联
        Data userDeptRemove = new Data();
        userDeptRemove.add("user_id", update.getString("id"), true);
        userDeptDao.remove(userDeptRemove, serial);
        if (UtilString.isNotEmpty(deptId)) {
            // 创建新部门关联
            Data userDeptAdd = new Data();
            userDeptAdd.add("dept_id", deptId);
            userDeptAdd.add("user_id", update.getString("id"));
            userDeptDao.save(userDeptAdd, serial);
        }
        update.remove("depts");

        // 获取用户角色信息
        JSONArray roles = update.getJsonArray("roles");
        // 删除旧用户角色关联
        Data userRoleRemove = new Data();
        userRoleRemove.add("user_id", update.getString("id"), true);
        userRoleDao.remove(userRoleRemove, serial);
        if (roles.size() > 0) {
            // 创建新角色关联
            DataList userRoleBatchAdd = new DataList();
            for (int i = 0; i < roles.size(); i++) {
                Data userRoleAdd = new Data();
                userRoleAdd.add("role_code", roles.getString(i));
                userRoleAdd.add("user_id", update.getString("id"));
                userRoleBatchAdd.add(userRoleAdd);
            }
            userRoleDao.batchSave(userRoleBatchAdd, serial);
        }
        update.remove("roles");

        // 更新用户信息
        update.setPrimaryKey("id");
        update.add("update_id", loginInfo.getUid());
        return userDao.update(update, serial);
    }

    /**
     * 删除用户
     *
     * @param id
     * @param serial
     * @return
     */
    @DataBase(transaction = true)
    public int remove(String id, Serial serial) {
        // 删除用户部门关联
        Data userDeptRemove = new Data();
        userDeptRemove.add("user_id", id, true);
        userDeptDao.remove(userDeptRemove, serial);
        // 删除用户角色关联
        Data userRoleRemove = new Data();
        userRoleRemove.add("user_id", id, true);
        userRoleDao.remove(userRoleRemove, serial);
        // 删除用户信息
        Data userRemove = new Data();
        userRemove.add("id", id, true);
        return userDao.remove(userRemove, serial);
    }
}
