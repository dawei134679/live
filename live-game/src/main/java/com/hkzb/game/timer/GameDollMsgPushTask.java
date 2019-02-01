package com.hkzb.game.timer;

import org.apache.log4j.Logger;

import com.hkzb.game.common.utils.SpringContextUtil;
import com.hkzb.game.dto.BaseCMod;
import com.hkzb.game.service.IMessageService;

/**
 * 抓娃娃中奖推送
 */
public class GameDollMsgPushTask implements IAsyncTask {

	private Long anchorId;
	private BaseCMod msgBody;
	private int type;// 1世界 2中间房间

	private static Logger log = Logger.getLogger(GameDollMsgPushTask.class);
	private IMessageService messageService = SpringContextUtil.getBean("messageService");

	/**
	 * 房间推送
	 * @param type 1世界推送 2房间推送
	 * @param anchorId
	 * @param msgBody
	 */
	public GameDollMsgPushTask(int type, Long anchorId, BaseCMod msgBody) {
		this.type = type;
		this.anchorId = anchorId;
		this.msgBody = msgBody;
	}

	@Override
	public void runAsync() {
		try {
			Thread.sleep(1000 * 2);
		} catch (InterruptedException e1) {
			log.error(e1.getMessage(), e1);
		}
		log.info(String.format("主播ID:%s,%s", anchorId, "抓娃娃消息推送-开始"));
		if (type == 1) {
			messageService.roomWorldNotice(anchorId, msgBody);
		} else if(type == 2){
			messageService.roomChatNotice(anchorId, msgBody);
		}
		log.info(String.format("主播ID:%s,抓娃娃消息推送-结束", anchorId));
	}
}