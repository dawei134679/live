package com.mpig.api.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mpig.api.SqlTemplete;
import com.mpig.api.db.DBHelper;
import com.mpig.api.dictionary.lib.UrlConfigLib;
import com.mpig.api.model.PKRecordModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.modelcomet.BaseCMod;
import com.mpig.api.modelcomet.PKInviteCMod;
import com.mpig.api.modelcomet.PKInviteResultCMod;
import com.mpig.api.modelcomet.PKStartCMod;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.service.IConfigService;
import com.mpig.api.service.ILiveService;
import com.mpig.api.service.IPkService;
import com.mpig.api.service.IUserService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.Constant;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.EncryptUtils;
import com.mpig.api.utils.JsonUtil;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.VarConfigUtils;

@Service
public class PkServiceImpl implements IPkService,SqlTemplete{
	
	@Resource
	private IUserService userService;
	
	@Resource
	private IConfigService configService;
	
	@Resource
	private ILiveService liveService;
	
	private static Logger logger = Logger.getLogger(PkServiceImpl.class);

	@Override
	public int insertPkRecord(Integer uid,Integer dstuid, Integer pkTime,Integer penaltyTime,Long firstUserVotes,Long secodUserVotes,Integer vinnerUid,Long createTime) {
		try {
			return DBHelper.execute("zhu_game",SQL_insPkRecord, true,uid,dstuid,pkTime,penaltyTime,firstUserVotes,secodUserVotes,vinnerUid,createTime);
		} catch (Exception e) {
			logger.error("<insertPkRecord->Exception>",e);
		}
		return 0;
	}

	@Override
	public int updatePkRecord(Long firstUserVotes,Long secodUserVotes,Integer vinnerUid,Integer id) {
		try {
			return DBHelper.execute("zhu_game",SQL_updPkRecord, false,firstUserVotes,secodUserVotes,vinnerUid,DateUtils.getCurrentTimeMillis(),id);
		} catch (Exception e) {
			logger.error("<updatePkRecord->Exception>",e);
		}
		return 0;
	}
	
	@Override
	public void pkInquireInvitePK(int uid, int dstuid, ReturnModel returnModel) {
		if(uid == dstuid) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("小主，不能与自己pk哦~");
			return ;
		}
	
		UserBaseInfoModel uDst = userService.getUserbaseInfoByUid(dstuid, false);
		if (null == uDst) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("地球查无此人，ID可能在外星...");
			return ;
		}
		
		if(false == uDst.getLiveStatus()) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("对方暂未上线，待会儿再来~");
			return ;
		}
		
		if(uDst.getGameStatus() == 1) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("对方正在游戏直播，不能打扰哦~");
			return ;
		}
		
		if(uDst.getLianmaiStatus() == 1) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("对方正在连麦中，待会儿再来~");
			return ;
		}
		
		if(OtherRedisService.getInstance().existInviteInfo(dstuid)) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("对方正在连麦中，待会儿再来~");
			return ;
		}
		
		if(OtherRedisService.getInstance().existInviteInfo(uid)) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("小主，自己正在连麦邀请中~");
			return ;
		}
		
		if(OtherRedisService.getInstance().existsPkAnchorById(uid,dstuid)) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("对方正在连麦中，待会儿再来~");
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		
		Map<String, Object> userInfoMap = new HashMap<String, Object>();
		userInfoMap.put("uid", uDst.getUid());
		userInfoMap.put("nickname", uDst.getNickname());
		userInfoMap.put("headimage", uDst.getHeadimage());
		map.put("userInfo", userInfoMap);
		Map<String, Object> pkTimeMap = new HashMap<String, Object>();
		pkTimeMap.put("pkTimeList", Constant.pk_time_list.split(","));
		pkTimeMap.put("pkPenaltyTimeList", Constant.pk_penalty_time_list.split(","));
		map.put("pkTimeInfo", pkTimeMap);
		
		returnModel.setData(map);
	}

	@Override
	public void prepareInvitePk(int uid, int dstuid, int pktime, int penaltytime, int istatus,ReturnModel returnModel) {
		if(istatus==1) {
			if(OtherRedisService.getInstance().existInviteInfo(dstuid)) {
				returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
				returnModel.setMessage("对方正在连麦中，待会儿再来~");
				return;
			}
			if(OtherRedisService.getInstance().existInviteInfo(uid)) {
				returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
				returnModel.setMessage("小主，自己正在连麦邀请中~");
				return ;
			}
			if(OtherRedisService.getInstance().existsPkAnchorById(uid,dstuid)) {
				returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
				returnModel.setMessage("对方正在连麦中，待会儿再来~");
				return;
			}
			OtherRedisService.getInstance().setInviteInfo(uid, dstuid);
			UserBaseInfoModel  userBase = userService.getUserbaseInfoByUid(uid, false);
			PKInviteCMod msgBody = new PKInviteCMod();
			msgBody.setUid(uid);
			msgBody.setInviteStatus(1);
			msgBody.setAvatar(userBase.getHeadimage());
			msgBody.setNickname(userBase.getNickname());
			msgBody.setSex(userBase.getSex());
			msgBody.setLevel(userBase.getAnchorLevel());
			msgBody.setPkTime(pktime);
			msgBody.setPenaltyTime(penaltytime);
			sendMessage(dstuid, msgBody);
		}else {
			if(OtherRedisService.getInstance().existInviteInfo(dstuid)) {
				OtherRedisService.getInstance().delInviteInfo(uid, dstuid);
			}
			UserBaseInfoModel  userBase = userService.getUserbaseInfoByUid(uid, false);
			PKInviteCMod msgBody = new PKInviteCMod();
			msgBody.setUid(uid);
			msgBody.setInviteStatus(0);
			msgBody.setAvatar(userBase.getHeadimage());
			msgBody.setNickname(userBase.getNickname());
			msgBody.setSex(userBase.getSex());
			msgBody.setLevel(userBase.getAnchorLevel());
			msgBody.setPkTime(pktime);
			msgBody.setPenaltyTime(penaltytime);
			sendMessage(dstuid, msgBody);
		}
	}

	@Override
	public void confirmInvitePk(int anchoruid, int dstuid,int pktime,int penaltytime, int cstatus, ReturnModel returnModel) {
		if(cstatus==0) {
			if(OtherRedisService.getInstance().existInviteInfo(dstuid)) {
				OtherRedisService.getInstance().delInviteInfo(anchoruid, dstuid);
			}
			PKInviteResultCMod msgBody = new PKInviteResultCMod();
			msgBody.setInviteResultStatus(0);
			sendRoomMessage(anchoruid, msgBody);
			return;
		}
		
		UserBaseInfoModel firstUserBaseInfo = userService.getUserbaseInfoByUid(anchoruid, false);
		if(!firstUserBaseInfo.getLiveStatus()) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("连麦主播已下播");
			return;
		}
		//更新用户PK状态
		boolean updRes = userService.updUserBasePKStatusById(anchoruid,dstuid,Constant.lianmai_status_1);
		if(!updRes) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("连麦失败，再试一次吧~");
			return;
		}
		
		String firstUserstream = configService.getThirdStream(anchoruid);
		if (null == firstUserstream) {
			firstUserstream = (String) liveService.getVideoConfig(0, anchoruid, firstUserBaseInfo.getVideoline()).get("domain");
		} 
		PKInviteResultCMod firstMsgBody = new PKInviteResultCMod();
		firstMsgBody.setAnchorId(anchoruid);
		firstMsgBody.setInviteResultStatus(1);
		firstMsgBody.setDomain(firstUserstream);
		
		sendRoomMessage(dstuid, firstMsgBody);
		
		UserBaseInfoModel secodUserBaseInfo = userService.getUserbaseInfoByUid(dstuid, false);
		String secodUserstream = configService.getThirdStream(dstuid);
		if (null == secodUserstream) {
			secodUserstream = (String) liveService.getVideoConfig(0, dstuid, secodUserBaseInfo.getVideoline()).get("domain");
		} 
		
		PKInviteResultCMod secodMsgBody = new PKInviteResultCMod();
		secodMsgBody.setAnchorId(dstuid);
		secodMsgBody.setInviteResultStatus(1);
		secodMsgBody.setDomain(secodUserstream);
		
		sendRoomMessage(anchoruid, secodMsgBody);
		
		startPK(anchoruid, dstuid, pktime, penaltytime, returnModel);
	}
	
	public void startPK(int anchoruid, int dstuid,int pktime,int penaltytime, ReturnModel returnModel) {
		PKRecordModel pkRecord = new PKRecordModel();
		pkRecord.setFirstUid(anchoruid);
		pkRecord.setSecodUid(dstuid);
		pkRecord.setPkTime(pktime);
		pkRecord.setPenaltyTime(penaltytime);
		pkRecord.setCreateTime(DateUtils.getCurrentTimeMillis());
				
		int pkRecordId = insertPkRecord(anchoruid,dstuid,pkRecord.getPkTime(),pkRecord.getPenaltyTime(),0L,0L,0,pkRecord.getCreateTime());
		if(0==pkRecordId) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("连麦失败，再试一次吧~   ");
			return;
		}
		pkRecord.setId(pkRecordId);
		//设置PK信息（以发起者ID为key）
		OtherRedisService.getInstance().setPKInfo(anchoruid, JsonUtil.toJson(pkRecord));
		
		//设置pk关系
		OtherRedisService.getInstance().setPkAnchorRel(anchoruid, dstuid);
		
		UserBaseInfoModel firstUserBaseInfo = userService.getUserbaseInfoByUid(anchoruid, false);
		//副主播房间消息
		PKStartCMod firstMsgBody = new PKStartCMod();
		firstMsgBody.setAnchorId(anchoruid);
		firstMsgBody.setHeadimage(firstUserBaseInfo.getHeadimage());
		firstMsgBody.setPkTime(pktime);
		firstMsgBody.setStartTime(pkRecord.getCreateTime());
		sendRoomMessage(dstuid, firstMsgBody);
		
		//主播房间消息(发起者)
		UserBaseInfoModel secodUserBaseInfo = userService.getUserbaseInfoByUid(dstuid, false);
		PKStartCMod msgBody = new PKStartCMod();
		msgBody.setAnchorId(dstuid);
		msgBody.setHeadimage(secodUserBaseInfo.getHeadimage());
		msgBody.setPkTime(pktime);
		msgBody.setStartTime(pkRecord.getCreateTime());
		sendRoomMessage(anchoruid, msgBody);
		
	}
	
	@Override
	public void getPKInfo(Integer anchoruid, ReturnModel returnModel) {
		Map<String, Object> map = new HashMap<String, Object>();
		String srcUid = OtherRedisService.getInstance().getPkAnchorRel(anchoruid);
		if(StringUtils.isNotBlank(srcUid)) {
			String pkRecordStr = OtherRedisService.getInstance().getPKInfo(anchoruid, Integer.valueOf(srcUid));
			if(StringUtils.isNotBlank(pkRecordStr)) {
				PKRecordModel pkRecord = JsonUtil.toBean(pkRecordStr, PKRecordModel.class);
				Double firstUserVotesScore = OtherRedisService.getInstance().zscore(RedisContant.RedisNameOther, RedisContant.pkAnchorCharm, pkRecord.getFirstUid().toString()); 
				Double secodUserVotesScore = OtherRedisService.getInstance().zscore(RedisContant.RedisNameOther, RedisContant.pkAnchorCharm, pkRecord.getSecodUid().toString());
				Long firstUserVotes = firstUserVotesScore==null?0L:firstUserVotesScore.longValue();
				Long secodUserVotes = secodUserVotesScore==null?0L:secodUserVotesScore.longValue();
				map.put("firstUserVotes", firstUserVotes);
				map.put("secodUserVotes", secodUserVotes);
				map.put("pkTime", pkRecord.getPkTime());
				map.put("startTime", pkRecord.getCreateTime());
				map.put("endTime", 0);
				Long remainTime = pkRecord.getCreateTime()/1000+Long.valueOf(pkRecord.getPkTime())-DateUtils.getCurrentTimeMillis()/1000;
				map.put("remainTime", remainTime);
				map.put("penaltyTime", pkRecord.getPenaltyTime());
				map.put("firstUid", pkRecord.getFirstUid());
				String firstHeadimage = userService.getUserbaseInfoByUid(pkRecord.getFirstUid(), false).getHeadimage();
				map.put("firstHeadimage", firstHeadimage);
				map.put("secodUid", pkRecord.getSecodUid());
				String secodHeadimage = userService.getUserbaseInfoByUid(pkRecord.getSecodUid(), false).getHeadimage();
				map.put("secodHeadimage", secodHeadimage);
				map.put("winnerUid", 0);
				returnModel.setMessage("正在PK中");
				returnModel.setData(map);
				return;
			}
		}
		String penaltyRecordStr = OtherRedisService.getInstance().get(RedisContant.RedisNameOther, RedisContant.pkPenaltyTimeInfo+anchoruid);
		if(StringUtils.isNotBlank(penaltyRecordStr)) {
			PKRecordModel penaltyRecord = JsonUtil.toBean(penaltyRecordStr, PKRecordModel.class);
			map.put("firstUserVotes", penaltyRecord.getFirstUserVotes());
			map.put("secodUserVotes", penaltyRecord.getSecodUserVotes());
			map.put("pkTime", penaltyRecord.getPkTime());
			map.put("startTime", penaltyRecord.getCreateTime());
			map.put("endTime", penaltyRecord.getUpdateTIme());
			map.put("penaltyTime", penaltyRecord.getPenaltyTime());
			Long remainTime = penaltyRecord.getCreateTime()/1000+Long.valueOf(penaltyRecord.getPkTime())+Long.valueOf(penaltyRecord.getPenaltyTime())-DateUtils.getCurrentTimeMillis()/1000;
			map.put("remainTime", remainTime);
			map.put("firstUid", penaltyRecord.getFirstUid());
			String firstHeadimage = userService.getUserbaseInfoByUid(penaltyRecord.getFirstUid(), false).getHeadimage();
			map.put("firstHeadimage", firstHeadimage);
			map.put("secodUid", penaltyRecord.getSecodUid());
			String secodHeadimage = userService.getUserbaseInfoByUid(penaltyRecord.getSecodUid(), false).getHeadimage();
			map.put("secodHeadimage", secodHeadimage);
			map.put("winnerUid", penaltyRecord.getWinnerUid());
			returnModel.setMessage("pk结束，惩罚时间");
			returnModel.setData(map);
			return;
		}
		//前端要求不能为空
		map.put("firstUserVotes", 0);
		map.put("secodUserVotes", 0);
		map.put("pkTime", 0);
		map.put("startTime", 0);
		map.put("endTime", 0);
		map.put("penaltyTime", 0);
		map.put("firstUid", 0);
		map.put("firstHeadimage", "");
		map.put("secodHeadimage", "");
		map.put("winnerUid", 0);
		returnModel.setData(map);
		returnModel.setMessage("未PK或已结束");
	}
	
	@Override
	public void sendMessage(Integer uid,BaseCMod baseCMod) {
		try {
			String msgbody = JSONObject.toJSONString(baseCMod);
			String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
					"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + msgbody,"users=" + uid);
			Future<HttpResponse<JsonNode>> asJsonAsync = Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_live()).field("appKey", VarConfigUtils.ServiceKey)
					.field("msgBody", msgbody).field("users",uid).field("sign", signParams).asJsonAsync();	
			if (asJsonAsync != null) {
				if (asJsonAsync.get().getStatus() == 200) {
					logger.info(String.format("%s%s", "消息推送成功,sign:", signParams));
				} else {
					logger.info(String.format("%s%s%s%s", "消息推送失败,sign:", signParams, ",status:",asJsonAsync.get().getStatus()));
				}
			} else {
				logger.info("IM服务器返回null,signParams:" + signParams);
			}
		} catch (Exception e) {
			logger.error("用戶PK消息异常",e);
		}
	}
	
	@Override
	public void sendRoomMessage(Integer roomOwnerUid,BaseCMod baseCMod) {
		try {
			String msgbody = JSONObject.toJSONString(baseCMod);
			String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret, "appKey=" + VarConfigUtils.ServiceKey,
					"msgBody=" + msgbody, "roomOwner=" + roomOwnerUid);
			Future<HttpResponse<JsonNode>> asJsonAsync = Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room()).field("appKey", VarConfigUtils.ServiceKey)
					.field("msgBody", msgbody).field("roomOwner", roomOwnerUid)
					.field("sign", signParams).asJsonAsync();
			if (asJsonAsync != null) {
				if (asJsonAsync.get().getStatus() == 200) {
					logger.info(String.format("%s%s", "消息推送成功,sign:", signParams));
				} else {
					logger.info(String.format("%s%s%s%s", "消息推送失败,sign:", signParams, ",status:",asJsonAsync.get().getStatus()));
				}
			} else {
				logger.info("IM服务器返回null,signParams:" + signParams);
			}
		} catch (Exception e) {
			logger.error("发送房间PK消息异常",e);
		}
	}
}