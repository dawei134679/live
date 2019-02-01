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
import com.tinypig.newadmin.web.entity.AgentUser;
import com.tinypig.newadmin.web.entity.AgentUserDto;
import com.tinypig.newadmin.web.entity.ExtensionCenter;
import com.tinypig.newadmin.web.entity.Promoters;
import com.tinypig.newadmin.web.entity.StrategicPartner;
import com.tinypig.newadmin.web.service.IAgentUserServie;
import com.tinypig.newadmin.web.service.IExtensionCenterServie;
import com.tinypig.newadmin.web.service.IPromotersServie;
import com.tinypig.newadmin.web.service.IStrategicPartnerServie;

/**
 * 黄金公会
 */
@Controller
@RequestMapping("/agentUser")
public class AgentUserController {

	@Resource
	private IAgentUserServie agentUserServie;

	@Resource
	private IPromotersServie promotersServie;

	@Resource
	private IExtensionCenterServie extensionCenterServie;

	@Resource
	private IStrategicPartnerServie strategicPartnerServie;

	@RequestMapping(value = "/getAgentUserList", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getAgentUserList(HttpServletRequest req) {
		String idParam = req.getParameter("id");
		String name = req.getParameter("name");
		String promotersIdParam = req.getParameter("promotersId");
		String strategicPartnerIdParam = req.getParameter("strategicPartnerId");
		String extensionCenterIdParam = req.getParameter("extensionCenterId");
		String contactsPhone = req.getParameter("contactsPhone");
		Integer page = Integer.valueOf(req.getParameter("page"));
		Integer rows = Integer.valueOf(req.getParameter("rows"));
		String strategicPartnerName = req.getParameter("strategicPartnerName");
		String extensionCenterName = req.getParameter("extensionCenterName");
		String promotersName = req.getParameter("promotersName");
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
		Long strategicPartnerId = 0l, extensionCenterId = 0l, promotersId = 0l, agentUserId = 0l;
		if (adminUser.getType() == Constant.AdminUserType.StrategicPartner.getVal()) {
			StrategicPartner strategicPartner = strategicPartnerServie.getStrategicPartnerByPhone(adminUser.getUsername());
			strategicPartnerId = strategicPartner.getId();
		} else if (adminUser.getType() == Constant.AdminUserType.ExtensionCenter.getVal()) {
			ExtensionCenter extensionCenter = extensionCenterServie.getExtensionCenterByPhone(adminUser.getUsername());
			extensionCenterId = extensionCenter.getId();
		} else if (adminUser.getType() == Constant.AdminUserType.Promoters.getVal()) {
			Promoters promoters = promotersServie.getPromotersByPhone(adminUser.getUsername());
			promotersId = promoters.getId();
		} else if (adminUser.getType() == Constant.AdminUserType.AgentUser.getVal()) {
			AgentUser agentUser = agentUserServie.getAgentUserByPhone(adminUser.getUsername());
			agentUserId = agentUser.getId();
		} else if (adminUser.getType() == Constant.AdminUserType.Normal.getVal()) {
			extensionCenterId = null;
		}

		if (strategicPartnerId == null || strategicPartnerId == 0) {
			if (StringUtils.isNotBlank(strategicPartnerIdParam)) {
				strategicPartnerId = Long.parseLong(strategicPartnerIdParam);
			}
		}

		if (extensionCenterId == null || extensionCenterId == 0) {
			if (StringUtils.isNotBlank(extensionCenterIdParam)) {
				extensionCenterId = Long.parseLong(extensionCenterIdParam);
			}
		}

		if (promotersId == null || promotersId == 0) {
			if (StringUtils.isNotBlank(promotersIdParam)) {
				promotersId = Long.parseLong(promotersIdParam);
			}
		}

		if (agentUserId == null || agentUserId == 0) {
			if (StringUtils.isNotBlank(idParam)) {
				agentUserId = Long.parseLong(idParam);
			}
		}

		Map<String,Object> result = agentUserServie.getAgentUserListPage(name, contactsPhone, strategicPartnerId,
				extensionCenterId, promotersId, agentUserId, strategicPartnerName, extensionCenterName, promotersName,
				stime, etime, orderBy, orderSort, page, rows);
		return JSONObject.toJSONString(result);
	}

	@RequestMapping(value = "/saveAgentUser", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String saveAgentUser(HttpServletRequest request, AgentUser agentUser, String password) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		Map<String, Object> resultMap = new HashMap<String, Object>();

		agentUser.setCreateTime(new Date().getTime());
		agentUser.setCreateUser(Long.valueOf(adminUser.getUid()));

		resultMap = agentUserServie.saveAgentUser(agentUser, password);
		return JSONObject.toJSONString(resultMap);
	}

	@RequestMapping(value = "/updateAgentUser", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateAgentUser(HttpServletRequest request, AgentUser agentUser, String oldContactsPhone,
			String password) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		agentUser.setModifyTime(new Date().getTime());
		agentUser.setModifyUser(Long.valueOf(adminUser.getUid()));
		resultMap = agentUserServie.updateAgentUser(agentUser, oldContactsPhone, password);
		return JSONObject.toJSONString(resultMap);
	}

	@RequestMapping(value = "/delAgentUser", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String delAgentUser(HttpServletRequest request) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Long id = Long.valueOf(request.getParameter("id"));
		resultMap = agentUserServie.delAgentUser(id, adminUser);
		return JSONObject.toJSONString(resultMap);
	}

	/**
	 * 获取钻石公会ID
	 * 如果当前登录后台的用户是铂金公会类型的用户，则可以取到，如果不是铂金公会类型，则反回0
	 */
	@RequestMapping(value = "/getPromoters", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getPromoters(HttpServletRequest request) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		Map<String, Object> resultMap = agentUserServie.getPromoters(adminUser);
		return JSONObject.toJSONString(resultMap);
	}
}
