package com.tinypig.newadmin.web.service;

import java.util.Map;

import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.newadmin.web.entity.Promoters;

public interface IPromotersServie {

	Map<String,Object> getPromotersListPage(String name,String contactsPhone,Long strategicPartnerId, Long extensionCenterId, Long promotersId,String strategicPartnerName, String extensionCenterName,Long stime,Long etime,String orderBy, String orderSort, Integer page, Integer rows);

	Map<String, Object> savePromoters(Promoters promoters, String password);

	Map<String, Object> updatePromoters(Promoters promoters, String oldContactsPhone, String password);

	Map<String, Object> delPromoters(Long id, AdminUserModel adminUser);

	Map<String, Object> getExtensionCenter(AdminUserModel adminUser);

	Promoters getPromotersByPhone(String phone);

	Promoters getPromotersById(Long promoters);

}
