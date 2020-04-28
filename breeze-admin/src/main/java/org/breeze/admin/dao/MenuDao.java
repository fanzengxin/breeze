package org.breeze.admin.dao;

import org.breeze.core.annotation.repository.Repository;
import org.breeze.core.annotation.repository.Select;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.service.dao.BaseDao;

import java.util.List;

/**
 * @description: 菜单数据层
 * @auther: 黑面阿呆
 * @date: 2020-04-05 12:00
 * @version: 1.0.0
 */
@Repository(tableName = "breeze.sys_menu")
public interface MenuDao extends BaseDao {

    /**
     * 获取当前登录用户的菜单
     *
     * @param permissions
     * @param children
     * @param serial
     * @return
     */
    @Select(sql = "select id, menu_name, menu_type, menu_icon, menu_permission, menu_url, menu_open_way, keep_alive, parent_menu_id, sort_no" +
            " from sys_menu where {?:children != true:? menu_type < 3 and} (menu_permission in ({for:permissions:for}) or menu_permission = '') order by sort_no")
    DataList getMenuTree(List<String> permissions, String children, Serial serial);

    /**
     * 查询菜单信息
     *
     * @param parentId
     * @param serial
     * @return
     */
    @Select(sql = "select id, menu_name, menu_type, menu_icon, menu_permission, menu_url, menu_open_way, keep_alive, parent_menu_id, sort_no" +
            " from breeze.sys_menu where 1=1{ and parent_menu_id=#:parentId:#} order by sort_no")
    DataList getMenuList(Long parentId, Serial serial);

    /**
     * 查看角色菜单关联信息
     *
     * @param roleCode
     * @param serial
     * @return
     */
    @Select(sql = "select sm.id, sm.menu_name, sm.menu_type, sm.menu_permission, sm.parent_menu_id, srp.permission " +
            "from breeze.sys_menu sm left join breeze.sys_role_permission srp on srp.permission=sm.menu_permission and srp.role_code=#:roleCode:# " +
            "order by sm.sort_no")
    DataList getRoleMenu(String roleCode, Serial serial);
}
