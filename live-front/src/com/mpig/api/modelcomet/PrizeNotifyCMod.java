package com.mpig.api.modelcomet;


/*
 * 奖励，宝箱通知协议
 */
public class PrizeNotifyCMod extends BaseCMod {
	public PrizeNotifyCMod(){
		this.setCometProtocol(CModProtocol.prizeNotify);		//public final static int prizeNotify = 1000;	//奖励在线通知
	}
	
	private String prize = null;		//奖励活动对应的token
	private long durHide = 0;			//多少秒时间隐藏提示  大于0才有作用
	private long res = 0;				//活动展示资源ID	默认先为0
	private String desc = null;			//奖励活动对应的语言提示
	
	
	public String getPrize() {
		return prize;
	}
	public void setPrize(String prize) {
		this.prize = prize;
	}
	public long getDurHide() {
		return durHide;
	}
	public void setDurHide(long durHide) {
		this.durHide = durHide;
	}
	public long getRes() {
		return res;
	}
	public void setRes(long res) {
		this.res = res;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

//	private long durOpen = 0;			//多少秒时间可点击
//	public long getDurOpen() {
//		return durOpen;
//	}
//	public void setDurOpen(long durOpen) {
//		this.durOpen = durOpen;
//	}
}
