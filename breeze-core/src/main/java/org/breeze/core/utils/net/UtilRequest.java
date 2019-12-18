package org.breeze.core.utils.net;

import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;
import org.breeze.core.utils.string.UtilString;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 请求方法工具类
 * @Auther: 黑面阿呆
 * @Date: 2019-12-18 09:58
 * @Version: 1.0.0
 */
public class UtilRequest {

    private static Log log = LogFactory.getLog(UtilRequest.class);

    /**
     * 获取request请求真实来源IP
     *
     * @param request
     * @return
     */
    public static String getRealIPAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (UtilString.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (UtilString.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }
}
