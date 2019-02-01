package com.mpig.api.utils;

/**
 * 接口返货code值
 *
 * @author fang
 */
public class CodeContant {
	public static final int OK = 200; //

	public static final int CONLOGIN = 2050; // 请先登录
	public static final int CONAUTHEMPTY = 2051; // 校验参数为空
	public static final int CONAUTHOVERTIME = 2052; // 校验请求超时
	public static final int CONNCODEUSED = 2053; // ncode已经使用过
	public static final int CONNCODECHECK = 2054; // ncode校验失败
	public static final int CONAUTHTOKEN = 2055; // TOKEN错误
	public static final int CONTOKENTIMEOUT = 2056; // TOKEN超时
	public static final int CONFORMAT = 2057; // 字符串类型错误
	public static final int CONOPENIDACCESS = 2058; // 用户标示和凭证不能为空
	public static final int CONPARAMSEXCEPT = 2059; // 参数错误
	public static final int CONSYSTEMERR = 2060; // 系统错误
	public static final int ConMobileExcept = 2061;// 手机号码不能为空
	public static final int ConParamIsEmptyOrNull = 2062;// 参数不能为空
	public static final int ConParamTypeIsErr = 2063;// 参数类型错误
	public static final int ConSignErr = 2064;// 加密失败
	public static final int ConAccountFroze = 2065;// 帐号冻结
	public static final int ConCodeTimes = 2066;// 验证超过5次
	public static final int COnYYBCodeError = 2067; //应用宝code过期
	public static final int ConVisitorsTokenPermErr = 2068; //游客访问权限不足
	public static final int ConVisitorsTokenErr = 2069; //游客访问权限不足

	public static final int USERPASSWORD = 3000; // 账号或密码错误
	public static final int THIRDINTERFACE = 3001; // 第三方接口不稳定
	public static final int ACCOUNTEXCEPT = 3002; // 账号异常
	public static final int STRINGLENTH = 3003; // 字符串长度异常
	public static final int PICFORMAT = 3004; // 图片格式错误
	public static final int PICSIZE = 3005; // 图片不能超过
	public static final int MobileTimes = 3006;// 该手机验证码当天发送超过5次
	public static final int MobileSendCode = 3007;// 验证码发送失败
	public static final int MobileCodeErr = 3008;// 验证码错误
	public static final int RegistFirst = 3009;// 请先注册
	public static final int MobileRegisted = 3010;// 该手机已经注册过
	public static final int InsertAccount = 3011;// 账号添加异常
	public static final int InsertUserBase = 3012;// 用户基本信息添加异常
	public static final int InsertAsset = 3013;// 资产信息添加异常
	public static final int updUserImage = 3014;// 图像更新失败
	public static final int updPayAccount = 3015;// 绑定提现账号失败
	public static final int mobileBindErr = 3016;// 该手机号已经绑定过
	public static final int WEIBO_NOT_VERIFIED = 3017;// 微博账号没有认证
	public static final int WEIBO_BIND = 3018;
	public static final int BaseInfo_identity = 3019;// 用户身份
	public static final int authenErr = 3020; // 认证信息添加失败
	public static final int authenImageErr = 3021; // 银行卡信息添加失败
	public static final int authenIng = 3022;// 审核中
	public static final int authened = 3023;// 审核通过
	public static final int authenCannelError = 3024; //取消认证失败
	public static final int authenNicknameError = 3025; //认证昵称已存在
	public static final int albumPhotoIsMaxError = 3026; //相册相片已达到上限
	public static final int delPhotoError = 3027; //相册相片已达到上限
	public static final int coveredPhotoError = 3028; //相册覆盖失败
	public static final int uploadPhotoError = 3029; //上传相片失败
	public static final int albumIsHavent = 3030; //相片不存在
	public static final int reportAlbumInfoIsHave = 3031; //举报记录已存在
	public static final int userIsNotInFamily = 3032; //用户不在公会
	public static final int exchangeNotInTime = 3033; //兑换不在时间内
	public static final int exchangeWasInTime = 3034; //兑换不在时间内
	public static final int activityNotInTime = 3035; //活动不在时间内
	public static final int UserBaseInfoIsNull = 3036; //活动不在时间内
	
	public static final int SalesmanlNotExist = 3037; //业务员ID不存在
	public static final int UserOrgRelationExist = 3038; //UID已存在
	
	public static final int ImageCodeErr = 3039; //图片验证码错误

	public static final int ARTISTIDEXCEPT = 3100; // 该主播未开播过或主播异常
	public static final int LIVEOPEN = 3101; // 开播失败
	public static final int LIVECLOSE = 3102; // 停播失败
	public static final int LIVEHEARTBEAT = 3103; // 心跳处理失败
	public static final int LIVESERVICECONFIG = 3104; // 获取配置失败
	public static final int LiveReportExist = 3105;// 举报对象不存在或未开播
	public static final int LiveLikesExist = 3106;// 点赞对象不存在
	public static final int LivingStatus = 3107;// 主播未开播
	public static final int FollowErr = 3108;// 关注失败
	public static final int LiveAgain = 3109;// 频繁开播
	public static final int FollowRep = 3110;// 重复关注
	public static final int enterSelfRoom = 3111;// 进了自己的房间
	public static final int liveManageErr = 3112;// 设置管理员失败
	public static final int liveSilenOnErr = 3113;// 设置禁言失败
	public static final int LiveKickErr = 3114;// 踢出房间失败
	public static final int LiveKickInto = 3115;// 踢出房间成功
	public static final int liveSilenting = 3116;// 禁言中
	public static final int liveRedEnvelopCnts = 3117;// 红包数量不正常
	public static final int liveRedEnvMoney = 3118;// 红包金额不正常
	public static final int liveRoomIdErr = 3119;// 房间号错误
	public static final int liveRedEnvelopFinish = 3120;// 红包抢完
	public static final int liveRedEnvelopeErr = 3121;// 红包不存在
	public static final int liveEnterRepeat = 3122;// 重复进入
	public static final int liveUserManage = 3123;// 超管不能被
	public static final int liveEnvelopeTry = 3124;// 抢红包失败
	public static final int authenOkErr = 3125;// 没有认证

	public static final int GIFTCONFIGEXIST = 3500; // 礼物不存在
	public static final int USERASSETEXITS = 3501; // 用户不存在
	public static final int MALLGIFTEXIST= 3502; // 商城道具不存在

	public static final int UIDERROR= 3601; // uid校验错误
	public static final int DESCISNULL= 3602; // 备注为空
	public static final int MONEYVALIDERROR = 3603; // 金币或声援值格式错误
	public static final int MODIFYMONEY_ERROR = 3604; // 修改金额失败

	public static final int MONEYLESS = 4000; // 金额不足
	public static final int MONEYDEDUCT = 4001; // 扣费失败
	public static final int MONEYACCEPT = 4002; // 收礼失败
	public static final int updUserNickName = 4003;// 更新昵称失败
	public static final int updUserSignature = 4004;// 更新签名失败
	public static final int updUserSex = 4005;// 性别更新失败
	public static final int sendRedEnvelope = 4006;// 发送红包失败
	public static final int updSignatuerSensit = 4007;// 修改昵称涉及到敏感词
	public static final int baggidless = 4008; // 背包礼物不足
	public static final int baggiddeduct = 4009; // 背包礼物不足
	public static final int lotteryCountDeduct = 4020; //抽奖次数不足
	
	/**
	 * 游戏 下注
	 */
	public static final int gameBetDeductErr = 4010;// 下注扣费失败
	public static final int gameBetLogAddErr = 4011;// 下注日志记录失败

	/**
	 * 砸蛋 砸
	 */
	public static final int gameEggNoGiftCfg = 4012;// 砸蛋没有礼物配置
	
	public static final int updRecommend = 2008;// 修改推荐失败
	public static final int nicknameLen = 2009;// 昵称超长
	public static final int nicknameIsAuth = 2010;// 昵称超长
	public static final int constellationIsAuth = 2011;// 星座
	public static final int hobbyIsAuth = 2012;// 爱好超长

	public static final int PayBrush = 4100;// 充值频繁
	public static final int PayOrderNo = 4101;// 订单号生成失败
	public static final int PayOrder = 4102;// 订单生成失败
	public static final int PayOrderExits = 4103;// 订单不存在
	public static final int PayWithDrawNo = 4104;// 生成提现单失败
	public static final int PayWithDrawRights = 4105;// 家族成员不能单独取现
	public static final int PayApplyTimes = 4106;
	public static final int PayOrderSignFail = 4107;// 订单号签名失败
	public static final int PayOrderException = 4108;// 保存订单异常
	public static final int PayOrderUpdStatusFail = 4109;// 更新订单状态失败

	public static final int PayTixianMonyErr = 4200;// 提现金额不正确
	public static final int PayTixianErr = 4201;// 提现失败
	public static final int PayTixianTimes = 4202;// 提现超过规定的次数
	public static final int PayTixianInterval = 4203;// 提现太频繁

	public static final int PayExchangeMoney = 4250;// 内兑金额错误
	public static final int PayExchangeRate = 4251;// 内兑金额错误
	public static final int PayExchangeAdd = 4252;// 内兑失败
	public static final int PayExchangeLess = 4253;// 声援值不足
	public static final int PayForbidExchange = 4254;// 禁止内兑

	//邀请任务相关
	public static final int inviteRewardIsntGet = 4500; //邀请的奖励暂时无法获取
	public static final int inviteRewardGetError = 4501; //重复领取任务奖励
	
	
	public static final int SEARCH_FAILED = 5000;// 搜索失败

	// 私信相关
	public static final int Pri_Msg_Empty = 5300; // 私信内容不能为空
	public static final int Pri_Msg_Follow = 5301; // 未关注
	public static final int Pri_Msg_pushErr = 5302;// 对方拒收
	public static final int Pri_Msg_idErr = 5303;// 获取消息ID失败
	public static final int Pri_Msg_setRedisErr = 5304;// 获取消息ID失败
	public static final int Pri_Msg_IdsEmpty = 5305;// 消息ID为空

	public static final int VersionNoExist = 6000;// 版本没有配置

	public static final int user_black_err = 6100;// 拉黑失败
	public static final int user_black = 6101;// 被拉黑
	public static final int user_unblack_err = 6102;// 解除失败
	
	/**
	 * 七牛 异常
	 */
	public static final int qiniu_gettoken_err = 7000;// 解除失败
	

	/**
	 * 商城相关状态码
	 */
	public static final int mall_guard_level_error = 6500;//购买的守护级别不可低于当前已有的身份
	public static final int mall_buy_guard_error = 6501;// 插入守护记录失败
	public static final int mall_vip_level_error = 6502;//购买的VIP级别不可低于当前已有的身份
	public static final int mall_buy_vip_error = 6503;// 插入VIP记录失败
	public static final int mall_buy_props_error = 6504;// 插入商城道具记录失败
	public static final int mall_force_buy_guard = 6505;//已有低级身份的购买警告
	public static final int mall_buy_car_error = 6506;// 插入座驾记录失败
	/**
	 * 身份权限相关
	 */
	public static final int userInfo_permission_error = 6600;
	
	/**
	 * 动态相关
	 */
	public static final int feed_content_length_err = 6700; //动态文本长度超过限制
	public static final int feed_upload_img_err = 6701; // 修改动态图片失败
	public static final int feed_delete_err = 6702; // 删除失败
	public static final int feed_addReply_err = 6703; // 回复失败
	public static final int feed_notfound_err = 6704; // 动态不存在
	public static final int feed_delReply_err = 6705; // 删除回复失败
	public static final int feed_reward_err = 6706; //打赏失败
	public static final int feedreport_IsHavent = 6707; //不允许重复举报
	public static final int feed_laud_err = 6708; //点赞错误
	public static final int feed_lauds_err = 6709; //重复点赞
	
	/**
	 * 任务相关的异常码
	 */
	public static final int TaskMissingConfig = 7100;// 缺少配置
	public static final int TaskRewardHasCommit = 7101;// 任务奖励已经领取
	public static final int TaskUnFinished = 7102;// 任务尚未完成
	public static final int TaskUnFinishedOrCommited = 7103;// 任务尚未完成或者已经领取

	/**
	 * 任务签到的异常码
	 */
	public static final int SignRewardHasCommit = 7200;// 签到奖励已经领取
	public static final int SignMissingData = 7201;// 查询不到此信息
	public static final int SignInternalError = 7202;// 服务器内部异常
	public static final int ReSignError = 7203;// 服务器内部异常

	
	/**
	 * 微信H5登陆异常码
	 */
	public static final int UnValidCode = 8000;// 无效凭证
	public static final int SNSUserInfoGetFailed = 8001;// 无效访问令牌
	public static final int UnValidOpenId = 8002;// 无效第三方标示
	public static final int OpenIdGetFailed = 8003;// opendId获取失败
	
	/*
	 * 游戏rpc请求
	 */
	public static final int GameSignatureFail = 9000;// 游戏签名无效
	public static final int GameRequireFail = 9001;// 游戏进入条件无效
	public static final int GameAppNotExist = 9002;// 小猪商店没有此游戏
	public static final int ERROR = 400;
	
	/*
	 * 应用宝接入
	 */
	public static final int TencentYYBSignatureFail = 9100;// 请求签名无效
	
	/**
	 * 签名
	 */
	public static final int SignatureFail = 10000;// 请求签名无效
	public static final int SignatureTimeOut = 10001;// 请求签名超时
	
	
	
	
	
	
	/**
	 * SDK相关提示
	 */
	public static final int channelError = 20000; //渠道错误
	public static final int signError = 20001; //签名错误

}
