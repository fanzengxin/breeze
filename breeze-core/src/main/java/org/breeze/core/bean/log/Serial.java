package org.breeze.core.bean.log;

import org.breeze.core.utils.date.UtilDateTime;
import org.breeze.core.utils.string.UtilString;

/**
 * @Description: 日志序列号，用户定位日志
 * @Auther: 黑面阿呆
 * @Date: 2019/2/9 21:39
 * @Version: 1.0.0
 */
public class Serial {

    // 序列对象
    private String serial;

    /**
     * 无参构造方法
     */
    public Serial() {
        this.serial = String.valueOf(UtilDateTime.currentTimeMillis());
    }

    /**
     * 带URL参数构造方法
     *
     * @param url
     */
    public Serial(String url) {
        StringBuffer serial = new StringBuffer();
        serial.append(UtilString.subString(url, "/service"));
        serial.append(":").append(UtilDateTime.currentTimeMillis());
        this.serial = serial.toString();
    }

    /**
     * 重写的toString方法
     *
     * @return
     */
    public String toString() {
        return serial;
    }
}
