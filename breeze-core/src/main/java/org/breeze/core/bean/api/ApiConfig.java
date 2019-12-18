package org.breeze.core.bean.api;

import org.breeze.core.utils.string.UtilString;

import java.util.List;

/**
 * @Description: 请求接口实体类
 * @Auther: 黑面阿呆
 * @Date: 2019/2/9 20:27
 * @Version: 1.0.0
 */
public class ApiConfig {

    // 请求前缀
    private String mapping;
    // 是否单例
    private boolean singleton;
    // 请求URL节点
    private String api;
    // 类名
    private String className;
    // 方法名
    private String method;
    // 允许的请求方法
    private String allowMethod;
    // 方法描述
    private String description;
    // 权限代码
    private String permissionCode;
    // 请求参数验证
    private List<MethodParameter> parameters;
    // 是否需要登录验证
    private boolean login;
    // 是否需要进行签名验证
    private boolean sign;

    public ApiConfig() {
    }

    public ApiConfig(String mapping, boolean singleton, String api, String className, String method, String allowMethod,
                     String description, List<MethodParameter> parameters, String permissionCode, boolean login, boolean sign) {
        if (mapping.startsWith("/")) {
            mapping = mapping.substring(1);
        }
        if (mapping.endsWith("/")) {
            mapping = mapping.substring(0, mapping.length() - 1);
        }
        this.mapping = mapping;
        this.singleton = singleton;
        if (api.startsWith("/")) {
            api = api.substring(1);
        }
        if (api.endsWith("/")) {
            api = api.substring(0, api.length() - 1);
        }
        this.api = api;
        this.className = className;
        this.method = method;
        this.allowMethod = allowMethod;
        this.description = description;
        this.parameters = parameters;
        this.permissionCode = permissionCode;
        this.login = login;
        this.sign = sign;
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    /**
     * 封装请求url
     *
     * @return
     */
    public String getUrl() {
        StringBuffer sb = new StringBuffer("/service");
        if (UtilString.isNotEmpty(mapping)) {
            sb.append("/").append(mapping);
        }
        if (UtilString.isNotEmpty(api)) {
            sb.append("/").append(api);
        }
        return sb.toString();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public boolean isSign() {
        return sign;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public List<MethodParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<MethodParameter> parameters) {
        this.parameters = parameters;
    }

    public String getAllowMethod() {
        return allowMethod;
    }

    public void setAllowMethod(String allowMethod) {
        this.allowMethod = allowMethod;
    }
}
