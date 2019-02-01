package com.mpig.api.dictionary;

public class ActGiftConfig {

	private int gid;
	private int num; 	// 礼物个数
	private int type;	// 礼物类型
	private int times;	// 礼物显示时效

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getGid() {
		return gid;
	}

	public int getNum() {
		return num;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

}
