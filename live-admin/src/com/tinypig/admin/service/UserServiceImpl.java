package com.tinypig.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.model.UserAccountModel;
import com.tinypig.admin.model.UserAssetModel;
import com.tinypig.admin.model.UserBaseInfoModel;
import com.tinypig.admin.redis.service.UserRedisService;
import com.tinypig.admin.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserServiceImpl{
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	public UserAccountModel getUserAccountByUid(int uid, Boolean bl) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		UserAccountModel userAccountModel = null;

		if (!bl) {
			// 读缓存
			String userAccount = UserRedisService.getInstance().getUserAccount(uid);
			if (org.apache.commons.lang.StringUtils.isNotEmpty(userAccount)) {
				userAccountModel = (UserAccountModel) JSONObject.parseObject(userAccount, UserAccountModel.class);
			}
		}
		try {
			if (userAccountModel == null) {
				conn = DbUtil.instance().getCon(VarConfigUtils.dbZhuUser,"slave");
				String sql = "select `uid`,`accountid`,`accountname`,`password`,`authkey`,`status` from %s where uid=?";
				sql = StringUtil.getSqlString(sql, "user_account_", uid);
				statement = conn
						.prepareStatement(sql);
				statement.setInt(1, uid);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						userAccountModel = new UserAccountModel().populateFromResultSet(rs);
						// 缓存
						UserRedisService.getInstance().setUserAccount(uid, JSONObject.toJSONString(userAccountModel));
						break;
					}
				}
			}

		} catch (SQLException e) {
			logger.error("<getUserAccountByUid->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getUserAccountByUid->Exception>" + e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return userAccountModel;
	}

	public UserAssetModel getUserAssetByUid(int uid, Boolean bl) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		UserAssetModel userAssetModel = null;
		if (!bl) {
			// 读缓存
			String userAsset = UserRedisService.getInstance().getUserAsset(uid);
			if (userAsset != null && !"".equals(userAsset)) {
				userAssetModel = (UserAssetModel) JSONObject.parseObject(userAsset, UserAssetModel.class);
			}
		}
		try {
			if (userAssetModel == null) {
				conn = DbUtil.instance().getCon(VarConfigUtils.dbZhuUser,"slave");
				String sql = "select `uid`,`money`,`wealth`,`credit`,`creditTotal`,from %s where uid=?";
				sql = StringUtil.getSqlString(sql, "user_asset_", uid);
				statement = conn.prepareStatement(sql);

				rs = statement.executeQuery();
				if (rs != null) {
					while (rs.next()) {
						userAssetModel = new UserAssetModel().populateFromResultSet(rs);
						UserRedisService.getInstance().setUserAsset(uid, JSONObject.toJSONString(userAssetModel));
						break;
					}
				}
			}
		} catch (SQLException e) {
			logger.error("<getUserAssetByUid->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getUserAssetByUid->Exception>" + e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return userAssetModel;
	}

	public UserBaseInfoModel getUserbaseInfoByUid(int uid, Boolean bl) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		UserBaseInfoModel userBaseInfoModel = null;

		if (!bl) {
			// 读缓存
			String userbaseinfo = UserRedisService.getInstance().getUserBaseInfo(uid);
			if (org.apache.commons.lang.StringUtils.isNotEmpty(userbaseinfo)) {
				userBaseInfoModel = (UserBaseInfoModel) JSONObject.parseObject(userbaseinfo, UserBaseInfoModel.class);
			}
		}
		try {
			if (userBaseInfoModel == null) {
				// 获取用户基本信息
				conn = DbUtil.instance().getCon(VarConfigUtils.dbZhuUser,"slave");
				String sql = "";
				sql = "";
				statement = conn
						.prepareStatement(sql);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						userBaseInfoModel = new UserBaseInfoModel().populateFromResultSet(rs);
						UserRedisService.getInstance().setUserBaseInfo(uid, JSONObject.toJSONString(userBaseInfoModel));
						break;
					}
				}
			}
		} catch (SQLException e) {
			logger.error("<getUserbaseInfoByUid->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getUserbaseInfoByUid->Exception>" + e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return userBaseInfoModel;
	}
}