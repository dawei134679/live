package com.tinypig.newadmin.web.dao;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.GameRecordStaDto;
import com.tinypig.newadmin.web.vo.GameRecordVo;

public interface GameRecordDao {

	List<Map<String, Object>> getGameRecordTotal(GameRecordStaDto gameRecordStaDto);

	List<GameRecordVo> gameRecordSta(GameRecordStaDto gameRecordStaDto);

	Long gameRecordCount(GameRecordStaDto gameRecordStaDto);
	
	List<Map<String, Object>> gameAllRecordSta(GameRecordStaDto param);

}
