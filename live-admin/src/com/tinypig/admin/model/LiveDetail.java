package com.tinypig.admin.model;

public class LiveDetail {
	/**直播日期*/
	private String date;
	/**直播时长(秒)*/
	private int liveSec;
	/**直播开始时间*/
	private int liveStartTime;
	/**直播结束时间*/
	private int liveEndTime;
	
	private String os;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getLiveSec() {
		return liveSec;
	}
	public void setLiveSec(int liveSec) {
		this.liveSec = liveSec;
	}
	public int getLiveStartTime() {
		return liveStartTime;
	}
	public void setLiveStartTime(int liveStartTime) {
		this.liveStartTime = liveStartTime;
	}
	public int getLiveEndTime() {
		return liveEndTime;
	}
	public void setLiveEndTime(int liveEndTime) {
		this.liveEndTime = liveEndTime;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	
}
