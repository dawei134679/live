package com.tinypig.admin.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.dao.UnionDao;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.model.UserBaseInfo;
import com.tinypig.admin.util.ResponseUtil;

public class UnionGetAnchorsServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6725155730159116351L;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	
	/**
	 * unionid	工会id
	 * nickname	主播昵称
	 * uid		主播id
	 * tmstart	时间范围开始
	 * tmend    时间范围结束
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if(null == adminUser){
			return;
		}
		JSONObject result=new JSONObject();
		
		//工会ID/主播昵称/主播ID	时间范围
		String unionid = request.getParameter("unionid");
		String nickname = request.getParameter("nickname");
		String uid = request.getParameter("uid");
		
		//获取time范围
		String strtmstart = request.getParameter("startDate");
		String strtmend = request.getParameter("endDate");
		if(null == unionid){
			try {
				result.put("fail", "缺少参数");
				ResponseUtil.write(response, result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		
		//搜索	主播昵称/主播ID/主播收益/播出时长/有效天数/主播收益/添加时间
		List<UserBaseInfo> data = UnionDao.getIns().getAnchorsInfoFromUnion(unionid, uid, nickname, strtmstart, strtmend
				,1 == adminUser.getRole_id());
		result.put("rows", data);
		result.put("total", data.size());
		
		try {
			ResponseUtil.write(response,result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
