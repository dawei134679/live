package com.tinypig.newadmin.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tinypig.newadmin.web.entity.StrategicPartner;
import com.tinypig.newadmin.web.entity.StrategicPartnerParamDto;

public interface StrategicPartnerDao {

	int deleteByPrimaryKey(Long id);

	int insert(StrategicPartner record);

	int insertSelective(StrategicPartner record);

	StrategicPartner selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(StrategicPartner record);

	int updateByPrimaryKey(StrategicPartner record);

	List<StrategicPartner> getStrategicPartnerListPage(StrategicPartnerParamDto params);

	Integer getStrategicPartnerTotal(StrategicPartnerParamDto params);

	StrategicPartner getStrategicPartnerByPhone(@Param("phone") String phone);

}