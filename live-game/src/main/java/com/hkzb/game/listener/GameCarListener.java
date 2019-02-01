package com.hkzb.game.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hkzb.game.common.utils.Constants;
import com.hkzb.game.common.utils.DateUtil;
import com.hkzb.game.common.utils.JsonUtil;
import com.hkzb.game.common.utils.RedisShardClient;
import com.hkzb.game.common.utils.SpringContextUtil;
import com.hkzb.game.dto.GameCarTimeDto;
import com.hkzb.game.model.GameCarRecord;
import com.hkzb.game.service.IGameCarService;
import com.hkzb.game.timer.AsyncManager;
import com.hkzb.game.timer.GameCarRoomTask;
import com.mashape.unirest.http.Unirest;

public class GameCarListener implements ServletContextListener {

	private static Logger log = Logger.getLogger(GameCarListener.class);
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		 log.info("GameCar初始化-begin");
		 RedisShardClient redisShardClient = SpringContextUtil.getBean("redisShardClient");
		 IGameCarService gameCarService = SpringContextUtil.getBean("gameCarService");
		 
		 String json = redisShardClient.get(Constants.gameCarTime);
		 if(StringUtils.isBlank(json)) {
			GameCarTimeDto gameCarTimeDto = new GameCarTimeDto();
			Long periods = gameCarService.getMaxPeriods();
			if(periods==0) {
				//需要初始化数据库
				GameCarRecord record = new GameCarRecord();
				record.setAnchorId(0L);
				record.setRoomId(0L);
				record.setPeriods(1L);
				gameCarService.saveGameCarRecordSelective(record);
			}
			gameCarTimeDto.setPeriods(periods+1);
			gameCarTimeDto.setTime(DateUtil.nowTime()/1000);
			redisShardClient.set(Constants.gameCarTime, JsonUtil.toJson(gameCarTimeDto));
		 }
		 AsyncManager.getInstance().execute(new GameCarRoomTask());
		 log.info("GameCar初始化-end");
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		try {
			AsyncManager.getInstance().onDestroyed();
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		try {
			Unirest.shutdown();
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
}
