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
@Controller(mapper = "user")
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
    @Permission(value = "sys_user_page")
    @Api(method = RequestMethod.GET)
    public R page(int page, int pageSize, Serial serial) {
        DataList dataList = userService.getPage(page, pageSize, serial);
        return R.success(dataList);
    }

    /**
     * 新增用户信息
     *
     * @param data
     * @param serial
     * @return
     */
    @Params(
            @Param(name="data", format = ParamFormatCheck.Data, required = true)
    )
    @Permission(value = "sys_user_create")
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
            @Param(name="data", format = ParamFormatCheck.Data, required = true)
    )
    @Permission(value = "sys_user_update")
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
            @Param(name="id", required = true)
    )
    @Permission(value = "sys_user_delete")
    @Api(method = RequestMethod.DELETE)
    public R delete(String id, Serial serial) {
        int count = userService.remove(id, serial);
        return R.success(count);
    }
}
