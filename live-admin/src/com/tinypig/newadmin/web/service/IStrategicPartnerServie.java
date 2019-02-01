package com.tinypig.newadmin.web.service;

import java.util.Map;

import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.newadmin.web.entity.StrategicPartner;

public interface IStrategicPartnerServie {

	Map<String, Object> updateStrategicPartner(StrategicPartner strategicPartner, String oldContactsPhone,
			String password);

	Map<String, Object> saveStrategicPartner(StrategicPartner strategicPartner, String password);

	Map<String, Object> getStrategicPartnerListPage(Long id,String name,String contactsPhone ,String phone, Long stime, Long etime, String orderBy,
			String orderSort, Integer page, Integer rows);

	Map<String, Object> delStrategicPartner(Long id, AdminUserModel adminUserModel);

	StrategicPartner getStrategicPartnerById(Long id);

	StrategicPartner getStrategicPartnerByPhone(String phone);

	Map<String, Object> getStrategicPartner(AdminUserModel adminUser);

}
