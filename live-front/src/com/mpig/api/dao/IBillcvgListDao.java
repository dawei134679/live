package com.mpig.api.dao;

public interface IBillcvgListDao {
	/**
	 * 插入用户购买守护,座驾,vip记录
	 * @param uid
	 * @param anchorid
	 * @param gid
	 * @param gname
	 * @param addtime
	 * @param starttime
	 * @param endtime
	 * @param type 道具类型 1座驾,2vip,3守护
	 * @return
	 */
	int insBillcvgList(int uid, int anchorid, int gid, String gname,int realpricetotal, int count, int addtime, int starttime, int endtime, int type);
}
