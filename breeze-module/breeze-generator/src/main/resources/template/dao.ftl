package ${code_package}.dao;

import org.breeze.core.annotation.repository.Repository;
import org.breeze.core.annotation.repository.Select;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.constant.OperationMethod;
import org.breeze.core.service.dao.BaseDao;

/**
 * @Description: ${desc_function}
 * @Auther: ${desc_username}
 * @Date: ${desc_datetime}
 * @Version: 1.0.0
 */
@Repository(tableName = "${code_table_name}")
public interface ${code_function}Dao extends BaseDao {

    /**
     * 分页查询${desc_function}列表
     *
     * @param page
     * @param pageSize
     * @param serial
     * @return
     */
    @Select(sql = "select * from breeze.${code_table_name} where 1=1", type = OperationMethod.FIND_PAGE)
    DataList getPage(int page, int pageSize, Serial serial);
}
