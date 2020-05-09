package org.breeze.generator.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.breeze.core.annotation.service.Service;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;
import org.breeze.core.utils.date.UtilDateTime;
import org.breeze.core.utils.string.UtilString;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 代码生成服务
 * @Author: 黑面阿呆
 * @Date: 2020-05-09 15:37
 * @Version: 1.0.0
 */
@Service
public class CodeService {

    private static Log log = LogFactory.getLog(CodeService.class);

    /**
     * 根据模板生成代码
     *
     * @param packageCode
     * @param moduleCode
     * @param moduleName
     * @param moduleAuthor
     * @param dynamic
     * @param tableName
     * @param serial
     * @return
     */
    public String generatorCode(HttpServletRequest request, String packageCode, String moduleCode, String moduleName,
                                String moduleAuthor, String dynamic, String tableName, Serial serial) {
        Map<String, Object> ftlMap = getftlMap(packageCode, moduleCode, moduleName, moduleAuthor, dynamic, tableName, serial);
        // 获取配置文件
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_29);
        // 指定模板读取路径
        configuration.setServletContextForTemplateLoading(request.getSession().getServletContext(), "/WEB-INF/classes/template");
        String rootPath = "D://code";
        // 将包名中的.替换为路径/
        String packagePath = packageCode.replaceAll("\\.", "/");
        createControllerFile(configuration, rootPath, packagePath, ftlMap, serial);
        createServiceFile(configuration, rootPath, packagePath, ftlMap, serial);
        createDaoFile(configuration, rootPath, packagePath, ftlMap, serial);
        return null;
    }

    /**
     * 创建替换模板的Map对象
     *
     * @param packageCode
     * @param moduleCode
     * @param moduleName
     * @param moduleAuthor
     * @param dynamic
     * @param tableName
     * @param serial
     * @return
     */
    private Map<String, Object> getftlMap(String packageCode, String moduleCode, String moduleName, String moduleAuthor,
                                          String dynamic, String tableName, Serial serial) {
        String code_function = moduleCode;
        String code_function_low = moduleCode;
        if (code_function.charAt(0) >= 'A' && code_function.charAt(0) <= 'Z') {
            code_function_low = code_function_low.substring(0, 1).toLowerCase() + code_function_low.substring(1);
        } else {
            code_function = code_function.substring(0, 1).toUpperCase() + code_function.substring(1);
        }
        // 封装配置参数
        Map<String, Object> ftlMap = new HashMap<String, Object>();
        ftlMap.put("code_package", packageCode);
        ftlMap.put("code_function", code_function);
        ftlMap.put("code_function_low", code_function_low);
        ftlMap.put("desc_function", moduleName);
        ftlMap.put("desc_username", moduleAuthor);
        ftlMap.put("desc_datetime", UtilDateTime.formatDate(new Date(), "yyyy-MM-dd HH:mm"));
        ftlMap.put("code_permission", code_function_low);
        StringBuffer search_doc = new StringBuffer();
        StringBuffer search_an = new StringBuffer();
        StringBuffer search_param = new StringBuffer();
        StringBuffer search = new StringBuffer();
        StringBuffer show_column = new StringBuffer();
        StringBuffer sql_column = new StringBuffer();
        JSONArray ja = JSONArray.parseArray(dynamic);
        for (int i = 0; i < ja.size(); i++) {
            JSONObject json = ja.getJSONObject(i);
            if (UtilString.isNotEmpty(json.getString("COLUMN_PK"))) {
                ftlMap.put("primary_key", json.getString("COLUMN_NAME"));
            }
            if (UtilString.isNotEmpty(json.getString("COLUMN_SEARCH"))) {
                search_doc.append("\n\t * @").append(json.getString("COLUMN_NAME"))
                        .append("\t\t").append(json.getString("COLUMN_COMMENT"));
                search_an.append(",\n\t\t").append("@Param(name = \"").append(json.getString("COLUMN_NAME"))
                        .append("\", description = \"").append(json.getString("COLUMN_COMMENT")).append("\")");
                search_param.append(", String ").append(json.getString("COLUMN_NAME"));
                show_column.append("{ and ").append(json.getString("COLUMN_NAME")).append(" = ").
                        append("#:").append(json.getString("COLUMN_NAME")).append(":#}");
                search.append(", ").append(json.getString("COLUMN_NAME"));
            }
            if (UtilString.isNotEmpty(json.getString("COLUMN_LIST"))) {
                sql_column.append(", ").append(json.getString("COLUMN_NAME"));
            }
        }
        ftlMap.put("search_doc", search_doc.toString());
        ftlMap.put("search_an", search_an.toString());
        ftlMap.put("search_param", search_param.toString());
        ftlMap.put("search", search.toString());
        ftlMap.put("search_column", search.toString());
        ftlMap.put("sql_column", sql_column.toString().length() > 2 ? search.toString().substring(2) : search.toString());
        ftlMap.put("sql_search_column", show_column.toString());
        ftlMap.put("code_permission", code_function_low);
        ftlMap.put("code_table_name", tableName);
        return ftlMap;
    }

    /**
     * 生成Controller.java文件
     *
     * @param configuration
     * @param rootPath
     * @param packagePath
     * @param ftlMap
     * @param serial
     */
    private void createControllerFile(Configuration configuration, String rootPath, String packagePath,
                                      Map<String, Object> ftlMap, Serial serial) {
        FileWriter cwriter = null;
        try {
            // 读取controller模板
            Template ct = configuration.getTemplate("controller.ftl");
            // 生成controller文件
            File cFile = new File(rootPath + File.separator + packagePath + File.separator + "controller");
            if (!cFile.exists()) {
                cFile.mkdirs();
            }
            cwriter = new FileWriter(new File(rootPath + File.separator + packagePath + File.separator + "controller"
                    + File.separator + ftlMap.get("code_function") + "Controller.java"));
            ct.process(ftlMap, cwriter);
        } catch (IOException | TemplateException e) {
            log.logError("生成Controller.java模板文件失败", serial, e);
        } finally {
            if (cwriter != null) {
                try {
                    cwriter.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 生成Service.java文件
     *
     * @param configuration
     * @param rootPath
     * @param packagePath
     * @param ftlMap
     * @param serial
     */
    private void createServiceFile(Configuration configuration, String rootPath, String packagePath,
                                   Map<String, Object> ftlMap, Serial serial) {
        FileWriter swriter = null;
        try {
            // 生成service文件
            File sFile = new File(rootPath + File.separator + packagePath + File.separator + "service");
            if (!sFile.exists()) {
                sFile.mkdirs();
            }
            Template st = configuration.getTemplate("service.ftl");
            swriter = new FileWriter(new File(rootPath + File.separator + packagePath + File.separator + "service"
                    + File.separator + ftlMap.get("code_function") + "Service.java"));
            st.process(ftlMap, swriter);
            swriter.close();
        } catch (IOException | TemplateException e) {
            log.logError("生成Service.java模板文件失败", serial, e);
        } finally {
            if (swriter != null) {
                try {
                    swriter.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 生成Dao.java文件
     *
     * @param configuration
     * @param rootPath
     * @param packagePath
     * @param ftlMap
     * @param serial
     */
    private void createDaoFile(Configuration configuration, String rootPath, String packagePath,
                                   Map<String, Object> ftlMap, Serial serial) {
        FileWriter swriter = null;
        try {
            // 生成service文件
            File sFile = new File(rootPath + File.separator + packagePath + File.separator + "dao");
            if (!sFile.exists()) {
                sFile.mkdirs();
            }
            Template st = configuration.getTemplate("dao.ftl");
            swriter = new FileWriter(new File(rootPath + File.separator + packagePath + File.separator + "dao"
                    + File.separator + ftlMap.get("code_function") + "Dao.java"));
            st.process(ftlMap, swriter);
            swriter.close();
        } catch (IOException | TemplateException e) {
            log.logError("生成Dao.java模板文件失败", serial, e);
        } finally {
            if (swriter != null) {
                try {
                    swriter.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
