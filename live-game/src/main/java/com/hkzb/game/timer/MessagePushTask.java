package com.hkzb.game.timer;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hkzb.game.common.utils.Constants;
import com.hkzb.game.common.utils.JsonUtil;
import com.hkzb.game.common.utils.RedisShardClient;
import com.hkzb.game.common.utils.SpringContextUtil;
import com.hkzb.game.dto.GameCarMessageDto;
import com.hkzb.game.service.IMessageService;

/**
 * 中奖消息推送类
 */
public class MessagePushTask implements IAsyncTask {

	private Long periods;
	
	private static Logger log = Logger.getLogger(MessagePushTask.class);

	private RedisShardClient redisShardClient = SpringContextUtil.getBean("redisShardClient");

	private IMessageService messageService = SpringContextUtil.getBean("messageService");

	public MessagePushTask(Long periods) {
		this.periods = periods;
	}

	@Override
	public void runAsync() {
		try {
			Thread.sleep(1000*5);
		} catch (InterruptedException e1) {
			log.error(e1.getMessage(),e1);
		}
		log.info(String.format("期数:%s,%s",periods,"消息推送-开始"));
		String msgKey = String.format("%s%s", Constants.gameMsg,periods);
		while (true) {
			try {
				List<String> list = redisShardClient.brpop(2,msgKey);
				if(list==null||list.size()==0) {
					break;
				}
				for (int i = 1; i < list.size(); i++) {
					String json = list.get(i);
					if(StringUtils.isBlank(json)) {
						break;
					}
					GameCarMessageDto message = JsonUtil.toBean(json, GameCarMessageDto.class);
					if(message==null) {
						continue;
					}
					if(1==message.getMesgType()) {
						messageService.roomNotice(message.getAnchorId(),Integer.parseInt(message.getUid().toString()), message.getUname(), message.getMoney());
					}else {
						messageService.roomAllNotice(message.getUname(), message.getMoney());
					}
				}
			} catch (Exception e) {
				log.error("推送消息异常",e);
			}
		}
		redisShardClient.del(msgKey);
		log.info(String.format("期数:%s,%s",periods,"消息推送-結束"));
	}
}