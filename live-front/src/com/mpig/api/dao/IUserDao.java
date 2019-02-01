package com.mpig.api.dao;

import java.util.Map;


public interface IUserDao {
	
	/**
	 * 获取新浪认证
	 * @return
	 */
	Map<String, Object> getSinaVerified(Integer uid);
	int addUserCover(int uId,String picCover,String picCover1,String picCover2);
	Map<String, Object> isNullId(int uId,int status);
	public boolean updUserCover(int id,String picCover,String picCover1,String picCover2);
	public Map<String, Object> getStatus(int uId);
	public Map<String, Object> getNewestRecord(int uId);
}
