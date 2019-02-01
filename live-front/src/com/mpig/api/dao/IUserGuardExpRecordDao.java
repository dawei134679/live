package com.mpig.api.dao;

public interface IUserGuardExpRecordDao {

	/**
	 * 加入成长值获取记录
	 * @param uid 
	 * @param roomid
	 * @param gid
	 * @param exp
	 * @param type =1首次登陆  =2首次消费  =3续费
	 * @return
	 */
	int insExpRecord(int uid,int roomid,int gid, int exp, int type);
}
