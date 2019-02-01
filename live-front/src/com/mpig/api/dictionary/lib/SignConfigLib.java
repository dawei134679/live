package com.mpig.api.dictionary.lib;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mpig.api.SqlTemplete;
import com.mpig.api.db.DataSource;
import com.mpig.api.dictionary.SignConfig;
import com.mpig.api.model.ConfigGiftModel;
import com.mpig.api.service.impl.ConfigServiceImpl;
import com.mpig.api.utils.VarConfigUtils;

public class SignConfigLib implements SqlTemplete{
	/**
	 * 连续登陆7天和15天
	 */
	public enum SignForSign{
		SignForSign7,//0
		SignForSign15,//1
		SignForSignAfter8//2
	}
	
	private static final Logger logger = Logger.getLogger(SignConfigLib.class);
	private static final Map<Integer, SignConfig> dayMap = new TreeMap<Integer, SignConfig>(new Comparator<Integer>() {
		@Override
		public int compare(Integer o1, Integer o2) {
			return o1 - o2;
		}
	});
	
	public static String ruleResign = "1-3";//TODO
	
	public static String ruleExp = "10,12,14,16,18,20,25#15,100";
	public static String ruleItem = ",,1-3,2-5,2-7,3-10,4-1#15,";
	public static int phase1 = 7;
	public static int phase3 = 8;
	public static int phase2 = 15;
	private static HashMap<Integer, String> descMap = new HashMap<Integer, String>();
	private static ArrayList<String> descList = new ArrayList<String>();
	
	public static SignConfig getLevelForLv(int day){
		return dayMap.get(day);
	}
	
	
	private static JSONObject itemIconObject = new JSONObject();
	
	
	public static synchronized boolean loadSignConfigFromDb(){
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		boolean bupdateOk = false;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuConfig).getConnection();
			statement = conn.prepareStatement(SQL_GetSignConfigAll);

			rs = statement.executeQuery();
			if (rs != null) {
				dayMap.clear();
				itemIconObject.clear();
				descList.clear();
				descMap.clear();
				int nCount = 0;
				while (rs.next()) {
					SignConfig level = new SignConfig();
					logger.info(">>>>>>>>><updateSign one:>" + JSON.toJSONString(rs.getString("day")));
					level.populateFromResultSet(rs);
					dayMap.put(level.getDay(), level);
					descMap.put(level.getDay(),level.getDesc());
					descList.add(level.getDesc());
					nCount++;
				}
				bupdateOk = true;
				logger.info("<loadSignConfigFromDb->OK: count:>" + nCount);
			} else {
				logger.info("<loadSignConfigFromDb->SQL RETURN NULL>");
			}
		} catch (SQLException e) {
			logger.error("<loadSignConfigFromDb->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<loadSignConfigFromDb->Exception>" + e.getMessage());
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
				logger.error("<loadSignConfigFromDb->finally->Exception>" + e.getMessage());
			}
		}

		StringBuffer sBuffer = new StringBuffer();
		String signFor15 = "";
		
		StringBuffer sBufferItem = new StringBuffer();
		String signFor15Item = "";
		int counter1 = 0;
		int counter2 = 0;
		for (Iterator<SignConfig> iterator = dayMap.values().iterator(); iterator.hasNext();) {
			SignConfig sign = (SignConfig) iterator.next();
			if(sign.getPhase() == SignForSign.SignForSign7||sign.getPhase() == SignForSign.SignForSignAfter8){
				if(sign.getPhase() == SignForSign.SignForSignAfter8){
					phase3 = sign.getDay();
				}
				sBuffer.append(sign.getExp());
				String expIcon = "exp-"+sign.getExp();
				if(!itemIconObject.containsKey(expIcon)){
					if(!sign.getExpIcon().isEmpty()){
						itemIconObject.put("exp-"+sign.getExp(), sign.getExpIcon());
					}
				}

				sBuffer.append(",");
				
				String items = sign.getItem();
				sBufferItem.append(items != null?items:"");
				sBufferItem.append(",");
				
				if(items != null){
					String[] split2 = items.split("~");
					for (int i = 0; i < split2.length; i++) {
						String itemString = split2[i];
						String[] split = itemString.split("-");
						if(split.length == 2){
							String itemId = split[0];
							ConfigGiftModel giftConfigByGidNew = ConfigServiceImpl.getGiftInfoByGid(Integer.parseInt(itemId));
							if(giftConfigByGidNew == null){
								logger.error(String.format("loadSignConfigFromDb>>itemId %s missing gift config", itemId));
							}else{
								itemIconObject.put(itemId, giftConfigByGidNew.getIcon());
							}
						}
					}
				}
				
				++counter1;
			}else if(sign.getPhase() == SignForSign.SignForSign15){
				signFor15 = sign.getDay()+","+sign.getExp();
				counter2 = sign.getDay();
				String expIcon = "exp-"+sign.getExp();
				if(!itemIconObject.containsKey(expIcon)){
					if(!sign.getExpIcon().isEmpty()){
						itemIconObject.put("exp-"+sign.getExp(), sign.getExpIcon());
					}
				}
				
				String items = sign.getItem();
				signFor15Item = sign.getDay()+","+(items == null?"":items);
			}
		}
		sBuffer.deleteCharAt(sBuffer.length() - 1);
		ruleExp = sBuffer + "#" + signFor15;
		
		sBufferItem.deleteCharAt(sBufferItem.length() - 1);
		ruleItem = sBufferItem + "#" + signFor15Item;
		
		phase1 = counter1;
		phase2 = counter2;
		return bupdateOk;
	}


	public static JSONObject getItemIconObject() {
		return itemIconObject;
	}
	
	public static HashMap<Integer, String> getRuleDesc(){
		return descMap;
	}
	
	public static ArrayList<String> getRuleDescList(){
		return descList;
	}
}
