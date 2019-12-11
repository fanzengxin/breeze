package org.breeze.core.database.dbinterface;

import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.exception.DBException;

import java.sql.Connection;


/**
 * 增加、修改、删除的扩展操作<br>
 * 可以通过此扩展操作，实现数据操作时的特殊处理
 *
 * @author 黑面阿呆
 */
public interface IDBExtend {
    /**
     * <p>
     * 批量插入的特殊处理
     * </p>
     *
     * @param conn     Connection
     * @param dataList 数据集
     * @return 返回true时，操作才会继续
     * @throws DBException
     */
    boolean batchCreate(Connection conn, DataList dataList) throws DBException;

    /**
     * <p>
     * 批量保存的特殊处理
     * </p>
     *
     * @param conn     Connection
     * @param dataList 数据集
     * @param isCreate 如果有新记录，是否创建，true 全部创建保存；false 只修改数据，新数据忽略
     * @return 返回true时，操作才会继续
     * @throws DBException
     */
    boolean batchStore(Connection conn, DataList dataList, boolean isCreate) throws DBException;

    /**
     * 插入时的特殊处理
     *
     * @param conn Connection
     * @param data
     * @return 返回true时，操作才会继续
     * @throws DBException
     */
    boolean create(Connection conn, Data data) throws DBException;


    /**
     * 删除数据的特殊处理
     *
     * @param conn     Connection
     * @param dataList 要删除的记录集
     * @return 返回true时，操作才会继续
     * @throws DBException
     */
    boolean remove(Connection conn, DataList dataList) throws DBException;


    /**
     * 删除数据的特殊处理
     *
     * @param conn       Connection
     * @param entityName 数据表名字
     * @param where      删除where子句
     * @return 返回true时，操作才会继续
     * @throws DBException
     */
    boolean remove(Connection conn, String entityName, String where) throws DBException;

    /**
     * 删除数据，只有一个删除条件时使用,的特殊处理
     *
     * @param conn       Connection
     * @param entityName 数据表名字
     * @param key        字段名字
     * @param inValue    in条件的参数
     * @return 返回true时，操作才会继续
     * @throws DBException
     */
    boolean remove(Connection conn, String entityName, String key, String... inValue) throws DBException;

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
    boolean remove(Connection conn, String entityName, String key1, String value1, String key2, String... inValue) throws DBException;

    boolean remove(Connection conn, String entityName, String key1, int value1, String key2, String... inValue) throws DBException;

    /**
     * 删除所有的特殊处理
     *
     * @param conn       Connection
     * @param entityName 数据表名字
     * @return 返回true时，操作才会继续
     * @throws DBException
     */
    boolean removeAll(Connection conn, String entityName) throws DBException;

    /**
     * 保存数据的特殊处理
     *
     * @param conn     Connection
     * @param data     要保存的数据
     * @param isCreate 如果是新数据是否创建
     * @return 返回true时，操作才会继续
     * @throws DBException
     */
    boolean store(Connection conn, Data data, boolean isCreate) throws DBException;

    /**
     * 插入一条新的同步数据的特殊处理
     *
     * @param conn Connection
     * @param data 同步数据
     * @return 返回true时，操作才会继续
     * @throws DBException
     */
    boolean transportCreate(Connection conn, Data data) throws DBException;

    /**
     * 删除同步数据的特殊处理
     *
     * @param conn Connection
     * @param data 要删除的数据，如果有主键的值，其它的值不需要，如果没有主键，所有的条件。
     * @return 返回true时，操作才会继续
     * @throws DBException
     */
    boolean transportRemove(Connection conn, Data data) throws DBException;

    /**
     * 保存同步数据的特殊处理
     *
     * @param conn Connection
     * @param data 要保存的同步数据
     * @return 返回true时，操作才会继续
     * @throws DBException
     */
    boolean transportStore(Connection conn, Data data) throws DBException;
}
