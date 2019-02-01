//package com.mpig.api.controller;
//
//import java.net.InetAddress;
//import java.util.Map;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.log4j.Logger;
//import org.json.JSONException;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.mpig.api.async.AsyncManager;
//import com.mpig.api.async.IAsyncTask;
//import com.mpig.api.dictionary.lib.TaskConfigLib.TaskFor;
//import com.mpig.api.model.ReturnModel;
//import com.mpig.api.model.UserAccountModel;
//import com.mpig.api.model.UserBaseInfoModel;
//import com.mpig.api.redis.service.OtherRedisService;
//import com.mpig.api.redis.service.UserRedisService;
//import com.mpig.api.service.IOrderService;
//import com.mpig.api.service.IRoomService;
//import com.mpig.api.service.ISearchService;
//import com.mpig.api.service.IUserService;
//import com.mpig.api.task.TaskService;
//import com.mpig.api.utils.CodeContant;
//import com.mpig.api.utils.DateUtils;
//import com.mpig.api.utils.ParamHandleUtils;
//
//@Controller
//@Scope("prototype")
//@RequestMapping("/peanut")
//public class PeanutController  extends BaseController{
//
//	@Resource
//	private IUserService userService;
//	@Resource
//	private IOrderService orderService;
//	@Resource
//	private ISearchService searchService;
//	@Resource
//	private IRoomService roomService;
//	
//	private static final Logger logger = Logger.getLogger(PeanutController.class);
//	
//	/**
//	 * 第三方登录
//	 *
//	 * @param req
//	 *            参数：openid第三方唯一标示,accesstoken第三方访问token,channel渠道,source第三方账号类型
//	 * @param resp
//	 */
//	@RequestMapping("/login")
//	@ResponseBody
//	public ReturnModel loginThird(HttpServletRequest req, final HttpServletResponse resp) throws JSONException {
//
//		long lg1 = System.currentTimeMillis();
//		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "openid", "channel","source")) {
//			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
//			returnModel.setMessage("缺少参数或参数为空");
//			return returnModel;
//		}
//		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
//			returnModel.setCode(CodeContant.ConParamTypeIsErr);
//			returnModel.setMessage("参数类型错误");
//			return returnModel;
//		}
//
//		// 校验码验证及token
//		authToken(req, false);
//		if (returnModel.getCode() != CodeContant.OK) {
//			return returnModel;
//		}
//
//		// 用户的唯一标识
//		String openid = req.getParameter("openid");
//		String channel = req.getParameter("channel");
//		final String source = req.getParameter("source");
//		final byte os = Byte.parseByte(req.getParameter("os"));
//		final String imei = req.getParameter("imei");
//
//		// 记录的用户authkey必须是openid和第三方标识
//		final String openidSource = openid + source;
//
//		long lg2 = System.currentTimeMillis();
//		logger.debug("loginThirdTime 参数获取时间 lg2-lg1=" + (lg2 - lg1));
//		
//		UserAccountModel userAccountModel = null;
//		String unionId = "";
//		
//		if (userAccountModel == null) {
//			userAccountModel = userService.getUserAccountByAuthKey(openidSource.toLowerCase(),false);
//		}
//		String nickName = req.getParameter("nickName") == null ? "":req.getParameter("nickName");
//		String headImage = req.getParameter("headImage") == null ? "":req.getParameter("headImage");
//		Boolean sex = "1".equals(req.getParameter("sex")) ? true : false;
//		if (userAccountModel == null) {
//			String pword = "";
//			
//			if (org.apache.commons.lang.StringUtils.isNotEmpty(nickName) || org.apache.commons.lang.StringUtils.isNotEmpty(headImage)) {
//				
//				// 新第三方注册
//				int ilen = openidSource.length();
//				if (ilen > 16) {
//					pword = openidSource.substring(0, 16);
//				} else {
//					pword = openidSource;
//				}
//				
//				long regiterIp;
//				try {
//					regiterIp = com.mpig.api.utils.StringUtils.ipToLong(InetAddress.getLocalHost().getHostAddress());
//					returnModel = authService.register("", pword, regiterIp, nickName, openidSource, source, channel,
//							"", os, imei, headImage, sex, "",unionId);
//
//				} catch (Exception e) {
//					logger.error("<loginThird->Exception>" + e.toString());
//				}
//			}
//			return returnModel;
//		} else {
//			// 已注册，登录
//			if (userAccountModel.getStatus() == 0) {
//				// 冻结用户
//				returnModel.setCode(CodeContant.ConAccountFroze);
//				returnModel.setMessage("您的帐号已冻结，请联系客服");
//			} else {
//				if (userAccountModel.getAccountid() == 0) {
//					authService.updAccountid(userAccountModel.getUid());
//				}else if (org.apache.commons.lang.StringUtils.isEmpty(userAccountModel.getUnionId()) && org.apache.commons.lang.StringUtils.isNotEmpty(unionId)) {
//
//					authService.updUnionIdOfAccount(userAccountModel.getUid(),unionId);
//				}
//				int uid = userAccountModel.getUid();
//				Map<String, Object> map = userService.getUserDataMap(uid, uid);
//
//				if (map.size() <= 0) {
//					returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
//					returnModel.setMessage("该账号异常，请重新登录");
//				} else {
//					// TOSY TODO 手机端需要知道增加两个字段，和作用，以及更新文档
//					// RPC admin获取comet的cid和token 放到map中
//					if (false == authService.rpcAdminLoginComet(uid, map)) {
//						returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
//						returnModel.setMessage("AdminFailed");
//					} else {
//						String headimage = headImage;
//						String nickname = nickName;
//						UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(uid, false);
//						boolean flag = false;
//						if(!userBaseInfoModel.getHeadimage().equals(headimage)){
//							int ires = userService.updUserBaseHeadimg(uid,headimage,headimage);
//							if(ires > 0){
//								flag = true;	
//							}
//						}
//						if(!userBaseInfoModel.getNickname().equals(nickname)){
//							int ires = userService.updUserNickName(uid, nickname);
//							if(ires > 0){
//								searchService.updateUserFieldsAnsyc(String.valueOf(uid), "nickname", nickname);	
//								flag = true;
//							}
//						}
//						if(flag){
//							map = userService.getUserDataMap(uid, uid);
//						}
//						map.put("token", authService.encryptToken(userAccountModel.getUid(),
//								userAccountModel.getPassword(), os, imei));
//						returnModel.setCode(CodeContant.OK);
//						returnModel.setData(map);
//
//						String strChannel = req.getParameter("channel") == null ? "" : req.getParameter("channel");
//
//						try{
//							AsyncManager.getInstance().execute(new UserLoginDetailAsyncTask(uid, os, "", "", strChannel));
//						}catch (Exception ex){
//							logger.error("LiveUserLoginDetailAsyncTasking>>>",ex);
//						}
//					}
//				}
//			}
//			return returnModel;
//		}
//	}
//	/**
//	 * 记录用户登录信息
//	 */
//	public class UserLoginDetailAsyncTask implements IAsyncTask {
//		int uid;
//		int os;
//		String mobileVer;
//		String mobileModel;
//		String channel;
//
//		public UserLoginDetailAsyncTask(int uid, int os, String mobileVer, String mobileModel, String channel) {
//			this.uid = uid;
//			this.os = os;
//			this.mobileVer = mobileVer;
//			this.mobileModel = mobileModel;
//			this.channel = channel;
//		}
//
//		@Override
//		public void runAsync() {
//			String uidStringfy = String.valueOf(uid);
//			// 是否初始化每日任务
//			String today = DateUtils.dateToString(null, "yyyyMMdd");
//			Boolean hasLoginInThisDay = UserRedisService.getInstance().hasLoginInThisDay(uid, today);
//			if (!hasLoginInThisDay) {
//				TaskService.getInstance().initTaskAcceptList(String.valueOf(uid), TaskFor.Daily);
//			}
//			//每日登陆奖励相关
////			loginReward(uid);
//			
//			// 检查是否派发过新手任务
//			boolean hasAcceptedTaskFlagForNewbie = OtherRedisService.getInstance()
//					.hasAcceptedTaskFlagForNewbie(uidStringfy);
//			if (!hasAcceptedTaskFlagForNewbie) {
//				TaskService.getInstance().initTaskAcceptList(uidStringfy, TaskFor.Newbie);
//			} else {
//				// 检查动态新增加的新任务 排除accepted|finished|commited桶中的任务之后就是需要动态更新的
//				TaskService.getInstance().reDispatchTasks(uidStringfy, TaskFor.Newbie);
//			}
//
//			authService.insertLoginDetail(uid, os, "", mobileModel, channel, mobileVer, 2, System.currentTimeMillis() / 1000);
//		}
//
//		@Override
//		public void afterOk() {
//
//		}
//
//		@Override
//		public void afterError(Exception e) {
//
//		}
//
//		@Override
//		public String getName() {
//			return "UserLoginDetailAsyncTask";
//		}
//	}
//	
//	@RequestMapping(value = "/entertainment/exchange", method = RequestMethod.GET)
//	@ResponseBody
//	public ReturnModel exchangeZhutou(HttpServletRequest req, HttpServletResponse resp) {
//		if (ParamHandleUtils.isBlank(req, "openid","zhutou","reqtime","channel","sign")) {
//			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
//			returnModel.setMessage("缺少参数或参数为空");
//			return returnModel;
//		}
//		if (!ParamHandleUtils.isInt(req, "zhutou","reqtime")) {
//			returnModel.setCode(CodeContant.ConParamTypeIsErr);
//			returnModel.setMessage("参数类型错误");
//			return returnModel;
//		}
//		String openid = req.getParameter("openid");
//		String channel = req.getParameter("channel");
//		final String source = "HSYL";
//		final String zhutou = req.getParameter("zhutou");
//		if(Integer.parseInt(zhutou) <= 0){
//			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
//			returnModel.setMessage("金额错误");
//			return returnModel;
//		}
//		
//		final String openidSource = openid + source;
//		UserAccountModel userAccountModel = userService.getUserAccountByAuthKey(openidSource.toLowerCase(),false);
//		if(userAccountModel != null){
//			int uid = userAccountModel.getUid();
//			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(uid, false);
//			int os = 1;
//			String orderNo = orderService.CreateOrderNo(os);
//			int creatAt = (int) (System.currentTimeMillis() / 1000);
//			int registtime = userBaseInfoModel.getRegisttime();
//			int ires = orderService.CreateOrderApp(orderNo, uid, uid, Float.parseFloat(zhutou), (int) (Float.parseFloat(zhutou)), creatAt, creatAt, os, source, 2, "", "兑换",userBaseInfoModel.getRegistchannel(),channel,registtime,returnModel);
//			
//			if(ires == 0){
//				returnModel.setCode(CodeContant.PayOrder);
//				returnModel.setMessage("订单生成失败");
//				return returnModel;
//			}
//		}else{
//			returnModel.setCode(CodeContant.USERASSETEXITS);
//			returnModel.setMessage("用户不存在");
//		}
//		return returnModel;
//	}
//	/**
//	 * 获取直播间的用户列表
//	 *
//	 * @param req
//	 * @param resp
//	 */
//	@RequestMapping(value = "/audience", method = RequestMethod.GET)
//	@ResponseBody
//	public ReturnModel getUserListOfLive(HttpServletRequest req, HttpServletResponse resp) {
//		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "artistid", "page")) {
//			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
//			returnModel.setMessage("缺少参数或参数为空");
//			return returnModel;
//		}
//		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "artistid", "page")) {
//			returnModel.setCode(CodeContant.ConParamTypeIsErr);
//			returnModel.setMessage("参数类型错误");
//			return returnModel;
//		}
//		authToken(req, false);
//		if (returnModel.getCode() != 200) {
//			return returnModel;
//		}
//		Integer dstUid = Integer.valueOf(req.getParameter("artistid"));
//		Integer page = Integer.valueOf(req.getParameter("page"));
//		roomService.userlistOfLive(dstUid, dstUid, page, returnModel);
//		return returnModel;
//	}
//}
