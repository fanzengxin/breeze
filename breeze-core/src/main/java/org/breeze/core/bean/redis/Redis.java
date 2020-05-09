package org.breeze.core.bean.redis;

import com.alibaba.fastjson.JSONObject;
import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.config.CommonConfig;
import org.breeze.core.config.RedisConfig;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;
import org.breeze.core.utils.date.UtilDateTime;
import org.breeze.core.utils.encry.Des3;
import org.breeze.core.utils.string.UtilString;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: redis实体对象
 * @Auther: 黑面阿呆
 * @Date: 2019/8/8 15:50
 * @Version: 1.0.0
 */
public class Redis {

    private static Log log = LogFactory.getLog(Redis.class);

    // 系统默认配置文件名称
    public static final int REDIS_DEFAULT_DB = 0;
    public static final int REDIS_SYS_DB = 1;
    public static final int REDIS_LOGIN_DB = 2;

    private static Map<String, JedisPool> redisInfos = new ConcurrentHashMap();
    private static Map<String, JSONObject> redisConfig = new ConcurrentHashMap();
    private String name = null;
    private int db = 0;

    public Redis() {
        this.name = RedisConfig.REDIS_DEFAULT_CONFIG;
        this.db = REDIS_DEFAULT_DB;
    }

    public Redis(String name) {
        this.name = name;
        this.db = REDIS_DEFAULT_DB;
    }

    public Redis(int db) {
        this.name = RedisConfig.REDIS_DEFAULT_CONFIG;
        this.db = db;
    }

    public Redis(String name, int db) {
        this.name = name;
        this.db = db;
    }

    /**
     * 获取redis配置
     *
     * @return
     */
    public JSONObject getRedisConfig(String dbName) {
        JSONObject json = redisConfig.get(dbName);
        if (json == null) {
            synchronized (name) {
                json = redisConfig.get(name);
                if (json == null) {
                    try {
                        Map<String, String> p = RedisConfig.initConfig(dbName);
                        String host = p.get("host");
                        int port = Integer.parseInt(p.get("port"), 10);
                        String auth = p.get("password");
                        //解密
                        int maxActive = Integer.parseInt(p.get("max_active"), 10);
                        int maxIdle = Integer.parseInt(p.get("max_idle"), 10);
                        int maxWait = Integer.parseInt(p.get("max_wait"), 10);
                        int timeOut = Integer.parseInt(p.get("timeout"), 10);
                        json = new JSONObject();
                        json.put("redis.host", host);
                        json.put("redis.port", port);
                        json.put("redis.db", db);
                        json.put("redis.password", auth);
                        json.put("redis.max_active", maxActive);
                        json.put("redis.max_idle", maxIdle);
                        json.put("redis.max_wait", maxWait);
                        json.put("redis.timeout", timeOut);
                        redisConfig.put(name, json);
                        return json;
                    } catch (Exception e1) {
                        log.logError("读取redis连接失败", e1, "sys/redis:" + UtilDateTime.currentTimeMillis());
                        throw new RuntimeException("读取redis连接失败，配置信息：redis_" + name);
                    }
                } else {
                    return json;
                }
            }
        } else {
            return json;
        }
    }

    /**
     * 获取jedis实体对象
     *
     * @param serial
     * @return
     */
    private Jedis getJedis(Serial serial) {
        String key = name + "___" + db;
        JedisPool jedisPool = redisInfos.get(key);
        if (jedisPool == null) {
            synchronized (key) {
                if (jedisPool == null) {
                    try {
                        JSONObject data = getRedisConfig(name);
                        String host = data.getString("redis.host");
                        int port = data.getIntValue("redis.port");
                        int db = this.db;
                        String auth = data.getString("redis.password");
                        //解密
                        if (UtilString.isNullOrEmpty(auth)) {
                            auth = null;
                        } else {
                            auth = Des3.decode(auth, CommonConfig.getSecretKey());
                        }
                        int maxActive = data.getIntValue("redis.max_active");
                        int maxIdle = data.getIntValue("redis.max_idle");
                        int maxWait = data.getIntValue("redis.max_wait");
                        int timeOut = data.getIntValue("redis.timeout");
                        JedisPoolConfig config = new JedisPoolConfig();
                        config.setMaxTotal(maxActive);
                        config.setMaxIdle(maxIdle);
                        config.setMaxWaitMillis(maxWait);
                        config.setTestOnBorrow(true);
                        log.logInfo("连接Redis：{}={}:{}/{}", serial, name, host, port, db);
                        try {
                            jedisPool = new JedisPool(config, host, port, timeOut, auth, db);
                            redisInfos.put(name, jedisPool);
                        } catch (Exception e) {
                            throw new RuntimeException("初始化JedisPool失败，配置信息：redis_" + name, e);
                        }
                    } catch (Exception e1) {
                        log.logError("连接Redis：初始化JedisPool失败失败", e1, serial);
                        throw new RuntimeException("初始化JedisPool失败，配置信息：redis_" + name, e1);
                    }
                }
            }
        }
        if (jedisPool != null) {
            synchronized (key) {
                Jedis resource = jedisPool.getResource();
                return resource;
            }
        } else {
            return null;
        }
    }

    /**
     * redis放入string值
     *
     * @param key
     * @param value
     * @param serial
     */
    public void set(String key, String value, Serial serial) {
        log.logDebug("redis插入数据:{}={}", serial, key, value);
        Jedis jedis = null;
        try {
            jedis = getJedis(serial);
            jedis.set(key, value);
        } catch (Exception e) {
            log.logError("redis:set操作出错！", e, serial);
        } finally {
            if (jedis != null) {
                returnResource(jedis);
            }
        }
    }

    /**
     * 向指定list列表的尾部添加一个值
     *
     * @param key
     * @param value
     * @param serial
     */
    public void rpush(String key, String value, Serial serial) {
        log.logDebug("redis列表{}尾部插入数据:{}", serial, key, value);
        Jedis jedis = null;
        try {
            jedis = getJedis(serial);
            jedis.rpush(key, value);
        } catch (Exception e) {
            log.logError("redis:list操作出错！", e, serial);
        } finally {
            if (jedis != null) {
                returnResource(jedis);
            }
        }
    }

    /**
     * 向指定list列表头部添加一个值
     *
     * @param key
     * @param value
     * @param serial
     */
    public void setList(String key, String value, Serial serial) {
        log.logDebug("redis列表{}头部插入数据:{}", serial, key, value);
        Jedis jedis = null;
        try {
            jedis = getJedis(serial);
            jedis.lpush(key, value);
        } catch (Exception e) {
            log.logError("redis:list操作出错！", e, serial);
        } finally {
            if (jedis != null) {
                returnResource(jedis);
            }
        }
    }

    /**
     * 获取redis整个列表的数据
     *
     * @param key
     * @param serial
     * @return
     */
    public List<String> getList(String key, Serial serial) {
        Jedis jedis = null;
        try {
            jedis = getJedis(serial);
            List<String> lists = jedis.lrange(key, 0, -1);
            log.logDebug("redis列表{}获取数据", serial, key);
            if (lists != null && lists.size() > 0) {
                return lists;
            }
        } catch (Exception e) {
            log.logError("redis:list操作出错！", e, serial);
        } finally {
            if (jedis != null) {
                returnResource(jedis);
            }
        }
        return null;

    }

    /**
     * 删除，并返回list列表保存在key的最后一个元素
     *
     * @param key
     * @param serial
     */
    public String getListRpop(String key, Serial serial) {
        Jedis jedis = null;
        try {
            jedis = getJedis(serial);
            String val = jedis.rpop(key);
            log.logDebug("redis列表{}尾部获取并删除数据:{}", serial, key);
            return val;
        } catch (Exception e) {
            log.logError("redis:list操作出错！", e, serial);
        } finally {
            if (jedis != null) {
                returnResource(jedis);
            }
        }
        return null;
    }

    /**
     * 向redis放入一个有有效期的数据
     *
     * @param key
     * @param seconds 有效期，秒
     * @param value
     * @param serial
     */
    public void setex(String key, int seconds, String value, Serial serial) {
        log.logDebug("redis插入临时数据:{}={}", serial, key, value);
        Jedis jedis = null;
        try {
            jedis = getJedis(serial);
            jedis.setex(key, seconds, value);
        } catch (Exception e) {
            log.logError("redis:set操作出错！", e, serial);
        } finally {
            if (jedis != null) {
                returnResource(jedis);
            }
        }
    }

    /**
     * 获取key的有效期
     *
     * @param key
     * @param serial
     * @return -3 系统错误，-2 不存在，-1 没有设置，大于0的时间是有效期
     */
    public long ttl(String key, Serial serial) {
        log.logDebug("redis获取数据有效期:{}", serial, key);
        Jedis jedis = null;
        try {
            jedis = getJedis(serial);
            long t = jedis.ttl(key);
            log.logInfo("TTL:" + key + "[" + t + "]");
            return t;
        } catch (Exception e) {
            log.logError("redis:ttl操作出错！", e, serial);
        } finally {
            if (jedis != null) {
                returnResource(jedis);
            }
        }
        return -3;
    }

    /**
     * 删除数据字段
     *
     * @param key
     * @param serial
     * @param value
     */
    public void hdel(String key, Serial serial, String... value) {
        log.logDebug("redis删除数据{}字段", serial, key);
        Jedis jedis = null;
        try {
            jedis = getJedis(serial);
            jedis.hdel(key, value);
        } catch (Exception e) {
            log.logError("redis:set操作出错！", e, serial);
        } finally {
            if (jedis != null) {
                returnResource(jedis);
            }
        }
    }

    /**
     * 设置多键值对
     *
     * @param key
     * @param value
     * @param serial
     */
    public void hmset(String key, Map<String, String> value, Serial serial) {
        log.logDebug("redis插入多键值对数据{}", serial, key);
        Jedis jedis = null;
        try {
            jedis = getJedis(serial);
            jedis.hmset(key, value);
        } catch (Exception e) {
            log.logError("redis:mset操作出错！", e, serial);
        } finally {
            if (jedis != null) {
                returnResource(jedis);
            }
        }
    }

    /**
     * 设置多键值对到Hash表中
     *
     * @param key
     * @param value
     * @param serial
     */
    public void hset(String key, String field, String value, Serial serial) {
        log.logDebug("redis插入数据{}到{}Hash表", serial, field, key);
        Jedis jedis = null;
        try {
            jedis = getJedis(serial);
            jedis.hset(key, field, value);
        } catch (Exception e) {
            log.logError("redis:hset操作出错！", e, serial);
        } finally {
            if (jedis != null) {
                returnResource(jedis);
            }
        }
    }

    /**
     * 获取hash表中的所有key
     *
     * @param key
     * @param serial
     * @return
     */
    public Set<String> hkeys(String key, Serial serial) {
        log.logDebug("redis获取Hash表{}中的所有key", serial, key);
        Jedis jedis = null;
        try {
            jedis = getJedis(serial);
            Set<String> keys = jedis.hkeys(key);
            return keys;
        } catch (Exception e) {
            log.logError("redis:hkeys操作出错！", e, serial);
            return null;
        } finally {
            if (jedis != null) {
                returnResource(jedis);
            }
        }
    }

    /**
     * 获取数据
     *
     * @param key
     * @param serial
     * @return
     */
    public String get(String key, Serial serial) {
        if (UtilString.isNullOrEmpty(key)) {
            return null;
        }
        log.logDebug("redis获取数据{}", serial, key);
        Jedis jedis = null;
        try {
            jedis = getJedis(serial);
            String value = jedis.get(key);
            log.logDebug("redis获取数据{}={}", serial, key, value);
            return value;
        } catch (Exception e) {
            log.logError("redis:get操作出错！", e, serial);
            return null;
        } finally {
            if (jedis != null) {
                returnResource(jedis);
            }
        }
    }

    /**
     * 获取Hash表中的值
     *
     * @param key
     * @param fields
     * @param serial
     * @return
     */
    public String hget(String key, String fields, Serial serial) {
        log.logDebug("redis获取Hash表{}数据{}", serial, key, fields);
        Jedis jedis = null;
        try {
            jedis = getJedis(serial);
            String value = jedis.hget(key, fields);
            log.logDebug("redis获取数据{},{}={}", serial, key, fields, value);
            return value;
        } catch (Exception e) {
            log.logError("redis:hget操作出错！", e, serial);
            return null;
        } finally {
            if (jedis != null) {
                returnResource(jedis);
            }
        }
    }

    /**
     * 获取Hash表中的值
     *
     * @param key
     * @param fields
     * @param serial
     * @return
     */
    public List<String> hmget(String key, Serial serial, String... fields) {
        log.logDebug("redis获取Hash表{}数据{}", serial, key, fields);
        Jedis jedis = null;
        try {
            jedis = getJedis(serial);
            List<String> value = jedis.hmget(key, fields);
            return value;
        } catch (Exception e) {
            log.logError("redis:hmget操作出错！", e, serial);
            return null;
        } finally {
            if (jedis != null) {
                returnResource(jedis);
            }
        }
    }

    /**
     * 释放jedis资源
     *
     * @param jedis
     */
    private void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    /**
     * 删除数据
     *
     * @param key
     * @param serial
     */
    public void del(String key, Serial serial) {
        log.logDebug("redis删除数据{}", serial, key);
        Jedis jedis = null;
        try {
            jedis = getJedis(serial);
            jedis.del(key);
        } catch (Exception e) {
            log.logError("redis:del操作出错！", e, serial);
        } finally {
            if (jedis != null) {
                returnResource(jedis);
            }
        }
    }

    /**
     * 获取指定前缀的数据集合
     *
     * @param key
     * @param serial
     * @return
     */
    public Map<String, String> getByPrefix(String key, Serial serial) {
        log.logDebug("redis获取指定前缀数据集{}", serial, key);
        Map<String, String> map = new HashMap<String, String>();
        Jedis jedis = null;
        try {
            jedis = getJedis(serial);
            Set<String> keys = jedis.keys(key + "*");
            Iterator<String> dks = keys.iterator();
            while (dks.hasNext()) {
                String dk = dks.next();
                map.put(dk, jedis.get(dk));
            }
        } catch (Exception e) {
            log.logError("redis:查询redis操作出错！", e, serial);
        } finally {
            if (jedis != null) {
                returnResource(jedis);
            }
        }
        return map;
    }

    /**
     * 根据key模糊查询
     *
     * @param key
     * @param serial
     * @return
     */
    public Map<String, String> getLikeKeys(String key, Serial serial) {
        log.logDebug("redis获取模糊搜索数据集{}", serial, key);
        Map<String, String> map = new HashMap<String, String>();
        Jedis jedis = null;
        try {
            jedis = getJedis(serial);
            Set<String> keys = jedis.keys("*" + key + "*");
            Iterator<String> dks = keys.iterator();
            while (dks.hasNext()) {
                String dk = dks.next();
                map.put(dk, jedis.get(dk));
            }
        } catch (Exception e) {
            log.logError("redis:查询redis操作出错！", e, serial);
        } finally {
            if (jedis != null) {
                returnResource(jedis);
            }
        }
        return map;
    }

    /**
     * 删除指定前缀数据
     *
     * @param key
     * @param serial
     */
    public void delByPrefix(String key, Serial serial) {
        log.logDebug("redis删除指定前缀数据集{}", serial, key);
        Jedis jedis = null;
        try {
            jedis = getJedis(serial);
            Set<String> keys = jedis.keys(key + "*");
            Iterator<String> dks = keys.iterator();
            while (dks.hasNext()) {
                String dk = dks.next();
                jedis.del(dk);
            }
        } catch (Exception e) {
            log.logError("redis:del操作出错！", e, serial);
        } finally {
            if (jedis != null) {
                returnResource(jedis);
            }
        }
    }

    /**
     * 插入Data数据
     *
     * @param key
     * @param data
     * @param serial
     */
    public void setData(String key, Data data, Serial serial) {
        set(key, data.toJsonStr(), serial);
    }

    /**
     * 插入有有效期的Data数据
     *
     * @param key
     * @param seconds
     * @param data
     * @param serial
     */
    public void setexData(String key, int seconds, Data data, Serial serial) {
        setex(key, seconds, data.toJsonStr(), serial);
    }

    /**
     * 获取Data数据
     *
     * @param key
     * @param serial
     * @return
     */
    public Data getData(String key, Serial serial) {
        String value = get(key, serial);
        if (UtilString.isNullOrEmpty(value)) {
            return null;
        } else {
            return Data.parseData(value, "");
        }
    }

    /**
     * 插入DataList数据
     *
     * @param key
     * @param dataList
     * @param serial
     */
    public void setDataList(String key, DataList dataList, Serial serial) {
        set(key, dataList.toJsonStr(), serial);
    }

    /**
     * 插入具有有效期的dataList数据
     *
     * @param key
     * @param seconds
     * @param dataList
     * @param serial
     */
    public void setexDataList(String key, int seconds, DataList dataList, Serial serial) {
        setex(key, seconds, dataList.toJsonStr(), serial);
    }

    /**
     * 获取dataList数据
     *
     * @param key
     * @param serial
     * @return
     */
    public DataList getDataList(String key, Serial serial) {
        String value = get(key, serial);
        if (UtilString.isNullOrEmpty(value)) {
            return null;
        } else {
            return DataList.parseDataList(value);
        }
    }
}
