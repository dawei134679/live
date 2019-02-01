package com.tinypig.newadmin.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tinypig.newadmin.web.entity.ExtensionCenter;
import com.tinypig.newadmin.web.entity.ExtensionCenterParamDto;

public interface ExtensionCenterDao {

	int deleteByPrimaryKey(Long id);

	int insert(ExtensionCenter record);

	int insertSelective(ExtensionCenter record);

	ExtensionCenter selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(ExtensionCenter record);

	int updateByPrimaryKey(ExtensionCenter record);

	List<ExtensionCenter> getExtensionCenterListPage(ExtensionCenterParamDto params);

	Integer getExtensionCenterTotal(ExtensionCenterParamDto params);

	ExtensionCenter getExtensionCenterByPhone(@Param("phone") String phone);

	int countExtensionCenterByStrategicPartnerId(@Param("strategicPartnerId") Long strategicPartnerId);

}