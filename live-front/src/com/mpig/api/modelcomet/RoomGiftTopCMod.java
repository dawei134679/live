package com.mpig.api.modelcomet;

public class RoomGiftTopCMod extends BaseCMod{
	public RoomGiftTopCMod(){
		this.setCometProtocol(CModProtocol.private_room_gifttop);
	}
	private int money;
	private Object data;
	private Integer winnersMoney;
	private Integer winnersMultiple;
	private Integer winnersCount;
	private Object giftList;
	
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public Object getGiftList() {
		return giftList;
	}
	public void setGiftList(Object giftList) {
		this.giftList = giftList;
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
