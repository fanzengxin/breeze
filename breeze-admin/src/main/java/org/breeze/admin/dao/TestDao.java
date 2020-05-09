package org.breeze.admin.dao;

import org.breeze.core.annotation.repository.Repository;
import org.breeze.core.annotation.repository.Select;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.constant.OperationMethod;
import org.breeze.core.service.dao.BaseDao;

/**
 * @Description: 测试
 * @Auther: 系统管理员
 * @Date: 2020-05-09 18:14
 * @Version: 1.0.0
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
    @Select(sql = "select nama, gender, age from breeze.breeze.test_code where 1=1{ and nama = #:nama:#}{ and gender = #:gender:#}{ and age = #:age:#}", type = OperationMethod.FIND_PAGE)
    DataList getPage(int page, int pageSize, String nama, String gender, String age, Serial serial);
}
