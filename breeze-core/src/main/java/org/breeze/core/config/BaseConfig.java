package org.breeze.core.config;

import org.breeze.core.utils.string.UtilString;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 获取所有基础配置信息
 * @Auther: 黑面阿呆
 * @Date: 2019-11-27 15:26
 * @Version: 1.0.0
 */
public class BaseConfig {

    private static final String BASE_CONFIG_SOURCE_FILENAME = "application-base.yml";
    private static final String APP_CONFIG_SOURCE_FILENAME = "application.yml";

    // 所有数据配置信息
    protected static Map<String, String> baseConfig = new HashMap<String, String>();

    /**
     * 初始化所有配置信息
     */
    public static void initAllConfig() {
        System.out.println("开始读取服务器配置信息...");
        baseConfig = new HashMap<String, String>();
        Yaml yaml = new Yaml();
        InputStream baseIs = BaseConfig.class.getClassLoader().getResourceAsStream(BASE_CONFIG_SOURCE_FILENAME);
        //加载流,获取yaml文件中的配置数据，然后转换为Map，
        Map<String, Object> baseMap = (Map<String, Object>) yaml.load(baseIs);
        readConfig(baseMap, "");
        InputStream appIs = BaseConfig.class.getClassLoader().getResourceAsStream(APP_CONFIG_SOURCE_FILENAME);
        //加载流,获取yaml文件中的配置数据，然后转换为Map，
        Map<String, Object> appMap = (Map<String, Object>) yaml.load(appIs);
        readConfig(appMap, "");
        System.out.println("完成读取服务器配置信息!!!");
    }

    /**
     * 遍历Map，将配置信息存储至统一配置缓存
     *
     * @param configMap
     * @param parentKey
     */
    private static void readConfig(Map<String, Object> configMap, String parentKey) {
        for (Map.Entry<String, Object> entry : configMap.entrySet()) {
            if (entry.getValue() instanceof Map) {
                readConfig((Map<String, Object>) entry.getValue(), (parentKey.length() > 0 ? parentKey + "." : "") + entry.getKey());
            } else if (entry.getValue() instanceof Integer) {
                if (entry.getValue() != null) {
                    baseConfig.put(getKey(parentKey, entry.getKey()), String.valueOf(entry.getValue()));
                }
            } else if (entry.getValue() instanceof String) {
                if (entry.getValue() != null) {
                    baseConfig.put(getKey(parentKey, entry.getKey()), entry.getValue().toString());
                }
            }
        }
    }

    /**
     * 生成指定key
     *
     * @param parent 父级key
     * @param key    当前key
     * @return
     */
    private static String getKey(String parent, Object key) {
        if (UtilString.isNullOrEmpty(parent)) {
            return String.valueOf(key);
        } else {
            return parent + "." + String.valueOf(key);
        }
    }

    /**
     * 获取指定key对应的数据，如数据为空则取默认值
     *
     * @param key        指定key
     * @param defaultKey 为空时的数据key
     * @return
     */
    public static String getValueOrDefault(String key, String defaultKey) {
        String value = baseConfig.get(key);
        if (value == null) {
            return baseConfig.get(defaultKey);
        } else {
            return value;
        }
    }
}
