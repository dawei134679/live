package com.tinypig.admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.Unirest;
import com.tinypig.admin.constant;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.model.UnionModel;
import com.tinypig.admin.model.UserAccountModel;
import com.tinypig.admin.model.UserAsset;
import com.tinypig.admin.model.UserBaseInfo;
import com.tinypig.admin.model.UserBaseInfoModel;
import com.tinypig.admin.redis.service.RedisOperat;
import com.tinypig.admin.util.Constant;
import com.tinypig.admin.util.DateUtil;
import com.tinypig.admin.util.DbUtil;
import com.tinypig.admin.util.NoticeAPIUtile;
import com.tinypig.admin.util.RedisContant;
import com.tinypig.admin.util.StringUtil;

public class UnionDao extends BaseDao {
	
	private static final Logger logger = Logger.getLogger(UnionDao.class);
	
	static private UnionDao ins = new UnionDao();
	private UnionDao(){
		super();
	}
	
	public static UnionDao getIns(){
		return ins;
	}
	
	/**
	 * 根据公会ID和主播UID获取全部信息
	 * @param unionid
	 * @param anchoruid
	 * @return
	 */
	public Map<String, Object> getInfoByUnionIDAnchorUID(int unionid,int anchoruid){
		Map<String, Object> mapResult = null ;
		
		String sql = " select * from union_anchor_ref where unionid = " + unionid + " and anchorid = " + anchoruid;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs =  null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_union, constant.db_slave);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				mapResult = new HashMap<String, Object>();
				while(rs.next()){
					mapResult.put("id", rs.getInt("id"));
					mapResult.put("unionid", rs.getInt("unionid"));
					mapResult.put("anchorid", rs.getInt("anchorid"));
					mapResult.put("salary", rs.getInt("salary"));
					mapResult.put("rate", rs.getDouble("rate"));
					mapResult.put("remarks", rs.getString("remarks"));
					mapResult.put("isvalid", rs.getInt("isvalid"));
					mapResult.put("adminid", rs.getInt("adminid"));
					mapResult.put("addtime", rs.getLong("addtime"));
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
	
	public int editAnchorINUnionSalary(int unionid,int anchoruid,double rate,int salary,String remarks,int admin_id){
		int iResult = 0;
		
		Map<String, Object> infoByUnionIDAnchorUID = getInfoByUnionIDAnchorUID(unionid, anchoruid);
		
		if ( infoByUnionIDAnchorUID == null) {
			// 不存在
			return iResult;
		}
		
		String sql = " update union_anchor_ref set rate=?,salary=?,remarks=? where unionid=? and anchorid=? ";
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DbUtil.instance().getCon(constant.db_zhu_union,constant.db_master);
			pstmt = con.prepareStatement(sql);
			pstmt.setDouble(1, rate);
			pstmt.setInt(2, salary);
			pstmt.setString(3, remarks);
			pstmt.setInt(4, unionid);
			pstmt.setInt(5, anchoruid);
			
			iResult = pstmt.executeUpdate();
			if (iResult > 0) {
				String previous_version = "{rate:" + infoByUnionIDAnchorUID.get("rate") + ",salary:" + infoByUnionIDAnchorUID.get("salary") + ",remarks:" + infoByUnionIDAnchorUID.get("remarks") + "}";
				String current_version = "{rate:" + rate + ",salary:" + salary + ",remarks:" + remarks + "}";
				AddOperationLog("union_anchor_ref", "0", "修改主播提成", 2, previous_version, current_version, admin_id);
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
		return iResult;
	}
	/**
	 * 判断该用户 该工会是否已经存在
	 * @param uid
	 * @param unionid
	 * @param isvalid =9全部
	 * @return
	 */
	public Map<String, Object> getSupportByUid(int uid,int unionid,int isvalid){
		
		Map<String, Object> mapResult = null;
		
		String sql = "select id,uid,unionid,amount,isvalid,remarks,addtime,updtime,adminid from union_support where uid = " + uid + " and unionid = " + unionid;
		
		if (isvalid != 9 ) {
			sql = sql + " and isvalid = "+ isvalid;
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_union, constant.db_slave);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				while(rs.next()){
					mapResult = new HashMap<String, Object>();
					mapResult.put("id", rs.getInt("id"));
					mapResult.put("uid", rs.getInt("uid"));
					mapResult.put("unionid", rs.getInt("unionid"));
					mapResult.put("amount", rs.getInt("amount"));
					mapResult.put("isvalid", rs.getInt("isvalid"));
					mapResult.put("remarks", rs.getString("remarks"));
					mapResult.put("addtime", rs.getLong("addtime"));
					mapResult.put("updtime", rs.getLong("updtime"));
					mapResult.put("adminid", rs.getLong("adminid"));
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
	
	/**
	 * 判断该用户 除该工会之外是否还有扶持
	 * @param uid
	 * @param unionid
	 * @return
	 */
	public Map<String, Object> checkSupportByUid(int uid,int unionid){
		
		Map<String, Object> mapResult = null;
		
		String sql = "select id,uid,unionid,isvalid,remarks,addtime,updtime,adminid from union_support where uid = " + uid + " and unionid != " + unionid + " and isvalid=1 ";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_union, constant.db_slave);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				while(rs.next()){
					mapResult = new HashMap<String, Object>();
					mapResult.put("id", rs.getInt("id"));
					mapResult.put("uid", rs.getInt("uid"));
					mapResult.put("unionid", rs.getInt("unionid"));
					mapResult.put("isvalid", rs.getInt("isvalid"));
					mapResult.put("remarks", rs.getString("remarks"));
					mapResult.put("addtime", rs.getLong("addtime"));
					mapResult.put("updtime", rs.getLong("updtime"));
					mapResult.put("adminid", rs.getLong("adminid"));
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
	
	/**
	 * 新增扶持账号
	 * @param unionid
	 * @param uid
	 * @param isvalid =1 有效
	 * @param remarks
	 * @param admin_id
	 * @return
	 */
	public int addSupport(int unionid,int uid,int isvalid,String remarks,int admin_id){
		int iResult = 0;
		if (isvalid == 1) {
			Map<String, Object> mapSupport = checkSupportByUid(uid, unionid);
			if (mapSupport != null) {
				// 已经存在扶持工会，不能重复
				return iResult;
			}
		}
		
		String sql = " insert into union_support(uid,unionid,isvalid,remarks,addtime,updtime,adminid)value(?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			
			con = DbUtil.instance().getCon(constant.db_zhu_union, constant.db_master);
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, uid);
			pstmt.setInt(2, unionid);
			pstmt.setInt(3, isvalid);
			pstmt.setString(4, remarks);
			pstmt.setLong(5, System.currentTimeMillis()/1000);
			pstmt.setLong(6, System.currentTimeMillis()/1000);
			pstmt.setInt(7, admin_id);
			
			iResult = pstmt.executeUpdate();
			if (iResult > 0) {
				if (isvalid == 1) {
					RedisOperat.getInstance().hset(RedisContant.host, RedisContant.port6380, RedisContant.keySupport, String.valueOf(uid), String.valueOf(unionid), 0);
				}
				
				//TOSY DEBUG
				NoticeAPIUtile.noticeApi("updUnionSupport", "adminid", "admin");
//				Unirest.get(constant.url_interface+"/admin/updUnionSupport?adminid=admin").asString();
				AddOperationLog("union_support", uid + "_" + unionid, "新增扶持", 1, "", "{isvalid:"+isvalid+"}", admin_id);
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
		
		return iResult;
	}
	
	/**
	 * 修改 扶持号 扶持状态
	 * @param unionid
	 * @param uid
	 * @param isvalid
	 * @param admin_id
	 * @return
	 */
	public int editSupport(int unionid,int uid,int amount,int isvalid,int admin_id){
		
		int iResult = 0;
		String sql = " update union_support set isvalid = " + isvalid + ",amount="+ amount +",updtime = " + System.currentTimeMillis()/1000 + " where uid = " + uid + " and unionid = " + unionid ;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			con = DbUtil.instance().getCon(constant.db_zhu_union, constant.db_master);
			pstmt = con.prepareStatement(sql);
			
			iResult = pstmt.executeUpdate();
			if (iResult > 0) {
				if (isvalid == 1) {
					RedisOperat.getInstance().hset(RedisContant.host, RedisContant.port6380, RedisContant.keySupport, String.valueOf(uid), String.valueOf(unionid), 0);
				}else {
					RedisOperat.getInstance().hdel(RedisContant.host, RedisContant.port6380, RedisContant.keySupport, String.valueOf(uid));
				}
				
				//TOSY DEBUG
				NoticeAPIUtile.noticeApi("updUnionSupport", "adminid", "admin");
//				Unirest.get(constant.url_interface+"/admin/updUnionSupport?adminid=admin").asString();
				AddOperationLog("union_support", uid + "_" + unionid, "修改扶持", 2, "", "{isvalid:"+isvalid+"}", admin_id);
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
		
		return iResult;
	}
	
	/**
	 * 统计扶持号 扶持细节
	 * @return
	 */
	public List<Map<String, Object>> getSupportList(){
		
		List<Map<String, Object>> listResult = null;
		String sql = " select uid,unionid from union_support where isvalid = 1 ";
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_union, constant.db_slave);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				listResult = new ArrayList<Map<String,Object>>();
				while (rs.next()) {
					Map<String, Object> map = new HashMap<String, Object>();
					
					map.put("uid", rs.getInt("uid"));
					map.put("unionid", rs.getInt("unionid"));
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
	
	/**
	 * 获取扶持账号列表
	 * @param isvalid =9全部 =1有效 =0无效
	 * @param unionid =0全部
	 * @return
	 */
	public Map<String, Object> getSupport(int isvalid,int unionid,int page,int size){
		
		Map<String, Object> mapResult = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		String sql = " select a.unionid,b.unionname,a.uid,a.remarks,a.isvalid,a.addtime,a.updtime,a.adminid,c.username "
				+ " from union_support a,union_info b,zhu_admin.admin_user c "
				+ " where a.unionid=b.unionid and a.adminid=c.uid";
		if (isvalid !=9 ) {
			sql = sql + " and a.isvalid = " + isvalid;
		}
		if (unionid > 0) {
			sql = sql + " and a.unionid = " + unionid;
		}
		
		 String sqlList = sql +" order by a.updtime desc limit " + (page-1)*size + "," + size;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_union, constant.db_slave);
			pstmt = con.prepareStatement(sqlList);
			
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				while(rs.next()){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("unionid", rs.getString("unionid"));
					map.put("unionname", rs.getString("unionname"));
					map.put("uid", rs.getInt("uid"));
					
					UserBaseInfoModel userBaseInfo = UserDao.getInstance().getUserBaseInfo(rs.getInt("uid"), false);
					if (userBaseInfo != null) {
						map.put("nickname", userBaseInfo.getNickname());
					}else {
						map.put("nickname", "未知");
					}
					map.put("remarks", rs.getString("remarks"));
					map.put("addtime", rs.getLong("addtime"));
					map.put("updtime", rs.getLong("updtime"));
					map.put("isvalid", rs.getInt("isvalid"));
					map.put("username", rs.getString("username"));
					
					list.add(map);
				}
			}
			
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			
			int total = 0;
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				rs.last();
				total = rs.getRow();
			}
			
			mapResult.put("rows", list);
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

	/**
	 * 添加工会
	 * @param unionname 公会名称
	 * @param ownerid 公会长ID
	 * @param remark  备注
	 * @param adminuid 归属运营人UID
	 * @param operationuid 操作人UID
	 * @return
	 */
	public Boolean addUnion(String unionname, String ownerid,String remark, int adminuid, int operationuid) {
		if(null == ownerid){
			return false;
		}
		Integer oid = valueOf(ownerid);
		if(null == oid){
			return false;
		}
		String adminname = "无负责人";
		if (adminuid > 0) {
			AdminUserModel adminInfo = AdminUserDao.getInstance().getAdminInfo(adminuid);
			adminname = adminInfo.getUsername();
		}
		
		unionname = (null == unionname)?"":unionname;

		String sql = "insert into " + constant.tb_union_info + " (unionname,ownerid,createtime,remarks,adminname,adminuid,operatoruid) values (?,?,?,?,?,?,?)";
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_union,"master");
			pstmt = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

			pstmt.setString(1, unionname);
			pstmt.setInt(2, oid);
			pstmt.setLong(3, System.currentTimeMillis()/1000);
			pstmt.setString(4, remark);
			pstmt.setString(5, adminname);
			pstmt.setInt(6, adminuid);
			pstmt.setInt(7, operationuid);
			
			pstmt.executeUpdate();
			
			rs = pstmt.getGeneratedKeys();
			int iresult = 0;
			if (rs != null && rs.next()) {
				iresult = rs.getInt(1);
			}

			if (iresult > 0) {
				
				String current_version = "{unionid:"+iresult+",unionname:"+unionname+"}";
				AddOperationLog(constant.tb_union_info, String.valueOf(iresult), "新增公会", 1, "", current_version, operationuid);
				
				return true;
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public List<Integer> getAnchorsFromUnionAnchorRef(Integer unionid){
		List<Integer> rt = new ArrayList<Integer>();
		
		String sql = "select anchorid from " + constant.tb_union_anchor_ref + " where unionid=" + unionid + ";";
		DbUtil.DbSimpleQuery dbq = new DbUtil.DbSimpleQuery();
		ResultSet rs =  dbq.simpleQuery(constant.db_zhu_union, sql);
		if(null != rs){
			try {
				while(rs.next()){
					rt.add(rs.getInt("anchorid"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		dbq.release();
		return rt;
	}
	
	public List<HashMap<String, Object>> getAnchorsListAnchorRef(String page, String rows, String anchorid){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		
		StringBuffer sb = new StringBuffer();
		sb.append("select ui.unionid, ui.unionname, uar.anchorid, ui.adminname as username, uar.totalmoney, uar.remarks");
		sb.append(" from zhu_union.union_info ui, zhu_union.union_anchor_ref uar");
		sb.append(" where ui.unionid = uar.unionid");
		if(StringUtil.isNotEmpty(anchorid)){
			sb.append(" and uar.anchorid = ");
			sb.append(anchorid);
		}
		sb.append(" order by ui.unionid");
		sb.append(" limit ");
		sb.append((Integer.valueOf(page) - 1) * Integer.valueOf(rows));
		sb.append(",");
		sb.append(Integer.valueOf(rows));
		
		try {
			con = DbUtil.instance().getCon("slave");
			pstmt = con.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			if(null != rs){
				while(rs.next()){
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("anchorid", rs.getInt("anchorid"));
					map.put("unionname", rs.getString("unionname"));
					map.put("username", rs.getString("username"));
					map.put("totalmoney", rs.getLong("totalmoney") + getCurrentMonthMoneyByUid(rs.getInt("anchorid")));
					map.put("remarks", rs.getString("remarks"));
					map.put("unionid", rs.getInt("unionid"));
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
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		return list;
	}
	
	public int editAnchorUion(int unionid,int anchorid,double rate,int salary,String remarks){
		
		int executeUpdate = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		String sql = "update union_anchor_ref set rate = ?,salary = ?,remarks = ? where unionid=? and anchorid=?";
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_union, constant.db_master);
			pstmt = con.prepareStatement(sql);
			pstmt.setDouble(1, rate);
			pstmt.setInt(2, salary);
			pstmt.setString(3, remarks);
			pstmt.setInt(4, unionid);
			pstmt.setInt(5, anchorid);
			
			executeUpdate = pstmt.executeUpdate();
			
			if (executeUpdate > 0) {
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return executeUpdate;
	}
	
	public List<HashMap<String, Object>> getUnionNameList(Boolean status){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> _map = new HashMap<String, Object>();
		_map.put("unionid", "");
		_map.put("unionname", "全部");
		list.add(_map);
		
		String sql = " select unionid, unionname from union_info ";
		
		if (status != null) {
			sql = sql + " where status = " + status;
		}
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_union,"slave");
			pstmt = con.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			if(null != rs){
				while(rs.next()){
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("unionid", rs.getInt("unionid"));
					map.put("unionname", rs.getString("unionname"));
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
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		return list;
	}
	
	public int getAnchorsCount(){
		int result = 0;
		String sql = "select count(id) count from " + constant.tb_union_anchor_ref;
		DbUtil.DbSimpleQuery dbq = new DbUtil.DbSimpleQuery();
		ResultSet rs =  dbq.simpleQuery(constant.db_zhu_union, sql);
		try {
			if(rs != null && rs.next()){
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbq.release();
		return result;
	}
	
	
	private Boolean deleteUionRef(Integer unionid){
		String sql = "delete from " + constant.tb_union_anchor_ref  + " where unionid=?;";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_union,"master");
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, unionid);
			pstmt.executeUpdate();
			return true;
			
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	private Boolean deleteUionFromUnioninfo(Integer unionid){
		String sql = "delete from " + constant.tb_union_info  + " where unionid=?;";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_union,"master");
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, unionid);
			pstmt.executeUpdate();
			return true;
			
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	/**
	 * 删除工会
	 * @param unionid
	 * @return
	 */
	public Boolean deleteUnion(String unionid,int adminid){
		Integer nUnionId = valueOf(unionid);
		if(null == nUnionId){
			return false;
		}
		//获取所有主播的uid
		List<Integer> arrayUid = getAnchorsFromUnionAnchorRef(nUnionId);
		if(null != arrayUid){
			//删除工会主播关系
			for(Integer nUid:arrayUid){ 
				if(updateFamilyIdFromUser(0,nUid)){
					AddOperationLog(constant.db_zhu_user,nUid.toString(),"更新主播所在公会"+unionid,2,unionid,String.valueOf(0),adminid);
				}
			}
			deleteUionRef(nUnionId);
		}
		
		//删除工会
		if(false == deleteUionFromUnioninfo(nUnionId)){
			return false;
		}

		AddOperationLog(constant.db_zhu_union,unionid,"删除公会"+unionid,3,unionid,String.valueOf(0),adminid);
		return true;
	}
	
	public static UserBaseInfo getUserBaseInfo(Integer uid){
		UserBaseInfo data = null;
		String tail = String.format("%02d", uid%100);
		String sql = "select * from "+constant.tb_userbaseinfo +tail + " where uid="+uid+";";
		DbUtil.DbSimpleQuery dbq = new DbUtil.DbSimpleQuery();
		ResultSet rs = dbq.simpleQuery(constant.db_zhu_user, sql);
		if(null != rs){
			try {
				while(rs.next()){
					data = new UserBaseInfo().populateFromResultSet(rs);

					dbq.release();
					return data;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		dbq.release();
		return data;
	}
	
	
	private String getUserNickName(Integer uid){
		String nickname = null;
		String tail = String.format("%02d", uid%100);
		String sql = "select nickname from "+constant.tb_userbaseinfo +tail + " where uid="+uid+";";
		DbUtil.DbSimpleQuery dbq = new DbUtil.DbSimpleQuery();
		ResultSet rs = dbq.simpleQuery(constant.db_zhu_user, sql);
		if(null != rs){
			try {
				while(rs.next()){
					nickname = rs.getString("nickname");

					dbq.release();
					return nickname;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		dbq.release();
		return nickname;
	}
	
	//获取后台管理员名字
	private String getAdminName(Integer uid){
		String username = null;
		String sql = "select username from "+constant.tb_admin_user + " where uid="+uid;
		DbUtil.DbSimpleQuery dbq = new DbUtil.DbSimpleQuery();
		ResultSet rs = dbq.simpleQuery(constant.db_zhu_admin, sql);
		if(null != rs){
			try {
				while(rs.next()){
					username = rs.getString("username");

					dbq.release();
					return username;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		dbq.release();
		return username;
	}
	
	public Map<String, Object> getAnchorCoverInUnionList(int unionid,int uid,int page,int size){
		
		Map<String, Object> mapResult = new HashMap<String, Object>();
		List<Map<String, Object>> list =  new ArrayList<Map<String,Object>>();
		
		int total = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select a.unionid,a.anchorid,b.unionname from union_anchor_ref a,union_info b where a.unionid=b.unionid and b.status=1 and a.isvalid=1";
		if (unionid > 0 ) {
			sql = sql + " and a.unionid = " + unionid;
		}
		if (uid > 0 ) {
			sql = sql + " and a.anchorid = " + uid;
		}
		
		String sqllist = sql + " order by addtime desc limit "+(page -1)*size+","+size;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_union, constant.db_slave);
			pstmt = con.prepareStatement(sqllist);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				while(rs.next()){
					Map<String, Object> map = new HashMap<String, Object>();
					
					UserBaseInfoModel userBaseInfo = UserDao.getInstance().getUserBaseInfo(rs.getInt("anchorid"), false);
					if (userBaseInfo != null) {
						map.put("nickname", userBaseInfo.getNickname());
						map.put("anchorLevel", userBaseInfo.getAnchorLevel());
						map.put("headimage", userBaseInfo.getHeadimage());
						map.put("livimage", userBaseInfo.getLivimage());
						map.put("pcimg1", userBaseInfo.getPcimg1());
						map.put("pcimg2", userBaseInfo.getPcimg2());
						map.put("recommend", userBaseInfo.getRecommend());
					}else {
						map.put("nickname", "未知");
						map.put("anchorLevel", 0);
						map.put("headimage", "");
						map.put("livimage", "");
						map.put("pcimg1", "");
						map.put("pcimg2", "");
						map.put("recommend", 0);
					}
					
					map.put("unionid", rs.getInt("unionid"));
					map.put("anchorid", rs.getInt("anchorid"));
					map.put("unionname", rs.getString("unionname"));
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
			rs = pstmt.executeQuery();
			if (rs != null) {
				rs.last();
				total = rs.getRow();
			}
			mapResult.put("rows", list);
			mapResult.put("total", total);
			
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
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return mapResult;
	}
	
	
	
	/**
	 * 获取工会下的主播列表
	 * @param unionid 工会ID
	 * @param status 工会是否有效  =9表示全部
	 * @param uid 主播UID
	 * @param pages 第几页
	 * @param size 每页大小
	 * @return
	 */
	public Map<String, Object> getAnchorUnionsList(int unionid,int uid,int pages,int size){
		
		Map<String, Object> mapResult = new HashMap<String, Object>();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = " select b.anchorid,b.remarks,a.unionid,a.unionname,b.salary,b.rate,c.username,a.adminname, b.addtime as createtime "
				+ "from union_info a,union_anchor_ref b,zhu_admin.admin_user c "
				+ "where a.unionid = b.unionid and b.adminid = c.uid and b.isvalid = 1 and a.status = 1 ";
		if (unionid > 0 ) {
			sql = sql + " and a.unionid = "+ unionid;
		}
		if (uid > 0 ) {
			sql = sql + " and b.anchorid = " + uid;
		}
		String sqllist = sql + " order by a.status desc,a.unionid desc limit " + (pages-1)*size +"," + size;
		
		try {
			
			con = DbUtil.instance().getCon(constant.db_zhu_union, constant.db_slave);
			pstmt = con.prepareStatement(sqllist);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				
				while (rs.next()) {
					
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("anchorid", rs.getObject("anchorid")); // 主播UID
					map.put("unionid", rs.getObject("unionid")); 
					map.put("createtime", rs.getLong("createtime"));
					
					UserBaseInfoModel anchorBaseInfo = UserDao.getInstance().getUserBaseInfo(rs.getInt("anchorid"), false);
					if (anchorBaseInfo != null) {
						
						map.put("nickname", anchorBaseInfo.getNickname()); // 主播昵称
						map.put("anchorLevel", anchorBaseInfo.getAnchorLevel()); // 主播等级
						if (anchorBaseInfo.getIdentity() == 2) {
							map.put("identity", anchorBaseInfo.getIdentity()); // 禁播
						}else {
							map.put("identity", 0); // 开播
						}
						map.put("phone", anchorBaseInfo.getPhone()); // 绑定手机号
						map.put("recommend", anchorBaseInfo.getRecommend()); // 开播等级
						map.put("registtime", anchorBaseInfo.getRegisttime()); // 注册时间
						map.put("opentime", anchorBaseInfo.getOpentime()); // 最近开播时间
					}else {
						
						map.put("nickname", "缓存异常"); // 主播昵称
						map.put("anchorLevel", 0); // 主播等级
						map.put("identity", 1); // 开播
						map.put("phone", ""); // 绑定手机号
						map.put("recommend", 0); // 开播等级
						map.put("registtime", 0); // 注册时间
						map.put("opentime", 0); // 最近开播时间
					}
					UserAsset anchorAssetInfo = UserDao.getInstance().getUserAsset(rs.getInt("anchorid"), false);
					if (anchorAssetInfo != null) {
						map.put("credit", anchorAssetInfo.getCredit());
						map.put("creditTotal", anchorAssetInfo.getCreditTotal());
					}else {
						map.put("credit", 0);
						map.put("creditTotal", 0);
					}
					UserAccountModel userAccountByUid = UserDao.getInstance().getUserAccountByUid(rs.getInt("anchorid"), false);
					if (userAccountByUid != null && "0".equals(userAccountByUid.getStatus())) {
						int accStatus = userAccountByUid.getStatus() & 0xFF;
						if (accStatus == 0) {
							map.put("identity", 1); // 封号
						}
					}
					map.put("rate", rs.getDouble("rate"));
					map.put("salary", rs.getInt("salary"));
					map.put("unionname", rs.getObject("unionname")); // 工会名称
					map.put("adminname", rs.getObject("adminname")); // 归属者名称
					map.put("username", rs.getObject("username")); // 操作者名称
					map.put("remarks", rs.getObject("remarks"));
					
					list.add(map);
				}
				
				mapResult.put("rows", list);
				
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				
				int total = 0;
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
				if (rs != null) {
					rs.last();
					total = rs.getRow();
				}
				mapResult.put("total", total);
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
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return mapResult;
	}

	/**
	 * 获取家族列表
	 * @param onwerid 添加者uid
	 * @param unionname 家族名称
	 * @param page 第几页
	 * @param rows 每页的条数
	 * @param sort 排序字段
     * @param order 排序规则 desc  asc
     * @return
     */
	public List<UnionModel> getUnions(String onwerid, String unionname, String unionid, String status, String page, String rows, String sort, String order){
		List<UnionModel> rt = new ArrayList<UnionModel>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select * from " + constant.tb_union_info+" where 1=1 ";

		if (StringUtils.isNotEmpty(status)){
			sql = sql+" and status= "+Integer.valueOf(status);

		}
		if (StringUtils.isNotEmpty(unionid)){
			sql = sql+" and unionid= "+Integer.valueOf(unionid);

		}
		if (StringUtils.isNotEmpty(onwerid)){
			sql = sql+" and ownerid= "+Integer.valueOf(onwerid);

		}
		if (StringUtils.isNotEmpty(unionname)){
			sql = sql + " and unionname= '"+ StringEscapeUtils.escapeSql(unionname)+"'";
		}
		if (StringUtils.isNotEmpty(sort)){
			sql = sql +" order by "+StringEscapeUtils.escapeSql(sort)+" "+ StringEscapeUtils.escapeSql(order);
		}
		if (StringUtils.isNotEmpty(page)) {
			sql = sql + " limit " + (Integer.valueOf(page) - 1) * Integer.valueOf(rows) + "," + Integer.valueOf(rows);
		}

		try{
			con = DbUtil.instance().getCon(constant.db_zhu_union,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null){
				while (rs.next()) {
					UnionModel unionModel = new UnionModel().populateFromResultSet(rs);
					unionModel.setOwnername(getUserNickName(unionModel.getOwnerid()));
					unionModel.setAnchorcount(getUnionAnchorCnt(unionModel.getUnionid()));
					unionModel.setAdminname(getAdminName(unionModel.getAdminuid()));
					unionModel.setOperatorname(getAdminName(unionModel.getOperatoruid()));
					unionModel.setTotalmoney(unionModel.getTotalmoney() + getCurrentMonthMoney(unionModel.getUnionid()));
					rt.add(unionModel);
				}
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
		return rt;
	}

	/**
	 * 获取家族列表
	 * @param onwerid 添加者uid
	 * @param unionname 家族名称
	 * @param page 第几页
	 * @param rows 每页的条数
	 * @param sort 排序字段
	 * @param order 排序规则 desc  asc
	 * @return
	 */
	public int getUnionsTotal(String onwerid, String unionname){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select count(*) as cnt from " + constant.tb_union_info+" where 1=1 ";

		if (StringUtils.isNotEmpty(onwerid)){
			sql = sql+" and ownerid= "+Integer.valueOf(onwerid);

		}
		if (StringUtils.isNotEmpty(unionname)){
			sql = sql + " and unionname= '"+ StringEscapeUtils.escapeSql(unionname)+"'";
		}

		try{
			con = DbUtil.instance().getCon(constant.db_zhu_union,"slave");
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
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	/**
	 * 获取工会的主播列表
	 * @param unionid
	 * @return
	 */
	public List<UserBaseInfo> getAnchorsInfoFromUnion(String unionid,String uid,String nickname,String tmStart,String tmEnd,boolean isSuper){
		List<UserBaseInfo> anchorList = new ArrayList<UserBaseInfo>();;

		Integer nTmStart = valueOf(tmStart);
		Integer nTmEnd = valueOf(tmEnd);
		if(null == nTmStart){
			nTmStart = 0;
		}
		if(null == nTmEnd){
			nTmEnd = Integer.MAX_VALUE;
		}
		
		Integer nUid = valueOf(uid);
		Integer nUnionid = valueOf(unionid);
		if(null == nUnionid){
			return anchorList;
		}
		
		//获取主播ref的List id
		List<Integer> dataIds = getAnchorsFromUnionAnchorRef(nUnionid);
		
		for(Integer id : dataIds){
			UserBaseInfo userdata = getUserBaseInfo(id);
			if(userdata == null)
				continue;
			
			if(false == isSuper){
				String strPhone = userdata.getPhone();
				if(null != strPhone && 11 == strPhone.length()){
					userdata.setPhone(strPhone.substring(0, 3) + "****"+strPhone.substring(7));	//tosy add protection for super admin	
				}
			}
			
			UserAsset userAsset = UserDao.getInstance().getUserAsset(id,false);
			userdata.setTotalLiveTime(LiveDao.getInstance().getTotalLiveTimeByUid(id,tmStart,tmEnd));
			userdata.setValidLiveDay(LiveDao.getInstance().getValidLiveDay(id, tmStart, tmEnd));
			if(!StringUtil.isEmpty(tmStart) && !StringUtil.isEmpty(tmEnd)){
				userdata.setCredit(BillDao.getInstance().getCredit(id, tmStart, tmEnd));
			}else{
				userdata.setCredit(userAsset.getCredit());
			}
			userdata.setCreditTotal(userAsset.getCreditTotal());
			if(null != nUid){
				if(userdata.getUid() == nUid){
					anchorList.add(userdata);
					return anchorList;
				}
				continue;
			}
			
//			if(false == nickname.isEmpty()){
//				if(userdata.getNickname().toLowerCase().contains(nickname.toLowerCase())){
//					Integer rtm = userdata.getRegisttime();
//					if(nTmStart <= rtm && nTmEnd >= rtm){
//						rt.add(userdata);
//					}
//				}
//			}else{
//				Integer rtm = userdata.getRegisttime();
//				if( nTmStart <= rtm && nTmEnd >= rtm){
//					rt.add(userdata);
//				}
//			}
			anchorList.add(userdata);
			
		}
		
		return anchorList;
	}
	
	/**
	 * 获取主播列表
	 * @return
	 */
	public JSONObject getAnchorList(String page, String rows, String anchorid){
		JSONObject result = new JSONObject();
		List<UserBaseInfo> anchorList = new ArrayList<UserBaseInfo>();

		//获取主播ref的List id
		List<HashMap<String, Object>> list = getAnchorsListAnchorRef(page, rows, anchorid);
		
		if(StringUtil.isNotEmpty(anchorid) && list.size() == 0){
			int id = Integer.parseInt(anchorid);
			UserBaseInfo userdata = getUserBaseInfo(id);
			if(userdata == null){
				result.put("rows", anchorList);
				result.put("total", 0);
				
				return result;
			}
			UserAsset userAsset = UserDao.getInstance().getUserAsset(id,false);
			userdata.setCredit(userAsset.getCredit());
			userdata.setCreditTotal(userAsset.getCreditTotal());
			userdata.setUnionname("无");
			userdata.setUsername("无");
			userdata.setStatus(UserDao.getInstance().getAnchorStatus(id));
			userdata.setRemarks("");
			anchorList.add(userdata);
			
			result.put("rows", anchorList);
			result.put("total", 1);
			
			return result;
		}
		
		for(HashMap<String, Object> map : list){
			int id = Integer.parseInt(map.get("anchorid").toString());
			UserBaseInfo userdata = getUserBaseInfo(id);
			if(userdata == null)
				continue;
			UserAsset userAsset = UserDao.getInstance().getUserAsset(id,false);
			userdata.setCredit(userAsset.getCredit());
			userdata.setCreditTotal(userAsset.getCreditTotal());
			userdata.setUnionname(map.get("unionname").toString());
			userdata.setUsername(map.get("username").toString());
			userdata.setTotalmoney(Long.parseLong(map.get("totalmoney").toString()));
			userdata.setStatus(UserDao.getInstance().getAnchorStatus(id));
			userdata.setRemarks(map.get("remarks").toString());
			userdata.setUnionid(Integer.parseInt(map.get("unionid").toString()));
			
			anchorList.add(userdata);
		}
		
		result.put("rows", anchorList);
		result.put("total", getAnchorsCount());
		
		return result;
	}
	

	//删除关系表中关系
	private Boolean deleteAnchorRalation(Integer nUnionid,Integer nUid){
		String sql = "delete from " + constant.tb_union_anchor_ref  + " where anchorid=?;";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_union,"master");
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, nUid);
			pstmt.executeUpdate();
			return true;
			
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 工会删除主播关系
	 * @param unionid
	 * @param uid
	 * @return
	 */
	public Boolean deleteAnchorFromUnion(String unionid, String uid,int adminid) {
		Integer nUnionid = valueOf(unionid);
		Integer nUid = valueOf(uid);
		if(null == nUnionid || null == nUid){
			return false;
		}
		if(false == updateFamilyIdFromUser(0,nUid)){
			return false;
		}
		AddOperationLog(constant.db_zhu_user,nUid.toString(),"更新主播所在公会"+nUnionid,2,nUnionid.toString(),String.valueOf(0),adminid);
		
		if(false == deleteAnchorRalation(nUnionid,nUid)){
			return false;
		}
		AddOperationLog(constant.db_zhu_union,nUid.toString(),"主播"+nUid+"加入公会"+nUnionid,1,nUnionid.toString(),String.valueOf(0),adminid);
		return true;
	}
	
	/**
	 * 工会增加主播关系
	 * @param unionid
	 * @param uid
	 * @return
	 */
	public Boolean addAnchorToUnion(String unionid, String uid,int adminid, String remarks) {
		Integer nUnionid = valueOf(unionid);
		Integer nUid = valueOf(uid);
		if(null == nUnionid || null == nUid){
			return false;
		}
		//判断关系是否已存在
		if(false == isAnchorCanAddToUnion(nUid)){
			return false;
		}
		//判断工会是否存在
		if(null == getUnionById(nUnionid)){
			return false;
		}
		//插入关系，更新个人信息中family
		if(false == addRelation(nUnionid,nUid,adminid,remarks)){
			return false;
		}
		AddOperationLog(constant.db_zhu_user,nUid.toString(),"更新主播所在公会"+nUnionid,2,String.valueOf(0),nUnionid.toString(),adminid);
		AddOperationLog(constant.db_zhu_union,nUid.toString(),"主播"+nUid+"加入公会"+nUnionid,1,String.valueOf(0),nUnionid.toString(),adminid);
		return true;
	}
	
	//更新familyId
	private boolean updateFamilyIdFromUser(Integer nUnionid, Integer nUid){
		String tail = String.format("%02d", nUid%100);
		String sql = "update " + constant.tb_userbaseinfo + tail + " set familyId=?" + " where uid="+nUid+";";
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user,"master");
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, nUnionid);
			if(1 == pstmt.executeUpdate()){
				Unirest.get(Constant.business_server_url + "/admin/refreshUserBaseInfo?uid=" + nUid).asString();
				return true;
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public boolean updateUnion(int unionid,String unionName,String ownerId,String remark, String adminuid){
		String sql = "update " + constant.tb_union_info +" set unionname=?,ownerid=?,remarks=?,adminuid=? where unionid=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_union,"master");
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, unionName);
			pstmt.setString(2, ownerId);
			pstmt.setString(3, remark);
			pstmt.setInt(4, Integer.parseInt(adminuid));
			pstmt.setInt(5, unionid);
			
			if(1 == pstmt.executeUpdate()){
				return true;
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public boolean updateAnchorById(int unionid, int anchorid, String remarks){
		String sql = "update " + constant.tb_union_anchor_ref +" set unionid=?,remarks=? where anchorid=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_union,"master");
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, unionid);
			pstmt.setString(2, remarks);
			pstmt.setInt(3, anchorid);
			
			if(1 == pstmt.executeUpdate()){
				return true;
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	//禁用启用公会
	public boolean banUnion(int unionid,String status){
		String sql = "update " + constant.tb_union_info +" set status=? where unionid=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_union,"master");
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(status));
			pstmt.setInt(2, unionid);
			
			if(1 == pstmt.executeUpdate()){
				return true;
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	//判断关系是否已存在
	private boolean addRelation(Integer nUnionid, Integer nUid,int adminid, String remarks) {
		String sql = "insert into " + constant.tb_union_anchor_ref + " (unionid,anchorid,remarks,adminid,addtime) values (?,?,?,?,?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_union,"master");
			pstmt = con.prepareStatement(sql);

			pstmt.setInt(1, nUnionid);
			pstmt.setInt(2, nUid);
			pstmt.setString(3, remarks);
			pstmt.setInt(4, adminid);
			pstmt.setLong(5, System.currentTimeMillis()/1000);

			if(1 == pstmt.executeUpdate()){		
				return updateFamilyIdFromUser(nUnionid,nUid);	//增加familyId
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	//判断工会是否存在
	public UnionModel getUnionById(Integer nUnionid) {
		UnionModel data = null;
		String sql = "select * from " + constant.tb_union_info + " where unionid=" + nUnionid + ";";

		DbUtil.DbSimpleQuery dbq = new DbUtil.DbSimpleQuery();
		ResultSet rs = dbq.simpleQuery(constant.db_zhu_union,sql);
		if(null != rs){
			try {
				while(rs.next()){
					data = new UnionModel().populateFromResultSet(rs);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		dbq.release();
		return data;
	}
	//判断关系是否已存在
	private boolean isAnchorCanAddToUnion(Integer nUid) {
		String tail = String.format("%02d", nUid%100);
		String sql = "select familyId from " + constant.tb_userbaseinfo + tail + " where uid="+nUid+";";

		DbUtil.DbSimpleQuery dbq = new DbUtil.DbSimpleQuery();
		ResultSet rs = dbq.simpleQuery(constant.db_zhu_user,sql);
		if(null != rs){
			try {
				while(rs.next()){
					if(0 == rs.getInt("familyId")){
						dbq.release();
						return true;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		dbq.release();
		return false;
	}
	
	//获取当月某公会的收入总数
	private long getCurrentMonthMoney(int unionid){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		//当前月份
		String currentMonth = DateUtil.datetime2String(new Date().getTime(), "yyyyMM");

		String sql = "SELECT sum(price * count) totalmoney FROM " + constant.tb_bill + currentMonth +" where familyid=?";
		try{
			con = DbUtil.instance().getCon(constant.db_zhu_bill,"slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, unionid);
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
	
	//获取当月某主播的收入总数
	private long getCurrentMonthMoneyByUid(int anchorid){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		//当前月份
		String currentMonth = DateUtil.datetime2String(new Date().getTime(), "yyyyMM");
		
		String sql = "SELECT sum(price * count) totalmoney FROM " + constant.tb_bill + currentMonth +" where dstuid=?";
		try{
			con = DbUtil.instance().getCon(constant.db_zhu_bill,"slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, anchorid);
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
	
	private int getUnionAnchorCnt(int unionid){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select count(*) as cnt from " + constant.tb_union_anchor_ref+" where unionid=?";
		try{
			con = DbUtil.instance().getCon(constant.db_zhu_union,"slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, unionid);
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
	
	//获取所有公会id
	public List<Integer> getAllUnionids(){
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select unionid from "+constant.tb_union_info;
		DbUtil.DbSimpleQuery dbq = new DbUtil.DbSimpleQuery();
		ResultSet rs = dbq.simpleQuery(constant.db_zhu_union, sql);
		if(null != rs){
			try {
				while(rs.next()){
					int unionid = rs.getInt("unionid");
					list.add(unionid);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		dbq.release();
		return list;
	}
	
	//获取所有公会id
	public List<Integer> getAllAnchorIDs(){
		List<Integer> list = new ArrayList<Integer>();
		String sql = "select anchorid from "+constant.tb_union_anchor_ref;
		DbUtil.DbSimpleQuery dbq = new DbUtil.DbSimpleQuery();
		ResultSet rs = dbq.simpleQuery(constant.db_zhu_union, sql);
		if(null != rs){
			try {
				while(rs.next()){
					int anchorid = rs.getInt("anchorid");
					list.add(anchorid);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		dbq.release();
		return list;
	}
	
	public HashMap<Integer, Long> getTotalConsumeYestodayOfUnion(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND,0);
		Long etime = cal.getTimeInMillis()/1000;
		cal.add(Calendar.DATE, -1);
		String month = DateUtil.datetime2String(cal.getTime().getTime(), "yyyyMM");
		
		Long stime = cal.getTimeInMillis()/1000;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		HashMap<Integer, Long> resultMap = new HashMap<Integer, Long>();
		String sql = "select familyid, sum(price * count) totalmoney from " + constant.tb_bill + month +" where addtime>="+stime+" and addtime<"+etime+" familyid > 0 group by familyid";
		try{
			con = DbUtil.instance().getCon(constant.db_zhu_bill,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null){
				while (rs.next()) {
					resultMap.put(rs.getInt("familyid"), rs.getLong("totalmoney"));
				}
			}
		}catch (Exception ex){
			logger.error("getTotalConsumeYestodayOfUnion-exception",ex);
		}finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (con != null) con.close();

			}catch (Exception e){

				logger.error("getTotalConsumeYestodayOfUnion-finally-exception",e);
			}
		}
		return resultMap;
	}
	
	//获取上月所有公会房间总消耗
	public HashMap<Integer, Long> getTotalMoneyOfLastMonth(List<Integer> unionidlist){
		//将unionidlist转为已逗号分隔字符串
		String unionids = unionidlist.toString().replace("[", "").replace("]", "");
		
		//获取上月月份字符串
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        String lastMonth = DateUtil.datetime2String(cal.getTime().getTime(), "yyyyMM");

        Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		HashMap<Integer, Long> resultMap = new HashMap<Integer, Long>();
		String sql = "select familyid, sum(price * count) totalmoney from " + constant.tb_bill + lastMonth +" where familyid in (" + unionids +") group by familyid";

		try{
			con = DbUtil.instance().getCon(constant.db_zhu_bill,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null){
				while (rs.next()) {
					resultMap.put(rs.getInt("familyid"), rs.getLong("totalmoney"));
				}
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

		return resultMap;

	}
	
	//获取上月所有公会主播房间总消耗
	public HashMap<Integer, Long> getAnchorTotalMoneyOfLastMonth(List<Integer> anchorIdlist){
		//将unionidlist转为已逗号分隔字符串
		String anchorids = anchorIdlist.toString().replace("[", "").replace("]", "");

		//获取上月月份字符串
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		String lastMonth = DateUtil.datetime2String(cal.getTime().getTime(), "yyyyMM");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		HashMap<Integer, Long> resultMap = new HashMap<Integer, Long>();
		String sql = "select dstuid, sum(price * count) totalmoney from " + constant.tb_bill + lastMonth +" where dstuid in (" + anchorids +") group by dstuid";

		try{
			con = DbUtil.instance().getCon(constant.db_zhu_bill,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null){
				while (rs.next()) {
					resultMap.put(rs.getInt("dstuid"), rs.getLong("totalmoney"));
				}
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (con != null) con.close();

			}catch (Exception e){
				e.printStackTrace();
			}
		}

		return resultMap;

	}
	
	//增加totalmoney的值
	public boolean updateTotalmoneyByUnionId(HashMap<Integer, Long> moneyMapOfYestoday){
		

		String sql = "update " + constant.tb_union_info +" set totalmoney=totalmoney+? where unionid=?";
		Connection con = null;
		Statement stmt = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_union,"master");
			con.setAutoCommit(true);
			
			stmt =  con.createStatement();
			for (Map.Entry<Integer, Long> map : moneyMapOfYestoday.entrySet()) {
				sql = String.format(sql, map.getValue(),map.getKey());
				stmt.addBatch(sql);
			}
			stmt.executeBatch();
			con.commit();
			
		} catch (Exception e) {
			logger.error("updateTotalmoneyByUnionId:",e);
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException e1) {
					logger.error("updateTotalmoneyByUnionId:",e1);
				}
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	//为公会主播增加totalmoney的值
	public boolean updateTotalmoneyByAnchorId(int anchorid, long totalmoney){
		String sql = "update " + constant.tb_union_anchor_ref +" set totalmoney=totalmoney+" + totalmoney + " where anchorid=" + anchorid;
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DbUtil.instance().getCon(constant.db_zhu_union,"master");
			pstmt = con.prepareStatement(sql);
			if(1 == pstmt.executeUpdate()){
				return true;
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	
	@SuppressWarnings("null")
	public Map<String, Object> getSupportConsume(Integer uid,Integer unionid,int stime,int etime,Integer page,Integer size){
		
		Map<String, Object> mapResult = new HashMap<String, Object>();
		
		String sql = "select a.uid,a.unionid,b.unionname,b.adminname,sum(a.pays) as pay,sum(a.consume) as consumes,sum(a.amount) as amounts "
				+ " from pay_union_support a,zhu_union.union_info b "
				+ " where a.unionid=b.unionid and a.ymdtime>= " + stime + " and a.ymdtime<" + etime;
		
		if (uid > 0) {
			sql = sql + " and a.uid=" + uid;
		}
		if (unionid > 0) {
			sql = sql + " and a.unionid=" + unionid;
		}
		
		String sqlList = sql + " group by a.uid order by a.unionid, pay desc limit " + (page - 1)*size + "," + size;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_pay, constant.db_slave);
			pstmt = con.prepareStatement(sqlList);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				
				while(rs.next()){
					Map<String, Object> map = new HashMap<String, Object>();

					Map<String, Object> supportInfo = UnionDao.getIns().getSupportByUid(rs.getInt("uid"),rs.getInt("unionid"),9);
					if (supportInfo == null) {
						map.put("supportAmount", 0);
					}else {
						map.put("supportAmount", supportInfo.get("amount"));
					}
					map.put("uid", rs.getInt("uid"));
					map.put("unionid", rs.getInt("unionid"));
					map.put("unionname", rs.getString("unionname"));
					map.put("adminname", rs.getString("adminname"));
					map.put("pay", rs.getInt("pay"));
					map.put("consumes", rs.getInt("consumes"));
					map.put("amounts", rs.getInt("amounts"));
					UserBaseInfoModel userBaseInfo = UserDao.getInstance().getUserBaseInfo(rs.getInt("uid"), false);
					if (userBaseInfo != null) {
						map.put("nickname", userBaseInfo.getNickname());
					}else{
						map.put("nickname", "未知");
					}
					list.add(map);
				}
				mapResult.put("rows", list);
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int total = 0;
			if (rs != null) {
				rs.last();
				total = rs.getRow();
			}
			mapResult.put("total", total);
		} catch (Exception e) {
			System.out.println("getSupportConsume-exception:" + e.getMessage());
			System.out.println("sql:" + sqlList);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null){
					con.close();
				}
			} catch (Exception e2) {
				System.out.println("getSupportConsume-exception-finally:" + e2.getMessage());
			}
		}
		
		return mapResult;
	}

//	public static void main(String[] args) {
//		UnionDao.getIns().updateFamilyId(0,10000000);
//		UnionDao.getIns().addUnion("tosyUnion", "123", "tosy");
//	}
	
	
	public Map<String, Object> getUserCoverList(int unionid,int uid,int status,int page,int size){
		Map<String, Object> mapResult = new HashMap<String, Object>();
		List<Map<String, Object>> list =  new ArrayList<Map<String,Object>>();
		int total = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select uc.id,a.unionid,a.anchorid,b.unionname,uc.picCover,uc.picCover1,uc.picCover2,uc.status,uc.cause from union_anchor_ref a,union_info b,user_cover_check uc where a.unionid=b.unionid and a.anchorid = uc.uid and b.status=1 and a.isvalid=1";
			if (unionid > 0 ) {
				sql = sql + " and a.unionid = " + unionid;
			}
			if (uid > 0 ) {
				sql = sql + " and a.anchorid = " + uid;
			}
			if(status>=0){
				sql = sql + " and uc.status = " + status;
			}
			String sqllist = sql +" order by uc.status asc,uc.id desc limit " + (page-1)*size + "," + size;
			// 获取用户基本信息
			con = DbUtil.instance().getCon(constant.db_zhu_union, constant.db_slave);
			pstmt = con.prepareStatement(sqllist);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while(rs.next()){
					Map<String, Object> map = new HashMap<String, Object>();
					UserBaseInfoModel userBaseInfo = UserDao.getInstance().getUserBaseInfo(rs.getInt("anchorid"),false);
					if (userBaseInfo != null) {
						map.put("nickname", userBaseInfo.getNickname());
						map.put("anchorLevel",userBaseInfo.getAnchorLevel());
						map.put("headimage", userBaseInfo.getHeadimage());
						map.put("picCover",rs.getString("picCover"));
						map.put("picCover1",rs.getString("picCover1"));
						map.put("picCover2",rs.getString("picCover2"));
						map.put("recommend",userBaseInfo.getRecommend());
						map.put("status",rs.getString("status"));
						map.put("cause",rs.getString("cause"));
						/*if(userBaseInfo.getFamilyId()==0){
							map.put("unionid", 0);
							map.put("anchorid", uid);
							map.put("unionname", "自由人");
						}else{*/
							map.put("unionid",rs.getInt("unionid"));
							map.put("anchorid", rs.getInt("anchorid"));
							map.put("unionname", rs.getString("unionname"));
							map.put("id", rs.getInt("id"));
						/*}*/
					}else {
						map.put("nickname", "未知");
						map.put("anchorLevel", 0);
						map.put("headimage", "");
						map.put("livimage", "");
						map.put("pcimg1", "");
						map.put("pcimg2", "");
						map.put("recommend", 0);
					}
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
			rs = pstmt.executeQuery();
			if (rs != null) {
				rs.last();
				total = rs.getRow();
			}
			mapResult.put("rows", list);
			mapResult.put("total", total);
			
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
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return mapResult;
	}
	
	/**
	 * 查询待审核的封面（非家族）
	 * @param uid
	 * @param status
	 * @param page
	 * @param size
	 * @return
	 */
	public Map<String, Object> getUserCoverList(int uid,int status,int page,int size){
		Map<String, Object> mapResult = new HashMap<String, Object>();
		List<Map<String, Object>> list =  new ArrayList<Map<String,Object>>();
		int total = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select uc.id,uc.uid,uc.picCover,uc.picCover1,uc.picCover2,uc.status,uc.cause from user_cover_check uc where 1=1 ";
			if (uid > 0 ) {
				sql = sql + " and uc.uid = " + uid;
			}
			if(status>=0){
				sql = sql + " and uc.status = " + status;
			}
			String sqllist = sql +" order by uc.status asc,uc.id desc limit " + (page-1)*size + "," + size;
			// 获取用户基本信息
			con = DbUtil.instance().getCon(constant.db_zhu_union, constant.db_slave);
			pstmt = con.prepareStatement(sqllist);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while(rs.next()){
					Map<String, Object> map = new HashMap<String, Object>();
					UserBaseInfoModel userBaseInfo = UserDao.getInstance().getUserBaseInfo(rs.getInt("uid"),false);
					if (userBaseInfo != null) {
						map.put("nickname", userBaseInfo.getNickname());
						map.put("anchorLevel",userBaseInfo.getAnchorLevel());
						map.put("headimage", userBaseInfo.getHeadimage());
						map.put("picCover",rs.getString("picCover"));
						map.put("picCover1",rs.getString("picCover1"));
						map.put("picCover2",rs.getString("picCover2"));
						map.put("recommend",userBaseInfo.getRecommend());
						map.put("status",rs.getString("status"));
						map.put("cause",rs.getString("cause"));
						map.put("id", rs.getInt("id"));
						map.put("uid", rs.getInt("uid"));
						/*if(userBaseInfo.getFamilyId()==0){
							map.put("unionid", 0);
							map.put("anchorid", uid);
							map.put("unionname", "自由人");
						}else{
							map.put("unionid",rs.getInt("unionid"));
							map.put("anchorid", rs.getInt("anchorid"));
							map.put("unionname", rs.getString("unionname"));
							map.put("id", rs.getInt("id"));
						}*/
					}else {
						map.put("nickname", "未知");
						map.put("anchorLevel", 0);
						map.put("headimage", "");
						map.put("livimage", "");
						map.put("pcimg1", "");
						map.put("pcimg2", "");
						map.put("recommend", 0);
					}
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
			rs = pstmt.executeQuery();
			if (rs != null) {
				rs.last();
				total = rs.getRow();
			}
			mapResult.put("rows", list);
			mapResult.put("total", total);
			
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
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return mapResult;
	}
}
