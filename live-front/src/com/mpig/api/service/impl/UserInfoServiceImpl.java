package com.mpig.api.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mpig.api.SqlTemplete;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.model.UserAccountModel;
import com.mpig.api.model.UserAssetModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.service.IUserInfoService;
import com.mpig.api.service.IUserService;
import com.mpig.api.utils.VarConfigUtils;

@Service
public class UserInfoServiceImpl implements IUserInfoService {

	@Autowired
	private IUserService userService;
	
	private final static Logger logger = Logger.getLogger(UserInfoServiceImpl.class);
	
	@Override
	public int saveUserInfoByUid(int uid) {
		Connection conn = null;
		PreparedStatement statement1 = null;
		ResultSet rs1 = null;
		try {
			logger.info(String.format("UID:%s同步数据-begin", uid));

			Long salesmanId= null;
			String salesmanName= null;
			String salesmanContactsName= null;
			String salesmanContactsPhone= null;
			
			Long agentUserId= null;
			String agentUserName= null;
			String agentUserContactsName= null;
			String agentUserContactsPhone= null;
			
			Long promotersId= null;
			String promotersName= null;
			String promotersContactsName= null;
			String promotersContactsPhone= null;
			
			Long extensionCenterId= null;
			String extensionCenterName= null;
			String extensionCenterContactsName= null;
			String extensionCenterContactsPhone= null;
			
			Long strategicPartnerId= null;
			String strategicPartnerName= null;
			String strategicPartnerContactsName= null;
			String strategicPartnerContactsPhone = null;
			
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuAdmin).getConnection();

			statement1 = DBHelper.getPreparedStatement(conn, SqlTemplete.SQL_getUserOrgInfo);	
			DBHelper.setPreparedStatementParam(statement1, uid);
			rs1 =  statement1.executeQuery();
			while(rs1.next()) {
				salesmanId = rs1.getLong("salesmanId");
				salesmanName = rs1.getString("salesmanName");
				salesmanContactsName = rs1.getString("salesmanContactsName");
				salesmanContactsPhone = rs1.getString("salesmanContactsPhone");
				
				agentUserId = rs1.getLong("agentUserId");
				agentUserName = rs1.getString("agentUserName");
				agentUserContactsName = rs1.getString("agentUserContactsName");
				agentUserContactsPhone = rs1.getString("agentUserContactsPhone");
				
				promotersId = rs1.getLong("promotersId");
				promotersName = rs1.getString("promotersName");
				promotersContactsName = rs1.getString("promotersContactsName");
				promotersContactsPhone = rs1.getString("promotersContactsPhone");
				
				extensionCenterId = rs1.getLong("extensionCenterId");
				extensionCenterName = rs1.getString("extensionCenterName");
				extensionCenterContactsName = rs1.getString("extensionCenterContactsName");
				extensionCenterContactsPhone = rs1.getString("extensionCenterContactsPhone");
				
				strategicPartnerId = rs1.getLong("strategicPartnerId");
				strategicPartnerName = rs1.getString("strategicPartnerName");
				strategicPartnerContactsName = rs1.getString("strategicPartnerContactsName");
				strategicPartnerContactsPhone = rs1.getString("strategicPartnerContactsPhone");
			}
			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(uid, false);
			UserAccountModel accountModel =  userService.getUserAccountByUid(uid, false);
			UserAssetModel userAssetModel = userService.getUserAssetByUid(uid, false);
			
			int res = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_insUserInfo, false,
					userBaseInfoModel.getUid(),
					converStr(userBaseInfoModel.getNickname()),
					userBaseInfoModel.getFamilyId(),
					userBaseInfoModel.getAnchorLevel(),
					userBaseInfoModel.getUserLevel(),
					userBaseInfoModel.getSex(),
					userBaseInfoModel.getIdentity(),
					converStr(userBaseInfoModel.getHeadimage()),
					converStr(userBaseInfoModel.getLivimage()),
					converStr(userBaseInfoModel.getPcimg1()),
					converStr(userBaseInfoModel.getPcimg2()),
					userBaseInfoModel.getBirthday(),
					userBaseInfoModel.getExp(),
					converStr(userBaseInfoModel.getRealPhone()),
					converStr(userBaseInfoModel.getProvince()),
					converStr(userBaseInfoModel.getCity()),
					converStr(userBaseInfoModel.getSignature()),
					userBaseInfoModel.getRegistip(),
					userBaseInfoModel.getRegisttime(),
					converStr(userBaseInfoModel.getRegistchannel()),
					converStr(userBaseInfoModel.getSubregistchannel()),
					userBaseInfoModel.getRegistos(),
					converStr(userBaseInfoModel.getRegistimei()),
					userBaseInfoModel.getLiveStatus(),
					userBaseInfoModel.getOpentime(),
					userBaseInfoModel.getRecommend(),
					userBaseInfoModel.getVideoline(),
					userBaseInfoModel.isVerified(),
					converStr(userBaseInfoModel.getVerified_reason()),
					userBaseInfoModel.getContrRq(),
					converStr(userBaseInfoModel.getConstellation()),
					converStr(userBaseInfoModel.getHobby()),
					userBaseInfoModel.getGrade(),
					userBaseInfoModel.getGameStatus(),
					userBaseInfoModel.getGameId(),
					
					accountModel.getAccountid(),
					accountModel.getAccountname(),
					accountModel.getPassword(),
					accountModel.getAuthkey(),
					accountModel.getUnionId(),
					accountModel.getStatus(),
					
					userAssetModel.getMoney(),
					userAssetModel.getWealth(),
					userAssetModel.getCredit(),
					userAssetModel.getCreditTotal(),
					0,//frozenCredit
					
					salesmanId,
					salesmanName,
					salesmanContactsName,
					salesmanContactsPhone,
					
					agentUserId,
					agentUserName,
					agentUserContactsName,
					agentUserContactsPhone,
					
					promotersId,
					promotersName,
					promotersContactsName,
					promotersContactsPhone,
					
					extensionCenterId,
					extensionCenterName,
					extensionCenterContactsName,
					extensionCenterContactsPhone,
					
					strategicPartnerId,
					strategicPartnerName,
					strategicPartnerContactsName,
					strategicPartnerContactsPhone,
					
					0);//充值总金额默认 0
			logger.info(String.format("UID:%s同步数据-end,%s", uid,res));
			return res;
		} catch (Exception e) {
			logger.error(String.format("UID:%s同步数据异常", uid),e);
			return 0;
		}finally {
			try {
				if (rs1 != null) {
					rs1.close();
				}
				if (statement1 != null) {
					statement1.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	@Override
	public int updateUserInfoBase(UserBaseInfoModel userBaseInfoModel) {
		try {
			logger.info(String.format("UID:%s同步用户base信息-begin", userBaseInfoModel.getUid()));
			int res = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_updateUserInfoBase, false,
					converStr(userBaseInfoModel.getNickname()),
					userBaseInfoModel.getFamilyId(),
					userBaseInfoModel.getAnchorLevel(),
					userBaseInfoModel.getUserLevel(),
					userBaseInfoModel.getSex(),
					userBaseInfoModel.getIdentity(),
					converStr(userBaseInfoModel.getHeadimage()),
					converStr(userBaseInfoModel.getLivimage()),
					converStr(userBaseInfoModel.getPcimg1()),
					converStr(userBaseInfoModel.getPcimg2()),
					userBaseInfoModel.getBirthday(),
					userBaseInfoModel.getExp(),
					converStr(userBaseInfoModel.getRealPhone()),
					converStr(userBaseInfoModel.getProvince()),
					converStr(userBaseInfoModel.getCity()),
					converStr(userBaseInfoModel.getSignature()),
					userBaseInfoModel.getRegistip(),
					userBaseInfoModel.getRegisttime(),
					converStr(userBaseInfoModel.getRegistchannel()),
					converStr(userBaseInfoModel.getSubregistchannel()),
					userBaseInfoModel.getRegistos(),
					converStr(userBaseInfoModel.getRegistimei()),
					userBaseInfoModel.getLiveStatus(),
					userBaseInfoModel.getOpentime(),
					userBaseInfoModel.getRecommend(),
					userBaseInfoModel.getVideoline(),
					userBaseInfoModel.isVerified(),
					converStr(userBaseInfoModel.getVerified_reason()),
					userBaseInfoModel.getContrRq(),
					converStr(userBaseInfoModel.getConstellation()),
					converStr(userBaseInfoModel.getHobby()),
					userBaseInfoModel.getGrade(),
					userBaseInfoModel.getGameStatus(),
					userBaseInfoModel.getGameId(),
					userBaseInfoModel.getUid()
					);
			logger.info(String.format("UID:%s同步用户base信息-end,%s", userBaseInfoModel.getUid(),res));
			return res;
		} catch (Exception e) {
			logger.error(String.format("UID:%s同步用户base信息异常", userBaseInfoModel.getUid()),e);
			return 0;
		}
	}

	@Override
	public int updateUserInfoAccount(UserAccountModel userAccountModel) {
		try {
			logger.info(String.format("UID:%s同步用户account信息-begin", userAccountModel.getUid()));
			int res = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_updUserInfoAccount, false,
					userAccountModel.getAccountid(),
					userAccountModel.getAccountname(),
					userAccountModel.getPassword(),
					userAccountModel.getAuthkey(),
					userAccountModel.getUnionId(),
					userAccountModel.getStatus(),
					userAccountModel.getUid()
					);
			logger.info(String.format("UID:%s同步用户account信息-end,%s", userAccountModel.getUid(),res));
			return res;
		} catch (Exception e) {
			logger.error(String.format("UID:%s同步用户account信息异常", userAccountModel.getUid()),e);
			return 0;
		}
	}

	@Override
	public int updateUserInfoBase(UserAssetModel userAssetModel) {
		try {
			logger.info(String.format("UID:%s同步用户asset信息-begin", userAssetModel.getUid()));
			int res = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_updUserInfoAsset, false,
					userAssetModel.getMoney(),
					userAssetModel.getWealth(),
					userAssetModel.getCredit(),
					userAssetModel.getCreditTotal(),
					0,
					userAssetModel.getUid()
					);
			logger.info(String.format("UID:%s同步用户asset信息-end,%s", userAssetModel.getUid(),res));
			return res;
		} catch (Exception e) {
			logger.error(String.format("UID:%s同步用户asset信息异常", userAssetModel.getUid()),e);
			return 0;
		}
	}
	
	@Override
	public int updateUserInfoMoneyRMB(Integer uid,Double moneyRMB) {
		try {
			logger.info(String.format("UID:%s同步用户moneyRMB信息-begin", uid));
			int res = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_updUserInfoMoneyRmb, false,moneyRMB,uid);
			logger.info(String.format("UID:%s同步用户moneyRMB信息-end,%s", uid,res));
			return res;
		} catch (Exception e) {
			logger.error(String.format("UID:%s同步用户moneyRMB信息异常", uid),e);
			return 0;
		}
	}
	
	private String converStr(String str) {
		return StringUtils.isEmpty(str)?"":str;
	}
}
