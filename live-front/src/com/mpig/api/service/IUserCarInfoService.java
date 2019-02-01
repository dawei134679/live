package com.mpig.api.service;

import java.util.List;

import com.mpig.api.model.UserCarInfoModel;

public interface IUserCarInfoService {
	
	/**
	 * 获取所有的有效座驾
	 * @param uid
	 * @return
	 */
	List<UserCarInfoModel> getUserValidCars(Integer uid);
	/**
	 * 获取用户的座驾信息(只读redis)
	 * @param uid
	 * @param gid
	 * @param directReadMysql 【false：读不出数据也不会从数据库读取】
	 * @return
	 */
	UserCarInfoModel getUserCarInfo(Integer uid, Integer gid, Boolean directReadMysql);
	
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
	 * 
	 * @param uid
	 * @param gid
	 * @param type
	 * @return
	 */
	int updUserCarInfoStatus(Integer uid,Integer gid, Integer type);
	
	/**
	 * 停用当前所有的座驾 
	 * @param uid
	 * @return
	 */
	int updUserCarInfoUnStatus(Integer uid);
}
