package com.tinypig.admin.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.dao.UserDao;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.model.UserInfoModel;
import com.tinypig.admin.util.ResponseUtil;

public class UserInfoServlet extends HttpServlet{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5890994443184645444L;
	public UserInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");
		
		if ("search".equalsIgnoreCase(method)) {
			this.searchInfo(request, response);
		}else if ("status".equalsIgnoreCase(method)) {
			this.statusInfo(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	//搜索用户信息
	protected void searchInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uid = request.getParameter("uid");

		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if(null == adminUser){
			return;
		}
		JSONObject result=new JSONObject();
		
		List<UserInfoModel> data = UserDao.getInstance().getUserInfo(Integer.parseInt(uid),1 == adminUser.getRole_id());
		result.put("rows", data);
		result.put("total", data.size());
		
		try {
			ResponseUtil.write(response,result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//状态查找用户信息
	protected void statusInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String status = request.getParameter("uid");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		List<UserInfoModel> data = UserDao.getInstance().statusUserBaseInfo(Integer.parseInt(status),page,rows);
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("rows", data);
			map.put("total", UserDao.getInstance().statusUserBaseTotal(Integer.parseInt(status)));
			ResponseUtil.writeJSON(response, map);
		} catch (Exception e) {
			System.out.println("getList->exception:"+e.getMessage());
		}
		
	}
	
}
