package com.tinypig.admin.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.tinypig.admin.dao.PayInfoDao;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.util.DateUtil;
import com.tinypig.admin.util.ResponseUtil;

public class PayInfoServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5608058080092934020L;
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	/**
	 * ARG:
	 * 		nickname		昵称
	 * 		uid			              主播ID
	 * 		tmstart			开始时间或者空
	 * 		tmend			开始时间或者空
	 * 
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String method = request.getParameter("method");
		
		if ("search".equalsIgnoreCase(method)) {
			this.searchUser(request, response);
		}
		
		
	}

	protected void searchUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		
		if (adminUser ==  null) {
			return;
		}
		String status = request.getParameter("status");
		String uid = request.getParameter("uid");
		String userSource = request.getParameter("userSource");
		
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		
		if (startdate == null || enddate == null) {
			return;
		}
	
		try {
	
			Long stime = DateUtil.dateToLong(startdate, "yyyy-MM-dd", "other", 0);
			Long etime = DateUtil.dateToLong(enddate, "yyyy-MM-dd", "other", 0) + 24*3600;
			
			Map<String, Object> map =PayInfoDao.getInstance().getPayInfo(userSource,Integer.valueOf(status),StringUtils.isEmpty(uid) ? 0 : Integer.valueOf(uid), stime, etime,Integer.valueOf(page),Integer.valueOf(rows),adminUser.getUid());
			
			ResponseUtil.writeJSON(response, map);
		} catch (Exception e) {
			System.out.println("getList->exception:"+e.getMessage());
		}
	}
}
