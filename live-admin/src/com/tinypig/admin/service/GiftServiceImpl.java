package com.tinypig.admin.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tinypig.admin.dao.GiftInfoDao;

@Service
public class GiftServiceImpl {

	/**
	 * 获取 填充下拉框的礼物列表
	 * @param subtype =99 全部
	 * @return
	 */
	public List<Map<String, Object>> getGiftForSelect(int subtype){
		
		return GiftInfoDao.getInstance().getGiftForSelect(subtype);
	}
	
	/**
	 * 获取 填充下拉框的礼物列表
	 * @param subtype =99 全部
	 * @return
	 */
	public List<Map<String, Object>> forSelectByType(int type,Boolean notEqual){
		
		return GiftInfoDao.getInstance().forSelectByType(type,notEqual);
	}
	
	/**
	 * 获取使用vip的用户列表
	 * @param uid
	 * @param stime
	 * @param etime
	 * @return
	 */
	public Map<String, Object> getUsedVIPList(int uid,Long stime,Long etime,int page,int size){
		return GiftInfoDao.getInstance().getUsedVIPList(uid, stime, etime, page, size);
	}
	
	/**
	 * 获取使用Car的用户列表
	 * @param uid
	 * @param stime
	 * @param etime
	 * @return
	 */
	public Map<String, Object> getUsedCarList(int uid,Long stime,Long etime,int page,int size){
		return GiftInfoDao.getInstance().getUsedCarList(uid, stime, etime, page, size);
	}
	
	/**
	 * 获取使用Guard的用户列表
	 * @param uid
	 * @param stime
	 * @param etime
	 * @return
	 */
	public Map<String, Object> getUsedGuardList(int uid,Long stime,Long etime,int page,int size){
		return GiftInfoDao.getInstance().getUsedGuardList(uid, stime, etime, page, size);
	}

	/**
	 * 获取 背包裂纹列表
	 * @param uid
	 * @param stime
	 * @param etime
	 * @return
	 */
	public Map<String, Object> getUsedGiftList(int uid,int page,int size){
		return GiftInfoDao.getInstance().getUsedGiftList(uid, page, size);
	}

	/**
	 * 获取 徽章列表
	 * @param uid
	 * @param stime
	 * @param etime
	 * @return
	 */
	public Map<String, Object> getUsedBadgeList(int uid,int page,int size){
		return GiftInfoDao.getInstance().getUsedBadgeList(uid, page, size);
	}
	
	public int addUserBadge(int uid,int gid,int num, Long stime,int days,String remarks,int adminid){
		return GiftInfoDao.getInstance().addUserBadge(uid, gid, num, stime, days, remarks, adminid);
	}
}
