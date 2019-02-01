//package com.mpig.api.controller;
//
//import java.net.InetAddress;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.lang.StringUtils;
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
//import com.mpig.api.dictionary.lib.PayConfigLib;
//import com.mpig.api.dictionary.lib.TaskConfigLib.TaskFor;
//import com.mpig.api.iapppay.HttpUtils;
//import com.mpig.api.model.ReturnModel;
//import com.mpig.api.model.UserAccountModel;
//import com.mpig.api.model.UserBaseInfoModel;
//import com.mpig.api.redis.service.OtherRedisService;
//import com.mpig.api.redis.service.UserRedisService;
//import com.mpig.api.service.IOrderService;
//import com.mpig.api.service.ISearchService;
//import com.mpig.api.service.ISlWifiService;
//import com.mpig.api.service.IUserService;
//import com.mpig.api.task.TaskService;
//import com.mpig.api.utils.AesUtil;
//import com.mpig.api.utils.CodeContant;
//import com.mpig.api.utils.DateUtils;
//import com.mpig.api.utils.MD5Encrypt;
//import com.mpig.api.utils.ParamHandleUtils;
//
//
//@Controller
//@Scope("prototype")
//@RequestMapping("/16wifi")
//public class SlWiFiController extends BaseController{
//	
//	@Resource
//	private ISlWifiService slWifiService;
//	@Resource
//	private IUserService userService;
//	@Resource
//	private IOrderService orderService;
//	@Resource
//	private ISearchService searchService;
//	
//	private static final Logger logger = Logger.getLogger(SlWiFiController.class);
//	
//	private static String key = "ee2b4449db309d46d861aa77fe79f76c";
//	private static String AES_Key = "_e6wifi_pay_key_";
//	/**
//	 * 第三方登录
//	 *
//	 * @param req
//	 *            参数：openid第三方唯一标示,accesstoken第三方访问token,channel渠道,source第三方账号类型
//	 * @param resp
//	 */
//	@RequestMapping(value = "/login", method = RequestMethod.GET)
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
//							logger.error("UserLoginDetailAsyncTask>>>",ex);
//						}
//					}
//				}
//			}
//			return returnModel;
//		}
//	}
//	
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
//	@RequestMapping(value = "/livelist", method = RequestMethod.GET)
//	@ResponseBody
//	public ReturnModel getLiveList(HttpServletRequest req, HttpServletResponse resp) {
//		
//		if (ParamHandleUtils.isBlank(req, "ncode", "imei", "reqtime")) {
//			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
//			returnModel.setMessage("缺少参数或参数为空");
//			return returnModel;
//		}
//		if (!ParamHandleUtils.isInt(req, "reqtime")) {
//			returnModel.setCode(CodeContant.ConParamTypeIsErr);
//			returnModel.setMessage("参数类型错误");
//			return returnModel;
//		}
//
//		authToken(req, false);
//		if (returnModel.getCode() != 200) {
//			// 验证失败
//			return returnModel;
//		}
//		String token = req.getParameter("token");// 充值操作对象
//		Integer srcUid = 0;
//		if (StringUtils.isEmpty(token) || "null".equalsIgnoreCase(token) || "(null)".equalsIgnoreCase(token)) {
//			srcUid = null;
//		}else {
//			srcUid = authService.decryptToken(token, returnModel);
//			if (srcUid <= 0) {
//				return returnModel;
//			}
//		}
//		Integer page = 0;// Integer.valueOf(req.getParameter("page"));
//		slWifiService.getLivingList(page, returnModel);
//		return returnModel;
//	}
//	
//	@RequestMapping(value="/order")
//	@ResponseBody
//	public ReturnModel order(HttpServletRequest req, HttpServletResponse resp){
//		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","token","payType","totalFee")) {
//			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
//			returnModel.setMessage("缺少参数或参数为空");
//			return returnModel;
//		}
//		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
//			returnModel.setCode(CodeContant.ConParamTypeIsErr);
//			returnModel.setMessage("参数类型错误");
//			return returnModel;
//		}
//		authToken(req, true);
//		if (returnModel.getCode() != CodeContant.OK) {
//			return returnModel;
//		}
//		String token = req.getParameter("token");
//		int uid = authService.decryptToken(token, returnModel);
//		if (uid <= 0) {
//			return returnModel;
//		}
//		String totalFee = req.getParameter("totalFee");
//		String payType = req.getParameter("payType");
//		String channel = req.getParameter("channel");
//		String userId = req.getParameter("userId");
//		String os = req.getParameter("os");
//		String payMethod=""; //支付方式
//		String subject=""; //支付宝标题
//		String body = ""; //微信支付标题或者是支付宝支付的描述
//		String res = ""; //来源
//		String des = "充值"+totalFee+"元猪头";
//		if(payType.equals("WX")){
//			body = des; 
//			payMethod = "APP";
//		}else if(payType.equals("ALIPAY")){
//			body = des; //微信支付标题或者是支付宝支付的描述
//			subject = des; //支付宝标题
//			payMethod = "MOBILE";
//		}else{
//			returnModel.setCode(CodeContant.ConParamTypeIsErr);
//			returnModel.setMessage("参数类型错误");
//			return returnModel;
//		}
//		if(os.equals("2")){
//			res = "IOS";
//		}else{
//			res = "ANDROID";
//		}
//		String bussId = "147989757374424744"; //TODO 上线修改
//		String channelCode = "147989757374424744"; //TODO 上线修改
//		String bussType = "live";
//		String productId = "1";
//		String orderCode = ""; //订单号
//		String sign = "";
//		String callbackUrl =  PayConfigLib.getConfig().getPay_notifyUrl()+"/16wifi/notify";
//		try {
//			String orderNo = orderService.CreateOrderNo(Integer.parseInt(os));
//			int creatAt = (int) (System.currentTimeMillis() / 1000);
//			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(uid, false);
//			int registtime = userBaseInfoModel.getRegisttime();
//			int ires = orderService.CreateOrder(callbackUrl, orderNo, uid, Integer.valueOf(uid), Float.parseFloat(totalFee), creatAt, 0, 4, "16wifi"+payType,
//					false, "", des, userBaseInfoModel.getRegistchannel(),channel,registtime,returnModel);
//			if(ires == 1){
//				Map<String,Object> map = new HashMap<String, Object>();
//				orderCode = orderNo;
//				map.put("totalFee", totalFee);
//				map.put("payType", payType);
//				map.put("payMethod", payMethod);
//				map.put("subject", subject);
//				map.put("body", body);
//				map.put("res", res);
//				map.put("bussId", bussId);
//				map.put("channelCode", channelCode);
//				map.put("bussType", bussType);
//				map.put("productId", productId);
//				map.put("orderCode", orderCode);
//				map.put("callbackUrl", callbackUrl);
//				map.put("userId", userId);
//				
//				sign = getSign(map);
//				map.put("sign", sign);
//				
//				String data = ascSort(map);
//				String respData = HttpUtils.sentPost("http://pay.16wifi.com:50391/e6wifi/pay/unifiedpay.html", AesUtil.AES_Encrypt(AES_Key, data.substring(0, data.length()-1)),"UTF-8"); // 请求验证服务端
////				String responseStr = AesUtil.AES_Decrypt(AES_Key, respData);
////				String[] split = responseStr.split("&");
////				Map<String,Object> Map = new HashMap<String,Object>();
////				for(String a: split){
////					String[] split2 = a.split("=");
////					Map.put(split2[0], split2[1]);
////				}
////				if(!Map.get("code").toString().endsWith("00")){
////					logger.error("下单失败 ： "+responseStr);
////				}
//				returnModel.setData(respData);
//			}else{
//				returnModel.setCode(CodeContant.PayOrder);
//				returnModel.setMessage("订单生成失败");
//			}
//		} catch (Exception e) {
//			logger.error(e);
//		}
//
//		return returnModel;
//	}
//	
//	@RequestMapping(value="/notify")
//	public void notify(HttpServletRequest req, HttpServletResponse resp){
//		Map<String,Object>  returnMap = new HashMap<>();
//		String verdorCode = req.getParameter("verdorCode");
//		String cashAmount = req.getParameter("cashAmount");
//		String orderId = req.getParameter("orderId");
//		String status = req.getParameter("status");
//		String payCode = req.getParameter("payCode");
//		String sign = req.getParameter("sign");
//		String uid = req.getParameter("uid");
//		
//		System.out.println(" verdorCode:"+verdorCode+" cashAmount:"+cashAmount +" orderId:"+orderId+" status:"+status+" payCode:"+payCode+" uid"+uid);
//		
//		if (ParamHandleUtils.isBlank(req, "verdorCode", "cashAmount", "orderId","status","payCode")) {
//			returnMap.put("ReturnCode", 400);
//		}
//		Map<String,Object> map = new HashMap<String, Object>();
//		try {
//			map.put("cashAmount", cashAmount);
//			map.put("orderId", orderId);
//			map.put("payCode", payCode);
//			map.put("status", status);
//			map.put("verdorCode", verdorCode);
//			if(StringUtils.isNotEmpty(uid)){
//				map.put("uid", uid);	
//			}
//			String newSign = getSign(map);
//			if(newSign.equals(sign)){
//				if(status.equals("9") || status.equals("11")){
//					double imoney = Double.parseDouble(cashAmount);
//					int zhutou = (int)(imoney * 10);
//				    
//		            int ires = orderService.updPayStatus(orderId, payCode, Double.valueOf(imoney), zhutou, 2, System.currentTimeMillis()/1000);
//		            if(ires==1){
//		            	returnMap.put("ReturnCode", 200);
//		            }else{
//		            	returnMap.put("ReturnCode", 401);
//		            }
//				}else{
//					returnMap.put("ReturnCode", 402);
//				}
//			}else{
//				returnMap.put("ReturnCode", 403);
//			}
//		} catch (Exception e) {
//			logger.error(e);
//			returnMap.put("ReturnCode", 412);
//		}
//		writeJson(resp, returnMap);
//	}
//	
//	public static String getSign(Map<String,Object> map) throws Exception{
//        String result = ascSort(map);
//        result += "key=" + key;
//        result = MD5Encrypt.encrypt(result).toUpperCase();
//        return result;
//    }
//	
//	public static String ascSort(Map<String,Object> map){
//        ArrayList<String> list = new ArrayList<String>();
//        for(Map.Entry<String,Object> entry:map.entrySet()){
//            if(!entry.getValue().equals("")){
//                list.add(entry.getKey() + "=" + entry.getValue() + "&");
//            }
//        }
//        int size = list.size();
//        String [] arrayToSort = list.toArray(new String[size]);
//        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
//        StringBuilder sb = new StringBuilder();
//        for(int i = 0; i < size; i ++) {
//            sb.append(arrayToSort[i]);
//        }
//        String result = sb.toString();
//        return result;
//	}
//	
//	public static void main(String[] args) {
//
//
//		
//		String totalFee = "100";
//		String payType = "WX";
//		String os = "1";
//		String payMethod=""; //支付方式
//		String subject=""; //支付宝标题
//		String body = ""; //微信支付标题或者是支付宝支付的描述
//		String res = ""; //来源
//		String des = "充值"+totalFee+"元猪头";
//		if(payType.equals("WX")){
//			body = des; 
//			payMethod = "APP";
//		}else if(payType.equals("ALIPAY")){
//			body = des; //微信支付标题或者是支付宝支付的描述
//			subject = des; //支付宝标题
//			payMethod = "MOBILE";
//		}
//		if(os.equals("2")){
//			res = "IOS";
//		}else{
//			res = "ANDROID";
//		}
//		String bussId = "147989757374424744"; //TODO 上线修改
//		String channelCode = "147989757374424744"; //TODO 上线修改
//		String bussType = "live";
//		String productId = "1";
//		String orderCode = ""; //订单号
//		String sign = "";
//		String callbackUrl =  "/16wifi/notify";
//		try {
//				Map<String,Object> map = new HashMap<String, Object>();
//				orderCode = "1000";
//				map.put("totalFee", totalFee);
//				map.put("payType", payType);
//				map.put("payMethod", payMethod);
//				map.put("subject", subject);
//				map.put("body", body);
//				map.put("res", res);
//				map.put("bussId", bussId);
//				map.put("channelCode", channelCode);
//				map.put("bussType", bussType);
//				map.put("productId", productId);
//				map.put("orderCode", orderCode);
//				map.put("callbackUrl", callbackUrl);
//				sign = getSign(map);
//				map.put("sign", sign);
//				
//				String data = ascSort(map);
//				String respData = HttpUtils.sentPost("http://pay.16wifi.com:50391/e6wifi/pay/unifiedpay.html", AesUtil.AES_Encrypt(AES_Key, data.substring(0, data.length()-1)),"UTF-8"); // 请求验证服务端
//				String resposeStr = AesUtil.AES_Decrypt(AES_Key, respData);
//				System.out.println(resposeStr);
//				String[] split = resposeStr.split("&");
//				System.out.println(split[1]);
//				Map<String,Object> Map = new HashMap<String,Object>();
//				for(String a: split){
//					String[] split2 = a.split("=");
//					Map.put(split2[0], split2[1]);
//				}
//				System.out.println(Map);
//	}catch (Exception e) {
//		e.printStackTrace();
//	}
//
//	
//	}
//}
