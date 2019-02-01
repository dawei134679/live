package com.mpig.api.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.mpig.api.model.LiveMicTimeModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.redis.service.RelationRedisService;
import com.mpig.api.service.IConfigService;
import com.mpig.api.service.ILiveService;
import com.mpig.api.service.ISlWifiService;
import com.mpig.api.service.IUserService;
import com.mpig.api.utils.VarConfigUtils;

@Service
public class SlWifiServiceImpl implements ISlWifiService{

	@Resource
	private IUserService userService;
	@Resource
	private IConfigService configService;
	@Resource
	private ILiveService liveService;
	
	@Override
	public void getLivingList(int page, ReturnModel returnModel) {
		List<Map<String, Object>> list = this.livingList(page);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		returnModel.setData(map);
	}
	
	/**
	 * 获取所有的
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> livingList(int page){
		Set<String> newLivingList = OtherRedisService.getInstance().getHotRoom(page);
		Set<String> recommendLivingList = OtherRedisService.getInstance().getRecommendRoom(page);
		
		newLivingList.removeAll(recommendLivingList);
		
		List<Map<String, Object>> recommendLiveUserDate = getLiveUserDate(recommendLivingList);
		List<Map<String, Object>> newLiveUserDate2 = getLiveUserDate(newLivingList);
		
		recommendLiveUserDate.addAll(newLiveUserDate2);
		
		return recommendLiveUserDate;
	}
	
	
	/**
	 * 根据用户uid list列表获取用户信息数组
	 * @param setLivingList
	 * @return
	 */
	public List<Map<String, Object>> getLiveUserDate(Set<String> setLivingList){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		if (setLivingList != null && setLivingList.size() > 0) {

			Map<String, UserBaseInfoModel> userbaseMap = userService.getUserbaseInfoByUid(setLivingList.toArray(new String[0]));
			Map<String, LiveMicTimeModel> livingMap = liveService.getLiveIngByUid(setLivingList.toArray(new String[0]));

			if (userbaseMap == null || livingMap == null) {
				return list;
			}
			UserBaseInfoModel userBaseInfoModel;
			LiveMicTimeModel liveMicTimeModel;
			for (String suid : setLivingList) {
				Map<String, Object> map = new HashMap<String, Object>();
				int uid = Integer.valueOf(suid);
				userBaseInfoModel = userbaseMap.get(suid);
				liveMicTimeModel = livingMap.get(suid);
				if (userBaseInfoModel == null || liveMicTimeModel == null || liveMicTimeModel.getType()|| !userBaseInfoModel.getLiveStatus()) {
					continue;
				} else {
					map.put("uid", suid);
					map.put("nickname", userBaseInfoModel.getNickname().trim());
					map.put("headimage", userBaseInfoModel.getHeadimage());
					map.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
					map.put("slogan", liveMicTimeModel.getSlogan().trim());
					map.put("city", StringUtils.isEmpty(liveMicTimeModel.getCity().trim()) ? VarConfigUtils.Location : liveMicTimeModel.getCity());
					map.put("enters", this.getRoomShowUsers(uid, userBaseInfoModel.getContrRq()));
					map.put("mobileliveimg", userBaseInfoModel.getLivimage());
					map.put("sex", userBaseInfoModel.getSex());
					map.put("opentime",System.currentTimeMillis()/1000-userBaseInfoModel.getOpentime());

					String stream = configService.getThirdStream(uid);
					if (null == stream) {
						map.put("domain",liveService.getVideoConfig(0, uid, userBaseInfoModel.getVideoline()).get("domain"));
					} else {
						map.put("domain", stream);
					}

					map.put("verified", userBaseInfoModel.isVerified());
				}
				if (map.size() > 0) {
					list.add(map);
				}
			}
		}
		return list;
	}
	
	/**
	 * 显示房间中的人数
	 * 
	 * @param anchoruid
	 *            主播UID
	 * @param virtuals
	 *            后台设置的虚拟用户数
	 * @return
	 */
	public int getRoomShowUsers(int anchoruid, int virtuals) {

		int realtimes = RelationRedisService.getInstance().getRealEnterRoomTimes(anchoruid);
		int realcout = RelationRedisService.getInstance().getLiveRealEnterTotal(anchoruid);

		int roomCreditThis = RelationRedisService.getInstance().getRoomCreditThis(anchoruid);

		return (int) (virtuals + (realcout + 1) * 3 + (realtimes + 1) * 8 + Math.ceil(roomCreditThis / 20));
	}
}
