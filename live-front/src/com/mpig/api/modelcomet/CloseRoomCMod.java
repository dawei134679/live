package com.mpig.api.modelcomet;

/**
 * 关闭本场直播
 * @author fangwuqing
 *
 */
public class CloseRoomCMod extends BaseCMod {
	   public CloseRoomCMod() {
	        this.setCometProtocol(CModProtocol.Close_Room);
	    }
}
