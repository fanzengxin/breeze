package org.breeze.core.service.dao;

import org.breeze.core.annotation.repository.Select;
import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.constant.OperationMethod;

/**
 * @Description: 数据库操作通用方法
 * @Auther: 黑面阿呆
 * @Date: 2019/8/10 10:47
 * @Version: 1.0.0
 */
public interface BaseDao {

    /**
     * 根据条件查询
     *
     * @param find
     * @return
     */
    @Select(type = OperationMethod.FIND_BY_DATA)
    DataList find(Data find);

    /**
     * 批量保存
     *
     * @param dataList
     * @return
     */
    @Select(type = OperationMethod.BATCH_SAVE)
    int batchSave(DataList dataList);

    /**
     * 单条保存
     *
     * @param data
     * @return
     */
    @Select(type = OperationMethod.SAVE)
    boolean save(Data data);

    /**
     * 批量修改
     *
     * @param dataList
     * @return
     */
    @Select(type = OperationMethod.BATCH_UPDATE)
    int batchUpdate(DataList dataList);

    /**
     * 单条修改
     *
     * @param data
     * @return
     */
    @Select(type = OperationMethod.UPDATE)
    boolean update(Data data);

    /**
     * 批量删除
     *
     * @param dataList
     * @return
     */
    @Select(type = OperationMethod.BATCH_REMOVE)
    int batchRemve(DataList dataList);

    /**
     * 单条删除
     *
     * @param data
     * @return
     */
    @Select(type = OperationMethod.REMOVE)
    int remove(Data data);
}
