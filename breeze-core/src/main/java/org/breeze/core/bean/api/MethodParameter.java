package org.breeze.core.bean.api;

import org.breeze.core.constant.ParamFormatCheck;

/**
 * @Description: 请求参数实体类
 * @Auther: 黑面阿呆
 * @Date: 2019-12-02 14:41
 * @Version: 1.0.0
 */
public class MethodParameter {

    // 参数名
    private String name;
    // 参数描述
    private String description;
    // 参数类名
    private Class<?> clazz;
    // 是否开启验证
    private boolean isCheck =  false;
    // 是否必填
    private boolean required = false;
    // 参数格式验证
    private int format = ParamFormatCheck.String;
    // 默认值
    private String defaultValue;
    // 最大长度
    private Integer maxLength;
    // 最小长度
    private Integer minLength;
    // 最大值
    private Long maxValue;
    // 最小值
    private Long minValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Long maxValue) {
        this.maxValue = maxValue;
    }

    public Long getMinValue() {
        return minValue;
    }

    public void setMinValue(Long minValue) {
        this.minValue = minValue;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }
}
