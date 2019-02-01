package com.tinypig.admin.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.tinypig.admin.dao.AdminMenuDao;
import com.tinypig.admin.dao.AdminRoleDao;
import com.tinypig.admin.model.AdminRoleModel;
import com.tinypig.admin.util.ResponseUtil;

public class AdminMenuServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminMenuServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");
		
		if ("getMenuTree".equalsIgnoreCase(method)) {
			this.getMenuTree(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	

	/**
	 * 
	 * @Description: 获取菜单树的json串
	 * @param request
	 * @param response   
	 * @return void  
	 * @throws
	 * @author guojp
	 * @date 2016-6-29
	 */
	protected void getMenuTree(HttpServletRequest request,HttpServletResponse response){
		int uid = Integer.valueOf(request.getParameter("uid"));
		AdminRoleModel adminRoleModel = AdminRoleDao.getInstance().getRoleByUid(uid);
		String menuids = adminRoleModel.getMenu_ids();
		JSONArray jsonArray = AdminMenuDao.getMenuListByMenuIds(menuids);
		
		try {
			ResponseUtil.write(response, jsonArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
