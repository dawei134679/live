package com.mpig.api.model;

/**
 * 用户进入直播间的聊天配置信息
 * @author fang
 *
 */
public class ChatInfoModel {

	private String ip;
	private String port;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
}
