package com.tinypig.newadmin.web.service;

import java.util.Map;

import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.newadmin.web.entity.ExtensionCenter;

public interface IExtensionCenterServie {

	Map<String, Object> updateExtensionCenter(ExtensionCenter extensionCenter, String oldContactsPhone,
			String password);

	Map<String, Object> saveExtensionCenter(ExtensionCenter extensionCenter, String password);

	Map<String,Object> getExtensionCenterListPage(Long id, String name, String contactsPhone, Long strategicPartnerId,
			String strategicPartnerName, Long stime, Long etime, String orderBy, String orderSort, Integer page,
			Integer rows);

	Map<String, Object> delExtensionCenter(Long id, AdminUserModel adminUserModel);

	ExtensionCenter getExtensionCenterById(Long id);

	ExtensionCenter getExtensionCenterByPhone(String phone);

}
