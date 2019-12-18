package org.breeze.admin.controller;

import com.alibaba.fastjson.JSONObject;
import org.breeze.admin.service.LoginService;
import org.breeze.core.annotation.common.AutoAdd;
import org.breeze.core.annotation.controller.*;
import org.breeze.core.bean.api.R;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.bean.login.LoginInfo;
import org.breeze.core.bean.login.LoginSession;
import org.breeze.core.bean.redis.Redis;
import org.breeze.core.config.CommonConfig;
import org.breeze.core.config.ResponseCode;
import org.breeze.core.constant.RequestMethod;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;
import org.breeze.core.utils.cache.UtilRedis;
import org.breeze.core.utils.string.UUIDGenerator;

/**
 * @Description: 用户登录
 * @Auther: 黑面阿呆
 * @Date: 2019-11-29 13:39
 * @Version: 1.0.0
 */
@Controller
public class LoginController {

    private static Log log = LogFactory.getLog(LoginController.class);

    private static final String SYS_USER_LOGIN_REDIS_KEY = "SYS_USER_LOGIN_REDIS_KEY_";

    @AutoAdd
    private LoginService loginService;

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @param serial   日志唯一序列
     * @return
     */
    @Params({
            @Param(name = "username", description = "用户名", required = true),
            @Param(name = "password", description = "密码", required = true)
    })
    @Permission(login = false)
    @Api(value = "login", method = RequestMethod.POST)
    public R login(String username, String password, Serial serial) {
        LoginInfo loginInfo = loginService.userLogin(username, password, serial);
        if (loginInfo.getLoginStatus() == ResponseCode.SUCCESS) {
            String auth_code = LoginSession.setLoginInfo(loginInfo, serial);
            return R.success(auth_code);
        } else {
            return R.failure(loginInfo.getLoginStatus());
        }
    }
}
