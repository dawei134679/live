package com.mpig.api.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.mpig.api.dictionary.lib.ActivitiesModeConfigLib;
import com.mpig.api.model.Am0AnchorResultUserValueTopModel;
import com.mpig.api.model.PrizeModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.service.IActivitiesModeService;
import com.mpig.api.service.IUserService;
import com.mpig.api.utils.RedisContant;

import redis.clients.jedis.Tuple;

@Service
public class ActivitiesModeServiceImp implements IActivitiesModeService{

	private static final Logger logger = Logger.getLogger(ActivitiesModeServiceImp.class);
			
	@Resource
	IUserService usersv;
	
	@Override
	public PrizeModel acceptPrize(int uid, String prize) {
		// TODO Auto-generated method stub
		PrizeModel rt = new PrizeModel();
		
		//TEST NO SVN UP 
		rt.setDesc("yahahaha~");
		
//		rt.setCriedt(1000);
//		rt.setWealth(200);
//		List<Map<Integer, Integer>> gifts = new ArrayList<>();
//		
//		for ( int i = 0; i< 5;i++){
//			Map<Integer, Integer> mapdata = new HashMap<>();
//			mapdata.put(i,i);
//			gifts.add(mapdata);	
//		}
//		rt.setGifts(gifts );
		
		logger.error("ActivitiesModeServiceImp acceptPrize todo");
		return rt;
	}

	//TODO 缓存哪些？？
	@Override
	public	void amtopsnap(int srcuid, long startsec,long endsec, String amname,ReturnModel rt) {	
		//获取活动对应的redis	TODO
		String rediskeyTopSnap = 
				ActivitiesModeConfigLib.getIns().getRedisKeyTailForCalc(
						amname,ActivitiesModeConfigLib.TYPE0,
						ActivitiesModeConfigLib.AmAnchorUserDurTopResult_NameStartBeginTs);
		
		String rediskeyAllStars = ActivitiesModeConfigLib.getIns().getRedisKeyTailForCalc(
				amname,ActivitiesModeConfigLib.TYPE0,
				ActivitiesModeConfigLib.AmAnchorResultSum_NameStartBeginTs);

		//是否存在
		if(StringUtils.isEmpty(rediskeyTopSnap) || StringUtils.isEmpty(rediskeyAllStars)){
			logger.error("tosy debug amtopsnap if(StringUtils.isEmpty(rediskeyTopSnap) || StringUtils.isEmpty(rediskeyAllStars))");
			return;
		}

		//获取统计结果
		Set<Tuple> dataSnap = OtherRedisService.getInstance().zrangeWithScores(RedisContant.RedisNameOther, rediskeyTopSnap, 0, 0);
		if(null == dataSnap || dataSnap.size() <= 0){
			return;
		}
		
		Set<Tuple> dataAllStar = OtherRedisService.getInstance().zrevrangeWithScores(RedisContant.RedisNameOther, rediskeyAllStars, 0, 0);
		if(null == dataAllStar || dataAllStar.size() <= 0){
			return;
		}
		
		//缓存，获取所有相关用户数据
		List<String> uids = new ArrayList<String>();
		List<Am0AnchorResultUserValueTopModel> ls = new ArrayList<Am0AnchorResultUserValueTopModel>();
		for	( Tuple item : dataSnap){
			double secTs = item.getScore();
			if(startsec <= secTs && secTs <= endsec){	//时间范围内
				Am0AnchorResultUserValueTopModel md = JSONObject.parseObject(item.getElement()
						, Am0AnchorResultUserValueTopModel.class);
				ls.add(md);
				uids.add(String.valueOf(md.getUid()));
				uids.add(String.valueOf(md.getAnchorid()));	
			}
		}
		Map<String, UserBaseInfoModel> userbaseMap = usersv.getUserbaseInfoByUid(uids.toArray(new String[0]));
		
		//拼装返回结果
		for(Am0AnchorResultUserValueTopModel md:ls){
			UserBaseInfoModel userbaseinfoA = userbaseMap.get(String.valueOf(md.getAnchorid()));
			UserBaseInfoModel userbaseinfoU = userbaseMap.get(String.valueOf(md.getUid()));
			
			md.setAheadpic(userbaseinfoA.getHeadimage());
			md.setAnickname(userbaseinfoA.getNickname());
			// calc title	取总数计算
			for(Tuple item : dataAllStar){
				if(Integer.valueOf(item.getElement()).intValue() == md.getAnchorid()){
					Double s = item.getScore();
					md.setAllstars(s.longValue());
					break;
				}
			}
			
			md.setUheadpic(userbaseinfoU.getHeadimage());
			md.setUnickname(userbaseinfoU.getNickname());
		}
		
		rt.setData(ls);
		return;
	}

}
