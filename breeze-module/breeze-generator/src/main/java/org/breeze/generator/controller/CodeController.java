package org.breeze.generator.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.breeze.core.annotation.controller.*;
import org.breeze.core.bean.api.R;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.constant.RequestMethod;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;
import org.breeze.core.utils.date.UtilDateTime;
import org.breeze.core.utils.string.UUIDGenerator;
import org.breeze.generator.util.UtilsZip;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 代码生成器
 * @Auther: 黑面阿呆
 * @Date: 2019-11-29 13:39
 * @Version: 1.0.0
 */
@Controller
public class CodeController {

    private static Log log = LogFactory.getLog(CodeController.class);

    /**
     * 生成代码
     *
     * @param request           request请求
     * @param rootPath          生成文件路径
     * @param packageName       包名称
     * @param code_function     代码
     * @param code_function_low 代码小写
     * @param desc_function     模块名称
     * @param desc_username     作者
     * @param code_permission   权限代码
     * @param primary_key       主键
     * @param code_table_name   表名
     * @param serial
     * @return
     */
    @Params({
            @Param(name = "rootPath", description = "生成文件路径", required = true),
            @Param(name = "packageName", description = "包名称", required = true),
            @Param(name = "code_function", description = "代码", required = true),
            @Param(name = "code_function_low", description = "代码小写", required = true),
            @Param(name = "desc_function", description = "模块名称", required = true),
            @Param(name = "desc_username", description = "作者", required = true),
            @Param(name = "code_permission", description = "权限代码", required = true),
            @Param(name = "primary_key", description = "主键", required = true),
            @Param(name = "code_table_name", description = "表名", required = true)
    })
    @Permission(login = false)
    @Api(value = "create", method = RequestMethod.GET)
    public R create(HttpServletRequest request, String rootPath, String packageName, String code_function, String code_function_low,
                    String desc_function, String desc_username, String code_permission, String primary_key, String code_table_name, Serial serial) {
        try {
            // 封装配置参数
            Map<String, Object> ftlMap = new HashMap<String, Object>();
            ftlMap.put("code_package", packageName);
            ftlMap.put("code_function", code_function);
            ftlMap.put("code_function_low", code_function_low);
            ftlMap.put("desc_function", desc_function);
            ftlMap.put("desc_username", desc_username);
            ftlMap.put("desc_datetime", UtilDateTime.formatDate(new Date(), "yyyy-MM-dd HH:mm"));
            ftlMap.put("code_permission", code_permission);
            ftlMap.put("primary_key", primary_key);
            ftlMap.put("code_table_name", code_table_name);
            // 获取配置文件
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_29);
            // 指定模板读取路径
            configuration.setServletContextForTemplateLoading(request.getSession().getServletContext(), "/WEB-INF/classes/template");
            // 读取controller模板
            Template ct = configuration.getTemplate("controller.ftl");
            // 将包名中的.替换为路径/
            String packagePath = packageName.replaceAll("\\.", "/");
            // 生成controller文件
            File cFile = new File(rootPath + File.separator + packagePath + File.separator + "controller");
            if (!cFile.exists()) {
                cFile.mkdirs();
            }
            FileWriter cwriter = new FileWriter(new File(rootPath + File.separator + packagePath + File.separator + "controller"
                    + File.separator + code_function + "Controller.java"));
            ct.process(ftlMap, cwriter);
            cwriter.close();
            // 生成service文件
            File sFile = new File(rootPath + File.separator + packagePath + File.separator + "service");
            if (!sFile.exists()) {
                sFile.mkdirs();
            }
            Template st = configuration.getTemplate("service.ftl");
            FileWriter swriter = new FileWriter(new File(rootPath + File.separator + packagePath + File.separator + "service"
                    + File.separator + code_function + "Service.java"));
            st.process(ftlMap, swriter);
            swriter.close();
            // 生成dao文件
            File dFile = new File(rootPath + File.separator + packagePath + File.separator + "dao");
            if (!dFile.exists()) {
                dFile.mkdirs();
            }
            Template dt = configuration.getTemplate("dao.ftl");
            FileWriter dwriter = new FileWriter(new File(rootPath + File.separator + packagePath + File.separator + "dao"
                    + File.separator + code_function + "Dao.java"));
            dt.process(ftlMap, dwriter);
            dwriter.close();
            return R.success();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return R.failure("代码生成失败");
    }

    /**
     * 下载代码
     *
     * @param request           request请求
     * @param response          response响应
     * @param rootPath          生成文件路径
     * @param packageName       包名称
     * @param code_function     代码
     * @param code_function_low 代码小写
     * @param desc_function     模块名称
     * @param desc_username     作者
     * @param code_permission   权限代码
     * @param primary_key       主键
     * @param code_table_name   表名
     * @param serial
     * @return
     */
    @Params({
            @Param(name = "rootPath", description = "生成文件路径", required = true),
            @Param(name = "packageName", description = "包名称", required = true),
            @Param(name = "code_function", description = "代码", required = true),
            @Param(name = "code_function_low", description = "代码小写", required = true),
            @Param(name = "desc_function", description = "模块名称", required = true),
            @Param(name = "desc_username", description = "作者", required = true),
            @Param(name = "code_permission", description = "权限代码", required = true),
            @Param(name = "primary_key", description = "主键", required = true),
            @Param(name = "code_table_name", description = "表名", required = true)
    })
    @Permission(login = false)
    @Api(value = "download", method = RequestMethod.GET)
    public void download(HttpServletRequest request, HttpServletResponse response, String rootPath, String packageName, String code_function, String code_function_low,
                         String desc_function, String desc_username, String code_permission, String primary_key, String code_table_name, Serial serial) {
        try {
            if (rootPath.endsWith("/")) {
                rootPath = rootPath.substring(0, rootPath.length() - 1);
            }
            // 生成临时路径
            rootPath = rootPath + "/" + UUIDGenerator.JavaUUID();
            // 封装配置参数
            Map<String, Object> ftlMap = new HashMap<String, Object>();
            ftlMap.put("code_package", packageName);
            ftlMap.put("code_function", code_function);
            ftlMap.put("code_function_low", code_function_low);
            ftlMap.put("desc_function", desc_function);
            ftlMap.put("desc_username", desc_username);
            ftlMap.put("desc_datetime", UtilDateTime.formatDate(new Date(), "yyyy-MM-dd HH:mm"));
            ftlMap.put("code_permission", code_permission);
            ftlMap.put("primary_key", primary_key);
            ftlMap.put("code_table_name", code_table_name);
            // 获取配置文件
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_29);
            // 指定模板读取路径
            configuration.setServletContextForTemplateLoading(request.getSession().getServletContext(), "/WEB-INF/classes/template");
            // 读取controller模板
            Template ct = configuration.getTemplate("controller.ftl");
            // 将包名中的.替换为路径/
            String packagePath = packageName.replaceAll("\\.", "/");
            // 生成controller文件
            File cFile = new File(rootPath + File.separator + packagePath + File.separator + "controller");
            if (!cFile.exists()) {
                cFile.mkdirs();
            }
            FileWriter cwriter = new FileWriter(new File(rootPath + File.separator + packagePath + File.separator + "controller"
                    + File.separator + code_function + "Controller.java"));
            ct.process(ftlMap, cwriter);
            cwriter.close();
            // 生成service文件
            File sFile = new File(rootPath + File.separator + packagePath + File.separator + "service");
            if (!sFile.exists()) {
                sFile.mkdirs();
            }
            Template st = configuration.getTemplate("service.ftl");
            FileWriter swriter = new FileWriter(new File(rootPath + File.separator + packagePath + File.separator + "service"
                    + File.separator + code_function + "Service.java"));
            st.process(ftlMap, swriter);
            swriter.close();
            // 生成dao文件
            File dFile = new File(rootPath + File.separator + packagePath + File.separator + "dao");
            if (!dFile.exists()) {
                dFile.mkdirs();
            }
            Template dt = configuration.getTemplate("dao.ftl");
            FileWriter dwriter = new FileWriter(new File(rootPath + File.separator + packagePath + File.separator + "dao"
                    + File.separator + code_function + "Dao.java"));
            dt.process(ftlMap, dwriter);
            dwriter.close();
            // 生成zip压缩文件
            UtilsZip.compress(rootPath, rootPath);
            // 读到流中
            InputStream inStream = new FileInputStream(rootPath + ".zip");// 文件的存放路径
            // 设置输出的格式
            response.reset();
            response.setContentType("bin");
            response.addHeader("Content-Disposition", "attachment; filename=\"code.zip\"");
            // 循环取出流中的数据
            byte[] b = new byte[100];
            int len;
            try {
                // 下载文件
                while ((len = inStream.read(b)) > 0) {
                    response.getOutputStream().write(b, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                inStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            // 删除生成的临时文件
            File file = new File(rootPath + ".zip");
            if (file.exists()) {
                file.delete();
            }
            File rfile = new File(rootPath);
            UtilsZip.deleteFolder(rfile);
            rfile.delete();
        }
    }
}
