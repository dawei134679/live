package com.tinypig.newadmin.web.service;

import java.util.Map;

import com.tinypig.newadmin.web.entity.SysVersion;

public interface SysVersionService {

	int insertSelective(SysVersion record);
	
	Map<String, Object> getVersionList();
}
