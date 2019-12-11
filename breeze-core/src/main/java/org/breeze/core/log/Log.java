package org.breeze.core.log;

import org.breeze.core.bean.log.Serial;

/**
 * @Description: 日志接口
 * @Auther: 黑面阿呆
 * @Date: 2019/2/9 21:37
 * @Version: 1.0.0
 */
public interface Log {

    /**
     * 日志对象初始化
     *
     * @param c
     */
    void init(Class c);

    /**
     * 记录跟踪调试信息
     *
     * @param msg 跟踪调试的信息
     */
    void logTrace(String msg, Object... objects);

    /**
     * 记录跟踪调试信息
     *
     * @param msg    跟踪调试信息
     * @param serial 序列号
     */
    void logTrace(String msg, Serial serial, Object... objects);

    /**
     * 记录跟踪调试信息
     *
     * @param msg 跟踪调试的信息
     * @param e
     */
    void logTrace(String msg, Throwable e, Object... objects);

    /**
     * 记录跟踪调试信息
     *
     * @param msg    跟踪调试信息
     * @param e
     * @param serial 序列号
     */
    void logTrace(String msg, Throwable e, Serial serial, Object... objects);

    /**
     * 记录调试信息
     *
     * @param msg 调试信息
     */
    void logDebug(String msg, Object... objects);

    /**
     * 记录调试信息
     *
     * @param msg    消息
     * @param serial 序列号
     */
    void logDebug(String msg, Serial serial, Object... objects);

    /**
     * 记录调试信息
     *
     * @param msg 调试信息
     * @param e
     */
    void logDebug(String msg, Throwable e, Object... objects);

    /**
     * 记录调试信息
     *
     * @param msg    调试信息
     * @param e      异常
     * @param serial 序列号
     */
    void logDebug(String msg, Throwable e, Serial serial, Object... objects);

    /**
     * 记录一般信息<br>
     *
     * @param msg 日志信息
     */
    void logInfo(String msg, Object... objects);

    /**
     * 记录一般信息
     *
     * @param msg    信息
     * @param serial 序列号
     */
    void logInfo(String msg, Serial serial, Object... objects);

    /**
     * 记录一般信息
     *
     * @param msg 日志信息
     * @param e
     */
    void logInfo(String msg, Throwable e, Object... objects);

    /**
     * 记录一般信息
     *
     * @param msg    日志
     * @param e
     * @param serial 序列号
     */
    void logInfo(String msg, Throwable e, Serial serial, Object... objects);

    /**
     * 记录警告信息
     *
     * @param msg 警告信息
     */
    void logWarn(String msg, Object... objects);

    /**
     * 记录警告信息
     *
     * @param msg    警告信息
     * @param serial 序列号
     */
    void logWarn(String msg, Serial serial, Object... objects);

    /**
     * 记录警告信息
     *
     * @param msg 警告信息
     * @param e
     */
    void logWarn(String msg, Throwable e, Object... objects);

    /**
     * 记录警告信息
     *
     * @param msg    警告信息
     * @param e
     * @param serial 序列号
     */
    void logWarn(String msg, Throwable e, Serial serial, Object... objects);

    /**
     * 记录错误信息
     *
     * @param msg 错误信息
     */
    void logError(String msg, Object... objects);

    /**
     * 记录错误信息
     *
     * @param msg    错误信息
     * @param serial 序列号
     */
    void logError(String msg, Serial serial, Object... objects);

    /**
     * 记录错误信息
     *
     * @param msg 错误信息
     * @param e
     */
    void logError(String msg, Throwable e, Object... objects);

    /**
     * 记录错误信息
     *
     * @param msg    错误信息
     * @param e
     * @param serial 序列号
     */
    void logError(String msg, Throwable e, Serial serial, Object... objects);

    /**
     * 跟踪信息是否打开
     *
     * @return
     */
    boolean isTrace();

    /**
     * 调试开关是否打开
     *
     * @return
     */
    boolean isDebug();

    /**
     * 一般信息开关是否打开
     *
     * @return
     */
    boolean isInfo();

    /**
     * 警告信息开关是否打开
     *
     * @return
     */
    boolean isWarn();

    /**
     * 错误信息是否打开
     *
     * @return
     */
    boolean isError();
}
