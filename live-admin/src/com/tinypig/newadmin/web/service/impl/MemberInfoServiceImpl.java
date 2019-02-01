package com.tinypig.newadmin.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinypig.newadmin.web.dao.MemberInfoDao;
import com.tinypig.newadmin.web.dao.UserOrgRelationDao;
import com.tinypig.newadmin.web.entity.MemberInfo;
import com.tinypig.newadmin.web.entity.MemberInfoParentParamDto;
import com.tinypig.newadmin.web.entity.MemberParamDto;
import com.tinypig.newadmin.web.entity.UserOrgRelation;
import com.tinypig.newadmin.web.service.MemberInfoService;

@Service
public class MemberInfoServiceImpl implements MemberInfoService{

	@Autowired
	private MemberInfoDao memberInfoDao;
	
	@Autowired
	private UserOrgRelationDao userOrgRelationDao;
	
	@Override
	public Map<String,Object> getMemberListPage(MemberParamDto param) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("rows", memberInfoDao.getMemberListPage(param));
		result.put("total", memberInfoDao.getMemberTotal(param));
		return  result;
	}

	@Override
	public int modifyMemberInfoRelation(UserOrgRelation baen) {
		return userOrgRelationDao.updateByPrimaryUidSelective(baen);
	}

	@Override
	public MemberInfo getMemberInfoById(Integer uid) {
		String suffix = "";
		return memberInfoDao.getMemberInfoById(getSqlString(suffix, uid), uid);
	}
	
	public static String getSqlString(String tableName, Integer uid) {
		int sufix = uid % 100;
		return sufix < 10 ? tableName + "0" + sufix : tableName + sufix;
	}

	@Override
	public List<Map<String, Object>> getAllMemberList(MemberParamDto param) {
		return memberInfoDao.getAllMemberList(param);
	}
	@Override
	public int updateStrategicPartnerInfo(MemberInfoParentParamDto params) {
		return memberInfoDao.updateStrategicPartnerInfo(params);
	}

	@Override
	public int updateExtensionCenterAndParentInfo(MemberInfoParentParamDto params) {
		return memberInfoDao.updateExtensionCenterAndParentInfo(params);
	}

	@Override
	public int updatePromotersAndParentInfo(MemberInfoParentParamDto params) {
		return memberInfoDao.updatePromotersAndParentInfo(params);
	}

	@Override
	public int updateAgentUserAndParentInfo(MemberInfoParentParamDto params) {
		return memberInfoDao.updateAgentUserAndParentInfo(params);
	}

	@Override
	public int updateSalesmanAndParentInfo(MemberInfoParentParamDto params) {
		return memberInfoDao.updateSalesmanAndParentInfo(params);
	}

	@Override
	public int updateUserAndParentInfo(MemberInfoParentParamDto params) {
		return memberInfoDao.updateUserAndParentInfo(params); 
	}
}
