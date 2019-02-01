package com.mpig.api.model;

import java.util.List;
import java.util.Map;

/*
 * 通用活动，领取奖励model		-----    ActivitesModeController acceptPrize
 */
public class PrizeModel {
//	long wealth = 0;							//获得的经验
//	long criedt = 0;							//获得的声援
//	long money = 0;
//	List<Map<Integer,Integer>> gifts = null;	//获得的礼物列表，map中gid和个数
	String desc = null;							//语言提示信息
	
//	public long getWealth() {
//		return wealth;
//	}
//	public void setWealth(long wealth) {
//		this.wealth = wealth;
//	}
//	public long getCriedt() {
//		return criedt;
//	}
//	public void setCriedt(long criedt) {
//		this.criedt = criedt;
//	}
//	public List<Map<Integer, Integer>> getGifts() {
//		return gifts;
//	}
//	public void setGifts(List<Map<Integer, Integer>> gifts) {
//		this.gifts = gifts;
//	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
