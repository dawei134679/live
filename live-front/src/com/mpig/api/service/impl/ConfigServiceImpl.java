package com.mpig.api.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.mpig.api.SqlTemplete;
import com.mpig.api.db.DataSource;
import com.mpig.api.dictionary.lib.BaseConfigLib;
import com.mpig.api.model.ConfigGiftActivityModel;
import com.mpig.api.model.ConfigGiftModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.UserItemModel;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.service.IConfigService;
import com.mpig.api.service.IUserItemService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.VarConfigUtils;
import com.qiniu.pili.QnStreamUtilv1;

@Service
public class ConfigServiceImpl implements IConfigService, SqlTemplete {
	private static final Logger logger = Logger.getLogger(ConfigServiceImpl.class);

	@Resource
	private IUserItemService userItemService;
	
	// 礼物列表
	private static ConcurrentHashMap<Integer, ConfigGiftModel> configGiftModels = new ConcurrentHashMap<Integer, ConfigGiftModel>();

	//徽章礼物（VIP徽章、徽章、守护徽章）
	private static ConcurrentHashMap<Integer, ConfigGiftModel> configBadgeModels = new ConcurrentHashMap<Integer, ConfigGiftModel>();
	
	//座驾（价格大于0）
	private static ConcurrentHashMap<Integer, ConfigGiftModel> configCarModels = new ConcurrentHashMap<Integer, ConfigGiftModel>();

	// 活动礼物缓存
	private static ConcurrentHashMap<Integer, ConfigGiftActivityModel> giftActList = new ConcurrentHashMap<Integer, ConfigGiftActivityModel>();
	
	//礼物列表 保证顺序
	private static List<ConfigGiftModel> configGiftList = new ArrayList<ConfigGiftModel>();

	// private static ConcurrentHashMap<K, V>

	// uid对应第三方流地址
	private static ConcurrentHashMap<Integer, String> dataUidToStream = new ConcurrentHashMap<Integer, String>();

	//连麦用户 最低等级
	private static volatile int micUserLv = 1;
	public static int getMicUserLv() {
		return micUserLv;
	}
	public static void setMicUserLv(int micUserLv) {
		ConfigServiceImpl.micUserLv = micUserLv;
	}

	public ConcurrentHashMap<Integer, ConfigGiftModel> getConfigCarModels(){
		return configCarModels;
	}
	/**
	 * 获取第三方流地址
	 * 
	 * @param gid
	 *            礼物gid
	 * @param bl
	 *            =true读取数据库 =false读取缓存
	 * @return
	 */
	@Override
	public String getThirdStream(Integer uid) {
		if (null == dataUidToStream) {
			return null;
		}
		return dataUidToStream.get(uid);
	}

	/**
	 * 更新第三方流地址
	 * 
	 * @return
	 */
	public static synchronized boolean updateThirdStream() {
		boolean bupdateOk = true;

		dataUidToStream.clear();
		Map<String, String> data = OtherRedisService.getInstance().getThirdStream();
		if (null != data) {
			for (Map.Entry<String, String> entry : data.entrySet()) {
				try {
					Integer uid = Integer.valueOf(entry.getKey());
					String stream = entry.getValue();
					if (null != uid && null != stream && stream.length() > 0) {
						dataUidToStream.put(uid, stream);
					}
				} catch (NumberFormatException e) {
					logger.error("<updateThirdStream->String to Integer>" + e.getMessage());
				}
			}
		} else {
			bupdateOk = false;
			logger.error("<updateThirdStream->No Data Get From Redis getThirdStream>");
		}

		return bupdateOk;
	}

	public static synchronized boolean updateGiftListNew() {
		logger.info("updateGiftListNew--start");
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		boolean bupdateOk = false;
		try {
			configGiftList.clear();
			configGiftModels.clear();
			configCarModels.clear();
			configBadgeModels.clear();
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuConfig).getConnection();
			statement = conn.prepareStatement(SQL_GetGiftConfigAllNew);

			rs = statement.executeQuery();
			if (rs != null) {


				int nCount = 0;
				while (rs.next()) {
					ConfigGiftModel giftModel = new ConfigGiftModel();
					giftModel.populateFromResultSet(rs);
					Integer gid = giftModel.getGid();

					//subtype:礼物类型(0=>普通礼物 1=>弹幕 2=>喇叭 3=>VIP 31=>VIP徽章 4=>贵族 5=>座驾 6=>徽章 7=>守护 71=>守护徽章 8=>商城道具)
					if (rs.getInt("subtype") == 6 || rs.getInt("subtype") == 31 || rs.getInt("subtype") == 71) {
						configBadgeModels.put(gid, giftModel);
					}
					if(rs.getInt("subtype") == 5 && rs.getInt("gprice")>0){
						configCarModels.put(gid, giftModel);
					}
					configGiftList.add(giftModel);
					configGiftModels.put(gid, giftModel);
					nCount++;
				}
				logger.info("<updateGiftListNew->OK: data:>" + JSON.toJSONString(configGiftModels));
				bupdateOk = true;
				logger.info("<updateGiftListNew->OK: count:>" + nCount);
			} else {
				logger.info("<updateGiftListNew->SQL RETURN NULL>");
			}
			if (rs != null) {
				rs.close();
			}
			if (statement != null) {
				statement.close();
			}
			
		} catch (SQLException e) {
			logger.error("<updateGiftListNew->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<updateGiftListNew->Exception>" + e.getMessage());
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
				logger.error("<updateGiftListNew->finally->Exception>" + e.getMessage());
			}
		}

		logger.info("updateGiftListNew--end");
		return bupdateOk;
	}
	


	public static synchronized boolean updateGiftListAct() {

		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		boolean bupdateOk = false;
		try {
			
			giftActList.clear();
			BaseConfigLib.clearLuckyGift();
			
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuConfig).getConnection();
			
			// 缓存活动列表
			statement = conn.prepareStatement(SQL_giftact+" and stime <= " + System.currentTimeMillis()/1000);
			rs = statement.executeQuery();
			
			if (rs != null) {
				while (rs.next()) {
					ConfigGiftActivityModel giftAct = new ConfigGiftActivityModel().populateFromResultSet(rs);
					if (giftAct.getAtype() == 2) {
						// 幸运礼物
						BaseConfigLib.updateLuckyGift(giftAct.getGid(), giftAct.getStime(), giftAct.getEtime());
					}
					giftActList.put(rs.getInt("gid"),giftAct);
				}
				bupdateOk = true;
			}
			
		} catch (SQLException e) {
			logger.error("<updateGiftListAct->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<updateGiftListAct->Exception>" + e.getMessage());
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
				logger.error("<updateGiftListAct->finally->Exception>" + e.getMessage());
			}
		}
		
		return bupdateOk;
	}

	/**
	 * 获取礼物信息
	 * 
	 * @param gid
	 *            礼物gid
	 * @param bl
	 *            =true读取数据库 =false读取缓存
	 * @return
	 */
	@Override
	public ConfigGiftModel getGiftInfoByGidNew(Integer gid) {
		ConfigGiftModel tmpConfigGiftModel = configGiftModels.get(gid);
		logger.debug("获取礼物gid："+gid+"结果："+tmpConfigGiftModel);
		return tmpConfigGiftModel;
	}
	
	/**
	 * 从内存中通过礼物gid获取礼物详细信息
	 * @param gid
	 * @return
	 */
	public static ConfigGiftModel getGiftInfoByGid(Integer gid) {
		return configGiftModels.get(gid);
	}
	
	

	/**
	 * 获取徽章信息
	 * 
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getBadges() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (Map.Entry<Integer, ConfigGiftModel> entry : configBadgeModels.entrySet()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("gid", entry.getKey());
			map.put("icon", entry.getValue().getIcon());
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 获取礼物列表
	 * 
	 * @param ver
	 *            app端的礼物版本号
	 * @param returnModel
	 */
	@Override
	public void getGiftList(int ver, ReturnModel returnModel) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> listact = new ArrayList<Map<String, Object>>();
		Map<String, Object> _map = new HashMap<String, Object>();

		int redisver = OtherRedisService.getInstance().getGiftVer();
		if (redisver <= ver) {
			_map.put("giftlist", list);
			_map.put("actlist", listact);
			_map.put("ver", redisver);
		} else {

			if (configGiftModels != null) {

			}
			Connection conn = null;
			PreparedStatement statement = null;
			ResultSet rs = null;
			try {
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuConfig).getConnection();
				statement = conn.prepareStatement(SQL_giftlist);
				rs = statement.executeQuery();
				if (rs != null) {
					while (rs.next()) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("gid", rs.getInt("gid"));
						map.put("gver", rs.getInt("gver"));
						map.put("gsort", rs.getInt("gsort"));
						list.add(map);
					}
				}
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}

				statement = conn.prepareStatement(SQL_giftact);
				rs = statement.executeQuery();
				if (rs != null) {
					long lg = System.currentTimeMillis() / 1000;
					while (rs.next()) {
						if (lg >= rs.getInt("stime") && lg < rs.getInt("etime")) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("gid", rs.getInt("gid"));
							map.put("atype", rs.getInt("atype"));
							listact.add(map);
						}
					}
				}
				_map.put("giftlist", list);
				_map.put("actlist", listact);
				_map.put("ver", redisver);
			} catch (Exception ex) {
				returnModel.setCode(CodeContant.ERROR);
				logger.error("error-getGiftList->Exc:" + ex.toString());
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
				} catch (Exception e) {
					returnModel.setCode(CodeContant.ERROR);
					logger.error("error-getGiftList->finally->Exc:" + e.toString());
				}
			}
			returnModel.setData(_map);
		}
	}

	/**
	 * 获取礼物列表
	 * 
	 * @param ver
	 *            app端的礼物版本号
	 * @param returnModel
	 */
	@Override
	public void getGiftListNew(int ver, ReturnModel returnModel) {

		Map<String, Object> _map = new HashMap<String, Object>();

		int redisver = OtherRedisService.getInstance().getGiftVer();

		// 易碎品(弹幕、喇叭)
		List<Map<String, Object>> listWiser = new ArrayList<Map<String, Object>>();
		// 房间内背包(可送出)
		List<Map<String, Object>> listSend = new ArrayList<Map<String, Object>>();
		// 时效道具
//		List<Map<String, Object>> listLimit = new ArrayList<Map<String, Object>>();

		if (redisver > ver) {
			for(Map.Entry<Integer, ConfigGiftModel> entry:configGiftModels.entrySet()){

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("gid", entry.getValue().getGid());
				map.put("gver", entry.getValue().getGver());
				map.put("gsort", entry.getValue().getGsort());
				map.put("isshow", entry.getValue().getIsshow());
				map.put("type", entry.getValue().getType());
				map.put("subtype", entry.getValue().getSubtype());

				if (entry.getValue().getType() == 1) {
					// 易耗品
					listWiser.add(map);
				} else if (entry.getValue().getType() == 2 || entry.getValue().getType() == 3) {
					listSend.add(map);
				}
			}
		}
		// 活动礼物列表
		List<Map<String, Object>> listact = new ArrayList<Map<String, Object>>();

		Map<Integer, ConfigGiftActivityModel> giftAct = this.getGiftAct(false);
		if (giftAct != null) {
			Long lgNow = System.currentTimeMillis()/1000;
			for(Map.Entry<Integer, ConfigGiftActivityModel> entry:giftAct.entrySet()){
				
				if (entry.getValue().getStime() <= lgNow && entry.getValue().getEtime() >lgNow ) {
					
					Map<String, Object> map = new HashMap<String,Object>();
					map.put("gid", entry.getValue().getGid());
					map.put("atype", entry.getValue().getAtype());
					listact.add(map);
				}
			}
		}
		
		// 易碎品(弹幕、喇叭)
		_map.put("giftwiser", listWiser);
		// 房间内背包(可送出)
		_map.put("giftsend", listSend);
		// 时效道具
//		_map.put("giftlimit", listLimit);
		// 周星
		_map.put("actlist", listact);
		_map.put("ver", redisver);
		
		returnModel.setData(_map);
	}
	
	@Override
	public List<Map<String, Object>> getBaglists(Integer uid){

		List<Map<String, Object>> bagsList = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = null;
		Map<Integer, UserItemModel> itemList = userItemService.getItemListByUid(uid, false);
		if (itemList != null && itemList.size() > 0) {
			for(Map.Entry<Integer, UserItemModel> enter:itemList.entrySet()){
				map = new HashMap<String,Object>();
				for(Map.Entry<Integer, ConfigGiftModel> _entry:configGiftModels.entrySet()){
					if (enter.getKey() == _entry.getKey()) {
						map.put("gid", enter.getKey());
						map.put("gname", _entry.getValue().getGname());
						map.put("desc", _entry.getValue().getGname());
						map.put("num", enter.getValue().getNum());
						map.put("gimg", _entry.getValue().getIcon());
						map.put("gpctype", _entry.getValue().getGpctype());
						bagsList.add(map);
						break;
					}
				}
			}
		}
		return bagsList;
	}

	/**
	 * 获取礼物列表
	 * 
	 * @param uid 当前用户
	 * @param returnModel
	 */
	@Override
	public void getGiftListPC(Integer uid,ReturnModel returnModel) {

		Long lgNow = System.currentTimeMillis()/1000;
		Map<String, Object> map = null;

		List<Map<String, Object>> hotList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> luxuryList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> actList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> exclusiveList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> bagsList = new ArrayList<Map<String,Object>>();
		
		if (configGiftList != null && configGiftList.size() > 0) {
			
			for(ConfigGiftModel giftModel : configGiftList){
				if (!giftModel.getIsshow()) {
					continue;
				}
				int category = giftModel.getCategory();
				if (category < 1 || category > 4) {
					continue;
				}
				map = new HashMap<String,Object>();

				map.put("gid", giftModel.getGid());
				map.put("gname", giftModel.getGname());
				map.put("gprice", giftModel.getGprice());
				map.put("desc", ""); // 下一版本添加礼物描述 
				map.put("gimg", giftModel.getIcon());
				map.put("gpctype", giftModel.getGpctype());
				map.put("gtype", giftModel.getGtype());
				map.put("tag", 0);
				
				if (giftActList != null && giftActList.size() > 0) {
					for(Map.Entry<Integer, ConfigGiftActivityModel> _entry:giftActList.entrySet()){
						if (_entry.getKey() == giftModel.getGid()) {
							if (_entry.getValue().getStime() <= lgNow && _entry.getValue().getEtime() > lgNow && _entry.getValue().isIsvalid()) {
								map.put("tag", _entry.getValue().getAtype());
								break;
							}
						}
					}
				}
				if (giftModel.getCategory() == 1) {
					hotList.add(map);
				}else if (giftModel.getCategory() == 2) {
					luxuryList.add(map);
				}else if (giftModel.getCategory() == 3) {
					actList.add(map);
				}else if (giftModel.getCategory() == 4) {
					exclusiveList.add(map);
				}
			}
		}
		if (uid > 0) {
			Map<Integer, UserItemModel> itemList = userItemService.getItemListByUid(uid, false);
			if (itemList != null && itemList.size() > 0) {
				for(Map.Entry<Integer, UserItemModel> enter:itemList.entrySet()){
					map = new HashMap<String,Object>();
					for(Map.Entry<Integer, ConfigGiftModel> _entry:configGiftModels.entrySet()){
						if (enter.getKey() == _entry.getKey()) {
							map.put("gid", enter.getKey());
							map.put("gname", _entry.getValue().getGname());
							map.put("desc", _entry.getValue().getGname());
							map.put("num", enter.getValue().getNum());
							map.put("gimg", _entry.getValue().getIcon());
							map.put("gpctype", _entry.getValue().getGpctype());
							bagsList.add(map);
							break;
						}
					}
				}
			}
		}
		map = new HashMap<String,Object>();
		// 热门
		map.put("hot", hotList);
		// 豪华
		map.put("luxury", luxuryList);
		// 活动
		map.put("act", actList);
		// 专属
		map.put("exclusive", exclusiveList);
		// 背包
		map.put("bags", bagsList);
		
		returnModel.setData(map);
	}

	/**
	 * 获取礼物列表
	 * 
	 * @param ver
	 *            app端的礼物版本号
	 * @param returnModel
	 */
	@Override
	public void getGiftListH5(ReturnModel returnModel) {

		// 待返回的对象
		List<ConfigGiftModel> list = new ArrayList<ConfigGiftModel>();
		
		// 获取活动礼物列表
		Map<Integer, ConfigGiftActivityModel> giftAct = this.getGiftAct(false);
		
		// 房间内背包(可送出)
		for(Map.Entry<Integer,ConfigGiftModel> entry :configGiftModels.entrySet()){
			
			if (giftAct.containsKey(entry.getKey())) {
				entry.getValue().setAct(giftAct.get(entry.getKey()).getAtype());
				
			}else {
				entry.getValue().setAct(0);
			}
			list.add(entry.getValue());
		}
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("list", list);
		returnModel.setData(map);
	}

	@Override
	public Map<Integer, ConfigGiftActivityModel> getGiftAct(boolean bl) {

		Map<Integer, ConfigGiftActivityModel> _map = new HashMap<Integer, ConfigGiftActivityModel>();
		if (!bl) {
			_map = giftActList;
			if (_map != null && _map.size() > 0) {
				return _map;
			}
		}

		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuConfig).getConnection();
			statement = conn.prepareStatement(SQL_giftact);
			rs = statement.executeQuery();
			
			if (rs != null) {
				while (rs.next()) {
					ConfigGiftActivityModel giftAct = new ConfigGiftActivityModel().populateFromResultSet(rs);
					_map.put(rs.getInt("gid"),giftAct);
					giftActList.put(rs.getInt("gid"),giftAct);
				}
			}
			
		} catch (Exception ex) {
			logger.error("error-getGiftAct->Exc:" + ex.toString());
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
			} catch (Exception e) {
				logger.error("error-getGiftAct->finally->Exc:" + e.toString());
			}
		}
		
		return _map;
	}
	
	//更新七牛推流key
	public static boolean updateStreamKey(String uid,String key) {
		if(null == uid){
			return false;
		}
		
		if(null == key){
			OtherRedisService.getInstance().delUidQnKey(uid);
			return true;
		}
		
		if(null != QnStreamUtilv1.getInstance().updateStreamKeyV1(uid, key,false)){
			//清理动态key
			OtherRedisService.getInstance().delUidQnKeyLimitOneDay(String.valueOf(uid));
			//保存fix的key
        	OtherRedisService.getInstance().setUidQnKey(uid, key);
			return true;
		}
		
		return false;
	}
	
	//tosy 	更新需要录制视频的主播名单
	public static boolean updateRecordUid(String uid, String isRecord) {
		if(null == uid){
			return false;
		}
		
		if(null == isRecord){
			OtherRedisService.getInstance().removefromRecordSet(uid);
		}else{
			OtherRedisService.getInstance().addtoRecordSet(uid);
		}
		
		return true;
	}
}
