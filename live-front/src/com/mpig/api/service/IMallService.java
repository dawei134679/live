package com.mpig.api.service;

import com.mpig.api.model.ReturnModel;

public interface IMallService {

	/**
	 * 购买守护
	 * 
	 * @param gid
	 * @param gname
	 * @param uid
	 * @param dstuid
	 * @param count
	 * @param wealths
	 * @param gets
	 * @param starttime
	 * @param endtime
	 * @param cushiontime
	 * @param price
	 * @param pricetotal
	 * @param realprice
	 * @param realpricetotal
	 * @param os
	 * @param exp
	 * @param returnModel
	 */
	public void buyGuard(Integer gid, String gname, Integer uid,
			Integer dstuid, Integer count, Integer wealths, Double gets,
			Integer starttime, Integer endtime, Integer cushiontime,
			Integer price, Integer pricetotal, Integer realprice,
			Integer realpricetotal, Byte os, Integer exp, Integer inroom,
			Integer isopen, ReturnModel returnModel);

	/**
	 * 购买vip
	 * 
	 * @param gid
	 * @param gname
	 * @param uid
	 * @param dstuid
	 * @param starttime
	 * @param endtime
	 * @param count
	 * @param wealths
	 * @param price
	 * @param pricetotal
	 * @param realprice
	 * @param realpricetotal
	 * @param returnModel
	 */
	public void buyVip(Integer gid, String gname, Integer uid, Integer dstuid,
			Integer starttime, Integer endtime, Integer count, Integer wealths,
			Integer price, Integer pricetotal, Integer realprice,
			Integer realpricetotal, ReturnModel returnModel);

	/**
	 * 购买道具
	 * 
	 * @param gid
	 * @param gname
	 * @param uid
	 * @param dstuid
	 * @param type
	 * @param subtype
	 * @param starttime
	 * @param endtime
	 * @param count
	 * @param wealths
	 * @param price
	 * @param pricetotal
	 * @param realprice
	 * @param realpricetotal
	 * @param returnModel
	 */
	public void buyProps(Integer gid, String gname, Integer uid,
			Integer dstuid, Integer type, Integer subtype, Integer starttime,
			Integer endtime, Integer count, Integer wealths, Integer price,
			Integer pricetotal, Integer realprice, Integer realpricetotal,
			ReturnModel returnModel);

	/**
	 * 购买座驾
	 * @param gid
	 * @param gname
	 * @param uid
	 * @param dstuid
	 * @param type
	 * @param subtype
	 * @param starttime
	 * @param endtime
	 * @param count
	 * @param wealths
	 * @param price
	 * @param pricetotal
	 * @param realprice
	 * @param realpricetotal
	 * @param returnModel
	 */
	public void buyCar(Integer gid, String gname, Integer uid, Integer dstuid,
			Integer type, Integer subtype, Integer starttime, Integer endtime,
			Integer count, Integer wealths, Integer price, Integer pricetotal,
			Integer realprice, Integer realpricetotal, ReturnModel returnModel);

	/**
	 * 获取我的道具
	 * 
	 * @param uid
	 * @param returnModel
	 */
	public void getMyProps(int uid, ReturnModel returnModel);
}
