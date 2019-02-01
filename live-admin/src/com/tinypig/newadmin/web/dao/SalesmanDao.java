package com.tinypig.newadmin.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tinypig.newadmin.web.entity.Salesman;
import com.tinypig.newadmin.web.entity.SalesmanDto;
import com.tinypig.newadmin.web.entity.SalesmanParamDto;

public interface SalesmanDao {
	int deleteByPrimaryKey(Long id);

	int insert(Salesman record);

	int insertSelective(Salesman record);

	Salesman selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(Salesman record);

	int updateByPrimaryKey(Salesman record);

	List<SalesmanDto> getSalesmanListPage(SalesmanParamDto params);

	Integer getSalesmanTotal(SalesmanParamDto params);

	int countSalesmanByAgentUserId(@Param("agentUserId") Long agentUserId);

	Salesman getSalesmanByPhone(@Param("phone") String phone);

	int updateParentsByPromoters(@Param("strategicPartnerId") Long strategicPartnerId,
			@Param("extensionCenterId") Long extensionCenterId, @Param("promotersId") Long promotersId);

	int updateParentsByAgentUser(@Param("strategicPartnerId") Long strategicPartnerId,
			@Param("extensionCenterId") Long extensionCenterId, @Param("promotersId") Long promotersId,
			@Param("agentUserId") Long agentUserId);

	Salesman getSalesmanById(Long id);

	int updateParentsByExtensionCenter(@Param("strategicPartnerId") Long strategicPartnerId,
			@Param("extensionCenterId") Long extensionCenterId);

	int invalidQrcode(@Param("ids")List<Long> ids);

}