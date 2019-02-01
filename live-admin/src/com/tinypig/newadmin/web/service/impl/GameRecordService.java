package com.tinypig.newadmin.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinypig.newadmin.web.dao.GameRecordDao;
import com.tinypig.newadmin.web.entity.GameRecordStaDto;
import com.tinypig.newadmin.web.service.IGameRecordService;
import com.tinypig.newadmin.web.vo.GameRecordVo;

@Service
public class GameRecordService implements IGameRecordService {
	@Autowired
	private GameRecordDao gameCarDao;

	@Override
	public Map<String, Object> gameRecordList(GameRecordStaDto gameRecordStaDto) {
		Integer startIndex = (gameRecordStaDto.getPage() - 1) * gameRecordStaDto.getRows();
		gameRecordStaDto.setStartIndex(startIndex);
		gameRecordStaDto.setPageSize(gameRecordStaDto.getRows());
		Map<String, Object> map = new HashMap<String, Object>();
		List<GameRecordVo> list = gameCarDao.gameRecordSta(gameRecordStaDto);
		Long total = gameCarDao.gameRecordCount(gameRecordStaDto);
		map.put("total", total);
		map.put("rows", list);
		return map;
	}

	@Override
	public List<Map<String, Object>> getGameRecordTotal(GameRecordStaDto gameRecordStaDto) {
		return gameCarDao.getGameRecordTotal(gameRecordStaDto);
	}

	@Override
	public List<Map<String, Object>> gameAllRecordSta(GameRecordStaDto param) {
		return gameCarDao.gameAllRecordSta(param);
	}

}
