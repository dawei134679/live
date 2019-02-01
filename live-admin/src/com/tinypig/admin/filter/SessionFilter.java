package com.tinypig.admin.filter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

/**
 * 用于检查用户是否登录了系统的过滤器
 * @author weber
 */
public class SessionFilter implements Filter {

    /** 要检查的 session 的名称 */
    private String sessionKey;
    
    /** 需要排除（不拦截）的URL的正则表达式 */
    private Pattern excepUrlPattern;
    
    /** 检查不通过时，转发的URL */
    private String forwardUrl;

    @Override
    public void init(FilterConfig cfg) throws ServletException {
        sessionKey = cfg.getInitParameter("sessionKey");

        String excepUrlRegex = cfg.getInitParameter("excepUrlRegex");
        if (!StringUtils.isBlank(excepUrlRegex)) {
            excepUrlPattern = Pattern.compile(excepUrlRegex);
        }

        forwardUrl = cfg.getInitParameter("forwardUrl");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        // 如果 sessionKey 为空，则直接放行
        if (StringUtils.isBlank(sessionKey)) {
            chain.doFilter(req, res);
            return;
        }

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String servletPath = request.getServletPath();

        // 如果请求的路径与forwardUrl相同，或请求的路径是排除的URL时，则直接放行
        if (servletPath.equals(forwardUrl) || excepUrlPattern.matcher(servletPath).matches() || isResource(servletPath)||isPullAnchorList(servletPath)) {
            chain.doFilter(req, res);
            return;
        }

        Object sessionObj = request.getSession().getAttribute(sessionKey);
        // 如果Session为空，则跳转到指定页面
        if (sessionObj == null) {
            String contextPath = request.getContextPath();
            String redirect = servletPath + "?" + StringUtils.defaultString(request.getQueryString());
            response.sendRedirect(contextPath + StringUtils.defaultIfEmpty(forwardUrl, "/")
                            + "?redirect=" + URLEncoder.encode(redirect, "UTF-8"));
        } else {
            chain.doFilter(req, res);
        }
    }
    
    private boolean isResource(String path){
    	path = path.toLowerCase();
    	System.out.println("path is:" + path);
    	if(path.endsWith("jpg") || path.endsWith("js") || path.endsWith("css") || path.endsWith("png")){
    		return true;
    	}else{
    		return false;
    	}
    }

    private boolean isPullAnchorList(String path){
    	return path.indexOf("monitor?method=monitor") != -1;
    }
    
    @Override
    public void destroy() {
    }
}