package org.breeze.core.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Auther: 黑面阿呆
 * @Date: 2019/8/9 15:18
 * @Version: 1.0.0
 */
public class RedisConfig {

    public static final String REDIS_DEFAULT_CONFIG = "default";

    private static final String REDIS_PERFIX = "redis";

    /**
     * 获取redis配置信息
     *
     * @param dbName 数据库名
     * @return
     */
    public static Map<String, String> initConfig(String dbName) {
        Map<String, String> config = new HashMap<String, String>();
        String defaultNameStr = REDIS_PERFIX + "." + REDIS_DEFAULT_CONFIG + ".";
        String dbNameStr = REDIS_PERFIX + "." + dbName + ".";
        for (Map.Entry<String, String> entry : BaseConfig.baseConfig.entrySet()) {
            if (entry.getKey().startsWith(dbNameStr)) {
                String value = BaseConfig.getValueOrDefault(dbNameStr + entry.getKey()
                        .substring(defaultNameStr.length()), entry.getKey());
                config.put(entry.getKey().substring(dbNameStr.length()), value);
            }
        }
        return config;
    }
}
