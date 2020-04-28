package org.breeze.auth.service;

import org.breeze.admin.service.DeptService;
import org.breeze.admin.service.PermissionService;
import org.breeze.admin.service.RoleService;
import org.breeze.auth.dao.LoginDao;
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
import org.breeze.core.utils.img.UtilImage;
import org.breeze.core.utils.string.UUIDGenerator;
import org.breeze.core.utils.string.UtilString;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Description: 用户登录服务
 * @Auther: 黑面阿呆
 * @Date: 2019-12-04 11:23
 * @Version: 1.0.0
 */
@Service
public class LoginService {

    private static final Log log = LogFactory.getLog(LoginService.class);
    /**
     * 用户锁定时间。默认5分钟
     */
    private static final int SYS_USER_LOGIN_LOCKED_TIME = 300;

    /**
     * 登录出错锁定次数
     */
    private static final int SYS_USER_LOGIN_LOCKED_NUMBER = 5;

    /**
     * 用户锁定redis Key
     */
    private static final String SYS_USER_LOGIN_LOCKED_REDIS_KEY = "SYS_USER_LOGIN_LOCKED_REDIS_KEY_";

    /**
     * 用户登录验证码redis Key
     */
    private static final String SYS_USER_LOGIN_VERIFY_CODE_KEY = "SYS_USER_LOGIN_VERIFY_CODE_KEY_";

    /**
     * 用户登录验证码有效期，默认60秒
     */
    private static final int SYS_USER_LOGIN_VERIFY_CODE_USEFUL_TIME = 60;

    @AutoAdd
    private LoginDao loginDao;

    @AutoAdd
    private DeptService deptService;

    @AutoAdd
    private RoleService roleService;

    @AutoAdd
    private PermissionService permissionService;

    /**
     * 用户登录
     *
     * @param username     用户名
     * @param password     密码
     * @param verifyCode   验证码
     * @param verifyCodeId 验证码id
     * @param serial       日志序列号
     * @return
     */
    public LoginInfo userLogin(String username, String password, String verifyCode, String verifyCodeId, Serial serial) {
        // 验证图片验证码
        String serverVerifyCode = UtilRedis.getRedis(Redis.REDIS_LOGIN_DB)
                .get(SYS_USER_LOGIN_VERIFY_CODE_KEY + verifyCodeId, serial);
        if (UtilString.isNullOrEmpty(serverVerifyCode)) {
            log.logInfo("验证码不存在或已失效:{}", serial, verifyCodeId);
            return new LoginInfo(ResponseCode.LOGIN_VERIFY_CODE_EXPIRE);
        }
        if (!verifyCode.equalsIgnoreCase(serverVerifyCode)) {
            log.logInfo("验证码错误:{}", serial, verifyCodeId);
            return new LoginInfo(ResponseCode.LOGIN_VERIFY_CODE_ERROR);
        }
        // 验证用户登录锁定
        String lockedKey = UtilRedis.getRedis(Redis.REDIS_LOGIN_DB)
                .get(SYS_USER_LOGIN_LOCKED_REDIS_KEY + username, serial);
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
            log.logInfo("用户不存在:{}", serial, username);
            return new LoginInfo(ResponseCode.LOGIN_STATUS_UNFIND);
        }
        if (user.getInt("status") == 0) {
            log.logInfo("用户已禁用:{}", serial, username);
            return new LoginInfo(ResponseCode.LOGIN_STATUS_DISABLED);
        }
        // 验证用户密码
        String passwordm = MD5.getPassword(password, user.getString("salt"));
        if (user.getString("password", "").equals(passwordm)) {
            String userId = user.getString("id");
            // 查询关联角色
            List<String> roles = roleService.getRoleByUser(userId, serial);
            // 查询关联部门
            List<String> depts = deptService.getDeptByUser(userId, serial);
            // 查询关联权限
            List<String> permissions = permissionService.getPermissionsByUser(userId, serial);
            // 构建用户信息
            LoginInfo loginInfo = new LoginInfo(user.getLong("id"), user.getString("user_id"),
                    user.getString("user_name"), user.getString("gender"), user.getString("phone"),
                    user.getString("email"), roles, depts,permissions);
            loginInfo.setLoginStatus(ResponseCode.SUCCESS);
            return loginInfo;
        } else {
            UtilRedis.getRedis(Redis.REDIS_LOGIN_DB).setex(SYS_USER_LOGIN_LOCKED_REDIS_KEY + username,
                    SYS_USER_LOGIN_LOCKED_TIME, String.valueOf(++lockedNum), serial);
            log.logInfo("用户密码验证失败:{}", serial, username);
            return new LoginInfo(ResponseCode.LOGIN_STATUS_ERROR_PWD);
        }
    }

    /**
     * 生成图片验证码
     *
     * @param verifyCodeId 验证码Id
     * @param serial
     * @return
     */
    public Data getVerifyCode(String verifyCodeId, Serial serial) {
        try {
            // 生成随机串
            Map<String, String> verifyCode = UtilString.getRandomVerifyCode();
            String code = UtilImage.createImageWithVerifyCode(200, 80, verifyCode.get("code"));
            if (UtilString.isNullOrEmpty(verifyCodeId)) {
                verifyCodeId = UUIDGenerator.JavaUUID();
            }
            UtilRedis.getRedis(Redis.REDIS_LOGIN_DB).setex(SYS_USER_LOGIN_VERIFY_CODE_KEY + verifyCodeId,
                    SYS_USER_LOGIN_VERIFY_CODE_USEFUL_TIME, verifyCode.get("value"), serial);
            Data data = new Data();
            data.add("code", code);
            data.add("codeId", verifyCodeId);
            return data;
        } catch (IOException e) {
            log.logError("验证码生成失败", e);
        }
        return null;
    }
}
