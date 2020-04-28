package org.breeze.core.database.impl;

import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.data.DataInfo;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.config.CommonConfig;
import org.breeze.core.database.BaseDataExecute;
import org.breeze.core.exception.DBException;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;
import org.breeze.core.utils.date.UtilDateTime;

import java.sql.Date;
import java.sql.*;
import java.util.*;

/**
 * @Description:
 * @ClassName: DataExecutePhoenixImpl
 * @Auther: 黑面阿呆
 * @Date: 2018/10/29 18:38
 * @Version: V1.0
 */
public class DataExecutePhoenixImpl extends BaseDataExecute {

    private Log log;

    private static final String INCREASE_SEQUENCE = "*************!@##$%^&*()INCREASE_SEQUENCE)(*&^%$#@!*************";

    public DataExecutePhoenixImpl() {
        super();
        Class<?> c = this.getClass();
        log = LogFactory.getLog(c);
    }

    @Override
    protected PreparedStatement getBatchCreatPstmt(PreparedStatement pstmt, DataList dataList) throws DBException {
        Data data = dataList.getData(0);
        String[] keys = data.getPrimaryKeys();
        Set<String> pKeys = new HashSet<String>();
        // 整理Data，进行主键的自动添加
        for (String pk : keys) {
            Object v = data.get(pk);
            if (v == null || "".equals(v) || "null".equals(v)) {
                data.add(pk, INCREASE_SEQUENCE);
                pKeys.add(pk);
            }
        }
        // 组合SQL
        // INSERT INTO 数据表 (key,key,key) VALUES (?,?,?)
        StringBuffer sql = new StringBuffer("UPSERT INTO ");
        sql.append(data.getEntityName()).append(" (");
        StringBuffer s = new StringBuffer();
        Object[] fields = data.fields();
        List<String> l = new ArrayList<String>();
        for (int i = 0; i < data.size(); i++) {
            if (i > 0) {
                sql.append(",");
                s.append(",");
            }
            sql.append(fields[i]);
            if (pKeys.contains(fields[i]) && INCREASE_SEQUENCE.equals(data.getString(fields[i]))) {
                s.append("NEXT VALUE FOR ").append(data.getEntityName()).append("_").append(fields[i]).append("_SEQUENCE");
            } else {
                s.append("?");
                l.add((String) fields[i]);
            }
        }
        sql.append(") VALUES (").append(s).append(")");
        try {
            pstmt = conn.prepareStatement(sql.toString());
            log.logDebug("要执行的插入SQL语句:{}", sql.toString());
            // 批量设置所有的数据
            for (Object od : dataList) {
                Data d = (Data) od;
                int si = 1;
                for (String k : l) {
                    setObject(pstmt, si, data.getType(k), d.get(k));
                    si++;
                }
                pstmt.addBatch();
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return pstmt;
    }

    /**
     * 设置PreparedStatement中的值
     *
     * @param pstmt    PreparedStatement
     * @param i        索引
     * @param typeName 类型
     * @param value    值
     * @throws SQLException
     */
    protected void setObject(PreparedStatement pstmt, int i, String typeName, Object value) throws SQLException {
        if ("java.lang.String".equals(typeName)) {
            pstmt.setString(i, (String) value);
        } else if ("java.sql.Timestamp".equals(typeName) || "oracle.sql.TIMESTAMP".equals(typeName)) {//(typeName.toUpperCase().indexOf("TIMESTAMP")>0 || typeName.toUpperCase().indexOf("DATE")>0)
            if (value == null || "".equals(value)) {
                pstmt.setDate(i, null);
            } else {
                if (value instanceof Date || value instanceof java.util.Date) {
                    if (value instanceof Date) {
                        pstmt.setObject(i, new Timestamp(((Date) value).getTime()));
                    } else {
                        pstmt.setObject(i, new Timestamp(((java.util.Date) value).getTime()));
                    }
                } else if (((String) value).length() > "1900-00-00".length()) {
                    try {
                        pstmt.setTimestamp(i, UtilDateTime.toTimestamp((String) value));
                    } catch (Exception e) {
                        throw new SQLException("时间格式转换失败");
                    }
                } else {
                    try {
                        pstmt.setDate(i, UtilDateTime.toSqlDate((String) value));
                    } catch (Exception e) {
                        throw new SQLException("日期格式转换失败");
                    }
                }
            }
        } else if ("java.math.BigDecimal".equals(typeName) || "java.lang.Integer".equals(typeName)
                || "java.lang.Long".equals(typeName) || "java.lang.Float".equals(typeName)
                || "java.lang.Double".equals(typeName)) {
            if (value == null || "".equals(value)) {
                pstmt.setBigDecimal(i, null);
            } else {
                if (value instanceof Integer) {
                    pstmt.setObject(i, value);
                } else if (value instanceof Long) {
                    pstmt.setObject(i, value);
                } else if (value instanceof Float) {
                    pstmt.setObject(i, value);
                } else if (value instanceof java.math.BigDecimal) {
                    pstmt.setObject(i, value);
                } else {
                    String valueStr = null;
                    try {
                        valueStr = String.valueOf(value);
                    } catch (Exception e) {
                        valueStr = (String) value;
                    }
                    if (valueStr.indexOf(".") > 0) {
                        try {
                            pstmt.setDouble(i, Double.parseDouble(valueStr));
                        } catch (Exception e) {
                            throw new SQLException("Double格式转换失败");
                        }
                    } else {
                        try {
                            pstmt.setLong(i, Long.parseLong(valueStr));
                        } catch (Exception e) {
                            throw new SQLException("Long类型数据转换失败");
                        }
                    }
                }
            }
        } else if ("byte[]".equals(typeName)) {
            pstmt.setObject(i, value);
        } else {
            pstmt.setObject(i, value);
        }
    }

    @Override
    protected PreparedStatement getBatchStorePstmt(PreparedStatement pstmt, DataList dataList) throws DBException {
        return getBatchCreatPstmt(pstmt, dataList);
    }

    @Override
    protected void setDataType(Data data) throws DBException {
        if (CommonConfig.isDataInfoCache()) {
            Map<String, DataInfo> infoMap = dataInfoMaps.get(data.getEntityName());
            if (infoMap != null) {
                data.setDataInfos(infoMap);
            }
            Map<String, String> typeMap = dataTypeMaps.get(data.getEntityName());
            if (typeMap != null) {
                data.setType(typeMap);
            }
            return;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // 查询数据表的数据，为了获取MetaData
            StringBuffer buf = new StringBuffer("SELECT * FROM ").append(data.getEntityName()).append(" WHERE 1=0");
            ps = getConnection().prepareStatement(buf.toString());
            rs = ps.executeQuery();
            setDataType(data, rs);
            if (CommonConfig.isDataInfoCache()) {
                dataInfoMaps.put(data.getEntityName(), data.getDataInfos());
                dataTypeMaps.put(data.getEntityName(), data.getTypes());
            }
        } catch (SQLException e) {
            throw new DBException("数据格式转换失败");
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e1) {
                    throw new DBException(e1);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e1) {
                    throw new DBException(e1);
                }
            }
        }
    }

    @Override
    protected void setDataType(Data data, ResultSet rs) throws DBException, SQLException {
        ResultSetMetaData w = rs.getMetaData();
        if (w != null) {
            for (int i = 0; i < w.getColumnCount(); i++) {
                int v = i + 1;
                if (w.getColumnTypeName(v).startsWith("INTEGER") || w.getColumnTypeName(v).startsWith("TINYINT") || w.getColumnTypeName(v).startsWith("BIGINT")) {
                    data.setType(w.getColumnName(v).toUpperCase(), "java.lang.Long");
                } else {
                    data.setType(w.getColumnName(v).toUpperCase(), w.getColumnClassName(v));
                }
            }
        }
    }

    @Override
    protected void createLob(Data data) throws DBException {

    }

    @Override
    protected void updateLob(Data data) throws DBException {

    }

    @Override
    protected PreparedStatement setCreatePreparedStatement(PreparedStatement pstmt, Data data) throws DBException {
        String[] keys = data.getPrimaryKeys();
        // 组合SQL
        // INSERT INTO 数据表 (key,key,key) VALUES (?,?,?)
        StringBuffer sql = new StringBuffer("UPSERT INTO ");
        sql.append(data.getEntityName()).append(" (");
        StringBuffer s = new StringBuffer();
        // 整理Data，进行主键的自动添加
        Set<String> pKeys = new HashSet<String>();
        for (String pk : keys) {
            Object v = data.get(pk);
            if (v == null || "".equals(v) || "null".equals(v)) {
                data.add(pk, INCREASE_SEQUENCE);
                pKeys.add(pk);
            }
        }
        Object[] fields = data.fields();
        List<String> l = new ArrayList<String>();
        for (int i = 0; i < data.size(); i++) {
            if (i > 0) {
                sql.append(",");
                s.append(",");
            }
            sql.append(fields[i]);
            if (pKeys.contains(fields[i]) && INCREASE_SEQUENCE.equals(data.getString(fields[i]))) {
                s.append("NEXT VALUE FOR ").append(data.getEntityName()).append("_").append(fields[i]).append("_sequence");
            } else {
                s.append("?");
                l.add((String) fields[i]);
            }
        }
        sql.append(") VALUES (").append(s).append(")");
        try {
            pstmt = conn.prepareStatement(sql.toString());
            log.logDebug("要执行的插入SQL语句:{}", sql.toString());
            // 批量设置所有的数据
            int si = 1;
            for (String k : l) {
                setObject(pstmt, si, data.getType(k), data.get(k));
                si++;
            }
            return pstmt;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    protected PreparedStatement setFindPreparedStatement(PreparedStatement pstmt, Data data, List<String> fields, int pageSize, int page) throws DBException {
        StringBuffer sql = new StringBuffer("SELECT ");
        StringBuffer A_sqlFields = new StringBuffer();
        StringBuffer AA_sqlFields = new StringBuffer();
        StringBuffer sqlFields = new StringBuffer();
        if (fields == null || fields.size() == 0) {
            A_sqlFields.append("A.*");
            AA_sqlFields.append("AA.*");
            sqlFields.append("*");
        } else {
            int ii = 0;
            for (String f : fields) {
                if (ii > 0) {
                    A_sqlFields.append(",");
                    AA_sqlFields.append(",");
                    sqlFields.append(",");
                }
                A_sqlFields.append("A.").append(f).append(" ");
                AA_sqlFields.append("AA.").append(f).append(" ");
                sqlFields.append(f).append(" ");
                ii++;
            }
        }
        sql.append(sqlFields);
        sql.append(" FROM ").append(data.getEntityName()).append(" ");
        Object[] fieldsName = data.fields();
        List<String> l = new ArrayList<String>();
        if (fieldsName != null && fieldsName.length > 0) {
            sql.append("WHERE ");
            int jj = 0;
            for (Object o : fieldsName) {
                if (jj > 0) {
                    sql.append("AND ");
                }
                sql.append(fieldsName[jj]).append("=? ");
                l.add((String) o);
                jj++;
            }
        }
        // SELECT A.* FROM (SELECT * FROM TABLE_NAME
        // WHERE 条件 ORDER BY ) AS A LIMIT 20,40
        StringBuffer pageSql = new StringBuffer();
        if (pageSize == 0 || page == 0) {
            pageSql.append(sql);
        } else {
            pageSql.append("SELECT ").append(A_sqlFields).append(" FROM (SELECT ");
            pageSql.append(AA_sqlFields);
            pageSql.append(",ROWNUMBER() OVER() AS RN FROM (");
            pageSql.append(sql).append(") AS AA) AS A WHERE A.RN <=").append(page * pageSize).append(" AND A.RN >")
                    .append((page - 1) * pageSize);
        }
        log.logDebug("要执行的查询SQL语句:{}", pageSql);
        try {
            pstmt = conn.prepareStatement(pageSql.toString());
            for (int i = 0; i < l.size(); i++) {
                setObject(pstmt, i + 1, data.getType((String) l.get(i)), data.get(l.get(i)));
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return pstmt;
    }

    @Override
    protected PreparedStatement setFindPageStatement(PreparedStatement pstmt, Data data) throws DBException, SQLException {
        StringBuffer sql = new StringBuffer("SELECT COUNT(1) C");
        sql.append(" FROM ").append(data.getEntityName()).append(" ");
        Object[] fieldsName = data.fields();
        List<String> l = new ArrayList<String>();
        if (fieldsName != null && fieldsName.length > 0) {
            sql.append("WHERE ");
            int ii = 0;
            for (Object o : fieldsName) {
                if (ii > 0) {
                    sql.append("AND ");
                }
                sql.append(o).append("=? ");
                l.add((String) o);
                ii++;
            }
        }
        pstmt = conn.prepareStatement(sql.toString());
        for (int i = 0; i < l.size(); i++) {
            setObject(pstmt, i + 1, data.getType((String) l.get(i)), data.get(l.get(i)));
        }
        return pstmt;
    }

    @Override
    protected Object getTimestamp(ResultSet rs, String fieldName) throws DBException {
        Timestamp timestamp = null;
        try {
            timestamp = rs.getTimestamp(fieldName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    @Override
    protected Object getCLOBString(ResultSet rs, String fieldName) throws DBException {
        return null;
    }

    @Override
    protected Object getBLOB(ResultSet rs, String fieldName) throws DBException {
        return null;
    }

    @Override
    protected int getSearchSqlDataTotal(String sql, String countStr, String countSql, boolean isParam, Object... paramValue) throws DBException {
        PreparedStatement pstmt1 = null;
        ResultSet rs1 = null;
        try {
            if (countSql == null) {
                countSql = getSearchCountSql(sql, countStr);
            }
            log.logDebug("得到总页数的SQL：" + countSql);
            try {
                int i = Integer.parseInt(countSql);
                return i;
            } catch (Exception e) {
            }
            pstmt1 = conn.prepareStatement(countSql);
            if (isParam) {
                int index = 1;
                for (Object o : paramValue) {
                    if (o == null || "".equals(o)) {
                        continue;
                    }
                    pstmt1.setObject(index, o);
                    index++;
                }
            }
            rs1 = pstmt1.executeQuery();
            while (rs1.next()) {
                return rs1.getInt("C");
            }
        } catch (SQLException e) {
            throw new DBException(e);
        } finally {
            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (SQLException e1) {
                    throw new DBException(e1);
                }
            }
            if (pstmt1 != null) {
                try {
                    pstmt1.close();
                } catch (SQLException e1) {
                    throw new DBException(e1);
                }
            }
        }
        return 0;
    }

    @Override
    protected String getSearchCountSql(String sql, String countStr) throws DBException {
        StringBuffer buf = new StringBuffer();
        buf.append("SELECT ");
        if (countStr != null) {
            if (countStr.toUpperCase().indexOf("COUNT(") == 0) {
                buf.append("COUNT(").append(countStr).append(")");
            } else {
                buf.append(countStr);
            }
        } else {
            buf.append("COUNT(1)");
        }
        buf.append(" C FROM (").append(sql).append(") AS CTABLE");
        return buf.toString();
    }

    @Override
    protected String getSearchPageSql(String fieldsStr, String sql, int page, int pageSize) {
        // SELECT * FROM table ORDER BY id LIMIT 1000, 10;
        int start = (page - 1) * pageSize;
        StringBuffer pageSql = new StringBuffer(sql);
        pageSql.append(" LIMIT ").append(pageSize).append(" OFFSET ").append(start);
        log.logDebug("要执行的分页SQL语句:{}", pageSql);
        return pageSql.toString();
    }

    @Override
    protected String getSearchSql(String sql, String orderString) throws DBException {
        if (orderString == null) {
            return sql;
        }
        if (sql.toUpperCase().indexOf(" ORDER BY ") > 0) {
            log.logWarn("排序规则已存在，无法加入新规则:{}", orderString);
            return sql;
        }
        StringBuffer buf = new StringBuffer(sql);
        buf.append(" ORDER BY ").append(orderString);
        return buf.toString();
    }

    @Override
    protected void setSearchParam(PreparedStatement pstmt, Object... paramValue) throws DBException {
        int i = 1;
        for (Object o : paramValue) {
            if (o == null || "".equals(o)) {
                continue;
            }
            try {
                pstmt.setObject(i, o);
            } catch (SQLException e) {
                throw new DBException(e);
            }
            i++;
        }
    }

    @Override
    protected String getFindAllSql(String entityName, String orderString, List<String> fields) {
        StringBuffer buf = new StringBuffer("SELECT ");
        if (fields == null) {
            buf.append("*");
        } else {
            boolean noone = false;
            for (String f : fields) {
                if (noone) {
                    buf.append(",");
                }
                noone = true;
                buf.append(f);
            }
        }

        buf.append(" FROM ").append(entityName);
        if (orderString != null) {
            buf.append(" ORDER BY ").append(orderString.toUpperCase());
        }
        return buf.toString();
    }

    @Override
    protected PreparedStatement getRemoveStmt(PreparedStatement pstmt, DataList dataList) throws DBException {
        Data td = dataList.getData(0);
        setDataType(td);
        StringBuffer buf = new StringBuffer("DELETE FROM ");
        buf.append(td.getEntityName()).append(" WHERE ");
        String[] pks = td.getPrimaryKeys();
        boolean allPk = true;
        StringBuffer where = new StringBuffer();
        int m = 0;
        List<String> l = new ArrayList<String>();
        for (String pk : pks) {
            String v = td.getString(pk);
            if (v == null || "".equals(v.trim())) {
                allPk = false;
                break;
            }
            if (m > 0) {
                where.append(" AND ");
            }
            where.append(pk).append("=?");
            l.add(pk);
            m++;
        }
        if (allPk) {
            buf.append(where);
        } else {
            throw new DBException("删除条件不能为空");
        }
        try {
            pstmt = conn.prepareStatement(buf.toString());
            log.logDebug("要执行的删除SQL语句:{}", buf.toString());
            int size = dataList.size();
            for (int i = 0; i < size; i++) {
                Data d = dataList.getData(i);
                int k = 0;
                for (String pk : pks) {
                    k++;
                    setObject(pstmt, k, td.getType(pk), d.get(pk));
                }
                pstmt.addBatch();
            }
            return pstmt;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    protected PreparedStatement getStoreStmt(PreparedStatement pstmt, Data data, boolean isCreate) throws DBException {
        return setCreatePreparedStatement(pstmt, data);
    }

    @Override
    public Map<Integer, Object> prepareCall(String call, Map<Integer, Integer> returnParams, Object... params) throws DBException {
        return null;
    }

    @Override
    public int remove(String entityName, String where) throws DBException {
        // Delete FROM table_name WHERE
        StringBuffer sql = new StringBuffer("DELETE FROM ");
        sql.append(entityName).append(" ");
        if (where != null && !"".equals(where.trim())) {
            sql.append("WHERE ").append(where);
        } else {
            throw new DBException("删除语句筛选条件不能为空");
        }
        return removeExecute(sql.toString());
    }

    private int removeExecute(String sql) throws DBException {
        int r = 0;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            r = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e);
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e1) {
                    throw new DBException(e1);
                }
            }
        }
        return r;
    }

    @Override
    public int remove(String entityName, String key, String... inValue) throws DBException {
        // Delete FROM table_name WHERE
        StringBuffer sql = new StringBuffer("DELETE FROM ");
        sql.append(entityName).append(" ");
        if (key != null && !"".equals(key.trim())) {
            sql.append("WHERE ").append(key).append(" IN (");
            int i = 0;
            for (String v : inValue) {
                if (i > 0) {
                    sql.append(",");
                }
                sql.append("'").append(v).append("'");
                i++;
            }
            sql.append(")");
        } else {
            throw new DBException("删除语句筛选条件不能为空");
        }
        return removeExecute(sql.toString());
    }

    @Override
    public int removeAll(String entityName) throws DBException {
        // Delete FROM table_name WHERE
        StringBuffer sql = new StringBuffer("DELETE FROM ");
        sql.append(entityName);
        return removeExecute(sql.toString());
    }

    @Override
    public Data transportCreate(Data data) throws DBException {
        return null;
    }

    @Override
    public Data transportStore(Data data) throws DBException {
        return null;
    }

    @Override
    public Data transportRemove(Data data) throws DBException {
        return null;
    }
}
