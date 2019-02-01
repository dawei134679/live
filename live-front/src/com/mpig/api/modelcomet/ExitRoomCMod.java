package com.mpig.api.modelcomet;

public class ExitRoomCMod extends BaseCMod {

	private long timeslong;// 在线时长
	private int persontimes;// 在线人数
	private int roomLikes;// 喜欢数
	private int creditTotal;// 总声援值
	private Object guardInfo;  //守护相关信息

	public ExitRoomCMod() {
		this.setCometProtocol(CModProtocol.Exit_Room);
	}

	public long getTimeslong() {
		return timeslong;
	}

	public void setTimeslong(long timeslong) {
		this.timeslong = timeslong;
	}

	public int getPersontimes() {
		return persontimes;
	}

	public void setPersontimes(int persontimes) {
		this.persontimes = persontimes;
	}

	public int getRoomLikes() {
		return roomLikes;
	}

	public void setRoomLikes(int roomLikes) {
		this.roomLikes = roomLikes;
	}

	public int getCreditTotal() {
		return creditTotal;
	}

	public void setCreditTotal(int creditTotal) {
		this.creditTotal = creditTotal;
	}

	public Object getGuardInfo() {
		return guardInfo;
	}

	public void setGuardInfo(Object guardInfo) {
		this.guardInfo = guardInfo;
	}
	
	
}
