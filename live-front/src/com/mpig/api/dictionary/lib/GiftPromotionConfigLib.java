package com.mpig.api.dictionary.lib;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.mpig.api.SqlTemplete;
import com.mpig.api.db.DataSource;
import com.mpig.api.dictionary.GiftPromotionConfig;
import com.mpig.api.redis.service.RedisCommService;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.VarConfigUtils;

public class GiftPromotionConfigLib {
	
	private static final Logger logger = Logger.getLogger(GiftPromotionConfigLib.class);
	
	private static final Map<Integer, GiftPromotionConfig> giftPromotionConfigs = new HashMap<Integer, GiftPromotionConfig>();
	private static final Map<String, String> unionSupport = new HashMap<String, String>();
	
	/**
	 * 根据价格获取当前商品的折扣价
	 * @param gid
	 * @param price
	 * @return
	 */
	public static Integer getDisPrice(Integer gid, Integer price){
		Integer nowDate = (int) (System.currentTimeMillis()/1000);
		Integer numerator = 100;
		GiftPromotionConfig giftPromotionConfig = giftPromotionConfigs.get(gid);
		if(giftPromotionConfig != null){
			if(giftPromotionConfig.getEndtime() >= nowDate && giftPromotionConfig.getStarttime() <= nowDate){
				Integer discount = giftPromotionConfig.getDiscount();
				float sale = (float)discount/(float)numerator;
				price = (int)(price * sale);
			}
		}
		return price;
	}
	
	public Map<Integer, GiftPromotionConfig> getGiftPromotionConfigs(){
		return giftPromotionConfigs;
	}
	/**
	 * 获取促销活动列表
	 */
	public static synchronized boolean loadGiftPromotionConfigFromDb(){
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		boolean ok = false;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuConfig).getConnection();
			statement = conn.prepareStatement(SqlTemplete.SQL_selGiftPromotion);

			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					GiftPromotionConfig promotionConfig = new GiftPromotionConfig();
					promotionConfig.populateFromResultSet(rs);
					giftPromotionConfigs.put(promotionConfig.getGid(), promotionConfig);
					ok = true;
				}
			} else {
				logger.info("<loadGiftPromotionConfigFromDb->SQL RETURN NULL>");
			}
		} catch (SQLException e) {
			logger.error("<loadGiftPromotionConfigFromDb->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<loadGiftPromotionConfigFromDb->Exception>" + e.getMessage());
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
				logger.error("<loadGiftPromotionConfigFromDb->finally->Exception>" + e.getMessage());
			}
		}
		return ok;
	}
	
	
	public static Map<String, String> getSupport(){
		return unionSupport;
	}
	/**
	 * 获取扶持号相关配置
	 * @return
	 */
	public static boolean loadSupportForRedis(){
		Map<String, String> hgetAll = RedisCommService.getInstance().hgetAll(RedisContant.RedisNameRelation, RedisContant.keySupport);
		unionSupport.putAll(hgetAll);
		logger.error("debug check ... loadSupportForRedis " + JSONObject.toJSONString(hgetAll));
		return true;
	}
}
