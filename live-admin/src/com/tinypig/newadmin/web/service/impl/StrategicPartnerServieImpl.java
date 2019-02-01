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
import com.tinypig.newadmin.web.dao.ExtensionCenterDao;
import com.tinypig.newadmin.web.dao.StrategicPartnerDao;
import com.tinypig.newadmin.web.entity.MemberInfoParentParamDto;
import com.tinypig.newadmin.web.entity.StrategicPartner;
import com.tinypig.newadmin.web.entity.StrategicPartnerParamDto;
import com.tinypig.newadmin.web.service.IStrategicPartnerServie;
import com.tinypig.newadmin.web.service.MemberInfoService;

@Service
@Transactional
public class StrategicPartnerServieImpl implements IStrategicPartnerServie {

	@Autowired
	private StrategicPartnerDao strategicPartnerDao;

	@Autowired
	private ExtensionCenterDao extensionCenterDao;

	@Autowired
	private MemberInfoService memberInfoService;

	@Override
	@Transactional(readOnly = true)
	public Map<String,Object> getStrategicPartnerListPage(Long id,String name,String contactsPhone,String phone,Long stime,Long etime, String orderBy,String orderSort,Integer page, Integer rows) {
		Integer startIndex = (page - 1) * rows;
		StrategicPartnerParamDto params = new StrategicPartnerParamDto();
		params.setId(id);
		params.setName(name);
		params.setPhone(phone);
		params.setContactsPhone(contactsPhone);
		params.setStime(stime);
		params.setEtime(etime);
		params.setStartIndex(startIndex);
		params.setPageSize(rows);
		params.setOrderBy(orderBy);
		params.setOrderSort(orderSort);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("rows", strategicPartnerDao.getStrategicPartnerListPage(params));
		result.put("total", strategicPartnerDao.getStrategicPartnerTotal(params));
		return result;
	}
	
	@Override
	public StrategicPartner getStrategicPartnerByPhone(String phone) {
		return strategicPartnerDao.getStrategicPartnerByPhone(phone);
	}

	@Override
	@Transactional(readOnly = true)
	public StrategicPartner getStrategicPartnerById(Long id) {
		return strategicPartnerDao.selectByPrimaryKey(id);
	}

	@Override
	public Map<String, Object> saveStrategicPartner(StrategicPartner strategicPartner, String password) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isBlank(password)) {
			result.put("code", 201);
			result.put("msg", "保存失败，请输入密码");
			return result;
		}
		if (StringUtils.isBlank(strategicPartner.getContactsPhone())) {
			result.put("code", 202);
			result.put("msg", "保存失败，请输入联系电话");
			return result;
		}
		AdminRoleModel roleByRole = AdminRoleDao.getInstance().getRoleByRoleName("星耀公会");
		if (null == roleByRole) {
			result.put("code", 203);
			result.put("msg", "保存失败，获取角色ID错误");
			return result;
		}
		int count = AdminUserDao.getInstance().countByUserName(strategicPartner.getContactsPhone());
		if (count > 0) {
			result.put("code", 204);
			result.put("msg", "保存失败，手机号账号已存在");
			return result;
		}

		int role_id = roleByRole.getRole_id();
		int adminUserType = Constant.AdminUserType.StrategicPartner.getVal();

		int ires = AdminUserDao.getInstance().addAdminUser(strategicPartner.getContactsPhone(), password, role_id,
				strategicPartner.getCreateUser().intValue(), adminUserType);
		if (ires > 0) {// 添加用户成功
			int row = strategicPartnerDao.insertSelective(strategicPartner);
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
	public Map<String, Object> updateStrategicPartner(StrategicPartner strategicPartner, String oldContactsPhone,
			String password) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isBlank(strategicPartner.getContactsPhone())) {
			result.put("code", 202);
			result.put("msg", "保存失败，请输入联系电话");
			return result;
		}
		if (StringUtils.isNotBlank(password)) {// 密码变了
			if (StringUtils.equals(oldContactsPhone, strategicPartner.getContactsPhone())) {// 没改变手机号
				AdminUserModel adminUserModel = AdminUserDao.getInstance()
						.getAdminInfoByUsername(strategicPartner.getContactsPhone());
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
				int count = AdminUserDao.getInstance().countByUserName(strategicPartner.getContactsPhone());
				if (count > 0) {
					result.put("code", 204);
					result.put("msg", "保存失败，手机号账号已存在");
					return result;
				}
				boolean fa = AdminUserDao.getInstance().updUsernamePwd(adminUserModel.getUid(),
						strategicPartner.getContactsPhone(), password);
				if (fa == false) {
					result.put("code", 205);
					result.put("msg", "保存失败，更新用户名密码失败");
					return result;
				}
			}
		} else {// 密码没变
			if (!StringUtils.equals(oldContactsPhone, strategicPartner.getContactsPhone())) {// 改变了手机号
				AdminUserModel adminUserModel = AdminUserDao.getInstance().getAdminInfoByUsername(oldContactsPhone);
				if (adminUserModel == null) {
					result.put("code", 203);
					result.put("msg", "保存失败，没有此手机号码用户");
					return result;
				}
				int count = AdminUserDao.getInstance().countByUserName(strategicPartner.getContactsPhone());
				if (count > 0) {
					result.put("code", 204);
					result.put("msg", "保存失败，手机号账号已存在");
					return result;
				}
				boolean fa = AdminUserDao.getInstance().updUsername(adminUserModel.getUid(),
						strategicPartner.getContactsPhone());
				if (fa == false) {
					result.put("code", 205);
					result.put("msg", "保存失败，更新用户名密码失败");
					return result;
				}
			}
		}
		int row = strategicPartnerDao.updateByPrimaryKeySelective(strategicPartner);
		if (row == 1) {
			//更新用户信息中关联的上级相关信息（手机号码、联系人、联系人名称）
			MemberInfoParentParamDto params = new MemberInfoParentParamDto();
			params.setStrategicPartnerId(strategicPartner.getId());
			params.setStrategicPartnerName(strategicPartner.getName());
			params.setStrategicPartnerContactsName(strategicPartner.getContacts());
			params.setStrategicPartnerContactsPhone(strategicPartner.getContactsPhone());
			memberInfoService.updateStrategicPartnerInfo(params);
			
			result.put("msg", "保存成功");
			result.put("code", 200);
		} else {
			result.put("msg", "保存失败");
			result.put("code", 201);
		}
		return result;
	}

	@Override
	public Map<String, Object> delStrategicPartner(Long id, AdminUserModel adminUserModel) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (null == id) {
			result.put("msg", "ID不能为空");
			result.put("code", 202);
			return result;
		}
		StrategicPartner strategicPartner = strategicPartnerDao.selectByPrimaryKey(id);
		if (null == strategicPartner) {
			result.put("msg", "删除失败，未找到该条数据");
			result.put("code", 203);
			return result;
		}
		int ro = extensionCenterDao.countExtensionCenterByStrategicPartnerId(id);
		if (ro > 0) {
			result.put("msg", "请先删除该战略合作中心下的钻石公会");
			result.put("code", 205);
			return result;
		}

		String contactsPhone = strategicPartner.getContactsPhone();
		int r = AdminUserDao.getInstance().deleteByUserName(contactsPhone);
		if (r != 1) {
			result.put("msg", "删除用户失败");
			result.put("code", 204);
			return result;
		}

		int row = strategicPartnerDao.deleteByPrimaryKey(id);
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
	public Map<String, Object> getStrategicPartner(AdminUserModel adminUser) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", 200);
		resultMap.put("data", 0);
		if (null != adminUser) {
			if (adminUser.getType() == Constant.AdminUserType.StrategicPartner.getVal()) {
				StrategicPartner strategicPartnerByPhone = strategicPartnerDao.getStrategicPartnerByPhone(adminUser.getUsername());
				if (null != strategicPartnerByPhone) {
					resultMap.put("data", strategicPartnerByPhone.getId());
					return resultMap;
				}
			}
		}
		return resultMap;
	}

}
