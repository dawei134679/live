package com.tinypig.newadmin.web.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tinypig.newadmin.web.entity.MemberInfo;
import com.tinypig.newadmin.web.entity.MemberInfoParentParamDto;
import com.tinypig.newadmin.web.entity.MemberParamDto;

public interface MemberInfoDao {

	List<MemberInfo> getMemberListPage(MemberParamDto param);

	Integer getMemberTotal(MemberParamDto param);

	MemberInfo getMemberInfoById(@Param("suffix") String suffix, @Param("uid") Integer uid);

	List<Map<String, Object>> getAllMemberList(MemberParamDto param);
	
	 int deleteByPrimaryKey(Integer uid);

    int insert(MemberInfo record);

    int insertSelective(MemberInfo record);

    MemberInfo selectByPrimaryKey(Integer uid);

    int updateByPrimaryKeySelective(MemberInfo record);

    int updateByPrimaryKey(MemberInfo record);

	int updateStrategicPartnerInfo(MemberInfoParentParamDto params);

	int updateExtensionCenterAndParentInfo(MemberInfoParentParamDto params);

	int updatePromotersAndParentInfo(MemberInfoParentParamDto params);

	int updateAgentUserAndParentInfo(MemberInfoParentParamDto params);

	int updateSalesmanAndParentInfo(MemberInfoParentParamDto params);

	int updateUserAndParentInfo(MemberInfoParentParamDto params);
}