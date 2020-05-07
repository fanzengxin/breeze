package org.breeze.admin.controller;

import org.breeze.admin.service.DictService;
import org.breeze.core.annotation.common.AutoAdd;
import org.breeze.core.annotation.controller.*;
import org.breeze.core.bean.api.R;
import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.bean.login.LoginInfo;
import org.breeze.core.constant.RequestMethod;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;
import org.breeze.core.utils.cache.UtilRedis;

/**
 * @description: 数据字典
 * @auther: 黑面阿呆
 * @date: 2020-05-01 17:21
 * @version: 1.0.0
 */
@Controller(mapper = "/dict")
public class DictController {

    private static Log log = LogFactory.getLog(DictController.class);

    @AutoAdd
    private DictService dictService;

    /**
     * 分页查询字典项列表
     *
     * @param page
     * @param pageSize
     * @param dictCode
     * @param dictDesc
     * @param dictType
     * @param serial
     * @return
     */
    @Permission("sys_dict_list")
    @Api(value = "page", method = RequestMethod.GET)
    public R page(int page, int pageSize, String dictParent, String dictCode, String dictDesc, String dictType, Serial serial) {
        DataList dataList = dictService.page(page, pageSize, dictParent, dictCode, dictDesc, dictType, serial);
        return R.success(dataList);
    }

    /**
     * 分页查询字典项列表
     *
     * @param dictCode
     * @param serial
     * @return
     */
    @Api(value = "/all", method = RequestMethod.GET)
    public R getAllDicts(String dictCode, Serial serial) {
        Data dicts = dictService.getAllDict(serial);
        return R.success(dicts);
    }

    /**
     * 获取数据字典最近更新时间
     *
     * @param serial
     * @return
     */
    @Api(value = "/check", method = RequestMethod.GET)
    public R checkUpdate(Serial serial) {
        String times = dictService.getLastUpdateTime(serial);
        return R.success(times);
    }

    /**
     * 分页查询字典项列表
     *
     * @param dictCode
     * @param serial
     * @return
     */
    @Params({
            @Param(name = "dictCode", description = "字典项", required = true)
    })
    @Permission("sys_dict_list")
    @Api(method = RequestMethod.GET)
    public R getDicts(String dictCode, Serial serial) {
        String dicts = dictService.getDicts(dictCode, serial);
        return R.successStr(dicts);
    }

    /**
     * 新增部门信息
     *
     * @param loginInfo 登录用户
     * @param data      字典信息
     * @param serial    日志序列
     * @return
     */
    @Permission("sys_dict_add")
    @Api(method = RequestMethod.POST)
    public R create(LoginInfo loginInfo, Data data, Serial serial) {
        if (dictService.create(loginInfo, data, serial)) {
            return R.success();
        } else {
            return R.failure("数据保存失败");
        }
    }

    /**
     * 更新菜单信息
     *
     * @param loginInfo 登录用户
     * @param data      菜单数据
     * @param serial    日志序列
     * @return
     */
    @Permission("sys_dict_edit")
    @Api(method = RequestMethod.PUT)
    public R update(LoginInfo loginInfo, Data data, Serial serial) {
        if (dictService.update(loginInfo, data, serial)) {
            return R.success();
        } else {
            return R.failure("数据修改失败");
        }
    }

    /**
     * 删除菜单信息
     *
     * @param id     菜单数据
     * @param serial 日志序列
     * @return
     */
    @Params({
            @Param(name = "id", description = "菜单id", required = true)
    })
    @Permission("sys_dict_del")
    @Api(method = RequestMethod.DELETE)
    public R delete(String id, Serial serial) {
        if (dictService.delete(id, serial) > 0) {
            return R.success();
        } else {
            return R.failure("数据修改失败");
        }
    }
}
