package com.tinypig.newadmin.web.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.util.Constant;
import com.tinypig.admin.util.ExcelExportData;
import com.tinypig.admin.util.ExcelUtil;
import com.tinypig.newadmin.common.ResponseUtils;
import com.tinypig.newadmin.web.entity.AdminMenu;
import com.tinypig.newadmin.web.entity.AdminRole;
import com.tinypig.newadmin.web.entity.AgentUser;
import com.tinypig.newadmin.web.entity.ExtensionCenter;
import com.tinypig.newadmin.web.entity.Promoters;
import com.tinypig.newadmin.web.entity.Salesman;
import com.tinypig.newadmin.web.entity.StrategicPartner;
import com.tinypig.newadmin.web.entity.UserTransactionHisSta;
import com.tinypig.newadmin.web.entity.UserTransactionHisStaParam;
import com.tinypig.newadmin.web.service.IAdminMenuService;
import com.tinypig.newadmin.web.service.IAdminRoleService;
import com.tinypig.newadmin.web.service.IAgentUserServie;
import com.tinypig.newadmin.web.service.IExtensionCenterServie;
import com.tinypig.newadmin.web.service.IPromotersServie;
import com.tinypig.newadmin.web.service.ISalesmanServie;
import com.tinypig.newadmin.web.service.IStrategicPartnerServie;
import com.tinypig.newadmin.web.service.UserTransactionHisStaService;

@Controller
@RequestMapping("/userTransactionHisSta")
public class UserTransactionHisStaAction {

	private static Logger log = Logger.getLogger(UserTransactionHisStaAction.class);

	@Autowired
	private UserTransactionHisStaService userTransactionHisStaService;

	@Autowired
	private IStrategicPartnerServie strategicPartnerServie;

	@Resource
	private ISalesmanServie salesmanServie;

	@Resource
	private IAgentUserServie agentUserServie;

	@Resource
	private IPromotersServie promotersServie;

	@Resource
	private IAdminMenuService adminMenuService;

	@Resource
	private IAdminRoleService adminRoleService;

	@Resource
	private IExtensionCenterServie extensionCenterServie;

	@RequestMapping(value = "/pageList", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String pageList(HttpServletRequest request, UserTransactionHisStaParam param) {
		long logSTime = System.currentTimeMillis();
		log.info("========统计信息列表=======START");
		
		Integer page = Integer.valueOf(request.getParameter("page"));
		Integer rows = Integer.valueOf(request.getParameter("rows"));
		Integer startIndex = (page - 1) * rows;
		param.setStartIndex(startIndex);
		param.setPageSize(rows);
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		if (adminUser.getType() == Constant.AdminUserType.StrategicPartner.getVal()) {
			StrategicPartner strategicPartner = strategicPartnerServie
					.getStrategicPartnerByPhone(adminUser.getUsername());
			param.setStrategicPartnerId(strategicPartner.getId());
		} else if (adminUser.getType() == Constant.AdminUserType.ExtensionCenter.getVal()) {
			ExtensionCenter extensionCenter = extensionCenterServie.getExtensionCenterByPhone(adminUser.getUsername());
			param.setExtensionCenterId(extensionCenter.getId());
		} else if (adminUser.getType() == Constant.AdminUserType.Promoters.getVal()) {
			Promoters promoters = promotersServie.getPromotersByPhone(adminUser.getUsername());
			param.setPromotersId(promoters.getId());
		} else if (adminUser.getType() == Constant.AdminUserType.AgentUser.getVal()) {
			AgentUser agentUser = agentUserServie.getAgentUserByPhone(adminUser.getUsername());
			param.setAgentUserId(agentUser.getId());
		} else if (adminUser.getType() == Constant.AdminUserType.Salesman.getVal()) {
			Salesman salesman = salesmanServie.getSalesmanByPhone(adminUser.getUsername());
			param.setSalesmanId(salesman.getId());
		}
		if(StringUtils.isNotBlank(request.getParameter("showDataTypeEq1"))) {
			param.setDataType(1); //只显示真实数据
		}
		Map<String, Object> resultMap = userTransactionHisStaService.getUserTransactionHisListPage(param);
		long logETime = System.currentTimeMillis();
		log.info("========统计信息列表=======END("+(logETime-logSTime)+")ss");
		return JSONObject.toJSONString(resultMap);
	}

	@RequestMapping(value = "/getUserTransactionHisTotal")
	@ResponseBody
	public Map<String, Object> getUserTransactionHisTotal(HttpServletRequest request,
			UserTransactionHisStaParam param) {
		long logSTime = System.currentTimeMillis();
		log.info("========统计信息合计=======START");
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		if (adminUser.getType() == Constant.AdminUserType.StrategicPartner.getVal()) {
			StrategicPartner strategicPartner = strategicPartnerServie
					.getStrategicPartnerByPhone(adminUser.getUsername());
			param.setStrategicPartnerId(strategicPartner.getId());
		} else if (adminUser.getType() == Constant.AdminUserType.ExtensionCenter.getVal()) {
			ExtensionCenter extensionCenter = extensionCenterServie.getExtensionCenterByPhone(adminUser.getUsername());
			param.setExtensionCenterId(extensionCenter.getId());
		} else if (adminUser.getType() == Constant.AdminUserType.Promoters.getVal()) {
			Promoters promoters = promotersServie.getPromotersByPhone(adminUser.getUsername());
			param.setPromotersId(promoters.getId());
		} else if (adminUser.getType() == Constant.AdminUserType.AgentUser.getVal()) {
			AgentUser agentUser = agentUserServie.getAgentUserByPhone(adminUser.getUsername());
			param.setAgentUserId(agentUser.getId());
		} else if (adminUser.getType() == Constant.AdminUserType.Salesman.getVal()) {
			Salesman salesman = salesmanServie.getSalesmanByPhone(adminUser.getUsername());
			param.setSalesmanId(salesman.getId());
		}
		if(StringUtils.isNotBlank(request.getParameter("showDataTypeEq1"))) {
			param.setDataType(1); //只显示真实数据
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", userTransactionHisStaService.getUserTransactionHisTotal(param));
		long logETime = System.currentTimeMillis();
		log.info("========统计信息合计=======END("+(logETime-logSTime)+")ss");
		return result;
	}

	@RequestMapping(value = "/expExcel")
	public void expExcel(HttpServletRequest request, HttpServletResponse response, UserTransactionHisStaParam param) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		if (adminUser.getType() == Constant.AdminUserType.StrategicPartner.getVal()) {
			StrategicPartner strategicPartner = strategicPartnerServie
					.getStrategicPartnerByPhone(adminUser.getUsername());
			param.setStrategicPartnerId(strategicPartner.getId());
		} else if (adminUser.getType() == Constant.AdminUserType.ExtensionCenter.getVal()) {
			ExtensionCenter extensionCenter = extensionCenterServie.getExtensionCenterByPhone(adminUser.getUsername());
			param.setExtensionCenterId(extensionCenter.getId());
		} else if (adminUser.getType() == Constant.AdminUserType.Promoters.getVal()) {
			Promoters promoters = promotersServie.getPromotersByPhone(adminUser.getUsername());
			param.setPromotersId(promoters.getId());
		} else if (adminUser.getType() == Constant.AdminUserType.AgentUser.getVal()) {
			AgentUser agentUser = agentUserServie.getAgentUserByPhone(adminUser.getUsername());
			param.setAgentUserId(agentUser.getId());
		} else if (adminUser.getType() == Constant.AdminUserType.Salesman.getVal()) {
			Salesman salesman = salesmanServie.getSalesmanByPhone(adminUser.getUsername());
			param.setSalesmanId(salesman.getId());
		}
		if(StringUtils.isNotBlank(request.getParameter("showDataTypeEq1"))) {
			param.setDataType(1); //只显示真实数据
		}
		List<UserTransactionHisSta> list = userTransactionHisStaService.getUserTransactionHisList(param);
		exportExcelData(list, "统计信息", adminUser, request, response);
	}

	/**
	 * 生成excel并下载
	 * 
	 * @param operateList
	 * @param date
	 * @param response
	 */
	private void exportExcelData(List list, String title, AdminUserModel adminUser, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			ExcelExportData info = new ExcelExportData();
			List<String[]> columnNames = new ArrayList<String[]>();

			/****************权限控制信息**************/
			boolean hasSalesmanAuth = haveAuth("userTransactionHisSta:list-salesmanName", adminUser);
			boolean hasAgentUserAuth = haveAuth("userTransactionHisSta:list-agentUserName", adminUser);
			boolean hasPromotersAuth = haveAuth("userTransactionHisSta:list-promotersName", adminUser);
			boolean hasExtensionCenterAuth = haveAuth("userTransactionHisSta:list-extensionCenterName", adminUser);
			boolean hasStrategicPartnerAuth = haveAuth("userTransactionHisSta:list-strategicPartnerName", adminUser);
			boolean hasDataType = haveAuth("userTransactionHisSta:dataType", adminUser);

			// 列
			String[] cols = new String[] { //
					"用户UID", "用户昵称", "类型", "金额", "金币", "时间"//
			};
			List<String> colsList = new ArrayList<String>();
			/****************基础信息***********/
			colsList.addAll(Arrays.asList(cols));

			// 家族助理
			if (hasSalesmanAuth) {
				colsList.add("所属家族助理姓名");
				colsList.add("所属家族助理电话");
			}

			// 黄金公会
			if (hasAgentUserAuth) {
				colsList.add("所属黄金公会名称");
				colsList.add("所属黄金公会联系人姓名");
				colsList.add("所属黄金公会联系电话");
			}

			// 铂金公会
			if (hasPromotersAuth) {
				colsList.add("所属铂金公会名称");
				colsList.add("所属铂金公会联系人姓名");
				colsList.add("所属铂金公会联系电话");
			}

			// 钻石公会
			if (hasExtensionCenterAuth) {
				colsList.add("所属钻石公会名称");
				colsList.add("所属钻石公会联系人姓名");
				colsList.add("所属钻石公会联系电话");
			}

			// 星耀公会
			if (hasStrategicPartnerAuth) {
				colsList.add("所属星耀公会名称");
				colsList.add("所属星耀公会联系人姓名");
				colsList.add("所属星耀公会联系电话");
			}
			
			if(hasDataType) {
				colsList.add("数据类型");
			}
			columnNames.add(colsList.toArray(new String[colsList.size()]));
			info.setColumnNames(columnNames);
			// 数据
			LinkedHashMap<String, List<?>> dataMap = new LinkedHashMap<String, List<?>>();
			dataMap.put(title, list);
			info.setDataMap(dataMap);

			// 对象属性名称
			List<String[]> fieldNames = new ArrayList<String[]>();

			List<String> fieldNameList = new ArrayList<>();
			/****************基础信息(数据字段)***********/
			String[] fieldNameTwo = new String[] { //
					"uid", "nickname", "transTypeStr", "amount", "money", "createTimeStr"//
			};

			/****************权限控制信息(数据字段)**************/
			fieldNameList.addAll(Arrays.asList(fieldNameTwo));

			// 家族助理
			if (hasSalesmanAuth) {
				fieldNameList.add("salesmanName");
				fieldNameList.add("salesmanContactsPhone");
			}

			// 黄金公会
			if (hasAgentUserAuth) {
				fieldNameList.add("agentUserName");
				fieldNameList.add("agentUserContactsName");
				fieldNameList.add("agentUserContactsPhone");
			}

			// 铂金公会
			if (hasPromotersAuth) {
				fieldNameList.add("promotersName");
				fieldNameList.add("promotersContactsName");
				fieldNameList.add("promotersContactsPhone");
			}

			// 钻石公会
			if (hasExtensionCenterAuth) {
				fieldNameList.add("extensionCenterName");
				fieldNameList.add("extensionCenterContactsName");
				fieldNameList.add("extensionCenterContactsPhone");
			}

			// 星耀公会
			if (hasStrategicPartnerAuth) {
				fieldNameList.add("strategicPartnerName");
				fieldNameList.add("strategicPartnerContactsName");
				fieldNameList.add("strategicPartnerContactsPhone");
			}
			if(hasDataType) {
				fieldNameList.add("dataTypeStr");
			}
			fieldNames.add(fieldNameList.toArray(new String[fieldNameList.size()]));
			info.setFieldNames(fieldNames);
			// 页签名称
			info.setTitles(new String[] { title });
			byte[] export2ByteArray = ExcelUtil.getInstance().export2ByteArray(info);
			ResponseUtils.download(request, response, export2ByteArray, title + ".xls");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private boolean haveAuth(String action, AdminUserModel adminUser) {
		if (null != adminUser) {
			String username = adminUser.getUsername();
			if (StringUtils.equals("admin", username)) {
				return true;
			}
			byte roleId = adminUser.getRole_id();
			AdminMenu adminMenu = adminMenuService.findByUrl(action);
			if (null != adminMenu) {
				String mid = adminMenu.getMid() + "";
				AdminRole adminRole = adminRoleService.findById(roleId);
				if (null != adminRole) {
					String menuIds = adminRole.getMenuIds();
					String[] splits = StringUtils.split(menuIds, ",");
					if (ArrayUtils.indexOf(splits, mid) > -1) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
