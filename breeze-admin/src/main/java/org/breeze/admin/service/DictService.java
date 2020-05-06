package org.breeze.admin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.breeze.admin.dao.DictDao;
import org.breeze.core.annotation.common.AutoAdd;
import org.breeze.core.annotation.service.Service;
import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.bean.login.LoginInfo;
import org.breeze.core.utils.cache.UtilRedis;
import org.breeze.core.utils.string.UtilString;

/**
 * @description: 数据字典
 * @auther: 黑面阿呆
 * @date: 2020-05-01 17:22
 * @version: 1.0.0
 */
@Service
public class DictService {

    @AutoAdd
    private DictDao dictDao;

    private static final String DICT_CACHE_REDIS_KEY = "DICT_CACHE_REDIS_KEY_";

    /**
     * 分页查询字典项列表
     *
     * @param page
     * @param pageSize
     * @param dictParent
     * @param dictCode
     * @param dictDesc
     * @param dictType
     * @param serial
     * @return
     */
    public DataList page(int page, int pageSize, String dictParent, String dictCode, String dictDesc, String dictType, Serial serial) {
        return dictDao.page(page, pageSize, dictParent, dictCode, dictDesc, dictType, serial);
    }

    /**
     * 获取数据字典
     *
     * @param dictCode
     * @param serial
     * @return
     */
    public String getDicts(String dictCode, Serial serial) {
        String dicts = UtilRedis.getRedis().get(DICT_CACHE_REDIS_KEY + dictCode, serial);
        if (UtilString.isNullOrEmpty(dicts)) {
            synchronized (DICT_CACHE_REDIS_KEY) {
                dicts = UtilRedis.getRedis().get(DICT_CACHE_REDIS_KEY + dictCode, serial);
                if (UtilString.isNullOrEmpty(dicts)) {
                    DataList dictList = dictDao.page(dictCode, serial);
                    JSONArray ja = new JSONArray();
                    for (int i = 0; i < dictList.size(); i++) {
                        Data data = dictList.getData(i);
                        JSONObject json = new JSONObject();
                        json.put("label", data.getString("DICT_DESC"));
                        json.put("value", data.getString("DICT_VALUE"));
                        ja.add(json);
                    }
                    dicts = ja.toJSONString();
                    UtilRedis.getRedis().set(DICT_CACHE_REDIS_KEY + dictCode, dicts, serial);
                }
            }
        }
        return dicts;
    }

    /**
     * 保存字典数据
     *
     * @param loginInfo
     * @param data
     * @param serial
     * @return
     */
    public boolean create(LoginInfo loginInfo, Data data, Serial serial) {
        if (UtilString.isNotEmpty(data.getString("DICT_PARENT"))) {
            UtilRedis.getRedis().del(DICT_CACHE_REDIS_KEY + data.getString("DICT_PARENT"), serial);
        }
        data.add("create_id", loginInfo.getUid());
        return dictDao.save(data, serial);
    }

    /**
     * 更新字典数据
     *
     * @param loginInfo
     * @param data
     * @param serial
     * @return
     */
    public boolean update(LoginInfo loginInfo, Data data, Serial serial) {
        if (UtilString.isNotEmpty(data.getString("DICT_PARENT"))) {
            UtilRedis.getRedis().del(DICT_CACHE_REDIS_KEY + data.getString("DICT_PARENT"), serial);
        }
        data.setPrimaryKey("ID");
        data.add("update_id", loginInfo.getUid());
        return dictDao.update(data, serial);
    }

    /**
     * 删除字典数据
     *
     * @param id
     * @param serial
     * @return
     */
    public int delete(String id, Serial serial) {
        Data remove = new Data();
        remove.add("id", id, true);
        DataList dl = dictDao.find(remove, serial);
        if (dl != null && dl.size() > 0 && UtilString.isNotEmpty(dl.getData(0).getString("DICT_PARENT"))) {
            UtilRedis.getRedis().del(DICT_CACHE_REDIS_KEY + dl.getData(0).getString("DICT_PARENT"), serial);
        }
        return dictDao.remove(remove, serial);
    }
}
