package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.SupportUser;
import com.tinypig.newadmin.web.entity.SupportUserParamDto;

public interface SupportUserServie {
	Map<String, Object> getSupportUserList(SupportUserParamDto params);

	Map<String, Object> saveSupportUser(SupportUser supportUser);

	Map<String, Object> updateSupportUser(SupportUser SspportUser);
	
	Map<String, Object> doValid(Long id, Integer uid, Integer status, Long updateUserId, Long updateTime);

	List<Map<String, Object>> getAllSupportUserList(SupportUserParamDto param);

}
