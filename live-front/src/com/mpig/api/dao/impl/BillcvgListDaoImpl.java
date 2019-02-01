package com.mpig.api.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.mpig.api.SqlTemplete;
import com.mpig.api.dao.IBillcvgListDao;
import com.mpig.api.db.DBHelper;
import com.mpig.api.utils.VarConfigUtils;
@Repository
public class BillcvgListDaoImpl implements IBillcvgListDao {
	
	private static final Logger logger = Logger.getLogger(IBillcvgListDao.class);

	@Override
	public int insBillcvgList(int uid, int anchorid, int gid, String gname,int realpricetotal, int count, int addtime, int starttime, int endtime,
			int type) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuBill, SqlTemplete.SQL_insBillcvgList, false, uid,anchorid,gid,gname,realpricetotal,count,addtime,starttime,endtime,type);
			return executeResult;
		} catch (Exception e) {
			logger.error("<insBillcvgList->Exception>" + e.toString());
		}
		return -1;
	}
}
