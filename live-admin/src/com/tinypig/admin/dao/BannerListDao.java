package com.tinypig.admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.constant;
import com.tinypig.admin.model.BannerListModel;
import com.tinypig.admin.redis.service.OtherRedisService;
import com.tinypig.admin.util.*;

public class BannerListDao {
	
	int time=(int) (System.currentTimeMillis() / 1000);
	private final static BannerListDao instance = new BannerListDao();

	public static BannerListDao getInstance() {
		return instance;
	}

	public int BannerTotal(int type){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select count(*) as cnt from web_banner where type="+type;

		try{
			con = DbUtil.instance().getCon(constant.db_zhu_web,"slave");
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
	 * @return
	 */
	public List<BannerListModel> getBannerList(){
		List<BannerListModel>  list =  new ArrayList<BannerListModel>();
		String sql = "SELECT * FROM web_banner where type=0";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_web,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					BannerListModel bListModel = new BannerListModel().populateFromResultSet(rs);
					if (bListModel != null) {
						list.add(bListModel);
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
					pstmt.close();;
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
	
	public List<BannerListModel> getCarouselList(){
		List<BannerListModel>  list =  new ArrayList<BannerListModel>();
		String sql = "SELECT * FROM web_banner where type=1 and switch=1";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_web,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					BannerListModel bListModel = new BannerListModel().populateFromResultSet(rs);
					if (bListModel != null) {
						OtherRedisService.getInstance().setHomeBanner(bListModel.getId(), JSONObject.toJSONString(bListModel));
						list.add(bListModel);
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
					pstmt.close();;
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
	
	public Boolean addBanner(int swi,String name, String start,String end,String picUrl)  {
		if(null == name){
			return false;
		}
		//Integer startShow = Integer.getInteger(start);
		Calendar c = Calendar.getInstance();
		Calendar d = Calendar.getInstance();
		try {
			c.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(start));
			d.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(end));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(null == start){
			return false;
		}
		
		name = (null == name)?"":name;

		String sql = "insert into  web_banner (picUrl,startShow,endShow,switch,createAT,jumpUrl,name,type) values (?,?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_web,"master");
			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, picUrl);
			pstmt.setLong(2, c.getTimeInMillis()/1000);
			pstmt.setLong(3, d.getTimeInMillis()/1000);
			pstmt.setInt(4, swi);
			pstmt.setLong(5, System.currentTimeMillis()/1000);
			pstmt.setString(6, "www");
			pstmt.setString(7, name);
			pstmt.setInt(8, 0);
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
	
	
	public Boolean delcarousel(int id)  {
		String sql="update web_banner set switch=0 where id=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_web,"master");
			pstmt = con.prepareStatement(sql);

			pstmt.setInt(1, id);
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
	
	
	public Boolean addcarousel(String jumpUrl,String name, String start,String end,String picUrl)  {
		if(null == name){
			return false;
		}
		//Integer startShow = Integer.getInteger(start);
		Calendar c = Calendar.getInstance();
		Calendar d = Calendar.getInstance();
		try {
			c.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(start));
			d.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(end));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(null == start){
			return false;
		}
		
		name = (null == name)?"":name;

		String sql = "insert into  web_banner (picUrl,startShow,endShow,switch,createAT,jumpUrl,name,type) values (?,?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_web,"master");
			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, picUrl);
			pstmt.setLong(2, c.getTimeInMillis()/1000);
			pstmt.setLong(3, d.getTimeInMillis()/1000);
			pstmt.setInt(4, 1);
			pstmt.setLong(5, System.currentTimeMillis()/1000);
			pstmt.setString(6, jumpUrl);
			pstmt.setString(7, name);
			pstmt.setInt(8, 1);
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
	
	public Boolean editcarousel(int id,String jumpUrl,String name, String start,String end,String picUrl)  {
		if(null == name){
			return false;
		}
		//Integer startShow = Integer.getInteger(start);
		Calendar c = Calendar.getInstance();
		Calendar d = Calendar.getInstance();
		try {
			c.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(start));
			d.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(end));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(null == start){
			return false;
		}
		
		name = (null == name)?"":name;
		String sql = "update web_banner set picUrl=?,startShow=?,endShow=?,name=?,jumpUrl=? where id=?";
//		String sql = "insert into  web_banner (picUrl,startShow,endShow,switch,createAT,jumpUrl,name,type) values (?,?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_web,"master");
			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, picUrl);
			pstmt.setLong(2, c.getTimeInMillis()/1000);
			pstmt.setLong(3, d.getTimeInMillis()/1000);
			pstmt.setString(4, name);
			pstmt.setString(5, jumpUrl);
			pstmt.setInt(6, id);
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
	
	public Boolean editBanner(int id,int swi,String name, String start,String end,String picUrl)  {
		if(null == name){
			return false;
		}
		//Integer startShow = Integer.getInteger(start);
		Calendar c = Calendar.getInstance();
		Calendar d = Calendar.getInstance();
		try {
			c.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(start));
			d.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(end));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(null == start){
			return false;
		}
		
		name = (null == name)?"":name;
		String sql = "update web_banner set picUrl=?,startShow=?,endShow=?,name=?,switch=? where id=?";
//		String sql = "insert into  web_banner (picUrl,,,switch,createAT,jumpUrl,,type) values (?,?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_web,"master");
			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, picUrl);
			pstmt.setLong(2, c.getTimeInMillis()/1000);
			pstmt.setLong(3, d.getTimeInMillis()/1000);
			pstmt.setString(4, name);
			pstmt.setLong(5, swi);
			pstmt.setInt(6,id);
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
}












