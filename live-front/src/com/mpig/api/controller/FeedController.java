package com.mpig.api.controller;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mpig.api.async.AsyncManager;
import com.mpig.api.async.IAsyncTask;
import com.mpig.api.model.ConfigGiftModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.UserAssetModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.model.UserFeedModel;
import com.mpig.api.model.UserFeedReplyModel;
import com.mpig.api.model.UserVipInfoModel;
import com.mpig.api.model.ValueaddPrivilegeModel;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.redis.service.RedisCommService;
import com.mpig.api.redis.service.RelationRedisService;
import com.mpig.api.redis.service.UserRedisService;
import com.mpig.api.service.IBillService;
import com.mpig.api.service.IConfigService;
import com.mpig.api.service.IFeedService;
import com.mpig.api.service.IOrderService;
import com.mpig.api.service.IRoomService;
import com.mpig.api.service.IUserGuardInfoService;
import com.mpig.api.service.IUserService;
import com.mpig.api.service.impl.UserServiceImpl;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.Constant;
import com.mpig.api.utils.ParamHandleUtils;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.ValueaddServiceUtil;

@Controller
@Scope("prototype")
@RequestMapping("/feed")
public class FeedController extends BaseController{
	
	private static final Logger logger = Logger.getLogger(FeedController.class);
	
	@Resource
	private IFeedService feedService;
	@Resource
	private IOrderService orderService;
	@Resource
	private IUserService userService;
	@Resource
	private IConfigService configService;
	@Resource
	private IBillService billService;
	@Resource
	private IUserGuardInfoService userGuardInfoService;
	@Resource
	private IRoomService roomService;
	
	@RequestMapping(value = "/publish")
	@ResponseBody
	public ReturnModel publishFeed(HttpServletRequest request, HttpServletResponse response) {
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
		authToken(request, true);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		String token = request.getParameter("token");
		final int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		String content = request.getParameter("content");
		int ilen = com.mpig.api.utils.StringUtils.length(content);
		if (ilen > 350) {
			returnModel.setCode(CodeContant.feed_content_length_err);
			returnModel.setMessage("动态超过长度限制");
			return returnModel;
		}
		int feedId = feedService.addFeed(uid, content);
		if(feedId <= 0){
			returnModel.setCode(CodeContant.CONSYSTEMERR);
			returnModel.setMessage("动态发布失败！");
			return returnModel;
		}
		
		Map<String,Object> dataMap = new HashMap<String, Object>();
		dataMap.put("feedId", feedId);
		returnModel.setData(dataMap);
		return returnModel;
	}
	
	@RequestMapping(value = "/uploadImg")
	@ResponseBody
	public ReturnModel uploadImg(HttpServletRequest request, HttpServletResponse response) {
		if (ParamHandleUtils.isBlank(request, "ncode", "os", "imei", "reqtime", "token", "feedId","imgs")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(request, "os", "reqtime","feedId")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		authToken(request, true);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		String token = request.getParameter("token");
		final int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		String feedId = request.getParameter("feedId");
		String imgs = request.getParameter("imgs");
		imgs = URLDecoder.decode(imgs);
		String[] split = imgs.split(";");
		String imgUrls = "";
		for(String str: split){
			imgUrls += Constant.qn_feed_bucket_domain+"/"+str+";";
		}
		imgUrls = imgUrls.substring(0, imgUrls.length()-1);
		int addFeedImgs = feedService.addFeedImgs(Integer.parseInt(feedId), imgUrls);
		if(addFeedImgs <= 0){
			returnModel.setCode(CodeContant.feed_upload_img_err);
			returnModel.setMessage("上传图片失败！");
			return returnModel;
		}
		return returnModel;
	}
	
	@RequestMapping(value = "/laud")
	@ResponseBody
	public ReturnModel feedLaud(HttpServletRequest request, HttpServletResponse response) {
		if (ParamHandleUtils.isBlank(request, "ncode", "os", "imei", "reqtime", "token", "feedId","type")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(request, "os", "reqtime","feedId")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		authToken(request, true);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		String token = request.getParameter("token");
		final int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		String feedId = request.getParameter("feedId");
		String type = request.getParameter("type");
		if(type.equals("1")){
			String hget = RedisCommService.getInstance().hget(RedisContant.RedisNameFeed, RedisContant.FeedLaud+feedId, uid+"");
			if(hget!=null){
				returnModel.setCode(CodeContant.feed_lauds_err);
				returnModel.setMessage("请勿重复点赞！");
				return returnModel;
			}
		}
		int feedlaud = feedService.addFeedLaud(Integer.parseInt(feedId), uid, Integer.parseInt(type));
		if(feedlaud <= 0){
			returnModel.setCode(CodeContant.feed_laud_err);
			returnModel.setMessage("点赞失败！");
			return returnModel;
		}
		return returnModel;
	}
	
	/**
	 * 打赏猪头
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/reward")
	@ResponseBody
	public ReturnModel feedReward(HttpServletRequest request, HttpServletResponse response) {
		if(true) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("该功能暂未开通");
			return returnModel;
		}
		if (ParamHandleUtils.isBlank(request, "ncode", "os", "imei", "reqtime", "token", "feedId","zhutou")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(request, "os", "reqtime","feedId","zhutou")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		authToken(request, true);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		String token = request.getParameter("token");
		final int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		
		String feedId = request.getParameter("feedId");
		int zhutou = Integer.valueOf(request.getParameter("zhutou")); 
		
		if (zhutou <= 0) {
			return returnModel;
		}
		
		UserFeedModel feed = feedService.getFeed(Integer.parseInt(feedId), false);
		if(feed != null){
			int res = userService.updUserAssetBySendUid(uid, zhutou, 0);
			if (res == 0) {
				returnModel.setCode(CodeContant.MONEYDEDUCT);
				returnModel.setMessage("余额不足，请充值！");
				return returnModel;
			}
			int addFeedReward = feedService.addFeedReward(Integer.parseInt(feedId), feed.getUid(), uid, zhutou);
			if(addFeedReward > 0){
				orderService.insertPayActivity(feed.getUid(), 2, 0, "用户打赏奖励", zhutou);
				Map<String, Object> map = new HashMap<String, Object>();
				UserAssetModel userAssetModel = userService.getUserAssetByUid(uid, false);
				map.put("surplus", userAssetModel.getMoney());
				String countStr = RedisCommService.getInstance().get(RedisContant.RedisNameFeed, RedisContant.FeedRewardCount+feedId);
				int laudCount = 0;
				if(StringUtils.isNotEmpty(countStr)){
					laudCount = Integer.parseInt(countStr);
				}
				map.put("laudCount", laudCount);
				returnModel.setData(map);
			}else{
				returnModel.setCode(CodeContant.feed_reward_err);
				returnModel.setMessage("打赏失败！");
				return returnModel;
			}
		}else{
			returnModel.setCode(CodeContant.feed_notfound_err);
			returnModel.setMessage("动态不存在！");
			return returnModel;
		}
		return returnModel;
	}
	
	/**
	 * 打赏礼物
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/reward/gift")
	@ResponseBody
	public ReturnModel feedRewardGift(HttpServletRequest request, HttpServletResponse response) {
		if (ParamHandleUtils.isBlank(request, "ncode", "os", "imei", "reqtime", "token", "feedId","gid","count")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(request, "os", "reqtime","feedId","gid","count")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		authToken(request, true);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		String token = request.getParameter("token");
		String os = request.getParameter("os");
		final int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		Long lgNow = System.currentTimeMillis() / 1000;
		String feedId = request.getParameter("feedId");
		int gid = Integer.valueOf(request.getParameter("gid")); 
		int count = Integer.valueOf(request.getParameter("count")); 
		if(gid != 2){
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(gid);
		if (giftConfigModel == null) {
			returnModel.setCode(CodeContant.GIFTCONFIGEXIST);
			returnModel.setMessage("礼物不存在");
			return returnModel;
		}
		if (count <= 0) {
			return returnModel;
		}
		int price = giftConfigModel.getGprice();
		int pricetotal = price * count;
		int wealths = giftConfigModel.getWealth() * count;
		double credits = giftConfigModel.getCredit() * count;
		
		UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(uid, false);
		if (sendUserAssetModel == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
			return returnModel;
		}
		if (pricetotal > sendUserAssetModel.getMoney()) {
			returnModel.setCode(CodeContant.MONEYLESS);
			returnModel.setMessage("金额不足");
			return returnModel;
		}
		
		UserFeedModel feed = feedService.getFeed(Integer.parseInt(feedId), false);
		if(feed != null){
			// 送礼者 扣费
			int res = userService.updUserAssetBySendUid(uid, pricetotal, wealths);
			if (res == 0) {
				returnModel.setCode(CodeContant.MONEYDEDUCT);
				returnModel.setMessage("余额不足，请充值！");
				return returnModel;
			}
			// 收礼者资产更新
			UserBaseInfoModel dstUserBaseinfo = userService.getUserbaseInfoByUid(feed.getUid(), false);
			res = userService.updUserAssetByGetUid(feed.getUid(), credits,credits);
			if (res == 0) {
				returnModel.setCode(CodeContant.MONEYACCEPT);
				returnModel.setMessage("收礼失败");
				return returnModel;
			}
			
			UserBaseInfoModel userBaseinfo = userService.getUserbaseInfoByUid(uid, false);
			UserAssetModel getUserAssetModel = userService.getUserAssetByUid(feed.getUid(), false);
			res = billService.insertBill(gid, count, price, uid, sendUserAssetModel.getMoney(),
					sendUserAssetModel.getWealth() + pricetotal, sendUserAssetModel.getCredit(), feed.getUid(),
					getUserAssetModel.getMoney(), getUserAssetModel.getWealth(), getUserAssetModel.getCredit(),
					lgNow, 1, credits, os, "", userBaseinfo.getNickname(),
					dstUserBaseinfo.getNickname(),dstUserBaseinfo.getFamilyId());
			
			if (res == 0) {
				logger.error("<sendGift>插入账单失败: gid="+gid+" count="+count+" price="+price+" srcUid="+uid+" surplus=" + sendUserAssetModel.getMoney() +" wealth="+
						(sendUserAssetModel.getWealth() + pricetotal)+" credit="+sendUserAssetModel.getCredit()+" dstUid="+feed.getUid()+" dstmoney="+
						getUserAssetModel.getMoney()+" dstWealth="+getUserAssetModel.getWealth()+" dstCredit="+getUserAssetModel.getCredit()+"  addtime="+
						lgNow+" type="+1+" dstGets="+credits+" os="+os+" bak="+" srcNickname="+ userBaseinfo.getNickname()+" dstNickname="+
						dstUserBaseinfo.getNickname());
			}

			try{
				AsyncManager.getInstance().execute(
						new UpdateUserLevelAsyncTask(uid, feed.getUid(), feed.getUid(), pricetotal, (int)credits, wealths, count));
			}catch (Exception ex){
				logger.error("UpdateUserLevelAsyncTask>>>",ex);
			}


			int addFeedReward = feedService.addFeedRewardGift(Integer.parseInt(feedId), feed.getUid(), uid, gid, count);
			if(addFeedReward > 0){
				Map<String, Object> map = new HashMap<String, Object>();
				UserAssetModel userAssetModel = userService.getUserAssetByUid(uid, false);
				map.put("surplus", userAssetModel.getMoney());
				String countStr = RedisCommService.getInstance().get(RedisContant.RedisNameFeed, RedisContant.FeedRewardGiftCount+feedId);
				int laudCount = 0;
				if(StringUtils.isNotEmpty(countStr)){
					laudCount = Integer.parseInt(countStr);
				}
				map.put("laudCount", laudCount);
				returnModel.setData(map);
			}else{
				returnModel.setCode(CodeContant.feed_reward_err);
				returnModel.setMessage("打赏失败！");
				return returnModel;
			}
		}else{
			returnModel.setCode(CodeContant.feed_notfound_err);
			returnModel.setMessage("动态不存在！");
			return returnModel;
		}
		return returnModel;
	}
	
	/**
	 * 更新用户等级数据
	 */
	private class UpdateUserLevelAsyncTask implements IAsyncTask {

		private int srcUid;
		private int dstUid;
		private int anchoruid;
		private int sends;
		private int gets;
		private int wealth;
		private int count;

		/**
		 * 初始化
		 * 
		 * @param srcUid
		 *            送礼人uid
		 * @param dstUid
		 *            收礼人uid
		 * @param sends
		 *            送出数
		 * @param gets
		 *            收到数
		 */
		public UpdateUserLevelAsyncTask(final Integer srcUid, Integer dstUid, int anchoruid, int sends, int gets,int wealth,int count) {
			this.srcUid = srcUid;
			this.dstUid = dstUid;
			this.anchoruid = anchoruid;
			this.sends = sends;
			this.gets = gets;
			this.wealth = wealth;
			this.count = count;
		}

		@Override
		public void runAsync() {

			if (gets > 0) {
				RelationRedisService.getInstance().addRoomCreditThis(dstUid, gets);
			}
			
			//每日消费增加经验
			userGuardInfoService.firstSpendUpdExp(srcUid, anchoruid);
			//用户送礼经验加成
			UserVipInfoModel vipInfoModel = ValueaddServiceUtil.getVipInfo(srcUid);
			if(vipInfoModel != null){
				ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(vipInfoModel.getGid(), 1);
				double speedup = privilegeModel.getLevelSpeedup();
				double exp = wealth * speedup;
				UserServiceImpl.getInstance().addUserExpByTask(srcUid, (int)exp);	
			}
			
			if (dstUid > 0) {
				// 记录当天收到的声援值
				if (gets > 0) {
					// 添加主播与用户关系
					RelationRedisService.getInstance().addDstGetSrc(srcUid, dstUid, (double) gets);
					// 修改粉丝的排序score 送礼值排序
					Double db = RelationRedisService.getInstance().isFansScore(dstUid, srcUid);
					if (db != null) {
						RelationRedisService.getInstance().addFans(dstUid, srcUid, db + gets, "on");
					}
					// 添加用户与主播的关系
					RelationRedisService.getInstance().addSrcSendDst(srcUid, dstUid, (double) sends);
					// 关注主播排序:送礼值+登录次数
					Double dlFollows = RelationRedisService.getInstance().isFollows(srcUid, dstUid);
					if (dlFollows != null) {
						RelationRedisService.getInstance().addFollows(srcUid, dstUid, sends * 1000 + dlFollows, "on");
					}
				}

			}

			// 用户侧
			roomService.updUserLevel(srcUid, dstUid, anchoruid, 1, 1);

			if (dstUid > 0) {
				// 主播侧
				roomService.updUserLevel(srcUid, dstUid, anchoruid, 2, 1);
			}

			// 日、周、月、总榜
			UserRedisService.getInstance().setRank(String.valueOf(srcUid), String.valueOf(dstUid), sends,gets);
			//鲜花榜的 日、周、月、总 榜
			UserRedisService.getInstance().setRewardGiftRank(String.valueOf(srcUid), String.valueOf(dstUid), count);
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "UpdateUserLevelAsyncTask";
		}

		@Override
		public void afterOk() {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterError(Exception e) {
			// TODO Auto-generated method stub

		}
	}
	
	@RequestMapping(value = "/delete")
	@ResponseBody
	public ReturnModel deleteFeed(HttpServletRequest request, HttpServletResponse response) {
		if (ParamHandleUtils.isBlank(request, "ncode", "os", "imei", "reqtime", "token", "feedId")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(request, "os", "reqtime", "feedId")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		authToken(request, true);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		String token = request.getParameter("token");
		final int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		String feedId = request.getParameter("feedId");
		UserFeedModel feed = feedService.getFeed(Integer.parseInt(feedId), false);
		if(feed != null){
			if(feed.getUid() != uid){
				returnModel.setCode(CodeContant.feed_delete_err);
				returnModel.setMessage("删除失败！");
				return returnModel;
			}
		}else{
			returnModel.setCode(CodeContant.feed_notfound_err);
			returnModel.setMessage("动态不存在！");
			return returnModel;
		}
		int delFeed = feedService.delFeed(Integer.parseInt(feedId));
		if(delFeed <= 0){
			returnModel.setCode(CodeContant.feed_delete_err);
			returnModel.setMessage("删除失败！");
			return returnModel;
		}
		return returnModel;
	}
	
	@RequestMapping(value = "/reply/add")
	@ResponseBody
	public ReturnModel addReply(HttpServletRequest request, HttpServletResponse response) {
		if (ParamHandleUtils.isBlank(request, "ncode", "os", "imei", "reqtime", "token", "feedId","content")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(request, "os", "reqtime", "feedId")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		authToken(request, true);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		String token = request.getParameter("token");
		final int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		String feedId = request.getParameter("feedId");
		String frId = request.getParameter("frId");
		String content = request.getParameter("content");
		String toUid = request.getParameter("toUid");
		int ilen = com.mpig.api.utils.StringUtils.length(content);
		if (ilen > 350) {
			returnModel.setCode(CodeContant.feed_content_length_err);
			returnModel.setMessage("动态超过长度限制");
			return returnModel;
		}
		UserFeedModel feed = feedService.getFeed(Integer.parseInt(feedId), false);
		if(feed == null){
			returnModel.setCode(CodeContant.feed_notfound_err);
			returnModel.setMessage("动态不存在！");
			return returnModel;
		}
		int addFeedReply = feedService.addFeedReply(Integer.parseInt(feedId),org.apache.commons.lang.StringUtils.isNotEmpty(frId)?Integer.parseInt(frId):0, uid, org.apache.commons.lang.StringUtils.isNotEmpty(toUid)?Integer.parseInt(toUid):0, content);
		if(addFeedReply <= 0){
			returnModel.setCode(CodeContant.feed_addReply_err);
			returnModel.setMessage("添加回复失败");
			return returnModel;
		}
		Map<String,Object> dataMap = new HashMap<String,Object>();
		dataMap.put("frId", addFeedReply);
		returnModel.setData(dataMap);
		return returnModel;
	}
	
	@RequestMapping(value = "/reply/delete")
	@ResponseBody
	public ReturnModel deleteReply(HttpServletRequest request, HttpServletResponse response) {
		if (ParamHandleUtils.isBlank(request, "ncode", "os", "imei", "reqtime", "token", "feedId","frId")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(request, "os", "reqtime", "feedId")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		authToken(request, true);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}
		String token = request.getParameter("token");
		final int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		String feedId = request.getParameter("feedId");
		String frId = request.getParameter("frId");
		UserFeedModel feed = feedService.getFeed(Integer.parseInt(feedId), false);
		boolean flag = false;
		if(feed == null){
			returnModel.setCode(CodeContant.feed_notfound_err);
			returnModel.setMessage("动态不存在！");
			return returnModel;
		}else{
			if(feed.getUid() == uid){
				flag= true;
			}
			UserFeedReplyModel userFeedReplyModel = feedService.getFeedReplyById(Integer.parseInt(frId));
			if(userFeedReplyModel != null){
				if(userFeedReplyModel.getFromUid() == uid){
					flag = true;
				} 
			}
		}
		if(flag){
			int delReply = feedService.delFeedReply(Integer.parseInt(frId), Integer.parseInt(feedId));
			if(delReply <= 0){
				returnModel.setCode(CodeContant.feed_delReply_err);
				returnModel.setMessage("删除回复失败");
				return returnModel;
			}	
		}else{
			returnModel.setCode(CodeContant.feed_delReply_err);
			returnModel.setMessage("删除回复失败");
			return returnModel;
		}
		return returnModel;
	}
	
	@RequestMapping(value = "/info")
	@ResponseBody
	public ReturnModel getFeedInfo(HttpServletRequest request, HttpServletResponse response) {
		if (ParamHandleUtils.isBlank(request, "ncode", "os", "imei", "reqtime", "token","feedId")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(request, "os", "reqtime","feedId")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		authToken(request, true);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}
		String rewardtype = request.getParameter("rewardtype");
		String token = request.getParameter("token");
		final int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		String feedId = request.getParameter("feedId");
		UserFeedModel feedById = feedService.getFeedById(Integer.parseInt(feedId),uid);
		Map<String,Object> dataMap = new HashMap<String,Object>();
		if(feedById == null) {
			returnModel.setCode(CodeContant.feed_notfound_err);
			returnModel.setMessage("动态不存在！");
			return returnModel;
		}
		dataMap.put("data", feedById);
		if(rewardtype==null){
			dataMap.put("rewards", feedService.getFeedReward(Integer.parseInt(feedId)));
		}else{
			dataMap.put("rewardsGift", feedService.getFeedRewardGift(Integer.parseInt(feedId)));
		}
		returnModel.setData(dataMap);
		return returnModel;
	}
	
	@RequestMapping(value = "/reply/list")
	@ResponseBody
	public ReturnModel getFeedReplyList(HttpServletRequest request, HttpServletResponse response) {
		if (ParamHandleUtils.isBlank(request, "ncode", "os", "imei", "reqtime", "token","feedId")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(request, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		authToken(request, true);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}
		String token = request.getParameter("token");
		final int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		String page = request.getParameter("page");
		if(page == null){
			page = "1";
		}
		String feedId = request.getParameter("feedId");
		List<UserFeedReplyModel> feedReplyByFeedId = feedService.getFeedReplyByFeedId(Integer.parseInt(feedId), Integer.parseInt(page));
		Map<String,Object> dataMap = new HashMap<String,Object>();
		dataMap.put("data", feedReplyByFeedId);
		returnModel.setData(dataMap);
		return returnModel;
	}
	
	@RequestMapping(value = "/follow")
	@ResponseBody
	public ReturnModel feedForFollow(HttpServletRequest request, HttpServletResponse response) {
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
		authToken(request, true);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}
		String token = request.getParameter("token");
		final int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		String page = request.getParameter("page");
		if(page == null){
			page = "1";
		}
		List<UserFeedModel> feedByUserFlow = feedService.getFeedByUserFlow(uid, Integer.parseInt(page));
		Map<String,Object> dataMap = new HashMap<String,Object>();
		dataMap.put("data", feedByUserFlow);
		returnModel.setData(dataMap);
		return returnModel;
	}
	
	@RequestMapping(value = "/user")
	@ResponseBody
	public ReturnModel feedForUser(HttpServletRequest request, HttpServletResponse response) {
		if (ParamHandleUtils.isBlank(request, "ncode", "os", "imei", "reqtime", "token","dstUid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(request, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		authToken(request, true);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}
		String token = request.getParameter("token");
		final int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		String page = request.getParameter("page");
		if(page == null){
			page = "1";
		}
		String dstUid = request.getParameter("dstUid");
		List<UserFeedModel> feedByUserFlow = feedService.getFeedByUser(uid, Integer.parseInt(dstUid), Integer.parseInt(page));
		Map<String,Object> dataMap = new HashMap<String,Object>();
		dataMap.put("data", feedByUserFlow);
		returnModel.setData(dataMap);
		return returnModel;
	}
	@RequestMapping(value = "/Square")
	@ResponseBody
	public ReturnModel feedForSquare(HttpServletRequest request, HttpServletResponse response) {
		if (ParamHandleUtils.isBlank(request, "ncode", "os", "imei", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(request, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		authToken(request, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}
		
		int uid = 0;
		
		String token = request.getParameter("token");
		if (!StringUtils.isEmpty(token)) {
			uid = authService.decryptToken(token, returnModel);
			if (uid <= 0) {
				return returnModel;
			}
		}
		String page = request.getParameter("page");
		if(page == null){
			page = "1";
		}
		List<UserFeedModel> feedByUserFlow = feedService.getFeedBySquare(uid, Integer.parseInt(page));
		Map<String,Object> dataMap = new HashMap<String,Object>();
		dataMap.put("data", feedByUserFlow);
		returnModel.setData(dataMap);
		return returnModel;
	}
	
	
	@RequestMapping("/report")
	@ResponseBody
	public ReturnModel report(Integer fid,String reportReason, HttpServletRequest req,HttpServletResponse resp){
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token","fid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}

		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		String token = req.getParameter("token");
		int dstuid = authService.decryptToken(token, returnModel);
		if (dstuid <= 0) {
			return returnModel;
		}
		try {
			//获取当前动态的信息
			UserFeedModel userFeedModel = feedService.getFeed(fid, false);
			if(userFeedModel != null){
				Integer uid = userFeedModel.getUid();
				//查询当前动态是否被举报过
				int rid = feedService.getReportFeedByFid(fid);
				if(rid>0){
					//查询当前用户是否举报过
					int rsc = feedService.getReportFeedUserByRid(rid, dstuid);
					if(rsc == 0){
						//增加举报信息的举报次数
						feedService.updReportFeed(fid);
						//增加用户的举报信息记录
						feedService.addReportFeedUser(rid, fid, reportReason, dstuid);
					}else{
						returnModel.setCode(CodeContant.feedreport_IsHavent);
						returnModel.setMessage("请勿重复举报！");
					}
				}else{
					rid = feedService.addReportFeed(uid, fid);
					feedService.addReportFeedUser(rid, fid, reportReason, dstuid);
				}
			}else{
				returnModel.setCode(CodeContant.feed_notfound_err);
				returnModel.setMessage("您举报的动态不存在！");
			}
		} catch (Exception e) {
			returnModel.setCode(CodeContant.CONSYSTEMERR);
			returnModel.setMessage("服务器暂时休克啦~");
			logger.error(e);
		}
		return returnModel;
	}
	
	@RequestMapping("/getRecommendAnchor")
	@ResponseBody
	public ReturnModel getRecommendAnchor(HttpServletRequest req,HttpServletResponse resp){
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","more")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","more")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}

		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		int uid = 0;
		String token = req.getParameter("token");
		if(!StringUtils.isEmpty(token)){
			// 非游客调用
			uid = authService.decryptToken(token, returnModel);
			if (uid <= 0) {
				return returnModel;
			}
		}
		String more = req.getParameter("more");
		List<Map<String, Object>> recommendAnchor = feedService.getRecommendAnchor();
		if(more.equals("1")){
			if(recommendAnchor.size() > 20){
				recommendAnchor = recommendAnchor.subList(0, 20);
			}
		}else{
			if(recommendAnchor.size() > 5){
				recommendAnchor = recommendAnchor.subList(0, 5);
			}
		}
		if (uid != 0) {
			for(Map<String, Object> userMap: recommendAnchor){
				userMap.put("isfan", RelationRedisService.getInstance().isFan(uid, (int)userMap.get("uid")));// 是否关注过
			}
		}
		Map<String,Object> dataMap = new HashMap<String,Object>();
		dataMap.put("list", recommendAnchor);
		returnModel.setData(dataMap);
		return returnModel;
	}
	
	
	@RequestMapping("/follows")
	@ResponseBody
	public ReturnModel addFollows(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "dstUids")) {
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
			return returnModel;
		}

		String token = req.getParameter("token");
		int srcUid = authService.decryptToken(token, returnModel);
		if (srcUid <= 0) {
			return returnModel;
		}
		String strType = "on";
		// uid 关注 dstuid
		String dstUids = req.getParameter("dstUids"); 
		String[] split = dstUids.split(";");
		for(String dstUidStr : split){
			Integer dstUid = Integer.parseInt(dstUidStr);
			UserBaseInfoModel userbaseInfoByUid = userService.getUserbaseInfoByUid(dstUid,false);
			if (userbaseInfoByUid == null) {
				continue;
			}else {
				if (srcUid == dstUid) {
					continue;
				} else {
					if (StringUtils.isNotEmpty(RelationRedisService.getInstance().getBlackUser(dstUid,srcUid))) {
						continue;
					} else {
						boolean bl = true;
						if (RelationRedisService.getInstance().getFollowsTimes(srcUid, dstUid)) {
							bl = false;
						}
						String os = "2".equals(req.getParameter("os")) ? "ios" : "android";
						boolean blFollows = RelationRedisService.getInstance().isFan(srcUid, dstUid);
						if (blFollows) {
							// 已经是粉丝
							if ("on".equalsIgnoreCase(strType)) {
								continue;
							} else {
								userService.addFollows(strType, srcUid, dstUid);
								UserRedisService.getInstance().delBroadcastAnchor(os, String.valueOf(dstUid), String.valueOf(srcUid));
							}
						} else {
							// 不是粉丝
							if ("on".equalsIgnoreCase(strType)) {
								
								userService.addFollows(strType, srcUid, dstUid);
								if (StringUtils.isNotEmpty(RelationRedisService.getInstance().getBlackUser(srcUid,dstUid))) {
									// 解除黑名单
									RelationRedisService.getInstance().unBlackUser(srcUid, dstUid);
								}
								// 检查是否已存在os、主播、用户的devicetoken关系
								String broadcastAnchor = UserRedisService.getInstance().getBroadcastAnchor(os, String.valueOf(dstUid), String.valueOf(srcUid));
								if (StringUtils.isEmpty(broadcastAnchor)) {
									String appBroadcast = OtherRedisService.getInstance().getAppBroadcast(os, String.valueOf(srcUid));
									if (StringUtils.isNotEmpty(appBroadcast)) {
										UserRedisService.getInstance().setBroadcastAnchor(os, String.valueOf(dstUid), String.valueOf(srcUid), appBroadcast);
									}
								}
							} else {
								continue;
							}
						}
						if (strType.equals("on") && bl) {
							userService.rpcAdminFollow(srcUid, dstUid, true);
						}
					}
				}
			}
		}
		return returnModel;
	}
}
