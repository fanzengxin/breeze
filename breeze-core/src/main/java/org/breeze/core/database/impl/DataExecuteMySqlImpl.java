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
import org.breeze.core.utils.string.UUIDGenerator;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 数据操作MySql实现
 */
public class DataExecuteMySqlImpl extends BaseDataExecute {

    private Log log;

    private SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss:SSS");

    public DataExecuteMySqlImpl() {
        super();
        log = LogFactory.getLog(this.getClass());
    }

    protected Object getByte(ResultSet rs, String fieldName) throws DBException {
        try {
            return rs.getBinaryStream(fieldName);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see hx.database.databean.BaseDataExecute#getBLOB(java.sql.ResultSet,
     * java.lang.String)
     */
    @Override
    protected Object getBLOB(ResultSet rs, String fieldName) throws DBException {
        try {
            return blobToBytes(rs.getBlob(fieldName));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * 将Blob字段转换为字节数组
     *
     * @param blob
     * @return
     */
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

    /*
     * (non-Javadoc)
     *
     * @see hx.database.databean.BaseDataExecute#getBatchCreatPstmt(java.sql.
     * PreparedStatement, hx.database.databean.DataList)
     */
    @Override
    protected PreparedStatement getBatchCreatPstmt(PreparedStatement pstmt, DataList dataList) throws DBException {
        Data data = dataList.getData(0);
        String[] keys = data.getPrimaryKeys();
        // 整理Data，进行主键的自动添加
        for (String pk : keys) {
            Object v = data.get(pk);
            if (v == null || "".equals(v) || "null".equals(v)) {
                data.addData(pk, UUIDGenerator.getUUID());
            }
        }
        // 组合SQL
        // INSERT INTO 数据表 (key,key,key) VALUES (?,?,?)
        StringBuffer sql = new StringBuffer("INSERT INTO ");
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
            s.append("?");
            l.add((String) fields[i]);
        }
        sql.append(") VALUES (").append(s).append(")");
        try {
            pstmt = conn.prepareStatement(sql.toString());
            log.logDebug("要执行的插入SQL语句:{}", sql.toString());
//			setDataType(data);
            // 批量设置所有的数据
            for (Object od : dataList) {
                Data d = (Data) od;
                String[] keys1 = d.getPrimaryKeys();
                // 整理Data，进行主键的自动添加
                for (String pk : keys1) {
                    Object v = d.get(pk);
                    if (v == null || "".equals(v) || "null".equals(v)) {
                        d.addData(pk, UUIDGenerator.getUUID());
                    }
                }
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
            try {
                pstmt.setString(i, String.valueOf(value));
            } catch (Exception e) {
                try {
                    pstmt.setString(i, (String) value);
                } catch (Exception e2) {
                    pstmt.setObject(i, value);
                }
            }
        } else if ("java.sql.Timestamp".equals(typeName)) {
            if (value == null || "".equals(value)) {
                pstmt.setDate(i, null);
            } else {
                if (value instanceof Date || value instanceof java.util.Date) {
                    pstmt.setObject(i, value);
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
                || "java.lang.Long".equals(typeName) || "java.lang.Float".equals(typeName) || "java.lang.Double".equals(typeName)) {
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
                    if (((String) value).indexOf(".") > 0) {
                        try {
                            pstmt.setDouble(i, Double.parseDouble((String) value));
                        } catch (Exception e) {
                            throw new SQLException("Double格式转换失败");
                        }
                    } else {
                        try {
                            pstmt.setLong(i, Long.parseLong((String) value));
                        } catch (Exception e) {
                            throw new SQLException("Long格式转换失败");
                        }
                    }

                }

            }
        } else if ("byte[]".equals(typeName)) {
            pstmt.setObject(i, value);
        } else if ("java.sql.Blob".equals(typeName) || "java.sql.BLOB".equals(typeName)) {
            byte[] bytebuffer = new byte[1024];
            int length = 0;
            int nread = 0;
            if (value != null && !"".equals(value)) {
                try {
                    while ((nread = ((InputStream) value).read(bytebuffer)) != -1) {
                        length += nread;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    ((InputStream) value).reset();
                } catch (IOException e) {
                    System.err.println("该InputStream不支持reset方法");
                }
                pstmt.setBinaryStream(i, (InputStream) value, length);
            } else {
                InputStream ii = null;
                try {
                    ii = new ByteArrayInputStream(((String) value).getBytes());
                    try {
                        while ((nread = ii.read(bytebuffer)) != -1) {
                            length += nread;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        ii.reset();
                    } catch (IOException e) {
                        System.err.println("该InputStream不支持reset方法");
                    }
                    pstmt.setBinaryStream(i, ii, length);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (ii != null) {
                        try {
                            ii.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else if ("java.sql.Blob".equals(typeName) || "java.sql.CLOB".equals(typeName)) {
            char[] charbuffer = new char[1024];
            int length = 0;
            int nread = 0;
            try {
                while ((nread = ((Reader) value).read(charbuffer)) != -1) {
                    length += nread;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                ((Reader) value).reset();
            } catch (IOException e) {
                System.err.println("该Reader不支持reset方法");
            }
            pstmt.setCharacterStream(i, (Reader) value, length);
        } else {
            pstmt.setObject(i, value);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see hx.database.databean.BaseDataExecute#getBatchStorePstmt(java.sql.
     * PreparedStatement, hx.database.databean.DataList)
     */
    @Override
    protected PreparedStatement getBatchStorePstmt(PreparedStatement pstmt, DataList dataList) throws DBException {
        Data data = dataList.getData(0);
        // UPDATE 数据表 SET col = ?,col=? where id=?
        // 组合SQL
        StringBuffer sql = new StringBuffer("UPDATE ");
        sql.append(data.getEntityName()).append(" SET ");
        Object[] fields = data.fields();
        List<String> l = new ArrayList<String>();
        for (int i = 0; i < data.size(); i++) {
            sql.append(fields[i]).append("=? ,");
            l.add((String) fields[i]);
        }
        if (l.size() > 0) {
            sql = new StringBuffer(sql.substring(0, sql.length() - 1));
        }
        sql.append(" WHERE ");
        for (int i = 0; i < data.primaryKeySize(); i++) {
            if (i > 0) {
                sql.append(" AND ");
            }
            sql.append(data.getPrimaryKey(i)).append("=?");
            l.add(data.getPrimaryKey(i));
        }
        log.logDebug("要执行的更新SQL语句:{}", sql.toString());
        try {
            pstmt = conn.prepareStatement(sql.toString());
//			setDataType(data);
            for (Object d : dataList) {
                int si = 1;
                for (String k : l) {
                    setObject(pstmt, si, data.getType(k), ((Data) d).get(k));
                    si++;
                }
                pstmt.addBatch();
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return pstmt;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * hx.database.databean.BaseDataExecute#getCLOBString(java.sql.ResultSet ,
     * java.lang.String)
     */
    @Override
    protected Object getCLOBString(ResultSet rs, String fieldName) throws DBException {
        try {
            int y;
            StringBuffer content = new StringBuffer();
            Clob clob1;
            char[] ac = new char[299];
            Object o = rs.getClob(fieldName);
            if (o == null) {
                return "";
            }
            clob1 = (Clob) o;
            Reader reader = clob1.getCharacterStream();
            while ((y = reader.read(ac, 0, 299)) != -1) {
                content.append(new String(ac, 0, y));
            }
            return content.toString();
        } catch (Exception e) {
            throw new DBException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see hx.database.databean.BaseDataExecute#getFindAllSql(java.lang.String,
     * java.lang.String, java.util.List)
     */
    @Override
    protected String getFindAllSql(String entityName, String orderString, List<String> fields) {

        // SELECT * FROM TABLE ORDER BY
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

    /*
     * (non-Javadoc)
     *
     * @seebreeze.database.databean.BaseDataExecute#getRemoveStmt(java.sql.
     * PreparedStatement, hx.database.databean.DataList, boolean)
     */
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

    /*
     * (non-Javadoc)
     *
     * @see
     * hx.database.databean.BaseDataExecute#getSearchPageSql(java.lang.String ,
     * java.lang.String, int, int)
     */
    @Override
    protected String getSearchPageSql(String fieldsStr, String sql, int page, int pageSize) {
        // SELECT * FROM table ORDER BY id LIMIT 1000, 10;
        int start = (page - 1) * pageSize;
        StringBuffer pageSql = new StringBuffer(sql);
        pageSql.append(" LIMIT ").append(start).append(",").append(pageSize);
        log.logDebug("要执行的分页SQL语句:{}", pageSql);
        return pageSql.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see hx.database.databean.BaseDataExecute#getSearchSql(java.lang.String,
     * java.lang.String)
     */
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

    /*
     * (non-Javadoc)
     *
     * @see hx.database.databean.BaseDataExecute#getSearchSqlDataTotal(java.lang
     * .String, java.lang.String, java.lang.String, boolean, java.lang.Object[])
     */
    @Override
    protected int getSearchSqlDataTotal(String sql, String countStr, String countSql, boolean isParam, Object... paramValue)
            throws DBException {
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

    /*
     * (non-Javadoc)
     *
     * @seebreeze.database.databean.BaseDataExecute#getStoreStmt(java.sql.
     * PreparedStatement, hx.database.databean.Data, boolean)
     */
    @Override
    protected PreparedStatement getStoreStmt(PreparedStatement pstmt, Data data, boolean isCreate) throws DBException {
        // UPDATE 数据表 SET col = ?,col=? where id=?
        if (isCreate) {
            // 如果发现有没有主键的字段，则转到创建数据表中
            String[] keys = data.getPrimaryKeys();
            for (String pk : keys) {
                Object v = data.get(pk);
                if (v == null || "".equals(v) || "null".equals(v)) {
                    return setCreatePreparedStatement(pstmt, data);
                }
            }
        }
        StringBuffer sql = new StringBuffer("UPDATE ");
        sql.append(data.getEntityName()).append(" SET ");
        Object[] fields = data.fields();
        List<String> l = new ArrayList<String>();
        for (int i = 0; i < data.size(); i++) {
            sql.append(fields[i]).append("=? ,");
            l.add((String) fields[i]);
        }
        if (l.size() > 0) {
            sql = new StringBuffer(sql.substring(0, sql.length() - 1));
        }
        sql.append(" WHERE ");
        for (int i = 0; i < data.primaryKeySize(); i++) {
            if (i > 0) {
                sql.append(" AND ");
            }
            sql.append(data.getPrimaryKey(i)).append("=?");
            l.add(data.getPrimaryKey(i));
        }
        log.logDebug("要执行的更新SQL语句:{}", sql.toString());
        try {
            pstmt = conn.prepareStatement(sql.toString());
            setDataType(data);
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

    /*
     * (non-Javadoc)
     *
     * @see hx.database.databean.BaseDataExecute#setCreatePreparedStatement(java
     * .sql.PreparedStatement, hx.database.databean.Data)
     */
    @Override
    protected PreparedStatement setCreatePreparedStatement(PreparedStatement pstmt, Data data) throws DBException {
        String[] keys = data.getPrimaryKeys();
        // 整理Data，进行主键的自动添加
        for (String pk : keys) {
            Object v = data.get(pk);
            if (v == null || "".equals(v) || "null".equals(v)) {
                data.addData(pk, UUIDGenerator.getUUID());
            }
        }
        // 组合SQL
        // INSERT INTO 数据表 (key,key,key) VALUES (?,?,?)
        StringBuffer sql = new StringBuffer("INSERT INTO ");
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
            s.append("?");
            l.add((String) fields[i]);
        }
        sql.append(") VALUES (").append(s).append(")");
        try {
            pstmt = conn.prepareStatement(sql.toString());
            log.logDebug("要执行的插入SQL语句:{}", sql.toString());
//			setDataType(data);
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

    /*
     * (non-Javadoc)
     *
     * @see hx.database.databean.BaseDataExecute#setDataType(hx.database.
     * databean.Data)
     */
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
            StringBuffer buf = new StringBuffer("SELECT * FROM ").append(data.getEntityName()).append(" WHERE 1=2");
            ps = getConnection().prepareStatement(buf.toString());
            rs = ps.executeQuery();
            setDataType(data, rs);
            if (CommonConfig.isDataInfoCache()) {
                dataInfoMaps.put(data.getEntityName(), data.getDataInfos());
                dataTypeMaps.put(data.getEntityName(), data.getTypes());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("数据表[" + data.getEntityName() + "]中有不支持的字段，请修改字段类型或者使用别的方式操作数据表");
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
        for (int i = 0; i < w.getColumnCount(); i++) {
            int v = i + 1;
            if (w.getColumnTypeName(v).startsWith("TINYINT")) {
                data.setType(w.getColumnName(v).toUpperCase(), "java.lang.Long");
            } else {
                data.setType(w.getColumnName(v).toUpperCase(), w.getColumnClassName(v));
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see hx.database.databean.BaseDataExecute#setFindPageStatement(java.sql
     * .PreparedStatement, hx.database.databean.Data)
     */
    @Override
    protected PreparedStatement setFindPageStatement(PreparedStatement pstmt, Data data) throws DBException, SQLException {
        setDataType(data);
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

    /*
     * (non-Javadoc)
     *
     * @see hx.database.databean.BaseDataExecute#setFindPreparedStatement(hx
     * .database.databean.Data, java.util.List, int, int)
     */
    @Override
    protected PreparedStatement setFindPreparedStatement(PreparedStatement pstmt, Data data, List<String> fields, int pageSize,
                                                         int page) throws DBException {
        setDataType(data);
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

    /*
     * (non-Javadoc)
     *
     * @seebreeze.database.databean.BaseDataExecute#setSearchParam(java.sql.
     * PreparedStatement, java.lang.Object[])
     */
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

    /*
     * (non-Javadoc)
     *
     * @see hx.database.execute.IDataExecute#remove(java.lang.String,
     * java.lang.String)
     */
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

    /*
     * (non-Javadoc)
     *
     * @see hx.database.execute.IDataExecute#removeAll(java.lang.String)
     */
    public int removeAll(String entityName) throws DBException {
        // Delete FROM table_name WHERE
//		StringBuffer sql = new StringBuffer("DELETE FROM ");
        StringBuffer sql = new StringBuffer("TRUNCATE TABLE ");
        sql.append(entityName);
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

    /*
     * (non-Javadoc)
     *
     * @see hx.database.execute.IDataExecute#remove(java.lang.String,
     * java.lang.String, java.lang.String[])
     */
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

    /*
     * (non-Javadoc)
     *
     * @see hx.database.execute.IDataExecute#transportCreate(hx.database.
     * databean.Data)
     */
    public Data transportCreate(Data data) throws DBException {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see hx.database.execute.IDataExecute#transportRemove(hx.database.
     * databean.Data)
     */
    public Data transportRemove(Data data) throws DBException {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see hx.database.execute.IDataExecute#transportStore(hx.database.databean
     * .Data)
     */
    public Data transportStore(Data data) throws DBException {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see hx.database.databean.BaseDataExecute#createLob(hx.database.databean
     * .Data)
     */
    @Override
    protected void createLob(Data data) throws DBException {
        Data cData = new Data();
        String[] pks = data.getPrimaryKeys();
        cData.setPrimaryKey(pks);
        for (String pk : pks) {
            cData.addData(pk, data.get(pk));
        }
        Data nData = findLob(cData);
        Object[] set = data.fields();
        for (Object o : set) {
            String fieldName = (String) o;
            if ("java.sql.Blob".equals(data.getType(fieldName))) {
                Blob blob = (Blob) nData.get(fieldName);
                try {
                    // OutputStream outStream = blob.getBinaryOutputStream();
                    if (data.get(fieldName) instanceof String) {
                        blob.setBytes(1, ((String) data.get(fieldName)).getBytes());
                    } else {
                        blob.setBytes(1, (byte[]) data.get(fieldName));
                    }
                    cData.addData(fieldName, blob);
                } catch (SQLException e) {
                    throw new DBException(e);
                }
            } else if ("java.sql.Clob".equals(data.getType(fieldName))) {
                Clob clob = (Clob) nData.get(fieldName);
                try {
                    clob.setString(1, data.getString(fieldName));
                    cData.addData(fieldName, clob);
                } catch (SQLException e) {
                    throw new DBException(e);
                }
            }
        }
        storeLOB(cData);
    }

    private Data findLob(Data data) throws DBException {
        Data vd = new Data();
        if (data == null || data.getEntityName() == null || "".equals(data.getEntityName())) {
            return vd;
        }
        // SELECT * FROM 数据表 WHERE (条件) ORDER BY 字段 DESC,字段 ASC
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = setFindPreparedStatement(pstmt, data, null, 0, 0);
            rs = pstmt.executeQuery();
            ResultSetMetaData WSsmd = rs.getMetaData();
            if (rs.next()) {
                vd.setPrimaryKey(data.getPrimaryKeys());
                vd.setEntityName(data.getEntityName());
                vd.setType(data.getTypes());
                for (int i = 0; i < WSsmd.getColumnCount(); i++) {
                    Object ob = null;
                    ob = rs.getObject(WSsmd.getColumnName(i + 1));
                    vd.addData(WSsmd.getColumnName(i + 1), ob);
                }
            }
        } catch (SQLException e) {
            throw new DBException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e1) {
                    throw new DBException(e1);
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e1) {
                    throw new DBException(e1);
                }
            }
        }
        return vd;
    }

    private Data storeLOB(Data data) throws DBException {
        if (data == null || data.getEntityName() == null || "".equals(data.getEntityName().trim()) || data.size() == 0
                || data.primaryKeySize() == 0) {
            return null;
        }
        PreparedStatement pstmt = null;
        int i = 0;
        try {
            pstmt = setStoreLOBPreparedStatement(pstmt, data);
            i = pstmt.executeUpdate();
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
        if (i > 0) {
            return data;
        } else {
            return null;
        }
    }

    private PreparedStatement setStoreLOBPreparedStatement(PreparedStatement pstmt, Data data) throws DBException, SQLException {
        // UPDATE 数据表 SET col = ?,col=? where id=?
        // 组合SQL
        StringBuffer sql = new StringBuffer("UPDATE ");
        sql.append(data.getEntityName()).append(" SET ");
        Object[] fields = data.fields();
        List<String> l = new ArrayList<String>();
        for (int i = 0; i < data.size(); i++) {
            // if (!data.getPrimaryKeys().contains(fields[i])) {
            sql.append(fields[i]).append("=? ,");
            l.add((String) fields[i]);
            // }
        }
        if (l.size() > 0) {
            sql = new StringBuffer(sql.substring(0, sql.length() - 1));
        }
        sql.append(" WHERE ");
        for (int i = 0; i < data.primaryKeySize(); i++) {
            if (i > 0) {
                sql.append(" AND ");
            }
            sql.append(data.getPrimaryKey(i)).append("=?");
            l.add(data.getPrimaryKey(i));
        }
        pstmt = conn.prepareStatement(sql.toString());
        setDataType(data);
        for (int i = 0; i < l.size(); i++) {
            String t = data.getType((String) l.get(i));
            setObject(pstmt, i + 1, t, data.get(l.get(i)));
        }
        return pstmt;
    }

    /*
     * (non-Javadoc)
     *
     * @see hx.database.databean.BaseDataExecute#updateLob(hx.database.databean
     * .Data)
     */
    @Override
    protected void updateLob(Data data) throws DBException {
        createLob(data);
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
    public Map<Integer, Object> prepareCall(String call, Map<Integer, Integer> returnParams, Object... params) throws DBException {
        return null;
    }
}
