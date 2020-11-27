package org.breeze.generator.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.breeze.core.annotation.service.Service;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.config.FilePathConfig;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;
import org.breeze.core.utils.date.UtilDateTime;
import org.breeze.core.utils.string.UUIDGenerator;
import org.breeze.core.utils.string.UtilString;
import org.breeze.generator.util.UtilsZip;

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
        // 将包名中的.替换为路径/
        String packagePath = packageCode.replaceAll("\\.", "/");
        // 指定模板读取路径
        configuration.setServletContextForTemplateLoading(request.getSession().getServletContext(), "/WEB-INF/classes/template");
        String rootPath = FilePathConfig.getFilePathCodeTemplate() + File.separator + UUIDGenerator.JavaUUID();
        // 生成controller文件
        createControllerFile(configuration, rootPath + "/web", packagePath, ftlMap, serial);
        // 生成service文件
        createServiceFile(configuration, rootPath + "/web", packagePath, ftlMap, serial);
        // 生成dao文件
        createDaoFile(configuration, rootPath + "/web", packagePath, ftlMap, serial);
        // 生成api.js文件
        createApiJsFile(configuration, rootPath + "/ui/api", ftlMap.get("package_name").toString(), ftlMap, serial);
        // 生成crud.js文件
        createCrudJsFile(configuration, rootPath + "/ui/const/crud", ftlMap.get("package_name").toString(), ftlMap, serial);
        // 生成index.vue文件
        createVueJsFile(configuration, rootPath + "/ui/views", ftlMap.get("package_name").toString(), ftlMap, serial);
        UtilsZip.compress(rootPath);
        UtilsZip.deleteFolder(new File(rootPath));
        return rootPath + ".zip";
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
        String[] pps = packageCode.split("\\.");
        ftlMap.put("package_name", pps[pps.length - 1]);
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
        StringBuffer crudColumns = new StringBuffer();
        JSONArray ja = JSONArray.parseArray(dynamic);
        String dict_import = "";
        for (int i = 0; i < ja.size(); i++) {
            JSONObject json = ja.getJSONObject(i);
            String nameValue = json.getString("COLUMN_NAME");
            String commentValue = json.getString("COLUMN_COMMENT");
            boolean pkValue = checkJsonDataNotNull(json.getString("COLUMN_PK"));
            boolean listValue = checkJsonDataNotNull(json.getString("COLUMN_LIST"));
            boolean hideValue = checkJsonDataNotNull(json.getString("COLUMN_HIDE"));
            boolean searchValue = checkJsonDataNotNull(json.getString("COLUMN_SEARCH"));
            boolean viewValue = checkJsonDataNotNull(json.getString("COLUMN_VIEW"));
            boolean addValue = checkJsonDataNotNull(json.getString("COLUMN_ADD"));
            boolean editValue = checkJsonDataNotNull(json.getString("COLUMN_EDIT"));
            boolean disabledValue = checkJsonDataNotNull(json.getString("COLUMN_DISABLED"));
            boolean requiredValue = checkJsonDataNotNull(json.getString("COLUMN_REQUIRED"));
            String typeValue = json.getString("INPUT_TYPE");
            String dictValue = json.getString("INPUT_DICT");
            if (pkValue) {
                ftlMap.put("primary_key", nameValue);
            }
            if (searchValue) {
                search_doc.append("\n\t * @").append(nameValue)
                        .append("\t\t").append(commentValue);
                search_an.append(",\n\t\t").append("@Param(name = \"").append(nameValue)
                        .append("\", description = \"").append(commentValue).append("\")");
                search_param.append(", String ").append(nameValue);
                show_column.append("{ and ").append(nameValue).append(" = ").
                        append("#:").append(nameValue).append(":#}");
                search.append(", ").append(nameValue);
            }
            if (listValue) {
                sql_column.append(", ").append(nameValue);
            }
            if (pkValue || listValue || hideValue || searchValue || viewValue || addValue || editValue) {
                if (crudColumns.length() > 0) {
                    crudColumns.append(", ");
                }
                crudColumns.append("{\n    label: '").append(commentValue).append("'");
                crudColumns.append(",\n    prop: '").append(nameValue).append("'");
                crudColumns.append(",\n    align: 'center'");
                if (hideValue) {
                    crudColumns.append(",\n    hide: true");
                }
                if (!"text".equalsIgnoreCase(typeValue)) {
                    crudColumns.append(",\n    type: '").append(typeValue).append("'");
                    if ("datetime".equalsIgnoreCase(typeValue)) {
                        crudColumns.append(",\n    format: 'yyyy-MM-dd HH:mm:ss'");
                        crudColumns.append(",\n    valueFormat: 'timestamp'");
                    }
                }
                if (!viewValue) {
                    crudColumns.append(",\n    viewDisplay: false");
                }
                if (!addValue) {
                    crudColumns.append(",\n    addDisplay: false");
                }
                if (!editValue) {
                    crudColumns.append(",\n    editDisplay: false");
                }
                if (!disabledValue) {
                    crudColumns.append(",\n    editDisabled: true");
                }
                if (UtilString.isNotEmpty(dictValue)) {
                    dict_import = "import {getStoreDict} from '@/util/store'\n\n";
                    crudColumns.append(",\n    dicData: getStoreDict('").append(dictValue).append("')");
                }
                if (requiredValue) {
                    crudColumns.append(",\n    rules: [{\n").append("      required: true");
                    crudColumns.append(",\n      message: '请输入").append(commentValue).append("'");
                    crudColumns.append(",\n      trigger: 'blur'\n    }]");
                }
                crudColumns.append("\n  }");
            }
        }
        ftlMap.put("dict_import", dict_import);
        ftlMap.put("column_crud", crudColumns.toString());
        ftlMap.put("search_doc", search_doc.toString());
        ftlMap.put("search_an", search_an.toString());
        ftlMap.put("search_param", search_param.toString());
        ftlMap.put("search", search.toString());
        ftlMap.put("search_column", search.toString());
        ftlMap.put("sql_column", sql_column.toString().length() > 2 ? sql_column.toString().substring(2) :
                sql_column.toString());
        if (ftlMap.get("primary_key") != null && !ftlMap.get("sql_column").toString().contains(ftlMap.get("primary_key").toString())) {
            ftlMap.put("sql_column", ftlMap.get("sql_column").toString() + ", " + ftlMap.get("primary_key").toString());
        }
        ftlMap.put("sql_search_column", show_column.toString());
        ftlMap.put("code_permission", code_function_low);
        ftlMap.put("code_table_name", tableName);
        if (ftlMap.get("primary_key") != null) {
            ftlMap.put("primary_key_upper", ftlMap.get("primary_key").toString().toUpperCase());
        } else {
            ftlMap.put("primary_key_upper", "");
        }
        return ftlMap;
    }

    /**
     * 检测json值是否为空
     *
     * @param value
     * @return
     */
    private boolean checkJsonDataNotNull(String value) {
        return UtilString.isNotEmpty(value) && !"[]".equalsIgnoreCase(value);
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
        String pathName = rootPath + File.separator + packagePath + File.separator + "controller";
        // 生成controller文件
        File cFile = new File(pathName);
        if (!cFile.exists()) {
            cFile.mkdirs();
        }
        try (FileWriter cwriter = new FileWriter(new File(pathName + File.separator +
                ftlMap.get("code_function") + "Controller.java"))) {
            // 读取controller模板
            Template ct = configuration.getTemplate("controller.ftl");
            ct.process(ftlMap, cwriter);
        } catch (IOException | TemplateException e) {
            log.logError("生成Controller.java模板文件失败", serial, e);
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
        String pathName = rootPath + File.separator + packagePath + File.separator + "service";
        // 生成service文件
        File sFile = new File(pathName);
        if (!sFile.exists()) {
            sFile.mkdirs();
        }
        try (FileWriter swriter = new FileWriter(new File(pathName + File.separator +
                ftlMap.get("code_function") + "Service.java"))) {
            Template st = configuration.getTemplate("service.ftl");
            st.process(ftlMap, swriter);
        } catch (IOException | TemplateException e) {
            log.logError("生成Service.java模板文件失败", serial, e);
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
        String pathName = rootPath + File.separator + packagePath + File.separator + "dao";
        // 生成service文件
        File sFile = new File(pathName);
        if (!sFile.exists()) {
            sFile.mkdirs();
        }
        try (FileWriter swriter = new FileWriter(new File(pathName + File.separator +
                ftlMap.get("code_function") + "Dao.java"))) {
            Template st = configuration.getTemplate("dao.ftl");
            st.process(ftlMap, swriter);
        } catch (IOException | TemplateException e) {
            log.logError("生成Dao.java模板文件失败", serial, e);
        }
    }

    /**
     * 生成api.js文件
     *
     * @param configuration
     * @param rootPath
     * @param packagePath
     * @param ftlMap
     * @param serial
     */
    private void createApiJsFile(Configuration configuration, String rootPath, String packagePath,
                                 Map<String, Object> ftlMap, Serial serial) {
        String pathName = rootPath + File.separator + packagePath;
        // 生成service文件
        File sFile = new File(pathName);
        if (!sFile.exists()) {
            sFile.mkdirs();
        }
        try (FileWriter swriter = new FileWriter(new File(pathName + File.separator +
                ftlMap.get("code_function_low") + ".js"))) {
            Template st = configuration.getTemplate("api.ftl");
            st.process(ftlMap, swriter);
        } catch (IOException | TemplateException e) {
            log.logError("生成api.js模板文件失败", serial, e);
        }
    }

    /**
     * 生成crud.js文件
     *
     * @param configuration
     * @param rootPath
     * @param packagePath
     * @param ftlMap
     * @param serial
     */
    private void createCrudJsFile(Configuration configuration, String rootPath, String packagePath,
                                  Map<String, Object> ftlMap, Serial serial) {
        String pathName = rootPath + File.separator + packagePath;
        // 生成service文件
        File sFile = new File(pathName);
        if (!sFile.exists()) {
            sFile.mkdirs();
        }
        try (FileWriter swriter = new FileWriter(new File(pathName + File.separator +
                ftlMap.get("code_function_low") + ".js"))) {
            Template st = configuration.getTemplate("crud.ftl");
            st.process(ftlMap, swriter);
        } catch (IOException | TemplateException e) {
            log.logError("生成crud.js模板文件失败", serial, e);
        }
    }

    /**
     * 生成api.vue文件
     *
     * @param configuration
     * @param rootPath
     * @param packagePath
     * @param ftlMap
     * @param serial
     */
    private void createVueJsFile(Configuration configuration, String rootPath, String packagePath,
                                 Map<String, Object> ftlMap, Serial serial) {
        String pathName = rootPath + File.separator + packagePath + File.separator + ftlMap.get("code_function_low");
        // 生成service文件
        File sFile = new File(pathName);
        if (!sFile.exists()) {
            sFile.mkdirs();
        }
        try (FileWriter swriter = new FileWriter(new File(pathName + File.separator + "index.vue"))) {
            Template st = configuration.getTemplate("vue.ftl");
            st.process(ftlMap, swriter);
        } catch (IOException | TemplateException e) {
            log.logError("生成vue.js模板文件失败", serial, e);
        }
    }
}
