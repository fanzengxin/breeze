package org.breeze.generator.controller;

import org.breeze.core.annotation.common.AutoAdd;
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
import org.breeze.core.utils.cache.UtilRedis;
import org.breeze.core.utils.string.UUIDGenerator;
import org.breeze.core.utils.string.UtilString;
import org.breeze.generator.service.CodeService;
import org.breeze.generator.util.UtilsZip;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;

/**
 * @description: 数据库链接查看
 * @auther: 黑面阿呆
 * @date: 2020-04-29 14:05
 * @version: 1.0.0
 */
@Controller(mapper = "/code")
public class CodeGeneratorController {

    private static Log log = LogFactory.getLog(CodeGeneratorController.class);

    private static final String GENERATOR_CODE_REDIS_KEY = "GENERATOR_CODE_REDIS_KEY_";

    @AutoAdd
    private CodeService codeService;

    /**
     * 生成代码
     *
     * @param connName 表名
     * @param serial
     * @return
     */
    @Params({
            @Param(name = "connName", description = "数据库连接名称", defaultValue = DataBaseConfig.DATABASE_DEFAULT_CONFIG),
            @Param(name = "dbName", description = "数据库名称", required = true),
            @Param(name = "tableName", description = "数据表名称")
    })
    @Permission("tools_code_generator")
    @Api(value = "table", method = RequestMethod.GET)
    public R tableList(String connName, String dbName, String tableName, Serial serial) {
        try (Connection conn = ConnectionManager.getConnection(connName)) {
            IDataExecute ide = DataBaseFactory.getDataBase(conn);
            String sql = "SELECT TABLE_NAME, TABLE_COMMENT, TABLE_COLLATION, ENGINE, CREATE_TIME FROM information_schema.TABLES WHERE table_schema = ?";
            if (UtilString.isNotEmpty(tableName)) {
                sql += " AND TABLE_NAME LIKE ?";
                tableName = "%" + tableName + "%";
            }
            DataList dl = ide.findSql(sql, dbName, tableName);
            return R.success(dl);
        } catch (Exception e) {
            log.logError("数据库操作失败", e);
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
        try (Connection conn = ConnectionManager.getConnection(connName)) {
            IDataExecute ide = DataBaseFactory.getDataBase(conn);
            String sql = "SELECT COLUMN_NAME, COLUMN_TYPE, COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE " +
                    "TABLE_SCHEMA = ? AND TABLE_NAME = ? ORDER BY TABLE_NAME, ORDINAL_POSITION";
            DataList dl = ide.findSql(sql, dbName, tableName);
            return R.success(dl);
        } catch (Exception e) {
            log.logError("数据库操作失败", e);
        }
        return R.failure("查询数据表信息失败");
    }

    /**
     * 生成代码
     *
     * @param request      request请求
     * @param packageCode  生成文件路径
     * @param moduleCode   包名称
     * @param moduleName   代码
     * @param moduleAuthor 代码小写
     * @param dynamic      模块名称
     * @param tableName    数据表名
     * @param serial
     * @return
     */
    @Params({
            @Param(name = "packageCode", description = "包路径", required = true),
            @Param(name = "moduleCode", description = "模块编码", required = true),
            @Param(name = "moduleName", description = "模块名称", required = true),
            @Param(name = "moduleAuthor", description = "作者", required = true),
            @Param(name = "dynamic", description = "字段描述", required = true),
            @Param(name = "tableName", description = "数据表名", required = true)
    })
    @Permission("tools_code_generator")
    @Api(value = "generator", method = RequestMethod.POST)
    public R generator(HttpServletRequest request, String packageCode, String moduleCode, String moduleName,
                       String moduleAuthor, String dynamic, String tableName, Serial serial) {
        String codePath = codeService.generatorCode(request, packageCode, moduleCode, moduleName, moduleAuthor, dynamic, tableName, serial);
        if (UtilString.isNotEmpty(codePath)) {
            String codeId = UUIDGenerator.JavaUUID();
            UtilRedis.set(GENERATOR_CODE_REDIS_KEY + codeId, codePath, serial);
            return R.success(codeId);
        } else {
            return R.success();
        }
    }

    /**
     * 下载代码
     *
     * @param response response响应
     * @param codeId   代码Id
     * @param serial
     * @return
     */
    @Params({
            @Param(name = "codeId", description = "代码Key", required = true)
    })
    @Permission(login = false)
    @Api(value = "download", method = RequestMethod.GET)
    public void downloadCode(HttpServletResponse response, String codeId, Serial serial) {
        String codePath = UtilRedis.get(GENERATOR_CODE_REDIS_KEY + codeId, serial);
        try {
            // path是指欲下载的文件的路径。
            File file = new File(codePath);
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(codePath));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=code.zip");
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            log.logError("代码生成失败", ex);
        } finally {
            new File(codePath).delete();
        }
    }
}
