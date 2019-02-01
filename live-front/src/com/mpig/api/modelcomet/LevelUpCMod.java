package com.mpig.api.modelcomet;

/**
 * 
 * @author tosy
 * 升级通知，聊天服务器广播
 * 
 * type		uid的类型		主播还是用户	0用户	1主播
 */
public class LevelUpCMod extends BaseCMod{

	public LevelUpCMod(){
		this.setCometProtocol(CModProtocol.LEVEL_UP);
	}
	
	private Integer type;
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}
