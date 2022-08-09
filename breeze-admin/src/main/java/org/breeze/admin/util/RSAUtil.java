package org.breeze.admin.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * 非对称加密算法RSA算法组件
 *
 * @Author adai
 */
public class RSAUtil {

    /**
     * 编码字符集
     */
    private static final String CHARSET = "UTF-8";
    /**
     * 算法
     */
    private static final String RSA_ALGORITHM = "RSA";
    /**
     * 公钥文件路径
     */
    private static final String ENCRPTY_FILE_NAME = "D:\\sso_pub_product.cer";
    private static final String JKS_FILE_NAME = "D:\\sso_store.jks";

    /**
     * RSA公钥加密
     *
     * @param data
     * @return
     */
    public static String encryptByPublicKey(String data) {
        try {
            PublicKey publicKey = getPublicKeyFromX509();
            // RSA加密
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            // 设置公钥
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.encodeBase64String(cipher.doFinal(data.getBytes(CHARSET)));
        } catch (Exception e) {
            e.getStackTrace();
            throw new RuntimeException("RSA签名验证异常", e);
        }
    }

    /**
     * RSA解密
     *
     * @param data
     * @return
     */
    public static String decryptByPrivateKey(String data, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(data.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

    /**
     * 从文件中获取公钥
     *
     * @return
     * @throws Exception
     */
    private static PublicKey getPublicKeyFromX509() throws Exception {
        InputStream fin = new FileInputStream(ENCRPTY_FILE_NAME);
        CertificateFactory f = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) f.generateCertificate(fin);
        return certificate.getPublicKey();
    }

    /**
     * RSA解密
     *
     * @param data
     * @return
     */
    public static String decryptByPrivateKey(String data) throws Exception {
        char[] password = "sinosoft@2020".toCharArray();
        KeyStore store = KeyStore.getInstance("jks");
        store.load(new FileInputStream(JKS_FILE_NAME), password);
        RSAPrivateCrtKey key = (RSAPrivateCrtKey) store.getKey("sso", password);
        try {
            //64位解码加密后的字符串
            byte[] inputByte = Base64.decodeBase64(data);
            //RSA解密
            Cipher cipher = Cipher.getInstance("RSA");
            // 设置私钥
            cipher.init(Cipher.DECRYPT_MODE, key);
            String outStr = new String(cipher.doFinal(inputByte), CHARSET);
            return outStr;
        } catch (Exception e) {
            e.getStackTrace();
        }
        return null;
    }
}
