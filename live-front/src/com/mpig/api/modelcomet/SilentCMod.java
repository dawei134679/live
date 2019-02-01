package com.mpig.api.modelcomet;

public class SilentCMod extends BaseCMod {
	public SilentCMod() {
		this.setCometProtocol(CModProtocol.PROHIBIT_SPEAK);
	}

	private int status; // 1禁言 0取消禁言

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
