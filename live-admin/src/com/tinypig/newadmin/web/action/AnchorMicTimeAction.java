package com.tinypig.newadmin.web.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import com.tinypig.admin.util.ExcelExportData;
import com.tinypig.admin.util.ExcelUtil;
import com.tinypig.newadmin.common.ResponseUtils;
import com.tinypig.newadmin.web.entity.AnchorMicTimeParamDto;
import com.tinypig.newadmin.web.entity.LiveMicTime;
import com.tinypig.newadmin.web.entity.PayOrderSta;
import com.tinypig.newadmin.web.entity.PayOrderStaParam;
import com.tinypig.newadmin.web.service.AnchorMicTimeService;

@Controller
@RequestMapping("/anchorTime")
public class AnchorMicTimeAction {
	
	private static Logger log = Logger.getLogger(AnchorMicTimeAction.class);
	
	@Autowired
	private AnchorMicTimeService anchorMicTimeService;

	@RequestMapping(value = "/getAnchorMicTimeList", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getAnchorMicTimeList(HttpServletRequest request, AnchorMicTimeParamDto param) {
		Integer page = Integer.valueOf(request.getParameter("page"));
		Integer rows = Integer.valueOf(request.getParameter("rows"));
		Integer startIndex = (page-1)*rows;
		param.setStartIndex(startIndex);
		param.setPageSize(rows);
		String gstype = request.getParameter("gstype");
		if(StringUtils.isNotBlank(gstype)) {
			Long gsid = null;
			String gsidStr = request.getParameter("gsid");
			if(StringUtils.isNotBlank(gsidStr)) {
				gsid = Long.parseLong(request.getParameter("gsid"));
			}
			if("1".equals(gstype)) {
				param.setStrategicPartnerId(gsid);
			}else if("2".equals(gstype)) {
				param.setExtensionCenterId(gsid);
			}else if("3".equals(gstype)) {
				param.setPromotersId(gsid);
			}else if("4".equals(gstype)) {
				param.setAgentUserId(gsid);
			}else if("5".equals(gstype)) {
				param.setSalesmanId(gsid);
			}
		}
		Map<String,Object> resultMap =  anchorMicTimeService.getAnchorMicTimeList(param);
		return JSONObject.toJSONString(resultMap);
	}
	
	@RequestMapping(value = "/expExcel")
	public void expExcel(HttpServletRequest request,HttpServletResponse response,AnchorMicTimeParamDto param) {
		String gstype = request.getParameter("gstype");
		if(StringUtils.isNotBlank(gstype)) {
			Long gsid = null;
			String gsidStr = request.getParameter("gsid");
			if(StringUtils.isNotBlank(gsidStr)) {
				gsid = Long.parseLong(request.getParameter("gsid"));
			}
			if("1".equals(gstype)) {
				param.setStrategicPartnerId(gsid);
			}else if("2".equals(gstype)) {
				param.setExtensionCenterId(gsid);
			}else if("3".equals(gstype)) {
				param.setPromotersId(gsid);
			}else if("4".equals(gstype)) {
				param.setAgentUserId(gsid);
			}else if("5".equals(gstype)) {
				param.setSalesmanId(gsid);
			}
		}
		List<Map<String, Object>> list = anchorMicTimeService.getAllAnchorMicTimeList(param);
		exportExcelData(list, "主播麦时统计", request, response);
	}
	

	/**
	 * 生成excel并下载
	 * 
	 * @param operateList
	 * @param date
	 * @param response
	 */
	private void exportExcelData(List<Map<String, Object>> list, String title, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			for (Map<String, Object> map : list) {
				Integer openTime = ((BigDecimal)map.get("openTime")).intValue();
				Integer sm = openTime/60;
				Integer h = sm/60;
				Integer m = sm%60;
				String openTimeStr = h+"小时"+(m < 10 ? ('0' + m) : m)+"分钟";
				map.put("openTimeStr", openTimeStr);
			}
			
			
			ExcelExportData info = new ExcelExportData();
			List<String[]> columnNames = new ArrayList<String[]>();
			// 列
			String[] cols = new String[] {"主播ID", "主播昵称", "开播日期", "开播时长", 
										  "所属家族助理姓名","所属家族助理电话",
										  "所属黄金公会名称","所属黄金公会联系人姓名","所属黄金公会联系电话", 
										  "所属铂金公会名称","所属铂金公会联系人姓名","所属铂金公会联系电话", 
										  "所属钻石公会名称", "所属钻石公会联系人姓名", "所属钻石公会联系电话",
										  "所属星耀公会名称","所属星耀公会联系人姓名","所属星耀公会联系电话"};
			columnNames.add(cols);
			info.setColumnNames(columnNames);
			// 数据
			LinkedHashMap<String, List<?>> dataMap = new LinkedHashMap<String, List<?>>();
			dataMap.put(title, list);
			info.setDataMap(dataMap);
	
			// 对象属性名称
			List<String[]> fieldNames = new ArrayList<String[]>();
			fieldNames.add(new String[] { "uid", "nickname", "RepDateStr","openTimeStr",
										  "salesmanName","salesmanContactsPhone",
										  "agentUserName","agentUserContactsName","agentUserContactsPhone",
										  "promotersName","promotersContactsName","promotersContactsPhone",
										  "extensionCenterName","extensionCenterContactsName","extensionCenterContactsPhone",
										  "strategicPartnerName","strategicPartnerContactsName","strategicPartnerContactsPhone"});
			info.setFieldNames(fieldNames);
			// 页签名称
			info.setTitles(new String[] { title });
			byte[] export2ByteArray = ExcelUtil.getInstance().export2ByteArray(info);
			ResponseUtils.download(request, response, export2ByteArray, title + ".xls");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	
	@RequestMapping(value = "/getAnchorLiveSta", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getAnchorLiveSta(HttpServletRequest request, AnchorMicTimeParamDto param) {
		List<LiveMicTime> list = anchorMicTimeService.getAnchorLiveSta(param);
		return JSONObject.toJSONString(list);
	}
	
	@RequestMapping(value = "/getAllAnchorTime")
	@ResponseBody
	public Map<String, Integer> getAllAnchorTime(HttpServletRequest request, AnchorMicTimeParamDto param) {
		String gstype = request.getParameter("gstype");
		if(StringUtils.isNotBlank(gstype)) {
			Long gsid = null;
			String gsidStr = request.getParameter("gsid");
			if(StringUtils.isNotBlank(gsidStr)) {
				gsid = Long.parseLong(request.getParameter("gsid"));
			}
			if("1".equals(gstype)) {
				param.setStrategicPartnerId(gsid);
			}else if("2".equals(gstype)) {
				param.setExtensionCenterId(gsid);
			}else if("3".equals(gstype)) {
				param.setPromotersId(gsid);
			}else if("4".equals(gstype)) {
				param.setAgentUserId(gsid);
			}else if("5".equals(gstype)) {
				param.setSalesmanId(gsid);
			}
		}
		Integer allAnchorTime =  anchorMicTimeService.getAllAnchorTime(param);
		Map<String, Integer> resultMap = new HashMap<String,Integer>();
		resultMap.put("allAnchorTime", allAnchorTime);
		return resultMap;
	}
}
