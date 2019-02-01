package com.tinypig.newadmin.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tinypig.admin.dao.AdminRoleDao;
import com.tinypig.admin.dao.AdminUserDao;
import com.tinypig.admin.model.AdminRoleModel;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.util.Constant;
import com.tinypig.newadmin.web.dao.AgentUserDao;
import com.tinypig.newadmin.web.dao.ExtensionCenterDao;
import com.tinypig.newadmin.web.dao.PromotersDao;
import com.tinypig.newadmin.web.dao.SalesmanDao;
import com.tinypig.newadmin.web.dao.StrategicPartnerDao;
import com.tinypig.newadmin.web.dao.UserOrgRelationDao;
import com.tinypig.newadmin.web.entity.AgentUser;
import com.tinypig.newadmin.web.entity.AgentUserParamDto;
import com.tinypig.newadmin.web.entity.ExtensionCenter;
import com.tinypig.newadmin.web.entity.MemberInfoParentParamDto;
import com.tinypig.newadmin.web.entity.Promoters;
import com.tinypig.newadmin.web.entity.StrategicPartner;
import com.tinypig.newadmin.web.service.IAgentUserServie;
import com.tinypig.newadmin.web.service.MemberInfoService;

@Service
@Transactional
public class AgentUserServieImpl implements IAgentUserServie {

	@Autowired
	private SalesmanDao salesmanDao;

	@Autowired
	private AgentUserDao agentUserDao;

	@Autowired
	private PromotersDao promotersDao;

	@Autowired
	private StrategicPartnerDao strategicPartnerDao;

	@Autowired
	private ExtensionCenterDao extensionCenterDao;

	@Autowired
	private MemberInfoService memberInfoService;

	@Autowired
	private UserOrgRelationDao userOrgRelationDao;

	@Override
	@Transactional(readOnly = true)
	public Map<String,Object> getAgentUserListPage(String name, String contactsPhone,Long strategicPartnerId, Long extensionCenterId,
			Long promotersId, Long agentUserId,String strategicPartnerName, String extensionCenterName, String promotersName, Long stime,
			Long etime, String orderBy, String orderSort, Integer page, Integer rows) {
		Integer startIndex = (page - 1) * rows;
		AgentUserParamDto params = new AgentUserParamDto();
		params.setName(name);
		params.setContactsPhone(contactsPhone);
		params.setStrategicPartnerId(strategicPartnerId);
		params.setStrategicPartnerName(strategicPartnerName);
		params.setExtensionCenterId(extensionCenterId);
		params.setExtensionCenterName(extensionCenterName);
		params.setPromotersId(promotersId);
		params.setPromotersName(promotersName);
		params.setAgentUserId(agentUserId);
		params.setStime(stime);
		params.setEtime(etime);
		params.setOrderBy(orderBy);
		params.setOrderSort(orderSort);
		params.setStartIndex(startIndex);
		params.setPageSize(rows);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("rows", agentUserDao.getAgentUserListPage(params));
		result.put("total", agentUserDao.getAgentUserTotal(params));
		return result;

	}

	@Override
	public AgentUser getAgentUserByPhone(String phone) {
		return agentUserDao.getAgentUserByPhone(phone);
	}

	@Override
	public Map<String, Object> saveAgentUser(AgentUser agentUser, String password) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isBlank(password)) {
			result.put("code", 201);
			result.put("msg", "保存失败，请输入密码");
			return result;
		}
		if (StringUtils.isBlank(agentUser.getContactsPhone())) {
			result.put("code", 202);
			result.put("msg", "保存失败，请输入联系电话");
			return result;
		}
		AdminRoleModel roleByRole = AdminRoleDao.getInstance().getRoleByRoleName("黄金公会");
		if (null == roleByRole) {
			result.put("code", 203);
			result.put("msg", "保存失败，获取角色ID错误");
			return result;
		}
		int count = AdminUserDao.getInstance().countByUserName(agentUser.getContactsPhone());
		if (count > 0) {
			result.put("code", 204);
			result.put("msg", "保存失败，手机号账号已存在");
			return result;
		}
		Promoters promoters = promotersDao.selectByPrimaryKey(agentUser.getPromoters());
		if (null == promoters) {
			result.put("code", 205);
			result.put("msg", "保存失败，未找到铂金公会");
			return result;
		}
		agentUser.setExtensionCenter(promoters.getExtensionCenter());
		agentUser.setStrategicPartner(promoters.getStrategicPartner());

		int role_id = roleByRole.getRole_id();
		int adminUserType = Constant.AdminUserType.AgentUser.getVal();

		int ires = AdminUserDao.getInstance().addAdminUser(agentUser.getContactsPhone(), password, role_id,
				agentUser.getCreateUser().intValue(), adminUserType);
		if (ires > 0) {// 添加用户成功
			int row = agentUserDao.insertSelective(agentUser);
			if (row == 1) {
				result.put("msg", "保存成功");
				result.put("code", 200);
			} else {
				result.put("msg", "保存失败");
				result.put("code", 204);
			}
		} else {
			result.put("errorMsg", "添加用户失败");
			result.put("errorCode", false);
		}
		return result;
	}

	@Override
	public Map<String, Object> updateAgentUser(AgentUser agentUser, String oldContactsPhone, String password) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isBlank(agentUser.getContactsPhone())) {
			result.put("code", 202);
			result.put("msg", "保存失败，请输入联系电话");
			return result;
		}
		if (StringUtils.isNotBlank(password)) {// 密码变了
			if (StringUtils.equals(oldContactsPhone, agentUser.getContactsPhone())) {// 没改变手机号
				AdminUserModel adminUserModel = AdminUserDao.getInstance()
						.getAdminInfoByUsername(agentUser.getContactsPhone());
				if (adminUserModel == null) {
					result.put("code", 203);
					result.put("msg", "保存失败，没有此手机号码用户");
					return result;
				}
				boolean fa = AdminUserDao.getInstance().updPwd(adminUserModel.getUid(), password);
				if (fa == false) {
					result.put("code", 204);
					result.put("msg", "保存失败，更新密码失败");
					return result;
				}
			} else {// 改变手机号、改变密码
				AdminUserModel adminUserModel = AdminUserDao.getInstance().getAdminInfoByUsername(oldContactsPhone);
				if (adminUserModel == null) {
					result.put("code", 203);
					result.put("msg", "保存失败，没有此手机号码用户");
					return result;
				}
				int count = AdminUserDao.getInstance().countByUserName(agentUser.getContactsPhone());
				if (count > 0) {
					result.put("code", 204);
					result.put("msg", "保存失败，手机号账号已存在");
					return result;
				}
				boolean fa = AdminUserDao.getInstance().updUsernamePwd(adminUserModel.getUid(),
						agentUser.getContactsPhone(), password);
				if (fa == false) {
					result.put("code", 205);
					result.put("msg", "保存失败，更新用户名密码失败");
					return result;
				}
			}
		} else {// 密码没变
			if (!StringUtils.equals(oldContactsPhone, agentUser.getContactsPhone())) {// 改变了手机号
				AdminUserModel adminUserModel = AdminUserDao.getInstance().getAdminInfoByUsername(oldContactsPhone);
				if (adminUserModel == null) {
					result.put("code", 203);
					result.put("msg", "保存失败，没有此手机号码用户");
					return result;
				}
				int count = AdminUserDao.getInstance().countByUserName(agentUser.getContactsPhone());
				if (count > 0) {
					result.put("code", 204);
					result.put("msg", "保存失败，手机号账号已存在");
					return result;
				}
				boolean fa = AdminUserDao.getInstance().updUsername(adminUserModel.getUid(),
						agentUser.getContactsPhone());
				if (fa == false) {
					result.put("code", 205);
					result.put("msg", "保存失败，更新用户名密码失败");
					return result;
				}
			}
		}
		Promoters promoters = promotersDao.selectByPrimaryKey(agentUser.getPromoters());
		if (null == promoters) {
			result.put("code", 205);
			result.put("msg", "保存失败，未找到铂金公会");
			return result;
		}
		agentUser.setExtensionCenter(promoters.getExtensionCenter());
		agentUser.setStrategicPartner(promoters.getStrategicPartner());

		int row = agentUserDao.updateByPrimaryKeySelective(agentUser);
		if (row == 1) {

			// 修改下级的上级id
			salesmanDao.updateParentsByAgentUser(promoters.getStrategicPartner(),promoters.getExtensionCenter(), promoters.getId(), agentUser.getId());
			userOrgRelationDao.updateParentsByAgentUser(promoters.getStrategicPartner(),promoters.getExtensionCenter(), promoters.getId(),
					agentUser.getId());

			//更新用户信息中关联的上级相关信息（手机号码、联系人、联系人名称）
			MemberInfoParentParamDto params = new MemberInfoParentParamDto();
			//星耀公会
			StrategicPartner tmpStrategicPartner = strategicPartnerDao.selectByPrimaryKey(promoters.getStrategicPartner());
			params.setStrategicPartnerId(tmpStrategicPartner.getId());
			params.setStrategicPartnerName(tmpStrategicPartner.getName());
			params.setStrategicPartnerContactsName(tmpStrategicPartner.getContacts());
			params.setStrategicPartnerContactsPhone(tmpStrategicPartner.getContactsPhone());
			//钻石公会
			ExtensionCenter tmpExtensionCenter = extensionCenterDao.selectByPrimaryKey(promoters.getExtensionCenter());
			params.setExtensionCenterId(tmpExtensionCenter.getId());
			params.setExtensionCenterName(tmpExtensionCenter.getName());
			params.setExtensionCenterContactsName(tmpExtensionCenter.getContacts());
			params.setExtensionCenterContactsPhone(tmpExtensionCenter.getContactsPhone());
			//铂金公会
			params.setPromotersId(promoters.getId());
			params.setPromotersName(promoters.getName());
			params.setPromotersContactsName(promoters.getContacts());
			params.setPromotersContactsPhone(promoters.getContactsPhone());
			//黄金公会
			params.setAgentUserId(agentUser.getId());
			params.setAgentUserName(agentUser.getName());
			params.setAgentUserContactsName(agentUser.getContacts());
			params.setAgentUserContactsPhone(agentUser.getContactsPhone());
			memberInfoService.updateAgentUserAndParentInfo(params);

			
			result.put("msg", "保存成功");
			result.put("code", 200);
		} else {
			result.put("msg", "保存失败");
			result.put("code", 201);
		}
		return result;
	}

	@Override
	public Map<String, Object> delAgentUser(Long id, AdminUserModel adminUserModel) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (null == id) {
			result.put("msg", "ID不能为空");
			result.put("code", 201);
			return result;
		}
		AgentUser agentUser = agentUserDao.selectByPrimaryKey(id);
		if (null == agentUser) {
			result.put("msg", "删除失败，未找到该条数据");
			result.put("code", 202);
			return result;
		}

		int ro = salesmanDao.countSalesmanByAgentUserId(id);
		if (ro > 0) {
			result.put("msg", "请先删除该黄金公会下的家族助理");
			result.put("code", 205);
			return result;
		}

		String contactsPhone = agentUser.getContactsPhone();
		int r = AdminUserDao.getInstance().deleteByUserName(contactsPhone);
		if (r != 1) {
			result.put("msg", "删除用户失败");
			result.put("code", 201);
			return result;
		}

		int row = agentUserDao.deleteByPrimaryKey(id);
		if (row == 1) {
			result.put("msg", "删除成功");
			result.put("code", 200);
		} else {
			result.put("msg", "删除失败");
			result.put("code", 201);
		}
		return result;
	}

	// 获取当前登录用户ID
	@Override
	public Map<String, Object> getPromoters(AdminUserModel adminUser) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", 200);
		resultMap.put("data", 0);
		if (null != adminUser) {
			if (adminUser.getType() == Constant.AdminUserType.Promoters.getVal()) {
				Promoters promoters = promotersDao.getPromotersByPhone(adminUser.getUsername());
				if (null != promoters) {
					resultMap.put("data", promoters.getId());
					return resultMap;
				}
			}
		}
		return resultMap;
	}

	@Override
	@Transactional(readOnly = true)
	public AgentUser getAgentUserById(Long id) {
		return agentUserDao.selectByPrimaryKey(id);
	}

}
