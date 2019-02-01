package com.tinypig.admin.web;

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
import com.tinypig.newadmin.web.entity.GameRecordStaDto;
import com.tinypig.newadmin.web.service.IGameRecordService;

/**
 * 游戏历史统计
 */
@Controller
@RequestMapping("/gameRecordSta")
public class GameRecordStaController {
	
	private static Logger log = Logger.getLogger(GameRecordStaController.class);


	@Autowired
	private IGameRecordService gameRecordService;

	@RequestMapping(value = "/gameRecordList")
	@ResponseBody
	public String gameRecordList(HttpServletRequest request, GameRecordStaDto param) {
		long logSTime = System.currentTimeMillis();
		log.info("========游戏历史列表=======START");
		String gstype = request.getParameter("gstype");
		if(StringUtils.isNotBlank(gstype)) {
			Integer gsid = null;
			String gsidStr = request.getParameter("gsid");
			if(StringUtils.isNotBlank(gsidStr)) {
				gsid = Integer.valueOf(request.getParameter("gsid"));
			}
			if("1".equals(gstype)) {
				param.setSpid(gsid);
			}else if("2".equals(gstype)) {
				param.setEcid(gsid);
			}else if("3".equals(gstype)) {
				param.setPid(gsid);
			}else if("4".equals(gstype)) {
				param.setAuid(gsid);
			}else if("5".equals(gstype)) {
				param.setAuid(gsid);
			}
		}
		Map<String, Object> map = gameRecordService.gameRecordList(param);
		long logETime = System.currentTimeMillis();
		log.info("========游戏历史列表=======END("+(logETime-logSTime)+")ss");
		return JSONObject.toJSONString(map);
	}

	@RequestMapping(value = "/getGameRecordTotal")
	@ResponseBody
	public List<Map<String, Object>> getGameRecordTotal(HttpServletRequest request, GameRecordStaDto param) {
		long logSTime = System.currentTimeMillis();
		log.info("========游戏历史合计=======START");
		String gstype = request.getParameter("gstype");
		if(StringUtils.isNotBlank(gstype)) {
			Integer gsid = null;
			String gsidStr = request.getParameter("gsid");
			if(StringUtils.isNotBlank(gsidStr)) {
				gsid = Integer.valueOf(request.getParameter("gsid"));
			}
			if("1".equals(gstype)) {
				param.setSpid(gsid);
			}else if("2".equals(gstype)) {
				param.setEcid(gsid);
			}else if("3".equals(gstype)) {
				param.setPid(gsid);
			}else if("4".equals(gstype)) {
				param.setAuid(gsid);
			}else if("5".equals(gstype)) {
				param.setAuid(gsid);
			}
		}
		List<Map<String, Object>> gameRecordTotal = gameRecordService.getGameRecordTotal(param);
		long logETime = System.currentTimeMillis();
		log.info("========游戏历史合计=======END("+(logETime-logSTime)+")ss");
		return gameRecordTotal;
	}
	
	@RequestMapping(value = "/expExcel")
	public void expExcel(HttpServletRequest request,HttpServletResponse response, GameRecordStaDto param) {
		String gstype = request.getParameter("gstype");
		String searchUid = request.getParameter("searchUid");
		if(StringUtils.isNotBlank(searchUid)) {
			param.setUid(Long.valueOf(searchUid));
		}
		if(StringUtils.isNotBlank(gstype)) {
			Integer gsid = null;
			String gsidStr = request.getParameter("gsid");
			if(StringUtils.isNotBlank(gsidStr)) {
				gsid = Integer.valueOf(request.getParameter("gsid"));
			}
			if("1".equals(gstype)) {
				param.setSpid(gsid);
			}else if("2".equals(gstype)) {
				param.setEcid(gsid);
			}else if("3".equals(gstype)) {
				param.setPid(gsid);
			}else if("4".equals(gstype)) {
				param.setAuid(gsid);
			}else if("5".equals(gstype)) {
				param.setAuid(gsid);
			}
		}
		List<Map<String, Object>> list = gameRecordService.gameAllRecordSta(param);
		exportExcelData(list, "游戏历史", request, response);
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
			ExcelExportData info = new ExcelExportData();
			List<String[]> columnNames = new ArrayList<String[]>();
			// 列
			String[] cols = new String[] {"用户UID", "主播UID", "游戏种类",  "游戏金额", "平台盈亏情况","游戏时间",
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
			fieldNames.add(new String[] { "uid", "roomId", "typeStr", "money", "profit", "ctimeAtStr",
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
}
