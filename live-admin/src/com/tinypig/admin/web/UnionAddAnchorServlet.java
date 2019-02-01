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

public class UnionAddAnchorServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6226505269419133163L;
	
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * unionid	工会ID
	 * uid		主播ID
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if(null == adminUser){
			return;
		}
		
		JSONObject result = new JSONObject();
		
		//工会ID		主播ID
		String unionid = request.getParameter("unionid");
		String uid = request.getParameter("uid");
		String remarks = request.getParameter("remarks");
		remarks = remarks == null?"":remarks;
		if(null == unionid || null == uid){
			try {
				result.put("fail", "缺少参数");
				ResponseUtil.write(response, result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		//清理主播关系？？
		//设置主播关系和主播所属工会状态
		Boolean bOk = UnionDao.getIns().addAnchorToUnion(unionid,uid,adminUser.getUid(), remarks);
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
