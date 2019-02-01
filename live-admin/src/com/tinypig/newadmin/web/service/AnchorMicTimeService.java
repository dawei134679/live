package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.AnchorMicTimeParamDto;
import com.tinypig.newadmin.web.entity.LiveMicTime;
import com.tinypig.newadmin.web.entity.PayOrderSta;
import com.tinypig.newadmin.web.entity.PayOrderStaParam;

public interface AnchorMicTimeService {
	
	public Map<String, Object> getAnchorMicTimeList(AnchorMicTimeParamDto param);
	
	public List<Map<String, Object>> getAllAnchorMicTimeList(AnchorMicTimeParamDto param);
	
	public List<LiveMicTime> getAnchorLiveSta(AnchorMicTimeParamDto param);
	
	public Integer getAllAnchorTime(AnchorMicTimeParamDto param);
	
}
