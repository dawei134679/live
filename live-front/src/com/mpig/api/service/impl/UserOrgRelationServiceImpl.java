package com.mpig.api.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.mpig.api.SqlTemplete;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.service.IUserOrgRelationService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.VarConfigUtils;

@Service
public class UserOrgRelationServiceImpl implements IUserOrgRelationService {

	private Logger log = Logger.getLogger(UserOrgRelationServiceImpl.class);
	
	@Override
	public ReturnModel saveUserOrgRelation(Integer uid, String phone, Long registTime, Long salesmanId) {
		ReturnModel returnModel = new ReturnModel();
		Connection conn = null;
		PreparedStatement statement1 = null;
		PreparedStatement statement2 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuAdmin).getConnection();
			statement1 = DBHelper.getPreparedStatement(conn, SqlTemplete.SQL_getUserOrgRelationCountByUid);
			DBHelper.setPreparedStatementParam(statement1, uid);
			rs1 = statement1.executeQuery();
			
			while (rs1.next()) {
				if(rs1.getInt(1)>0) {
					returnModel.setCode(CodeContant.UserOrgRelationExist);
					returnModel.setMessage("数据保存失败，请重试");
					return returnModel;
				}
			}
			
			statement2 = DBHelper.getPreparedStatement(conn, SqlTemplete.SQL_getSalemanById);
			DBHelper.setPreparedStatementParam(statement2, salesmanId);
			rs2 = statement2.executeQuery();
			if(rs2==null) {
				returnModel.setCode(CodeContant.SalesmanlNotExist);
				returnModel.setMessage("业务员不存在");
				return returnModel;
			}
			Long strategicPartner = null;//战略合作伙伴ID
			Long extensionCenter = null; //推广中心ID
			Long promoters = null; //推广商ID
			Long agentUser = null; //代理商ID
			while(rs2.next()) {
				strategicPartner = rs2.getLong("strategic_partner");
				extensionCenter = rs2.getLong("extension_center");
				promoters = rs2.getLong("promoters");
				agentUser = rs2.getLong("agent_user");
			}
			if(extensionCenter==null||promoters==null||agentUser==null) {
				returnModel.setCode(CodeContant.SalesmanlNotExist);
				returnModel.setMessage("业务员不存在");
				return returnModel;
			}
			int i = DBHelper.execute(VarConfigUtils.dbZhuAdmin, SqlTemplete.SQL_insUserOrgRelation, false, uid,phone,registTime,strategicPartner,
					extensionCenter,promoters,agentUser,salesmanId,registTime,0,0,0);
			if(i==0) {
				returnModel.setCode(CodeContant.CONSYSTEMERR);
				returnModel.setMessage("保存数据失败");
				return returnModel;
			}
			return returnModel;
		} catch (Exception e) {
			log.error("保存用户与业务关系失败",e);
			returnModel.setCode(CodeContant.CONSYSTEMERR);
			returnModel.setMessage("保存数据失败");
			return returnModel;
		}finally {
			try {
				if (rs1 != null) {
					rs1.close();
				}
				if (statement1 != null) {
					statement1.close();
				}
				if (rs2 != null) {
					rs2.close();
				}
				if (statement2 != null) {
					statement2.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}
	}
}
