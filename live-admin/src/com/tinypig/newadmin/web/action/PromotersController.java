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
import com.tinypig.newadmin.web.entity.ExtensionCenter;
import com.tinypig.newadmin.web.entity.Promoters;
import com.tinypig.newadmin.web.entity.StrategicPartner;
import com.tinypig.newadmin.web.service.IExtensionCenterServie;
import com.tinypig.newadmin.web.service.IPromotersServie;
import com.tinypig.newadmin.web.service.IStrategicPartnerServie;

@Controller
@RequestMapping("/promoters")
public class PromotersController {

	@Resource
	private IPromotersServie promotersServie;

	@Resource
	private IExtensionCenterServie extensionCenterServie;

	@Resource
	private IStrategicPartnerServie strategicPartnerServie;

	@RequestMapping(value = "/getPromotersList", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getPromotersList(HttpServletRequest req) {
		String idParam = req.getParameter("id");
		String name = req.getParameter("name");
		String contactsPhone = req.getParameter("contactsPhone");
		String strategicPartnerIdParam = req.getParameter("strategicPartnerId");
		String extensionCenterIdParam = req.getParameter("extensionCenterId");
		Integer page = Integer.valueOf(req.getParameter("page"));
		Integer rows = Integer.valueOf(req.getParameter("rows"));
		String strategicPartnerName = req.getParameter("strategicPartnerName");
		String extensionCenterName = req.getParameter("extensionCenterName");
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
		Long strategicPartnerId = 0l, extensionCenterId = 0l,promotersId = 0l;
		if (adminUser.getType() == Constant.AdminUserType.StrategicPartner.getVal()) {
			StrategicPartner strategicPartner = strategicPartnerServie.getStrategicPartnerByPhone(adminUser.getUsername());
			strategicPartnerId = strategicPartner.getId();
		}else if (adminUser.getType() == Constant.AdminUserType.ExtensionCenter.getVal()) {
			ExtensionCenter extensionCenter = extensionCenterServie.getExtensionCenterByPhone(adminUser.getUsername());
			extensionCenterId = extensionCenter.getId();
		} else if (adminUser.getType() == Constant.AdminUserType.Promoters.getVal()) {
			Promoters promoters = promotersServie.getPromotersByPhone(adminUser.getUsername());
			promotersId = promoters.getId();
		} else if (adminUser.getType() == Constant.AdminUserType.Normal.getVal()) {
			extensionCenterId = null;
		}
		
		if(strategicPartnerId == null || strategicPartnerId == 0) {
			if(StringUtils.isNotBlank(strategicPartnerIdParam)) {
				strategicPartnerId = Long.parseLong(strategicPartnerIdParam);
			}
		}
		
		if(extensionCenterId == null || extensionCenterId == 0) {
			if(StringUtils.isNotBlank(extensionCenterIdParam)) {
				extensionCenterId = Long.parseLong(extensionCenterIdParam);
			}
		}
		
		if(promotersId == null || promotersId == 0) {
			if(StringUtils.isNotBlank(idParam)) {
				promotersId = Long.parseLong(idParam);
			}
		}
		
		Map<String,Object> result = promotersServie.getPromotersListPage(name,contactsPhone,strategicPartnerId, extensionCenterId,promotersId, strategicPartnerName,extensionCenterName, stime,
				etime, orderBy, orderSort, page, rows);
		return JSONObject.toJSONString(result);
	}

	@RequestMapping(value = "/savePromoters", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String savePromoters(HttpServletRequest request, Promoters promoters, String password) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		Map<String, Object> resultMap = new HashMap<String, Object>();

		promoters.setCreateTime(new Date().getTime());
		promoters.setCreateUser(Long.valueOf(adminUser.getUid()));

		resultMap = promotersServie.savePromoters(promoters, password);
		return JSONObject.toJSONString(resultMap);
	}

	@RequestMapping(value = "/updatePromoters", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updatePromoters(HttpServletRequest request, Promoters promoters, String oldContactsPhone,
			String password) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		promoters.setModifyTime(new Date().getTime());
		promoters.setModifyUser(Long.valueOf(adminUser.getUid()));
		resultMap = promotersServie.updatePromoters(promoters, oldContactsPhone, password);
		return JSONObject.toJSONString(resultMap);
	}

	@RequestMapping(value = "/delPromoters", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String delPromoters(HttpServletRequest request) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Long id = Long.valueOf(request.getParameter("id"));
		resultMap = promotersServie.delPromoters(id, adminUser);
		return JSONObject.toJSONString(resultMap);
	}

	/**
	 * 获取钻石公会ID
	 * 如果当前登录后台的用户是钻石公会类型的用户，则可以取到，如果不是钻石公会类型，则反回0
	 */
	@RequestMapping(value = "/getExtensionCenter", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getExtensionCenter(HttpServletRequest request) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		Map<String, Object> resultMap = promotersServie.getExtensionCenter(adminUser);
		return JSONObject.toJSONString(resultMap);
	}
}
