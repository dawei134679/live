package com.mpig.api.service;

import com.mpig.api.model.UserMallItemModel;

public interface IUserMallItemService {
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
	 * 获取用户当前有效的指定道具
	 * @param uid
	 * @param subtype
	 * @param starttime 可为null
	 * @param endtime 可为null
	 * @return
	 */
	UserMallItemModel getItem(Integer uid, Integer subtype,Boolean directReadMysql);
	/**
	 * 获取用户当前所有的指定类型的道具
	 * @param uid
	 * @param subtype
	 * @return
	 */
	UserMallItemModel getUserAllMallItemBySubtype(Integer uid, Integer subtype);
}
