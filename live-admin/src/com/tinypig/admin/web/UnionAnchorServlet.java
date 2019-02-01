package com.tinypig.admin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tinypig.admin.dao.UnionDao;
import com.tinypig.admin.model.AdminUserModel;

@Controller
@RequestMapping("/unionAnchor")
public class UnionAnchorServlet{
	
	@RequestMapping(value = "/getAnchorList")
	@ResponseBody
	public Map<String, Object> getAnchorList(HttpServletRequest request,HttpServletResponse response){
		
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser ==  null) {
			return null;
		}
		String strUnionId = request.getParameter("unionid");
		int unionid = StringUtils.isEmpty(strUnionId) ? 0 : Integer.valueOf(strUnionId);
		
		String strUid = request.getParameter("anchorid");
		int uid = StringUtils.isEmpty(strUid) ? 0 : Integer.valueOf(strUid);
		
		int ipage = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int irows = request.getParameter("rows") == null ? 20 : Integer.valueOf(request.getParameter("rows"));
		if (ipage == 0) {
			ipage = 1;
		}
		
		return UnionDao.getIns().getAnchorUnionsList(unionid, uid, ipage, irows);
	}
	
	@RequestMapping(value = "/getUnionNameList")
	@ResponseBody
	public List<HashMap<String, Object>> getAnchorUnionList(HttpServletRequest request,HttpServletResponse response){
		return UnionDao.getIns().getUnionNameList(true);
	}
	
	@RequestMapping(value = "/editAnchor")
	@ResponseBody
	public Map<String, Object> editAnchor(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, Object> map = new HashMap<String, Object>();
		try {

			AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
			if (adminUser ==  null) {
				map.put("errorMsg", "请先登录");
				return map;
			}
			
			String strUnionid = request.getParameter("unionid");
			if (StringUtils.isEmpty(strUnionid)) {
				map.put("errorMsg", "请输入公会ID");
				return map;
			}
			int unionid = Integer.valueOf(strUnionid);
			
			String strAnchorid = request.getParameter("anchorid");
			if (StringUtils.isEmpty(strAnchorid)) {
				map.put("errorMsg", "请输入主播UID");
				return map;
			}
			int anchorid = Integer.valueOf(strAnchorid);
			
			String strRate = request.getParameter("rate");
			double rate = StringUtils.isEmpty(strRate) ? 0 : Double.valueOf(strRate);
			
			String strSalary = request.getParameter("salary");
			int salary = StringUtils.isEmpty(strSalary) ? 0 : Integer.valueOf(strSalary);
			
			String remarks = request.getParameter("remarks") == null ? "" : request.getParameter("remarks").trim();
			
			if (unionid <= 0 && anchorid <= 0) {
				map.put("errorMsg", "参数不正确");
			} else if (rate <= 0 || rate > 1) {
				map.put("errorMsg", "参数不正确");
			} else {
				int iResult = UnionDao.getIns().editAnchorINUnionSalary(unionid, anchorid, rate, salary, remarks, adminUser.getUid());
				
				if (iResult <= 0) {
					map.put("errorMsg", "更新失败");
				}
			}
		} catch (Exception e) {
			map.put("errorMsg", e.getMessage());
		}
		
		return map;
	}
}
