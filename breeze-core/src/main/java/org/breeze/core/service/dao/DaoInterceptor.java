package org.breeze.core.service.dao;


import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.breeze.core.annotation.repository.Repository;
import org.breeze.core.annotation.repository.Select;
import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.constant.OperationMethod;
import org.breeze.core.database.DataBaseFactory;
import org.breeze.core.database.dbinterface.IDataExecute;
import org.breeze.core.database.manager.ConnectionManager;
import org.breeze.core.exception.DBException;
import org.breeze.core.exception.NoSerialException;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;
import org.breeze.core.service.service.ServiceInterceptor;
import org.breeze.core.utils.cache.UtilRedis;
import org.breeze.core.utils.date.UtilDateTime;
import org.breeze.core.utils.string.UtilSqlFormat;
import org.breeze.core.utils.string.UtilString;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 数据库操作相关AOP
 * @Auther: 黑面阿呆
 * @Date: 2019/2/19 22:45
 * @Version: 1.0.0
 */
public class DaoInterceptor implements MethodInterceptor {

    private static Log log = LogFactory.getLog(DaoInterceptor.class);

    private static final String SQL_FIND_NOT_COUNT = "NULL";
    // 默认缓存redis key前缀
    private static final String DATA_CACHE = "DATA_CACHE_";
    // 默认缓存Redis库 1
    private static final int DATA_CACHE_DB = 1;
    // 默认缓存时间1小时
    private static final int DATA_CACHE_TIME = 3600;

    /**
     * 数据库操作AOP
     *
     * @param object
     * @param method
     * @param objects
     * @param methodProxy
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object object, Method method, Object[] objects, MethodProxy methodProxy) throws Exception {
        // 获取query注解信息
        Select select = method.getAnnotation(Select.class);
        Serial serial = null;
        if (object != null) {
            for (int i = 0; i < objects.length; i++) {
                if (objects[i] instanceof Serial) {
                    serial = (Serial) objects[i];
                }
            }
        }
        if (serial == null) {
            log.logError("日志序列号serial对象不能为空");
            throw new NoSerialException("日志序列号serial对象不能为空");
        }
        // 重新整理请求参数，获取日志序列号等
        Map<String, Object> fieldParam = new HashMap<String, Object>();
        Map<Class<?>, List<Object>> classParam = new HashMap<Class<?>, List<Object>>();
        Connection conn = ServiceInterceptor.getServiceConnection(serial);
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < objects.length; i++) {
            fieldParam.put(parameters[i].getName(), objects[i]);
            List list = classParam.get(parameters[i].getType());
            if (list == null) {
                list = new ArrayList();
            }
            list.add(objects[i]);
            classParam.put(parameters[i].getType(), list);
        }
        // 判断是否开启缓存
        if (UtilString.isNotEmpty(select.cache())) {
            // 如果获取到缓存数据，则直接返回
            DataList dataList = UtilRedis.getRedis(DATA_CACHE_DB).getDataList(DATA_CACHE + method.getDeclaringClass().getName()
                    + "_" + select.cache(), serial);
            if (dataList != null) {
                return dataList;
            }
        }
        // 获取执行的class
        Class<?>[] interfaces = object.getClass().getInterfaces();
        if (interfaces.length == 0) {
            log.logError("获取Dao层执行方法实例失败", serial);
            throw new DBException("获取Dao层执行方法实例失败");
        }
        Class<?> instance = interfaces[0];
        Repository repository = instance.getAnnotation(Repository.class);
        if (repository == null) {
            log.logError("获取Dao层Reository注解失败", serial);
            throw new DBException("获取Dao层Reository注解失败");
        }
        Object result = doRepository(conn, select, instance, fieldParam, classParam, repository, serial);
        if (method.getReturnType().equals(Data.class) && result instanceof DataList) {
            DataList dl = (DataList) result;
            if (dl.size() == 0) {
                return null;
            } else if (dl.size() == 1) {
                return dl.getData(0);
            } else {
                log.logError("单数据查询结果为多记录", serial);
                throw new DBException("单数据查询结果为多记录");
            }
        }
        return result;
    }

    /**
     * 处理数据库请求
     *
     * @param conn       数据库链接
     * @param select     sql注解
     * @param instance   执行实例对象
     * @param fieldParam 按参数名Map
     * @param classParam 按参数类型Map
     * @param repository 数据表明
     * @param serial     日志唯一序列
     * @return
     */
    private Object doRepository(Connection conn, Select select, Class<?> instance, Map<String, Object> fieldParam,
                                Map<Class<?>, List<Object>> classParam, Repository repository, Serial serial) throws DBException {
        boolean daoConn = false;
        try {
            if (conn == null) {
                conn = ConnectionManager.getConnection(repository.connection());
                daoConn = true;
            }
            IDataExecute ide = DataBaseFactory.getDataBase(conn);
            DataList result = null;
            switch (select.type()) {
                case OperationMethod.FIND:
                    // 查询
                    result = findSql(ide, select.sql(), fieldParam);
                    break;
                case OperationMethod.FIND_PAGE:
                    // 分页查询
                    result = findSqlPage(ide, select.sql(), fieldParam, true);
                    break;
                case OperationMethod.FIND_PAGE_NOCOUNT:
                    // 分页不求和
                    result = findSqlPage(ide, select.sql(), fieldParam, false);
                    break;
                case OperationMethod.FIND_BY_DATA:
                    List fdList = classParam.get(Data.class);
                    if (fdList == null || fdList.size() == 0) {
                        return false;
                    }
                    // 条件查询
                    result = getByData(ide, repository, (Data) fdList.get(0));
                    break;
                case OperationMethod.BATCH_SAVE:
                    List bsList = classParam.get(DataList.class);
                    if (bsList == null || bsList.size() == 0) {
                        return -1;
                    }
                    // 批量保存
                    int btSave = batchSave(ide, repository, (DataList) bsList.get(0));
                    emptyCacheData(instance, serial);
                    return btSave;
                case OperationMethod.SAVE:
                    List sList = classParam.get(Data.class);
                    if (sList == null || sList.size() == 0) {
                        return false;
                    }
                    // 单条保存
                    boolean save = save(ide, repository, (Data) sList.get(0));
                    emptyCacheData(instance, serial);
                    return save;
                case OperationMethod.BATCH_UPDATE:
                    List buList = classParam.get(DataList.class);
                    if (buList == null || buList.size() == 0) {
                        return -1;
                    }
                    // 批量更新
                    int btUpdate = batchUpdate(ide, repository, (DataList) buList.get(0));
                    emptyCacheData(instance, serial);
                    return btUpdate;
                case OperationMethod.UPDATE:
                    List uList = classParam.get(Data.class);
                    if (uList == null || uList.size() == 0) {
                        return -1;
                    }
                    // 单条更新
                    boolean update = update(ide, repository, (Data) uList.get(0));
                    emptyCacheData(instance, serial);
                    return update;
                case OperationMethod.BATCH_REMOVE:
                    List brList = classParam.get(DataList.class);
                    if (brList == null || brList.size() == 0) {
                        return -1;
                    }
                    // 批量删除
                    int btRemove = batchRemove(ide, repository, (DataList) brList.get(0));
                    emptyCacheData(instance, serial);
                    return btRemove;
                case OperationMethod.REMOVE:
                    List rList = classParam.get(Data.class);
                    if (rList == null || rList.size() == 0) {
                        return -1;
                    }
                    // 单条删除
                    int remove = remove(ide, repository, (Data) rList.get(0));
                    emptyCacheData(instance, serial);
                    return remove;
                default:
                    log.logError("未知类型的数据库操作：{}", select.type());
                    break;
            }
            String cache = select.cache();
            if (UtilString.isNotEmpty(cache) && result != null) {
                // 缓存结果数据
                UtilRedis.getRedis(DATA_CACHE_DB).setexDataList(DATA_CACHE + instance.getName() + "_" + cache,
                        DATA_CACHE_TIME, result, serial);
            }
            return result;
        } catch (Exception e) {
            log.logError("数据查询失败", e, serial);
            throw new DBException(e);
        } finally {
            try {
                if (daoConn && conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                log.logError("数据库链接关闭失败", e);
            }
        }
    }

    /**
     * 查询语句
     *
     * @param ide   SQL执行器
     * @param sql   sql语句
     * @param param 参数
     * @return
     * @throws DBException
     */
    private DataList findSql(IDataExecute ide, String sql, Map<String, Object> param) throws DBException {
        Map<String, Object> result = UtilSqlFormat.prepareSql(sql, param);
        return ide.findSql(result.get("sql").toString(), ((List) (result.get("param"))).toArray());
    }

    /**
     * 分页查询语句
     *
     * @param ide
     * @param sql
     * @param fieldParam
     * @param count
     * @return
     * @throws DBException
     */
    private DataList findSqlPage(IDataExecute ide, String sql, Map<String, Object> fieldParam, boolean count) throws DBException {
        int page = (int) fieldParam.get("page");
        int pageSize = (int) fieldParam.get("pageSize");
        Map<String, Object> result = UtilSqlFormat.prepareSql(sql, fieldParam);
        String execute = result.get("sql").toString();
        if (count) {
            // 统计查询总数
            return ide.findSqlPage(execute, pageSize, page, ((List) (result.get("param"))).toArray());
        } else {
            // 不统计查询总数
            return ide.findSqlPage(execute, pageSize, page, SQL_FIND_NOT_COUNT, ((List) (result.get("param"))).toArray());
        }
    }

    /**
     * 保存数据
     *
     * @param ide
     * @param repository
     * @param find
     * @return
     * @throws DBException
     */
    private DataList getByData(IDataExecute ide, Repository repository, Data find) throws DBException {
        if (find != null) {
            if (UtilString.isNullOrEmpty(find.getEntityName())) {
                find.setEntityName(repository.tableName());
            }
            return ide.find(find);
        } else {
            return new DataList();
        }
    }

    /**
     * 批量保存数据
     *
     * @param ide
     * @param repository
     * @param batchSave
     * @return
     * @throws DBException
     */
    private int batchSave(IDataExecute ide, Repository repository, DataList batchSave) throws DBException {
        if (batchSave != null && batchSave.size() > 0) {
            for (int i = 0; i < batchSave.size(); i++) {
                Data create = batchSave.getData(i);
                if (i == 0 && UtilString.isNullOrEmpty(create.getEntityName())) {
                    create.setEntityName(repository.tableName());
                }
                if (repository.createTime()) {
                    create.add("create_time", UtilDateTime.currentTimeMillis());
                }
            }
            return ide.batchCreate(batchSave);
        }
        return 0;
    }

    /**
     * 保存数据
     *
     * @param ide
     * @param repository
     * @param save
     * @return
     * @throws DBException
     */
    private boolean save(IDataExecute ide, Repository repository, Data save) throws DBException {
        if (save != null) {
            if (UtilString.isNullOrEmpty(save.getEntityName())) {
                save.setEntityName(repository.tableName());
            }
            if (repository.createTime()) {
                save.add("create_time", UtilDateTime.currentTimeMillis());
            }
            Data data = ide.create(save);
            if (data != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 批量更新数据
     *
     * @param ide
     * @param repository
     * @param batchUpdate
     * @return
     * @throws DBException
     */
    private int batchUpdate(IDataExecute ide, Repository repository, DataList batchUpdate) throws DBException {
        if (batchUpdate != null && batchUpdate.size() > 0) {
            for (int i = 0; i < batchUpdate.size(); i++) {
                Data update = batchUpdate.getData(i);
                if (i == 0 && UtilString.isNullOrEmpty(update.getEntityName())) {
                    update.setEntityName(repository.tableName());
                }
                if (repository.updateTime()) {
                    update.add("update_time", UtilDateTime.currentTimeMillis());
                }
            }
            return ide.batchStore(batchUpdate);
        }
        return 0;
    }

    /**
     * 更新数据
     *
     * @param ide
     * @param repository
     * @param update
     * @return
     * @throws DBException
     */
    private boolean update(IDataExecute ide, Repository repository, Data update) throws DBException {
        if (update != null) {
            if (UtilString.isNullOrEmpty(update.getEntityName())) {
                update.setEntityName(repository.tableName());
            }
            if (repository.updateTime()) {
                update.add("update_time", UtilDateTime.currentTimeMillis());
            }
            Data data = ide.store(update);
            if (data != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 批量删除数据
     *
     * @param ide
     * @param repository
     * @param batchRemove
     * @return
     * @throws DBException
     */
    private int batchRemove(IDataExecute ide, Repository repository, DataList batchRemove) throws DBException {
        if (batchRemove != null && batchRemove.size() > 0) {
            if (UtilString.isNullOrEmpty(batchRemove.getData(0).getEntityName())) {
                batchRemove.getData(0).setEntityName(repository.tableName());
            }
            return ide.remove(batchRemove);
        }
        return 0;
    }

    /**
     * 删除数据
     *
     * @param ide
     * @param repository
     * @param remove
     * @return
     * @throws DBException
     */
    private int remove(IDataExecute ide, Repository repository, Data remove) throws DBException {
        if (remove != null) {
            if (UtilString.isNullOrEmpty(remove.getEntityName())) {
                remove.setEntityName(repository.tableName());
            }
            return ide.remove(remove);
        }
        return 0;
    }

    /**
     * 操作数据后自动清空redis缓存
     *
     * @param instance
     * @param serial
     */
    private void emptyCacheData(Class<?> instance, Serial serial) {
        Method[] methods = instance.getDeclaredMethods();
        for (Method m : methods) {
            Select select = m.getAnnotation(Select.class);
            String cache = select.cache();
            if (UtilString.isNotEmpty(cache)) {
                UtilRedis.getRedis(DATA_CACHE_DB).del(DATA_CACHE + instance.getName() + "_" + cache, serial);
            }
        }
    }
}
