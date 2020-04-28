package org.breeze.admin.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * AES 对称加密算法
 */
public class AESUtil {

    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    /**
     * 统一门户密码加密方式
     */
    private static final String PORTAL_CIPHER_ALGORITHM = "AES/CBC/NoPadding";
    /**
     * 加密算法
     */
    private static final String KEY_ALGORITHM = "AES";
    /**
     * 字符集
     */
    private static final String CHAR_SET = "UTF-8";

    /**
     * AES 加密   AES/ECB/PKCS5Padding
     *
     * @param data 待加密的字符串
     * @param key  AES key
     * @return
     * @throws Exception
     */
    public static String encrypt4ECB(String data, String key) {
        if (key == null) {
            throw new RuntimeException("Key为空null");
        }
        if (key.length() != 16) {
            throw new RuntimeException("Key长度不是16位");
        }
        try {
            byte[] b = key.getBytes(CHAR_SET);
            SecretKeySpec skeySpec = new SecretKeySpec(b, KEY_ALGORITHM);
            //"算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(data.getBytes(CHAR_SET));
            //使用BASE64做转码
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            e.getStackTrace();
            throw new RuntimeException("AES加密异常！", e);
        }
    }

    /**
     * AES解密    AES/ECB/PKCS5Padding
     *
     * @param data 待解密的字符串
     * @param key  AES key
     * @return
     * @throws Exception
     */
    public static String decrypt4ECB(String data, String key) {
        if (key == null) {
            throw new RuntimeException("Key为空null");
        }
        if (key.length() != 16) {
            throw new RuntimeException("Key长度不是16位");
        }
        try {
            byte[] b = key.getBytes(CHAR_SET);
            // 指定AES解密
            SecretKeySpec skeySpec = new SecretKeySpec(b, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            // 初始化解码器
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            //先用base64解密
            byte[] encrypted = Base64.getDecoder().decode(data);
            // 执行解密
            byte[] original = cipher.doFinal(encrypted);
            return new String(original, CHAR_SET);
        } catch (Exception e) {
            e.getStackTrace();
            throw new RuntimeException("AES解密异常！", e);
        }
    }
}
