package org.breeze.admin.controller;

import org.breeze.admin.service.UserService;
import org.breeze.core.annotation.common.AutoAdd;
import org.breeze.core.annotation.controller.*;
import org.breeze.core.bean.api.R;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
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
     * @param param1
     * @param param2
     * @param serial
     * @return
     */
    @Params({
            @Param(name = "param1", description = "必填参数1", required = true),
            @Param(name = "param2", description = "选填参数2")
    })
    @Permission(value = "sys_user_page", login = false)
    @Api(value = "page", method = RequestMethod.GET)
    public R page(int page, int pageSize, String param1, String param2, Serial serial) {
        DataList dataList = userService.getPage(page, pageSize, param1, param2, serial);
        return R.success(dataList);
    }

    @Permission(value = "sys_user_create", login = false)
    @Api(value = "page", method = RequestMethod.GET)
    public R create() {
        return R.success();
    }

    @Permission(value = "sys_user_update", login = false)
    @Api(value = "page", method = RequestMethod.GET)
    public R update() {
        return R.success();
    }

    @Permission(value = "sys_user_delete", login = false)
    @Api(value = "page", method = RequestMethod.DELETE)
    public R delete() {
        return R.success();
    }
}
