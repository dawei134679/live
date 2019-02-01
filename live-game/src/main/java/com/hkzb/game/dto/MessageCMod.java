package com.hkzb.game.dto;

import com.hkzb.game.common.utils.CModProtocol;

public class MessageCMod extends BaseCMod {
	
	public MessageCMod() {
		this.setCometProtocol(CModProtocol.private_msg);
	}

	/** 消息内容 */
	private Object msg;

	public Object getMsg() {
		return msg;
	}

	public void setMsg(Object msg) {
		this.msg = msg;
	}
}
