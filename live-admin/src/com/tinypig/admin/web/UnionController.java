package com.tinypig.admin.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.service.UnionServiceImpl;
import com.tinypig.admin.util.DateUtil;

@Controller
@RequestMapping("/union")
public class UnionController {

	@Resource
	private UnionServiceImpl unionService;
	
	@RequestMapping(value = "/getsupport")
	@ResponseBody
	public Map<String, Object> getSupport(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, Object> mapResult = new HashMap<String, Object>();
		
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			return null;
		}
		// =9全部 =1有效 =0无效
		int isvalid = request.getParameter("status") == null ? 9:Integer.valueOf(request.getParameter("status"));
		// =0 全部，其他则指定
		int unionid = StringUtils.isEmpty(request.getParameter("unionid")) ? 0:Integer.valueOf(request.getParameter("unionid"));
		
		int ipage = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int irows = request.getParameter("rows") == null ? 20:Integer.valueOf(request.getParameter("rows"));
		if (ipage == 0) {
			ipage = 1;
		}
		
		mapResult =  unionService.getSupport(isvalid, unionid, ipage, irows);
		
		return mapResult;
	}
	
	@RequestMapping(value = "/addsupport")
	@ResponseBody
	public Map<String, Object> addSupport(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, Object> mapResult = new HashMap<String, Object>();
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			mapResult.put("errMsg", "请刷新重新登录");
			return mapResult;
		}
		int unionid = request.getParameter("unionid") == null ? 0 : Integer.valueOf(request.getParameter("unionid"));
		int uid = request.getParameter("uid") == null ? 0 : Integer.valueOf(request.getParameter("uid"));
		
		int isvalid = request.getParameter("isvalid") == null ? 9 : Integer.valueOf(request.getParameter("isvalid"));
		
		String remarks = request.getParameter("remarks") == null ? "" : request.getParameter("remarks");

		if (unionid <=0 || isvalid == 9 || uid <= 0) {
			mapResult.put("errMsg", "参数错误");
			return mapResult;
		}
		
		int addSupport = unionService.addSupport(unionid, uid, isvalid, remarks, adminUser.getUid());
		if (addSupport != 1) {
			mapResult.put("errMsg", "添加错误");
		}
		
		return mapResult;
	}
	
	@RequestMapping(value = "/editsupport")
	@ResponseBody
	public Map<String, Object> editSupport(HttpServletRequest request,HttpServletResponse response){

		Map<String, Object> mapResult = new HashMap<String, Object>();
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			mapResult.put("errMsg", "请刷新重新登录");
			return mapResult;
		}
		
		int unionid = request.getParameter("unionid") == null ? 0 : Integer.valueOf(request.getParameter("unionid"));
		int uid = request.getParameter("uid") == null ? 0 : Integer.valueOf(request.getParameter("uid"));
		int amount = request.getParameter("amount") == null ? 0 : Integer.valueOf(request.getParameter("amount"));
		int isvalid = request.getParameter("isvalid") == null ? 9 : Integer.valueOf(request.getParameter("isvalid"));
		
		if (unionid <=0 || isvalid == 9 || uid <= 0) {
			mapResult.put("errMsg", "参数错误");
			return mapResult;
		}
		
		int editSupport = unionService.editSupport(unionid, uid,amount, isvalid, adminUser.getUid());
		if (editSupport != 1) {
			mapResult.put("errMsg", "修改失败");
		}
		return mapResult;
	}
	
	@RequestMapping(value = "/getSupportConsume")
	@ResponseBody
	public Map<String, Object> getSupportConsume(HttpServletRequest request,HttpServletResponse response){

		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			return null;
		}
		
		String strUid = request.getParameter("uid");
		String strUnionid = request.getParameter("unionid");
		String strYM = request.getParameter("ymd");
		
		Integer uid = StringUtils.isEmpty(strUid) ? 0:Integer.valueOf(strUid);
		Integer unionid = StringUtils.isEmpty(strUnionid) ? 0 : Integer.valueOf(strUnionid);
		
		if (StringUtils.isEmpty(strYM)) {
			return null;
		}
		
		int stime = DateUtil.getYearMonthDay(strYM, 0);
		int etime = DateUtil.getYearMonthDay(strYM, 1);
		
		int ipage = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int irows = request.getParameter("rows") == null ? 20:Integer.valueOf(request.getParameter("rows"));
		if (ipage == 0) {
			ipage = 1;
		}
		
		return unionService.getSupportConsume(uid, unionid, stime, etime, ipage, irows);
	}
}
