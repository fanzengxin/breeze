package org.breeze.core.utils.encry;

import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AES {

    private static Log log = LogFactory.getLog(AES.class);

    public static final String ENCODINGFORMAT = "UTF-8";
    // 密钥算法
    public static final String KEY_ALGORITHM = "AES";
    // 加解密算法/工作模式/填充方式,Java6.0支持PKCS5Padding填充方式,BouncyCastle支持PKCS7Padding填充方式
    public static final String CIPHER_ALGORITHM_ECB_PKCS5 = "AES/ECB/PKCS5Padding";
    // ECB-ZeroPadding加解密方式
    public static final String CIPHER_ALGORITHM_CBC_ZERO = "AES/CBC/NoPadding";

    /**
     * AES 加密操作
     *
     * @param content 待加密内容
     * @param key     加密密钥
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String key) {
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB_PKCS5);
            byte[] byteContent = content.getBytes("utf-8");
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key));
            // 加密
            byte[] result = cipher.doFinal(byteContent);
            //通过Base64转码返回
            return Base64.encode(result);
        } catch (Exception ex) {
            log.logError("AES加密失败", ex);
        }
        return null;
    }

    /**
     * AES 解密操作
     *
     * @param content
     * @param key
     * @return
     */
    public static String decrypt(String content, String key) {
        try {
            //实例化
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB_PKCS5);
            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key));
            //执行操作
            byte[] result = cipher.doFinal(Base64.decode(content));
            return new String(result, "utf-8");
        } catch (Exception ex) {
            log.logError("AES解密失败", ex);
        }
        return null;
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKeySpec getSecretKey(final String key) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            //AES 要求密钥长度为 128
            kg.init(128, new SecureRandom(key.getBytes()));
            //生成一个密钥
            SecretKey secretKey = kg.generateKey();
            // 转换为AES专用密钥
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException ex) {
            log.logError("生成AES加密秘钥失败", ex);
        }
        return null;
    }

    /**
     * CBC加密
     *
     * @param content
     * @param key
     * @param ivParameter
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String key, String ivParameter) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC_ZERO);
        // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(content.getBytes(ENCODINGFORMAT));
        // 此处使用BASE64做转码。
        return Base64.encode(encrypted);
    }

    /**
     * CBC解密
     *
     * @param content
     * @param key
     * @param ivParameter
     * @return
     * @throws Exception
     */
    public static String decrypt(String content, String key, String ivParameter) {
        try {
            byte[] raw = key.getBytes("utf-8");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC_ZERO);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            //先用base64解密
            byte[] encrypted1 = Base64.decode(content);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, ENCODINGFORMAT);
            return originalString;
        } catch (Exception ex) {
            log.logError("AES解密失败", ex);
        }
        return null;
    }
}
