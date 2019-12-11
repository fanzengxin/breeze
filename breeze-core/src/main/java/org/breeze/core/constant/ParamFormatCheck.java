package org.breeze.core.constant;

/**
 * @Description: 请求参数格式验证
 * @Auther: 黑面阿呆
 * @Date: 2019-12-04 10:25
 * @Version: 1.0.0
 */
public interface ParamFormatCheck {
    /**
     * 字符串
     */
    public static final int String = 0;
    /**
     * 整型
     */
    public static final int Int = 1;
    /**
     * 长整型
     */
    public static final int Long = 2;
    /**
     * 浮点型
     */
    public static final int Float = 3;
    /**
     * 双精度浮点型
     */
    public static final int Double = 4;
    /**
     * 日期类型
     */
    public static final int Date = 5;
    /**
     * 时间类型
     */
    public static final int Time = 6;
    /**
     * 完整时间类型
     */
    public static final int DateTime = 7;
    /**
     * 完整时间类型
     */
    public static final int Email = 8;
    /**
     * 完整时间类型
     */
    public static final int Phone = 9;
}
