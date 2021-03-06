package org.breeze.admin.controller;

import com.alibaba.fastjson.JSONArray;
import org.breeze.admin.service.MenuService;
import org.breeze.core.annotation.common.AutoAdd;
import org.breeze.core.annotation.controller.*;
import org.breeze.core.bean.api.R;
import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.bean.login.LoginInfo;
import org.breeze.core.constant.RequestMethod;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;

/**
 * @Description: 菜单管理
 * @Auther: 黑面阿呆
 * @Date: 2019-12-17 14:42
 * @Version: 1.0.0
 */
@Controller(mapper = "/menu")
public class MenuController {

    private static Log log = LogFactory.getLog(MenuController.class);

    @AutoAdd
    private MenuService menuService;

    /**
     * 分页查询用户列表
     *
     * @param loginInfo
     * @param serial
     * @return
     */
    @Api(value = "tree", method = RequestMethod.GET)
    public R menuTree(LoginInfo loginInfo, String children, Serial serial) {
        JSONArray result = menuService.getMenuTree(loginInfo, children, serial);
        return R.success(result);
    }

    /**
     * 查询菜单信息
     *
     * @param lazy     是否懒加载
     * @param parentId 父级菜单id
     * @param serial   日志序列
     * @return
     */
    @Permission("sys_menu_list")
    @Api(method = RequestMethod.GET)
    public R getMenu(boolean lazy, Long parentId, Serial serial) {
        DataList dataList = menuService.getMenu(lazy, parentId, serial);
        return R.success(dataList);
    }

    /**
     * 新增菜单信息
     *
     * @param loginInfo 登录用户
     * @param data      菜单信息
     * @param serial    日志序列
     * @return
     */
    @Permission("sys_menu_add")
    @Api(method = RequestMethod.POST)
    public R create(LoginInfo loginInfo, Data data, Serial serial) {
        if (menuService.create(loginInfo, data, serial)) {
            return R.success();
        } else {
            return R.failure("数据保存失败");
        }
    }

    /**
     * 更新菜单信息
     *
     * @param loginInfo 登录用户
     * @param data      菜单数据
     * @param serial    日志序列
     * @return
     */
    @Permission("sys_menu_edit")
    @Api(method = RequestMethod.PUT)
    public R update(LoginInfo loginInfo, Data data, Serial serial) {
        if (menuService.update(loginInfo, data, serial)) {
            return R.success();
        } else {
            return R.failure("数据修改失败");
        }
    }

    /**
     * 删除菜单信息
     *
     * @param id     菜单数据
     * @param serial 日志序列
     * @return
     */
    @Params({
            @Param(name = "id", description = "菜单id", required = true)
    })
    @Permission("sys_menu_del")
    @Api(method = RequestMethod.DELETE)
    public R delete(String id, Serial serial) {
        if (menuService.delete(id, serial) > 0) {
            return R.success();
        } else {
            return R.failure("数据修改失败");
        }
    }

    /**
     * 分页查询用户列表
     *
     * @param roleCode
     * @param serial
     * @return
     */
    @Params({
            @Param(name = "roleCode", description = "角色编码", required = true)
    })
    @Permission("sys_role_permission")
    @Api(value = "/role", method = RequestMethod.GET)
    public R roleMenu(String roleCode, Serial serial) {
        DataList result = menuService.getRoleMenu(roleCode, serial);
        return R.success(result);
    }
}
