package com.mpig.api.modelcomet;

public class GameWinNoticeCmod extends BaseCMod{
	public GameWinNoticeCmod(){
		this.setCometProtocol(CModProtocol.game_win_notice);
	}

	private int anchorUid;
	private String anchorName;
	private int money;
	private Object winMsg;
	
	public int getAnchorUid() {
		return anchorUid;
	}
	public void setAnchorUid(int anchorUid) {
		this.anchorUid = anchorUid;
	}
	public String getAnchorName() {
		return anchorName;
	}
	public void setAnchorName(String anchorName) {
		this.anchorName = anchorName;
	}
	public Object getWinMsg() {
		return winMsg;
	}
	public void setWinMsg(Object winMsg) {
		this.winMsg = winMsg;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
}
