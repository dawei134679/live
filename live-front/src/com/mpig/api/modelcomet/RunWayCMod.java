package com.mpig.api.modelcomet;

public class RunWayCMod extends BaseCMod {
	public RunWayCMod(){
		this.setCometProtocol(CModProtocol.gift_runway);
	}
	
	private int anchorUid;
	public int getAnchorUid() {
		return anchorUid;
	}
	public String getAnchorName() {
		return anchorName;
	}
	public void setAnchorUid(int anchorUid) {
		this.anchorUid = anchorUid;
	}
	public void setAnchorName(String anchorName) {
		this.anchorName = anchorName;
	}

	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

	private String anchorName;
	
	private Object data;
}
