package com.tinypig.admin.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.service.AdminServiceImpl;
import com.tinypig.admin.util.DateUtil;

@Controller
@RequestMapping("/adminoperat")
public class AdminOperatController {
	
	@Resource
	private AdminServiceImpl adminService;

	@RequestMapping(value = "/operat/logs")
	@ResponseBody
	public Map<String, Object> getOperatLogs(HttpServletRequest request,HttpServletResponse response){

		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			return null;
		}
		
		String strAction = request.getParameter("action");
		String strUid = request.getParameter("uid");
		String strSTime = request.getParameter("startDate");
		String strETime = request.getParameter("endDate");
		
		try {
			int action = StringUtils.isEmpty(strAction) ? 0: Integer.valueOf(strAction);
			int uid = StringUtils.isEmpty(strUid) ? 0 : Integer.valueOf(strUid);

			Long stime = DateUtil.dateToLong(strSTime, "yyyy-MM-dd", "other", 0);
			Long etime = DateUtil.dateToLong(strETime, "yyyy-MM-dd", "other", 0) + 24*3600;
			
			int page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
			int size = request.getParameter("rows") == null ? 20:Integer.valueOf(request.getParameter("rows"));
			if (page <= 0) {
				page = 1;
			}
			return adminService.getOperatLogs(action, uid, stime, etime, page, size);
		} catch (Exception e) {
			System.out.println("getOperatLogs-Exception:" + e.getMessage());
		}
		return null;
	}
	
	@RequestMapping(value = "/forSelect")
	@ResponseBody
	public List<Map<String, Object>> adminForSelect(HttpServletRequest request,HttpServletResponse response){
		
		String strIsvalid = request.getParameter("isvalid");
		int isvalid = StringUtils.isEmpty(strIsvalid) ? 9 : Integer.valueOf(strIsvalid);
		
		return adminService.getAdminForSelect(isvalid);
	}
}
