package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.UserXiaozhuAuth;
import com.tinypig.newadmin.web.entity.UserXiaozhuAuthWithBLOBs;

public interface XiaozhuAuthService {

	Integer selectCount(UserXiaozhuAuth auth);
	
	List<UserXiaozhuAuth> selectList(UserXiaozhuAuth auth);
	
	UserXiaozhuAuthWithBLOBs selectByPrimaryKey(Integer id);
	
	Boolean updateByPrimaryKey(Map<String, Object> map);
	
	Boolean rejectByPrimaryKey(Map<String, Object> map);
	
	UserXiaozhuAuth checkNickName(String nickname);
}
