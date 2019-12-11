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
    public static final int FIND = 0;
    /**
     * 分页查询
     */
    public static final int FIND_PAGE = 1;
    /**
     * 分页查询不查总数
     */
    public static final int FIND_PAGE_NOCOUNT = 2;
    /**
     * 保存数据
     */
    public static final int SAVE = 3;
    /**
     * 批量保存数据
     */
    public static final int BATCH_SAVE = 4;
    /**
     * 更新数据
     */
    public static final int UPDATE = 5;
    /**
     * 批量更新数据
     */
    public static final int BATCH_UPDATE = 6;
    /**
     * 删除数据
     */
    public static final int REMOVE = 7;
    /**
     * 批量删除数据
     */
    public static final int BATCH_REMOVE = 8;
}
