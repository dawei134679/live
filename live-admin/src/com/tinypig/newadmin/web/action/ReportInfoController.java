package com.tinypig.newadmin.web.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.newadmin.web.entity.ReportInfoParamDto;
import com.tinypig.newadmin.web.service.IReportInfoService;

@Controller
@RequestMapping("/reportInfo")
public class ReportInfoController {

	@Resource
	private IReportInfoService reportInfoService;

	@RequestMapping(value = "/getReportInfoPage", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getReportInfoList(HttpServletRequest request, ReportInfoParamDto params) {
		Integer page = Integer.valueOf(request.getParameter("page"));
		Integer rows = Integer.valueOf(request.getParameter("rows"));
		Integer startIndex = (page - 1) * rows;
		params.setStartIndex(startIndex);
		params.setPageSize(rows);
		Map<String, Object> result = reportInfoService.getReportInfoPage(params);
		return JSONObject.toJSONString(result);
	}

	@RequestMapping(value = "/handler", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String handler(HttpServletRequest request, Long id, String handlemark) {
		AdminUserModel sessionAdminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		if (sessionAdminUser == null) {
			Map<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("success", 201);
			resultMap.put("msg", "请重新登录");
			return JSONObject.toJSONString(resultMap);
		}
		Map<String, Object> resultMap = reportInfoService.handler(id,handlemark,new Long(sessionAdminUser.getUid()));
		return JSONObject.toJSONString(resultMap);
	}
}
