package org.breeze.admin.dao;

import org.breeze.core.annotation.repository.Repository;
import org.breeze.core.annotation.repository.Select;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.constant.OperationMethod;
import org.breeze.core.service.dao.BaseDao;

/**
 * @description: 测试
 * @author: 系统管理员
 * @date: 2020-05-11 17:14
 * @version: 1.0.0
 */
@Repository(tableName = "breeze.test_code")
public interface TestDao extends BaseDao {

    /**
     * 分页查询测试列表
     *
     * @param page
     * @param pageSize
     * @param serial
     * @return
     */
    @Select(sql = "select id, nama, gender, age, create_time from breeze.test_code where 1=1{ and nama = #:nama:#}{ and gender = #:gender:#}", type = OperationMethod.FIND_PAGE)
    DataList getPage(int page, int pageSize, String nama, String gender, Serial serial);
}
