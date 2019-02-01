package com.tinypig.admin.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

/**
 * Servlet implementation class AppConfigServlet
 */
public class AppConfigServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AppConfigServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");
		if ("select".equalsIgnoreCase(method)) {
			this.getSelect(request, response);
		}else if ("add".equalsIgnoreCase(method)) {
			// todo
		}else if ("del".equalsIgnoreCase(method)) {
			// todo
		}
	}
	
	protected void getSelect(HttpServletRequest request,HttpServletResponse response){
//		String app = request.getParameter("app");
		String ver = request.getParameter("ver");
		if (StringUtils.isNotEmpty(ver)) {
			
		}else {
			return ;
		}
	}

}
