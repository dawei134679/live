package com.tinypig.newadmin.web.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.util.Constant;
import com.tinypig.admin.util.DateUtil;
import com.tinypig.newadmin.web.entity.StrategicPartner;
import com.tinypig.newadmin.web.service.IStrategicPartnerServie;

/**
 * 星耀公会
 */
@Controller
@RequestMapping("/strategicPartner")
public class StrategicPartnerController {

	@Resource
	private IStrategicPartnerServie strategicPartnerServie;

	@RequestMapping(value = "/getStrategicPartnerList", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getStrategicPartnerList(HttpServletRequest req) {
		String idParam = req.getParameter("id");
		String name = req.getParameter("name");
		String contactsPhone = req.getParameter("contactsPhone");
		Integer page = Integer.valueOf(req.getParameter("page"));
		Integer rows = Integer.valueOf(req.getParameter("rows"));

		String strSTime = req.getParameter("startDate");
		String strETime = req.getParameter("endDate");
		Long stime = null, etime = null;
		if (StringUtils.isNotBlank(strSTime)) {
			stime = DateUtil.dateToLong(strSTime);
		}
		if (StringUtils.isNotBlank(strETime)) {
			etime = DateUtil.dateToLong(strETime);
		}
		String sort = req.getParameter("order");
		String orderBy = null, orderSort = null;
		if (StringUtils.isNotBlank(sort)) {
			orderBy = sort.split(",")[0];
			orderSort = sort.split(",")[1];
		}
		AdminUserModel adminUser = (AdminUserModel) req.getSession().getAttribute("currentUser");
		String phone = "_";
		if (adminUser.getType() == Constant.AdminUserType.StrategicPartner.getVal()) {
			phone = adminUser.getUsername();
		}else if (adminUser.getType() == Constant.AdminUserType.Normal.getVal()) {
			phone = null;
		}
		Long id = 0l;
		if(StringUtils.isNotBlank(idParam)) {
			id = Long.valueOf(idParam);
		}
		Map<String,Object> result = strategicPartnerServie.getStrategicPartnerListPage(id,name,contactsPhone,phone, stime, etime, orderBy,
				orderSort, page, rows);
		return JSONObject.toJSONString(result);
	}

	@RequestMapping(value = "/getStrategicPartnerById", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getStrategicPartnerById(HttpServletRequest res) {
		Long id = Long.valueOf(res.getParameter("id"));
		StrategicPartner bean = strategicPartnerServie.getStrategicPartnerById(id);
		return JSONObject.toJSONString(bean);
	}

	@RequestMapping(value = "/saveStrategicPartner", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String saveStrategicPartner(HttpServletRequest request, StrategicPartner strategicPartner,
			String oldContactsPhone, String password) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		Map<String, Object> resultMap = new HashMap<String, Object>();

		strategicPartner.setCreateTime(new Date().getTime());
		strategicPartner.setCreateUser(Long.valueOf(adminUser.getUid()));

		resultMap = strategicPartnerServie.saveStrategicPartner(strategicPartner, password);
		return JSONObject.toJSONString(resultMap);
	}

	@RequestMapping(value = "/updateStrategicPartner", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateStrategicPartner(HttpServletRequest request, StrategicPartner strategicPartner,
			String oldContactsPhone, String password) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		strategicPartner.setModifyTime(new Date().getTime());
		strategicPartner.setModifyUser(Long.valueOf(adminUser.getUid()));
		resultMap = strategicPartnerServie.updateStrategicPartner(strategicPartner, oldContactsPhone, password);
		return JSONObject.toJSONString(resultMap);
	}

	@RequestMapping(value = "/delStrategicPartner", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String delStrategicPartner(HttpServletRequest request) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Long id = Long.valueOf(request.getParameter("id"));
		resultMap = strategicPartnerServie.delStrategicPartner(id, adminUser);
		return JSONObject.toJSONString(resultMap);
	}
}
