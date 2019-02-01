package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.AnchorInfoParamDto;

public interface AnchorInfoService {
	
	Map<String, Object>  getAnchorList(AnchorInfoParamDto param);
	
	public List<Map<String,Object>>  getAnchorAllList(AnchorInfoParamDto param) ;

}
