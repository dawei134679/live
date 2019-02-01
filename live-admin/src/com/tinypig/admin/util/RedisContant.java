package com.tinypig.admin.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class RedisContant {

	private static Logger logger = Logger.getLogger(RedisContant.class);

	public static int port6379 = 0;
	public static int port6380 = 0;
	public static int port6381 = 0;
	public static int port6382 = 0;
	public static String host = null;
	/** redis密码 */
	public static String auth = null;

	public static final String RedisNameUser = "user"; // 记录用户账号、资产、基本信息、开播列表
	public static final String RedisNameRelation = "relation"; // 关注、粉丝、观众、黑名单、好友
	public static final String RedisBlackWords = "words"; // 黑词 敏感词
	public static final String RedisNameOther = "other";// 其他
	public static final String RedisNameFeed = "feed";// 动态

	public static final String keyAssetList = "usr:asset";// "ua&l";
	public static final String keyBaseInfoList = "usr:base";// "ub&l";
	public static final String keyAccountList = "usr:acc";// "uac&l";
	public static final String keyPayAccountList = "pay:acc";// "p&a&l"; //
																// 用户与提现账户的关联
	public static final String keyLiveInfoList = "living:list";// "li&l";
	public static final String keyLiveEnterTotal = "rm:users:";// 记录本场直播进入的人数

	public static final String keyAccountNameAndUid = "aname:uid";// "an&u";
	public static final String keyAuthKeyAndUid = "akey:uid";// "ak&u";
	public static final String keyMbileAndUid = "mb:uid";// 手机与ID的关系

	public static final String keyNicknameList = "nicknamelist";
	public static final String keyAuthkeyList = "authkeylist";

	public static final String KeyLiveInfoList = "r&ing"; // 开播列表

	public static final String KeyRoomRecommend = "rm:recomm";// "r&rec";// 推荐列表
	public static final String KeyRoomHot = "rm:hot";// 热门
	public static final String keyRoomBase = "rm:base";// 普通主播
	public static final String keyRoomHead = "rm:head";// 头牌主播

	public static final String keyRoomSilent = "rm:sl:";// 禁言列表
	public static final String keyRoomKick = "rm;kick";

	public static final String keyRoomManage = "rm:mg:";// 房间管理员列表
	public static final String keyUserManage = "usr:mg:";// 用户管理的房间列表

	public static final String keyFollows = "follows:all:";// "userfollows";//
	public static final String keyFollowTimes = "follows:tm:";// 关注频率显示
																// 总关注数
	public static final String keyPushMsg = "push:";// "a&u&p";
	public static final String keyFans = "fans:";// "userfans"; // 总粉丝数
	public static final String KeyLikes = "rm:like:";// "r&like";// 总喜欢数
	public static final String KeyRoomLikes = "rm:this:";// "r@likes";// 本场喜欢数
	public static final String keyStartLive = "rm:hb";// "r@endt";// 记录主播开始心跳时间
	public static final String KEYROOMUSERLIST = "roomuserlist_";

	public static final String KeyGiftConfigList = "gift:list";// "giftconfiglist_";
	
	public static final String FeedInfo = "feed:info:";// 动态详情 feedid/1000 
	public static final String FeedReply = "feed:reply:"; // 动态回复 feedid
	public static final String FeedReward = "feed:reward:"; // 动态打赏 feedid
	public static final String FeedRewardGift = "feed:rewardGift:"; // 动态打赏礼物 feedid
	public static final String FeedUser = "feed:user:"; // 用户的动态列表 uid
	public static final String FeedUserFollow = "feed:follow:"; // 用户关注的列表 uid
	public static final String FeedSquare = "Feed:Square"; //动态广场
	public static final String FeedLaud = "feed:laud:"; //点赞列表 feedid
	public static final String FeedLaudCount = "feed:laudC:"; //动态点赞数 feedid
	public static final String FeedReplyCount = "feed:replyC:"; //动态评论数 feedid
	public static final String FeedRewardCount = "feed:rewardC:"; //动态打赏数 feedid
	public static final String FeedRewardGiftCount = "feed:rewardGiftC:"; //动态打赏数 feedid

	public static final String MchBillNo = "pay:mchBillNo";// 微信订单编号生成
	public static final String PayNotice = "pay:notic";// 支付公告
	public static final String keyApplyIAP = "app:iap:";// apply 内购使用

	public static final String KeySysText = "sys:text";// 客户端显示提示文字

	public static final String KeySysMsg = "sys:msg";// 官方私信

	public static final String actMidRoom = "act:rm:mid";// 直播间中部的活动

	public static final String VersionAndroid = "version:android";
	
	public static final String VersionIos = "version:ios"; //版本强制更新

	public static final String verifyIos = "verify:ios";// 审核屏蔽功能

	public static final String newAnchorAll = "rm:newjoin:all";// 最新主播
	public static final String newAnchor = "rm:newjoin";// 最新主播
	/**
	 * 用户进房记录
	 */
	public static final String KEYUSERENTERROOM = "userentertime_"; // 关注列表
	// public static final String KeySrcFollwsDst = "src&dst";// src关注了dst
	public static final String KeyUserAndAnchor = "usr:room";// "u&a"; //
																// 用户(user)跟房间(anchor)的关系(1:1)
	public static final String KeyAnchorAndUser = "rm:user:";// "a&u";//
																// 主播跟用户的关系(1:N)
	public static final String KeyUserEnterLong = "rm:intime:";// "u&e&l";//
																// 记录用户在房间里的时长

	public static final String KeyRoomTimes = "rm:times:";// "r&u&s";
															// 房间进入用户人次(不排重)
	// public static final String KeyRoomUsersAll = "rm:usr:all:";//房间总人数（排重）
	public static final String KeyRoomUsers = "r&us";// 房间进入用户数（排重） 随用户的进出而改变
	public static final String KeyRoomGetsToday = "rm:day:gets:";// "r&g&t";//
																	// 当天(today)房间(room)的声援值(gets)
	public static final String KeyDstGetsSrcMoney = "rm:gets:";// "r&g&us";//
																// 主播的声援榜
																// 主播收到礼物的用户列表
	public static final String KeySrcSendDstMoney = "usr:rm:send:";// "u&r&s";//
																	// 记录用户送礼给主播的列表
	public static final String KeyMsgtotal = "mb:send:";// "msg&tl";// 记录月发送短信数量
														// 总数量
	public static final String KeyMsgDetail = "msg&dl";// 记录每月发送短信明细

	public static final String KeyPayRecord = "order:";// "p&r&u";// 充值记录
	public static final String keyWithDrawLimit = "order:limit";// "wd&l";//
																// 提现限制额度
	public static final String keyWithDraws = "usr:draw:";// 记录提现次数

	public static final String KeyMobileSendCode = "mb:code:";// "mobilesendcode_";//
																// 缓存分钟之内手机发送验证次数
	public static final String KeyMobileSendCodeOfDay = "mb:";// "m&s&d";//
																// 缓存一天之内手机发送验证码次数

	public static final String KeyRoomReports = "rm:report:";// "r&rep";// 房间举报数

	public static final String KeyHttpReq = "ncode:";// "httpReq_";//

	public static final String KeySendGiftTotal = "sendgifttotalByUid_";// 记录每个人送礼给每个主播的礼物价值总数

	public static final String keyLivingClick = "click:";// 记录开播时间，阻止频繁开播
	public static final String keyUserIntoRoom = "o:usr:rm:";// 记录用户进入房间
																// 防止用户多次进同一房间
																// 产生多次推送

	public static final String keySupport = "support:uid";// 扶持账号

	public static final String giftver = "gift:ver";// 礼物版本号（整体）

	public static final String homeBanner = "home:banner";// 轮播广告
	public static final String webManager = "web:m"; // app管理员 -后台新增

	public static final String AllAuth = "all:auth:";// 小猪认

	public static final String zxTimes = "zhouxin:times:";

	public static final String User_login = "login:d:";

	public static final String roomChat = "room:chat"; // 直播间中的公告信息

	public static final String WeekTitle = "week:title"; // 周星标签
	/** 房间押注key */
	public static final String gameCarConfig = "game:carConfig";
	/** 命中概率和倍数 */
	public final static String gameCarPRAndBS = "game:carPRAndBS";
	/**房间游戏管理 list*/
	public final static String gameList = "game:list";
	/**房间游戏管理 hash*/
	public final static String gameHashList = "game:hlist";
	/** 游戏平台设置（中1 2 3概率 ） */
	public final static String gameCarLotteryTypePR = "game:cltpr";
	/** 游戏平台设置（抽成 比例） */
	public final static String gameCarCommission = "game:carCSN";
	/** 游戏平台总开关(0为关闭 1为启用)*/
	public final static String gameSwitch = "game:switch";
	/**游戏通知金币数设置(直播间,平台)*/
	public final static String gameInformMoney = "game:informMoney";
	/**砸蛋配置列表缓存*/
	public static final String SmashedEggGiftCfgList = "game:smashedegg:gifts";
	/**砸蛋配置锤子金额缓存*/
	public static final String SmashedEggMoneyCfgList = "game:smashedegg:money";
	/**抓娃娃游戏列表缓存*/
	public static final String gameDollConfig = "game:dollConfig";
	/**抓娃娃游戏(一,二,三级爪子金币数)*/
	public final static String gameDollTypeClaw = "game:dollTypeClaw";
	/**游戏通知金币数设置(直播间,平台)*/
	public final static String gameDollInformMoney = "game:dollInformMoney";
	
	public static final String sysCopyright = "sys:copyrigh";
	/**直播间的真实人数*/
	public static final String roomRealUserNum = "rm:rl:usm";
	/** 直播间机器人信息*/
	public static final String roomRobots = "rm:robots:";
	/**全部机器人基本信息*/
	public static final String keyRobotBaseInfoList = "robot:base";
	/**全部机器人UID 用于随机获取*/
	public static final String keyRobotUidSet = "robot:set";
	/**敏感词汇*/
	public final static String WORDS_SENSITIVE_QUEUE = "words_sensitive_queue";
	/**直播间真实人数信息*/
	public final static String roomUsers = "rm:real:users:";
	/**扶持用户ID*/
	public final static String supportUsersID = "sp:uid";
	/**
	 * 房间守护排序(根据金币排序)
	 */
	public static final String roomAllGuardSort = "room:all:guard:sort:";
	/**微信支付通道*/
	public static final String wxPayWay = "wx:payway";

	
	public static void initialize(String configPath) {
		logger.info("初始化redis.properties - begin");
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(configPath);
			Properties properties = new Properties();
			properties.load(fileReader);
			host = (String) properties.get("redis.host");
			auth = (String) properties.get("redis.auth");
			port6379 = Integer.parseInt((String) properties.get("redis.port"));
			port6380 = Integer.parseInt((String) properties.get("redis.port1"));
			port6381 = Integer.parseInt((String) properties.get("redis.port2"));
			port6382 = Integer.parseInt((String) properties.get("redis.port3"));
		} catch (IOException e) {
			logger.error("初始化redis.properties - exception", e);
		} finally {
			if(fileReader!=null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					logger.error("关闭流异常",e);
				}
			}
			logger.info("初始化redis.properties - end");
		}
	}
}
