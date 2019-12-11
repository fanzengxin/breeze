package org.breeze.core.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 数据库配置
 * @Auther: 黑面阿呆
 * @Date: 2019/8/9 15:18
 * @Version: 1.0.0
 */
public class DataBaseConfig {

    public static final String DATABASE_DEFAULT_CONFIG = "default";

    private static final String DATABASE_PERFIX = "database";

    /**
     * 获取数据库配置信息
     *
     * @return
     */
    public static Map<String, String> initConfig() {
        return initConfig(DATABASE_DEFAULT_CONFIG);
    }

    /**
     * 获取数据库配置信息
     *
     * @param dbName 数据库名
     * @return
     */
    public static Map<String, String> initConfig(String dbName) {
        Map<String, String> config = new HashMap<String, String>();
        String defaultNameStr = DATABASE_PERFIX + "." + DATABASE_DEFAULT_CONFIG + ".";
        String dbNameStr = DATABASE_PERFIX + "." + dbName + ".";
        for (Map.Entry<String, String> entry : BaseConfig.baseConfig.entrySet()) {
            if (entry.getKey().startsWith(dbNameStr)) {
                String value = BaseConfig.getValueOrDefault(dbNameStr + entry.getKey()
                        .substring(defaultNameStr.length()), entry.getKey());
                config.put(entry.getKey().substring(dbNameStr.length()), value);
            }
        }
        return config;
    }

    /**
     * 获取所有已配置的数据库名
     *
     * @return
     */
    public static Map<String, Boolean> getAllDBs() {
        Map<String, Boolean> dbMap = new HashMap<String, Boolean>();
        for (Map.Entry<String, String> entry : BaseConfig.baseConfig.entrySet()) {
            String[] keys = entry.getKey().split("\\.");
            if (keys.length > 1 && DATABASE_PERFIX.equalsIgnoreCase(keys[0]) && dbMap.get(keys[1]) == null) {
                dbMap.put(keys[1], true);
            }
        }
        return dbMap;
    }
}
