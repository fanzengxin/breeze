package ${code_package}.controller;

import ${code_package}.service.${code_function}Service;
import org.breeze.core.annotation.common.AutoAdd;
import org.breeze.core.bean.api.R;
import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.bean.login.LoginInfo;
import org.breeze.core.constant.ParamFormatCheck;
import org.breeze.core.constant.RequestMethod;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;

/**
 * @Description: ${desc_function}管理
 * @Auther: ${desc_username}
 * @Date: ${desc_datetime}
 * @Version: 1.0.0
 */
@Controller(mapper = "${code_function_low}")
public class ${code_function}Controller {

    private static Log log = LogFactory.getLog(${code_function}Controller.class);

    @AutoAdd
    private ${code_function}Service ${code_function_low}Service;

    /**
     * 分页查询${desc_function}列表
     *
     * @param page
     * @param pageSize
     * @param serial
     * @return
     */
    @Permission(value = "${code_permission}_page")
    @Api(method = RequestMethod.GET)
    public R page(int page, int pageSize, Serial serial) {
        DataList dataList = ${code_function_low}Service.getPage(page, pageSize, serial);
        return R.success(dataList);
    }

    /**
     * 新增${desc_function}信息
     *
     * @param data
     * @param serial
     * @return
     */
    @Params(
            @Param(name="data", format = ParamFormatCheck.Data, required = true)
    )
    @Permission(value = "${code_permission}_create")
    @Api(method = RequestMethod.POST)
    public R create(Data data, LoginInfo loginInfo, Serial serial) {
        if (${code_function_low}Service.create(create, loginInfo, serial)) {
            return R.success();
        } else {
            return R.failure("${desc_function}保存失败");
        }
    }

    /**
     * 修改${desc_function}信息
     *
     * @param data
     * @param serial
     * @return
     */
    @Params(
            @Param(name="update", format = ParamFormatCheck.Data, required = true)
    )
    @Permission(value = "${code_permission}_update")
    @Api(method = RequestMethod.PUT)
    public R update(Data update, LoginInfo loginInfo, Serial serial) {
        if (${code_function_low}Service.update(update, loginInfo, serial)) {
            return R.success();
        } else {
            return R.failure("${desc_function}修改失败");
        }
    }

    /**
     * 删除${desc_function}信息
     *
     * @param id
     * @param serial
     * @return
     */
    @Params(
            @Param(name="${primary_key}", required = true)
    )
    @Permission(value = "${code_permission}_delete")
    @Api(method = RequestMethod.DELETE)
    public R delete(String ${primary_key}, Serial serial) {
        int count = ${code_function_low}Service.remove(${primary_key}, serial);
        return R.success(count);
    }
}
