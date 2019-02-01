package com.mpig.api.utils;

public class RedisContant {
	public static final String RedisAdminList = "admin:all"; // 记录用户账号、资产、基本信息、开播列表
	public static final String RedisNameUser = "user"; // 记录用户账号、资产、基本信息、开播列表
	public static final String RedisNameRelation = "relation"; // 关注、粉丝、观众、黑名单、好友
	public static final String RedisBlackWords = "words"; // 黑词 敏感词
	public static final String RedisNameRedEnvelop = "redEnvelop";// 红包
	public static final String RedisNameOther = "other";// 其他
	public static final String RedisNameFeed = "feed";// 动态

	public static final String keyAssetList = "usr:asset";// "ua&l";
	public static final String keyBaseInfoList = "usr:base";// "ub&l";
	public static final String keyUserAlbum  = "usr:album:"; //用户相册  usr：album：100001
	public static final String keyRobotBaseInfoList = "robot:base";// "ub&l";
	public static final String keyAccountList = "usr:acc";// "uac&l";
	public static final String keyPayAccountList = "pay:acc";// "p&a&l"; //
																// 用户与提现账户的关联
	public static final String keyLiveInfoList = "living:list";// "li&l";
	public static final String keyLiveEnterTotal = "rm:users:";// 记录本场直播进入的人数
	public static final String KeyRoomUserCount = "rm:users:cnt:";// 记录真人进入房间人数
	public static final String KeyRoomAnchorGetSends = "rm:get:sends:";// 记录本场收到的猪头数
	public static final String KeyRoomAnchorGetRq = "rm:get:rq:";// 记录本场收到的人气猪
	public static final String KeyRoomShowUserCounts = "rm:show:uc:";// 记录当前主播的房间显示人数

	public static final String keyAccountNameAndUid = "aname:uid";// "an&u";
	public static final String keyAuthKeyAndUid = "akey:uid";// "ak&u";
	public static final String keyMbileAndUid = "mb:uid";// 手机与ID的关系
	public static final String keyUnionIdAndUid = "unionid:uid";

	public static final String keyNicknameList = "nicknamelist";
	public static final String keyAuthkeyList = "authkeylist";

	public static final String KeyLiveInfoList = "r&ing"; // 开播列表

	public static final String KeyRoomRecommend = "rm:recomm";// "r&rec";// 推荐列表
	public static final String KeyRoomHot = "rm:hot";// 热门
	public static final String keyRoomBase = "rm:base";// 普通主播
	public static final String keyRoomNewJoin = "rm:newjoin"; //最新入驻
	public static final String keyRoomNewJoinAll = "rm:newjoin:all"; //全部最新入驻名单
	public static final String keyRoomMobile = "rm:mobile"; //手机开播列表

	public static final String keyUsrStream = "usr:stream";// 获取第三方流地址

	public static final String keyRoomSilent = "rm:sl:";// 禁言列表
	public static final String keyRoomKick = "rm;kick";

	public static final String keyRoomManage = "rm:mg:";// 房间管理员列表
	public static final String keyUserManage = "usr:mg:";// 用户管理的房间列表

	public static final String keyFollows = "follows:all:";// "userfollows";//
	public static final String keyFollowTimes = "follows:tm:";// 关注频率显示
	public static final String keyUnFollowTimes = "unFollows:tm:";// 关注频率显示
																// 总关注数
	public static final String keyPushMsg = "push:";// "a&u&p";
	public static final String keyFans = "fans:";// "userfans"; // 总粉丝数
	public static final String KeyLikes = "rm:like:";// "r&like";// 总喜欢数
	public static final String KeyRoomLikes = "rm:this:";// "r@likes";// 本场喜欢数
	public static final String keyStartLive = "rm:hb";// "r@endt";// 记录主播开始心跳时间
	public static final String KEYROOMUSERLIST = "roomuserlist_";

	public static final String KeyGiftConfigList = "gift:list";// "giftconfiglist_";

	public static final String MchBillNo = "pay:mchBillNo";// 微信订单编号生成
	public static final String PayNotice = "pay:notic";// 支付公告

	public static final String KeyWxInfo = "wx:info";

	public static final String KeySysText = "sys:text";// 客户端显示提示文字

	public static final String KeySysMsg = "sys:msg";// 官方私信
	
	public static final String keySupport = "support:uid";// 扶持账号

	/**
	 * 用户进房记录
	 */
	public static final String KEYUSERENTERROOM = "userentertime_"; // 关注列表
	// public static final String KeySrcFollwsDst = "src&dst";// src关注了dst
	public static final String KeyUserAndAnchor = "usr:room";// "u&a"; //
																// 用户(user)跟房间(anchor)的关系(1:1)
	public static final String KeyAnchorAndUser = "rm:user:";// "a&u";//
																// 主播跟用户的关系(1:N)
	public static final String KeyAnchorAndGuardUser = "rm:guarduser:"; //房间里的守护席位
	public static final String KeyAnchorAndAllUser = "rm:alluser:"; //房间里的所有用户（排序）
	public static final String KeyUserEnterLong = "rm:intime:";// "u&e&l";//
																// 记录用户在房间里的时长

	public static final String KeyRoomTimes = "rm:times:";// "r&u&s";
															// 房间进入用户人次(不排重)
	public static final String KeyRoomGuards = "rs:guardcount:";
	public static final String keyRoomUserTimes = "rm:usr:times:";// 记录房间的真人
	// public static final String KeyRoomUsersAll = "rm:usr:all:";//房间总人数（排重）
	public static final String KeyRoomUsers = "r&us";// 房间进入用户数（排重） 随用户的进出而改变
	public static final String KeyRoomGetsToday = "rm:day:gets:";// "r&g&t";//
																	// 当天(today)房间(room)的声援值(gets)
	public static final String KeyDstGetsSrcMoney = "rm:gets:";// "r&g&us";//
																// 主播的声援榜
																// 主播收到礼物的用户列表
	
	public static final String KeyDstGetsSrcMoneyDay = "rm:getsday:";		//tosy add day week
	public static final String KeyDstGetsSrcMoneyWeek = "rm:getsweek:";
	
	
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
	
	public static final String KeyMobileBing = "mb:bing:"; // 手机绑定次数限制  一个手机号至多绑定3个账号

	public static final String KeyRoomReports = "rm:report:";// "r&rep";// 房间举报数

	public static final String KeyHttpReq = "ncode:";// "httpReq_";//

	public static final String KeySendGiftTotal = "sendgifttotalByUid_";// 记录每个人送礼给每个主播的礼物价值总数

	public static final String keyLivingClick = "click:";// 记录开播时间，阻止频繁开播
	public static final String keyUserIntoRoom = "o:usr:rm:";// 记录用户进入房间

	public static final String keyApplyIAP = "app:iap:";// apply 内购使用

	public static final String keyRecommend = "_krcd";// 临时允许进入推荐位

	public static final String keyAclTrustedHostList = "acl:trusted:host";// 可信任主机列表
	public static final String keyAclTrusteUserList = "acl:trusted:user";// 可信任用户列表

	public static final String SwithActivity = "switch:act";// 活动开关
	public static final String Swith520 = "swith:ac";// 520活动开关
	public static final String SwithEnvelope = "swith:env";// 520活动开关
	public static final String Rank520Anchor = "rank:anchor";// 520活动 主播排榜
	public static final String Rank520User = "rank:user";// 520活动 用户排榜
	
	
	public static final String RankAnchorPriAudition2016 = "rank:anchorPAudition2016";// 2016海选初赛 主播排行榜
	public static final String RankAnchorFinalAudition2016 = "rank:anchorFAudition2016";// 2016海选决赛 主播排行榜
	public static final String RankActivityTop = "rank:act:top:"; //各种排行榜榜单
	public static final String ActivityUserList = "activity:user:"; //各类活动中奖名单 

	public static final String VersionAndroid = "version:android";
	public static final String VersionIos = "version:ios"; //版本强制更新
	
	public static final String verifyIos = "verify:ios";// 审核屏蔽功能

	public static final String PushTempleteLiveOn = "push:templetes:liveon";

	public static final String SwithPlate = "swith:plate:";// 切换前后台

	public static final String BroadcastIos = "bc:ios";
	public static final String BroadcastAndroid = "bc:android";
	public static final String BroadcastAnchor = "bc:anchor:";
	public static final String PrivateMsg = "pri:msg:";
	public static final String PrivateMsgId = "pri:msgid";

	public static final String AuthenIdentity = "au:iden";// 身份认证
	
	public static final String AuthXiaozhuPic = "au:xzpic";// 小猪认证相关图片
	
	public static final String AllAuth = "all:auth:";// 小猪认证

	public static final String blacklist = "black:list:";// 黑名单
	
	public static final String userGuard = "user:guard:"; //用户守护信息 (用户id:房间id)
	public static final String userAllGuard = "user:allguard:"; //用户守护信息 (用户id)
	public static final String userVip = "user:vip:"; //用户VIP信息(用户id)
	public static final String userCar = "user:car:"; //用户座驾信息(用户id)
	public static final String guardFirstSpendOfDay = "guard:fs:"; //守护首次消费 +date
	public static final String guardFirstLoginOfDay = "guard:fl:"; //守护首次登陆 +date
	public static final String mengzhucard = "props:mengzhucard:"; //萌猪卡(用户id)
	public static final String roomAllGuard = "room:all:guard:"; //房间所有的守护信息
	public static final String roomAllGuardSort = "room:all:guard:sort:"; //房间守护排序(根据金币排序)
	

	/**
	 * 已完成尚未领取奖励的任务-每日
	 */
	public static final String TaskFinishedDaily = "task:finished:daily";

	/**
	 * 已完成尚未领取奖励的任务-新手
	 */
	public static final String TaskFinishedNewBie = "task:finished:newbie";
	
	/**
	 * 已领取奖励的任务-每日
	 */
	public static final String TaskCommitedDaily = "task:commited:daily";
	
	/**
	 * 已领取奖励任务-新手
	 */
	public static final String TaskCommitedNewBie = "task:commited:newbie";
	
	/**
	 * 可接受的任务-每日
	 */
	public static final String TaskAcceptedDaily = "task:accepted:daily";

	/**
	 * 可接受的任务-新手
	 */
	public static final String TaskAcceptedNewBie = "task:accepted:newbie";
	
	/**
	 * 新手任务的货币奖励标示
	 */
	public static final String TaskHasFinishedNewBieTask = "task:hasFinishedNewBieTask";
	
	/**
	 * 新手任务是否派发过
	 */
	public static final String TaskHasAcceptedNewBieTask = "task:hasAcceptedNewBieTask";

	/**
	 * 指定月份的签到信息
	 */
	public static final String TaskSignByMonth = "task:sign:month";
	public static final String TaskReSignByMonth = "task:resign:month";	//补签
	public static final String TaskReSignCountByMonth = "task:resigncount:month";	//补签
	
	/**
	 * 耗时长记录键值
	 */
	public static final String StatCostKeyPrefix = "stat:cost";
	
	/**
	 * 异常记录键值
	 */
	public static final String ErrorOccurredKeyPrefix = "stat:error";
	
	/**
	 * 用户签到信息
	 */
	public static final String TaskSignByUser = "task:sign:user";

	public static final String User_login = "login:d:";// 活跃数
	public static final String loginCode = "login:code";// 记录用验证码登录次数

	public static final String anchorRqDay = "anchorrq:day:";

	public static final String anchorRqWeek = "anchorrq:week:";
	
	public static final String anchorRqMonth = "anchorrq:month:";
	
	public static final String anchorRqAll = "anchorrq:all";
	
	public static final String anchorDay = "anchor:day:";
	public static final String userDay = "user:day:";
	
	public static final String anchorWeek = "anchor:week:";
	public static final String userWeek = "user:week:";
	
	public static final String anchorMonth = "anchor:month:";
	public static final String userMonth = "user:month:";
	
	public static final String anchorAll = "anchor:all";
	public static final String userAll = "user:all";
	
	/**
	 * 打赏礼物的鲜花榜单
	 */
	public static final String anchorRewardGiftDay = "anchor:rg:day:";
	public static final String userRewardGiftDay = "user:rg:day:";
	
	public static final String anchorRewardGiftWeek = "anchor:rg:week:";
	public static final String userRewardGiftWeek = "user:rg:week:";
	
	public static final String anchorRewardGiftMonth = "anchor:rg:month:";
	public static final String userRewardGiftMonth = "user:rg:month:";
	
	public static final String anchorRewardGiftAll = "anchor:rg:all";
	public static final String userRewardGiftAll = "user:rg:all";
	
	
	public static final String giftver = "gift:ver";// 礼物版本号（整体）
	public static final String anchorZhouxin = "anchor:zx:";
	public static final String userZhouxin = "user:zx:";
	public static final String zxTimes = "zhouxin:times:";
	public static final String homeBanner = "home:banner";
	
	public static final String ShareApp = "share:app:";// APP分享相关记录 如:(share:app:audition日期)
	public static final String rankDayAlternate = "day:alt:";
	public static final String rankWeekAlternate = "week:alt:";

	public static final String WeekTitle = "week:title"; // 周星标签
	
	public static final String adTxFeedBack = "ad:gdt:feed:"; //广点通feed 广告点击相关
	public static final String adTxIOSStatistics = "ad:gdt:ios"; //广点通IOS注册数据
	public static final String adMobFeedBack = "ad:mob:feed:"; //mobVista 广告点击
	public static final String adMobIOSStatistics = "ad:mob:ios"; //mobVista注册数据
	public static final String adAsFeedBack = "ad:as:feed:"; //爱思 广告点击相关
	public static final String adAsIOSStatistics = "ad:as:ios:"; //爱思 广告点击相关
	

	public static final String ortherUserGiftEx = "srcuser:gift:";//礼物与用户送礼间隔过滤
	public static final String mengzhuOfDay = "mengz:day:";// 萌气猪用户每天送的数量
	
	public static final String forbidExchange = "forbid:exc";
	public static final String userItem = "user:item:";
	public static final String userBadge = "user:badge:";
	public static final String userItemSpecail = "user:item:spec";
	public static final String firstPay = "first:pay";
	
	public static final String ortherLiveInvite =	"liveinvit:anchor:";		//邀请人id:被邀请人id
	public static final String ortherLive2ndUid =	"live2ndUid:anchor:";		//直播间id:另一个主播id
	public static final String ortherQnLiveKey =	"liveUid:qnkey";		//直播间id:QnKey
	public static final String ortherQnLiveKeyOneDay = "qn1d:uidkey:";// 保存一天的动态key
	public static final String inviteWxAuth = "invite:wxauth"; //邀请活动的 微信授权记录； 登录清除移至到数据库和已邀请KEY
	public static final String inviteWxAuthActivate = "invite:wxauth:act";  //已经激活的微信key

	public static final String recordUidSet = "recordUidSet";  //录制视频的主播集合	//tosy
	public static final String UidrecHash = "UidrecHash:";  //主播录制集合:uid	//tosy
	
	/**
	 * 微信web验证回调
	 */
	public static final String weixinState2Code = "weixin:code:";
	public static final String weixinShareAccessToken = "wx:token";
	public static final String weixinShareJsapiTicket = "wx:ticket";
	
	public static final String olympicHeatUser = "olym:heat:user";// 奥运用户热度
	public static final String olympicHeatAnchor = "olym:heat:anchor";//奥运主播热度
	
	public static final String olympicGift = "oly:gift:";// 奥运礼物
	public static final String olympicReward = "oly:reward:";// 奥运之星任务完成 发放奖励标记
	public static final String olympicStar = "oly:star:"; //当前任务星级
	public static final String olympicDayFixed = "oly:datfixed"; //任务完成次数
	
	public static final String withdrawInterval = "withdraw:interval:";// 提现间隔控制

	public static final String withdrawDay = "withdraw:day:";// 提现每天限制
	
	public static final String actMidRoom = "act:rm:mid";// 直播间中部的活动
	public static final String actRightTopRoom = "act:rm:rt";// 直播间right top的活动
	
	// 跑道数据
	
	public static final String tmpRunway = "tmp:runway:";
	
	public static final String midAutumn = "mid:autumn"; // 中秋

	public static final String midMultiple = "mid:multi"; // 中秋
	
	public static final String nationalDay = "national:day";
	public static final String nationalGloryRank = "national:glory:rank"; // 国庆 荣耀排名
	public static final String nationalCharmRank = "national:charm:rank"; // 国庆魅力排名
	
	public static final String halloweenAnchor = "halloween:anchor"; // 万圣节活动
	public static final String halloweenUser = "halloween:user";
	
	public static final String SinglesDayAnchor = "single:anchor"; // 光棍节
	public static final String SinglesDayUser = "single:user";
	
	public static final String AIDSAnchor = "AIDS:anchor"; // 艾滋病日
	public static final String AIDSUser = "AIDS:user"; // 艾滋病日

	public static final String Double12Anchor = "double:anchor"; // 双12活动
	public static final String Double12User = "double:user"; // 双12活动
	
	public static final String ActivityTopAnchor = "activity:topanchor"; // 圣诞节
	public static final String ActivityTopUser = "activity:topuser"; // 圣诞节
	
	public static final String ActivityGohomeAnchor = "activity:gohomeanchor"; // 圣诞节
	public static final String ActivityGohomeUser = "activity:gohomeuser"; // 圣诞节
	
	public static final String ActivityNewYearAnchor = "activity:nyanchor"; // 新年活动
	public static final String ActivityNewYearUser = "activity:nyuser"; // 新年活动
	
	public static final String ActivityYXAnchor = "activity:yxanchor"; //元宵情人节
	public static final String ActivityYXUser = "activity:yxuser"; //元宵情人节

	public static final String ActivityNRAnchor = "activity:nranchor"; //女人节
	public static final String ActivityNRUser = "activity:nruser"; //女人节
	
	public static final String nationalReceive = "national:day:";// 国庆活动领取
	
	public static final String UserXiaozhuRun = "user:xiaozhuRun"; //10.1小猪快跑活动 用户
	public static final String AnchorXiaozhuRun = "anchor:xiaozhuRun"; //10.1小猪快跑活动 主播
	
	
	public static final String noIncludePay = "no:include:pay"; // 充值不参与返利
	
	public static final String payDayAct = "pay:day:no1"; // 每日首充活动记录
	
	public static final String payDayActDraw = "pay:draw:times"; //抽奖次数
	
	public static final String newYearDayActDraw = "newYear:draw:times"; //抽奖次数

	public static final String ortherTianChengCheckEx = "tiancheng:check";//礼物与用户送礼间隔过滤
	
	public static final String ApplyPayTimes = "apply:pay";
	

	public static final String tcOffRoom = "tc:offroom";//tc 补发下线
	
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
	
	public static final String Cover = "cover";		//主播封面
	
	/**房间游戏管理*/
	public final static String gameList = "game:list";
	/**房间游戏管理 hash*/
	public final static String gameHashList = "game:hlist";
	
	/**游戏房间声援榜 */
	public static final String gameRoomCredit = "rm:game:credit:";
	
	/**砸蛋配置列表缓存*/
	public static final String SmashedEggGiftCfgList = "game:smashedegg:gifts";
	/**砸蛋配置锤子金额缓存*/
	public static final String SmashedEggMoneyCfgList = "game:smashedegg:money";
	
	// 用户声援贡献榜（新） 2017-11-25
	public static final String userSupportDay = "user:support:day:";
	public static final String userSupportWeek = "user:support:week:";
	public static final String userSupportMonth = "user:support:month:";
	public static final String userSupportAll = "user:support:all";
	// 主播声援榜（新）2017-11-25
	public static final String anchorSupportDay = "anchor:support:day:";
	public static final String anchorSupportWeek = "anchor:support:week:";
	public static final String anchorSupportMonth = "anchor:support:month:";
	public static final String anchorSupportAll = "anchor:support:all";
	
	//用户对主播声援值日榜
	public static final String userSupportForAnchorDay= "user:sfa:day:";
	//用户对主播声援值周榜
	public static final String userSupportForAnchorWeek= "user:sfa:week:";
	//用户对主播声援值月榜
	public static final String userSupportForAnchorMonth= "user:sfa:month:";
	//用户对主播声援值总榜
	public static final String userSupportForAnchorAll= "user:sfa:all:";
	
	
	public static final String sysCopyright = "sys:copyrigh";
	
	//直播间的真实人数
	public static final String roomRealUserNum = "rm:rl:usm";
	// 游戏平台总开关(0为关闭 1为启用)
	public final static String gameSwitch = "game:switch";
	
	//直播间机器人信息
	public static final String roomRobots = "rm:robots:";
	
	public static final String userEnterRoomDay = "rm:enter:day:";
	
	public static final String userFollowDay = "user:follow:day:";
	//直播间真实人数信息
	public final static String roomUsers = "rm:real:users:";
	
	/**主播当前PK信息*/
	public static final String pkInfo = "pk:info";
	
	/**主播当前PK票数*/
	public static final String pkAnchorCharm = "pk:anchor:charm:";
	
	/**主播pk关系*/
	public static final String pkAnchorRel = "pk:anchor:rel:";
	
	/**主播pk信息 用于惩罚用*/
	public static final String pkPenaltyTimeInfo = "pk:PenaltyTimeInfo:";
	
	/**主播邀请信息*/
	public static final String userLianmaiInviteInfo = "user:lm:invite:";
	
	/**扶持号用户ID集合redis key*/
	public final static String supportUsersID = "sp:uid";
	
	/**微信支付通道*/
	public static final String wxPayWay = "wx:payway";
	
	/**图片验证码*/
	public static final String mbImageVerifyCode = "mb:imgCode:";
	
	/**微信公众号jsapi_ticket*/
	public static final String wxAccessToken = "wx:access_token";
	
	/**微信公众号jsapi_ticket*/
	public static final String wxJsapiTicket = "wx:jsapi_ticket";
	
	//！！！！不要Am开头定义redis在otherRedis中，此开头为活动模板开头数据！！！
	//实际定义在	ActivitiesModeConfigLib	中
	//+活动名+开始时间+阶段时间点
	//实时插入
//	public static final String AmUnionValueNameStartDurTs = "AmUnionVal:";			//活动模板0 工会阶段总值
//	public static final String AmAnchorValueNameStartDurTs = "AmAnchorVal:";		//活动模板0 主播阶段总值
//	public static final String AmUserValueNameStartDurTs = "AmUserVal:";			//活动模板0 用户阶段总值
//	public static final String AmAnchorValueSumNameStartDurTs = "AmAnchorValSum:";	//活动模板0 主播总值
//	public static final String AmUserValueSumNameStartDurTs = "AmUserValSum:";		//活动模板0 用户总值
//	//统计
//	public static final String AmAnchorResultNameStartDurTs = "AmAnchorResult:";		//活动模板0 主播阶段统计值
//	public static final String AmAnchorResultSumNameStartDurTs = "AmAnchorResultSum:";	//活动模板0 主播总统计值
//	public static final String AmAnchorValueTopNameStartDurTs = "AmAnchorValueTop:";	//活动模板0 主播阶段值各期最高
	//public static final String AmAnchorUserSnapTop0NameStartDurTs = "AmAnchorUserSnapTop:";//活动模板0 主播用户阶段值各期最高快照	
}
