package com.mpig.api.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bcloud.msg.http.HttpSender;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mpig.api.SqlTemplete;
import com.mpig.api.async.AsyncManager;
import com.mpig.api.async.IAsyncTask;
import com.mpig.api.dao.IUserDao;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.dictionary.LevelsConfig;
import com.mpig.api.dictionary.lib.ExchangeConfigLib;
import com.mpig.api.dictionary.lib.LevelsConfigLib;
import com.mpig.api.dictionary.lib.LivingListConfigLib;
import com.mpig.api.dictionary.lib.PayConfigLib;
import com.mpig.api.dictionary.lib.TaskConfigLib;
import com.mpig.api.dictionary.lib.UrlConfigLib;
import com.mpig.api.model.AuthenticationModel;
import com.mpig.api.model.PayAccountModel;
import com.mpig.api.model.PayExchangeModel;
import com.mpig.api.model.RecordItem;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.RobotBaseInfoModel;
import com.mpig.api.model.UserAccountModel;
import com.mpig.api.model.UserAssetModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.model.UserVipInfoModel;
import com.mpig.api.model.UserXiaozhuAuthModel;
import com.mpig.api.model.ValueaddPrivilegeModel;
import com.mpig.api.modelcomet.BaseCMod;
import com.mpig.api.modelcomet.ExitRoomCMod;
import com.mpig.api.modelcomet.FollowCMod;
import com.mpig.api.modelcomet.UpdateAccountStatusCMod;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.redis.service.RedisCommService;
import com.mpig.api.redis.service.RelationRedisService;
import com.mpig.api.redis.service.UserRedisService;
import com.mpig.api.service.IConfigService;
import com.mpig.api.service.ILiveService;
import com.mpig.api.service.IOrderService;
import com.mpig.api.service.IRoomService;
import com.mpig.api.service.IUserInfoService;
import com.mpig.api.service.IUserItemService;
import com.mpig.api.service.IUserService;
import com.mpig.api.task.TaskService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.EncryptUtils;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.SmsIhwxUtil;
import com.mpig.api.utils.SpringContextUtil;
import com.mpig.api.utils.StringUtils;
import com.mpig.api.utils.ValueaddServiceUtil;
import com.mpig.api.utils.VarConfigUtils;
import com.mysql.fabric.xmlrpc.base.Array;

import redis.clients.jedis.Tuple;

@Service
public class UserServiceImpl implements IUserService, SqlTemplete {
	
	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

	private static UserServiceImpl instance = null;

	public static UserServiceImpl getInstance() {
		if(instance==null) {
			instance = SpringContextUtil.getBean("userServiceImpl");
		}
		return instance;
	}

	@Autowired
	private IUserDao userDao;

	@Resource
	private ILiveService liveService;
	@Resource
	private IOrderService payService;
	@Resource
	private IConfigService configService;
	@Resource
	private IRoomService roomService;

	@Resource
	private WeixinServiceImpl weixinService;

	@Resource
	private IUserItemService userItemService;
	
	@Resource
	private IUserInfoService userInfoService;

	@Override
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
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
				statement = conn
						.prepareStatement(StringUtils.getSqlString(SQL_GetUserAccountByUid, "user_account_", uid));
				DBHelper.setPreparedStatementParam(statement, uid);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						userAccountModel = new UserAccountModel().populateFromResultSet(rs);
						// 缓存
						UserRedisService.getInstance().setUserAccount(uid, JSONObject.toJSONString(userAccountModel));
						userInfoService.updateUserInfoAccount(userAccountModel);
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

	@Override
	public UserAssetModel getUserAssetByUid(int uid, Boolean syncRedis) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		UserAssetModel userAssetModel = null;
		if (!syncRedis) {
			// 读缓存
			String userAsset = UserRedisService.getInstance().getUserAsset(uid);
			if (userAsset != null && !"".equals(userAsset)) {
				userAssetModel = (UserAssetModel) JSONObject.parseObject(userAsset, UserAssetModel.class);
			}
		}
		try {
			if (userAssetModel == null) {
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
				statement = conn.prepareStatement(StringUtils.getSqlString(SQL_GetUserAssetByUid, "user_asset_", uid));
				DBHelper.setPreparedStatementParam(statement, uid);
				rs = statement.executeQuery();
				if (rs != null) {
					while (rs.next()) {
						userAssetModel = new UserAssetModel().populateFromResultSet(rs);
						UserRedisService.getInstance().setUserAsset(uid, JSONObject.toJSONString(userAssetModel));
						userInfoService.updateUserInfoBase(userAssetModel);
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

	@Override
	public Map<String, UserBaseInfoModel> getUserbaseInfoByUid(String... uids) {

		Map<String, UserBaseInfoModel> map = new HashMap<String, UserBaseInfoModel>();
		List<String> userBaseInfo = UserRedisService.getInstance().getUserBaseInfo(uids);
		if (userBaseInfo != null) {
			for (String string : userBaseInfo) {
				UserBaseInfoModel userBaseInfoModel = null;
				userBaseInfoModel = (UserBaseInfoModel) JSONObject.parseObject(string, UserBaseInfoModel.class);
				if (userBaseInfoModel != null) {
					map.put(userBaseInfoModel.getUid().toString(), userBaseInfoModel);
				}
			}
		}
		return map;
	}

	@Override
	public Map<String, UserAccountModel> getUserAccountByUid(String... uids) {

		Map<String, UserAccountModel> map = new HashMap<String, UserAccountModel>();
		List<String> userAccount = UserRedisService.getInstance().getAccountInfo(uids);
		if (userAccount != null) {
			for (String string : userAccount) {
				UserAccountModel userAccountModel = null;
				userAccountModel = (UserAccountModel) JSONObject.parseObject(string, UserAccountModel.class);
				if (userAccountModel != null) {
					map.put(userAccountModel.getUid().toString(), userAccountModel);
				}
			}
		}
		return map;
	}

	@Override
	public UserBaseInfoModel getUserbaseInfoByUid(int uid, Boolean directReadMysql) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		UserBaseInfoModel userBaseInfoModel = null;

		if (!directReadMysql) {
			// 读缓存
			String userbaseinfo = UserRedisService.getInstance().getUserBaseInfo(uid);
			if (org.apache.commons.lang.StringUtils.isNotEmpty(userbaseinfo)) {
				userBaseInfoModel = (UserBaseInfoModel) JSONObject.parseObject(userbaseinfo, UserBaseInfoModel.class);
			}
		}
		try {
			if (userBaseInfoModel == null) {
				// 获取用户基本信息
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
				statement = conn
						.prepareStatement(StringUtils.getSqlString(SQL_GetUserBaseInfoByUid, "user_base_info_", uid));
				DBHelper.setPreparedStatementParam(statement, uid);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						userBaseInfoModel = new UserBaseInfoModel().populateFromResultSet(rs);
						UserXiaozhuAuthModel userXiaozhuAuthModel = this.getXiaozhuAuth(uid);
						if (userXiaozhuAuthModel != null) {
							if (userXiaozhuAuthModel.getStatus() == 3) {
								userBaseInfoModel.setNickname(userXiaozhuAuthModel.getNickname());
								userBaseInfoModel.setVerified(true);
								userBaseInfoModel.setVerified_reason(userXiaozhuAuthModel.getAuthContent());
							}
						}
						UserRedisService.getInstance().setUserBaseInfo(uid, JSONObject.toJSONString(userBaseInfoModel));
						userInfoService.updateUserInfoBase(userBaseInfoModel);
						break;
					}
				}
			}
		} catch (SQLException e) {
			logger.error("<getUserbaseInfoByUid->SQLException>", e);
		} catch (Exception e) {
			logger.error("<getUserbaseInfoByUid->Exception>", e);
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
				logger.error("<getUserbaseInfoByUid->finally->Exception>" + e2.getMessage());
			}
		}
		return userBaseInfoModel;
	}

	@Override
	public RobotBaseInfoModel getRobotbaseInfoByUid(int uid, Boolean bl) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		RobotBaseInfoModel robotBaseInfoModel = null;

		if (!bl) {
			// 读缓存
			String robotbaseinfo = UserRedisService.getInstance().getRobotBaseInfo(uid);
			if (org.apache.commons.lang.StringUtils.isNotEmpty(robotbaseinfo)) {
				robotBaseInfoModel = (RobotBaseInfoModel) JSONObject.parseObject(robotbaseinfo,
						RobotBaseInfoModel.class);
			}
		}
		try {
			if (robotBaseInfoModel == null) {
				// 获取用户基本信息
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
				statement = conn.prepareStatement(SQL_GetRobotByUid);
				DBHelper.setPreparedStatementParam(statement, uid);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						robotBaseInfoModel = new RobotBaseInfoModel().populateFromResultSet(rs);
						UserRedisService.getInstance().setRobotBaseInfo(uid,
								JSONObject.toJSONString(robotBaseInfoModel));
						break;
					}
				}
			}
		} catch (SQLException e) {
			logger.error("<getRobotbaseInfoByUid->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getRobotbaseInfoByUid->Exception>" + e.getMessage());
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
		return robotBaseInfoModel;
	}

	@Override
	public UserAccountModel getUserAccountByAuthKey(String authKey, Boolean bl) {
		// 通过缓存获取uid
		Integer uid = UserRedisService.getInstance().getAuthKeyAndUid(authKey.toLowerCase());
		if (uid <= 0) {
			return null;
		}
		return this.getUserAccountByUid(uid, false);
	}

	@Override
	public UserAccountModel getUserAccountByUnionId(String unionid, Boolean bl) {
		// 通过缓存获取uid
		Integer uid = UserRedisService.getInstance().getUnionIdAndUid(unionid);
		if (uid <= 0) {
			return null;
		}
		return this.getUserAccountByUid(uid, false);
	}

	/**
	 * 生成用户UID
	 *
	 * @return uid
	 */
	@Override
	public int getUid() {
		int uid = 0;
		try {
			uid = DBHelper.execute(VarConfigUtils.dbZhuUser, SQL_GetUid, true, System.currentTimeMillis() / 1000);
		} catch (Exception e) {
			logger.error("<getUid->SQLException>" + e.getMessage());
		}
		return uid;
	}

	/**
	 * 生成用户靓号accountId
	 *
	 * @return uid
	 */
	@Override
	public int getAccountId() {
		int uid = 0;
		try {
			uid = DBHelper.execute(VarConfigUtils.dbZhuUser, SQL_GetAccountId, true, System.currentTimeMillis() / 1000);
		} catch (Exception e) {
			logger.error("<getUid->SQLException>" + e.getMessage());
		}
		return uid;
	}

	/**
	 * 新增用户账号
	 *
	 * @return res 影响行数
	 */
	@Override
	public int InsertUserAccount(Object... objects) {
		int res = 0;
		try {
			String sql = StringUtils.getSqlString(SQL_InsertUserAccount, "user_account_",
					((Integer) objects[0]).intValue());
			res = DBHelper.execute(VarConfigUtils.dbZhuUser, sql, false, objects);
			if (res > 0) {
				this.getUserAccountByUid(((Integer) objects[0]).intValue(), true);
			}
			return res;
		} catch (Exception e) {
			logger.error("<InsertUserAccount->Exception>" + e.getMessage());
		}
		return res;
	}

	/**
	 * 新增用户基本信息
	 *
	 * @return res 影响行数
	 */
	@Override
	public int InsertUserBaseInfo(Object... objects) {
		int res = 0;
		try {
			res = DBHelper.execute(VarConfigUtils.dbZhuUser, StringUtils.getSqlString(SQL_InsertUserBaseInfo,
					"user_base_info_", ((Integer) objects[0]).intValue()), false, objects);
			if (res > 0) {
				this.getUserbaseInfoByUid(((Integer) objects[0]).intValue(), true);
				// // 同步后台数据
				// Map<String, Object> map = new HashMap<String, Object>();
				// map.put("db", "zhu_admin");
				// map.put("sql", String.format(SQL_InsertUserBaseInfo,
				// "user_base_info"));
				// map.put("fields", objects);
				// OtherRedisService.getInstance().pushQueue(map);
			}
			return res;
		} catch (Exception e) {
			logger.error("<InsertUserBaseInfo->Exception>" + e.getMessage());
		}
		return res;
	}

	/**
	 * 新增用户资产信息
	 *
	 * @return 影响行数
	 */
	@Override
	public int InsertUserAsset(Object... objects) {
		int res = 0;
		try {
			String sql = StringUtils.getSqlString(SQL_InsertUserAsset, "user_asset_",
					((Integer) objects[0]).intValue());
			res = DBHelper.execute(VarConfigUtils.dbZhuUser, sql, false, objects);
			if (res > 0) {
				this.getUserAssetByUid(((Integer) objects[0]).intValue(), true);
				// // 同步后台数据
				// Map<String, Object> map = new HashMap<String, Object>();
				// map.put("db", "zhu_admin");
				// map.put("sql", String.format(SQL_InsertUserAsset,
				// "user_asset"));
				// map.put("fields", objects);
				// OtherRedisService.getInstance().pushQueue(map);
			}
			return res;
		} catch (Exception e) {
			logger.error("<InsertUserAsset->Exception>" + e.getMessage());
		}
		return res;
	}

	/**
	 * 获取用户简介
	 *
	 * @param uid
	 */
	@Override
	public List<Map<String, Object>> getUserProfile(String... uids) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		List<String> userBaseInfo = UserRedisService.getInstance().getUserBaseInfo(uids);

		if (userBaseInfo != null) {
			for (String string : userBaseInfo) {
				UserBaseInfoModel userBaseInfoModel = null;
				Map<String, Object> map = new HashMap<String, Object>();
				userBaseInfoModel = (UserBaseInfoModel) JSONObject.parseObject(string, UserBaseInfoModel.class);
				if (userBaseInfoModel != null) {
					map.put("uid", userBaseInfoModel.getUid());
					map.put("identity", userBaseInfoModel.getIdentity());
					map.put("headimage", userBaseInfoModel.getHeadimage());
					map.put("userLevel", userBaseInfoModel.getUserLevel());
					map.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
					map.put("nickName", userBaseInfoModel.getNickname());
					map.put("sex", userBaseInfoModel.getSex());
					list.add(map);
				}
			}
		}
		return list;
	}

	/**
	 * 获取用户简介
	 *
	 * @param uid
	 */
	@Override
	public Map<String, Object> getUserProfile(int uid) {

		Map<String, Object> map = new HashMap<String, Object>();

		UserBaseInfoModel userBaseInfoModel = this.getUserbaseInfoByUid(uid, false);

		if (userBaseInfoModel != null) {
			map.put("uid", userBaseInfoModel.getUid());
			map.put("identity", userBaseInfoModel.getIdentity());
			map.put("headimage", userBaseInfoModel.getHeadimage());
			map.put("userLevel", userBaseInfoModel.getUserLevel());
			map.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
			map.put("nickName", userBaseInfoModel.getNickname());
			map.put("sex", userBaseInfoModel.getSex());
		}
		return map;
	}

	/**
	 * 获取用户简介
	 *
	 * @param uid
	 */
	@Override
	public List<Map<String, Object>> getRobotProfile(String... uids) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		List<String> userBaseInfo = UserRedisService.getInstance().getRobotBaseInfo(uids);

		if (userBaseInfo != null) {
			for (String string : userBaseInfo) {
				UserBaseInfoModel userBaseInfoModel = null;
				Map<String, Object> map = new HashMap<String, Object>();
				userBaseInfoModel = (UserBaseInfoModel) JSONObject.parseObject(string, UserBaseInfoModel.class);
				if (userBaseInfoModel != null) {
					map.put("uid", userBaseInfoModel.getUid());
					map.put("identity", userBaseInfoModel.getIdentity());
					map.put("headimage", userBaseInfoModel.getHeadimage());
					map.put("userLevel", userBaseInfoModel.getUserLevel());
					map.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
					map.put("nickName", userBaseInfoModel.getNickname());
					map.put("sex", userBaseInfoModel.getSex());
					list.add(map);
				}
			}
		}
		return list;
	}

	/**
	 * 获取用户简介
	 *
	 * @param uid
	 */
	@Override
	public Map<String, Object> getRobotProfile(int uid) {

		Map<String, Object> map = new HashMap<String, Object>();
		RobotBaseInfoModel robotBaseInfoModel = this.getRobotbaseInfoByUid(uid, false);

		if (robotBaseInfoModel != null) {
			map.put("uid", robotBaseInfoModel.getUid());
			map.put("identity", robotBaseInfoModel.getIdentity());
			map.put("headimage", robotBaseInfoModel.getHeadimage());
			map.put("userLevel", robotBaseInfoModel.getUserLevel());
			map.put("anchorLevel", robotBaseInfoModel.getAnchorLevel());
			map.put("nickName", robotBaseInfoModel.getNickname());
			map.put("sex", robotBaseInfoModel.getSex());
		}
		return map;
	}

	@Override
	public int updUserAssetBySendUid(int uid, int money, int wealths) {
		int res = 0;
		try {
			String sql = StringUtils.getSqlString(SQL_UpdUserAssetBySendUid, "user_asset_", uid);
			res = DBHelper.execute(VarConfigUtils.dbZhuUser, sql, false, money, wealths, uid, money);
			if (res > 0) {
				this.getUserAssetByUid(uid, true);
				// // 同步后台数据
				// Object[] fields = new Object[4];
				// fields[0] = money;
				// fields[1] = money;
				// fields[2] = uid;
				// fields[3] = money;
				//
				// Map<String, Object> map = new HashMap<String, Object>();
				// map.put("db", "zhu_admin");
				// map.put("sql", String.format(SQL_UpdUserAssetBySendUid,
				// "user_asset"));
				// map.put("fields", fields);
				// OtherRedisService.getInstance().pushQueue(map);
			}
		} catch (Exception e) {
			logger.error("<updUserAssetBySendUid->Exception>" + e.getMessage());
		}
		return res;
	}

	@Override
	public int updUserAssetByExchangeUid(int uid, int credit, int money) {
		int res = 0;
		try {
			String sql = StringUtils.getSqlString(Sql_UpdUserAssetCreditByUid, "user_asset_", uid);
			res = DBHelper.execute(VarConfigUtils.dbZhuUser, sql, false, credit, money, uid, credit);
			if (res > 0) {
				this.getUserAssetByUid(uid, true);
			}
		} catch (Exception e) {
			logger.error("<updUserAssetBySendUid->Exception>" + e.getMessage());
		}
		return res;
	}

	@Override
	public int updUserAssetByPcExchange(int uid, int credit) {
		int res = 0;
		try {
			String sql = StringUtils.getSqlString(Sql_UpdUserAssetCreditForExchange, "user_asset_", uid);
			res = DBHelper.execute(VarConfigUtils.dbZhuUser, sql, false, credit, credit, uid, credit);
			if (res > 0) {
				this.getUserAssetByUid(uid, true);
			}
		} catch (Exception e) {
			logger.error("<updUserAssetByPcExchange->Exception>" + e.getMessage());
		}
		return res;
	}

	/**
	 * 用户发红包
	 * 
	 * @param uid
	 * @param money
	 * @return
	 */
	@Override
	public int updUserAssetByRedEnvelopUid(int uid, int money) {
		int res = 0;
		String sql = StringUtils.getSqlString(Sql_UpdUserAssetByRedEnevlopUid, "user_asset_", uid);
		try {
			res = DBHelper.execute(VarConfigUtils.dbZhuUser, sql, false, money, uid, money);
			if (res > 0) {
				this.getUserAssetByUid(uid, true);
				// // 同步后台数据
				// Object[] fields = new Object[4];
				// fields[0] = money;
				// fields[1] = uid;
				// fields[2] = money;
				//
				// Map<String, Object> map = new HashMap<String, Object>();
				// map.put("db", "zhu_admin");
				// map.put("sql", String.format(Sql_UpdUserAssetByRedEnevlopUid,
				// "user_asset"));
				// map.put("fields", fields);
				// OtherRedisService.getInstance().pushQueue(map);
			}
		} catch (Exception e) {
			logger.error("<updUserAssetByRedEnvelopUid->Exception>" + e.getMessage());
		}
		return res;
	}

	@Override
	public int updUserAssetByGetUid(int uid, Double gets, Double getsTotal) {
		int res = 0;
		try {
			String sql = StringUtils.getSqlString(SQL_UpdUserAssetByGetUid, "user_asset_", uid);
			res = DBHelper.execute(VarConfigUtils.dbZhuUser, sql, false, gets, getsTotal, uid);
			if (res > 0) {
				this.getUserAssetByUid(uid, true);
				// // 同步后台数据
				// Object[] fields = new Object[3];
				// fields[0] = gets;
				// fields[1] = gets;
				// fields[2] = uid;
				//
				// Map<String, Object> map = new HashMap<String, Object>();
				// map.put("db", "zhu_admin");
				// map.put("sql", String.format(SQL_UpdUserAssetByGetUid,
				// "user_asset"));
				// map.put("fields", fields);
				// OtherRedisService.getInstance().pushQueue(map);
			}
		} catch (Exception e) {
			logger.error("<updUserAssetByGetUid->Exception>" + e.getMessage());
		}
		return res;
	}

	/**
	 * 更新用户等级
	 *
	 * @param level
	 * @param uid
	 * @param type
	 *            =1表示用户 =2表示主播
	 * @return
	 */
	@Override
	public int updUserBaseInfoLevelByUid(int level, int uid, int type) {
		int res = 0;
		try {
			String sqlString = type == 1
					? StringUtils.getSqlString(SQL_UpdUserBaseInfoUserLevelByUid, "user_base_info_", uid)
					: StringUtils.getSqlString(SQL_UpdUserBaseInfoAnchorLevelByUid, "user_base_info_", uid);
			res = DBHelper.execute(VarConfigUtils.dbZhuUser, sqlString, false, level, uid);
			if (res > 0) {
				this.getUserbaseInfoByUid(uid, true);
				// Object[] fields = new Object[2];
				// fields[0] = level;
				// fields[1] = uid;
				//
				// Map<String, Object> map = new HashMap<String, Object>();
				// map.put("db", "zhu_admin");
				// if (type == 1) {
				// map.put("sql",
				// String.format(SQL_UpdUserBaseInfoUserLevelByUid,
				// "user_base_info"));
				// } else {
				// map.put("sql",
				// String.format(SQL_UpdUserBaseInfoAnchorLevelByUid,
				// "user_base_info"));
				// }
				// map.put("fields", fields);
				// OtherRedisService.getInstance().pushQueue(map);
			}
		} catch (Exception e) {
			logger.error("<updUserBaseInfoUserLevelByUid->Exception>" + e.toString());
		}
		return res;
	}

	/**
	 * 获取用户资料 弹层使用
	 *
	 * @param dstUid
	 *            资料用户
	 * @param uid
	 *            当前用户
	 * @return
	 */
	@Override
	public Map<String, Object> getUserDataMap(int dstUid, int uid) {

		Map<String, Object> map = new HashMap<String, Object>();
		if (dstUid >= 900000000) {
			// 机器人
			map = this.getRobotByUid(dstUid);

			map.put("familyId", 0);
			map.put("livimage", "");
			map.put("signature", "");
			map.put("sex", true);
			map.put("anchorLevel", 0);
			map.put("phone", "");
			map.put("anchorNext", 0);
			map.put("userNext", 0);
			map.put("money", 0);
			map.put("city", VarConfigUtils.Location);
			map.put("identity", 1);
			map.put("follows", RelationRedisService.getInstance().getFollowsTotal(dstUid));
			map.put("fans", RelationRedisService.getInstance().getFansTotal(dstUid));// 粉丝数
			if (dstUid != uid) {
				map.put("isfan", RelationRedisService.getInstance().isFan(uid, dstUid));// 是否关注过
			} else {
				map.put("isfan", true);
			}
			map.put("creditTotal", 0); // 总声援值
			map.put("wealth", 0);
			map.put("creditToDay", 0); // 当天声援值
			map.put("credit", 0);// 提现声援值
			map.put("likes", 0);
			map.put("status", 0);
			map.put("verified_reason", false); // 认证原因
			map.put("verified", ""); // false 未认证
										// true
			map.put("domain", "");
			map.put("weixin", "");
			map.put("wx_openid", "");
			map.put("wx_unionid", "");
			map.put("alipay", "");
			map.put("isuse", 0);
			map.put("exp", 0);
			map.put("badges", new Array());
			map.put("rq", UserRedisService.getInstance().getUserAllRankRqScore(String.valueOf(dstUid)));
			map.put("constellation", "");
			map.put("hobby", "");
			map.put("monthSupport", UserRedisService.getInstance().getSupportByUid(dstUid, 4));//新声援值
		} else {
			UserBaseInfoModel userBaseInfoModel = this.getUserbaseInfoByUid(dstUid, false);
			UserAssetModel userAssetModel = this.getUserAssetByUid(dstUid, false);
			PayAccountModel payAccountModel = payService.getPayAccountByUid(dstUid, false);
			if (userBaseInfoModel == null || userAssetModel == null) {
				return null;
			}

			map.put("uid", dstUid);
			map.put("headimage", userBaseInfoModel.getHeadimage());
			map.put("familyId", userBaseInfoModel.getFamilyId());
			map.put("livimage", userBaseInfoModel.getLivimage());
			map.put("signature", userBaseInfoModel.getSignature());
			map.put("sex", userBaseInfoModel.getSex());
			map.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
			map.put("phone", userBaseInfoModel.getPhone());
			map.put("anchorNext", this.getNextLevel(userBaseInfoModel.getAnchorLevel(), "anchor"));
			map.put("userLevel", userBaseInfoModel.getUserLevel());
			map.put("userNext", this.getNextLevel(userBaseInfoModel.getUserLevel(), "user"));
			map.put("nickname", userBaseInfoModel.getNickname());
			map.put("money", userAssetModel.getMoney());
			map.put("city",
					org.apache.commons.lang.StringUtils.isEmpty(userBaseInfoModel.getCity()) ? VarConfigUtils.Location
							: userBaseInfoModel.getCity());
			map.put("follows", RelationRedisService.getInstance().getFollowsTotal(dstUid));
			map.put("identity", userBaseInfoModel.getIdentity());
			map.put("fans", RelationRedisService.getInstance().getFansTotal(dstUid));// 粉丝数
			if (dstUid != uid) {
				map.put("isfan", RelationRedisService.getInstance().isFan(uid, dstUid));// 是否关注过
			} else {
				map.put("isfan", true);
			}
			map.put("creditTotal", userAssetModel.getCreditTotal()); // 总声援值
			map.put("wealth", userAssetModel.getWealth() / 100); // 转换为猪头数
			map.put("exp", userAssetModel.getWealth() + userBaseInfoModel.getExp());
			map.put("creditToDay", RelationRedisService.getInstance().getRoomCreditThis(dstUid)); // 当天声援值
			map.put("credit", userAssetModel.getCredit());// 提现声援值
			map.put("likes", RelationRedisService.getInstance().getLikes(dstUid));
			map.put("status", userBaseInfoModel.getLiveStatus());
			map.put("verified_reason", userBaseInfoModel.getVerified_reason()); // 认证原因
			map.put("verified", userBaseInfoModel.isVerified()); // false 未认证
			map.put("constellation", userBaseInfoModel.getConstellation());
			map.put("hobby", userBaseInfoModel.getHobby());

			map.put("rq", UserRedisService.getInstance().getUserAllRankRqScore(String.valueOf(dstUid)));
			map.put("badges", userItemService.getBadgeListByUid(uid, false));
			map.put("monthSupport", UserRedisService.getInstance().getSupportByUid(dstUid, 4));//新声援值
			
			// 已认证
			if (userBaseInfoModel.getLiveStatus()) {
				map.put("domain",
						liveService.getVideoConfig(uid, dstUid, userBaseInfoModel.getVideoline()).get("domain"));
			} else {
				map.put("domain", "");
			}
			if (payAccountModel == null) {
				map.put("weixin", "");
				map.put("wx_openid", "");
				map.put("wx_unionid", "");
				map.put("alipay", "");
				map.put("isuse", 0);
			} else {
				map.put("weixin", "");
				map.put("wx_openid", payAccountModel.getWx_openid());
				map.put("wx_unionid", payAccountModel.getWx_unionid());
				map.put("alipay", payAccountModel.getAlipay());
				map.put("isuse", payAccountModel.getIsUse());
			}
		}
		int vipIcon = 0;
		UserVipInfoModel vipInfoModel = ValueaddServiceUtil.getVipInfo(dstUid);
		if (vipInfoModel != null) {
			int gid = vipInfoModel.getGid();
			ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(gid, 1);
			if (privilegeModel != null) {
				vipIcon = privilegeModel.getIconId();
			}
		}
		Map<String, Object> vipmap = new HashMap<String, Object>();
		vipmap.put("vipIcon", vipIcon);
		map.put("vipInfo", vipmap);
		return map;
	}

	/**
	 * 获取登录后用户的信息
	 */
	@Override
	public Map<String, Object> getAuthUserInfo(int uid) {

		Map<String, Object> map = new HashMap<String, Object>();
		UserBaseInfoModel userBaseInfoModel = this.getUserbaseInfoByUid(uid, false);
		UserAssetModel userAssetModel = this.getUserAssetByUid(uid, false);
		UserAccountModel userAccountModel = this.getUserAccountByUid(uid, false);

		if (userBaseInfoModel != null && userAssetModel != null && userAccountModel != null) {
			map.put("uid", userBaseInfoModel.getUid());
			map.put("nickname", userBaseInfoModel.getNickname());
			map.put("headimage", userBaseInfoModel.getHeadimage());
			map.put("identity", userBaseInfoModel.getIdentity());
			map.put("signature", userBaseInfoModel.getSignature());
			map.put("city", userBaseInfoModel.getCity());
			map.put("sex", userBaseInfoModel.getSex());
			map.put("money", userAssetModel.getMoney());
			map.put("accountname", userAccountModel.getAccountname());
		}

		return map;
	}

	@Override
	public void rpcAdminFollow(Integer uid, Integer dstUid, Boolean bfollow) {
		// TODO 广播关注在房间 被关注推送？？？？
		if (bfollow) {
			FollowCMod msgBody = new FollowCMod();
			msgBody.setbFollow(bfollow);
			msgBody.setDstUid(dstUid);

			// UserBaseInfoModel dstUserMod = this.getUserbaseInfoByUid(uid,
			// false);
			// msgBody.setDstSex(dstUserMod.getSex());
			// msgBody.setDstLevel(dstUserMod.getAnchorLevel());
			// msgBody.setDstAvatar(dstUserMod.getHeadimage());
			// msgBody.setDstNick(dstUserMod.getNickname());
			// msgBody.setDstAnchorLevel(dstUserMod.getAnchorLevel());

			msgBody.setUid(uid);
			UserBaseInfoModel userMod = this.getUserbaseInfoByUid(uid, false);
			msgBody.setLevel(userMod.getUserLevel());
			msgBody.setSex(userMod.getSex());
			msgBody.setNickname(userMod.getNickname());
			// Unirest

			String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
					"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + JSONObject.toJSONString(msgBody),
					"roomOwner=" + dstUid);

			Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room())
					.field("appKey", VarConfigUtils.ServiceKey).field("msgBody", JSONObject.toJSONString(msgBody))
					.field("roomOwner", dstUid.toString()).field("sign", signParams).asJsonAsync();
		}

	}

	/**
	 * 修改用户昵称
	 *
	 * @param uid
	 * @param nickName
	 * @return
	 */
	@Override
	public int updUserNickName(int uid, String nickName) {
		UserBaseInfoModel userBaseInfoModel = this.getUserbaseInfoByUid(uid, false);
		if (userBaseInfoModel == null) {
			return 0;
		} else if (userBaseInfoModel.getNickname().equals(nickName)) {
			return 1;
		}

		try {
			String sql = StringUtils.getSqlString(SQL_UpdUserNickNameByUid, "user_base_info_", uid);
			int ires = DBHelper.execute(VarConfigUtils.dbZhuUser, sql, false, nickName, uid);
			if (ires > 0) {
				this.getUserbaseInfoByUid(uid, true);

				// // 同步后台数据
				// Object[] fields = new Object[2];
				// fields[0] = nickName;
				// fields[1] = uid;
				//
				// Map<String, Object> map = new HashMap<String, Object>();
				// map.put("db", "zhu_admin");
				// map.put("sql", String.format(SQL_UpdUserNickNameByUid,
				// "user_base_info"));
				// map.put("fields", fields);
				// OtherRedisService.getInstance().pushQueue(map);

				TaskService.getInstance().taskProcess(uid, TaskConfigLib.CompleteData, 1);
			}
			return ires;
		} catch (Exception e) {
			logger.error("<updUserNickName->Exception>" + e.toString());
		}
		return 0;
	}

	/**
	 * 修改签名
	 *
	 * @param uid
	 * @param signature
	 * @return
	 */
	@Override
	public int updUserSignature(int uid, String signature) {
		UserBaseInfoModel userBaseInfoModel = this.getUserbaseInfoByUid(uid, false);
		int ires = 0;
		if (userBaseInfoModel == null) {
			ires = 0;
		} else if (userBaseInfoModel.getSignature().equals(signature)) {
			ires = 1;
		} else {
			try {
				String sql = StringUtils.getSqlString(SQL_UpdUserSignatureByUid, "user_base_info_", uid);
				ires = DBHelper.execute(VarConfigUtils.dbZhuUser, sql, false, signature, uid);
				if (ires > 0) {
					this.getUserbaseInfoByUid(uid, true);

					// // 同步后台数据
					// Object[] fields = new Object[2];
					// fields[0] = signature;
					// fields[1] = uid;
					//
					// Map<String, Object> map = new HashMap<String, Object>();
					// map.put("db", "zhu_admin");
					// map.put("sql", String.format(SQL_UpdUserSignatureByUid,
					// "user_base_info"));
					// map.put("fields", fields);
					// OtherRedisService.getInstance().pushQueue(map);

					// TOSY TASK
					TaskService.getInstance().taskProcess(uid, TaskConfigLib.CompleteData, 1);
				}
			} catch (Exception e) {
				logger.error("<updUserSignature->Exception>" + e.toString());
			}
		}
		return ires;
	}

	/**
	 * 修改星座
	 * @param uid
	 * @param constellation
	 * @return
	 */
	@Override
	public int updUserConstellation(int uid, String constellation) {
		UserBaseInfoModel userBaseInfoModel = this.getUserbaseInfoByUid(uid, false);
		int ires = 0;
		if (userBaseInfoModel == null) {
			ires = 0;
		} else if (userBaseInfoModel.getConstellation().equals(constellation)) {
			ires = 1;
		} else {
			try {
				String sql = StringUtils.getSqlString(SQL_UpdUserConstellationByUid, "user_base_info_", uid);
				ires = DBHelper.execute(VarConfigUtils.dbZhuUser, sql, false, constellation, uid);
				if (ires > 0) {
					this.getUserbaseInfoByUid(uid, true);

					// TOSY TASK
					TaskService.getInstance().taskProcess(uid, TaskConfigLib.CompleteData, 1);
				}
			} catch (Exception e) {
				logger.error("<updUserConstellation->Exception>" + e.toString());
			}
		}
		return ires;
	}

	@Override
	public int updUserHobby(int uid, String hobby) {
		UserBaseInfoModel userBaseInfoModel = this.getUserbaseInfoByUid(uid, false);
		int ires = 0;
		if (userBaseInfoModel == null) {
			ires = 0;
		} else if (userBaseInfoModel.getHobby().equals(hobby)) {
			ires = 1;
		} else {
			try {
				String sql = StringUtils.getSqlString(SQL_UpdUserHobbyByUid, "user_base_info_", uid);
				ires = DBHelper.execute(VarConfigUtils.dbZhuUser, sql, false, hobby, uid);
				if (ires > 0) {
					this.getUserbaseInfoByUid(uid, true);

					// TOSY TASK
					TaskService.getInstance().taskProcess(uid, TaskConfigLib.CompleteData, 1);
				}
			} catch (Exception e) {
				logger.error("<updUserHobby->Exception>" + e.toString());
			}
		}
		return ires;
	}

	/**
	 * 更新用户经验值
	 *
	 * @param uid
	 * @param exp
	 * @return
	 */
	@Override
	public int updateUserExp(int uid, int exp) {
		UserBaseInfoModel userBaseInfoModel = this.getUserbaseInfoByUid(uid, false);
		int ires = 0;
		if (userBaseInfoModel == null) {
			ires = 0;
		} else {
			try {
				String sql = StringUtils.getSqlString(SQL_UpdUserExpByUid, "user_base_info_", uid);
				ires = DBHelper.execute(VarConfigUtils.dbZhuUser, sql, false, exp, uid);
				if (ires > 0) {
					this.getUserbaseInfoByUid(uid, true);
				}
			} catch (Exception e) {
				logger.error("<updateUserExp->Exception>" + e.toString());
			}
		}
		return ires;
	}

	/**
	 * 获取用户经验值
	 *
	 * @param uid
	 * @param exp
	 * @return -1 出现异常
	 */
	@Override
	public Long getUserExp(int uid) {
		UserBaseInfoModel userBaseInfoModel = this.getUserbaseInfoByUid(uid, false);
		if (userBaseInfoModel != null) {
			return userBaseInfoModel.getExp();
		}
		return -1l;
	}

	/**
	 * 获取手机号码
	 * @param uid
	 * @return
	 */
	@Override
	public String getUserPhone(int uid) {

		String phone = "";
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		String sql = "SELECT phone FROM %s WHERE uid=?";
		try {
			// 获取用户基本信息
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
			statement = conn.prepareStatement(StringUtils.getSqlString(sql, "user_base_info_", uid));
			DBHelper.setPreparedStatementParam(statement, uid);
			rs = statement.executeQuery();

			if (rs != null) {
				while (rs.next()) {
					phone = rs.getString("phone");
					break;
				}
			}
		} catch (SQLException e) {
			logger.error("<getUserPhone->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getUserPhone->Exception>" + e.getMessage());
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
				logger.error("<getUserPhone->finally->Exception>" + e2.getMessage());
			}
		}
		return phone;
	}

	/**
	 * 获取用户等级
	 *
	 * @param uid
	 * @param exp
	 * @return -1 出现异常
	 */
	@Override
	public int getUserLevel(int uid) {
		UserBaseInfoModel userBaseInfoModel = this.getUserbaseInfoByUid(uid, false);
		if (userBaseInfoModel != null) {
			return userBaseInfoModel.getUserLevel();
		}
		return -1;
	}

	/**
	 * 新增用户经验值
	 *
	 * @param uid
	 * @param exp
	 * @return
	 */
	@Override
	public Long addUserExpByTask(int uid, int exp) {
		if (exp <= 0) {
			return -1l;
		}
		try {
			String sql = StringUtils.getSqlString(SQL_AddUserExpByUid, "user_base_info_", uid);
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, sql, false, exp, uid);
			if (executeResult > 0) {
				// 更新缓存
				UserBaseInfoModel userbaseInfoByUid = this.getUserbaseInfoByUid(uid, true);
				// 计算等级是否发生变化 当前正确的用户等级计算公式=wealth(人民币经验)+exp(活跃收益)
				if (userbaseInfoByUid != null) {
					// 调用方总写的升级方法
					boolean updLevel = RoomServiceImpl.getInstance().updUserLevel(uid, 0, 0, 1, 2);
					if (!updLevel) {
						logger.error(String.format("addUserExpByTask updUserLevel filaed uid:%d,add exp:%d", uid, exp));
					}
					return userbaseInfoByUid.getExp();
				}
			}

		} catch (Exception e) {
			logger.error("<addUserExpByTask->Exception>" + e.toString());
		}
		return -1l;
	}

	/**
	 * @param uid
	 * @param zhutou
	 * @param reason
	 * @param taskID
	 * @param taskName
	 */
	@Override
	public int addUserWealth(int uid, int zhutou, int reason, int taskId, String taskName) {
		return OrderServiceImpl.getInstance().insertPayActivity(uid, reason, taskId, taskName, zhutou);
	}

	@Override
	public int updUserPwordByUid(int uid, String pword) {
		int ires = 0;
		try {
			ires = DBHelper.execute(VarConfigUtils.dbZhuUser,
					StringUtils.getSqlString(SQL_updUserPwordByUid, "user_account_", uid), false, pword, uid);
			if (ires > 0) {
				this.getUserAccountByUid(uid, true);
			}
		} catch (Exception e) {
			logger.error("<updUserPwordByUid->Exception>" + e.toString());
		}
		return ires;
	}

	/**
	 * 获取下个等级的值
	 *
	 * @param level
	 * @param type
	 *            =user =anchor
	 * @return
	 */
	@Override
	public Long getNextLevel(int level, String type) {
		if (level <= 0) {
			level = 1;
		}

		LevelsConfig levelsConfig = null;
		if ("user".equals(type)) {
			levelsConfig = LevelsConfigLib.getForNormal(level + 1);
		} else if ("anchor".equals(type)) {
			levelsConfig = LevelsConfigLib.getForAdvanced(level + 1);
		}

		if (levelsConfig != null) {
			return levelsConfig.getNumber();
		} else {
			return 0l;
		}

	}

	@Override
	public void addFollows(String type, Integer srcUid, Integer dstUid) {
		logger.debug("用户: "+srcUid+" 关注: "+type+"主播: "+dstUid);
		// 获得该用户给该主播送礼的总价值
		Double lg = RelationRedisService.getInstance().getSrcSendDst(srcUid, dstUid);

		// 关注列表 排序： 送礼价值+观看测试
		RelationRedisService.getInstance().addFollows(srcUid, dstUid, lg * 1000 + 1, type);
		if (type.equals("off")) {
			// 获取上一次取关的时间如果为空则可以进行批量删除关注列表的动态任务
			/*String unFollowTime = RedisCommService.getInstance().get(RedisContant.RedisNameRelation,
					RedisContant.keyUnFollowTimes + srcUid + ":" + dstUid);
			if (org.apache.commons.lang.StringUtils.isEmpty(unFollowTime)) {*/
				try {
					AsyncManager.getInstance().execute(new UnFollowTask(srcUid, dstUid));
				} catch (Exception ex) {
					logger.error("UnFollowTask>>>", ex);
				}
			/*}*/
		}
		if(type.equals("on")) {
				try {
					AsyncManager.getInstance().execute(new FollowTask(srcUid, dstUid));
				} catch (Exception ex) {
					logger.error("UnFollowTask>>>", ex);
			}
		}
		
		
		// 提交关注新手任务
		TaskService.getInstance().taskProcess(srcUid, TaskConfigLib.FollowAnchor, 1);
		//是否进入过直播间
		boolean havUserFollow = OtherRedisService.getInstance().havUserFollowDay(srcUid, dstUid);
		if(!havUserFollow) {
			OtherRedisService.getInstance().userFollowDay(srcUid, dstUid);
			TaskService.getInstance().taskProcess(srcUid, TaskConfigLib.Follow3Anchor, 1);
		}
	}

	/**
	 * 取关相关任务
	 * @author zyl
	 * @date 2016-12-22 下午4:27:29
	 */
	private class UnFollowTask implements IAsyncTask {
		private Integer srcUid;
		private Integer dstUid;

		public UnFollowTask(Integer srcUid, Integer dstUid) {
			this.srcUid = srcUid;
			this.dstUid = dstUid;
		}

		@Override
		public void runAsync() {
			logger.debug(srcUid+"取关:"+dstUid);
			Set<Tuple> set = RedisCommService.getInstance().zrevrangeWithScores(RedisContant.RedisNameFeed,
					RedisContant.FeedUser + dstUid, 0, -1);
			List<String> feedIds = new ArrayList<String>();
			for (Tuple tuple : set) {
				feedIds.add(tuple.getElement());
			}
			RedisCommService.getInstance().zrem(RedisContant.RedisNameFeed, RedisContant.FeedUserFollow + srcUid,
					feedIds.toArray(new String[0]));
			// 更新当前取关的时间 防止刷关注导致多余的任务产生
		/*	RedisCommService.getInstance().set(RedisContant.RedisNameRelation,
					RedisContant.keyUnFollowTimes + srcUid + ":" + dstUid, "1", 1 * 60 * 60);*/
		}

		@Override
		public void afterOk() {

		}

		@Override
		public void afterError(Exception e) {

		}

		@Override
		public String getName() {
			return "UnFollowTask";
		}
	}
	
	private class FollowTask implements IAsyncTask {
		private Integer srcUid;
		private Integer dstUid;

		public FollowTask(Integer srcUid, Integer dstUid) {
			this.srcUid = srcUid;
			this.dstUid = dstUid;
		}

		@Override
		public void runAsync() {
			logger.debug(srcUid+"关注:"+dstUid);
			Set<Tuple> set = RedisCommService.getInstance().zrevrangeWithScores(RedisContant.RedisNameFeed,
					RedisContant.FeedUser + dstUid, 0, -1);
			for (Tuple tuple : set) {
				RedisCommService.getInstance().zadd(RedisContant.RedisNameFeed, RedisContant.FeedUserFollow+srcUid, (int) tuple.getScore() ,tuple.getElement());
			}
		}

		@Override
		public void afterOk() {

		}

		@Override
		public void afterError(Exception e) {

		}

		@Override
		public String getName() {
			return "FollowTask";
		}
	}

	/**
	 * 用户中心关注列表
	 *
	 * @param uid
	 */
	@Override
	public void getFollowsCenter(Integer curUid, Integer dstUid, ReturnModel returnModel) {
		UserBaseInfoModel userBaseInfoModel = null;
		UserAccountModel userAccountModel = null;
		Map<String, Object> map = null;
		List<Map<String, Object>> followList = new ArrayList<Map<String, Object>>();

		Set<String> set = RelationRedisService.getInstance().getFollows(dstUid);

		if (set.size() > 0) {

			for (String strUid : set) {

				map = new HashMap<String, Object>();
				int suid = Integer.valueOf(strUid);

				if (suid >= 900000000) {

					RobotBaseInfoModel robotBaseInfoModel = this.getRobotbaseInfoByUid(suid, false);
					if (robotBaseInfoModel == null) {
						continue;
					}
					map.put("uid", strUid);
					map.put("headimage", robotBaseInfoModel.getHeadimage());
					map.put("anchorLevel", robotBaseInfoModel.getAnchorLevel());
					map.put("userLevel", robotBaseInfoModel.getUserLevel());
					map.put("nickname", robotBaseInfoModel.getNickname());
					map.put("isFollow",
							RelationRedisService.getInstance().isFollows(curUid, suid) == null ? false : true);
					map.put("sex", userBaseInfoModel.getSex());
					map.put("isPush", true);

					if (curUid.equals(dstUid)) { // 只有在个人中心查询关注列表时才提供以下属性
						map.put("status", userBaseInfoModel.getLiveStatus());
						map.put("domain", "");
					}
					followList.add(map);
				} else {

					userBaseInfoModel = this.getUserbaseInfoByUid(suid, false);
					userAccountModel = this.getUserAccountByUid(suid, false);
					if (userBaseInfoModel == null || userAccountModel.getStatus() == 0) {
						continue;
					} else {
						map.put("uid", strUid);
						map.put("headimage", userBaseInfoModel.getHeadimage());
						map.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
						map.put("userLevel", userBaseInfoModel.getUserLevel());
						map.put("nickname", userBaseInfoModel.getNickname());
						map.put("isFollow",
								RelationRedisService.getInstance().isFollows(curUid, suid) == null ? false : true);
						map.put("sex", userBaseInfoModel.getSex());
						if (RelationRedisService.getInstance().getPushFollow(dstUid, suid)) {
							map.put("isPush", true);
						} else {
							map.put("isPush", false);
						}
						if (curUid.equals(dstUid)) { // 只有在个人中心查询关注列表时才提供以下属性
							map.put("status", userBaseInfoModel.getLiveStatus());
							if (userBaseInfoModel.getLiveStatus()) {
								map.put("domain", liveService
										.getVideoConfig(curUid, suid, userBaseInfoModel.getVideoline()).get("domain"));
							} else {
								map.put("domain", "");
							}
						}
						followList.add(map);
					}
				}
			}
			map = new HashMap<String, Object>();
			map.put("list", followList);
			returnModel.setData(map);
		}
	}

	/**
	 * 用户中心关注列表
	 *
	 * @param uid
	 */
	@Override
	public void getPCFollowsCenter(Integer curUid, ReturnModel returnModel) {

		List<Map<String, Object>> followList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> liveFollowList = new ArrayList<Map<String, Object>>();

		UserBaseInfoModel userBaseInfoModel = null;
		Map<String, Object> map = null;
		int total = 5;

		if (curUid > 0) {

			UserAccountModel userAccountModel = null;

			Set<String> set = RelationRedisService.getInstance().getFollows(curUid);

			if (set != null && set.size() > 0) {

				for (String strUid : set) {

					map = new HashMap<String, Object>();
					int suid = Integer.valueOf(strUid);

					if (suid >= 900000000) {

						RobotBaseInfoModel robotBaseInfoModel = this.getRobotbaseInfoByUid(suid, false);
						if (robotBaseInfoModel == null) {
							continue;
						}
						map.put("uid", strUid);
						map.put("headimage", robotBaseInfoModel.getHeadimage());
						map.put("anchorLevel", robotBaseInfoModel.getAnchorLevel());
						map.put("nickname", robotBaseInfoModel.getNickname());
						map.put("recommend", false);
						if (userBaseInfoModel.getLiveStatus()) {
							map.put("opentime", System.currentTimeMillis() / 1000 - userBaseInfoModel.getOpentime());
						} else {
							map.put("opentime", 0);
						}
						map.put("status", false);
						followList.add(map);
					} else {

						userBaseInfoModel = this.getUserbaseInfoByUid(suid, false);
						userAccountModel = this.getUserAccountByUid(suid, false);
						if (userBaseInfoModel == null || userAccountModel.getStatus() == 0) {
							continue;
						} else {
							map.put("uid", strUid);
							map.put("headimage", userBaseInfoModel.getHeadimage());
							map.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
							map.put("nickname", userBaseInfoModel.getNickname());
							map.put("recommend", false);
							if (userBaseInfoModel.getLiveStatus()) {
								map.put("opentime",
										System.currentTimeMillis() / 1000 - userBaseInfoModel.getOpentime());
							} else {
								map.put("opentime", 0);
							}
							map.put("status", userBaseInfoModel.getLiveStatus());
							if (userBaseInfoModel.getLiveStatus()) {
								liveFollowList.add(map);
								total--;
							} else {
								followList.add(map);
							}

						}
					}
					if (total == 0) {
						break;
					}
				}
			}
			liveFollowList.addAll(followList);
			if (total > liveFollowList.size()) {
				liveFollowList.addAll(this.recommedFollows(total - liveFollowList.size()));
			}
		} else {
			liveFollowList = this.recommedFollows(total - liveFollowList.size());
		}
		if (liveFollowList.size() > total) {
			liveFollowList = liveFollowList.subList(0, 5);
		}
		returnModel.setData(liveFollowList);
	}

	/**
	 * 获取推荐关注的主播列表
	 * @param total 推荐主播数量
	 * @return
	 */
	private List<Map<String, Object>> recommedFollows(int total) {

		int num = total;
		List<Map<String, Object>> followList = new ArrayList<Map<String, Object>>();
		UserBaseInfoModel userBaseInfoModel = null;

		// 未登陆
		Set<String> setLivingList = OtherRedisService.getInstance().getRecommendRoom(0);
		if (setLivingList == null) {
			// 没有开播数据
			setLivingList = new HashSet<String>();
			// 获取日榜数据
			Set<Tuple> userRank = UserRedisService.getInstance().getUserDayRank("anchor", 5);
			if (userRank != null && userRank.size() > 0) {
				for (Tuple tuple : userRank) {
					if (setLivingList.contains(tuple.getElement())) {
						continue;
					}
					setLivingList.add(tuple.getElement());
					total--;
					if (total == 0) {
						break;
					}
				}
			}
			if (total > 0) {
				userRank = null;
				// 获取周榜数据
				userRank = UserRedisService.getInstance().getUserWeekRank("anchor", 9);
				for (Tuple tuple : userRank) {
					if (setLivingList.contains(tuple.getElement())) {
						continue;
					}
					setLivingList.add(tuple.getElement());
					total--;
					if (total == 0) {
						break;
					}
				}
			}
		} else {
			// 推荐数据 少于5条
			if (setLivingList.size() < total) {

				int nextCount = total - setLivingList.size();
				Set<Tuple> userRank = UserRedisService.getInstance().getUserDayRank("anchor", 5);
				if (userRank != null && userRank.size() > 0) {
					for (Tuple tuple : userRank) {
						if (setLivingList.contains(tuple.getElement())) {
							continue;
						}
						setLivingList.add(tuple.getElement());
						nextCount--;
						if (nextCount == 0) {
							break;
						}
					}
				}
				if (nextCount > 0) {
					userRank = null;
					userRank = UserRedisService.getInstance().getUserWeekRank("anchor", 9);
					for (Tuple tuple : userRank) {

						if (setLivingList.contains(tuple.getElement())) {
							continue;
						}
						setLivingList.add(tuple.getElement());
						nextCount--;
						if (nextCount == 0) {
							break;
						}
					}
				}
			} else if (setLivingList.size() > total) {

				// int isize = setLivingList.size()-total;
				// for(int i = isize; i >= 0; i--){
				// setLivingList.remove(i);
				// }
			}
		}
		if (setLivingList != null) {
			for (String suid : setLivingList) {
				int uid = Integer.valueOf(suid);

				Map<String, Object> map = new HashMap<String, Object>();
				userBaseInfoModel = this.getUserbaseInfoByUid(uid, false);

				if (userBaseInfoModel == null) {
					continue;
				} else {
					num--;
					map.put("uid", uid);
					map.put("headimage", userBaseInfoModel.getHeadimage());
					map.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
					map.put("nickname", userBaseInfoModel.getNickname());
					map.put("recommend", true);
					if (userBaseInfoModel.getLiveStatus()) {
						map.put("opentime", System.currentTimeMillis() / 1000 - userBaseInfoModel.getOpentime());
					} else {
						map.put("opentime", 0);
					}

					map.put("status", userBaseInfoModel.getLiveStatus());
					followList.add(map);
					if (num <= 0) {
						break;
					}
				}
			}
		}
		return followList;
	}

	/**
	 * 设置关注主播 是否有推送信息的权利
	 *
	 * @param srcUid
	 * @param dstUid
	 * @param type
	 */
	@Override
	public void setPushSwitch(Integer srcUid, Integer dstUid, int type) {
		RelationRedisService.getInstance().setPushSwitch(srcUid, dstUid, type);
	}

	public static void main(String... args) {
		try {
			System.out.println(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// new UserServiceImpl().sendMobileCode("17310288949","8888");
	}

	@Override
	public Boolean sendMobileCode(String mobile, String code) {
		logger.info(String.format("reg sms to:%s code:%s",mobile,code));
		try {
			Map<String, String> result = SmsIhwxUtil.sendSms(mobile, code);
			if (org.apache.commons.lang.StringUtils.equals("2",result.get("code"))) {
				OtherRedisService.getInstance().setSendMsgs(mobile, code);
				return true;
			}else {
				logger.info("<sendMobileCode faild:"+result.get("msg"));
			}
		} catch (Exception e) {
			logger.error("<sendMobileCode Exception:"+e.getMessage(),e);
		}
		
		return false;
	}

	@Deprecated
	public Boolean sendMobileCode1(String mobile, String code) {
		Boolean bl = false;
		String uri = "http://send.18sms.com/msg/HttpBatchSendSM";// 应用地址
		String account = "tc4u28";// 账号
		String pswd = "99Ju58K4";// 密码
		String mobiles = mobile;// 手机号码，多个号码使用","分割
		String content = "您的验证码是:" + code + ",请收到后注意保密并且尽快验证";// 短信内容
		boolean needstatus = true;// 是否需要状态报告，需要true，不需要false
		String product = "";// 产品ID(不用填写)
		String extno = "";// 扩展码(请登陆网站用户中心——>服务管理找到签名对应的extno并填写，线下用户请为空)
		try {
			System.out.println(mobile + ">>>[" + content + "]");
			String returnString = HttpSender.batchSend(uri, account, pswd, mobiles, content, needstatus, product,
					extno);
			System.out.println(mobile + ">>>[" + returnString + "]");
			String[] splits = returnString.split("\n");
			if (splits.length >= 2) {
				String[] response = splits[0].split(",");
				if (response.length >= 2) {
					String resultCode = response[1];
					if (resultCode.equals("0")) {
						bl = true;
						OtherRedisService.getInstance().setSendMsgs(mobile, code);
					} else {
						logger.error("<sendMobileCode->Exception>" + returnString);
						System.out.println("<sendMobileCode->Exception 0>" + resultCode);
					}
				} else {
					System.out.println("<sendMobileCode->Exception 1>" + splits[0]);
				}
			} else {
				System.out.println("<sendMobileCode->Exception 2>" + splits.length);
			}
			// TODO 处理返回值,参见HTTP协议文档
		} catch (Exception e) {
			logger.error("<sendMobileCode->Exception>", e);
			e.printStackTrace(System.out);
			System.out.println("<sendMobileCode->Exception 3>" + e.toString());
		}
		return bl;
	}

	// @Override
	// public Boolean sendMobileCode(String mobile, String code) {
	//
	// String url = String.format(UrlConfigLib.getUrl("url").getMobilemsgchannel(), mobile, code);
	// Boolean bl = false;
	// try {
	// HttpResponse<String> response = Unirest.get(url).asString();
	// org.json.JSONObject jsonObject = XML.toJSONObject(response.getBody());
	// logger.info("<sendMobileCode->>" + jsonObject.toString());
	// if (jsonObject == null || response.getStatus() != 200) {
	// bl = false;
	// } else {
	// String resCode = jsonObject.getJSONObject("response").get("code").toString();
	// logger.info("<sendMobileCode-rescode>" + resCode);
	// if ("3".equals(resCode)) {
	// bl = true;
	// OtherRedisService.getInstance().setSendMsgs(mobile, code);
	// }
	// }
	// } catch (Exception e) {
	// logger.error("<sendMobileCode->Exception>" , e);
	// }
	// return bl;
	// }

	/**
	 * 获取声援榜
	 *
	 * @param dstUid
	 * @param uid
	 * @param page
	 * @param mode /day/week/all
	 */
	@Override
	public void getSupportByUid(Integer dstUid, Integer uid, Integer page, ReturnModel returnModel, String mode) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		UserBaseInfoModel userInfoModel;

		// tosy add mode
		Set<Tuple> set = RelationRedisService.getInstance().getSrcListInDst(dstUid, page, mode);
		int total = RelationRedisService.getInstance().getDstGetNumber(dstUid, mode).intValue();

		for (Tuple tuple : set) {
			map = new HashMap<String, Object>();
			userInfoModel = this.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()), false);
			if (userInfoModel != null) {
				map.put("uid", userInfoModel.getUid());
				map.put("isFollow", RelationRedisService.getInstance().isFan(uid, userInfoModel.getUid())); // =true已关注
				// ＝
				// false
				// 未关注
				map.put("headimage", userInfoModel.getHeadimage());
				map.put("nickname", userInfoModel.getNickname());
				map.put("sex", userInfoModel.getSex());
				map.put("money", tuple.getScore());
				map.put("userLevel", userInfoModel.getUserLevel());
				map.put("anchorLevel", userInfoModel.getAnchorLevel());
			} else {
				continue;
			}
			list.add(map);
		}
		map = new HashMap<String, Object>();
		map.put("total", total);
		map.put("list", list);

		returnModel.setData(map);
	}

	/**
	 * 获取声援榜
	 *
	 * @param dstUid
	 * @param uid
	 * @param page
	 */
	@Override
	public List<Map<String, Object>> getSupportByUid(Integer dstUid, Integer start, Integer rows) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		UserBaseInfoModel userInfoModel;

		Set<Tuple> set = RelationRedisService.getInstance().getSrcListInDstNew(dstUid, start, rows);

		for (Tuple tuple : set) {
			map = new HashMap<String, Object>();
			userInfoModel = this.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()), false);
			if (userInfoModel != null) {
				map.put("uid", userInfoModel.getUid());
				map.put("headimage", userInfoModel.getHeadimage());
				map.put("nickname", userInfoModel.getNickname());
				map.put("sex", userInfoModel.getSex());
				map.put("money", tuple.getScore());
				map.put("userLevel", userInfoModel.getUserLevel());
				map.put("anchorLevel", userInfoModel.getAnchorLevel());
			} else {
				continue;
			}
			list.add(map);
		}
		return list;
	}

	/**
	 * 绑定手机号码
	 *
	 * @param uid
	 * @param mobile
	 * @return
	 */
	@Override
	public int bindMobileByUid(Integer uid, String mobile) {
		int ires = 0;
		UserBaseInfoModel userBaseInfoModel = null;
		try {
			userBaseInfoModel = this.getUserbaseInfoByUid(uid, false);
			if (mobile.equals(userBaseInfoModel.getPhone())) {
				ires = 1;
			} else {
				String sql = StringUtils.getSqlString(SQL_UpdUserMobileByUid, "user_base_info_", uid);
				ires = DBHelper.execute(VarConfigUtils.dbZhuUser, sql, false, mobile, uid);
				if (ires > 0) {
					this.getUserbaseInfoByUid(uid, true);
					// TOSY TASK
					TaskService.getInstance().taskProcess(uid, TaskConfigLib.BoundPhone, 1);
				}
			}
		} catch (Exception e) {
			logger.error("<bindMobileByUid->Exception>" + e.toString());
		}
		return ires;
	}

	@Override
	public int updUserSex(Integer uid, Boolean sex) {
		int ires = 0;
		try {
			UserBaseInfoModel userBaseInfoModel = this.getUserbaseInfoByUid(uid, false);
			if (userBaseInfoModel != null) {
				if (userBaseInfoModel.getSex() == sex) {
					ires = 1;
				} else {
					String sql = StringUtils.getSqlString(SQL_UpdUserSexByUid, "user_base_info_", uid);
					ires = DBHelper.execute(VarConfigUtils.dbZhuUser, sql, false, sex, uid);
					if (ires > 0) {
						this.getUserbaseInfoByUid(uid, true);

						TaskService.getInstance().taskProcess(uid, TaskConfigLib.CompleteData, 1);

						// // 同步后台数据
						// Object[] fields = new Object[2];
						// fields[0] = sex;
						// fields[1] = uid;
						//
						// Map<String, Object> map = new HashMap<String,
						// Object>();
						// map.put("db", "zhu_admin");
						// map.put("sql", String.format(SQL_UpdUserSexByUid,
						// "user_base_info"));
						// map.put("fields", fields);
						// OtherRedisService.getInstance().pushQueue(map);
					}
				}
			}
		} catch (Exception e) {
			logger.error("<updUserSex->Exception>" + e.toString());
		}
		return ires;
	}

	@Override
	public int updAssetMoneyByUid(int uid, Double money) {
		int ires = 0;
		try {
			String sql = StringUtils.getSqlString(SQL_UpdAssetMondyByUid, "user_asset_", uid);
			ires = DBHelper.execute(VarConfigUtils.dbZhuUser, sql, false, money, uid);// TODO
			// 现在7倍
			if (ires == 1) {
				this.getUserAssetByUid(uid, true);
			}
		} catch (Exception e) {
			logger.error("<updAssetMoneyByUid->Exception>" + e.toString());
		}
		return ires;
	}

	/**
	 * 获取主播信息（进房间的时候获取）
	 *
	 * @param srcUid
	 * @param dstUid
	 * @return
	 */
	@Override
	public Map<String, Object> getLivIngInfo(Integer srcUid, Integer dstUid) {
		UserBaseInfoModel userBaseInfoModel = this.getUserbaseInfoByUid(dstUid, false);
		UserAssetModel userAssetModel = this.getUserAssetByUid(dstUid, false);
		// LiveMicTimeModel liveMicTimeModel = liveService.getLiveMicInfoByUid(dstUid, false);
		Map<String, Object> map = new HashMap<String, Object>();

		if (userBaseInfoModel == null || userAssetModel == null) {
			return null;
		} else {
			map.put("uid", userBaseInfoModel.getUid());
			map.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
			map.put("headimage", userBaseInfoModel.getHeadimage());
			map.put("sex", userBaseInfoModel.getSex());
			map.put("nickname", userBaseInfoModel.getNickname());
			if (userBaseInfoModel.getLiveStatus()) {
				map.put("timeslong", System.currentTimeMillis() / 1000 - userBaseInfoModel.getOpentime());
			} else {
				map.put("timeslong", 0);

			}

			Double dlDouble = RelationRedisService.getInstance().isFollows(srcUid, dstUid);
			map.put("isFollow", dlDouble == null ? false : true);
			map.put("creditTotal", userAssetModel.getCreditTotal());
			map.put("fans", RelationRedisService.getInstance().getFansTotal(dstUid));// 粉丝数
			map.put("userNext", this.getNextLevel(userBaseInfoModel.getAnchorLevel(), "anchor"));

		}
		return map;
	}

	@Override
	public int updUserBaseInfoLiveStatusByUid(Integer uid, Boolean status) {
		int ires = 0;
		try {
			if (status) {
				String sql = StringUtils.getSqlString(SQL_UpdUserBaseLivingByUid, "user_base_info_", uid);
				// 上播
				ires = DBHelper.execute(VarConfigUtils.dbZhuUser, sql, false, status,
						System.currentTimeMillis() / 1000,uid);
			} else {
				String sql = StringUtils.getSqlString(SQL_UpdUserBaseOffByUid, "user_base_info_", uid);
				// 下播
				ires = DBHelper.execute(VarConfigUtils.dbZhuUser, sql, false, status, uid);
			}

			if (ires == 1) {
				this.getUserbaseInfoByUid(uid, true);
			}
		} catch (Exception e) {
			logger.error("<updUserBaseInfoLiveStatusByUid->Exception>" + e.toString());
		}
		return ires;
	}

	@Override
	public int updUserBaseHeadimg(Integer uid, String headimage, String livimg) {
		int ires = 0;
		try {
			UserBaseInfoModel userbaseInfo = getUserbaseInfoByUid(uid, false);
			if (userbaseInfo == null) {
				// 用户不存在
			} else {
				if (org.apache.commons.lang.StringUtils.isNotEmpty(userbaseInfo.getLivimage())) {
					// 1:1 图片已经存在
					livimg = userbaseInfo.getLivimage();
				}

				String sql = StringUtils.getSqlString(SQL_UpdUserHeadimageByUid, "user_base_info_", uid);
				ires = DBHelper.execute(VarConfigUtils.dbZhuUser, sql, false, headimage, livimg, uid);
				if (ires == 1) {
					this.getUserbaseInfoByUid(uid, true);

					// TOSY TASK
					TaskService.getInstance().taskProcess(uid, TaskConfigLib.CompleteData, 1);
				}
			}
		} catch (Exception e) {
			logger.error("<updUserBaseHeadimg->Exception>" + e.toString());
		}
		return ires;
	}

	@Override
	public void getWithDraw(int uid, ReturnModel returnModel) {
		UserAssetModel userAssetModel = this.getUserAssetByUid(uid, false);
		UserBaseInfoModel userBaseInfoModel = this.getUserbaseInfoByUid(uid, false);

		if (userAssetModel == null || userBaseInfoModel == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
		} else {
			int rate = LevelsConfigLib.getForAdvanced(userBaseInfoModel.getAnchorLevel()).getRate();
			Map<String, Object> map = new HashMap<String, Object>();
			if (rate == 0) {
				map.put("credit", userAssetModel.getCredit());// 声援值
				map.put("cash", 0);// 可提现的金额(元为单位)
				map.put("limit", OtherRedisService.getInstance().getWithDrawLimit());
			} else {
				map.put("credit", userAssetModel.getCredit());// 声援值
				int credit = (int) (userAssetModel.getCredit() / 100); // 猪头：RMB
																		// ＝
																		// 1:100
				credit = credit * rate / 100;
				map.put("cash", credit);// 可提现的金额(元为单位)
				map.put("limit", OtherRedisService.getInstance().getWithDrawLimit());
			}
			returnModel.setData(map);
		}
	}

	@Override
	public void tixian(String type, int amount, Integer uid, ReturnModel returnModel) {

		// 提现 间隔不能小于2分钟
		String strInterval = RedisCommService.getInstance().get(RedisContant.RedisNameOther,
				RedisContant.withdrawInterval + uid);
		if (strInterval != null) {
			returnModel.setCode(CodeContant.PayTixianInterval);
			returnModel.setMessage("提现太频繁，请在3分钟后再尝试");
			return;
		}
		String strday = RedisCommService.getInstance().get(RedisContant.RedisNameOther, RedisContant.withdrawDay + uid);
		if (strday != null) {
			if (Integer.valueOf(strday) > 3) {

				returnModel.setCode(CodeContant.PayTixianTimes);
				returnModel.setMessage("一天之内提现不能超过3次");
				return;
			}
		}

		if ("weixin".equalsIgnoreCase(type)) {
			this.weixinTiXian(amount, uid, returnModel);
		} else if ("alipay".equalsIgnoreCase(type)) {

		}

	}

	private void weixinTiXian(int amount, Integer uid, ReturnModel returnModel) {
		Double credit = 0d;

		if (amount <= 0 || amount > OtherRedisService.getInstance().getWithDrawLimit()) {
			returnModel.setCode(CodeContant.PayTixianMonyErr);
			returnModel.setMessage("提现金额不正确");
			return;
		}

		UserBaseInfoModel userBaseInfoModel = this.getUserbaseInfoByUid(uid, false);
		if (userBaseInfoModel == null || userBaseInfoModel.getFamilyId() > 0) {
			returnModel.setCode(CodeContant.PayWithDrawRights);
			returnModel.setMessage("您属于公会主播，没有提现权限，请联系您的公会");
			return;
		} else if (org.apache.commons.lang.StringUtils.isEmpty(userBaseInfoModel.getPhone())) {
			returnModel.setCode(CodeContant.ConMobileExcept);
			returnModel.setMessage("请先绑定手机号后，再提现");
			return;
		}

		// 实名认证
		AuthenticationModel authenticationModel = this.getAuthentication(uid);
		if (authenticationModel != null) {
			if (authenticationModel.getAuditStatus() != 3) {
				returnModel.setCode(CodeContant.authenOkErr);
				returnModel.setMessage("请实名认证，认证后方可提现");
				return;
			}
		} else {
			returnModel.setCode(CodeContant.authenOkErr);
			returnModel.setMessage("请实名认证，认证后方可提现");
			return;
		}

		LevelsConfig forAdvanced = LevelsConfigLib.getForAdvanced(userBaseInfoModel.getAnchorLevel());
		// *100*100
		credit = Math.ceil(amount * 10000 / (forAdvanced.getRate()));

		PayAccountModel payAccountModel = payService.getPayAccountByUid(uid, false);
		if (payAccountModel == null || payAccountModel.getWx_openid() == null
				|| payAccountModel.getWx_openid().equals("''")) {
			returnModel.setCode(CodeContant.updPayAccount);
			returnModel.setMessage("用户没有绑定微信号");
			return;
		}

		UserAssetModel userAssetModel = this.getUserAssetByUid(uid, false);
		if (userAssetModel == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
		} else if (userAssetModel.getCredit() < credit) {
			returnModel.setCode(CodeContant.MONEYLESS);
			returnModel.setMessage("金额不足");
		} else {
			try {
				// 扣费
				int ires = DBHelper.execute(VarConfigUtils.dbZhuUser,
						StringUtils.getSqlString(SQL_UpdAssetCreditByUid, "user_asset_", uid), false, credit, uid,
						credit);

				if (ires == 1) {
					this.getUserAssetByUid(uid, true);

					String billno = weixinService.buildMch_Billno();
					ires = DBHelper.execute(VarConfigUtils.dbZhuPay, SQL_InsertWithDraw, false, billno, uid, "weixin",
							amount, credit.intValue(), "", 0, System.currentTimeMillis() / 1000, 0,
							payAccountModel.getWx_unionid());
					if (ires == 0) {
						logger.info("weixinTiXian SQL_InsertWithDraw is error");
					} else {
						returnModel.setMessage("提交成功，24小时内审核");
					}
				} else {
					returnModel.setCode(CodeContant.PayTixianErr);
					returnModel.setMessage("提现失败，请联系客服1");
				}
			} catch (UnirestException e) {
				logger.error("<weixinTiXian->UnirestException>", e);
				returnModel.setCode(CodeContant.PayTixianErr);
				returnModel.setMessage("提现失败，请联系客服3");
			} catch (Exception e) {
				logger.error("<weixinTiXian->ex>", e);
				returnModel.setCode(CodeContant.PayTixianErr);
				returnModel.setMessage("提现失败，请联系客服4");
			}
		}
	}

	@Override
	public Map<String, Object> getTixianInfoById(int id) {

		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		Map<String, Object> map = null;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuPay).getConnection();
			statement = conn.prepareStatement(SQL_GetWithDrawINfoById);
			DBHelper.setPreparedStatementParam(statement, id);
			rs = statement.executeQuery();

			if (rs != null) {
				while (rs.next()) {
					map = new HashMap<String, Object>();
					map.put("id", rs.getInt("id"));
					map.put("billno", rs.getString("billno"));
					map.put("uid", rs.getInt("uid"));
					map.put("sendListid", rs.getString("sendListid"));
					map.put("sendTime", rs.getLong("sendTime"));
					map.put("openid", rs.getString("openid"));
					map.put("amount", rs.getDouble("amount"));
					map.put("credit", rs.getInt("credit"));
					map.put("createAt", rs.getInt("createAt"));
					map.put("isSecc", rs.getInt("isSecc"));
					map.put("type", rs.getString("type"));
					break;
				}
			}
		} catch (Exception e) {
			logger.error("<getTixian->Exception>" + e.toString());
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
				logger.error("<getTixian->finally->Exception>" + e2.toString());
			}
		}
		return map;
	}

	@Override
	public void getTixianList(int uid, ReturnModel returnModel) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		double total = 0.0;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuPay).getConnection();
			statement = conn.prepareStatement(SQL_GetWithDrawList);
			DBHelper.setPreparedStatementParam(statement, uid);
			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					map = new HashMap<String, Object>();
					map.put("amount", rs.getDouble("amount"));
					map.put("credit", rs.getInt("credit"));
					map.put("createAt", rs.getInt("createAt"));
					map.put("isSecc", rs.getInt("isSecc"));
					list.add(map);
					if (rs.getInt("isSecc") == 1) {
						total += rs.getDouble("amount");
					}
				}
			}
		} catch (Exception e) {
			logger.error("<getTixian->Exception>" + e.toString());
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
				logger.error("<getTixian->finally->Exception>" + e2.toString());
			}
		}

		map = new HashMap<>();
		map.put("list", list);
		map.put("total", total);
		returnModel.setData(map);
	}

	@Override
	public UserAccountModel getUserAccountByAccountName(String accountname, boolean bl) {
		int uid = UserRedisService.getInstance().getAccountNameAndUid(accountname);
		if (uid > 0) {
			return this.getUserAccountByUid(uid, bl);
		} else {
			return null;
		}
	}

	@Override
	public int updateWeiboVrified(int uid, boolean verified, String verified_reason) {
		int ires = 0;
		try {
			ires = DBHelper.execute(VarConfigUtils.dbZhuUser,
					StringUtils.getSqlString(SQL_UpdateUserVerifiedByUid, "user_base_info_", uid), false, verified,
					verified_reason, uid);
			if (ires == 1) {
				String auths = UserRedisService.getInstance().get(RedisContant.AllAuth + uid);
				JSONObject jsonObject = JSONObject.parseObject(auths);

				if (verified) {
					jsonObject.put("weiboAuth", 3);
				} else {
					jsonObject.put("weiboAuth", 0);
				}

				UserRedisService.getInstance().set(RedisContant.AllAuth + uid, jsonObject.toJSONString());
				this.getUserbaseInfoByUid(uid, true);

				// TOSY TASK
				// TaskService.getInstance().taskProcess(uid, TaskConfigLib.Authentication, 1);

				// // 同步后台数据
				// Object[] fields = new Object[3];
				// fields[0] = verified;
				// fields[1] = verified_reason;
				// fields[2] = uid;
				//
				// Map<String, Object> map = new HashMap<>();
				// map.put("db", "zhu_admin");
				// map.put("sql", String.format(SQL_UpdateUserVerifiedByUid,
				// "user_base_info"));
				// map.put("fields", fields);
				// OtherRedisService.getInstance().pushQueue(map);
			}
		} catch (Exception e) {
			logger.error("<updUserBaseHeadimg->Exception>" + e.toString());
		}
		return ires;
	}

	@Override
	public int updateUserIdentity(int uid, int identity) {
		int ires = 0;
		try {
			ires = DBHelper.execute(VarConfigUtils.dbZhuUser,
					StringUtils.getSqlString(SQL_UpdateUserIdentity, "user_base_info_", uid), false, identity, uid);
			if (ires == 1) {
				this.getUserbaseInfoByUid(uid, true);
				// // 同步后台数据
				// Object[] fields = new Object[2];
				// fields[0] = identity;
				// fields[1] = uid;
				//
				// Map<String, Object> map = new HashMap<>();
				// map.put("db", "zhu_admin");
				// map.put("sql", String.format(SQL_UpdateUserIdentity,
				// "user_base_info"));
				// map.put("fields", fields);
				// OtherRedisService.getInstance().pushQueue(map);
			}
		} catch (Exception e) {
			logger.error("<updateUserIdentity->Exception>" + e.toString());
		}
		return ires;
	}

	@Override
	public int updateUserAccountStatus(int uid, int status, ReturnModel returnModel) {
		int ires = 0;
		try {
			ires = DBHelper.execute(VarConfigUtils.dbZhuUser,
					StringUtils.getSqlString(SQL_UpdateUserAccountStatus, "user_account_", uid), false, status, uid);
			if (ires == 1) {

				this.getUserAccountByUid(uid, true);
				// liveService.exitLive(uid, returnModel);
				// // 同步后台数据
				// Object[] fields = new Object[2];
				// fields[0] = status;
				// fields[1] = uid;
				//
				// Map<String, Object> map = new HashMap<>();
				// map.put("db", "zhu_admin");
				// map.put("sql", String.format(SQL_UpdateUserAccountStatus,
				// "user_account"));
				// map.put("fields", fields);
				// OtherRedisService.getInstance().pushQueue(map);
			} else {
				returnModel.setMessage("修改帐号状态失败");
			}
		} catch (Exception e) {
			logger.error("<updateUserIdentity->Exception>" + e.toString());
			returnModel.setMessage(e.toString());
		}
		return ires;
	}

	@Override
	public void pushUserMessage(int uid, BaseCMod cMod) {

		String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret, "appKey=" + VarConfigUtils.ServiceKey,
				"msgBody=" + JSONObject.toJSONString(cMod), "users=" + uid);

		Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_live()).field("appKey", VarConfigUtils.ServiceKey)
				.field("msgBody", JSONObject.toJSONString(cMod)).field("users", uid).field("sign", signParams)
				.asJsonAsync();
	}

	@Override
	public JSONObject getUserWXToken(String code) {

		String url = String.format(UrlConfigLib.getUrl("url").getWeixin_userInfo(),
				PayConfigLib.getConfig().getWeixin_appid(), PayConfigLib.getConfig().getWeixin_appKey(), code);
		logger.info("<bindWeixin>--url" + url);
		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.get(url).asJson();
		} catch (Exception e) {
			logger.error("Error getUserWXToken-exception:" + e.toString());
		}
		if (response == null || response.getStatus() != 200) {
			return null;
		}

		JSONObject jsonObject = JSON.parseObject(response.getBody().toString());

		logger.info("<bindWeixin>--" + jsonObject.toString());

		// {"errcode":40029,"errmsg":"invalid code"}
		if (jsonObject.get("errcode") != null) {
			return null;
		}

		return jsonObject;
	}

	@Override
	public JSONObject getUserInfoByToken(String token, String openid) {

		String url = String.format(UrlConfigLib.getUrl("url").getWeixin_userInfoByToken(), token, openid);

		logger.info("<bindWeixin>--url" + url);

		HttpResponse<String> response = null;
		try {
			response = Unirest.get(url).asString();
		} catch (Exception e) {
			logger.error("Error getUserInfoByToken-exception:" + e.toString());
		}
		if (response == null || response.getStatus() != 200) {
			return null;
		}

		JSONObject object = JSON.parseObject(response.getBody());
		logger.info("<getUserInfoByToken>--" + object.toString());
		if (object.get("errcode") != null) {
			return null;
		}
		return object;
	}

	public Map<String, Object> getUserDataMapForRoomRobot(int dstUid, int uid) {
		RobotBaseInfoModel robotbaseInfoByUid = this.getRobotbaseInfoByUid(dstUid, false);
		if (robotbaseInfoByUid == null) {
			return null;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", dstUid);
		map.put("headimage", robotbaseInfoByUid.getHeadimage());
		map.put("signature", robotbaseInfoByUid.getSignature());
		map.put("sex", robotbaseInfoByUid.getSex());
		map.put("anchorLevel", robotbaseInfoByUid.getAnchorLevel());
		map.put("userLevel", robotbaseInfoByUid.getUserLevel());
		map.put("nickname", robotbaseInfoByUid.getNickname());
		map.put("city",
				org.apache.commons.lang.StringUtils.isEmpty(robotbaseInfoByUid.getCity()) ? VarConfigUtils.Location
						: robotbaseInfoByUid.getCity());
		map.put("follows", RelationRedisService.getInstance().getFollowsTotal(dstUid));// 关注数
		map.put("fans", RelationRedisService.getInstance().getFansTotal(dstUid));// 粉丝数
		if (dstUid != uid) {
			map.put("isfan", RelationRedisService.getInstance().isFan(uid, dstUid));// 是否关注过
		} else {
			map.put("isfan", true);// 是否关注过
		}
		map.put("creditTotal", 0); // 总声援值
		map.put("likes", RelationRedisService.getInstance().getLikes(dstUid));
		map.put("status", false);
		map.put("blockstatus", 1); // =0 封号 ＝1 正常
		map.put("identity", 1);// =1主播 ＝2普通话 ＝3管理
		map.put("verified_reason", ""); // 认证原因
		map.put("verified", false); // false 未认证 true
		// 已认证
		map.put("domain", "");
		return map;
	}

	/**
	 * 获取用户卡片信息接口
	 * 
	 * @param srcUid
	 * @param dstUid
	 *            要获取的用户信息
	 * @return
	 */
	@Override
	public Map<String, Object> getUserCard(int srcUid, int dstUid) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (dstUid >= 900000000) {
			RobotBaseInfoModel robotbaseInfoByUid = this.getRobotbaseInfoByUid(dstUid, false);
			if (robotbaseInfoByUid == null) {
				return null;
			}
			map.put("uid", dstUid);
			map.put("headimage", robotbaseInfoByUid.getHeadimage());
			map.put("livimage", robotbaseInfoByUid.getHeadimage());
			map.put("signature", robotbaseInfoByUid.getSignature());
			map.put("sex", robotbaseInfoByUid.getSex());
			map.put("anchorLevel", robotbaseInfoByUid.getAnchorLevel());
			map.put("userLevel", robotbaseInfoByUid.getUserLevel());
			map.put("nickname", robotbaseInfoByUid.getNickname());
			map.put("city",
					org.apache.commons.lang.StringUtils.isEmpty(robotbaseInfoByUid.getCity()) ? VarConfigUtils.Location
							: robotbaseInfoByUid.getCity());
			map.put("follows", RelationRedisService.getInstance().getFollowsTotal(dstUid));// 关注数
			map.put("fans", RelationRedisService.getInstance().getFansTotal(dstUid));// 粉丝数
			map.put("isFollow", RelationRedisService.getInstance().isFollows(srcUid, dstUid) == null ? false : true);// 是否关注过
			map.put("creditTotal", 0); // 总声援值
			map.put("verified_type", 1);// =1微博认证 =2小猪认证
			map.put("verified_reason", ""); // 认证原因
			map.put("verified", false); // false未认证 true已认证
			map.put("wealth", 0);// 送出猪头数
			map.put("exp", 0);
			map.put("isblack", org.apache.commons.lang.StringUtils
					.isEmpty(RelationRedisService.getInstance().getBlackUser(srcUid, dstUid)) ? false : true);
			map.put("status", false);
			map.put("domain", "");
			map.put("likes", 0);
			map.put("constellation", "");
			map.put("hobby", "");
			map.put("rq", UserRedisService.getInstance().getUserAllRankRqScore(String.valueOf(dstUid)));
		} else {
			UserBaseInfoModel userBaseInfoModel = this.getUserbaseInfoByUid(dstUid, false);
			UserAssetModel userAssetModel = this.getUserAssetByUid(dstUid, false);
			if (userBaseInfoModel == null || userAssetModel == null) {
				return null;
			}
			map.put("uid", userBaseInfoModel.getUid());
			map.put("headimage", userBaseInfoModel.getHeadimage());
			map.put("livimage", userBaseInfoModel.getLivimage());
			map.put("signature", userBaseInfoModel.getSignature());
			map.put("sex", userBaseInfoModel.getSex());
			map.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
			map.put("userLevel", userBaseInfoModel.getUserLevel());
			map.put("nickname", userBaseInfoModel.getNickname());
			map.put("city",
					org.apache.commons.lang.StringUtils.isEmpty(userBaseInfoModel.getCity()) ? VarConfigUtils.Location
							: userBaseInfoModel.getCity());
			map.put("follows", RelationRedisService.getInstance().getFollowsTotal(dstUid));// 关注数
			map.put("fans", RelationRedisService.getInstance().getFansTotal(dstUid));// 粉丝数
			map.put("creditTotal", userAssetModel.getCreditTotal()); // 总声援值
			map.put("wealth", userAssetModel.getWealth() / 100);// 送出猪头数
			map.put("exp", userBaseInfoModel.getExp());
			map.put("isblack", org.apache.commons.lang.StringUtils
					.isEmpty(RelationRedisService.getInstance().getBlackUser(srcUid, dstUid)) ? false : true);
			if (dstUid != srcUid) {
				map.put("isFollow",
						RelationRedisService.getInstance().isFollows(srcUid, dstUid) == null ? false : true);// 是否关注过
			} else {
				map.put("isFollow", true);// 是否关注过
			}
			map.put("verified_reason", userBaseInfoModel.getVerified_reason()); // 认证原因
			map.put("verified", userBaseInfoModel.isVerified()); // false 未认证
			map.put("constellation", userBaseInfoModel.getConstellation());
			map.put("hobby", userBaseInfoModel.getHobby()); // true
			map.put("status", userBaseInfoModel.getLiveStatus());
			if (userBaseInfoModel.getLiveStatus()) {
				// map.put("domain",
				// liveService.getVideoConfig(srcUid, dstUid, userBaseInfoModel.getVideoline()).get("domain"));
				// TOSY 第三方流
				String stream = configService.getThirdStream(dstUid);
				if (null == stream) {
					map.put("domain",
							liveService.getVideoConfig(srcUid, dstUid, userBaseInfoModel.getVideoline()).get("domain"));
				} else {
					map.put("domain", stream);
				}

			} else {
				map.put("domain", "");
			}
			map.put("likes", RelationRedisService.getInstance().getLikes(dstUid));

			map.put("rq", UserRedisService.getInstance().getUserAllRankRqScore(String.valueOf(dstUid)));
		}
		return map;
	}

	/**
	 * 房间内 用户卡片信息
	 * 
	 * @param dstUid
	 * @param uid
	 * @return
	 */
	public Map<String, Object> getUserDataMapForRoom(int dstUid, int uid) {
		if (dstUid >= 900000000) {
			return getUserDataMapForRoomRobot(dstUid, uid);
		}

		UserBaseInfoModel userBaseInfoModel = null;
		userBaseInfoModel = this.getUserbaseInfoByUid(dstUid, false);
		UserAssetModel userAssetModel = this.getUserAssetByUid(dstUid, false);
		UserAccountModel userAccountModel = this.getUserAccountByUid(dstUid, false);

		Map<String, Object> map = new HashMap<String, Object>();
		if (userBaseInfoModel == null || userAssetModel == null) {
			return null;
		}

		map.put("uid", dstUid);
		map.put("headimage", userBaseInfoModel.getHeadimage());
		map.put("signature", userBaseInfoModel.getSignature());
		map.put("sex", userBaseInfoModel.getSex());
		map.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
		map.put("userLevel", userBaseInfoModel.getUserLevel());
		map.put("nickname", userBaseInfoModel.getNickname());
		map.put("city",
				org.apache.commons.lang.StringUtils.isEmpty(userBaseInfoModel.getCity()) ? VarConfigUtils.Location
						: userBaseInfoModel.getCity());
		map.put("follows", RelationRedisService.getInstance().getFollowsTotal(dstUid));// 关注数
		map.put("fans", RelationRedisService.getInstance().getFansTotal(dstUid));// 粉丝数
		if (dstUid != uid) {
			map.put("isfan", RelationRedisService.getInstance().isFan(uid, dstUid));// 是否关注过
		} else {
			map.put("isfan", true);// 是否关注过
		}
		map.put("creditTotal", userAssetModel.getCreditTotal()); // 总声援值
		map.put("likes", RelationRedisService.getInstance().getLikes(dstUid));
		map.put("status", userBaseInfoModel.getLiveStatus());
		map.put("blockstatus", userAccountModel.getStatus()); // =0 封号 ＝1 正常
		map.put("identity", userBaseInfoModel.getIdentity());// =1主播 ＝2普通话 ＝3管理
		map.put("verified_reason", userBaseInfoModel.getVerified_reason()); // 认证原因
		map.put("verified", userBaseInfoModel.isVerified()); // false 未认证 true
		// 已认证

		if (userBaseInfoModel.getLiveStatus()) {
			map.put("domain", liveService.getVideoConfig(uid, dstUid, userBaseInfoModel.getVideoline()).get("domain"));
		} else {
			map.put("domain", "");
		}
		return map;
	}

	/**
	 * 房间内用户信息 弹层使用
	 *
	 * @param curUid
	 *            当前用户
	 * @param dstUid
	 *            获取资料用户
	 * @param anchorUid
	 *            主播
	 * @return
	 */
	@Override
	public Map<String, Object> getUserDataInRoomMap(int curUid, int dstUid, int anchorUid) {
		Map<String, Object> map = this.getUserDataMapForRoom(dstUid, curUid);

		if (map != null) {
			// 该用户是否是管理员
			boolean isManage = RelationRedisService.getInstance().checkManageByAnchorUser(anchorUid, dstUid);
			String userId = RelationRedisService.getInstance().hgetRoomUser(dstUid,anchorUid);
			if (org.apache.commons.lang.StringUtils.isEmpty(userId)&&dstUid<900000000) {
				// 不在房间
				map.put("banned", 0);
				map.put("kick", false);
				map.put("manage", 0);
			} else {
				// 在房间
				UserBaseInfoModel userBaseInfoModel = this.getUserbaseInfoByUid(curUid, false);
				if (userBaseInfoModel == null) {
					// 操作者信息异常
					map.put("banned", 0);
					map.put("kick", false);
					map.put("manage", 0);
				} else if (userBaseInfoModel.getIdentity() == 3) {
					// 超管理
					map.put("banned", OtherRedisService.getInstance().getSilent(anchorUid, dstUid) ? "2" : "1");
					map.put("kick", true);
					map.put("manage", 0);
				} else {
					// 操作着其他身份
					if (curUid == anchorUid) {// 主播操作
						if (dstUid == anchorUid) {
							// 主播对自己操作
							map.put("banned", 0);
							map.put("kick", false);
							map.put("manage", 0);
						} else {
							// 主播对他人操作
							if (isManage) {// 该房间管理员
								map.put("banned", 0);
								map.put("kick", false);
								map.put("manage", 2);
							} else {// 非管理员
								map.put("banned",OtherRedisService.getInstance().getSilent(anchorUid, dstUid) ? "2" : "1");
								map.put("kick", true);
								map.put("manage", 1);
							}
						}
					} else {
						// 非主播
						boolean isCurManage = RelationRedisService.getInstance().checkManageByAnchorUser(anchorUid,
								curUid);
						if (isCurManage) {
							// 管理员对他人操作
							if (isManage) {// 该房间管理员
								map.put("banned", 0);
								map.put("kick", false);
								map.put("manage", 0);
							} else {// 非管理员
								map.put("banned",
										OtherRedisService.getInstance().getSilent(anchorUid, dstUid) ? "2" : "1");
								map.put("kick", true);
								map.put("manage", 0);
							}
						} else {
							map.put("banned", 0);
							map.put("kick", false);
							map.put("manage", 0);
						}
					}
				}
			}
		}
		return map;
	}

	/**
	 * 获取用户卡片上的信息
	 *
	 * @param srcUid  当前用户
	 * @param dstUid  获取资料用户
	 * @return
	 */
	@Override
	public Map<String, Object> getUserCardMap(int srcUid, int dstUid, int anchorUid) {

		Map<String, Object> map = this.getUserCard(srcUid, dstUid);

		if (map != null) {
			if (anchorUid == 0) {
				// 不在房间内
			} else {
				// 该用户是否是管理员
				boolean isManage = RelationRedisService.getInstance().checkManageByAnchorUser(anchorUid, dstUid);
				String userId = RelationRedisService.getInstance().hgetRoomUser(dstUid,anchorUid);
				if (org.apache.commons.lang.StringUtils.isEmpty(userId)&&dstUid<900000000) {
					// 不在房间
					map.put("banned", 0);
					map.put("kick", false);
					map.put("manage", 0);
				} else {
					// 在房间
					UserBaseInfoModel userBaseInfoModel = this.getUserbaseInfoByUid(srcUid, false);
					if (userBaseInfoModel == null) {
						// 操作者信息异常
						map.put("banned", 0);
						map.put("kick", false);
						map.put("manage", 0);
					} else if (userBaseInfoModel.getIdentity() == 3) {
						// 超管理
						map.put("banned", OtherRedisService.getInstance().getSilent(anchorUid, dstUid) ? "2" : "1");
						map.put("kick", true);
						map.put("manage", 0);
					} else {
						// 操作着其他身份
						if (srcUid == anchorUid) {// 主播操作
							if (dstUid == anchorUid) {
								// 主播对自己操作
								map.put("banned", 0);
								map.put("kick", false);
								map.put("manage", 0);
							} else {
								// 主播对他人操作
								if (isManage) {// 该房间管理员
									map.put("banned",OtherRedisService.getInstance().getSilent(anchorUid, dstUid) ? "2" : "1");
									map.put("kick", true);
									map.put("manage", 2);
								} else {// 非管理员
									map.put("banned",
											OtherRedisService.getInstance().getSilent(anchorUid, dstUid) ? "2" : "1");
									map.put("kick", true);
									map.put("manage", 1);
								}
							}
						} else {
							// 非主播
							boolean isCurManage = RelationRedisService.getInstance().checkManageByAnchorUser(anchorUid,
									srcUid);
							if (isCurManage) {
								// 管理员对他人操作
								if (isManage) {// 该房间管理员
									map.put("banned",
											OtherRedisService.getInstance().getSilent(anchorUid, dstUid) ? "2" : "1");
									map.put("kick", true);
									map.put("manage", 0);
								} else {// 非管理员
									map.put("banned",
											OtherRedisService.getInstance().getSilent(anchorUid, dstUid) ? "2" : "1");
									map.put("kick", true);
									map.put("manage", 0);
								}
							} else {
								map.put("banned", 0);
								map.put("kick", false);
								map.put("manage", 0);
							}
						}
					}
				}
			}
			int vipIcon = 0;
			UserVipInfoModel vipInfoModel = ValueaddServiceUtil.getVipInfo(dstUid);
			if (vipInfoModel != null) {
				int gid = vipInfoModel.getGid();
				ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(gid, 1);
				if (privilegeModel != null) {
					vipIcon = privilegeModel.getIconId();
				}
			}
			Map<String, Object> vipmap = new HashMap<String, Object>();
			vipmap.put("vipIcon", vipIcon);
			map.put("vipInfo", vipmap);
		}
		return map;
	}

	/**
	 * 封号
	 * 
	 * @param uid
	 */
	@Override
	public void forbidAccountByManage(int uid, ReturnModel returnModel) {

		UserAccountModel userAccountByUid = this.getUserAccountByUid(uid, true);
		if (userAccountByUid == null) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("未找到用户");
		} else {
			if (userAccountByUid.getStatus() == 0) {
				returnModel.setCode(CodeContant.ERROR);
				returnModel.setMessage("用户已经是此状态");
			} else {
				int i = this.updateUserAccountStatus(uid, 0, returnModel);
				if (i != 1) {
					returnModel.setCode(CodeContant.ERROR);
					returnModel.setMessage("封号失败");
				}
			}

			UpdateAccountStatusCMod updateAccountStatusCMod = new UpdateAccountStatusCMod();
			updateAccountStatusCMod.setStatus(0);
			this.pushUserMessage(Integer.valueOf(uid), updateAccountStatusCMod);

			if (liveService.exitLive(Integer.valueOf(uid), returnModel)) {

				String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
						"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + returnModel.getData().toString(),
						"roomOwner=" + uid);

				Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_end_live())
						.field("appKey", VarConfigUtils.ServiceKey).field("msgBody", returnModel.getData())
						.field("roomOwner", uid).field("sign", signParams).asStringAsync();

				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) returnModel.getData();
				UserBaseInfoModel userBaseInfoModel = new UserBaseInfoModel();
				ExitRoomCMod exitRoomCMod = new ExitRoomCMod();
				exitRoomCMod.setAvatar(userBaseInfoModel.getHeadimage());
				exitRoomCMod.setLevel(userBaseInfoModel.getUserLevel());
				exitRoomCMod.setNickname(userBaseInfoModel.getNickname());
				exitRoomCMod.setSex(userBaseInfoModel.getSex());
				exitRoomCMod.setUid(Integer.valueOf(uid));
				exitRoomCMod.setCreditTotal(Integer.valueOf(map.get("creditTotal").toString()));
				exitRoomCMod.setPersontimes(Integer.valueOf(map.get("persontimes").toString()));
				exitRoomCMod.setRoomLikes(Integer.valueOf(map.get("roomlikes").toString()));
				exitRoomCMod.setTimeslong(Long.valueOf(map.get("timeslong").toString()));

				signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
						"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + JSONObject.toJSONString(exitRoomCMod),
						"roomOwner=" + uid);

				Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room())
						.field("appKey", VarConfigUtils.ServiceKey)
						.field("msgBody", JSONObject.toJSONString(exitRoomCMod)).field("roomOwner", uid)
						.field("sign", signParams).asStringAsync();
			}
		}
	}

	/**
	 * 解封
	 * 
	 * @param uid
	 */
	@Override
	public void unForbidAccountByManage(int uid, ReturnModel returnModel) {

		UserAccountModel userAccountByUid = this.getUserAccountByUid(uid, true);
		if (userAccountByUid == null) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("未找到用户");
		} else {
			if (userAccountByUid.getStatus() == 1) {
				returnModel.setCode(CodeContant.ERROR);
				returnModel.setMessage("用户已经是此状态");
			} else {
				int i = this.updateUserAccountStatus(uid, 1, returnModel);
				if (i != 1) {
					returnModel.setCode(CodeContant.ERROR);
					returnModel.setMessage("解封失败");
				}
			}
		}
	}

	@Override
	public void changeCityByUid(int uid, String city, ReturnModel returnModel) {
		UserBaseInfoModel userBaseInfoModel = this.getUserbaseInfoByUid(uid, false);
		if (userBaseInfoModel == null) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("未找到用户");
		} else {
			if (city.equalsIgnoreCase(userBaseInfoModel.getCity())) {
				return;
			}
			int ires = 0;
			try {
				ires = DBHelper.execute(VarConfigUtils.dbZhuUser,
						StringUtils.getSqlString(SQL_UpdateUserCity, "user_base_info_", uid), false, city, uid);
				if (ires == 1) {
					this.getUserbaseInfoByUid(uid, true);
				} else {
					returnModel.setCode(CodeContant.ERROR);
					returnModel.setMessage("修改帐号状态失败");
				}
			} catch (Exception e) {
				logger.error("<changeCityByUid->Exception>" + e.toString());
				returnModel.setCode(CodeContant.ERROR);
				returnModel.setMessage(e.toString());
			}
		}
	}

	@Override
	public List<Map<String, Object>> getRobotList() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			// 获取用户基本信息
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
			statement = conn.prepareStatement(SQL_GetRobotList);
			DBHelper.setPreparedStatementParam(statement);
			rs = statement.executeQuery();

			if (rs != null) {
				while (rs.next()) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("uid", rs.getInt("uid"));
					map.put("nickname", rs.getString("nickname"));
					map.put("sex", rs.getBoolean("sex"));
					map.put("level", rs.getInt("userLevel"));
					map.put("avatar", rs.getString("headimage"));
					list.add(map);
				}
			}
		} catch (SQLException e) {
			logger.error("<getRobotList->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getRobotList->Exception>" + e.getMessage());
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
		return list;
	}

	public Map<String, Object> getRobotByUid(int uid) {
		Map<String, Object> map = new HashMap<String, Object>();

		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			// 获取用户基本信息
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
			statement = conn.prepareStatement(SQL_GetRobotByUid);
			DBHelper.setPreparedStatementParam(statement, uid);
			rs = statement.executeQuery();

			if (rs != null) {
				while (rs.next()) {
					map.put("uid", rs.getInt("uid"));
					map.put("nickname", rs.getString("nickname"));
					map.put("sex", rs.getBoolean("sex"));
					map.put("userLevel", rs.getInt("userLevel"));
					map.put("headimage", rs.getString("headimage"));
				}
			}
		} catch (SQLException e) {
			logger.error("<getRobotList->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getRobotList->Exception>" + e.getMessage());
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
		return map;
	}

	@Override
	public List<Map<String, Object>> getAnchorList() {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Set<String> hotRoom = OtherRedisService.getInstance().getHotRoom(0);

		if (hotRoom != null) {
			hotRoom.add("99001"); // 游戏房
			for (String suid : hotRoom) {
				int iuid = Integer.valueOf(suid);
				UserBaseInfoModel userBaseInfoModel = this.getUserbaseInfoByUid(iuid, false);
				if (userBaseInfoModel != null) {
					Map<String, Object> map = new HashMap<String, Object>();

					int realtimes = RelationRedisService.getInstance().getRealEnterRoomTimes(iuid);
					int realcount = RelationRedisService.getInstance().getLiveRealEnterTotal(iuid);

					map.put("uid", userBaseInfoModel.getUid());
					map.put("nickname", userBaseInfoModel.getNickname());
					map.put("userLevel", userBaseInfoModel.getUserLevel());
					map.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
					map.put("opentime", userBaseInfoModel.getOpentime());
					map.put("recommend", userBaseInfoModel.getRecommend());
					map.put("verified", userBaseInfoModel.isVerified());
					map.put("contrRq", userBaseInfoModel.getContrRq());
					if (iuid == 99001) {
						map.put("userList",
								RelationRedisService.getInstance().getUserListInAnchor(userBaseInfoModel.getUid(), 1L));
					} else {
						map.put("userList",
								RelationRedisService.getInstance().getUserListInAnchor(userBaseInfoModel.getUid(), 0L));
					}
					map.put("realcout", realcount);
					map.put("realtimes", realtimes);
					list.add(map);
				}
			}
		}
		return list;
	}

	@Override
	public void setRecomendRQ(int uid, int recommend, int rq, int grade, ReturnModel returnModel) {
		UserBaseInfoModel userBaseInfoModel = this.getUserbaseInfoByUid(uid, false);
		int ires = 0;

		if (userBaseInfoModel != null) {
			if (userBaseInfoModel.getRecommend() == recommend && userBaseInfoModel.getContrRq() == rq
					&& userBaseInfoModel.getGrade() == grade) {
				// 无需修改
				logger.info(String.format("setRecomendRQ new recommend:%s,new rq:%s  is same", recommend, rq));
			} else {
				Connection con = null;
				PreparedStatement pstmt = null;
				try {
					con = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
					pstmt = con.prepareStatement(
							StringUtils.getSqlString(SQL_UpdUserRecommendByUid, "user_base_info_", uid));
					DBHelper.setPreparedStatementParam(pstmt, recommend, rq, grade, uid);
					ires = pstmt.executeUpdate();
					if (ires == 1) {
						this.getUserbaseInfoByUid(uid, true);

						if (recommend == 0) {
							// 设置为普通
							if (userBaseInfoModel.getRecommend() == 2 || userBaseInfoModel.getRecommend() == 3) {
								// 之前为热门
								OtherRedisService.getInstance().delRecommendRoom(uid);
								OtherRedisService.getInstance().delHotRoom(uid);
								OtherRedisService.getInstance().delMobileRoom(uid);
								OtherRedisService.getInstance().addBaseRoom(uid, recommend, rq, 0);
							} else if (userBaseInfoModel.getRecommend() == 1) {
								// 之前为最新
								OtherRedisService.getInstance().delHotRoom(uid);
								OtherRedisService.getInstance().delMobileRoom(uid);
								OtherRedisService.getInstance().addBaseRoom(uid, recommend, rq, 0);
							}
						} else if (recommend == 1) {
							// 设置为最新
							if (userBaseInfoModel.getRecommend() == 2 || userBaseInfoModel.getRecommend() == 3) {
								// 之前为热门
								OtherRedisService.getInstance().delRecommendRoom(uid);
								if (rq != userBaseInfoModel.getContrRq()) {
									OtherRedisService.getInstance().addhotRoom(uid, recommend,
											userBaseInfoModel.getOpentime(), 0);
								}
							} else if (userBaseInfoModel.getRecommend() == 0) {
								// 之前为普通
								OtherRedisService.getInstance().delBaseRoom(uid);
								OtherRedisService.getInstance().addhotRoom(uid, recommend,
										userBaseInfoModel.getOpentime(), 0);
							} else {
								if (rq != userBaseInfoModel.getContrRq()) {
									OtherRedisService.getInstance().addhotRoom(uid, recommend,
											userBaseInfoModel.getOpentime(), 0);
								}
							}
						} else if (recommend == 2 || recommend == 3) {
							// 设置为推荐
							int roomShowUsers = roomService.getRoomShowUsers(uid, rq);
							OtherRedisService.getInstance().addRecommendRoom(uid, recommend, roomShowUsers, 0);

							if (userBaseInfoModel.getRecommend() == 0) {
								// 之前为普通
								OtherRedisService.getInstance().addhotRoom(uid, recommend,
										userBaseInfoModel.getOpentime(), 0);
								OtherRedisService.getInstance().delBaseRoom(uid);
							}
						}
					} else {
						returnModel.setCode(CodeContant.updRecommend);
						returnModel.setMessage("更新失败");
					}

				} catch (Exception e) {
					logger.error("setRecomendRQ->Exception:" + e.getMessage());
				} finally {
					try {
						if (pstmt != null) {
							pstmt.close();
						}
						if (con != null) {
							con.close();
						}
					} catch (Exception e2) {
						logger.error("setRecomendRQ-finally-Exception:" + e2.getMessage());
					}
				}
			}
		} else {
			returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
			returnModel.setMessage("用户信息异常");
		}
	}

	/**
	 * 获取用户的充值记录
	 * 
	 * @param uid
	 * @param returnModel
	 */
	@Override
	public void getPaylist(int uid, ReturnModel returnModel) {
		List<Map<String, Object>> paylist = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> paymonthlist = new ArrayList<Map<String, Object>>();

		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			// 获取用户基本信息
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuPay).getConnection();
			statement = conn.prepareStatement(SQL_SelectPayOrder);
			DBHelper.setPreparedStatementParam(statement, uid);
			rs = statement.executeQuery();

			if (rs != null) {
				while (rs.next()) {
					Map<String, Object> map = new HashedMap<String, Object>();
					Date date = new Date(rs.getInt("paytime") * 1000L);
					// map.put("paytimes",
					// DateUtils.parseDate(String.valueOf(rs.getInt("paytime")*1000L),
					// "yyyy-MM-dd HH:mm:ss"));
					map.put("order_id", rs.getString("order_id"));
					map.put("paytimes", DateUtils.dateToString(date, "yyyy-MM-dd HH:mm:ss"));
					map.put("amount", rs.getDouble("amount"));
					map.put("zhutou", rs.getInt("zhutou"));
					map.put("os", rs.getInt("os") == 3 ? "微信公众号" : StringUtils.getPay(rs.getString("paytype")));
					paylist.add(map);
				}

				rs.close();
				statement.close();
			}
			statement = conn.prepareStatement(sQL_SelectPayOrderTotalByMonth);
			DBHelper.setPreparedStatementParam(statement, uid);
			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					Map<String, Object> map = new HashedMap<String, Object>();
					map.put("paytimes", rs.getString("paytimes"));
					map.put("amount", rs.getDouble("amount"));
					map.put("zhutou", rs.getInt("zhutou"));
					paymonthlist.add(map);
				}
			}

		} catch (SQLException e) {
			logger.error("<getPaylist->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getPaylist->Exception>" + e.getMessage());
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
				System.out.println("<getPaylist->finally->Exception>" + e2.getMessage());
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("paylist", paylist);
		map.put("paymonthlist", paymonthlist);
		returnModel.setData(map);
	}

	@Override
	public int insertAuthentication(int uid, String realName, String cardID, String cardNo, String bankAccount,
			String provinceOfBank, String cityOfBank, String branchBank, String positiveImage, String negativeImage,
			String handImage) {
		Connection conn = null;
		PreparedStatement statement = null;
		int ires = 0;
		try {
			// 获取用户基本信息
			String Sql = "";
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
			statement = conn.prepareStatement(SQL_selAuthen);
			DBHelper.setPreparedStatementParam(statement, uid);
			ResultSet executeQuery = statement.executeQuery();

			if (executeQuery != null && executeQuery.next()) {
				Sql = SQL_UpdateAuthen;
				executeQuery.close();
			} else {
				Sql = SQL_InsertAuthen;
			}
			if (statement != null) {
				statement.close();
			}
			statement = conn.prepareStatement(Sql);
			DBHelper.setPreparedStatementParam(statement, realName, cardID, cardNo, bankAccount, provinceOfBank,
					cityOfBank, branchBank, positiveImage, negativeImage, handImage, System.currentTimeMillis() / 1000,
					1, uid);
			ires = statement.executeUpdate();
		} catch (SQLException e) {
			logger.error("<insertAuthentication->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<insertAuthentication->Exception>" + e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error("<insertAuthentication->finally->Exception>" + e2.getMessage());
			}
		}
		return ires;
	}

	@Override
	public AuthenticationModel getAuthentication(int uid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		AuthenticationModel authenticationModel = null;

		try {
			// 获取用户基本信息
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
			statement = conn.prepareStatement(SQL_selAuthen);
			DBHelper.setPreparedStatementParam(statement, uid);
			rs = statement.executeQuery();

			if (rs != null && rs.next()) {
				authenticationModel = new AuthenticationModel().populateFromResultSet(rs);
			}
		} catch (SQLException e) {
			logger.error("<getAuthentication->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getAuthentication->Exception>" + e.getMessage());
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
				logger.error("<getAuthentication->finally->Exception>" + e2.getMessage());
			}
		}
		return authenticationModel;
	}

	@Override
	public void exchangeConfig(ReturnModel returnModel) {
		List<ConcurrentHashMap<String, Object>> config = ExchangeConfigLib.getConfig();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("config", config);
		returnModel.setData(map);
	}

	@Override
	public void exchangeList(int uid, ReturnModel returnModel) {

		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		List<PayExchangeModel> list = new ArrayList<PayExchangeModel>();

		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuPay).getConnection();
			statement = conn.prepareStatement(SQL_GetExchangeList);
			statement.setInt(1, uid);

			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					list.add(new PayExchangeModel().populateFromResultSet(rs));
				}
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", list);
			returnModel.setData(map);

		} catch (Exception ex) {
			returnModel.setCode(400);
			returnModel.setMessage("兑换查询错误");
			logger.error("error exchangeList-exception:" + ex.toString());
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
				returnModel.setCode(400);
				returnModel.setMessage("兑换finally错误");
				logger.error("error exchangeList-finally-exception:" + e.toString());
			}
		}
	}

	/**
	 * 声援值兑换
	 * 
	 * @param uid
	 *            用户uid
	 * @param amount
	 *            兑换金额
	 * @return
	 */
	@Override
	public void exchange(int uid, int money, ReturnModel returnModel) {

		int credit = 0;
		int zhutou = ExchangeConfigLib.getZhutou(money);
		if (UserRedisService.getInstance().getForbidExchange(String.valueOf(uid))) {
			returnModel.setCode(CodeContant.PayForbidExchange);
			returnModel.setMessage("你的兑换权限被锁定，请联系官方客服");
		} else if (zhutou <= 0) {
			returnModel.setCode(CodeContant.PayExchangeMoney);
			returnModel.setMessage("内兑金额错误");
		} else {

			UserAssetModel userAssetModel = this.getUserAssetByUid(uid, false);
			UserBaseInfoModel userBaseInfoModel = this.getUserbaseInfoByUid(uid, false);

			int rate = LevelsConfigLib.getForAdvanced(userBaseInfoModel.getAnchorLevel()).getRate();
			if (rate > 0) {
				// 消耗的声援值1:100
				credit = (int) Math.ceil(money * 10000 / rate);
				if (userAssetModel.getCredit() < credit) {

					returnModel.setCode(CodeContant.PayExchangeLess);
					returnModel.setMessage("声援值不足");
				} else {

					int res = this.updUserAssetByExchangeUid(uid, credit, zhutou);
					if (res == 1) {
						try {
							res = DBHelper.execute(VarConfigUtils.dbZhuPay, SqlTemplete.SQL_InsertExchange, false, uid,
									credit, money, zhutou, System.currentTimeMillis() / 1000, rate);
						} catch (Exception e) {
							logger.error("exchange->exception:" + e.toString());
						}
						if (res != 1) {
							logger.error("error insert into exchange is err");
						}

						userAssetModel = this.getUserAssetByUid(uid, false);
						rate = LevelsConfigLib.getForAdvanced(userBaseInfoModel.getAnchorLevel()).getRate();

						credit = userAssetModel.getCredit();
						money = (credit / 100) * (rate) / 100;
						zhutou = userAssetModel.getMoney();
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("credit", credit);// 声援值
						map.put("zhutou", zhutou);// 剩余猪头数
						map.put("money", money);// 可兑换金额
						Map<String, Object> exchange = new HashMap<String, Object>();
						exchange.put("exchange", map);

						returnModel.setData(exchange);
					} else {
						returnModel.setCode(CodeContant.MONEYDEDUCT);
						returnModel.setMessage("扣款失败");
					}
				}
			} else {
				returnModel.setCode(CodeContant.PayExchangeRate);
				returnModel.setMessage("兑换比率异常");
			}
		}
	}

	@Override
	public UserXiaozhuAuthModel getXiaozhuAuth(int uid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		UserXiaozhuAuthModel xiaozhuAuthModel = null;

		try {
			// 获取用户基本信息
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
			statement = conn.prepareStatement(SQL_selXiaozhuAuth);
			DBHelper.setPreparedStatementParam(statement, uid);
			rs = statement.executeQuery();

			if (rs != null && rs.next()) {
				xiaozhuAuthModel = new UserXiaozhuAuthModel().populateFromResultSet(rs);
			}
		} catch (SQLException e) {
			logger.error("<UserXiaozhuAuthModel->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<UserXiaozhuAuthModel->Exception>" + e.getMessage());
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
				logger.error("<UserXiaozhuAuthModel->finally->Exception>" + e2.getMessage());
			}
		}
		return xiaozhuAuthModel;
	}

	public int selectXiaozhuAuthForStatus(int uid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		int ires = 0;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
			statement = conn.prepareStatement(SqlTemplete.SQL_selXiaozhuAuth_STATUS);
			DBHelper.setPreparedStatementParam(statement, uid);
			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					ires++;
				}
			}
		} catch (SQLException e) {
			logger.error("<selectXiaozhuAuthForStatus->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<selectXiaozhuAuthForStatus->Exception>" + e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error("<selectXiaozhuAuthForStatus->finally->Exception>" + e2.getMessage());
			}
		}
		return ires;
	}

	public int insertXiaozhuAuth(int uid, String nickname, String authContent, String authPics, String authURLs) {
		Connection conn = null;
		PreparedStatement statement = null;
		int ires = 0;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
			statement = conn.prepareStatement(SqlTemplete.SQL_insXiaozhuAuth);
			DBHelper.setPreparedStatementParam(statement, uid, nickname, authContent, authPics, authURLs, 1,
					System.currentTimeMillis() / 1000, 0);
			ires = statement.executeUpdate();
		} catch (SQLException e) {
			logger.error("<insertXiaozhuAuth->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<insertXiaozhuAuth->Exception>" + e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error("<insertXiaozhuAuth->finally->Exception>" + e2.getMessage());
			}
		}
		return ires;
	}

	@Override
	public int getAuthenticationForStatus(int uid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		int ires = 0;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
			statement = conn.prepareStatement(SqlTemplete.SQL_selAuthenForStatus);
			DBHelper.setPreparedStatementParam(statement, uid);
			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					ires++;
				}
			}
		} catch (SQLException e) {
			logger.error("<getAuthenticationForStatus->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getAuthenticationForStatus->Exception>" + e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error("<getAuthenticationForStatus->finally->Exception>" + e2.getMessage());
			}
		}
		return ires;
	}

	public int cannelXzAuth(int uid) {
		Connection conn = null;
		PreparedStatement statement = null;
		int ires = 0;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
			statement = conn.prepareStatement(SqlTemplete.SQL_delXiaozhuAuth);
			DBHelper.setPreparedStatementParam(statement, uid);
			ires = statement.executeUpdate();
			if (ires > 0) {
				this.getUserbaseInfoByUid(uid, true);
				String auths = UserRedisService.getInstance().get(RedisContant.AllAuth + uid);
				JSONObject jsonObject = JSONObject.parseObject(auths);
				jsonObject.put("xiaozhuAuth", 0);
				UserRedisService.getInstance().set(RedisContant.AllAuth + uid, jsonObject.toJSONString());
			}
		} catch (SQLException e) {
			logger.error("<cannelXzAuth->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<cannelXzAuth->Exception>" + e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error("<cannelXzAuth->finally->Exception>" + e2.getMessage());
			}
		}
		return ires;
	}

	public int getAuthNikenameCount(String nikename) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		int ires = 0;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
			statement = conn.prepareStatement(SqlTemplete.SQL_selAuthNickname);
			DBHelper.setPreparedStatementParam(statement, nikename);
			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					ires++;
				}
			}
		} catch (SQLException e) {
			logger.error("<getAuthNikenameCount->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getAuthNikenameCount->Exception>" + e.getMessage());
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
				logger.error("<getAuthNikenameCount->finally->Exception>" + e2.getMessage());
			}
		}
		return ires;
	}

	@Override
	public Map<String, Object> getSinaVerified(Integer uid) {
		return userDao.getSinaVerified(uid);
	}

	@Override
	public void getUserExpNextLevel(int uid, ReturnModel returnModel) {

		Map<String, Object> map = new HashMap<String, Object>();

		UserBaseInfoModel userBaseInfoModel = this.getUserbaseInfoByUid(uid, false);
		UserAssetModel userAssetModel = this.getUserAssetByUid(uid, false);

		if (userBaseInfoModel != null && userAssetModel != null) {

			map.put("userNext", this.getNextLevel(userBaseInfoModel.getUserLevel(), "user"));
			map.put("exp", userBaseInfoModel.getExp() + userAssetModel.getWealth());
			map.put("headimage", userBaseInfoModel.getHeadimage());
			map.put("nickname", userBaseInfoModel.getNickname());
			returnModel.setData(map);
		} else {
			returnModel.setCode(400);
			returnModel.setMessage("用户不存在");
		}
	}

	private void copyFriendReturnlist(List<Map<String, Object>> list, List<Map<String, Object>> listRt, int uid) {
		for (Map<String, Object> map : listRt) {
			String dstUid = (String) map.get("uid");
			if (null == dstUid)
				continue;

			Integer dstuid = Integer.valueOf(dstUid);
			if (null == dstuid)
				continue;

			if (dstuid.intValue() == uid)
				continue;

			if (RelationRedisService.getInstance().isFan(uid, dstuid)
					&& RelationRedisService.getInstance().isFan(dstuid, uid)) {

				Map<String, Object> mapRt = new HashMap<String, Object>();
				if (null == OtherRedisService.getInstance().getLive2ndUid(dstuid)) {
					mapRt.put("isJoin", 0);
				} else {
					mapRt.put("isJoin", 1);
				}

				mapRt.put("status", map.get("status"));
				mapRt.put("uid", map.get("uid"));
				mapRt.put("nickname", map.get("nickname"));
				mapRt.put("headimage", map.get("headimage"));
				mapRt.put("anchorLevel", map.get("anchorLevel"));
				mapRt.put("slogan", map.get("slogan"));
				mapRt.put("city", map.get("city"));
				mapRt.put("enters", map.get("enters"));
				mapRt.put("mobileliveimg", map.get("mobileliveimg"));
				mapRt.put("sex", map.get("sex"));

				if (null != map.get("domain")) {
					mapRt.put("domain", map.get("domain"));
				}
				mapRt.put("thirdstream", map.get("thirdstream"));
				mapRt.put("verified", map.get("verified"));

				list.add(mapRt);
			}

		}
	}

	@Override
	public void getUserFriendsInliving(int uid, int os, int page, ReturnModel returnModel) {
		List<Map<String, Object>> listRt0 = LivingListConfigLib.getLivingList(os, 0, page);
		List<Map<String, Object>> listRt1 = LivingListConfigLib.getLivingList(os, 1, page);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		copyFriendReturnlist(list, listRt1, uid);
		copyFriendReturnlist(list, listRt0, uid);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		returnModel.setData(map);
	}

	@Override
	public Map<String, String> getRecordAllByUid(String uid, ReturnModel returnModel) {
		Map<String, String> data = OtherRedisService.getInstance().getUidRecordAll(uid);

		// 返回有序数组
		if (null != returnModel) {
			// 排序
			LinkedList<RecordItem> recslist = new LinkedList<RecordItem>();
			SortedSet<String> keys = new TreeSet<String>(data.keySet());
			for (String key : keys) {
				String value = data.get(key);
				try {
					recslist.push(JSONObject.parseObject(value, RecordItem.class));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 统一封面
			UserBaseInfoModel info = this.getUserbaseInfoByUid(Integer.valueOf(uid), false);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cover", info.getPcimg1());
			map.put("reclist", recslist);

			returnModel.setData(map);
		}

		return data;
	}

	@Override
	public void delmyRecordByTime(int uid, String time, ReturnModel returnModel) {
		// TODO request 7n
		// get info
		// from redis
		// del redis
	}

	/**
	 * 插入user_cover_check表数据
	 *
	 * @param userAccountModel
	 * @return
	 */
	@Override
	public int InsertUserCover(int uId, String picCover, String picCover1, String picCover2) {
		return userDao.addUserCover(uId, picCover, picCover1, picCover2);
	}

	@Override
	public Map<String, Object> isNullId(int uId, int status) {
		return userDao.isNullId(uId, status);
	}

	@Override
	public boolean updUserCover(int id, String picCover, String picCover1, String picCover2) {
		return userDao.updUserCover(id, picCover, picCover1, picCover2);
	}

	@Override
	public Map<String, Object> getStatus(int uId) {
		return userDao.getStatus(uId);
	}

	@Override
	public Map<String, Object> getNewestRecord(int uId) {
		return userDao.getNewestRecord(uId);
	}

	@Override
	public ReturnModel modifyMoney(int uid, int zhutou, int credit, int type, String desc) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ReturnModel returnModel = new ReturnModel();
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
			conn.setAutoCommit(false);
			
			if (type == 1) {// 添加
				pstmt = conn.prepareStatement(StringUtils.getSqlString(SQL_UpdUserAssetJia, "user_asset_", uid));
				DBHelper.setPreparedStatementParam(pstmt, zhutou, credit, credit, uid);
			} else if (type == 0) {// 消费
				pstmt = conn.prepareStatement(StringUtils.getSqlString(SQL_UpdUserAssetJian, "user_asset_", uid));
				DBHelper.setPreparedStatementParam(pstmt, zhutou, 0, uid, zhutou);
			} else {
				returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
				returnModel.setMessage("参数类型错误");
				return returnModel;
			}
			pstmt.addBatch();
			String userAssetTableName = StringUtils.getSqlString("user_asset_", uid);
			pstmt.addBatch(String.format(SQL_InsertUserMoneyChange, type, zhutou, System.currentTimeMillis() / 1000,
					desc, userAssetTableName, uid));

			int[] executeBatch = pstmt.executeBatch();
			
			if(executeBatch[0]==0) {
				conn.rollback();
				returnModel.setCode(CodeContant.MONEYLESS);
				returnModel.setMessage("余额不足");
				return returnModel;
			}
			if(executeBatch[1]==0) {
				conn.rollback();
				returnModel.setCode(CodeContant.MONEYDEDUCT);
				returnModel.setMessage("扣费失败");
				return returnModel;
			}
			conn.commit();
			//更新redis中缓存信息
			getUserAssetByUid(uid, true);
			//更新用户等级排行等
			roomService.addCreditByAdmin(uid, credit);
			return returnModel;
		} catch (Exception e) {
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (SQLException e1) {
				logger.error("回滚事物异常",e);
			}
			logger.error("更新用户余额异常",e);
			
			returnModel.setCode(CodeContant.CONSYSTEMERR);
			returnModel.setMessage("参数类型错误"); 
			return returnModel;
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error("关闭连接异常",e2);
			}
		}
	}

	@Override
	public int updUserBaseGameStatusById(int uid,Integer gameStatus, Long gameId) {
		int iResult = 0;
		Connection conn = null;
		PreparedStatement statement = null;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
			String sql = StringUtils.getSqlString(SqlTemplete.SQL_UpdUserGameInfo,"user_base_info_",uid);
			statement = conn.prepareStatement(sql);
			DBHelper.setPreparedStatementParam(statement, gameStatus,gameId,uid);
			iResult = statement.executeUpdate();
			if(iResult>0) {
				this.getUserbaseInfoByUid(uid, true);
			}
		} catch (Exception e) {
			logger.error("更新用户游戏状态异常",e);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error(e2.getMessage(),e2);
			}
		}
		return iResult;
	}

	@Override
	public boolean updUserBasePKStatusById(int firstUid, int secodUid, int lianmaiStatus) {
		Connection conn = null;
		PreparedStatement statement = null;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
			conn.setAutoCommit(false);
			
			String firstUidSql = StringUtils.getSqlString(SqlTemplete.SQL_UpdUserPKStatus,"user_base_info_",firstUid);
			statement = conn.prepareStatement(firstUidSql);
			DBHelper.setPreparedStatementParam(statement, lianmaiStatus,secodUid,firstUid);
			int first = statement.executeUpdate();
			if(first==0) {
				return false;
			}
			String secodUidSql = StringUtils.getSqlString(SqlTemplete.SQL_UpdUserPKStatus,"user_base_info_",secodUid);
			statement = conn.prepareStatement(secodUidSql);
			DBHelper.setPreparedStatementParam(statement, lianmaiStatus,firstUid,secodUid);
			int secod = statement.executeUpdate();
			if(secod==0) {
				//回滚事务
				conn.rollback();
				return false;
			}
			conn.commit();
			this.getUserbaseInfoByUid(firstUid, true);
			this.getUserbaseInfoByUid(secodUid, true);
			return true;
		} catch (Exception e) {
			logger.error("更新用户PK状态异常",e);
			if(conn!=null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					logger.error("回滚事务异常",e1);
					return false;
				}
			}
			return false;
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error(e2.getMessage(),e2);
			}
		}
	}

	@Override
	public int updateUserMoneyRbm(String uid, Double imoney) {
		int iResult = 0;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement statement = null;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
			String sql_Select = SqlTemplete.SQL_selUserRationInfo_Money;
			statement = conn.prepareStatement(sql_Select);
			DBHelper.setPreparedStatementParam(statement,uid);
			rs = statement.executeQuery();
			Long totalCount = 0l;
			if (rs != null && rs.next()) {
				totalCount = rs.getLong("ct");
			}
			if(totalCount > 0) {//更新
				String sql_Update = SqlTemplete.SQL_updUserRationInfo_Money;
				statement = conn.prepareStatement(sql_Update);
				DBHelper.setPreparedStatementParam(statement, imoney,uid);
				iResult = statement.executeUpdate();
			}else {//插入
				String sql_Insert = SqlTemplete.SQL_insUserRationInfo_Money;
				statement = conn.prepareStatement(sql_Insert);
				DBHelper.setPreparedStatementParam(statement,uid,imoney);
				iResult = statement.executeUpdate();
			}
		} catch (Exception e) {
			logger.error("更新或添加用户充值金额（人民币）：",e);
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
				logger.error(e2.getMessage(),e2);
			}
		}
		return iResult;
	}
	
	public Map<String, Object> getSupportContributionRankByDstuid(int dstuid,int uid, int type, int page) {
		Set<Tuple> set = UserRedisService.getInstance().getSupportContributionRankByDstuid(dstuid, type, page);
		int total = UserRedisService.getInstance().getSupportContributionTotalByDstuid(dstuid, type).intValue();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Tuple tuple : set) {
			UserBaseInfoModel userInfoModel = this.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()), false);
			if (userInfoModel == null) {
				continue;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", userInfoModel.getUid());
			map.put("headimage", userInfoModel.getHeadimage());
			map.put("nickname", userInfoModel.getNickname());
			map.put("sex", userInfoModel.getSex());
			map.put("money", tuple.getScore());//贡献声援值
			map.put("userLevel", userInfoModel.getUserLevel());
			map.put("anchorLevel", userInfoModel.getAnchorLevel());

			list.add(map);
		}
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("total", total);
		dataMap.put("list", list);
		return dataMap;
	}

	@Override
	public int updUserBaseInfoByUid(Integer uid, String nickname,Boolean sex, String signature, String hobby, String headimage,String livimage) {
		UserBaseInfoModel userBaseInfoModel = this.getUserbaseInfoByUid(uid, false);
		if (userBaseInfoModel == null) {
			return 0;
		}
		try {
			String sql = StringUtils.getSqlString(SQL_UpdUserNicknameAignatureAobbyHeadimageLivimageByUid, "user_base_info_", uid);
			int ires = DBHelper.execute(VarConfigUtils.dbZhuUser, sql, false, nickname,sex,signature,hobby,headimage,livimage,uid);
			if (ires > 0) {
				this.getUserbaseInfoByUid(uid, true);
				TaskService.getInstance().taskProcess(uid, TaskConfigLib.CompleteData, 1);
			}
			return ires;
		} catch (Exception e) {
			logger.error("<updUserBaseInfoByUid->Exception>" + e.toString(),e);
			return 0;
		}
	}
}