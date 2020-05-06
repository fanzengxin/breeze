package org.breeze.core.filter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.breeze.core.bean.api.ApiConfig;
import org.breeze.core.bean.api.BeanFactory;
import org.breeze.core.bean.api.MethodParameter;
import org.breeze.core.bean.api.R;
import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.bean.login.LoginInfo;
import org.breeze.core.bean.login.LoginSession;
import org.breeze.core.config.CommonConfig;
import org.breeze.core.config.ResponseCode;
import org.breeze.core.constant.ParamFormatCheck;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;
import org.breeze.core.utils.date.UtilDateTime;
import org.breeze.core.utils.string.UtilString;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @Description: 分发请求
 * @Auther: 黑面阿呆
 * @Date: 2019/7/31 9:40
 * @Version: 1.0.0
 */
@WebFilter(filterName = "ServiceController", value = "/service/*", description = "服务控制器")
public class DispatcherFilter implements Filter {

    private static Log log = LogFactory.getLog(DispatcherFilter.class);

    private FilterConfig filterConfig;

    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3;

    // 数据验证正则
    private static final Pattern intFormat = Pattern.compile("-?\\d+");
    private static final Pattern numFormat = Pattern.compile("-?\\d+(\\.\\d+)?");
    private static final Pattern dateFormat = Pattern.compile("[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])");
    private static final Pattern timeFormat = Pattern.compile("(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d");
    private static final Pattern datetimeFormat = Pattern.compile("[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d");
    private static final Pattern emailFormat = Pattern.compile("[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+");
    private static final Pattern phoneFormat = Pattern.compile("1[3456789]\\d{9}");

    /**
     * 初始化截器配置文件
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    /**
     * 请求拦截，处理业务请求
     *
     * @param servletRequest  request对象
     * @param servletResponse response对象
     * @param filterChain     chain对象
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest hsr = (HttpServletRequest) servletRequest;
        HttpServletResponse hsq = (HttpServletResponse) servletResponse;
        String url = hsr.getServletPath();
        Serial serial = new Serial(url);
        ServletContext context = filterConfig.getServletContext();
        log.logInfo("接入请求：{}", serial, url);
        long startTime = UtilDateTime.currentTimeMillis();
        doExecute(hsr, hsq, context, url, serial);
        long endTime = UtilDateTime.currentTimeMillis();
        log.logInfo("结束请求，耗时：{}ms", serial, String.valueOf(endTime - startTime));
    }


    /**
     * 处理请求
     *
     * @param request
     * @param response
     * @param context
     * @param url
     * @param serial
     */
    private void doExecute(HttpServletRequest request, HttpServletResponse response, ServletContext context, String url, Serial serial) {
        try {
            // 获取API 实例
            String method = request.getMethod();
            ApiConfig apiBean = BeanFactory.getApiConfig(url, method);
            if (apiBean == null) {
                // 指定URL的实例不存在
                log.logInfo("未配置的url地址：{}或未经许可的请求方式：{}", serial, url, request.getMethod());
                printString(response, R.failure(ResponseCode.ERROR_API_URL).toString());
                return;
            }
            // 根据实例配置获取指定的class
            Class<?> action = Class.forName(apiBean.getClassName());
            Object instance = null;
            // 判断是否单例
            if (apiBean.isSingleton()) {
                // 单例模式从缓存中获取controller实例对象
                instance = BeanFactory.getControllerBean(action);
            } else {
                // 非单例模式初始化实例对象
                instance = action.newInstance();
            }
            // 验证请求数据，整理请求参数
            Map<String, Object> checkResult = checkData(apiBean, request, response, context, serial);
            if (checkResult.get("error") != null) {
                // 请求参数验证失败
                printString(response, R.failure((int) checkResult.get("error")).toString());
            } else {
                // 根据指定参数获取对应方法，同时注入方法参数
                List<Class<?>> classList = (List<Class<?>>) checkResult.get("class");
                // 获取指定请求参数的方法对象
                Method runMethod = action.getMethod(apiBean.getMethod(), classList.toArray(new Class<?>[classList.size()]));
                // 反射执行方法
                Object o = runMethod.invoke(instance, ((List<Object>) checkResult.get("value")).toArray(new Object[classList.size()]));
                // 返回执行结果
                if (o == null) {
                    log.logDebug("响应结果为空", serial);
                } else {
                    if (o instanceof String) {
                        log.logDebug("响应结果：{}", serial, o.toString());
                        printString(response, (String) o);
                    } else if (o instanceof R) {
                        log.logDebug("响应结果：{}", serial, o.toString());
                        printString(response, ((R) o).toString());
                    }
                }
            }
        } catch (Exception e) {
            log.logInfo("系统异常", serial, e);
            printString(response, R.failure(ResponseCode.SYSTEM_ERROR).toString());
        }
    }

    /**
     * 数据验证
     *
     * @param apiBean
     * @param request
     * @param response
     * @param context
     * @param serial
     * @return
     */
    private Map<String, Object> checkData(ApiConfig apiBean, HttpServletRequest request, HttpServletResponse response, ServletContext context, Serial serial) {
        Map<String, Object> result = new HashMap<String, Object>();
        LoginInfo loginInfo = null;
        // 验证用户登录
        if (apiBean.isLogin()) {
            String auth_code = request.getHeader("auth_code");
            loginInfo = LoginSession.getLoginInfo(auth_code, serial);
            if (loginInfo == null) {
                log.logInfo("用户未登录", serial);
                // 用户未登录
                result.put("error", ResponseCode.LOGIN_STATUS_NO_LOGIN);
                return result;
            }
            if (LoginSession.checkUserLocked(loginInfo.getUserId(), serial)) {
                log.logInfo("用户{}账户被锁定", serial, loginInfo.getUserId());
                // 账户锁定
                result.put("error", ResponseCode.LOGIN_STATUS_LOCKED);
                return result;
            }
        }
        String permissionCode = apiBean.getPermissionCode();
        // 验证权限
        if (loginInfo != null && UtilString.isNotEmpty(permissionCode)) {
            String[] permissions = permissionCode.split(",");
            boolean allowPermission = false;
            for (String pm : permissions) {
                if (loginInfo.getPermissions().contains(pm)) {
                    allowPermission = true;
                    break;
                }
            }
            if (!allowPermission) {
                log.logInfo("没有请求该数据接口的权限：{}", serial, permissionCode);
                // 账户锁定
                result.put("error", ResponseCode.REQUEST_NO_PERMISSION);
                return result;
            }
        }
        // 验证请求签名
        if (apiBean.isSign()) {

        }
        List<MethodParameter> list = apiBean.getParameters();
        List<Class<?>> classList = new ArrayList<Class<?>>();
        List<Object> valueList = new ArrayList<Object>();
        Map<String, Object> params = setParamToMap(request, serial);
        for (int i = 0; i < list.size(); i++) {
            MethodParameter mp = list.get(i);
            Object value = params.get(mp.getName());
            if (mp.isCheck()) {
                // 空参数验证
                if (mp.isRequired() && (value == null || "".equals(value.toString()))) {
                    log.logInfo("用户参数{}不能为空", serial, mp.getName());
                    result.put("error", ResponseCode.ERROR_API_PARAM_CHECK);
                    return result;
                }
                if (value != null && "".equals(value.toString()) && mp.getFormat() != ParamFormatCheck.String && !formatCheck(value, mp.getFormat())) {
                    log.logInfo("用户参数{}格式验证失败", serial, mp.getName());
                    result.put("error", ResponseCode.ERROR_API_PARAM_CHECK);
                    return result;
                }
                // 默认值
                if (UtilString.isNotEmpty(mp.getDefaultValue()) && (value == null || "".equals(value.toString()))) {
                    value = mp.getDefaultValue();
                }
                // 最大长度
                if (mp.getMaxLength() != -1 && value != null && mp.getMaxLength() < value.toString().length()) {
                    log.logInfo("用户参数{}={}大于最大长度{}", serial, mp.getName(), value, mp.getMaxLength());
                    result.put("error", ResponseCode.ERROR_API_PARAM_CHECK);
                    return result;
                }
                // 最小长度
                if (mp.getMinLength() != -1 && value != null && mp.getMinLength() > value.toString().length()) {
                    log.logInfo("用户参数{}={}小于最小长度{}", serial, mp.getName(), value, mp.getMinLength());
                    result.put("error", ResponseCode.ERROR_API_PARAM_CHECK);
                    return result;
                }
                // 最大值
                if (mp.getMaxValue() != -1 && value != null && new BigDecimal(mp.getMaxValue()).compareTo(new BigDecimal(value.toString())) == -1) {
                    log.logInfo("用户参数{}={}大于最大值{}", serial, mp.getName(), value, mp.getMaxValue());
                    result.put("error", ResponseCode.ERROR_API_PARAM_CHECK);
                    return result;
                }
                // 最小值
                if (mp.getMinValue() != -1 && value != null && new BigDecimal(mp.getMinValue()).compareTo(new BigDecimal(value.toString())) == 1) {
                    log.logInfo("用户参数{}={}小于最小值{}", serial, mp.getName(), value, mp.getMinValue());
                    result.put("error", ResponseCode.ERROR_API_PARAM_CHECK);
                    return result;
                }
            }
            classList.add(mp.getClazz());
            if (mp.getClazz() == HttpServletRequest.class) {
                // 自动封装request参数
                valueList.add(request);
            } else if (mp.getClazz() == HttpServletResponse.class) {
                // 自动封装response参数
                valueList.add(response);
            } else if (mp.getClazz() == ServletContext.class) {
                // 自动封装context参数
                valueList.add(context);
            } else if (mp.getClazz() == LoginInfo.class) {
                // 自动封装loginInfo参数
                if (loginInfo == null && !apiBean.isLogin()) {
                    String auth_code = request.getHeader("code");
                    loginInfo = LoginSession.getLoginInfo(auth_code, serial);
                }
                valueList.add(loginInfo);
            } else if (mp.getClazz() == Serial.class) {
                // 自动封装serial参数
                valueList.add(serial);
            } else {
                // 自动封装其他参数
                valueList.add(convert(value, mp.getClazz()));
            }
        }
        result.put("class", classList);
        result.put("value", valueList);
        return result;
    }

    /**
     * 数据格式验证
     *
     * @param value
     * @param checkType
     * @return
     */
    private boolean formatCheck(Object value, int checkType) {
        Pattern pattern = null;
        switch (checkType) {
            // 整数验证
            case ParamFormatCheck.Int:
            case ParamFormatCheck.Long:
                return intFormat.matcher(value.toString()).matches();
            // 数字验证
            case ParamFormatCheck.Float:
            case ParamFormatCheck.Double:
                return numFormat.matcher(value.toString()).matches();
            case ParamFormatCheck.Date:// 日期验证
                return dateFormat.matcher(value.toString()).matches();
            case ParamFormatCheck.Time:// 时间验证
                return timeFormat.matcher(value.toString()).matches();
            case ParamFormatCheck.DateTime:// 时间时间验证
                return datetimeFormat.matcher(value.toString()).matches();
            case ParamFormatCheck.Email:// 邮箱验证
                return emailFormat.matcher(value.toString()).matches();
            case ParamFormatCheck.Phone:// 手机号验证
                return phoneFormat.matcher(value.toString()).matches();
            case ParamFormatCheck.Json:// JSON验证
            case ParamFormatCheck.Data:// Data验证
                try {
                    JSONObject.parse(value.toString());
                    return true;
                } catch (JSONException e) {
                }
                return false;
            case ParamFormatCheck.JsonArray:// JSON数组验证
            case ParamFormatCheck.DataList:// DataList数组验证
                try {
                    JSONArray.parse(value.toString());
                    return true;
                } catch (JSONException e) {
                }
                return false;
            default:
                return true;
        }
    }

    /**
     * 参数转换
     *
     * @param obj
     * @param type
     * @param <T>
     * @return
     */
    private static <T> T convert(Object obj, Class<T> type) {
        if (obj != null && UtilString.isNotEmpty(obj.toString())) {
            if (type.equals(Integer.class) || type.equals(int.class)) {
                return (T) new Integer(obj.toString());
            } else if (type.equals(Long.class) || type.equals(long.class)) {
                return (T) new Long(obj.toString());
            } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
                return (T) new Boolean(obj.toString());
            } else if (type.equals(Short.class) || type.equals(short.class)) {
                return (T) new Short(obj.toString());
            } else if (type.equals(Float.class) || type.equals(float.class)) {
                return (T) new Float(obj.toString());
            } else if (type.equals(Double.class) || type.equals(double.class)) {
                return (T) new Double(obj.toString());
            } else if (type.equals(Byte.class) || type.equals(byte.class)) {
                return (T) new Byte(obj.toString());
            } else if (type.equals(Character.class) || type.equals(char.class)) {
                return (T) new Character(obj.toString().charAt(0));
            } else if (type.equals(String.class)) {
                return (T) String.valueOf(obj);
            } else if (type.equals(BigDecimal.class)) {
                return (T) new BigDecimal(obj.toString());
            } else if (type.equals(LocalDateTime.class)) {
                return (T) LocalDateTime.parse(obj.toString());
            } else if (type.equals(JSONObject.class)) {
                return (T) JSONObject.parse(obj.toString());
            } else if (type.equals(JSONArray.class)) {
                return (T) JSONArray.parse(obj.toString());
            } else if (type.equals(Data.class)) {
                return (T) Data.parseData(obj.toString(), null);
            } else if (type.equals(DataList.class)) {
                return (T) DataList.parseDataList(obj.toString(), null);
            } else if (type.equals(Date.class)) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    return (T) formatter.parse(obj.toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e.getMessage());
                }
            } else {
                return null;
            }
        } else {
            if (type.equals(int.class)) {
                return (T) new Integer(0);
            } else if (type.equals(long.class)) {
                return (T) new Long(0L);
            } else if (type.equals(boolean.class)) {
                return (T) new Boolean(false);
            } else if (type.equals(short.class)) {
                return (T) new Short("0");
            } else if (type.equals(float.class)) {
                return (T) new Float(0.0);
            } else if (type.equals(double.class)) {
                return (T) new Double(0.0);
            } else if (type.equals(byte.class)) {
                return (T) new Byte("0");
            } else if (type.equals(char.class)) {
                return (T) new Character('\u0000');
            } else if (type.equals(JSONObject.class)) {
                return (T) JSONObject.parse("{}");
            } else if (type.equals(JSONArray.class)) {
                return (T) JSONArray.parse("[]");
            } else if (type.equals(Data.class)) {
                return (T) new Data();
            } else if (type.equals(DataList.class)) {
                return (T) new DataList();
            } else {
                return null;
            }
        }
    }

    /**
     * 获取post请求参数
     *
     * @param request
     * @param serial
     */
    private Map<String, Object> setParamToMap(HttpServletRequest request, Serial serial) {
        if (ServletFileUpload.isMultipartContent(request)) {
            // 如果包含多媒体参数
            return getMultipartContentMap(request, serial);
        } else {
            // 常规请求参数
            return getCommonMap(request, serial);
        }
    }

    /**
     * 获取多媒体上传参数
     *
     * @param request
     * @param serial
     * @return
     */
    private Map<String, Object> getMultipartContentMap(HttpServletRequest request, Serial serial) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // 设置临时存储目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 多媒体上传
        try {
            List<FileItem> formItems = upload.parseRequest(request);
            if (formItems != null && formItems.size() > 0) {
                // 迭代表单数据
                for (FileItem item : formItems) {
                    // 处理不在表单中的字段
                    if (item.isFormField()) {
                        paramMap.put(item.getFieldName(), item.getString());
                    } else {//如果fileitem中封装的是上传文件
                        // 得到上传的文件名称，
                        String filename = item.getName();
                        log.logDebug("上传文件名:{}", serial, filename);
                        if (filename == null || filename.trim().equals("")) {
                            continue;
                        }
                        // 注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
                        // 处理获取到的上传文件的文件名的路径部分，只保留文件名部分
                        filename = filename.substring(filename.lastIndexOf("\\") + 1);
                        //获取item中的上传文件的输入流
                        InputStream in = item.getInputStream();
                        //创建一个文件输出流
                        FileOutputStream out = new FileOutputStream("\\" + filename);
                        //创建一个缓冲区
                        byte buffer[] = new byte[1024];
                        //判断输入流中的数据是否已经读完的标识
                        int len = 0;
                        //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
                        while ((len = in.read(buffer)) > 0) {
                            //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
                            out.write(buffer, 0, len);
                        }
                        //关闭输入流
                        in.close();
                        //关闭输出流
                        out.close();
                        //删除处理文件上传时生成的临时文件
                        item.delete();
                    }
                }
            }
        } catch (Exception e) {
            log.logError("获取多媒体post请求参数失败", serial, e);
        }
        return paramMap;
    }

    /**
     * 获取常规请求参数
     *
     * @param request
     * @param serial
     * @return
     */
    private Map<String, Object> getCommonMap(HttpServletRequest request, Serial serial) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        // 普通key value
        Enumeration names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement().toString();
            paramMap.put(name, request.getParameter(name));
        }
        // post body 体参数
        String data = null;
        try {
            InputStream input = request.getInputStream();
            data = IOUtils.toString(input, "UTF-8");
            String contentType = request.getContentType();
            if (UtilString.isNullOrEmpty(contentType)) {
                contentType = "";
            }
            if (UtilString.isNotEmpty(data) && contentType.toUpperCase().contains("application/json".toUpperCase())) {
                paramMap.putAll(JSONObject.parseObject(data).getInnerMap());
            }
        } catch (IOException e) {
            log.logInfo("解析json格式的请求参数失败：{}", serial, data);
        }
        return paramMap;
    }

    /**
     * 返回处理的json结果
     *
     * @param response
     * @param result
     */
    private void printString(HttpServletResponse response, String result) {
        String encoding = CommonConfig.getEncoding();
        response.addHeader("Content-Type", "text/html;charset=" + encoding);
        response.setContentType("text/html;charset=" + encoding);
        // 打印返回值
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 销毁方法，置空拦截器配置文件
     */
    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}
