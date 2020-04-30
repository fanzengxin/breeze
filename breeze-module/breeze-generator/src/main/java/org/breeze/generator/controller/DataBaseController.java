package org.breeze.generator.controller;

import org.breeze.core.annotation.controller.*;
import org.breeze.core.bean.api.R;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.config.DataBaseConfig;
import org.breeze.core.constant.RequestMethod;
import org.breeze.core.database.DataBaseFactory;
import org.breeze.core.database.dbinterface.IDataExecute;
import org.breeze.core.database.manager.ConnectionManager;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @description: 数据库链接查看
 * @auther: 黑面阿呆
 * @date: 2020-04-29 14:05
 * @version: 1.0.0
 */
@Controller(mapper = "/conn")
public class DataBaseController {

    private static Log log = LogFactory.getLog(DataBaseController.class);

    /**
     * 生成代码
     *
     * @param connName 表名
     * @param serial
     * @return
     */
    @Params({
            @Param(name = "connName", description = "数据库连接名称", defaultValue = DataBaseConfig.DATABASE_DEFAULT_CONFIG),
            @Param(name = "dbName", description = "数据库名称", required = true)
    })
    @Permission("tools_code_generator")
    @Api(value = "table", method = RequestMethod.GET)
    public R tableList(String connName, String dbName, Serial serial) {
        Connection conn = null;
        try {
            conn = ConnectionManager.getConnection(connName);
            IDataExecute ide = DataBaseFactory.getDataBase(conn);
            String sql = "SELECT TABLE_NAME, TABLE_COMMENT, TABLE_COLLATION, ENGINE, CREATE_TIME FROM information_schema.TABLES WHERE table_schema = ?";
            DataList dl = ide.findSql(sql, dbName);
            return R.success(dl);
        } catch (Exception e) {
            log.logError("数据库操作失败", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e1) {
                }
            }
        }
        return R.failure("查询数据表信息失败");
    }

    @Params({
            @Param(name = "connName", description = "数据库连接名称", defaultValue = DataBaseConfig.DATABASE_DEFAULT_CONFIG),
            @Param(name = "dbName", description = "数据库名称", required = true),
            @Param(name = "tableName", description = "数据表名称", required = true)
    })
    @Permission("tools_code_generator")
    @Api(value = "column", method = RequestMethod.GET)
    public R columnList(String connName, String dbName, String tableName, Serial serial) {
        Connection conn = null;
        try {
            conn = ConnectionManager.getConnection(connName);
            IDataExecute ide = DataBaseFactory.getDataBase(conn);
            String sql = "SELECT COLUMN_NAME, COLUMN_TYPE, COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE " +
                    "TABLE_SCHEMA = ? AND TABLE_NAME = ? ORDER BY TABLE_NAME, ORDINAL_POSITION";
            DataList dl = ide.findSql(sql, dbName, tableName);
            return R.success(dl);
        } catch (Exception e) {
            log.logError("数据库操作失败", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e1) {
                }
            }
        }
        return R.failure("查询数据表信息失败");
    }
}
