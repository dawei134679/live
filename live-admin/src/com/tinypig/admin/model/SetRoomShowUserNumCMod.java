package com.tinypig.admin.model;

public class SetRoomShowUserNumCMod extends BaseCMod {
	
	public SetRoomShowUserNumCMod() {
		setCometProtocol(402);
	}
	
	/**
	 * 房间显示的人数
	 */
	private Integer showUserNum;

	public Integer getShowUserNum() {
		return showUserNum;
	}

	public void setShowUserNum(Integer showUserNum) {
		this.showUserNum = showUserNum;
	}
}
