package ${code_package}.controller;

import ${code_package}.service.${code_function}Service;
import org.breeze.core.annotation.common.AutoAdd;
import org.breeze.core.annotation.controller.*;
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
 * @description: ${desc_function}管理
 * @author: ${desc_username}
 * @date: ${desc_datetime}
 * @version: 1.0.0
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
     * @param pageSize ${search_doc}
     * @param serial
     * @return
     */
    @Params({
        @Param(name = "page", description = "当前页码"),
        @Param(name = "pageSize", description = "每页数据量")${search_an}
    })
    @Permission(value = "${code_permission}_list")
    @Api(method = RequestMethod.GET)
    public R page(int page, int pageSize${search_param}, Serial serial) {
        DataList dataList = ${code_function_low}Service.getPage(page, pageSize${search}, serial);
        return R.success(dataList);
    }

    /**
     * 获取单条${desc_function}信息
     *
     * @param ${primary_key}
     * @param serial
     * @return
     */
    @Params(
        @Param(name="${primary_key}", required = true)
    )
    @Permission(value = "${code_permission}_list")
    @Api(value = "get", method = RequestMethod.GET)
    public R get(String ${primary_key}, Serial serial) {
        DataList dataList = ${code_function_low}Service.get(${primary_key}, serial);
        return R.success(dataList);
    }

    /**
     * 新增${desc_function}信息
     *
     * @param create
     * @param serial
     * @return
     */
    @Params(
            @Param(name="create", format = ParamFormatCheck.Data, required = true)
    )
    @Permission(value = "${code_permission}_add")
    @Api(method = RequestMethod.POST)
    public R create(Data create, LoginInfo loginInfo, Serial serial) {
        if (${code_function_low}Service.create(create, loginInfo, serial)) {
            return R.success();
        } else {
            return R.failure("${desc_function}保存失败");
        }
    }

    /**
     * 修改${desc_function}信息
     *
     * @param update
     * @param serial
     * @return
     */
    @Params(
            @Param(name="update", format = ParamFormatCheck.Data, required = true)
    )
    @Permission(value = "${code_permission}_edit")
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
    @Permission(value = "${code_permission}_del")
    @Api(method = RequestMethod.DELETE)
    public R delete(String ${primary_key}, Serial serial) {
        int count = ${code_function_low}Service.remove(${primary_key}, serial);
        return R.success(count);
    }
}
