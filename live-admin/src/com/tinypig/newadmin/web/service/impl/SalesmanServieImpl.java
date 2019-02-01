package com.tinypig.newadmin.web.service.impl;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tinypig.admin.dao.AdminRoleDao;
import com.tinypig.admin.dao.AdminUserDao;
import com.tinypig.admin.model.AdminRoleModel;
import com.tinypig.admin.model.AdminUser;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.util.AESCipher;
import com.tinypig.admin.util.Constant;
import com.tinypig.admin.util.EncryptUtils;
import com.tinypig.admin.util.QiniuUpUtils;
import com.tinypig.admin.util.QrCodeCreateUtil;
import com.tinypig.newadmin.web.dao.AgentUserDao;
import com.tinypig.newadmin.web.dao.ExtensionCenterDao;
import com.tinypig.newadmin.web.dao.MemberInfoDao;
import com.tinypig.newadmin.web.dao.PromotersDao;
import com.tinypig.newadmin.web.dao.SalesmanDao;
import com.tinypig.newadmin.web.dao.StrategicPartnerDao;
import com.tinypig.newadmin.web.dao.UserOrgRelationDao;
import com.tinypig.newadmin.web.entity.AgentUser;
import com.tinypig.newadmin.web.entity.ExtensionCenter;
import com.tinypig.newadmin.web.entity.MemberParamDto;
import com.tinypig.newadmin.web.entity.Promoters;
import com.tinypig.newadmin.web.entity.Salesman;
import com.tinypig.newadmin.web.entity.SalesmanParamDto;
import com.tinypig.newadmin.web.entity.StrategicPartner;
import com.tinypig.newadmin.web.entity.MemberInfoParentParamDto;
import com.tinypig.newadmin.web.service.ISalesmanServie;
import com.tinypig.newadmin.web.service.MemberInfoService;

@Service
@Transactional
public class SalesmanServieImpl implements ISalesmanServie {
	@Autowired
	private SalesmanDao salesmanDao;

	@Autowired
	private AgentUserDao agentUserDao;

	@Autowired
	private PromotersDao promotersDao;
	
	@Autowired
	private MemberInfoService memberInfoService;
	
	@Autowired
	private ExtensionCenterDao extensionCenterDao;
	
	@Autowired
	private StrategicPartnerDao strategicPartnerDao;

	@Autowired
	private UserOrgRelationDao userOrgRelationDao;

	@Autowired
	private MemberInfoDao memberInfoDao;

	@Autowired
	private com.tinypig.newadmin.web.dao.AdminUserDao adminUserDao;

	@Override
	@Transactional(readOnly = true)
	public Map<String, Object> getSalesmanListPage(String name, String contactsPhone, Long strategicPartnerId,
			Long extensionCenterId, Long promotersId, Long agentUserId, Long salesmanId, String strategicPartnerName,
			String extensionCenterName, String promotersName, String agentName, Long stime, Long etime, String orderBy,
			String orderSort, Integer page, Integer rows) {
		Integer startIndex = (page - 1) * rows;

		SalesmanParamDto params = new SalesmanParamDto();
		params.setName(name);
		params.setContactsPhone(contactsPhone);
		params.setStrategicPartnerId(strategicPartnerId);
		params.setStrategicPartnerName(strategicPartnerName);
		params.setExtensionCenterId(extensionCenterId);
		params.setExtensionCenterName(extensionCenterName);
		params.setPromotersId(promotersId);
		params.setPromotersName(promotersName);
		params.setAgentUserId(agentUserId);
		params.setAgentName(agentName);
		params.setSalesmanId(salesmanId);
		params.setStime(stime);
		params.setEtime(etime);
		params.setOrderBy(orderBy);
		params.setOrderSort(orderSort);
		params.setStartIndex(startIndex);
		params.setPageSize(rows);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", salesmanDao.getSalesmanListPage(params));
		result.put("total", salesmanDao.getSalesmanTotal(params));
		return result;
	}

	@Override
	public Salesman getSalesmanByPhone(String phone) {
		return salesmanDao.getSalesmanByPhone(phone);
	}

	@Override
	public Map<String, Object> saveSalesman(Salesman salesman, String password) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isBlank(password)) {
			result.put("code", 201);
			result.put("msg", "保存失败，请输入密码");
			return result;
		}
		if (StringUtils.isBlank(salesman.getContactsPhone())) {
			result.put("code", 202);
			result.put("msg", "保存失败，请输入联系电话");
			return result;
		}
		AdminRoleModel roleByRole = AdminRoleDao.getInstance().getRoleByRoleName("家族助理");
		if (null == roleByRole) {
			result.put("code", 203);
			result.put("msg", "保存失败，获取角色ID错误");
			return result;
		}
		int count = AdminUserDao.getInstance().countByUserName(salesman.getContactsPhone());
		if (count > 0) {
			result.put("code", 204);
			result.put("msg", "保存失败，手机号账号已存在");
			return result;
		}
		AgentUser agentUser = agentUserDao.selectByPrimaryKey(salesman.getAgentUser());
		if (null == agentUser) {
			result.put("code", 205);
			result.put("msg", "保存失败，未找到黄金公会");
			return result;
		}
		salesman.setPromoters(agentUser.getPromoters());
		salesman.setExtensionCenter(agentUser.getExtensionCenter());
		salesman.setStrategicPartner(agentUser.getStrategicPartner());

		int role_id = roleByRole.getRole_id();
		int adminUserType = Constant.AdminUserType.Salesman.getVal();

		// int ires =
		// AdminUserDao.getInstance().addAdminUser(salesman.getContactsPhone(),
		// password, role_id,
		// agentUser.getCreateUser().intValue(), adminUserType);
		try {
			AdminUser adminUser = new AdminUser();
			adminUser.setCreateUid(agentUser.getCreateUser().intValue());
			adminUser.setLogin_time((int) (System.currentTimeMillis() / 1000));
			adminUser.setPassword(EncryptUtils.md5Encrypt(password));
			adminUser.setReg_time((int) (System.currentTimeMillis() / 1000));
			adminUser.setRole_id(role_id);
			adminUser.setType(adminUserType);
			adminUser.setUsername(salesman.getContactsPhone());
			int ires = adminUserDao.insertSelective(adminUser);

			if (ires > 0) {// 添加用户成功
				int row = salesmanDao.insertSelective(salesman);
				if (row == 1) {

					String tempDir = System.getProperty("java.io.tmpdir");
					String filename = RandomStringUtils.random(16, true, true) + System.currentTimeMillis() + ".PNG";
					String localFilePath = tempDir + "/" + filename;
					
					long nowtime = System.currentTimeMillis();
					String code = URLEncoder.encode(AESCipher.aesEncryptString(salesman.getId() + "_" + nowtime), "utf-8");
					String qrcodeContent = String.format(Constant.anchor_register_url, code);
					
					boolean createQrCodeRes = QrCodeCreateUtil.createQrCode(tempDir, filename, qrcodeContent, 900,
							"PNG");
					if (createQrCodeRes) {
						QiniuUpUtils.upload(localFilePath, filename, Constant.qn_default_bucket);
						String url = QiniuUpUtils.download(filename, Constant.qn_default_bucket_domain);
						salesman.setQrcode(url+"?code=" + code);
						salesmanDao.updateByPrimaryKeySelective(salesman);
					}

					result.put("msg", "保存成功");
					result.put("code", 200);
				} else {
					result.put("msg", "保存失败");
					result.put("code", 204);
				}
			} else {
				result.put("msg", "添加用户失败");
				result.put("code", 206);
			}
		} catch (DuplicateKeyException e) {
			result.put("msg", "手机号码已存在");
			result.put("code", 205);
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException("添加失败");
		}
		return result;
	}

	@Override
	public Map<String, Object> updateSalesman(Salesman salesman, String oldContactsPhone, String password) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isBlank(salesman.getContactsPhone())) {
			result.put("code", 202);
			result.put("msg", "保存失败，请输入联系电话");
			return result;
		}
		if (StringUtils.isNotBlank(password)) {// 密码变了
			if (StringUtils.equals(oldContactsPhone, salesman.getContactsPhone())) {// 没改变手机号
				AdminUserModel adminUserModel = AdminUserDao.getInstance()
						.getAdminInfoByUsername(salesman.getContactsPhone());
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
				int count = AdminUserDao.getInstance().countByUserName(salesman.getContactsPhone());
				if (count > 0) {
					result.put("code", 204);
					result.put("msg", "保存失败，手机号账号已存在");
					return result;
				}
				boolean fa = AdminUserDao.getInstance().updUsernamePwd(adminUserModel.getUid(),
						salesman.getContactsPhone(), password);
				if (fa == false) {
					result.put("code", 205);
					result.put("msg", "保存失败，更新用户名密码失败");
					return result;
				}
			}
		} else {// 密码没变
			if (!StringUtils.equals(oldContactsPhone, salesman.getContactsPhone())) {// 改变了手机号
				AdminUserModel adminUserModel = AdminUserDao.getInstance().getAdminInfoByUsername(oldContactsPhone);
				if (adminUserModel == null) {
					result.put("code", 203);
					result.put("msg", "保存失败，没有此手机号码用户");
					return result;
				}
				int count = AdminUserDao.getInstance().countByUserName(salesman.getContactsPhone());
				if (count > 0) {
					result.put("code", 204);
					result.put("msg", "保存失败，手机号账号已存在");
					return result;
				}
				boolean fa = AdminUserDao.getInstance().updUsername(adminUserModel.getUid(),
						salesman.getContactsPhone());
				if (fa == false) {
					result.put("code", 205);
					result.put("msg", "保存失败，更新用户名密码失败");
					return result;
				}
			}
		}
		AgentUser agentUser = agentUserDao.selectByPrimaryKey(salesman.getAgentUser());
		if (null == agentUser) {
			result.put("code", 205);
			result.put("msg", "保存失败，未找到黄金公会");
			return result;
		}
		salesman.setPromoters(agentUser.getPromoters());
		salesman.setExtensionCenter(agentUser.getExtensionCenter());
		salesman.setStrategicPartner(agentUser.getStrategicPartner());

		int row = salesmanDao.updateByPrimaryKeySelective(salesman);
		if (row == 1) {

			userOrgRelationDao.updateParentsBySalesman(agentUser.getStrategicPartner(), agentUser.getExtensionCenter(),
					agentUser.getPromoters(), agentUser.getId(), salesman.getId());

			// 更新用户信息中关联的上级相关信息（手机号码、联系人、联系人名称）
			MemberInfoParentParamDto params = new MemberInfoParentParamDto();
			// 星耀公会
			StrategicPartner tmpStrategicPartner = strategicPartnerDao
					.selectByPrimaryKey(agentUser.getStrategicPartner());
			params.setStrategicPartnerId(tmpStrategicPartner.getId());
			params.setStrategicPartnerName(tmpStrategicPartner.getName());
			params.setStrategicPartnerContactsName(tmpStrategicPartner.getContacts());
			params.setStrategicPartnerContactsPhone(tmpStrategicPartner.getContactsPhone());
			// 钻石公会
			ExtensionCenter tmpExtensionCenter = extensionCenterDao.selectByPrimaryKey(agentUser.getExtensionCenter());
			params.setExtensionCenterId(tmpExtensionCenter.getId());
			params.setExtensionCenterName(tmpExtensionCenter.getName());
			params.setExtensionCenterContactsName(tmpExtensionCenter.getContacts());
			params.setExtensionCenterContactsPhone(tmpExtensionCenter.getContactsPhone());
			// 铂金公会
			Promoters tmpPromoters = promotersDao.selectByPrimaryKey(agentUser.getPromoters());
			params.setPromotersId(tmpPromoters.getId());
			params.setPromotersName(tmpPromoters.getName());
			params.setPromotersContactsName(tmpPromoters.getContacts());
			params.setPromotersContactsPhone(tmpPromoters.getContactsPhone());
			// 黄金公会
			params.setAgentUserId(agentUser.getId());
			params.setAgentUserName(agentUser.getName());
			params.setAgentUserContactsName(agentUser.getContacts());
			params.setAgentUserContactsPhone(agentUser.getContactsPhone());
			// 家族助理
			params.setSalesmanId(salesman.getId());
			params.setSalesmanName(salesman.getName());
			params.setSalesmanContactsName(salesman.getContacts());
			params.setSalesmanContactsPhone(salesman.getContactsPhone());
			memberInfoService.updateSalesmanAndParentInfo(params);

			result.put("msg", "保存成功");
			result.put("code", 200);
		} else {
			result.put("msg", "保存失败");
			result.put("code", 201);
		}
		return result;
	}

	@Override
	public Map<String, Object> delSalesman(Long id, AdminUserModel adminUserModel) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (null == id) {
			result.put("msg", "ID不能为空");
			result.put("code", 201);
			return result;
		}
		Salesman salesman = salesmanDao.selectByPrimaryKey(id);
		if (null == salesman) {
			result.put("msg", "删除失败，未找到该条数据");
			result.put("code", 202);
			return result;
		}

		MemberParamDto mpd = new MemberParamDto();
		mpd.setSalesmanId(id);
		int ro = memberInfoDao.getMemberTotal(mpd);
		if (ro > 0) {
			result.put("msg", "该家族助理下有会员，不能删除");
			result.put("code", 205);
			return result;
		}

		String contactsPhone = salesman.getContactsPhone();
		int r = AdminUserDao.getInstance().deleteByUserName(contactsPhone);
		if (r != 1) {
			result.put("msg", "删除用户失败");
			result.put("code", 201);
			return result;
		}

		int row = salesmanDao.deleteByPrimaryKey(id);
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
	public Map<String, Object> getAgentUser(AdminUserModel adminUser) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", 200);
		resultMap.put("data", 0);
		if (null != adminUser) {
			if (adminUser.getType() == Constant.AdminUserType.AgentUser.getVal()) {
				AgentUser agentUser = agentUserDao.getAgentUserByPhone(adminUser.getUsername());
				if (null != agentUser) {
					resultMap.put("data", agentUser.getId());
					return resultMap;
				}
			}
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> reCreateQrcode(Long id, AdminUserModel adminUser) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (null == id) {
			result.put("msg", "ID不能为空");
			result.put("code", 201);
			return result;
		}

		String tempDir = System.getProperty("java.io.tmpdir");
		String filename = RandomStringUtils.random(16, true, true) + System.currentTimeMillis() + ".PNG";
		String localFilePath = tempDir + "/" + filename;
		long nowtime = System.currentTimeMillis();
		try {
			String code = URLEncoder.encode(AESCipher.aesEncryptString(id + "_" + nowtime), "utf-8");
			String qrcodeContent = String.format(Constant.anchor_register_url, code);
			boolean createQrCodeRes = QrCodeCreateUtil.createQrCode(tempDir, filename, qrcodeContent, 900, "PNG");
			if (createQrCodeRes) {
				QiniuUpUtils.upload(localFilePath, filename, Constant.qn_default_bucket);
				String url = QiniuUpUtils.download(filename, Constant.qn_default_bucket_domain);
				Salesman salesman = new Salesman();
				salesman.setId(id);
				salesman.setQrcode(url + "?code=" + code);
				salesmanDao.updateByPrimaryKeySelective(salesman);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		result.put("msg", "操作成功");
		result.put("code", 200);
		return result;
	}

	@Override
	public Salesman getSalesmanById(Long id) {
		return salesmanDao.getSalesmanById(id);
	}

	@Override
	public int invalidQrcode(List<Long> ids, AdminUserModel adminUser) {
		return salesmanDao.invalidQrcode(ids);
	}
}
