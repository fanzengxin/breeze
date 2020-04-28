package org.breeze.admin.controller;

import org.breeze.admin.service.UserService;
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
 * @Description: 用户管理
 * @Auther: 黑面阿呆
 * @Date: 2019-12-16 17:21
 * @Version: 1.0.0
 */
@Controller(mapper = "/admin/user")
public class UserController {

    private static Log log = LogFactory.getLog(UserController.class);

    @AutoAdd
    private UserService userService;

    /**
     * 分页查询用户列表
     *
     * @param page
     * @param pageSize
     * @param serial
     * @return
     */
    @Params({
            @Param(name = "userId", description = "用户名"),
            @Param(name = "username", description = "姓名"),
            @Param(name = "deptId", description = "部门id"),
            @Param(name = "childrenDept", description = "是否包含下级部门")
    })
    @Permission(value = "sys_user_list")
    @Api(value = "page", method = RequestMethod.GET)
    public R page(int page, int pageSize, String userId, String username, String deptId, String childrenDept, Serial serial) {
        DataList dataList = userService.getPage(page, pageSize, userId, username, deptId, childrenDept, serial);
        return R.success(dataList);
    }

    /**
     * 分页查询用户列表
     *
     * @param serial
     * @return
     */
    @Params({
            @Param(name = "id", description = "用户id")
    })
    @Permission(value = "sys_user_list")
    @Api(value = "get", method = RequestMethod.GET)
    public R get(String id, Serial serial) {
        Data data = userService.getUserInfo(id, serial);
        return R.success(data);
    }

    /**
     * 获取当前登录用户信息
     *
     * @param loginInfo
     * @param serial
     * @return
     */
    @Api(value = "/info", method = RequestMethod.GET)
    public R userInfo(LoginInfo loginInfo, Serial serial) {
        return R.successStr(loginInfo.toString());
    }

    /**
     * 新增用户信息
     *
     * @param data
     * @param serial
     * @return
     */
    @Params(
            @Param(name = "data", format = ParamFormatCheck.Data, required = true)
    )
    @Permission(value = "sys_user_add")
    @Api(method = RequestMethod.POST)
    public R create(Data data, LoginInfo loginInfo, Serial serial) {
        if (userService.create(data, loginInfo, serial)) {
            return R.success();
        } else {
            return R.failure("用户保存失败");
        }
    }

    /**
     * 修改用户信息
     *
     * @param data
     * @param serial
     * @return
     */
    @Params(
            @Param(name = "data", format = ParamFormatCheck.Data, required = true)
    )
    @Permission(value = "sys_user_edit")
    @Api(method = RequestMethod.PUT)
    public R update(Data data, LoginInfo loginInfo, Serial serial) {
        if (userService.update(data, loginInfo, serial)) {
            return R.success();
        } else {
            return R.failure("用户修改失败");
        }
    }

    /**
     * 删除用户信息
     *
     * @param id
     * @param serial
     * @return
     */
    @Params(
            @Param(name = "id", required = true)
    )
    @Permission(value = "sys_user_del")
    @Api(method = RequestMethod.DELETE)
    public R delete(String id, Serial serial) {
        int count = userService.remove(id, serial);
        return R.success(count);
    }
}
