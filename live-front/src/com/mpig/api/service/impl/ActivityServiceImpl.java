package com.mpig.api.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mpig.api.SqlTemplete;
import com.mpig.api.dao.IInviteUserInfoDao;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.dictionary.InvitationConfig;
import com.mpig.api.dictionary.InvitationRewardsConfig;
import com.mpig.api.dictionary.RechargeLotteryConfig;
import com.mpig.api.dictionary.lib.ActivityConfigLib;
import com.mpig.api.dictionary.lib.InviteRewardsConfigLib;
import com.mpig.api.model.ConfigGiftModel;
import com.mpig.api.model.InviteUserInfoModel;
import com.mpig.api.model.InviteUserPeckLogModel;
import com.mpig.api.model.InviteUserRewardInfoModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.UserAssetModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.model.UserCarInfoModel;
import com.mpig.api.model.UserVipInfoModel;
import com.mpig.api.redis.service.RedisCommService;
import com.mpig.api.service.IActivityService;
import com.mpig.api.service.IConfigService;
import com.mpig.api.service.IOrderService;
import com.mpig.api.service.IRoomService;
import com.mpig.api.service.IUserCarInfoService;
import com.mpig.api.service.IUserItemService;
import com.mpig.api.service.IUserItemService.ItemSource;
import com.mpig.api.service.IUserService;
import com.mpig.api.service.IUserVipInfoService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.MathRandomUtil;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.ValueaddServiceUtil;
import com.mpig.api.utils.VarConfigUtils;

@Service
public class ActivityServiceImpl implements IActivityService{
	private static final Logger logger = Logger.getLogger(ActivityServiceImpl.class);
	@Autowired
	private IInviteUserInfoDao inviteUserInfoDao;
	@Resource
	private IUserService userService;
	@Resource
	private IConfigService configService;
	@Resource
	private IUserItemService userItemService;
	@Resource
	private IOrderService orderService;
	@Resource
	private IUserVipInfoService vipInfoService;
	@Resource
	private IRoomService roomService;
	@Resource
	private IUserCarInfoService userCarInfoService;
	
	@Override
	public int getLotteryCount(Integer uid){
		String dateFormat = DateUtils.dateFormat(new Date(), "yyyyMMdd");
		String drawkey = RedisContant.payDayActDraw + ":" + dateFormat + ":" + uid;
		Long count = RedisCommService.getInstance().incrBy(RedisContant.RedisNameUser, drawkey, -1, 0);
		if(count < 0){
			RedisCommService.getInstance().incrBy(RedisContant.RedisNameUser, drawkey, 1, 0);
			return -1;
		}
		return count.intValue();
	}
	
	@Override
	public int getSurplusLotteryCount(Integer uid){
		String dateFormat = DateUtils.dateFormat(new Date(), "yyyyMMdd");
		String drawkey = RedisContant.payDayActDraw + ":" + dateFormat + ":" + uid;
		String count = RedisCommService.getInstance().get(RedisContant.RedisNameUser, drawkey);
		return Integer.parseInt(count);
	}
	
	
	@Override
	public int getNewYearLotteryCount(Integer uid){
		int maxCount = 5;
		int surplusCount = 0;
		String dateFormat = DateUtils.dateFormat(new Date(), "yyyyMMdd");
		String drawkey = RedisContant.newYearDayActDraw + ":" + dateFormat + ":" + uid;
		Long count = RedisCommService.getInstance().incrBy(RedisContant.RedisNameUser, drawkey, 1, 30*24*60*60);
		if(count > 5){
			RedisCommService.getInstance().incrBy(RedisContant.RedisNameUser, drawkey, -1, 0);
			surplusCount =  -1;
		}else{
			surplusCount = maxCount - count.intValue();
		}
		return surplusCount;
	}
	@Override
	public int getNewYearSurplusCount(Integer uid){
		String dateFormat = DateUtils.dateFormat(new Date(), "yyyyMMdd");
		String drawkey = RedisContant.newYearDayActDraw + ":" + dateFormat + ":" + uid;
		String count = RedisCommService.getInstance().get(RedisContant.RedisNameUser, drawkey);
		int surplusCount = 0;
		int maxCount = 5;
		if(count == null){
			surplusCount = maxCount;
		}else{
			int tmpCount = Integer.parseInt(count);
			if(tmpCount < maxCount){
				surplusCount = maxCount - tmpCount;
			}else{
				surplusCount = 0;
			}
		}
		return surplusCount;
	}
	
	@Override
	public ReturnModel newYearLottery(int uid, ReturnModel returnModel){
		long now = System.currentTimeMillis()/1000;
		UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(uid, false);
		if (sendUserAssetModel == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
			return returnModel;
		}
		int maxCount = 5;
		int surplusCount = getNewYearLotteryCount(uid);
		if(surplusCount < 0){
			returnModel.setCode(CodeContant.lotteryCountDeduct);
			returnModel.setMessage("抽奖次数不够！");
			return returnModel;
		}
		int count = maxCount-surplusCount;
		int price = 20;
		if(count == 1){
			price = 0;
		}
		if(count ==2){
			price = 1;
		}
		if(count ==3){
			price = 5;
		}
		if(count ==4){
			price = 10;
		}
		if(count ==5){
			price = 20;
		}
		if(price > 0){
			if (price > sendUserAssetModel.getMoney()) {
				String dateFormat = DateUtils.dateFormat(new Date(), "yyyyMMdd");
				String drawkey = RedisContant.newYearDayActDraw + ":" + dateFormat + ":" + uid;
				RedisCommService.getInstance().incrBy(RedisContant.RedisNameUser, drawkey, -1, 0);
				returnModel.setCode(CodeContant.MONEYLESS);
				returnModel.setMessage("金额不足");
				return returnModel;
			}
			int res = userService.updUserAssetBySendUid(uid, price, price);
			if (res == 0) {
				String dateFormat = DateUtils.dateFormat(new Date(), "yyyyMMdd");
				String drawkey = RedisContant.newYearDayActDraw + ":" + dateFormat + ":" + uid;
				RedisCommService.getInstance().incrBy(RedisContant.RedisNameUser, drawkey, -1, 0);
				returnModel.setCode(CodeContant.MONEYDEDUCT);
				returnModel.setMessage("扣费失败");
				return returnModel;
			}
			roomService.updUserLevel(uid, uid, uid, 1, 1);
			insConsumeLottory(uid, 2, price, "春节活动");
		}
		ConcurrentHashMap<Integer, RechargeLotteryConfig> rechargeLotteryMap = ActivityConfigLib.getNewYearLotteryConfig();
		int prize = MathRandomUtil.newYearRandom();
		RechargeLotteryConfig rechargeLotteryConfig = rechargeLotteryMap.get(prize);
		String lotteryMsg = "";
		if(rechargeLotteryConfig != null){
			if(rechargeLotteryConfig.getType() == 0){
				String dateFormat = DateUtils.dateFormat(new Date(), "yyyyMMdd");
				String countstr = RedisCommService.getInstance().get(RedisContant.RedisNameOther, "newYear:gift:"+rechargeLotteryConfig.getGid()+dateFormat);
				int carCount = 0;
				if(StringUtils.isNotEmpty(countstr)){
					carCount = Integer.parseInt(countstr);
				}
				if(carCount>=rechargeLotteryConfig.getNum()){
					prize = 8;
					lotteryMsg = "谢谢惠顾";
				}else{
					RedisCommService.getInstance().incrBy(RedisContant.RedisNameOther, "newYear:gift:"+rechargeLotteryConfig.getGid()+dateFormat,1,30*24*60*60);
					userItemService.insertUserItem(uid, rechargeLotteryConfig.getGid(), 1, ItemSource.newYearConsumeLottery);
					ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(rechargeLotteryConfig.getGid());
					lotteryMsg = "恭喜您抽到了1个"+giftConfigModel.getGname();
				}
			}
			if(rechargeLotteryConfig.getType() == -1){
				lotteryMsg = "谢谢惠顾";
			}
			if(rechargeLotteryConfig.getType() == 5){
				String dateFormat = DateUtils.dateFormat(new Date(), "yyyyMMdd");
				String countstr = RedisCommService.getInstance().get(RedisContant.RedisNameOther, "newYear:car:"+rechargeLotteryConfig.getGid()+dateFormat);
				int carCount = 0;
				if(StringUtils.isNotEmpty(countstr)){
					carCount = Integer.parseInt(countstr);
				}
				if(carCount>=rechargeLotteryConfig.getNum()){
					prize = 8;
					lotteryMsg = "谢谢惠顾";
				}else{
					Date date = new Date();
					Date startdate = new Date();
					Date newEndtime = new Date();
					int gid = rechargeLotteryConfig.getGid();
					UserCarInfoModel userCarInfo = userCarInfoService.getUserCarInfo(uid, gid, true);
					int rsc = 0;
					if(userCarInfo != null){
						long endtime = userCarInfo.getEndtime();
						long starttime = userCarInfo.getStarttime();
						if(endtime > now){
							date = new Date(endtime*1000);
							startdate = new Date(starttime*1000);
							newEndtime = DateUtils.getNMonthAfterDate(date, 1);
							rsc = userCarInfoService.updUserCarInfo(uid, gid, new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 0);
						}else{
							newEndtime = DateUtils.getNMonthAfterDate(date, 1);
							rsc = userCarInfoService.updUserCarInfo(uid, gid, new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 0);
						}
					}else{
						newEndtime = DateUtils.getNMonthAfterDate(date, 1);
						rsc = userCarInfoService.addUserCarInfo(uid, gid, new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 0);
					}
					if(rsc == 0){
						logger.error("赠送座驾失败！ uid: "+uid+" gid : "+gid+" starttime :"+new Long(startdate.getTime()/1000).intValue()+ " endtime"+new Long(newEndtime.getTime()/1000).intValue());
						returnModel.setCode(CodeContant.mall_buy_car_error);
						returnModel.setMessage("赠送座驾失败，请联系客服");
						return returnModel;
					}
					ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(rechargeLotteryConfig.getGid());
					lotteryMsg = "恭喜您抽到了1辆 "+giftConfigModel.getGname()+" 座驾";
					RedisCommService.getInstance().incrBy(RedisContant.RedisNameOther, "newYear:car:"+rechargeLotteryConfig.getGid()+dateFormat,1,30*24*60*60);
					userItemService.insertUserItemLog(uid, rechargeLotteryConfig.getGid(), 30, ItemSource.consumeLottery);
				}
			}
			if(rechargeLotteryConfig.getType() == 88){
				String dateFormat = DateUtils.dateFormat(new Date(), "yyyyMMdd");
				String countstr = RedisCommService.getInstance().get(RedisContant.RedisNameOther, "newYear:hongbao:88"+dateFormat);
				int carCount = 0;
				if(StringUtils.isNotEmpty(countstr)){
					carCount = Integer.parseInt(countstr);
				}
				if(carCount>=rechargeLotteryConfig.getNum()){
					prize = 8;
					lotteryMsg = "谢谢惠顾";
				}else{
					int insRewardLottory = insRewardLottory(uid, 2, 88, "新年活动抽奖");
					if(insRewardLottory>0){
						RedisCommService.getInstance().incrBy(RedisContant.RedisNameOther, "newYear:hongbao:88"+dateFormat,1,30*24*60*60);
						userService.updAssetMoneyByUid(uid, (double)88);
						lotteryMsg = "恭喜您抽到了88猪头";
					}else{
						prize = 8;
						lotteryMsg = "谢谢惠顾";
					}
				}
			}
			if(rechargeLotteryConfig.getType() == 188){
				String dateFormat = DateUtils.dateFormat(new Date(), "yyyyMMdd");
				String countstr = RedisCommService.getInstance().get(RedisContant.RedisNameOther, "newYear:hongbao:188"+dateFormat);
				int carCount = 0;
				if(StringUtils.isNotEmpty(countstr)){
					carCount = Integer.parseInt(countstr);
				}
				if(carCount>=rechargeLotteryConfig.getNum()){
					prize = 8;
					lotteryMsg = "谢谢惠顾";
				}else{
					int insRewardLottory = insRewardLottory(uid, 2, 188, "新年活动抽奖");
					if(insRewardLottory > 0){
						RedisCommService.getInstance().incrBy(RedisContant.RedisNameOther, "newYear:hongbao:188"+dateFormat,1,30*24*60*60);
						userService.updAssetMoneyByUid(uid, (double)188);
						lotteryMsg = "恭喜您抽到了188猪头";
					}else{
						prize = 8;
						lotteryMsg = "谢谢惠顾";
					}
				}
			}
			if(rechargeLotteryConfig.getType() == 888){
				String dateFormat = DateUtils.dateFormat(new Date(), "yyyyMMdd");
				String countstr = RedisCommService.getInstance().get(RedisContant.RedisNameOther, "newYear:hongbao:888"+dateFormat);
				int carCount = 0;
				if(StringUtils.isNotEmpty(countstr)){
					carCount = Integer.parseInt(countstr);
				}
				if(carCount>=rechargeLotteryConfig.getNum()){
					prize = 8;
					lotteryMsg = "谢谢惠顾";
				}else{
					int insRewardLottory = insRewardLottory(uid, 2, 888, "新年活动抽奖");
					if(insRewardLottory > 0){
						RedisCommService.getInstance().incrBy(RedisContant.RedisNameOther, "newYear:hongbao:888"+dateFormat,1,30*24*60*60);
						userService.updAssetMoneyByUid(uid, (double)888);
						lotteryMsg = "恭喜您抽到了888猪头";
					}else{
						prize = 8;
						lotteryMsg = "谢谢惠顾";
					}
				}
			}
			
			if(rechargeLotteryConfig.getType() == 3){
				String dateFormat = DateUtils.dateFormat(new Date(), "yyyyMMdd");
				String countstr = RedisCommService.getInstance().get(RedisContant.RedisNameOther, "newYear:vip:43"+dateFormat);
				int carCount = 0;
				if(StringUtils.isNotEmpty(countstr)){
					carCount = Integer.parseInt(countstr);
				}
				if(carCount>=rechargeLotteryConfig.getNum()){
					prize = 8;
					lotteryMsg = "谢谢惠顾";
				}else{
					UserVipInfoModel userVipInfoModel = ValueaddServiceUtil.getVipInfo(uid);
					Date date = new Date();
					Date startdate = new Date();
					if(userVipInfoModel != null){
						long endtime = userVipInfoModel.getEndtime();
						long starttime = userVipInfoModel.getStarttime();
						date = new Date(endtime * 1000);
						startdate = new Date(starttime * 1000);
					}
					Date newEndtime = DateUtils.getNDaysAfterDate(date, 7);
					/**
					 * 加入身份记录 更新缓存
					 */
					ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(rechargeLotteryConfig.getGid());
					int rsc = 0;
					if(userVipInfoModel != null){
						if(userVipInfoModel.getGid()==44){
							int zhnum = 200;
							lotteryMsg = "发现您已有钻石VIP，7天 白金VIP体验 系统默认置换 "+zhnum+"个新年快乐";
							userItemService.insertUserItemNew(uid, 100, zhnum);
							userItemService.insertUserItem(uid, rechargeLotteryConfig.getGid(), 7, ItemSource.newYearConsumeLottery);
						}else{
							rsc  = vipInfoService.updUserVipInfo(uid,new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 0,rechargeLotteryConfig.getGid());
							userItemService.insertUserItemLog(uid, rechargeLotteryConfig.getGid(), 7, ItemSource.newYearConsumeLottery);
							lotteryMsg = "恭喜您抽到了7天"+giftConfigModel.getGname();
						}
					}else{
						UserVipInfoModel newUserVipInfoModel = vipInfoService.getUserVipInfoByUid(uid, rechargeLotteryConfig.getGid());
						if(newUserVipInfoModel != null){
							rsc = vipInfoService.updUserVipInfo(uid,new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 0,rechargeLotteryConfig.getGid());
						}else{
							rsc = vipInfoService.addUserVipInfo(uid, rechargeLotteryConfig.getGid(), new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 0);
						}
						userItemService.insertUserItemLog(uid, rechargeLotteryConfig.getGid(), 7, ItemSource.newYearConsumeLottery);
						lotteryMsg = "恭喜您抽到了7天"+giftConfigModel.getGname();
					}
					RedisCommService.getInstance().incrBy(RedisContant.RedisNameOther, "newYear:vip:43"+dateFormat,1,30*24*60*60);
					vipInfoService.getUserVipInfo(uid, true);
				}
			}
		}
		int surplusCounts = maxCount-count;
		if(surplusCounts == 5){
			price = 0;
		}
		if(surplusCounts ==4){
			price = 1;
		}
		if(surplusCounts ==3){
			price = 5;
		}
		if(surplusCounts ==2){
			price = 10;
		}
		if(surplusCounts ==1){
			price = 20;
		}
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("lotteryIndex", prize);
		dataMap.put("surplusCount", surplusCounts);
		dataMap.put("price", price);
		dataMap.put("lotteryMsg", lotteryMsg);
		returnModel.setData(dataMap);
		return returnModel;
	}
	
	@Override
	public Map<String, Object> getInviteInfo(Integer uid){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		//获取邀请人的邀请次数相关信息
		InviteUserInfoModel inviteUserInfoModel = inviteUserInfoDao.selInviteUserInfoByUid(uid);
		
		
		
		if(inviteUserInfoModel != null){
			dataMap.put("inviteCount", inviteUserInfoModel.getInviteCount());
			dataMap.put("inviteGets", inviteUserInfoModel.getGets());
		}else{
			dataMap.put("inviteCount", 0);
			dataMap.put("inviteGets", 0);
		}
		List<Map<String, Object>> inviteTasks = new ArrayList<Map<String, Object>>();
		Map<String, Object> taskMap = new HashMap<String, Object>();
		//将当前用户的已领阶段奖励列表组装成map
		Map<Integer, InviteUserPeckLogModel> inviteUserPeckTempMap = new HashMap<Integer, InviteUserPeckLogModel>();
		//获取用户的阶段奖励领取情况
		List<InviteUserPeckLogModel> inviteUserPeckLogs = inviteUserInfoDao.getInviteUserPeckLog(uid);
		for(InviteUserPeckLogModel inviteUserPeckLogModel: inviteUserPeckLogs){
			inviteUserPeckTempMap.put(inviteUserPeckLogModel.getInvitationId(), inviteUserPeckLogModel);
		}
		//获取任务列表
		List<InvitationConfig> invitationConfigs = InviteRewardsConfigLib.getInvitationConfigs();
		//组装当前用户的奖励领取情况以及任务信息
		for(InvitationConfig invitationConfig: invitationConfigs){
			InviteUserPeckLogModel inviteUserPeckLogModel = inviteUserPeckTempMap.get(invitationConfig.getId());
			taskMap = new HashMap<String, Object>();
			taskMap.put("taskRewardId", invitationConfig.getId());
			taskMap.put("taskName", invitationConfig.getName());
			taskMap.put("schedule", invitationConfig.getSchedule());
			if(inviteUserPeckLogModel!=null){
				taskMap.put("status", 2);
			}else{
				taskMap.put("status", 1);
			}
			inviteTasks.add(taskMap);
		}
		dataMap.put("inviteTask", inviteTasks);
		//查找被邀请用户的奖励领取列表
		List<Map<String,Object>> inviteUserList = new ArrayList<Map<String,Object>>();
		Map<String,Object> inviteUserMap = new HashMap<String,Object>();
		List<String> uids = new ArrayList<String>();
		List<InviteUserRewardInfoModel> inviteUserRewardInfoModels = inviteUserInfoDao.selInviteUserRewardByUid(uid);
		for(InviteUserRewardInfoModel infoModel : inviteUserRewardInfoModels){
			uids.add(infoModel.getUid().toString());
		}
		Map<String, UserBaseInfoModel>  userBaseInfoMap = new HashMap<String, UserBaseInfoModel>();
		if(inviteUserRewardInfoModels.size() > 0){
			userBaseInfoMap = userService.getUserbaseInfoByUid(uids.toArray(new String[0]));
		}
		for(InviteUserRewardInfoModel infoModel : inviteUserRewardInfoModels){
			UserBaseInfoModel userBaseInfo = userBaseInfoMap.get(infoModel.getUid().toString());
			if(userBaseInfo != null){
				inviteUserMap = new HashMap<String,Object>();
				inviteUserMap.put("uid", userBaseInfo.getUid());
				inviteUserMap.put("nickname", userBaseInfo.getNickname());
				inviteUserMap.put("headimage", userBaseInfo.getHeadimage());
				inviteUserMap.put("status", infoModel.getStatus());
				inviteUserMap.put("createAt", infoModel.getCreateAt());
				inviteUserList.add(inviteUserMap);
			}
		}
		dataMap.put("inviteUsers", inviteUserList);
		return dataMap;
	}
	
	@Override
	public void getInviteReward(Integer uid, Integer taskRewardId,Integer forcebuy, ReturnModel returnModel){
		//查询当前用户该阶段奖励是否已领取
		int ishavecount = inviteUserInfoDao.selInviteUserPeckLogByInvitationId(uid, taskRewardId);
		if(ishavecount==0){
			//获得所有奖励列表
			Map<Integer, List<InvitationRewardsConfig>> rewardMap = InviteRewardsConfigLib.getRewardMap();
			//根据任务id获取奖励列表
			List<InvitationRewardsConfig> rewardsConfigs = rewardMap.get(taskRewardId);
			if(rewardsConfigs.size() > 0){
				//根据任务id获取任务详情
				Map<Integer, InvitationConfig> invitationConfigsMap = InviteRewardsConfigLib.getInvitationConfigsMap();
				InvitationConfig invitationConfig = invitationConfigsMap.get(taskRewardId);
				//获取当前用户的任务情况
				InviteUserInfoModel inviteUserInfoModel = inviteUserInfoDao.selInviteUserInfoByUid(uid);
				if(inviteUserInfoModel.getInviteCount() < invitationConfig.getSchedule()){
					returnModel.setCode(CodeContant.inviteRewardIsntGet);
					returnModel.setMessage("该阶段奖励暂时无法领取！");
					return;
				}
				int gets = 0;
				for(InvitationRewardsConfig rewardsConfig : rewardsConfigs){
					int type = rewardsConfig.getType();
					
					if(type==1){ //金币
						int zhutou = new Integer(rewardsConfig.getRewards());
						orderService.insertPayActivity(uid, 2, invitationConfig.getId(), invitationConfig.getName(), zhutou);
						gets = gets+zhutou;
					}else if(type==2){ //礼物
						String[] split = rewardsConfig.getRewards().split(",");
						Integer gid = new Integer(split[0]);
						Integer count = new Integer(split[1]);
						ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(gid);
						userItemService.insertUserItem(uid, giftConfigModel.getGid(), count, ItemSource.inviteReward);
						gets = gets+giftConfigModel.getGprice();
					}else if(type==3){ //增值服务
						String[] split = rewardsConfig.getRewards().split(",");
						Integer gid = new Integer(split[0]);
						Integer count = new Integer(split[1]);
						ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(new Integer(gid));
						gets = gets+giftConfigModel.getGprice();

						UserVipInfoModel userVipInfoModel = ValueaddServiceUtil.getVipInfo(uid);
						Date date = new Date();
						Date startdate = new Date();
						if(userVipInfoModel != null){
							long endtime = userVipInfoModel.getEndtime();
							long starttime = userVipInfoModel.getStarttime();
							date = new Date(endtime*1000);
							startdate = new Date(starttime*1000);
						}
						Date newEndtime = DateUtils.getNDaysAfterDate(date, count);
						/**
						 * 加入身份记录 更新缓存
						 */
						if(userVipInfoModel != null){
							if(userVipInfoModel.getGid()==44){
								if(gid==43){
									//作废 不添加至用户的VIP信息中
								}else{
									vipInfoService.updUserVipInfo(uid,new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 0, gid);
									userItemService.insertUserItemLog(uid, gid, count, ItemSource.inviteReward);
								}
							}else{
								if(gid==43){
									vipInfoService.updUserVipInfo(uid,new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 0, gid);
									userItemService.insertUserItemLog(uid, gid, count, ItemSource.inviteReward);
								}else{
									vipInfoService.updUserVipInfo(uid, userVipInfoModel.getStarttime(), userVipInfoModel.getEndtime(), 1, userVipInfoModel.getGid());
									UserVipInfoModel tempUserVipInfoModel = vipInfoService.getUserVipInfoByUid(uid, gid);
									if(tempUserVipInfoModel == null){
										vipInfoService.addUserVipInfo(uid, gid, new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 0);
									}else{
										vipInfoService.updUserVipInfo(uid, new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 0, gid);
									}
									userItemService.insertUserItemLog(uid, gid, count, ItemSource.inviteReward);
								}
							}
						}else{
							UserVipInfoModel newUserVipInfoModel = vipInfoService.getUserVipInfoByUid(uid, gid);
							if(newUserVipInfoModel != null){
								vipInfoService.updUserVipInfo(uid,new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 0, gid);
							}else{
								vipInfoService.addUserVipInfo(uid, gid, new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 0);
							}
							userItemService.insertUserItemLog(uid, gid, count, ItemSource.inviteReward);
						}
						vipInfoService.getUserVipInfo(uid, true);
						gets = gets+giftConfigModel.getGprice();
					}
				}
				inviteUserInfoDao.updInviteGets(uid, gets);
				inviteUserInfoDao.insInviteUserPeckLog(uid, taskRewardId, 1);
				UserAssetModel userAssetModel = userService.getUserAssetByUid(uid, false);
				Map<String,Object> dataMap = new HashMap<String,Object>();
				dataMap.put("surplus", userAssetModel.getMoney());
				dataMap.put("inviteGets", inviteUserInfoModel.getGets()+gets);
				returnModel.setData(dataMap);
				return; 
			}else{
				returnModel.setCode(CodeContant.CONSYSTEMERR);
				returnModel.setMessage("领取失败");
				return;
			}
		}else{
			returnModel.setCode(CodeContant.inviteRewardGetError);
			returnModel.setMessage("请勿重复领取！");
			return;
		}
	}
	
	@Override
	public void getInviteUserReward(Integer uid, Integer inviteUid, Integer getAll, ReturnModel returnModel){
		int inviteZhutou = 30;
		if(getAll==0){
			int rsc = inviteUserInfoDao.updInviteUserRewardStatus(inviteUid, 2);
			if(rsc > 0){
				int zhutou = rsc*inviteZhutou;
				orderService.insertPayActivity(uid, 2, 0, "领取单个邀请人的猪头奖励", zhutou);
				inviteUserInfoDao.updInviteGets(uid, zhutou);
			}
		}else{
			int rsc = inviteUserInfoDao.updInviteUserRewardStatusByInviteUid(uid, 2);	
			if(rsc>0){
				int zhutou = rsc*inviteZhutou;
				orderService.insertPayActivity(uid, 2, 0, "领取单个邀请人的猪头奖励", zhutou);
				inviteUserInfoDao.updInviteGets(uid, zhutou);
			}
		}
		UserAssetModel userAssetModel = userService.getUserAssetByUid(uid, false);
		InviteUserInfoModel inviteUserInfoModel = inviteUserInfoDao.selInviteUserInfoByUid(uid);
		Map<String,Object> dataMap = new HashMap<String,Object>();
		dataMap.put("surplus", userAssetModel.getMoney());
		dataMap.put("inviteGets", inviteUserInfoModel.getGets());
		returnModel.setData(dataMap);
		return;
	}
	
	@Override
	public int insConsumeLottory(Integer uid,Integer activityId,Integer consume,String des){
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuPay, SqlTemplete.SQL_insConsumeLottory, false,uid,activityId,consume,des,System.currentTimeMillis()/1000);
			return executeResult;
		} catch (Exception e) {
			logger.error("<insConsumeLottory->Exception>" + e.toString());
		}
		return -1;
	}
	@Override
	public int insRewardLottory(Integer uid,Integer activityId,Integer reward,String des){
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuPay, SqlTemplete.SQL_insRewardLottory, false,uid,activityId,reward,des,System.currentTimeMillis()/1000);
			return executeResult;
		} catch (Exception e) {
			logger.error("<insRewardLottory->Exception>" + e.toString());
		}
		return -1;
	}
	
	@Override
	public List<Map<String, Object>> getRewardLottoryList(int activityId) {

		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		List<Map<String, Object>> rewardLottoryList = new ArrayList<Map<String, Object>>();
		try {
			// 获取用户基本信息
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuPay).getConnection();
			statement = conn
					.prepareStatement(SqlTemplete.SQL_getRewardLottoryList);
			DBHelper.setPreparedStatementParam(statement, activityId);
			rs = statement.executeQuery();

			if (rs != null) {
				while (rs.next()) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("reward", rs.getString("reward"));
					map.put("uid", rs.getInt("uid"));
					map.put("addtime", rs.getInt("addtime"));
					rewardLottoryList.add(map);
				}
			}
		} catch (SQLException e) {
			logger.error("<getRewardLottoryList->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getRewardLottoryList->Exception>" + e.getMessage());
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
			} catch (Exception e2) {
				logger.error("<getRewardLottoryList->finally->Exception>" + e2.getMessage());
			}
		}
		return rewardLottoryList;
	}
}
