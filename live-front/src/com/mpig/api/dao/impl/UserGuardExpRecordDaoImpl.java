package com.mpig.api.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.mpig.api.SqlTemplete;
import com.mpig.api.dao.IUserGuardExpRecordDao;
import com.mpig.api.db.DBHelper;
import com.mpig.api.utils.VarConfigUtils;
@Repository
public class UserGuardExpRecordDaoImpl implements IUserGuardExpRecordDao{
	private static final Logger logger = Logger.getLogger(UserGuardExpRecordDaoImpl.class);
	@Override
	public int insExpRecord(int uid,int roomid,int gid, int exp, int type) {
		if (exp <= 0) {
			return -1;
		}
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_insUserGuardExpRecord, false,uid,roomid,gid,System.currentTimeMillis()/1000,type,exp);
			return executeResult;
		} catch (Exception e) {
			logger.error("<addExpByUid->Exception>" + e.toString());
		}
		return -1;
	}
}
