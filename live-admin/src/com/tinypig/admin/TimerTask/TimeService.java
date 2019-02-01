package com.tinypig.admin.TimerTask;

import java.sql.Connection;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.tinypig.admin.constant;
import com.tinypig.admin.dao.GiftInfoDao;
import com.tinypig.admin.dao.OperateDao;
import com.tinypig.admin.dao.TimerTaskDao;
import com.tinypig.admin.dao.UnionDao;
import com.tinypig.admin.dao.UserGuardInfoDao;
import com.tinypig.admin.model.ChannelModel;
import com.tinypig.admin.model.ConfigGiftModel;
import com.tinypig.admin.redis.service.OtherRedisService;
import com.tinypig.admin.redis.service.RedisOperat;
import com.tinypig.admin.util.Constant;
import com.tinypig.admin.util.DateUtil;
import com.tinypig.admin.util.DbUtil;
import com.tinypig.admin.util.EncryptUtils;
import com.tinypig.admin.util.NoticeAPIUtile;
import com.tinypig.admin.util.RedisContant;
import com.tinypig.admin.util.VarConfigUtils;

import redis.clients.jedis.Tuple;

public class TimeService {

	private static final Logger logger = Logger.getLogger(TimeService.class);
	
	/**
	 * 周星发放，每周一 零时30分零秒发
	 */
	public void giveWeekReward(){
		
		String week = DateUtil.getWeekStart(1);
//		String week = "20161121";
		Long lgNow = System.currentTimeMillis()/1000;
		// 获取上周周星礼物
		Set<Tuple> giftlist = RedisOperat.getInstance().zrevrangeWithScores(RedisContant.host, RedisContant.port6379, "zhouxin:times:" + week, 0, -1);
		if (giftlist != null && giftlist.size() > 0 ) {
			Connection con = null;
			Statement pstmt = null;
			boolean bl = false;
			
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			Set<String> anchors = new HashSet<String>();
			
			try {
				con = DbUtil.instance().getCon(constant.db_zhu_pay,"master");
				pstmt = con.createStatement();
				
				String strSql = null;
				String sql = null;
				String tbName = null;
				
				con.setAutoCommit(false);
				
				RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6379, RedisContant.WeekTitle);
				for(Tuple tuple:giftlist){
					Map<String, Object> _map = new HashMap<String, Object>();
					ConfigGiftModel giftInfo = GiftInfoDao.getInstance().getGiftInfo(Integer.valueOf(tuple.getElement()));
					
					// 获取主播的周星第一名
					Set<Tuple> anchor = RedisOperat.getInstance().zrevrangeWithScores(RedisContant.host, RedisContant.port6379, "anchor:zx:"+week+":"+tuple.getElement(), 0, 0);
					Set<Tuple> user = RedisOperat.getInstance().zrevrangeWithScores(RedisContant.host, RedisContant.port6379, "user:zx:"+week+":"+tuple.getElement(), 0, 0);
					
					if (anchor != null && anchor.size() > 0) {
						
						for(Tuple anchorTuple:anchor){

							sql = "insert into pay_week_star(uid,gid,usertype,cycle,scores,amount,adminuid,addtime)value("+anchorTuple.getElement()+","+tuple.getElement()+",1,'"+week+"',"+anchorTuple.getScore()+","+Constant.anchor_weekStar_reward+",0,"+lgNow+")";
							pstmt.addBatch(sql);
							
							tbName = "zhu_user.user_asset_"+anchorTuple.getElement().substring(anchorTuple.getElement().length()-2, anchorTuple.getElement().length());
							strSql = "update "+tbName+" set credit=credit+"+Constant.anchor_weekStar_reward+",creditTotal=creditTotal+"+Constant.anchor_weekStar_reward+" where uid="+anchorTuple.getElement();
							
							RedisOperat.getInstance().zincrby(RedisContant.host, RedisContant.port6379, "anchor:all", Integer.valueOf(Constant.anchor_weekStar_reward), anchorTuple.getElement());
							
							// 标记上周周星
							RedisOperat.getInstance().hset(RedisContant.host, RedisContant.port6379, RedisContant.WeekTitle, tuple.getElement(), anchorTuple.getElement(), 7*24*3600);

							pstmt.addBatch(strSql);
							
							_map.put("uid", anchorTuple.getElement());
							_map.put("type", "anchor");
							_map.put("gname", giftInfo.getGname());
							list.add(_map);
							
							anchors.add(anchorTuple.getElement());
							bl = true;
						}
					}
					if (user != null && user.size() > 0) {
						for(Tuple userTuple:user){

							sql = "insert into pay_week_star(uid,gid,usertype,cycle,scores,amount,adminuid,addtime)value("+userTuple.getElement()+","+tuple.getElement()+",2,'"+week+"',"+userTuple.getScore()+","+Constant.user_weekStar_reward+",0,"+lgNow+")";
							pstmt.addBatch(sql);
							
							tbName = "zhu_user.user_asset_"+userTuple.getElement().substring(userTuple.getElement().length()-2, userTuple.getElement().length());
							strSql = "update "+tbName+" set money=money+"+Constant.user_weekStar_reward+" where uid= "+userTuple.getElement();
							pstmt.addBatch(strSql);
							
							_map.put("uid", userTuple.getElement());
							_map.put("type", "user");
							_map.put("gname", giftInfo.getGname());
							list.add(_map);
							bl = true;
						}
					}
				}
				if (bl) {
					pstmt.executeBatch();
					con.commit();
					con.setAutoCommit(true);
					
					for(Map<String, Object> map:list){
						String url =  Constant.business_server_url+"/admin/refreshUserAsset?uid="+map.get("uid");
						HttpResponse<JsonNode> res = Unirest.get(url).asJson();
						org.json.JSONObject object = res.getBody().getObject();
						if(Integer.parseInt(object.get("code").toString()) == 200){
							logger.info("TimeService-giveWeekReward-refreshUserAsset update is sucess url:"+url);
						}else{
							logger.info("TimeService-giveWeekReward update err url:"+url);
						}
						
						//public static final String PrivateMsgId = "pri:msgid";
						Long msgId = RedisOperat.getInstance().incrBy(RedisContant.host, RedisContant.port6381, "pri:msgid", 1, 0);
						// 组装消息体
						Map<String, Object> msgMap = new HashMap<String, Object>();
						msgMap.put("msgId", msgId.intValue());
						msgMap.put("uid", 0);
						msgMap.put("nickname", "官方");
						msgMap.put("headimage", "");
						msgMap.put("sex", true);
						msgMap.put("level", "10");
						if ("anchor".equals(map.get("type"))) {
							msgMap.put("msg", "恭喜您获得上周 "+map.get("gname")+" 周星收礼榜第一名。");
						}else {
							msgMap.put("msg", "恭喜您获得上周 "+map.get("gname")+" 周星贡献榜第一名。");
						}
						msgMap.put("sendtime", lgNow);
						msgMap.put("relation", 0);
						msgMap.put("msgType", 0);

						String msgbody = JSONObject.toJSONString(msgMap);
						// 添加redis
						RedisOperat.getInstance().hset(RedisContant.host, RedisContant.port6379, "pri:msg:"+map.get("uid"), msgId.toString(), msgbody, 30*24*60*60);
					}
				}
			} catch (Exception e) {
				try {
					if (!con.isClosed()) {
						con.rollback();
						con.setAutoCommit(true);
						if (anchors != null && anchors.size() >0) {
							for(String anchor:anchors){
								RedisOperat.getInstance().zincrby(RedisContant.host, RedisContant.port6379, "anchor:all", -100000, anchor);
							}
						}
					}
				} catch (Exception e2) {
					System.out.println("giveWeekReward-exc-exc:" + e2.toString());
				}

				System.out.println("giveWeekReward-exc:" + e.toString());
			}finally {
				try {
					if (pstmt != null) {
						pstmt.close();
					}
					if (con != null) {
						con.close();
					}
				} catch (Exception e2) {
					System.out.println("giveWeekReward-finally-exc:"+e2.toString());
				}
			}
		}
	}
	
	
	/**
	 * 周星定时运行 每周一 零时零分零秒 执行
	 */
	public void executeWeekStar(){

		try {
			NoticeAPIUtile.noticeApi("updateGiftAct", "adminid", "admin");
		} catch (Exception e) {
			logger.info("TimeService-executeWeekStar Exception:",e);
		}
		
		OtherRedisService.getInstance().setGiftVer();
	}
	
	/**
	 * 公会房间总消耗,每日凌晨3点执行 统计上一天总消耗加到表union_info的totalmoney字段
	 */
	public void addYestodayTotalMoneyForUnion(){
		
		//获取所有公会的上月的房间总消耗
		HashMap<Integer, Long> moneyMapOfYestoday = UnionDao.getIns().getTotalConsumeYestodayOfUnion();
		
		//前一天的总消耗增加到union_info的totalmoney字段
		UnionDao.getIns().updateTotalmoneyByUnionId(moneyMapOfYestoday);
	}
	
	/**
	 * 主播房间总消耗,每月1日统计上月总消耗加到表union_anchor_ref的totalmoney字段
	 */
	public void addLastMonthTotalMoneyForAnchor(){
		//获取所有公会主播id
		List<Integer> list = UnionDao.getIns().getAllAnchorIDs();
		
		//获取所有公会主播的上月的房间总消耗
		HashMap<Integer, Long> moneyMapOfLastMonth = UnionDao.getIns().getAnchorTotalMoneyOfLastMonth(list);
		
		//循环公会主播id，将公会主播上月总消耗增加到union_anchor_ref的totalmoney字段
		for (Integer anchorid : list) {
			if(moneyMapOfLastMonth.get(anchorid) != null)
				UnionDao.getIns().updateTotalmoneyByAnchorId(anchorid, moneyMapOfLastMonth.get(anchorid));
		}
	}
	
	/**
	 * 过期守护每日成长值扣除
	 */
	public void minusExpOfUserGuard(){
		int defaultExp = 5;
		int defaultExpForRecord = -5;
		List<HashMap<String,Object>> expireGuards = UserGuardInfoDao.getIns().getExpireGuard();
		for(HashMap<String,Object> map: expireGuards){
			int gid = new Integer(map.get("gid").toString());
			int uid = new Integer(map.get("uid").toString());
			//删除房间内某个人的守护排名信息（金币）
			RedisOperat.getInstance().zrem(RedisContant.host, RedisContant.port6379, RedisContant.roomAllGuardSort+map.get("roomid"), map.get("uid").toString());
				
			int roomid = new Integer(map.get("roomid").toString());
			int rsc = UserGuardInfoDao.getIns().selExpRecordForDate(uid, roomid, gid);
			if(rsc==0){
				int rs = UserGuardInfoDao.getIns().updateExp(uid, roomid, gid, defaultExp);
				if(rs>0){
					UserGuardInfoDao.getIns().addExpRecord(uid, roomid,gid, defaultExpForRecord, 4);
				}else{
					logger.error("minusExpOfUserGuard =>  updateExp : 修改经验值失败");
					System.out.println("minusExpOfUserGuard =>  updateExp : 修改经验值失败");
				}
			}else{
				logger.error("minusExpOfUserGuard =>  selExpRecordForDate : 记录已存在");
				System.out.println("minusExpOfUserGuard =>  selExpRecordForDate : 记录已存在");
			}
		}
		logger.error("minusExpOfUserGuard =>  执行完毕");
		System.out.println("minusExpOfUserGuard =>  执行完毕");
	}
	
	/**
	 * 设备新增激活，新增注册，充值金额，充值人数
	 */
	@SuppressWarnings("unchecked")
	public void getCollect(){
		
		Long lgNow = System.currentTimeMillis()/1000;
		System.out.println("getCollect start *********************:");
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		long startTime = new Date().getTime();
		// 时间段
		Long etime = DateUtil.getDayBegin();
		Long stime = etime - 24*3600;
		
		// =1安卓 =2苹果 =3H5 =4Ipad =5WEB
		List<Integer> list = Arrays.asList(1,2,3,4,5);
		//获取所有的渠道集合
		List channelList = (List) OperateDao.getInstance().getAllChannelName(1, 0);
		//List<String> channelList = Arrays.asList("baofengcj");
		for(int i=0;i<channelList.size();i++){
			//String channelName = channelList.get(i);
			String channelName = ((ChannelModel)channelList.get(i)).getChannelCode();
			if(!channelName.equals("16wifi")){
			//当日
			//获取某个渠道当日的充值总人数和充值金额
			Map<String, Object> paySummary1 = TimerTaskDao.getInstance().getPaySummary(stime, etime,channelName);
			//获取某个渠道当日注册用户的充值总人数和充值金额
			Map<String, Object> payCounts1 = TimerTaskDao.getInstance().getRegistPaySummary(stime, etime, channelName);
			/*//2天内
			stime = stime - 24*3600;
			//获取某个渠道2日的充值总人数和充值金额
			Map<String, Object> paySummary2 = TimerTaskDao.getInstance().getPaySummary(stime, etime,channelName);
			//获取某个渠道2日注册用户的充值总人数和充值金额
			Map<String, Object> payCounts2 = TimerTaskDao.getInstance().getRegistPaySummary(stime, etime, channelName);
			//3天内
			stime = stime - 24*3600;
			//获取某个渠道3日的充值总人数和充值金额
			Map<String, Object> paySummary3 = TimerTaskDao.getInstance().getPaySummary(stime, etime,channelName);
			//获取某个渠道3日注册用户的充值总人数和充值金额
			Map<String, Object> payCounts3 = TimerTaskDao.getInstance().getRegistPaySummary(stime, etime, channelName);
			//4天内
			stime = stime - 24*3600;
			//获取某个渠道4日的充值总人数和充值金额
			Map<String, Object> paySummary4 = TimerTaskDao.getInstance().getPaySummary(stime, etime,channelName);
			//获取某个渠道4日注册用户的充值总人数和充值金额
			Map<String, Object> payCounts4 = TimerTaskDao.getInstance().getRegistPaySummary(stime, etime, channelName);
			//5天内
			stime = stime - 24*3600;
			//获取某个渠道5日的充值总人数和充值金额
			Map<String, Object> paySummary5 = TimerTaskDao.getInstance().getPaySummary(stime, etime,channelName);
			//获取某个渠道5日注册用户的充值总人数和充值金额
			Map<String, Object> payCounts5 = TimerTaskDao.getInstance().getRegistPaySummary(stime, etime, channelName);
			//6天内
			stime = stime - 24*3600;
			//获取某个渠道6日的充值总人数和充值金额
			Map<String, Object> paySummary6 = TimerTaskDao.getInstance().getPaySummary(stime, etime,channelName);
			//获取某个渠道6日注册用户的充值总人数和充值金额
			Map<String, Object> payCounts6 = TimerTaskDao.getInstance().getRegistPaySummary(stime, etime, channelName);
			//7天内
			stime = stime - 24*3600;
			//获取某个渠道7日的充值总人数和充值金额
			Map<String, Object> paySummary7 = TimerTaskDao.getInstance().getPaySummary(stime, etime,channelName);
			//获取某个渠道7日注册用户的充值总人数和充值金额
			Map<String, Object> payCounts7 = TimerTaskDao.getInstance().getRegistPaySummary(stime, etime, channelName);
			//14天内
			stime = stime - 7*24*3600;
			//获取某个渠道14日的充值总人数和充值金额
			Map<String, Object> paySummary14 = TimerTaskDao.getInstance().getPaySummary(stime, etime,channelName);
			//获取某个渠道14日注册用户的充值总人数和充值金额
			Map<String, Object> payCounts14 = TimerTaskDao.getInstance().getRegistPaySummary(stime, etime, channelName);
			//30天内
			stime = stime - 16*24*3600;
			//获取某个渠道30日的充值总人数和充值金额
			Map<String, Object> paySummary30 = TimerTaskDao.getInstance().getPaySummary(stime, etime,channelName);
			//获取某个渠道30日注册用户的充值总人数和充值金额
			Map<String, Object> payCounts30 = TimerTaskDao.getInstance().getRegistPaySummary(stime, etime, channelName);
			//60天内
			stime = stime - 30*24*3600;
			//获取某个渠道60日的充值总人数和充值金额
			Map<String, Object> paySummary60 = TimerTaskDao.getInstance().getPaySummary(stime, etime,channelName);
			//获取某个渠道60日注册用户的充值总人数和充值金额
			Map<String, Object> payCounts60 = TimerTaskDao.getInstance().getRegistPaySummary(stime, etime, channelName);
			//90天内
			stime = stime - 30*24*3600;
			//获取某个渠道90日的充值总人数和充值金额
			Map<String, Object> paySummary90 = TimerTaskDao.getInstance().getPaySummary(stime, etime,channelName);
			//获取某个渠道90日注册用户的充值总人数和充值金额
			Map<String, Object> payCounts90 = TimerTaskDao.getInstance().getRegistPaySummary(stime, etime, channelName);*/
			for(Integer os:list){
					int equipmentsNew = 0; //平台总激活设备数
					int registers = 0; //平台总注册数
					int equipments = 0; //平台总活跃设备数
					int actives = 0; //平台总活跃账号数
					int amounts = 0; //平台总充值金额
					int payUsers = 0; //平台总充值人数
					int registAccounts =0;		//当日注册用户充值总金额
					int registPayUsers = 0;		//当日注册用户充值总人数
					equipmentsNew = TimerTaskDao.getInstance().getEquipmentsNew(os, stime, etime,channelName);
					registers = TimerTaskDao.getInstance().getRegisters(os, stime, etime,channelName);
					equipments = TimerTaskDao.getInstance().getEquipments(os, stime, etime,channelName);
					actives = RedisOperat.getInstance().hlen(RedisContant.host, RedisContant.port6379, RedisContant.User_login+os+":"+formatter.format(new Date(stime*1000)));
					if (!paySummary1.isEmpty()) {
						if (paySummary1.containsKey("os"+os)) {
							Map<String, Integer> mp = (Map<String, Integer>) paySummary1.get("os"+os);
							amounts = mp.get("amount");
							payUsers = mp.get("uid");
						}
					}
					if (!payCounts1.isEmpty()) {
						if (payCounts1.containsKey("os"+os)) {
							Map<String, Integer> mp = (Map<String, Integer>) payCounts1.get("os"+os);
							registAccounts = mp.get("amount");
							registPayUsers = mp.get("uid");
						}
					}
					
					/*if (!paySummary2.isEmpty()) {
						if (paySummary2.containsKey("os"+os)) {
							Map<String, Integer> mp = (Map<String, Integer>) paySummary2.get("os"+os);
							amounts = mp.get("amount");
							payUsers = mp.get("uid");
						}
					}
					if (!payCounts2.isEmpty()) {
						if (payCounts2.containsKey("os"+os)) {
							Map<String, Integer> mp = (Map<String, Integer>) payCounts2.get("os"+os);
							registAccounts = mp.get("amount");
							registPayUsers = mp.get("uid");
						}
					}
					
					if (!paySummary3.isEmpty()) {
						if (paySummary3.containsKey("os"+os)) {
							Map<String, Integer> mp = (Map<String, Integer>) paySummary3.get("os"+os);
							amounts = mp.get("amount");
							payUsers = mp.get("uid");
						}
					}
					if (!payCounts3.isEmpty()) {
						if (payCounts3.containsKey("os"+os)) {
							Map<String, Integer> mp = (Map<String, Integer>) payCounts3.get("os"+os);
							registAccounts = mp.get("amount");
							registPayUsers = mp.get("uid");
						}
					}
					
					if (!paySummary4.isEmpty()) {
						if (paySummary4.containsKey("os"+os)) {
							Map<String, Integer> mp = (Map<String, Integer>) paySummary4.get("os"+os);
							amounts = mp.get("amount");
							payUsers = mp.get("uid");
						}
					}
					if (!payCounts4.isEmpty()) {
						if (payCounts4.containsKey("os"+os)) {
							Map<String, Integer> mp = (Map<String, Integer>) payCounts4.get("os"+os);
							registAccounts = mp.get("amount");
							registPayUsers = mp.get("uid");
						}
					}
					
					if (!paySummary5.isEmpty()) {
						if (paySummary5.containsKey("os"+os)) {
							Map<String, Integer> mp = (Map<String, Integer>) paySummary5.get("os"+os);
							amounts = mp.get("amount");
							payUsers = mp.get("uid");
						}
					}
					if (!payCounts5.isEmpty()) {
						if (payCounts5.containsKey("os"+os)) {
							Map<String, Integer> mp = (Map<String, Integer>) payCounts5.get("os"+os);
							registAccounts = mp.get("amount");
							registPayUsers = mp.get("uid");
						}
					}
					
					if (!paySummary6.isEmpty()) {
						if (paySummary6.containsKey("os"+os)) {
							Map<String, Integer> mp = (Map<String, Integer>) paySummary6.get("os"+os);
							amounts = mp.get("amount");
							payUsers = mp.get("uid");
						}
					}
					if (!payCounts6.isEmpty()) {
						if (payCounts6.containsKey("os"+os)) {
							Map<String, Integer> mp = (Map<String, Integer>) payCounts6.get("os"+os);
							registAccounts = mp.get("amount");
							registPayUsers = mp.get("uid");
						}
					}
					
					if (!paySummary7.isEmpty()) {
						if (paySummary7.containsKey("os"+os)) {
							Map<String, Integer> mp = (Map<String, Integer>) paySummary7.get("os"+os);
							amounts = mp.get("amount");
							payUsers = mp.get("uid");
						}
					}
					if (!payCounts7.isEmpty()) {
						if (payCounts7.containsKey("os"+os)) {
							Map<String, Integer> mp = (Map<String, Integer>) payCounts7.get("os"+os);
							registAccounts = mp.get("amount");
							registPayUsers = mp.get("uid");
						}
					}
					
					if (!paySummary14.isEmpty()) {
						if (paySummary14.containsKey("os"+os)) {
							Map<String, Integer> mp = (Map<String, Integer>) paySummary14.get("os"+os);
							amounts = mp.get("amount");
							payUsers = mp.get("uid");
						}
					}
					if (!payCounts14.isEmpty()) {
						if (payCounts14.containsKey("os"+os)) {
							Map<String, Integer> mp = (Map<String, Integer>) payCounts14.get("os"+os);
							registAccounts = mp.get("amount");
							registPayUsers = mp.get("uid");
						}
					}
					
					if (!paySummary30.isEmpty()) {
						if (paySummary30.containsKey("os"+os)) {
							Map<String, Integer> mp = (Map<String, Integer>) paySummary30.get("os"+os);
							amounts = mp.get("amount");
							payUsers = mp.get("uid");
						}
					}
					if (!payCounts30.isEmpty()) {
						if (payCounts30.containsKey("os"+os)) {
							Map<String, Integer> mp = (Map<String, Integer>) payCounts30.get("os"+os);
							registAccounts = mp.get("amount");
							registPayUsers = mp.get("uid");
						}
					}
					
					
					if (!paySummary60.isEmpty()) {
						if (paySummary60.containsKey("os"+os)) {
							Map<String, Integer> mp = (Map<String, Integer>) paySummary60.get("os"+os);
							amounts = mp.get("amount");
							payUsers = mp.get("uid");
						}
					}
					if (!payCounts60.isEmpty()) {
						if (payCounts60.containsKey("os"+os)) {
							Map<String, Integer> mp = (Map<String, Integer>) payCounts60.get("os"+os);
							registAccounts = mp.get("amount");
							registPayUsers = mp.get("uid");
						}
					}
					
					if (!paySummary90.isEmpty()) {
						if (paySummary90.containsKey("os"+os)) {
							Map<String, Integer> mp = (Map<String, Integer>) paySummary90.get("os"+os);
							amounts = mp.get("amount");
							payUsers = mp.get("uid");
						}
					}
					if (!payCounts90.isEmpty()) {
						if (payCounts90.containsKey("os"+os)) {
							Map<String, Integer> mp = (Map<String, Integer>) payCounts90.get("os"+os);
							registAccounts = mp.get("amount");
							registPayUsers = mp.get("uid");
						}
					}*/
					if (equipmentsNew+registers+equipments+actives+amounts+payUsers+registAccounts+registPayUsers>0) {
						TimerTaskDao.getInstance().addAnalysisSummary(os,stime, equipmentsNew, registers, equipments, actives, amounts, payUsers, lgNow,channelName,registAccounts,registPayUsers);
					}
				}
			}
		}
		long endTime = new Date().getTime();
		System.out.println("总用时："+(endTime-startTime));
		System.out.println("getCollect end *********************:" + (System.currentTimeMillis()/1000 - lgNow));
	}
	
	/**
	 * 新增用户
	 */
	public void getNewRegistser(){

		Long etime = DateUtil.getDayBegin();
		Long stime = etime - 24*3600;
		// 新增用户
		List<Map<String, Object>> registers = TimerTaskDao.getInstance().getRegisters(stime, etime);
		TimerTaskDao.getInstance().addRemain(stime, registers);
	}
	
	public void getRemain(){
		
		System.out.println("getRemain*******start");
		
		try {

			Long etime = DateUtil.getDayBegin();
			Long stime = etime - 24*3600;
				
			// 登录用户
			List<Integer> logins = TimerTaskDao.getInstance().getLogins(stime, etime);
			System.out.println("getRemain   time:" + stime + " size:" + logins.size());
			Map<String, Object> register = null;
			
			for(Integer uid:logins){
				// 次日留存
				int type = 1;
				register = TimerTaskDao.getInstance().getRegisterByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (register != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)register.get("os"), (String)register.get("channel"), type,1);
					continue;
				}
				// 2日留存
				type = 2;
				register = TimerTaskDao.getInstance().getRegisterByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (register != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)register.get("os"), (String)register.get("channel"), type,1);
					continue;
				}
				// 3日留存
				type = 3;
				register = TimerTaskDao.getInstance().getRegisterByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (register != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)register.get("os"), (String)register.get("channel"), type,1);
					continue;
				}
				// 4日留存
				type = 4;
				register = TimerTaskDao.getInstance().getRegisterByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (register != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)register.get("os"), (String)register.get("channel"), type,1);
					continue;
				}
				// 5日留存
				type = 5;
				register = TimerTaskDao.getInstance().getRegisterByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (register != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)register.get("os"), (String)register.get("channel"), type,1);
					continue;
				}
				// 6日留存
				type = 6;
				register = TimerTaskDao.getInstance().getRegisterByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (register != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)register.get("os"), (String)register.get("channel"), type,1);
					continue;
				}
				// 7日留存
				type = 7;
				register = TimerTaskDao.getInstance().getRegisterByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (register != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)register.get("os"), (String)register.get("channel"), type,1);
					continue;
				}
				// 14日留存
				type = 14;
				register = TimerTaskDao.getInstance().getRegisterByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (register != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)register.get("os"), (String)register.get("channel"), type,1);
					continue;
				}
				// 30日留存
				type = 30;
				register = TimerTaskDao.getInstance().getRegisterByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (register != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)register.get("os"), (String)register.get("channel"), type,1);
					continue;
				}
			}
			System.out.println("getRemain*******end");
		} catch (Exception e) {
			System.out.println("getRemain:" + e);
		}
	}
	
	
	public void getLtv(){
		System.out.println("getLtv*******start");
		try {
			Long etime = DateUtil.getDayBegin();
			Long stime = etime - 24*3600;
			// 登录用户
			List<Integer> logins = TimerTaskDao.getInstance().getLogins(stime, etime);
			System.out.println("getLtv  time:" + stime + " size:" + logins.size());
			Map<String, Object> register = null;
			for(Integer uid:logins){
				// 次日留存
				int type = 1;
				register = TimerTaskDao.getInstance().getRegisterByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (register != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)register.get("os"), (String)register.get("channel"), type,1);
					continue;
				}
				// 2日留存
				type = 2;
				register = TimerTaskDao.getInstance().getRegisterByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (register != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)register.get("os"), (String)register.get("channel"), type,1);
					continue;
				}
				// 3日留存
				type = 3;
				register = TimerTaskDao.getInstance().getRegisterByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (register != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)register.get("os"), (String)register.get("channel"), type,1);
					continue;
				}
				// 4日留存
				type = 4;
				register = TimerTaskDao.getInstance().getRegisterByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (register != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)register.get("os"), (String)register.get("channel"), type,1);
					continue;
				}
				// 5日留存
				type = 5;
				register = TimerTaskDao.getInstance().getRegisterByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (register != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)register.get("os"), (String)register.get("channel"), type,1);
					continue;
				}
				// 6日留存
				type = 6;
				register = TimerTaskDao.getInstance().getRegisterByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (register != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)register.get("os"), (String)register.get("channel"), type,1);
					continue;
				}
				// 7日留存
				type = 7;
				register = TimerTaskDao.getInstance().getRegisterByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (register != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)register.get("os"), (String)register.get("channel"), type,1);
					continue;
				}
				// 14日留存
				type = 14;
				register = TimerTaskDao.getInstance().getRegisterByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (register != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)register.get("os"), (String)register.get("channel"), type,1);
					continue;
				}
				// 30日留存
				type = 30;
				register = TimerTaskDao.getInstance().getRegisterByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (register != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)register.get("os"), (String)register.get("channel"), type,1);
					continue;
				}
			}
			System.out.println("getLtv*******end");
		} catch (Exception e) {
			System.out.println("getLtv:" + e);
		}
	}
	
	
	public void getLoginer(){
		System.out.println("getLoginer*******start");
		try {
			Long etime = DateUtil.getDayBegin();
			Long stime = etime - 24*3600;
			// 登录用户
			List<Integer> logins = TimerTaskDao.getInstance().getLogins(stime, etime);
			System.out.println("getLoginer   time:" + stime + " size:" + logins.size());
			Map<String, Object> loginer = null;
			
			for(Integer uid:logins){
				// 次日留存
				int type = 1;
				loginer = TimerTaskDao.getInstance().getLoginerByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (loginer != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)loginer.get("os"), (String)loginer.get("channel"), type,2);
					continue;
				}
				// 2日留存
				type = 2;
				loginer = TimerTaskDao.getInstance().getLoginerByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (loginer != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)loginer.get("os"), (String)loginer.get("channel"), type,2);
					continue;
				}
				// 3日留存
				type = 3;
				loginer = TimerTaskDao.getInstance().getLoginerByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (loginer != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)loginer.get("os"), (String)loginer.get("channel"), type,2);
					continue;
				}
				// 4日留存
				type = 4;
				loginer = TimerTaskDao.getInstance().getLoginerByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (loginer != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)loginer.get("os"), (String)loginer.get("channel"), type,2);
					continue;
				}
				// 5日留存
				type = 5;
				loginer = TimerTaskDao.getInstance().getLoginerByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (loginer != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)loginer.get("os"), (String)loginer.get("channel"), type,2);
					continue;
				}
				// 6日留存
				type = 6;
				loginer = TimerTaskDao.getInstance().getLoginerByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (loginer != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)loginer.get("os"), (String)loginer.get("channel"), type,2);
					continue;
				}
				// 7日留存
				type = 7;
				loginer = TimerTaskDao.getInstance().getLoginerByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (loginer != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)loginer.get("os"), (String)loginer.get("channel"), type,2);
					continue;
				}
				// 14日留存
				type = 14;
				loginer = TimerTaskDao.getInstance().getLoginerByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (loginer != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)loginer.get("os"), (String)loginer.get("channel"), type,2);
					continue;
				}
				// 30日留存
				type = 30;
				loginer = TimerTaskDao.getInstance().getLoginerByUidAndTime(stime-type*24*3600, etime-type*24*3600, uid);
				if (loginer != null) {
					TimerTaskDao.getInstance().updRemain(stime-type*24*3600, (Integer)loginer.get("os"), (String)loginer.get("channel"), type,2);
					continue;
				}
			}
			System.out.println("getRemain*******end");
		} catch (Exception e) {
			System.out.println("getRemain:" + e);
		}
	}
	
	/**
	 * 主播结算基础数据
	 * @throws ParseException
	 */
	public void getWages() throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		
		Long etime = DateUtil.getDayBegin();
		Long stime = etime - 24*3600;

		String times = sdf.format(new Date(stime*1000));
		
		TimerTaskDao.getInstance().getValidDateTime(stime, etime,Integer.parseInt(times));
		TimerTaskDao.getInstance().getCredit(stime, etime, Integer.parseInt(times));
		TimerTaskDao.getInstance().getWeekStar(stime, etime, Integer.parseInt(times));
		TimerTaskDao.getInstance().getActivity(stime, etime, Integer.parseInt(times));
		TimerTaskDao.getInstance().getExchange(stime, etime, Integer.parseInt(times));
		TimerTaskDao.getInstance().getWithdraw(stime, etime, Integer.parseInt(times));

//		Long etime = DateUtil.dateToLong("2016-11-28", "yyyy-MM-dd", "other", 1);
//		Long stime = 0L;
//		String times = "";
//		
//		while(etime <= 1480262400){
//			stime = etime - 24*3600;
//			times = sdf.format(new Date(stime*1000));
//			
//			TimerTaskDao.getInstance().getValidDateTime(stime, etime,Integer.parseInt(times));
//			TimerTaskDao.getInstance().getCredit(stime, etime, Integer.parseInt(times));
//			TimerTaskDao.getInstance().getWeekStar(stime, etime, Integer.parseInt(times));
//			TimerTaskDao.getInstance().getActivity(stime, etime, Integer.parseInt(times));
//			TimerTaskDao.getInstance().getExchange(stime, etime, Integer.parseInt(times));
//			TimerTaskDao.getInstance().getWithdraw(stime, etime, Integer.parseInt(times));
//			
//			etime = etime+24*3600;
//		}
	}
	
	/**
	 * 获取礼物消耗数据 静态化
	 * @throws ParseException
	 */
	public void getGiftSendS() throws ParseException{

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

		Long etime = DateUtil.getDayBegin();
		Long stime = etime - 24*3600;

		String YM = sdf.format(new Date(stime*1000));

		TimerTaskDao.getInstance().getGiftSends(stime, etime, YM);
	}
	
	/**
	 * 砸蛋静态数据统计
	 * 铜锤 10金币[礼物排序 小色猪*5  小色猪*7  小黄鸡*2 小黄鸡*3  亲亲*3  亲亲*6  金砖    跑车]
	 * 金锤 25金币[礼物排序 小色猪*10 小色猪*15 小黄鸡*5 小黄鸡*7  亲亲*7  亲亲*10 甲壳虫   兰博基尼]
	 * 紫锤 50金币[礼物排序 小色猪*15 小色猪*20 小黄鸡*8 小黄鸡*10 亲亲*10 亲亲*15 兰博基尼 飞机]
	 */
	public void getSmashingEggs(){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		Long etime = DateUtil.getDayBegin();
		Long stime = etime - 24*3600;
		String times = sdf.format(new Date(stime*1000));
		
		TimerTaskDao.getInstance().getSmashingEggs(stime, etime, Integer.valueOf(times));
		
//		Long etime = 1479312000L;
//		Long stime = 0L;
//		String times = "";
//		
//		while(etime <= 1479484800){
//			stime = etime - 24*3600;
//			times = sdf.format(new Date(stime*1000));
//			
//			TimerTaskDao.getInstance().getSmashingEggs(stime, etime, Integer.valueOf(times));
//			
//			etime = etime+24*3600;
//		}
	}
	
	/**
	 * 每5小时执行一次
	 * 回收未被抢的红包
	 */
	public void getRedenve(){
		TimerTaskDao.getInstance().getRedenve();
	}
	
	/**
	 * 直播间 公告推送
	 */
	@SuppressWarnings("unchecked")
	public void pushRoomChat(){
		
		Long lgNow = System.currentTimeMillis()/1000;
		
		Map<String, Object> map = null;
		String lpop = RedisOperat.getInstance().lpop(RedisContant.host, RedisContant.port6380, RedisContant.roomChat);
		
		if (StringUtils.isEmpty(lpop)) {
			// 无缓存 读数据库
			map = TimerTaskDao.getInstance().getRoomChatList();
		}else {
			// 有缓存
			map = (Map<String, Object>)JSONObject.parse(lpop);
		}

		// 待发送的信息
		System.out.println("pushRoomChat-room_all msg:" + map);
		if (map != null) {
			
			if ("1".equals(map.get("isvalid").toString()) && Long.valueOf(map.get("starttime").toString()) <= lgNow && Long.valueOf(map.get("endtime").toString()) > lgNow) {
				Map<String, Object> msgMap = new HashMap<String, Object>();
				msgMap.put("msg", map.get("content"));
				msgMap.put("cometProtocol", 10);
				
				String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret, "appKey=" + VarConfigUtils.ServiceKey,
						"msgBody=" + JSONObject.toJSONString(msgMap));

				Future<HttpResponse<JsonNode>> asJsonAsync = Unirest.post(Constant.im_publish_room_all_url)
						.field("appKey", VarConfigUtils.ServiceKey)
						.field("msgBody", JSONObject.toJSONString(msgMap))
						.field("sign", signParams).asJsonAsync();
				
				try {
					if (asJsonAsync != null){
						if (asJsonAsync.get().getStatus() == 200) {
							System.out.println("pushRoomChat-room_all: success  msg:"+ signParams);
						}else {
							System.out.println("pushRoomChat-room_all: fail  msg:"+ signParams + " status:"+asJsonAsync.get().getStatusText());
						}
					}else {
						System.out.println("pushRoomChat-room_all: null  msg:"+ signParams);
					}
				} catch (InterruptedException e) {
					System.out.println("pushRoomChat-InterruptedException " + e.getMessage());
				} catch (ExecutionException e) {
					System.out.println("pushRoomChat-ExecutionException " + e.getMessage());
				}
			}
		}
	}
}