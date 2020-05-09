package org.breeze.admin.controller;

import org.breeze.admin.service.TestService;
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
 * @description: 测试管理
 * @author: 系统管理员
 * @date: 2020-05-09 18:14
 * @version: 1.0.0
 */
@Controller(mapper = "test")
public class TestController {

    private static Log log = LogFactory.getLog(TestController.class);

    @AutoAdd
    private TestService testService;

    /**
     * 分页查询测试列表
     *
     * @param page
     * @param pageSize 
	 * @nama		姓名
	 * @gender		性别
	 * @age		年龄
     * @param serial
     * @return
     */
    @Params({
        @Param(name = "page", description = "当前页码"),
        @Param(name = "pageSize", description = "每页数据量"),
		@Param(name = "nama", description = "姓名"),
		@Param(name = "gender", description = "性别"),
		@Param(name = "age", description = "年龄")
    })
    @Permission(value = "test_list")
    @Api(method = RequestMethod.GET)
    public R page(int page, int pageSize, String nama, String gender, String age, Serial serial) {
        DataList dataList = testService.getPage(page, pageSize, nama, gender, age, serial);
        return R.success(dataList);
    }

    /**
     * 获取单条测试信息
     *
     * @param id
     * @param serial
     * @return
     */
    @Params(
        @Param(name="id", required = true)
    )
    @Permission(value = "test_list")
    @Api(value = "get", method = RequestMethod.GET)
    public R get(String id, Serial serial) {
        DataList dataList = testService.get(id, serial);
        return R.success(dataList);
    }

    /**
     * 新增测试信息
     *
     * @param create
     * @param serial
     * @return
     */
    @Params(
            @Param(name="create", format = ParamFormatCheck.Data, required = true)
    )
    @Permission(value = "test_add")
    @Api(method = RequestMethod.POST)
    public R create(Data create, LoginInfo loginInfo, Serial serial) {
        if (testService.create(create, loginInfo, serial)) {
            return R.success();
        } else {
            return R.failure("测试保存失败");
        }
    }

    /**
     * 修改测试信息
     *
     * @param update
     * @param serial
     * @return
     */
    @Params(
            @Param(name="update", format = ParamFormatCheck.Data, required = true)
    )
    @Permission(value = "test_edit")
    @Api(method = RequestMethod.PUT)
    public R update(Data update, LoginInfo loginInfo, Serial serial) {
        if (testService.update(update, loginInfo, serial)) {
            return R.success();
        } else {
            return R.failure("测试修改失败");
        }
    }

    /**
     * 删除测试信息
     *
     * @param id
     * @param serial
     * @return
     */
    @Params(
            @Param(name="id", required = true)
    )
    @Permission(value = "test_del")
    @Api(method = RequestMethod.DELETE)
    public R delete(String id, Serial serial) {
        int count = testService.remove(id, serial);
        return R.success(count);
    }
}
