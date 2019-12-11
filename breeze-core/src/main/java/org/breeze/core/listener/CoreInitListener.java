package org.breeze.core.listener;

import org.breeze.core.bean.log.Serial;
import org.breeze.core.bean.api.BeanFactory;
import org.breeze.core.config.CommonConfig;
import org.breeze.core.config.ResponseCode;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @Description: 系统初始化监听
 * @Auther: 黑面阿呆
 * @Date: 2019/8/7 11:18
 * @Version: 1.0.0
 */
public class CoreInitListener implements ServletContextListener {

    private static Log log = LogFactory.getLog(CoreInitListener.class);

    /**
     * 系统实例初始化
     *
     * @param event
     */
    public void contextInitialized(ServletContextEvent event) {
        Serial serial = new Serial();
        log.logInfo("[ 系统初始化 ] 开始...", serial);
        CommonConfig.serverInit(event);
        // 加载所有系统实体类
        BeanFactory.scanAnnotationBean();
        // 初始化系统响应码至内存
        ResponseCode.initCodeMap();
        log.logInfo("[ 系统初始化 ] 完成...", serial);
    }

    /**
     * 系统关闭，销毁实例
     *
     * @param servletContextEvent
     */
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.logInfo("系统关闭...");
    }
}
