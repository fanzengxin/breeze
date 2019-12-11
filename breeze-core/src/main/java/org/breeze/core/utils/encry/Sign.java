package org.breeze.core.utils.encry;

import org.breeze.core.bean.log.Serial;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;
import org.breeze.core.utils.date.UtilDateTime;
import org.breeze.core.utils.string.UtilString;

import java.util.Map;


public class Sign {

    private static Log log = LogFactory.getLog(Sign.class);

    /**
     * SHA1加密
     *
     * @param str 要加密的字符串
     * @return
     */
    public static String SHA1(String str) {
        return EncoderHandler.encode("SHA-1", str);
    }

    /**
     * 签名
     *
     * @param str
     * @param secret
     * @return
     */
    public static String sign(String str, String secret) {
        str += secret;
        return EncoderHandler.encode("SHA-1", str.toUpperCase());
    }

    /**
     * 验证签名是否正确
     *
     * @param paramMap
     * @param data
     * @param serial
     * @return
     */
    public static boolean verifySign(Map<String, String> paramMap, String data, Serial serial) {
        String timestamp = paramMap.get("timestamp");
        String appid = paramMap.get("appid");
        String signStr = paramMap.get("sign");
        //如果没有签名必须参数，直接返回签名无效
        if (UtilString.isNullOrEmpty(appid) || UtilString.isNullOrEmpty(timestamp) || UtilString.isNullOrEmpty(signStr)) {
            log.logInfo("签名参数无效:" + appid, serial);
            return false;
        }
        //如果时间戳跟服务器时间相差超过30*2分钟，签名无效
        long nowTime = UtilDateTime.currentTimeSeconds();
        long time = 0l;
        try {
            time = Long.parseLong(timestamp, 10);
        } catch (Exception e) {
            log.logInfo("签名时间戳不正确:[" + timestamp + "]" + e.getMessage(), e, serial);
            return false;
        }
        long disparity = nowTime - time;
        if (disparity > 60 * 30 || disparity < -60 * 30) {
            log.logInfo("签名失效", serial);
            return false;
        }
        String str = getSignStr(appid, timestamp, data);
        // String secret = AppConfig.getString(appid);
        String secret = "";
        if (UtilString.isNullOrEmpty(secret)) {
            log.logInfo("签名appid无效", serial);
            return false;
        }
        String signValue = sign(str, secret);
        if (signValue.equalsIgnoreCase(signStr)) {
            log.logInfo("签名正确", serial);
            return true;
        } else {
            log.logInfo("签名无效:" + appid, serial);
            return false;
        }
    }

    /**
     * 根据参数得到要签名的字符串
     *
     * @param appid     应用标识
     * @param timestamp 时间戳
     * @param data
     * @return
     */
    public static String getSignStr(String appid, String timestamp, String data) {
        StringBuffer str = new StringBuffer();
        str.append("appid").append(appid);
        str.append("timestamp").append(timestamp);
        String sha1Data = EncoderHandler.encode("SHA-1", data.trim()).toUpperCase();
        str.append(sha1Data);
        return str.toString();
    }

    /**
     * 计算签名
     *
     * @param appid
     * @param secret
     * @param timestamp
     * @param data
     * @return 签名串
     */
    public static String getSign(String appid, String secret, String timestamp, String data) {
        String str = getSignStr(appid, timestamp, data);
        String signValue = sign(str, secret);
        return signValue;
    }
}
