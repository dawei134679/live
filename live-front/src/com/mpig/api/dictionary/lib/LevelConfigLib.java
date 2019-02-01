package com.mpig.api.dictionary.lib;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.mpig.api.SqlTemplete;
import com.mpig.api.db.DataSource;
import com.mpig.api.dictionary.LevelConfig;
import com.mpig.api.model.ValueaddLevelConfModel;
import com.mpig.api.utils.VarConfigUtils;

public class LevelConfigLib implements SqlTemplete{
	private static final Logger logger = Logger.getLogger(LevelConfigLib.class);
	private static final Map<Integer, LevelConfig> levelMap = new TreeMap<Integer, LevelConfig>(new Comparator<Integer>() {
		@Override
		public int compare(Integer o1, Integer o2) {
			return o1 - o2;
		}
	});
	
	private static final Map<Integer, Map<Integer,ValueaddLevelConfModel>> valueaddlevelMap = new TreeMap<Integer, Map<Integer,ValueaddLevelConfModel>>(new Comparator<Integer>() {
		@Override
		public int compare(Integer o1, Integer o2) {
			return o1 - o2;
		}
	});
	
	public static LevelConfig getLevelForLv(int lv){
		return levelMap.get(lv);
	}
	
	public static ValueaddLevelConfModel getValueaddLevelForLv(int gid, int lv){
		return valueaddlevelMap.get(gid).get(lv);
	}

	/**
	 * 获取合适的等级适配
	 * @param exp
	 * @return
	 */
	public static int getSuitableLevel(Long exp){
		 Collection<LevelConfig> dataSource = levelMap.values();
		 int lev = 1;
		 for (Iterator<LevelConfig> iterator = dataSource.iterator(); iterator.hasNext();) {
			LevelConfig levelConfig = (LevelConfig) iterator.next();
			if(levelConfig.getExp()>exp){
				lev = levelConfig.getLv() - 1;
				break;
			}
		}
		if(lev < 1){
			lev = 1;
		}
		return lev;
	}
	
	/**
	 * 获取合适的等级适配
	 * @param exp
	 * @return
	 */
	public static int getValueaddSuitableLevel(int gid, int exp){
		 Collection<ValueaddLevelConfModel> dataSource = valueaddlevelMap.get(gid).values();
		 int lev = 1;
		 for (Iterator<ValueaddLevelConfModel> iterator = dataSource.iterator(); iterator.hasNext();) {
			 ValueaddLevelConfModel levelConfig = (ValueaddLevelConfModel) iterator.next();
			if(levelConfig.getExp()>exp){
				lev = levelConfig.getLevel() - 1;
				break;
			}
		}
		if(lev < 1){
			lev = 1;
		}
		return lev;
	}
	
	public static synchronized boolean loadLevelConfigFromDb(){
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		boolean bupdateOk = false;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuConfig).getConnection();
			statement = conn.prepareStatement(SQL_GetLevelConfigAll);

			rs = statement.executeQuery();
			if (rs != null) {
				levelMap.clear();
				int nCount = 0;
				while (rs.next()) {
					LevelConfig level = new LevelConfig();
					logger.info(">>>>>>>>><updateLevel one:>" + JSON.toJSONString(rs.getString("lv")));
					level.populateFromResultSet(rs);
					levelMap.put(level.getLv(), level);
					nCount++;
				}
				bupdateOk = true;
				logger.info("<loadLevelConfigFromDb->OK: count:>" + nCount);
			} else {
				logger.info("<loadLevelConfigFromDb->SQL RETURN NULL>");
			}
		} catch (SQLException e) {
			logger.error("<loadLevelConfigFromDb->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<loadLevelConfigFromDb->Exception>" + e.getMessage());
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
				logger.error("<loadLevelConfigFromDb->finally->Exception>" + e.getMessage());
			}
		}
		return bupdateOk;
	}
	
	public static synchronized boolean loadValueaddLevelConfigFromDb(){
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		boolean bupdateOk = false;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuConfig).getConnection();
			statement = conn.prepareStatement(SQL_valueaddLevelConfList);

			rs = statement.executeQuery();
			if (rs != null) {
				valueaddlevelMap.clear();
				int nCount = 0;
				while (rs.next()) {
					ValueaddLevelConfModel level = new ValueaddLevelConfModel();
					logger.info(">>>>>>>>><updateLevel one:>" + JSON.toJSONString(rs.getString("level")));
					level.populateFromResultSet(rs);
					Map<Integer, ValueaddLevelConfModel> map = new HashMap<Integer, ValueaddLevelConfModel>();
					if(valueaddlevelMap.get(level.getGid())!=null){
						map = valueaddlevelMap.get(level.getGid());
					}
					map.put(level.getLevel(), level);
					valueaddlevelMap.put(level.getGid(), map);
					nCount++;
				}
				bupdateOk = true;
				logger.info("<loadValueaddLevelConfigFromDb->OK: count:>" + nCount);
			} else {
				logger.info("<loadValueaddLevelConfigFromDb->SQL RETURN NULL>");
			}
		} catch (SQLException e) {
			logger.error("<loadValueaddLevelConfigFromDb->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<loadValueaddLevelConfigFromDb->Exception>" + e.getMessage());
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
				logger.error("<loadLevelConfigFromDb->finally->Exception>" + e.getMessage());
			}
		}
		return bupdateOk;
	}
}
