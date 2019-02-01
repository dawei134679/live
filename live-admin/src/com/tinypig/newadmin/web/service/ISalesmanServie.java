package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.newadmin.web.entity.Salesman;

public interface ISalesmanServie {

	Map<String,Object> getSalesmanListPage(String name, String contactsPhone, Long strategicPartnerId,
			Long extensionCenterId, Long promotersId, Long agentUserId, Long salesmanId, String strategicPartnerName,
			String extensionCenterName, String promotersName, String agentName, Long stime, Long etime, String orderBy,
			String orderSort, Integer page, Integer rows);

	Map<String, Object> updateSalesman(Salesman salesman, String oldContactsPhone, String password);

	Map<String, Object> delSalesman(Long id, AdminUserModel adminUserModel);

	Map<String, Object> saveSalesman(Salesman salesman, String password);

	Map<String, Object> getAgentUser(AdminUserModel adminUser);

	Salesman getSalesmanByPhone(String phone);

	Salesman getSalesmanById(Long id);

	Map<String, Object> reCreateQrcode(Long id, AdminUserModel adminUser);

	int invalidQrcode(List<Long> idsList, AdminUserModel adminUser);

}
