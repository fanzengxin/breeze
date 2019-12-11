package org.breeze.core.bean.api;


import net.sf.cglib.proxy.Enhancer;
import org.breeze.core.annotation.common.AutoAdd;
import org.breeze.core.annotation.controller.*;
import org.breeze.core.annotation.repository.Repository;
import org.breeze.core.annotation.service.Service;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;
import org.breeze.core.service.dao.DaoInterceptor;
import org.breeze.core.service.service.ServiceInterceptor;
import org.breeze.core.utils.string.UtilString;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Description: 注解实例初始化
 * @Auther: 黑面阿呆
 * @Date: 2019/2/18 22:10
 * @Version: 1.0.0
 */
public class BeanFactory {

    private static Log log = LogFactory.getLog(BeanFactory.class);

    // service 请求配置信息map
    private static Map<String, ApiConfig> apiConfigMap = new HashMap<String, ApiConfig>();
    // service 实例map
    private static Map<Class<?>, Object> controllerMap = new HashMap<>();
    // handler 实例map
    private static Map<Class<?>, Object> handlerMap = new HashMap<>();
    // repository 实例map
    private static Map<Class<?>, Object> repositoryMap = new HashMap<>();

    /**
     * 遍历所有实例，解析注解
     */
    public static void scanAnnotationBean() {
        try {
            scanAndInfo();
            aotuInitField();
        } catch (IllegalAccessException e) {
            log.logError("初始化bean出错", e);
        }
    }

    /**
     * 扫描并实例化bean
     */
    private static void scanAndInfo() {
        log.logInfo("初始化所有service配置信息");
        String packageName = "";
        String packageDirName = packageName.replaceAll(".", "/");
        boolean recursive = true;
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources("");
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    // 文件扫描，非代码不扫描注解
                    if (!UtilString.endWith(url.getPath(), "/classes/")) {
                        continue;
                    }
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findClassile(packageName, filePath, recursive);
                } else if ("jar".equals(protocol)) {
                    // jar包扫描，非core核心jar包不扫描注解
                    if (!UtilString.endWith(url.getPath(), "/core-1.0.0.jar!/")) {
                        continue;
                    }
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (UtilString.startWith(name, "/")) {
                                // 获取后面的字符串
                                name = UtilString.subString(name, 1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (UtilString.startWith(name, packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    // 如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        // 去掉后面的".class" 获取真正的类名
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            initBean(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                            log.logError("未找到指定的类：{}", e, packageName + '.' + className);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        log.logError("初始化所有实体类配置信息失败", e);
                    }
                }
            }
        } catch (IOException e) {
            log.logError("初始化所有实体类配置信息失败", e);
        }
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     */
    public static void findClassile(String packageName, String packagePath, final boolean recursive) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            log.logWarn("用户定义包名 [{}] 下没有任何文件", packageName);
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                String fName = null;
                if (UtilString.isNullOrEmpty(packageName)) {
                    fName = file.getName();
                } else {
                    fName = packageName + "." + file.getName();
                }
                findClassile(fName, file.getAbsolutePath(), recursive);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    initBean(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    log.logError("未找到指定的类：{}", e, packageName + '.' + className);
                }
            }
        }
    }

    /**
     * 自动注入引用实例
     */
    private static void aotuInitField() throws IllegalAccessException {
        log.logInfo("自动注入引用实例");
        for (Class<?> service : handlerMap.keySet()) {
            Object instance = BeanFactory.getServiceBean(service);
            setField(service, instance);
        }
        for (Class<?> controller : controllerMap.keySet()) {
            Object instance = BeanFactory.getControllerBean(controller);
            setField(controller, instance);
        }
    }

    /**
     * 自动注入字段
     *
     * @param action
     * @param instance
     * @throws IllegalAccessException
     */
    private static void setField(Class<?> action, Object instance) throws IllegalAccessException {
        Field[] fields = action.getDeclaredFields();
        // 遍历实例对象下的所有field
        for (Field field : fields) {
            // 对象下所有包含AutoAdd注解的字段
            if (field.isAnnotationPresent(AutoAdd.class)) {
                Class<?> c = field.getType();
                Object bh = BeanFactory.getServiceBean(c);
                if (bh != null) {
                    field.setAccessible(true);
                    field.set(instance, bh);
                }
                Object br = BeanFactory.getRepositoryBean(c);
                if (br != null) {
                    field.setAccessible(true);
                    field.set(instance, br);
                }
            }
        }
    }

    /**
     * 添加实例至相应的map缓存
     *
     * @param action
     */
    private static void initBean(Class<?> action) {
        if (action.isAnnotationPresent(Controller.class)) {
            addMethodInfo(action);
            addControllerBean(action);
        } else if (action.isAnnotationPresent(Service.class)) {
            addServiceBean(action);
        } else if (action.isAnnotationPresent(Repository.class)) {
            addRepositoryBean(action);
        }
    }

    /**
     * 加载指定control实例
     *
     * @param action
     */
    public static void addControllerBean(Class<?> action) {
        try {
            controllerMap.put(action, action.newInstance());
        } catch (Exception e) {
            log.logError("初始化control bean失败：{}", e, action.getName());
        }
    }

    /**
     * 遍历解析service下的所有注解方法
     *
     * @param action
     */
    public static void addMethodInfo(Class<?> action) {
        // 获取controller注解信息
        Controller controller = action.getAnnotation(Controller.class);
        // 遍历类下所有方法
        Method[] methods = action.getMethods();
        for (Method method : methods) {
            Api api = method.getAnnotation(Api.class);
            if (api == null) {
                // 如果方法上没有Api注解则不作为对外访问接口方法
                continue;
            }
            // 获取参数验证注解
            Params params = method.getAnnotation(Params.class);
            Map<String, Param> paramMap = new HashMap<String, Param>();
            if (params != null && params.value().length > 0) {
                for (int i = 0; i < params.value().length; i++) {
                    paramMap.put(params.value()[i].name(), params.value()[i]);
                }
            }
            // 获取该方法下所有参数信息，因为在JDK8下编译添加了-parameters参数。所有参数名均会保留
            Parameter[] parameters = method.getParameters();
            List<MethodParameter> paramList = new ArrayList<MethodParameter>();
            // 遍历封装方法参数，包括参数注入及参数验证
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                MethodParameter methodParameter = new MethodParameter();
                methodParameter.setName(parameter.getName());
                methodParameter.setClazz(parameter.getType());
                // 获取参数验证注解
                Param param = paramMap.get(parameter.getName());
                if (param != null) {
                    methodParameter.setCheck(true);
                    methodParameter.setDescription(param.description());
                    methodParameter.setRequired(param.required());
                    methodParameter.setFormat(param.format());
                    methodParameter.setDefaultValue(param.defaultValue());
                    methodParameter.setMaxLength(param.maxLength());
                    methodParameter.setMinLength(param.minLength());
                    methodParameter.setMaxValue(param.maxValue());
                    methodParameter.setMinValue(param.minValue());
                }
                paramList.add(methodParameter);
            }
            // 获取权限注解信息
            Permission permission = method.getAnnotation(Permission.class);
            ApiConfig apiConfig = null;
            if (permission == null) {
                apiConfig = new ApiConfig(
                        controller.mapper(),
                        controller.singleton(),
                        api.value(),
                        action.getName(),
                        method.getName(),
                        api.method(),
                        api.descripition(),
                        paramList,
                       null,
                        true,
                        true);
            } else {
                apiConfig = new ApiConfig(
                        controller.mapper(),
                        controller.singleton(),
                        api.value(),
                        action.getName(),
                        method.getName(),
                        api.method(),
                        api.descripition(),
                        paramList,
                        permission.value(),
                        permission.login(),
                        permission.sign());
            }
            apiConfigMap.put(apiConfig.getUrl(), apiConfig);
        }
    }

    /**
     * 获取指定URL的service配置实例
     *
     * @param url
     * @return
     */
    public static ApiConfig getApiConfig(String url) {
        return apiConfigMap.get(url);
    }

    /**
     * 获取指定control实例
     *
     * @param action
     * @return
     */
    public static Object getControllerBean(Class<?> action) {
        return controllerMap.get(action);
    }

    /**
     * 加载指定handler实例
     *
     * @param action
     */
    public static void addServiceBean(Class<?> action) {
        try {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(action);
            enhancer.setCallback(new ServiceInterceptor());
            Object serviceBean = enhancer.create();
            handlerMap.put(action, serviceBean);
        } catch (Exception e) {
            log.logError("初始化service bean失败：{}", e, action.getName());
        }
    }

    /**
     * 获取指定handler实例
     *
     * @param service
     * @return
     */
    public static Object getServiceBean(Class<?> service) {
        return handlerMap.get(service);
    }


    /**
     * 加载指定repository实例
     *
     * @param action
     */
    public static void addRepositoryBean(Class<?> action) {
        try {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(action);
            enhancer.setCallback(new DaoInterceptor());
            Object repositoryBean = enhancer.create();
            repositoryMap.put(action, repositoryBean);
        } catch (Exception e) {
            log.logError("初始化service bean失败：{}", e, action.getName());
        }
    }

    /**
     * 获取指定repository实例
     *
     * @param repository
     * @return
     */
    public static Object getRepositoryBean(Class<?> repository) {
        return repositoryMap.get(repository);
    }
}
