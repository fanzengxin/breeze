package org.breeze.core.config;

/**
 * @Description: 文件路径配置信息
 * @Author: 黑面阿呆
 * @Date: 2020-05-11 15:30
 * @Version: 1.0.0
 */
public class FilePathConfig {

    /**
     * 获取服务器节点ID
     *
     * @return
     */
    public static String getFilePathCodeTemplate() {
        return BaseConfig.baseConfig.get("file.path.template");
    }

}
