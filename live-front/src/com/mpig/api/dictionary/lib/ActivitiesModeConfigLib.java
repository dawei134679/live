package com.mpig.api.dictionary.lib;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.utils.RedisContant;


/*
 * 活动模板配置
 */
public class ActivitiesModeConfigLib {
	
	private static final Logger logger = Logger.getLogger(ActivitiesModeConfigLib.class);
	
	public static final int TYPE0 = 0;
	public static final int TYPE_TREASURE = 1;
	
	private static final int TIME_OUT = 60*60*24*30;
	
	//！！！！不要Am开头定义redis在otherRedis中，此开头为活动模板开头数据！！！
		//+活动名+开始时间+结束时间+阶段时间起点
	//实时插入
	public static final String AmUnionValue_NameStartDurTs = "AmUnionVal:";			//活动模板0 工会阶段总值
	public static final String AmAnchorValue_NameStartDurTs = "AmAnchorVal:";		//活动模板0 主播阶段总值
	public static final String AmUserValue_NameStartDurTs = "AmUserVal:";			//活动模板0 用户阶段总值
	
	public static final String AmAnchorValueSum_NameStartBeginTs = "AmAnchorValSum:";	//活动模板0 主播总值
	public static final String AmUserValueSum_NameStartBeginTs = "AmUserValSum:";		//活动模板0 用户总值
	public static final String AmUnionValueSum_NameStartBeginTs = "AmUnionValSum:";	//活动模板0 工会总值


	public static final String AmUserValueTreasure_NameStartDurTs = "AmUserTreasureVal:";	//活动模板0 用户阶段总值
	public static final String AmUserValueTreasureAll_NameStartDurTs = "AmUserTreasureValAll:";	//活动模板0 用户阶段总值

//	统计
    public static final String AmAnchorResult_NameStartDurTs = "AmAnchorResult:";		    //活动模板0 主播阶段统计值         1
    public static final String AmAnchorResultSum_NameStartBeginTs = "AmAnchorResultSum:";	     //活动模板0 主播总统计值          1.1
    public static final String AmAnchorValueDurTopResultSum_NameStartBeginTs = "AmAnchorValueTopSum:"; //活动模板0 主播阶段值各期最高     1.2
    public static final String AmAnchorUserDurTopResult_NameStartBeginTs = "AmAnchorUserDurTopSum:";//活动模板0 主播用户阶段值各期最高快照 1.3

	
	static public class AMTimeConfig{		
		String activitiesName = null;		//活动名字
		int typeIdx = 0;					//活动类型 目前0
		long start = 0;						//活动开始时间	（秒）
		long end  = 0;						//活动结束时间
		
		long duraion = 0;					//活动运行间隔
		long delay = 0;						
											//astart = start + n*duration + delay
		long subduration = 0;				//aend = start + n*duration + delay + subduration
		
		List<Integer> listgid = new ArrayList<Integer>();	//数据收集的GID
		
		public String getActivitiesName() {
			return activitiesName;
		}
		public void setActivitiesName(String activitiesName) {
			this.activitiesName = activitiesName;
		}
		public int getTypeIdx() {
			return typeIdx;
		}
		public void setTypeIdx(int typeIdx) {
			this.typeIdx = typeIdx;
		}
		public long getStart() {
			return start;
		}
		public void setStart(long start) {
			this.start = start;
		}
		public long getEnd() {
			return end;
		}
		public void setEnd(long end) {
			this.end = end;
		}
		public long getDuraion() {
			return duraion;
		}
		public void setDuraion(long duraion) {
			this.duraion = duraion;
		}
		public long getDelay() {
			return delay;
		}
		public void setDelay(long delay) {
			this.delay = delay;
		}
		public long getSubduration() {
			return subduration;
		}
		public void setSubduration(long subduration) {
			this.subduration = subduration;
		}
		public List<Integer> getListgid() {
			return listgid;
		}
		public void setListgid(List<Integer> listgid) {
			this.listgid = listgid;
		}
		public long isInduration(long tm , boolean isInAllDur) {
			long nTsStart = -1;
			
			if(tm < start || tm >= end){
				return nTsStart;
			}
	
			//全统计
			if(isInAllDur){
				nTsStart = start;
			}else{
				//分时段统计
				long tsInDur = (tm -start) % duraion;
				long durStart = tm - tsInDur;
				
				long durSubStart = durStart + delay;
				long durSubEnd = durStart + delay + subduration;
				
				if(tm >= durSubStart && tm < durSubEnd ){
					nTsStart = durSubStart;
				}	
			}
			
			return nTsStart;
		}

	}
	
	private ActivitiesModeConfigLib(){
	}
	
	static private final ActivitiesModeConfigLib ins = new ActivitiesModeConfigLib();
	public static ActivitiesModeConfigLib getIns() {
		if(0 == timecfgs.size()){
			//TODO	TEST  from redis or db plz
			InitConfig();
		}
		return ins;
	}
	
	/*
	 * 各个活动开始结束时间点，和统计间隔
	 */
	static private ConcurrentHashMap<Long, AMTimeConfig> timecfgs = new ConcurrentHashMap<Long, AMTimeConfig>();

	public static ConcurrentHashMap<Long, AMTimeConfig> getTimecfgs() {
		return timecfgs;
	}
	public static void setTimecfgs(ConcurrentHashMap<Long, AMTimeConfig> timecfgs) {
		ActivitiesModeConfigLib.timecfgs = timecfgs;
	}
	
	static public void InitConfig(){
		 ConcurrentHashMap<Long, AMTimeConfig> data  = getTimecfgs();
		 data.clear();
		 long start = 1490702400;
		 long end = 1490763600;

		 AMTimeConfig cfg1 = new AMTimeConfig();
		 cfg1.setTypeIdx(TYPE0);
		 cfg1.setStart(start);
		 cfg1.setEnd(end);
		 cfg1.setDuraion(60*60);
		 cfg1.setDelay(0);
		 cfg1.setSubduration(60*60);
		 cfg1.setActivitiesName("yahaha");
		 cfg1.getListgid().clear();
		 cfg1.getListgid().add(4);

		 long activitesIndx1 = 1;
		 data.put(activitesIndx1, cfg1);
		 
		 AMTimeConfig cfg2 = new AMTimeConfig();
		 cfg2.setTypeIdx(TYPE_TREASURE);
		 cfg2.setStart(start);
		 cfg2.setEnd(end);
		 cfg2.setDuraion(60*60);
		 cfg2.setDelay(60*30);
		 cfg2.setSubduration(60*15);
		 cfg2.setActivitiesName("Treasure");
		 cfg2.getListgid().clear();
		 cfg2.getListgid().add(4);

		 long activitesIndx2 = 2;
		 data.put(activitesIndx2, cfg2);
		 
		 logger.error("InitConfig.... " + JSONObject.toJSONString(data));
	}
	
	/**
	 * 通过活动名字，获取活动的统计rediskeytail
	 */
	public String getRedisKeyTailForCalc(String amname,int type,String constHead){
		String rt = null;
//				TODO
		return rt;
	}
	
	/**
	 * 获取对应时间，该活动的redis匹配字符串尾部	名字:start:end:阶段（差值%duration）
	 * @param tm
	 * @return
	 */
	public String getRedisRuleNameTail(AMTimeConfig cfg,long tm,boolean isInAllDur){
		String redisName = null;
		if(null == cfg || org.apache.commons.lang.StringUtils.isEmpty(cfg.getActivitiesName())){
			return redisName;
		}
		
		long tsStart = cfg.isInduration(tm,isInAllDur);
		if(tsStart < 0){
			return redisName;
		}
		
		redisName = "" + cfg.getTypeIdx() + cfg.getActivitiesName() + ":" + cfg.getStart() + ":" + cfg.getEnd() + ":" + tsStart;
		return redisName;
	}
	
	//zadd		zscore		zrevrank	zrevrangeWithScores		zincrby
	/*
	 * 送礼插入数据统计
	 * 无工会主播	unionid小于0 
	 */
	
	public void actModeSendGiftCheckPoint(int uid,int anchorid , int unionid , int gid , int numb , long tm){
		if(numb <= 0){
			logger.error("actModeSendGiftCheckPoint if(numb <= 0){");
			return;
		}
		if(null != timecfgs){
			for (AMTimeConfig value : timecfgs.values()) {  
				//是否活动包含该gid
				if(value.getListgid().contains(gid)){
					
					//获取redis对应字符串tail，如不在活动期间，则null
					String strRedisKeySum = getRedisRuleNameTail(value,tm,true);
					String strRedisKey =	getRedisRuleNameTail(value,tm,false);

					if(TYPE0 == value.getTypeIdx()){
						if(false == StringUtils.isEmpty(strRedisKeySum)){
							//主播总值
							if(anchorid>0){
								String strKeyAnchorSum = AmAnchorValueSum_NameStartBeginTs+strRedisKeySum;
								OtherRedisService.getInstance().zincrby(RedisContant.RedisNameOther, 
										strKeyAnchorSum, String.valueOf(anchorid), numb, TIME_OUT);
							}
							//用户总值
							if(uid>0){
								String strKeyUserSum = AmUserValueSum_NameStartBeginTs+strRedisKeySum;
								OtherRedisService.getInstance().zincrby(RedisContant.RedisNameOther, 
										strKeyUserSum, String.valueOf(uid), numb, TIME_OUT);	
							}
							//工会总值
							if(unionid>0){
								String strUionValueTsKey = AmUnionValueSum_NameStartBeginTs + strRedisKeySum;
								OtherRedisService.getInstance().zincrby(RedisContant.RedisNameOther, 
										strUionValueTsKey, String.valueOf(unionid), numb, TIME_OUT);
							}
						}
					  
						if(false == StringUtils.isEmpty(strRedisKey)){
							//工会阶段值
							if(unionid>0){
								String strUionValueTsKey = AmUnionValue_NameStartDurTs + strRedisKey;
								OtherRedisService.getInstance().zincrby(RedisContant.RedisNameOther, 
										strUionValueTsKey, String.valueOf(unionid), numb, TIME_OUT);
							}

							//主播阶段值
							if(anchorid>0){
								String strAnchorValueTsKey = AmAnchorValue_NameStartDurTs + strRedisKey;
								OtherRedisService.getInstance().zincrby(RedisContant.RedisNameOther, 
										strAnchorValueTsKey, String.valueOf(anchorid), numb, TIME_OUT);	
							}
							
							//用户阶段值
							if(uid>0){
								String strUserValueTsKey = AmUserValue_NameStartDurTs + strRedisKey;
								OtherRedisService.getInstance().zincrby(RedisContant.RedisNameOther, 
										strUserValueTsKey, String.valueOf(uid), numb, TIME_OUT);	
							}
						}
					}else if(TYPE_TREASURE == value.getTypeIdx()){	//宝箱活动
						//宝箱，阶段用户贡献
						if(uid>0 && false == StringUtils.isEmpty(strRedisKey)){
							String strUserTreasureTsKey = AmUserValueTreasure_NameStartDurTs + strRedisKey;
							OtherRedisService.getInstance().zincrby(RedisContant.RedisNameOther, 
									strUserTreasureTsKey, String.valueOf(uid), numb, TIME_OUT);
						}
						//宝箱，阶段总贡献大小
						if(uid>0 && false == StringUtils.isEmpty(strRedisKey)){
							String strUserTreasureAllTsKey = AmUserValueTreasureAll_NameStartDurTs + strRedisKey;
							OtherRedisService.getInstance().zincrby(RedisContant.RedisNameOther, 
									strUserTreasureAllTsKey, "treasure", numb, TIME_OUT);
						}
					}
//					OtherRedisService.getInstance().zincrby(redisName, strKey, member, score, expires)

				}
				
			}
			
		}
		
	}
	
	
}
