package com.tinypig.admin.model;

public class RoomRobotSpeakCMod extends BaseCMod {
	
	public RoomRobotSpeakCMod() {
		setCometProtocol(403);
	}
	
	/**消息内容*/
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
