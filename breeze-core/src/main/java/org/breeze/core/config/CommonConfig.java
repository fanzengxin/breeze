package org.breeze.core.config;

import org.breeze.core.utils.string.UtilString;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 常规配置信息
 * @Auther: 黑面阿呆
 * @Date: 2019/8/6 11:57
 * @Version: 1.0.0
 */
public class CommonConfig {

    // 存储配置信息的Map变量
    private static Map<String, String> config = new HashMap<String, String>();
    // 是否初始化
    private static boolean isInit = false;

    /**
     * 设置项目路径信息
     * @param event
     */
    public static void serverInit(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        String appRealPath = context.getRealPath("/");
        if (!appRealPath.endsWith(String.valueOf(File.separatorChar))) {
            appRealPath = appRealPath + File.separatorChar;
        }
        String contextPath = context.getContextPath();
        CommonConfig.setContextPath(contextPath);
        if (CommonConfig.getProjectPath() == null) {
            appRealPath = appRealPath.replace("\\", "/");
            CommonConfig.setProjectPath(appRealPath);
        }
    }

    /**
     * 初始化数据配置信息
     */
    public static synchronized void initConfig() {
        if (!isInit) {
            BaseConfig.initAllConfig();
            isInit = true;
        }
    }

    /**
     * 获取服务器节点ID
     *
     * @return
     */
    public static String getNodeId() {
        if (!isInit) {
            initConfig();
        }
        return BaseConfig.baseConfig.get("base.node.id");
    }

    /**
     * 获取服务器节点名称
     *
     * @return
     */
    public static String getNodeName() {
        if (!isInit) {
            initConfig();
        }
        return BaseConfig.baseConfig.get("node.name");
    }

    /**
     * 获取日志实现类
     *
     * @return
     */
    public static String getLogImplClassName() {
        if (!isInit) {
            initConfig();
        }
        return BaseConfig.baseConfig.get("log.impl.class");
    }

    /**
     * 设置字符编码
     *
     * @param encoding
     */
    public static void setEncoding(String encoding) {
        if (!isInit) {
            initConfig();
        }
        BaseConfig.baseConfig.put("system.encoding", encoding);
    }

    /**
     * 获取字符编码
     *
     * @return
     */
    public static String getEncoding() {
        if (!isInit) {
            initConfig();
        }
        return BaseConfig.baseConfig.get("system.encoding");
    }

    /**
     * 获取系统加解密密钥
     *
     * @return
     */
    public static String getSecretKey() {
        if (!isInit) {
            initConfig();
        }
        return BaseConfig.baseConfig.get("security.secretKey");
    }

    /**
     * 获取系统加解密密钥
     *
     * @return
     */
    public static String getSecretAuthKey() {
        if (!isInit) {
            initConfig();
        }
        return BaseConfig.baseConfig.get("security.secretAuthKey");
    }

    /**
     * 获取用户session有效期
     *
     * @return
     */
    public static int getLoginSessionValidityTime() {
        if (!isInit) {
            initConfig();
        }
        String time = BaseConfig.baseConfig.get("base.login.alive-time");
        if (UtilString.isNotEmpty(time)) {
            return Integer.parseInt(time);
        } else {
            return 1800;
        }
    }

    /**
     * 获取数据库实现类
     *
     * @param dbType
     * @return
     */
    public static String getDataBaseImplClassName(String dbType) {
        if (!isInit) {
            initConfig();
        }
        return BaseConfig.baseConfig.get("database." + dbType);
    }

    /**
     * 是否缓存数据库数据类型
     *
     * @return
     */
    public static boolean isDataInfoCache() {
        if (!isInit) {
            initConfig();
        }
        return "true".equalsIgnoreCase(BaseConfig.baseConfig.get("db.datainfo.cache"));
    }

    /**
     * 获取数据库配置文件
     *
     * @return
     */
    public static String getDataBaseConfigFile() {
        if (!isInit) {
            initConfig();
        }
        return BaseConfig.baseConfig.get("datasource.config.file");
    }

    /**
     * 获取数据库链接实现类
     *
     * @return
     */
    public static String getDefaultConn(String type) {
        if (!isInit) {
            initConfig();
        }
        return BaseConfig.baseConfig.get("default.database.conn." + type);
    }

    /**
     * 获取项目配置信息路径
     *
     * @return
     */
    public static String getConfigPath() {
        if (!isInit) {
            initConfig();
        }
        return BaseConfig.baseConfig.get("server.config.path");
    }

    /**
     * 设置项目配置信息路径
     *
     * @return
     */
    public static void setContextPath(String contextPath) {
        BaseConfig.baseConfig.put("server.context.path", contextPath);
    }

    /**
     * 获取项目配置信息路径
     *
     * @return
     */
    public static String getContextPath() {
        return BaseConfig.baseConfig.get("server.context.path");
    }

    /**
     * 设置项目路径
     *
     * @param projectPath
     */
    public static void setProjectPath(String projectPath) {
        BaseConfig.baseConfig.put("server.project.path", projectPath);
    }

    /**
     * 获取项目路径
     *
     * @param
     */
    public static String getProjectPath() {
        return BaseConfig.baseConfig.get("server.project.path");
    }
}
