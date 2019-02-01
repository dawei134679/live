package com.mpig.api.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mpig.api.SqlTemplete;
import com.mpig.api.async.AsyncManager;
import com.mpig.api.async.IAsyncTask;
import com.mpig.api.dao.IInviteUserInfoDao;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.dictionary.lib.TaskConfigLib;
import com.mpig.api.dictionary.lib.TaskConfigLib.TaskFor;
import com.mpig.api.dictionary.lib.UrlConfigLib;
import com.mpig.api.model.InviteUserInfoModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.SearchModel;
import com.mpig.api.model.UserAccountModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.redis.service.RedisCommService;
import com.mpig.api.redis.service.UserRedisService;
import com.mpig.api.service.IAuthService;
import com.mpig.api.service.ISearchService;
import com.mpig.api.service.IUserInfoService;
import com.mpig.api.service.IUserOrgRelationService;
import com.mpig.api.service.IUserService;
import com.mpig.api.task.TaskService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.Constant;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.EncryptTokenUtils;
import com.mpig.api.utils.EncryptUtils;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.StringUtils;
import com.mpig.api.utils.VarConfigUtils;

@Service
public class AuthServiceImpl implements IAuthService, SqlTemplete {

	private static final Logger logger = Logger.getLogger(AuthServiceImpl.class);

	public static String TOKENHTTPREQKEY = "e1671797c72e17f763380b47e841ec32";//"f1671797c72f17f763380b47f841fc32";

	private static String userRoomKey = "67xiukkirnkxi37797";

	@Autowired
	private IUserService userservice;

	@Autowired
	private ISearchService searchservice;
	
	@Autowired
	private IInviteUserInfoDao inviteUserInfoDao;
	
	@Autowired
	private IUserOrgRelationService userOrgRelationService;
	
	@Autowired
	private IUserInfoService userInfoService;

	/**
	 * 验证token
	 *
	 * @param ncode
	 *            客户端请求码
	 * @param os
	 *            平台代码
	 * @param imei
	 *            用户设备唯一标识码
	 * @param reqtime
	 *            请求时间，单位为秒
	 * @param token
	 *            用户唯一码，服务端生成，可解码
	 * @param bl
	 *            当token为空时有效 true 强制登录 false 忽略
	 * @return
	 */
	@Override
	public void authToken(String ncode, Byte os, String imei, Long reqtime, String token, Boolean bl,
			ReturnModel returnModel) {

		if ("".equals(ncode) || os == 0 || "".equals(imei) || reqtime == 0) {
			returnModel.setCode(CodeContant.CONAUTHEMPTY);
			returnModel.setMessage("参数有问题:"+CodeContant.CONAUTHEMPTY);
			return;
		}
		// 验证单个请求时间不能超过24小时(使用毫秒计算)或提前1小时
		Long lgtime = 0L;
		if (reqtime > 1000000000000000L) {
			lgtime = System.currentTimeMillis() - reqtime/1000;
		}else if (reqtime > 1000000000000L) {
			lgtime = System.currentTimeMillis() - reqtime;
		} else {
			lgtime = System.currentTimeMillis()/1000 - reqtime;
		}
		
		if (lgtime > 86400000 || lgtime <= -3600000) {
			returnModel.setCode(CodeContant.CONAUTHOVERTIME);
			returnModel.setMessage("您的系统时间有误，请确认核实下系统时间" + CodeContant.CONAUTHOVERTIME);
			return;
		}

		// 判断ncode是否被用户使用过
		String redisNcode = OtherRedisService.getInstance().getNcode(ncode);
		if (org.apache.commons.lang.StringUtils.isNotBlank(redisNcode)) {
			 returnModel.setCode(CodeContant.CONNCODEUSED);
			 returnModel.setMessage("签名错误:" + CodeContant.CONNCODEUSED);
			 return;
		 }

		// 开始校验token值
		String signStr = TOKENHTTPREQKEY;
		signStr = signStr + imei + os + reqtime;

		String sign = EncryptUtils.md5Encrypt(signStr);
		
		//pc端单独做校验
		if(5 == os) {
			String left = sign.substring(0,10);
			String right=sign.substring(22);
			String  string = (left+right).toUpperCase();
			sign = EncryptUtils.md5Encrypt(string);
		}
		
		if (sign.equalsIgnoreCase(ncode)) {
			OtherRedisService.getInstance().setNcode(ncode, 86400);
		} else {
			returnModel.setCode(CodeContant.CONNCODECHECK);
			returnModel.setMessage("签名校验失败:" + CodeContant.CONNCODECHECK);
			return;
		}

		if (token.isEmpty()) {
			if (bl) {
				returnModel.setCode(CodeContant.CONLOGIN);
				returnModel.setMessage("请先登录");
			}
		}
	}

	/**
	 * 解析token
	 *
	 * @param token
	 * @return
	 */
	public Integer decryptToken(String token, ReturnModel returnModel) {
		Integer uid = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String decryptStr = EncryptTokenUtils.decode(token);
			String[] descrypt = decryptStr.split("_");
			if (descrypt.length == 5) {

				uid = Integer.valueOf(descrypt[0]);
				// 检查账号是否正确
				UserAccountModel userAccountModel = userservice.getUserAccountByUid(uid, false);
				UserBaseInfoModel userBaseInfoModel = userservice.getUserbaseInfoByUid(uid, false);

				if (userAccountModel == null || userBaseInfoModel == null) {
					uid = 0;
					returnModel.setCode(CodeContant.USERPASSWORD);
					returnModel.setMessage("账号或密码错误");
				} else if (userAccountModel.getStatus() == 0) {
					uid = 0;
					returnModel.setCode(CodeContant.ConAccountFroze);
					returnModel.setMessage("您的帐号已冻结，请联系客服");
				} else {
					if (!userAccountModel.getPassword().equals(descrypt[1])) {
						uid = 0;
						returnModel.setCode(CodeContant.CONLOGIN);
						returnModel.setMessage("请先登录");
					} else {
						// 检查token是否过期(7天)
						if ((Integer.valueOf(descrypt[4]) + 604800) < (System.currentTimeMillis() / 1000)) {
							String newToken = encryptToken(uid, descrypt[1], Byte.parseByte(descrypt[2]), descrypt[3]);
							map.put("token", newToken);
							returnModel.setCode(CodeContant.CONTOKENTIMEOUT);
							returnModel.setMessage("为了安全，请重新登录");
							returnModel.setData(map);
							uid = 0;
						} else {
							returnModel.setCode(CodeContant.OK);
							if (userAccountModel.getAccountid() == 0) {
								this.updAccountid(uid);
							}
						}
					}
				}
			} else {
				uid = 0;
				returnModel.setCode(CodeContant.CONAUTHTOKEN);
				returnModel.setMessage("TOKEN错误");
			}
		} catch (Exception e) {
			uid = 0;
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("系统问题");
			logger.error("<decryptToken->Exception>" ,e);
		}
		return uid;
	}

	/**
	 * 获取Token
	 *
	 * @param uid
	 *            用户uid
	 * @param pword
	 *            用户密码 加密后的
	 * @param os
	 *            平台
	 * @param imei
	 *            设备唯一码
	 * @return
	 */
	@Override
	public String encryptToken(Integer uid, String pword, Byte os, String imei) {
		String tokenStr = uid + "_" + pword + "_" + os + "_" + imei + "_" + System.currentTimeMillis() / 1000;
		String token = null;
		try {
			token = EncryptTokenUtils.encode(tokenStr);
		} catch (Exception e) {
			logger.error("<encryptToken->Exception>" + e.getMessage());
		}
		return token;
	}

	/**
	 * 注册接口
	 *
	 * @param accountName
	 *            账号名称
	 * @param pword
	 *            密码
	 * @param regiterIp
	 *            注册IP
	 * @param nickName
	 *            昵称
	 * @param authKey
	 *            第三方标识openid + source
	 * @param source
	 *            第三方登陆标识
	 * @param registchannel
	 *            注册渠道
	 * @param subregistchannel
	 *            注册子渠道
	 * @param registos
	 *            注册平台注册平台 0=>默认 1=>android 2=>iphone 3=>ipad
	 * @param registimei
	 *            手机imei
	 * @param headimage
	 *            用户图像
	 * @return
	 */
	@Override
	public ReturnModel register(String accountName, String pword, Long regiterIp, String nickName, String authKey,
			String source, String registchannel, String subregistchannel, Byte registos, String registimei,
			String headimage, Boolean sex, String phone,String unionid,Long salesmanId) {

		long lg1 = System.currentTimeMillis();
		// 声明model对象
		Map<String, Object> map = new HashMap<String, Object>();

		ReturnModel returnModel = new ReturnModel();
		if(org.apache.commons.lang.StringUtils.isEmpty(headimage) && null!=Constant.user_defaultHeadImage) {
			headimage = Constant.user_defaultHeadImage;
		}
		String livingImg = headimage;
		// 获取用户uid
		Integer uid = userservice.getUid();
		Integer accountid = userservice.getAccountId();

		if (uid <= 0 || accountid <= 0) {
			returnModel.setCode(10000);
			returnModel.setMessage("系统繁忙，请稍等");// 获取uid失败
			return returnModel;
		}
		Long registTime  = System.currentTimeMillis() / 1000;
		//保存会员与业务关系
		ReturnModel tempReturnModel = userOrgRelationService.saveUserOrgRelation(uid, phone, registTime, salesmanId);
		if(tempReturnModel.getCode()!=200) {
			return tempReturnModel;
		}
		// 判断并设置用户账号和昵称
		if (accountName == "") {
			accountName = source + uid;
		}
		if (nickName == "") {
			nickName = accountName;
		}

		long lg2 = System.currentTimeMillis();
		logger.debug("registertime 参数判断时间 lg2-lg1=" + (lg2 - lg1));
		// 新增用户账号信息
		int res;
		try {
			res = userservice.InsertUserAccount(uid, accountid, accountName, pword, authKey,unionid);
		} catch (Exception e) {
			logger.error("<register UserAccount>:" + e.getMessage());
			returnModel.setCode(CodeContant.CONSYSTEMERR);
			returnModel.setMessage("系统繁忙，请稍等");
			return returnModel;
		}
		if (res == 0) {
			returnModel.setCode(CodeContant.InsertAccount);
			returnModel.setMessage("账号添加异常");
			return returnModel;
		}

		try {
			res = userservice.InsertUserBaseInfo(uid, nickName, sex, headimage, livingImg, phone, "", "",
					"Ta 什么都没有留下……", regiterIp, registTime, registchannel, subregistchannel,
					registos, registimei);
		} catch (Exception e) {
			logger.error("<register UserBaseInfo>:" + e.getMessage());
			returnModel.setCode(CodeContant.CONSYSTEMERR);
			returnModel.setMessage("系统繁忙，请稍等");
			return returnModel;
		}
		if (res == 0) {
			returnModel.setCode(CodeContant.InsertUserBase);
			returnModel.setMessage("用户基本信息添加异常");
			return returnModel;
		}
		// 新增用户资产信息
		try {
			res = userservice.InsertUserAsset(uid);
		} catch (Exception e) {
			logger.error("<register UserAsset>:" + e.getMessage());
			returnModel.setCode(CodeContant.CONSYSTEMERR);
			returnModel.setMessage("系统繁忙，请稍等");
			return returnModel;
		}
		if (res == 0) {
			returnModel.setCode(CodeContant.InsertAsset);
			returnModel.setMessage("资产信息添加异常");
			return returnModel;
		}

		userInfoService.saveUserInfoByUid(uid);
		
		long lg3 = System.currentTimeMillis();
		logger.debug("registertime 添加数据时间 lg3-lg2=" + (lg3 - lg2));
		Map<String, Object> mapArg = new HashMap<String, Object>();
		if (false == rpcAdminLoginComet(uid, mapArg)) {
			returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
			returnModel.setMessage("AdminFailed");
			return returnModel;
		}

		long lg4 = System.currentTimeMillis();
		logger.debug("registertime 通知服务时间 lg4-lg3=" + (lg4 - lg3));
		// 放入缓存
		UserRedisService.getInstance().setAccountNameAndUid(accountName.toLowerCase(), uid);
		UserRedisService.getInstance().setAuthKeyAndUid(authKey.toLowerCase(), uid);
		
		if (org.apache.commons.lang.StringUtils.isNotEmpty(unionid)) {
			RedisCommService.getInstance().hset(RedisContant.RedisNameUser, RedisContant.keyUnionIdAndUid, unionid, String.valueOf(uid), 0);
		}
		
		if (!"".equals(phone)) {
			UserRedisService.getInstance().setMobileAndUid(phone, uid);
		}

		long lg5 = System.currentTimeMillis();
		logger.debug("registertime 缓存数据时间 lg5-lg4=" + (lg5 - lg4));
		// TOSY 插入ES搜索
		String numb = uid.toString(); // 靓号TODO
		List<SearchModel> datas = new ArrayList<SearchModel>();
		SearchModel data = new SearchModel();
		data.setNickname(nickName);
		data.setUid(uid);
		data.setNumb(numb);
		data.setAvatar(headimage);
		data.setSex(sex);
		data.setSlogan("");
		datas.add(data);
		searchservice.insertUserInfosAnsyc(datas,false);

		try{
			AsyncManager.getInstance().execute(new UserRegistDetailAsyncTask(uid, registos, source, registchannel));
		}catch (Exception ex){
			logger.error("UserRegistDetailAsyncTask>>>",ex);
		}


		long lg6 = System.currentTimeMillis();
		logger.debug("registertime 添加ES搜索时间 lg6-lg5=" + (lg6 - lg5));
		returnModel.setCode(200);
		map = userservice.getUserDataMap(uid, uid);

		for (Map.Entry<String, Object> entry : mapArg.entrySet()) {
			map.put(entry.getKey(), entry.getValue());
		}
		map.put("newUser", "1");
		map.put("token", encryptToken(uid, pword, registos, registimei));
		returnModel.setData(map);
		return returnModel;
	}

	public class UserRegistDetailAsyncTask implements IAsyncTask {

		int uid;
		int os;
		/**
		 * 登录方式 sina、qq、weixin、ios\android
		 */
		String source;
		String channel;

		AuthServiceImpl authService = new AuthServiceImpl();

		public UserRegistDetailAsyncTask(int uid, int os, String source, String channel) {
			this.uid = uid;
			this.os = os;
			this.source = source;
			this.channel = channel;
		}

		@Override
		public void runAsync() {
			// 初始化新手任务
			TaskService.getInstance().initTaskAcceptList(String.valueOf(uid), TaskFor.Newbie);

			// 是否初始化每日任务
			String today = DateUtils.dateToString(null, "yyyyMMdd");
			Boolean hasLoginInThisDay = UserRedisService.getInstance().hasLoginInThisDay(uid, today);
			if (!hasLoginInThisDay) {
				TaskService.getInstance().initTaskAcceptList(String.valueOf(uid), TaskFor.Daily);
			}

			// 通过手机号注册
			if (source.equals("ios") || source.equals("android") || source.equals("mobile")) {
				TaskService.getInstance().taskProcess(uid, TaskConfigLib.BoundPhone, 1);
			}

			// int uid, int os, String loginMethod, String mobileModel, String
			// channel,
			// String mobileVersion, int isType, Long loginTime
			authService.insertLoginDetail(uid, os, source, "", channel, "", 1, System.currentTimeMillis() / 1000);
			if("weixin".equals(source)){
				UserAccountModel userAccountModel = userservice.getUserAccountByUid(uid, false);
				String inviteUid = RedisCommService.getInstance().hget(RedisContant.RedisNameUser, RedisContant.inviteWxAuth, userAccountModel.getUnionId());
				if(org.apache.commons.lang.StringUtils.isNotEmpty(inviteUid)){
					RedisCommService.getInstance().hdel(RedisContant.RedisNameUser, RedisContant.inviteWxAuth, userAccountModel.getUnionId());
					RedisCommService.getInstance().hset(RedisContant.RedisNameUser, RedisContant.inviteWxAuthActivate, userAccountModel.getUid().toString(), inviteUid, 0);
					InviteUserInfoModel inviteUserInfoModel = inviteUserInfoDao.selInviteUserInfoByUid(new Integer(inviteUid));
					int rsc = inviteUserInfoDao.addInviteUserRewardInfo(uid, new Integer(inviteUid), 1);
					if(rsc > 0){
						if(inviteUserInfoModel==null){
							inviteUserInfoDao.addInviteUserInfo(new Integer(inviteUid), 1, 0);
						}else{
							inviteUserInfoDao.updInviteCount(new Integer(inviteUid), 1);
						}
					}
					//添加 互相关注关系
					String osa = "2".equals(os+"") ? "ios" : "android";
					userservice.addFollows("on", uid, new Integer(inviteUid));
					// 检查是否已存在os、主播、用户的devicetoken关系
					String broadcastAnchor = UserRedisService.getInstance().getBroadcastAnchor(osa,String.valueOf(inviteUid), String.valueOf(uid));
					if (org.apache.commons.lang.StringUtils.isEmpty(broadcastAnchor)) {
						String appBroadcast = OtherRedisService.getInstance().getAppBroadcast(osa,
								String.valueOf(uid));
						if (org.apache.commons.lang.StringUtils.isNotEmpty(appBroadcast)) {
							UserRedisService.getInstance().setBroadcastAnchor(osa, inviteUid,
									String.valueOf(uid), appBroadcast);
						}
					}
					
					userservice.addFollows("on",  new Integer(inviteUid), uid);
					// 检查是否已存在os、主播、用户的devicetoken关系
					broadcastAnchor = UserRedisService.getInstance().getBroadcastAnchor(osa,String.valueOf(uid), String.valueOf(inviteUid));
					if (org.apache.commons.lang.StringUtils.isEmpty(broadcastAnchor)) {
						String appBroadcast = OtherRedisService.getInstance().getAppBroadcast(osa,
								String.valueOf(inviteUid));
						if (org.apache.commons.lang.StringUtils.isNotEmpty(appBroadcast)) {
							UserRedisService.getInstance().setBroadcastAnchor(osa, String.valueOf(uid),
									inviteUid, appBroadcast);
						}
					}
				}
			}
		}

		@Override
		public void afterOk() {
		}

		@Override
		public void afterError(Exception e) {
		}

		@Override
		public String getName() {
			return "UserRegistDetailAsyncTask";
		}
	}

	/**
	 * 获取第三方用户信息
	 *
	 * @param accessToken
	 * @param openid
	 * @param source
	 *            =weixin 微信 =qq QQ =sina 新浪微博
	 * @return ThirdModel
	 */
	@Override
	public HttpResponse<JsonNode> getThirdUserInfo(String accessToken, String openid, String source, Byte os) {

		String url = null;
		switch (source) {
		case "weixin":
			url = String.format(UrlConfigLib.getUrl("url").getChannel_weixin(), accessToken, openid);
			break;
		case "qq":
			String appId = "1105357920";
			url = String.format(UrlConfigLib.getUrl("url").getChannel_qq(), accessToken, appId, openid);
			break;
		case "sina":
			url = String.format(UrlConfigLib.getUrl("url").getChannel_weibo(), accessToken, openid);
			break;
//		case "tiancheng":
//			return TcPush.getinstance().userApprove(openid,accessToken);
		default:
			break;
		}

		if (url == null) {
			return null;
		} else {
			HttpResponse<JsonNode> asJson;
			try {
				asJson = Unirest.get(url).asJson();
				return asJson;
			} catch (UnirestException e) {
				logger.error("<asJson->UnirestException>" + e.toString());
			}
		}
		return null;
	}

	@Override
	public String getUserRoomToken(Integer uid) {
		Random random = new Random();
		int s = random.nextInt(999) % (999 - 101 + 1) + 101;

		String str = String.valueOf(s) + uid.toString() + (int) System.currentTimeMillis() / 1000;

		char[] charArr = str.toCharArray();
		char[] charKey = userRoomKey.toCharArray();

		int str_len = charArr.length;
		int key_len = charKey.length;

		for (int i = 0; i < str_len; i++) {
			for (int j = 0; j < key_len; j++) {
				charArr[i] = (char) (charArr[i] ^ charKey[j]);
			}
		}
		String res = String.valueOf(charArr);
		return res;
	}

	/**
	 * RPC admin to comet login
	 *
	 * @param uid
	 * @param map
	 *            OUT ARG
	 * @return true success
	 */
	@Override
	public boolean rpcAdminLoginComet(int uid, Map<String, Object> map) {
		Map<String, Object> mapUser = userservice.getUserProfile(uid);
		if (null == mapUser || false == mapUser.containsKey("headimage") || false == mapUser.containsKey("userLevel")
				|| false == mapUser.containsKey("nickName") || false == mapUser.containsKey("sex")) {
			return false;
		}

		HttpResponse<String> responeString = null;
		// GOTO ADMIN user/token
		try {
			String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
					"appKey=" + VarConfigUtils.ServiceKey, "openId=" + uid);

			responeString = Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_user_token())
					.field("appKey", VarConfigUtils.ServiceKey).field("openId", uid).field("sign", signParams).asString();
		} catch (UnirestException e) {
			logger.error("<rpcAdminLoginComet->UnirestException> uid:" + uid + ">>>" + e.toString());
			return false;
		} catch (Exception e) {
			logger.error("<rpcAdminLoginComet->Exception> uid:" + uid + ">>>" + e.toString());
			return false;
		}

		if (null == responeString) {
			return false;
		}

		org.json.JSONObject jsonObjectRt = null;
		try {
			jsonObjectRt = new org.json.JSONObject(responeString.getBody());
		} catch (JSONException e) {
			logger.error("<rpcAdminLoginComet->Exception> uid:" + uid + ">>>" + responeString.getBody());
			e.printStackTrace();
			return false;
		}

		try {
			// get respone
			if (null == jsonObjectRt || !jsonObjectRt.has("result")) {
				return false;
			}
			org.json.JSONObject jsonObject = jsonObjectRt.getJSONObject("result");
			if (null == jsonObject || !jsonObject.has("client_id") || !jsonObject.has("token")) {
				return false;
			}

			String strCometCid = jsonObject.getString("client_id");
			String strCometToken = jsonObject.getString("token");
			if (null == strCometCid || null == strCometToken || strCometCid.isEmpty() || strCometToken.isEmpty()) {
				return false;
			}
			// comet id token
			map.put("cometCid", strCometCid);
			map.put("cometToken", strCometToken);
		} catch (Exception e) {
			logger.error("<rpcAdminLoginComet->Exception> uid:" + uid + ">>>" + e.toString());
			return false;
		}
		return true;
	}

	@Override
	public void updAccountid(int uid) {
		Integer accountid = userservice.getAccountId();
		int ires = 0;
		try {
			ires = DBHelper.execute(VarConfigUtils.dbZhuUser,
					StringUtils.getSqlString(SQL_updAccountIdByUid, "user_account_", uid), false, accountid, uid);
			if (ires > 0) {
				userservice.getUserAccountByUid(uid, true);
			}
		} catch (Exception e) {
			logger.error("<updUserPwordByUid->Exception>" + e.toString());
		}
	}

	/**
	 * 记录注册/登录详细
	 * 
	 * @param uid
	 *            用户uid
	 * @param os
	 *            注册/登录设备
	 * @param loginMethod
	 *            注册方式
	 * @param mobileModel
	 *            机型
	 * @param channel
	 *            注册渠道
	 * @param mobileVersion
	 *            版本号
	 * @param isType
	 *            =1注册 =2登录
	 * @param loginTime
	 *            登录时间
	 */
	@Override
	public void insertLoginDetail(int uid, int os, String loginMethod, String mobileModel, String channel,
			String mobileVersion, int isType, Long loginTime) {

		String today = DateUtils.dateToString(null, "yyyyMMdd");
		Long logintime = System.currentTimeMillis() / 1000;
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuAdmin).getConnection();
			statement = conn.prepareStatement(SQL_getLoginDetail);
			DBHelper.setPreparedStatementParam(statement, uid, 1);
			rs = statement.executeQuery();
			if (rs != null && rs.next()) {
				if (isType == 2) {
					// 登录
					if (!UserRedisService.getInstance().getLoginDetail(uid, today)) {
						// 未添加记录
						int registTime = rs.getInt("loginTime") + 1296000;
						if (System.currentTimeMillis() / 1000 < registTime) {
							// 15天内
							if (rs != null) {
								rs.close();
							}
							if (statement != null) {
								statement.close();
							}
							statement = conn.prepareStatement(SQL_InsertLoginDetail);
							DBHelper.setPreparedStatementParam(statement, uid, os, loginMethod, mobileModel, channel,
									mobileVersion, isType, loginTime);
							statement.executeUpdate();
						}
					}
				}
			} else {
				if (isType == 1) {
					// 新注册
					if (statement != null) {
						statement.close();
					}
					statement = conn.prepareStatement(SQL_InsertLoginDetail);
					DBHelper.setPreparedStatementParam(statement, uid, os, loginMethod, mobileModel, channel,
							mobileVersion, isType, loginTime);
					statement.executeUpdate();
				}
			}
			UserRedisService.getInstance().addLoginDetail(uid, today, logintime.toString());
			//日活跃量不包括16wifi渠道数据
			if(!channel.equals("16wifi")){
				UserRedisService.getInstance().addLoginDetail(uid, os+":"+today, logintime.toString());
			}
			
		} catch (Exception e) {
			logger.error("传入参数：uid=" + uid + " os=" + os + " loginMethod=" + loginMethod + " mobileModel=" + mobileModel
					+ " channel=" + channel + " mobileVersion=" + mobileVersion + " isType=" + isType + " loginTime="
					+ loginTime);
			logger.error("error-insertLoginDetail->Exception:" + e.toString());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception ex) {
				logger.error("error-insertLoginDetail->finally->Exception:" + ex.toString());
			}
		}
	}

	public static void main(String[] args) {
		// 开始校验token值
		/**
		 * /room/ranks?ncode=F2E4864F7C05B1D63E76CEC7FC6D9419&channel=lsyysd&
		 * reqtime=1467705725908&os=1&imei=869552025911934&source=weixin&token=
		 * MTAwNTkzNzNfQ0VDM0E3RDFDQzRBNEU5NUQyOUU0MTM0OUM0NDhDMEJfMV84Njk1NTIwMjU5MTE5MzRfMTQ2NzcwNTU2Mw
		 * ==
		 */
		String signStr = AuthServiceImpl.TOKENHTTPREQKEY;
		String imei = "869552025911934";
		String os = "1";
		String reqtime = "1467705725908";
		signStr = signStr + imei + os + reqtime;

		String sign = EncryptUtils.md5Encrypt(signStr);
		String leftSign = sign.substring(0, 10);
		String rightSign = sign.substring(sign.length() - 10);
		sign = EncryptUtils.md5Encrypt(leftSign + rightSign);

		System.err.println(sign);

		///////////////////////////
		String day = DateUtils.dateToString(null, "yyyyMMdd");
		String month = DateUtils.getTimesMonthmorning("yyyyMM");
		String week = DateUtils.getWeekStart(0);

		System.err.println(String.format("day:%s,week:%s,month:%s", day, week, month));

	}

	@Override
	public void updUnionIdOfAccount(int uid, String unionId) {

		int ires = 0;
		try {
			ires = DBHelper.execute(VarConfigUtils.dbZhuUser,
					StringUtils.getSqlString(SQL_updUnionIdByUid, "user_account_", uid), false, unionId, uid);
			if (ires > 0) {
				userservice.getUserAccountByUid(uid, true);

				RedisCommService.getInstance().hset(RedisContant.RedisNameUser, RedisContant.keyUnionIdAndUid, unionId, String.valueOf(uid), 0);
			}
		} catch (Exception e) {
			logger.error("<updUnionIdOfAccount->Exception>" + e.toString());
		}		
	}
}
