package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.GameRecordStaDto;

public interface IGameRecordService {

	Map<String, Object> gameRecordList(GameRecordStaDto gameRecordStaDto);

	List<Map<String, Object>> getGameRecordTotal(GameRecordStaDto gameRecordStaDto);
	
	List<Map<String, Object>> gameAllRecordSta(GameRecordStaDto param);


}
