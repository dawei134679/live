package com.tinypig.admin.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tinypig.admin.dao.AdminUserDao;
import com.tinypig.admin.util.ResponseUtil;

/**
 * Servlet implementation class ManagerUserServlet
 */
public class AdminUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminUserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");
		
		if ("getList".equalsIgnoreCase(method)) {
			this.setIsValid(request, response);
		}else if ("getList".equalsIgnoreCase(method)) {
			this.getList(request, response);
		}else if ("setRole".equalsIgnoreCase(method)) {
			this.setRole(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	protected void setRole(HttpServletRequest request,HttpServletResponse response){
		 int uid = Integer.valueOf(request.getParameter("uid"));
		 int roleId = Integer.valueOf(request.getParameter("roleid"));
		 int ires = AdminUserDao.getInstance().setRole(uid, roleId);
		 try {
			ResponseUtil.write(response, ires);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取猴后台管理员列表
	 * @param request
	 * @param response
	 */
	protected void getList(HttpServletRequest request,HttpServletResponse response){
		String username = request.getParameter("username");
		Integer page = Integer.valueOf(request.getParameter("page"));
		Integer rows = Integer.valueOf(request.getParameter("rows"));
		Integer startIndex = (page - 1) * rows;
		List<Map<String, Object>> list = AdminUserDao.getInstance().getAdminList(username,rows,startIndex);
		try {
			ResponseUtil.write(response, list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置用户有效性
	 * @param request
	 * @param response
	 */
	protected void setIsValid(HttpServletRequest request,HttpServletResponse response){
		int uid = Integer.valueOf(request.getParameter("uid"));
		Boolean isValid = Boolean.valueOf(request.getParameter("isvalid"));
		
		int ires = AdminUserDao.getInstance().setIsValid(uid, isValid);
		try {
			ResponseUtil.write(response, ires);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
