package org.breeze.admin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.breeze.admin.dao.MenuDao;
import org.breeze.admin.dao.RolePermissionDao;
import org.breeze.admin.util.TreeUtils;
import org.breeze.core.annotation.common.AutoAdd;
import org.breeze.core.annotation.service.DataBase;
import org.breeze.core.annotation.service.Service;
import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.bean.login.LoginInfo;
import org.breeze.core.utils.string.UtilString;

/**
 * @description: 菜单服务层
 * @auther: 黑面阿呆
 * @date: 2020-04-04 20:23
 * @version: 1.0.0
 */
@Service
public class MenuService {

    @AutoAdd
    private MenuDao menuDao;

    @AutoAdd
    private RolePermissionDao rolePermissionDao;

    /**
     * 获取当前登录用户的菜单
     *
     * @param loginInfo
     * @param serial
     * @return
     */
    public JSONArray getMenuTree(LoginInfo loginInfo, String children, Serial serial) {
        DataList dataList = menuDao.getMenuTree(loginInfo.getPermissions(), children, serial);
        JSONArray ja = new JSONArray();
        for (int i = 0; i < dataList.size(); i++) {
            Data d = dataList.getData(i);
            JSONObject json = new JSONObject();
            json.put("id", d.getLong("ID"));
            json.put("parentId", d.getLong("PARENT_MENU_ID"));
            json.put("label", d.getString("MENU_NAME"));
            json.put("path", d.getString("MENU_URL"));
            json.put("permission", d.getString("MENU_PERMISSION"));
            json.put("keepAlive", d.getString("KEEP_ALIVE"));
            if (d.getString("MENU_ICON").length() > 9) {
                json.put("icon", d.getString("MENU_ICON").substring(9));
            } else {
                json.put("icon", d.getString("MENU_ICON"));
            }
            json.put("children", new JSONArray());
            ja.add(json);
        }
        return TreeUtils.buildTree(ja, "0");
    }

    /**
     * 查询菜单信息
     *
     * @param lazy
     * @param parentId
     * @param serial
     * @return
     */
    public DataList getMenu(boolean lazy, Long parentId, Serial serial) {
        if (!lazy) {
            parentId = null;
        }
        return menuDao.getMenuList(parentId, serial);
    }

    public DataList getRoleMenu(String roleCode, Serial serial) {
        return menuDao.getRoleMenu(roleCode, serial);
    }

    /**
     * 根据条件查询
     *
     * @param id
     * @param serial
     * @return
     */
    public DataList get(String id, Serial serial) {
        Data find = new Data();
        find.add("id", id);
        return menuDao.find(find, serial);
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
        return menuDao.save(data, serial);
    }

    /**
     * 更新菜单数据
     *
     * @param loginInfo
     * @param data
     * @param serial
     * @return
     */
    public boolean update(LoginInfo loginInfo, Data data, Serial serial) {
        data.setPrimaryKey("ID");
        data.add("update_id", loginInfo.getUid());
        return menuDao.update(data, serial);
    }

    /**
     * 删除菜单数据
     *
     * @param id
     * @param serial
     * @return
     */
    @DataBase(transaction = true)
    public int delete(String id, Serial serial) {
        Data remove = new Data();
        remove.add("id", id, true);
        DataList dataList = menuDao.find(remove, serial);
        if (dataList.size() == 1) {
            if (UtilString.isNotEmpty(dataList.getData(0).getString("MENU_PERMISSION"))) {
                Data rolePermissionRemove = new Data();
                rolePermissionRemove.add("PERMISSION", dataList.getData(0).getString("MENU_PERMISSION"), true);
                rolePermissionDao.remove(rolePermissionRemove, serial);
            }
            return menuDao.remove(remove, serial);
        }
        return 0;
    }
}
