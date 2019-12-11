package org.breeze.core.log;

import org.breeze.core.log.impl.BreezeLog;

/**
 * @Description: 获取日志实例对象
 * @Auther: 黑面阿呆
 * @Date: 2019/2/9 21:35
 * @Version: 1.0.0
 */
public class LogFactory {

    /**
     * 得到log实现类
     *
     * @param c
     * @return
     */
    public static Log getLog(Class c) {
        Log log = new BreezeLog();
        log.init(c);
        return log;
    }
}
