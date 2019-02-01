package com.tinypig.newadmin.web.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.util.ExcelExportData;
import com.tinypig.admin.util.ExcelUtil;
import com.tinypig.newadmin.common.ResponseUtils;
import com.tinypig.newadmin.web.entity.SupportUser;
import com.tinypig.newadmin.web.entity.SupportUserParamDto;
import com.tinypig.newadmin.web.service.SupportUserServie;

@Controller
@RequestMapping("/support")
public class SupportUserAction {
	private static Logger log = Logger.getLogger(SupportUserAction.class);

	@Autowired
	private SupportUserServie supportUserServie;

	@RequestMapping(value = "/getSupportUserList", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getSupportUserList(HttpServletRequest request,SupportUserParamDto params) {
		Integer page = Integer.valueOf(request.getParameter("page"));
		Integer rows = Integer.valueOf(request.getParameter("rows"));
		Integer startIndex = (page-1)*rows;
		params.setStartIndex(startIndex);
		params.setPageSize(rows);
		Map<String, Object> map = supportUserServie.getSupportUserList(params);
		return JSONObject.toJSONString(map);
	}

	@RequestMapping(value = "/saveSupportUser", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String saveCar(HttpServletRequest request,SupportUser supportUser) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		supportUser.setCreateUserId(Long.valueOf(adminUser == null ? 0 : adminUser.getUid()));
		supportUser.setCreateTime(new Date().getTime());
		Map<String, Object> map = supportUserServie.saveSupportUser(supportUser);
		return JSONObject.toJSONString(map);
	}
	
	@RequestMapping(value = "/updateSupportUser", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateCar(HttpServletRequest request,SupportUser supportUser) {
		supportUser.setUpdateTime(new Date().getTime());
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		supportUser.setUpdateUserId(Long.valueOf(adminUser == null ? 0 : adminUser.getUid()));
		Map<String, Object> map = supportUserServie.updateSupportUser(supportUser);
		return JSONObject.toJSONString(map);
	}
	
	@RequestMapping(value = "/doValid", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String doValid(Long id, Integer status,Integer uid, HttpServletRequest request) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		Long updateUserId = Long.valueOf(adminUser == null ? 0 : adminUser.getUid());
		Long updateTime = new Date().getTime();
		Map<String, Object> map = supportUserServie.doValid(id, uid, status, updateUserId, updateTime);
		return JSONObject.toJSONString(map);
	}
	
	@RequestMapping(value = "/expExcel")
	public void expExcel(HttpServletRequest request,HttpServletResponse response, SupportUserParamDto param) {
		List<Map<String, Object>> list = supportUserServie.getAllSupportUserList(param);
		exportExcelData(list, "扶持用户列表", request, response);
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
			String[] cols = new String[] {"ID", "备注", "状态", "创建时间", 
										  "所属家族助理姓名","所属家族助理电话",
										  "所属黄金公会名称","所属黄金公会联系人姓名","所属黄金公会联系电话", 
										  "所属铂金公会名称","所属铂金公会联系人姓名","所属铂金公会联系电话", 
										  "所属钻石公会名称", "所属钻石公会联系人姓名", "所属钻石公会联系电话",
										  "所属星耀公会名称","所属星耀公会联系人姓名","所属星耀公会联系电话"
										  };
			columnNames.add(cols);
			info.setColumnNames(columnNames);
			// 数据
			LinkedHashMap<String, List<?>> dataMap = new LinkedHashMap<String, List<?>>();
			dataMap.put(title, list);
			info.setDataMap(dataMap);
	
			// 对象属性名称
			List<String[]> fieldNames = new ArrayList<String[]>();
			fieldNames.add(new String[] { "uid", "notes", "status", "create_time",
										  "salesmanName","salesmanContactsPhone",
										  "agentUserName","agentUserContactsName","agentUserContactsPhone",
										  "promotersName","promotersContactsName","promotersContactsPhone",
										  "extensionCenterName","extensionCenterContactsName","extensionCenterContactsPhone",
										  "strategicPartnerName","strategicPartnerContactsName","strategicPartnerContactsPhone",
										 });
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
