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
@Repository(tableName = "sys_menu")
public interface MenuDao extends BaseDao {

    /**
     * 获取当前登录用户的菜单
     *
     * @param permissions
     * @param children
     * @param serial
     * @return
     */
    @Select(sql = "select id, menu_name, menu_type, menu_icon, menu_permission, menu_url, menu_open_way, parent_menu_id, sort_no" +
            " from sys_menu where {?:children != true:? menu_type < 3 and} (menu_permission in ({for:permissions:for}) or menu_permission = '') order by sort_no")
    DataList getMenuTree(List<String> permissions, String children, Serial serial);

    /**
     * 查询菜单信息
     *
     * @param parentId
     * @param serial
     * @return
     */
    @Select(sql = "select id, menu_name, menu_type, menu_icon, menu_permission, menu_url, menu_open_way, parent_menu_id, sort_no" +
            " from sys_menu where 1=1{ and parent_menu_id=#:parentId:#} order by sort_no")
    DataList getMenuList(Long parentId, Serial serial);
}
