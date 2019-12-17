package org.breeze.admin.service;

import org.breeze.admin.dao.LoginDao;
import org.breeze.core.annotation.common.AutoAdd;
import org.breeze.core.annotation.service.Service;
import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.bean.login.LoginInfo;
import org.breeze.core.bean.redis.Redis;
import org.breeze.core.config.ResponseCode;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;
import org.breeze.core.utils.cache.UtilRedis;
import org.breeze.core.utils.encry.MD5;
import org.breeze.core.utils.string.UtilString;

import java.util.HashSet;

/**
 * @Description: 用户登录服务
 * @Auther: 黑面阿呆
 * @Date: 2019-12-04 11:23
 * @Version: 1.0.0
 */
@Service
public class LoginService {

    private static final Log log = LogFactory.getLog(LoginService.class);
    // 用户锁定时间。默认5分钟
    private static final int SYS_USER_LOGIN_LOCKED_TIME = 300;
    private static final int SYS_USER_LOGIN_LOCKED_NUMBER = 5;
    private static final String SYS_USER_LOGIN_LOCKED_REDIS_KEY = "SYS_USER_LOGIN_LOCKED_REDIS_KEY_";

    @AutoAdd
    private LoginDao loginDao;

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @param serial   日志序列号
     * @return
     */
    public LoginInfo userLogin(String username, String password, Serial serial) {
        String lockedKey = UtilRedis.getRedis(Redis.REDIS_LOGIN_DB).get(SYS_USER_LOGIN_LOCKED_REDIS_KEY + username, serial);
        int lockedNum = 0;
        if (UtilString.isNotEmpty(lockedKey)) {
            lockedNum = Integer.parseInt(lockedKey);
        }
        if (lockedNum >= SYS_USER_LOGIN_LOCKED_NUMBER) {
            log.logInfo("用户已锁定:{}", serial, username);
            return new LoginInfo(ResponseCode.LOGIN_STATUS_LOCKED);
        }
        // 查询用户信息
        Data user = loginDao.getUserInfo(username, serial);
        if (user == null) {
            log.logInfo("用户名不存在:{}", serial, username);
            return new LoginInfo(ResponseCode.LOGIN_STATUS_UNFIND);
        }
        if (user.getInt("status") != 0) {
            log.logInfo("用户名已禁用:{}", serial, username);
            return new LoginInfo(ResponseCode.LOGIN_STATUS_DISABLED);
        }
        String passwordm = MD5.getPassword(password, user.getString("salt"));
        if (user.getString("password", "").equals(passwordm)) {
            LoginInfo loginInfo = new LoginInfo(user.getString("id"), user.getString("user_id"),
                    user.getString("user_name"), user.getString("gender"), user.getString("phone"),
                    user.getString("email"), new HashSet<String>(), new HashSet<String>(), new HashSet<String>());
            loginInfo.setLoginStatus(ResponseCode.SUCCESS);
            return loginInfo;
        } else {
            UtilRedis.getRedis(Redis.REDIS_LOGIN_DB).setex(SYS_USER_LOGIN_LOCKED_REDIS_KEY + username,
                    SYS_USER_LOGIN_LOCKED_TIME, String.valueOf(++lockedNum), serial);
            log.logInfo("用户密码验证失败:{}", serial, username);
            return new LoginInfo(ResponseCode.LOGIN_STATUS_ERROR_PWD);
        }
    }
}
