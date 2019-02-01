package com.tinypig.newadmin.web.action;

import java.util.ArrayList;
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
import com.tinypig.admin.util.ExcelExportData;
import com.tinypig.admin.util.ExcelUtil;
import com.tinypig.newadmin.common.ResponseUtils;
import com.tinypig.newadmin.web.service.BillcvgInfoService;
import com.tinypig.newadmin.web.vo.BillcvgParamVo;

@Controller
@RequestMapping("/cvgSta")
public class BillcvgInfoAction {
	private static Logger log = Logger.getLogger(BillcvgInfoAction.class);

	@Autowired
	private BillcvgInfoService billcvgInfoService;

	@RequestMapping(value = "/getcvgStaList", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getcvgStaList(HttpServletRequest request, BillcvgParamVo param) {
		long logSTime = System.currentTimeMillis();
		log.info("========购买商城道具统计列表=======START");
		Integer page = Integer.valueOf(request.getParameter("page"));
		Integer rows = Integer.valueOf(request.getParameter("rows"));
		Integer startIndex = (page - 1) * rows;
		param.setStartIndex(startIndex);
		param.setPageSize(rows);
		Map<String, Object> map = billcvgInfoService.getcvgStaList(param);
		long logETime = System.currentTimeMillis();
		log.info("========购买商城道具统计列表=======END("+(logETime-logSTime)+")ss");
		return JSONObject.toJSONString(map);
	}

	@RequestMapping(value = "/expExcel")
	public void expExcel(HttpServletRequest request, HttpServletResponse response, BillcvgParamVo param) {
		List<Map<String, Object>> list = billcvgInfoService.getAllCvgStaList(param);
		exportExcelData(list, "购买商城道具统计信息", request, response);
	}

	/**
	 * 生成excel并下载
	 * 
	 * @param operateList
	 * @param date
	 * @param response
	 */
	private void exportExcelData(List list, String title, HttpServletRequest request, HttpServletResponse response) {
		try {
			ExcelExportData info = new ExcelExportData();
			List<String[]> columnNames = new ArrayList<String[]>();
			// 列
			String[] cols = new String[] { "购买用户ID", "接收道具用户ID", "道具名称", "道具类型", //
					"购买道具时长（月）", "购买道具总价", "道具状态", "购买时间", "开始时间", "结束时间", //
					"购买用户所属家族助理姓名", "购买用户所属家族助理电话", //
					"购买用户所属黄金公会名称", "购买用户所属黄金公会联系人姓名", "购买用户所属黄金公会联系电话", //
					"购买用户所属铂金公会名称", "购买用户所属铂金公会联系人姓名", "购买用户所属铂金公会联系电话", //
					"购买用户所属钻石公会名称", "购买用户所属钻石公会联系人姓名", "购买用户所属钻石公会联系电话", //
					"购买用户所属星耀公会名称", "购买用户所属星耀公会联系人姓名", "购买用户所属星耀公会联系电话",//
					"接收道具用户所属家族助理姓名", "接收道具用户所属家族助理电话", //
					"接收道具用户所属黄金公会名称", "接收道具用户所属黄金公会联系人姓名", "接收道具用户所属黄金公会联系电话", //
					"接收道具用户所属铂金公会名称", "接收道具用户所属铂金公会联系人姓名", "接收道具用户所属铂金公会联系电话", //
					"接收道具用户所属钻石公会名称", "接收道具用户所属钻石公会联系人姓名", "接收道具用户所属钻石公会联系电话", //
					"接收道具用户所属星耀公会名称", "接收道具用户所属星耀公会联系人姓名", "接收道具用户所属星耀公会联系电话"//
			};
			columnNames.add(cols);
			info.setColumnNames(columnNames);
			// 数据
			LinkedHashMap<String, List<?>> dataMap = new LinkedHashMap<String, List<?>>();
			dataMap.put(title, list);
			info.setDataMap(dataMap);

			// 对象属性名称
			List<String[]> fieldNames = new ArrayList<String[]>();
			fieldNames.add(new String[] { "uid", "anchorid", "gname", "type", //
					"count", "realpricetotal", "gstatus", "addtime", "starttime", "endtime", //
					"salesmanName1", "salesmanContactsPhone1", //
					"agentUserName1", "agentUserContactsName1", "agentUserContactsPhone1", //
					"promotersName1", "promotersContactsName1", "promotersContactsPhone1", //
					"extensionCenterName1", "extensionCenterContactsName1", "extensionCenterContactsPhone1", //
					"strategicPartnerName1", "strategicPartnerContactsName1", "strategicPartnerContactsPhone1",//
					"salesmanName2", "salesmanContactsPhone2", //
					"agentUserName2", "agentUserContactsName2", "agentUserContactsPhone2", //
					"promotersName2", "promotersContactsName2", "promotersContactsPhone2", //
					"extensionCenterName2", "extensionCenterContactsName2", "extensionCenterContactsPhone2", //
					"strategicPartnerName2", "strategicPartnerContactsName2", "strategicPartnerContactsPhone2"//
			});
			info.setFieldNames(fieldNames);
			// 页签名称
			info.setTitles(new String[] { title });
			byte[] export2ByteArray = ExcelUtil.getInstance().export2ByteArray(info);
			ResponseUtils.download(request, response, export2ByteArray, title + ".xls");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
