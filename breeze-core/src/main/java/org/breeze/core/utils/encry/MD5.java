package org.breeze.core.utils.encry;

import org.breeze.core.utils.string.UtilString;

import java.security.MessageDigest;

/**
 * @Description: MD5加密工具类
 * @Auther: 黑面阿呆
 * @Date: 2019-11-29 13:39
 * @Version: 1.0.0
 */
public class MD5 {

    /**
     * MD5加密
     *
     * @param dataStr 待加密的字符串
     * @return
     */
    public static String encrypt(String dataStr) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(dataStr.getBytes("UTF8"));
            byte s[] = m.digest();
            StringBuffer result = new StringBuffer();
            for (int i = 0; i < s.length; i++) {
                result.append(Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6));
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 密码计算方式
     *
     * @param password 密码明文
     * @param salt     随机盐
     * @return
     */
    public static String getPassword(String password, String salt) {
        if (UtilString.isNotEmpty(salt) && salt.length() >= 16) {
            return encrypt(salt.substring(0, 8) + password + salt.substring(8));
        } else {
            return encrypt(salt + password);
        }
    }
}
