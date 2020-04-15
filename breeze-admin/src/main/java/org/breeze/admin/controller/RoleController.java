package org.breeze.admin.controller;

import org.breeze.admin.service.RoleService;
import org.breeze.core.annotation.common.AutoAdd;
import org.breeze.core.annotation.controller.Api;
import org.breeze.core.annotation.controller.Controller;
import org.breeze.core.annotation.controller.Permission;
import org.breeze.core.bean.api.R;
import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.bean.login.LoginInfo;
import org.breeze.core.constant.RequestMethod;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;

/**
 * @description: 角色管理
 * @auther: 黑面阿呆
 * @date: 2020-04-12 10:47
 * @version: 1.0.0
 */
@Controller(mapper = "/admin/role")
public class RoleController {

    private static Log log = LogFactory.getLog(RoleController.class);

    @AutoAdd
    private RoleService roleService;

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
    @Permission("sys_role_list")
    @Api(value = "page", method = RequestMethod.GET)
    public R page(int page, int pageSize, Long id, String name, String type, Serial serial) {
        DataList dataList = roleService.list(page, pageSize, id, name, type, serial);
        return R.success(dataList);
    }

    /**
     * 新增角色信息
     *
     * @param loginInfo 登录用户
     * @param data      角色信息
     * @param serial    日志序列
     * @return
     */
    @Permission("sys_role_add")
    @Api(method = RequestMethod.POST)
    public R create(LoginInfo loginInfo, Data data, Serial serial) {
        if (roleService.create(loginInfo, data, serial)) {
            return R.success();
        } else {
            return R.failure("数据保存失败");
        }
    }

    /**
     * 更新角色信息
     *
     * @param loginInfo 登录用户
     * @param data      角色数据
     * @param serial    日志序列
     * @return
     */
    @Permission("sys_role_edit")
    @Api(method = RequestMethod.PUT)
    public R update(LoginInfo loginInfo, Data data, Serial serial) {
        if (roleService.update(loginInfo, data, serial)) {
            return R.success();
        } else {
            return R.failure("数据修改失败");
        }
    }

    /**
     * 删除角色信息
     *
     * @param id     角色数据
     * @param serial 日志序列
     * @return
     */
    @Permission("sys_role_del")
    @Api(method = RequestMethod.DELETE)
    public R delete(String id, Serial serial) {
        if (roleService.delete(id, serial) > 0) {
            return R.success();
        } else {
            return R.failure("数据修改失败");
        }
    }

    /**
     * 删除角色信息
     *
     * @param roleCode    角色编码
     * @param permissions 权限集合
     * @param serial      日志序列
     * @return
     */
    @Permission("sys_role_permission")
    @Api(value = "permissions", method = RequestMethod.POST)
    public R permissions(String roleCode, String permissions, Serial serial) {
        int result = roleService.savePermission(roleCode, permissions, serial);
        return R.success(result);
    }
}
