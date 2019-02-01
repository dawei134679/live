package com.tinypig.newadmin.web.action;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.tinypig.newadmin.web.entity.PayOrderSta;
import com.tinypig.newadmin.web.entity.PayOrderStaParam;
import com.tinypig.newadmin.web.service.PayOrderStaService;

@Controller
@RequestMapping("/payOrderSta")
public class PayOrderStaAction {
	
	private static Logger log = Logger.getLogger(PayOrderStaAction.class);
	
	@Autowired
	private PayOrderStaService payOrderStaService;

	@RequestMapping(value = "/pageList", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String pageList(HttpServletRequest request,PayOrderStaParam param) {
		long logSTime = System.currentTimeMillis();
		log.info("=======充值查询=======START");
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
		Map<String,Object> resultMap =  payOrderStaService.getPayOrderListPage(param);
		long logETime = System.currentTimeMillis();
		log.info("=======充值查询========END("+(logETime-logSTime)+")ss");
		return JSONObject.toJSONString(resultMap);
	}
	
	@RequestMapping(value = "/getPayOrderTotalAmount")
	@ResponseBody
	public Map<String, Object> getPayOrderTotalAmount(HttpServletRequest request,PayOrderStaParam param) {
		long logSTime = System.currentTimeMillis();
		log.info("=======充值查询合计=======START");
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
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("totalAmount", payOrderStaService.getPayOrderTotalAmount(param));
		long logETime = System.currentTimeMillis();
		log.info("=======充值查询合计=======END("+(logETime-logSTime)+")ss");
		return result;
	}
	
	@RequestMapping(value = "/expExcel")
	public void expExcel(HttpServletRequest request,HttpServletResponse response,PayOrderStaParam param) {
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
		List<PayOrderSta> list = payOrderStaService.getPayOrderList(param);
		exportExcelData(list, "充值信息", request, response);
	}
	
	/**
	 * 生成excel并下载
	 * 
	 * @param operateList
	 * @param date
	 * @param response
	 */
	private void exportExcelData(List<PayOrderSta> list, String title, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			ExcelExportData info = new ExcelExportData();
			List<String[]> columnNames = new ArrayList<String[]>();
			// 列
			String[] cols = new String[] {"用户UID", "用户昵称", "交易号", "金额", "订单状态","数据类型", "第三方交易号", "充值方式", "创建时间", "充值时间", "注册时间",
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
			fieldNames.add(new String[] { "srcuid", "srcnickname", "orderId", "amount", "statusStr","dataTypeStr","inpourNo","paytype","creatAtStr","paytimeStr","registtimeStr",
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
