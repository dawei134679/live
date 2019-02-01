package com.tinypig.admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tinypig.admin.constant;
import com.tinypig.admin.util.DbUtil;

public class UserGuardInfoDao {
	
	static private UserGuardInfoDao ins = new UserGuardInfoDao();
	private UserGuardInfoDao(){
		super();
	}
	
	public static UserGuardInfoDao getIns(){
		return ins;
	}
	
	/**
	 * 获取所有过期的,经验值大于0的守护
	 * @return
	 */
	public List<HashMap<String, Object>> getExpireGuard(){
		List<HashMap<String, Object>>  list =  new ArrayList<HashMap<String, Object>>();
		String sql = "select uid,roomid,gid from user_guard_info WHERE exp>0 AND cushiontime < UNIX_TIMESTAMP(NOW()) and isdel=0";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("uid", rs.getString("uid"));
					map.put("roomid", rs.getString("roomid"));
					map.put("gid", rs.getString("gid"));
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
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return list;
	}
	/**
	 * 修改守护经验值
	 * @param uid
	 * @param roomid
	 * @param gid
	 * @param exp
	 * @return
	 */
	public int updateExp(int uid, int roomid,int gid, int exp){
		String sql = "UPDATE user_guard_info SET exp = exp-?  WHERE uid = ? AND roomid = ? AND gid = ? AND exp>=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		int rs = 0;
		try {
			conn = DbUtil.instance().getCon(constant.db_zhu_user,"master");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, exp);
			pstmt.setInt(2, uid);
			pstmt.setInt(3, roomid);
			pstmt.setInt(4, gid);
			pstmt.setInt(5, exp);
			rs = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return rs;
	}
	/**
	 * 增加经验值相关的记录
	 * @param uid
	 * @param roomid
	 * @param exp
	 * @param type
	 * @return
	 */
	public int addExpRecord(int uid, int roomid, int gid, int exp, int type){
		String sql = "INSERT INTO user_guard_exp_record(uid,roomid,gid,date,type,exp)VALUE(?,?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement pstmt = null;
		int rs = 0;
		try {
			conn = DbUtil.instance().getCon(constant.db_zhu_user,"master");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, uid);
			pstmt.setInt(2, roomid);
			pstmt.setInt(3, gid);
			pstmt.setLong(4, System.currentTimeMillis()/1000);
			pstmt.setInt(5, 4);
			pstmt.setInt(6, exp);
			rs = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return rs;
	}
	
	/**
	 * 查询今天该用户的身份是否已经被扣除过经验值
	 * @return
	 */
	public int selExpRecordForDate(int uid, int roomid, int gid){
		String sql = "select count(id) as count from user_guard_exp_record WHERE  from_unixtime(date,'%Y%m%d') = date_format(now(),'%Y%m%d') AND type = 4 AND uid = ? AND roomid=? AND gid = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int rsc = 0;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user,"slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, uid);
			pstmt.setInt(2, roomid);
			pstmt.setInt(3, gid);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					rsc = rs.getInt("count");
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
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return rsc;
	}
}
