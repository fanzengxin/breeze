package org.breeze.admin.controller;

import org.breeze.admin.service.DeptService;
import org.breeze.core.annotation.common.AutoAdd;
import org.breeze.core.annotation.controller.*;
import org.breeze.core.bean.api.R;
import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.bean.login.LoginInfo;
import org.breeze.core.constant.ParamFormatCheck;
import org.breeze.core.constant.RequestMethod;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;

/**
 * @description: 部门信息管理
 * @auther: 黑面阿呆
 * @date: 2020-04-18 12:04
 * @version: 1.0.0
 */
@Controller(mapper = "/dept")
public class DeptController {

    private static Log log = LogFactory.getLog(DeptController.class);

    @AutoAdd
    private DeptService deptService;

    /**
     * 查看部门树
     *
     * @param serial 日志序列
     * @return
     */
    @Permission("sys_dept_tree")
    @Api(value = "tree", method = RequestMethod.GET)
    public R tree(Serial serial) {
        return R.success(deptService.getDeptTree(serial));
    }

    /**
     * 查看部门树
     *
     * @param serial 日志序列
     * @return
     */
    @Params({
            @Param(name = "id", description = "部门id", format = ParamFormatCheck.Long, required = true)
    })
    @Api(method = RequestMethod.GET)
    public R list(Long id, Serial serial) {
        return R.success(deptService.getDeptList(id, serial));
    }

    /**
     * 新增部门信息
     *
     * @param loginInfo 登录用户
     * @param data      菜单信息
     * @param serial    日志序列
     * @return
     */
    @Permission("sys_dept_add")
    @Api(method = RequestMethod.POST)
    public R create(LoginInfo loginInfo, Data data, Serial serial) {
        if (deptService.create(loginInfo, data, serial)) {
            return R.success();
        } else {
            return R.failure("数据保存失败");
        }
    }

    /**
     * 更新部门信息
     *
     * @param loginInfo 登录用户
     * @param data      菜单数据
     * @param serial    日志序列
     * @return
     */
    @Permission("sys_dept_edit")
    @Api(method = RequestMethod.PUT)
    public R update(LoginInfo loginInfo, Data data, Serial serial) {
        if (deptService.update(loginInfo, data, serial)) {
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
    @Permission("sys_dept_del")
    @Api(method = RequestMethod.DELETE)
    public R delete(String id, Serial serial) {
        if (deptService.delete(id, serial) > 0) {
            return R.success();
        } else {
            return R.failure("数据删除失败");
        }
    }
}
