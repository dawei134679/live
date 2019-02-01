package com.mpig.api.modelcomet;

public class DanmakuCMod extends BaseCMod {
	public DanmakuCMod(){
		this.setCometProtocol(CModProtocol.GIFT_Danmaku);
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	private String msg;
}
