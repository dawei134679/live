package com.tinypig.admin.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.dao.UserDao;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.model.UserMannerModel;
import com.tinypig.admin.util.ResponseUtil;

public class UserManagerServlet extends HttpServlet{
	
	private static final long serialVersionUID = 5890994443184645444L;
	public UserManagerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");
		if ("search".equalsIgnoreCase(method)) {
			this.searchInfo(request, response);
		}else if ("status".equalsIgnoreCase(method)) {
			this.statusInfo(request, response);
		}else if ("edit".equalsIgnoreCase(method)){
			this.edithandle(request, response);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		doGet(request, response);
	}
	
	protected void searchInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uid = request.getParameter("uid");
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if(null == adminUser){
			return;
		}
		JSONObject result=new JSONObject();
		List<Map<String, Object>> data = UserDao.getInstance().getManagerUser(Integer.parseInt(uid));
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
		int page = Integer.valueOf(request.getParameter("page"));
		int rows = Integer.valueOf(request.getParameter("rows"));
		List<Map<String, Object>> data = UserDao.getInstance().statusUserManager(Integer.parseInt(status),page,rows);
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("rows", data);
			map.put("total", UserDao.getInstance().statusUserBaseTotal(Integer.parseInt(status)));
			ResponseUtil.writeJSON(response, map);
		} catch (Exception e) {
			System.out.println("getList->exception:"+e.getMessage());
		}
	}
	
	protected void edithandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		
		Map<String, Object> result = new HashMap<String, Object>();
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if(adminUser == null){
			result.put("errorMsg", "长时间未操作请重新登录");
		}else{
			int admin=adminUser.getUid();
			String cause=request.getParameter("cause");
			
			if (cause == null) {
				result.put("errorMsg", "理由不能为空");
			}else{
				cause = cause.trim();
				if (StringUtils.isEmpty(cause)) {
					result.put("errorMsg", "理由不能为空");
				}else {
					String id=request.getParameter("id");
					String uid=request.getParameter("uid");
					result = UserDao.getInstance().edithandle(Integer.parseInt(id),cause,Integer.parseInt(uid),admin);
				}
			}
		}
		
		try {
			ResponseUtil.writeJSON(response, result);
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
