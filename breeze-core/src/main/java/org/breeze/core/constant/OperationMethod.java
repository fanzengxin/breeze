package org.breeze.core.constant;

/**
 * @Description: 数据库数据操作类型
 * @Auther: 黑面阿呆
 * @Date: 2019/8/14 15:44
 * @Version: 1.0.0
 */
public interface OperationMethod {
    /**
     * 查询
     */
    public static final int FIND = 11;
    /**
     * 分页查询
     */
    public static final int FIND_PAGE = 12;
    /**
     * 分页查询不查总数
     */
    public static final int FIND_PAGE_NOCOUNT = 13;
    /**
     * 根据Data进行条件查询
     */
    public static final int FIND_BY_DATA = 14;
    /**
     * 保存数据
     */
    public static final int SAVE = 21;
    /**
     * 批量保存数据
     */
    public static final int BATCH_SAVE = 22;
    /**
     * 更新数据
     */
    public static final int UPDATE = 31;
    /**
     * 批量更新数据
     */
    public static final int BATCH_UPDATE = 32;
    /**
     * 删除数据
     */
    public static final int REMOVE = 41;
    /**
     * 批量删除数据
     */
    public static final int BATCH_REMOVE = 42;
}
