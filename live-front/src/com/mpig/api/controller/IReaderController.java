//package com.mpig.api.controller;
//
//import java.io.UnsupportedEncodingException;
//import java.net.InetAddress;
//import java.net.URLDecoder;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ConcurrentLinkedQueue;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.mpig.api.async.AsyncManager;
//import com.mpig.api.async.IAsyncTask;
//import com.mpig.api.dictionary.lib.TaskConfigLib.TaskFor;
//import com.alibaba.fastjson.JSONObject;
//import com.mpig.api.model.LiveMicTimeModel;
//import com.mpig.api.model.RecordItem;
//import com.mpig.api.model.ReturnModel;
//import com.mpig.api.model.UserAccountModel;
//import com.mpig.api.model.UserBaseInfoModel;
//import com.mpig.api.redis.service.OtherRedisService;
//import com.mpig.api.redis.service.UserRedisService;
//import com.mpig.api.service.ISearchService;
//import com.mpig.api.service.ILiveService;
//import com.mpig.api.service.IUserService;
//import com.mpig.api.task.TaskService;
//import com.mpig.api.utils.CodeContant;
//import com.mpig.api.utils.DateUtils;
//import com.mpig.api.utils.DesUtil;
//import com.mpig.api.utils.ParamHandleUtils;
//
//@Controller
//@Scope("prototype")
//@RequestMapping("/ireader")
//public class IReaderController extends BaseController {
//	private static final Logger logger = Logger.getLogger(IReaderController.class);
//
//	@Resource
//	private IUserService userService;
//	@Resource
//	private ISearchService searchService;
//	@Resource
//	private ILiveService liveService;
//
//
//	final static int[] archors = {10708934};	//10708934
//	
//	volatile private static long nStartTime = 0;
//	final private static long nDuration = 60*1000;
//
//	static private ConcurrentLinkedQueue<ArchorsIReader> data = new ConcurrentLinkedQueue<ArchorsIReader>();
//	
//	static public class ArchorsIReader{
//		int uid;
//		String cover;
//		String title;
//		boolean living;
//		
//		public ArchorsIReader(int nUid,String strCover,boolean bLiving,String strTitle){
//			uid = nUid;
//			cover = strCover;
//			living = bLiving;
//			title = strTitle;
//		}
//		
//		public String getTitle() {
//			return title;
//		}
//		public void setTitle(String title) {
//			this.title = title;
//		}
//		public int getUid() {
//			return uid;
//		}
//		public void setUid(int uid) {
//			this.uid = uid;
//		}
//		public String getCover() {
//			return cover;
//		}
//		public void setCover(String cover) {
//			this.cover = cover;
//		}
//		public boolean isLiving() {
//			return living;
//		}
//		public void setLiving(boolean living) {
//			this.living = living;
//		}
//	}
//	
//	@RequestMapping(value = "/getireaderlist", method = RequestMethod.GET)
//	@ResponseBody
//	public ReturnModel getireaderlist(HttpServletRequest req) {
//		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token")) {
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
//		authToken(req, false);
//		if (returnModel.getCode() != CodeContant.OK) {
//			return returnModel;
//		}
//		
//		long tm = System.currentTimeMillis();
//		if(tm - nStartTime > nDuration){ 
//			nStartTime = tm;
//			
//			//update
//			data.clear();
//			for (int i : archors) {
//				UserBaseInfoModel ui = userService.getUserbaseInfoByUid(i, false);
//				if(null != ui){
//					ArchorsIReader archor = new ArchorsIReader(i,ui.getPcimg2(),ui.getLiveStatus(),null);
//					if(ui.getLiveStatus()){
//						//当前
//						LiveMicTimeModel linfo =  liveService.getLiveMicInfoByUid(i,true);
//						archor.setTitle(linfo.getSlogan());
//					}else{
//						//上一次
//						LiveMicTimeModel linfo =  liveService.getliveMicInfoLivedByUid(i,true);
//						if(null != linfo){
//							archor.setTitle(linfo.getSlogan());
//						}
//						
////						Map<String,String> recs = userService.getRecordAllByUid(String.valueOf(i), null);
////						String maxkey = "";
////						for (String key : recs.keySet()) {
////							if(key.compareToIgnoreCase(maxkey) > 0){
////								maxkey = key;
////							}
////						}
////						if(maxkey.length() > 0){
////							String jsonRecitem = recs.get(maxkey);
////							RecordItem rec = JSONObject.parseObject(jsonRecitem,RecordItem.class);
////							archor.setTitle(rec.getTitle());
////						}
//					}
//					data.add(archor);
//				}
//			}
//		}
//		
//		returnModel.setData(data);
//		returnModel.setCode(CodeContant.OK);
//		returnModel.setMessage("成功");
//		return returnModel;
//	}
//	
//	@RequestMapping(value = "/login")
//	@ResponseBody
//	public ReturnModel login(HttpServletRequest req){
//		long lg1 = System.currentTimeMillis();
//		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "channel","source","info")) {
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
//		String info = req.getParameter("info");
//		logger.error(info);
//		Map<String,Object> Map = new HashMap<String,Object>();
//		try {
//			String encryptStr = DesUtil.decrypt(info);
//			String[] split = encryptStr.split("&");
//			logger.error(" info 解密字符串 ： "+split);
//			for(String a: split){
//				String[] split2 = a.split("=");
//				Map.put(split2[0], split2[1]);
//			}
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		if(Map ==null || Map.size()==0){
//			returnModel.setCode(CodeContant.ConParamTypeIsErr);
//			returnModel.setMessage("参数类型错误");
//			return returnModel;
//		}
//		// 校验码验证及token
//		authToken(req, false);
//		if (returnModel.getCode() != CodeContant.OK) {
//			return returnModel;
//		}
//
//		// 用户的唯一标识
//		String openid = "";
//		if(Map.get("uid") != null){
//			openid = Map.get("uid").toString();
//		}else{
//			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
//			returnModel.setMessage("info缺少参数");
//			return returnModel;
//		}
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
//		String nickName="";
//		try {
//			nickName = Map.get("nick") == null ? "" : URLDecoder.decode(Map.get("nick").toString(),"UTF-8");
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//		}
//		String headImage = "";
//		try {
//			headImage = Map.get("avatar") == null ? "": URLDecoder.decode(Map.get("avatar").toString(),"UTF-8");
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//		}
//		Boolean sex = "1".equals(Map.get("sex")) ? true : false;
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
//
//					}
//				}
//			}
//			return returnModel;
//		}
//	
//	}
//	
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
//}
