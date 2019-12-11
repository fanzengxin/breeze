package org.breeze.core.filter;

import org.breeze.core.config.CommonConfig;
import org.breeze.core.utils.string.UtilString;

import javax.servlet.*;
import java.io.IOException;

/**
 * @Description: 字符编码过滤器
 * @Auther: 黑面阿呆
 * @Date: 2019/8/6 11:40
 * @Version: 1.0.0
 */
public class CharacterEncodingFilter implements Filter {

    /**
     * 编码过滤器
     */
    private String encoding = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter("encoding");
        if (UtilString.isNullOrEmpty(encoding)) {
            encoding = CommonConfig.getEncoding();
            System.out.println("加载系统默认编码 ： " + encoding);
        } else {
            CommonConfig.setEncoding(encoding);
            System.out.println("加载编码 ： " + encoding);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (encoding != null) {
            request.setCharacterEncoding(encoding);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        this.encoding = null;
    }
}
