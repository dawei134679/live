package com.tinypig.newadmin.web.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.tinypig.newadmin.web.entity.ExtensionCenter;
import com.tinypig.newadmin.web.entity.StrategicPartner;
import com.tinypig.newadmin.web.service.IExtensionCenterServie;
import com.tinypig.newadmin.web.service.IStrategicPartnerServie;

/**
 * 钻石公会
 */
@Controller
@RequestMapping("/extensionCenter")
public class ExtensionCenterController {

	@Resource
	private IExtensionCenterServie extensionCenterServie;

	@Resource
	private IStrategicPartnerServie strategicPartnerServie;

	@RequestMapping(value = "/getExtensionCenterList", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getExtensionCenterList(HttpServletRequest req) {
		String idParam = req.getParameter("id");
		String name = req.getParameter("name");
		String strategicPartnerIdParam = req.getParameter("strategicPartnerId");
		String strategicPartnerName = req.getParameter("strategicPartnerName");
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
		Long extensionCenterId = 0l, strategicPartnerId = 0l;
		if (adminUser.getType() == Constant.AdminUserType.StrategicPartner.getVal()) {
			StrategicPartner strategicPartner = strategicPartnerServie.getStrategicPartnerByPhone(adminUser.getUsername());
			strategicPartnerId = strategicPartner.getId();
		}else if (adminUser.getType() == Constant.AdminUserType.ExtensionCenter.getVal()) {
			ExtensionCenter extensionCenter = extensionCenterServie.getExtensionCenterByPhone(adminUser.getUsername());
			extensionCenterId = extensionCenter.getId();
		}else if (adminUser.getType() == Constant.AdminUserType.Normal.getVal()) {
			strategicPartnerId = null;
		}
		

		if(strategicPartnerId == null || strategicPartnerId == 0) {
			if(StringUtils.isNotBlank(strategicPartnerIdParam)) {
				strategicPartnerId = Long.parseLong(strategicPartnerIdParam);
			}
		}
		if(extensionCenterId == null || extensionCenterId == 0) {
			if(StringUtils.isNotBlank(idParam)) {
				extensionCenterId = Long.valueOf(idParam);
			}
		}
		
		Map<String,Object> result = extensionCenterServie.getExtensionCenterListPage(extensionCenterId,name,contactsPhone,strategicPartnerId,strategicPartnerName,stime, etime, orderBy,
				orderSort, page, rows);
		return JSONObject.toJSONString(result);
	}

	@RequestMapping(value = "/getExtensionCenterById", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getExtensionCenterById(HttpServletRequest res) {
		Long id = Long.valueOf(res.getParameter("id"));
		ExtensionCenter bean = extensionCenterServie.getExtensionCenterById(id);
		return JSONObject.toJSONString(bean);
	}

	@RequestMapping(value = "/saveExtensionCenter", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String saveExtensionCenter(HttpServletRequest request, ExtensionCenter extensionCenter,
			String oldContactsPhone, String password) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		Map<String, Object> resultMap = new HashMap<String, Object>();

		extensionCenter.setCreateTime(new Date().getTime());
		extensionCenter.setCreateUser(Long.valueOf(adminUser.getUid()));

		resultMap = extensionCenterServie.saveExtensionCenter(extensionCenter, password);
		return JSONObject.toJSONString(resultMap);
	}

	@RequestMapping(value = "/updateExtensionCenter", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateExtensionCenter(HttpServletRequest request, ExtensionCenter extensionCenter,
			String oldContactsPhone, String password) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		extensionCenter.setModifyTime(new Date().getTime());
		extensionCenter.setModifyUser(Long.valueOf(adminUser.getUid()));
		resultMap = extensionCenterServie.updateExtensionCenter(extensionCenter, oldContactsPhone, password);
		return JSONObject.toJSONString(resultMap);
	}

	@RequestMapping(value = "/delExtensionCenter", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String delExtensionCenter(HttpServletRequest request) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Long id = Long.valueOf(request.getParameter("id"));
		resultMap = extensionCenterServie.delExtensionCenter(id, adminUser);
		return JSONObject.toJSONString(resultMap);
	}
	
	/**
	 * 获取钻石公会ID
	 * 如果当前登录后台的用户是钻石公会类型的用户，则可以取到，如果不是钻石公会类型，则反回0
	 */
	@RequestMapping(value = "/getStrategicPartner", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getExtensionCenter(HttpServletRequest request) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		Map<String, Object> resultMap = strategicPartnerServie.getStrategicPartner(adminUser);
		return JSONObject.toJSONString(resultMap);
	}
}
