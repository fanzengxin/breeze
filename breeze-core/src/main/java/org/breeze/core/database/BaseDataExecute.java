package org.breeze.core.database;

import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.data.DataInfo;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.database.dbinterface.IDataExecute;
import org.breeze.core.exception.DBException;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;
import org.breeze.core.utils.date.UtilDateTime;
import org.breeze.core.utils.string.UtilString;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 基本的数据访问
 *
 * @author 黑面阿呆
 */
public abstract class BaseDataExecute implements IDataExecute {

    protected static final int IS_BLOB = 1;

    protected static final int IS_CLOB = 10;

    protected static final int IS_RAW = 100;

    /**
     * 不在后台统计记录数
     */
    public static final String NO_COUNT_DATA = "NULL";

    public static String countPassword = null;

    protected Connection conn = null;

    private Log log;

    protected static Map<String, Map<String, DataInfo>> dataInfoMaps = new HashMap<String, Map<String, DataInfo>>();
    protected static Map<String, Map<String, String>> dataTypeMaps = new HashMap<String, Map<String, String>>();

    public BaseDataExecute() {
        log = LogFactory.getLog(BaseDataExecute.class);
    }

    public void setConnection(Connection conn) {
        this.conn = conn;
    }

    /*
     * (non-Javadoc)
     *
     * @see hx.database.execute.IDataExecute#find(hx.database.databean.Data)
     */
    public DataList find(Data data) throws DBException {
        return find(data, 0, 0);
    }

    /*
     * (non-Javadoc)
     *
     * @see hx.database.execute.IDataExecute#find(hx.database.databean.Data,
     * int, int)
     */
    public DataList find(Data data, int pageSize, int page) throws DBException {
        return find(data, null, pageSize, page);
    }

    /*
     * (non-Javadoc)
     *
     * @see hx.database.execute.IDataExecute#find(hx.database.databean.Data,
     * java.util.List, int, int)
     */
    public DataList find(Data data, List<String> fields, int pageSize, int page) throws DBException {
        DataList dl = new DataList();
        // 判断条件是否正确
        if (data == null || UtilString.isNullOrEmpty(data.getEntityName())) {
            log.logError("要查询的data不合法");
            return dl;
        }
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        if (pageSize == 0 || page == 0) {
            // 不分页的查询
            try {
                pstmt = setFindPreparedStatement(pstmt, data, fields, 0, 0);
                rs = pstmt.executeQuery();
                // 将数据放到dl中
                find(rs, dl, data);
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
        } else {
            // 分页的查询
            dl.setNowPage(page);
            dl.setPageSize(pageSize);
            dl.setDataTotal(getPageTotal(data));
            dl.setCount(false);
            if (dl.getDataTotal() == 0) {
                return dl;
            }
            try {
                pstmt = setFindPreparedStatement(pstmt, data, fields, pageSize, page);
                rs = pstmt.executeQuery();
                // 将数据放到dl中
                find(rs, dl, data);
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
        }
        return dl;
    }

    /*
     * (non-Javadoc)
     *
     * @see hx.database.execute.IDataExecute#find(java.lang.String)
     */
    public DataList find(String sql) throws DBException {
        return find(sql, 0, 0);
    }

    /*
     * (non-Javadoc)
     *
     * @see hx.database.execute.IDataExecute#find(java.lang.String, int, int)
     */
    public DataList find(String sql, int pageSize, int page) throws DBException {
        return find(sql, pageSize, page, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see hx.database.execute.IDataExecute#find(java.lang.String, int, int,
     * hx.database.databean.Data)
     */
    public DataList find(String sql, int pageSize, int page, Data data) throws DBException {
        return find(sql, pageSize, page, data, null);
    }

    public DataList find(String sql, int pageSize, int page, Data data, String countStr) throws DBException {
        return find(sql, pageSize, page, data, countStr, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see hx.database.execute.IDataExecute#find(java.lang.String, int, int,
     * hx.database.databean.Data, java.lang.String, java.util.List)
     */
    public DataList find(String sql, int pageSize, int page, Data data, String countStr, List<String> fields)
            throws DBException {
        return find(sql, pageSize, page, data, countStr, fields, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see hx.database.execute.IDataExecute#find(java.lang.String, int, int,
     * hx.database.databean.Data, java.lang.String, java.util.List,
     * java.lang.String)
     */
    public DataList find(String sql, int pageSize, int page, Data data, String countStr, List<String> fields,
                         String orderString) throws DBException {
        return find(sql, pageSize, page, data, countStr, fields, orderString, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see hx.database.execute.IDataExecute#find(java.lang.String, int, int,
     * hx.database.databean.Data, java.lang.String, java.util.List,
     * java.lang.String, java.lang.String)
     */
    public DataList find(String sql, int pageSize, int page, Data data, String countStr, List<String> fields,
                         String orderString, String countSql) throws DBException {
        return find(sql, pageSize, page, data, countStr, fields, orderString, countSql, false);
    }

    @Override
    public DataList find(String sql, int pageSize, int page, Data data, String countStr, List<String> fields,
                         String orderString, String countSql, boolean isParam, Object... paramValue) throws DBException {
        DataList dl = new DataList();
        // 将sql转为大写，此对象不能用作查询的sql，只能用作sql的解析
        String upperSql = sql.toUpperCase().trim();
        // 判断sql是否是查询sql，如果不是，抛出异常
        if (sql == null || !(upperSql.startsWith("SELECT") || upperSql.startsWith("WITH"))) {
            log.logError("不合法的查询sql：{}", sql);
            throw new DBException("不合法的查询sql：" + sql);
        }
        if (data == null) {
            data = new Data();
        }
        // 自动为SQL语句的别名加上AS，为了防止高版本MYSQL别名出现问题
        if (upperSql.indexOf("SELECT") != -1 && upperSql.indexOf("FROM") != -1) {
            StringBuffer sb = new StringBuffer("SELECT ");
            String[] columns = upperSql.substring(upperSql.indexOf("SELECT") + 6, upperSql.indexOf("FROM")).split(",");
            for (int i = 0; i < columns.length; i++) {
                if (columns[i].trim().contains(" ") && !columns[i].trim().contains("AS")) {
                    sb.append(columns[i].trim().replaceAll(" ", " AS "));
                } else {
                    sb.append(columns[i].trim());
                }
                if (i < columns.length - 1) {
                    sb.append(",");
                }
                sb.append(" ");
            }
            upperSql = sb.toString() + upperSql.substring(upperSql.indexOf("FROM"));
        }
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // 执行sql
        String executeSql = getSearchSql(sql, orderString);
        // 是否分页
        if (0 >= pageSize || 0 >= page) {
            // 不分页
            log.logDebug("FIND SQL:" + executeSql);
            try {
                pstmt = conn.prepareStatement(executeSql);
                // 如果有参数，则将参数设置好
                if (isParam) {
                    setSearchParam(pstmt, paramValue);
                }
                rs = pstmt.executeQuery();
                find(rs, dl, data);
            } catch (SQLException e) {
                throw new DBException("数据操作错误：" + e.getMessage() + "；执行sql：" + executeSql, e);
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
        } else {
            // 分页
            String fieldsStr;
            long startTime = UtilDateTime.countStart("分页字段处理");
            if (fields == null) {
                // 如果没有指定查询的字段，则通过解析sql语句获取
                fieldsStr = upperSql;
                while (fieldsStr.indexOf(") AS") > 0) {
                    fieldsStr = fieldsStr.replaceAll("\\([^\\(^\\)]*\\)( AS )?", "");
                }
                fieldsStr = fieldsStr.substring(6, fieldsStr.indexOf(" FROM ")).trim();
                if (fieldsStr.indexOf(" AS ") > 0) {
                    String[] fieldArray = fieldsStr.split(",");
                    StringBuffer fbuf = new StringBuffer();
                    int fi = 0;
                    for (String f : fieldArray) {
                        int len = f.indexOf(" AS ");
                        if (len > 0) {
                            f = f.substring(len + 4);
                        }
                        if (fi > 0) {
                            fbuf.append(",").append(f);
                        } else {
                            fbuf.append(f);
                        }
                        fi++;
                    }
                    fieldsStr = fbuf.toString();
                }
                if (fieldsStr.indexOf(".") > 0) {
                    String[] fieldArray = fieldsStr.split(",");
                    StringBuffer fbuf = new StringBuffer();
                    int fi = 0;
                    for (String f : fieldArray) {
                        int len = f.indexOf(".");
                        if (len > 0) {
                            f = f.substring(len + 1);
                        }
                        if (fi > 0) {
                            fbuf.append(",").append(f);
                        } else {
                            fbuf.append(f);
                        }
                        fi++;
                    }
                    fieldsStr = fbuf.toString();
                }
            } else {
                StringBuffer buf = new StringBuffer();
                for (String f : fields) {
                    if (buf.length() > 0) {
                        buf.append(",");
                    }
                    buf.append(f);
                }
                fieldsStr = buf.toString();
            }
            UtilDateTime.countEnd("分页字段处理", startTime);
            // 得到总记录数
            // 得到总记录时，无需调用排序的sql
            // 只有没有参数的时候，才将汇总sql存入DataList
            int dataTotal = 0;
            boolean countSize = true;
            if (NO_COUNT_DATA.equals(countSql)) {
                countSize = false;
                log.logDebug("不统计查询总数");
            } else {
                dataTotal = getSearchSqlDataTotal(sql, countStr, countSql, isParam, paramValue);
                dl.setCount(true);
            }
            dl.setNowPage(page);
            dl.setPageSize(pageSize);
            dl.setDataTotal(dataTotal);
            // 如果总记录为0，则返回长度为0的DataList
            if (dataTotal == 0 && countSize) {
                return dl;
            }
            // 查询数据
            // 要执行的分页sql
            String exePageSql = getSearchPageSql(fieldsStr, executeSql, page, pageSize);
            log.logDebug("分页查询：{}", executeSql);
            try {
                pstmt = conn.prepareStatement(exePageSql);
                // 如果有参数，则将参数设置好
                if (isParam) {
                    setSearchParam(pstmt, paramValue);
                }
                rs = pstmt.executeQuery();
                find(rs, dl, data);
            } catch (SQLException e) {
                throw new DBException("数据操作错误：" + e.getMessage() + "；执行sql：" + executeSql, e);
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
        }
        return dl;
    }

    /**
     * 将查询结果放入数据集对象中
     *
     * @param rs   ResultSet
     * @param dl   DataList,结果将放在此对象中
     * @param data Data，查询的条件以及数据表的信息
     * @throws SQLException
     * @throws DBException
     */
    protected void find(ResultSet rs, DataList dl, Data data) throws SQLException, DBException {
        ResultSetMetaData WSsmd = rs.getMetaData();
        setDataType(data, rs);
        Map<String, String> types = data.getTypes();
        while (rs.next()) {
            Data vd = new Data();
            vd.setPrimaryKey(data.getPrimaryKeys());
            vd.setEntityName(data.getEntityName());
            // 如果数据类型为null，那么重新获取数据类型
            if (types == null) {
                setDataType(data);
                types = data.getTypes();
            }
            vd.setType(types);
            for (int i = 0; i < WSsmd.getColumnCount(); i++) {
                Object ob = null;
                // 解决高版本数据库无法使用字段别名的问题
                String name = WSsmd.getColumnName(i + 1);
                String lable = WSsmd.getColumnLabel(i + 1);
                String className = types.get(name.toUpperCase());
                if ("java.sql.Timestamp".equals(className)) {
                    ob = getTimestamp(rs, lable);
                } else if (className.toUpperCase().indexOf("CLOB") >= 0) {
                    ob = getCLOBString(rs, lable);
                } else if (className.toUpperCase().indexOf("BLOB") >= 0 || // ORACLE
                        // BLOB
                        className.toUpperCase().indexOf("[B") >= 0) { // MYSQL
                    // BLOB
                    ob = getBLOB(rs, lable);
                } else if ("java.lang.Long".equalsIgnoreCase(className)) {
                    ob = rs.getLong(lable);
                } else {
                    try {
                        ob = rs.getObject(lable);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                vd.addData(lable, ob);
            }
            dl.add(vd);

        }
    }

    @Override
    public DataList findSql(String sql, Object... paramValue) throws DBException {
        //find(String sql, int pageSize, int page,Data data, String countStr,List<String> fields,String orderString,String countSql,boolean isParam,Object... paramValue)
        return find(sql, 0, 0, null, null, null, null, null, true, paramValue);
    }

    @Override
    public DataList findSql(String sql, int pageSize, int page, String countSql) throws DBException {
        return find(sql, pageSize, page, null, null, null, null, countSql);
    }

    @Override
    public DataList findSql(String sql, int pageSize, int page, String countSql, Object... paramValue) throws DBException {
        return find(sql, pageSize, page, null, null, null, null, countSql, true, paramValue);
    }

    @Override
    public DataList findSqlPage(String sql, int pageSize, int page, Object... paramValue) throws DBException {
        return find(sql, pageSize, page, null, null, null, null, null, true, paramValue);
    }


    /*
     * (non-Javadoc)
     *
     * @see hx.database.execute.IDataExecute#findAll(java.lang.String,
     * java.lang.String)
     */
    public DataList findAll(String entityName, String orderString) throws DBException {
        return findAll(entityName, orderString, 0, 0);
    }

    /*
     * (non-Javadoc)
     *
     * @see hx.database.execute.IDataExecute#findAll(java.lang.String,
     * java.lang.String, int, int)
     */
    public DataList findAll(String entityName, String orderString, int pageSize, int page) throws DBException {

        return findAll(entityName, orderString, pageSize, page, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see hx.database.execute.IDataExecute#findAll(java.lang.String,
     * java.lang.String, int, int, java.util.List)
     */
    public DataList findAll(String entityName, String orderString, int pageSize, int page, List<String> fields)
            throws DBException {
        String sql = getFindAllSql(entityName, orderString, fields);
        Data d = new Data();
        d.setEntityName(entityName);
        return find(sql, pageSize, page, d, null, fields);
    }

    /**
     * 得到查询的sql
     *
     * @param entityName
     * @param orderString
     * @param fields
     */
    protected abstract String getFindAllSql(String entityName, String orderString, List<String> fields);

    public int batchCreate(DataList dataList) throws DBException {
        if (dataList == null || dataList.size() == 0) {
            log.logWarn("要保存的dataList为空");
            return 0;
        }
        // 设置数据类型
        long ls = UtilDateTime.countStart("设置数据类型");
        Data dataNo1 = dataList.getData(0);
        setDataType(dataNo1);
        UtilDateTime.countEnd("设置数据类型", ls);
        //在插入之前调用校验类
        PreparedStatement pstmt = null;
        try {
            long l = UtilDateTime.countStart("批量插入执行");
            pstmt = getBatchCreatPstmt(pstmt, dataList);
            pstmt.executeBatch();
            UtilDateTime.countEnd("批量插入执行", l);
            return dataList.size();
        } catch (Exception ex) {
            throw new DBException(ex);
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e1) {
                    throw new DBException(e1);
                }
            }
        }
    }

    /**
     * 得到批量插入的PreparedStatement<br>
     * <p>
     * 参考实现：
     * </p>
     * pstmt = conn.preparedStatement(sql);<br>
     * for(Data d:dataList){<br>
     * pstmt.setXXX(1,xxx);<br>
     * ...<br>
     * pstmt.addBatch();<br>
     * }<br>
     * ps.executeBatch();//此代码不需要实现，在父类中已经调用 <br>
     *
     * @param pstmt    null的PreparedStatement
     * @param dataList 数据集
     * @return PreparedStatement
     */
    protected abstract PreparedStatement getBatchCreatPstmt(PreparedStatement pstmt, DataList dataList)
            throws DBException;

    public int batchStore(DataList dataList) throws DBException {
        return batchStore(dataList, false);
    }

    public int batchStore(DataList dataList, boolean isCreate) throws DBException {
        if (dataList == null || dataList.size() == 0) {
            log.logWarn("要修改的dataList为空");
            return 0;
        }
        // 设置数据类型
        long ls = UtilDateTime.countStart("设置数据类型");
        Data dataNo1 = dataList.getData(0);
        setDataType(dataNo1);
        UtilDateTime.countEnd("设置数据类型", ls);
        //在插入之前调用校验类
        DataList newdl = new DataList();
        DataList dl = new DataList();
        if (dataList == null || dataList.size() == 0) {
            log.logWarn("要修改的dataList为空");
            return 0;
        }
        for (Object o : dataList) {
            boolean isNew = false;
            Data d = (Data) o;
            String[] keys = d.getPrimaryKeys();
            for (String key : keys) {
                if (d.get(key) == null) {
                    isNew = true;
                    break;
                }
            }
            if (isNew) {
                newdl.add(d);
            } else {
                dl.add(d);
            }
        }
        // 过滤新数据，将新数据进行处理
        if (isCreate) {
            if (newdl.size() > 0) {
                batchCreate(newdl);
            }
            if (dl.size() == 0) {
                return newdl.size();
            }
        } else {
            if (newdl.size() > 0) {
                log.logError("dataList数据主键不存在");
            }
            if (dl.size() == 0) {
                return 0;
            }
        }
        // 执行插入操作
        PreparedStatement pstmt = null;
        try {
            long l = UtilDateTime.countStart("批量修改执行");
            pstmt = getBatchStorePstmt(pstmt, dl);
            pstmt.executeBatch();
            UtilDateTime.countEnd("批量修改执行", l);
            return dataList.size();
        } catch (Exception ex) {
            throw new DBException(ex);
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e1) {
                    throw new DBException(e1);
                }
            }
        }
    }

    /**
     * 得到批量保存的PreparedStatement
     * <p>
     * 参考实现：
     * </p>
     * pstmt = conn.preparedStatement(sql);<br>
     * for(Data d:dataList){<br>
     * pstmt.setXXX(1,xxx);<br>
     * ...<br>
     * pstmt.addBatch();<br>
     * }<br>
     * ps.executeBatch();//此代码不需要实现，在父类中已经调用 <br>
     *
     * @param pstmt    null的PreparedStatement
     * @param dataList 数据集
     * @return PreparedStatement
     */
    protected abstract PreparedStatement getBatchStorePstmt(PreparedStatement pstmt, DataList dataList)
            throws DBException;

    /*
     * (non-Javadoc)
     *
     * @see hx.database.execute.IDataExecute#create(hx.database.databean. Data)
     */
    public Data create(Data data) throws DBException {
        if (data == null || data.getEntityName() == null || "".equals(data.getEntityName().trim()) || data.size() == 0) {
            log.logError("要保存的data不合法:", data.toJsonStr());
            return null;
        }
        PreparedStatement pstmt = null;
        // 设置数据类型
        long l = UtilDateTime.countStart("设置数据类型");
        setDataType(data);
        UtilDateTime.countEnd("设置数据类型", l);
        //在插入之前调用校验类
        // 更新的数据
        int upint;
        try {
            l = UtilDateTime.countStart("数据插入");
            pstmt = setCreatePreparedStatement(pstmt, data);
            upint = pstmt.executeUpdate();
            UtilDateTime.countEnd("数据插入", l);
        } catch (DBException e) {
            throw new DBException(e);
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
        // 更新大字段
        if (upint > 0) {
            if (data.hasLob()) {
                createLob(data);
            }
            return data;
        } else {
            return null;
        }
    }

    /**
     * 设置Data的表的数据类型 <br>
     * 将data中数据表的类型存入data中
     *
     * @param data
     * @throws DBException
     */
    protected abstract void setDataType(Data data) throws DBException;

//	protected abstract void getDataInfo() throws DBException;

    protected abstract void setDataType(Data data, ResultSet rs) throws DBException, SQLException;

    /**
     * 创建LOB字段的数据
     *
     * @param data 数据
     * @throws DBException
     */
    protected abstract void createLob(Data data) throws DBException;

    /**
     * 更新LOB字段的数据
     *
     * @param data
     * @throws DBException
     */
    protected abstract void updateLob(Data data) throws DBException;

    /**
     * 设置创建数据的Statement <br>
     *
     * @param pstmt 要设置的Statement
     * @param data  数据，必须带数据类型
     * @return PreparedStatement
     * @throws DBException
     */
    protected abstract PreparedStatement setCreatePreparedStatement(PreparedStatement pstmt, Data data)
            throws DBException;

    public boolean execute(String sql) throws DBException {
        Statement stmt = null;
        boolean b = false;
        try {
            stmt = conn.createStatement();
            b = stmt.execute(sql);
        } catch (SQLException e) {
            throw new DBException(e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e1) {
                    throw new DBException(e1);
                }
            }
        }
        return b;
    }

    /**
     * 设置查询的PreparedStatement,并将字段的类型设置到data中。
     *
     * @param data     数据对象
     * @param fields   要查询的列集合
     * @param pageSize 每页多少条记录
     * @param page     当前页
     * @return PreparedStatement
     * @throws DBException
     */
    protected abstract PreparedStatement setFindPreparedStatement(PreparedStatement pstmt, Data data,
                                                                  List<String> fields, int pageSize, int page) throws DBException;


    /**
     * 得到总记录数，主要是find(data)中调用
     *
     * @param data
     * @return
     * @throws DBException
     */
    protected int getPageTotal(Data data) throws DBException {
        int pageTotal = 0;
        PreparedStatement pstmt1 = null;
        ResultSet rs1 = null;
        try {
            pstmt1 = setFindPageStatement(pstmt1, data);
            rs1 = pstmt1.executeQuery();
            while (rs1.next()) {
                pageTotal = rs1.getInt("C");
            }
        } catch (SQLException e) {
            throw new DBException(e);
        } catch (DBException e) {
            throw e;
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
        return pageTotal;
    }

    /**
     * 设置查询总页数的 PreparedStatement
     *
     * @param pstmt PreparedStatement
     * @param data  Data
     * @return
     * @throws DBException
     * @throws SQLException
     */
    protected abstract PreparedStatement setFindPageStatement(PreparedStatement pstmt, Data data) throws DBException,
            SQLException;

    /**
     * 得到TIMESTAMP类型的数据
     *
     * @param rs        数据结果集
     * @param fieldName 字段名称
     * @return CLOB结果的对象
     * @throws DBException
     */
    protected abstract Object getTimestamp(ResultSet rs, String fieldName) throws DBException;

    /**
     * 得到CLOB类型的数据
     *
     * @param rs        数据结果集
     * @param fieldName 字段名称
     * @return CLOB结果的对象
     * @throws DBException
     */
    protected abstract Object getCLOBString(ResultSet rs, String fieldName) throws DBException;

    /**
     * 得到BLOB类型的数据
     *
     * @param rs        数据结果集
     * @param fieldName 字段名称
     * @return BLOB结果的对象
     * @throws DBException
     */
    protected abstract Object getBLOB(ResultSet rs, String fieldName) throws DBException;

    /**
     * 得到以sql为条件的查询的总记录数，主要是find(sql)使用
     *
     * @param sql        查询的sql语句
     * @param countStr   count的字符串
     * @param countSql   count的sql，如果此参数不为空，则优先使用此参数
     * @param isParam    sql是否带参数，只有是true时，后面的参数才生效
     * @param paramValue 参数
     * @return 总记录数
     * @throws DBException
     */
    protected abstract int getSearchSqlDataTotal(String sql, String countStr, String countSql, boolean isParam,
                                                 Object... paramValue) throws DBException;

    /**
     * 得到搜索的汇总sql
     *
     * @param sql
     * @param countStr
     * @return
     * @throws DBException
     */
    protected abstract String getSearchCountSql(String sql, String countStr) throws DBException;

    /**
     * 得到分页的sql
     *
     * @param fieldsStr 查询的字段字符串
     * @param sql       查询的sql语句
     * @param page      当前页
     * @param pageSize  每页数据数
     * @return 查询的sql
     */
    protected abstract String getSearchPageSql(String fieldsStr, String sql, int page, int pageSize);

    /**
     * 得到要查询的sql语句
     *
     * @param sql         基本的sql语句
     * @param orderString 扩展的排序语句
     * @return 要执行的sql语句
     * @throws DBException
     */
    protected abstract String getSearchSql(String sql, String orderString) throws DBException;

    /**
     * 设置查询sql语句的参数
     *
     * @param pstmt      PreparedStatement
     * @param paramValue sql语句的参数
     */
    protected abstract void setSearchParam(PreparedStatement pstmt, Object... paramValue) throws DBException;

    /*
     * (non-Javadoc)
     *
     * @see hx.database.execute.IDataExecute#findByPrimaryKey(hx.database
     * .databean.Data)
     */
    public Data findByPrimaryKey(Data data) throws DBException {
        if (data == null) {
            log.logError("待查询的data不能为空");
            throw new DBException("待查询的data不能为空");
        }
        String entityName = data.getEntityName();
        String[] keys = data.getPrimaryKeys();
        if (entityName == null) {
            log.logError("待查询的data表名不能为空");
            throw new DBException("待查询的data表名不能为空");
        }
        if (keys == null || keys.length == 0) {
            log.logError("待查询的data主键不能为空");
            throw new DBException("待查询的data主键不能为空");
        }
        Data d = new Data();
        d.setEntityName(entityName);
        d.setPrimaryKey(keys);
        for (String key : keys) {
            Object o = data.get(key);
            if (o == null) {
                log.logError("待查询的data主键值不能为空");
                throw new DBException("待查询的data主键值不能为空");
            }
            d.add(key, o);
            log.logDebug("findByPrimaryKey:PrimaryKey={}", key);
        }
        log.logDebug("findByPrimaryKey:data=" + d);
        DataList dl = find(d);
        int len = dl.size();
        if (len == 0) {
            return null;
        }
        if (len > 1) {
            log.logWarn("通过主键查询到多个值");
        }
        return dl.getData(0);
    }

    /*
     * (non-Javadoc)
     *
     * @see hx.database.execute.IDataExecute#getConnection()
     */
    @Override
    public Connection getConnection() {
        return conn;
    }


    /**
     * 执行sql
     *
     * @param sql sql语句
     * @return 成功返回true
     * @throws DBException
     */
    protected boolean executeSql(String sql) throws DBException {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            return stmt.execute(sql);
        } catch (Exception e) {
            throw new DBException("数据操作错误：" + e.getMessage() + "；执行sql：" + sql, e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e1) {
                    throw new DBException(e1);
                }
            }
        }
    }

    /**
     * 执行多条Sql语句
     *
     * @param sql 要执行sql语句
     * @return 执行成功返回true
     */
    public boolean batchExecuteSql(String[] sql) throws DBException {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            int len = sql.length;
            for (int i = 0; i < len; i++, stmt.addBatch(sql[i]))
                ;
            stmt.executeBatch();// 执行
            return (true);
        } catch (Exception ex) {
            throw new DBException("数据操作错误：" + ex.getMessage() + "；执行sql：" + sql, ex);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e1) {
                    throw new DBException(e1);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see hx.database.execute.IDataExecute#store(hx.database.databean.Data)
     */
    @Override
    public Data store(Data data) throws DBException {
        return store(data, false);
    }

    @Override
    public Data store(Data data, boolean isCreate) throws DBException {
        PreparedStatement pstmt = null;
        int upint = 0;
        try {
            // 设置数据类型
            long ls = UtilDateTime.countStart("设置数据类型");
            setDataType(data);
            UtilDateTime.countEnd("设置数据类型", ls);
            //在插入之前调用校验类
            long l = UtilDateTime.countStart("保存数据");
            pstmt = getStoreStmt(pstmt, data, isCreate);
            upint = pstmt.executeUpdate();
            UtilDateTime.countEnd("保存数据", l);
        } catch (Exception ex) {
            throw new DBException("数据操作错误：" + ex.getMessage(), ex);
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e1) {
                    throw new DBException(e1);
                }
            }
        }
        // 更新大字段
        if (upint > 0) {
            if (data.hasLob()) {
                updateLob(data);
            }
            return data;
        } else {
            return data;
        }
    }

    protected abstract PreparedStatement getStoreStmt(PreparedStatement pstmt, Data data, boolean isCreate)
            throws DBException;

    /*
     * (non-Javadoc)
     *
     * @see hx.database.execute.IDataExecute#remove(hx.database.databean. Data)
     */
    @Override
    public int remove(Data data) throws DBException {
        DataList dl = new DataList(0);
        dl.add(data);
        return remove(dl);
    }

    /*
     * (non-Javadoc)
     *
     * @see hx.database.execute.IDataExecute#remove(hx.database.databean.
     * DataList)
     */
    public int remove(DataList dataList) throws DBException {
        if (dataList == null || dataList.size() == 0) {
            log.logWarn("要删除的dataList为空");
            return 0;
        }
        PreparedStatement pstmt = null;
        try {
            pstmt = getRemoveStmt(pstmt, dataList);
            pstmt.executeBatch();
            return dataList.size();
        } catch (Exception ex) {
            throw new DBException(ex);
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e1) {
                    throw new DBException(e1);
                }
            }
        }
    }

    public int remove(String entityName, String key1, String value1, String key2, String... inValue) throws DBException {
        // Delete FROM table_name WHERE
        StringBuffer sql = new StringBuffer("DELETE FROM ");
        sql.append(entityName).append(" ");
        if (key1 != null && !"".equals(key1.trim())) {
            sql.append("WHERE ");
            if (value1 == null) {
                sql.append(key1).append(" IS NULL");
            } else {
                sql.append(key1).append(" = '").append(value1).append("'");
            }
            sql.append(" AND ");
        } else {
            sql.append("WHERE ");
        }
        if (key2 != null && !"".equals(key2.trim())) {
            sql.append(key2).append(" IN (");
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
            throw new DBException("删除的筛选条件为空");
        }
        return removeExecute(sql.toString());
    }

    public int remove(String entityName, String key1, int value1, String key2, String... inValue) throws DBException {
        StringBuffer sql = new StringBuffer("DELETE FROM ");
        sql.append(entityName).append(" ");
        if (key1 != null && !"".equals(key1.trim()) && value1 != 0) {
            sql.append("WHERE ");
            sql.append(key1).append(" = ").append(value1);
            sql.append(" AND ");
        } else {
            sql.append("WHERE ");
        }
        if (key2 != null && !"".equals(key2.trim())) {
            sql.append(key2).append(" IN (");
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
            throw new DBException("删除的筛选条件为空");
        }
        return removeExecute(sql.toString());
    }

    /**
     * 得到删除sql的PreparedStatement
     *
     * @param pstmt    (null)空的PreparedStatement
     * @param dataList 条件的DataList
     * @return PreparedStatement
     */
    protected abstract PreparedStatement getRemoveStmt(PreparedStatement pstmt, DataList dataList) throws DBException;

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
}
