package com.mpig.api.dao;

import com.mpig.api.model.UserMallItemModel;

public interface IUserMallItemDao {
	/**
	 * 增加用户商城道具背包记录
	 * @param uid
	 * @param gid
	 * @param type
	 * @param subtype
	 * @param num
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	int addUserMallItem(int uid, int gid, int type, int subtype, int num, int starttime, int endtime);
	/**
	 * 修改商城道具的时间
	 * @param uid
	 * @param subtype
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	int updUserMallItem(int uid, int subtype, int starttime, int endtime);
	
	/**
	 * 根据时间获取某个类型的道具
	 * @param uid
	 * @param subtype
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	UserMallItemModel getItemBySubtype(int uid, int subtype);
	/**
	 * 获取某个类型的道具
	 * @param uid
	 * @param subtype
	 * @return
	 */
	UserMallItemModel getAllItemBySubtype(int uid, int subtype);
}
