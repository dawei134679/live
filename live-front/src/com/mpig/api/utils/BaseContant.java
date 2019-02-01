package com.mpig.api.utils;

/**
 * 基础常量配置
 * @author zyl
 * @time 2016-7-7
 */
public class BaseContant {

	//base config
	public static final String CONFIG_OPENSCREEM_ANDROID = "openscreen_android"; //android 开屏
	public static final String CONFIG_OPENSCREEM_IOS = "openscreen_ios";  //ios开屏
	
	//房间公告属性相关
	public static final String CONFIG_ROOM_NOTICE = "roomNotice";
	public static final String CONFIG_XIAOZHU_NOTICE = "xiaozhuNotice";
	
	//活动配置相关
	public static final String CONFIG_ACTIVITY = "activity";
	public static final String CONFIG_ACTMIDLLE = "act_midlle";
	public static final String CONFIG_ACTRIGHTTOP = "act_righttop"; 
	
	public final static String accessKey = Constant.qn_accessKey;
	public final static String secretKey = Constant.qn_secretKey;	
	
	
	//最新入驻数量调整
	public final static int newJoinSize = 30;
	
	//暴风的跳转链接
	public final static String Baofeng_Url = "http://show.baofeng.com/";
	public final static String Baofeng_channel = "?channel=baofeng_"; //暴风渠道
	public final static String Baofeng_channel_box = "6"; //暴风盒子
	public final static String Baofeng_channel_cardInfo = "12"; //暴风盒子
}
