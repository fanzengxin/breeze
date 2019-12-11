package org.breeze.core.utils.file;

import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;
import org.breeze.core.utils.string.UtilString;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @Description: 读取properties文件
 * @Auther: 黑面阿呆
 * @Date: 2019/2/9 22:06
 * @Version: 1.0.0
 */
public class UtilPropertyRead {

    private static Log log = LogFactory.getLog(UtilPropertyRead.class);

    /**
     * 获取properties文件的键值对信息
     *
     * @param propertyFileName
     * @return
     */
    public static Map<String, String> getProperty(String propertyFileName) {
        Map<String, String> result = new HashMap<String, String>();
        if (!UtilString.isNullOrEmpty(propertyFileName)) {
            try {
                ResourceBundle resourceBundle = ResourceBundle.getBundle(propertyFileName);
                Enumeration<String> enumeration = resourceBundle.getKeys();
                while (enumeration.hasMoreElements()) {
                    String key = enumeration.nextElement();
                    result.put(key, new String(resourceBundle.getString(key).getBytes("ISO-8859-1"), "UTF8"));
                }
            } catch (Exception e) {
                log.logError("获取properties文件失败：{}", e, propertyFileName);
            }
        }
        return result;
    }
}
