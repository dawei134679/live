package com.tinypig.admin.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.tinypig.admin.dao.AdminUserDao;
import com.tinypig.admin.dao.CheckUserDao;
import com.tinypig.admin.dao.UnionDao;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.model.CheckUserModel;
import com.tinypig.admin.model.UnionModel;
import com.tinypig.admin.util.ResponseUtil;

public class CheckUserListServlet extends HttpServlet{
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
		}else if("wait".equalsIgnoreCase(method)){
			this.searchWait(request, response);
		}else if("already".equalsIgnoreCase(method)){
			this.searchAlready(request, response);
		}else if("list".equalsIgnoreCase(method)){
			//TOSY ADD USER
			this.checkList(request, response);
		}
	}
	
protected void checkList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	int rows = Integer.valueOf(request.getParameter("rows"));
	int page = Integer.valueOf(request.getParameter("page"));
	List<CheckUserModel> list = CheckUserDao.getInstance().getCheckList(rows,page);
	try {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total",CheckUserDao.getInstance().CheckUserTotal(null, null,null));
		
		ResponseUtil.writeJSON(response, map);
	} catch (Exception e) {
		System.out.println("getList->exception:"+e.getMessage());
	}	
}

protected void searchWait(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String method = request.getParameter("method");
	int rows = Integer.valueOf(request.getParameter("rows"));
	int page = Integer.valueOf(request.getParameter("page"));
	List<CheckUserModel> list = CheckUserDao.getInstance().getWait(rows,page);
	try {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total", CheckUserDao.getInstance().CheckUserTotal(null,null,method));
		
		ResponseUtil.writeJSON(response, map);
	} catch (Exception e) {
		System.out.println("getList->exception:"+e.getMessage());
	}
		
		
}

protected void searchAlready(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	int rows = Integer.valueOf(request.getParameter("rows"));
	int page = Integer.valueOf(request.getParameter("page"));
	List<CheckUserModel> list = CheckUserDao.getInstance().getAlready(rows,page);
	String method = request.getParameter("method");
	try {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total", CheckUserDao.getInstance().CheckUserTotal(null,null,method));
		
		ResponseUtil.writeJSON(response, map);
	} catch (Exception e) {
		System.out.println("getList->exception:"+e.getMessage());
	}
}

protected void searchUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String uid=request.getParameter("uid");
	String cardId=request.getParameter("cardId");
	int rows = Integer.valueOf(request.getParameter("rows"));
	int page = Integer.valueOf(request.getParameter("page"));
	List<CheckUserModel> list = CheckUserDao.getInstance().getSearch(rows,page,uid,cardId);
	try {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total", CheckUserDao.getInstance().CheckUserTotal(uid, cardId,null));
		
		ResponseUtil.writeJSON(response, map);
	} catch (Exception e) {
		System.out.println("getList->exception:"+e.getMessage());
	}
	}
}
