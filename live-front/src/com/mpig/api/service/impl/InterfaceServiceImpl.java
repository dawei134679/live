package com.mpig.api.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.redis.service.RedisCommService;
import com.mpig.api.service.IInterfaceService;
import com.mpig.api.service.IRoomService;
import com.mpig.api.service.IUserService;
import com.mpig.api.utils.RedisContant;

@Service
public class InterfaceServiceImpl implements IInterfaceService {
	private static final Logger logger = Logger.getLogger(InterfaceServiceImpl.class);

	@Resource
	private IUserService userService;
	@Resource
	private IRoomService roomService;
	
	@Override
	public List<Map<String, Object>> getLivingAnchor(int num) {
		
		List<Map<String, Object>> listResult = new ArrayList<Map<String,Object>>();
		try {

			// 热门列表
			Set<String> recommend = RedisCommService.getInstance().zrevrange(RedisContant.RedisNameOther, RedisContant.KeyRoomRecommend,0,num+10);
			
			if (recommend != null && recommend.size() > 0 ) {
				
				Map<String, UserBaseInfoModel> recommendList = userService.getUserbaseInfoByUid(recommend.toArray(new String[0]));
				
				for(String suid: recommend){
					
					Map<String, Object> map = new HashMap<String, Object>();
					int uid = Integer.valueOf(suid);

					UserBaseInfoModel userBaseInfoModel = recommendList.get(suid);
					
					if (userBaseInfoModel == null || !userBaseInfoModel.getLiveStatus()) {
						// 非开播主播
						continue;
					}
					map.put("name", userBaseInfoModel.getNickname());
					map.put("liveimg", userBaseInfoModel.getLivimage());
					map.put("liveurl", "http://www.xiaozhutv.com/"+userBaseInfoModel.getUid()+"?channel=52daohang");
					map.put("enters", roomService.getRoomShowUsers(uid, userBaseInfoModel.getContrRq()));
					listResult.add(map);
					
					num-- ;
					if (num <= 0 ) {
						break;
					}
				}
			}
			if (num > 0) {
				// 最新列表
				Set<String> hot = RedisCommService.getInstance().zrevrange(RedisContant.RedisNameOther, RedisContant.KeyRoomHot,0,num);
				if (hot != null && hot.size() > 0) {
					Map<String, UserBaseInfoModel> hotList = userService.getUserbaseInfoByUid(hot.toArray(new String[0]));
					
					for(String suid : hot){
						if (recommend.contains(suid)) {
							continue;
						}
						
						Map<String, Object> map = new HashMap<String, Object>();
						int uid = Integer.valueOf(suid);

						UserBaseInfoModel userBaseInfoModel = hotList.get(suid);
						
						if (userBaseInfoModel == null || !userBaseInfoModel.getLiveStatus()) {
							// 非开播主播
							continue;
						}
						map.put("name", userBaseInfoModel.getNickname());
						map.put("liveimg", userBaseInfoModel.getLivimage());
						map.put("liveurl", "http://www.xiaozhutv.com/"+userBaseInfoModel.getUid()+"?channel=52daohang");
						map.put("enters", roomService.getRoomShowUsers(uid, userBaseInfoModel.getContrRq()));
						listResult.add(map);
						
						num--;
						if (num <= 0 ) {
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("getLivingAnchor:", e);
		}
		return listResult;
	}

}
