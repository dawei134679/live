package com.tinypig.admin.protocol;

/**
 * 
 * 业务服务器透传协议
 * @author jackzhang
 *
 */
public class Protocol {
	/**
	 * 系统公告
	 */
	public static final int Notice_Announcement = 4000;
	
	/**
	 * 跑马灯
	 */
	public static final int Notice_Marquee = 4100;
	
	/**
	 * 通知客户端重新拉取推荐和粉丝列表(慎用)
	 */
	public static final int Notice_PullAnchors = 4200;
	
	/**
	 * 通知客户端推荐和粉丝列表移除某人
	 */
	public static final int Notice_RemoveSomeAnchor = 4300;
}
