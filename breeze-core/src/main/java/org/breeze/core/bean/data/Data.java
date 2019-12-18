package org.breeze.core.bean.data;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;
import org.breeze.core.utils.date.UtilDateTime;
import org.breeze.core.utils.string.UtilString;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 数据基本对象
 * @Auther: 黑面阿呆
 * @Date: 2019/2/18 23:51
 * @Version: 1.0.0
 */
public class Data extends HashMap<String, Object> {

    private static Log log = LogFactory.getLog(Data.class);
    /**
     * 序列化版本标记
     */
    private static final long serialVersionUID = 9056853840179881021L;
    private static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static String DATE_FORMAT = "yyyy-MM-dd";
    private String entityName;
    private List<String> primaryKey;
    private Map<String, String> dataType = new HashMap<String, String>();
    private Map<String, DataInfo> dataInfos = new HashMap<String, DataInfo>();
    // 增加对序列的支持
    private Map<String, String> sequence = new HashMap<String, String>();

    /**
     * 构造数据对象
     */
    public Data() {
        super();
        entityName = null;
        primaryKey = new ArrayList<String>();
    }

    /**
     * 构造数据对象
     *
     * @param entityName 数据表名字
     */
    public Data(String entityName) {
        super();
        initData(entityName, null);
    }

    /**
     * 构造数据对象
     *
     * @param entityName 数据表名字
     * @param dataMap    数据集的Map
     * @param primaryKey 主键，或者主键的集合
     */
    public Data(String entityName, Map<String, Object> dataMap, String... primaryKey) {
        super();
        initData(entityName, dataMap, primaryKey);
    }

    public Map<String, DataInfo> getDataInfos() {
        return dataInfos;
    }

    public void setDataInfos(Map<String, DataInfo> dataInfos) {
        this.dataInfos = dataInfos;
    }

    private void initData(String entityName, Map<String, Object> dataMap, String... primaryKey) {
        this.entityName = entityName;
        if (dataMap != null) {
            Object[] set = dataMap.keySet().toArray();
            for (int i = 0; i < set.length; i++) {
                String object = (String) set[i];
                this.addData(object, dataMap.get(object));
            }
        }
        this.primaryKey = new ArrayList<String>();
        if (primaryKey != null) {
            addPrimaryKey(primaryKey);
        }
        log = LogFactory.getLog(this.getClass());
    }

    /**
     * 构造数据对象
     *
     * @param entityName 数据表名字
     * @param primaryKey 主键，或者主键的集合
     */
    public Data(String entityName, String... primaryKey) {
        this(entityName, null, primaryKey);
    }

    /**
     * 构造数据对象
     *
     * @param dataMap 数据集的Map
     */
    public Data(Map<String, Object> dataMap) {
        this(null, dataMap);
    }

    /**
     * 增加一个数据字段内容
     *
     * @param key   字段名称
     * @param value 数据
     */
    public void add(String key, Object value) {
        put(key.toUpperCase(), value);
    }

    /**
     * 增加一个数据字段内容
     *
     * @param key   字段名称
     * @param value 数据
     * @param isPk  是否主键
     */
    public void add(String key, Object value, boolean isPk) {
        add(key, value);
        if (isPk) {
            if (!isPrimaryKey(key)) {
                addPrimaryKey(key);
            }
        }
    }

    /**
     * 增加数据
     *
     * @param datas 数据
     */
    public void addData(Map<String, Object> datas) {
        putAll(keyUpperCase(datas));
    }

    /**
     * 将所有数据为字符串的Map增加到Data中
     *
     * @param datas
     */
    public void addDataString(Map<String, String> datas) {
        putAll(keyUpperCaseString(datas));
    }

    private Map<String, String> keyUpperCaseString(Map<String, String> data) {
        Object[] keys = data.keySet().toArray();
        int n = keys.length;
        for (int i = 0; i < n; i++) {
            String v = data.get(keys[i]);
            data.remove(keys[i]);
            data.put(((String) keys[i]).toUpperCase(), v);
        }
        return data;
    }

    /**
     * 增加字段值
     *
     * @param fieldName  字段名字
     * @param fieldValue 数据值
     */
    public void addData(String fieldName, Object fieldValue) {
        put(fieldName.toUpperCase(), fieldValue);
    }

    @Override
    public Object remove(Object key) {
        return super.remove(((String) key).toUpperCase());
    }

    /**
     * 增加字段值
     *
     * @param fieldName    字段名称
     * @param fieldValue   数据值
     * @param isPrimaryKey 是否主键
     */
    public void addData(String fieldName, Object fieldValue, boolean isPrimaryKey) {
        if (isPrimaryKey) {
            addPrimaryKey(fieldName);
        }
        addData(fieldName, fieldValue);
    }

    /**
     * 增加主键
     *
     * @param primaryKey 主键，或者主键的集合
     */
    public void addPrimaryKey(String... primaryKey) {
        if (primaryKey == null) {
            return;
        }
        if (this.primaryKey == null) {
            this.primaryKey = new ArrayList<String>();
        }
        for (String pk : primaryKey) {
            this.primaryKey.add(pk.toUpperCase());
        }
    }

    /**
     * 增加一个数据字段内容
     *
     * @param key   字段名称
     * @param value 字符串数据数据
     */
    public void addString(String key, String value) {
        add(key, value);
    }

    private Object checkNull(Object obj, String defaultString) {
        return obj == null ? defaultString : obj;
    }

    /**
     * 清除主键
     */
    public void clearPrimaryKey() {
        primaryKey.clear();
    }

    /**
     * 得到所有的字段
     *
     * @return 对象数组
     */
    public Object[] fields() {
        return this.keySet().toArray();
    }

    /**
     * 得到数据对象
     *
     * @param fieldName 字段的名称
     * @return 数据对象
     */
    public Object get(String fieldName) {
        fieldName = fieldName.toUpperCase();
        return super.get(fieldName);
    }

    public Object get(Object key) {
        return get(((String) key).toUpperCase());
    }

    /**
     * 得到Blob数据
     *
     * @param fieldName
     * @param defaultString
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public Object get(String fieldName, String defaultString) {
        return checkNull(super.get(fieldName), defaultString);
    }

    /**
     * 得到Blob数据
     *
     * @param key
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public byte[] getBlob(Object key) throws IOException, SQLException {
        if (key == null) {
            return null;
        }
        String fieldName = String.valueOf(key).toUpperCase();
        Object b = super.get(fieldName);
        if (b == null) {
            return null;
        }
        return (byte[]) b;
    }

    /**
     * 得到Blob数据
     *
     * @param key
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public InputStream getStream(Object key) throws IOException, SQLException {
        Object b = super.get(key);
        if (b == null) {
            return null;
        }
        return ((Blob) b).getBinaryStream();
    }

    /**
     * @param blob
     * @return
     */
    @SuppressWarnings("unused")
    private byte[] blobToBytes(Blob blob) {
        if (blob == null) {
            return null;
        }
        BufferedInputStream is = null;
        try {
            is = new BufferedInputStream(blob.getBinaryStream());
            byte[] bytes = new byte[(int) blob.length()];
            int len = bytes.length;
            int offset = 0;
            int read = 0;
            while (offset < len && (read = is.read(bytes, offset, len - offset)) >= 0) {
                offset += read;
            }
            return bytes;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                is.close();
                is = null;
            } catch (IOException e) {
                return null;
            }
        }
    }

    /**
     * 得到Clob文本的内容
     *
     * @param key 字段
     * @return 字符串
     * @throws SQLException
     * @throws IOException
     */
    public String getClobString(Object key) throws SQLException, IOException {
        return getString(key);
    }

    /**
     * 得到数据
     *
     * @return Map
     */
    public Map<String, Object> getData() {
        return this;
    }

    /**
     * 得到默认日期格式的数据<b>yyyy-MM-dd</b>
     *
     * @param fieldName 字段名称
     * @return 日期格式的字符串
     */
    public String getDate(String fieldName) {
        return getDate(fieldName, DATE_FORMAT);
    }

    /**
     * 得到日期格式的数据
     *
     * @param fieldName    字段名称
     * @param formatString 日期格式，例如：yyyy-MM-DD
     * @return 日期格式的字符串
     */
    public String getDate(String fieldName, String formatString) {
        Object v = get(fieldName);
        if (v == null || "".equals(v)) {
            return "";
        }
        if (formatString == null) {
            formatString = DATE_FORMAT;
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatString);
            if (v instanceof String) {
                Date date = format.parse((String) v);
                return format.format(date);
            } else if (v instanceof Date) {
                return format.format(v);
            } else {
                return format.format(v);
            }
        } catch (Exception e) {
            if (log.isWarn()) {
                log.logWarn("日期格式转换失败", e);
            }
            return (String) v;
        }
    }

    /**
     * 将long类型的日期值转换为字符串 YYYY-MM-DD
     *
     * @param fieldName 字段名称
     * @return 日期格式的字符串
     */
    public String getDateStringForLong(String fieldName) {
        return getDateStringForLong(fieldName, null);
    }

    public String getDateStringForLong(String fieldName, String formatString) {
        long k = getLong(fieldName);
        if (k == 0) {
            return "";
        }
        if (UtilString.isNullOrEmpty(formatString)) {
            formatString = DATETIME_FORMAT;
        }
        try {
            if (!"".equals(fieldName)) {
                return UtilDateTime.formatDate(new Date(k), formatString);
            }
            return "";
        } catch (Exception e) {
            if (log.isWarn()) {
                log.logWarn("日期格式化失败", e);
            }
            return getString(fieldName);
        }
    }

    /**
     * 得到日期，值为long
     *
     * @param fieldName
     * @return
     */
    public String getDateTimeStringForLong(String fieldName) {
        return getDateTimeStringForLong(fieldName, null);
    }

    public String getDateTimeStringForLong(String fieldName, String formatString) {
        long k = getLong(fieldName);
        if (k == 0) {
            return "";
        }
        try {
            if (!"".equals(fieldName)) {
                return UtilDateTime.toDateTimeString(new Date(k));
            }
            return "";
        } catch (Exception e) {
            if (log.isWarn()) {
                log.logWarn("时间格式转化失败");
            }
            return getString(fieldName);
        }
    }

    public String getDateStringForSeconds(String fieldName) {
        return getDateStringForSeconds(fieldName, null);
    }

    public String getDateStringForSeconds(String fieldName, String formatString) {
        long k = getInt(fieldName);
        if (k == 0) {
            return "";
        }
        try {
            if (!"".equals(fieldName)) {
                return UtilDateTime.toDateString(new Date(k * 1000));
            }
            return "";
        } catch (Exception e) {
            if (log.isWarn()) {
                log.logWarn("时间格式转化失败");
            }
            return getString(fieldName);
        }
    }

    /**
     * 得到日期时间
     *
     * @param fieldName
     * @return
     */
    public String getDateTime(String fieldName) {
        return getDateTime(fieldName, null);
    }

    /**
     * 得到日期时间
     *
     * @param fieldName
     * @param formatString
     * @return
     */
    public String getDateTime(String fieldName, String formatString) {
        if (formatString == null) {
            formatString = DATETIME_FORMAT;
        }
        Object v = get(fieldName);
        if (v == null || "".equals(v)) {
            return "";
        } else {
            try {
                SimpleDateFormat format = new SimpleDateFormat(DATETIME_FORMAT);
                if (v instanceof String) {
                    Date date = format.parse((String) v);
                    return format.format(date);
                } else if (v instanceof Date) {
                    return format.format(v);
                } else {
                    return format.format(v);
                }
            } catch (Exception e) {
                if (log.isWarn()) {
                    log.logWarn("时间格式转换失败");
                }
                return (String) v;
            }
        }
    }

    public String getDateTimeStringForPhpInt(String fieldName) {
        return getDateTimeStringForPhpInt(fieldName, null);
    }

    public String getDateTimeStringForPhpInt(String fieldName, String formatString) {
        long k = getInt(fieldName);
        if (k == 0) {
            return "";
        }
        try {
            if (!"".equals(fieldName)) {
                return UtilDateTime.toDateTimeString(new Date(k * 1000));
            }
            return "";
        } catch (Exception e) {
            if (log.isWarn()) {
                log.logWarn("时间格式转换失败");
            }
            return getString(fieldName);
        }
    }

    /**
     * 得到Doublel类型的数据
     *
     * @param fieldName 字段名称
     * @return Double
     */
    public double getDouble(String fieldName) {
        Object v = get(fieldName);
        if (v == null || "".equals(v)) {
            return 0;
        }
        if (v instanceof BigDecimal) {
            return ((BigDecimal) v).doubleValue();
        } else {
            return Double.parseDouble(String.valueOf(v));
        }
    }

    /**
     * 得到表名
     *
     * @return String 表名字
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * 得到浮点类型的数据
     *
     * @param fieldName 字段名称
     * @return float
     */
    public float getFloat(String fieldName) {
        Object v = get(fieldName);
        if (v == null || "".equals(v)) {
            return 0;
        }
        if (v instanceof BigDecimal) {
            return ((BigDecimal) v).floatValue();
        } else {
            return Float.parseFloat((String) v);
        }
    }

    /**
     * 得到整数类型的值
     *
     * @param fieldName 字段的名称
     * @return 整数类型的值
     */
    public int getInt(String fieldName) {
        Object v = get(fieldName);
        if (v == null || "".equals(v)) {
            return 0;
        }
        if (v instanceof BigDecimal) {
            return ((BigDecimal) v).intValue();
        }
        if (v instanceof Integer) {
            return ((Integer) v).intValue();
        } else {
            try {
                return Integer.parseInt(String.valueOf(v), 10);
            } catch (Exception e) {
                if (log.isWarn()) {
                    log.logWarn("时间格式转换失败");
                }
                return 0;
            }
        }
    }

    /**
     * 得到long类型的数据
     *
     * @param fieldName 字段名称
     * @return long
     */
    public long getLong(String fieldName) {
        Object v = get(fieldName);
        if (v == null || "".equals(v)) {
            return 0;
        }
        if (v instanceof BigDecimal) {
            return ((BigDecimal) v).longValue();
        } else {
            return Long.parseLong(String.valueOf(v));
        }

    }

    /**
     * 得到指定索引号的主键
     *
     * @param i 索引号
     * @return 主键的字段名字
     */
    public String getPrimaryKey(int i) {
        return primaryKey.get(i);
    }

    /**
     * 得到所有的主键
     *
     * @return 主键的集合
     */
    public List<String> getPrimaryListKeys() {
        return primaryKey;
    }

    /**
     * 得到主键数组
     *
     * @return String[]
     */
    public String[] getPrimaryKeys() {
        int lenght = primaryKey.size();
        String[] pks = new String[lenght];
        int i = 0;
        for (String pk : primaryKey) {
            pks[i] = pk;
            i++;
        }
        return pks;
    }

    /**
     * 得到字符串数据对象
     *
     * @param fieldName 字段的名称
     * @return 字符串类型的数据
     */
    public String getString(Object fieldName) {
        Object v = get(fieldName);
        if (v == null) {
            return null;
        }
        if (v instanceof String) {
            return (String) v;
        } else if (v instanceof java.sql.Timestamp) {
            return ((new SimpleDateFormat(DATETIME_FORMAT)).format(v));
        } else if (v instanceof BigDecimal) {
            return ((BigDecimal) v).toString();
        } else if (v instanceof java.sql.Date) {
            return UtilDateTime.toDateTimeString((java.sql.Date) v);
        } else if (v instanceof java.sql.Time) {
            return UtilDateTime.toDateTimeString((java.sql.Time) v);
        } else if (v instanceof Long) {
            return ((Long) v).toString();
        } else if (v instanceof Integer) {
            return ((Integer) v).toString();
        } else if (v instanceof Boolean) {
            return ((Boolean) v).toString();
        } else if (v instanceof Double) {
            return ((Double) v).toString();
        } else {
            return v.toString();
        }
    }

    /**
     * 得到json对象
     *
     * @param fieldName
     * @return
     * @throws JSONException
     */
    public JSONObject getJson(Object fieldName) throws JSONException {
        String v = (String) get(fieldName);
        if (UtilString.isNullOrEmpty(v)) {
            return new JSONObject();
        }
        JSONObject json = JSONObject.parseObject(v);
        return json;
    }

    /**
     * 得到json数组
     *
     * @param fieldName
     * @return
     * @throws JSONException
     */
    public JSONArray getJsonArray(Object fieldName) throws JSONException {
        String v = (String) get(fieldName);
        if (UtilString.isNullOrEmpty(v)) {
            return new JSONArray();
        }
        JSONArray json = JSONArray.parseArray(v);
        return json;
    }

    /**
     * 得到字符串数据对象
     *
     * @param fieldName    字段名称
     * @param defaultValue 如果为null的话，返回此默认值
     * @return 字符串类型的数据
     */
    public String getString(Object fieldName, String defaultValue) {
        String v = getString(fieldName);
        if (v == null) {
            return defaultValue;
        } else {
            return v;
        }
    }

    public String getType(String fieldName) {
        String dt = (String) dataType.get(fieldName.toUpperCase());
        if (dt == null) {
        }
        return dt;
    }

    public Map<String, String> getTypes() {
        return dataType;
    }

    public boolean hasLob() {
        boolean b = dataType.containsValue("oracle.sql.BLOB");
        if (!b) {
            b = dataType.containsValue("oracle.sql.CLOB");
        }
        if (!b) {
            b = dataType.containsValue("java.sql.Clob");
        }
        if (!b) {
            b = dataType.containsValue("java.sql.Blob");

        }
        return b;
    }

    /**
     * 是否存在相应的类别
     *
     * @param type 类别
     * @return
     */
    public boolean hasType(String type) {
        return dataType.containsValue(type);
    }

    /**
     * 主键中是否包含了指定的key
     *
     * @param key 字段
     * @return 如果包含了该主键，返回true
     */
    public boolean isPrimaryKey(String key) {
        return primaryKey.contains(key.toUpperCase());
    }

    private Map<String, Object> keyUpperCase(Map<String, Object> data) {
        Object[] keys = data.keySet().toArray();
        int n = keys.length;
        for (int i = 0; i < n; i++) {
            Object v = data.get(keys[i]);
            data.remove(keys[i]);
            data.put(((String) keys[i]).toUpperCase(), v);
        }
        return data;
    }

    /**
     * 得到主键的个数
     *
     * @return i
     */
    public int primaryKeySize() {
        return primaryKey.size();
    }

    /**
     * 除去一些字段
     *
     * @param fieldNames 字段名字列表
     */
    public void removeData(List<String> fieldNames) {
        for (int i = 0; i < fieldNames.size(); i++) {
            remove(fieldNames.get(i));
        }
    }

    /**
     * 除去某一个字段的数据
     *
     * @param fieldName 字段名字
     */
    public void removeData(String fieldName) {

        remove(fieldName.toUpperCase());
    }

    /**
     * 删除指定的主键
     *
     * @param primaryKey
     */
    public void removePrimaryKey(String primaryKey) {
        this.primaryKey.remove(primaryKey.toUpperCase());
    }

    public boolean equals(Data data) {
        String entityName = data.getEntityName();
        List<String> pks = data.getPrimaryListKeys();
        // 如果表名字相同，继续比较
        if (this.entityName.equals(entityName)) {
            if (pks.size() == this.primaryKeySize()) {
                if (this.primaryKey.containsAll(pks)) {
                    return equalsKey(data);
                }
            }
        }
        return false;
    }

    private boolean equalsKey(Data data) {
        boolean rv = true;
        for (String pk : this.primaryKey) {
            Object k = data.get(pk);
            Object v = this.get(pk);
            if (k != null) {
                if (!rv) {
                    return rv;
                }
                if (!k.equals(v)) {
                    rv = false;
                }
            } else {
                if (v != null) {
                    rv = false;
                }
            }
        }
        return rv;
    }

    /**
     * 设置数据
     *
     * @param map Map格式的数据[key=value]
     */
    public void setData(Map<String, Object> map) {
        clear();
        putAll(keyUpperCase(map));
    }

    /**
     * 设置数据表名字
     *
     * @param entityName 数据表名字
     */
    public void setEntityName(String entityName) {
        if (entityName != null) {
            this.entityName = entityName;
        }
    }

    /**
     * 设置主键
     *
     * @param primaryKey 主键的字段名字
     */
    public void setPrimaryKey(String... primaryKey) {
        if (this.primaryKey != null) {
            this.primaryKey.clear();
        }
        addPrimaryKey(primaryKey);
    }

    public void setType(Map<String, String> map) {
        dataType.putAll(map);
    }

    public void setType(String fieldName, String type) {
        dataType.put(fieldName.toUpperCase(), type);
    }

    /**
     * 将Data中的某个字段的日期类型串转换为Long类型
     *
     * @param fieldName
     * @return
     */
    public boolean toDateLong(String fieldName) {
        try {
            String o = getString(fieldName);
            put(fieldName.toUpperCase(), new Long(UtilDateTime.toDate(o, "00:00:00").getTime()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将Data中的某个字段的时间日期类型串转换为long类型串
     *
     * @param fieldName
     * @return
     */
    public boolean toDateTimeLong(String fieldName) {
        try {
            String o = getString(fieldName);
            put(fieldName.toUpperCase(), new Long(UtilDateTime.toDate(o).getTime()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String toString() {
        StringBuffer result = new StringBuffer("{");
        Object[] keys = fields();
        int n = keys.length;
        for (int i = 0; i < n; i++) {
            boolean addLog = true;
            if (i > 3) {
                addLog = false;
            }
            boolean isPK = getPrimaryListKeys().contains(keys[i]);
            if (addLog || isPK) {
                result.append("[").append(keys[i]);
                if (isPK) {
                    result.append("<主键>");
                }
                result.append("=");
                result.append(getString(keys[i]));
                result.append("]");
            }
        }

        result.append("size=").append(n);
        result.append("}");
        return result.toString();
    }

    /**
     * 将Data转变为XML格式的字符串
     *
     * @return StringBuffer
     * @throws SQLException
     * @throws IOException
     */
    public StringBuffer toXml() throws IOException, SQLException {
        StringBuffer buff = new StringBuffer("\t<data entityName=\"");
        buff.append(entityName).append("\">\n");
        Object[] keys = fields();
        int n = keys.length;
        for (int i = 0; i < n; i++) {
            String key = (String) keys[i];
            buff.append("\t\t<").append(key);
            if (getPrimaryListKeys().contains(key)) {
                buff.append(" pk=\"true\"");
            } else {
                buff.append(" pk=\"false\"");
            }
            String type = getType(key);
            if (type == null) {
                if ("".equals(getString(key, ""))) {
                    buff.append("></").append(key).append(">\n");
                } else {
                    buff.append("><![CDATA[");
                    buff.append(getString(key, ""));
                    buff.append("]]></").append(key).append(">\n");
                }
            } else {
                if (type.indexOf("BLOB") >= 0) {
                    if ("".equals(getString(key, ""))) {
                        buff.append("></").append(key).append(">\n");
                    } else {
                        buff.append("><![CDATA[");
                        byte[] bb = getBlob(key);
                        if (bb != null) {
                            buff.append(new String(bb));
                        }
                        buff.append("]]></").append(key).append(">\n");
                    }
                } else {
                    if ("".equals(getString(key, ""))) {
                        buff.append("></").append(key).append(">\n");
                    } else {
                        buff.append("><![CDATA[");
                        buff.append(getString(key, ""));
                        buff.append("]]></").append(key).append(">\n");
                    }
                }
            }

        }
        buff.append("\t</data>\n");
        return buff;
    }

    /**
     * 将Data转变为XML格式的字符串
     *
     * @return String
     * @throws SQLException
     * @throws IOException
     */
    public String toXmlStr() throws IOException, SQLException {
        return toXml().toString();
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject(this);
        return json;
    }

    /**
     * 数据转json字符串
     *
     * @return
     */
    public String toJsonStr() {
        return toJson().toString().replaceAll("\"null\"", "\"\"")
                .replaceAll("null", "\"\"");
    }

    /**
     * json数据转data
     *
     * @param jsonStr json字符串
     * @param pk      主键
     * @param field   字段
     * @return
     */
    public static Data parseData(String jsonStr, String pk, String... field) {
        try {
            JSONObject json = JSONObject.parseObject(jsonStr);
            return parseData(json, pk, field);
        } catch (Exception e) {
            log.logInfo("json解析失败");
            throw new JSONException("20010");
        }
    }

    /**
     * json数据转data
     *
     * @param json  json数据
     * @param pk    数据主键
     * @param field 字段
     * @return
     */
    public static Data parseData(JSONObject json, String pk, String... field) {
        Data data = new Data();
        if (field == null || field.length == 0) {
            data = new Data(json.getInnerMap());
        } else {
            Map<String, Object> map = json.getInnerMap();
            for (int i = 0; i < field.length; i++) {
                data.add(field[i], map.get(field[i]));
            }
        }
        if (UtilString.isNotEmpty(pk)) {
            data.setPrimaryKey(pk);
        }
        return data;
    }

    /**
     * 判断data是否为空
     *
     * @param data
     * @return
     */
    public static boolean isNotEmpty(Data data) {
        return data == null && data.size() > 0;
    }
}
