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
import com.tinypig.admin.dao.CheckUserDao;
import com.tinypig.admin.dao.UserDao;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.model.CheckUserModel;
import com.tinypig.admin.model.UserBaseInfo;
import com.tinypig.admin.util.ResponseUtil;

public class CheckUserInfoServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckUserInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if(null == adminUser){
			return;
		}
		
		String uid = request.getParameter("uid");
		List<CheckUserModel> list = CheckUserDao.getInstance().getCheckInfo(uid);
		UserBaseInfo list2 = UserDao.getInstance().getUserPhone(Integer.parseInt(uid),1==adminUser.getRole_id());
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("rows", list);
			map.put("total", 0);
			map.put("phone", list2.getPhone());
			ResponseUtil.writeJSON(response, map);
		} catch (Exception e) {
			System.out.println("getList->exception:"+e.getMessage());
		}
	}
}
