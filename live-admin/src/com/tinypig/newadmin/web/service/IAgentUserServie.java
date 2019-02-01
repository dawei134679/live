package com.tinypig.newadmin.web.service;

import java.util.Map;

import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.newadmin.web.entity.AgentUser;

public interface IAgentUserServie {

	Map<String,Object> getAgentUserListPage(String name, String contactsPhone, Long strategicPartnerId,
			Long extensionCenterId, Long promotersId, Long agentUserId, String strategicPartnerName,
			String extensionCenterName, String promotersName, Long stime, Long etime, String orderBy, String orderSort,
			Integer page, Integer rows);

	Map<String, Object> saveAgentUser(AgentUser agentUser, String password);

	Map<String, Object> updateAgentUser(AgentUser agentUser, String oldContactsPhone, String password);

	Map<String, Object> delAgentUser(Long id, AdminUserModel adminUser);

	Map<String, Object> getPromoters(AdminUserModel adminUser);

	AgentUser getAgentUserByPhone(String phone);

	AgentUser getAgentUserById(Long agentUser);

}
