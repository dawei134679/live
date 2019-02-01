package com.mpig.api.dictionary;

import java.io.Serializable;

/**
 * 游戏app对象
 * @author jackzhang
 */
public class GameAppConfig implements Serializable{
	private static final long serialVersionUID = 1L;
	public int appId;
	public String appName;
	public String appIcon;
	public String roomId;
	public String serverAddress;
	public int requireLv;
	public String requireDesc;
	
	public GameAppConfig initWith(int appId,String appName,String roomId,String serverAddress,String appIcon){
		this.appId = appId;
		this.appName = appName;
		this.appIcon = appIcon;
		this.roomId = roomId;
		this.serverAddress = serverAddress;
		return this;
	}
	
	public GameAppConfig initWithRequire(int requireLv,String requireDesc){
		this.requireLv = requireLv;
		this.requireDesc = requireDesc;
		return this;
	}
}