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
import org.breeze.generator.service.CodeService;

import javax.servlet.http.HttpServletRequest;
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
            @Param(name = "dbName", description = "数据库名称", required = true)
    })
    @Permission("tools_code_generator")
    @Api(value = "table", method = RequestMethod.GET)
    public R tableList(String connName, String dbName, Serial serial) {
        try (Connection conn = ConnectionManager.getConnection(connName)) {
            IDataExecute ide = DataBaseFactory.getDataBase(conn);
            String sql = "SELECT TABLE_NAME, TABLE_COMMENT, TABLE_COLLATION, ENGINE, CREATE_TIME FROM information_schema.TABLES WHERE table_schema = ?";
            DataList dl = ide.findSql(sql, dbName);
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
     * @param serial       请求序列号
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
//        try {
//
//            // 获取配置文件
//            Configuration configuration = new Configuration(Configuration.VERSION_2_3_29);
//            // 指定模板读取路径
//            configuration.setServletContextForTemplateLoading(request.getSession().getServletContext(), "/WEB-INF/classes/template");
//            // 读取controller模板
//            Template ct = configuration.getTemplate("controller.ftl");
//            // 将包名中的.替换为路径/
//            String packagePath = packageName.replaceAll("\\.", "/");
//            // 生成controller文件
//            File cFile = new File(rootPath + File.separator + packagePath + File.separator + "controller");
//            if (!cFile.exists()) {
//                cFile.mkdirs();
//            }
//            FileWriter cwriter = new FileWriter(new File(rootPath + File.separator + packagePath + File.separator + "controller"
//                    + File.separator + code_function + "Controller.java"));
//            ct.process(ftlMap, cwriter);
//            cwriter.close();
//            // 生成service文件
//            File sFile = new File(rootPath + File.separator + packagePath + File.separator + "service");
//            if (!sFile.exists()) {
//                sFile.mkdirs();
//            }
//            Template st = configuration.getTemplate("service.ftl");
//            FileWriter swriter = new FileWriter(new File(rootPath + File.separator + packagePath + File.separator + "service"
//                    + File.separator + code_function + "Service.java"));
//            st.process(ftlMap, swriter);
//            swriter.close();
//            // 生成dao文件
//            File dFile = new File(rootPath + File.separator + packagePath + File.separator + "dao");
//            if (!dFile.exists()) {
//                dFile.mkdirs();
//            }
//            Template dt = configuration.getTemplate("dao.ftl");
//            FileWriter dwriter = new FileWriter(new File(rootPath + File.separator + packagePath + File.separator + "dao"
//                    + File.separator + code_function + "Dao.java"));
//            dt.process(ftlMap, dwriter);
//            dwriter.close();
        return R.success();
    }
}
