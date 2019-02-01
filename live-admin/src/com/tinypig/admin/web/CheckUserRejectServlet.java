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

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.dao.CheckUserDao;
import com.tinypig.admin.model.CheckUserModel;
import com.tinypig.admin.util.ResponseUtil;

public class CheckUserRejectServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckUserRejectServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String uid = request.getParameter("uid");
		String cause=new String(request.getParameter("reason").toString());
		int list;
		try {
			list = CheckUserDao.getInstance().CheckReject(uid,cause);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("rows", list);
			map.put("total", 0);
			ResponseUtil.writeJSON(response, map);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getList->exception:"+e.getMessage());
		}

	}
}
