package com.tinypig.admin.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
 
public class SetCharacterEncodingFilter implements Filter {
 
    /**
     * The default character encoding to set for requests that pass through
     * this filter.
     */
    protected String encoding = null;
 
    /**
     * The filter configuration object we are associated with.  If this value
     * is null, this filter instance is not currently configured.
     */
    protected FilterConfig filterConfig = null;
 
    public void destroy() {
        this.encoding = null;
        this.filterConfig = null;
    }
 
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
    	request.setCharacterEncoding(encoding);
        chain.doFilter(request, response);
 
    }
 
    /**
     * Place this filter into service.
     * @param filterConfig The filter configuration object
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        this.encoding = filterConfig.getInitParameter("encoding");
    }
 
}
