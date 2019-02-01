package com.tinypig.admin.web;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.qiniu.common.BannerConfig;
import com.tinypig.admin.dao.UnionDao;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.model.UnionModel;
import com.tinypig.admin.service.AnchorMainServiceImpl;
import com.tinypig.admin.util.Constant;
import com.tinypig.admin.util.DateUtil;
import com.tinypig.admin.util.ExcelExportData;
import com.tinypig.admin.util.ExcelUtil;
import com.tinypig.admin.util.QiniuUpUtils;
import com.tinypig.newadmin.common.ResponseUtils;
import com.tinypig.newadmin.web.entity.RealNameParamDto;

@Controller
@RequestMapping("/anchor")
public class AnchorController {
	
	private static Logger log = Logger.getLogger(AnchorController.class);

	
	@Resource
	private AnchorMainServiceImpl anchorService;

	@RequestMapping(value = "/getCover")
	@ResponseBody
	public Map<String, Object> getAnchorCover(HttpServletRequest request,HttpServletResponse response){
		
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			return null;
		}
		
		String strUnionid = request.getParameter("unionid");
		String strUid = request.getParameter("anchoruid");
		
		int unionid = StringUtils.isEmpty(strUnionid)? 0 : Integer.valueOf(strUnionid);
		int anchoruid = StringUtils.isEmpty(strUid)?0:Integer.valueOf(strUid);
		
		int page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int size = request.getParameter("rows") == null ? 20:Integer.valueOf(request.getParameter("rows"));
		if (page <= 0) {
			page = 1;
		}
		
		return anchorService.getAnchorCover(unionid, anchoruid, page, size);
	}
	/**
	 * 获取所有主播上传封面记录(带公会)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getUserCoverList")
	@ResponseBody
	public Map<String, Object> getUserCoverList(HttpServletRequest request,HttpServletResponse response){
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			return null;
		}
		String strUnionid = request.getParameter("unionid");		//公会编号
		String strUid = request.getParameter("anchoruid");			//主播编号
		int status = Integer.parseInt(request.getParameter("status"));			//审核状态
		
		int unionid = StringUtils.isEmpty(strUnionid)? 0 : Integer.valueOf(strUnionid);
		int anchoruid = StringUtils.isEmpty(strUid)?0:Integer.valueOf(strUid);
		
		int page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int size = request.getParameter("rows") == null ? 20:Integer.valueOf(request.getParameter("rows"));
		if (page <= 0) {
			page = 1;
		}
		
		return anchorService.getUserCoverList(unionid, anchoruid,status, page, size);
	}
	
	/**
	 * 获取所有主播上传封面记录（非公会）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getNoUnionUserCoverList")
	@ResponseBody
	public Map<String, Object> getNoUnionUserCoverList(HttpServletRequest request,HttpServletResponse response){
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			return null;
		}
		String strUid = request.getParameter("anchoruid");			//主播编号
		int status = Integer.parseInt(request.getParameter("status"));			//审核状态
		int anchoruid = StringUtils.isEmpty(strUid)?0:Integer.valueOf(strUid);
		
		int page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int size = request.getParameter("rows") == null ? 20:Integer.valueOf(request.getParameter("rows"));
		if (page <= 0) {
			page = 1;
		}
		
		return anchorService.getUserCoverList(anchoruid,status, page, size);
	}
	
	@RequestMapping(value = "/editCover")
	@ResponseBody
	public Map<String, Object> editAnchorCover(@RequestParam(value = "imgApp", required = false) MultipartFile imgApp,
			@RequestParam(value = "imgPc1", required = false) MultipartFile imgPc1,
			@RequestParam(value = "imgPc2", required = false) MultipartFile imgPc2,HttpServletRequest request){
		
		Map<String, Object> mapResult = new HashMap<String, Object>();
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			mapResult.put("errMsg", "请刷新登录");
			return mapResult;
		}
		
		int uid = request.getParameter("uid") == null ? 0 : Integer.valueOf(request.getParameter("uid"));
		
		if (uid <= 0) {
			mapResult.put("errMsg", "请重新选择再修改");
			return mapResult;
		}
		
		String uploadImg = "";
		Boolean bl = false;
		
		String livimage = "";
		if (imgApp != null && !imgApp.isEmpty()) {
			uploadImg =  request.getParameter("livimage");
			if (StringUtils.isNotEmpty(uploadImg)) {
				String urlapp[] = uploadImg.split("/");
				QiniuUpUtils.delete(urlapp[urlapp.length-1], BannerConfig.Cover);
			}

			livimage = QiniuUpUtils.uploadfile(imgApp,BannerConfig.Cover,BannerConfig.coverDomail);
			bl = true;
		}
		
		String pcimg1 = "";
		if (imgPc1 != null && !imgPc1.isEmpty()) {
			uploadImg =  request.getParameter("pcimg1");
			if (StringUtils.isNotEmpty(uploadImg)) {
				String urlapp[] = uploadImg.split("/");
				QiniuUpUtils.delete(urlapp[urlapp.length-1], BannerConfig.Cover);
			}

			pcimg1 = QiniuUpUtils.uploadfile(imgPc1,BannerConfig.Cover,BannerConfig.coverDomail);
			bl = true;
		}
		String pcimg2 = "";
		if (imgPc2 != null && !imgPc2.isEmpty()) {
			uploadImg =  request.getParameter("pcimg2");
			if (StringUtils.isNotEmpty(uploadImg)) {
				String urlapp[] = uploadImg.split("/");
				QiniuUpUtils.delete(urlapp[urlapp.length-1], BannerConfig.Cover);
			}

			pcimg2 = QiniuUpUtils.uploadfile(imgPc2,BannerConfig.Cover,BannerConfig.coverDomail);
			bl = true;
		}
		
		if (bl) {
			// 有图片上传
			int editAnchoCover = anchorService.editAnchoCover(uid, livimage, pcimg1, pcimg2, adminUser.getUid());
			if (editAnchoCover <= 0) {
				mapResult.put("errMsg", "上传失败");
			}
		}else {
			// 无图片上传
			mapResult.put("errMsg", "请上传图片");
		}
		
		return mapResult;
	}
	@RequestMapping(value = "/allow")
	@ResponseBody
	public int allow(@RequestParam(value = "id", required = false)int id,
						@RequestParam(value = "uid", required = false)int uid,
						@RequestParam(value = "picCover", required = false)String picCover,
						@RequestParam(value = "picCover1", required = false)String picCover1,
						@RequestParam(value = "picCover2", required = false)String picCover2){
		//审核通过，更新user_base_info表中记录
		return anchorService.updCover(id, uid,picCover, picCover1, picCover2);
		
	}
	
	@RequestMapping(value = "/reject")
	@ResponseBody
	public int reject(@RequestParam(value = "id", required = false)int id,
						@RequestParam(value = "picCover", required = false)String picCover,
						@RequestParam(value = "picCover1", required = false)String picCover1,
						@RequestParam(value = "picCover2", required = false)String picCover2,
						@RequestParam(value = "cause", required = false)String cause){
		//审核驳回
		return anchorService.updCover(id,cause);
	}
	@RequestMapping(value = "/getGrant")
	@ResponseBody
	public Map<String, Object> getGrant(HttpServletRequest request,HttpServletResponse response){
		
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			return null;
		}
		String startDate = request.getParameter("startDate");
		
		String endDate = request.getParameter("endDate");
		
		String gStatus = request.getParameter("gStatus");
		
		String gsid = request.getParameter("gsid");
		
		String strUid = request.getParameter("uid");
		String strOperateUid = request.getParameter("operate_uid");
		int uid = StringUtils.isEmpty(strUid) ? 0 : Integer.valueOf(strUid);
		int operateUid = StringUtils.isEmpty(strOperateUid) ? 0 : Integer.valueOf(strOperateUid);
		
		int page = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int size = request.getParameter("rows") == null ? 20:Integer.valueOf(request.getParameter("rows"));
		if (page <= 0) {
			page = 1;
		}
		
		return anchorService.getGrant(uid,operateUid, page, size,startDate,endDate,gStatus,gsid);
	}
	
	@RequestMapping(value = "/expExcel")
	public void expExcel(HttpServletRequest request,HttpServletResponse response, RealNameParamDto param) {
		String startDate = request.getParameter("startdate");
		
		String endDate = request.getParameter("enddate");
		
		String gStatus = request.getParameter("gStatus");
		
		String gsid = request.getParameter("gsid");
		
		String strUid = request.getParameter("searchUid");
		String strOperateUid = request.getParameter("operate_uid");
		int uid = StringUtils.isEmpty(strUid) ? 0 : Integer.valueOf(strUid);
		int operateUid = StringUtils.isEmpty(strOperateUid) ? 0 : Integer.valueOf(strOperateUid);
		List<Map<String, Object>> list = anchorService.getAllGrant(uid, operateUid, startDate, endDate, gStatus, gsid);
		exportExcelData(list, "活动加币信息", request, response);
	}
	/**
	 * 生成excel并下载
	 * 
	 * @param operateList
	 * @param date
	 * @param response
	 */
	private void exportExcelData(List list, String title, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			ExcelExportData info = new ExcelExportData();
			List<String[]> columnNames = new ArrayList<String[]>();
			// 列
			String[] cols = new String[] {"用户UID", "加前金币数", "加前声援值", "新加金币数", "新加声援值","备注","操作者",
										  "所属家族助理姓名","所属家族助理电话",
										  "所属黄金公会名称","所属黄金公会联系人姓名","所属黄金公会联系电话", 
										  "所属铂金公会名称","所属铂金公会联系人姓名","所属铂金公会联系电话", 
										  "所属钻石公会名称", "所属钻石公会联系人姓名", "所属钻石公会联系电话",
										  "所属星耀公会名称","所属星耀公会联系人姓名","所属星耀公会联系电话",
										  "添加时间"};
			columnNames.add(cols);
			info.setColumnNames(columnNames);
			// 数据
			LinkedHashMap<String, List<?>> dataMap = new LinkedHashMap<String, List<?>>();
			dataMap.put(title, list);
			info.setDataMap(dataMap);
	
			// 对象属性名称
			List<String[]> fieldNames = new ArrayList<String[]>();
			fieldNames.add(new String[] { "uid", "oldzhutou", "oldcredit", "zhutou", "credit","descrip","username",
										  "salesmanName","salesmanContactsPhone",
										  "agentUserName","agentUserContactsName","agentUserContactsPhone",
										  "promotersName","promotersContactsName","promotersContactsPhone",
										  "extensionCenterName","extensionCenterContactsName","extensionCenterContactsPhone",
										  "strategicPartnerName","strategicPartnerContactsName","strategicPartnerContactsPhone",
										  "addtimeStr"});
			info.setFieldNames(fieldNames);
			// 页签名称
			info.setTitles(new String[] { title });
			byte[] export2ByteArray = ExcelUtil.getInstance().export2ByteArray(info);
			ResponseUtils.download(request, response, export2ByteArray, title + ".xls");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	
	
	@RequestMapping(value = "/addGrant")
	@ResponseBody
	public Map<String, Object> addGrant(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, Object> mapResult = new HashMap<String, Object>();
		try{
			AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
			if (adminUser == null) {
				mapResult.put("errMsg", "请先登录");
				return mapResult;
			}
			
			String strUid = request.getParameter("uid");
			String strZhutou = request.getParameter("zhutou");
			String strCredit = request.getParameter("credit");
			String descrip = request.getParameter("descrip");
			
			int uid = StringUtils.isEmpty(strUid) ? 0 : Integer.valueOf(strUid);
			if (uid < 10000000 || uid > 100000000) {
				mapResult.put("errMsg", "用户UID错误");
				return mapResult;
			}
			int zhutou = StringUtils.isEmpty(strZhutou) ? 0 : Integer.valueOf(strZhutou);
			int credit = StringUtils.isEmpty(strCredit) ? 0 : Integer.valueOf(strCredit);
			
			if (StringUtils.isEmpty(descrip)) {
				mapResult.put("errMsg", "请添加说明");
				return mapResult;
			}
			
			int addGrant = anchorService.addGrant(uid, zhutou, credit, descrip, adminUser.getUid());
			if (addGrant != 2) {
				mapResult.put("errMsg", "添加失败");
			}
			if (zhutou > 100000) {
				String content = "【麦芽直播】: "+adminUser.getUsername() + " 给 " +uid +" 添加了 " + zhutou + "金币";
				content = URLEncoder.encode(content, "gbk");
			}
		}catch(Exception ex){
			System.out.println("addGrant-Exception:" + ex.getMessage());
			mapResult.put("errMsg", ex.getMessage());
		}
		return mapResult;
	}

	
	@RequestMapping(value = "/getNickName",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getNickName(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, Object> map = new HashMap<String, Object>();
		String strUid = request.getParameter("uid");
		int uid = StringUtils.isEmpty(strUid) ? 0: Integer.valueOf(strUid);
		if (uid <= 0) {
			map.put("success", 201);
			map.put("msg", "参数无效201");
		}else {
			String nickName = anchorService.getNickName(uid);
			if ("undefined".equals(nickName)) {
				map.put("success", 202);
				map.put("msg", "参数无效202");
			}else {
				map.put("success", 200);
				map.put("nickname", nickName);
			}
		}
		return map;
	}
	
	@RequestMapping(value = "/getUsedExpList")
	@ResponseBody
	public Map<String, Object> getUsedExpList(HttpServletRequest request,HttpServletResponse response){

		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			return null;
		}
		
		String strUid = request.getParameter("uid");
		String strSTime = request.getParameter("startDate");
		String strETime = request.getParameter("endDate");
		
		if (StringUtils.isEmpty(strSTime) || StringUtils.isEmpty(strETime)) {
			return null;
		}
		
		int uid = StringUtils.isEmpty(strUid) ? 0:Integer.valueOf(strUid);

		Long stime = DateUtil.dateToLong(strSTime, "yyyy-MM-dd", "other", 0);
		Long etime = DateUtil.dateToLong(strETime, "yyyy-MM-dd", "other", 0) + 24*3600;
		
		int ipage = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int irows = request.getParameter("rows") == null ? 20:Integer.valueOf(request.getParameter("rows"));
		if (ipage == 0) {
			ipage = 1;
		}
		
		return anchorService.getUsedExpList(uid, stime, etime, ipage, irows);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addUserExp")
	@ResponseBody
	public Map<String, Object> addUserExp(HttpServletRequest request,HttpServletResponse response){

		Map<String, Object> map = new HashMap<String, Object>();
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			map.put("success", 201);
			map.put("msg", "请先登录");
			return map;
		}
		
		String strUid = request.getParameter("uid");
		String strExp = request.getParameter("exp");
		String strDescrip = request.getParameter("descrip");
		
		try {
			int uid = StringUtils.isEmpty(strUid) ? 0:Integer.valueOf(strUid);
			Long exp = StringUtils.isEmpty(strExp) ? 0:Long.valueOf(strExp);
			if (uid <= 0 || exp <= 0 || StringUtils.isEmpty(strDescrip)) {
				map.put("success", 202);
				map.put("msg", "参数不正确202");
			} else if (exp > 1000000  && adminUser.getRole_id() != 1) {
				map.put("success", 206);
				map.put("msg", "经验值太大，请找让管理人员添加206");
			} else {

				int expId = anchorService.addUserExp(uid, exp, strDescrip, adminUser.getUid());
				if (expId > 0) {
					HttpResponse<String> addVipJson = Unirest.get(Constant.business_server_url + "/admin/addExp?dstuid=" + uid +"&exp=" + exp).asString();
					if (addVipJson.getStatus() == 200) {
						Map<String, Object> parse = (Map<String, Object>)JSONObject.parse(addVipJson.getBody().toString());
						
						if ("200".equals(parse.get("code").toString())) {
							// 添加成功
							int updUserExpStatus = anchorService.updUserExpStatus(expId, 1);
							if (updUserExpStatus > 0) {
								map.put("success", 200);
								map.put("msg", "成功");
							}else {
								map.put("success", 208);
								map.put("msg", "线上添加成功，但后端状态修改失败208");
							}
						}else {
							// 添加失败
							map.put("success", 203);
							map.put("msg", parse.get("message"));
						}
					}else {
						map.put("success", 205);
						map.put("msg", "服务器不稳定205");
					}
				}else {
					map.put("success", 207);
					map.put("msg", "添加记录失败207");
				}
			}
		} catch (Exception e) {
			
			System.out.println("addUserVip-excep:" + e.getMessage());

			map.put("success", 204);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	
	@RequestMapping(value = "/getAnchorCashOfArttube")
	@ResponseBody
	public Map<String, Object> getAnchorCashOfArttube(HttpServletRequest request,HttpServletResponse response){
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			return null;
		}
		
		String strUid = request.getParameter("uid");
		String strTimes = request.getParameter("times");
		String strUnionId = request.getParameter("unionid");
		
		int uid = StringUtils.isEmpty(strUid) ? 0: Integer.valueOf(strUid);
		int times = StringUtils.isEmpty(strTimes) ? 0 : Integer.valueOf(strTimes);
		int unionid = StringUtils.isEmpty(strUnionId) ? 0 : Integer.valueOf(strUnionId);
		
		int ipage = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int irows = request.getParameter("rows") == null ? 20:Integer.valueOf(request.getParameter("rows"));
		if (ipage == 0) {
			ipage = 1;
		}
		
		return anchorService.getAnchorCashOfArttube(unionid,uid, times, ipage, irows);
	}
	
	@RequestMapping(value = "/verifyByArttube")
	@ResponseBody
	public Map<String, Object> verifyByArttube(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, Object> mapResult = new HashMap<String, Object>();
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			mapResult.put("success", 201);
			mapResult.put("msg", "请先登录201");
			return mapResult;
		}
		
		String strId = request.getParameter("id");
		String strUid = request.getParameter("uid");
		String strType = request.getParameter("type");
		String strUnionid =request.getParameter("unionid");
		
		
		if (StringUtils.isEmpty(strId) || StringUtils.isEmpty(strUid) || StringUtils.isEmpty(strType) || StringUtils.isEmpty(strUnionid)) {
			mapResult.put("success", 202);
			mapResult.put("msg", "参数异常202");
			return mapResult;
		}
		try {
			int unionid = Integer.valueOf(strUnionid);
			
			UnionModel unionInfo = UnionDao.getIns().getUnionById(unionid);
			if (unionInfo == null) {
				mapResult.put("success", 203);
				mapResult.put("msg", "参数异常203");

				return mapResult;
			}
			
			if (unionInfo.getAdminuid() != adminUser.getUid()) {

				mapResult.put("success", 204);
				mapResult.put("msg", "你没有权限审核204");

				return mapResult;
			}
			int id = Integer.valueOf(strId);
			int uid = Integer.valueOf(strUid);
			int type = Integer.valueOf(strType);
			
			int verifyByArttube = anchorService.verifyByArttube(type, id, uid, adminUser.getUid());
			if (verifyByArttube > 0) {
				mapResult.put("success", 200);
			}else {
				mapResult.put("success", 205);
				mapResult.put("msg", "你没有权限审核205");
			}
			
		} catch (Exception e) {
			mapResult.put("success", 401);
			mapResult.put("msg", e.getMessage());
		}
		return mapResult;
	}
	
	@RequestMapping(value = "/getAnchorCashOfOperate")
	@ResponseBody
	public Map<String, Object> getAnchorCashOfOperate(HttpServletRequest request,HttpServletResponse response){
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			return null;
		}
		
		String strTimes = request.getParameter("times");
		String strUid = request.getParameter("uid");
		String strUnionid = request.getParameter("unionid");
		
		int times = StringUtils.isEmpty(strTimes) ? 0 : Integer.valueOf(strTimes);
		int uid = StringUtils.isEmpty(strUid) ? 0:Integer.valueOf(strUid);
		int unionid = StringUtils.isEmpty(strUnionid) ? 0 : Integer.valueOf(strUnionid);
		
		
		int ipage = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int irows = request.getParameter("rows") == null ? 20:Integer.valueOf(request.getParameter("rows"));
		if (ipage == 0) {
			ipage = 1;
		}
		
		return anchorService.getAnchorCashOfOperate(uid,unionid,times, ipage, irows);
	}
	
	@RequestMapping(value = "/verifyByOperate")
	@ResponseBody
	public Map<String, Object> verifyByOperate(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, Object> mapResult = new HashMap<String, Object>();
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			mapResult.put("success", 201);
			mapResult.put("msg", "请先登录201");
			return mapResult;
		}
		
		String strTimes = request.getParameter("times");
		String strUid = request.getParameter("uid");
		String strType = request.getParameter("type");
		
		if (StringUtils.isEmpty(strTimes) || StringUtils.isEmpty(strUid) || StringUtils.isEmpty(strType)) {
			mapResult.put("success", 202);
			mapResult.put("msg", "参数异常202");
			return mapResult;
		}
		try {

			int times = Integer.valueOf(strTimes);
			int uid = Integer.valueOf(strUid);
			int type = Integer.valueOf(strType);
			
			int verifyByArttube = anchorService.verifyByOperate(type, times, uid, adminUser.getUid());
			if (verifyByArttube > 0) {
				mapResult.put("success", 200);
			}else {
				mapResult.put("success", 205);
				mapResult.put("msg", "你没有权限审核205");
			}
			
		} catch (Exception e) {
			mapResult.put("success", 401);
			mapResult.put("msg", e.getMessage());
		}
		return mapResult;
	}
	
	@RequestMapping(value = "/getCashCreditList")
	@ResponseBody
	public Map<String, Object> getCashCreditList(HttpServletRequest request,HttpServletResponse response){
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			return null;
		}
		
		String strUid = request.getParameter("uid");
		String strStatus = request.getParameter("status"); // =9全部，其他则具体状态
		String strStartDate = request.getParameter("startdate") == null ? "" : request.getParameter("startdate");
		String strEndDate = request.getParameter("enddate") == null ? "" : request.getParameter("enddate");
		if (StringUtils.isEmpty(strStartDate) || StringUtils.isEmpty(strEndDate)) {
			return null;
		}

		long lgStime = 0;
		long lgEtime = 0;
		lgStime = DateUtil.dateToLong(strStartDate, "yyyy-MM-dd", "", 0);
		lgEtime = DateUtil.dateToLong(strEndDate, "yyyy-MM-dd", "", 0);
		
		int ipage = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int irows = request.getParameter("rows") == null ? 20:Integer.valueOf(request.getParameter("rows"));
		if (ipage == 0) {
			ipage = 1;
		}
		
		try {
			int uid = StringUtils.isEmpty(strUid) ? 0 : Integer.valueOf(strUid);
			int status = StringUtils.isEmpty(strStatus) ? 0 : Integer.valueOf(strStatus);
			
			return anchorService.getCashCreditList(uid,status,ipage,irows,lgStime,lgEtime);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
