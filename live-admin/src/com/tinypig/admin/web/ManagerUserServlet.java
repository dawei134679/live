package com.tinypig.admin.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.tinypig.admin.dao.AdminUserDao;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.util.ResponseUtil;

/**
 * Servlet implementation class ManagerUserServlet
 */
public class ManagerUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ManagerUserServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");

		if ("setIsvalid".equalsIgnoreCase(method)) {
			this.setIsValid(request, response);
		} else if ("getList".equalsIgnoreCase(method)) {
			this.getList(request, response);
		} else if ("setRole".equalsIgnoreCase(method)) {
			this.setRole(request, response);
		} else if ("add".equalsIgnoreCase(method)) {
			this.addUser(request, response);
		} else if ("update".equalsIgnoreCase(method)) {
			this.update(request, response);
		} else if ("remove".equalsIgnoreCase(method)) {
			this.removeById(request, response);
		} else if ("getUserNameList".equalsIgnoreCase(method)) {
			this.getUserNameList(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	//删除用户
	private void removeById(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		int uid = Integer.valueOf(request.getParameter("uid"));
		int ires = AdminUserDao.getInstance().deleteByUid(uid);
		if (ires > 0) {
			map.put("errorMsg", "删除用户成功");
			map.put("errorCode", true);
		}else{
			map.put("errorMsg", "删除用户失败");
			map.put("errorCode", false);
		}
		try {
			ResponseUtil.writeJSON(response, map);
		} catch (Exception e) {
			System.out.println("deleteByUid->exception:" + e.getMessage());
		}
	}

	// TOSY ADD USER
	protected void addUser(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> map = new HashMap<String, Object>();
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		if (adminUser.getUid() == 1) {
			int role_id = Integer.valueOf(request.getParameter("role_id"));
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			int createUid = adminUser.getUid();

			int ires = AdminUserDao.getInstance().addAdminUser(username, password, role_id, createUid,0);
			if (ires > 0) {
				map.put("errorMsg", "添加用户成功");
				map.put("errorCode", true);
			}else{
				map.put("errorMsg", "添加用户失败");
				map.put("errorCode", false);
			}
		} else {
			map.put("errorMsg", "你还没有该权限");
			map.put("errorCode", false);
		}
		try {
			ResponseUtil.writeJSON(response, map);
		} catch (Exception e) {
			System.out.println("setRole->exception:" + e.getMessage());
		}
	}

	protected void update(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> map = new HashMap<String, Object>();

		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		if (adminUser.getUid() != 1) {
			map.put("errorMsg", "你还没有该权限");
			map.put("errorCode", false);
		} else {
			int uid = Integer.valueOf(request.getParameter("uid"));
			int roleId = Integer.valueOf(request.getParameter("role_id"));
			int isvalid = Integer.valueOf(request.getParameter("isvalid"));
			int type = Integer.valueOf(request.getParameter("type"));
			String password = request.getParameter("password");
			int ires = AdminUserDao.getInstance().updateUserByUid(uid,roleId,isvalid,type,password);
			if (ires > 0) {
				map.put("errorMsg", "修改用户成功");
				map.put("errorCode", true);
			}else{
				map.put("errorMsg", "修改用户失败");
				map.put("errorCode", false);
			}
		}
		try {
			ResponseUtil.writeJSON(response, map);
		} catch (Exception e) {
			System.out.println("update->exception:" + e.getMessage());
		}
	}

	protected void setRole(HttpServletRequest request, HttpServletResponse response) {
		int uid = Integer.valueOf(request.getParameter("uid"));
		int roleId = Integer.valueOf(request.getParameter("roleid"));
		int ires = AdminUserDao.getInstance().setRole(uid, roleId);
		try {
			ResponseUtil.writeJSON(response, ires);
		} catch (Exception e) {
			System.out.println("setRole->exception:" + e.getMessage());
		}
	}

	/**
	 * 获取猴后台管理员列表
	 * 
	 * @param request
	 * @param response
	 */
	protected void getList(HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter("username");
		Integer page = Integer.valueOf(request.getParameter("page"));
		Integer rows = Integer.valueOf(request.getParameter("rows"));
		Integer startIndex = (page - 1) * rows;

		List<Map<String, Object>> list = AdminUserDao.getInstance().getAdminList(username,rows,startIndex);
		Integer total = AdminUserDao.getInstance().getAdminListTotal(username);
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("rows", list);
			map.put("total", total);

			ResponseUtil.writeJSON(response, map);
		} catch (Exception e) {
			System.out.println("getList->exception:" + e.getMessage());
		}
	}
	
	/**
	 * 获取猴后台管理员名字列表，供前端下拉列表使用
	 * 
	 * @param request
	 * @param response
	 */
	protected void getUserNameList(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> list = AdminUserDao.getInstance().getAdminNameList();
		try {
			ResponseUtil.writeJSON(response, list);
		} catch (Exception e) {
			System.out.println("getList->exception:" + e.getMessage());
		}
	}

	/**
	 * 设置用户有效性
	 * 
	 * @param request
	 * @param response
	 */
	protected void setIsValid(HttpServletRequest request, HttpServletResponse response) {
		int uid = Integer.valueOf(request.getParameter("uid"));
		Boolean isValid = Boolean.valueOf(request.getParameter("isvalid"));

		int ires = AdminUserDao.getInstance().setIsValid(uid, isValid);
		try {
			ResponseUtil.writeJSON(response, ires);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
