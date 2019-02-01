package com.tinypig.newadmin.web.dao;

import org.apache.ibatis.annotations.Param;

import com.tinypig.newadmin.web.entity.UserOrgRelation;

public interface UserOrgRelationDao {
	int deleteByPrimaryKey(Long id);

	int insert(UserOrgRelation record);

	int insertSelective(UserOrgRelation record);

	UserOrgRelation selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(UserOrgRelation record);

	int updateByPrimaryUidSelective(UserOrgRelation record);

	int updateByPrimaryKey(UserOrgRelation record);

	int updateParentsByPromoters(@Param("strategicPartnerId") Long strategicPartnerId,@Param("extensionCenterId") Long extensionCenterId,
			@Param("promotersId") Long promotersId);

	int updateParentsByAgentUser(@Param("strategicPartnerId") Long strategicPartnerId,
			@Param("extensionCenterId") Long extensionCenterId, @Param("promotersId") Long promotersId,
			@Param("agentUserId") Long agentUserId);

	int updateParentsBySalesman(@Param("strategicPartnerId") Long strategicPartnerId,
			@Param("extensionCenterId") Long extensionCenterId, @Param("promotersId") Long promotersId,
			@Param("agentUserId") Long agentUserId, @Param("salesmanId") Long salesmanId);

	int updateParentsByExtensionCenter(@Param("strategicPartnerId") Long strategicPartnerId, @Param("extensionCenterId") Long extensionCenterId);
}