package com.tinypig.admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.constant;
import com.tinypig.admin.redis.service.RedisOperat;
import com.tinypig.admin.util.DbUtil;
import com.tinypig.admin.util.RedisContant;

public class SysNoticeDao extends BaseDao {

	private final static SysNoticeDao instance = new SysNoticeDao();
	
	public static SysNoticeDao getInstance(){
		return instance;
	}
	
	public List<Map<String, Object>> getSysNoticeList(){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		String sql = " select a.id,a.content,a.url,a.sendtime,a.addtime,b.username"
				+ " from system_notice a,zhu_admin.admin_user b where a.adminid=b.uid order by a.sendtime desc limit 30";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {

			con = DbUtil.instance().getCon(constant.db_zhu_web,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				
				Map<String, Object> map = null;
				while (rs.next()) {
					map = new HashMap<String, Object>();
					map.put("id", rs.getInt("id"));
					map.put("content", rs.getString("content"));
					map.put("url", rs.getString("url"));
					map.put("sendtime", rs.getLong("sendtime"));
					map.put("addtime", rs.getLong("addtime"));
					map.put("adminname", rs.getString("username"));
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
	
	public List<Map<String, Object>> getValidSysNotice(Long stime){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		String sql = " select id,content,url,sendtime from system_notice where sendtime > " + stime;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {

			con = DbUtil.instance().getCon(constant.db_zhu_web,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				
				Map<String, Object> map = null;
				while (rs.next()) {
					map = new HashMap<String, Object>();
					map.put("id", rs.getInt("id"));
					map.put("content", rs.getString("content"));
					map.put("url", rs.getString("url"));
					map.put("sendtime", rs.getLong("sendtime"));
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
	

	
	public Map<String, Object> getSysNoticeById(int id){

		Map<String, Object> map = new HashMap<String, Object>();
		
		String sql = " select a.id,a.content,a.url,a.sendtime,a.isvalid,from_unixtime(a.addtime,'%Y-%m-%d %H:%i:%s') as addtime,b.username"
				+ " from system_notice a,zhu_admin.admin_user b where a.adminid=b.uid and a.id=" + id;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {

			con = DbUtil.instance().getCon(constant.db_zhu_web,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				while (rs.next()) {
					map.put("id", rs.getInt("id"));
					map.put("content", rs.getString("content"));
					map.put("url", rs.getString("url"));
					map.put("sendtime", rs.getString("sendtime"));
					map.put("addtime", rs.getString("addtime"));
					map.put("adminname", rs.getString("username"));
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
	
	public int addSysNoctice(String content,String url,Long sendtime,Long addtime,int adminid){

		String sql = " insert into system_notice(content,url,sendtime,addtime,adminid)value(?,?,?,?,?)";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int iresult = -1;
		try {

			con = DbUtil.instance().getCon(constant.db_zhu_web,"master");
			pstmt = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, content);
			pstmt.setString(2, url);
			pstmt.setLong(3, sendtime);
			pstmt.setLong(4, addtime);
			pstmt.setInt(5, adminid);
			
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				iresult = rs.getInt(1);
			}
			
			if (iresult > 0) {
				String current_version = String.format("{content:%s,url:%s,sendtime:%d}", content,url,sendtime);
				AddOperationLog("system_notice", String.valueOf(iresult), "新增官方私信", 1, "", current_version, adminid);
			}
			
		} catch (Exception e) {
			iresult = -1;
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
		
		return iresult;
	}

	public Map<String, Object> getRoomChat(){
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		String sql = " select a.id,a.content,a.starttime,a.endtime,a.interval,a.isvalid,a.addtime,b.username from web_room_chat a,zhu_admin.admin_user b where a.adminid=b.uid order by a.id desc limit 30";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Object> map = new HashMap<String, Object>();
		
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
					map.put("username", rs.getString("username"));
					
					list.add(map);
				}
				map = new HashMap<String, Object>();
				map.put("rows", list);
				map.put("total", 30);
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

	public Map<String, Object> getRoomChatById(int id){
		
		
		String sql = " select a.id,a.content,a.starttime,a.endtime,a.interval,a.isvalid,a.addtime from web_room_chat a where a.id=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Object> map = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_web,"slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, id);
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
		return map;
	}
	
	public int addRoomChat(String content,Long starttime,Long endtime,int isvalid,Long addtime,int adminid){

		String sql = " insert into web_room_chat(content,starttime,endtime,isvalid,addtime,adminid)value(?,?,?,?,?,?)";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int iresult = -1;
		try {

			con = DbUtil.instance().getCon(constant.db_zhu_web,"master");
			pstmt = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, content);
			pstmt.setLong(2, starttime);
			pstmt.setLong(3, endtime);
			pstmt.setInt(4, isvalid);
			pstmt.setLong(5, addtime);
			pstmt.setInt(6, adminid);
			
			pstmt.executeUpdate();
			
			rs = pstmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				iresult = rs.getInt(1);
			}
			
			if (iresult > 0) {
				String current_version = String.format("{content:%s,starttime:%d,endtime:%d,isvalid:%d}", content,starttime,endtime,isvalid);
				AddOperationLog("web_room_chat", String.valueOf(iresult), "新增直播间公告", 1, "", current_version, adminid);
				roomChatToRedis(iresult);
			}
			
		} catch (Exception e) {
			iresult = -1;
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
		
		return iresult;
	}
	
	public void roomChatToRedis(int id){
		Map<String, Object> roomChat = getRoomChatById(id);
		if (roomChat != null && "1".equals(roomChat.get("isvalid").toString())) {
			RedisOperat.getInstance().rpush(RedisContant.host, RedisContant.port6380, RedisContant.roomChat, JSONObject.toJSONString(roomChat));
		}
	}
	
	public int editRoomChat(String content,Long starttime,Long endtime,int isvalid,int id,int adminid){

		String sql = " update web_room_chat set content=?,starttime=?,endtime=?,isvalid=? where id = ?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int iresult = -1;
		try {

			con = DbUtil.instance().getCon(constant.db_zhu_web,"master");
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, content);
			pstmt.setLong(2, starttime);
			pstmt.setLong(3, endtime);
			pstmt.setInt(4, isvalid);
			pstmt.setLong(5, id);
			
			iresult = pstmt.executeUpdate();
			
			if (iresult > 0) {
				Map<String, Object> roomChatById = getRoomChatById(id);
				String previous_version = JSONObject.toJSONString(roomChatById);
				String current_version = String.format("{content:%s,starttime:%d,endtime:%d,isvalid:%d,id:%d}", content,starttime,endtime,isvalid,id);
				AddOperationLog("web_room_chat", String.valueOf(id), "修改直播间公告", 2, previous_version, current_version, adminid);
				roomChatToRedis(iresult);
			}
			
		} catch (Exception e) {
			iresult = -1;
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
		
		return iresult;
	}
	
	public int removeRoomChat(int id,int adminid){

		String sql = " update web_room_chat set isvalid = 0 where id = "+ id;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int iresult = -1;
		try {

			con = DbUtil.instance().getCon(constant.db_zhu_web,"master");
			pstmt = con.prepareStatement(sql);
			
			iresult = pstmt.executeUpdate();
			
			if (iresult > 0) {
				Map<String, Object> roomChatById = getRoomChatById(id);
				String previous_version = JSONObject.toJSONString(roomChatById);
				String current_version = String.format("{isvalid:0,id:%d}",id);
				AddOperationLog("web_room_chat", String.valueOf(id), "删除直播间公告", 2, previous_version, current_version, adminid);
			}
			
		} catch (Exception e) {
			iresult = -1;
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
		
		return iresult;
	}
}
