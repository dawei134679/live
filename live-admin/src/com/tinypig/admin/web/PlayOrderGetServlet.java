package com.tinypig.admin.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.dao.PayOrderDao;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.model.PayOrderModel;
import com.tinypig.admin.util.ResponseUtil;

public class PlayOrderGetServlet extends HttpServlet {

	/**
	 * srcuid
	 * tmstart	下单时间范围
	 * tmend	下单时间范围
	 * 
	 */
	private static final long serialVersionUID = -4849977658087340983L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * ARG:
	 * 		srcuid			充值id
	 * 		paytype			充值方式
	 * 		tmstart			开始时间或者空
	 * 		tmend			开始时间或者空
	 * 		status			0生成订单，1已到支付平台等待支付结果，2已支付，3取消
	 * 
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if(null == adminUser){
			return;
		}
		JSONObject result=new JSONObject();
		
		String srcuid = request.getParameter("srcuid");
		String strpaytype = request.getParameter("paytype");
		String strtmstart = request.getParameter("tmstart");
		String strtmend = request.getParameter("tmend");
		String strstatus = request.getParameter("status");

		if(null == srcuid || null == strtmstart || null == strtmend || null == strpaytype){
			try {
				result.put("fail", "缺少参数");
				ResponseUtil.write(response, result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		//搜索		//公会昵称/族长ID/添加时间/旗下主播个数/公会总收益/公会详情
		List<PayOrderModel> data = new PayOrderDao().getPayOrderList(srcuid,strpaytype,strstatus,strtmstart,strtmend);
		//返回
		try {
			result.put("success", data);
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
