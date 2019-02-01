package com.hkzb.game.timer;

import org.apache.log4j.Logger;

import com.hkzb.game.common.utils.Constants;
import com.hkzb.game.common.utils.DateUtil;
import com.hkzb.game.common.utils.JsonUtil;
import com.hkzb.game.common.utils.RedisShardClient;
import com.hkzb.game.common.utils.SpringContextUtil;
import com.hkzb.game.dto.GameCarTimeDto;
import com.hkzb.game.service.IGameCarService;

public class GameCarRoomTask implements IAsyncTask {

	private static Logger log = Logger.getLogger(GameCarRoomTask.class);

	private RedisShardClient redisShardClient = SpringContextUtil.getBean("redisShardClient");

	private IGameCarService gameCarService = SpringContextUtil.getBean("gameCarService");

	@Override
	public void runAsync() {
		while (true) {
			try {
				Thread.sleep(1000);
				String json = redisShardClient.get(Constants.gameCarTime);
				GameCarTimeDto gameCarTimeDto = JsonUtil.toBean(json, GameCarTimeDto.class);
				long ttl = (DateUtil.nowTime() / 1000) - gameCarTimeDto.getTime();
				if (ttl >= Constants.time) {
					gameCarService.lottery();
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}
}