package org.breeze.core.utils.url;

import org.breeze.core.config.CommonConfig;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;

import java.net.URL;


/**
 * URL工具类<br>
 *
 * @author 黑面阿呆
 *
 */
public class UtilURL {
    /**
     * 得到配置文件的URL全路径<br>
     * 必须在调用类的编译路径中
     *
     * @param xmlFullName
     *            xml文件的全路径，例如：<b>/config/test.xml</b>
     * @return 指定配置文件的全路径URL
     */
    public static URL getXmlConfigUri(String xmlFullName) {
        URL s = Class.class.getResource(xmlFullName);
        return s;
    }

    /**
     * 得到默认的配置文件路径
     *
     * @param xmlName
     *            配置文件的名称和路径
     * @return 指定配置文件的全路径的字符串
     */
    public static String getDefaultXmlConfigUriString(String xmlName) {
        String projectPath = CommonConfig.getProjectPath();
        StringBuffer buf = null;
        if (projectPath == null) {
            String contextPath = CommonConfig.getContextPath();
            if (contextPath == null) {
                contextPath = "/WebContent";
            }
            if ("".equals(contextPath)) {
                contextPath = "/";
            }
            projectPath = System.getProperty("user.dir") + contextPath;
        }
        if (!projectPath.endsWith("/")) {
            projectPath = projectPath + "/";
        }
        buf = new StringBuffer(projectPath);
        buf.append(CommonConfig.getConfigPath()).append("/").append(xmlName);
        Log log = LogFactory.getLog(UtilURL.class);
        if (log.isDebug()) {
            log.logDebug("getDefaultXmlConfigUriString=" + buf.toString());
        }
        return buf.toString();
    }
}
