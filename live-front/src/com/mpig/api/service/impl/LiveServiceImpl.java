package com.mpig.api.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.Unirest;
import com.mpig.api.SqlTemplete;
import com.mpig.api.async.AsyncManager;
import com.mpig.api.async.IAsyncTask;
import com.mpig.api.async.NotificationEngine;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.dictionary.VideoLineConfig;
import com.mpig.api.dictionary.lib.TaskConfigLib;
import com.mpig.api.dictionary.lib.UrlConfigLib;
import com.mpig.api.dictionary.lib.VideoLineConfigLib;
import com.mpig.api.model.BillRedenvelopeModel;
import com.mpig.api.model.LiveMicTimeModel;
import com.mpig.api.model.PayGetRedenvelopModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.UserAssetModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.modelcomet.CModProtocol;
import com.mpig.api.modelcomet.CloseRoomCMod;
import com.mpig.api.modelcomet.ExitRoomCMod;
import com.mpig.api.modelcomet.GiftSendCMod;
import com.mpig.api.modelcomet.InviteCloseCMod;
import com.mpig.api.modelcomet.LiveInviteAckCMod;
import com.mpig.api.modelcomet.LiveInviteCMod;
import com.mpig.api.modelcomet.RunWayCMod;
import com.mpig.api.modelcomet.UpdateIdentityCMod;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.redis.service.RelationRedisService;
import com.mpig.api.redis.service.UserRedisService;
import com.mpig.api.service.IBillService;
import com.mpig.api.service.IConfigService;
import com.mpig.api.service.ILiveService;
import com.mpig.api.service.IPkService;
import com.mpig.api.service.IRedEnvelope;
import com.mpig.api.service.IRoomService;
import com.mpig.api.service.IUserItemService;
import com.mpig.api.service.IUserItemService.ItemSource;
import com.mpig.api.service.IUserService;
import com.mpig.api.task.TaskService;
import com.mpig.api.utils.ChatMessageUtil;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.EncryptUtils;
import com.mpig.api.utils.GameServerUtil;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.VarConfigUtils;
import com.qiniu.pili.QnStreamUtilv1;

@Service
public class LiveServiceImpl implements ILiveService, SqlTemplete {
	private static final Logger logger = Logger.getLogger(LiveServiceImpl.class);
	@Resource
	private IUserService userService;

	@Resource
	private IBillService billService;

	@Resource
	private IRedEnvelope redEnvelope;

	@Resource
	private IRoomService roomService;
	
	@Resource
	private IConfigService configService;

	@Resource
	private IUserItemService userItemService;
	
	@Resource
	private IPkService pkService;
	/**
	 * 用户进入房间获取的主播信息
	 * 
	 * @param uid
	 * @return
	 */
	@Override
	public Map<String, LiveMicTimeModel> getLiveIngByUid(String... uids) {

		List<String> liveInfoList = UserRedisService.getInstance().getLiveInfoList(uids);

		if (liveInfoList != null && liveInfoList.size() > 0) {

			Map<String, LiveMicTimeModel> map = new HashMap<String, LiveMicTimeModel>();

			for (String string : liveInfoList) {

				LiveMicTimeModel liveMicTimeModel = null;
				liveMicTimeModel = (LiveMicTimeModel) JSONObject.parseObject(string, LiveMicTimeModel.class);

				if (liveMicTimeModel != null) {
					map.put(liveMicTimeModel.getUid().toString(), liveMicTimeModel);
				}
			}
			return map;
		}
		return null;
	}

	/**
	 * 主播开播
	 * 
	 * @param uid
	 * @param slogan
	 * @param province
	 * @param city
	 * @return
	 */
	@Override
	public Boolean startLive(String os, Integer uid, String slogan, String province, String city) {
		UserBaseInfoModel userBaseInfoModel = null;
		int res = 0;

		// 当前时间
		Long nowtime = System.currentTimeMillis() / 1000;
		try {
			userBaseInfoModel = userService.getUserbaseInfoByUid(uid, false);
			if (userBaseInfoModel == null) {
				return false;
			}
			// 上次开播未结束
			if (userBaseInfoModel.getLiveStatus()) {
				if(userBaseInfoModel.getGameId()!=null&&userBaseInfoModel.getGameId()!=0) {
					String gameId = String.valueOf(userBaseInfoModel.getGameId());
					OtherRedisService.getInstance().delGameRoomCreditThis(uid, userBaseInfoModel.getGameId());
					GameServerUtil.destoryGame(gameId, uid);
					userService.updUserBaseGameStatusById(uid, 0, 0L);
				}
				Long endtime = 0L;
				// 获取上次开播最后一次心跳时间
				String redisStr = OtherRedisService.getInstance().getRoomEndTime(uid);
				if (redisStr != null && !"".equals(redisStr)) {
					String[] redis = redisStr.split(",");
					endtime = Long.valueOf(redis[1]);

					if ((nowtime - endtime) <= 60) {
						// 1分钟以内 算一次心跳
						OtherRedisService.getInstance().addRoomEndTime(uid, Integer.valueOf(redis[0]), endtime + 60);
						return true;
					} else {
						// 先结束上次开播
						endtime = endtime + 300;
						res = this.updLiveMicTime(uid, endtime.intValue(), true,
								RelationRedisService.getInstance().getUsersInAnchor(uid).intValue(),
								RelationRedisService.getInstance().getEnterRoomTimes(uid),
								RelationRedisService.getInstance().getRoomLikes(uid),
								RelationRedisService.getInstance().getRoomCreditThis(uid), Integer.valueOf(redis[0]));
						if (res == 0) {
							logger.debug("startLive:一个心跳外重新开播 结束上次开播记录失败（live_mic_time）id=" + redis[0]);
						}
					}
				} else {
					LiveMicTimeModel liveMicTimeModel = this.getLiveMicInfoByUid(uid, true);
					if (liveMicTimeModel != null) {
						res = this.updLiveMicTime(uid, liveMicTimeModel.getStarttime() + 300, true,
								RelationRedisService.getInstance().getUsersInAnchor(uid).intValue(),
								RelationRedisService.getInstance().getEnterRoomTimes(uid),
								RelationRedisService.getInstance().getRoomLikes(uid),
								RelationRedisService.getInstance().getRoomCreditThis(uid), liveMicTimeModel.getId());
						if (res == 0) {
							logger.debug("startLive:之前开播状态未结束，但心跳已经不存，结束上次开播记录失败（live_mic_time）id=" + liveMicTimeModel.getId());
						}
					} else {
						res = 1;
					}
				}
			} else {
				// 新开播
				res = userService.updUserBaseInfoLiveStatusByUid(uid, true);
				if (res == 0) {
					logger.debug("startLive：新增开播 修改开播状态失败:" + uid);
				}
			}
			if (res <= 0) {
				return false;
			}
			// 添加麦时记录表
			res = this.insertLiveMicTime(uid,os, slogan, province, city, nowtime, nowtime);
			if (res > 0) {

				// 记录主播开播后心跳时间
				OtherRedisService.getInstance().addRoomEndTime(uid, res, nowtime);

				// 添加开播列表
				if (userBaseInfoModel.getRecommend() == 2 || userBaseInfoModel.getRecommend() == 3) {
					// 推荐 和 头牌
					OtherRedisService.getInstance().addRecommendRoom(uid, userBaseInfoModel.getRecommend(),
							roomService.getRoomShowUsers(uid, userBaseInfoModel.getContrRq()),0);
				}

				if (userBaseInfoModel.getRecommend() == 1 || userBaseInfoModel.getRecommend() == 2 || userBaseInfoModel.getRecommend() == 3) {
					OtherRedisService.getInstance().addhotRoom(uid, userBaseInfoModel.getRecommend(),userBaseInfoModel.getOpentime(),0);
					//手机开播
					if(os.equals("android") || os.equals("ios")){
						OtherRedisService.getInstance().addMobileRoom(uid, userBaseInfoModel.getOpentime());
					}
				} else if (userBaseInfoModel.getRecommend() == 0) {
//					//实名认证时写入 最新列表，放入年月日时间戳。   非实名认证时写入基本的开播列表
//					boolean authen = false;
//					AuthenticationModel authenticationModel = userService.getAuthentication(uid);
//					if(authenticationModel!=null){
//						if(authenticationModel.getAuditStatus()==3){
//							authen = true;
//						}
//					}
//					if(authen){
//						OtherRedisService.getInstance().addhotRoom(uid, userBaseInfoModel.getRecommend(),DateUtils.getDayBegin(),0);
//					}else{
//						OtherRedisService.getInstance().addBaseRoom(uid, userBaseInfoModel.getRecommend(),userBaseInfoModel.getContrRq(),0);
//					}
					OtherRedisService.getInstance().addBaseRoom(uid, userBaseInfoModel.getRecommend(),userBaseInfoModel.getContrRq(),0);
				}
				//删除显示人数记录
				OtherRedisService.getInstance().delRoomUserCounts(uid);
				// 删除真是人数
				RelationRedisService.getInstance().cleanUsersInRedis(uid);
				RelationRedisService.getInstance().cleanRoomRobots(uid);
				// 删除上场喜欢数
				RelationRedisService.getInstance().delRoomLikes(uid);
				RelationRedisService.getInstance().delRoomKick(uid);
				OtherRedisService.getInstance().delSilent(uid);
				RelationRedisService.getInstance().delLiveEnterTotal(uid);
				//清除本场收到的人气
				RelationRedisService.getInstance().delNowLiveRq(uid);
				//清除本场收到的金币数
				RelationRedisService.getInstance().delNowLiveSends(uid);
				// 删除上一场的声援值
				RelationRedisService.getInstance().delRoomCreditThis(uid);

				System.out.println("BroadCastTask is start....");

				try{
					AsyncManager.getInstance().execute(new BroadCastTask(os, userBaseInfoModel.getAnchorLevel(),
							userBaseInfoModel.getNickname(), uid));
				}catch (Exception ex){
					logger.error("BroadCastTask>>>",ex);
				}


				return true;
			} else {
				logger.debug("startLive：添加麦时记录失败:" + uid);
				return false;
			}
		} catch (Exception e) {
			logger.error("<startLive>->Exception",e);
		}
		return false;
	}

	private class BroadCastTask implements IAsyncTask {
		private int uid;
		private int anchorlevel;
		private String nickname;
		private String os;

		public BroadCastTask(String os, int level, String nickname, int uid) {
			this.uid = uid;
			this.anchorlevel = level;
			this.nickname = nickname;
			this.os = os;
		}

		@Override
		public void runAsync() {
			try{
				if (this.uid > 0) {
					Map<String, String> broadcastAnchor = UserRedisService.getInstance().getBroadcastAnchor(os,
							String.valueOf(uid));

					System.out.println("BroadCastTask is start get broadcastAnchor=" + broadcastAnchor.toString());
					if (broadcastAnchor != null) {
						List<String> list = new ArrayList<String>();
						for (Map.Entry<String, String> _map : broadcastAnchor.entrySet()) {
							list.add(_map.getValue());
						}

						if (list.size() > 0) {
							String data = "{\"level\":" + this.anchorlevel + ",\"uid\":" + this.uid + ",\"nickname\":\""
									+ this.nickname + "\"}";
							NotificationEngine.getInstance().sendNotificationListWithDataWhenLiveOn(this.nickname, data, list);
						}
					}

					//开播增加至最新入驻列表
					Double isHave = OtherRedisService.getInstance().getAllNewJoinRoomInfoByUser(uid);
					if(isHave!=null){
						OtherRedisService.getInstance().addNewJoinRoom(uid, isHave.intValue());
					}
				}
			}catch(Exception ex){
				logger.error("BroadCastTask runAsync",ex);
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
			return null;
		}

	}

	/**
	 * 下麦
	 * 
	 * @param uid
	 *            主播UID
	 * @return
	 */
	@Override
	public Boolean exitLive(Integer uid, ReturnModel returnModel) {
		int res = 0;

		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(uid, false);

		//TOSY ADD 2nd Stream
		try{
			if(null != userBaseInfoModel){
				String str2ndUid = OtherRedisService.getInstance().getLive2ndUid(uid);
				if(null != str2ndUid){
					Integer nUid2nd = Integer.valueOf(str2ndUid);
					if(null != nUid2nd){
						UserBaseInfoModel uSrc = userService.getUserbaseInfoByUid(nUid2nd, false);
						if(null != uSrc){
							updateLiveMic(uSrc,userBaseInfoModel,0);
							liveInviteCanncel(uSrc);
						}	
					}
				}
			}
			closeLiveInvite(uid, returnModel);
		}catch(Exception e){
			logger.error("<exitLive updateLiveMic>异常" + e.toString());
		}
		
	
		// 获取开播麦时时间点缓存
		String redisStr = OtherRedisService.getInstance().getRoomEndTime(uid);
		if (redisStr == null || "".equals(redisStr) || userBaseInfoModel == null) {
			logger.info("exitLive first false out redisStr = "+redisStr);
			return false;
		}
		String[] idtime = redisStr.split(",");
		if (redisStr != null && !"".equals(redisStr)) {
			// 获取开播麦时的id和时间
			int micid = Integer.valueOf(idtime[0]);
			try {
				// 下播更新
				res = userService.updUserBaseInfoLiveStatusByUid(uid,false);

				logger.info("exitLive update status sucess res:" + res);
				
				if (res > 0) {
					try {
						if(userBaseInfoModel.getGameId()!=null&&userBaseInfoModel.getGameId()!=0) {
							if(userBaseInfoModel.getGameId()!=null&&userBaseInfoModel.getGameId()!=0) {
								String gameId = String.valueOf(userBaseInfoModel.getGameId());
								OtherRedisService.getInstance().delGameRoomCreditThis(uid, userBaseInfoModel.getGameId());
								GameServerUtil.destoryGame(gameId, uid);
								userService.updUserBaseGameStatusById(uid, 0, 0L);
							}
						}
					}catch (Exception e) {
						logger.error("关闭游戏服务器异常",e);
					}
					// 开始麦时
					int starttime = this.getLiveMicTimeById(micid);
					// 结束麦时
					long endtime = System.currentTimeMillis() / 1000;
					Map<String, Object> map = new HashMap<String, Object>();

					UserAssetModel userAssetModel = userService.getUserAssetByUid(uid, false);

					// TOSY TASK
					long livingSecs = endtime - starttime;
					if (livingSecs > 15 * 60) {
						TaskService.getInstance().taskProcess(uid, TaskConfigLib.Living15Mins, 1);
					}

					
					map.put("timeslong", livingSecs);// 时长
					map.put("persontimes", RelationRedisService.getInstance().getEnterRoomTimes(uid));

					map.put("roomlikes", RelationRedisService.getInstance().getRoomLikes(uid));// 喜欢数
					map.put("creditTotal", userAssetModel.getCreditTotal());


					res = this.updLiveMicTime(uid, (int) endtime, true,
							RelationRedisService.getInstance().getUsersInAnchor(uid).intValue(),
							RelationRedisService.getInstance().getEnterRoomTimes(uid),
							RelationRedisService.getInstance().getRoomLikes(uid),
							RelationRedisService.getInstance().getRoomCreditThis(uid), micid);

					try{
						//record video	在直播结束前，数据库更改后
						createRecord(String.valueOf(uid),starttime,endtime);//sec
					}catch(Exception e){
						logger.error("<qn generate record>异常" + e.toString());
					}
					
					logger.info("exitLive update mic sucess res:" + res);
					if (res > 0) {

						// 获取在房间用户的时长
						// Map<String, String> mapTimes =
						// RelationRedisService.getInstance().getUsersTimeInRoom(uid);
						// if (mapTimes != null && mapTimes.size() > 0) {
						// // 计算用户在该房间的时长，运营可能需要
						// }

						logger.info(
								"exitLive user's recommend is :" + userBaseInfoModel.getRecommend() + " uid=" + uid);
						// 删除开播麦时缓存
						RelationRedisService.getInstance().delEnterRoomTimes(uid);
						// 删除心跳数据
						OtherRedisService.getInstance().delRoomEndTime(uid);
						//获取上一场的真实进房人次
						int liveRealEnterTotal = RelationRedisService.getInstance().getLiveRealEnterTotal(uid);
						//设置下播时房间的人数
						OtherRedisService.getInstance().setRoomUserCounts(uid, (double)liveRealEnterTotal);
						// 删除房间记录的人数
						RelationRedisService.getInstance().delLiveEnterTotal(uid);
						// 删除上一场的声援值
						RelationRedisService.getInstance().delRoomCreditThis(uid);
						//清除本场收到的人气
						RelationRedisService.getInstance().delNowLiveRq(uid);
						//清除本场收到的金币数
						RelationRedisService.getInstance().delNowLiveSends(uid);

						// 删除开播推荐
						if (userBaseInfoModel.getRecommend() >= 2) {
							
							Long delRecommendRoom = OtherRedisService.getInstance().delRecommendRoom(uid);
							logger.info("delRecommendRoom=====" + delRecommendRoom);
							OtherRedisService.getInstance().delHotRoom(uid);
							OtherRedisService.getInstance().delMobileRoom(uid);
							
						} else if (userBaseInfoModel.getRecommend() == 1) {
							OtherRedisService.getInstance().delHotRoom(uid);
							OtherRedisService.getInstance().delMobileRoom(uid);
						} else {
							OtherRedisService.getInstance().delBaseRoom(uid);
							// 普通用户认证后开播 进入最新列表中 
							OtherRedisService.getInstance().delHotRoom(uid);
							OtherRedisService.getInstance().delMobileRoom(uid);
						}
						
						// 删除开播信息
						UserRedisService.getInstance().delLiveInfo(uid);
						returnModel.setData(map);
						
						//下播删除最新入驻
						OtherRedisService.getInstance().delNewJoinRoom(uid);
						return true;
					} else {
						// 系统问题
						logger.debug("<exitLive->err：更新麦时出错>" + redisStr);
					}
				} else {
					// 系统问题
					logger.debug("<exitLive->err：更新下播状态出错>" + redisStr);
				}
			} catch (NumberFormatException e) {
				logger.error("<exitLive->NumberFormatException>" + e.toString());

			} catch (Exception e) {
				e.printStackTrace();
				logger.error("<exitLive->Exception>" + e.toString());
			}
		} else {
			logger.debug("exitLive:下麦失败，没有找到心跳");
		}
		return false;
	}

	private void createRecord(String uid, int starttime, long endtime) {
		//检查redis 是否需要生成record		以及时长是否足够
		if(false == OtherRedisService.getInstance().isUidinRecordSet(uid)){
			return;
		}
		if(QnStreamUtilv1.recLimitSec > endtime - starttime){
			return;
		}
		//title
		String title = null;
		LiveMicTimeModel info = getliveMicInfoLivedByUid(Integer.valueOf(uid),false); 
		if(null != info){
			title = info.getSlogan();	
		}
		//异步调用七牛，生成record	插入redis数据
		AsyncManager.getInstance().execute(
				new QnStreamUtilv1.Qnv1CreateDelRecords(uid,starttime,endtime,title));
	}

	/**
	 * 麦时心跳
	 * 
	 * @param uid
	 * @return
	 */
	@Override
	public Boolean heartBeatLive(Integer uid) {
		String redisStr = OtherRedisService.getInstance().getRoomEndTime(uid);
		if (StringUtils.isNotEmpty(redisStr)) {
			String[] idtime = redisStr.split(",");
			OtherRedisService.getInstance().addRoomEndTime(uid, Integer.valueOf(idtime[0]),
					System.currentTimeMillis() / 1000);
			return true;
		} else {
			logger.debug("heartBeatLive:心跳 找不到缓存");
			return false;
		}
	}

	/**
	 * 获取用户/主播进房间的配置信息 如 Domain 服务器IP 服务器端口
	 * 
	 * @param srcUid
	 *            用户UID
	 * @param dstUid
	 *            主播UID
	 * @param videoline
	 *            线路
	 * @return
	 */
	@Override
	public Map<String, Object> getVideoConfig(final Integer srcUid, final Integer dstUid, Integer videoline) {
		final VideoLineConfig config = srcUid.intValue() == dstUid.intValue()
				? VideoLineConfigLib.getForAdvanced(videoline) : VideoLineConfigLib.getForNormal(videoline);
		if (config != null) {
			return new HashMap<String, Object>() {
				private static final long serialVersionUID = 1L;
				{
					put("domain", config.getDomainPrefix());
					put("pageUrl", config.getPageUrl());
					put("shareUrl", config.getShareUrl());
					put("host", config.getHost());
					put("port", config.getPort());
					put("key", config.getKey());
					//TOSY QINIU KEY 查redis
					if(srcUid.intValue() == dstUid.intValue()){
						String qnKey = OtherRedisService.getInstance().getUidQnKey(String.valueOf(srcUid));
						if(null == qnKey){
							logger.debug("debug qn getkey is not fix key: "+srcUid);
							//qnKey = QnStreamUtilv1.getInstance().updateStreamKeyV1(srcUid.toString(), qnKey,true);
							//从redis获取key
							qnKey = OtherRedisService.getInstance().getUidQnKeyLimitOneDay(String.valueOf(srcUid));
						}
						if(null != qnKey){
							put("key", qnKey);
							logger.debug("debug qn getkey: "+srcUid+" key = "+qnKey);							
						}else{
							logger.error("err qn getkey:"+srcUid+"no key! failed!");
						}
					}
				}
			};
		}
		return null;
	}

	/**
	 * 插入麦时表
	 */
	@Override
	public int insertLiveMicTime(Object... objs) {
		int ires = 0;
		try {
			ires = DBHelper.execute(VarConfigUtils.dbZhuLive, SQL_InsertLiveMicTime, true, objs);
			if (ires >= 1) {
				this.getLiveMicInfoByUid((int) objs[0], true);
			}
		} catch (Exception e) {
			logger.error("<insertLiveMicTime->Exception>" + e.toString());
		}
		return ires;
	}

	/**
	 * 更新麦时信息
	 */
	@Override
	public int updLiveMicTime(Integer uid, Integer endtime, Boolean type, int audience, int views, int likes,
			int credit, Integer id) {
		int ires = 0;
		try {
			ires = DBHelper.execute(VarConfigUtils.dbZhuLive, SQL_UpdLiveMicTimeById, false, endtime, type, audience,
					views, likes, credit, id);
			if (ires > 0) {

				this.getLiveMicInfoByUid(uid, true);
			}
		} catch (Exception e) {
			logger.error("<updLiveMicTime->Exception>" + e.toString());
		}
		return ires;
	}

	/**
	 * 获取麦时信息
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public int getLiveMicTimeById(int id) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		int iStart = 0;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuLive).getConnection();
			statement = conn.prepareStatement(SQL_GetLiveMicTimeById);
			DBHelper.setPreparedStatementParam(statement, id);
			rs = statement.executeQuery();
			while (rs.next()) {
				iStart = rs.getInt("starttime");
				break;
			}
		} catch (Exception e) {
			logger.error("<getLiveMicTimeById->Exception>" + e.toString());
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
				logger.error("<getLiveMicTimeById->finally->Exception>" + e2.toString());
			}
		}
		return iStart;
	}

	/**
	 * 获取该用户开播的最后一条数据（开播未结束的数据）
	 */
	@Override
	public LiveMicTimeModel getLiveMicInfoByUid(Integer uid, Boolean bl) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		LiveMicTimeModel liveMicTimeModel = null;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuLive).getConnection();
			statement = conn.prepareStatement(SQL_GetLiveMicInfoByUid);
			DBHelper.setPreparedStatementParam(statement, uid);
			rs = statement.executeQuery();
			if (rs != null && rs.next()) {

				liveMicTimeModel = new LiveMicTimeModel().populateFromResultSet(rs);
				// 缓存
				UserRedisService.getInstance().setLiveInfo(uid, JSONObject.toJSONString(liveMicTimeModel));

			} else {
				UserRedisService.getInstance().delLiveInfo(uid);
			}

		} catch (Exception e) {
			logger.error("<getLiveMicInfoByUid-exception:>" + e.toString());
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
				logger.error("<getLiveMicInfoByUid-finally-exception:>" + e2.toString());
			}
		}
		return liveMicTimeModel;
	}

	/**
	 * 获取刚结束的主播数据
	 * 
	 * @param uid
	 * @param bl
	 * @return
	 */
	@Override
	public LiveMicTimeModel getliveMicInfoLivedByUid(Integer uid, Boolean bl) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		LiveMicTimeModel liveMicTimeModel = null;
		// if (!bl) {
		// String redisLiveMicInfo =
		// UserRedisService.getInstance().getLiveInfo(uid);
		// if (StringUtils.isNotEmpty(redisLiveMicInfo)) {
		// liveMicTimeModel = (LiveMicTimeModel)
		// JSONObject.parseObject(redisLiveMicInfo, LiveMicTimeModel.class);
		// }
		// }
		if (liveMicTimeModel == null) {
			try {
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuLive).getConnection();
				statement = conn.prepareStatement(SQL_GetLiveMicInfoOpenedByUid);
				DBHelper.setPreparedStatementParam(statement, uid);

				rs = statement.executeQuery();
				while (rs.next()) {
					liveMicTimeModel = new LiveMicTimeModel().populateFromResultSet(rs);
					// 缓存
					// UserRedisService.getInstance().setLiveInfo(uid,
					// JSONObject.toJSONString(liveMicTimeModel));
					break;
				}

			} catch (Exception e) {
				logger.error("<getLiveInfoByUid>" + e.getMessage());
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
					logger.error("<getLiveInfoByUid->finally->exception>:" + e2.getMessage());
				}
			}
		}
		return liveMicTimeModel;
	}

	/**
	 * 设置管理员
	 * 
	 * @param anchorUid
	 *            主播uid
	 * @param userUid
	 *            用户uid
	 * @param type
	 *            ＝on 添加管理员 ＝off 解除管理员
	 * @return ＝1成功 其他失败
	 */
	@Override
	public int setAnchorManage(int anchorUid, int userUid, String type) {
		int ires = 0;

		Connection conn = null;
		PreparedStatement statement = null;

		try {
			if (this.checkLiveManage(anchorUid, userUid)) {
				// 是管理员
				if ("off".equals(type)) {
					// 解除管理员
					conn = DataSource.instance.getPool(VarConfigUtils.dbZhuLive).getConnection();
					statement = conn.prepareStatement(SQL_DelLiveManageByAnchorUser);
					statement.setBoolean(1, false);
					statement.setInt(2, anchorUid);
					statement.setInt(3, userUid);
					ires = statement.executeUpdate();
					if (ires == 1) {
						RelationRedisService.getInstance().delAnchorManage(anchorUid, userUid);
					}
				}
			} else {
				// 不是管理员
				if ("on".equals(type)) {
					// 添加管理员
					conn = DataSource.instance.getPool(VarConfigUtils.dbZhuLive).getConnection();
					statement = conn.prepareStatement(SQL_GETLiveManageByAnchorUser);

					statement.setInt(1, anchorUid);
					statement.setInt(2, userUid);
					ResultSet rs = statement.executeQuery();
					if (rs != null && rs.next()) {
						if (!rs.getBoolean("isvalid")) {
							statement.close();
							statement = conn.prepareStatement(SQL_DelLiveManageByAnchorUser);
							statement.setBoolean(1, true);
							statement.setInt(2, anchorUid);
							statement.setInt(3, userUid);
							ires = statement.executeUpdate();
						}
					} else {
						statement.close();
						statement = conn.prepareStatement(SQL_AddLiveManageByAnchorUser);
						int times = (int) (System.currentTimeMillis() / 1000);

						statement.setInt(1, anchorUid);
						statement.setInt(2, userUid);
						statement.setBoolean(3, true);
						statement.setInt(4, times);
						statement.setInt(5, times);
						ires = statement.executeUpdate();
					}

					if (ires == 1) {
						RelationRedisService.getInstance().setAnchorManage(anchorUid, userUid);
					}
				}
			}
		} catch (Exception e) {
			logger.error("<setAnchorManage->exception>:" + e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception ex) {
				logger.error("<setAnchorManage->finally->exception>:" + ex.getMessage());
			}
		}
		return ires;
	}

	/**
	 * 获取主播房间中的管理员列表
	 * 
	 * @param anchor
	 *            主播uid
	 * @param bl
	 *            true从数据库中读取 false从缓存中读取
	 * @return List<LiveManageModel>
	 */
	@Override
	public Map<String, String> getManagelistOfAnchor(int anchor, boolean bl) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		Map<String, String> map = new HashMap<String, String>();

		if (!bl) {
			map = RelationRedisService.getInstance().getManagelistOfAnchor(anchor);
		}
		if (map == null || map.isEmpty()) {
			try {
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuLive).getConnection();
				statement = conn.prepareStatement(SQL_GetLiveManageListOfAnchor);
				DBHelper.setPreparedStatementParam(statement, anchor);

				rs = statement.executeQuery();
				while (rs.next()) {
					map.put(String.valueOf(rs.getInt("userUid")), "1");
					// 缓存
					RelationRedisService.getInstance().setAnchorManage(anchor, rs.getInt("userUid"));
					break;
				}
			} catch (Exception e) {
				logger.error("<getManagelistOfAnchor>" + e.getMessage());
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
					logger.error("<getManagelistOfAnchor->finally->exception>:" + e2.getMessage());
				}
			}
		}
		return map;
	}

	/**
	 * 判断用户是否未主播的管理员
	 * 
	 * @param anchorUid
	 *            主播uid
	 * @param userUid
	 *            用户uid
	 * @return true 是管理员 false 不是管理员
	 */
	@Override
	public boolean checkLiveManage(int anchorUid, int userUid) {
		if (RelationRedisService.getInstance().checkManageByAnchorUser(anchorUid, userUid)) {
			// 缓存存在
			return true;
		} else {
			// 缓存不存在 则查数据库
			Connection conn = null;
			PreparedStatement statement = null;
			ResultSet rs = null;
			try {
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuLive).getConnection();
				statement = conn.prepareStatement(SQL_GetLiveManageByAnchorUser);
				statement.setInt(1, anchorUid);
				statement.setInt(2, userUid);

				rs = statement.executeQuery();
				while (rs.next()) {
					if (rs.getBoolean("isvalid")) {
						// 添加缓存
						RelationRedisService.getInstance().setAnchorManage(anchorUid, userUid);
						return true;
					} else {
						return false;
					}
				}
			} catch (Exception e) {
				logger.error("<getLiveInfoByUid>" + e.getMessage());
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
					logger.error("<getLiveInfoByUid->finally->exception>:" + e2.getMessage());
				}
			}
			return false;
		}
	}

	@Override
	public void closeRoom(int manageid, int anchoruid, String cause, ReturnModel returnModel) {
		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(manageid, false);
		if (userBaseInfoModel == null) {
			returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
			returnModel.setMessage("帐号异常");
		} else if (userBaseInfoModel.getIdentity() != 3) {
			returnModel.setCode(CodeContant.BaseInfo_identity);
			returnModel.setMessage("该帐号权限不够");
		} else {
			Connection conn = null;
			PreparedStatement statement = null;
			try {

				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
				statement = conn.prepareStatement(com.mpig.api.utils.StringUtils.getSqlString(SQL_UpdateUserIdentity,
						"user_base_info_", anchoruid));
				statement.setInt(1, 2);
				statement.setInt(2, anchoruid);
				// 修改用户identity
				int ires = statement.executeUpdate();
				if (ires == 1) {
					userService.getUserbaseInfoByUid(anchoruid, true);
				}
				if (statement != null) {
					statement.close();
				}

				// 插入日志
				String sql = "insert into user_handle(uid,handle,cause,source,adminid,creatAt)value(?,?,?,?,?,?)";
				statement = conn.prepareStatement(sql);
				statement.setInt(1, anchoruid);
				statement.setInt(2, 1);
				statement.setString(3, cause);
				statement.setInt(4, 1);
				statement.setInt(5, manageid);
				statement.setInt(6, (int) (System.currentTimeMillis() / 1000));
				statement.executeUpdate();

				UserBaseInfoModel anchorInfo = userService.getUserbaseInfoByUid(anchoruid, false);
				if (anchorInfo == null) {
					returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
					returnModel.setMessage("帐号异常");
				} else if (anchorInfo.getLiveStatus()) {
					// 开播
					if (this.exitLive(anchoruid, returnModel)) {

						@SuppressWarnings("unchecked")
						Map<String, Object> map = (Map<String, Object>) returnModel.getData();
						ExitRoomCMod exitRoomCMod = new ExitRoomCMod();
						exitRoomCMod.setAvatar(anchorInfo.getHeadimage());
						exitRoomCMod.setLevel(anchorInfo.getUserLevel());
						exitRoomCMod.setNickname(anchorInfo.getNickname());
						exitRoomCMod.setSex(anchorInfo.getSex());
						exitRoomCMod.setUid(anchoruid);
						exitRoomCMod.setCreditTotal(Integer.valueOf(map.get("creditTotal").toString()));
						exitRoomCMod.setPersontimes(Integer.valueOf(map.get("persontimes").toString()));
						exitRoomCMod.setRoomLikes(Integer.valueOf(map.get("roomlikes").toString()));
						exitRoomCMod.setTimeslong(Long.valueOf(map.get("timeslong").toString()));

						String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
								"appKey=" + VarConfigUtils.ServiceKey,
								"msgBody=" + JSONObject.toJSONString(exitRoomCMod), "roomOwner=" + anchoruid);

						Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room())
								.field("appKey", VarConfigUtils.ServiceKey)
								.field("msgBody", JSONObject.toJSONString(exitRoomCMod))
								.field("roomOwner", String.valueOf(anchoruid)).field("sign", signParams)
								.asStringAsync();
					}
				}
				UpdateIdentityCMod updateIdentityCMod = new UpdateIdentityCMod();
				updateIdentityCMod.setIdentity(2);
				userService.pushUserMessage(anchoruid, updateIdentityCMod);

			} catch (Exception e) {
				logger.error("<closeRoom->Exception:>" + e.getMessage());
			} finally {
				try {
					if (statement != null) {
						statement.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (Exception e2) {
					logger.error("<closeRoom->finally->exception>:" + e2.getMessage());
				}
			}
		}
	}

	/**
	 * 禁播 后台操作
	 * 
	 * @param anchoruid
	 * @param returnModel
	 */
	@Override
	public void banRoomByManage(int anchoruid, ReturnModel returnModel) {

		Connection conn = null;
		PreparedStatement statement = null;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
			statement = conn.prepareStatement(
					com.mpig.api.utils.StringUtils.getSqlString(SQL_UpdateUserIdentity, "user_base_info_", anchoruid));
			statement.setInt(1, 2);
			statement.setInt(2, anchoruid);
			// 修改用户identity
			int ires = statement.executeUpdate();
			if (ires == 1) {
				userService.getUserbaseInfoByUid(anchoruid, true);
			}

			UserBaseInfoModel anchorInfo = userService.getUserbaseInfoByUid(anchoruid, false);
			if (anchorInfo == null) {
				returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
				returnModel.setMessage("帐号异常");
			} else if (anchorInfo.getLiveStatus()) {
				// 开播
				if (this.exitLive(anchoruid, returnModel)) {

					@SuppressWarnings("unchecked")
					Map<String, Object> map = (Map<String, Object>) returnModel.getData();
					ExitRoomCMod exitRoomCMod = new ExitRoomCMod();
					exitRoomCMod.setAvatar(anchorInfo.getHeadimage());
					exitRoomCMod.setLevel(anchorInfo.getUserLevel());
					exitRoomCMod.setNickname(anchorInfo.getNickname());
					exitRoomCMod.setSex(anchorInfo.getSex());
					exitRoomCMod.setUid(anchoruid);
					exitRoomCMod.setCreditTotal(Integer.valueOf(map.get("creditTotal").toString()));
					exitRoomCMod.setPersontimes(Integer.valueOf(map.get("persontimes").toString()));
					exitRoomCMod.setRoomLikes(Integer.valueOf(map.get("roomlikes").toString()));
					exitRoomCMod.setTimeslong(Long.valueOf(map.get("timeslong").toString()));

					String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
							"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + JSONObject.toJSONString(exitRoomCMod),
							"roomOwner=" + anchoruid);

					Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room())
							.field("appKey", VarConfigUtils.ServiceKey)
							.field("msgBody", JSONObject.toJSONString(exitRoomCMod))
							.field("roomOwner", String.valueOf(anchoruid)).field("sign", signParams).asStringAsync();
				}
			}
			UpdateIdentityCMod updateIdentityCMod = new UpdateIdentityCMod();
			updateIdentityCMod.setIdentity(2);
			userService.pushUserMessage(anchoruid, updateIdentityCMod);

		} catch (Exception e) {
			returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
			returnModel.setMessage(e.getMessage());
			logger.error("<banRoomByManage->Exception:>" + e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error("<banRoomByManage->finally->exception>:" + e2.getMessage());
			}
		}
	}

	/**
	 * 解禁 后台操作
	 * 
	 * @param anchoruid
	 * @param returnModel
	 */
	@Override
	public void unBanRoomByManage(int anchoruid, ReturnModel returnModel) {

		Connection conn = null;
		PreparedStatement statement = null;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
			statement = conn.prepareStatement(
					com.mpig.api.utils.StringUtils.getSqlString(SQL_UpdateUserIdentity, "user_base_info_", anchoruid));
			statement.setInt(1, 1);
			statement.setInt(2, anchoruid);
			// 修改用户identity
			int ires = statement.executeUpdate();
			if (ires == 1) {
				userService.getUserbaseInfoByUid(anchoruid, true);

				if (statement != null) {
					statement.close();
				}

				String sql = "delete from user_handle where uid=" + anchoruid;
				statement = conn.prepareStatement(sql);
				statement.executeUpdate();

			} else {
				returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
				returnModel.setMessage("解禁失败");
			}

		} catch (Exception e) {
			returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
			returnModel.setMessage(e.getMessage());
			logger.error("<unBanRoomByManage->Exception:>" + e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error("<unBanRoomByManage->finally->exception>:" + e2.getMessage());
			}
		}
	}

	@Override
	public void closeLive(int uid, ReturnModel returnModel) {
		Connection conn = null;
		PreparedStatement statement = null;
		try {
			UserBaseInfoModel anchorInfo = userService.getUserbaseInfoByUid(uid, false);
			if (anchorInfo == null) {
				returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
				returnModel.setMessage("帐号异常");
			} else if (anchorInfo.getLiveStatus()) {
				// 开播
				if (this.exitLive(uid, returnModel)) {

					@SuppressWarnings("unchecked")
					Map<String, Object> map = (Map<String, Object>) returnModel.getData();
					ExitRoomCMod exitRoomCMod = new ExitRoomCMod();
					exitRoomCMod.setAvatar(anchorInfo.getHeadimage());
					exitRoomCMod.setLevel(anchorInfo.getUserLevel());
					exitRoomCMod.setNickname(anchorInfo.getNickname());
					exitRoomCMod.setSex(anchorInfo.getSex());
					exitRoomCMod.setUid(uid);
					exitRoomCMod.setCreditTotal(Integer.valueOf(map.get("creditTotal").toString()));
					exitRoomCMod.setPersontimes(Integer.valueOf(map.get("persontimes").toString()));
					exitRoomCMod.setRoomLikes(Integer.valueOf(map.get("roomlikes").toString()));
					exitRoomCMod.setTimeslong(Long.valueOf(map.get("timeslong").toString()));

					String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
							"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + JSONObject.toJSONString(exitRoomCMod),
							"roomOwner=" + uid);

					Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room())
							.field("appKey", VarConfigUtils.ServiceKey)
							.field("msgBody", JSONObject.toJSONString(exitRoomCMod))
							.field("roomOwner", String.valueOf(uid)).field("sign", signParams).asStringAsync();

					// Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_end_live())
					// .field("roomOwner", String.valueOf(uid)).field("appKey",
					// VarConfigUtils.ServiceKey)
					// .field("msgBody", returnModel.getData()).asStringAsync();
				}
			} else {
				if (anchorInfo.getRecommend() == 0) {
					if (OtherRedisService.getInstance().getBaseRoomInfo(uid) != null) {
						OtherRedisService.getInstance().delBaseRoom(uid);
					}
				} else if (anchorInfo.getRecommend() == 1) {
					if (OtherRedisService.getInstance().getHotRoomInfo(uid) != null) {
						OtherRedisService.getInstance().delHotRoom(uid);
					}
					if (OtherRedisService.getInstance().getMobileRoomInfo(uid) != null) {
						OtherRedisService.getInstance().delMobileRoom(uid);
					}
				} else if (anchorInfo.getRecommend() == 2) {
					if (OtherRedisService.getInstance().getHotRoomInfo(uid) != null) {
						OtherRedisService.getInstance().delHotRoom(uid);
					}
					if (OtherRedisService.getInstance().getRecommendRoomInfo(uid) != null) {
						OtherRedisService.getInstance().delRecommendRoom(uid);
					}
					if (OtherRedisService.getInstance().getMobileRoomInfo(uid) != null) {
						OtherRedisService.getInstance().delMobileRoom(uid);
					}
				}
			}
			CloseRoomCMod cmod = new CloseRoomCMod();
			userService.pushUserMessage(uid, cmod);

		} catch (Exception e) {
			returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
			returnModel.setMessage(e.getMessage());
			logger.error("<closeLive->Exception:>" + e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error("<closeLive->finally->exception>:" + e2.getMessage());
			}
		}
	}

	/**
	 * 发红包
	 * 
	 * @param type
	 *            =1送给主播 ＝2送给用户 ＝3送给大众 【 =3 定值】
	 * @param srcUid
	 *            发红包人
	 * @param dstUid
	 *            type＝1时 有该uid type＝2 则为0
	 * @param money
	 *            总金额
	 * @param count
	 *            红包数量
	 * @param reddesc
	 *            红包说明
	 * @param roomId
	 *            房间uid 即主播uid
	 */
	@Override
	public void sendRedEnvelope(int srcUid, int dstUid, int money, int count, String reddesc, int roomId,
			ReturnModel returnModel) {
		UserBaseInfoModel srcUserBase = userService.getUserbaseInfoByUid(srcUid, false);
		UserAssetModel srcUserAsset = userService.getUserAssetByUid(srcUid, false);

		UserBaseInfoModel roomUserbase = userService.getUserbaseInfoByUid(roomId, false);
		
		if (srcUserBase == null || roomUserbase == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("发红包人异常");
		} else {
			if (srcUserAsset.getMoney() < money) {
				returnModel.setCode(CodeContant.MONEYLESS);
				returnModel.setMessage("金额不足，请充值");
			} else {
				int ires = 0;
				// 扣金额
				ires = userService.updUserAssetByRedEnvelopUid(srcUid, money);
				if (ires <= 0) {
					returnModel.setCode(CodeContant.MONEYDEDUCT);
					returnModel.setMessage("扣费失败，请重新操作");
				} else {

					ires = billService.insertRedEnvelop(roomId, reddesc, srcUid, dstUid, money, count,
							System.currentTimeMillis() / 1000);
					if (ires > 0) {
						Map<String, Object> map = new HashMap<String, Object>();

						map.put("srcmoney", srcUserAsset.getMoney() - money);
						returnModel.setData(map);
						// 聊天服务器 拆分红包
						try {
							RedEnvelopService.getInstance().generateRedEnvelopData(srcUid, ires, (long) money, count,
									(long) money, (long) 1, srcUserBase.getNickname(), srcUserBase.getUserLevel(),
									srcUserBase.getHeadimage(), reddesc, 3, roomId, dstUid);
							
							// 上跑道   ************* start
							if (money >= 1000) {
								
								userService.getUserbaseInfoByUid(roomId, false);
								List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
								HashMap<String, Object> mapRun = new HashMap<String, Object>();
								
								mapRun.put("name", srcUserBase.getNickname());
								mapRun.put("color", "#fff08c");
								list.add(mapRun);
								
								mapRun = new HashMap<String, Object>();
								mapRun.put("name", "在");
								mapRun.put("color", "");
								list.add(mapRun);
								
								mapRun = new HashMap<String, Object>();
								mapRun.put("name", roomUserbase.getNickname());
								mapRun.put("color", "#fff08c");
								list.add(mapRun);

								mapRun = new HashMap<String, Object>();
								mapRun.put("name", "直播间 发放了一个超级红包");
								mapRun.put("color", "");
								list.add(mapRun);
								
								mapRun = new HashMap<String, Object>();
								mapRun.put("gid", 85); // 红包gid
								mapRun.put("color", "");
								list.add(mapRun);
								
								mapRun = new HashMap<String, Object>();
								mapRun.put("name", "，引发现场观众哄抢！，壕就是这么任性");
								mapRun.put("color", "");
								list.add(mapRun);
								
								RunWayCMod redBody = new RunWayCMod();
								Map<String,Object> _map = new HashMap<String, Object>();
								
								_map.put("list", list);
								redBody.setData(_map);
								redBody.setAnchorUid(roomId);
								redBody.setAnchorName(roomUserbase.getNickname());

								String redParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
										"appKey=" + VarConfigUtils.ServiceKey,
										"msgBody=" + JSONObject.toJSONString(redBody));

								Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room_all())
										.field("appKey", VarConfigUtils.ServiceKey)
										.field("msgBody", JSONObject.toJSONString(redBody))
										.field("sign", redParams).asJsonAsync();
							}
							// 上跑道   ************* end
							
							
						} catch (Exception ex) {
							logger.error("sendRedEnvelope->RedEnvelopService:拆红包失败：" + ex.getMessage());

							returnModel.setCode(CodeContant.sendRedEnvelope);
							returnModel.setMessage("红包发送失败,钱已扣，请联系客服，红包ID＝" + ires + " 发红包的人:" + srcUid);
						}
					} else {
						returnModel.setCode(CodeContant.sendRedEnvelope);
						returnModel.setMessage("红包发送失败，请联系客服");
					}
				}
			}
		}
	}

	/**
	 * 抢红包
	 * 
	 * @param dstUid
	 *            抢红包人
	 * @param envelopeId
	 *            红包id
	 * @param returnModel
	 */
	@Override
	public void getRedEnvelope(int dstUid, int envelopeId, ReturnModel returnModel) {

		BillRedenvelopeModel billRedenvelopeModel = billService.getRedEnvelopById(envelopeId);
		if (billRedenvelopeModel == null) {
			returnModel.setCode(CodeContant.liveRedEnvelopeErr);
			returnModel.setMessage("该红包已经失效");
		} else if (billRedenvelopeModel.getIsfinish() == 1
				|| billRedenvelopeModel.getSendcnts() == billRedenvelopeModel.getGetcnts()
				|| (billRedenvelopeModel.getSendmoney() - billRedenvelopeModel.getGetmoney() < 1)) {
			returnModel.setCode(CodeContant.liveRedEnvelopFinish);
			returnModel.setMessage("手慢了，红包派完了");
		} else {
			PayGetRedenvelopModel payGetRedenvelopModel = redEnvelope.getGetRedenvelopInfo(envelopeId, dstUid);
			if (payGetRedenvelopModel != null) {
				returnModel.setCode(CodeContant.liveRedEnvelopFinish);
				returnModel.setMessage("手慢了，红包派完了");
				return;
			}

			int srcUid = billRedenvelopeModel.getSrcUid();
			// 抢红包
			try {
				Object object = RedEnvelopService.getInstance().tryGetRedEnvelop(srcUid, envelopeId, dstUid);
				if (object == null) {
					returnModel.setCode(CodeContant.liveRedEnvelopFinish);
					returnModel.setMessage("手慢了，红包派完了");
					System.out.println("getRedEnvelope: 抢完了");
				} else {
					JSONObject jsonObject = JSONObject.parseObject((String) object);

					int money = jsonObject.getIntValue("money");
					if (billRedenvelopeModel.getSendmoney() - billRedenvelopeModel.getGetmoney() - money < 0) {
						returnModel.setCode(CodeContant.liveRedEnvelopFinish);
						System.out.println("getRedEnvelope: 金额不对");
						returnModel.setMessage("手慢了，红包派完了");
					} else {

						// 更新红包信息
						if (billRedenvelopeModel.getSendcnts() - billRedenvelopeModel.getGetcnts() > 1) {
							// 未抢完
							billService.updRedEnvelopeById(0, money, 1, 0, envelopeId);
						} else {
							// 抢最后一个
							billService.updRedEnvelopeById(1, money, 1, (int) (System.currentTimeMillis() / 1000),
									envelopeId);
							//删除缓存
							RedEnvelopService.getInstance().delRedEnvelop(srcUid, envelopeId);
						}

						// 添加抢红包记录
						int ires = 0;

						ires = redEnvelope.insertPayGetRedenvelop(envelopeId, srcUid, dstUid,
								billRedenvelopeModel.getRoomId(), money, System.currentTimeMillis() / 1000);
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("money", money);
						returnModel.setData(map);

						if (ires == 1) {
							// 更新抢红包人的资产
							ires = userService.updAssetMoneyByUid(dstUid, (double) money);
							if (ires != 1) {
								logger.error("xiaozhutv:抢到红包后添加猪头失败：envelopeId=" + envelopeId + " 抢到红包的人 dstUid="
										+ dstUid + " 发红包的人 srcUid＝" + srcUid + " 红包金额 money=" + money);
							}

						} else {
							logger.error("xiaozhutv:抢到红包后添加记录失败：envelopeId=" + envelopeId + " 抢到红包的人 dstUid=" + dstUid
									+ " 发红包的人 srcUid＝" + srcUid + " 红包金额 money=" + money);
						}
					}
				}
			} catch (Exception ex) {
				returnModel.setCode(CodeContant.liveEnvelopeTry);
				returnModel.setMessage("抢红包失败，请再尝试");
				logger.error("getRedEnvelope->RedEnvelopService 抢红包失败：" + ex.getMessage());
			}
		}
	}

	@Override
	public void checkTriedEnvelope(int envelopeid, int uid, ReturnModel returnModel) {

		Map<String, Object> map = new HashMap<String, Object>();

		BillRedenvelopeModel billRedenvelopeModel = billService.getRedEnvelopById(envelopeid);
		if (billRedenvelopeModel == null) {
			returnModel.setCode(CodeContant.liveRedEnvelopeErr);
			returnModel.setMessage("该红包不存在");
		} else {
			UserBaseInfoModel srcUserModel = userService.getUserbaseInfoByUid(billRedenvelopeModel.getSrcUid(), false);

			map.put("headimage", srcUserModel.getHeadimage());
			map.put("nickname", srcUserModel.getNickname());
			map.put("bless", billRedenvelopeModel.getReddesc());

			PayGetRedenvelopModel payGetRedenvelopModel = redEnvelope.getGetRedenvelopInfo(envelopeid, uid);
			if (payGetRedenvelopModel == null) {
				// 未抢红包
				map.put("status", 0);
				map.put("zhutou", 0);
				if (billRedenvelopeModel.getIsfinish() == 0
						|| billRedenvelopeModel.getSendcnts() == billRedenvelopeModel.getGetcnts()
						|| billRedenvelopeModel.getSendmoney() == billRedenvelopeModel.getGetmoney()) {
					// 红包已抢完
					map.put("over", 0);
				} else {
					map.put("over", 1);
				}

			} else {
				// 已抢红包
				map.put("status", 1);
				map.put("zhutou", payGetRedenvelopModel.getMoney());
				map.put("over", 0);
			}
			returnModel.setData(map);
		}

	}

	/**
	 * 在指定房间内 给指定人发红包
	 * 
	 * @param srcUid
	 *            发红包人UID
	 * @param dstUid
	 *            收红包人UID
	 * @param money
	 *            金额
	 * @param count
	 *            个数
	 * @param reddesc
	 *            祝福语
	 * @param roomId
	 *            房间uid
	 * @param returnModel
	 */
	@Override
	public void sendRedEnvelopeToOne(int srcUid, int dstUid, int money, int count, String reddesc, int roomId,
			ReturnModel returnModel) {
		UserBaseInfoModel srcUserBase = userService.getUserbaseInfoByUid(srcUid, false);
		UserAssetModel srcUserAsset = userService.getUserAssetByUid(srcUid, false);
		UserAssetModel dstUserAsset = userService.getUserAssetByUid(dstUid, false);
		UserBaseInfoModel dstUserBase = userService.getUserbaseInfoByUid(dstUid, false);
		
		UserBaseInfoModel roomUserbase = userService.getUserbaseInfoByUid(roomId, false);
		
		if (srcUserBase == null || roomUserbase == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("发红包人异常");
		} else {
			if (srcUserAsset.getMoney() < money) {
				returnModel.setCode(CodeContant.MONEYLESS);
				returnModel.setMessage("金额不足，请充值");
			} else {
				int ires = 0;
				// 扣金额
				ires = userService.updUserAssetByRedEnvelopUid(srcUid, money);
				if (ires <= 0) {
					returnModel.setCode(CodeContant.MONEYDEDUCT);
					returnModel.setMessage("扣费失败，请重新操作");
				} else {

					int envelopeId = billService.insertRedEnvelop(roomId, reddesc, srcUid, dstUid, money, count,
							System.currentTimeMillis() / 1000);
					if (envelopeId > 0) {
						// 抢最后一个
						billService.updRedEnvelopeById(1, money, 1, (int) (System.currentTimeMillis() / 1000),
								envelopeId);

						ires = redEnvelope.insertPayGetRedenvelop(envelopeId, srcUid, dstUid, roomId, money,
								System.currentTimeMillis() / 1000);

						if (ires == 1) {
							// 更新抢红包人的资产
							ires = userService.updAssetMoneyByUid(dstUid, (double) money);
							if (ires != 1) {
								logger.error("xiaozhutv:抢到红包后添加猪头失败：envelopeId=" + envelopeId + " 抢到红包的人 dstUid="
										+ dstUid + " 发红包的人 srcUid＝" + srcUid + " 红包金额 money=" + money);
							}
						} else {
							logger.error("xiaozhutv:抢到红包后添加记录失败：envelopeId=" + envelopeId + " 抢到红包的人 dstUid=" + dstUid
									+ " 发红包的人 srcUid＝" + srcUid + " 红包金额 money=" + money);
						}
						Map<String, Object> map = new HashMap<String, Object>();

						map.put("srcmoney", srcUserAsset.getMoney() - money);
						map.put("dstmoney", dstUserAsset.getMoney() + money);

						returnModel.setData(map);
						
						// 发协议号100 通知红包接收人  更新资产  ******* start
						final JSONObject msgNotice = new JSONObject();
						msgNotice.put("cometProtocol", CModProtocol.REDENVELOP_GENERATE_FOR_ANCHOR);
						msgNotice.put("money", dstUserAsset.getMoney() + money);
						msgNotice.put("uid", dstUid);
						
						final String url = UrlConfigLib.getUrl("url").getAdminrpc_publish_live();
						
						String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
								"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + JSONObject.toJSONString(msgNotice),
								"users=" + dstUid + "");
						Unirest.post(url).field("appKey", VarConfigUtils.ServiceKey)
								.field("msgBody", JSONObject.toJSONString(msgNotice)).field("users", dstUid + "")
								.field("sign", signParams).asJsonAsync();
						// 发协议号100 通知红包接收人  更新资产  ******* end
						
						// TOSY 拼装发送房间广播  ******* start
						GiftSendCMod msgBody = new GiftSendCMod();
						msgBody.setStamp(String.valueOf(System.currentTimeMillis()/1000));
						msgBody.setGid(85);
						msgBody.setCombo(1);
						msgBody.setType(0);
						msgBody.setName("红包");
						msgBody.setGpctype(1);
						msgBody.setCount(1);
						msgBody.setDstuid(dstUid);
						
						msgBody.setUid(srcUid);
						msgBody.setGets(0);
						msgBody.setGetTotal(dstUserAsset.getCreditTotal());
						msgBody.setAvatar(srcUserBase.getHeadimage());
						msgBody.setNickname(srcUserBase.getNickname());
						msgBody.setSex(srcUserBase.getSex());
						msgBody.setLevel(srcUserBase.getUserLevel());
						msgBody.setDstNickname(dstUserBase.getNickname());
						
						signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret, "appKey=" + VarConfigUtils.ServiceKey,
								"msgBody=" + JSONObject.toJSONString(msgBody), "roomOwner=" + roomId);

						Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room()).field("appKey", VarConfigUtils.ServiceKey)
								.field("msgBody", JSONObject.toJSONString(msgBody)).field("roomOwner", roomId)
								.field("sign", signParams).asJsonAsync();
						// TOSY 拼装发送房间广播  ******* end 
						
						// 对个人发送红包 通知消息
						List<Map<String, Object>> giftList = new ArrayList<Map<String, Object>>();
						List<Map<String, Object>> msgList = new ArrayList<Map<String, Object>>();
						
						Map<String, Object> msgMap = new HashMap<String,Object>();
						msgMap.put("name", "收到 ");
						msgMap.put("color", "");
						msgList.add(msgMap);
						
						msgMap = new HashMap<String,Object>();
						msgMap.put("name", srcUserBase.getNickname());
						msgMap.put("color", "#fff08c");
						msgList.add(msgMap);
						
						msgMap = new HashMap<String,Object>();
						msgMap.put("name", "发来 ");
						msgMap.put("color", "");
						msgList.add(msgMap);
						
						msgMap = new HashMap<String,Object>();
						msgMap.put("name", money);
						msgMap.put("color", "#fff08c");
						msgList.add(msgMap);
						
						msgMap = new HashMap<String,Object>();
						msgMap.put("name", "猪头的红包");
						msgMap.put("color", "");
						msgList.add(msgMap);
						Map<String, Object> toUserMap = new HashMap<String,Object>();
						toUserMap.put("list", msgList);
						ChatMessageUtil.sendGiftUpdateAssetAndBag(dstUid,"", null, toUserMap,giftList,null, null,null);
						
						// 上跑道   ************* start
						if (money >= 1000) {
							userService.getUserbaseInfoByUid(roomId, false);
							List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
							HashMap<String, Object> mapRun = new HashMap<String, Object>();
							
							mapRun.put("name", srcUserBase.getNickname());
							mapRun.put("color", "#fff08c");
							list.add(mapRun);
							
							mapRun = new HashMap<String, Object>();
							mapRun.put("name", "在");
							mapRun.put("color", "");
							list.add(mapRun);
							
							mapRun = new HashMap<String, Object>();
							mapRun.put("name", roomUserbase.getNickname());
							mapRun.put("color", "#fff08c");
							list.add(mapRun);

							mapRun = new HashMap<String, Object>();
							mapRun.put("name", "直播间 发放了一个私人红包");
							mapRun.put("color", "");
							list.add(mapRun);
							
							mapRun = new HashMap<String, Object>();
							mapRun.put("gid", 85); // 红包gid
							mapRun.put("color", "");
							list.add(mapRun);
							
							mapRun = new HashMap<String, Object>();
							mapRun.put("name", "，壕就是这么任性");
							mapRun.put("color", "");
							list.add(mapRun);
							
							RunWayCMod redBody = new RunWayCMod();
							Map<String,Object> _map = new HashMap<String, Object>();
							
							_map.put("list", list);
							redBody.setData(_map);
							redBody.setAnchorUid(roomId);
							redBody.setAnchorName(roomUserbase.getNickname());

							String redParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
									"appKey=" + VarConfigUtils.ServiceKey,
									"msgBody=" + JSONObject.toJSONString(redBody));

							Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room_all())
									.field("appKey", VarConfigUtils.ServiceKey)
									.field("msgBody", JSONObject.toJSONString(redBody))
									.field("sign", redParams).asJsonAsync();
						}
						// 上跑道   ************* end

					} else {
						returnModel.setCode(CodeContant.sendRedEnvelope);
						returnModel.setMessage("红包发送失败，请联系客服");
					}
				}
			}
		}
	}
	
	/**
	 * 退房，下播，都会调用
	 * @param uSrc
	 */
	@Override
	public void liveInviteCanncel(UserBaseInfoModel uSrc){
		String lastDstUid = OtherRedisService.getInstance().getInviteMic(uSrc.getUid());
		if(null == lastDstUid){
			return;
		}
		Integer dstuid = Integer.valueOf(lastDstUid);
		if(null == dstuid){
			return;
		}
		
		OtherRedisService.getInstance().delInviteMic(uSrc.getUid());
		
		//发送请求
		int isCanncel = 1;
		LiveInviteCMod msgBody = new LiveInviteCMod();
		msgBody.setUid(uSrc.getUid());
		msgBody.setNickname(uSrc.getNickname());
		msgBody.setAvatar(uSrc.getHeadimage());
		msgBody.setSex(uSrc.getSex());
		msgBody.setLevel(uSrc.getUserLevel());
		msgBody.setIsCanncel(isCanncel);

		String msgbody = JSONObject.toJSONString(msgBody);
		String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
				"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + msgbody,
				"users=" + dstuid);

		Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_live()).field("appKey", VarConfigUtils.ServiceKey)
				.field("msgBody", msgbody).field("users",String.valueOf(dstuid)).field("sign", signParams).asJsonAsync();
	}
	
	/**
	 * 邀请或撤销邀请
	 * arg:isCannecl 0邀请   1取消邀请
	 * arg:uid		   发起邀请人
	 * arg：dstuid	   被邀请人
	 * 返回值
	 * 	map.put("isInviteOK", 1); 0失败	1成功
	 *	map.put("reason", "OK");
	 */
	@Override
	public void liveInvite(int uid,int dstuid,int isCanncel,ReturnModel returnModel){
		//返回值
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isInviteOK", 1);
		map.put("reason", "OK");
		
		UserBaseInfoModel uSrc = userService.getUserbaseInfoByUid(uid, false);
		if (null == uSrc) {
			map.put("isInviteOK", 0);
			map.put("reason", "无发起用户");
			returnModel.setData(map);
			return ;
		}
		UserBaseInfoModel uDst = userService.getUserbaseInfoByUid(dstuid, false);
		if (null == uDst || false == uDst.getLiveStatus()) {
			map.put("isInviteOK", 0);
			map.put("reason", "无接收用户");
			returnModel.setData(map);
			return ;
		}
		if(false == uDst.getLiveStatus()) {
			map.put("isInviteOK", 0);
			map.put("reason", "对方未开播");
			returnModel.setData(map);
			return ;
		}
		//发起邀请
		if(isCanncel==0) {
			//确保一个用户只能发起唯一的邀请
			String lastDstUid = OtherRedisService.getInstance().getInviteMic(uid);
			if(null != lastDstUid){
				int nlastDstUid = Integer.valueOf(lastDstUid);
				if(nlastDstUid != dstuid){
					LiveInviteCMod msgBody = new LiveInviteCMod();
					msgBody.setUid(uid);
					msgBody.setNickname(uSrc.getNickname());
					msgBody.setAvatar(uSrc.getHeadimage());
					msgBody.setSex(uSrc.getSex());
					msgBody.setLevel(uSrc.getUserLevel());
					msgBody.setIsCanncel(1);

					String msgbody = JSONObject.toJSONString(msgBody);
					String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
							"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + msgbody,
							"users=" + dstuid);

					Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_live()).field("appKey", VarConfigUtils.ServiceKey)
							.field("msgBody", msgbody).field("users",lastDstUid).field("sign", signParams).asJsonAsync();					
				}
			}
		}else {
			//清理redis的邀请缓存
			OtherRedisService.getInstance().delInviteMic(uid);
		}
		
		String rt = OtherRedisService.getInstance().setInviteMic(uid, dstuid);
		if(null == rt){
			//未发送邀请
			map.put("isInviteOK", 0);
			map.put("reason", "状态更新失败");
			returnModel.setData(map);
		}
		//发送请求
		LiveInviteCMod msgBody = new LiveInviteCMod();
		msgBody.setUid(uid);
		msgBody.setNickname(uSrc.getNickname());
		msgBody.setAvatar(uSrc.getHeadimage());
		msgBody.setSex(uSrc.getSex());
		msgBody.setLevel(uSrc.getUserLevel());
		msgBody.setIsCanncel(isCanncel);
		msgBody.setIsLiving(uSrc.getLiveStatus());
		
		String msgbody = JSONObject.toJSONString(msgBody);
		String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
				"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + msgbody,
				"users=" + dstuid);

		Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_live()).field("appKey", VarConfigUtils.ServiceKey)
				.field("msgBody", msgbody).field("users",String.valueOf(dstuid)).field("sign", signParams).asJsonAsync();
	
		returnModel.setData(map);
		return;
	}

	/**
	 * 连麦的广播，开关连麦状态
	 * arg:isjoin	是否同意被邀请  0不接受邀请	1接受邀请
	 * arg:uid		发起邀请人
	 * argdstuid	被邀请人
	 * 返回
	 * 	map.put("isAckOK", 1);	int	0失败	1成功
	 *	map.put("reason", "OK");
	 */
	@Override
	public void liveAckInvite(int uid, int dstuid,int isjoin, ReturnModel returnModel) {
		//返回值
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isAckOK", 1);
		map.put("reason", "OK");

		int nSrcUid = dstuid;	//邀请人
		int nDstUid = uid;		//被邀请人
		
		UserBaseInfoModel uDst = userService.getUserbaseInfoByUid(nDstUid, false);
		if (null == uDst) {
			map.put("isAckOK", 0);
			map.put("reason", "无被邀请用户");
			returnModel.setData(map);
			return ;
		}
		UserBaseInfoModel uSrc = userService.getUserbaseInfoByUid(nSrcUid, false);
		if (null == uSrc) {
			map.put("isAckOK", 0);
			map.put("reason", "无发起邀请的用户");
			returnModel.setData(map);
			return ;
		}
		
		if(isjoin>0){
			//redis验证过时内容
			String strLastDstUid = OtherRedisService.getInstance().getInviteMic(nSrcUid);
			if(null == strLastDstUid){
				map.put("isAckOK", 0);
				map.put("reason", "邀请已取消");
				returnModel.setData(map);
				return;
			}
			Integer lastdstuid = Integer.valueOf(strLastDstUid); 
			if(null == lastdstuid || nDstUid != lastdstuid){
				map.put("isAckOK", 0);
				map.put("reason", "邀请已取消");
				returnModel.setData(map);
				return;			
			}
		}
		updateLiveMic(uSrc,uDst,isjoin);

		returnModel.setData(map);
	}

	@Override
	public void updateLiveMic(UserBaseInfoModel uSrc,UserBaseInfoModel uDst,int isjoin){
		boolean nRet1 = updateLiveMicDomain(uSrc,uDst,isjoin);
		boolean nRet2 = updateLiveMicDomain(uDst,uSrc,isjoin);
		
		if(nRet1 || nRet2){
			//广播开启或关闭
			broadcastLiveMic(uSrc,uDst,isjoin);	
			broadcastLiveMic(uDst,uSrc,isjoin);
		}		
	}

	public boolean updateLiveMicDomain(UserBaseInfoModel uSrc,UserBaseInfoModel uDst,int isbroadcastMicON){
		boolean isNeedUpdate = false;
		
		if(isbroadcastMicON > 0){
			if(uSrc.getLiveStatus()){
				OtherRedisService.getInstance().setLive2ndUid(uSrc.getUid(),uDst.getUid());
				isNeedUpdate = true;	
			}
//			if(uDst.getLiveStatus()){
//				OtherRedisService.getInstance().setLive2ndUid(uDst.getUid(),uSrc.getUid());
//				isNeedUpdate = true;
//			}
		}else{
			if(uSrc.getLiveStatus()){
				String rt = OtherRedisService.getInstance().getLive2ndUid(uSrc.getUid());
				if(null != rt){
					Integer uid2nd = Integer.valueOf(rt);
					if(null != uid2nd && uid2nd.equals(uDst.getUid())){
						OtherRedisService.getInstance().delLive2ndUid(uSrc.getUid());
						isNeedUpdate = true;
					}
				}	
			}
//			if(uDst.getLiveStatus()){
//				String rt = OtherRedisService.getInstance().getLive2ndUid(uDst.getUid());
//				if(null != rt){
//					Integer uid2nd = Integer.valueOf(rt);
//					if(null != uid2nd && uid2nd == uSrc.getUid()){
//						OtherRedisService.getInstance().delLive2ndUid(uSrc.getUid());
//						isNeedUpdate = true;
//					}
//				}	
//			}
		}
		
		return isNeedUpdate;
	}

	public void broadcastLiveMic(UserBaseInfoModel uSrc,UserBaseInfoModel uDst,int isbroadcastMicON){
		
		if(uSrc.getLiveStatus()){
			//广播请求
			LiveInviteAckCMod msgBody = new LiveInviteAckCMod();
			msgBody.setUid(uSrc.getUid());
			msgBody.setNickname(uSrc.getNickname());
			msgBody.setAvatar(uSrc.getHeadimage());
			msgBody.setSex(uSrc.getSex());
			msgBody.setLevel(uSrc.getUserLevel());

			msgBody.setUid2nd(uDst.getUid());
			msgBody.setNickname2nd(uDst.getNickname());
			msgBody.setAvatar2nd(uDst.getHeadimage());
			msgBody.setSex2nd(uDst.getSex());
			msgBody.setLevel2nd(uDst.getUserLevel());
			
			msgBody.setIsJoin(isbroadcastMicON);

			Map<String, Object> map = new HashMap<String, Object>();
			if(isbroadcastMicON>0){	
				map = this.getVideoConfig(uSrc.getUid(), uDst.getUid(), uDst.getVideoline());
				//TOSY 第三方流判断
				String stream = configService.getThirdStream(uDst.getUid());
				if (null != stream) {
					map.put("domain", stream);
				}
			}
			msgBody.setVideoStream(map);

			String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
					"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + JSONObject.toJSONString(msgBody),
					"roomOwner=" + uSrc.getUid());

//			logger.debug("app broadcastLiveMic " + JSONObject.toJSONString(msgBody));
			
			Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room())
					.field("appKey", VarConfigUtils.ServiceKey)
					.field("msgBody", JSONObject.toJSONString(msgBody))
					.field("roomOwner", String.valueOf(uSrc.getUid())).field("sign", signParams).asStringAsync();
		}
	}
	
	public void liveAckReward(int anchoruid, int uid){
		Long lg = System.currentTimeMillis() / 1000;
		if(lg >= 1470412800 && lg <= 1470758400){
			String liveAckRewardKey = RedisContant.ActivityUserList+"7x";
	    	if(OtherRedisService.getInstance().hget(liveAckRewardKey, anchoruid+":"+uid)==null){
	    		// 鹊桥id
	    		int queqId = 40;
	    		OtherRedisService.getInstance().hset(liveAckRewardKey, anchoruid+":"+uid, "1");
	    		userItemService.insertUserItem(uid, queqId, 1, ItemSource.Activity);
	    		userItemService.insertUserItem(anchoruid, queqId, 1, ItemSource.Activity);
	    		
	    		//更新礼物至背包
				List<Map<String, Object>> giftList = new ArrayList<Map<String, Object>>();
				HashMap<String, Object> giftMap = new HashMap<String, Object>();
				HashMap<String, Object> toUserMap = new HashMap<String, Object>();
				giftMap.put("gid", queqId);
				giftMap.put("num", "1");
				giftList.add(giftMap);
				UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(uid, false);
				ChatMessageUtil.sendGiftUpdateAssetAndBag(uid,null, sendUserAssetModel.getMoney(), toUserMap, giftList, null, null, null);
	    	}
		}
		
	}

	@Override
	public void check2ndStream(Integer roomid, ReturnModel returnModel) {
		Map<String, Object> mapList = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		String rt = OtherRedisService.getInstance().getLive2ndUid(roomid);
		if(null != rt){
			Integer uid2nd = Integer.valueOf(rt);
			if(null != uid2nd && uid2nd != roomid){
				//TOSY ADD NEW STREAM2
				UserBaseInfoModel userBaseInfoModel2 = userService.getUserbaseInfoByUid(uid2nd, false);
				if(null != userBaseInfoModel2){
					map = getVideoConfig(roomid, uid2nd, userBaseInfoModel2.getVideoline());
					map.put("uid2nd",uid2nd);
					String stream = configService.getThirdStream(uid2nd);
					if (null != stream) {
						map.put("domain", stream);
					}
					map.put("headimage", userBaseInfoModel2.getHeadimage());
					map.put("nickname", userBaseInfoModel2.getNickname());
					
					mapList.put("videoStream2nd", map);					
				}
			}
		}	
		
		returnModel.setCode(CodeContant.OK);
		returnModel.setData(mapList);
	}

	@Override
	public List<Map<String, Object>> getLivedTimeList(int uid, String type, int page, int pageSize) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			Date date = new Date();
			long startDateLong = 0l , endDateLong = 0l;
			if("1".equals(type)) {
				startDateLong = DateUtils.getMonthStartDay(date, 0).getTime()/1000;
				endDateLong = DateUtils.getMonthEndDay(date, 0).getTime()/1000;
			}else if("-1".equals(type)) {
				startDateLong = DateUtils.getMonthStartDay(date, -1).getTime()/1000;
				endDateLong = DateUtils.getMonthEndDay(date, -1).getTime()/1000;
			}
			con= DataSource.instance.getPool(VarConfigUtils.dbZhuLive).getConnection();
			pstmt = con.prepareStatement(SqlTemplete.SQL_getLivedTimeList);
			DBHelper.setPreparedStatementParam(pstmt,uid,startDateLong,endDateLong,page*pageSize,pageSize);
			rs = pstmt.executeQuery();
			while (rs != null && rs.next()) {
				Map<String ,Object> map = new HashMap<String, Object>();
				map.put("starttime", rs.getString("starttime"));
				map.put("endtime", rs.getString("endtime"));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public long getLivedTimeTotalCount(int uid, String type) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long totalCount = 0;
		try {
			Date date = new Date();
			long startDateLong = 0l , endDateLong = 0l;
			if("1".equals(type)) {
				startDateLong = DateUtils.getMonthStartDay(date, 0).getTime()/1000;
				endDateLong = DateUtils.getMonthEndDay(date, 0).getTime()/1000;
			}else if("-1".equals(type)) {
				startDateLong = DateUtils.getMonthStartDay(date, -1).getTime()/1000;
				endDateLong = DateUtils.getMonthEndDay(date, -1).getTime()/1000;
			}
			con= DataSource.instance.getPool(VarConfigUtils.dbZhuLive).getConnection();
			pstmt = con.prepareStatement(SqlTemplete.SQL_getLivedTimeTotalCount);
			DBHelper.setPreparedStatementParam(pstmt,uid,startDateLong,endDateLong);
			rs = pstmt.executeQuery();
			while (rs != null && rs.next()) {
				totalCount = rs.getLong("totalCount");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return totalCount;
	}

	@Override
	public Map<String, Object> getLivedTimeSumary(int uid, String type) {
		Map<String, Object> result = new HashMap<String,Object>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			Date date = new Date();
			String month = "";
			if("1".equals(type)) {
				Date d=  DateUtils.getMonthStartDay(date, 0);
				month = DateUtils.dateFormat(d, "yyyyMM");
			}else if("-1".equals(type)) {
				Date d=  DateUtils.getMonthStartDay(date, -1);
				month = DateUtils.dateFormat(d, "yyyyMM");
			}
			con= DataSource.instance.getPool(VarConfigUtils.dbZhuLive).getConnection();
			System.out.println(uid);
			System.out.println(month);
			System.out.println(SqlTemplete.SQL_getLivedTimeSumary);
			pstmt = con.prepareStatement(SqlTemplete.SQL_getLivedTimeSumary);
			DBHelper.setPreparedStatementParam(pstmt,uid,month);
			rs = pstmt.executeQuery();
			while (rs != null && rs.next()) {
				result.put("validDays", rs.getInt("validday"));
				result.put("sumLiveTime", rs.getLong("airtime"));
				result.put("sumCredit", rs.getLong("credits"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public void closeLiveInvite(int uid, ReturnModel returnModel) {
		if(OtherRedisService.getInstance().existInviteInfo(uid)) {
			int dstuid = 0;
			String res = OtherRedisService.getInstance().getInviteOtherId(uid);
			if(StringUtils.isNotBlank(res)) {
				dstuid = Integer.valueOf(res);
			}
			OtherRedisService.getInstance().delInviteInfo(uid, dstuid);
		}
		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(uid, false);
		if(userBaseInfoModel.getLianmaiStatus()==0) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("主播没有连麦");
			return;
		}
		int secodUid = userBaseInfoModel.getLianmaiAnchorId();
		boolean res = userService.updUserBasePKStatusById(uid, secodUid, 0);
		if(res) {
			InviteCloseCMod msgBody = new InviteCloseCMod();
			pkService.sendRoomMessage(uid, msgBody);
			pkService.sendRoomMessage(secodUid, msgBody);
		}
	}
}