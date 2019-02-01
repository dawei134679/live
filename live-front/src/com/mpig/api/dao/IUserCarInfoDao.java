package com.mpig.api.dao;

import java.util.List;

import com.mpig.api.model.UserCarInfoModel;

public interface IUserCarInfoDao {
	/**
	 * 根据用户id获取当前用户所有有效的坐骑
	 * @param uid
	 * @return
	 */
	List<UserCarInfoModel> getUserValidCars(Integer uid);
	
	/**
	 * 根据用户和car id获取详情
	 * @param uid
	 * @param gid
	 * @return
	 */
	UserCarInfoModel getUserCarInfoByGid(Integer uid, Integer gid);
	
	/**
	 * 增加用户的座驾信息
	 * @param uid
	 * @param gid
	 * @param starttime
	 * @param endtime
	 * @param status 0未启用 1已启用
	 * @return
	 */
	int addUserCarInfo(Integer uid, Integer gid, Integer starttime, Integer endtime, Integer status);
	
	/**
	 * 修改座驾信息
	 * @param uid
	 * @param gid
	 * @param starttime
	 * @param endtime
	 * @param status 0未启用 1已启用
	 * @return
	 */
	int updUserCarInfo(Integer uid, Integer gid, Integer starttime, Integer endtime, Integer status);
	
	/**
	 * 修改某个用户指定座驾的状态
	 * @param uid
	 * @param gid
	 * @param status 0未启用 1已启用
	 * @return
	 */
	int updUserCarInfoStatus(Integer uid, Integer gid, Integer status);
	
	/**
	 * 禁用用户所有的座驾
	 * @param uid
	 * @return
	 */
	int updUserCarInfoUnStatus(Integer uid);
}
