package org.breeze.core.service.service;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.breeze.core.annotation.service.DataBase;
import org.breeze.core.bean.api.BeanFactory;
import org.breeze.core.database.manager.ConnectionManager;
import org.breeze.core.database.transaction.DBTransaction;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Connection;

/**
 * @Description: Service注解处理类
 * @Auther: 黑面阿呆
 * @Date: 2019/2/20 15:31
 * @Version: 1.0.0
 */
public class ServiceInterceptor implements MethodInterceptor {

    private static Log log = LogFactory.getLog(ServiceInterceptor.class);

    @Override
    public Object intercept(Object object, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        // 获取service对象
        Object service = BeanFactory.getServiceBean(method.getDeclaringClass());
        if (service != null) {
            // 判断是否加载DataBase注解
            DataBase dataBase = method.getAnnotation(DataBase.class);
            if (dataBase != null) {
                // 如果加载注解，则在service层打开数据库链接
                String connectionName = dataBase.connection();
                Connection conn = null;
                DBTransaction dt = null;
                try {
                    conn = ConnectionManager.getConnection(connectionName);
                    if (dataBase.transaction()) {
                        // 判断是否开启事务
                        dt = DBTransaction.getInstance(conn);
                    }
                    // 自动注入connection参数
                    Parameter[] parameters = method.getParameters();
                    for (int i = 0; i < parameters.length; i++) {
                        if (parameters[i].getType().equals(Connection.class)) {
                            objects[i] = conn;
                            break;
                        }
                    }
                    // 执行方法
                    Object result = methodProxy.invokeSuper(object, objects);
                    if (dt != null && dataBase.transaction()) {
                        // 提交事务
                        dt.commit();
                    }
                    return result;
                } catch (Exception e) {
                    if (dt != null && dataBase.transaction()) {
                        // 失败回滚事务
                        dt.rollback();
                    }
                    log.logError("数据库操作失败", e);
                } finally {
                    if (dt != null && dataBase.transaction()) {
                        // 设置事务自动提交
                        dt.setAutoCommit();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                }
            }
        }
        return methodProxy.invokeSuper(object, objects);
    }
}
