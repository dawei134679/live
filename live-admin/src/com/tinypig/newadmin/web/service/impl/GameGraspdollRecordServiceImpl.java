package com.tinypig.newadmin.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinypig.newadmin.web.dao.GameGraspdollRecordDao;
import com.tinypig.newadmin.web.entity.GameGraspdollRecord;
import com.tinypig.newadmin.web.entity.GameGraspdollRecordParamDto;
import com.tinypig.newadmin.web.service.IGameGraspdollRecordService;

@Service
public class GameGraspdollRecordServiceImpl implements IGameGraspdollRecordService{

	@Autowired
	private GameGraspdollRecordDao gameGraspdollRecordDao;
	
	@Override
	public Map<String,Object> getGameGraspdollRecordPage(GameGraspdollRecordParamDto param) {
		Map<String,Object> map = new HashMap<String,Object>();
		Integer page = param.getPage();
		Integer rows = param.getRows();
		Integer startIndex = (page-1)*rows;
		param.setStartIndex(startIndex);
		map.put("rows", gameGraspdollRecordDao.getGameGraspdollRecordPage(param));
		map.put("total", gameGraspdollRecordDao.getGameGraspdollRecordTotalCount(param));
		return map;
	}

	@Override
	public GameGraspdollRecord getGameGraspdollRecordTotal(GameGraspdollRecordParamDto param) {
		return gameGraspdollRecordDao.getGameGraspdollRecordTotal(param);
	}
}
