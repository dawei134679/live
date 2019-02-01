package com.mpig.api.modelcomet;

public class AnchorOffCMod extends BaseCMod {
	public AnchorOffCMod(){
		this.setCometProtocol(CModProtocol.Anchor_Off);
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	private int type;
}
