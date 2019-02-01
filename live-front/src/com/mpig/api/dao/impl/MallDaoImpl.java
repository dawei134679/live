package com.mpig.api.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.mpig.api.SqlTemplete;
import com.mpig.api.dao.IMallDao;
import com.mpig.api.db.DBHelper;
import com.mpig.api.utils.VarConfigUtils;
@Repository
public class MallDaoImpl implements IMallDao{
	private static final Logger logger = Logger.getLogger(MallDaoImpl.class);
	
	@Override
	public int addMallInfo(int gid, String gname, int srcuid, String srcnickname, int dstuid, String dstnickname, int count, int price, int realprice, int pricetotal, int realpricetotal, int credit, int starttime, int endtime, int type) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuPay, SqlTemplete.SQL_insBillMall, false, gid,gname,srcuid,srcnickname,dstuid,dstnickname,count,price,realprice,pricetotal,realpricetotal,credit,starttime,endtime,type,System.currentTimeMillis()/1000);
			return executeResult;
		} catch (Exception e) {
			logger.error("<addMallInfo->Exception>" + e.toString());
		}
		return -1;
	}
}
