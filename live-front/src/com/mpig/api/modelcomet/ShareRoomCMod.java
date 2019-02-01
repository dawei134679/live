package com.mpig.api.modelcomet;

public class ShareRoomCMod extends BaseCMod{
	public ShareRoomCMod(){
	 this.setCometProtocol(CModProtocol.To_Share);
	}
	private Object data;
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
}
