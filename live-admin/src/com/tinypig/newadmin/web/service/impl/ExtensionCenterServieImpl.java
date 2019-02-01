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
import com.tinypig.newadmin.web.entity.ExtensionCenterParamDto;
import com.tinypig.newadmin.web.entity.MemberInfoParentParamDto;
import com.tinypig.newadmin.web.entity.StrategicPartner;
import com.tinypig.newadmin.web.service.IExtensionCenterServie;
import com.tinypig.newadmin.web.service.MemberInfoService;

@Service
@Transactional
public class ExtensionCenterServieImpl implements IExtensionCenterServie {

	@Autowired
	private AgentUserDao agentUserDao;

	@Autowired
	private PromotersDao promotersDao;

	@Autowired
	private SalesmanDao salesmanDao;

	@Autowired
	private ExtensionCenterDao extensionCenterDao;

	@Autowired
	private UserOrgRelationDao userOrgRelationDao;

	@Autowired
	private StrategicPartnerDao strategicPartnerDao;
	
	@Autowired
	private MemberInfoService memberInfoService;

	@Override
	@Transactional(readOnly = true)
	public Map<String,Object> getExtensionCenterListPage(Long id,String name,String contactsPhone,Long strategicPartnerId,String strategicPartnerName,Long stime,Long etime, String orderBy,String orderSort,Integer page, Integer rows) {
		Integer startIndex = (page - 1) * rows;
		ExtensionCenterParamDto params = new ExtensionCenterParamDto();
		params.setId(id);
		params.setName(name);
		params.setContactsPhone(contactsPhone);
		params.setStrategicPartnerId(strategicPartnerId);
		params.setStrategicPartnerName(strategicPartnerName);
		params.setStime(stime);
		params.setEtime(etime);
		params.setStartIndex(startIndex);
		params.setPageSize(rows);
		params.setOrderBy(orderBy);
		params.setOrderSort(orderSort);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("rows", extensionCenterDao.getExtensionCenterListPage(params));
		result.put("total", extensionCenterDao.getExtensionCenterTotal(params));
		return result;
	}
	
	@Override
	public ExtensionCenter getExtensionCenterByPhone(String phone) {
		return extensionCenterDao.getExtensionCenterByPhone(phone);
	}

	@Override
	@Transactional(readOnly = true)
	public ExtensionCenter getExtensionCenterById(Long id) {
		return extensionCenterDao.selectByPrimaryKey(id);
	}

	@Override
	public Map<String, Object> saveExtensionCenter(ExtensionCenter extensionCenter, String password) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isBlank(password)) {
			result.put("code", 201);
			result.put("msg", "保存失败，请输入密码");
			return result;
		}
		if (StringUtils.isBlank(extensionCenter.getContactsPhone())) {
			result.put("code", 202);
			result.put("msg", "保存失败，请输入联系电话");
			return result;
		}
		AdminRoleModel roleByRole = AdminRoleDao.getInstance().getRoleByRoleName("钻石公会");
		if (null == roleByRole) {
			result.put("code", 203);
			result.put("msg", "保存失败，获取角色ID错误");
			return result;
		}
		int count = AdminUserDao.getInstance().countByUserName(extensionCenter.getContactsPhone());
		if (count > 0) {
			result.put("code", 204);
			result.put("msg", "保存失败，手机号账号已存在");
			return result;
		}
		
		StrategicPartner strategicPartner = strategicPartnerDao.selectByPrimaryKey(extensionCenter.getStrategicPartner());
		if (null == strategicPartner) {
			result.put("code", 205);
			result.put("msg", "保存失败，未找到星耀公会");
			return result;
		}

		int role_id = roleByRole.getRole_id();
		int adminUserType = Constant.AdminUserType.ExtensionCenter.getVal();

		int ires = AdminUserDao.getInstance().addAdminUser(extensionCenter.getContactsPhone(), password, role_id,
				extensionCenter.getCreateUser().intValue(), adminUserType);
		if (ires > 0) {// 添加用户成功
			int row = extensionCenterDao.insertSelective(extensionCenter);
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
	public Map<String, Object> updateExtensionCenter(ExtensionCenter extensionCenter, String oldContactsPhone,
			String password) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isBlank(extensionCenter.getContactsPhone())) {
			result.put("code", 202);
			result.put("msg", "保存失败，请输入联系电话");
			return result;
		}
		if (StringUtils.isNotBlank(password)) {// 密码变了
			if (StringUtils.equals(oldContactsPhone, extensionCenter.getContactsPhone())) {// 没改变手机号
				AdminUserModel adminUserModel = AdminUserDao.getInstance()
						.getAdminInfoByUsername(extensionCenter.getContactsPhone());
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
				int count = AdminUserDao.getInstance().countByUserName(extensionCenter.getContactsPhone());
				if (count > 0) {
					result.put("code", 204);
					result.put("msg", "保存失败，手机号账号已存在");
					return result;
				}
				boolean fa = AdminUserDao.getInstance().updUsernamePwd(adminUserModel.getUid(),
						extensionCenter.getContactsPhone(), password);
				if (fa == false) {
					result.put("code", 205);
					result.put("msg", "保存失败，更新用户名密码失败");
					return result;
				}
			}
		} else {// 密码没变
			if (!StringUtils.equals(oldContactsPhone, extensionCenter.getContactsPhone())) {// 改变了手机号
				AdminUserModel adminUserModel = AdminUserDao.getInstance().getAdminInfoByUsername(oldContactsPhone);
				if (adminUserModel == null) {
					result.put("code", 203);
					result.put("msg", "保存失败，没有此手机号码用户");
					return result;
				}
				int count = AdminUserDao.getInstance().countByUserName(extensionCenter.getContactsPhone());
				if (count > 0) {
					result.put("code", 204);
					result.put("msg", "保存失败，手机号账号已存在");
					return result;
				}
				boolean fa = AdminUserDao.getInstance().updUsername(adminUserModel.getUid(),
						extensionCenter.getContactsPhone());
				if (fa == false) {
					result.put("code", 205);
					result.put("msg", "保存失败，更新用户名密码失败");
					return result;
				}
			}
		}
		StrategicPartner strategicPartner = strategicPartnerDao.selectByPrimaryKey(extensionCenter.getStrategicPartner());
		if (null == strategicPartner) {
			result.put("code", 205);
			result.put("msg", "保存失败，未找到星耀公会");
			return result;
		}
		
		int row = extensionCenterDao.updateByPrimaryKeySelective(extensionCenter);
		if (row == 1) {
			
			// 修改所有下级为当前id的数据，将上级、上上级改为新修改的id
			Long strategicPartnerId = extensionCenter.getStrategicPartner();
			Long extensionCenterId = extensionCenter.getId();
			
			promotersDao.updateParentsByExtensionCenter(strategicPartnerId, extensionCenterId);
			agentUserDao.updateParentsByExtensionCenter(strategicPartnerId, extensionCenterId);
			salesmanDao.updateParentsByExtensionCenter(strategicPartnerId, extensionCenterId);
			userOrgRelationDao.updateParentsByExtensionCenter(strategicPartnerId, extensionCenterId);
			
			//更新用户信息中关联的上级相关信息（手机号码、联系人、联系人名称）
			MemberInfoParentParamDto params = new MemberInfoParentParamDto();
			//星耀公会
			params.setStrategicPartnerId(strategicPartnerId);
			params.setStrategicPartnerName(strategicPartner.getName());
			params.setStrategicPartnerContactsName(strategicPartner.getContacts());
			params.setStrategicPartnerContactsPhone(strategicPartner.getContactsPhone());
			//钻石公会
			params.setExtensionCenterId(extensionCenterId);
			params.setExtensionCenterName(extensionCenter.getName());
			params.setExtensionCenterContactsName(extensionCenter.getContacts());
			params.setExtensionCenterContactsPhone(extensionCenter.getContactsPhone());
			memberInfoService.updateExtensionCenterAndParentInfo(params);
			
			result.put("msg", "保存成功");
			result.put("code", 200);
		} else {
			result.put("msg", "保存失败");
			result.put("code", 201);
		}
		return result;
	}

	@Override
	public Map<String, Object> delExtensionCenter(Long id, AdminUserModel adminUserModel) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (null == id) {
			result.put("msg", "ID不能为空");
			result.put("code", 202);
			return result;
		}
		ExtensionCenter extensionCenter = extensionCenterDao.selectByPrimaryKey(id);
		if (null == extensionCenter) {
			result.put("msg", "删除失败，未找到该条数据");
			result.put("code", 203);
			return result;
		}
		int ro = promotersDao.countPromotersByExtensionCenterId(id);
		if (ro > 0) {
			result.put("msg", "请先删除该钻石公会下的铂金公会");
			result.put("code", 205);
			return result;
		}

		String contactsPhone = extensionCenter.getContactsPhone();
		int r = AdminUserDao.getInstance().deleteByUserName(contactsPhone);
		if (r != 1) {
			result.put("msg", "删除用户失败");
			result.put("code", 204);
			return result;
		}

		int row = extensionCenterDao.deleteByPrimaryKey(id);
		if (row == 1) {
			result.put("msg", "删除成功");
			result.put("code", 200);
		} else {
			result.put("msg", "删除失败");
			result.put("code", 201);
		}
		return result;
	}

}
