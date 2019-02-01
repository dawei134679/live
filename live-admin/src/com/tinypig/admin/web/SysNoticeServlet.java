package com.tinypig.admin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.redis.service.RedisOperat;
import com.tinypig.admin.service.SysNoticeService;
import com.tinypig.admin.util.Constant;
import com.tinypig.admin.util.DateUtil;
import com.tinypig.admin.util.RedisContant;

@Controller
@Scope("prototype")
@RequestMapping("/sysnotice")
public class SysNoticeServlet extends webBaseServlet {
	
	@Resource
	private SysNoticeService sysNoticeService;

	@RequestMapping(value="/getSysNoticelist")
	@ResponseBody
	public Map<String, Object> getSysNoticelist(HttpServletRequest request,HttpServletResponse response){
		
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		Map<String, Object> result = new HashMap<String, Object>();
		if (adminUser == null) {
			return null;
		}
		
		List<Map<String, Object>> remain = sysNoticeService.sysNoticeList();
		int remainSize = 30;
		
		result.put("rows", remain);
		result.put("total", remainSize);
		
		return result;
	}
	
	@RequestMapping(value="/addSysNoctice")
	@ResponseBody
	public Map<String, Object> addSysNoctice(HttpServletRequest request,HttpServletResponse response){
		
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		Map<String, Object> result = new HashMap<String, Object>();
		if (adminUser == null) {
			result.put("success", 400);
			return result;
		}
		
		String content = request.getParameter("content") == null?"":request.getParameter("content");
		String url = request.getParameter("url") == null?"":request.getParameter("url");
		
		Long sendtime = System.currentTimeMillis()/1000;
		
		int addSysNoctice = sysNoticeService.addSysNoctice(content, url, sendtime, sendtime, adminUser.getUid());
		if (addSysNoctice < 0) {
			result.put("success", 401);
			result.put("errorMsg", "更新失败");
		}else {
			
			Long stime = System.currentTimeMillis()/1000 - 7*24*3600;
			List<Map<String, Object>> sysMsgList = sysNoticeService.getValidSysNotice(stime);

			String msgbody = JSONObject.toJSONString(sysMsgList);
			
			RedisOperat.getInstance().set(RedisContant.host, RedisContant.port6379, RedisContant.KeySysMsg, msgbody,7*24*3600);
			
			try {
				HttpResponse<String> asString = Unirest.get(Constant.business_server_url+"/admin/sysnotice?sysMsgPush=allUsers").asString();
				
				System.out.println("asString:"+asString.toString());
			} catch (UnirestException e) {
				e.printStackTrace();
			}
			result.put("success", 200);
		}
		
		return result;
	}
	
	@RequestMapping(value = "/getRoomChat")
	@ResponseBody
	public Map<String, Object> getRoomChat(HttpServletRequest request,HttpServletResponse response){

		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			return null;
		}

		return sysNoticeService.getRoomChat();
	}
	
	@RequestMapping(value = "/addRoomChat")
	@ResponseBody
	public Map<String, Object> addRoomChat(HttpServletRequest request,HttpServletResponse response){

		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		Map<String, Object> result = new HashMap<String, Object>();
		if (adminUser == null) {
			result.put("success", 400);
			return result;
		}
		
		String content = request.getParameter("content") == null ? "" : request.getParameter("content").trim();
		Long starttime = request.getParameter("starttime") == null ? 0 : DateUtil.dateToLong(request.getParameter("starttime"), "yyyy-MM-dd H:m:s", "other", 0);
		Long endtime = request.getParameter("endtime") == null ? 0 : DateUtil.dateToLong(request.getParameter("endtime"), "yyyy-MM-dd H:m:s", "other", 0);
		int isvalid = request.getParameter("isvalid") == null ? 0 : Integer.valueOf(request.getParameter("isvalid"));
		if (StringUtils.isEmpty(content) || starttime <= 0 || endtime <= 0 || starttime >= endtime) {
			result.put("success", 400);
			result.put("errorMsg", "参数不正确");
		}else {
			int addRoomChat = sysNoticeService.addRoomChat(content, starttime, endtime, isvalid, System.currentTimeMillis()/1000, adminUser.getUid());
			if (addRoomChat >= 1) {
				result.put("success", 200);
				RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6380, RedisContant.roomChat);
			}else {
				result.put("success", 401);
				result.put("errorMsg", "添加失败");
			}
		}
		return result;
	}
	
	@RequestMapping(value = "/editRoomChat")
	@ResponseBody
	public Map<String, Object> editRoomChat(HttpServletRequest request,HttpServletResponse response){

		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		Map<String, Object> result = new HashMap<String, Object>();
		if (adminUser == null) {
			result.put("success", 400);
			return result;
		}
		int id = request.getParameter("id") == null ? 0 : Integer.valueOf(request.getParameter("id"));
		String content = request.getParameter("content") == null ? "" : request.getParameter("content").trim();
		Long starttime = request.getParameter("starttime") == null ? 0 : DateUtil.dateToLong(request.getParameter("starttime"), "yyyy-MM-dd H:m:s", "other", 0);
		Long endtime = request.getParameter("endtime") == null ? 0 : DateUtil.dateToLong(request.getParameter("endtime"), "yyyy-MM-dd H:m:s", "other", 0);
		int isvalid = request.getParameter("isvalid") == null ? 0 : Integer.valueOf(request.getParameter("isvalid"));
		
		if (StringUtils.isEmpty(content) || starttime <= 0 || endtime <= 0 || starttime >= endtime || id <= 0) {
			result.put("success", 400);
			result.put("errorMsg", "参数不正确");
		}else {
			int editRoomChat = sysNoticeService.editRoomChat(content, starttime, endtime, isvalid, id, adminUser.getUid());
			if (editRoomChat == 1) {
				result.put("success", 200);
				RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6380, RedisContant.roomChat);
			}else {
				result.put("success", 401);
				result.put("errorMsg", "修改失败");
			}
		}
		return result;
	}
	
	@RequestMapping(value = "/removeRoomChat")
	@ResponseBody
	public Map<String, Object> removeRoomChat(HttpServletRequest request,HttpServletResponse response){

		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		Map<String, Object> result = new HashMap<String, Object>();
		if (adminUser == null) {
			result.put("success", 400);
			return result;
		}
		int id = request.getParameter("id") == null ? 0 : Integer.valueOf(request.getParameter("id"));
		
		if (id <= 0) {
			result.put("success", 400);
			result.put("errorMsg", "参数不正确");
		}else {
			int editRoomChat = sysNoticeService.removeRoomChat(id, adminUser.getUid());
			if (editRoomChat == 1) {
				result.put("success", 200);
				RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6380, RedisContant.roomChat);
			}else {
				result.put("success", 401);
				result.put("errorMsg", "作废失败");
			}
		}
		return result;
	}
}
