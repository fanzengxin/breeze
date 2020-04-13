package org.breeze.auth.controller;

import org.breeze.auth.service.LoginService;
import org.breeze.core.annotation.common.AutoAdd;
import org.breeze.core.annotation.controller.*;
import org.breeze.core.bean.api.R;
import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.bean.login.LoginInfo;
import org.breeze.core.bean.login.LoginSession;
import org.breeze.core.config.CommonConfig;
import org.breeze.core.config.ResponseCode;
import org.breeze.core.constant.RequestMethod;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;
import org.breeze.core.utils.encry.AES;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 用户登录
 * @Auther: 黑面阿呆
 * @Date: 2019-11-29 13:39
 * @Version: 1.0.0
 */
@Controller(mapper = "auth")
public class LoginController {

    private static Log log = LogFactory.getLog(LoginController.class);

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
            @Param(name = "password", description = "密码", required = true),
            @Param(name = "verifyCode", description = "验证码", required = true),
            @Param(name = "verifyCodeId", description = "验证码ID", required = true),
    })
    @Permission(login = false)
    @Api(value = "login", method = RequestMethod.POST)
    public R login(String username, String password, String verifyCode, String verifyCodeId, Serial serial) {
        password = AES.decrypt(password, CommonConfig.getSecretAuthKey(), CommonConfig.getSecretAuthKey());
        LoginInfo loginInfo = loginService.userLogin(username, password, verifyCode, verifyCodeId, serial);
        if (loginInfo.getLoginStatus() == ResponseCode.SUCCESS) {
            log.logInfo("用户{}登录成功", serial, username);
            String auth_code = LoginSession.setLoginInfo(loginInfo, serial);
            return R.success(auth_code);
        } else {
            return R.failure(loginInfo.getLoginStatus());
        }
    }

    /**
     * 获取图片验证码
     *
     * @param verifyCodeId 验证码Id,重复刷新code不变，避免多次请求造成的垃圾数据
     * @param serial
     * @return
     */
    @Params({
            @Param(name = "verifyCodeId", description = "验证码id")
    })
    @Permission(login = false)
    @Api(value = "code", method = RequestMethod.GET)
    public R code(String verifyCodeId, Serial serial) {
        Data data = loginService.getVerifyCode(verifyCodeId, serial);
        if (data != null) {
            return R.success(data);
        } else {
            return R.failure("验证码刷新失败");
        }
    }

    /**
     * 用户登出
     *
     * @param request
     * @param serial
     * @return
     */
    @Permission
    @Api(value = "logout", method = RequestMethod.DELETE)
    public R logout(HttpServletRequest request, Serial serial) {
        LoginSession.removeLoginInfo(request.getHeader("auth_code"), serial);
        return R.success();
    }
}
