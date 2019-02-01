package com.tinypig.admin.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tinypig.admin.dao.CheckUserDao;
import com.tinypig.admin.util.ResponseUtil;

public class CheckUserPassServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckUserPassServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String uid = request.getParameter("uid");
		System.out.println(uid);
		int list;
		try {
			list = CheckUserDao.getInstance().CheckPass(uid);
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
