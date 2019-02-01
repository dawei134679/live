package com.tinypig.admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.constant;
import com.tinypig.admin.model.UserBaseInfoModel;
import com.tinypig.admin.redis.service.RedisOperat;
import com.tinypig.admin.util.DbUtil;
import com.tinypig.admin.util.RedisContant;

public class TimerTaskDao {

	private final static TimerTaskDao instance = new TimerTaskDao();
	
	public static TimerTaskDao getInstance(){
		return instance;
	}
	
	/**
	 * 获取新增设备激活数
	 * @param stime 开始时间
	 * @param etime 结束时间
	 */
	public Integer getEquipmentsNew(Integer os,Long stime,Long etime,String channel){
		Integer inum = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "select count(*) as cnts from zhu_equipment where activationTime>=? and activationTime<? and isType=1 and os=? and channel=?";

			con = DbUtil.instance().getCon(constant.db_zhu_admin,"slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, stime);
			pstmt.setLong(2, etime);
			pstmt.setInt(3, os);
			pstmt.setString(4, channel);
			
			rs = pstmt.executeQuery();
			if (rs != null) {
				while(rs.next()){
					inum = rs.getInt("cnts");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return inum;
	}
	
	/**
	 * 获取注册数
	 * @param stime
	 * @param etime
	 * @return
	 */
	public Integer getRegisters(Integer os,Long stime,Long etime,String channelName){
		Integer inum = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "select count(*) as cnts from user_login_detail where loginTime>=? and loginTime<? and isType=1 and os=? and channel!='16wifi' and channel=?";

			con = DbUtil.instance().getCon(constant.db_zhu_admin,"slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, stime);
			pstmt.setLong(2, etime);
			pstmt.setInt(3, os);
			pstmt.setString(4,channelName);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while(rs.next()){
					inum = rs.getInt("cnts");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return inum;
	}
	
	/**
	 * 获取注册数
	 * @param stime
	 * @param etime
	 * @return
	 */
	public List<Map<String, Object>> getRegisters(Long stime,Long etime){
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "select os,channel, count(*) as cnts from user_login_detail where loginTime>=? and loginTime<? and isType=1 group by os,channel";

			con = DbUtil.instance().getCon(constant.db_zhu_admin,"slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, stime);
			pstmt.setLong(2, etime);
			
			rs = pstmt.executeQuery();
			if (rs != null) {
				while(rs.next()){
					map = new HashMap<String, Object>();
					map.put("os", rs.getInt("os"));
					map.put("channel", rs.getString("channel"));
					map.put("cnts", rs.getInt("cnts"));
					list.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	/**
	 * 获取登录数
	 * @param stime
	 * @param etime
	 * @return
	 */
	public List<Integer> getLogins(Long stime,Long etime){
		
		List<Integer> list = new ArrayList<Integer>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "select distinct uid from user_login_detail where loginTime>=? and loginTime<? and isType=2 ";

			con = DbUtil.instance().getCon(constant.db_zhu_admin,"slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, stime);
			pstmt.setLong(2, etime);
			
			rs = pstmt.executeQuery();
			if (rs != null) {
				while(rs.next()){
					list.add(rs.getInt("uid"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	/**
	 * 获取登录数
	 * @param stime
	 * @param etime
	 * @return
	 */
	public Map<String, Object> getRegisterByUidAndTime(Long stime,Long etime,Integer uid){
		
		Map<String, Object> map = null;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "select os,channel from user_login_detail where loginTime>=? and loginTime<? and isType=1 and uid=?";

			con = DbUtil.instance().getCon(constant.db_zhu_admin,"slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, stime);
			pstmt.setLong(2, etime);
			pstmt.setInt(3, uid);
			
			rs = pstmt.executeQuery();
			if (rs != null) {
				while(rs.next()){
					map = new HashMap<String, Object>();
					map.put("os", rs.getInt("os"));
					map.put("channel", rs.getString("channel"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
	
	/**
	 * 获取登录数
	 * @param stime
	 * @param etime
	 * @return
	 */
	public Map<String, Object> getLoginerByUidAndTime(Long stime,Long etime,Integer uid){
		
		Map<String, Object> map = null;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "select os,channel from user_login_detail where loginTime>=? and loginTime<? and isType=1 and uid=?";

			con = DbUtil.instance().getCon(constant.db_zhu_admin,"slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, stime);
			pstmt.setLong(2, etime);
			pstmt.setInt(3, uid);
			
			rs = pstmt.executeQuery();
			if (rs != null) {
				while(rs.next()){
					map = new HashMap<String, Object>();
					map.put("os", rs.getInt("os"));
					map.put("channel", rs.getString("channel"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
	/**
	 * tian
	 * @param times
	 * @param os
	 * @param channel
	 * @param type
	 * @return
	 */
	public int updRemain(Long times,Integer os,String channel,int type,int category){

		Connection con = null;
		PreparedStatement pstmt = null;
		int result = 0;
		try {
			String sql = "update analysis_remain set remain"+type+"=remain"+type+"+1,category="+category+" where times=? and os=? and channel=?";

			con = DbUtil.instance().getCon(constant.db_zhu_analysis,"master");
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, times);
			pstmt.setInt(2, os);
			pstmt.setString(3, channel);
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 添加新增用户数
	 * @param times
	 * @param list
	 */
	public void addRemain(Long times,List<Map<String, Object>> list){
		Connection con = null;
		PreparedStatement pstmt = null;
		
		Long lgNow = System.currentTimeMillis()/1000;
		
		try {
			String sql = "insert into analysis_remain(times,os,channel,newUser,addtime)value(?,?,?,?,?)";

			con = DbUtil.instance().getCon(constant.db_zhu_analysis,"master");
			pstmt = con.prepareStatement(sql);
			for(Map<String, Object> map:list){
				pstmt.setLong(1, times);
				pstmt.setInt(2, (Integer) map.get("os"));
				pstmt.setString(3, (String) map.get("channel"));
				pstmt.setInt(4, (Integer) map.get("cnts"));
				pstmt.setLong(5, lgNow);
				pstmt.addBatch();
			}
			if (list.size() > 0) {
				pstmt.executeBatch();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 活跃设备数 排重
	 * @param stime
	 * @param etime
	 * @return
	 */
	public Integer getEquipments(Integer os, Long stime,Long etime,String channelName){

		Integer inum = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "select count(distinct mac,imei) as cnts from zhu_equipment where activationTime>=? and activationTime<? and os=? and channel=?";

			con = DbUtil.instance().getCon(constant.db_zhu_admin,"slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, stime);
			pstmt.setLong(2, etime);
			pstmt.setInt(3, os);
			pstmt.setString(4, channelName);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while(rs.next()){
					inum = rs.getInt("cnts");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return inum;
	}
	
	/**
	 * 获取充值金额总数及充值人数
	 * @param stime
	 * @param etime
	 * @return
	 */
	public Map<String, Object> getPaySummary(Long stime,Long etime,String channelName){
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Integer> map1 = new HashMap<String, Integer>();
		Map<String, Integer> map2 = new HashMap<String, Integer>();
		Map<String, Integer> map3 = new HashMap<String, Integer>();
		Map<String, Integer> map4 = new HashMap<String, Integer>();
		Map<String, Integer> map5 = new HashMap<String, Integer>();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "select sum(amount) amounts,srcuid from pay_order where status=2 and srcuid not in(10000002,10000008,10000082) and paytime>=? and paytime<? and userSource=? group by srcuid";

			con = DbUtil.instance().getCon(constant.db_zhu_pay,"slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, stime);
			pstmt.setLong(2, etime);
			pstmt.setString(3, channelName);
			rs = pstmt.executeQuery();
			if (rs != null) {

				while(rs.next()){
					UserBaseInfoModel userBaseInfo = UserDao.getInstance().getUserBaseInfo(rs.getInt("srcuid"),false);
					if (userBaseInfo != null) {
						
						if (userBaseInfo.getRegistos() == 1) {
							// 安卓
							if (map1.containsKey("amount")) {
								map1.put("amount", map1.get("amount")+rs.getInt("amounts"));
							}else {
								map1.put("amount", rs.getInt("amounts"));
							}

							if (map1.containsKey("uid")) {
								map1.put("uid", map1.get("uid")+1);
							}else {
								map1.put("uid", 1);
							}
						}else if (userBaseInfo.getRegistos() == 2) {
							// 苹果
							if (map2.containsKey("amount")) {
								map2.put("amount", map2.get("amount")+rs.getInt("amounts"));
							}else {
								map2.put("amount", rs.getInt("amounts"));
							}

							if (map2.containsKey("uid")) {
								map2.put("uid", map2.get("uid")+1);
							}else {
								map2.put("uid", 1);
							}
						}else if (userBaseInfo.getRegistos() == 3) {
							// H5
							if (map3.containsKey("amount")) {
								map3.put("amount", map3.get("amount")+rs.getInt("amounts"));
							}else {
								map3.put("amount", rs.getInt("amounts"));
							}

							if (map3.containsKey("uid")) {
								map3.put("uid", map3.get("uid")+1);
							}else {
								map3.put("uid", 1);
							}
						}else if (userBaseInfo.getRegistos() == 4) {
							// ipad
							if (map4.containsKey("amount")) {
								map4.put("amount", map4.get("amount")+rs.getInt("amounts"));
							}else {
								map4.put("amount", rs.getInt("amounts"));
							}

							if (map4.containsKey("uid")) {
								map4.put("uid", map4.get("uid")+1);
							}else {
								map4.put("uid", 1);
							}
						}else if (userBaseInfo.getRegistos() == 5) {
							// web
							if (map5.containsKey("amount")) {
								map5.put("amount", map5.get("amount")+rs.getInt("amounts"));
							}else {
								map5.put("amount", rs.getInt("amounts"));
							}

							if (map5.containsKey("uid")) {
								map5.put("uid", map5.get("uid")+1);
							}else {
								map5.put("uid", 1);
							}
						}
					}
				}
				if (map1.size() > 0) {
					map.put("os1", map1);
				}
				if (map2.size() > 0) {
					map.put("os2", map2);
				}
				if (map3.size() > 0) {
					map.put("os3", map3);
				}
				if (map4.size() > 0) {
					map.put("os4", map4);
				}
				if (map5.size() > 0) {
					map.put("os5", map5);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
	/**
	 * 获取当日注册用户充值金额总数及充值人数
	 * @param stime
	 * @param etime
	 * @return
	 */
	public Map<String, Object> getRegistPaySummary(Long stime,Long etime,String channelName){
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Integer> map1 = new HashMap<String, Integer>();
		Map<String, Integer> map2 = new HashMap<String, Integer>();
		Map<String, Integer> map3 = new HashMap<String, Integer>();
		Map<String, Integer> map4 = new HashMap<String, Integer>();
		Map<String, Integer> map5 = new HashMap<String, Integer>();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "select sum(amount) amounts,srcuid from pay_order where status=2 and srcuid not in(10000002,10000008,10000082) and paytime>=? and paytime<? and userSource=? and registtime < ? and registtime>=? group by srcuid";

			con = DbUtil.instance().getCon(constant.db_zhu_pay,"slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, stime);
			pstmt.setLong(2, etime);
			pstmt.setString(3, channelName);
			pstmt.setLong(4, etime);
			pstmt.setLong(5, stime);
			rs = pstmt.executeQuery();
			if (rs != null) {

				while(rs.next()){
					UserBaseInfoModel userBaseInfo = UserDao.getInstance().getUserBaseInfo(rs.getInt("srcuid"),false);
					if (userBaseInfo != null) {
						
						if (userBaseInfo.getRegistos() == 1) {
							// 安卓
							if (map1.containsKey("amount")) {
								map1.put("amount", map1.get("amount")+rs.getInt("amounts"));
							}else {
								map1.put("amount", rs.getInt("amounts"));
							}

							if (map1.containsKey("uid")) {
								map1.put("uid", map1.get("uid")+1);
							}else {
								map1.put("uid", 1);
							}
						}else if (userBaseInfo.getRegistos() == 2) {
							// 苹果
							if (map2.containsKey("amount")) {
								map2.put("amount", map2.get("amount")+rs.getInt("amounts"));
							}else {
								map2.put("amount", rs.getInt("amounts"));
							}

							if (map2.containsKey("uid")) {
								map2.put("uid", map2.get("uid")+1);
							}else {
								map2.put("uid", 1);
							}
						}else if (userBaseInfo.getRegistos() == 3) {
							// H5
							if (map3.containsKey("amount")) {
								map3.put("amount", map3.get("amount")+rs.getInt("amounts"));
							}else {
								map3.put("amount", rs.getInt("amounts"));
							}

							if (map3.containsKey("uid")) {
								map3.put("uid", map3.get("uid")+1);
							}else {
								map3.put("uid", 1);
							}
						}else if (userBaseInfo.getRegistos() == 4) {
							// ipad
							if (map4.containsKey("amount")) {
								map4.put("amount", map4.get("amount")+rs.getInt("amounts"));
							}else {
								map4.put("amount", rs.getInt("amounts"));
							}

							if (map4.containsKey("uid")) {
								map4.put("uid", map4.get("uid")+1);
							}else {
								map4.put("uid", 1);
							}
						}else if (userBaseInfo.getRegistos() == 5) {
							// web
							if (map5.containsKey("amount")) {
								map5.put("amount", map5.get("amount")+rs.getInt("amounts"));
							}else {
								map5.put("amount", rs.getInt("amounts"));
							}

							if (map5.containsKey("uid")) {
								map5.put("uid", map5.get("uid")+1);
							}else {
								map5.put("uid", 1);
							}
						}
					}
				}
				if (map1.size() > 0) {
					map.put("os1", map1);
				}
				if (map2.size() > 0) {
					map.put("os2", map2);
				}
				if (map3.size() > 0) {
					map.put("os3", map3);
				}
				if (map4.size() > 0) {
					map.put("os4", map4);
				}
				if (map5.size() > 0) {
					map.put("os5", map5);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
	/**
	 * 添加注册、设备激活等静态数据
	 * @param times
	 * @param equipmentsNew
	 * @param registers
	 * @param equipments
	 * @param actives
	 * @param amounts
	 * @param payUsers
	 * @param addtime
	 */
	public void addAnalysisSummary(Integer os,Long times,int equipmentsNew,int registers,int equipments,int actives,double amounts,int payUsers,Long addtime,String channelName,int registAccounts,int registPayUsers){
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			String sql = "insert into analysis_summary(times,equipmentsNew,registers,equipments,actives,amounts,payUsers,addtime,os,channelName,registAccounts,registPayUsers) value (?,?,?,?,?,?,?,?,?,?,?,?)";

			con = DbUtil.instance().getCon(constant.db_zhu_analysis,"master");
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, times);
			pstmt.setInt(2, equipmentsNew);
			pstmt.setInt(3, registers);
			pstmt.setInt(4, equipments);
			pstmt.setInt(5, actives);
			pstmt.setDouble(6, amounts);
			pstmt.setInt(7, payUsers);
			pstmt.setLong(8, addtime);
			pstmt.setInt(9, os);
			pstmt.setString(10, channelName);
			pstmt.setInt(11, registAccounts);
			pstmt.setInt(12, registPayUsers);
			int result = pstmt.executeUpdate();
			if (result <= 0) {
				System.out.println("addAnalysisSummary:"+pstmt.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取有效天 和 有效时长
	 * @param stime
	 * @param etime
	 * @return
	 */
	public void getValidDateTime(Long stime,Long etime, int times){
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// 开播有效天及有效时长
			String sql = "select uid,sum(endtime-starttime) as lgtimes from zhu_live.live_mic_time where starttime >= ? and starttime < ? and type=1 and (endtime-starttime) > 300 group by uid";
			con = DbUtil.instance().getCon(constant.db_zhu_live,"slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, stime);
			pstmt.setLong(2, etime);
			
			rs = pstmt.executeQuery();
			if (rs != null) {
				Long lgNow = System.currentTimeMillis()/1000;
				while(rs.next()){
					this.addValidDateTime(rs.getInt("uid"), rs.getInt("lgtimes"), rs.getInt("lgtimes") >= 5400? 1:0, times,lgNow);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * 添加 主播的有效天和有效时长
	 * @param uid
	 * @param validTime
	 * @param validDay
	 * @param times
	 */
	public void addValidDateTime(int uid,int validTime,int validDay,int times,Long lgNow){
		Connection con = null;
		Statement pstmt = null;
		
		ResultSet rs = null;
		
		int result = 0;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_analysis,"master");
			pstmt = con.createStatement();
			String sql = "select id from zhu_analysis.analysis_wages where times="+times+" and anchorid="+uid;
			rs = pstmt.executeQuery(sql);
			
			if (rs != null && rs.next()) {
				result = pstmt.executeUpdate("update analysis_wages set validday=validday+" + validDay + ", airtime=airtime+" + validTime + " where anchorid=" + uid + " and times=" + times);
			}else {
				result = pstmt.executeUpdate("insert into analysis_wages(times,anchorid,validday,airtime,addtime)value("+times+","+uid+","+validDay+","+validTime+","+lgNow+")");
			}
			if (result == 0) {
				System.out.println("addValidDateTime result:"+result+", uid:"+uid+", times:"+times+", validday:"+validDay+", validTime:"+validTime);
			}
		} catch (Exception e) {
			System.out.println("addValidDateTime excep result:"+result+", uid:"+uid+", times:"+times+", validday:"+validDay+", validTime:"+validTime);
			e.printStackTrace();
		}finally{
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取 主播收到的声援值
	 * @param stime
	 * @param etime
	 * @param times
	 */
	public void getCredit(Long stime,Long etime, int times){
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// 主播获取的声援值
			String sql = "select a.dstuid,sum(a.count*b.credit) as credis from bill_" + times + " a,zhu_config.config_giftlist b where a.gid=b.gid and b.credit>0 and a.addtime>=? and a.addtime<? group by a.dstuid";
			con = DbUtil.instance().getCon(constant.db_zhu_bill,"slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, stime);
			pstmt.setLong(2, etime);
			
			rs = pstmt.executeQuery();
			if (rs != null) {
				Long lgNow = System.currentTimeMillis()/1000;
				while(rs.next()){
					this.addCredit(rs.getInt("dstuid"), rs.getInt("credis"), times,lgNow);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 添加声援值
	 * @param uid
	 * @param credits
	 * @param times
	 */
	public void addCredit(int uid,int credits,int times,Long lgNow){
		Connection con = null;
		Statement pstmt = null;
		
		ResultSet rs = null;
		
		int result = 0;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_analysis,"master");
			pstmt = con.createStatement();
			String sql = "select id from zhu_analysis.analysis_wages where times="+times+" and anchorid="+uid;
			rs = pstmt.executeQuery(sql);
			
			if (rs != null && rs.next()) {
				result = pstmt.executeUpdate("update analysis_wages set credits=credits+" + credits + " where anchorid=" + uid + " and times=" + times);
			}else {
				result = pstmt.executeUpdate("insert into analysis_wages(times,anchorid,credits,addtime)value("+times+","+uid+","+credits+","+lgNow+")");
			}
			if (result == 0) {
				System.out.println("addCredit result:"+result+", uid:"+uid+", times:"+times+", credits:"+credits);
			}
		} catch (Exception e) {
			System.out.println("addCredit excep result:"+result+", uid:"+uid+", times:"+times+", credits:"+credits);
			e.printStackTrace();
		}finally{
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取 主播获得周星奖励
	 * @param stime
	 * @param etime
	 * @param times
	 */
	public void getWeekStar(Long stime,Long etime, int times){
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// 主播获取的声援值
			String sql = "select uid,sum(amount) as credis from pay_week_star where usertype=1 and addtime >= ? and addtime < ? group by uid";
			con = DbUtil.instance().getCon(constant.db_zhu_pay,"slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, stime);
			pstmt.setLong(2, etime);
			
			rs = pstmt.executeQuery();
			if (rs != null) {
				Long lgNow = System.currentTimeMillis()/1000;
				while(rs.next()){
					this.addWeekStar(rs.getInt("uid"), rs.getInt("credis"), times,lgNow);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * 添加周星奖励数
	 * @param uid
	 * @param weekstar
	 * @param times
	 */
	public void addWeekStar(int uid,int weekstar,int times,Long lgNow){
		Connection con = null;
		Statement pstmt = null;
		
		ResultSet rs = null;
		
		int result = 0;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_analysis,"master");
			pstmt = con.createStatement();
			String sql = "select id from zhu_analysis.analysis_wages where times="+times+" and anchorid="+uid;
			rs = pstmt.executeQuery(sql);
			
			if (rs != null && rs.next()) {
				result = pstmt.executeUpdate("update analysis_wages set weekstar=weekstar+" + weekstar + " where anchorid=" + uid + " and times=" + times);
			}else {
				result = pstmt.executeUpdate("insert into analysis_wages(times,anchorid,weekstar,addtime)value("+times+","+uid+","+weekstar+","+lgNow+")");
			}
			if (result == 0) {
				System.out.println("addWeekStar result:"+result+", uid:"+uid+", times:"+times+", weekstar:"+weekstar);
			}
		} catch (Exception e) {
			System.out.println("addWeekStar excep result:"+result+", uid:"+uid+", times:"+times+", weekstar:"+weekstar);
			e.printStackTrace();
		}finally{
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取活动产生的声援值
	 * @param stime
	 * @param etime
	 * @param times
	 */
	public void getActivity(Long stime,Long etime, int times){
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// 主播获取的声援值
			String sql = "select uid,sum(credit) as credis from pay_activity where createAt >= ? and createAt < ? group by uid";
			con = DbUtil.instance().getCon(constant.db_zhu_pay,"slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, stime);
			pstmt.setLong(2, etime);
			
			rs = pstmt.executeQuery();
			if (rs != null) {
				Long lgNow = System.currentTimeMillis()/1000;
				while(rs.next()){
					this.addActivity(rs.getInt("uid"), rs.getInt("credis"), times,lgNow);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * 添加活动获得的奖励
	 * @param uid
	 * @param activity
	 * @param times
	 */
	public void addActivity(int uid,int activity,int times,Long lgNow){
		Connection con = null;
		Statement pstmt = null;
		
		ResultSet rs = null;
		
		int result = 0;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_analysis,"master");
			pstmt = con.createStatement();
			String sql = "select id from zhu_analysis.analysis_wages where times="+times+" and anchorid="+uid;
			rs = pstmt.executeQuery(sql);
			
			if (rs != null && rs.next()) {
				result = pstmt.executeUpdate("update analysis_wages set activity=activity+" + activity + " where anchorid=" + uid + " and times=" + times);
			}else {
				result = pstmt.executeUpdate("insert into analysis_wages(times,anchorid,activity,addtime)value("+times+","+uid+","+activity+","+lgNow+")");
			}
			if (result == 0) {
				System.out.println("addActivity result:"+result+", uid:"+uid+", times:"+times+", activity:"+activity);
			}
		} catch (Exception e) {
			System.out.println("addActivity excep result:"+result+", uid:"+uid+", times:"+times+", activity:"+activity);
			e.printStackTrace();
		}finally{
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取 内兑数据
	 * @param stime
	 * @param etime
	 * @param times
	 */
	public void getExchange(Long stime,Long etime, int times){
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// 主播获取的声援值
			String sql = "select uid,sum(credit) as credis from pay_exchange where createAT >= ? and createAT < ? group by uid";
			con = DbUtil.instance().getCon(constant.db_zhu_pay,"slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, stime);
			pstmt.setLong(2, etime);
			
			rs = pstmt.executeQuery();
			if (rs != null) {
				Long lgNow = System.currentTimeMillis()/1000;
				while(rs.next()){
					this.addExchange(rs.getInt("uid"), rs.getInt("credis"), times,lgNow);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
		
	/**
	 * 添加内兑数据
	 * @param uid
	 * @param exchange
	 * @param times
	 */
	public void addExchange(int uid,int exchange,int times,Long lgNow){
		Connection con = null;
		Statement pstmt = null;
		
		ResultSet rs = null;
		
		int result = 0;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_analysis,"master");
			pstmt = con.createStatement();
			String sql = "select id from zhu_analysis.analysis_wages where times="+times+" and anchorid="+uid;
			rs = pstmt.executeQuery(sql);
			
			if (rs != null && rs.next()) {
				result = pstmt.executeUpdate("update analysis_wages set exchange=exchange+" + exchange + " where anchorid=" + uid + " and times=" + times);
			}else {
				result = pstmt.executeUpdate("insert into analysis_wages(times,anchorid,exchange,addtime)value("+times+","+uid+","+exchange+","+lgNow+")");
			}
			if (result == 0) {
				System.out.println("addExchange result:"+result+", uid:"+uid+", times:"+times+", exchange:"+exchange);
			}
		} catch (Exception e) {
			System.out.println("addExchange excep result:"+result+", uid:"+uid+", times:"+times+", exchange:"+exchange);
			e.printStackTrace();
		}finally{
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public void getWithdraw(Long stime,Long etime, int times){
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// 主播提现消耗的声援值
			String sql = "select uid,sum(credit) as credis from pay_withdraw where isSecc=1 and sendTime >= ? and sendTime < ? group by uid";
			con = DbUtil.instance().getCon(constant.db_zhu_pay,"slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, stime);
			pstmt.setLong(2, etime);
			
			rs = pstmt.executeQuery();
			if (rs != null) {
				Long lgNow = System.currentTimeMillis()/1000;
				while(rs.next()){
					this.addWithdraw(rs.getInt("uid"), rs.getInt("credis"), times,lgNow);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 添加提现记录
	 * @param uid
	 * @param withdraw
	 * @param times
	 */
	public void addWithdraw(int uid,int withdraw,int times,Long lgNow){
		Connection con = null;
		Statement pstmt = null;
		
		ResultSet rs = null;
		
		int result = 0;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_analysis,"master");
			pstmt = con.createStatement();
			String sql = "select id from zhu_analysis.analysis_wages where times="+times+" and anchorid="+uid;
			rs = pstmt.executeQuery(sql);
			
			if (rs != null && rs.next()) {
				result = pstmt.executeUpdate("update analysis_wages set withdraw=withdraw+" + withdraw + " where anchorid=" + uid + " and times=" + times);
			}else {
				result = pstmt.executeUpdate("insert into analysis_wages(times,anchorid,withdraw,addtime)value("+times+","+uid+","+withdraw+","+lgNow+")");
			}
			if (result == 0) {
				System.out.println("addWithdraw result:"+result+", uid:"+uid+", times:"+times+", withdraw:"+withdraw);
			}
		} catch (Exception e) {
			System.out.println("addWithdraw excep result:"+result+", uid:"+uid+", times:"+times+", withdraw:"+withdraw);
			e.printStackTrace();
		}finally{
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * 礼物消耗数据的静态化
	 * @param stime
	 * @param etime
	 * @param YM
	 */
	public void getGiftSends(Long stime,Long etime,String YM){
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = " select from_unixtime(a.addtime,'%Y%m%d') as times,a.gid,b.gname,sum(a.count) as counts,sum(a.count*b.gprice) as amount "
					+ " from bill_"+YM+" a,zhu_config.config_giftlist b "
							+ " where a.gid=b.gid and a.addtime>="+stime+" and a.addtime<"+etime+" "
									+ " group by times,a.gid ";
			con = DbUtil.instance().getCon(constant.db_zhu_bill,"slave");
			pstmt = con.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			if (rs != null) {
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				Map<String, Object> map = null;
				while(rs.next()){
					map = new HashMap<String, Object>();
					map.put("times", rs.getObject("times"));
					map.put("gid", rs.getInt("gid"));
					map.put("gname", rs.getString("gname"));
					map.put("counts", rs.getInt("counts"));
					map.put("amount", rs.getInt("amount"));
					list.add(map);
				}
				if (list.size() > 0) {
					int addGiftSends = this.addGiftSends(list);
					if (addGiftSends <= 0) {
						System.out.println("getGiftSends add err, stime:" + stime + " etime:" + etime + " YM:" + YM);
					}
				}
			}
			
		} catch (Exception e) {
			System.out.println("getGiftSends add err, stime:" + stime + " etime:" + etime + " YM:" + YM);
			System.out.println("getGiftSends excep:" + e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
	}
	
	public int addGiftSends(List<Map<String, Object>> list){
		
		if (list == null || list.size() == 0) {
			return 0;
		}
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		Long lgNow = System.currentTimeMillis()/1000;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_analysis,"master");
			con.setAutoCommit(false);
			String sql = "insert into analysis_gift_consume(times,gid,gname,counts,amount,addtime)values(?,?,?,?,?,"+lgNow+")";
			
			pstmt = con.prepareStatement(sql);
			for(Map<String, Object> map : list){
				pstmt.setInt(1, Integer.valueOf(map.get("times").toString()));
				pstmt.setInt(2, (Integer)map.get("gid"));
				pstmt.setString(3, map.get("gname").toString());
				pstmt.setInt(4, (Integer)map.get("counts"));
				pstmt.setInt(5, (Integer)map.get("amount"));
				pstmt.addBatch();
			}
			
			int[] executeBatch = pstmt.executeBatch();
			con.commit();
			return executeBatch.length;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return 0;
	}
	
	/**
	 * 砸蛋统计
	 * @param stime
	 * @param etime
	 * @param YMD
	 */
	public void getSmashingEggs(Long stime,Long etime,Integer YMD){

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = " select hammer_price,sum(hammer_price*hammer_count) as amount,count(*) as cnts,reward_gift_type,sum(reward_gift_totalPrice) as gets "
					+ " from game_smashed_egg_log "
					+ " where createAt >= "+stime+" and createAt < "+etime+" group by hammer_price,reward_gift_type ";
			con = DbUtil.instance().getCon(constant.db_zhu_game,"slave");
			pstmt = con.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			if (rs != null) {
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				Map<String, Object> mapHammer1 = null;
				Map<String, Object> mapHammer2 = null;
				Map<String, Object> mapHammer3 = null;
				
				while(rs.next()){
					if (rs.getInt("hammer_price") == 10) {
						if (mapHammer1 == null) {
							mapHammer1 = new HashMap<String, Object>();
							mapHammer1.put("hammer", 1); // 铜锤
						}
						// 汇总砸蛋 消费金币
						if (mapHammer1.containsKey("amount")) {
							mapHammer1.put("amount", (Integer)mapHammer1.get("amount")+rs.getInt("amount"));
						}else {
							mapHammer1.put("amount", rs.getInt("amount"));
						}
						// 汇总砸蛋 中奖金币
						if (mapHammer1.containsKey("gets")) {
							mapHammer1.put("gets", (Integer)mapHammer1.get("gets")+rs.getInt("gets"));
						}else {
							mapHammer1.put("gets", rs.getInt("gets"));
						}
						// 汇总砸蛋次数
						if (mapHammer1.containsKey("times")) {
							mapHammer1.put("times", (Integer)mapHammer1.get("times")+rs.getInt("cnts"));
						}else {
							mapHammer1.put("times", rs.getInt("cnts"));
						}
						
						switch (rs.getInt("reward_gift_type")) {
							case 1:mapHammer1.put("gid1", rs.getInt("cnts"));break;
							case 2:mapHammer1.put("gid2", rs.getInt("cnts"));break;
							case 3:mapHammer1.put("gid3", rs.getInt("cnts"));break;
							case 4:mapHammer1.put("gid4", rs.getInt("cnts"));break;
							case 5:mapHammer1.put("gid5", rs.getInt("cnts"));break;
							case 6:mapHammer1.put("gid6", rs.getInt("cnts"));break;
							case 7:mapHammer1.put("gid7", rs.getInt("cnts"));break;
							case 8:mapHammer1.put("gid8", rs.getInt("cnts"));break;
							default:
								break;
						}
						
					}else if (rs.getInt("hammer_price") == 25) {
						if (mapHammer2 == null) {
							mapHammer2 = new HashMap<String, Object>();
							mapHammer2.put("hammer", 2); // 金锤
						}
						// 汇总砸蛋 消费金币
						if (mapHammer2.containsKey("amount")) {
							mapHammer2.put("amount", (Integer)mapHammer2.get("amount")+rs.getInt("amount"));
						}else {
							mapHammer2.put("amount", rs.getInt("amount"));
						}
						// 汇总砸蛋 中奖金币
						if (mapHammer2.containsKey("gets")) {
							mapHammer2.put("gets", (Integer)mapHammer2.get("gets")+rs.getInt("gets"));
						}else {
							mapHammer2.put("gets", rs.getInt("gets"));
						}
						// 汇总砸蛋次数
						if (mapHammer2.containsKey("times")) {
							mapHammer2.put("times", (Integer)mapHammer2.get("times")+rs.getInt("cnts"));
						}else {
							mapHammer2.put("times", rs.getInt("cnts"));
						}
						
						switch (rs.getInt("reward_gift_type")) {
							case 9:mapHammer2.put("gid1", rs.getInt("cnts"));break;
							case 10:mapHammer2.put("gid2", rs.getInt("cnts"));break;
							case 11:mapHammer2.put("gid3", rs.getInt("cnts"));break;
							case 12:mapHammer2.put("gid4", rs.getInt("cnts"));break;
							case 13:mapHammer2.put("gid5", rs.getInt("cnts"));break;
							case 14:mapHammer2.put("gid6", rs.getInt("cnts"));break;
							case 15:mapHammer2.put("gid7", rs.getInt("cnts"));break;
							case 16:mapHammer2.put("gid8", rs.getInt("cnts"));break;
							default:
								break;
						}
					}else if (rs.getInt("hammer_price") == 50) {
						if (mapHammer3 == null) {
							mapHammer3 = new HashMap<String, Object>();
							mapHammer3.put("hammer", 3); // 紫锤
						}
						// 汇总砸蛋 消费金币
						if (mapHammer3.containsKey("amount")) {
							mapHammer3.put("amount", (Integer)mapHammer3.get("amount")+rs.getInt("amount"));
						}else {
							mapHammer3.put("amount", rs.getInt("amount"));
						}
						// 汇总砸蛋 中奖金币
						if (mapHammer3.containsKey("gets")) {
							mapHammer3.put("gets", (Integer)mapHammer3.get("gets")+rs.getInt("gets"));
						}else {
							mapHammer3.put("gets", rs.getInt("gets"));
						}
						// 汇总砸蛋次数
						if (mapHammer3.containsKey("times")) {
							mapHammer3.put("times", (Integer)mapHammer3.get("times")+rs.getInt("cnts"));
						}else {
							mapHammer3.put("times", rs.getInt("cnts"));
						}
						
						switch (rs.getInt("reward_gift_type")) {
							case 17:mapHammer3.put("gid1", rs.getInt("cnts"));break;
							case 18:mapHammer3.put("gid2", rs.getInt("cnts"));break;
							case 19:mapHammer3.put("gid3", rs.getInt("cnts"));break;
							case 20:mapHammer3.put("gid4", rs.getInt("cnts"));break;
							case 21:mapHammer3.put("gid5", rs.getInt("cnts"));break;
							case 22:mapHammer3.put("gid6", rs.getInt("cnts"));break;
							case 23:mapHammer3.put("gid7", rs.getInt("cnts"));break;
							case 24:mapHammer3.put("gid8", rs.getInt("cnts"));break;
							default:
								break;
						}
					}
				}
				if (mapHammer1 != null) {
					list.add(mapHammer1);
				}
				if (mapHammer2 != null) {
					list.add(mapHammer2);
				}
				if (mapHammer3 != null) {
					list.add(mapHammer3);
				}
				
				if (list.size() > 0) {
					int addGiftSends = this.addSmashingEggs(list,YMD);
					if (addGiftSends <= 0) {
						System.out.println("addSmashingEggs add err, stime:" + stime + " etime:" + etime + " YMD:" + YMD);
					}
				}
			}
			
		} catch (Exception e) {
			System.out.println("getSmashingEggs add err, stime:" + stime + " etime:" + etime + " YMD:" + YMD);
			System.out.println("getSmashingEggs excep:" + e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 添加砸蛋的静态数据
	 * @param list
	 * @param datetime
	 * @return
	 */
	public int addSmashingEggs(List<Map<String, Object>> list,int datetime){
		
		if (list == null || list.size() == 0) {
			return 0;
		}
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		Long lgNow = System.currentTimeMillis()/1000;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_analysis,"master");
			con.setAutoCommit(false);
			String sql = "insert into analysis_eggs(hammer,datetimes,times,consume,gid1,gid2,gid3,gid4,gid5,gid6,gid7,gid8,gets,addtime)values(?,"+datetime+",?,?,?,?,?,?,?,?,?,?,?,"+lgNow+")";
			
			pstmt = con.prepareStatement(sql);
			for(Map<String, Object> map : list){
				pstmt.setInt(1, (Integer)map.get("hammer"));
				pstmt.setInt(2, (Integer)map.get("times"));
				pstmt.setInt(3, (Integer)map.get("amount"));
				pstmt.setInt(4, map.get("gid1")==null?0:(Integer)map.get("gid1"));
				pstmt.setInt(5, map.get("gid2")==null?0:(Integer)map.get("gid2"));
				pstmt.setInt(6, map.get("gid3")==null?0:(Integer)map.get("gid3"));
				pstmt.setInt(7, map.get("gid4")==null?0:(Integer)map.get("gid4"));
				pstmt.setInt(8, map.get("gid5")==null?0:(Integer)map.get("gid5"));
				pstmt.setInt(9, map.get("gid6")==null?0:(Integer)map.get("gid6"));
				pstmt.setInt(10, map.get("gid7")==null?0:(Integer)map.get("gid7"));
				pstmt.setInt(11, map.get("gid8")==null?0:(Integer)map.get("gid8"));
				pstmt.setInt(12, map.get("gets")==null?0:(Integer)map.get("gets"));
				pstmt.addBatch();
			}
			
			int[] executeBatch = pstmt.executeBatch();
			con.commit();
			return executeBatch.length;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return 0;
	}
	
	/**
	 * 添加 抢红包记录
	 * @param envelopeid 红包ID
	 * @param srcuid 发红包人UID
	 * @param dstuid 抢红包人UID
	 * @param roomid 房间UID
	 * @param money 抢红包的金额
	 */
	public void addredenvelop(int envelopeid,int srcuid,int dstuid,int roomid,int money){

		String RED_ENVELOP_TAKED_LIST_RKEY_PREFIX = "re:%d:%d:taked_list";
		String RED_ENVELOP_TAKED_MAP_RKEY_PREFIX = "re:%d:%d:taked_map";

		String sql = "insert into `pay_get_redenvelop`(envelopeid,srcuid,dstuid,roomId,money,getTime)value("+envelopeid+","+srcuid+","+dstuid+","+roomid+","+money+","+System.currentTimeMillis()/1000+")";

		String sql2 = "update zhu_bill.bill_redenvelope set getmoney=sendmoney,getcnts=sendcnts,isfinish=1 where id=" + envelopeid;
		
		Connection con = null;
		Statement pstmt = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_pay,"master");
			con.setAutoCommit(false);
			pstmt = con.createStatement();
			pstmt.addBatch(sql);
			pstmt.addBatch(sql2);
			
			int[] executeBatch = pstmt.executeBatch();
			con.commit();
			
			if (executeBatch != null ) {
				String redEnvelopTakedList = String.format(RED_ENVELOP_TAKED_LIST_RKEY_PREFIX, srcuid, envelopeid);
				String redEnvelopTakedMap = String.format(RED_ENVELOP_TAKED_MAP_RKEY_PREFIX, srcuid, envelopeid);
				RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6382, redEnvelopTakedList);
				RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6382, redEnvelopTakedMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * 红包 超过5小时，系统正常没收
	 * @return
	 */
	public void getRedenve(){
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int dstUid = 10000048;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_bill,"slave");
			Long lgNow = System.currentTimeMillis()/1000 - 5*3600;
			String sql = "select id,srcUid,sendmoney,getmoney,roomId from bill_redenvelope where sendtime< " + lgNow + " and isfinish=0 and sendmoney>getmoney";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				
				while(rs.next()){
					
					this.addredenvelop(rs.getInt("id"), rs.getInt("srcUid"), dstUid, rs.getInt("roomId"), rs.getInt("sendmoney")-rs.getInt("getmoney"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	
	/**
	 * 直播间 公告推送
	 * @return
	 */
	public Map<String, Object> getRoomChatList(){
		
		Long lgNow = System.currentTimeMillis()/1000;
		
		String sql = " select a.id,a.content,a.starttime,a.endtime,a.interval,a.isvalid,a.addtime from web_room_chat a where a.isvalid=1 and a.starttime<="+lgNow +" and a.endtime >"+lgNow;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Object> map = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_web,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				
				while (rs.next()) {
					map = new HashMap<String, Object>();
					map.put("id", rs.getInt("id"));
					map.put("content", rs.getString("content"));
					map.put("starttime", rs.getLong("starttime"));
					map.put("endtime", rs.getLong("endtime"));
					map.put("interval", rs.getInt("interval"));
					map.put("isvalid", rs.getInt("isvalid"));
					map.put("addtime", rs.getLong("addtime"));
					RedisOperat.getInstance().rpush(RedisContant.host, RedisContant.port6380, RedisContant.roomChat, JSONObject.toJSONString(map));
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return map;
	}
}
