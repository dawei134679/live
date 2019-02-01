package com.tinypig.newadmin.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tinypig.newadmin.web.entity.AgentUser;
import com.tinypig.newadmin.web.entity.AgentUserDto;
import com.tinypig.newadmin.web.entity.AgentUserParamDto;

public interface AgentUserDao {
	/**
	 * 逻辑删除
	 */
	int deleteByPrimaryKey(Long id);

	int insert(AgentUser record);

	int insertSelective(AgentUser record);

	AgentUser selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(AgentUser record);

	int updateByPrimaryKey(AgentUser record);

	List<AgentUserDto> getAgentUserListPage(AgentUserParamDto params);

	Integer getAgentUserTotal(AgentUserParamDto params);

	AgentUser getAgentUserByPhone(@Param("phone") String phone);

	int countAgentUserByPromotersId(@Param("promotersId") Long promotersId);

	int updateParentsByPromoters(@Param("strategicPartnerId") Long strategicPartnerId,
			@Param("extensionCenterId") Long extensionCenterId, @Param("promotersId") Long promotersId);

	int updateParentsByExtensionCenter(@Param("strategicPartnerId") Long strategicPartnerId,
			@Param("extensionCenterId") Long extensionCenterId);

}