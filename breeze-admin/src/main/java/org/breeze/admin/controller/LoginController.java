package org.breeze.admin.controller;

import org.breeze.admin.service.LoginService;
import org.breeze.core.annotation.common.AutoAdd;
import org.breeze.core.annotation.controller.*;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.constant.ParamFormatCheck;
import org.breeze.core.constant.RequestMethod;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;

/**
 * @Description: 用户登录
 * @Auther: 黑面阿呆
 * @Date: 2019-11-29 13:39
 * @Version: 1.0.0
 */
@Controller(mapper = "sys")
public class LoginController {

    private static Log log = LogFactory.getLog(LoginController.class);

    @AutoAdd
    private LoginService loginService;

    /**
     * 用户登录
     *
     * @param id
     * @param name
     * @param value
     * @return
     */
    @Permission(login = false)
    @Api(value = "login", method = RequestMethod.POST)
    public String login(int id, String name, String value, Serial serial) {
        System.out.println(loginService.getLoginUser(id, name, value, serial));
        return "用户登录";
    }

    @Api(value = "aaa", method = RequestMethod.POST)
    public String test() {
        return "null";
    }
}
