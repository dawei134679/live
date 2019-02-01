package com.mpig.api.utils;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.mpig.api.dictionary.lib.ValueaddServiceConfigLib;
import com.mpig.api.model.UserCarInfoModel;
import com.mpig.api.model.UserGuardInfoModel;
import com.mpig.api.model.UserMallItemModel;
import com.mpig.api.model.UserVipInfoModel;
import com.mpig.api.model.ValueaddPrivilegeModel;
import com.mpig.api.redis.service.RedisCommService;
import com.mpig.api.redis.service.RelationRedisService;
import com.mpig.api.redis.service.UserRedisService;

import sun.io.ByteToCharGB18030;

/**
 * 获取增值服务的权限相关
 * @author zyl
 *
 */
public class ValueaddServiceUtil {

	/**
	 * 获取当前增值服务的权限
	 * @param gid
	 * @param level
	 * @return
	 */
	public static ValueaddPrivilegeModel getPrivilege(int gid, int level){
		Map<String, ValueaddPrivilegeModel> privileges =  ValueaddServiceConfigLib.getValueaddPrivilege();
		ValueaddPrivilegeModel privilegeModel = privileges.get(gid + ":" + level);
		return privilegeModel;
	}
	
	/**
	 * 获取用户守护信息 
	 * @param uid
	 * @param roomid
	 * @return
	 */
	public static UserGuardInfoModel getGuardInfo(int uid, int roomid){
		String userGuardKey = RedisContant.userGuard+uid+":"+roomid;
		String userGuardStr = UserRedisService.getInstance().get(userGuardKey);
		UserGuardInfoModel userGuardInfoModel = null;
		if(StringUtils.isNotEmpty(userGuardStr)){
			userGuardInfoModel = (UserGuardInfoModel) JSONObject.parseObject(userGuardStr, UserGuardInfoModel.class);	
		}
		return userGuardInfoModel;
	}
	
	/**
	 * 获取用户VIP信息
	 * @param uid
	 * @return
	 */
	public static UserVipInfoModel getVipInfo(int uid){
		String userVipKey = RedisContant.userVip+uid;
		String userVipStr = UserRedisService.getInstance().get(userVipKey);
		UserVipInfoModel userVipInfoModel = null;
		if(StringUtils.isNotEmpty(userVipStr)){
			userVipInfoModel = (UserVipInfoModel) JSONObject.parseObject(userVipStr, UserVipInfoModel.class);
		}
		return userVipInfoModel;
	}
	/**
	 * 获取用户座驾信息
	 * @param uid
	 * @return
	 */
	public static UserCarInfoModel getCarInfo(int uid){
		String userCarKey = RedisContant.userCar+uid;
		String userCarStr = UserRedisService.getInstance().get(userCarKey);
		UserCarInfoModel userCarInfoModel = null;
		if(StringUtils.isNotEmpty(userCarStr)){
			userCarInfoModel = (UserCarInfoModel) JSONObject.parseObject(userCarStr, UserCarInfoModel.class);
		}
		return userCarInfoModel;
	}
	/**
	 * 获取用户在当前房间是否拥有踢人/禁言权限
	 * @param anchorUid 房间号
	 * @param userUid 操作人id
	 * @param dstUid 被操作人id
	 * @return
	 */
	public static boolean getSilentAndKickPrivilege(int anchorUid, int userUid, int dstUid){
		 //操作的用户是否是管理员
		if (!RelationRedisService.getInstance().checkManageByAnchorUser(anchorUid, userUid)) {
			return false;
		}
		//操作人是守护信息
		UserGuardInfoModel guardInfoModel = ValueaddServiceUtil.getGuardInfo(userUid, anchorUid);
		if(guardInfoModel!=null) {
			return true;
		}
		
		//操作人是VIP信息
		UserVipInfoModel userVipInfoModel = ValueaddServiceUtil.getVipInfo(userUid);
		if(userVipInfoModel!=null) {
			//被操作人VIP信息
			UserVipInfoModel dstUserVipInfoModel = ValueaddServiceUtil.getVipInfo(dstUid);
			//不能对钻石VIP操作
			if(dstUserVipInfoModel!=null&&dstUserVipInfoModel.getGid()==44) {
				return false;
			}
			UserGuardInfoModel dstGuardInfoModel = ValueaddServiceUtil.getGuardInfo(dstUid, anchorUid);
			if(dstGuardInfoModel!=null) {
				return false;
			}
			return true;
		}
		
		UserVipInfoModel dstUserVipInfoModel = ValueaddServiceUtil.getVipInfo(dstUid);
		if(dstUserVipInfoModel!=null) {
			return false;
		}
		UserGuardInfoModel dstGuardInfoModel = ValueaddServiceUtil.getGuardInfo(dstUid, anchorUid);
		if(dstGuardInfoModel!=null) {
			return false;
		}
		return true;
	}
	
	/**
	 * 获取当前用户是否拥有萌猪卡
	 * @param uid
	 * @return
	 */
	public static UserMallItemModel getMengzhuCard(int uid){
		UserMallItemModel userMallItemModel = null;
		String itemKey = RedisContant.mengzhucard + uid;
		String userMallItemStr = UserRedisService.getInstance().get(itemKey);
		if (StringUtils.isNotEmpty(userMallItemStr)) {
			userMallItemModel = (UserMallItemModel) JSONObject.parseObject(userMallItemStr, UserMallItemModel.class);
		}
		return userMallItemModel;
	}
	
	/**
	 * 获得用户剩余的可送萌猪数量
	 * @param uid
	 * @return
	 */
	public static int getMengzhuSurplusCount(int uid){
		int defaultMengzhus = 5;
		int mengzhuCards_mengzhus = 30;
		int nowMengzhus = defaultMengzhus;
		
		UserMallItemModel mallItemModel = getMengzhuCard(uid);
		if(mallItemModel!=null){
			nowMengzhus = defaultMengzhus+mengzhuCards_mengzhus;
		}
		String sentMengzhus = RedisCommService.getInstance().get(RedisContant.RedisNameOther,RedisContant.mengzhuOfDay+uid+":"+DateUtils.dateToString(null, "yyyyMMdd"));
		if (sentMengzhus != null) {
			nowMengzhus = nowMengzhus-Integer.parseInt(sentMengzhus);
		}
		return nowMengzhus;
	}
}
