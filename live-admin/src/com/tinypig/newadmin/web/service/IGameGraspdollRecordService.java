package com.tinypig.newadmin.web.service;

import java.util.Map;

import com.tinypig.newadmin.web.entity.GameGraspdollRecord;
import com.tinypig.newadmin.web.entity.GameGraspdollRecordParamDto;

public interface IGameGraspdollRecordService {
		
    Map<String,Object> getGameGraspdollRecordPage(GameGraspdollRecordParamDto param);
    
    GameGraspdollRecord getGameGraspdollRecordTotal(GameGraspdollRecordParamDto param);
}
