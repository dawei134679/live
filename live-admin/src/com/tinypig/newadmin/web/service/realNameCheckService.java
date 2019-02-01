package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.RealNameParamDto;

public interface realNameCheckService {
	
	Map<String, Object> getRealNameListPage(RealNameParamDto param);
	
	List<Map<String, Object>> getAllRealNameList(RealNameParamDto param);
}
