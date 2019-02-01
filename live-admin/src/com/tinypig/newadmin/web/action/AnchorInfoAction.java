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
import com.tinypig.admin.util.ExcelExportData;
import com.tinypig.admin.util.ExcelUtil;
import com.tinypig.newadmin.common.ResponseUtils;
import com.tinypig.newadmin.web.entity.AnchorInfoParamDto;
import com.tinypig.newadmin.web.service.AnchorInfoService;

@Controller
@RequestMapping("/anchoIl")
public class AnchorInfoAction {
	
	@Autowired
	private AnchorInfoService anchorInfoService;
	
	private static Logger log = Logger.getLogger(AnchorInfoAction.class);

	@RequestMapping(value = "/getanchorInfoList", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getanchorInfoList(HttpServletRequest request, AnchorInfoParamDto param) {
		Integer page = Integer.valueOf(request.getParameter("page"));
		Integer rows = Integer.valueOf(request.getParameter("rows"));
		Integer startIndex = (page-1)*rows;
		param.setStartIndex(startIndex);
		param.setPageSize(rows);
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
		Map<String, Object> map = anchorInfoService.getAnchorList(param);
		return JSONObject.toJSONString(map);
	}
	
	@RequestMapping(value = "/expExcel")
	public void expExcel(HttpServletRequest request,HttpServletResponse response,AnchorInfoParamDto param) {
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
		List<Map<String,Object>> list = anchorInfoService.getAnchorAllList(param);
		exportExcelData(list, "主播信息", request, response);
	}
	
	/**
	 * 生成excel并下载
	 * 
	 * @param operateList
	 * @param date
	 * @param response
	 */
	private void exportExcelData(List<Map<String,Object>> list, String title, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			ExcelExportData info = new ExcelExportData();
			List<String[]> columnNames = new ArrayList<String[]>();
			// 列
			String[] cols = new String[] {"主播ID", "主播昵称", "开通主播权限时间",
										  "所属家族助理姓名","所属家族助理电话",
										  "所属黄金公会名称","所属黄金公会联系人姓名","所属黄金公会联系电话", 
										  "所属铂金公会名称","所属铂金公会联系人姓名","所属铂金公会联系电话", 
										  "所属钻石公会名称", "所属钻石公会联系人姓名", "所属钻石公会联系电话",
										  "所属星耀公会名称","所属星耀公会联系人姓名","所属星耀公会联系电话",
										  "直播状态","主播状态"};
			columnNames.add(cols);
			info.setColumnNames(columnNames);
			// 数据
			LinkedHashMap<String, List<?>> dataMap = new LinkedHashMap<String, List<?>>();
			dataMap.put(title, list);
			info.setDataMap(dataMap);
	
			// 对象属性名称
			List<String[]> fieldNames = new ArrayList<String[]>();
			fieldNames.add(new String[] { "uid", "nickname", "auditAtStr",
										  "salesmanName","salesmanContactsPhone",
										  "agentUserName","agentUserContactsName","agentUserContactsPhone",
										  "promotersName","promotersContactsName","promotersContactsPhone",
										  "extensionCenterName","extensionCenterContactsName","extensionCenterContactsPhone",
										  "strategicPartnerName","strategicPartnerContactsName","strategicPartnerContactsPhone",
										  "liveStatusStr","identityStr"});
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
