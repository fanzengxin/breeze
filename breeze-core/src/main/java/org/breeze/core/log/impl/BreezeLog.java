package org.breeze.core.log.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.config.CommonConfig;
import org.breeze.core.log.Log;


/**
 * @Description: 日志实体类
 * @Auther: 黑面阿呆
 * @Date: 2019/2/9 21:51
 * @Version: 1.0.0
 */
public class BreezeLog implements Log {

    private Logger log = null;

    /**
     * 处理日志信息
     *
     * @param msg    日志内容
     * @param serial 日志序列号
     * @return
     */
    private String getMsg(String msg, Serial serial) {
        if (serial == null) {
            serial = new Serial("");
        }
        StringBuffer sb = new StringBuffer();
        sb.append("[NODE:").append(CommonConfig.getNodeId()).append("]");
        sb.append("[");
        sb.append(serial);
        sb.append("]");
        sb.append(msg);
        return sb.toString();
    }

    /**
     * 处理带参数的日志信息
     *
     * @param msg     日志内容
     * @param serial  日志序列号
     * @param objects 日志参数
     * @return
     */
    private String getMsg(String msg, Serial serial, Object... objects) {
        return String.format(getMsg(msg, serial).replace("{}", "%s"), objects);
    }

    @Override
    public void init(Class c) {
        log = LogManager.getLogger(c);
    }

    @Override
    public void logTrace(String msg, Object... objects) {
        if (isTrace()) {
            log.trace(getMsg(msg, null), objects);
        }
    }

    @Override
    public void logTrace(String msg, Serial serial, Object... objects) {
        if (isTrace()) {
            log.trace(getMsg(msg, serial), objects);
        }
    }

    @Override
    public void logTrace(String msg, Throwable e, Object... objects) {
        if (isTrace()) {
            log.trace(getMsg(msg, null, objects), e);
        }
    }

    @Override
    public void logTrace(String msg, Throwable e, Serial serial, Object... objects) {
        if (isTrace()) {
            log.trace(getMsg(msg, serial, objects), e);
        }
    }

    @Override
    public void logDebug(String msg, Object... objects) {
        if (isDebug()) {
            log.debug(getMsg(msg, null), objects);
        }
    }

    @Override
    public void logDebug(String msg, Serial serial, Object... objects) {
        if (isDebug()) {
            log.debug(getMsg(msg, serial), objects);
        }
    }

    @Override
    public void logDebug(String msg, Throwable e, Object... objects) {
        if (isDebug()) {
            log.debug(getMsg(msg, null, objects), e);
        }
    }

    @Override
    public void logDebug(String msg, Throwable e, Serial serial, Object... objects) {
        if (isDebug()) {
            log.debug(getMsg(msg, serial, objects), e);
        }
    }

    @Override
    public void logInfo(String msg, Object... objects) {
        if (isInfo()) {
            log.info(getMsg(msg, null), objects);
        }
    }

    @Override
    public void logInfo(String msg, Serial serial, Object... objects) {
        if (isInfo()) {
            log.info(getMsg(msg, serial), objects);
        }
    }

    @Override
    public void logInfo(String msg, Throwable e, Object... objects) {
        if (isInfo()) {
            log.info(getMsg(msg, null, objects), e);
        }
    }

    @Override
    public void logInfo(String msg, Throwable e, Serial serial, Object... objects) {
        if (isInfo()) {
            log.info(getMsg(msg, serial, objects), e);
        }
    }

    @Override
    public void logWarn(String msg, Object... objects) {
        if (isWarn()) {
            log.warn(getMsg(msg, null), objects);
        }
    }

    @Override
    public void logWarn(String msg, Serial serial, Object... objects) {
        if (isWarn()) {
            log.warn(getMsg(msg, serial), objects);
        }
    }

    @Override
    public void logWarn(String msg, Throwable e, Object... objects) {
        if (isWarn()) {
            log.warn(getMsg(msg, null, objects), e);
        }
    }

    @Override
    public void logWarn(String msg, Throwable e, Serial serial, Object... objects) {
        if (isWarn()) {
            log.warn(getMsg(msg, serial, objects), e);
        }
    }

    @Override
    public void logError(String msg, Object... objects) {
        if (isError()) {
            log.error(getMsg(msg, null), objects);
        }
    }

    @Override
    public void logError(String msg, Serial serial, Object... objects) {
        if (isError()) {
            log.error(getMsg(msg, serial), objects);
        }
    }

    @Override
    public void logError(String msg, Throwable e, Object... objects) {
        if (isError()) {
            log.error(getMsg(msg, null, objects), e);
        }
    }

    @Override
    public void logError(String msg, Throwable e, Serial serial, Object... objects) {
        if (isError()) {
            log.error(getMsg(msg, serial, objects), e);
        }
    }

    @Override
    public boolean isTrace() {
        return log.isTraceEnabled();
    }

    @Override
    public boolean isDebug() {
        return log.isDebugEnabled();
    }

    @Override
    public boolean isInfo() {
        return log.isInfoEnabled();
    }

    @Override
    public boolean isWarn() {
        return log.isWarnEnabled();
    }

    @Override
    public boolean isError() {
        return log.isErrorEnabled();
    }
}
