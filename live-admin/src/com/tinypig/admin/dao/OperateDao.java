package com.tinypig.admin.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.Unirest;
import com.tinypig.admin.constant;
import com.tinypig.admin.model.ChannelModel;
import com.tinypig.admin.redis.service.RedisOperat;
import com.tinypig.admin.util.Constant;
import com.tinypig.admin.util.DateUtil;
import com.tinypig.admin.util.DbUtil;
import com.tinypig.admin.util.RedisContant;

public class OperateDao extends BaseDao {

	private final static OperateDao instance = new OperateDao();
	
	public static OperateDao getInstance(){
		return instance;
	}
	
	public Map<String, Object> getRemain(int os,String channel,Long stime,Long etime,int pages,int size,int platform,int category,int type){
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		String sqlList = "";
		String sql = " select from_unixtime(times,'%Y-%m-%d') as times,"
					+ " sum(newUser) as newUsers,"
					+ " sum(remain1) as remain1s,"
					+ " sum(remain2) as remain2s,"
					+ " sum(remain3) as remain3s,"
					+ " sum(remain4) as remain4s,"
					+ " sum(remain5) as remain5s,"
					+ " sum(remain6) as remain6s,"
					+ " sum(remain7) as remain7s,"
					+ " sum(remain14) as remain14s,"
					+ " sum(remain30) as remain30s,"
					+ " channel"
					+ " from analysis_remain where times >= "+stime+" and times <= "+etime;
			if (os > 0) {
				sql = sql + " and os = "+os;
			}

			if (!StringUtils.isEmpty(channel)) {
				sql = sql + " and channel = ? ";
			}
			if(platform!=0){
				sql = sql +" and platform = " + platform;
			}
			sql = sql + " and category = "+type;
		if(category==1){
			sqlList = sql + " group by times";
		}else{
			sqlList = sql + " group by channel";
		}
		sqlList = sqlList + " limit "+(pages -1) *size+","+size;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Object> map = null;
		
		try {

			con = DbUtil.instance().getCon(constant.db_zhu_analysis,"slave");
			pstmt = con.prepareStatement(sqlList);
			if (!StringUtils.isEmpty(channel)) {
				pstmt.setString(1, channel);
			}
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				DecimalFormat df = new DecimalFormat("#.0");
				while (rs.next()) {
					map = new LinkedHashMap<String, Object>();
					map.put("times", rs.getString("times"));
					map.put("newUser", rs.getInt("newUsers"));
					map.put("remain1", rs.getInt("remain1s"));
					map.put("Rate1", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain1s")*100/rs.getInt("newUsers")):0);
					map.put("remain2", rs.getInt("remain2s"));
					map.put("Rate2", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain2s")*100/rs.getInt("newUsers")):0);
					map.put("remain3", rs.getInt("remain3s"));
					map.put("Rate3", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain3s")*100/rs.getInt("newUsers")):0);
					map.put("remain4", rs.getInt("remain4s"));
					map.put("Rate4", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain4s")*100/rs.getInt("newUsers")):0);
					map.put("remain5", rs.getInt("remain5s"));
					map.put("Rate5", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain5s")*100/rs.getInt("newUsers")):0);
					map.put("remain6", rs.getInt("remain6s"));
					map.put("Rate6", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain6s")*100/rs.getInt("newUsers")):0);
					map.put("remain7", rs.getInt("remain7s"));
					map.put("Rate7", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain7s")*100/rs.getInt("newUsers")):0);
					map.put("remain14", rs.getInt("remain14s"));
					map.put("Rate14", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain14s")*100/rs.getInt("newUsers")):0);
					map.put("remain30", rs.getInt("remain30s"));
					map.put("Rate30", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain30s")*100/rs.getInt("newUsers")):0);
					map.put("channel",rs.getString("channel"));
					list.add(map);
				}
			}

			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			
			pstmt = con.prepareStatement(sql);
			if (!StringUtils.isEmpty(channel)) {
				pstmt.setString(1, channel);
			}
			rs = pstmt.executeQuery();
			int icount = 0;
			if (rs != null) {

				rs.last();
				icount = rs.getRow();
			}
			map = new HashMap<String, Object>();
			map.put("rows", list);
			map.put("total", icount);
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
	
	public Map<String, Object> ltv(int os,String channel,Long stime,Long etime,int pages,int size,int platform,int category,int type){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		String sqlList = "";
		String sql = " select from_unixtime(registtime,'%Y-%m-%d') as times,"
					+ " sum(newUser) as newUsers,"
					+ " sum(remain1) as remain1s,"
					+ " sum(remain2) as remain2s,"
					+ " sum(remain3) as remain3s,"
					+ " sum(remain4) as remain4s,"
					+ " sum(remain5) as remain5s,"
					+ " sum(remain6) as remain6s,"
					+ " sum(remain7) as remain7s,"
					+ " sum(remain14) as remain14s,"
					+ " sum(remain30) as remain30s,"
					+ " channel"
					+ " from analysis_remain ar inner join pay_order po on ar.channel = po.channel where times >= "+stime+" and times <= "+etime;
			if (os > 0) {
				sql = sql + " and os = "+os;
			}

			if (!StringUtils.isEmpty(channel)) {
				sql = sql + " and channel = ? ";
			}
			if(platform!=0){
				sql = sql +" and platform = " + platform;
			}
			sql = sql + " and category = "+type;
		if(category==1){
			sqlList = sql + " group by times";
		}else{
			sqlList = sql + " group by channel";
		}
		sqlList = sqlList + " limit "+(pages -1) *size+","+size;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Object> map = null;
		
		try {

			con = DbUtil.instance().getCon(constant.db_zhu_analysis,"slave");
			pstmt = con.prepareStatement(sqlList);
			if (!StringUtils.isEmpty(channel)) {
				pstmt.setString(1, channel);
			}
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				DecimalFormat df = new DecimalFormat("#.0");
				while (rs.next()) {
					map = new LinkedHashMap<String, Object>();
					map.put("times", rs.getString("times"));
					map.put("newUser", rs.getInt("newUsers"));
					map.put("remain1", rs.getInt("remain1s"));
					map.put("Rate1", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain1s")*100/rs.getInt("newUsers")):0);
					map.put("remain2", rs.getInt("remain2s"));
					map.put("Rate2", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain2s")*100/rs.getInt("newUsers")):0);
					map.put("remain3", rs.getInt("remain3s"));
					map.put("Rate3", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain3s")*100/rs.getInt("newUsers")):0);
					map.put("remain4", rs.getInt("remain4s"));
					map.put("Rate4", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain4s")*100/rs.getInt("newUsers")):0);
					map.put("remain5", rs.getInt("remain5s"));
					map.put("Rate5", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain5s")*100/rs.getInt("newUsers")):0);
					map.put("remain6", rs.getInt("remain6s"));
					map.put("Rate6", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain6s")*100/rs.getInt("newUsers")):0);
					map.put("remain7", rs.getInt("remain7s"));
					map.put("Rate7", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain7s")*100/rs.getInt("newUsers")):0);
					map.put("remain14", rs.getInt("remain14s"));
					map.put("Rate14", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain14s")*100/rs.getInt("newUsers")):0);
					map.put("remain30", rs.getInt("remain30s"));
					map.put("Rate30", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain30s")*100/rs.getInt("newUsers")):0);
					map.put("channel",rs.getString("channel"));
					list.add(map);
				}
			}

			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			
			pstmt = con.prepareStatement(sql);
			if (!StringUtils.isEmpty(channel)) {
				pstmt.setString(1, channel);
			}
			rs = pstmt.executeQuery();
			int icount = 0;
			if (rs != null) {

				rs.last();
				icount = rs.getRow();
			}
			map = new HashMap<String, Object>();
			map.put("rows", list);
			map.put("total", icount);
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
	
	public List getRemainList(int os,String channel,Long stime,Long etime,int platform,int category,int type){
		
		
		String sqlList = "";
		String sql = " select from_unixtime(times,'%Y-%m-%d') as times,"
					+ " sum(newUser) as newUsers,"
					+ " sum(remain1) as remain1s,"
					+ " sum(remain2) as remain2s,"
					+ " sum(remain3) as remain3s,"
					+ " sum(remain4) as remain4s,"
					+ " sum(remain5) as remain5s,"
					+ " sum(remain6) as remain6s,"
					+ " sum(remain7) as remain7s,"
					+ " sum(remain14) as remain14s,"
					+ " sum(remain30) as remain30s,"
					+ " channel"
					+ " from analysis_remain where times >= "+stime+" and times <= "+etime;
			if (os > 0) {
				sql = sql + " and os = "+os;
			}

			if (!StringUtils.isEmpty(channel)) {
				sql = sql + " and channel = ? ";
			}
			if(platform!=0){
				sql = sql +" and platform = " + platform;
			}
			sql = sql + " and category = "+type;
			if(category==1){
				sqlList = sql + " group by times";
			}else{
				sqlList = sql + " group by channel";
			}
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Object> map = null;
		List<Map> list = new ArrayList<Map>();
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_analysis,"slave");
			pstmt = con.prepareStatement(sqlList);
			if (!StringUtils.isEmpty(channel)) {
				pstmt.setString(1, channel);
			}
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				DecimalFormat df = new DecimalFormat("#.0");
				while (rs.next()) {
					map = new LinkedHashMap<String, Object>();
					map.put("times", rs.getString("times"));
					map.put("channel",rs.getString("channel"));
					map.put("newUser", rs.getInt("newUsers"));
					map.put("remain1", rs.getInt("remain1s"));
					map.put("Rate1", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain1s")*100/rs.getInt("newUsers")):0);
					map.put("remain2", rs.getInt("remain2s"));
					map.put("Rate2", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain2s")*100/rs.getInt("newUsers")):0);
					map.put("remain3", rs.getInt("remain3s"));
					map.put("Rate3", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain3s")*100/rs.getInt("newUsers")):0);
					map.put("remain4", rs.getInt("remain4s"));
					map.put("Rate4", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain4s")*100/rs.getInt("newUsers")):0);
					map.put("remain5", rs.getInt("remain5s"));
					map.put("Rate5", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain5s")*100/rs.getInt("newUsers")):0);
					map.put("remain6", rs.getInt("remain6s"));
					map.put("Rate6", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain6s")*100/rs.getInt("newUsers")):0);
					map.put("remain7", rs.getInt("remain7s"));
					map.put("Rate7", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain7s")*100/rs.getInt("newUsers")):0);
					map.put("remain14", rs.getInt("remain14s"));
					map.put("Rate14", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain14s")*100/rs.getInt("newUsers")):0);
					map.put("remain30", rs.getInt("remain30s"));
					map.put("Rate30", rs.getInt("newUsers") > 0 ? df.format(rs.getInt("remain30s")*100/rs.getInt("newUsers")):0);
					list.add(map);
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
		return list;
	}
	
	public Map<String, Object> getRemainTest(Long stime,Long etime,int pages,int size,int category){
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		Long lgNow = DateUtil.getDayBegin();
		if (etime >= lgNow) {
			etime = lgNow;
		}
		
		String sql = " select times,"
				+ " sum(newUser) as newUsers,"
				+ " sum(remain1) as remain1s,"
				+ " sum(remain2) as remain2s,"
				+ " sum(remain3) as remain3s,"
				+ " sum(remain4) as remain4s,"
				+ " sum(remain5) as remain5s,"
				+ " sum(remain6) as remain6s,"
				+ " sum(remain7) as remain7s,"
				+ " sum(remain14) as remain14s,"
				+ " sum(remain30) as remain30s "
				+ " from analysis_remain_test where times >= from_unixtime("+stime+") and times < from_unixtime("+etime+")";
		
		String sqlList = sql + " group by times  limit "+(pages -1) *size+","+size;
		
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Object> map = null;
		
		try {

			con = DbUtil.instance().getCon(constant.db_zhu_analysis,"slave");
			pstmt = con.prepareStatement(sqlList);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				DecimalFormat df = new DecimalFormat("#.00");
				
				if (etime < lgNow) {
					etime = lgNow;
				}
				
				Date d = new Date(etime*1000);
				while (rs.next()) {
					
					map = new HashMap<String, Object>();
					int iday = DateUtil.getDaysBetweenDates(rs.getDate("times"), d);

					map.put("times", rs.getString("times"));
					map.put("newUser", rs.getInt("newUsers"));
					
					if (iday == 0) {
						map.put("remain1", 0);
						map.put("Rate1", 0);
					}else {
						map.put("remain1", rs.getDouble("remain1s"));
						map.put("Rate1", df.format(rs.getDouble("remain1s")*100.0)+"%");
					}
					
					if (iday <= 1 ) {
						map.put("remain2", 0);
						map.put("Rate2", 0);
					}else {
						map.put("remain2", rs.getInt("remain2s"));
						map.put("Rate2", df.format(rs.getDouble("remain2s")*100.0)+"%");
					}
					
					if (iday <=2 ) {
						map.put("remain3", 0);
						map.put("Rate3", 0);
					}else {
						map.put("remain3", rs.getInt("remain3s"));
						map.put("Rate3", df.format(rs.getDouble("remain3s")*100.0)+"%");
					}
					
					if (iday <= 3) {
						map.put("remain4", 0);
						map.put("Rate4", 0);
					}else {
						map.put("remain4", rs.getInt("remain4s"));
						map.put("Rate4", df.format(rs.getDouble("remain4s")*100.0)+"%");
					}
					
					if (iday <= 4) {
						map.put("remain5", 0);
						map.put("Rate5", 0);
					}else {
						map.put("remain5", rs.getInt("remain5s"));
						map.put("Rate5", df.format(rs.getDouble("remain5s")*100.0)+"%");
					}
					
					if (iday <= 5) {
						map.put("remain6", 0);
						map.put("Rate6", 0);
					}else {
						map.put("remain6", rs.getInt("remain6s"));
						map.put("Rate6", df.format(rs.getDouble("remain6s")*100.0)+"%");
					}
					
					if (iday <= 6) {
						map.put("remain7", 0);
						map.put("Rate7", 0);
					}else {
						map.put("remain7", rs.getInt("remain7s"));
						map.put("Rate7", df.format(rs.getDouble("remain7s")*100.0)+"%");
					}
					
					if (iday <= 13) {
						map.put("remain14", 0);
						map.put("Rate14", 0);
					}else {
						map.put("remain14", rs.getInt("remain14s"));
						map.put("Rate14", df.format(rs.getDouble("remain14s")*100.0)+"%");
					}
					
					if (iday <= 29) {
						map.put("remain30", 0);
						map.put("Rate30", 0);
					}else {
						map.put("remain30", rs.getInt("remain30s"));
						map.put("Rate30", df.format(rs.getDouble("remain30s")*100.0)+"%");
					}
					
					list.add(map);
				}
			}
			if (rs != null) {
				rs.close();
			}
			rs = pstmt.executeQuery(sql + " group by times ");
			int icount = 0;
			if (rs != null) {

				rs.last();
				icount = rs.getRow();
			}
			map = new HashMap<String, Object>();
			map.put("rows", list);
			map.put("total", icount);
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
	
	public Map<String, Object> getSummary(int os,Long stime,Long etime,int pages,int size,String channelName){
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		String sql = " select from_unixtime(times,'%Y-%m-%d') as times,"
				+ " sum(equipmentsNew) as news,"
				+ " sum(registers) as registers,"
				+ " sum(equipments) as equipments,"
				+ " sum(amounts) as amounts,"
				+ " sum(payUsers) as payUsers,"
				+ " sum(registAccounts) as registAccounts,"
				+ " sum(registPayUsers) as registPayUsers,"
				+" (select sum(ac.actives) from (select actives from analysis_summary group by os) ac) as actives"
				+ " from analysis_summary where times >= "+stime+" and times <= "+etime;
		
		if (os > 0) {
			
			sql = sql + " and os = "+os;
		}
		if(!channelName.equals("全部")){
			sql = sql + " and channelName = '"+channelName+"'";
		}
		String sqlList = sql + " group by times  limit "+(pages -1) *size+","+size;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Object> map = null;
		
		try {

			con = DbUtil.instance().getCon(constant.db_zhu_analysis,"slave");
			pstmt = con.prepareStatement(sqlList);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				DecimalFormat df = new DecimalFormat("#.00");
				while (rs.next()) {
					map = new HashMap<String, Object>();
					map.put("times", rs.getString("times"));
					map.put("news", rs.getInt("news"));
					map.put("registers", rs.getInt("registers"));
					map.put("rate", rs.getInt("news") > 0 ? df.format(rs.getInt("registers")*100.0/rs.getInt("news")):0);
					map.put("equipments", rs.getInt("equipments"));
					map.put("actives", rs.getInt("actives"));
					map.put("amounts", rs.getInt("amounts"));
					map.put("payUsers", rs.getInt("payUsers"));
					map.put("registAccounts", rs.getInt("registAccounts"));
					map.put("registPayUsers", rs.getInt("registPayUsers"));
					map.put("payRate", rs.getInt("actives") > 0 ? df.format(rs.getInt("payUsers")*100.0/rs.getInt("actives")):0);
					map.put("payArpu", rs.getInt("payUsers") > 0 ? (rs.getInt("amounts")/rs.getInt("payUsers")):0);
					map.put("registPayArpu", rs.getInt("registPayUsers") > 0 ? (rs.getInt("registAccounts")/rs.getInt("registPayUsers")):0);
					list.add(map);
				}
			}
			if (rs != null) {
				rs.close();
			}
			rs = pstmt.executeQuery(sql);
			int icount = 0;
			if (rs != null) {

				rs.last();
				icount = rs.getRow();
			}
			map = new HashMap<String, Object>();
			map.put("rows", list);
			map.put("total", icount);
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

	
	public Map<String, Object> getSummaryTest(Long stime,Long etime,int pages,int size){
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		Long lgNow = DateUtil.getDayBegin();
		if (etime >= lgNow) {
			etime = lgNow;
		}
		
		String sql = " select times,"
				+ " '0' as news,"
				+ " sum(registers) as registers,"
				+ " '0' as equipments,"
				+ " sum(actives) as actives,"
				+ " sum(amounts) as amounts,"
				+ " sum(payUsers) as payUsers"
				+ " from analysis_summary_test where times >= from_unixtime("+stime+") and times < from_unixtime("+etime +") group by times ";
		
		String sqlList = sql + " limit "+(pages -1) *size+","+size;

		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Object> map = null;
		
		try {

			con = DbUtil.instance().getCon(constant.db_zhu_analysis,"slave");
			pstmt = con.prepareStatement(sqlList);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				DecimalFormat df = new DecimalFormat("#.00");
				while (rs.next()) {
					map = new HashMap<String, Object>();
					map.put("times", rs.getString("times"));
					map.put("news", rs.getInt("news"));
					map.put("registers", rs.getInt("registers"));
					map.put("rate", 0);
					map.put("equipments", 0);
					map.put("actives", rs.getInt("actives"));
					map.put("amounts", rs.getInt("amounts"));
					map.put("payUsers", rs.getInt("payUsers"));
					map.put("payRate", rs.getInt("actives") > 0 ? df.format(rs.getInt("payUsers")*100.0/rs.getInt("actives")):0);
					map.put("payArpu", rs.getInt("payUsers") > 0 ? df.format(rs.getInt("amounts")*1.00/rs.getInt("payUsers")):0);
					list.add(map);
				}
			}
			if (rs != null) {
				rs.close();
			}
			rs = pstmt.executeQuery(sql);
			int icount = 0;
			if (rs != null) {

				rs.last();
				icount = rs.getRow();
			}
			map = new HashMap<String, Object>();
			map.put("rows", list);
			map.put("total", icount);
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
	
	/**
	 * 获取主播结算的基础数据
	 * @param unionid 工会ID
	 * @param uid 主播UID
	 * @param dateYM 日期[年月]
	 * @param type 类别[=0全部 =1归属工会 =2自由人]
	 * @param pages 页数
	 * @param size 每页显示条数
	 * @return
	 */
	public List<Map<String, Object>> getWages(int unionid,int uid,String dateYM,int type,int pages,int size){

		int iYM = Integer.valueOf(dateYM);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		String sql = "";
		if (type == 0) {
			// 全部
			sql = " select a.times,coalesce(c.unionid,0) as unionids,coalesce(c.unionname,'自由人') as unionname,a.anchorid,a.validday,a.airtime,a.credits,a.weekstar,a.activity,a.exchange,a.withdraw from analysis_wages a left join zhu_union.union_anchor_ref b on a.anchorid=b.anchorid left join zhu_union.union_info c on b.unionid=c.unionid where a.times="+iYM;
			if (unionid > 0) {
				sql = sql + " and c.unionid=" + unionid;
			}
			if (uid > 0 ) {
				sql = sql + " and a.anchorid=" + uid;
			}
		}else if (type == 1) {
			// 工会
			sql = " select a.times,c.unionid as unionids,c.unionname,a.anchorid,a.validday,a.airtime,a.credits,a.weekstar,a.activity,a.exchange,a.withdraw from analysis_wages a inner join zhu_union.union_anchor_ref b on a.anchorid=b.anchorid inner join zhu_union.union_info c on b.unionid=c.unionid where a.times="+iYM;
			if (unionid > 0) {
				sql = sql + " and c.unionid=" + unionid;
			}
			if (uid > 0 ) {
				sql = sql + " and a.anchorid=" + uid;
			}
		}else {
			// 自由人
			sql = " select a.times,'0' as unionids,'自由人' as unionname,a.anchorid,a.validday,a.airtime,a.credits,a.weekstar,a.activity,a.exchange,a.withdraw from analysis_wages a where a.times="+iYM+" and not exists( select id from zhu_union.union_anchor_ref b where a.anchorid=b.anchorid) ";

			if (uid > 0 ) {
				sql = sql + " and a.anchorid=" + uid;
			}
		}

		sql = sql+" order by unionids desc limit "+(pages-1)*size+","+size;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {

			con = DbUtil.instance().getCon(constant.db_zhu_analysis,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				
				Map<String, Object> map = null;
				while (rs.next()) {
					map = new HashMap<String, Object>();
					map.put("times", rs.getString("times"));
					map.put("unionname", rs.getString("unionname"));
					map.put("anchorid", rs.getInt("anchorid"));
					map.put("validday", rs.getInt("validday"));
					map.put("airtime", rs.getInt("airtime"));
					map.put("credits", rs.getInt("credits"));
					map.put("weekstar", rs.getInt("weekstar"));
					map.put("activity", rs.getInt("activity"));
					map.put("exchange", rs.getInt("exchange"));
					map.put("withdraw", rs.getInt("withdraw"));
					list.add(map);
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
		return list;
	}


	/**
	 * 获取主播结算的基础数据
	 * @param unionid 工会ID
	 * @param uid 主播UID
	 * @param dateYM 日期[年月]
	 * @param type 类别[=0全部 =1归属工会 =2自由人]
	 * @return
	 */
	public int getWagesSize(int unionid,int uid,String dateYM,int type){
		
		int iYM = Integer.valueOf(dateYM);
		
		int icount = 0;
		String sql = "";
		if (type == 0) {
			// 全部
			sql = " select a.times,c.unionname,a.anchorid,a.validday,a.airtime,a.credits,a.weekstar,a.activity,a.exchange,a.withdraw from analysis_wages a left join zhu_union.union_anchor_ref b on a.anchorid=b.anchorid left join zhu_union.union_info c on b.unionid=c.unionid where a.times="+iYM;
			if (unionid > 0) {
				sql = sql + " and c.unionid=" + unionid;
			}
			if (uid > 0 ) {
				sql = sql + " and a.anchorid=" + uid;
			}
		}else if (type == 1) {
			// 工会
			sql = " select a.times,c.unionname,a.anchorid,a.validday,a.airtime,a.credits,a.weekstar,a.activity,a.exchange,a.withdraw from analysis_wages a inner join zhu_union.union_anchor_ref b on a.anchorid=b.anchorid inner join zhu_union.union_info c on b.unionid=c.unionid where a.times="+iYM;
			if (unionid > 0) {
				sql = sql + " and c.unionid=" + unionid;
			}
			if (uid > 0 ) {
				sql = sql + " and a.anchorid=" + uid;
			}
		}else {
			// 自由人
			sql = " select a.times,'自由人' as unionname,a.anchorid,a.validday,a.airtime,a.credits,a.weekstar,a.activity,a.exchange,a.withdraw from analysis_wages a where a.times="+iYM+" and not exists( select id from zhu_union.union_anchor_ref b where a.anchorid=b.anchorid) ";

			if (uid > 0 ) {
				sql = sql + " and a.anchorid=" + uid;
			}
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {

			con = DbUtil.instance().getCon(constant.db_zhu_analysis,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				rs.last();
				icount = rs.getRow();
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
		return icount;
	}
	
	public List<Map<String, Object>> getSendGiftList(int stime,int etime,int pages,int size){

		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		String sql = " select gid,gname,sum(counts) as count,sum(amount) as amounts "
				+ " from analysis_gift_consume "
				+ " where times>="+stime+" and times<="+etime
				+ " group by gid order by count desc limit "+(pages-1)*size+","+size;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {

			con = DbUtil.instance().getCon(constant.db_zhu_analysis,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				
				Map<String, Object> map = null;
				while (rs.next()) {
					map = new HashMap<String, Object>();
					map.put("gid", rs.getInt("gid"));
					map.put("gname", rs.getString("gname"));
					map.put("counts", rs.getInt("count"));
					map.put("amount", rs.getInt("amounts"));
					list.add(map);
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
		return list;
	}
	
	public int getSendGiftListSize(int stime,int etime){
		
		int icount = 0;
		String sql = " select gid,gname,sum(counts) as count,sum(amount) as amounts "
				+ " from analysis_gift_consume "
				+ " where times>="+stime+" and times<="+etime
				+ " group by gid ";
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_analysis,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				rs.last();
				icount = rs.getRow();
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
		return icount;
	}
	
	public List<Map<String, Object>> getMallStatic(List<Integer> guardlist,List<Integer> viplist,List<Integer> propList,Long stime,Long etime,int pages,int size){

		Long lgNow = System.currentTimeMillis()/1000;
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		String sqlGuard = "";
		
		if (guardlist != null && guardlist.size() > 0) {
			sqlGuard = " select gid, sum(case when endtime >= " + lgNow + " then 1 else 0 end) as exist,sum(case when endtime < " + lgNow + " and cushiontime >= " + lgNow + " then 1 else 0 end) as cushions,count(*) as counts"
			+ " from user_guard_info "
			+ " where starttime >= " + stime + " and starttime <= " + etime + " and gid in(";
			
			boolean bl = true;
			for(Integer gid : guardlist){
				if (bl) {
					sqlGuard = sqlGuard+gid;
					bl = false;
				}else {
					sqlGuard = sqlGuard + ","+gid;
				}
			}
			sqlGuard = sqlGuard + ") group by gid";
		}
		
		String sqlVip = "";
		if (viplist != null && viplist.size() > 0) {
			sqlVip = " select gid, sum(case when endtime >= " + lgNow + " then 1 else 0 end) as exist,'0' as cushions,count(*) as counts "
					+ " from user_vip_info "
					+ " where starttime >= " + stime + " and starttime < " + etime + " and gid in(";
			
			boolean bl = true;
			for(Integer gid : viplist){
				if (bl) {
					sqlVip = sqlVip+gid;
					bl = false;
				}else {
					sqlVip = sqlVip + ","+gid;
				}
			}
			sqlVip = sqlVip + ") group by gid";
		}

		
		String sqlProp = "";
		if (propList != null && propList.size() > 0) {
			sqlProp = " select gid, sum(case when endtime >= " + lgNow + " then 1 else 0 end) as exist,'0' as cushions,count(*) as counts "
					+ " from zhu_item.user_mall_item "
					+ " where starttime >= " + stime + " and starttime < " + etime + " and gid in(";
			
			boolean bl = true;
			for(Integer gid : propList){
				if (bl) {
					sqlProp = sqlProp+gid;
					bl = false;
				}else {
					sqlProp = sqlProp + ","+gid;
				}
			}
			sqlProp = sqlProp + ") group by gid";
		}
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {

			con = DbUtil.instance().getCon(constant.db_zhu_user,"slave");
			stmt = con.createStatement();
			java.text.DecimalFormat   df=new   java.text.DecimalFormat("#.##");
			
			if (StringUtils.isNotEmpty(sqlGuard)) {

				rs = stmt.executeQuery(sqlGuard);
				if (rs != null) {
					Map<String, Object> map = null;
					while (rs.next()) {
						map = new HashMap<String, Object>();
						map.put("gid", rs.getInt("gid"));
						if (rs.getInt("gid") == 45) {
							map.put("gname", "骑士守护");
						}else if (rs.getInt("gid") == 46) {
							map.put("gname", "王子守护");
						}else {
							map.put("gname", "未知");
						}
						map.put("exist", rs.getInt("exist"));
						map.put("cushions", rs.getInt("cushions"));
						map.put("counts", rs.getInt("counts"));
						if (rs.getInt("counts") > 0) {
							map.put("rate", df.format((rs.getInt("counts")-rs.getInt("exist"))*100.0/rs.getInt("counts")));
						}else {
							map.put("rate", 0.00);
						}
						list.add(map);
					}
				}
			}
			if (StringUtils.isNotEmpty(sqlVip)) {

				if (rs != null) {
					rs.close();
				}
				rs = stmt.executeQuery(sqlVip);
				if (rs != null) {
					
					Map<String, Object> map = null;
					while (rs.next()) {
						map = new HashMap<String, Object>();
						map.put("gid", rs.getInt("gid"));
						if (rs.getInt("gid") == 43) {
							map.put("gname", "白金VIP");
						}else if (rs.getInt("gid") == 44) {
							map.put("gname", "钻石VIP");
						}else {
							map.put("gname", "未知");
						}
						map.put("exist", rs.getInt("exist"));
						map.put("cushions", rs.getInt("cushions"));
						map.put("counts", rs.getInt("counts"));
						if (rs.getInt("counts") > 0) {
							map.put("rate", df.format((rs.getInt("counts")-rs.getInt("exist"))*100.0/rs.getInt("counts")));
						}else {
							map.put("rate", 0.00);
						}
						list.add(map);
					}
				}
			}

			if (StringUtils.isNotEmpty(sqlProp)) {

				if (rs != null) {
					rs.close();
				}
				rs = stmt.executeQuery(sqlProp);
				if (rs != null) {
					
					Map<String, Object> map = null;
					while (rs.next()) {
						map = new HashMap<String, Object>();
						map.put("gid", rs.getInt("gid"));
						if (rs.getInt("gid") == 60) {
							map.put("gname", "萌猪卡");
						}else {
							map.put("gname", "未知");
						}
						map.put("exist", rs.getInt("exist"));
						map.put("cushions", rs.getInt("cushions"));
						map.put("counts", rs.getInt("counts"));
						if (rs.getInt("counts") > 0) {
							map.put("rate", df.format((rs.getInt("counts")-rs.getInt("exist"))*100.0/rs.getInt("counts")));
						}else {
							map.put("rate", 0.00);
						}
						list.add(map);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 
	 * @param gid =0 忽略
	 * @param uid =0 忽略
	 * @param type =0全部 =1VIP =2守护
	 * @param stime 购买时间端
	 * @param etime 购买时间端
	 * @param pages
	 * @param size
	 * @return
	 */
	public Map<String, Object> getMallSearch(Integer gid,Integer uid,Integer type,Long stime,Long etime,int pages,int size){

		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		String sql = "select gid,gname,srcuid,srcnickname,dstuid,dstnickname,pricetotal,realpricetotal,endtime,createAt from pay_mall_list where createAt>=" + stime +" and createAt<=" + etime;
		
		if (gid > 0) {
			sql = sql + " and gid = " + gid;
		}
		if (uid > 0) {
			sql = sql + " and srcuid = " + uid;
		}
		if (type > 0) {
			sql = sql + " and type = " + type;
		}
		
		String sqlList = sql + " order by type limit " +(pages-1)*size+","+size;
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, Object> map = null;
		int icount = 0;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_pay,"slave");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sqlList);
			
			if (rs != null) {
				
				while (rs.next()) {
					map = new HashMap<String, Object>();
					map.put("gid", rs.getInt("gid"));
					map.put("gname", rs.getString("gname"));
					
					map.put("srcuid", rs.getInt("srcuid"));
					map.put("srcnickname", rs.getString("srcnickname"));
					map.put("dstuid", rs.getInt("dstuid"));
					map.put("dstnickname", rs.getString("dstnickname"));
					map.put("pricetotal", rs.getInt("pricetotal"));
					map.put("realpricetotal", rs.getInt("realpricetotal"));
					map.put("endtime", rs.getInt("endtime"));
					map.put("createAt", rs.getInt("createAt"));
					list.add(map);
				}
			}
			if (rs != null) {
				rs.close();
			}
			rs = stmt.executeQuery(sql);
			if (rs != null) {

				rs.last();
				icount = rs.getRow();
			}
			map = new HashMap<String, Object>();
			map.put("rows", list);
			map.put("total", icount);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
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
	
	/**
	 * 查看具体日期 具体礼物的中奖列表
	 * @param reward_gift_type
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public Map<String, Object> getUserListOfEgg(int reward_gift_type,Long starttime,Long endtime){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		String sql = " select uid,roomId,reward_gift_name,reward_gift_count,createAt from game_smashed_egg_log where reward_gift_type=" + reward_gift_type + " and createAt>=" + starttime + " and createAt< " + endtime +" order by createAt desc ";

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, Object> map = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_game,"slave");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			if (rs != null) {

				while (rs.next()) {
					map = new HashMap<String, Object>();
					map.put("createAt", rs.getLong("createAt"));
					map.put("uid", rs.getInt("uid"));
					map.put("roomId", rs.getInt("roomId"));
					
					map.put("reward_gift_name", rs.getString("reward_gift_name")+"*"+rs.getInt("reward_gift_count"));
					
					String nickname = "";
					String roomName = "";
					String userLevel = "";
					String anchorLevel = "";
					
					List<String> hmget = RedisOperat.getInstance().hmget(RedisContant.host, RedisContant.port6379, RedisContant.keyBaseInfoList, String.valueOf(rs.getInt("uid")),String.valueOf(rs.getInt("roomId")));
					
					if (hmget != null) {
						for(String jsonString:hmget){
							JSONObject parseObject = JSONObject.parseObject(jsonString);
							if (parseObject.getInteger("uid") == rs.getInt("uid")) {
								nickname = parseObject.get("nickname").toString();
								userLevel = parseObject.getString("userLevel");
							}else {
								roomName = parseObject.get("nickname").toString();
								anchorLevel = parseObject.getString("anchorLevel");
							}
						}
					}
					map.put("nickname", nickname);
					map.put("userLevel", userLevel);
					map.put("roomName", roomName);
					map.put("anchorLevel", anchorLevel);

					list.add(map);
				}
			}
			map = new HashMap<String, Object>();
			map.put("rows", list);
			map.put("total", 0);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
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
	
	public Map<String, Object> getEggList(int hammer, int stime, int etime, int ipage, int irows){

		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		String sql = "select datetimes,times,consume,gid1,gid2,gid3,gid4,gid5,gid6,gid7,gid8,gets from analysis_eggs where hammer = " + hammer +" and datetimes >= " + stime + " and datetimes <= " + etime;

		
		String sqlList = sql + " order by datetimes limit " +(ipage-1)*irows+","+irows;
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, Object> map = null;
		int icount = 0;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_analysis,"slave");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sqlList);
			
			if (rs != null) {

				java.text.DecimalFormat   df=new   java.text.DecimalFormat("#.##");
				while (rs.next()) {
					map = new HashMap<String, Object>();
					map.put("datetimes", rs.getString("datetimes"));
					map.put("times", rs.getInt("times"));
					map.put("consume", rs.getInt("consume"));
					
					map.put("gid1", rs.getInt("gid1"));
					map.put("gid2", rs.getInt("gid2"));
					map.put("gid3", rs.getInt("gid3"));
					map.put("gid4", rs.getInt("gid4"));
					map.put("gid5", rs.getInt("gid5"));
					map.put("gid6", rs.getInt("gid6"));
					map.put("gid7", rs.getInt("gid7"));
					map.put("gid8", rs.getInt("gid8"));
					
					map.put("gets", rs.getInt("gets"));
					
					map.put("profit", rs.getInt("consume")-rs.getInt("gets"));
					if (rs.getInt("consume") > 0) {
						map.put("odds", df.format(rs.getInt("gets")*100.0/rs.getInt("consume")));
					}else {
						map.put("odds", 0);
					}
					list.add(map);
				}
			}
			if (rs != null) {
				rs.close();
			}
			rs = stmt.executeQuery(sql);
			if (rs != null) {
				rs.last();
				icount = rs.getRow();
			}
			map = new HashMap<String, Object>();
			map.put("rows", list);
			map.put("total", icount);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
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
	
	public Map<String, Object> getPromotList(int id){

		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		String sql = "select a.id,a.gid,b.gname,b.gprice,a.promotionName,a.isvalid,a.discount,a.disPrice,a.isvalid,a.starttime,a.endtime,c.username as adminName "
				+ " from config_gift_promotion a "
				+ " left join config_giftlist b on a.gid=b.gid "
				+ " left join zhu_admin.admin_user c on a.adminid=c.uid ";
		if (id > 0) {
			sql = sql+" where a.id="+id;
		}
		sql = sql + " order by a.id desc limit 30 ";
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, Object> map = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_config,"slave");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			if (rs != null) {

				while (rs.next()) {
					map = new HashMap<String, Object>();
					map.put("id", rs.getInt("id"));
					map.put("promotionName", rs.getString("promotionName"));
					
					map.put("gid", rs.getInt("gid"));
					map.put("gname", rs.getString("gname"));
					map.put("gprice", rs.getInt("gprice"));
					map.put("discount", rs.getInt("discount"));
					map.put("disPrice", rs.getInt("disPrice"));
					map.put("isvalid", rs.getInt("isvalid"));
					map.put("starttime", rs.getInt("starttime"));
					map.put("endtime", rs.getInt("endtime"));
					map.put("adminName", rs.getString("adminName"));
					list.add(map);
				}
			}
			map = new HashMap<String, Object>();
			map.put("rows", list);
			map.put("total", 30);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
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
	
	public Map<String, Object> getPromotInfo(int id){

		String sql = "select a.id,a.gid,a.promotionName,a.isvalid,a.discount,a.disPrice,a.isvalid,a.starttime,a.endtime "
				+ " from config_gift_promotion a where a.id="+id;
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, Object> map = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_config,"slave");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			if (rs != null) {
				while (rs.next()) {
					map = new HashMap<String, Object>();
					map.put("id", rs.getInt("id"));
					map.put("promotionName", rs.getString("promotionName"));
					map.put("gid", rs.getInt("gid"));
					map.put("discount", rs.getInt("discount"));
					map.put("disPrice", rs.getInt("disPrice"));
					map.put("isvalid", rs.getInt("isvalid"));
					map.put("starttime", rs.getInt("starttime"));
					map.put("endtime", rs.getInt("endtime"));
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
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

	
	public List<Map<String, Object>> getMallGift(){

		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		String sql = "select gid,gname,gprice from config_giftlist where subtype in(3,4,5,7,8) and isvalid=1 ";
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, Object> map = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_config,"slave");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			if (rs != null) {

				while (rs.next()) {
					map = new HashMap<String, Object>();
					map.put("gid", rs.getInt("gid"));
					map.put("gname", rs.getString("gname")+","+rs.getInt("gprice"));
					list.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return list;
	}
	
	/**
	 * 添加促销信息
	 * @param gid
	 * @param promotionName
	 * @param discount
	 * @param isvalid
	 * @param starttime
	 * @param endtime
	 * @param adminid
	 * @param disPrice
	 * @return
	 */
	public int promotAdd(int gid,String promotionName,int discount,int isvalid,Long starttime,Long endtime,int adminid,int disPrice){

		String sql = "insert into config_gift_promotion(gid,promotionName,discount,disPrice,isvalid,starttime,endtime,addtime,adminid)value(?,?,?,?,?,?,?,?,?)";
		
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;
		
		try {
			Long lgNow = System.currentTimeMillis()/1000;
			con = DbUtil.instance().getCon(constant.db_zhu_config,"master");
			stmt = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, gid);
			stmt.setString(2, promotionName);
			stmt.setInt(3, discount);
			stmt.setInt(4, disPrice);
			stmt.setInt(5, isvalid);
			stmt.setLong(6, starttime);
			stmt.setLong(7, endtime);
			stmt.setLong(8, lgNow);
			stmt.setInt(9, adminid);
			
			stmt.executeUpdate();
			
			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				result = rs.getInt(1);
			}
			
			if (result > 0) {
				String current_version = String.format("gid:%d,promotionName:%s,discount:%d,disPrice:%d,isvalid:%d,starttime:%d,endtime:%d", gid,promotionName,discount,disPrice,isvalid,starttime,endtime);
				AddOperationLog("config_gift_promotion", String.valueOf(result), "新增促销活动", 1, "", current_version, adminid);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
	
	public int promotEdit(int gid,String promotionName,int discount,int isvalid,Long starttime,Long endtime,int id,int disPrice,int adminid){

		String sql = "update config_gift_promotion set gid=?,promotionName=?,discount=?,disPrice=?,isvalid=?,starttime=?,endtime=? where id=?";
		
		Connection con = null;
		PreparedStatement stmt = null;
		int result = 0;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_config,"master");
			stmt = con.prepareStatement(sql);
			stmt.setInt(1, gid);
			stmt.setString(2, promotionName);
			stmt.setInt(3, discount);
			stmt.setInt(4, disPrice);
			stmt.setInt(5, isvalid);
			stmt.setLong(6, starttime);
			stmt.setLong(7, endtime);
			stmt.setInt(8, id);
			
			result = stmt.executeUpdate();
			if (result > 0) {
				Map<String, Object> promotList = getPromotInfo(id);
				String previous_version =JSONObject.toJSONString(promotList);
				String current_version = String.format("gid:%d,promotionName:%s,discount:%d,disPrice:%d,isvalid:%d,starttime:%d,endtime:%d", gid,promotionName,discount,disPrice,isvalid,starttime,endtime);
				AddOperationLog("config_gift_promotion", String.valueOf(id), "修改促销活动", 2, previous_version, current_version, adminid);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		return result;
	}
	
	/**
	 * 获取用户提现记录
	 * @param isSecc
	 * @param starttime
	 * @param endtime
	 * @param uid
	 * @param pages
	 * @param size
	 * @return
	 */
	public Map<String, Object> getWithdraw(int isSecc,Long starttime,Long endtime, int uid,int pages,int size){
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		String sql = " select id,billno,uid,amount,credit,isSecc,createAt from pay_withdraw where createAt>= "+starttime + " and createAt < "+endtime;
		if (isSecc >= 0) {
			sql = sql + " and isSecc = " + isSecc;
		}
		if (uid > 0 ) {
			sql = sql + " and uid = " + uid;
		}
		String sqlList = sql + " order by createAt desc limit "+(pages - 1)*size+"," + size;
				
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, Object> map = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_pay,"slave");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sqlList);
			
			if (rs != null) {

				while (rs.next()) {
					map = new HashMap<String, Object>();
					
					String nickname = "";
					String hget = RedisOperat.getInstance().hget(RedisContant.host, RedisContant.port6379, RedisContant.keyBaseInfoList, String.valueOf(rs.getInt("uid")));
					if (StringUtils.isNotEmpty(hget)) {
						JSONObject parseObject = JSONObject.parseObject(hget);
						nickname = parseObject.get("nickname").toString();
					}
					
					map.put("id", rs.getInt("id"));
					map.put("billno", rs.getString("billno"));
					map.put("uid", rs.getInt("uid"));
					
					map.put("nickname", nickname);
					
					map.put("amount", rs.getString("amount"));
					map.put("credit", rs.getInt("credit"));
					map.put("isSecc", rs.getInt("isSecc"));
					map.put("createAt", rs.getInt("createAt"));
					list.add(map);
				}
			}

			if (rs != null) {
				rs.close();
			}
			int icount = 0;
			rs = stmt.executeQuery(sql);
			if (rs != null) {

				rs.last();
				icount = rs.getRow();
			}
			
			map = new HashMap<String, Object>();
			map.put("rows", list);
			map.put("total", icount);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
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
	

	
	/**
	 * 通过ID 获取用户提现记录
	 * @param isSecc
	 * @param starttime
	 * @param endtime
	 * @param uid
	 * @param pages
	 * @param size
	 * @return
	 */
	public Map<String, Object> getWithdrawBy(int id){
		
		String sql = " select id,billno,uid,amount,credit,isSecc,createAt from pay_withdraw where id>= "+id;
				
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, Object> map = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_pay,"slave");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			if (rs != null) {

				while (rs.next()) {
					map = new HashMap<String, Object>();
					
					String nickname = "";
					String hget = RedisOperat.getInstance().hget(RedisContant.host, RedisContant.port6379, RedisContant.keyBaseInfoList, String.valueOf(rs.getInt("uid")));
					if (StringUtils.isNotEmpty(hget)) {
						JSONObject parseObject = JSONObject.parseObject(hget);
						nickname = parseObject.get("nickname").toString();
					}
					
					map.put("id", rs.getInt("id"));
					map.put("billno", rs.getString("billno"));
					map.put("uid", rs.getInt("uid"));
					map.put("nickname", nickname);
					
					map.put("amount", rs.getString("amount"));
					map.put("credit", rs.getInt("credit"));
					map.put("isSecc", rs.getInt("isSecc"));
					map.put("createAt", rs.getInt("createAt"));
					
					break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
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
	
	public Map<String, Object> getReportalbum(int status,Long starttime,Long endtime,int pages,int size){
		

		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		String sql = " select id,createAt,report_uid as uid,report_pid as pid,copy_url as url,status from report_album";
		if(starttime != null) {
			sql = sql+" where createAt >= "+starttime;
		}
		if(endtime != null) {
			if(starttime != null) {
				sql = sql+" and createAt < "+endtime;
			}else {
				
			}
		}
		if(starttime != null || endtime != null) {
			sql = sql + " and status = " + status;
		}else {
			sql = sql + " where status = " + status;
		}
		
		String sqlList = sql + " order by createAt desc limit "+(pages - 1)*size+"," + size;
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, Object> map = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin,"slave");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sqlList);
			if (rs != null) {

				while (rs.next()) {
					map = new HashMap<String, Object>();
					
					String nickname = "";
					String hget = RedisOperat.getInstance().hget(RedisContant.host, RedisContant.port6379, RedisContant.keyBaseInfoList, String.valueOf(rs.getInt("uid")));
					if (StringUtils.isNotEmpty(hget)) {
						JSONObject parseObject = JSONObject.parseObject(hget);
						nickname = parseObject.get("nickname").toString();
					}
					map.put("id", rs.getInt("id"));
					map.put("createAt", rs.getInt("createAt"));
					map.put("uid", rs.getInt("uid"));
					map.put("pid", rs.getInt("pid"));
					map.put("nickname", nickname);
					
					map.put("url", rs.getString("url"));
					map.put("status", rs.getInt("status"));
					list.add(map);
				}
			}

			if (rs != null) {
				rs.close();
			}
			int icount = 0;
			rs = stmt.executeQuery(sql);
			if (rs != null) {

				rs.last();
				icount = rs.getRow();
			}
			
			map = new HashMap<String, Object>();
			map.put("rows", list);
			map.put("total", icount);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
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
	
	public Map<String, Object> getReportalbumInfo(int id){
		
		String sql = " select * from report_album where id = "+ id;
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, Object> map = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin,"slave");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			if (rs != null) {

				while (rs.next()) {
					map = new HashMap<String, Object>();
					
					map.put("id", rs.getInt("id"));
					map.put("report_uid", rs.getInt("report_uid"));
					map.put("report_pid", rs.getInt("report_pid"));
					
					map.put("copy_filename", rs.getString("copy_filename"));
					
					map.put("copy_url", rs.getString("copy_url"));
					map.put("report_num", rs.getInt("report_num"));
					map.put("status", rs.getInt("status"));
					map.put("createAt", rs.getInt("createAt"));
					break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
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
	
	/**
	 * 审核相册举报
	 * @param id
	 * @param status
	 * @param adminid
	 * @return
	 */
	public int verifyAlbum(int id,int status,int adminid){

		String sql = " update report_album set status= "+ status +" where id= "+ id;

		Connection con = null;
		PreparedStatement stmt = null;
		int result = 0;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin,"master");
			stmt = con.prepareStatement(sql);
			
			result = stmt.executeUpdate();
			if (result > 0) {
				Map<String, Object> reportalbumInfo = getReportalbumInfo(id);
				String previous_version = JSONObject.toJSONString(reportalbumInfo);
				String current_version = String.format("status:%d", status);
				AddOperationLog("report_album", String.valueOf(id), "审核相册举报", 2, previous_version, current_version, adminid);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
	
	public int verifyWithdraw(int id,String billno,int adminid){

		int result = 0;
		Map<String, Object> wdMap = getWithdrawBy(id);
		if (wdMap != null && "0".equals(wdMap.get("isSecc").toString())) {

			int uid = Integer.valueOf(wdMap.get("uid").toString());
			String sql = " update pay_withdraw set isSecc= "+ 2 +" where id= "+ id + " and billno = '" + billno + "'  and isSecc = 0 ";
			
			String dbName = getDbName(uid, "zhu_user.user_asset_");
			String sql2 = " update " + dbName + " set credit=credit + " + Integer.valueOf(wdMap.get("credit").toString()) + " where uid = " + uid;

			Connection con = null;
			PreparedStatement stmt = null;
			
			try {
				con = DbUtil.instance().getCon(constant.db_zhu_pay,"master");
				con.setAutoCommit(false);
				
				stmt = con.prepareStatement(sql);
				result = stmt.executeUpdate();
				if (result == 1) {
					
					if (stmt!= null) {
						stmt.close();
					}
					stmt = con.prepareStatement(sql2);
					result = stmt.executeUpdate();
				}
				
				con.commit();
				if (result == 1) {
					Unirest.get(Constant.business_server_url+"/admin/refreshUserAsset?uid="+uid).asJson();
					AddOperationLog("pay_withdraw", String.valueOf(id), "审核提现", 2, "{isSecc:0}", "{isSecc:2}", adminid);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (stmt != null) {
						stmt.close();
					}
					if (con != null) {
						con.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return result;
	}
	
	/**
	 * 获取动态广场 推荐主播列表数据
	 * @param pages
	 * @param size
	 * @return
	 */
	public Map<String, Object> getRecommend(int pages,int size){

		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		String sql = " select a.id,a.uid,a.sort,a.isvalid,a.adminname as operatname,c.unionname as uname,c.unionid,c.adminname"
				+ " from web_recommend_anchor a "
				+ " left join zhu_union.union_anchor_ref b on a.uid = b.anchorid "
				+ " left join zhu_union.union_info c on b.unionid = c.unionid "
				+ " order by isvalid desc,id desc ";

		String sqlList = sql + " limit "+(pages - 1)*size+"," + size;
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, Object> map = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_web,"slave");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sqlList);
			if (rs != null) {

				while (rs.next()) {
					map = new HashMap<String, Object>();
					
					String hget = RedisOperat.getInstance().hget(RedisContant.host, RedisContant.port6379, RedisContant.keyBaseInfoList, String.valueOf(rs.getInt("uid")));
					if (StringUtils.isNotEmpty(hget)) {
						JSONObject parseObject = JSONObject.parseObject(hget);

						map.put("anchorLevel", parseObject.get("anchorLevel").toString());
						map.put("recommend", parseObject.get("recommend").toString());
						map.put("nickname", parseObject.get("nickname").toString());
					}else {
						map.put("anchorLevel", -1);
						map.put("recommend", -1);
						map.put("nickname", rs.getInt("uid"));
					}
					
					map.put("id", rs.getInt("id"));
					map.put("uid", rs.getInt("uid"));
					map.put("sort", rs.getInt("sort"));
					map.put("isvalid", rs.getInt("isvalid"));
					map.put("adminname", rs.getString("adminname"));
					map.put("operatname", rs.getString("operatname"));
					map.put("uname", rs.getString("uname"));
					map.put("unionid", rs.getInt("unionid"));
					
					list.add(map);
				}
			}

			if (rs != null) {
				rs.close();
			}
			int icount = 0;
			rs = stmt.executeQuery(sql);
			if (rs != null) {

				rs.last();
				icount = rs.getRow();
			}
			
			map = new HashMap<String, Object>();
			map.put("rows", list);
			map.put("total", icount);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
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

	/**
	 * 获取动态广场 推荐主播列表数据
	 * @param pages
	 * @param size
	 * @return
	 */
	public Map<String, Object> getRecommendByUid(int uid){

		String sql = " select id,uid,sort,isvalid,adminname,adminid,addtime "
				+ " from web_recommend_anchor where uid = " + uid ;
				
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, Object> map = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_web,"slave");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			if (rs != null) {

				while (rs.next()) {
					map = new HashMap<String, Object>();
					
					map.put("id", rs.getInt("id"));
					map.put("uid", rs.getInt("uid"));
					map.put("sort", rs.getInt("sort"));
					map.put("isvalid", rs.getInt("isvalid"));
					map.put("adminname", rs.getString("adminname"));
					map.put("adminid", rs.getInt("adminid"));
					map.put("addtime", rs.getLong("addtime"));
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
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

	/**
	 * 获取动态广场 推荐主播列表数量
	 * @param pages
	 * @param size
	 * @return
	 */
	public int getRecommends(){

		String sql = " select count(*) as cnts "
				+ " from web_recommend_anchor where isvalid = 1" ;
				
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		int icount = 0;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_web,"slave");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			if (rs != null) {

				while (rs.next()) {
					icount = rs.getInt("cnts");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return icount;
	}
	
	/**
	 * 新增广场主播推荐
	 * @param uid
	 * @param sort
	 * @return
	 */
	public int addRecommend(int uid,int sort,int adminid,String adminname){
		
		int result = 0;
		Long lgNow = System.currentTimeMillis()/1000;
		
		Boolean blNew = false;
		String sql = "";
		
		Map<String, Object> recommendInfo = getRecommendByUid(uid);
		if (recommendInfo == null) {
			// 新增 有效数据不能超过50条
			int recommends = getRecommends();
			if (recommends > 50) {
				return 0;
			}
			blNew = true;
			sql = " insert into web_recommend_anchor(uid,sort,isvalid,adminname,adminid,addtime)value(?,?,1,?,?,?)";
		}else {
			// 修改
			if ("1".equals(recommendInfo.get("isvalid").toString()) && sort == Integer.valueOf(recommendInfo.get("sort").toString())) {
				return 1;
			}
			sql = " update web_recommend_anchor set isvalid=1,sort="+sort+" where uid="+uid;
		}
		
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_web, constant.db_master);
			
			stmt = con.prepareStatement(sql);
			
			if (blNew) {
				stmt.setInt(1, uid);
				stmt.setInt(2, sort);
				stmt.setString(3, adminname);
				stmt.setInt(4, adminid);
				stmt.setLong(5, lgNow);
			}
			
			result = stmt.executeUpdate();
			
			AddOperationLog("web_recommend_anchor", String.valueOf(uid), blNew?"新增推荐":"修改推荐", 2, blNew?JSONObject.toJSONString(recommendInfo):"", "{uid:"+uid+",sort:"+sort+"}", adminid);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 新增广场主播推荐
	 * @param uid
	 * @param sort
	 * @return
	 */
	public int delRecommend(int id, int adminid){
		
		int result = 0;
		
		String sql = " update web_recommend_anchor set isvalid=0 where id="+id;
		
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_web, constant.db_master);
			
			stmt = con.prepareStatement(sql);
			result = stmt.executeUpdate();
			
			AddOperationLog("web_recommend_anchor", String.valueOf(id), "删除推荐", 1, "", "{isvalid:0}", adminid);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
	
	public Map<String, Object> getChannelInfoByCode(String channelCode){
		
		Map<String, Object> mapResult = null;
		
		String sql = "select a.id,a.channelName,a.channelCode,a.isvalid,a.addtime from web_channel a where a.channelCode = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_web, constant.db_slave);
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, channelCode);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				while(rs.next()){
					mapResult = new HashMap<String, Object>();
					mapResult.put("id", rs.getInt("id"));
					mapResult.put("channelName", rs.getString("channelName"));
					mapResult.put("channelCode", rs.getString("channelCode"));
					mapResult.put("isvalid", rs.getInt("isvalid"));
					mapResult.put("addtime", rs.getLong("addtime"));
				}
				rs.close();
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
		return mapResult;
	}
	
	
	public Map<String, Object> getChannelList(int status,int platform,String channelName,int page,int size){
		
		Map<String, Object> mapResult = new HashMap<String, Object>();
		
		String sql = "select a.id,a.channelName,a.channelCode,a.isvalid,a.addtime,a.edittime,a.loginport,a.platform,b.username from web_channel a,zhu_admin.admin_user b where a.adminid=b.uid and a.isvalid=? and a.platform=?";
		if (!StringUtils.isEmpty(channelName)) {
			sql = sql + " and a.channelName like ?";
		}
		String sqlList = sql + " order by a.channelCode limit " + (page-1)*size + "," + size;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_web, constant.db_slave);
			pstmt = con.prepareStatement(sqlList);
			pstmt.setInt(1, status);
			pstmt.setInt(2, platform);
			if (StringUtils.isNotEmpty(channelName)) {
				pstmt.setString(3, "%"+channelName+"%");
			}
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				while(rs.next()){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", rs.getInt("id"));
					map.put("channelName", rs.getString("channelName"));
					map.put("channelCode", rs.getString("channelCode"));
					map.put("isvalid", rs.getInt("isvalid"));
					map.put("addtime", rs.getLong("addtime"));
					map.put("username", rs.getString("username"));
					map.put("platform", rs.getInt("platform"));
					map.put("loginport", rs.getInt("loginport"));
					map.put("edittime", rs.getLong("edittime"));
					list.add(map);
				}
				
				mapResult.put("rows", list);
			}
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, status);
			pstmt.setInt(2, platform);
			if (StringUtils.isNotEmpty(channelName)) {
				pstmt.setString(3, "%"+channelName+"%");
			}
			rs = pstmt.executeQuery();
			int total = 0;
			if (rs != null) {
				rs.last();
				total = rs.getRow();
			}
			mapResult.put("total", total);
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
		return mapResult;
	}
	
	public List<Object> getAllChannelName(int status,int platform){
		String sql = "select channelCode,channelName from web_channel a,zhu_admin.admin_user b where a.adminid=b.uid and a.isvalid=? and a.platform=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ChannelModel channelModel = null;
		List<Object> list = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_web, constant.db_slave);
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, status);
			pstmt.setInt(2, platform);
			rs = pstmt.executeQuery();
			if (rs != null) {
				list = new ArrayList<Object>();
				while(rs.next()){
					channelModel = new ChannelModel().populateFromResultSet(rs);
					list.add(channelModel);
				}
			}
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
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
		return list;
	}
	
	public int addChannel(String channelCode,String channelName,int isvalid,int adminid,int loginport,int platform){
		int iResult = 0;
		Map<String, Object> channelInfoByCode = getChannelInfoByCode(channelCode);
		if (channelInfoByCode == null) {
			
			String sql = "insert into web_channel(channelName,channelCode,isvalid,adminid,addtime,loginport,platform)value(?,?,?,?,?,?,?)";
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				con = DbUtil.instance().getCon(constant.db_zhu_web, constant.db_master);
				pstmt = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				pstmt.setString(1, channelName);
				pstmt.setString(2, channelCode);
				pstmt.setInt(3, isvalid);
				pstmt.setInt(4, adminid);
				pstmt.setLong(5, System.currentTimeMillis()/1000);
				pstmt.setInt(6, loginport);
				pstmt.setLong(7,platform);
				iResult = pstmt.executeUpdate();
				if (iResult==1) {
					String current_version = "{channelName:%s,channelCode:%s,isvalid:%d}";
					AddOperationLog("web_channel", "0", "新增渠道", 1, "", String.format(current_version, channelName,channelName,isvalid), adminid);
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
		return iResult;
	}
	
	public int editChannel(String id,int isvalid,String channelCode,String channelName,int platform,int loginport){
			int iResult = 0;
			String sql = "update web_channel set channelCode='"+channelCode+"',channelName='"+channelName+"',platform="+platform+",loginport="+loginport+",isvalid ="+isvalid+",edittime = "+System.currentTimeMillis()/1000+" where id="+id;
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
				con = DbUtil.instance().getCon(constant.db_zhu_web, constant.db_master);
				pstmt = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				iResult = pstmt.executeUpdate();
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
		return iResult;
	}
	
	public List<Map<String, Object>> getChannelForSelect(){

		List<Map<String, Object>> listResult = new ArrayList<Map<String,Object>>();
		
		Map<String, Object> _map = new HashMap<String, Object>();
		_map.put("channelName", "全部");
		_map.put("channelCode", "");
		listResult.add(_map);
		
		String sql = "select channelName,channelCode from web_channel where isvalid=1 ";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_web, constant.db_slave);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				while(rs.next()){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("channelName", rs.getString("channelName"));
					map.put("channelCode", rs.getString("channelCode"));
					listResult.add(map);
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
		return listResult;
	}
}