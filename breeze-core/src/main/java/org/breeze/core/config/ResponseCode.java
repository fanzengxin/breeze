package org.breeze.core.config;

import org.breeze.core.utils.file.UtilPropertyRead;
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
    private static Map<String, String> codeMap = new HashMap<String, String>();

    private static final String CODE_MAP_CONFIG = "response-code";

    // 请求成功
    public static final String SUCCESS = "0";
    public static final String ERROR_API_URL = "0001";
    public static final String ERROR_API_METHOD = "0001";
    public static final String SYSTEM_ERROR = "9999";

    public static final String USER_NO_LOGIN = "0011";
    public static final String USER_LOCKED_LOGIN = "0012";
    public static final String USER_DISABLED = "0013";

    public static final String PARAM_CHECK_FAILURE = "10011";



    /**
     * 初始化系统响应码
     */
    public static void initCodeMap() {
//        codeMap = UtilPropertyRead.getProperty(CODE_MAP_CONFIG);
    }

    /**
     * 获取响应码对应的相应信息
     * @param code
     * @return
     */
    public static String getResponseMsg(String code) {
        String msg = codeMap.get(code);
        if (UtilString.isNullOrEmpty(msg)) {
            msg = "未知的错误信息，请联系管理员";
        }
        return msg;
    }
}
