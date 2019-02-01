package com.hkzb.game.common.utils;

public class CModProtocol {
	final static Integer ENTER_ROOM = 1; // 进房
	final static Integer GIFT_SEND = 2; // 送礼
	final static Integer LEVEL_UP = 3; // 升级
	final static Integer FOLLOW = 4; // 关注

	final static Integer BEGIN_LIVE_M = 5; // 开播 同服务端（广播）

	final static Integer GIFT_Danmaku = 6; // 弹幕
	final static Integer Exit_Room = 7;// 退房

	final static Integer Prohibit_Speak = 8;// 禁言

	final static Integer Notice = 9;// 公告 [已被占用]
	final static Integer userNotice = 10;// 用户公告[已被占用]

	final static Integer Anchor_Off = 11;// 主播异常掉线

	final static Integer To_Share = 12;// 分享
	final static Integer Add_Like = 13;// 点赞

	final static Integer UPDATE_IDENTITY = 14;// 修改用户身份 1.主播 2. 普通用户

	final static Integer UPDATE_ACCOUNT_STATUS = 15;// 修改账户状态 0 冻结 1 正常

	final static Integer VIOLATION_WARNING = 16; // 违规警告

	final static int PROHIBIT_SPEAK = 17;// 1禁言 0取消禁言

	final static int SYS_MSG = 18; // 系统消息 1消息 2 跑马灯

	final static int KICK = 19; // 踢出房间

	final static int SET_ADMIN = 20;// 设置管理员 1 设置 0 取消

	final static int Close_Room = 21;// 关房间

	public final static int private_msg = 22;// 私信

	public final static int Gift_Horn = 23;// 喇叭
	
	public final static int gift_runway = 24;//跑道

	public final static int game_runway = 240;//跑道
	
	public final static int room_lucky_notice = 25; //中奖消息
	
	public final static int live_invite = 26; 		//连麦邀请
	public final static int live_invite_ack = 27; 	//连麦回复，及其广播
//	public final static int live_join = 28; 	//连麦开始
//	public final static int live_quit = 29; 	//连麦退出
	public final static int private_room_gifttop = 30; //房间内的个人通知(礼物栏上方)
	public final static int open_service = 31;// 开通服务的房间聊天区提醒
	
	public final static int game_win_notice = 32; // 十二生肖中奖消息
	
	public final static int sys_msg = 40; // 官方私信通知
	public final static int feed_notice = 41; // 动态相关通知
	
	public final static int REDENVELOP_GENERATE_FOR_ANCHOR = 100;// 红包发放给主播
	public final static int REDENVELOP_GENERATE_FOR_ROOM_ALL = 110;// 红包发放给房间所有用户
	public final static int REDENVELOP_GENERATE_FOR_ANCHOR_NOTICE_ALL = 111;// 红包发放给主播并通知房间所有用户
	
	public final static int gameCarUserTotalStake = 401; //押注总额推送
	public final static int setRoomShowUserNum = 402; //设置房间显示人数

	/**
	 * 任务进度发生变更
	 */
	public final static int TASK_PROCESS = 200;
	/**
	 * 任务进度已经完成
	 */
	public final static int TASK_FINISHED = 210;
	
	// 奥运活动
	public final static int olympic = 300;


	public final static int prizeNotify = 1000;	//奖励在线通知
}
