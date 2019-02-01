package com.mpig.api.modelcomet;

public class ManageRoomCMod extends BaseCMod {
	public ManageRoomCMod() {
		this.setCometProtocol(CModProtocol.SET_ADMIN);
	}

	private int status;// 1 设置 0 取消

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
