package com.tinypig.admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.tinypig.admin.constant;
import com.tinypig.admin.model.CashInfoModel;
import com.tinypig.admin.model.PayInfoModel;
import com.tinypig.admin.util.*;

public class CashInfoDao {
	
	int time=(int) (System.currentTimeMillis() / 1000);
	private final static CashInfoDao instance = new CashInfoDao();

	public static CashInfoDao getInstance() {
		return instance;
	}

	public List<CashInfoModel> getCashInfo(String uid,String startdate,String enddate,String page,String rows){
		String start="";
		String end ="";
		if(!startdate.isEmpty()){
		 start = startdate.substring(0,4) +startdate.substring(5,7)+startdate.substring(8,10)+startdate.substring(11,13)+startdate.substring(14,16)+startdate.substring(17);
		}else if(!enddate.isEmpty()){
		  end = enddate.substring(0,4) +enddate.substring(5,7)+enddate.substring(8,10)+enddate.substring(11,13)+enddate.substring(14,16)+enddate.substring(17);
		}
		 Calendar c = Calendar.getInstance();
		Calendar d = Calendar.getInstance();
		try {
			if(!startdate.isEmpty()){
				c.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(start));
			}else if(!enddate.isEmpty()){
				d.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(end));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int sufix =0;
		if(!uid.isEmpty()){
			sufix=Integer.parseInt(uid) % 100;
		}
		String dbname = "user_base_info_" + (sufix < 10 ? "0" + sufix : sufix);
		List<CashInfoModel>  list =  new ArrayList<CashInfoModel>();
		String sql="select w.createAt time,w.uid,i.nickname,i.anchorLevel,(select unionname "
				+ "from zhu_union.union_info u where u.unionid=i.familyId)unionname,w.type,"
				+ "w.billno,w.amount,w.createAt,w.isSecc from zhu_pay.pay_withdraw w "
				+ "left join zhu_user."+dbname+" i on i.uid=w.uid where 1=1";
//		String sq = "select o.creatAt time,o.srcuid,r.nickname,r.registchannel,o.order_id,"
//				+ "o.amount,o.os,o.paytype,o.creatAt,o.status from zhu_pay.pay_order o "
//				+ "left join zhu_user."+dbname+" r on r.uid=o.srcuid where 1=1";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		if(!uid.isEmpty()&&!String.valueOf(startdate).isEmpty()&&!String.valueOf(enddate).isEmpty()){
			sql=sql+" and w.uid="+uid+" and w.createAt>"+c.getTimeInMillis()/1000+" and w.createAt<"+d.getTimeInMillis()/1000;
			System.out.println(sql);
		}else if(!uid.isEmpty()&&!String.valueOf(startdate).isEmpty()&&String.valueOf(enddate).isEmpty()){
			sql=sql+" and w.uid="+uid+" and w.createAt>"+c.getTimeInMillis()/1000;
		}else if(!uid.isEmpty()&&String.valueOf(startdate).isEmpty()&&!String.valueOf(enddate).isEmpty()){
			sql=sql+" and w.uid="+uid+" and w.createAt<"+c.getTimeInMillis()/1000;
		}else if(!uid.isEmpty()&&String.valueOf(startdate).isEmpty()&&String.valueOf(enddate).isEmpty()){
			sql=sql+" and w.uid="+uid;
		}if (StringUtils.isNotEmpty(page)) {
			sql = sql + " limit " + (Integer.valueOf(page) - 1) * Integer.valueOf(rows) + "," + Integer.valueOf(rows);
		}
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					CashInfoModel cUserModel = new CashInfoModel().populateFromResultSet(rs);
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
	
	
	
	public int getCashTotal(String uid,String startdate,String enddate){
		String start="";
		String end ="";
		if(!startdate.isEmpty()){
		 start = startdate.substring(0,4) +startdate.substring(5,7)+startdate.substring(8,10)+startdate.substring(11,13)+startdate.substring(14,16)+startdate.substring(17);
		}else if(!enddate.isEmpty()){
		  end = enddate.substring(0,4) +enddate.substring(5,7)+enddate.substring(8,10)+enddate.substring(11,13)+enddate.substring(14,16)+enddate.substring(17);
		}
		 Calendar c = Calendar.getInstance();
		Calendar d = Calendar.getInstance();
		try {
			if(!startdate.isEmpty()){
				c.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(start));
			}else if(!enddate.isEmpty()){
				d.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(end));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int sufix =0;
		if(!uid.isEmpty()){
			sufix=Integer.parseInt(uid) % 100;
		}
		String dbname = "user_base_info_" + (sufix < 10 ? "0" + sufix : sufix);
		List<CashInfoModel>  list =  new ArrayList<CashInfoModel>();
		String sql="select w.createAt time,w.uid,i.nickname,i.anchorLevel,(select unionname "
				+ "from zhu_union.union_info u where u.unionid=i.familyId)unionname,w.type,"
				+ "w.billno,w.amount,w.createAt,w.isSecc from zhu_pay.pay_withdraw w "
				+ "left join zhu_user."+dbname+" i on i.uid=w.uid where 1=1";
//		String sq = "select o.creatAt time,o.srcuid,r.nickname,r.registchannel,o.order_id,"
//				+ "o.amount,o.os,o.paytype,o.creatAt,o.status from zhu_pay.pay_order o "
//				+ "left join zhu_user."+dbname+" r on r.uid=o.srcuid where 1=1";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		if(!uid.isEmpty()&&!String.valueOf(startdate).isEmpty()&&!String.valueOf(enddate).isEmpty()){
			sql=sql+" and w.uid="+uid+" and w.createAt>"+c.getTimeInMillis()/1000+" and w.createAt<"+d.getTimeInMillis()/1000;
			System.out.println(sql);
		}else if(!uid.isEmpty()&&!String.valueOf(startdate).isEmpty()&&String.valueOf(enddate).isEmpty()){
			sql=sql+" and w.uid="+uid+" and w.createAt>"+c.getTimeInMillis()/1000;
		}else if(!uid.isEmpty()&&String.valueOf(startdate).isEmpty()&&!String.valueOf(enddate).isEmpty()){
			sql=sql+" and w.uid="+uid+" and w.createAt<"+c.getTimeInMillis()/1000;
		}else if(!uid.isEmpty()&&String.valueOf(startdate).isEmpty()&&String.valueOf(enddate).isEmpty()){
			sql=sql+" and w.uid="+uid;
		}
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					CashInfoModel cUserModel = new CashInfoModel().populateFromResultSet(rs);
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
		return list.size();
	}
}












