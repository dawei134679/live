package com.mpig.api.async.task;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.mpig.api.async.IAsyncTask;
import com.mpig.api.dao.impl.UserVipInfoDaoImpl;
import com.mpig.api.dictionary.ActGiftConfig;
import com.mpig.api.dictionary.FirstPayConfig;
import com.mpig.api.dictionary.lib.ActivityConfigLib;
import com.mpig.api.model.UserVipInfoModel;
import com.mpig.api.redis.service.RedisCommService;
import com.mpig.api.redis.service.UserRedisService;
import com.mpig.api.service.IUserItemService.ItemSource;
import com.mpig.api.service.impl.OrderServiceImpl;
import com.mpig.api.service.impl.UserItemServiceImpl;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.ValueaddServiceUtil;

public class AsyncTask {

	/**
	 * 充值活动
	 * 
	 * @author fangwuqing
	 *
	 */
	public static class PayActAsyncTask implements IAsyncTask {

		private int uid;
		private double money;

		public PayActAsyncTask(final Integer uid, double money) {
			this.money = money;
			this.uid = uid;
		}

		@Override
		public void runAsync() {
			// 首付活动
			/*
			if (OrderServiceImpl.getInstance().checkFirst(uid)) {
				List<FirstPayConfig> firstPay = ActivityConfigLib.getFirstPay();
				if (firstPay != null && firstPay.size() > 0) {

					for (FirstPayConfig firstPayConfig : firstPay) {
						if (firstPayConfig.getStart() <= money) {

							if (firstPayConfig.getEnd() == 0 || firstPayConfig.getEnd() > money) {
								// 无限制
								List<ActGiftConfig> configs = firstPayConfig.getConfigs();
								for (ActGiftConfig actGift : configs) {
									//buy vip
									if(actGift.getGid()==43){
										int days = actGift.getTimes();
										Date date = new Date();
										Date startdate = new Date();
										UserVipInfoModel userVipInfoModel = ValueaddServiceUtil.getVipInfo(uid);
										if(userVipInfoModel != null){
											if(userVipInfoModel.getGid() ==actGift.getGid()){
												long endtime = userVipInfoModel.getEndtime();
												long starttime = userVipInfoModel.getStarttime();
												date = new Date(endtime*1000);
												startdate = new Date(starttime*1000);
											}
										}
										Date newEndtime = DateUtils.getNDaysAfterDate(date, days);
										UserVipInfoDaoImpl userVipInfoDaoImpl = new UserVipInfoDaoImpl();
										userVipInfoModel = userVipInfoDaoImpl.getUserVipInfoByUid(uid, actGift.getGid());
										int rsc = 0;
										if(userVipInfoModel != null){
											rsc = userVipInfoDaoImpl.updUserVipInfo(uid,new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 0,actGift.getGid());
										}else{
											rsc = userVipInfoDaoImpl.addUserVipInfo(uid, actGift.getGid(),new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 0);
										}
										if(rsc > 0){
											getUserVipInfo(uid, true);
										}
									}else{
										UserItemServiceImpl.insertUserItemStatic(uid, actGift.getGid(), actGift.getNum(),ItemSource.Activity);
									}
								}
							}
						}
					}
				}
			}
			
			Long lgNow = System.currentTimeMillis()/1000;
			//2016-11-05 开始
			if (lgNow >= 1478275200 || uid == 10000048) {
				
				String dateFormat = DateUtils.dateFormat(new Date(), "yyyyMMdd");
				int secondeToNextDay = DateUtils.getSecondeToNextDay();
				
				String actkey = RedisContant.payDayAct + ":" + dateFormat + ":" + uid;
				String string = RedisCommService.getInstance().get(RedisContant.RedisNameUser, actkey);
				
				String drawkey = RedisContant.payDayActDraw + ":" + dateFormat + ":" + uid;
				RedisCommService.getInstance().get(RedisContant.RedisNameUser, drawkey);
				
				if (StringUtils.isEmpty(string)) {
					if (money >= 6 && money < 98) {

						UserItemServiceImpl.insertUserItemStatic(uid, 24, 10,ItemSource.Activity);
						UserItemServiceImpl.insertUserItemStatic(uid, 1, 5,ItemSource.Activity);
						RedisCommService.getInstance().set(RedisContant.RedisNameUser, actkey,"ok",secondeToNextDay);
						RedisCommService.getInstance().incrBy(RedisContant.RedisNameUser, drawkey, 1, secondeToNextDay);
					}else if (money >= 98 && money < 298) {
						
						UserItemServiceImpl.insertUserItemStatic(uid, 24, 20,ItemSource.Activity);
						UserItemServiceImpl.insertUserItemStatic(uid, 1, 10,ItemSource.Activity);
						UserItemServiceImpl.insertUserItemStatic(uid, 23, 1,ItemSource.Activity);
						RedisCommService.getInstance().set(RedisContant.RedisNameUser, actkey,"ok",secondeToNextDay);
						RedisCommService.getInstance().incrBy(RedisContant.RedisNameUser, drawkey, 2, secondeToNextDay);
					}else if (money >= 298 && money < 588) {
						
						UserItemServiceImpl.insertUserItemStatic(uid, 24, 30,ItemSource.Activity);
						UserItemServiceImpl.insertUserItemStatic(uid, 1, 15,ItemSource.Activity);
						UserItemServiceImpl.insertUserItemStatic(uid, 23, 2,ItemSource.Activity);
						RedisCommService.getInstance().set(RedisContant.RedisNameUser, actkey,"ok",secondeToNextDay);
						RedisCommService.getInstance().incrBy(RedisContant.RedisNameUser, drawkey, 6, secondeToNextDay);
					}else if (money >= 588) {
						
						UserItemServiceImpl.insertUserItemStatic(uid, 24, 50,ItemSource.Activity);
						UserItemServiceImpl.insertUserItemStatic(uid, 1, 25,ItemSource.Activity);
						UserItemServiceImpl.insertUserItemStatic(uid, 23, 3,ItemSource.Activity);
						RedisCommService.getInstance().set(RedisContant.RedisNameUser, actkey,"ok",secondeToNextDay);
						RedisCommService.getInstance().incrBy(RedisContant.RedisNameUser, drawkey, 12, secondeToNextDay);
					}
				}
			}
			*/
		}

		public UserVipInfoModel getUserVipInfo(int uid, Boolean directReadMysql) {
			UserVipInfoDaoImpl userVipInfoDaoImpl = new UserVipInfoDaoImpl();
			UserVipInfoModel userVipInfoModel = null;
			String userVipKey = RedisContant.userVip+uid;
			if (!directReadMysql) {
				String userVipStr = UserRedisService.getInstance().get(userVipKey);
				if(StringUtils.isNotEmpty(userVipStr)){
					userVipInfoModel = (UserVipInfoModel) JSONObject.parseObject(userVipStr, UserVipInfoModel.class);
				}
			}
			if(userVipInfoModel == null){
				userVipInfoModel = userVipInfoDaoImpl.getUserVipInfo(uid);
				if(userVipInfoModel!=null){
					UserRedisService.getInstance().set(userVipKey, JSONObject.toJSONString(userVipInfoModel));
					long nowtime = System.currentTimeMillis() / 1000;
					int endtime = userVipInfoModel.getEndtime();
					if (nowtime <= endtime) {
						long expiretime = endtime - nowtime;
						UserRedisService.getInstance().expire(userVipKey, (int) expiretime);
					}
				}
			}
			return userVipInfoModel;
		}
		@Override
		public void afterOk() {

		}

		@Override
		public void afterError(Exception e) {

		}

		@Override
		public String getName() {
			return "PayActAsyncTask";
		}

	}
}
