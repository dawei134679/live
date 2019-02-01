package com.tinypig.admin.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class SessionTimeOutFilter implements Filter {
	
	//将无需过滤的请求
	private String[] paths = new String[]{"/login/login", "/index.jsp"};

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
				//将参数转型
				HttpServletRequest request = 
					(HttpServletRequest) req;
				HttpServletResponse response = 
					(HttpServletResponse) res;
				String sp = request.getServletPath();
				//若当前路径是排除的路径之一,
				//则无需检查是否登录,请求继续.
				if(needIgnore(sp)) {
					chain.doFilter(request, response);
					return;
				}
				//从session中获取账号
				Object user= request.getSession().getAttribute("currentUser");
				//判断用户是否已登录,或已超时
				if(user == null) {
					//未登录或者超时,重定向到登录页面
					//response.sendRedirect(request.getContextPath() + "/index.jsp?top=1");
					response.setContentType("text/html;charset=utf-8");
					PrintWriter out=response.getWriter();
					out.println("<script>top.location.href = '"+ request.getContextPath()+"/index.jsp' </script>");
					out.flush();
					out.close();
				} else {
					//已登录,请求继续执行
					chain.doFilter(request, response);
				}
	}

	@Override
	public void init(FilterConfig cfg) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	
	private boolean needIgnore(String baseUrl) {
		if (baseUrl.endsWith(".html") || baseUrl.endsWith(".htm") || baseUrl.endsWith(".js") || baseUrl.endsWith(".css")
				|| baseUrl.endsWith(".jpg") || baseUrl.endsWith(".jpeg") || baseUrl.endsWith(".gif") || baseUrl.endsWith(".png")) {
			return true;
		}
		for (String url : paths) {
			if (StringUtils.equals(url, baseUrl)) {
				return true;
			}
		}
		return false;
	}

}
