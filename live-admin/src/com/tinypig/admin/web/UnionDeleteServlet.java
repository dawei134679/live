package com.tinypig.admin.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.dao.UnionDao;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.util.ResponseUtil;

public class UnionDeleteServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3568031287448154194L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * unionid	工会ID
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if(null == adminUser){
			return;
		}
		JSONObject result=new JSONObject();
		
		String unionid = request.getParameter("unionid");
		if(null == unionid ){
			try {
				result.put("fail", "缺少参数");
				ResponseUtil.write(response, result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		
		//工会ID
		//获取主播列表
		//解除关系	设置为无家族
		//家族删除
		Boolean bOk = UnionDao.getIns().deleteUnion(unionid,adminUser.getUid());
		if(bOk){
			result.put("success", bOk);
		}else{
			result.put("fail", bOk);
		}
		
		try {
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
