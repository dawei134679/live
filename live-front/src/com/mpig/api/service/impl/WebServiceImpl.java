package com.mpig.api.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.mpig.api.SqlTemplete;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.dictionary.GameAppConfig;
import com.mpig.api.dictionary.lib.GameAppConfigLib;
import com.mpig.api.model.IosVersionModel;
import com.mpig.api.model.WebVerModel;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.service.IWebService;
import com.mpig.api.utils.JsonUtil;
import com.mpig.api.utils.VarConfigUtils;

@Service
public class WebServiceImpl implements IWebService, SqlTemplete {

	private static final Logger logger = Logger.getLogger(WebServiceImpl.class);

	private static List<Map<String, Object>> bannerlist = new ArrayList<Map<String, Object>>();

	/**
	 * 初始化轮播图列表
	 */
	public static boolean updateBannerlist() {
		boolean isOk = false;
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {

			bannerlist.clear();
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuWeb).getConnection();
			statement = conn.prepareStatement(SQL_GetBanners);
			rs = statement.executeQuery();

			if (rs != null) {

				while (rs.next()) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", rs.getInt("id"));
					map.put("picUrl", rs.getString("picUrl"));
					map.put("webPicUrl", rs.getString("webPicUrl"));
					map.put("jumpUrl", rs.getString("jumpUrl"));
					map.put("sort", rs.getInt("sort"));
					map.put("startShow", rs.getInt("startShow"));
					map.put("endShow", rs.getInt("endShow"));
					map.put("os", rs.getInt("platform"));
					map.put("roomId", rs.getInt("roomId"));
					map.put("roomType", rs.getInt("roomType"));
					if (rs.getInt("roomType") > 0) {
						List<GameAppConfig> allGameApp = GameAppConfigLib.allGameApp();
						for (GameAppConfig config : allGameApp) {
							if (Integer.valueOf(config.roomId) == rs.getInt("roomId")) {
								map.put("appId", config.appId);
								map.put("serverAddress", config.serverAddress);
								break;
							}
						}
					} else {
						map.put("appId", 0);
						map.put("serverAddress", "");
					}

					bannerlist.add(map);
				}
			}
			isOk = true;
		} catch (Exception e) {
			logger.error("getBannerlist-Exception:", e);
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
				logger.error("<getBannerlist-finally-Exception>", e2);
			}
		}
		return isOk;
	}

	/**
	 * 添加反馈
	 * 
	 * @param uid
	 * @param cls
	 * @param des
	 * @return
	 */
	@Override
	public int addFeedback(int uid, int cls, String mobile, String des) {
		int ires = 0;
		try {
			ires = DBHelper.execute(VarConfigUtils.dbZhuWeb, SQL_InsertFeedback, false, uid, cls, des, mobile,
					System.currentTimeMillis() / 1000);
		} catch (Exception e) {
			logger.error("<addFeedback->Exception>" + e.toString());
		}
		return ires;
	}

	@Override
	public List<Map<String, Object>> getBanners(String os, String channel) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (bannerlist != null) {
			for (Map<String, Object> map : bannerlist) {
				if (os == null) {
					list.add(map);
				}
				if ("5".equals(map.get("os").toString()) && StringUtils.isEmpty(map.get("webPicUrl").toString())) {
					continue;
				}
				if (os.equals(map.get("os").toString())||"9".equals(map.get("os").toString())) {
					list.add(map);
					continue;
				}
			}
		}
		return list;
	}

	@Override
	public IosVersionModel getIosShow(String version) {
		String json =  OtherRedisService.getInstance().getIOSShow(version);
		if(StringUtils.isBlank(json)) {
			return null;
		}
		return JsonUtil.toBean(json, IosVersionModel.class);
	}

	@Override
	public WebVerModel getWebVer() {

		WebVerModel webVerModel = null;
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuWeb).getConnection();
			statement = conn.prepareStatement(SQL_GetWebVer);
			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					webVerModel = new WebVerModel().populateFromResultSet(rs);
				}
			}
		} catch (SQLException e) {
			logger.error("<getWebVer->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getWebVer->Exception>" + e.getMessage());
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
				logger.error("<getWebVer->finally->Exception>" + e2.toString());
			}
		}
		return webVerModel;
	}
}
