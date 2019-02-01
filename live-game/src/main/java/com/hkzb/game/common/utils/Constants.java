package com.hkzb.game.common.utils;

public class Constants {

	/** 汽车押注游戏配置 key */
	public final static String gameCarConfig = "game:carConfig";
	/** 房间游戏信息 key */
	public final static String gameCarTime = "game:carTime";
	/** 开奖间隔 */
	public final static int time = 60;
	/** 房间押注key */
	public final static String gameCarUserStake = "game:cus";
	/** 房间总押注key */
	public final static String gameCarUserTotalStake = "game:cuts";
	/** 房间押注key */
	public final static String gameCarUsertakeIdx = "game:crs:idx";
	/** 命中概率 */
	public final static String gameCarPR = "game:carPR";
	/** 命中概率和倍数 */
	public final static String gameCarPRAndBS = "game:carPRAndBS";
	/** 游戏平台设置（中1 2 3概率 ） */
	public final static String gameCarLotteryTypePR = "game:cltpr";
	/** 游戏平台设置（抽成 比例） */
	public final static String gameCarCommission = "game:carCSN";
	/**游戏通知金币数设置(直播间,平台)*/
	public final static String gameInformMoney = "game:informMoney";
	
	/** 开奖间隔 */
	public final static int redisBlpopTimeout = 60;

	public final static int status_1 = 1;
	public final static int status_2 = 2;
	public final static int status_3 = 3;

	/** 默认页码 */
	public final static int defaultPageNo = 1;
	/** 默认每页显示条数 */
	public final static int defaultPageSize = 10;

	public final static String code_3000 = "3000";
	public final static String msg_3000 = "未开奖";
	/**可信用户Host*/
	public final static String gameTrustedHost = "game:trusted:host";
	
	/** 中一 */
	public final static int lotteryType1=1;
	public final static String PR1 = "probability1";
	/** 中二 */
	public final static int lotteryType2=2;
	public final static String PR2 = "probability2";
	/** 中三 */
	public final static int lotteryType3=3;
	public final static String PR3 = "probability3";
	
	/** 中奖消息*/
	public final static String gameMsg = "game:msg:";
	
	/** 抓到娃娃推送消息 */
	public final static String gameMsgGraspDoll = "game:msg:graspdoll";	

	/**抓娃娃游戏列表缓存*/
	public static final String gameDollConfig = "game:dollConfig";
	
	/**抓娃娃游戏(一,二,三级爪子金币数)*/
	public final static String gameDollTypeClaw = "game:dollTypeClaw";
	
	/**游戏通知金币数设置(直播间,平台)*/
	public final static String gameDollInformMoney = "game:dollInformMoney";

	/** 用户基本信息*/
	public static final String keyBaseInfoList = "usr:base";
	
	public final static String gameHashList = "game:hlist";
	
	/**游戏房间声援榜 */
	public static final String gameRoomCredit = "rm:game:credit:";
	
	/**六選三 */
	public static final String  gameKey = "gameKey_001";
	
}