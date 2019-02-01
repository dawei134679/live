package com.mpig.api.dictionary.lib;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mpig.api.SqlTemplete;
import com.mpig.api.db.DataSource;
import com.mpig.api.dictionary.OpenScreenConfig;
import com.mpig.api.model.ActivityModel;
import com.mpig.api.redis.service.RedisCommService;
import com.mpig.api.utils.BaseContant;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.VarConfigUtils;

public class BaseConfigLib implements SqlTemplete{
	private static final Logger logger = Logger.getLogger(BaseConfigLib.class);
	
	private static ConcurrentHashMap<String, Object> allConfig= new ConcurrentHashMap<String, Object>();
	private static ConcurrentHashMap<String, Object> luckGiftConfig= new ConcurrentHashMap<String, Object>();
	private static ConcurrentHashMap<String, Object> probasConfig= new ConcurrentHashMap<String, Object>();

	static{
//		allConfig.put(BaseContant.CONFIG_ROOM_NOTICE, "直播提示：小猪直播提倡绿色文明直播，封面和直播内容含吸烟、低俗、引诱、暴露、挑逗等都将被屏蔽热门或封停账号，网警24小时巡视！");
		allConfig.put(BaseContant.CONFIG_XIAOZHU_NOTICE, "欢迎来到麦芽直播，祝你玩的开心");
		ActivityModel activityModel = new ActivityModel();
		activityModel.setOn(false);
		activityModel.setAct_url("http://www.xiaozhutv.com/duanwu/");
		activityModel.setActName("duanwu");
		activityModel.setExpireat(1465660800l);
		activityModel.setPic_bannner("http://7xsddj.com1.z0.glb.clouddn.com/activity/pic_banner.png");
		activityModel.setPic_btn("http://7xsddj.com1.z0.glb.clouddn.com/activity/pic_bnt.png");
		activityModel.setPic_ext("http://7xsddj.com1.z0.glb.clouddn.com/activity/pic_pay.png");
		activityModel.setPic_main("http://7xsddj.com1.z0.glb.clouddn.com/activity/pic_main.png");
		activityModel.setPosition(1);
		allConfig.put(BaseContant.CONFIG_ACTIVITY, activityModel);
	}
	
	/**
	 * 获取开屏同步至内存
	 * @return
	 */
	public static synchronized boolean updateOpenScreen() {
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		allConfig.put(BaseContant.CONFIG_OPENSCREEM_ANDROID, new JSONObject());
		allConfig.put(BaseContant.CONFIG_OPENSCREEM_IOS, new JSONObject());
		boolean bupdateOk = false;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuWeb).getConnection();
			statement = conn.prepareStatement(SQL_GetOpenScreemConfig);
			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					OpenScreenConfig openScreemConfig = new OpenScreenConfig();
					openScreemConfig.populateFromResultSet(rs);
					if(openScreemConfig.getPlatform()==1){
						allConfig.put(BaseContant.CONFIG_OPENSCREEM_ANDROID, openScreemConfig);	
					}else if(openScreemConfig.getPlatform()==2){
						allConfig.put(BaseContant.CONFIG_OPENSCREEM_IOS, openScreemConfig);	
					}else if(openScreemConfig.getPlatform()==9){
						allConfig.put(BaseContant.CONFIG_OPENSCREEM_ANDROID, openScreemConfig);
						allConfig.put(BaseContant.CONFIG_OPENSCREEM_IOS, openScreemConfig);
					}
					
				}
				logger.info("<updateOpenScreen->OK: data:>" + JSON.toJSONString(allConfig));
				bupdateOk = true;
			}
		} catch (SQLException e) {
			logger.error("<updateOpenScreen->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<updateOpenScreen->Exception>" + e.getMessage());
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

		return bupdateOk;
	}
	
	/**
	 * 获取直播公告
	 * @return
	 */
	public static synchronized boolean updateLiveNotice() {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		boolean bupdateOk = false;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuConfig).getConnection();
			statement = conn.prepareStatement(SQL_GetLiveNotice);
			rs = statement.executeQuery();
			if (rs != null) {
				if (rs.next()) {
					allConfig.put(BaseContant.CONFIG_ROOM_NOTICE, rs.getString("content")==null?"":rs.getString("content"));
				}else{
					allConfig.put(BaseContant.CONFIG_ROOM_NOTICE, "直播提示：麦芽直播提倡绿色文明直播，封面和直播内容含吸烟、低俗、引诱、暴露、挑逗等都将被屏蔽热门或封停账号，网警24小时巡视！");
				}
				logger.info("<updateLiveNotice->OK: data:>" + JSON.toJSONString(allConfig));
				bupdateOk = true;
			}
		} catch (SQLException e) {
			logger.error("<updateLiveNotice->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<updateLiveNotice->Exception>" + e.getMessage());
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

		return bupdateOk;
	}
	
	/**
	 * 获取幸运礼物
	 * @return
	 */
	public static synchronized boolean updateLuckyGift(int gid, int stime, int etime) {
		Map<String, Integer> luckGiftMap = new HashMap<String, Integer>();
		luckGiftMap.put("stime", stime);
		luckGiftMap.put("etime", etime);
		luckGiftConfig.put(gid+"", luckGiftMap);
		return true;
	}
	/**
	 * 清除幸运礼物
	 * @return
	 */
	public static synchronized boolean clearLuckyGift() {
		luckGiftConfig.clear();
		return true;
	}
	/**
	 * 获取抽奖倍率
	 * @return
	 */
	public static synchronized boolean updateProbabilitys(){
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		boolean bupdateOk = false;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuConfig).getConnection();
			statement = conn.prepareStatement(SQL_selLuckyGiftProbabilitys);
			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					Map<String, Object> luckGiftProbabilitysMap = new HashMap<String, Object>();
					luckGiftProbabilitysMap.put("multiples", Long.parseLong(rs.getString("multiples")));
					luckGiftProbabilitysMap.put("divisor", Long.parseLong(rs.getString("divisor")));
					luckGiftProbabilitysMap.put("dividend", Long.parseLong(rs.getString("dividend")));
					luckGiftProbabilitysMap.put("isRunWay", Long.parseLong(rs.getString("isRunWay")));
					luckGiftProbabilitysMap.put("maxcount", Long.parseLong(rs.getString("maxcount")));
					luckGiftProbabilitysMap.put("decoratedWord", rs.getString("decoratedWord"));
					luckGiftProbabilitysMap.put("gid", rs.getString("gid"));
					probasConfig.put(rs.getString("multiples"), luckGiftProbabilitysMap);
				}
				logger.info("<updateProbabilitys->OK: data:>" + JSON.toJSONString(probasConfig));
				bupdateOk = true;
			}
		} catch (SQLException e) {
			logger.error("<updateProbabilitys->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<updateProbabilitys->Exception>" + e.getMessage());
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
				logger.error("<updateLuckyGift->finally->Exception>" + e.getMessage());
			}
		}

		return bupdateOk;
	}
	
	public static ConcurrentHashMap<String, Object> getAllConfig(){
		
		String string = RedisCommService.getInstance().get(RedisContant.RedisNameOther, RedisContant.actMidRoom);
		if (StringUtils.isNotEmpty(string)) {
			Long lgNow = System.currentTimeMillis()/1000;
			JSONObject parseObject = JSONObject.parseObject(string);
			
			if (parseObject.getLong("stime") <= lgNow && parseObject.getLong("etime") > lgNow ) {
				allConfig.put(BaseContant.CONFIG_ACTMIDLLE, JSONObject.parse(string));
			}
		}else {
			allConfig.remove(BaseContant.CONFIG_ACTMIDLLE);
		}
		
		String actRightTopStr = RedisCommService.getInstance().get(RedisContant.RedisNameOther, RedisContant.actRightTopRoom);
		if (StringUtils.isNotEmpty(actRightTopStr)) {
			Long lgNow = System.currentTimeMillis()/1000;
			JSONObject parseObject = JSONObject.parseObject(actRightTopStr);
			if (parseObject.getLong("stime") <= lgNow && parseObject.getLong("etime") > lgNow ) {
				allConfig.put(BaseContant.CONFIG_ACTRIGHTTOP, JSONObject.parse(actRightTopStr));
			}
		}else {
			allConfig.remove(BaseContant.CONFIG_ACTRIGHTTOP);
		}
		
		return allConfig;
	}
	
	public static ConcurrentHashMap<String, Object> getLuckyGiftConfig(){
		return luckGiftConfig;
	}
	
	public static ConcurrentHashMap<String, Object> getprobasConfig(){
		return probasConfig;
	}
}
