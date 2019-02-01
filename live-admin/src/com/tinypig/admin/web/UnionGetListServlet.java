package com.tinypig.admin.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.tinypig.admin.dao.UnionDao;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.model.UnionModel;

public class UnionGetListServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5608058080092934020L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * ARG:
	 * 		unionname		unionname
	 * 		ownerid			会长ID
	 * 		tmstart			开始时间或者空
	 * 		tmend			开始时间或者空
	 * 
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		AdminUserModel adminUser = (AdminUserModel)req.getSession().getAttribute("currentUser");
		if(null == adminUser){
			return;
		}
		
		String unionname = "";
		String ownerid = "";
		String unionid = "";
		
		String page = req.getParameter("page");
		String rows = req.getParameter("rows");
		String sort = req.getParameter("sort");
		String order = req.getParameter("order");
		String status = req.getParameter("searchstatus");
		
		String condition = req.getParameter("condition");
		if("1".equals(condition)){
			unionid = req.getParameter("content");
		}else if("2".equals(condition)){
			unionname = req.getParameter("content");
		}else if("3".equals(condition)){
			ownerid = req.getParameter("content");
		}

//		JSONObject result=new JSONObject();

		//搜索		//公会昵称/族长ID/添加时间/旗下主播个数/公会总收益/公会详情
		List<UnionModel> data = UnionDao.getIns().getUnions(ownerid, unionname, unionid, status, page, rows,sort,order);
		//返回

		resp.setContentType("application/json;charset=utf-8");
		HashMap<String, Object> map = new HashMap<String,Object>();
		map.put("rows", data);
		map.put("total", UnionDao.getIns().getUnionsTotal(ownerid,unionname));
		resp.getWriter().write(JSON.toJSONString(map));
	}
	
//	public static void main(String[] args) {
//		System.out.println(UnionDao.getIns().getUnions("", "tosy", "", "").toString());
//	}
}
