package com.tinypig.admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.mashape.unirest.http.Unirest;
import com.tinypig.admin.constant;
import com.tinypig.admin.model.CheckUserModel;
import com.tinypig.admin.redis.service.OtherRedisService;
import com.tinypig.admin.util.Constant;
import com.tinypig.admin.util.DbUtil;

public class CheckUserDao {

	private final static CheckUserDao instance = new CheckUserDao();

	public static CheckUserDao getInstance() {
		return instance;
	}

	public int CheckUserTotal(String uid, String cardId,String method){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select count(*) as cnt from user_authentication where 1=1 ";

		if (StringUtils.isNotEmpty(uid)){
			sql = sql+" and uid= "+Integer.valueOf(uid);

		}
		if (StringUtils.isNotEmpty(cardId)){
			sql = sql + " and cardId= '"+ StringEscapeUtils.escapeSql(cardId)+"'";
		}
		if (StringUtils.isNotEmpty(method)){
			if("wait".equals(method)){
				sql =sql + "and auditStatus=1";
			}else if("already".equals(method)){
				sql =sql + "and auditStatus in(2,3)";
			}
		}
		try{
			con = DbUtil.instance().getCon(constant.db_zhu_user,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()){
				return rs.getInt(1);
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (con != null) con.close();

			}catch (Exception e){

			}
		}
		return 0;
	}
	
	/**
	 * 获取所有审核人员列表
	 * 
	 * @return
	 */
	public List<CheckUserModel> getCheckList(int rows,int page) {
		
		List<CheckUserModel> list = new ArrayList<CheckUserModel>();
		int startIndex = rows*(page-1);
		String sql = "SELECT * FROM user_authentication limit "+startIndex+","+rows;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					CheckUserModel cUserModel = new CheckUserModel().populateFromResultSet(rs);
					if (cUserModel != null) {
						list.add(cUserModel);
					}
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public List<CheckUserModel> getWait(int rows,int page) {
		int startIndex = rows*(page-1);
		List<CheckUserModel> list = new ArrayList<CheckUserModel>();
		String sql = "SELECT * FROM user_authentication where auditStatus=1 limit "+startIndex+","+rows;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					CheckUserModel cUserModel = new CheckUserModel().populateFromResultSet(rs);
					if (cUserModel != null) {
						list.add(cUserModel);
					}
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public List<CheckUserModel> getAlready(int rows,int page) {
		List<CheckUserModel> list = new ArrayList<CheckUserModel>();
		int startIndex = rows*(page-1);
		String sql = "SELECT * FROM user_authentication where auditStatus in (2,3) limit "+startIndex+","+rows;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					CheckUserModel cUserModel = new CheckUserModel().populateFromResultSet(rs);
					if (cUserModel != null) {
						list.add(cUserModel);
					}
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public List<CheckUserModel> getCheckInfo(String uid){
		List<CheckUserModel>  list =  new ArrayList<CheckUserModel>();
		int sufix=Integer.parseInt(uid) % 100;
		String dbname = "user_base_info_" + (sufix < 10 ? "0" + sufix : sufix);
		String sql = "SELECT a.*,u.phone FROM user_authentication a left join "+dbname+" u on a.uid=u.uid where a.uid="+uid;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					CheckUserModel cUserModel = new CheckUserModel().populateFromResultSet(rs);
					if (cUserModel != null) {
						list.add(cUserModel);
					}
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public int CheckReject(String uid,String cause) throws SQLException{
		int ries=0;
		int time = (int) (System.currentTimeMillis() / 1000);
		String sql = "update user_authentication set auditStatus=2,auditAt="+time+",cause="+"'"+cause+"' where uid="+uid;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user,"master");
			pstmt = con.prepareStatement(sql);
			ries=pstmt.executeUpdate();
			if(ries == 1)
				OtherRedisService.getInstance().updateRealnameAuth(uid, 2);
			 
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ries;
	}

	public int CheckPass(String uid) throws SQLException {
		int time = (int) (System.currentTimeMillis() / 1000);
		String sql = "update user_authentication set auditStatus=3,auditAt=" + time + "  where uid=" + uid;
		Connection con = null;
		PreparedStatement pstmt = null;
		int ies=0;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user,"master");
			pstmt = con.prepareStatement(sql);
			ies=pstmt.executeUpdate();
			if(ies == 1)
				OtherRedisService.getInstance().updateRealnameAuth(uid, 3);
			 if (ies == 1) {
				 Unirest.get(Constant.business_server_url + "/admin/taskProcess?adminid=admin&uid=" + uid).asJson();
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ies;
	}

	public List<CheckUserModel> getSearch(int rows,int page,String uid, String cardId) {
		List<CheckUserModel> list = new ArrayList<CheckUserModel>();
		String sql = null;
		int startIndex = rows*(page-1);
		if (uid == "" && cardId == "") {
			sql = "SELECT * FROM user_authentication limit "+startIndex+","+rows;
		} else if (uid != "" && cardId == "") {
			sql = "SELECT * FROM user_authentication where uid=" +uid+ " limit "+startIndex+","+rows;
		} else if (cardId != "" && uid == "") {
			sql = "SELECT * FROM user_authentication where cardId=" +cardId+" limit "+startIndex+","+rows;
		} else {
			sql = "SELECT * FROM user_authentication where uid=" +uid + " and cardId=" + cardId+" limit "+startIndex+","+rows;
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					CheckUserModel cUserModel = new CheckUserModel().populateFromResultSet(rs);
					if (cUserModel != null) {
						list.add(cUserModel);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

}
