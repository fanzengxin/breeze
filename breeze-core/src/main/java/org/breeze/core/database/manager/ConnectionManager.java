package org.breeze.core.database.manager;

import org.breeze.core.config.DataBaseConfig;
import org.breeze.core.exception.DBException;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;


/**
 * 连接池管理器<br>
 *
 * @author 黑面阿呆
 */
public class ConnectionManager {

    // 日志类
    private static Log log = LogFactory.getLog(ConnectionManager.class);

    // 配置信息
    private static Map<String, Boolean> group = new HashMap<String, Boolean>();

    /**
     * 得到默认的数据库连接
     *
     * @return 配置文件中配置的默认连接
     * @throws DBException
     */
    public static Connection getConnection() throws DBException {
        return getConnection(DataBaseConfig.DATABASE_DEFAULT_CONFIG);
    }

    /**
     * 获取数据库DataSource
     *
     * @return
     * @throws DBException
     */
    public static DataSource getDataSource() throws DBException {
        return getDataSource(DataBaseConfig.DATABASE_DEFAULT_CONFIG);
    }


    /**
     * 得到数据库连接
     *
     * @param datasourceName
     * @return
     * @throws DBException
     */
    public static Connection getConnection(String datasourceName) throws DBException {
        initConnConfig();
        Boolean value = group.get(datasourceName);
        if (value == null) {
            log.logError("没有配置数据源的名称:{}", datasourceName);
            return null;
        }
        IConnection connClass = new DruidConnect();
        return connClass.getConnection(datasourceName);
    }

    /**
     * 获取数据库DataSource
     *
     * @param datasourceName
     * @return
     * @throws DBException
     */
    public static DataSource getDataSource(String datasourceName) {
        initConnConfig();
        Boolean value = group.get(datasourceName);
        if (value == null) {
            log.logError("没有配置数据源的名称:{}", datasourceName);
            return null;
        }
        IConnection connClass = new DruidConnect();
        DataSource ds = connClass.getDataSource(datasourceName);
        return ds;
    }

    /**
     * 没有读取配置文件，那么就从指定的数据源配置文件中读取
     */
    private static void initConnConfig() {
        if (group.size() == 0) {
            log.logDebug("读取数据连接配置文件");
            group = DataBaseConfig.getAllDBs();
            log.logDebug("数据连接配置文件:{}", group.keySet().toString());
        }
    }
}
