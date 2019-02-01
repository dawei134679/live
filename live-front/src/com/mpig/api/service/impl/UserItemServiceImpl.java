 package com.mpig.api.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.mpig.api.dao.IUserItemDao;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.model.ConfigGiftModel;
import com.mpig.api.model.UserGuardInfoModel;
import com.mpig.api.model.UserItemLogModel;
import com.mpig.api.model.UserItemModel;
import com.mpig.api.model.UserItemSpecialModel;
import com.mpig.api.redis.service.RedisCommService;
import com.mpig.api.redis.service.UserRedisService;
import com.mpig.api.service.IConfigService;
import com.mpig.api.service.IUserItemService;
import com.mpig.api.service.IUserItemService.ItemSource;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.VarConfigUtils;

import com.mpig.api.SqlTemplete;

@Service
public class UserItemServiceImpl implements IUserItemService, SqlTemplete {

	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	private static UserItemServiceImpl instance = null;
	
	@Autowired
	private IUserItemDao userItemDao;
	
	static{
		if (instance == null) {
			instance = new UserItemServiceImpl();
		}
	}

	@SuppressWarnings("unused")
	@Resource
	private IConfigService configService;
	
	/**
	 * 获取用户背包的礼物 供app使用
	 * @param uid
	 * @return list map<gid,num>
	 */
	@Override
	public List<Map<String, Object>> getBagByUid(int uid) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<Integer,UserItemModel> itemList = this.getItemListByUid(uid, false);
//		Map<Integer,UserItemSpecialModel> itemSpecailList = this.getItemSpecialByUid(uid, false);
		
		for(Map.Entry<Integer, UserItemModel> enter:itemList.entrySet()){
			if (enter.getValue() != null && enter.getValue().getNum() > 0) {
				Map<String, Object> map = new HashMap<String,Object>();
				map.put("type", enter.getValue().getType());
				map.put("gid", enter.getValue().getGid());
				map.put("num", enter.getValue().getNum());
				map.put("gprice", enter.getValue().getGprice());
				list.add(map);
			}
		}
//		for(Map.Entry<Integer, UserItemSpecialModel> enter:itemSpecailList.entrySet()){
//			if (enter.getValue() != null && enter.getValue().getNum() > 0) {
//				Map<String, Object> map = new HashMap<String,Object>();
//				map.put("type", enter.getValue().getType());
//				map.put("gid", enter.getValue().getGid());
//				map.put("num", enter.getValue().getNum());
//				list.add(map);
//			}
//		}
		return list;
	}
	
	@Override
	public Map<Integer,UserItemSpecialModel> getItemSpecialByUid(int uid,boolean bl){
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		Map<Integer,UserItemSpecialModel> map = new HashMap<Integer,UserItemSpecialModel>();
		

		if (!bl) {
			Map<String, String> userItemList = UserRedisService.getInstance().getUserItemSpecialList(uid);
			if (userItemList != null) {
				for(Map.Entry<String, String> entry:userItemList.entrySet()){
					if (org.apache.commons.lang.StringUtils.isNotEmpty(entry.getValue())) {
						map.put(Integer.valueOf(entry.getKey()), JSONObject.parseObject(entry.getValue(), UserItemSpecialModel.class));
					}
				}
			}
		}
		try {
			if (map.size() <= 0) {
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuItem).getConnection();
				statement = conn
						.prepareStatement(SQL_getUserItemSpecialByUid);
				DBHelper.setPreparedStatementParam(statement, uid);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						UserItemSpecialModel userItemSpecailModel = new UserItemSpecialModel().populateFromResultSet(rs);
						if (userItemSpecailModel != null) {
							UserRedisService.getInstance().setUserItemSpecialInfo(uid, userItemSpecailModel.getGid(), JSONObject.toJSONString(userItemSpecailModel));
							map.put(userItemSpecailModel.getGid(),userItemSpecailModel);
						}
					}
				}
			}
		} catch (SQLException e) {
			logger.error("<getItemSpecialByUid->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getItemSpecialByUid->Exception>" + e.getMessage());
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
				logger.error("<getItemSpecialByUid->finally->Exception>" + e2.getMessage());
			}
		}
		return map;
	}
	
	/**
	 * 根据用户获取背包中的普通礼物
	 * @param uid
	 * @param bl
	 */
	@Override
	public Map<Integer,UserItemModel> getItemListByUid(int uid,boolean bl){
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		Map<Integer,UserItemModel> map = new HashMap<Integer,UserItemModel>();
		

		if (!bl) {
			Map<String, String> userItemList = UserRedisService.getInstance().getUserItemList(uid);
			if (userItemList != null) {
				for(Map.Entry<String, String> entry:userItemList.entrySet()){
					if (org.apache.commons.lang.StringUtils.isNotEmpty(entry.getValue())) {
						map.put(Integer.valueOf(entry.getKey()), JSONObject.parseObject(entry.getValue(), UserItemModel.class));
					}
				}
			}
		}
		try {
			if (map.size() <= 0) {
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuItem).getConnection();
				statement = conn
						.prepareStatement(SQL_getUserItemByUid);
				DBHelper.setPreparedStatementParam(statement, uid);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						UserItemModel userItemModel = new UserItemModel().populateFromResultSet(rs);
						if (userItemModel != null) {
							if (userItemModel.getNum() > 0) {
								UserRedisService.getInstance().setUserItemInfo(uid, userItemModel.getGid(), JSONObject.toJSONString(userItemModel));
								map.put(userItemModel.getGid(),userItemModel);
							}else {
								// 清除 已送完的礼物
								RedisCommService.getInstance().hdel(RedisContant.RedisNameUser,RedisContant.userItem+userItemModel.getUid(), String.valueOf(userItemModel.getGid()));
							}
						}
					}
				}
			}
		} catch (SQLException e) {
			logger.error("<getItemListByUid->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getItemListByUid->Exception>" + e.getMessage());
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
				logger.error("<getItemListByUid->finally->Exception>" + e2.getMessage());
			}
		}
		return map;
	}

	@Override
	public int updUserItemBySendUid(int uid, int gid,int count) {
		int res = 0;
		try {
			res = DBHelper.execute(VarConfigUtils.dbZhuItem, SQL_UpdUserItemBySendUid, false, count, uid, gid,count);
			if (res > 0) {
				this.getItemListByUid(uid, true);
			}else {
				// 清除 已送完的礼物
				RedisCommService.getInstance().hdel(RedisContant.RedisNameUser,RedisContant.userItem+uid, String.valueOf(gid));
			}
		} catch (Exception e) {
			logger.error("<updUserItemBySendUid->Exception>" + e.getMessage());
		}
		return res;
	}

	@Override
	public  void insertUserItem(final int uid, int gid, int num, ItemSource source) {
		insertUserItemStatic(uid,gid,num,source);
	}
	
	/**
	 * @param uid
	 * @param gid
	 * @param num
	 * @param source 1-任务 2-活动 3-商城
	 */
	public static void insertUserItemStatic(final int uid, int gid, int num, ItemSource source) {
		ConfigGiftModel giftInfo = ConfigServiceImpl.getGiftInfoByGid(gid);

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuItem).getConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

			long nowLg = System.currentTimeMillis() / 1000;

			if (giftInfo.getType() == 3) {
				// 时效礼物
				rs = stmt
						.executeQuery("select gid,endtime from user_item_special where uid=" + uid + " and gid=" + gid);

				if (rs != null && rs.next()) {
					// 修改背包数据
					int endtime = rs.getInt("endtime");
					if (endtime < nowLg) {
						// 过期
						 stmt.execute("update user_item_special set num = " + num + ",starttime=" + nowLg + ",endtime="
								+ (nowLg + giftInfo.getUseDuration() * num * 86400) + " where uid=" + uid + " and gid="
								+ gid);
					} else {
						stmt.execute("update user_item_special set num = num+" + num + ",endtime=endtime+"
								+ (giftInfo.getUseDuration() * num * 86400) + " where uid=" + uid + " and gid=" + gid);
					}
				} else {
					// 新增背包
					stmt.execute("INSERT INTO user_item_special(uid,gid,type,subtype,num,starttime,endtime) VALUES(" + uid + ","
							+ gid + "," + giftInfo.getType()+","+giftInfo.getSubtype() + "," + num + "," + nowLg + ","
							+ (nowLg + giftInfo.getUseDuration() * num * 86400) + ")");
				}

			} else {
				rs = stmt.executeQuery("select gid from user_item where uid=" + uid + " and gid=" + gid);

				if (rs != null && rs.next()) {
					stmt.execute("update user_item set num=num+" + num + " where uid=" + uid + " and gid=" + gid);
				} else {
					stmt.execute("INSERT INTO user_item(uid,gid,type,subtype,num) VALUES(" + uid + "," + gid + ","
							+ giftInfo.getType()+","+giftInfo.getSubtype() + "," + num + ")");
				}
			}
			stmt.execute("insert into user_item_log(uid,gid,type,subtype,num,sourc,addtime)value(" + uid + "," + gid + ","
					+ giftInfo.getType()+","+giftInfo.getSubtype() + "," + num + "," + source.ordinal() + "," + nowLg + ")");
			
			conn.commit();
			if (giftInfo.getType() == 3) {
				instance.getItemSpecialByUid(uid, true);
			}else {
				instance.getItemListByUid(uid, true);
			}

			instance.getBadgeListByUid(uid, true);
			
//			if (giftInfo.getSubtype() == 6) {
//				instance.getBadgeListByUid(uid, true);
//			}
		} catch (Exception ex) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				logger.error(String.format("insertUserItem-SQLException:uid:%d,cause:%s", uid, ex.toString()));
				logger.error("insertUserItem-SQLException：",e);
			}
			logger.error(String.format("insertUserItem-exception:uid:%d,cause:%s", uid, ex.toString()));
			logger.error("insertUserItem-exception detail：",ex);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				logger.error("insertUserItem-finally-exception:", e);
			}
		}
	}
	
	@Override
	public void insertUserItemNew(int uid, int gid, int num) {
		ConfigGiftModel giftInfo = ConfigServiceImpl.getGiftInfoByGid(gid);

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuItem).getConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
			long nowLg = System.currentTimeMillis() / 1000;
			rs = stmt.executeQuery("select gid from user_item where uid=" + uid+ " and gid=" + gid);

			if (rs != null && rs.next()) {
				stmt.execute("update user_item set num=num+" + num+ " where uid=" + uid + " and gid=" + gid);
			} else {
				stmt.execute("INSERT INTO user_item(uid,gid,type,subtype,num) VALUES("
						+ uid
						+ ","
						+ gid
						+ ","
						+ giftInfo.getType()
						+ ","
						+ giftInfo.getSubtype() + "," + num + ")");
			}
			conn.commit();
			instance.getItemListByUid(uid, true);
			instance.getBadgeListByUid(uid, true);
		} catch (Exception ex) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				logger.error(String.format("insertUserItemNew-SQLException:uid:%d,cause:%s", uid,ex.toString()));
			}
			logger.error(String.format("insertUserItemNew-exception:uid:%d,cause:%s", uid,ex.toString()));
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				logger.error("insertUserItemNew-finally-exception:", e);
			}
		}
	}
	@Override
	public int insertUserItemLog(int uid, int gid, int num, ItemSource source) {
		ConfigGiftModel giftInfo = ConfigServiceImpl.getGiftInfoByGid(gid);
		long nowLg = System.currentTimeMillis() / 1000;
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuItem, "insert into user_item_log(uid,gid,type,subtype,num,sourc,addtime)value(?,?,?,?,?,?,?)", false, uid, gid, giftInfo.getType(), giftInfo.getSubtype(),num,source.ordinal(),nowLg);
			return executeResult;
		} catch (Exception e) {
			logger.error("<updUserGuardInfo->Exception>" + e.toString());
		}
		return -1;
	}

	@Override
	public List<Integer> getBadgeListByUid(int uid,boolean bl) {
		List<Integer> list = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		Long lgNow = System.currentTimeMillis()/1000;

		if (!bl) {
			Map<String, String> userItemList = UserRedisService.getInstance().getUserBadge(uid);
			if (userItemList != null) {
				for(Map.Entry<String, String> entry:userItemList.entrySet()){
					
					if (org.apache.commons.lang.StringUtils.isNotEmpty(entry.getValue())) {
						UserItemSpecialModel userItemSpecialModel = (UserItemSpecialModel)JSONObject.parseObject(entry.getValue(),UserItemSpecialModel.class);
						if (userItemSpecialModel.getStarttime() <= lgNow && userItemSpecialModel.getEndtime() > lgNow) {
							list.add(Integer.valueOf(entry.getKey()));
						}
					}
				}
			}
		}
		try {
			if (list.size() <= 0) {
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuItem).getConnection();
				statement = conn
						.prepareStatement(SQL_getBadgeListByUid);
				DBHelper.setPreparedStatementParam(statement, uid);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						UserItemSpecialModel userItemSpecialModel = new UserItemSpecialModel().populateFromResultSet(rs);
						if (userItemSpecialModel != null) {
							UserRedisService.getInstance().setUserBadge(uid, userItemSpecialModel.getGid(), JSONObject.toJSONString(userItemSpecialModel));
							list.add(userItemSpecialModel.getGid());
						}
					}
				}
			}
		} catch (SQLException e) {
			logger.error("<getBadgeListByUid->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getBadgeListByUid->Exception>" + e.getMessage());
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
				logger.error("<getBadgeListByUid->finally->Exception>" + e2.getMessage());
			}
		}
		return list;
	}

	@Override
	public int getItemCountByGid(Integer uid, Integer gid) {
		return userItemDao.getItemCountByGid(uid, gid);
	}

	@Override
	public int delItemByGid(Integer uid, Integer gid) {
		int rsc = userItemDao.delItemByGid(uid, gid);
		if(rsc > 0){
			instance.getItemListByUid(uid, true);
		}
		return 0;
	}
	
	@Override
	public List<Map<String,Object>> selItemLogBySubType(ItemSource source,int start, int rows) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		List<Map<String,Object>> userItemLogModels = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = null;
		try {
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuItem).getConnection();
				statement = conn.prepareStatement(SqlTemplete.SQL_SelUserItemLogBySubType);
				DBHelper.setPreparedStatementParam(statement, source.ordinal(), start, rows);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						map = new HashMap<String,Object>();
						map.put("uid", rs.getInt("uid"));
						map.put("gid", rs.getInt("gid"));
						map.put("num", rs.getInt("num"));
						map.put("subtype", rs.getInt("subtype"));
						map.put("addtime", rs.getInt("addtime"));
						userItemLogModels.add(map);
					}
				}
		} catch (SQLException e) {
			logger.error("<selItemLogBySubType->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<selItemLogBySubType->Exception>" + e.getMessage());
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
				logger.error("<selItemLogBySubType->finally->Exception>" + e2.getMessage());
			}
		}
		return userItemLogModels;
	}
	
}
