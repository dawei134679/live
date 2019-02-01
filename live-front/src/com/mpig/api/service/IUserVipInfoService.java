package com.mpig.api.service;

import com.mpig.api.model.UserVipInfoModel;

public interface IUserVipInfoService {
	/**
	 * 获取用户当前有效的VIP身份
	 * @param uid
	 * @return
	 */
	public UserVipInfoModel getUserVipInfo(int uid, Boolean directReadMysql);
	/**
	 * 获取用户有过的身份(包含无效过期的)
	 * @param uid
	 * @param gid
	 * @return
	 */
	public UserVipInfoModel getUserVipInfoByUid(int uid, int gid);
	/**
	 * 添加VIP记录
	 * @param uid
	 * @param gid
	 * @param starttime
	 * @param endtime
	 * @param isdel
	 * @return
	 */
	public int addUserVipInfo(int uid, int gid, int starttime,int endtime, int isdel);
	/**
	 * 修改用户VIP记录
	 * @param starttime
	 * @param endtime
	 * @param isdel
	 * @return
	 */
	public int updUserVipInfo(int uid, int starttime, int endtime, int isdel, int gid);
}
