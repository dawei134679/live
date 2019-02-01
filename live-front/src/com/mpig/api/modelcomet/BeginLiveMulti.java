package com.mpig.api.modelcomet;

/**
 * @author tosy
 * 开播广播
 */
public class BeginLiveMulti extends BaseCMod{
	public BeginLiveMulti(){
		this.setCometProtocol(CModProtocol.BEGIN_LIVE_M);
	}
	private String msg;
	private String videoDomain;
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getVideoDomain() {
		return videoDomain;
	}
	public void setVideoDomain(String videoDomain) {
		this.videoDomain = videoDomain;
	}
	
}
