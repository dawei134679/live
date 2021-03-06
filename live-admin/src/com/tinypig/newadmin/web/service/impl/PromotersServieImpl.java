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
import com.tinypig.newadmin.web.entity.ExtensionCenter;
import com.tinypig.newadmin.web.entity.MemberInfoParentParamDto;
import com.tinypig.newadmin.web.entity.Promoters;
import com.tinypig.newadmin.web.entity.PromotersParamDto;
import com.tinypig.newadmin.web.entity.StrategicPartner;
import com.tinypig.newadmin.web.service.IPromotersServie;
import com.tinypig.newadmin.web.service.MemberInfoService;

@Service
@Transactional
public class PromotersServieImpl implements IPromotersServie {

	@Autowired
	private AgentUserDao agentUserDao;

	@Autowired
	private PromotersDao promotersDao;

	@Autowired
	private SalesmanDao salesmanDao;

	@Autowired
	private ExtensionCenterDao extensionCenterDao;

	@Autowired
	private StrategicPartnerDao strategicPartnerDao;

	@Autowired
	private MemberInfoService memberInfoService;

	@Autowired
	private UserOrgRelationDao userOrgRelationDao;

	@Override
	@Transactional(readOnly = true)
	public Map<String, Object> getPromotersListPage(String name, String contactsPhone, Long strategicPartnerId,
			Long extensionCenterId, Long promotersId, String strategicPartnerName, String extensionCenterName,
			Long stime, Long etime, String orderBy, String orderSort, Integer page, Integer rows) {
		Integer startIndex = (page - 1) * rows;
		PromotersParamDto params = new PromotersParamDto();
		params.setName(name);
		params.setContactsPhone(contactsPhone);
		params.setPromotersId(promotersId);
		params.setStrategicPartnerId(strategicPartnerId);
		params.setExtensionCenterName(extensionCenterName);
		params.setExtensionCenterId(extensionCenterId);
		params.setExtensionCenterName(extensionCenterName);
		params.setStartIndex(startIndex);
		params.setPageSize(rows);
		params.setStime(stime);
		params.setEtime(etime);
		params.setOrderBy(orderBy);
		params.setOrderSort(orderSort);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", promotersDao.getPromotersListPage(params));
		result.put("total", promotersDao.getPromotersTotal(params));
		return result;

	}

	@Override
	public Promoters getPromotersByPhone(String phone) {
		return promotersDao.getPromotersByPhone(phone);
	}

	@Override
	public Map<String, Object> savePromoters(Promoters promoters, String password) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isBlank(password)) {
			result.put("code", 201);
			result.put("msg", "保存失败，请输入密码");
			return result;
		}
		if (StringUtils.isBlank(promoters.getContactsPhone())) {
			result.put("code", 202);
			result.put("msg", "保存失败，请输入联系电话");
			return result;
		}
		AdminRoleModel roleByRole = AdminRoleDao.getInstance().getRoleByRoleName("铂金公会");
		if (null == roleByRole) {
			result.put("code", 203);
			result.put("msg", "保存失败，获取角色ID错误");
			return result;
		}
		int count = AdminUserDao.getInstance().countByUserName(promoters.getContactsPhone());
		if (count > 0) {
			result.put("code", 204);
			result.put("msg", "保存失败，手机号账号已存在");
			return result;
		}
		ExtensionCenter extensionCenter = extensionCenterDao.selectByPrimaryKey(promoters.getExtensionCenter());
		if (null == extensionCenter) {
			result.put("code", 205);
			result.put("msg", "保存失败，未找到钻石公会");
			return result;
		}

		int role_id = roleByRole.getRole_id();
		int adminUserType = Constant.AdminUserType.Promoters.getVal();

		promoters.setStrategicPartner(extensionCenter.getStrategicPartner());

		int ires = AdminUserDao.getInstance().addAdminUser(promoters.getContactsPhone(), password, role_id,
				promoters.getCreateUser().intValue(), adminUserType);
		if (ires > 0) {// 添加用户成功
			int row = promotersDao.insertSelective(promoters);
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
	public Map<String, Object> updatePromoters(Promoters promoters, String oldContactsPhone, String password) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isBlank(promoters.getContactsPhone())) {
			result.put("code", 202);
			result.put("msg", "保存失败，请输入联系电话");
			return result;
		}
		if (StringUtils.isNotBlank(password)) {// 密码变了
			if (StringUtils.equals(oldContactsPhone, promoters.getContactsPhone())) {// 没改变手机号
				AdminUserModel adminUserModel = AdminUserDao.getInstance()
						.getAdminInfoByUsername(promoters.getContactsPhone());
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
				int count = AdminUserDao.getInstance().countByUserName(promoters.getContactsPhone());
				if (count > 0) {
					result.put("code", 204);
					result.put("msg", "保存失败，手机号账号已存在");
					return result;
				}
				boolean fa = AdminUserDao.getInstance().updUsernamePwd(adminUserModel.getUid(),
						promoters.getContactsPhone(), password);
				if (fa == false) {
					result.put("code", 205);
					result.put("msg", "保存失败，更新用户名密码失败");
					return result;
				}
			}
		} else {// 密码没变
			if (!StringUtils.equals(oldContactsPhone, promoters.getContactsPhone())) {// 改变了手机号
				AdminUserModel adminUserModel = AdminUserDao.getInstance().getAdminInfoByUsername(oldContactsPhone);
				if (adminUserModel == null) {
					result.put("code", 203);
					result.put("msg", "保存失败，没有此手机号码用户");
					return result;
				}
				int count = AdminUserDao.getInstance().countByUserName(promoters.getContactsPhone());
				if (count > 0) {
					result.put("code", 204);
					result.put("msg", "保存失败，手机号账号已存在");
					return result;
				}
				boolean fa = AdminUserDao.getInstance().updUsername(adminUserModel.getUid(),
						promoters.getContactsPhone());
				if (fa == false) {
					result.put("code", 205);
					result.put("msg", "保存失败，更新用户名密码失败");
					return result;
				}
			}
		}
		ExtensionCenter extensionCenter = extensionCenterDao.selectByPrimaryKey(promoters.getExtensionCenter());
		if (null == extensionCenter) {
			result.put("code", 205);
			result.put("msg", "保存失败，未找到钻石公会");
			return result;
		}

		promoters.setStrategicPartner(extensionCenter.getStrategicPartner());

		int row = promotersDao.updateByPrimaryKeySelective(promoters);
		if (row == 1) {

			// 修改所有下级为当前id的数据，将上级、上上级改为新修改的id
			Long extensionCenterId = promoters.getExtensionCenter();
			Long promotersId = promoters.getId();
			Long strategicPartnerId = promoters.getStrategicPartner();
			agentUserDao.updateParentsByPromoters(strategicPartnerId, extensionCenterId, promotersId);
			salesmanDao.updateParentsByPromoters(strategicPartnerId, extensionCenterId, promotersId);
			userOrgRelationDao.updateParentsByPromoters(strategicPartnerId, extensionCenterId, promotersId);

			// 更新用户信息中关联的上级相关信息（手机号码、联系人、联系人名称）
			MemberInfoParentParamDto params = new MemberInfoParentParamDto();
			// 星耀公会
			StrategicPartner tmpStrategicPartner = strategicPartnerDao.selectByPrimaryKey(strategicPartnerId);
			params.setStrategicPartnerId(strategicPartnerId);
			params.setStrategicPartnerName(tmpStrategicPartner.getName());
			params.setStrategicPartnerContactsName(tmpStrategicPartner.getContacts());
			params.setStrategicPartnerContactsPhone(tmpStrategicPartner.getContactsPhone());
			// 钻石公会
			params.setExtensionCenterId(extensionCenterId);
			params.setExtensionCenterName(extensionCenter.getName());
			params.setExtensionCenterContactsName(extensionCenter.getContacts());
			params.setExtensionCenterContactsPhone(extensionCenter.getContactsPhone());
			// 铂金公会
			params.setPromotersId(promotersId);
			params.setPromotersName(promoters.getName());
			params.setPromotersContactsName(promoters.getContacts());
			params.setPromotersContactsPhone(promoters.getContactsPhone());
			memberInfoService.updatePromotersAndParentInfo(params);

			result.put("msg", "保存成功");
			result.put("code", 200);
		} else {
			result.put("msg", "保存失败");
			result.put("code", 201);
		}
		return result;
	}

	@Override
	public Map<String, Object> delPromoters(Long id, AdminUserModel adminUserModel) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (null == id) {
			result.put("msg", "ID不能为空");
			result.put("code", 201);
			return result;
		}
		Promoters promoters = promotersDao.selectByPrimaryKey(id);
		if (null == promoters) {
			result.put("msg", "删除失败，未找到该条数据");
			result.put("code", 202);
			return result;
		}

		int ro = agentUserDao.countAgentUserByPromotersId(id);
		if (ro > 0) {
			result.put("msg", "请先删除该铂金公会下的黄金公会");
			result.put("code", 205);
			return result;
		}

		String contactsPhone = promoters.getContactsPhone();
		int r = AdminUserDao.getInstance().deleteByUserName(contactsPhone);
		if (r != 1) {
			result.put("msg", "删除用户失败");
			result.put("code", 201);
			return result;
		}

		int row = promotersDao.deleteByPrimaryKey(id);
		if (row == 1) {
			result.put("msg", "删除成功");
			result.put("code", 200);
		} else {
			result.put("msg", "删除失败");
			result.put("code", 201);
		}
		return result;
	}

	@Override
	public Map<String, Object> getExtensionCenter(AdminUserModel adminUser) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", 200);
		resultMap.put("data", 0);
		if (null != adminUser) {
			if (adminUser.getType() == Constant.AdminUserType.ExtensionCenter.getVal()) {
				ExtensionCenter extensionCenterByPhone = extensionCenterDao
						.getExtensionCenterByPhone(adminUser.getUsername());
				if (null != extensionCenterByPhone) {
					resultMap.put("data", extensionCenterByPhone.getId());
					return resultMap;
				}
			}
		}
		return resultMap;
	}

	@Override
	@Transactional(readOnly = true)
	public Promoters getPromotersById(Long id) {
		return promotersDao.selectByPrimaryKey(id);
	}

}
