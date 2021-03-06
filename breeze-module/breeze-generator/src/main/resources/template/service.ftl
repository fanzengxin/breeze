package ${code_package}.service;

import ${code_package}.dao.${code_function}Dao;
import org.breeze.core.annotation.common.AutoAdd;
import org.breeze.core.annotation.service.Service;
import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.bean.login.LoginInfo;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;

/**
 * @description: ${desc_function}
 * @author: ${desc_username}
 * @date: ${desc_datetime}
 * @version: 1.0.0
 */
@Service
public class ${code_function}Service {

    private static final Log log = LogFactory.getLog(${code_function}Service.class);

    @AutoAdd
    private ${code_function}Dao ${code_function_low}Dao;

    /**
     * 分页查询${desc_function}列表
     *
     * @param page
     * @param pageSize ${search_doc}
     * @param serial
     * @return
     */
    public DataList getPage(int page, int pageSize${search_param}, Serial serial) {
        return ${code_function_low}Dao.getPage(page, pageSize${search}, serial);
    }

    /**
     * 获取单条${desc_function}信息
     *
     * @param ${primary_key}
     * @param serial
     * @return
     */
    public DataList get(String ${primary_key}, Serial serial) {
        Data find = new Data();
        find.add("${primary_key}", ${primary_key}, true);
        return ${code_function_low}Dao.find(find, serial);
    }

    /**
     * 新增${desc_function}
     *
     * @param create
     * @param loginInfo
     * @param serial
     * @return
     */
    public boolean create(Data create, LoginInfo loginInfo, Serial serial) {
        create.add("create_id", loginInfo.getUid());
        return ${code_function_low}Dao.save(create, serial);
    }

    /**
     * 修改${desc_function}
     *
     * @param update
     * @param loginInfo
     * @param serial
     * @return
     */
    public boolean update(Data update, LoginInfo loginInfo, Serial serial) {
        update.setPrimaryKey("${primary_key}");
        update.add("update_id", loginInfo.getUid());
        return ${code_function_low}Dao.update(update, serial);
    }

    /**
     * 删除${desc_function}
     *
     * @param ${primary_key}
     * @param serial
     * @return
     */
    public int remove(String ${primary_key}, Serial serial) {
        Data remove = new Data();
        remove.add("${primary_key}", ${primary_key}, true);
        return ${code_function_low}Dao.remove(remove, serial);
    }
}
