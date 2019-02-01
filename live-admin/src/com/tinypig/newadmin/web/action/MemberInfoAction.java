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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.util.Constant;
import com.tinypig.admin.util.ExcelExportData;
import com.tinypig.admin.util.ExcelUtil;
import com.tinypig.admin.util.QiniuUpUtils;
import com.tinypig.newadmin.common.ConstantsAction;
import com.tinypig.newadmin.common.ResponseUtils;
import com.tinypig.newadmin.web.entity.AdminMenu;
import com.tinypig.newadmin.web.entity.AdminRole;
import com.tinypig.newadmin.web.entity.AgentUser;
import com.tinypig.newadmin.web.entity.ExtensionCenter;
import com.tinypig.newadmin.web.entity.MemberInfo;
import com.tinypig.newadmin.web.entity.MemberInfoParentParamDto;
import com.tinypig.newadmin.web.entity.MemberParamDto;
import com.tinypig.newadmin.web.entity.Promoters;
import com.tinypig.newadmin.web.entity.Salesman;
import com.tinypig.newadmin.web.entity.StrategicPartner;
import com.tinypig.newadmin.web.entity.UserOrgRelation;
import com.tinypig.newadmin.web.service.IAdminMenuService;
import com.tinypig.newadmin.web.service.IAdminRoleService;
import com.tinypig.newadmin.web.service.IAgentUserServie;
import com.tinypig.newadmin.web.service.IExtensionCenterServie;
import com.tinypig.newadmin.web.service.IPromotersServie;
import com.tinypig.newadmin.web.service.ISalesmanServie;
import com.tinypig.newadmin.web.service.IStrategicPartnerServie;
import com.tinypig.newadmin.web.service.MemberInfoService;

@Controller
@RequestMapping("/member")
public class MemberInfoAction {
	private static Logger log = Logger.getLogger(MemberInfoAction.class);

	@Autowired
	private MemberInfoService memberInfoService;

	@Autowired
	private IStrategicPartnerServie strategicPartnerService;

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

	@RequestMapping(value = "/getMemberList", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getMemberListPage(HttpServletRequest request, MemberParamDto param) {
		long logSTime = System.currentTimeMillis();
		log.info("========会员列表=======START");
		Integer page = Integer.valueOf(request.getParameter("page"));
		Integer rows = Integer.valueOf(request.getParameter("rows"));
		Integer startIndex = (page - 1) * rows;
		param.setStartIndex(startIndex);
		param.setPageSize(rows);
		if(StringUtils.isEmpty(param.getSort())) {
			param.setSort("registtime");
			if(StringUtils.isEmpty(param.getOrder())) {
				param.setOrder("desc");
			}
		}
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		if (adminUser.getType() == Constant.AdminUserType.StrategicPartner.getVal()) {
			StrategicPartner strategicPartner = strategicPartnerService
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
		String jsonString = JSONObject.toJSONString(memberInfoService.getMemberListPage(param));
		long logETime = System.currentTimeMillis();
		log.info("========会员列表=======END("+(logETime-logSTime)+")ss");
		return jsonString;
	}

	@RequestMapping(value = "/modifyMemberSalesmanId", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String modifyMemberSalesmanId(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String salesmanId = request.getParameter("salesmanId");
		String uid = request.getParameter("uid");
		if (StringUtils.isBlank(salesmanId)) {
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "家族助理ID不能为空");
			return JSONObject.toJSONString(resultMap);
		}
		Salesman salesman = salesmanServie.getSalesmanById(Long.valueOf(salesmanId));
		if (salesman == null) {
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "家族助理不存在");
			return JSONObject.toJSONString(resultMap);
		}
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		UserOrgRelation bean = new UserOrgRelation();
		bean.setAgentUserId(salesman.getAgentUser());
		bean.setStrategicPartnerId(salesman.getStrategicPartner());
		bean.setExtensionCenterId(salesman.getExtensionCenter());
		bean.setPromotersId(salesman.getPromoters());
		bean.setSalesmanId(salesman.getId());
		bean.setUid(Integer.valueOf(uid));
		bean.setUpdateTime(System.currentTimeMillis() / 1000);
		bean.setUpdateUser(Long.valueOf(String.valueOf(adminUser.getUid())));

		int i = memberInfoService.modifyMemberInfoRelation(bean);
		if (i == 0) {
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "保存失败");
			return JSONObject.toJSONString(resultMap);
		}
		
		// 更新用户信息中关联的上级相关信息（手机号码、联系人、联系人名称）
		MemberInfoParentParamDto params = new MemberInfoParentParamDto();
		// 星耀公会
		StrategicPartner tmpStrategicPartner = strategicPartnerService.getStrategicPartnerById(salesman.getStrategicPartner());
		params.setStrategicPartnerId(tmpStrategicPartner.getId());
		params.setStrategicPartnerName(tmpStrategicPartner.getName());
		params.setStrategicPartnerContactsName(tmpStrategicPartner.getContacts());
		params.setStrategicPartnerContactsPhone(tmpStrategicPartner.getContactsPhone());
		// 钻石公会
		ExtensionCenter tmpExtensionCenter = extensionCenterServie.getExtensionCenterById(salesman.getExtensionCenter());
		params.setExtensionCenterId(tmpExtensionCenter.getId());
		params.setExtensionCenterName(tmpExtensionCenter.getName());
		params.setExtensionCenterContactsName(tmpExtensionCenter.getContacts());
		params.setExtensionCenterContactsPhone(tmpExtensionCenter.getContactsPhone());
		// 铂金公会
		Promoters tmpPromoters = promotersServie.getPromotersById(salesman.getPromoters());
		params.setPromotersId(tmpPromoters.getId());
		params.setPromotersName(tmpPromoters.getName());
		params.setPromotersContactsName(tmpPromoters.getContacts());
		params.setPromotersContactsPhone(tmpPromoters.getContactsPhone());
		// 黄金公会
		AgentUser tmpAgentUser = agentUserServie.getAgentUserById(salesman.getAgentUser());
		params.setAgentUserId(tmpAgentUser.getId());
		params.setAgentUserName(tmpAgentUser.getName());
		params.setAgentUserContactsName(tmpAgentUser.getContacts());
		params.setAgentUserContactsPhone(tmpAgentUser.getContactsPhone());
		// 家族助理
		params.setSalesmanId(salesman.getId());
		params.setSalesmanName(salesman.getName());
		params.setSalesmanContactsName(salesman.getContacts());
		params.setSalesmanContactsPhone(salesman.getContactsPhone());
		//要修改的uid
		params.setUid(Long.valueOf(uid));
		memberInfoService.updateUserAndParentInfo(params);
		
		resultMap.put(ConstantsAction.RESULT, true);
		resultMap.put(ConstantsAction.MSG, "保存成功");
		return JSONObject.toJSONString(resultMap);
	}

	@RequestMapping(value = "/getMemberInfoById", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getMemberInfoById(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String uid = request.getParameter("uid");
		if (StringUtils.isBlank(uid)) {
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "用户ID不能为空");
			return JSONObject.toJSONString(resultMap);
		}
		MemberInfo memeber = memberInfoService.getMemberInfoById(Integer.parseInt(uid));
		if (memeber == null) {
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "查询失败");
			return JSONObject.toJSONString(resultMap);
		}
		resultMap.put(ConstantsAction.RESULT, true);
		resultMap.put("data", memeber);
		return JSONObject.toJSONString(resultMap);
	}
	
	@RequestMapping(value = "/modifyMemberInfoById", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String modifyMemberInfoById(@RequestParam(value = "headimage", required = false) MultipartFile image1,
			@RequestParam(value = "livimage", required = false) MultipartFile image2,HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String,Object> params = new HashMap<String,Object>();
			if (image1 != null && !image1.isEmpty()) {
				String headimageUrl = QiniuUpUtils.uploadfile(image1, Constant.qn_default_bucket, Constant.qn_default_bucket_domain);
				params.put("headimage", headimageUrl);
			}else {
				params.put("headimage", request.getParameter("headimageUrl"));
			}
			if (image2 != null && !image2.isEmpty()) {
				String livimageUrl = QiniuUpUtils.uploadfile(image2, Constant.qn_default_bucket, Constant.qn_default_bucket_domain);
				params.put("livimage", livimageUrl);
			} else{
				params.put("livimage", request.getParameter("livimageUrl"));
			}
			params.put("uid", request.getParameter("uid"));
			params.put("nickname", request.getParameter("nickname"));
			params.put("sex", request.getParameter("sex"));
			params.put("hobby", request.getParameter("hobby"));
			params.put("signature", request.getParameter("signature"));
			
			params.put("adminid", "admin");
			HttpResponse<JsonNode> res = Unirest.post(Constant.business_server_url+"/admin/modifyUserInfoByUid").fields(params).asJson();
			
			if(res==null) {
				resultMap.put(ConstantsAction.RESULT, false);
				resultMap.put(ConstantsAction.MSG, "修改失败");
				return JSONObject.toJSONString(resultMap);
			}
			if (!"200".equals(String.valueOf(res.getBody().getObject().get("code")))) {
				resultMap.put(ConstantsAction.RESULT, false);
				resultMap.put(ConstantsAction.MSG, String.valueOf(res.getBody().getObject().get("message")));
				return JSONObject.toJSONString(resultMap);
			}
			
			resultMap.put(ConstantsAction.RESULT, true);
			resultMap.put(ConstantsAction.MSG, "修改成功");
			return JSONObject.toJSONString(resultMap);
		}catch (Exception e) {
			e.printStackTrace();
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "修改失败");
			return JSONObject.toJSONString(resultMap);
		}
	}
	
	@RequestMapping(value = "/updateUserPassword", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateUserPassword(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("uid", request.getParameter("uid"));
			params.put("passWord", request.getParameter("passWord"));
			params.put("adminid", "admin");
			HttpResponse<JsonNode> res = Unirest.post(Constant.business_server_url+"/admin/updateUserPassword").fields(params).asJson();
			if(res==null) {
				resultMap.put(ConstantsAction.RESULT, false);
				resultMap.put(ConstantsAction.MSG, "修改失败");
				return JSONObject.toJSONString(resultMap);
			}
			if (!"200".equals(String.valueOf(res.getBody().getObject().get("code")))) {
				resultMap.put(ConstantsAction.RESULT, false);
				resultMap.put(ConstantsAction.MSG, String.valueOf(res.getBody().getObject().get("message")));
				return JSONObject.toJSONString(resultMap);
			}
			resultMap.put(ConstantsAction.RESULT, true);
			resultMap.put(ConstantsAction.MSG, "修改成功");
			return JSONObject.toJSONString(resultMap);
		}catch (Exception e) {
			e.printStackTrace();
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "修改失败");
			return JSONObject.toJSONString(resultMap);
		}
	}

	@RequestMapping(value = "/expExcel")
	public void expExcel(HttpServletRequest request, HttpServletResponse response, MemberParamDto param) {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		if (adminUser.getType() == Constant.AdminUserType.StrategicPartner.getVal()) {
			StrategicPartner strategicPartner = strategicPartnerService
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
		List<Map<String, Object>> list = memberInfoService.getAllMemberList(param);
		exportExcelData(list, "会员信息列表", adminUser, request, response);
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
			boolean hasSalesmanAuth = haveAuth("member:list-salesmanName", adminUser);
			boolean hasAgentUserAuth = haveAuth("member:list-agentUserName", adminUser);
			boolean hasPromotersAuth = haveAuth("member:list-promotersName", adminUser);
			boolean hasExtensionCenterAuth = haveAuth("member:list-extensionCenterName", adminUser);
			boolean hasStrategicPartnerAuth = haveAuth("member:list-strategicPartnerName", adminUser);
			boolean hasPhoneAuth = haveAuth("member:list-phone", adminUser);

			// 列
			String[] cols1 = new String[] { //
					"ID", "昵称" };
			String[] cols2 = new String[] { //
					"主播等级", "用户等级", //
					"充值总金额", "金币(余额)", "注册时间", "虚拟账号", "封号" //
			};
			List<String> colsList = new ArrayList<String>();
			/****************基础信息***********/
			colsList.addAll(Arrays.asList(cols1));
			if (hasPhoneAuth) {
				colsList.add("手机号");
			}
			colsList.addAll(Arrays.asList(cols2));

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
			String[] fieldNameOne = new String[] { "uid", "nickname" };
			String[] fieldNameTwo = new String[] { "anchorLevel", "userLevel", //
					"moneyRmb", "money", "registtimeStr", "supportUserFlag", "statusName"//
			};

			/****************权限控制信息(数据字段)**************/
			fieldNameList.addAll(Arrays.asList(fieldNameOne));
			if (hasPhoneAuth) {
				fieldNameList.add("phone");
			}
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
