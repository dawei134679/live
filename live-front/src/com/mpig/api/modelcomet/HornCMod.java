package com.mpig.api.modelcomet;

public class HornCMod extends BaseCMod {
	public HornCMod(){
		this.setCometProtocol(CModProtocol.Gift_Horn);
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getAnchoruid() {
		return anchoruid;
	}
	public void setAnchoruid(int anchoruid) {
		this.anchoruid = anchoruid;
	}
	
	public String getAnchorName() {
		return anchorName;
	}
	public void setAnchorName(String anchorName) {
		this.anchorName = anchorName;
	}

	private String msg;
	private int anchoruid;
	private String anchorName;
}
