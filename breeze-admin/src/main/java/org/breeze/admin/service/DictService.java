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
import org.breeze.core.utils.date.UtilDateTime;
import org.breeze.core.utils.string.UtilString;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    /**
     * 数据字典hash表缓存 redis key
     */
    private static final String DICT_CACHE_REDIS_KEY = "DICT_CACHE_REDIS_KEY";

    /**
     * 数据字典总表最新更新时间
     */
    private static final String DICT_CACHE_REDIS_KEY_LAST_UPDATE_TIME = "DICT_CACHE_REDIS_KEY_LAST_UPDATE_TIME";

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
     * 获取数据字典最近更新时间
     *
     * @param serial
     * @return
     */
    public String getLastUpdateTime(Serial serial) {
        return UtilRedis.get(DICT_CACHE_REDIS_KEY_LAST_UPDATE_TIME, serial);
    }

    /**
     * 分页查询字典项列表
     *
     * @param serial
     * @return
     */
    public Data getAllDict(Serial serial) {
        String times = UtilRedis.get(DICT_CACHE_REDIS_KEY_LAST_UPDATE_TIME, serial);
        if (UtilString.isNullOrEmpty(times)) {
            synchronized (DICT_CACHE_REDIS_KEY) {
                times = UtilRedis.get(DICT_CACHE_REDIS_KEY_LAST_UPDATE_TIME, serial);
                if (UtilString.isNullOrEmpty(times)) {
                    loadAllDictData(serial);
                }
            }
        }
        Set<String> keys = UtilRedis.hkeys(DICT_CACHE_REDIS_KEY, serial);
        Data result = new Data();
        for (String key: keys) {
            result.put(key, UtilRedis.hget(DICT_CACHE_REDIS_KEY, key, serial));
        }
        return result;
    }

    /**
     * 查询所有字典项的字典值
     *
     * @param serial
     * @return
     */
    private void loadAllDictData(Serial serial) {
        DataList dataList = dictDao.getAllDictCode(serial);
        Map<String, JSONArray> map = new HashMap<>();
        for (int i = 0; i < dataList.size(); i++) {
            Data data = dataList.getData(i);
            JSONArray ja = map.get(data.getString("DICT_PARENT"));
            if (ja == null) {
                ja = new JSONArray();
                map.put(data.getString("DICT_PARENT"), ja);
            }
            JSONObject json = new JSONObject();
            json.put("label", data.getString("DICT_DESC"));
            if (data.getInt("VALUE_TYPE") == 0) {
                json.put("value", data.getInt("DICT_VALUE"));
            } else {
                json.put("value", data.getString("DICT_VALUE"));
            }
            ja.add(json);
        }
        Map<String, String> resultMap = new HashMap<>();
        for (Map.Entry<String, JSONArray> entry : map.entrySet()) {
            resultMap.put(entry.getKey(), entry.getValue().toJSONString());
        }
        UtilRedis.hmset(DICT_CACHE_REDIS_KEY, resultMap, serial);
        UtilRedis.set(DICT_CACHE_REDIS_KEY_LAST_UPDATE_TIME,
                String.valueOf(UtilDateTime.currentTimeMillis()), serial);
    }

    /**
     * 获取数据字典
     *
     * @param dictCode
     * @param serial
     * @return
     */
    public String getDicts(String dictCode, Serial serial) {
        String times = UtilRedis.get(DICT_CACHE_REDIS_KEY_LAST_UPDATE_TIME, serial);
        if (UtilString.isNullOrEmpty(times)) {
            synchronized (DICT_CACHE_REDIS_KEY) {
                times = UtilRedis.get(DICT_CACHE_REDIS_KEY_LAST_UPDATE_TIME, serial);
                if (UtilString.isNullOrEmpty(times)) {
                    loadAllDictData(serial);
                }
            }
        }
        String dicts = UtilRedis.hget(DICT_CACHE_REDIS_KEY, dictCode, serial);
        return "{\"data\":" + dicts + "}";
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
        UtilRedis.del(DICT_CACHE_REDIS_KEY_LAST_UPDATE_TIME, serial);
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
        UtilRedis.del(DICT_CACHE_REDIS_KEY_LAST_UPDATE_TIME, serial);
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
        UtilRedis.del(DICT_CACHE_REDIS_KEY_LAST_UPDATE_TIME, serial);
        Data remove = new Data();
        remove.add("id", id, true);
        return dictDao.remove(remove, serial);
    }
}
