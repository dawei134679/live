package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.MemberInfo;
import com.tinypig.newadmin.web.entity.MemberInfoParentParamDto;
import com.tinypig.newadmin.web.entity.MemberParamDto;
import com.tinypig.newadmin.web.entity.UserOrgRelation;

public interface MemberInfoService {
	
	Map<String,Object> getMemberListPage(MemberParamDto param);
	
	int modifyMemberInfoRelation(UserOrgRelation bean);
	
	MemberInfo getMemberInfoById(Integer uid);

	List<Map<String, Object>> getAllMemberList(MemberParamDto param);
	
	public int updateStrategicPartnerInfo(MemberInfoParentParamDto params);

	public int updateExtensionCenterAndParentInfo(MemberInfoParentParamDto params);

	public int updatePromotersAndParentInfo(MemberInfoParentParamDto params);

	public int updateAgentUserAndParentInfo(MemberInfoParentParamDto params);

	public int updateSalesmanAndParentInfo(MemberInfoParentParamDto params);

	public int updateUserAndParentInfo(MemberInfoParentParamDto params);
}
