package com.mpig.api.service;

import java.util.List;

import com.mpig.api.model.UserGuardInfoModel;


public interface IUserGuardInfoService {

	/**
	 * 增加用户守护经验
	 * @param uid
	 * @param roomid
	 * @param exp
	 * @return
	 */
	int addGuardExp(int uid, int roomid, int exp, int gid);
	/**
	 * 获取守护详情
	 * @param uid
	 * @param directReadMysql
	 * @return
	 */
	public UserGuardInfoModel getUserGuardInfo(int uid,int roomid, Boolean directReadMysql);
	
	/**
	 * 查询用户的当前房间指定守护信息(包含过期的)
	 * @param uid 用户id
	 * @param roomid 房间id
	 * @return
	 */
	public UserGuardInfoModel getUserGuardInfoByUid(int uid, int roomid, int gid);
	/**
	 * 用户等级变更
	 * @param uid
	 * @param roomid
	 * @param gid 守护id
	 * @param userLevel 用户当前等级
	 * @param exp 用户当前经验值
	 * @return
	 */
	public boolean updUserGuardLevel(int uid, int roomid, int gid, int userLevel, int exp);
	
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
	 * @param exp
	 * @param starttime
	 * @param endtime
	 * @param cushiontime
	 * @param isdel
	 * @param uid
	 * @param roomid
	 * @param gid
	 * @return
	 */
	int updUserGuardInfo(int exp, int starttime, int endtime, int cushiontime,int isdel,int uid,int roomid, int gid);
	
	/**
	 * 首次登陆奖励相关
	 * @param uid
	 */
	int firstLoginUpdExp(int uid);
	
	/**
	 * 首次消费增加守护成长值
	 * @param uid
	 */
	void firstSpendUpdExp(int uid, int roomid);
	
	/**
	 * 根据房间id查询当前房间所有的守护信息
	 * @param roomid
	 * @return
	 */
	List<UserGuardInfoModel> selUserAllGardInfoByRoomId(int roomid);
}
