package com.tinypig.newadmin.web.action;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.util.DateUtil;
import com.tinypig.admin.util.ExcelExportData;
import com.tinypig.admin.util.ExcelUtil;
import com.tinypig.newadmin.common.ResponseUtils;
import com.tinypig.newadmin.web.entity.RealNameParamDto;
import com.tinypig.newadmin.web.service.realNameCheckService;

@Controller
@RequestMapping("/rCheck")
public class realNameCheckAction {
	
	private static Logger log = Logger.getLogger(realNameCheckAction.class);

	
	@Autowired
	private realNameCheckService realNameCheckService;
	
	@RequestMapping(value = "/getRealNameList", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getRealNameList(HttpServletRequest request, RealNameParamDto param) {
		Integer page = Integer.valueOf(request.getParameter("page"));
		Integer rows = Integer.valueOf(request.getParameter("rows"));
		Integer startIndex = (page-1)*rows;
		param.setStartIndex(startIndex);
		param.setPageSize(rows);
		Integer dateStatus = null;
		if(StringUtils.isNotEmpty(request.getParameter("dateStatus"))) {
			dateStatus = Integer.valueOf(request.getParameter("dateStatus"));
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			if(dateStatus == 1) {
				if(StringUtils.isNotEmpty(startDate)) {
					param.setcStartTime(DateUtil.dateToLong(startDate, "yyyy-MM-dd", "other", 0));
				}
				if(StringUtils.isNotEmpty(request.getParameter("endDate"))) {
					param.setcEndTime(DateUtil.dateToLong(endDate, "yyyy-MM-dd", "day", 1));
				}
			}else if(dateStatus ==2) {
				if(StringUtils.isNotEmpty(startDate)) {
					param.setpStartTime(DateUtil.dateToLong(startDate, "yyyy-MM-dd", "other", 0));
				}
				if(StringUtils.isNotEmpty(request.getParameter("endDate"))) {
					param.setpEndTime(DateUtil.dateToLong(endDate, "yyyy-MM-dd", "day", 1));
				}
			}
		}
		Integer gStatus =  null;
		if(StringUtils.isNotEmpty(request.getParameter("gStatus"))) {
			gStatus = Integer.valueOf(request.getParameter("gStatus"));
			if(StringUtils.isNotEmpty(request.getParameter("gsid"))) {
				Integer gsid = Integer.valueOf(request.getParameter("gsid"));
				if(gStatus == 1) {
					param.setSpid(gsid);
				}else if(gStatus == 2) {
					param.setEcid(gsid);
				}else if(gStatus == 3) {
					param.setPid(gsid);
				}else if(gStatus == 4) {
					param.setAuid(gsid);
				}else if(gStatus == 5) {
					param.setSid(gsid);
				}
			}
		}
		Map<String, Object> map = realNameCheckService.getRealNameListPage(param);
		return JSONObject.toJSONString(map);
	}
	
	@RequestMapping(value = "/expExcel")
	public void expExcel(HttpServletRequest request,HttpServletResponse response, RealNameParamDto param) {
		Integer dateStatus = null;
		if(StringUtils.isNotEmpty(request.getParameter("dateStatus"))) {
			dateStatus = Integer.valueOf(request.getParameter("dateStatus"));
			String startDate = request.getParameter("startdate");
			String endDate = request.getParameter("enddate");
			String auditStatus = request.getParameter("aStatus");
			if(StringUtils.isNotEmpty(auditStatus)) {
				param.setAuditStatus(Integer.valueOf(auditStatus));
			}
			if(dateStatus == 1) {
				if(StringUtils.isNotEmpty(startDate)) {
					param.setcStartTime(DateUtil.dateToLong(startDate, "yyyy-MM-dd", "other", 0));
				}
				if(StringUtils.isNotEmpty(request.getParameter("endDate"))) {
					param.setcEndTime(DateUtil.dateToLong(endDate, "yyyy-MM-dd", "day", 1));
				}
			}else if(dateStatus ==2) {
				if(StringUtils.isNotEmpty(startDate)) {
					param.setpStartTime(DateUtil.dateToLong(startDate, "yyyy-MM-dd", "other", 0));
				}
				if(StringUtils.isNotEmpty(request.getParameter("endDate"))) {
					param.setpEndTime(DateUtil.dateToLong(endDate, "yyyy-MM-dd", "day", 1));
				}
			}
		}
		Integer gStatus =  null;
		if(StringUtils.isNotEmpty(request.getParameter("gStatus"))) {
			gStatus = Integer.valueOf(request.getParameter("gStatus"));
			if(StringUtils.isNotEmpty(request.getParameter("gsid"))) {
				Integer gsid = Integer.valueOf(request.getParameter("gsid"));
				if(gStatus == 1) {
					param.setSpid(gsid);
				}else if(gStatus == 2) {
					param.setEcid(gsid);
				}else if(gStatus == 3) {
					param.setPid(gsid);
				}else if(gStatus == 4) {
					param.setAuid(gsid);
				}else if(gStatus == 5) {
					param.setSid(gsid);
				}
			}
		}
		List<Map<String, Object>> list = realNameCheckService.getAllRealNameList(param);
		exportExcelData(list, "身份审核信息", request, response);
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
			String[] cols = new String[] {"用户UID", "真实名字", "身份证ID", "提交审核时间", "审核时间",
										  "所属家族助理姓名","所属家族助理电话",
										  "所属黄金公会名称","所属黄金公会联系人姓名","所属黄金公会联系电话", 
										  "所属铂金公会名称","所属铂金公会联系人姓名","所属铂金公会联系电话", 
										  "所属钻石公会名称", "所属钻石公会联系人姓名", "所属钻石公会联系电话",
										  "所属星耀公会名称","所属星耀公会联系人姓名","所属星耀公会联系电话",
										  "状态"};
			columnNames.add(cols);
			info.setColumnNames(columnNames);
			// 数据
			LinkedHashMap<String, List<?>> dataMap = new LinkedHashMap<String, List<?>>();
			dataMap.put(title, list);
			info.setDataMap(dataMap);
	
			// 对象属性名称
			List<String[]> fieldNames = new ArrayList<String[]>();
			fieldNames.add(new String[] { "uid", "realName", "cardID", "creatAtStr", "auditAtStr",
										  "salesmanName","salesmanContactsPhone",
										  "agentUserName","agentUserContactsName","agentUserContactsPhone",
										  "promotersName","promotersContactsName","promotersContactsPhone",
										  "extensionCenterName","extensionCenterContactsName","extensionCenterContactsPhone",
										  "strategicPartnerName","strategicPartnerContactsName","strategicPartnerContactsPhone",
										  "auditStatusStr"});
			info.setFieldNames(fieldNames);
			// 页签名称
			info.setTitles(new String[] { title });
			byte[] export2ByteArray = ExcelUtil.getInstance().export2ByteArray(info);
			ResponseUtils.download(request, response, export2ByteArray, title + ".xls");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	
}
