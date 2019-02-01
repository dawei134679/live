package com.hkzb.game.service;

import com.hkzb.game.dto.BaseCMod;

public interface IMessageService {
	/**
	 * 游戏在押注总额推送
	 * @param json 消息内容
	 * @param uid 主播ID
	 */
	public void pushRoomTotalStakeMsg(String content, Long uid);

	public void roomAllNotice(String uname, Long money);

	public void roomNotice(Long anchorid, Integer uid, String uname, Long money);

	/**
	 * im房间聊天推送
	 */
	public void roomChatNotice(Long anchorid, BaseCMod msgBody);

	/**
	 * im世界推送
	 * @param msgBody
	 */
	public void roomWorldNotice(Long anchoruid,BaseCMod msgBody);
}
