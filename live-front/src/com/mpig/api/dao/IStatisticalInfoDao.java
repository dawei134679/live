package com.mpig.api.dao;

public interface IStatisticalInfoDao {

	int report(int uid,int anchorId, String os,String appVersion,String msg);
}
