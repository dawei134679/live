package com.mpig.api.dictionary.lib;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.mpig.api.SqlTemplete;
import com.mpig.api.db.DataSource;
import com.mpig.api.dictionary.InvitationConfig;
import com.mpig.api.dictionary.InvitationRewardsConfig;
import com.mpig.api.utils.VarConfigUtils;

public class InviteRewardsConfigLib {
	private static final Logger logger = Logger.getLogger(InviteRewardsConfigLib.class);
	
	private static final List<InvitationConfig> invitationConfigs = new ArrayList<InvitationConfig>();
	private static final Map<Integer, InvitationConfig> invitationConfigMap = new HashMap<Integer, InvitationConfig>();
	private static final Map<Integer, List<InvitationRewardsConfig>> rewardMap = new TreeMap<Integer, List<InvitationRewardsConfig>>(new Comparator<Integer>() {
		@Override
		public int compare(Integer o1, Integer o2) {
			return o1 - o2;
		}
	});
	
	/**
	 * 获取任务列表
	 * @return
	 */
	public static List<InvitationConfig> getInvitationConfigs(){
		return invitationConfigs;
	}
	/**
	 * 获取任务列表
	 * @return
	 */
	public static Map<Integer, InvitationConfig> getInvitationConfigsMap(){
		return invitationConfigMap;
	}
	
	public static Map<Integer, List<InvitationRewardsConfig>> getRewardMap(){
		return rewardMap;
	}
	
	public static synchronized void loadInvitationConfig(){
		loadInvitationConfigFromDb();
		loadInvitationRewardsConfig();
	}
	/**
	 * 加载任务奖励
	 */
	public static synchronized void loadInvitationRewardsConfig(){
		List<InvitationRewardsConfig> invitationRewards = loadInvitationRewardsConfigFromDb();
		for (InvitationConfig invitationConfig : invitationConfigs) {
			List<InvitationRewardsConfig> invita = new ArrayList<InvitationRewardsConfig>();
			for(InvitationRewardsConfig rewardsConfig :invitationRewards){
				if(invitationConfig.getId() == rewardsConfig.getInvitationId()){
					invita.add(rewardsConfig);
				}
			}
			rewardMap.put(invitationConfig.getId(), invita);
		}
	}

	/**
	 * 获取邀请任务奖励列表
	 */
	public static synchronized List<InvitationRewardsConfig> loadInvitationRewardsConfigFromDb(){
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		List<InvitationRewardsConfig> invitationRewardsConfigs = new ArrayList<InvitationRewardsConfig>();
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuConfig).getConnection();
			statement = conn.prepareStatement(SqlTemplete.SQL_selInvitationRewardsConfig);

			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					InvitationRewardsConfig invitationRewardsConfig = new InvitationRewardsConfig();
					invitationRewardsConfig.populateFromResultSet(rs);
					invitationRewardsConfigs.add(invitationRewardsConfig);
				}
			} else {
				logger.info("<loadInvitationRewardsConfigFromDb->SQL RETURN NULL>");
			}
		} catch (SQLException e) {
			logger.error("<loadInvitationRewardsConfigFromDb->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<loadInvitationRewardsConfigFromDb->Exception>" + e.getMessage());
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
			} catch (SQLException e) {
				logger.error("<loadInvitationConfigFromDb->finally->Exception>" + e.getMessage());
			}
		}
		return invitationRewardsConfigs;
	}
	
	/**
	 * 获取邀请任务列表
	 */
	public static synchronized boolean loadInvitationConfigFromDb(){
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		boolean ok = false;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuConfig).getConnection();
			statement = conn.prepareStatement(SqlTemplete.SQL_selInvitationConfig);

			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					InvitationConfig invitationConfig = new InvitationConfig();
					invitationConfig.populateFromResultSet(rs);
					invitationConfigs.add(invitationConfig);
					invitationConfigMap.put(invitationConfig.getId(), invitationConfig);
					ok = true;
				}
			} else {
				logger.info("<loadInvitationConfigFromDb->SQL RETURN NULL>");
			}
		} catch (SQLException e) {
			logger.error("<loadInvitationConfigFromDb->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<loadInvitationConfigFromDb->Exception>" + e.getMessage());
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
			} catch (SQLException e) {
				logger.error("<loadInvitationConfigFromDb->finally->Exception>" + e.getMessage());
			}
		}
		return ok;
	}
}
