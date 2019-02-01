package com.tinypig.newadmin.web.action;

import java.util.ArrayList;
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
import com.tinypig.newadmin.web.entity.ExtensionCenter;
import com.tinypig.newadmin.web.entity.Promoters;
import com.tinypig.newadmin.web.entity.Salesman;
import com.tinypig.newadmin.web.entity.StrategicPartner;
import com.tinypig.newadmin.web.service.IAgentUserServie;
import com.tinypig.newadmin.web.service.IExtensionCenterServie;
import com.tinypig.newadmin.web.service.IPromotersServie;
import com.tinypig.newadmin.web.service.ISalesmanServie;
import com.tinypig.newadmin.web.service.IStrategicPartnerServie;

@Controller
@RequestMapping("/salesman")
public class SalesmanController {

	@Resource
	private ISalesmanServie salesmanServie;

	@Resource
	private IAgentUserServie agentUserServie;

	@Resource
	private IPromotersServie promotersServie;

	@Resource
	private IExtensionCenterServie extensionCenterServie;

	@Resource
	private IStrategicPartnerServie strategicPartnerServie;

	@RequestMapping(value = "/getSalesmanList", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getSalesmanList(HttpServletRequest req) {
		String idParam = req.getParameter("id");
		String name = req.getParameter("name");
		String agentUserIdParam = req.getParameter("agentUserId");
		String promotersIdParam = req.getParameter("promotersId");
		String strategicPartnerIdParam = req.getParameter("strategicPartnerId");
		String extensionCenterIdParam = req.getParameter("extensionCenterId");
		String contactsPhone = req.getParameter("contactsPhone");
		Integer page = Integer.valueOf(req.getParameter("page"));
		Integer rows = Integer.valueOf(req.getParameter("rows"));
		String strategicPartnerName = req.getParameter("strategicPartnerName");
		String extensionCenterName = req.getParameter("extensionCenterName");
		String promotersName = req.getParameter("promotersName");
		String agentUserName = req.getParameter("agentUserName");
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
		Long strategicPartnerId = 0l,extensionCenterId = 0l, promotersId = 0l, agentUserId = 0l, salesmanId = 0l;
		if (adminUser.getType() == Constant.AdminUserType.StrategicPartner.getVal()) {
			StrategicPartner strategicPartner = strategicPartnerServie.getStrategicPartnerByPhone(adminUser.getUsername());
			strategicPartnerId = strategicPartner.getId();
		}else if (adminUser.getType() == Constant.AdminUserType.ExtensionCenter.getVal()) {
			ExtensionCenter extensionCenter = extensionCenterServie.getExtensionCenterByPhone(adminUser.getUsername());
			extensionCenterId = extensionCenter.getId();
		} else if (adminUser.getType() == Constant.AdminUserType.Promoters.getVal()) {
			Promoters promoters = promotersServie.getPromotersByPhone(adminUser.getUsername());
			promotersId = promoters.getId();
		} else if (adminUser.getType() == Constant.AdminUserType.AgentUser.getVal()) {
			AgentUser agentUser = agentUserServie.getAgentUserByPhone(adminUser.getUsername());
			agentUserId = agentUser.getId();
		} else if (adminUser.getType() == Constant.AdminUserType.Salesman.getVal()) {
			Salesman salesman = salesmanServie.getSalesmanByPhone(adminUser.getUsername());
			salesmanId = salesman.getId();
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
			if(StringUtils.isNotBlank(promotersIdParam)) {
				promotersId = Long.parseLong(promotersIdParam);
			}
		}
		
		if(agentUserId == null || agentUserId == 0) {
			if(StringUtils.isNotBlank(agentUserIdParam)) {
				agentUserId = Long.parseLong(agentUserIdParam);
			}
		}
		
		if(salesmanId == null || salesmanId == 0) {
			if(StringUtils.isNotBlank(idParam)) {
				salesmanId = Long.parseLong(idParam);
			}
		}
		
		Map<String,Object> result = salesmanServie.getSalesmanListPage(name,contactsPhone, strategicPartnerId,extensionCenterId, promotersId, agentUserId,
				salesmanId, strategicPartnerName,extensionCenterName, promotersName, agentUserName, stime, etime, orderBy, orderSort, page,
				rows);
		return JSONObject.toJSONString(result);
	}

	@RequestMapping(value = "/saveSalesman", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String saveSalesman(HttpServletRequest request, Salesman salesman, String password) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		Map<String, Object> resultMap = new HashMap<String, Object>();

		salesman.setCreateTime(new Date().getTime());
		salesman.setCreateUser(Long.valueOf(adminUser.getUid()));

		try {
			resultMap = salesmanServie.saveSalesman(salesman, password);
		} catch (Exception e) {
			resultMap.put("msg", "添加失败");
			resultMap.put("code", false);
		}
		return JSONObject.toJSONString(resultMap);
	}

	@RequestMapping(value = "/updateSalesman", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateSalesman(HttpServletRequest request, Salesman salesman, String oldContactsPhone,
			String password) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		salesman.setModifyTime(new Date().getTime());
		salesman.setModifyUser(Long.valueOf(adminUser.getUid()));
		resultMap = salesmanServie.updateSalesman(salesman, oldContactsPhone, password);
		return JSONObject.toJSONString(resultMap);
	}

	@RequestMapping(value = "/delSalesman", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String delSalesman(HttpServletRequest request) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Long id = Long.valueOf(request.getParameter("id"));
		resultMap = salesmanServie.delSalesman(id, adminUser);
		return JSONObject.toJSONString(resultMap);
	}

	@RequestMapping(value = "/reCreateQrcode", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String reCreateQrcode(HttpServletRequest request,String ids) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String[] split = StringUtils.split(ids,",");
		int a = 0;
		for (String idstr : split) {
			Map<String, Object> res = salesmanServie.reCreateQrcode(Long.parseLong(idstr), adminUser);
			if(((Integer)res.get("code")).intValue() == 200) {
				a++;
			}
		}
		resultMap.put("code", 200);
		resultMap.put("msg", "操作成功，【"+a+"】条数据受影响！");
		return JSONObject.toJSONString(resultMap);
	}
	
	@RequestMapping(value = "/invalidQrcode", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String invalidQrcode(HttpServletRequest request,String ids) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String[] split = StringUtils.split(ids,",");
		List<Long> idsList = new ArrayList<Long>();
		for (String idstr : split) {
			idsList.add(Long.parseLong(idstr));
		}
		int res = salesmanServie.invalidQrcode(idsList, adminUser);
		if(res == 1) {
			resultMap.put("code", 200);
			resultMap.put("msg", "操作成功");
		}else {
			resultMap.put("code", 201);
			resultMap.put("msg", "操作失败");
		}
		return JSONObject.toJSONString(resultMap);
	}

	/**
	 * 获取黄金公会ID
	 * 如果当前登录后台的用户是黄金公会类型的用户，则可以取到，如果不是黄金公会类型，则反回0
	 */
	@RequestMapping(value = "/getAgentUser", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getAgentUser(HttpServletRequest request) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		Map<String, Object> resultMap = salesmanServie.getAgentUser(adminUser);
		return JSONObject.toJSONString(resultMap);
	}
}
