package com.tinypig.admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tinypig.admin.constant;
import com.tinypig.admin.model.PayInfoModel;
import com.tinypig.admin.model.UserBaseInfoModel;
import com.tinypig.admin.util.*;

public class PayInfoDao extends BaseDao {
	
	int time=(int) (System.currentTimeMillis() / 1000);
	private final static PayInfoDao instance = new PayInfoDao();

	public static PayInfoDao getInstance() {
		return instance;
	}

	public Map<String, Object> getPayInfo(String userSource,Integer status, Integer uid,Long startdate,Long enddate,Integer page,Integer rows,int adminid){
		
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = " select order_id,srcuid,dstuid,amount,zhutou,creatAt,paytime,paytype,status,os,inpour_no,userSource,details,registtime from pay_order where creatAt >= "+startdate+" and creatAt < "+enddate;
		
		if (uid > 0) {
			sql = sql + " and srcuid = " + uid;
		}
		if (status == 2) {
			sql = sql + " and status = 2 ";
		}else if (status == 1) {
			sql = sql + " and status != 2 ";
		}
		
		if (StringUtil.isNotEmpty(userSource)) {
			sql = sql + " and userSource = ? ";
		}
		
		String sqlList = sql + " order by status desc,paytime desc limit " + (page - 1) * rows + "," + rows;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_pay,"slave");
			pstmt = con.prepareStatement(sqlList);
			if (StringUtil.isNotEmpty(userSource)) {
				pstmt.setString(1, userSource);
			}
			rs = pstmt.executeQuery();
			if (rs != null) {

				List<PayInfoModel>  list =  new ArrayList<PayInfoModel>();
				while (rs.next()) {
					
					PayInfoModel cUserModel = new PayInfoModel().populateFromResultSet(rs);
					if (cUserModel != null) {
						UserBaseInfoModel userBaseInfo = UserDao.getInstance().getUserBaseInfo(cUserModel.getSrcuid(),false);
						if (userBaseInfo != null) {
							cUserModel.setSrcname(userBaseInfo.getNickname());
							cUserModel.setUserLevel(userBaseInfo.getUserLevel());
							cUserModel.setRegistchannel(userBaseInfo.getRegistchannel());
							
							if (cUserModel.getSrcuid() == cUserModel.getDstuid()) {
								cUserModel.setDstname(userBaseInfo.getNickname());
							}else {
								UserBaseInfoModel userBaseInfo2 = UserDao.getInstance().getUserBaseInfo(cUserModel.getDstuid(),false);
								if (userBaseInfo2 != null) {
									cUserModel.setDstname(userBaseInfo2.getNickname());
								}
							}
						}
						if (cUserModel.getOs() == 1) {
							if ("weixin".equals(cUserModel.getPaytype())) {
								cUserModel.setPaytype("ipay-weixin");
							}else if ("alipay".equals(cUserModel.getPaytype())) {
								cUserModel.setPaytype("ipay-alipay");
							}
						}else if (cUserModel.getOs() == 2) {
							if ("weixin".equals(cUserModel.getPaytype())) {
								cUserModel.setPaytype("ipay-weixin");
							}else if ("alipay".equals(cUserModel.getPaytype())) {
								cUserModel.setPaytype("ipay-alipay");
							}
						}else if (cUserModel.getOs() == 3) {
							if ("weixin".equals(cUserModel.getPaytype())) {
								cUserModel.setPaytype("wechat-weixin");
							}
						}
						list.add(cUserModel);
					}
				}
				map.put("rows", list);
			}
			
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			
			pstmt = con.prepareStatement(sql);
			if (StringUtil.isNotEmpty(userSource)) {
				pstmt.setString(1, userSource);
			}
			rs = pstmt.executeQuery();
			
			int icount = 0;
			List<Map<String, Object>> footer = new ArrayList<Map<String,Object>>();
			
			int amount = 0;
			if (rs != null) {
				Map<String, Object> mapFooter = new HashMap<String, Object>();
				while(rs.next()){
					amount = amount+rs.getInt("amount");
					icount++;
				}
				mapFooter.put("srcuid", "金额合计");
				mapFooter.put("srcname", amount);
				
				footer.add(mapFooter);
			}
			
			map.put("total", icount);
			map.put("footer", footer);
			
			String bak = "查询条件：status="+status+" startdate="+DateUtil.convert2String(startdate*1000, "yyyy-MM-dd")+" enddate="+DateUtil.convert2String(enddate*1000, "yyyy-MM-dd")+" uid="+uid;
			AddOperationLog("pay_order","","查询充值",3,bak,"",adminid);
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
		return map;
	}
	
	
	
	
	public int getPayTotal(String uid,String startdate,String enddate){
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
		List<PayInfoModel>  list =  new ArrayList<PayInfoModel>();
		String sql = "select o.creatAt time,o.srcuid,r.nickname,r.registchannel,o.order_id,"
				+ "o.amount,o.os,o.paytype,o.creatAt,o.status from zhu_pay.pay_order o "
				+ "left join zhu_user."+dbname+" r on r.uid=o.srcuid where 1=1";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		if(!uid.isEmpty()&&!String.valueOf(startdate).isEmpty()&&!String.valueOf(enddate).isEmpty()){
			sql=sql+" and o.srcuid="+uid+" and o.creatAt>"+c.getTimeInMillis()/1000+" and o.creatAt<"+d.getTimeInMillis()/1000;
			System.out.println(sql);
		}else if(!uid.isEmpty()&&!String.valueOf(startdate).isEmpty()&&String.valueOf(enddate).isEmpty()){
			sql=sql+" and o.srcuid="+uid+" and o.creatAt>"+c.getTimeInMillis()/1000;
		}else if(!uid.isEmpty()&&String.valueOf(startdate).isEmpty()&&!String.valueOf(enddate).isEmpty()){
			sql=sql+" and o.srcuid="+uid+" and o.creatAt<"+c.getTimeInMillis()/1000;
		}else if(!uid.isEmpty()&&String.valueOf(startdate).isEmpty()&&String.valueOf(enddate).isEmpty()){
			sql=sql+" and o.srcuid="+uid;
		}
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					PayInfoModel cUserModel = new PayInfoModel().populateFromResultSet(rs);
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












