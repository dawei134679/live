package com.tinypig.admin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.portlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.qiniu.util.ExcelUitl;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.redis.service.RedisOperat;
import com.tinypig.admin.service.OperateService;
import com.tinypig.admin.util.Constant;
import com.tinypig.admin.util.DateUtil;
import com.tinypig.admin.util.NoticeAPIUtile;
import com.tinypig.admin.util.RedisContant;
import com.tinypig.newadmin.web.entity.SysVersion;
import com.tinypig.newadmin.web.service.SysVersionService;

@Controller
@RequestMapping("/operat")
public class OperateServlet extends webBaseServlet {
	
	
	@Resource
	private OperateService operateService;
	
	@Resource
	private SysVersionService sysVersionService;
	

	@RequestMapping(value="/remain")
	@ResponseBody
	public Map<String, Object> getRemain(HttpServletRequest request,HttpServletResponse response){

		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		
		if (adminUser ==  null) {
			return null;
		}
		
		int ios = request.getParameter("os") == null ? 0 : Integer.valueOf(request.getParameter("os"));
//		int ios = 0;
		String strChannel = request.getParameter("channel") == null ? "":request.getParameter("channel").trim();
//		String strChannel = "";
		
		String strStartDate = request.getParameter("startDate") == null ? "" : request.getParameter("startDate");
		String strEndDate = request.getParameter("endDate") == null ? "" : request.getParameter("endDate");
		int platform = request.getParameter("platform")==null ? 0 : Integer.parseInt(request.getParameter("platform"));
		int category = request.getParameter("category")==null ? 0 : Integer.parseInt(request.getParameter("category"));
		int type = Integer.parseInt(request.getParameter("type"));
		int ipage = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int irows = request.getParameter("rows") == null ? 20:Integer.valueOf(request.getParameter("rows"));
		if (ipage == 0) {
			ipage = 1;
		}
		
		Long lgStime = 0L;
		Long lgEtime = 0L;
		
		if (StringUtils.isEmpty(strStartDate) ) {
			lgStime = DateUtil.getDayBegin() - 7*24*3600;
		}else {
			lgStime = DateUtil.dateToLong(strStartDate, "yyyy-MM-dd", "", 0);
		}
		if (StringUtils.isEmpty(strEndDate)) {
			lgEtime = lgStime + 7*24*3600;
		}else {
			lgEtime = DateUtil.dateToLong(strEndDate, "yyyy-MM-dd", "", 0);
		}
		
		return operateService.getRemain(ios, strChannel, lgStime, lgEtime,ipage,irows,adminUser.getUid(),platform,category,type);
	}
	
	@RequestMapping(value="/listToExcel")
	public ModelAndView listToExcel(HttpServletRequest request,HttpServletResponse response){

		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		
		if (adminUser ==  null) {
			return null;
		}
		
		int ios = request.getParameter("os") == null ? 0 : Integer.valueOf(request.getParameter("os"));
//		int ios = 0;
		String strChannel = request.getParameter("channel") == null ? "":request.getParameter("channel").trim();
//		String strChannel = "";
		
		String strStartDate = request.getParameter("startDate") == null ? "" : request.getParameter("startDate");
		String strEndDate = request.getParameter("endDate") == null ? "" : request.getParameter("endDate");
		int platform = request.getParameter("platform")==null ? 0: Integer.parseInt(request.getParameter("platform"));
		int category = request.getParameter("category")==null ? 0 : Integer.parseInt(request.getParameter("category"));
		int type = Integer.parseInt(request.getParameter("type"));
		int ipage = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int irows = request.getParameter("rows") == null ? 20:Integer.valueOf(request.getParameter("rows"));
		if (ipage == 0) {
			ipage = 1;
		}
		
		Long lgStime = 0L;
		Long lgEtime = 0L;
		
		if (StringUtils.isEmpty(strStartDate) ) {
			lgStime = DateUtil.getDayBegin() - 7*24*3600;
		}else {
			lgStime = DateUtil.dateToLong(strStartDate, "yyyy-MM-dd", "", 0);
		}
		if (StringUtils.isEmpty(strEndDate)) {
			lgEtime = lgStime + 7*24*3600;
		}else {
			lgEtime = DateUtil.dateToLong(strEndDate, "yyyy-MM-dd", "", 0);
		}
		//获取查询结果集
		List list = operateService.getRemainList(ios, strChannel, lgStime, lgEtime,platform,category,type);
		//获取表格标题行
		List<String> headers = new ArrayList<String>();
		headers.add("日期");
		headers.add("渠道");
		
		headers.add("次日留存");
		headers.add("1日留存率(%)");
		headers.add("2日留存");
		headers.add("2日留存率(%)");
		headers.add("3日留存");
		headers.add("3日留存率(%)");
		headers.add("4日留存");
		headers.add("4日留存率(%)");
		headers.add("5日留存");
		headers.add("5日留存率(%)");
		headers.add("6日留存");
		headers.add("6日留存率(%)");
		headers.add("7日留存");
		headers.add("7日留存率(%)");
		headers.add("14日留存");
		headers.add("14日留存率(%)");
		headers.add("30日留存");
		headers.add("30日留存率(%)");
		switch(type){
			case 1:
				headers.add("新注册数");
				ExcelUitl.ExpExs("新增留存数据", "新增留存数据", headers, list, response,type);
				break;
			case 2:
				headers.add("登录账号数");
				ExcelUitl.ExpExs("登录留存数据", "登录留存数据", headers, list, response,type);
			case 3:
				headers.add("付费用户数");
				ExcelUitl.ExpExs("付费留存数据", "付费留存数据", headers, list, response,type);
		}
		return null;
	}
	
	@RequestMapping(value="/ltv")
	@ResponseBody
	public Map<String, Object> ltv(HttpServletRequest request,HttpServletResponse response){
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser ==  null) {
			return null;
		}
		int ios = request.getParameter("os") == null ? 0 : Integer.valueOf(request.getParameter("os"));
//		int ios = 0;
		String strChannel = request.getParameter("channel") == null ? "":request.getParameter("channel").trim();
//		String strChannel = "";
		
		String strStartDate = request.getParameter("startDate") == null ? "" : request.getParameter("startDate");
		String strEndDate = request.getParameter("endDate") == null ? "" : request.getParameter("endDate");
		int platform = request.getParameter("platform")==null ? 0 : Integer.parseInt(request.getParameter("platform"));
		int category = request.getParameter("category")==null ? 0 : Integer.parseInt(request.getParameter("category"));
		int type = Integer.parseInt(request.getParameter("type"));
		int ipage = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int irows = request.getParameter("rows") == null ? 20:Integer.valueOf(request.getParameter("rows"));
		if (ipage == 0) {
			ipage = 1;
		}
		
		Long lgStime = 0L;
		Long lgEtime = 0L;
		
		if (StringUtils.isEmpty(strStartDate) ) {
			lgStime = DateUtil.getDayBegin() - 7*24*3600;
		}else {
			lgStime = DateUtil.dateToLong(strStartDate, "yyyy-MM-dd", "", 0);
		}
		if (StringUtils.isEmpty(strEndDate)) {
			lgEtime = lgStime + 7*24*3600;
		}else {
			lgEtime = DateUtil.dateToLong(strEndDate, "yyyy-MM-dd", "", 0);
		}
		
		return operateService.ltv(ios, strChannel, lgStime, lgEtime,ipage,irows,adminUser.getUid(),platform,category,type);
	}

	@RequestMapping(value="/summary")
	@ResponseBody
	public Map<String, Object> getSummary(HttpServletRequest request,HttpServletResponse response){

		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		
		if (adminUser ==  null) {
			return null;
		}
		
		int ios = request.getParameter("os") == null ? 0 : Integer.valueOf(request.getParameter("os"));
		String channelCode = request.getParameter("channelCode")==null?"全部":request.getParameter("channelCode");
		String strStartDate = request.getParameter("startDate") == null ? "" : request.getParameter("startDate");
		String strEndDate = request.getParameter("endDate") == null ? "" : request.getParameter("endDate");
		
		int ipage = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int irows = request.getParameter("rows") == null ? 20:Integer.valueOf(request.getParameter("rows"));
		if (ipage == 0) {
			ipage = 1;
		}
		
		Long lgStime = 0L;
		Long lgEtime = 0L;
		
		if (StringUtils.isEmpty(strStartDate) ) {
			lgStime = DateUtil.getDayBegin() - 7*24*3600;
		}else {
			lgStime = DateUtil.dateToLong(strStartDate, "yyyy-MM-dd", "", 0);
		}
		if (StringUtils.isEmpty(strEndDate)) {
			lgEtime = lgStime + 7*24*3600;
		}else {
			lgEtime = DateUtil.dateToLong(strEndDate, "yyyy-MM-dd", "", 0);
		}
		
		return operateService.getSummary(ios, lgStime, lgEtime,ipage,irows,adminUser.getUid(),channelCode);
	}
	
	@RequestMapping(value="/wages")
	@ResponseBody
	public Map<String, Object> getWages(HttpServletRequest request,HttpServletResponse response){
	
		int unionid = StringUtils.isEmpty(request.getParameter("unionid")) ? 0 : Integer.valueOf(request.getParameter("unionid"));
		String strDateYM = request.getParameter("dateYM");
		int uid = StringUtils.isEmpty(request.getParameter("uid"))? 0 : Integer.valueOf(request.getParameter("uid").trim());
		
		int itype = StringUtils.isEmpty(request.getParameter("searchos")) ? 0 : Integer.valueOf(request.getParameter("searchos"));
		if (StringUtils.isEmpty(strDateYM)) {
			return null;
		}
		
		strDateYM= strDateYM.replace("-", "");
		
		int ipage = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int irows = request.getParameter("rows") == null ? 20:Integer.valueOf(request.getParameter("rows"));
		
		if (ipage == 0) {
			ipage = 1;
		}
		
		List<Map<String, Object>> wages = operateService.getWages(unionid, uid, strDateYM, itype, ipage, irows);
		int wagesSize = operateService.getWagesSize(unionid, uid, strDateYM, itype);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", wages);
		map.put("total", wagesSize);
		
		return map;
	}
	
	@RequestMapping(value="/sendgiftlist")
	@ResponseBody
	public Map<String, Object> getSendgiftlist(HttpServletRequest request,HttpServletResponse response){
		
		String strStartDate = request.getParameter("startDate") == null ? "" : request.getParameter("startDate");
		String strEndDate = request.getParameter("endDate") == null ? "" : request.getParameter("endDate");
		
		int ipage = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int irows = request.getParameter("rows") == null ? 20:Integer.valueOf(request.getParameter("rows"));
		if (ipage == 0) {
			ipage = 1;
		}
		
		int lgStime = 0;
		int lgEtime = 0;
		
		if (StringUtils.isEmpty(strStartDate) || StringUtils.isEmpty(strEndDate)) {
			return null;
		}
		
		lgStime = Integer.valueOf(strStartDate.replace("-", ""));
		lgEtime = Integer.valueOf(strEndDate.replace("-", ""));
		
		List<Map<String, Object>> sendGiftList = operateService.getSendGiftList(lgStime, lgEtime, ipage, irows);
		int sendGiftListSize = operateService.getSendGiftListSize(lgStime, lgEtime);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", sendGiftList);
		map.put("total", sendGiftListSize);
		
		return map;
	}
	
	@RequestMapping(value="mallStatic")
	@ResponseBody
	public Map<String, Object> getMallStatic(HttpServletRequest request,HttpServletResponse response){
		
		int gid = request.getParameter("searchos") == null ? 0 : Integer.valueOf(request.getParameter("searchos"));
		String strStartDate = request.getParameter("startDate") == null ? "" : request.getParameter("startDate");
		String strEndDate = request.getParameter("endDate") == null ? "" : request.getParameter("endDate");
		
		int ipage = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int irows = request.getParameter("rows") == null ? 20 : Integer.valueOf(request.getParameter("rows"));
		if (ipage == 0) {
			ipage = 1;
		}
		
		if (StringUtils.isEmpty(strStartDate) || StringUtils.isEmpty(strEndDate)) {
			return null;
		}
		List<Integer> guardList = new ArrayList<Integer>();
		List<Integer> vipList = new ArrayList<Integer>();
		List<Integer> propList = new ArrayList<Integer>();
		
		if (gid == 0) {
			guardList.add(45);
			guardList.add(46);
			
			vipList.add(43);
			vipList.add(44);
			
			propList.add(60);
			
		}else if (gid == 43 || gid == 44) {
			vipList.add(gid);
		}else if (gid == 45 || gid == 46)  {
			guardList.add(gid);
		}else if (gid == 60) {
			propList.add(gid);
		}

		Long stime = DateUtil.dateToLong(strStartDate, "yyyy-MM-dd", "other", 1);
		Long etime = DateUtil.dateToLong(strEndDate, "yyyy-MM-dd", "other", 1);
		List<Map<String, Object>> getMallStatic = operateService.getMallStatic(guardList, vipList, propList, stime, etime, ipage, irows);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", getMallStatic);
		map.put("total", getMallStatic != null ? getMallStatic.size():0);

		return map;
	}
	
	@RequestMapping(value="/mallSearch")
	@ResponseBody
	public Map<String, Object> getMallSearch(HttpServletRequest request,HttpServletResponse response){
		
		String strStartDate = request.getParameter("startDate") == null ? "" : request.getParameter("startDate");
		String strEndDate = request.getParameter("endDate") == null ? "" : request.getParameter("endDate");
		String condition = request.getParameter("condition");
		int type = request.getParameter("searchos") == null? 0 :Integer.valueOf(request.getParameter("searchos")); // =0全部 =1VIP =2守护 =3商城道具

		if (StringUtils.isEmpty(strStartDate) || StringUtils.isEmpty(strEndDate)) {
			return null;
		}
		int gid = 0;
		int uid = 0;
		if ("1".equals(condition)) {
			gid = StringUtils.isEmpty(request.getParameter("content").trim()) ? 0 : Integer.valueOf(request.getParameter("content").trim());
		}else if ("2".equals(condition)) {
			uid = StringUtils.isEmpty(request.getParameter("content").trim()) ? 0 : Integer.valueOf(request.getParameter("content").trim());
		}
		
		int ipage = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int irows = request.getParameter("rows") == null ? 20 : Integer.valueOf(request.getParameter("rows"));
		if (ipage == 0) {
			ipage = 1;
		}

		Long stime = DateUtil.dateToLong(strStartDate, "yyyy-MM-dd", "other", 1);
		Long etime = DateUtil.dateToLong(strEndDate, "yyyy-MM-dd", "other", 1);
		return operateService.getMallSearch(gid, uid, type, stime, etime, ipage, irows);
	}
	
	@RequestMapping(value="addNewVer")
	@ResponseBody
	public Map<String, Object> addNewVer(HttpServletRequest request,HttpServletResponse response){

		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		Map<String, Object> result = new HashMap<String, Object>();
		
		if (adminUser ==  null) {
			result.put("success", 400);
			result.put("errorMsg", "请重新登录");
			return result;
		}
		
		String sys = request.getParameter("sys") == null?"":request.getParameter("sys");
		String ver = request.getParameter("ver") == null?"":request.getParameter("ver");
		String force = request.getParameter("force") == null?"":request.getParameter("force");
		String desc = request.getParameter("desc") == null?"":request.getParameter("desc");
		String uploadUrl = request.getParameter("uploadUrl") == null?"":request.getParameter("uploadUrl");
		String utime = request.getParameter("utime") == null?"":request.getParameter("utime");
		
		if (StringUtils.isEmpty(sys) ||StringUtils.isEmpty(ver) || StringUtils.isEmpty(force) || StringUtils.isEmpty(desc) || StringUtils.isEmpty(uploadUrl) || StringUtils.isEmpty(utime) ) {
			result.put("success", 401);
			result.put("errorMsg", "参数不能为空");
			return result;
		}
		
		return null;
	}
	
	/**
	 * 获取直播间活动列表
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="getMiddleRight")
	@ResponseBody
	public Map<String, Object> getMiddleRigthAct(HttpServletRequest request,HttpServletResponse response){

		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		Map<String, Object> _map = new HashMap<String, Object>();
		
		if (adminUser ==  null) {
			return null;
		}
		
		String string = RedisOperat.getInstance().get(RedisContant.host, RedisContant.port6381, RedisContant.actMidRoom);
		
		if (StringUtils.isNotEmpty(string)) {
			_map = (Map<String, Object>)JSON.parse(string);
		}
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		list.add(_map);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total", 0);
		return map;
	}
	
	/**
	 * 直播间中靠右的活动
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="addMiddleRight")
	@ResponseBody
	public Map<String, Object> addMiddleRigthAct(HttpServletRequest request,HttpServletResponse response){

		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (adminUser ==  null) {
			map.put("success", 400);
			map.put("errorMsg", "请重新登录");
			return map;
		}
		
		String actName = request.getParameter("actName");
		String url = request.getParameter("url");
		String stime = request.getParameter("stime");
		String etime = request.getParameter("etime");
		
		if (StringUtils.isEmpty(actName) || StringUtils.isEmpty(url) || StringUtils.isEmpty(stime) || StringUtils.isEmpty(etime)) {
			return null;
		}
		
		map.put("url", url);
		map.put("actName", actName);
		map.put("stime", DateUtil.dateToLong(stime, "yyyy-MM-dd H:m:s", "other", 0));
		map.put("etime", DateUtil.dateToLong(etime, "yyyy-MM-dd H:m:s", "other", 0));
		map.put("expire", map.get("etime"));
		
		String jsonString = JSONObject.toJSONString(map);
		RedisOperat.getInstance().set(RedisContant.host, RedisContant.port6381, RedisContant.actMidRoom, jsonString);
		
		map = new HashMap<String, Object>();
		map.put("success", 200);
		
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getVersion")
	@ResponseBody
	public Map<String, Object> getVersion(HttpServletRequest request,HttpServletResponse response){

		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		Map<String, Object> _map = new HashMap<String, Object>();
		
		if (adminUser ==  null) {
			return null;
		}
		
		String string = RedisOperat.getInstance().hget(RedisContant.host, RedisContant.port6381, RedisContant.VersionAndroid,"android");
		
		if (StringUtils.isNotEmpty(string)) {
			_map = (Map<String, Object>)JSON.parse(string);
		}
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		list.add(_map);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total", 0);
		return map;
	}
	
	@RequestMapping(value="/addVersion")
	@ResponseBody
	public Map<String, Object> addVersion(HttpServletRequest request,HttpServletResponse response){
		
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (adminUser ==  null) {
			map.put("success", 400);
			map.put("errorMsg", "请重新登录");
			return map;
		}
		
		String sys = request.getParameter("sys");
		String ver = request.getParameter("ver");
		String force = request.getParameter("force");
		String desc = request.getParameter("desc");
		String uploadUrl = request.getParameter("uploadUrl");
		String utime = request.getParameter("utime");
		
		if (StringUtils.isEmpty(sys) || StringUtils.isEmpty(ver) || StringUtils.isEmpty(force) || StringUtils.isEmpty(desc) || StringUtils.isEmpty(uploadUrl) || StringUtils.isEmpty(utime)) {

			map.put("success", 401);
			map.put("errorMsg", "参数不能为空");
			return map;
		}
		String sysType = "";
		if ("0".equals(sys)) {
			sysType = "android";
		}else if ("1".equals(sys)) {
			sysType = "ios";
		}
		
		if (StringUtils.isEmpty(sysType)) {

			map.put("success", 401);
			map.put("errorMsg", "请选择手机类型");
			return map;
		}
		
		map.put("sys", sysType);
		map.put("ver", ver);
		map.put("force", "1".equals(force)?"true":"false");
		map.put("desc", desc);
		map.put("uploadUrl", uploadUrl);
		map.put("updateTime", DateUtil.dateToLong(utime, "yyyy-MM-dd H:m:s", "other", 0));
		
		String jsonString = JSONObject.toJSONString(map);
		RedisOperat.getInstance().hset(RedisContant.host, RedisContant.port6381, RedisContant.VersionAndroid,sysType, jsonString,0);
		
		// 通知API
		NoticeAPIUtile.noticeApi("updateVersion", "adminid", "admin");
		
		map = new HashMap<String, Object>();
		map.put("success", 200);
		
		return map;
	}
	
	/**
	 * 统计砸蛋信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/egglist")
	@ResponseBody
	public Map<String, Object> getEggList(HttpServletRequest request,HttpServletResponse response){
		
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser ==  null) {
			return null;
		}
		
		// =1 铜锤[10金币]  =2金锤[25金币] =3紫锤[50金币]
		String hammer = request.getParameter("condition");
		String strStartDate = request.getParameter("startDate");
		String strEndDate = request.getParameter("endDate");

		if (StringUtils.isEmpty(hammer) || StringUtils.isEmpty(strStartDate) || StringUtils.isEmpty(strEndDate)) {
			return null;
		}

		int ipage = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int irows = request.getParameter("rows") == null ? 20 : Integer.valueOf(request.getParameter("rows"));
		if (ipage == 0) {
			ipage = 1;
		}

		int stime = Integer.valueOf(strStartDate.replace("-", ""));
		int etime = Integer.valueOf(strEndDate.replace("-", ""));
		return operateService.getEggList(Integer.valueOf(hammer), stime, etime, ipage, irows);
	}
	
	@RequestMapping(value = "/egglist/userlist")
	@ResponseBody
	public Map<String, Object> getUserListOfEgg(HttpServletRequest request,HttpServletResponse response){
		
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser ==  null) {
			return null;
		}
		
		try {
			int type = Integer.valueOf(request.getParameter("type"));
			// =1 铜锤[10金币]  =2金锤[25金币] =3紫锤[50金币]
			int hammer = Integer.valueOf(request.getParameter("hammer"));
			type = type + (hammer -1)*8;
			String datetimes = request.getParameter("datetimes");
			
			
			Long starttime = DateUtil.dateToLong(datetimes, "yyyyMMdd", "other", 0);
			Long endtime = starttime + 24*3600;
			
			return operateService.getUserListOfEgg(type, starttime, endtime);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取礼物促销活动
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/promotlist")
	@ResponseBody
	public Map<String, Object> getPromotList(HttpServletRequest request,HttpServletResponse response){
		
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser ==  null) {
			return null;
		}
		return operateService.getPromotList(0);
	}
	
	/**
	 * 获取商城礼物列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/getMallGift")
	@ResponseBody
	public List<Map<String, Object>> getMallGift(HttpServletRequest request,HttpServletResponse response){
		
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser ==  null) {
			return null;
		}
		return operateService.getMallGift();
	}
	
	@RequestMapping(value = "/promotAdd")
	@ResponseBody
	public Map<String, Object> promotAdd(HttpServletRequest request,HttpServletResponse response){

		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (adminUser ==  null) {
			map.put("errorMsg", "请重新登录");
			return map;
		}
		
		int gid = request.getParameter("gid")==null?0:Integer.valueOf(request.getParameter("gid"));
		String promotionName = request.getParameter("promotionName");
		int discount = request.getParameter("discount") == null?0:Integer.valueOf(request.getParameter("discount"));
		int isvalid = request.getParameter("isvalid") == null?0:Integer.valueOf(request.getParameter("isvalid"));
		
		String starttime = request.getParameter("starttime");
		String endtime = request.getParameter("endtime");
		
		Long istime = DateUtil.dateToLong(starttime, "yyyy-MM-dd H:m:s", "other", 0);
		Long ietime = DateUtil.dateToLong(endtime, "yyyy-MM-dd H:m:s", "other", 0);
		
		if (gid <= 0) {
			map.put("errorMsg", "请选择礼物");
		}else if (discount < 80 || discount >= 100) {
			map.put("errorMsg", "折扣设置错误：" + discount);
		}else if (istime >= ietime || ietime <= System.currentTimeMillis()/1000) {
			map.put("errorMsg", "活动时间设置错误：" + discount);
		}else {
			int promotAdd = operateService.promotAdd(gid, promotionName, discount, isvalid, istime, ietime, adminUser.getUid());
			if (promotAdd > 0) {
				// 通知API
				NoticeAPIUtile.noticeApi("updGiftPromotion", "adminid", "admin");
				map.put("errorCode", 200);
			}else {
				map.put("errorMsg", "添加促销失败");
			}
		}
		return map;
	}
	
	@RequestMapping(value = "/promotEdit")
	@ResponseBody
	public Map<String, Object> promotEdit(HttpServletRequest request,HttpServletResponse response){

		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (adminUser ==  null) {
			map.put("errorMsg", "请重新登录");
			return map;
		}
		
		int id = Integer.valueOf(request.getParameter("id"));
		
		int gid = request.getParameter("gid")==null?0:Integer.valueOf(request.getParameter("gid"));
		
		String promotionName = request.getParameter("promotionName");
		int discount = request.getParameter("discount") == null?0:Integer.valueOf(request.getParameter("discount"));
		int isvalid = request.getParameter("isvalid") == null?0:Integer.valueOf(request.getParameter("isvalid"));
		
		String starttime = request.getParameter("starttime");
		String endtime = request.getParameter("endtime");
		
		Long istime = DateUtil.dateToLong(starttime, "yyyy-MM-dd H:m:s", "other", 0);
		Long ietime = DateUtil.dateToLong(endtime, "yyyy-MM-dd H:m:s", "other", 0);
		if (gid <= 0) {
			map.put("errorMsg", "请选择礼物");
		}else if (discount < 80 || discount >= 100) {
			map.put("errorMsg", "折扣设置错误：" + discount);
		}else if (istime >= ietime || ietime <= System.currentTimeMillis()/1000) {
			map.put("errorMsg", "活动时间设置错误：" + discount);
		}else if (id <= 0) {
			map.put("errorMsg", "请刷新后 重新操作：" + discount);
		}else {

			int promotAdd = operateService.promotEdit(gid, promotionName, discount, isvalid, istime, ietime, id,adminUser.getUid());
			if (promotAdd > 0) {
				// 通知API
				NoticeAPIUtile.noticeApi("updGiftPromotion", "adminid", "admin");
				map.put("errorCode", 200);
			}else {
				map.put("errorMsg", "添加促销失败");
			}
		}
		return map;
	}
	
	@RequestMapping(value="/withdraw")
	@ResponseBody
	public Map<String, Object> getWithdraw(HttpServletRequest request,HttpServletResponse response){

		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser ==  null) {
			return null;
		}
		
		String isSecc = request.getParameter("isSecc");
		String uid = request.getParameter("uid");
		String starttime = request.getParameter("startDate");
		String endtime = request.getParameter("endDate");
		
		if (StringUtils.isEmpty(isSecc) || StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime)) {
			return null;
		}
		if (StringUtils.isEmpty(uid)) {
			uid = "0";
		}
		int pages = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int size = request.getParameter("rows") == null ? 20 : Integer.valueOf(request.getParameter("rows"));
		if (pages == 0) {
			pages = 1;
		}
		
		try {
			Long istime = DateUtil.dateToLong(starttime, "yyyy-MM-dd", "other", 0);
			Long ietime = DateUtil.dateToLong(endtime, "yyyy-MM-dd", "other", 0);
			
			return operateService.getWithdraw(Integer.valueOf(isSecc), istime, ietime, Integer.valueOf(uid), pages, size);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@RequestMapping(value = "/verifyWithdraw")
	@ResponseBody
	public Map<String, Object> getVerifyWithdraw(HttpServletRequest request,HttpServletResponse response){
		
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (adminUser ==  null) {
			map.put("success", 201);
			map.put("errorMsg", "请先登录");
			return map;
		}
		
		try {
			String id = request.getParameter("id");
			String billno = request.getParameter("billno");
			String type = request.getParameter("result"); // =1通过 =2不通过
			
			map = operateService.verifyWithdraw(Integer.valueOf(id), billno, adminUser.getUid(), type);
			
			if (!map.containsKey("success")) {
				map.put("success", 402);
			}
		} catch (Exception e) {
			map.put("success", 400);
			map.put("errorMsg", "系统异常");
			e.printStackTrace();
		}
		
		return map;
	}
	
	@RequestMapping(value = "/reportalbum")
	@ResponseBody
	public Map<String, Object> getReportalbum(HttpServletRequest request,HttpServletResponse response){
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser ==  null) {
			return null;
		}
		
		try {
			String status = request.getParameter("status");
			String startdate = request.getParameter("startDate");
			String enddate = request.getParameter("endDate");
			int iStatus = 0;
			if (StringUtils.isEmpty(status)) {
				iStatus = 0;
			}else {
				iStatus = Integer.valueOf(status);
			}
			
			int pages = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
			int size = request.getParameter("rows") == null ? 20 : Integer.valueOf(request.getParameter("rows"));
			if (pages == 0) {
				pages = 1;
			}
			Long istime = null;
			Long ietime = null;
			if(StringUtils.isNotEmpty(startdate)) {
				istime = DateUtil.dateToLong(startdate, "yyyy-MM-dd", "other", 0);
			}
			if(StringUtils.isNotEmpty(enddate)) {
				DateUtil.dateToLong(enddate, "yyyy-MM-dd", "other", 0);
			}
			return operateService.getReportalbum(iStatus, istime, ietime, pages, size);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/verifyAlbum")
	@ResponseBody
	public Map<String, Object> verifyAlbum(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, Object> map = new HashMap<String, Object>();
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser ==  null) {
			map.put("errorMsg", "请先登录");
			return map;
		}
		
		try {
			int id = Integer.valueOf(request.getParameter("id"));
			int status =  Integer.valueOf(request.getParameter("status"));
			int uid = Integer.valueOf(request.getParameter("uid"));
			int pid = Integer.valueOf(request.getParameter("pid"));
			if (status != 1 && status != 2) {
				map.put("errorMsg", "请选择审核结果");
			}else {
				int verifyAlbum = operateService.verifyAlbum(id, status, adminUser.getUid());
				if (verifyAlbum > 0) {
					map.put("errorCode", 200);
					if(status == 2) {//删除
						HttpResponse<JsonNode> jsonNode = Unirest.post(Constant.business_server_url + "/admin/delPhoto")
								.queryString("adminid", "admin").queryString("uid", uid).queryString("pid", pid)
								.asJson();
					}
				}else {
					map.put("errorMsg", "审核失败");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errorMsg", "系统异常");
		}
		
		return map;
	}
	
	@RequestMapping(value = "/withdrawlist")
	@ResponseBody
	public Map<String, Object> getWithdrawlist(HttpServletRequest request,HttpServletResponse response){
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser ==  null) {
			return null;
		}
		
		try {
			String status = request.getParameter("status");
			String startdate = request.getParameter("startDate");
			String enddate = request.getParameter("endDate");
			
			if (StringUtils.isEmpty(startdate) || StringUtils.isEmpty(enddate)) {
				return null;
			}
			int iStatus = 0;
			if (StringUtils.isEmpty(status)) {
				iStatus = -1;
			}else {
				iStatus = Integer.valueOf(status);
			}
			
			int pages = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
			int size = request.getParameter("rows") == null ? 20 : Integer.valueOf(request.getParameter("rows"));
			if (pages == 0) {
				pages = 1;
			}
			
			Long istime = DateUtil.dateToLong(startdate, "yyyy-MM-dd", "other", 0);
			Long ietime = DateUtil.dateToLong(enddate, "yyyy-MM-dd", "other", 0);
			
			return operateService.getReportalbum(iStatus, istime, ietime, pages, size);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取动态广场 推荐主播列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getRecommendList")
	@ResponseBody
	public Map<String, Object> getRecommendList(HttpServletRequest request,HttpServletResponse response){
		
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser ==  null) {
			return null;
		}
		
		int pages = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int size = request.getParameter("rows") == null ? 20 : Integer.valueOf(request.getParameter("rows"));
		if (pages == 0) {
			pages = 1;
		}
		
		return operateService.getRecommend(pages, size);
	}
	
	/**
	 * 动态广场，新增|修改 推荐主播
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addRecommend")
	@ResponseBody
	public Map<String, Object> addRecommend(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, Object> map = new HashMap<String, Object>();
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser ==  null) {
			map.put("errorMsg", "请先登录");
			return map;
		}
		
		String strUid = request.getParameter("uid");
		String strSort = request.getParameter("sort");
		
		int addRecommend = operateService.addRecommend(Integer.valueOf(strUid), Integer.valueOf(strSort), adminUser.getUsername(), adminUser.getUid());
		
		if (addRecommend > 0) {
			map.put("success", 200);
		}else {
			map.put("errorMsg", "新增失败");
		}
		return map;
	}

	
	/**
	 * 动态广场，新增|修改 推荐主播
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/delRecommend")
	@ResponseBody
	public Map<String, Object> delRecommend(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, Object> map = new HashMap<String, Object>();
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser ==  null) {
			map.put("errorMsg", "请先登录");
			return map;
		}
		
		String id = request.getParameter("id");
		
		int addRecommend = operateService.delRecommend(Integer.valueOf(id), adminUser.getUid());
		
		if (addRecommend > 0) {
			map.put("success", 200);
		}else {
			map.put("errorMsg", "删除失败");
		}
		return map;
	}
	
	@RequestMapping(value = "/getChannel")
	@ResponseBody
	public Map<String, Object> getChannelList(HttpServletRequest request,HttpServletResponse response){
		
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser ==  null) {
			return null;
		}
		//获取渠道名称
		String channelName = request.getParameter("searchChannelName");
		//获取所选平台
		int platform = Integer.parseInt(request.getParameter("searchPlatform"));
		//获取所选状态
		int status = Integer.parseInt(request.getParameter("searchStatus"));
		int pages = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int size = request.getParameter("rows") == null ? 20 : Integer.valueOf(request.getParameter("rows"));
		if (pages == 0) {
			pages = 1;
		}
		
		return  operateService.getChannelList(status,platform,channelName, pages, size);
	}
	
	@RequestMapping(value = "/addChannel")
	@ResponseBody
	public Map<String, Object> addChannel(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, Object> mapResult = new HashMap<String, Object>();
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser ==  null) {
			mapResult.put("errMsg", "请刷新登录");
			return mapResult;
		}
		
		String channelCode = request.getParameter("channelCode");
		String channelName = request.getParameter("channelName");
		String strIsvalid = request.getParameter("isvalid");
		int loginport = Integer.parseInt(request.getParameter("loginport"));
		int platform = Integer.parseInt(request.getParameter("platform"));
		
		if (StringUtils.isEmpty(channelCode) || StringUtils.isEmpty(channelName) || StringUtils.isEmpty(strIsvalid)) {
			mapResult.put("errMsg", "参数不正确");
		}
		
		int addChannel = operateService.addChannel(channelCode, channelName, Integer.valueOf(strIsvalid), adminUser.getUid(),loginport,platform);
		if (addChannel != 1) {
			mapResult.put("errMsg", "添加失败");
		}
		return mapResult;
	}
	
	@RequestMapping(value = "/editChannel")
	@ResponseBody
	public Map<String, Object> editChannel(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, Object> mapResult = new HashMap<String, Object>();
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser ==  null) {
			mapResult.put("errMsg", "请刷新登录");
			return mapResult;
		}
		
		String id = request.getParameter("id");
		String strIsvalid = request.getParameter("isvalid");
		String channelCode = request.getParameter("channelCode");
		String channelName = request.getParameter("channelName");
		int platform = Integer.parseInt(request.getParameter("platform"));
		int loginport =Integer.parseInt(request.getParameter("loginport"));
		
		
		if (StringUtils.isEmpty(id)|| StringUtils.isEmpty(strIsvalid)) {
			mapResult.put("errMsg", "参数不正确");
		}
		
		int editChannel = operateService.editChannel(id,Integer.valueOf(strIsvalid),channelCode,channelName,platform,loginport);
		if (editChannel != 1) {
			mapResult.put("errMsg", "修改失败");
		}
		return mapResult;
	}
	
	@RequestMapping(value = "/getChannelForSelect")
	@ResponseBody
	public List<Map<String, Object>> getChannelForSelect(HttpServletRequest request,HttpServletResponse response){
		
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser ==  null) {
			return null;
		}
		return operateService.getChannelForSelect();
	}
	
	@RequestMapping(value="/saveVersion")
	@ResponseBody
	public Map<String, Object> saveVersion(HttpServletRequest request,HttpServletResponse response,SysVersion sysVersion){
		
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		Map<String, Object> map = new HashMap<String, Object>();
		sysVersion.setUpdateUserId((long) adminUser.getUid());
		String time = String.valueOf(DateUtil.dateToLong(request.getParameter("utime"), "yyyy-MM-dd H:m:s", "other", 0));
		sysVersion.setUpdateTime(Integer.valueOf(time));
		int num = sysVersionService.insertSelective(sysVersion);
		if (num != 1) {
			map.put("success", 401);
			map.put("errorMsg", "添加版本失败");
			return map;
		}
		String sysType = "";
		String versionKey = "";
		if (0==sysVersion.getSys()) {
			versionKey = RedisContant.VersionAndroid;
			sysType = "android";
		}else {
			versionKey = RedisContant.VersionIos;
			sysType = "ios";
		}
		map.put("sys", sysType);
		map.put("ver", sysVersion.getVer());
		map.put("force", 1==sysVersion.getIsforce()?"true":"false");
		map.put("desc", sysVersion.getDescribtion());
		map.put("uploadUrl", sysVersion.getUploadUrl());
		map.put("updateTime", sysVersion.getUpdateTime());
		
		String jsonString = JSONObject.toJSONString(map);
		RedisOperat.getInstance().set(RedisContant.host, RedisContant.port6381, versionKey, jsonString);
		// 通知API
		NoticeAPIUtile.noticeApi("updateVersion", "adminid", "admin");
		map = new HashMap<String, Object>();
		map.put("success", 200);
		return map;
	}
	
	@RequestMapping(value="/getVersionList")
	@ResponseBody
	public Map<String, Object> getVersionList(HttpServletRequest request,HttpServletResponse response){
		return sysVersionService.getVersionList();
	}
}
