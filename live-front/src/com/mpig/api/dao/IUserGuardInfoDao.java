package com.mpig.api.dao;

import java.util.List;

import com.mpig.api.model.UserGuardInfoModel;

public interface IUserGuardInfoDao {
	
	/**
	 * 增加守护经验值
	 * @param uid 用户id
	 * @param roomid 房间id/主播id
	 * @param exp 经验值
	 * @return
	 */
	int addExpByUid(int uid, int roomid, int exp, int gid);
	
	/**
	 * 查询用户当前有效的守护信息
	 * @param uid
	 * @param roomid
	 * @return
	 */
	UserGuardInfoModel getUserGuardInfo(int uid, int roomid);
	
	/**
	 * 修改用户的守护等级
	 * @param uid
	 * @param roomid
	 * @param level
	 * @return
	 */
	int updUserGuardLevel(int uid, int roomid, int level, int gid);
	
	/**
	 * 查询用户的当前房间指定守护信息(包含过期的)
	 * @param uid
	 * @param roomid
	 * @return
	 */
	UserGuardInfoModel getUserGuardInfoByUid(int uid, int roomid, int gid);
	
	/**
	 * 插入用户守护信息
	 * @param roomid
	 * @param uid
	 * @param gid
	 * @param level
	 * @param exp
	 * @param starttime
	 * @param endtime
	 * @param cushiontime
	 * @param isdel
	 * @return
	 */
	int addUserGuardInfo(int roomid, int uid, int gid, int level, int exp, int starttime, int endtime, int cushiontime, int isdel);
	
	/**
	 * 修改用户守护信息
	 * @param starttime
	 * @param endtime
	 * @param cushiontime
	 * @param isdel
	 * @return
	 */
	int updUserGuardInfo(int exp, int starttime, int endtime, int cushiontime, int isdel, int uid, int roomid, int gid);
	
	/**
	 * 获取用户所有的有效守护信息
	 * @param uid
	 */
	List<UserGuardInfoModel> selUserAllGardInfo(int uid);
	
	/**
	 * 根据房间id获取该房间的所有守护信息
	 * @param roomid
	 * @return
	 */
	List<UserGuardInfoModel> selUserAllGardInfoByRoomId(int roomid);
}
