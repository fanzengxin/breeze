package org.breeze.core.database.dbinterface;

import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.exception.DBException;

import java.sql.Connection;
import java.util.List;
import java.util.Map;


/**
 * 数据库操作的接口
 *
 * @author 黑面阿呆
 */
public interface IDataExecute {
    /**
     * <p>
     * 调用存储过程或函数，暂时不支持返回值
     * </p>
     *
     * @param call
     * @param params
     * @throws DBException
     */
    Map<Integer, Object> prepareCall(String call, Map<Integer, Integer> returnParams, Object... params) throws DBException;

    /**
     * <p>
     * 批量插入
     * </p>
     *
     * @param dataList 数据集
     * @return 创建的记录数
     * @throws DBException
     */
    int batchCreate(DataList dataList) throws DBException;

    /**
     * <p>
     * 批量保存
     * </p>
     *
     * @param dataList 数据集
     * @return 保存的记录数
     * @throws DBException
     */
    int batchStore(DataList dataList) throws DBException;

    /**
     * <p>
     * 批量保存
     * </p>
     *
     * @param dataList 数据集
     * @param isCreate 如果有新记录，是否创建，true 全部创建保存；false 只修改数据，新数据忽略
     * @return 保存的记录数
     * @throws DBException
     */
    int batchStore(DataList dataList, boolean isCreate) throws DBException;

    /**
     * 插入一条新的数据
     *
     * @param data 数据
     * @return Data，查询失败返回null
     * @throws DBException
     */
    Data create(Data data) throws DBException;

    /**
     * 执行SQL，严格禁止使用此方法进行，增删改的操作
     * <br>只能进行某些特殊逻辑的数据库操作
     *
     * @param sql 标准的SQL
     * @return 执行成功，返回true
     * @throws DBException
     */
    boolean execute(String sql) throws DBException, DBException;

    /**
     * 根据Data的数据条件查询完整的数据
     *
     * @param data Data
     * @return DataMsg
     * @throws DBException
     */
    DataList find(Data data) throws DBException;

    /**
     * 根据Data的数据条件查询完整的数据
     *
     * @param data     Data
     * @param pageSize 每页显示多少条记录
     * @param page     当前页码
     * @return DataMsg
     * @throws DBException
     */
    DataList find(Data data, int pageSize, int page) throws DBException;

    /**
     * 根据Data的数据条件查询完整的数据
     *
     * @param data     Data
     * @param fields   要查询的字段
     * @param pageSize 每页显示多少条记录
     * @param page     当前页码
     * @return DataMsg
     * @throws DBException
     */
    DataList find(Data data, List<String> fields, int pageSize, int page) throws DBException;

    /**
     * 得到指定sql查询的数据
     *
     * @param sql 标准SQL
     * @return DataMsg
     * @throws DBException
     */
    DataList find(String sql) throws DBException;

    /**
     * @param sql        标准SQL
     * @param paramValue 增强的sql的？参数，参数值按顺序输入即可
     * @return
     * @throws DBException
     */
    DataList findSql(String sql, Object... paramValue) throws DBException;

    /**
     * 得到指定sql查询的数据
     *
     * @param sql      标准SQL
     * @param pageSize 每页显示多少条记录
     * @param page     当前页码
     * @return DataMsg
     * @throws DBException
     */
    DataList find(String sql, int pageSize, int page) throws DBException;

    /**
     * 得到指定sql查询的数据
     *
     * @param sql        标准SQL
     * @param pageSize   每页显示多少条记录
     * @param page       当前页码
     * @param paramValue 增强的sql的？参数，参数值按顺序输入即可
     * @return
     * @throws DBException
     */
    DataList findSqlPage(String sql, int pageSize, int page, Object... paramValue) throws DBException;

    /**
     * 得到指定sql查询的数据
     *
     * @param sql      标准SQL
     * @param pageSize 每页显示多少条记录
     * @param page     当前页码
     * @param countSql 统计总页数的sql
     * @return
     * @throws DBException
     */
    DataList findSql(String sql, int pageSize, int page, String countSql) throws DBException;

    /**
     * 得到指定sql查询的数据
     *
     * @param sql        标准SQL
     * @param pageSize   每页显示多少条记录
     * @param page       当前页码
     * @param countSql   统计总页数的sql
     * @param paramValue 增强的sql的？参数，参数值按顺序输入即可
     * @return
     * @throws DBException
     */
    DataList findSql(String sql, int pageSize, int page, String countSql, Object... paramValue) throws DBException;

    /**
     * 得到指定sql查询的数据
     *
     * @param sql      标准SQL
     * @param pageSize 每页显示多少条记录
     * @param page     当前页码
     * @param data     包含数据表名称和数据表主键的Data
     * @return DataMsg
     * @throws DBException
     */
    DataList find(String sql, int pageSize, int page, Data data) throws DBException;

    /**
     * @param sql      标准SQL
     * @param pageSize 每页显示多少条记录
     * @param page     当前页码
     * @param data     包含数据表名称和数据表主键的Data
     * @param countStr 统计总条数的Select的count语句，例如：<br>
     *                 select count(id) from tableName<br>
     *                 这里需要传"count(id)"
     * @return
     * @throws DBException
     */
    DataList find(String sql, int pageSize, int page, Data data, String countStr) throws DBException;

    /**
     * @param sql      标准SQL
     * @param pageSize 每页显示多少条记录
     * @param page     当前页码
     * @param data     包含数据表名称和数据表主键的Data
     * @param countStr 统计总条数的Select的count语句，例如：<br>
     *                 select count(id) from tableName<br>
     *                 这里需要传"count(id)"
     * @param fields   查询字段的列表
     * @return
     * @throws DBException
     */
    DataList find(String sql, int pageSize, int page, Data data, String countStr, List<String> fields) throws DBException;

    /**
     * @param sql         标准SQL
     * @param pageSize    每页显示多少条记录
     * @param page        当前页码
     * @param data        包含数据表名称和数据表主键的Data
     * @param countStr    统计总条数的Select的count语句，例如：<br>
     *                    select count(id) from tableName<br>
     *                    这里需要传"count(id)"
     * @param fields      查询字段的列表
     * @param orderString orderString
     *                    排序用的列
     * @return
     * @throws DBException
     */
    DataList find(String sql, int pageSize, int page, Data data, String countStr, List<String> fields, String orderString) throws DBException;

    /**
     * @param sql         标准SQL
     * @param pageSize    每页显示多少条记录
     * @param page        当前页码
     * @param data        包含数据表名称和数据表主键的Data
     * @param countStr    如果此参数有值，那么countSql就必须为null<br>
     *                    统计总条数的Select的count语句，例如：<br>
     *                    select count(id) from tableName<br>
     *                    这里需要传"count(id)"
     * @param fields      查询字段的列表
     * @param orderString orderString
     *                    排序用的列
     * @param countSql    统计查询结果的条数SQL
     * @return
     * @throws DBException
     */
    DataList find(String sql, int pageSize, int page, Data data, String countStr, List<String> fields, String orderString, String countSql) throws DBException;

    /**
     * @param sql         标准SQL
     * @param pageSize    每页显示多少条记录
     * @param page        当前页码
     * @param data        包含数据表名称和数据表主键的Data
     * @param countStr    如果此参数有值，那么countSql就必须为null<br>
     *                    统计总条数的Select的count语句，例如：<br>
     *                    select count(id) from tableName<br>
     *                    这里需要传"count(id)"
     * @param fields      查询字段的列表
     * @param orderString orderString
     *                    排序用的列
     * @param countSql    统计查询结果的条数SQL
     * @param isParam     sql是否带参数，只有是true时，后面的参数才生效
     * @param paramValue  增强的sql的？参数，参数值按顺序输入即可
     * @return
     * @throws DBException
     */
    DataList find(String sql, int pageSize, int page, Data data, String countStr, List<String> fields, String orderString, String countSql, boolean isParam, Object... paramValue) throws DBException;

    /**
     * 得到指定数据表所有的数据
     *
     * @param entityName  数据表名字
     * @param orderString OrderBy格式字符串，例如：ID DESC,NAME ASC
     * @return DataList
     * @throws DBException
     */
    DataList findAll(String entityName, String orderString) throws DBException;

    /**
     * 得到指定数据表的所有数据
     *
     * @param entityName  数据表名字
     * @param orderString OrderBy格式字符串，例如：ID DESC,NAME ASC
     * @param pageSize    每页显示多少条记录
     * @param page        当前页码
     * @return DataList
     * @throws DBException
     */
    DataList findAll(String entityName, String orderString, int pageSize, int page) throws DBException;

    /**
     * 得到指定数据表的所有数据
     *
     * @param entityName  数据表名字
     * @param orderString OrderBy格式字符串，例如：ID DESC,NAME ASC
     * @param pageSize    每页显示多少条记录
     * @param page        当前页码
     * @param fields      要查询的字段
     * @return DataMsg
     * @throws DBException
     */
    DataList findAll(String entityName, String orderString, int pageSize, int page, List<String> fields) throws DBException;

    /**
     * 根据主键进行查询（如果Data没有设置主键或者主键和数据表不符，或者没有主键的数据，返回null）
     *
     * @param data Data对象
     * @return Data
     * @throws DBException
     */
    Data findByPrimaryKey(Data data) throws DBException;


    /**
     * 得到数据连接
     *
     * @return Connection
     */
    Connection getConnection();

    /**
     * 删除数据
     *
     * @param data 数据（按照data中的条件进行删除）
     * @return 删除的记录数
     * @throws DBException
     */
    int remove(Data data) throws DBException;

    /**
     * 删除数据
     *
     * @param dataList 要删除的记录集
     * @return 删除的data条件数，如果每个data组成的条件是唯一记录，那么返回的就是记录数
     * @throws DBException
     */
    int remove(DataList dataList) throws DBException;

    /**
     * 删除数据
     *
     * @param entityName 数据表名字
     * @param where      删除where子句
     * @return 删除的记录数
     * @throws DBException
     */
    int remove(String entityName, String where) throws DBException;

    /**
     * 删除数据，只有一个删除条件时使用
     *
     * @param entityName 数据表名字
     * @param key        字段名字
     * @param inValue    in条件的参数
     * @return 删除的记录数
     * @throws DBException
     */
    int remove(String entityName, String key, String... inValue) throws DBException;

    /**
     * 删除数据，有2个删除条件，其中一个是固定的值，另外一个是in条件时使用
     *
     * @param entityName
     * @param key1
     * @param value1
     * @param key2
     * @param inValue
     * @return
     * @throws DBException
     */
    int remove(String entityName, String key1, String value1, String key2, String... inValue) throws DBException;

    int remove(String entityName, String key1, int value1, String key2, String... inValue) throws DBException;


    /**
     * 删除所有
     *
     * @param entityName 数据表名字
     * @return 删除的记录数
     * @throws DBException
     */
    int removeAll(String entityName) throws DBException;

    /**
     * 保存数据
     *
     * @param data 要保存的数据
     * @return 保存完成的数据
     * @throws DBException
     */
    Data store(Data data) throws DBException;

    /**
     * 保存数据
     *
     * @param data     要保存的数据
     * @param isCreate 如果是新数据是否创建
     * @return 保存完成的数据
     * @throws DBException
     */
    Data store(Data data, boolean isCreate) throws DBException;

    /**
     * 插入一条新的同步数据
     *
     * @param data 同步数据
     * @return Data，查询失败返回null
     * @throws DBException
     */
    Data transportCreate(Data data) throws DBException;

    /**
     * 保存同步数据
     *
     * @param data 要保存的同步数据
     * @return 保存完成的数据
     * @throws DBException
     */
    Data transportStore(Data data) throws DBException;

    /**
     * 删除同步数据
     *
     * @param data 要删除的数据，如果有主键的值，其它的值不需要，如果没有主键，所有的条件。
     * @return 删除的数据
     * @throws DBException
     */
    Data transportRemove(Data data) throws DBException;
}
