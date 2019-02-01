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

public class UnionAddServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -863295646213062975L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * unionname
	 * onwerid
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if(null == adminUser){
			return;
		}
		JSONObject result=new JSONObject();
		
		//添加 公会昵称/族长ID/添加时间/旗下主播个数/公会总收益/公会详情
		String unionname = request.getParameter("unionname");
		String ownerid = request.getParameter("ownerid");
		String remark = request.getParameter("desc");
		String adminuid = request.getParameter("adminuid");
		if(null == unionname || null == ownerid || null ==  adminuid){
			try {
				result.put("fail", "缺少参数");
				ResponseUtil.write(response,result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		
		try {

			int iuid = Integer.valueOf(adminuid);
			Boolean bOk = UnionDao.getIns().addUnion(unionname,ownerid,remark,iuid,adminUser.getUid());
			if(bOk){
				result.put("success", bOk);
			}else{
				result.put("fail", bOk);
			}
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
