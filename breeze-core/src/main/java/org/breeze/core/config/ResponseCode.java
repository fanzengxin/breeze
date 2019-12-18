package org.breeze.core.config;

import org.breeze.core.utils.string.UtilString;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 响应码配置
 * @Auther: 黑面阿呆
 * @Date: 2019-11-29 09:41
 * @Version: 1.0.0
 */
public class ResponseCode {

    private static Map<Integer, String> codeMap = new HashMap<Integer, String>();

    private static final String RESPONSE_CODE_PERFIX = "response.code.";

    private static final String CODE_MAP_CONFIG = "response-code";

    // 请求成功
    public static final int SUCCESS = 0;
    public static final int FAILURE = 1;
    public static final int SYSTEM_ERROR = 99999;
    public static final String SUCCESS_MSG = "请求成功";

    public static final int LOGIN_STATUS_UNFIND = 20001; // 用户名不存在
    public static final int LOGIN_STATUS_ERROR_PWD = 20002;// 密码错误
    public static final int LOGIN_STATUS_DISABLED = 20003;// 用户禁用
    public static final int LOGIN_STATUS_LOCKED = 20004; // 登录锁定
    public static final int LOGIN_STATUS_NO_LOGIN = 20005; // 未登录
    public static final int LOGIN_STATUS_OTHER_ERROR = 20009;// 其他登录错误

    public static final int ERROR_API_URL = 10001;
    public static final int ERROR_API_METHOD = 10002;
    public static final int ERROR_API_PARAM_CHECK = 10003;



    /**
     * 初始化系统响应码
     */
    public static void initCodeMap() {
        for (Map.Entry<String, String> entry : BaseConfig.baseConfig.entrySet()) {
            if (entry.getKey().startsWith(RESPONSE_CODE_PERFIX)) {
                codeMap.put(Integer.parseInt(entry.getKey().substring(RESPONSE_CODE_PERFIX.length())), entry.getValue());
            }
        }
    }

    /**
     * 获取响应码对应的相应信息
     * @param code
     * @return
     */
    public static String getResponseMsg(Integer code) {
        String msg = codeMap.get(code);
        if (UtilString.isNullOrEmpty(msg)) {
            msg = "未知的错误信息，请联系管理员";
        }
        return msg;
    }
}
