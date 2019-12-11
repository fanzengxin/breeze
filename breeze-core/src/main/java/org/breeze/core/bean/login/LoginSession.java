package org.breeze.core.bean.login;

import org.breeze.core.bean.log.Serial;
import org.breeze.core.config.CommonConfig;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;
import org.breeze.core.utils.cache.UtilRedis;
import org.breeze.core.utils.string.UUIDGenerator;
import org.breeze.core.utils.string.UtilString;

/**
 * @Description: 用户session工具类
 * @Auther: 黑面阿呆
 * @Date: 2019/8/8 15:21
 * @Version: 1.0.0
 */
public class LoginSession {

    private static Log log = LogFactory.getLog(LoginSession.class);
    // 用户登录 redis 库序号
    public static final int LOGIN_INFO_REDIS_DB = 3;
    // login 数据 key 前缀
    private static final String LOGIN_INFO_REDIS_PREFIX = "LoginInfo_";
    // login 锁定数据 key 前缀
    private static final String LOCK_INFO_REDIS_PREFIX = "LoginLocked_";

    private static final String USER_LOGIN_LOCKED = "locked";

    /**
     * 获取当前登录用户的信息
     *
     * @param auth_code
     * @param serial
     * @return
     */
    public static LoginInfo getLoginInfo(String auth_code, Serial serial) {
        String info = UtilRedis.getRedis(LOGIN_INFO_REDIS_DB).get(LOGIN_INFO_REDIS_PREFIX + auth_code, serial);
        if (UtilString.isNotEmpty(info)) {
            return LoginInfo.parseLoginInfo(info);
        } else {
            return null;
        }
    }

    /**
     * 保存用户登录信息
     *
     * @param loginInfo
     * @param serial
     * @return
     */
    public static String setLoginInfo (LoginInfo loginInfo, Serial serial) {
        String sessionId = UUIDGenerator.JavaUUID();
        UtilRedis.getRedis(LOGIN_INFO_REDIS_DB).setex(LOGIN_INFO_REDIS_PREFIX + sessionId, CommonConfig.getLoginSessionValidityTime(),
                loginInfo.toString() , serial);
        return sessionId;
    }

    /**
     * 检测用户是否被锁定
     *
     * @param userId
     * @param serial
     * @return
     */
    public static boolean checkUserLocked(String userId, Serial serial) {
        String userStatus = UtilRedis.getRedis(LOGIN_INFO_REDIS_DB).get(LOCK_INFO_REDIS_PREFIX + userId, serial);
        if (USER_LOGIN_LOCKED.equalsIgnoreCase(userStatus)) {
            return true;
        } else {
            return false;
        }
    }
}
