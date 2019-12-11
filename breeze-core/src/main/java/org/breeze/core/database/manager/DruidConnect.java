package org.breeze.core.database.manager;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.breeze.core.config.CommonConfig;
import org.breeze.core.config.DataBaseConfig;
import org.breeze.core.exception.DBException;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;
import org.breeze.core.utils.encry.Des3;
import org.breeze.core.utils.xml.XmlBean;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Druid的实现<br>
 *
 * @author 黑面阿呆
 */
public class DruidConnect implements IConnection {

    private static Log log = LogFactory.getLog(DruidConnect.class);
    private static Map<String, DataSource> dataSources = new HashMap<String, DataSource>();

    /*
     * (non-Javadoc)
     *
     * @see hx.database.manager.IConnection#getConnection(hx.util.XmlBean)
     */
    public Connection getConnection(String databasseName) throws DBException {
        DataSource dataSource;
        try {
            dataSource = getDataSource(databasseName);
        } catch (Exception e) {
            log.logError("获取Druid数据源失败：" + e.getMessage(), e);
            throw new DBException(e.getMessage());
        }
        Connection conn;
        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            log.logError("获取Druid连接失败：" + e.getMessage(), e);
            throw new DBException(e.getMessage());
        }
        return conn;
    }

    /*
     * (non-Javadoc)
     *
     * @see hx.database.manager.IConnection#getConnection(hx.util.XmlBean)
     */
    public DataSource getDataSource(String databasseName) {
        DataSource dataSource = dataSources.get(databasseName);
        if (dataSource == null) {
            synchronized ("DruidConnectDataSource") {
                dataSource = dataSources.get(databasseName);
                if (dataSource == null) {
                    if (log.isDebug()) {
                        log.logDebug("加载数据源");
                    }
                    try {
                        Map<String, String> config = DataBaseConfig.initConfig(databasseName);
                        String pass = Des3.decode(config.get("password"), CommonConfig.getSecretKey());
                        config.put("password", pass);
                        dataSource = DruidDataSourceFactory.createDataSource(config);
                        dataSources.put(databasseName, dataSource);
                    } catch (FileNotFoundException e) {
                        log.logError("读取数据库连接失败", e);
                    } catch (Exception e) {
                        log.logError("读取数据库连接失败", e);
                    }
                }
            }
        }
        return dataSource;
    }
}
