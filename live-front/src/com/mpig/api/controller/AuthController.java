package com.mpig.api.controller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mpig.api.as.util.AsUtil;
import com.mpig.api.async.AsyncManager;
import com.mpig.api.async.IAsyncTask;
import com.mpig.api.dictionary.lib.PayConfigLib;
import com.mpig.api.dictionary.lib.TaskConfigLib.TaskFor;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.SalesmanModel;
import com.mpig.api.model.UserAccountModel;
import com.mpig.api.model.UserAssetModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.redis.service.RedisCommService;
import com.mpig.api.redis.service.UserRedisService;
import com.mpig.api.service.IAuthService;
import com.mpig.api.service.IOrderService;
import com.mpig.api.service.ISalesmanService;
import com.mpig.api.service.IUserGuardInfoService;
import com.mpig.api.service.IUserItemService;
import com.mpig.api.service.IUserService;
import com.mpig.api.service.IVerifyCodeService;
import com.mpig.api.sns.SnsApi;
import com.mpig.api.sns.SnsApiForBaofeng;
import com.mpig.api.statistics.Statistics;
import com.mpig.api.task.TaskService;
import com.mpig.api.utils.AESCipher;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.Constant;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.ParamHandleUtils;
import com.mpig.api.utils.RandomUtil;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.StringUtils;

@Controller
@Scope("prototype")
@RequestMapping("/auth")
public class AuthController extends BaseController {

	@Resource
	private IAuthService authService;
	@Resource
	private IUserService userService;
	@Resource
	private IOrderService orderService;
	@Resource
	private IUserGuardInfoService guardInfoService;
	@Resource
	private IUserItemService userItemService;
	@Resource
	private IVerifyCodeService verifyCodeService;
	@Resource
	private ISalesmanService salesmanService;

	private static final Logger logger = Logger.getLogger(AuthController.class);

	@RequestMapping("/weixinCode")
	public void refreshWeixinAuthorizeCode(HttpServletRequest req, final HttpServletResponse resp) {
		String url = req.getParameter("url");
		if (url == null) {
			return;
		}
		logger.debug("<refreshWeixinAuthorizeCode>-->url" + url);
		String encode;
		try {
			encode = URLEncoder.encode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return;
		}
		String redirectUrl = String.format(SnsApi.TencentWeixinAuthorizeCodeApi,
				PayConfigLib.getConfig().getWeixin_appid(), encode);
		logger.info("refreshWeixinAuthorizeCode>" + ">>>>>>>>" + redirectUrl);
		try {
			resp.sendRedirect(redirectUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/sns_weixin_info", method = RequestMethod.GET)
	public void snsWeixinInfo(HttpServletRequest req, final HttpServletResponse resp) throws JSONException {
		String code = req.getParameter("code");
		if (code != null && !"authdeny".equals(code)) {
			com.alibaba.fastjson.JSONObject jsonObject = SnsApi.getUserWXTokenForH5(code);
			if (jsonObject.get("errcode") != null && jsonObject.get("errcode").equals("40001")) {
				logger.warn(String.format("getUserWXToken>>state:%s,code:%s", jsonObject.toString(), code));
				returnModel.setCode(CodeContant.OpenIdGetFailed);
				returnModel.setMessage("获取AccessToken信息失败");
				writeJson(resp, returnModel);
				return;
			}
			if (jsonObject.getString("openid") == null) {
				logger.warn(String.format("weixinWebCall>>openId is null:%s", jsonObject.toString()));
				returnModel.setCode(CodeContant.UnValidOpenId);
				returnModel.setMessage("无效第三方标示");
				writeJson(resp, returnModel);
				return;
			}

			String openId = jsonObject.getString("openid");
			String access_token = jsonObject.getString("access_token");

			com.alibaba.fastjson.JSONObject snsUserInfo = SnsApi.getUserInfoByToken(access_token, openId);
			if (snsUserInfo == null) {
				logger.error(String.format("getUserInfoByToken filed access_token:%s,openId:%s", access_token, openId));
				returnModel.setCode(CodeContant.SNSUserInfoGetFailed);
				returnModel.setMessage("获取第三方消息失败");
				writeJson(resp, returnModel);
				return;
			} else {
				snsUserInfo.put("access_token", access_token);
				returnModel.setData(snsUserInfo);
				logger.info(">>>>>>>>>>" + snsUserInfo.toString());
				writeJson(resp, returnModel);
			}
			// req.setAttribute("snsUserInfo", snsUserInfo);
		} else {
			logger.warn(String.format("weixinWebCall>>code:%s client code is unvalid", code));
			returnModel.setCode(CodeContant.UnValidCode);
			returnModel.setMessage("无效凭证");
			writeJson(resp, returnModel);
			return;
		}

		// req.getRequestDispatcher("main.jsp").forward(req, resp);
	}

	@RequestMapping(value = "/sns_weixin_info_for_open", method = RequestMethod.GET)
	public void snsWeixinInfoForOpen(HttpServletRequest req, final HttpServletResponse resp) throws JSONException {
		String code = req.getParameter("code");
		if (code != null && !"authdeny".equals(code)) {
			com.alibaba.fastjson.JSONObject jsonObject = SnsApi.getUserWXTokenForOpen(code);
			if (jsonObject.get("errcode") != null && jsonObject.get("errcode").equals("40001")) {
				logger.warn(String.format("snsWeixinInfoForOpen>>state:%s,code:%s", jsonObject.toString(), code));
				returnModel.setCode(CodeContant.OpenIdGetFailed);
				returnModel.setMessage("获取AccessToken信息失败");
				writeJson(resp, returnModel);
				return;
			}
			if (jsonObject.getString("openid") == null) {
				logger.warn(String.format("snsWeixinInfoForOpen>>openId is null:%s", jsonObject.toString()));
				returnModel.setCode(CodeContant.UnValidOpenId);
				returnModel.setMessage("无效第三方标示");
				writeJson(resp, returnModel);
				return;
			}

			String openId = jsonObject.getString("openid");
			String access_token = jsonObject.getString("access_token");

			com.alibaba.fastjson.JSONObject snsUserInfo = SnsApi.getUserInfoByToken(access_token, openId);
			if (snsUserInfo == null) {
				logger.error(
						String.format("snsWeixinInfoForOpen filed access_token:%s,openId:%s", access_token, openId));
				returnModel.setCode(CodeContant.SNSUserInfoGetFailed);
				returnModel.setMessage("获取第三方消息失败");
				writeJson(resp, returnModel);
				return;
			} else {
				snsUserInfo.put("access_token", access_token);
				returnModel.setData(snsUserInfo);
				logger.info(">>>>>>>>>>" + snsUserInfo.toString());
				writeJson(resp, returnModel);
			}
			// req.setAttribute("snsUserInfo", snsUserInfo);
		} else {
			logger.warn(String.format("snsWeixinInfoForOpen>>code:%s client code is unvalid", code));
			returnModel.setCode(CodeContant.UnValidCode);
			returnModel.setMessage("无效凭证");
			writeJson(resp, returnModel);
			return;
		}

		// req.getRequestDispatcher("main.jsp").forward(req, resp);
	}

	@RequestMapping(value = "/sns_weixin_info_for_bf", method = RequestMethod.GET)
	public void snsWeixinInfoForBaoFeng(HttpServletRequest req, final HttpServletResponse resp) throws JSONException {
		String code = req.getParameter("code");
		if (code != null && !"authdeny".equals(code)) {
			com.alibaba.fastjson.JSONObject jsonObject = SnsApiForBaofeng.getUserWXTokenForOpen(code);
			if (jsonObject.get("errcode") != null && jsonObject.get("errcode").equals("40001")) {
				logger.warn(String.format("snsWeixinInfoForBaoFeng>>state:%s,code:%s", jsonObject.toString(), code));
				returnModel.setCode(CodeContant.OpenIdGetFailed);
				returnModel.setMessage("获取AccessToken信息失败");
				writeJson(resp, returnModel);
				return;
			}
			if (jsonObject.getString("openid") == null) {
				logger.warn(String.format("snsWeixinInfoForBaoFeng>>openId is null:%s", jsonObject.toString()));
				returnModel.setCode(CodeContant.UnValidOpenId);
				returnModel.setMessage("无效第三方标示");
				writeJson(resp, returnModel);
				return;
			}

			String openId = jsonObject.getString("openid");
			String access_token = jsonObject.getString("access_token");

			com.alibaba.fastjson.JSONObject snsUserInfo = SnsApi.getUserInfoByToken(access_token, openId);
			if (snsUserInfo == null) {
				logger.error(
						String.format("snsWeixinInfoForBaoFeng filed access_token:%s,openId:%s", access_token, openId));
				returnModel.setCode(CodeContant.SNSUserInfoGetFailed);
				returnModel.setMessage("获取第三方消息失败");
				writeJson(resp, returnModel);
				return;
			} else {
				snsUserInfo.put("access_token", access_token);
				returnModel.setData(snsUserInfo);
				logger.info(">>>>>>>>>>" + snsUserInfo.toString());
				writeJson(resp, returnModel);
			}
			// req.setAttribute("snsUserInfo", snsUserInfo);
		} else {
			logger.warn(String.format("snsWeixinInfoForBaoFeng>>code:%s client code is unvalid", code));
			returnModel.setCode(CodeContant.UnValidCode);
			returnModel.setMessage("无效凭证");
			writeJson(resp, returnModel);
			return;
		}

		// req.getRequestDispatcher("main.jsp").forward(req, resp);
	}

	@RequestMapping(value = "/sns_qq_info", method = RequestMethod.GET)
	public void snsQQInfo(HttpServletRequest req, final HttpServletResponse resp) throws JSONException {
		String accessToken = req.getParameter("access_token");
		if (accessToken != null) {
			/**
			 * {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"}
			 */
			com.alibaba.fastjson.JSONObject jsonObject = SnsApi.getUserQQOpenId(accessToken);
			if (jsonObject.get("errcode") != null) {
				logger.warn(String.format("sns_qq_info>>state:%s,code:%s", jsonObject.toString(), accessToken));
				returnModel.setCode(CodeContant.OpenIdGetFailed);
				returnModel.setMessage("获取AccessToken信息失败");
				writeJson(resp, returnModel);
				return;
			}

			// 获取OpenID
			String openId = jsonObject.getString("openid");
			// 获取用户信息
			com.alibaba.fastjson.JSONObject snsUserInfo = SnsApi.getQQUserInfoByToken(accessToken, openId);

			if (snsUserInfo == null) {
				logger.error(String.format("getUserInfoByToken filed access_token:%s,openId:%s", accessToken, openId));
				returnModel.setCode(CodeContant.SNSUserInfoGetFailed);
				returnModel.setMessage("获取第三方消息失败");
				writeJson(resp, returnModel);
				return;
			} else {
				snsUserInfo.put("openid", openId);
				snsUserInfo.put("access_token", accessToken);
				returnModel.setData(snsUserInfo);
				writeJson(resp, returnModel);
			}
		} else {
			logger.warn(String.format("qqWebCall>>code:%s client code is unvalid", accessToken));
			returnModel.setCode(CodeContant.UnValidCode);
			returnModel.setMessage("无效凭证");
			writeJson(resp, returnModel);
			return;
		}
	}

	@RequestMapping(value = "/sns_qq_info_for_bf", method = RequestMethod.GET)
	public void snsQQInfoForBaoFeng(HttpServletRequest req, final HttpServletResponse resp) throws JSONException {
		String accessToken = req.getParameter("access_token");
		if (accessToken != null) {
			/**
			 * {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"}
			 */
			com.alibaba.fastjson.JSONObject jsonObject = SnsApi.getUserQQOpenId(accessToken);
			if (jsonObject.get("errcode") != null) {
				logger.warn(String.format("snsQQInfoForBaoFeng>>state:%s,code:%s", jsonObject.toString(), accessToken));
				returnModel.setCode(CodeContant.OpenIdGetFailed);
				returnModel.setMessage("获取AccessToken信息失败");
				writeJson(resp, returnModel);
				return;
			}

			String openId = jsonObject.getString("openid");
			com.alibaba.fastjson.JSONObject snsUserInfo = SnsApi.getQQUserInfoByToken(accessToken, openId);

			if (snsUserInfo == null) {
				logger.error(String.format("snsQQInfoForBaoFeng>>getUserInfoByToken filed access_token:%s,openId:%s",
						accessToken, openId));
				returnModel.setCode(CodeContant.SNSUserInfoGetFailed);
				returnModel.setMessage("获取第三方消息失败");
				writeJson(resp, returnModel);
				return;
			} else {
				snsUserInfo.put("openid", openId);
				snsUserInfo.put("access_token", accessToken);
				returnModel.setData(snsUserInfo);
				writeJson(resp, returnModel);
			}
		} else {
			logger.warn(String.format("qqWebCall>>code:%s client code is unvalid", accessToken));
			returnModel.setCode(CodeContant.UnValidCode);
			returnModel.setMessage("无效凭证");
			writeJson(resp, returnModel);
			return;
		}
	}

	@RequestMapping(value = "/sns_sina_info", method = RequestMethod.GET)
	public void snsSinaWeiboInfo(HttpServletRequest req, final HttpServletResponse resp) throws JSONException {
		String code = req.getParameter("code");
		String redirectUrl = req.getParameter("redirectUrl");
		if (code != null && redirectUrl != null) {
			com.alibaba.fastjson.JSONObject jsonObject = SnsApi.getSinaWeiboId(code, redirectUrl);
			if (jsonObject == null || ((jsonObject != null) && jsonObject.containsKey("error_code"))) {
				logger.warn(String.format("sns_qq_info>>redirectUrl:%s,code:%s", redirectUrl, code));
				returnModel.setCode(CodeContant.OpenIdGetFailed);
				returnModel.setMessage("获取AccessToken信息失败");
				writeJson(resp, returnModel);
				return;
			}

			// 获取uid
			String uid = jsonObject.getString("uid");
			String access_token = jsonObject.getString("access_token");
			// 获取用户信息
			com.alibaba.fastjson.JSONObject snsUserInfo = SnsApi.getSinaWeiboUserInfoByToken(access_token, uid);

			if (snsUserInfo == null) {
				logger.error(String.format("snsSinaWeiboInfo filed access_token:%s,openId:%s", access_token, uid));
				returnModel.setCode(CodeContant.SNSUserInfoGetFailed);
				returnModel.setMessage("获取第三方消息失败");
				writeJson(resp, returnModel);
				return;
			} else {
				snsUserInfo.put("openid", uid);
				snsUserInfo.put("access_token", access_token);
				returnModel.setData(snsUserInfo);
				writeJson(resp, returnModel);
			}
		} else {
			logger.warn(String.format("sinaWebCall>>code:%s client code is unvalid", code));
			returnModel.setCode(CodeContant.UnValidCode);
			returnModel.setMessage("无效凭证");
			writeJson(resp, returnModel);
			return;
		}
	}

	/**
	 * 第三方登录
	 *
	 * @param req
	 *            参数：openid第三方唯一标示,accesstoken第三方访问token,channel渠道,source第三方账号类型
	 * @param resp
	 */
	@RequestMapping("/loginThird")
	public void loginThird(HttpServletRequest req, final HttpServletResponse resp) throws JSONException {

		long lg1 = System.currentTimeMillis();
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "openid", "accesstoken", "channel",
				"source")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		// 校验码验证及token
		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		// 用户的唯一标识
		String openid = req.getParameter("openid");
		// 网页授权接口调用凭证
		String accessToken = req.getParameter("accesstoken");
		String channel = req.getParameter("channel");
		final String source = req.getParameter("source");

		if ("(null)".equalsIgnoreCase(openid)) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("请下载最新版本...");
			writeJson(resp, returnModel);
			return;
		}
		// 广点通渠道验证
		if (channel.equals("ios")) {
			String idfa = req.getParameter("idfa");
			if (org.apache.commons.lang.StringUtils.isNotEmpty(idfa)) {
				String ostype = AsUtil.registerSource(idfa, req);
				if (org.apache.commons.lang.StringUtils.isNotEmpty(ostype)) {
					channel = ostype;
				}
			}
		}
		final byte os = Byte.parseByte(req.getParameter("os"));
		final String imei = req.getParameter("imei");

		// 记录的用户authkey必须是openid和第三方标识
		final String openidSource = openid + source;

		long lg2 = System.currentTimeMillis();
		logger.debug("loginThirdTime 参数获取时间 lg2-lg1=" + (lg2 - lg1));

		UserAccountModel userAccountModel = null;
		String unionId = "";

		// 如果是手机老用户登录 需要依据unionId合并账号数据
		boolean needMerge = false;

		if ("weixin".equals(source)) {
			unionId = req.getParameter("unionId") == null ? "" : req.getParameter("unionId");
			if (unionId.contains("null")) {

			} else if (org.apache.commons.lang.StringUtils.isNotEmpty(unionId)) {
				userAccountModel = userService.getUserAccountByUnionId(unionId, false);
			}
		}

		if (userAccountModel == null) {
			userAccountModel = userService.getUserAccountByAuthKey(openidSource.toLowerCase(), false);
			if (userAccountModel != null) {
				if (os == 1 || os == 2) {
					needMerge = true;
				}
			}
		}

		if (userAccountModel == null) {
			String nickName = req.getParameter("nickName") == null ? "" : req.getParameter("nickName");
			String headImage = req.getParameter("headImage") == null ? "" : req.getParameter("headImage");
			Boolean sex = "1".equals(req.getParameter("sex")) ? true : false;
			String pword = "";

			if (org.apache.commons.lang.StringUtils.isNotEmpty(nickName)
					|| org.apache.commons.lang.StringUtils.isNotEmpty(headImage)) {
				// 新第三方注册
				int ilen = openidSource.length();
				if (ilen > 16) {
					pword = openidSource.substring(0, 16);
				} else {
					pword = openidSource;
				}

				long regiterIp;
				try {
					regiterIp = StringUtils.ipToLong(InetAddress.getLocalHost().getHostAddress());
					returnModel = authService.register("", pword, regiterIp, nickName, openidSource, source, channel,
							"", os, imei, headImage, sex, "", unionId, 0L);

					if ("weixin".equals(source) && returnModel.getCode() == 200) {

						orderService.insertPayAccount(
								UserRedisService.getInstance().getAuthKeyAndUid(openidSource.toLowerCase()), unionId,
								"", System.currentTimeMillis() / 1000);
					}
				} catch (Exception e) {
					logger.error("<loginThird->Exception>" + e.toString());
				}
				writeJson(resp, returnModel);
			} else {
				// 旧的第三方注册方式
				HttpResponse<JsonNode> asJson = authService.getThirdUserInfo(accessToken, openid, source, os);
				if (asJson == null) {
					returnModel.setCode(CodeContant.THIRDINTERFACE);
					returnModel.setMessage("第三方接口不稳定");
				} else {
					int result = asJson.getStatus();
					if (result == 200) {
						JSONObject jsonObject = asJson.getBody().getObject();

						if ("weixin".equals(source)) {
							if (jsonObject.has("errcode")) {
								returnModel.setCode(CodeContant.THIRDINTERFACE);
								returnModel.setMessage(jsonObject.getString("errmsg"));
							} else {
								nickName = subNickname(jsonObject.getString("nickname"), 14);// 7个汉字
								// 14个英文
								headImage = jsonObject.getString("headimgurl");
								sex = jsonObject.getInt("sex") == 2 ? false : true;
								unionId = jsonObject.getString("unionid");
							}
						} else if ("qq".equals(source)) {
							if (Integer.valueOf(jsonObject.getInt("ret")) != 0) {
								returnModel.setCode(CodeContant.THIRDINTERFACE);
								returnModel.setMessage(jsonObject.getString("msg"));
							} else {
								nickName = subNickname(jsonObject.getString("nickname"), 14);
								headImage = "";
								sex = jsonObject.getString("gender").equals("男") ? true : false;
							}
						} else if ("sina".equals(source)) {
							if (jsonObject.has("error_code")) {
								returnModel.setCode(CodeContant.THIRDINTERFACE);
								returnModel.setMessage(jsonObject.getString("error"));
							} else {

								nickName = subNickname(jsonObject.getString("screen_name"), 14);
								headImage = jsonObject.getString("avatar_hd");
								sex = jsonObject.getString("gender").equals("m") ? true : false;
							}
						} else if ("tiancheng".equals(source)) { // h5登录 目前不用
							// if (jsonObject.has("result") && 0 ==
							// jsonObject.getString("result").compareToIgnoreCase("OK")) {
							// nickName = subNickname(jsonObject.getString("userNickName"), 14);
							// headImage = jsonObject.getString("userIcon");
							// sex = jsonObject.getString("userGender").equals("M") ? true : false;
							// } else {
							// returnModel.setCode(CodeContant.THIRDINTERFACE);
							// returnModel.setMessage(jsonObject.getString("result"));
							// }
						} else {
							// 参数出问题
							returnModel.setCode(CodeContant.THIRDINTERFACE);
							returnModel.setMessage("参数不对");
						}
						if (returnModel.getCode() == CodeContant.OK) {
							int ilen = openidSource.length();
							if (ilen > 16) {
								pword = openidSource.substring(0, 16);
							} else {
								pword = openidSource;
							}
							long regiterIp;
							try {
								regiterIp = StringUtils.ipToLong(InetAddress.getLocalHost().getHostAddress());
								returnModel = authService.register("", pword, regiterIp, nickName, openidSource, source,
										channel, "", os, imei, headImage, sex, "", unionId, 0L);
								if ("weixin".equals(source) && returnModel.getCode() == 200) {
									orderService.insertPayAccount(
											UserRedisService.getInstance().getAuthKeyAndUid(openidSource.toLowerCase()),
											unionId, "", System.currentTimeMillis() / 1000);
								}

							} catch (Exception e) {
								logger.error("<completed->Exception>" + e.toString());
							}
						}
					}
					writeJson(resp, returnModel);
				}
			}
		} else {
			// 已注册，登录
			if (userAccountModel.getStatus() == 0) {
				// 冻结用户
				returnModel.setCode(CodeContant.ConAccountFroze);
				returnModel.setMessage("您的帐号已冻结，请联系客服");
			} else {
				if (userAccountModel.getAccountid() == 0) {
					authService.updAccountid(userAccountModel.getUid());
				} else if (org.apache.commons.lang.StringUtils.isEmpty(userAccountModel.getUnionId())
						&& org.apache.commons.lang.StringUtils.isNotEmpty(unionId)) {

					authService.updUnionIdOfAccount(userAccountModel.getUid(), unionId);
				}
				int uid = userAccountModel.getUid();
				Map<String, Object> map = userService.getUserDataMap(uid, uid);

				if (map.size() <= 0) {
					returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
					returnModel.setMessage("该账号异常，请重新登录");
				} else {
					// TOSY 手机端需要知道增加两个字段，和作用，以及更新文档
					// RPC admin获取comet的cid和token 放到map中
					if (false == authService.rpcAdminLoginComet(uid, map)) {
						returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
						returnModel.setMessage("AdminFailed");
					} else {
						map.put("token", authService.encryptToken(userAccountModel.getUid(),
								userAccountModel.getPassword(), os, imei));
						returnModel.setCode(CodeContant.OK);
						returnModel.setData(map);

						String strChannel = req.getParameter("channel") == null ? "" : req.getParameter("channel");

						try {
							// new UserLoginDetailAsyncTask(uid, os, "", "", strChannel).runAsync();
							AsyncManager.getInstance()
									.execute(new UserLoginDetailAsyncTask(uid, os, "", "", strChannel));
						} catch (Exception ex) {
							logger.error("loginThird>>>", ex);
						}

					}
				}
			}
			writeJson(resp, returnModel);
		}
	}

	@Deprecated
	public void loginReward(int uid) {
		/* 这里注释是为了去掉登录奖励 */
		/*
		 * int shrqzhuId = 29; //守护人气猪id //获取用户背包里已有的人气猪数量 String firstLogin =
		 * RedisCommService.getInstance().get(RedisContant.RedisNameOther, RedisContant.guardFirstLoginOfDay + uid); if
		 * (firstLogin == null) { //首次登陆增加守护的经验值并返回所有守护身份需要增加的守护人气猪数量 int mengzhuCount =
		 * guardInfoService.firstLoginUpdExp(uid); Map<Integer, UserItemModel> userItemMap =
		 * userItemService.getItemListByUid(uid, false); UserItemModel itemModel = userItemMap.get(shrqzhuId); int
		 * haveRqNum = 0; // 背包里已有的守护人气猪数量 if (itemModel != null) { haveRqNum = itemModel.getNum(); } if (mengzhuCount >
		 * 0) { if (haveRqNum > mengzhuCount) { userItemService.delItemByGid(uid, shrqzhuId); } else { mengzhuCount =
		 * mengzhuCount - haveRqNum; } // 更新人气猪数量并刷新缓存 if(mengzhuCount>0){ userItemService.insertUserItem(uid,
		 * shrqzhuId, mengzhuCount, ItemSource.Activity); } } else { if (haveRqNum > 0) {
		 * userItemService.delItemByGid(uid, shrqzhuId); } } }
		 */}

	/**
	 * 按字节截取字符串
	 *
	 * @param nickname
	 * @param count
	 *            字节数 GBK 编码
	 * @return
	 */
	private String subNickname(String nickname, int count) {
		if (nickname != null && !"".equals(nickname)) {
			try {
				int length = nickname.getBytes("GBK").length;
				if (count > 0 && count < length) {
					StringBuilder buff = new StringBuilder();
					char c;
					for (int i = 0; i < count; i++) {
						c = nickname.charAt(i);
						buff.append(c);
						if (String.valueOf(c).getBytes("GBK").length > 1) {
							// 遇到中文汉字，截取字节总数减1
							--count;
						}
					}
					return new String(buff.toString().getBytes(), "UTF-8");
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				nickname = nickname.substring(0, 7);
			}
		}
		return nickname;
	}

	/**
	 * 扫描注册二维码跳转地址<p/>
	 */
	@RequestMapping(value = "/qrregist", method = RequestMethod.GET)
	public void qrregist(HttpServletRequest req, HttpServletResponse resp, String code) {
		String id = null;
		try {
			do {
				if (ParamHandleUtils.isBlank(req, "code")) {
					returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
					returnModel.setMessage("缺少参数或参数为空");
					break;
				}
				String idtime = AESCipher.aesDecryptString(code);
				String[] split = org.apache.commons.lang.StringUtils.split(idtime, "_");
				id = split[0];
				// String time = split[1];
				SalesmanModel salesman = salesmanService.getSalesmanById(Long.parseLong(id));
				if (null == salesman) {
					returnModel.setCode(201);
					returnModel.setMessage("二维码不正确");
					break;
				}
				String qrcode = salesman.getQrcode();
				if (org.apache.commons.lang.StringUtils.isBlank(qrcode)) {
					returnModel.setCode(202);
					returnModel.setMessage("二维码已失效");
					break;
				}
				// 取出code
				String codestr = org.apache.commons.lang.StringUtils.substring(qrcode,
						org.apache.commons.lang.StringUtils.lastIndexOf(qrcode, "?code="));
				if (null != codestr && codestr.length() > 6) {
					codestr = org.apache.commons.lang.StringUtils.substring(codestr, 6);
					if (org.apache.commons.lang.StringUtils.equals(codestr, URLEncoder.encode(code,"utf-8"))) {
						returnModel.setCode(200);
						returnModel.setMessage("二维码扫描成功");
						break;
					} else {
						returnModel.setCode(203);
						returnModel.setMessage("二维码已失效");
						break;
					}
				} else {
					returnModel.setCode(204);
					returnModel.setMessage("二维码已失效");
					break;
				}
			} while (false);
		} catch (Exception e) {
			returnModel.setCode(500);
			returnModel.setMessage("二维码识别出现异常");
		}
		try {
			// writeJson(resp, returnModel);
			//String msg = returnModel.getCode() == 200 ? "" : returnModel.getMessage();
			String msg = returnModel.getCode() + "";
			resp.sendRedirect(String.format(Constant.anchor_register_url,URLEncoder.encode(code, "utf-8"),msg));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 手机注册
	 *
	 * @param req  参数: mobile手机号、pword密码
	 * @param resp
	 */
	@RequestMapping(value = "/regist", method = RequestMethod.GET)
	public void regist(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "mobile", "pword", "code", "nickName", "codestr", "os", "channel")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		String mobile = req.getParameter("mobile");
		String pword = req.getParameter("pword").toUpperCase();
		String chcekCode = req.getParameter("code");
		String nickName = req.getParameter("nickName");
		String channel = req.getParameter("channel");
		String source = req.getParameter("source");
		String code = req.getParameter("codestr");
		try {
			// 获取salesmanId
			logger.debug("codestr:"+code);
			String idtime = AESCipher.aesDecryptString(URLDecoder.decode(code,"utf-8"));
			String[] split = org.apache.commons.lang.StringUtils.split(idtime, "_");
			String salesmanId = split[0];
			//判断链接（二维码）是否失效
			SalesmanModel salesman = salesmanService.getSalesmanById(Long.parseLong(salesmanId));
			if (null == salesman) {
				returnModel.setCode(201);
				returnModel.setMessage("二维码不正确，请重新扫码进入页面");
				writeJson(resp, returnModel);
				return;
			}
			String qrcode = salesman.getQrcode();
			if (org.apache.commons.lang.StringUtils.isBlank(qrcode)) {
				returnModel.setCode(202);
				returnModel.setMessage("二维码已失效，请重新扫码进入页面");
				writeJson(resp, returnModel);
				return;
			}
			// 取出code
			String codestr = org.apache.commons.lang.StringUtils.substring(qrcode,
					org.apache.commons.lang.StringUtils.lastIndexOf(qrcode, "?code="));
			boolean checkQrCodeFlag = false;
			if (null != codestr && codestr.length() > 6) {
				codestr = org.apache.commons.lang.StringUtils.substring(codestr, 6);
				logger.debug("对比:"+codestr+" === "+code);
				if (org.apache.commons.lang.StringUtils.equals(codestr, code)) {
					checkQrCodeFlag = true;
				} else {
					returnModel.setMessage("二维码已失效");
				}
			} else {
				returnModel.setMessage("二维码已失效!");
			}
			if(!checkQrCodeFlag) {
				returnModel.setCode(203);
				writeJson(resp, returnModel);
				return;
			}
			
			byte os = Byte.parseByte(req.getParameter("os"));
			String imei = req.getParameter("imei") == null ? "" : req.getParameter("imei");
			int uid = UserRedisService.getInstance().getAccountNameAndUid(mobile);
			if (uid > 0) {
				returnModel.setCode(CodeContant.MobileRegisted);
				returnModel.setMessage("该手机号已注册过");
				writeJson(resp, returnModel);
				return;
			}
			if (!OtherRedisService.getInstance().getSendCode(mobile, chcekCode)) {
				returnModel.setCode(CodeContant.MobileCodeErr);
				returnModel.setMessage("验证码不正确");
				writeJson(resp, returnModel);
				return;
			}
			String openidSource = mobile;

			long regiterIp = StringUtils.ipToLong(InetAddress.getLocalHost().getHostAddress());
			returnModel = authService.register(mobile, pword, regiterIp, nickName, openidSource, source, channel, "",
					os, imei, "", false, mobile, "", Long.parseLong(salesmanId));
		} catch (Exception e) {
			logger.error("<completed->Exception>" + e.toString());
			returnModel.setCode(500);
			returnModel.setMessage("注册失败");
		}
		writeJson(resp, returnModel);
	}

	/**
	 * 手机密码登录
	 *
	 * @param req  参数：mobile手机号，pword密码
	 * @param resp
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public void login(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "mobile", "pword")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "mobile")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		if("2".equals(req.getParameter("os"))&&org.apache.commons.lang.StringUtils.isBlank(req.getParameter("appVersion"))) {
			returnModel.setCode(CodeContant.CONSYSTEMERR);
			returnModel.setMessage("请升级为最新版本");
			writeJson(resp, returnModel);
			return;
		}
		
		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			writeJson(resp, returnModel);
			return;
		}

		String mobile = req.getParameter("mobile");
		String pword = req.getParameter("pword").toUpperCase();
		byte os = Byte.parseByte(req.getParameter("os"));
		String imei = req.getParameter("imei");
		String strChannel = req.getParameter("channel") == null ? "" : req.getParameter("channel");

		int uid = UserRedisService.getInstance().getAccountNameAndUid(mobile);

		if (uid <= 0) {
			returnModel.setCode(CodeContant.USERPASSWORD);
			returnModel.setMessage("账号不存在或密码错误！");
			writeJson(resp, returnModel);
			return;
		}

		UserAccountModel userAccountModel = userService.getUserAccountByUid(uid, false);
		if (userAccountModel == null) {
			returnModel.setCode(CodeContant.USERPASSWORD);
			returnModel.setMessage("账号异常，请重新登录");
			writeJson(resp, returnModel);
			return;
		}
		if (userAccountModel.getStatus() == 0) {
			returnModel.setCode(CodeContant.ConAccountFroze);
			returnModel.setMessage("您的帐号已冻结，请联系客服");
			writeJson(resp, returnModel);
			return;
		}

		if (!userAccountModel.getPassword().equals(pword)) {
			returnModel.setCode(CodeContant.USERPASSWORD);
			returnModel.setMessage("密码错误");
			writeJson(resp, returnModel);
			return;
		}

		if (userAccountModel.getAccountid() == 0) {
			authService.updAccountid(userAccountModel.getUid());
		}
		Map<String, Object> map = userService.getUserDataMap(uid, uid);
		if (map.size() <= 0) {
			returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
			returnModel.setMessage("该账号异常，请重新登录");
			writeJson(resp, returnModel);
			return;
		}

		// TOSY 手机端需要知道增加两个字段，和作用，以及更新文档
		// RPC admin获取comet的cid和token 放到map中
		if (false == authService.rpcAdminLoginComet(uid, map)) {
			returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
			returnModel.setMessage("AdminFailed");
			writeJson(resp, returnModel);
			return;
		}

		map.put("token", authService.encryptToken(userAccountModel.getUid(), userAccountModel.getPassword(), os, imei));
		returnModel.setCode(CodeContant.OK);
		returnModel.setData(map);
		try {
			AsyncManager.getInstance().execute(new UserLoginDetailAsyncTask(uid, os, "", "", strChannel));
		} catch (Exception ex) {
			logger.error("UserLoginDetailAsyncTask>>>", ex);
		}
		writeJson(resp, returnModel);
	}

	/**
	 * 断线重连 获取socket id 和token
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getComet")
	@ResponseBody
	public ReturnModel getComet(HttpServletRequest request, HttpServletResponse response) {
		if (ParamHandleUtils.isBlank(request, "ncode", "os", "imei", "reqtime", "token")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(request, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		authToken(request, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}
		String token = request.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (false == authService.rpcAdminLoginComet(uid, map)) {
			returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
			returnModel.setMessage("AdminFailed");
		} else {
			returnModel.setCode(CodeContant.OK);
			returnModel.setData(map);
		}
		return returnModel;
	}

	@RequestMapping(value = "/idslogin")
	@ResponseBody
	public ReturnModel idsLogin(HttpServletRequest request, HttpServletResponse response) {

		if (ParamHandleUtils.isBlank(request, "ncode", "os", "imei", "reqtime", "mobile", "ids")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(request, "os", "reqtime", "mobile", "ids")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}

		// 校验码验证及token
		authToken(request, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}
		int uid = Integer.valueOf(request.getParameter("ids"));
		String mobile = request.getParameter("mobile");

		byte os = Byte.parseByte(request.getParameter("os"));
		String imei = request.getParameter("imei");

		int times = UserRedisService.getInstance().getCodeUsesTimes(mobile);
		if (times > 5) {
			returnModel.setCode(CodeContant.ConCodeTimes);
			returnModel.setMessage("验证码错误输入超过5次，请10分钟后再试");

			return returnModel;
		}
		if (true) {
			// 已注册用户
			UserAccountModel userAccountModel = userService.getUserAccountByUid(uid, false);
			if (userAccountModel == null) {
				returnModel.setCode(CodeContant.ConParamTypeIsErr);
				returnModel.setMessage("参数有问题，请重新输入1");
				return returnModel;
			}
			if (userAccountModel.getStatus() == 0) {
				// 冻结用户
				returnModel.setCode(CodeContant.ConAccountFroze);
				returnModel.setMessage("您的帐号已冻结，请联系客服");
				return returnModel;
			}

			String userPhone = userService.getUserPhone(uid);
			if (org.apache.commons.lang.StringUtils.isEmpty(userPhone) || !mobile.equals(userPhone)) {
				returnModel.setCode(CodeContant.mobileBindErr);
				returnModel.setMessage("该手机号或账号不正确");
				return returnModel;
			}

			Map<String, Object> map = userService.getUserDataMap(uid, uid);

			if (map.size() <= 0) {
				returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
				returnModel.setMessage("该账号异常，请重新登录");
			} else {
				// 手机端需要知道增加两个字段，和作用，以及更新文档
				// RPC admin获取comet的cid和token 放到map中
				if (false == authService.rpcAdminLoginComet(uid, map)) {
					returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
					returnModel.setMessage("AdminFailed");
				} else {
					map.put("token", authService.encryptToken(userAccountModel.getUid(), userAccountModel.getPassword(),
							os, imei));
					returnModel.setCode(CodeContant.OK);
					returnModel.setData(map);
				}
			}
		} else {
			// UserRedisService.getInstance().setCodeUsesTimes(mobile);
			// returnModel.setCode(CodeContant.MobileCodeErr);
			// returnModel.setMessage("验证码不正确");
		}

		return returnModel;
	}

	/**
	 * 手机验证码登录
	 *
	 * @param req  参数 ：mobile手机号，code验证码
	 * @param resp
	 */
	@RequestMapping(value = "/logincode", method = RequestMethod.GET)
	public void loginCode(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "mobile", "code", "channel", "source")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "mobile")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		// 校验码验证及token
		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String mobile = req.getParameter("mobile");
		String code = req.getParameter("code");
		byte os = Byte.parseByte(req.getParameter("os"));
		String imei = req.getParameter("imei");
		int times = UserRedisService.getInstance().getCodeUsesTimes(mobile);
		if (times > 5) {
			returnModel.setCode(CodeContant.ConCodeTimes);
			returnModel.setMessage("验证码错误输入超过5次，请10分钟后再试");
			writeJson(resp, returnModel);
			return;
		}

		if (OtherRedisService.getInstance().getSendCode(mobile, code)) {
			int uid = UserRedisService.getInstance().getAccountNameAndUid(mobile);
			if (uid <= 0) {
				// 未注册
				String channel = req.getParameter("channel") == null ? "" : req.getParameter("channel");
				String source = req.getParameter("source") == null ? "" : req.getParameter("source");
				String openidSource = mobile;
				long regiterIp;
				int pword = new Random().nextInt(999999) % (999999 - 100000 + 1) + 100000;
				try {
					// regiterIp = StringUtils.ipToLong(InetAddress.getLocalHost().getHostAddress());
					regiterIp = 170731826;
					returnModel = authService.register(mobile, String.valueOf(pword), regiterIp,
							mobile.substring(0, 3) + "****" + mobile.substring(7), openidSource, source, channel, "",
							os, imei, "", false, mobile, "", 0L);
					if (returnModel.getCode() != 200) {
						writeJson(resp, returnModel);
						return;
					}
					@SuppressWarnings("unchecked")
					Map<String, Object> map = (Map<String, Object>) returnModel.getData();

					System.out.println("prepare get im token");
					// TOSY 手机端需要知道增加两个字段，和作用，以及更新文档
					// RPC admin获取comet的cid和token 放到map中
					if (false == authService.rpcAdminLoginComet((int) map.get("uid"), map)) {
						returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
						returnModel.setMessage("AdminFailed");
					}
				} catch (Exception e) {
					logger.error("<loginCode->Exception>" + e.toString());
				}
			} else {
				// 已注册用户
				UserAccountModel userAccountModel = userService.getUserAccountByUid(uid, false);
				if (userAccountModel.getStatus() == 0) {
					// 冻结用户
					returnModel.setCode(CodeContant.ConAccountFroze);
					returnModel.setMessage("您的帐号已冻结，请联系客服");
				}

				Map<String, Object> map = userService.getUserDataMap(uid, uid);
				if (map.size() <= 0) {
					returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
					returnModel.setMessage("该账号异常，请重新登录");
				} else {
					// TOSY 手机端需要知道增加两个字段，和作用，以及更新文档
					// RPC admin获取comet的cid和token 放到map中
					if (false == authService.rpcAdminLoginComet(uid, map)) {
						returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
						returnModel.setMessage("AdminFailed");
					} else {
						map.put("token", authService.encryptToken(userAccountModel.getUid(),
								userAccountModel.getPassword(), os, imei));
						returnModel.setCode(CodeContant.OK);
						returnModel.setData(map);

						String mobileVer = req.getParameter("mobileVersion");
						if (org.apache.commons.lang.StringUtils.isEmpty(mobileVer)) {
							mobileVer = "mobile";
						}
						String mobileModel = req.getParameter("mobileModel");
						if (org.apache.commons.lang.StringUtils.isEmpty(mobileModel)) {
							mobileModel = "login";
						}
						String strChannel = req.getParameter("channel") == null ? "" : req.getParameter("channel");

						try {
							AsyncManager.getInstance()
									.execute(new UserLoginDetailAsyncTask(uid, os, mobileVer, mobileModel, strChannel));
						} catch (Exception ex) {
							logger.error("UserLoginDetailAsyncTask>>>", ex);
						}
					}
				}
			}
		} else {
			UserRedisService.getInstance().setCodeUsesTimes(mobile);
			returnModel.setCode(CodeContant.MobileCodeErr);
			returnModel.setMessage("验证码不正确");
		}
		writeJson(resp, returnModel);
	}

	/**
	 * 手机注册，手机登录发送验证码 同一手机号一天只能发5次验证码
	 *
	 * @param req
	 *            参数：mobile手机号
	 * @param resp
	 */
	@RequestMapping(value = "/sendcode", method = RequestMethod.GET)
	public void sendCode(HttpServletRequest req, HttpServletResponse resp) {
		String mobile = req.getParameter("mobile");
		String strType = req.getParameter("type");
		if (org.apache.commons.lang.StringUtils.isBlank(mobile)) {
			returnModel.setCode(CodeContant.ConMobileExcept);
			returnModel.setMessage("手机号不能为空");
			writeJson(resp, returnModel);
			return;
		}
		if (org.apache.commons.lang.StringUtils.isBlank(strType)) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("类型不能为空");
			writeJson(resp, returnModel);
			return;
		}
		mobile = mobile.trim();

		// 注册
		if ("register".equals(strType)) {
			String imageCode = req.getParameter("imageCode");
			if (org.apache.commons.lang.StringUtils.isBlank(imageCode)) {
				returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
				returnModel.setMessage("类型不能为空");
				writeJson(resp, returnModel);
				return;
			}
			String redisImageCode = OtherRedisService.getInstance().getImageVerifyCode(mobile);
			if (!imageCode.toUpperCase().equals(redisImageCode)) {
				returnModel.setCode(CodeContant.ImageCodeErr);
				returnModel.setMessage("图片验证码不正确");
				writeJson(resp, returnModel);
				return;
			}
			OtherRedisService.getInstance().delImageVerifyCode(mobile);
		}

		int uid = UserRedisService.getInstance().getAccountNameAndUid(mobile);
		if (uid > 0) {
			UserAccountModel userAccountByUid = userService.getUserAccountByUid(uid, false);
			if (userAccountByUid != null && Integer.valueOf(userAccountByUid.getStatus()) == 0) {
				returnModel.setCode(CodeContant.ConAccountFroze);
				returnModel.setMessage("您的账号被封了，请联系客服");
				writeJson(resp, returnModel);
				return;
			}
		}
		int ires = OtherRedisService.getInstance().getSendCodeTimes(mobile);
		if (ires > 5) {
			returnModel.setCode(CodeContant.MobileTimes);
			returnModel.setMessage("该手机号码验证码发送超过5次啦");
			writeJson(resp, returnModel);
			return;
		}
		// 绑定
		if ("banding".equalsIgnoreCase(strType)) {
			String bings = RedisCommService.getInstance().get(RedisContant.RedisNameUser,
					RedisContant.KeyMobileBing + mobile);
			if (bings != null && "3".equals(bings)) {
				returnModel.setCode(CodeContant.mobileBindErr);
				returnModel.setMessage("该手机已经多次绑定账号了，请使用其他手机号");
				writeJson(resp, returnModel);
				return;
			}
		}
		// 重置密码
		if ("forget".equalsIgnoreCase(strType)) {
			if (uid <= 0) {
				returnModel.setCode(CodeContant.MobileSendCode);
				returnModel.setMessage("该手机号码未注册！");
				writeJson(resp, returnModel);
				return;
			}
		}
		int code = new Random().nextInt(9999) % (9999 - 1000 + 1) + 1000;
		boolean flag = userService.sendMobileCode(mobile, String.valueOf(code));
		if (!flag) {
			returnModel.setCode(CodeContant.MobileSendCode);
			returnModel.setMessage("验证码发送失败");
			writeJson(resp, returnModel);
			return;
		}
		long iseconds = DateUtils.getDayBegin() + 86400;
		OtherRedisService.getInstance().setSendCodeTimes(mobile, iseconds);
		long takeEffectTime = System.currentTimeMillis() / 1000;
		OtherRedisService.getInstance().setSendCode(mobile, String.valueOf(code), 300);

		// 记录短信发送
		verifyCodeService.insertVerifyCode(mobile, strType, code, takeEffectTime, takeEffectTime + 300);

		writeJson(resp, returnModel);
	}

	/**
	 * 手机重置密码
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/resetpword", method = RequestMethod.GET)
	public void resetPword(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "mobile", "code", "pword")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "mobile")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			writeJson(resp, returnModel);
			return;
		}
		String mobile = req.getParameter("mobile");
		String pword = req.getParameter("pword").toUpperCase();
		String code = req.getParameter("code");

		int times = UserRedisService.getInstance().getCodeUsesTimes(mobile);
		if (times > 5) {
			returnModel.setCode(CodeContant.ConCodeTimes);
			returnModel.setMessage("验证码错误输入超过5次，请10分钟后再试");
			writeJson(resp, returnModel);
			return;
		}
		byte os = Byte.parseByte(req.getParameter("os"));
		String imei = req.getParameter("imei");

		int uid = UserRedisService.getInstance().getMobileAndUid(mobile);
		if (uid <= 0) {
			returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
			returnModel.setMessage("该账号异常，请重新登录");
			writeJson(resp, returnModel);
			return;
		}
		if (!OtherRedisService.getInstance().getSendCode(mobile, code)) {
			UserRedisService.getInstance().setCodeUsesTimes(mobile);
			returnModel.setCode(CodeContant.MobileCodeErr);
			returnModel.setMessage("验证码不正确");
			writeJson(resp, returnModel);
			return;
		}

		int ires = userService.updUserPwordByUid(uid, pword);
		if (ires == 0) {
			returnModel.setCode(CodeContant.MobileCodeErr);
			returnModel.setMessage("验证码不正确");
			writeJson(resp, returnModel);
			return;
		}

		UserAccountModel userAccountModel = userService.getUserAccountByUid(uid, false);
		Map<String, Object> map = userService.getUserDataMap(uid, uid);
		if (map.size() <= 0) {
			returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
			returnModel.setMessage("该账号异常，请重新登录");
			writeJson(resp, returnModel);
			return;
		}

		// TOSY 手机端需要知道增加两个字段，和作用，以及更新文档
		// RPC admin获取comet的cid和token 放到map中
		if (false == authService.rpcAdminLoginComet(uid, map)) {
			returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
			returnModel.setMessage("AdminFailed");
			writeJson(resp, returnModel);
			return;
		}
		map.put("token", authService.encryptToken(userAccountModel.getUid(), userAccountModel.getPassword(), os, imei));
		returnModel.setCode(CodeContant.OK);
		returnModel.setData(map);
		writeJson(resp, returnModel);
	}

	/**
	 * 获取rpc token
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/rpctoken", method = RequestMethod.GET)
	public void getRPCToken(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}

		Map<String, Object> map = new HashMap<String, Object>();

		if (!authService.rpcAdminLoginComet(uid, map)) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("重连失败");
		}

		returnModel.setData(map);
		writeJson(resp, returnModel);
	}

	/**
	 * token登录（非pc端）
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/tokenlogin", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel tokenLogin(HttpServletRequest req, HttpServletResponse resp) {
		// 统计
		logger.info("DEBUG~~tokenLogin~~~SendPigAnalysis~~~");
		Statistics.SendPigAnalysis(-1, req.getParameter("os"), req);

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "mobileVersion", "mobileModel")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			return returnModel;
		}

		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		int os = Integer.valueOf(req.getParameter("os"));
		String mobileVer = req.getParameter("mobileVersion");
		String mobileModel = req.getParameter("mobileModel");
		String strChannel = req.getParameter("channel");

		UserAccountModel userAccount = userService.getUserAccountByUid(uid, false);

		if (userAccount != null && userAccount.getAccountname().contains("weixin")
				&& org.apache.commons.lang.StringUtils.isEmpty(userAccount.getUnionId())) {
			returnModel.setCode(CodeContant.CONTOKENTIMEOUT);
			returnModel.setMessage("TOKEN超时");
			return returnModel;
		}
		try {
			// 异步调用处理用户等级问题
			AsyncManager.getInstance()
					.execute(new UserLoginDetailAsyncTask(uid, os, mobileVer, mobileModel, strChannel));
		} catch (Exception ex) {
			logger.error("tokenLogin>>>", ex);
		}
		// 统计
		logger.info("DEBUG~~tokenLogin~~~SendPigAnalysis~~~ uid " + uid);
		Statistics.SendPigAnalysis(uid, String.valueOf(os), req);

		return returnModel;
	}

	/**
	 * token登录（pc端）
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/tokenloginpc", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel tokenLoginPc(HttpServletRequest req, HttpServletResponse resp) {
		// 统计
		logger.info("DEBUG~~tokenLogin~~~SendPigAnalysis~~~");
		Statistics.SendPigAnalysis(-1, req.getParameter("os"), req);

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			return returnModel;
		}
		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		int os = Integer.valueOf(req.getParameter("os"));
		String mobileVer = "pc";
		String mobileModel = "login";
		String strChannel = "pc";

		try {
			// 异步调用处理用户等级问题
			AsyncManager.getInstance()
					.execute(new UserLoginDetailAsyncTask(uid, os, mobileVer, mobileModel, strChannel));
		} catch (Exception ex) {
			logger.error("UserLoginDetailAsyncTask>>>", ex);
		}

		// 统计
		logger.info("DEBUG~~tokenLogin~~~SendPigAnalysis~~~ uid " + uid);
		Statistics.SendPigAnalysis(uid, String.valueOf(os), req);

		Map<String, Object> map = userService.getUserDataMap(uid, uid);

		UserAccountModel userAccountModel = userService.getUserAccountByUid(uid, false);

		if (map.size() <= 0) {
			returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
			returnModel.setMessage("该账号异常，请重新登录");
		} else {
			// TOSY 手机端需要知道增加两个字段，和作用，以及更新文档
			// RPC admin获取comet的cid和token 放到map中
			if (false == authService.rpcAdminLoginComet(uid, map)) {
				returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
				returnModel.setMessage("AdminFailed");
			} else {
				map.put("token", authService.encryptToken(userAccountModel.getUid(), userAccountModel.getPassword(),
						(byte) os, req.getParameter("imei")));
				returnModel.setCode(CodeContant.OK);
				returnModel.setData(map);

				try {
					AsyncManager.getInstance()
							.execute(new UserLoginDetailAsyncTask(uid, os, mobileVer, mobileModel, strChannel));
				} catch (Exception ex) {
					logger.error("UserLoginDetailAsyncTask>>>", ex);
				}
			}
		}
		return returnModel;
	}

	/**
	 * 记录用户登录信息
	 */
	public class UserLoginDetailAsyncTask implements IAsyncTask {
		int uid;
		int os;
		String mobileVer;
		String mobileModel;
		String channel;

		public UserLoginDetailAsyncTask(int uid, int os, String mobileVer, String mobileModel, String channel) {
			this.uid = uid;
			this.os = os;
			this.mobileVer = mobileVer;
			this.mobileModel = mobileModel;
			this.channel = channel;
		}

		@Override
		public void runAsync() {
			try {
				System.out.println("+++++++++++++11");
				String uidStringfy = String.valueOf(uid);
				// 是否初始化每日任务
				String today = DateUtils.dateToString(null, "yyyyMMdd");
				Boolean hasLoginInThisDay = UserRedisService.getInstance().hasLoginInThisDay(uid, today);
				if (!hasLoginInThisDay) {
					TaskService.getInstance().initTaskAcceptList(String.valueOf(uid), TaskFor.Daily);
				}
				// 每日登陆奖励相关
				loginReward(uid);

				System.out.println("+++++++++++++22");
				// 检查是否派发过新手任务
				boolean hasAcceptedTaskFlagForNewbie = OtherRedisService.getInstance()
						.hasAcceptedTaskFlagForNewbie(uidStringfy);
				if (!hasAcceptedTaskFlagForNewbie) {
					TaskService.getInstance().initTaskAcceptList(uidStringfy, TaskFor.Newbie);
				} else {
					// 检查动态新增加的新任务 排除accepted|finished|commited桶中的任务之后就是需要动态更新的
					TaskService.getInstance().reDispatchTasks(uidStringfy, TaskFor.Newbie);
				}

				// 检查老账号是否绑定过手机
				// final int bindMobileTaskId = 1;
				// boolean hasSomeTaskForInAccepetedBucket = TaskService.getInstance().hasSomeTaskFor(uidStringfy,
				// TaskFor.Newbie,
				// TaskState.Accepted, bindMobileTaskId);
				// if (hasSomeTaskForInAccepetedBucket) {
				// TaskService.getInstance().taskProcess(uid, TaskConfigLib.BoundPhone, 1);
				// }

				System.out.println("+++++++++++++33");
				authService.insertLoginDetail(uid, os, "", mobileModel, channel, mobileVer, 2,
						System.currentTimeMillis() / 1000);
				System.out.println("+++++++++++++44");
			} catch (Exception ex) {
				logger.error("UserLoginDetailAsyncTask>>>", ex);
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
			return "UserLoginDetailAsyncTask";
		}
	}

	@RequestMapping(value = "/weixin/token", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel getInviteWeiXinToken(HttpServletRequest req, final HttpServletResponse resp)
			throws JSONException {
		String uid = req.getParameter("uid");
		String code = req.getParameter("code");
		if (org.apache.commons.lang.StringUtils.isEmpty(uid)) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(Integer.parseInt(uid), false);
		if (sendUserAssetModel == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
			return returnModel;
		}
		if (code != null && !"authdeny".equals(code)) {
			com.alibaba.fastjson.JSONObject jsonObject = SnsApi.getUserWXTokenForH5(code);
			if (jsonObject.get("errcode") != null && jsonObject.get("errcode").equals("40001")) {
				logger.warn(String.format("getUserWXToken>>state:%s,code:%s", jsonObject.toString(), code));
				returnModel.setCode(CodeContant.OpenIdGetFailed);
				returnModel.setMessage("获取AccessToken信息失败");
				return returnModel;
			}
			if (jsonObject.getString("openid") == null) {
				logger.warn(String.format("weixinWebCall>>openId is null:%s", jsonObject.toString()));
				returnModel.setCode(CodeContant.UnValidOpenId);
				returnModel.setMessage("无效第三方标识");
				return returnModel;
			}

			String openId = jsonObject.getString("openid");
			String access_token = jsonObject.getString("access_token");

			com.alibaba.fastjson.JSONObject snsUserInfo = SnsApi.getUserInfoByToken(access_token, openId);
			if (snsUserInfo == null) {
				logger.error(String.format("getUserInfoByToken filed access_token:%s,openId:%s", access_token, openId));
				returnModel.setCode(CodeContant.SNSUserInfoGetFailed);
				returnModel.setMessage("获取第三方信息失败");
				return returnModel;
			} else {
				String unionid = snsUserInfo.getString("unionid");
				UserAccountModel userAccountModel = userService.getUserAccountByUnionId(unionid, false);

				String inviteUid = "";
				if (userAccountModel != null) {
					inviteUid = RedisCommService.getInstance().hget(RedisContant.RedisNameUser,
							RedisContant.inviteWxAuthActivate, userAccountModel.getUid().toString());
				}
				UserBaseInfoModel userInfoModel = null;
				if (org.apache.commons.lang.StringUtils.isNotEmpty(inviteUid)) {
					userInfoModel = userService.getUserbaseInfoByUid(Integer.parseInt(inviteUid), false);
				} else {
					userInfoModel = userService.getUserbaseInfoByUid(Integer.parseInt(uid), false);
				}
				if (userInfoModel != null) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("nickname", userInfoModel.getNickname());
					map.put("headimage", userInfoModel.getHeadimage());
					returnModel.setData(map);
				} else {
					returnModel.setCode(CodeContant.USERASSETEXITS);
					returnModel.setMessage("用户不存在");
					return returnModel;
				}

				if (userAccountModel == null) {
					RedisCommService.getInstance().hset(RedisContant.RedisNameUser, RedisContant.inviteWxAuth, unionid,
							uid, 0);
				}
				return returnModel;
			}
		} else {
			logger.warn(String.format("weixinWebCall>>code:%s client code is unvalid", code));
			returnModel.setCode(CodeContant.UnValidCode);
			returnModel.setMessage("无效凭证");
			writeJson(resp, returnModel);
			return returnModel;
		}
	}

	@RequestMapping(value = "/imageVerifyCode", method = RequestMethod.GET)
	public void imageVerifyCode(HttpServletRequest request, final HttpServletResponse response) throws JSONException {
		ServletOutputStream outputStream = null;
		try {
			String mobile = request.getParameter("mobile");
			if (org.apache.commons.lang.StringUtils.isBlank(mobile)) {
				return;
			}
			int width = 100;
			int height = 40;
			int fontSize = 20;
			int piffx = 2;

			BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			Graphics2D g = img.createGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, width, height);
			g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
			g.setFont(new Font("微软雅黑", Font.BOLD, fontSize));

			String imageVerifyCode = RandomUtil.getRandom(4);
			int seconds = (int) (DateUtils.getDayBegin() + 60*10);

			OtherRedisService.getInstance().setImageVerifyCode(mobile, imageVerifyCode.toUpperCase(), seconds);

			char[] chars = imageVerifyCode.toCharArray();
			Random random = new Random();

			for (int i = 0; i < chars.length; i++) {
				Color c = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
				g.setColor(c);
				int x = width / chars.length * i + random.nextInt(10) + piffx;
				int y = random.nextInt(height - fontSize) + fontSize;
				g.drawString(String.valueOf(chars[i]), x, y);
			}

			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			for (int i = 0; i < 10; i++) {
				Color c = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
				g.setColor(c);
				int x1 = random.nextInt(width - piffx);
				int y1 = random.nextInt(height - piffx);
				int x2 = random.nextInt(width - piffx);
				int y2 = random.nextInt(height - piffx);
				g.drawLine(x1, y1, x2, y2);
			}
			g.dispose();
			img.flush();
			outputStream = response.getOutputStream();
			response.setContentType("image/jpeg");
			ImageIO.write(img, "jpg", outputStream);
			outputStream.flush();
		} catch (Exception e) {
			logger.error("获取图片验证码异常", e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					logger.error("关闭outputStream异常", e);
				}
			}
		}
	}
}