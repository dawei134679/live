package com.tinypig.admin.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tinypig.admin.dao.CashInfoDao;
import com.tinypig.admin.model.CashInfoModel;
import com.tinypig.admin.util.ResponseUtil;

public class CashInfoServlet extends HttpServlet{
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
		System.out.println("sssssss14s321s3");
		String method = request.getParameter("method");
		
		if ("search".equalsIgnoreCase(method)) {
			this.searchUser(request, response);
		}
		
		
	}
	


protected void searchUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String uid=request.getParameter("uid");
	String startdate=request.getParameter("startdate");
	String enddate=request.getParameter("enddate");
	String page = request.getParameter("page");
	String rows = request.getParameter("rows");
	List<CashInfoModel> list=CashInfoDao.getInstance().getCashInfo(uid, startdate, enddate,page,rows);
//	List<CheckUserModel> list = CheckUserDao.getInstance().getSearch(uid,cardId);
	try {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total",CashInfoDao.getInstance().getCashTotal(uid, startdate, enddate));
		
		ResponseUtil.writeJSON(response, map);
	} catch (Exception e) {
		System.out.println("getList->exception:"+e.getMessage());
	}
		
		
	}
	
	

}
