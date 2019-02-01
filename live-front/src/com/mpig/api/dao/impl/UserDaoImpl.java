package com.mpig.api.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.mpig.api.SqlTemplete;
import com.mpig.api.dao.IUserDao;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.utils.StringUtils;
import com.mpig.api.utils.VarConfigUtils;
import com.mysql.jdbc.Statement;

/**
 * @author zyl
 * @time 2016-7-25
 */
@Repository
public class UserDaoImpl implements IUserDao{
	
	private static final Logger logger = Logger.getLogger(UserDaoImpl.class);
	
	@Override
	public Map<String, Object> getSinaVerified(Integer uid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
			statement = conn.prepareStatement(StringUtils.getSqlString(SqlTemplete.SQL_selSinaVerifiedByUid, "user_base_info_", uid));
			DBHelper.setPreparedStatementParam(statement,uid);
			rs = statement.executeQuery();
			if (rs != null && rs.next()) {
				model.put("verified", rs.getInt("verified"));
				model.put("verified_reason", rs.getString("verified_reason"));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
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
				logger.error(e2.getMessage());
			}
		}
		return model;
	
	}
	
	public int addUserCover(int uId,String picCover,String picCover1,String picCover2){
		int iResult = 0;
		String sql = "insert into user_cover_check (uid,picCover,picCover1,picCover2) values (?,?,?,?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DataSource.instance.getPool(VarConfigUtils.dbZhuUnion).getConnection();
			pstmt = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, uId);
			pstmt.setString(2, picCover);
			pstmt.setString(3, picCover1);
			pstmt.setString(4, picCover2);
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				iResult = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("addUserCover-Exception:" + e.getMessage()+"	;sql : "+sql);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				logger.error("addUserCover-finally-Exception:" + e2.getMessage());
			}
		}
		return iResult;
	}
	public boolean updUserCover(int id,String picCover,String picCover1,String picCover2){
		int iResult = 0;
		String sql = "update user_cover_check";
		if(picCover!=null){
			sql += "	set picCover = ?";
		}
		if(picCover1!=null){
			sql += "	set picCover1 = ?";
		}
		if(picCover2!=null){
			sql += "	set picCover2 = ?";
		}
		sql += " where id = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DataSource.instance.getPool(VarConfigUtils.dbZhuUnion).getConnection();
			pstmt = con.prepareStatement(sql);
			if(picCover!=null){
				pstmt.setString(1, picCover);
			}
			if(picCover1!=null){
				pstmt.setString(1, picCover1);
			}
			if(picCover2!=null){
				pstmt.setString(1, picCover2);
			}
			pstmt.setInt(2, id);
			iResult = pstmt.executeUpdate();
			if (iResult > 0) {
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			logger.error("updUserCover-Exception:" + e.getMessage()+"	;sql : "+sql);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				logger.error("updUserCover-finally-Exception:" + e2.getMessage());
			}
		}
		return false;
	}
	
	public Map<String, Object> isNullId(int uId,int status){
		String sql = "";
		if(status==1){			//审核中或者审核驳回，则返回上次上传审核通过的封面图片,如果第一次上传，则只返回审核状态和驳回原因
			sql = "select id,uid,picCover,picCover1,picCover2,status,cause from user_cover_check where uid = ? and status = 1 order by id desc limit 1";
		}else if(status==0){			//审核通过，则返回最新上传的封面图片
			sql = "select id,uid,picCover,picCover1,picCover2,status,cause from user_cover_check where uid = ? and status = 0 order by id desc limit 1";
		}else{
			sql = "select id,status,cause from user_cover_check where uid = ? and status = 2 order by id desc limit 1";
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			con = DataSource.instance.getPool(VarConfigUtils.dbZhuUnion).getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, uId);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				if(status==1||status==0){
					resultMap.put("id", rs.getInt("id"));
					resultMap.put("picCover", rs.getString("picCover"));
					resultMap.put("picCover1", rs.getString("picCover1"));
					resultMap.put("picCover2", rs.getString("picCover2"));
					resultMap.put("status", rs.getInt("status"));
					resultMap.put("cause", rs.getString("cause"));
					resultMap.put("uid", rs.getString("uid"));
				}else{
					resultMap.put("id", rs.getInt("id"));
					resultMap.put("status", rs.getInt("status"));
					resultMap.put("cause", rs.getString("cause"));
				}
			}
		} catch (Exception e) {
			logger.error("isNullId-Exception:" + e.getMessage()+"	;sql : "+sql);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				logger.error("isNullId-finally-Exception:" + e2.getMessage());
			}
		}
		return resultMap;
	}
	
	public Map<String, Object> getStatus(int uId){
		String sql = "select status,cause from user_cover_check where uid = ? order by id desc limit 1";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			con = DataSource.instance.getPool(VarConfigUtils.dbZhuUnion).getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, uId);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				resultMap.put("status", rs.getInt("status"));
				resultMap.put("cause", rs.getString("cause"));
			}
		} catch (Exception e) {
			logger.error("getStatus-Exception:" + e.getMessage()+"	sql	: "+sql);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				logger.error("getStatus-finally-Exception:" + e2.getMessage());
			}
		}
		return resultMap;
	}
	
	public Map<String, Object> getNewestRecord(int uId){
		String sql = "select picCover,picCover1,picCover2 from user_cover_check where uid = ? order by id desc limit 1";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			con = DataSource.instance.getPool(VarConfigUtils.dbZhuUnion).getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, uId);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				resultMap.put("picCover", rs.getString("picCover"));
				resultMap.put("picCover1", rs.getString("picCover1"));
				resultMap.put("picCover2", rs.getString("picCover2"));
			}
		} catch (Exception e) {
			logger.error("getStatus-Exception:" + e.getMessage()+"	sql	: "+sql);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				logger.error("getStatus-finally-Exception:" + e2.getMessage());
			}
		}
		return resultMap;
	}
}
