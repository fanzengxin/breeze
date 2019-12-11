package org.breeze.core.bean.data;

/**
 * @Description:
 * @Auther: 黑面阿呆
 * @Date: 2019/2/21 19:53
 * @Version: 1.0.0
 */
public class DataInfo {

    /**
     * 这是标题属性时，在备注后面增加这个
     */
    public static final String TITLE_CHAR = "^_^";
    /**
     * 是标题属性且不需要校验时增加这个
     */
    public static final String TITLE_CHAR_AND_NOT_CHECK = "^=^";
    /**
     * 不需要校验时增加这个
     */
    public static final String TITLE_NOT_CHECK = "^@^";
    //显示的名称（备注）
    private String remarks;
    //字段类型代码
    private int columnType;
    //字段类型的名字
    private String columnTypeName;
    //长度
    private int precision;
    //小数点位数
    private int scale;
    //需要检查的数据长度，单位字节
    private int length;
    private boolean notNull;

    /**
     * 是否为空，true 不能为空
     *
     * @return
     */
    public boolean isNotNull() {
        return notNull;
    }

    /**
     * 设置不能为空
     *
     * @param notNull true 不为空
     */
    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    /**
     * 是否是行标题
     *
     * @return
     */
    public boolean isTitle() {
        return (remarks.endsWith(TITLE_CHAR) || remarks.endsWith(TITLE_CHAR_AND_NOT_CHECK));
    }

    /**
     * 不需要验证
     *
     * @return
     */
    public boolean isNotCheck() {
        return (remarks.endsWith(TITLE_NOT_CHECK) || remarks.endsWith(TITLE_CHAR_AND_NOT_CHECK));
    }

    /**
     * 得到备注
     *
     * @return
     */

    public String getRemarks() {
        if (remarks == null) {
            remarks = "";
        }
        if (remarks.endsWith(TITLE_CHAR)) {
            return remarks.substring(0, remarks.length() - TITLE_CHAR.length());
        }
        if (remarks.endsWith(TITLE_NOT_CHECK)) {
            return remarks.substring(0, remarks.length() - TITLE_NOT_CHECK.length());
        }
        if (remarks.endsWith(TITLE_CHAR_AND_NOT_CHECK)) {
            return remarks.substring(0, remarks.length() - TITLE_CHAR_AND_NOT_CHECK.length());
        }
        return remarks;
    }

    /**
     * 设置备注
     *
     * @param remarks
     */
    public void setRemarks(String remarks) {
        if (remarks == null) {
            remarks = "";
        }
        this.remarks = remarks.trim();
    }

    /**
     * 得到字段类型
     *
     * @return
     */
    public int getColumnType() {
        return columnType;
    }

    /**
     * 设置字段类型
     *
     * @param columnType
     */
    public void setColumnType(int columnType) {
        this.columnType = columnType;
    }

    /**
     * 得到字段类型对应的名字
     *
     * @return
     */
    public String getColumnTypeName() {
        return columnTypeName;
    }

    /**
     * 设置字段类型对应的名字
     *
     * @param columnTypeName
     */
    public void setColumnTypeName(String columnTypeName) {
        this.columnTypeName = columnTypeName;
    }

    /**
     * 得到字段长度
     *
     * @return
     */
    public int getPrecision() {
        return precision;
    }

    /**
     * 设置字段长度
     *
     * @param precision
     */
    public void setPrecision(int precision) {
        this.precision = precision;
    }

    /**
     * 得到小数位数
     *
     * @return
     */
    public int getScale() {
        return scale;
    }

    /**
     * 设置小数位数
     *
     * @param scale
     */
    public void setScale(int scale) {
        this.scale = scale;
    }

    /**
     * 得到验证的长度
     *
     * @return
     */
    public int getLength() {
        return length;
    }

    /**
     * 设置验证的长度
     *
     * @param length
     */
    public void setLength(int length) {
        this.length = length;
    }
}
