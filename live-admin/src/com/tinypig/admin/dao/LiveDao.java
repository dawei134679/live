package com.tinypig.admin.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.tinypig.admin.constant;
import com.tinypig.admin.model.LiveDetail;
import com.tinypig.admin.redis.service.OtherRedisService;
import com.tinypig.admin.util.Constant;
import com.tinypig.admin.util.DateUtil;
import com.tinypig.admin.util.DbUtil;
import com.tinypig.admin.util.StringUtil;

/**
 * Created by fangwuqing on 16/5/9.
 */
public class LiveDao extends BaseDao {

	private final static LiveDao instance = new LiveDao();

	public static LiveDao getInstance() {
		return instance;
	}

	/**
	 * 获取开播列表
	 * 
	 * @param recommend
	 * @return
	 */
	public Set<String> getLivingList(int recommend) {
		Set<String> set = new HashSet<String>();
		if (recommend == 0) {
			set = OtherRedisService.getInstance().getBaseRoom(0);
		} else if (recommend == 1) {
			set = OtherRedisService.getInstance().getHotRoom(0);
		} else if (recommend == 2) {
			set = OtherRedisService.getInstance().getRecommendRoom(0);
		} else if (recommend == 3){
			set = OtherRedisService.getInstance().getHeadRoom(0);
		}else {
			// 全部
			Set<String> _set = null;
			_set = OtherRedisService.getInstance().getRecommendRoom(0);
			for (String string : _set) {
				set.add(string);
			}
			_set = OtherRedisService.getInstance().getHotRoom(0);
			for (String string : _set) {
				set.add(string);
			}
			_set = OtherRedisService.getInstance().getBaseRoom(0);
			for (String string : _set) {
				set.add(string);
			}
			_set = OtherRedisService.getInstance().getHeadRoom(0);
			for (String string : _set) {
				set.add(string);
			}
		}
		return set;
	}

	/**
	 * 关闭房间
	 * 
	 * @param uid
	 * @return
	 */
	public Map<String, Object> closeRoom(int uid, int adminid) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			HttpResponse<JsonNode> result = Unirest.get(Constant.business_server_url_shark1 + "/admin/closelive?uid=" + uid)
					.asJson();
			if (result != null) {
				JSONObject object = result.getBody().getObject();
				if (object.getInt("code") != 200) {
					map.put("code", 201);
					map.put("msg", "201"+object.getString("message"));
				} else {
					map.put("code", 200);
					map.put("msg", "设置成功");

					this.AddOperationLog("live_mic_time", String.valueOf(uid), "关闭用户身份", 2, "开播中", "关闭", adminid);
				}
			} else {
				System.out.println("closeRoom->admin/closelive＝＝＝>失败");
				map.put("code", 202);
				map.put("msg", "202关闭直播失败");
			}
		} catch (Exception e) {
			map.put("code", 203);
			map.put("msg", "203 关闭直播失败" + e.getMessage());
			System.out.println("closeRoom－>exception:"+e.getMessage());
		}
		return map;
	}
	
	/**
	 * 获取主播有效开播总时长
	 * @param uid 主播UID
	 * @return 直播总时长
	 */
	public int getTotalLiveTimeByUid(int uid, String startDate, String endDate){
		int totalLiveTime = 0;
		Long startSec = 0L;
		Long endSec = 0L;
		if(!StringUtil.isEmpty(startDate)){
			startDate+=" 00:00:00";
			try {
				startSec = DateUtil.dateToLong(startDate, "yyyy-MM-dd HH:mm:ss", "other", 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			startSec = DateUtil.truncateToMonth(new Date()).getTime()/1000;
		}
		
		if(!StringUtil.isEmpty(endDate)){
			endDate+=" 23:59:59";
			try {
				endSec = DateUtil.dateToLong(endDate, "yyyy-MM-dd HH:mm:ss", "other", 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			endSec = System.currentTimeMillis()/1000;
		}
		//String sql = "select sum(liveTime) from (select (endtime-starttime) as liveTime,uid from live_mic_time where ((os != 'android' && starttime >= unix_timestamp('2016-12-14')) || starttime < unix_timestamp('2016-12-14')) and endtime>0 and uid="+uid+" and addtime>="+ startSec;
		String sql = "select sum(liveTime) from (select (endtime-starttime) as liveTime,uid from live_mic_time where (( starttime >= unix_timestamp('2016-12-14')) || starttime < unix_timestamp('2016-12-14')) and endtime>0 and uid="+uid+" and addtime>="+ startSec;
		if(endSec > 0){
			sql += " and addtime <="+endSec;
		}
		sql += ") as t where liveTime>" + constant.VALID_LIVE_TIME;
		DbUtil.DbSimpleQuery dbq = new DbUtil.DbSimpleQuery();
		ResultSet rs =  dbq.simpleQuery(constant.db_zhu_live, sql);
		if(null != rs){
			try {
				while(rs.next()){
					totalLiveTime = rs.getInt(1);
					break;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		dbq.release();
		return totalLiveTime;
	}
	
	/**
	 * 获取主播有效直播天数
	 * @param uid 主播UID
	 * @return 有效开播天数
	 */
	public int getValidLiveDay(int uid,String startDate,String endDate){
		HashMap<String, Integer> dayTimeMap = new HashMap<String, Integer>();
		int startSec = 0;
		int endSec = 0;
		if(!StringUtil.isEmpty(startDate)){
			startDate+=" 00:00:00";
			try {
				startSec = DateUtil.getTimeStamp(DateUtil.formatString(startDate, "yyyy-MM-dd HH:mm:ss"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(!StringUtil.isEmpty(endDate)){
			endDate+=" 23:59:59";
			try {
				endSec = DateUtil.getTimeStamp(DateUtil.formatString(endDate, "yyyy-MM-dd HH:mm:ss"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		int validLiveDay = 0;
		//String sql = "select * from (select (endtime-starttime) as liveTime,addtime from live_mic_time where ((os != 'android' && starttime >= unix_timestamp('2016-12-14')) || starttime < unix_timestamp('2016-12-14'))  and uid="+uid+" and endtime>0 and addtime>="+startSec;
		String sql = "select * from (select (endtime-starttime) as liveTime,addtime from live_mic_time where (( starttime >= unix_timestamp('2016-12-14')) || starttime < unix_timestamp('2016-12-14'))  and uid="+uid+" and endtime>0 and addtime>="+startSec;
		if(endSec > 0){
			sql += " and addtime <="+endSec;
		}
		sql+=") as t where liveTime>" + constant.VALID_LIVE_TIME;
		DbUtil.DbSimpleQuery dbq = new DbUtil.DbSimpleQuery();
		ResultSet rs =  dbq.simpleQuery(constant.db_zhu_live, sql);
		if(null != rs){
			try {
				while(rs.next()){
					int liveTime = rs.getInt(1);
					long addTime = rs.getLong(2);
					String date = DateUtil.formatDate(new Date(addTime*1000), "yyyy-MM-dd");
					if(dayTimeMap.containsKey(date)){
						int time = dayTimeMap.get(date);
						dayTimeMap.put(date, time+liveTime);
					}else{
						dayTimeMap.put(date, liveTime);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Iterator<String> days = dayTimeMap.keySet().iterator();
			while(days.hasNext()){
				String day = days.next();
				int liveTime = dayTimeMap.get(day);
				if(liveTime >= constant.VALID_LIVE_DAY){
					validLiveDay++;
				}
			}
		}
		dbq.release();
		return validLiveDay;
	}
	
	public List<LiveDetail> getLiveDetailByUid(int uid,String startDate,String endDate){
		List<LiveDetail> list = new ArrayList<LiveDetail>();
		int startSec = 0;
		int endSec = 0;
		if(!StringUtil.isEmpty(startDate)){
			startDate+=" 00:00:00";
			try {
				startSec = DateUtil.getTimeStamp(DateUtil.formatString(startDate, "yyyy-MM-dd HH:mm:ss"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(!StringUtil.isEmpty(endDate)){
			endDate+=" 23:59:59";
			try {
				endSec = DateUtil.getTimeStamp(DateUtil.formatString(endDate, "yyyy-MM-dd HH:mm:ss"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//String sql = "select * from (select (endtime-starttime) as liveTime,addtime,starttime,endtime,uid,os from live_mic_time where ((os != 'android' && starttime >= unix_timestamp('2016-12-14')) || starttime < unix_timestamp('2016-12-14'))  and endtime>0 and uid="+uid+" and addtime>="+ startSec;
		String sql = "select * from (select (endtime-starttime) as liveTime,addtime,starttime,endtime,uid,os from live_mic_time where (( starttime >= unix_timestamp('2016-12-14')) || starttime < unix_timestamp('2016-12-14'))  and endtime>0 and uid="+uid+" and addtime>="+ startSec;
		if(endSec > 0){
			sql += " and addtime <="+endSec;
		}
		sql += ") as t where liveTime>" + constant.VALID_LIVE_TIME + " order by addtime desc";

		DbUtil.DbSimpleQuery dbq = new DbUtil.DbSimpleQuery();
		ResultSet rs =  dbq.simpleQuery(constant.db_zhu_live, sql);
		if(null != rs){
			try {
				while(rs.next()){
					LiveDetail ld = new LiveDetail();
					ld.setOs(rs.getString("os"));
					ld.setLiveSec(rs.getInt(1));
					ld.setDate(DateUtil.formatDate(new Date(rs.getInt(2)*1000), "yyyy-MM-dd"));
					ld.setLiveStartTime(rs.getInt(3));
					ld.setLiveEndTime(rs.getInt(4));
					list.add(ld);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		dbq.release();
		return list;
	}
	
}
