package com.mpig.api.async.task;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.mpig.api.async.IAsyncTask;
import com.mpig.api.model.PKRecordModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.modelcomet.PKResultCMod;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.service.IPkService;
import com.mpig.api.service.IUserService;
import com.mpig.api.utils.Constant;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.JsonUtil;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.SpringContextUtil;

/**
 * PK结果计算
 */
public class PKResultTask implements IAsyncTask {

	private static Logger log = Logger.getLogger(PKResultTask.class);

	private IPkService pkService = SpringContextUtil.getBean("pkServiceImpl");
	private IUserService userService = SpringContextUtil.getBean("userServiceImpl");
	
	
	@Override
	public void runAsync() {
		while (true) {
			try {
				Thread.sleep(1000);
				Map<String,String>  pkMap = OtherRedisService.getInstance().getAllPKInfo();
				if(pkMap!=null) {
					for (Entry<String, String> entry : pkMap.entrySet()) {
						PKRecordModel pkRecordModel = JsonUtil.toBean(entry.getValue(),PKRecordModel.class);
						long pkttl = (DateUtils.getCurrentTimeMillis()/ 1000) - pkRecordModel.getCreateTime()/1000;
						log.info("PK时间:"+pkRecordModel.getPkTime()+",剩余时间:"+(pkRecordModel.getPkTime()-pkttl)+","+entry.getValue());
						UserBaseInfoModel firstUserBaseInfoModel = userService.getUserbaseInfoByUid(pkRecordModel.getFirstUid(), false);
						if(pkttl>=pkRecordModel.getPkTime()||firstUserBaseInfoModel.getLianmaiStatus()==Constant.lianmai_status_0) {
							Double firstUserVotesScore = OtherRedisService.getInstance().zscore(RedisContant.RedisNameOther, RedisContant.pkAnchorCharm, pkRecordModel.getFirstUid().toString()); 
							Double secodUserVotesScore = OtherRedisService.getInstance().zscore(RedisContant.RedisNameOther, RedisContant.pkAnchorCharm, pkRecordModel.getSecodUid().toString());
							Long firstUserVotes = firstUserVotesScore==null?0L:firstUserVotesScore.longValue();
							Long secodUserVotes = secodUserVotesScore==null?0L:secodUserVotesScore.longValue();
							Integer winnerUid =  firstUserVotes.longValue()>= secodUserVotes.longValue()?pkRecordModel.getFirstUid():pkRecordModel.getSecodUid(); 
							int i = pkService.updatePkRecord(firstUserVotes,secodUserVotes,winnerUid,pkRecordModel.getId());
							if(i>0) {
								
								pkRecordModel.setWinnerUid(winnerUid);
								pkRecordModel.setFirstUserVotes(firstUserVotes);
								pkRecordModel.setSecodUserVotes(secodUserVotes);
								
								OtherRedisService.getInstance().delPkAnchorRel(pkRecordModel.getFirstUid(),pkRecordModel.getSecodUid());
								OtherRedisService.getInstance().zrem(RedisContant.RedisNameOther, RedisContant.pkAnchorCharm,String.valueOf(pkRecordModel.getFirstUid()),String.valueOf(pkRecordModel.getSecodUid()));
								OtherRedisService.getInstance().delPKInfoById(pkRecordModel.getFirstUid());
								
								if(firstUserBaseInfoModel.getLianmaiStatus()==Constant.lianmai_status_0) {
									log.info("主播已结束连麦");
								}else {
									if( pkRecordModel.getPenaltyTime() == 0) {
										log.info("惩罚时间为0,或者主播已结束连麦");
									}else {
										OtherRedisService.getInstance().set(RedisContant.RedisNameOther, RedisContant.pkPenaltyTimeInfo+pkRecordModel.getFirstUid(), 
												JsonUtil.toJson(pkRecordModel), pkRecordModel.getPenaltyTime());
										OtherRedisService.getInstance().set(RedisContant.RedisNameOther, RedisContant.pkPenaltyTimeInfo+pkRecordModel.getSecodUid(), 
												JsonUtil.toJson(pkRecordModel), pkRecordModel.getPenaltyTime());
									}
									Integer penaltyTime = pkRecordModel.getPenaltyTime();
									PKResultCMod msgBody = new PKResultCMod();
									msgBody.setPenaltyTime(penaltyTime);
									msgBody.setWinnerUid(winnerUid);
									pkService.sendRoomMessage(pkRecordModel.getFirstUid(), msgBody);
									pkService.sendRoomMessage(pkRecordModel.getSecodUid(), msgBody);
								}
								
							}
						}
					}
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
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
		return "PKResultTask";
	}

}