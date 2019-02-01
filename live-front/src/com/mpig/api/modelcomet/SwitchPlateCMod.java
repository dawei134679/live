package com.mpig.api.modelcomet;

public class SwitchPlateCMod extends BaseCMod {
	public SwitchPlateCMod(){
		this.setCometProtocol(CModProtocol.LEVEL_UP);
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private String type;
}
