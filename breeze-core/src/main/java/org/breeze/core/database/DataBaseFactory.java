/**
 *
 */
package org.breeze.core.database;


import org.breeze.core.config.CommonConfig;
import org.breeze.core.database.dbinterface.IDataExecute;
import org.breeze.core.database.impl.DataExecuteMySqlImpl;
import org.breeze.core.database.impl.DataExecutePhoenixImpl;
import org.breeze.core.exception.DBException;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;
import org.breeze.core.utils.file.UtilClassLoader;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * 数据操作对象获取工厂
 *
 * @author 黑面阿呆
 *
 */
public class DataBaseFactory {

    private static Log log = LogFactory.getLog(DataBaseFactory.class);

    public static IDataExecute getDataBase(Connection conn) throws DBException {
        BaseDataExecute ide = null;
        try {
            if (conn.getMetaData().getDriverName().toUpperCase().indexOf("MYSQL") != -1) {
                ide = new DataExecuteMySqlImpl();
            } else if (conn.getMetaData().getDriverName().toUpperCase().indexOf("PHOENIX") != -1) {
                ide = new DataExecutePhoenixImpl();
            }
        } catch (SQLException e) {
            log.logError("获取数据库类型失败，不支持的数据库类型", e);
            throw new DBException("获取数据库类型失败，不支持的数据库类型");
        }
        ide.setConnection(conn);
        return ide;
    }
}
