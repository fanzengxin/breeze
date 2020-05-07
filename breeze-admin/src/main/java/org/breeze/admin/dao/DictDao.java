package org.breeze.admin.dao;

import org.breeze.core.annotation.repository.Repository;
import org.breeze.core.annotation.repository.Select;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.constant.OperationMethod;
import org.breeze.core.service.dao.BaseDao;

/**
 * @auther: 黑面阿呆
 * @date: 2020-05-01 17:23
 * @version: 1.0.0
 */
@Repository(tableName = "sys_dict")
public interface DictDao extends BaseDao {

    /**
     * 分页查询字典项列表
     *
     * @param page
     * @param pageSize
     * @param dictCode
     * @param dictDesc
     * @param serial
     * @return
     */
    @Select(sql = "select id, dict_code, dict_value, dict_desc, value_type, dict_parent, sort, remarks, create_time from sys_dict where " +
            "1=1{and dict_parent = #:dictParent:#}{ and dict_code = #:dictCode:#}{ and dict_desc like #:%dictDesc%:#}" +
            "{ and dict_type = #:dictType:#} order by sort", type = OperationMethod.FIND_PAGE)
    DataList page(int page, int pageSize, String dictParent, String dictCode, String dictDesc, String dictType, Serial serial);

    /**
     * 查询所有字典值
     *
     * @param serial
     * @return
     */
    @Select(sql = "select dict_parent, dict_desc, dict_value, value_type from sys_dict where dict_type = 1 order by dict_parent, sort")
    DataList getAllDictCode(Serial serial);
}
