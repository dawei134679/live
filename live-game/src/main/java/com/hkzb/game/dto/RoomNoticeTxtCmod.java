package com.hkzb.game.dto;

import com.hkzb.game.common.utils.CModProtocol;

public class RoomNoticeTxtCmod extends BaseCMod{
	public RoomNoticeTxtCmod(){
		this.setCometProtocol(CModProtocol.room_lucky_notice);
	}
	private Object data;
	private Integer gid;
	private Object luckyMsg;
	private Integer winnersMoney;
	private Integer winnersMultiple;
	private Integer winnersCount;
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public Integer getGid() {
		return gid;
	}
	public void setGid(Integer gid) {
		this.gid = gid;
	}
	public Object getLuckyMsg() {
		return luckyMsg;
	}
	public void setLuckyMsg(Object luckyMsg) {
		this.luckyMsg = luckyMsg;
	}
	public Integer getWinnersMoney() {
		return winnersMoney;
	}
	public void setWinnersMoney(Integer winnersMoney) {
		this.winnersMoney = winnersMoney;
	}
	public Integer getWinnersMultiple() {
		return winnersMultiple;
	}
	public void setWinnersMultiple(Integer winnersMultiple) {
		this.winnersMultiple = winnersMultiple;
	}
	public Integer getWinnersCount() {
		return winnersCount;
	}
	public void setWinnersCount(Integer winnersCount) {
		this.winnersCount = winnersCount;
	}
	
	
}
