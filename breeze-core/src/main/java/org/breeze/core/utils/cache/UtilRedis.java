package org.breeze.core.utils.cache;

import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.bean.redis.Redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: redis 缓存工具类
 * @Auther: 黑面阿呆
 * @Date: 2019/8/8 15:26
 * @Version: 1.0.0
 */
public class UtilRedis {

    private static final String DEFAULT_REDIS = "default";
    private static final int DEFAULT_DB = 0;
    private static Map<String, Redis> redisMap = new HashMap<String, Redis>();
    private static Redis defaultRedis = new Redis(DEFAULT_REDIS, DEFAULT_DB);

    /**
     * 获取redis对象
     *
     * @return
     */
    public static Redis getRedis() {
        return defaultRedis;
    }

    /**
     * 获取redis对象
     *
     * @param name
     * @return
     */
    public static Redis getRedis(String name) {
        Redis redis = redisMap.get(name + "___" + DEFAULT_REDIS);
        if (redis == null) {
            redis = new Redis(name, DEFAULT_DB);
            redisMap.put(name + "___" + DEFAULT_REDIS, redis);
        }
        return redis;
    }

    /**
     * 获取redis对象
     *
     * @param db
     * @return
     */
    public static Redis getRedis(int db) {
        Redis redis = redisMap.get(DEFAULT_REDIS + "___" + db);
        if (redis == null) {
            redis = new Redis(DEFAULT_REDIS, db);
            redisMap.put(DEFAULT_REDIS + "___" + db, redis);
        }
        return redis;
    }

    /**
     * 获取redis对象
     *
     * @param name
     * @param db
     * @return
     */
    public static Redis getRedis(String name, int db) {
        Redis redis = redisMap.get(name + "___" + db);
        if (redis == null) {
            redis = new Redis(name, db);
            redisMap.put(name + "___" + db, redis);
        }
        return redis;
    }

    /**
     * redis放入string值
     *
     * @param key
     * @param value
     * @param serial
     */
    public static void set(String key, String value, Serial serial) {
        defaultRedis.set(key, value, serial);
    }

    /**
     * 向指定list列表的尾部添加一个值
     *
     * @param key
     * @param value
     * @param serial
     */
    public static void rpush(String key, String value, Serial serial) {
        defaultRedis.rpush(key, value, serial);
    }

    /**
     * 向指定list列表头部添加一个值
     *
     * @param key
     * @param value
     * @param serial
     */
    public static void setList(String key, String value, Serial serial) {
        defaultRedis.setList(key, value, serial);
    }

    /**
     * 获取redis整个列表的数据
     *
     * @param key
     * @param serial
     * @return
     */
    public static List<String> getList(String key, Serial serial) {
        return defaultRedis.getList(key, serial);
    }

    /**
     * 删除，并返回list列表保存在key的最后一个元素
     *
     * @param key
     * @param serial
     */
    public static String getListRpop(String key, Serial serial) {
        return defaultRedis.getListRpop(key, serial);
    }

    /**
     * 向redis放入一个有有效期的数据
     *
     * @param key
     * @param seconds 有效期，秒
     * @param value
     * @param serial
     */
    public static void setex(String key, int seconds, String value, Serial serial) {
        defaultRedis.setex(key, seconds, value, serial);
    }

    /**
     * 获取key的有效期
     *
     * @param key
     * @param serial
     * @return -3 系统错误，-2 不存在，-1 没有设置，大于0的时间是有效期
     */
    public static long ttl(String key, Serial serial) {
        return defaultRedis.ttl(key, serial);
    }

    /**
     * 删除数据字段
     *
     * @param key
     * @param serial
     * @param value
     */
    public static void hdel(String key, Serial serial, String... value) {
        defaultRedis.hdel(key, serial, value);
    }

    /**
     * 设置多键值对
     *
     * @param key
     * @param value
     * @param serial
     */
    public static void mset(String key, Map<String, String> value, Serial serial) {
        defaultRedis.mset(key, value, serial);
    }

    /**
     * 设置多键值对到Hash表中
     *
     * @param key
     * @param value
     * @param serial
     */
    public static void hmset(String key, Map<String, String> value, Serial serial) {
        defaultRedis.hmset(key, value, serial);
    }

    /**
     * 获取hash表中的所有key
     *
     * @param key
     * @param serial
     * @return
     */
    public static Set<String> hkeys(String key, Serial serial) {
        return defaultRedis.hkeys(key, serial);
    }

    /**
     * 获取数据
     *
     * @param key
     * @param serial
     * @return
     */
    public static String get(String key, Serial serial) {
        return defaultRedis.get(key, serial);
    }

    /**
     * 获取Hash表中的值
     *
     * @param key
     * @param fields
     * @param serial
     * @return
     */
    public static List<String> hmget(String key, String fields, Serial serial) {
        return defaultRedis.hmget(key, fields, serial);
    }

    /**
     * 删除数据
     *
     * @param key
     * @param serial
     */
    public static void del(String key, Serial serial) {
        defaultRedis.del(key, serial);
    }

    /**
     * 获取指定前缀的数据集合
     *
     * @param key
     * @param serial
     * @return
     */
    public static Map<String, String> getByPrefix(String key, Serial serial) {
        return defaultRedis.getByPrefix(key, serial);
    }

    /**
     * 根据key模糊查询
     *
     * @param key
     * @param serial
     * @return
     */
    public static Map<String, String> getLikeKeys(String key, Serial serial) {
        return defaultRedis.getLikeKeys(key, serial);
    }

    /**
     * 删除指定前缀数据
     *
     * @param key
     * @param serial
     */
    public static void delByPrefix(String key, Serial serial) {
        defaultRedis.delByPrefix(key, serial);
    }

    /**
     * 插入Data数据
     *
     * @param key
     * @param data
     * @param serial
     */
    public static void setData(String key, Data data, Serial serial) {
        defaultRedis.set(key, data.toJsonStr(), serial);
    }

    /**
     * 插入有有效期的Data数据
     *
     * @param key
     * @param seconds
     * @param data
     * @param serial
     */
    public static void setexData(String key, int seconds, Data data, Serial serial) {
        defaultRedis.setex(key, seconds, data.toJsonStr(), serial);
    }

    /**
     * 获取Data数据
     *
     * @param key
     * @param serial
     * @return
     */
    public static Data getData(String key, Serial serial) {
        return defaultRedis.getData(key, serial);
    }

    /**
     * 插入DataList数据
     *
     * @param key
     * @param dataList
     * @param serial
     */
    public static void setDataList(String key, DataList dataList, Serial serial) {
        defaultRedis.set(key, dataList.toJsonStr(), serial);
    }

    /**
     * 插入具有有效期的dataList数据
     *
     * @param key
     * @param seconds
     * @param dataList
     * @param serial
     */
    public static void setexDataList(String key, int seconds, DataList dataList, Serial serial) {
        defaultRedis.setex(key, seconds, dataList.toJsonStr(), serial);
    }

    /**
     * 获取dataList数据
     *
     * @param key
     * @param serial
     * @return
     */
    public static DataList getDataList(String key, Serial serial) {
        return defaultRedis.getDataList(key, serial);
    }
}
