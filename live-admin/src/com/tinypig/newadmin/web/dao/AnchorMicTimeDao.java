package com.tinypig.newadmin.web.dao;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.AnchorMicTimeParamDto;
import com.tinypig.newadmin.web.entity.LiveMicTime;

public interface AnchorMicTimeDao {
	
	List<Map<String, Object>> getAnchorMicTimeList(AnchorMicTimeParamDto param);
	
	Integer getAnchorMicTimeTotal(AnchorMicTimeParamDto param);
	
	List<Map<String, Object>> getAllAnchorMicTimeList(AnchorMicTimeParamDto param);
	
	List<LiveMicTime> getAnchorLiveSta(AnchorMicTimeParamDto param);
	
	Integer getAllAnchorTime(AnchorMicTimeParamDto param);
	
}