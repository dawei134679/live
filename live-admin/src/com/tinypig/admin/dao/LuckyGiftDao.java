package com.tinypig.admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tinypig.admin.constant;
import com.tinypig.admin.util.DbUtil;
import com.tinypig.admin.util.StringUtil;

public class LuckyGiftDao extends BaseDao {

	public HashMap<String, Object> getPriizeList(String page, String rows, String startdate,String enddate, String gid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		HashMap<String, Object> resultMap = new HashMap<String, Object>(); 

		try {
			//list-查询分页数据
			List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			int begin = (Integer.valueOf(page) - 1) * Integer.valueOf(rows);
			sql.append("select B.*, C.totalsendprice, C.totalsendcount, C.totalprize, C.totalprofit");
			sql.append(" from");
			sql.append(" (");
			sql.append(" select A.createdate, sum(A.totalluckcount10) totalcount10, sum(A.totalluckcount100) totalcount100, sum(A.totalluckcount500) totalcount500, sum(A.totalluckcount1000) totalcount1000");
			sql.append(" from");
			sql.append(" (");
			sql.append(" select");
			sql.append(" if(t.multiples = 10, sum(t.luckyCount), 0)totalluckcount10,");
			sql.append(" if(t.multiples = 100, sum(t.luckyCount), 0)totalluckcount100,");
			sql.append(" if(t.multiples = 500, sum(t.luckyCount), 0)totalluckcount500,");
			sql.append(" if(t.multiples = 1000, sum(t.luckyCount), 0)totalluckcount1000,");
			sql.append(" FROM_UNIXTIME(t.createAt, '%Y-%m-%d') createdate");
			sql.append(" from bill_prize_list t");
			sql.append(" where 1=1");
			//幸运礼物判断
			if(StringUtil.isNotEmpty(gid)){
				sql.append(" and t.gid=");
				sql.append(gid);
			}
			//起始日期判断
			if(StringUtil.isNotEmpty(startdate)){
				sql.append(" and FROM_UNIXTIME(t.createAt, '%Y-%m-%d')>='");
				sql.append(startdate);
				sql.append(" '");
			}
			//结束日期判断
			if(StringUtil.isNotEmpty(enddate)){
				sql.append(" and FROM_UNIXTIME(t.createAt, '%Y-%m-%d')<='");
				sql.append(enddate);
				sql.append(" '");
			}
			sql.append(" GROUP BY createdate, t.multiples");
			sql.append(" )");
			sql.append(" A");
			sql.append(" GROUP BY A.createdate");
			sql.append(" )");
			sql.append(" B,");
			sql.append(" (");
			sql.append(" SELECT sum(t.sendPrice) totalsendprice, sum(t.sendCount) totalsendcount, sum(t.priceTotal) totalprize, sum(t.sendPrice)-sum(t.priceTotal) totalprofit, FROM_UNIXTIME(t.createAt, '%Y-%m-%d') createdate");
			sql.append(" FROM bill_prize_list t");
			sql.append(" where 1=1");
			//幸运礼物判断
			if(StringUtil.isNotEmpty(gid)){
				sql.append(" and t.gid=");
				sql.append(gid);
			}
			//起始日期判断
			if(StringUtil.isNotEmpty(startdate)){
				sql.append(" and FROM_UNIXTIME(t.createAt, '%Y-%m-%d')>='");
				sql.append(startdate);
				sql.append(" '");
			}
			//结束日期判断
			if(StringUtil.isNotEmpty(enddate)){
				sql.append(" and FROM_UNIXTIME(t.createAt, '%Y-%m-%d')<='");
				sql.append(enddate);
				sql.append(" '");
			}
			sql.append(" GROUP BY createdate");
			sql.append(" )");
			sql.append(" C");
			sql.append(" where B.createdate = C.createdate");
			sql.append(" order by B.createdate");
			sql.append(" limit ");
			sql.append(begin);
			sql.append(", ");
			sql.append(Integer.valueOf(rows));

			con = DbUtil.instance().getCon(constant.db_zhu_bill,"slave");
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			if (null != rs) {
				while (rs.next()) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("createdate", rs.getString("createdate"));
					map.put("totalcount10", rs.getInt("totalcount10"));
					map.put("totalcount100", rs.getInt("totalcount100"));
					map.put("totalcount500", rs.getInt("totalcount500"));
					map.put("totalcount1000", rs.getInt("totalcount1000"));
					map.put("totalsendprice", rs.getInt("totalsendprice"));
					map.put("totalsendcount", rs.getInt("totalsendcount"));
					map.put("totalprize", rs.getInt("totalprize"));
					map.put("totalprofit", rs.getInt("totalprofit"));

					list.add(map);
				}
			}

			//查询footer所需总计
			sql = new StringBuffer();
			sql.append(" SELECT sum(t.sendPrice) totalsendprice, sum(t.sendCount) totalsendcount, sum(t.priceTotal) totalprize, sum(t.sendPrice)-sum(t.priceTotal) totalprofit, FROM_UNIXTIME(t.createAt, '%Y-%m-%d') createdate");
			sql.append(" FROM bill_prize_list t");
			sql.append(" where 1=1");
			//幸运礼物判断
			if(StringUtil.isNotEmpty(gid)){
				sql.append(" and t.gid=");
				sql.append(gid);
			}
			//起始日期判断
			if(StringUtil.isNotEmpty(startdate)){
				sql.append(" and FROM_UNIXTIME(t.createAt, '%Y-%m-%d')>='");
				sql.append(startdate);
				sql.append(" '");
			}
			//结束日期判断
			if(StringUtil.isNotEmpty(enddate)){
				sql.append(" and FROM_UNIXTIME(t.createAt, '%Y-%m-%d')<='");
				sql.append(enddate);
				sql.append(" '");
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (rs != null) {
				rs.close();
			}
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			if (null != rs) {
				while (rs.next()) {
					HashMap<String, Object> footer = new HashMap<String, Object>();
					footer.put("createdate", "总计");
					footer.put("totalcount10", "");
					footer.put("totalcount100", "");
					footer.put("totalcount500", "");
					footer.put("totalcount1000", "");
					footer.put("totalsendprice", rs.getInt("totalsendprice"));
					footer.put("totalsendcount", rs.getInt("totalsendcount"));
					footer.put("totalprize", rs.getInt("totalprize"));
					footer.put("totalprofit", rs.getInt("totalprofit"));

					list.add(footer);
				}
			}
			resultMap.put("rows", list);

			//total-查询总数
			int total = 0;
			sql = new StringBuffer();
			sql.append("select count(B.createdate)");
			sql.append(" from");
			sql.append(" (");
			sql.append(" select A.createdate, sum(A.totalluckcount10) totalcount10, sum(A.totalluckcount100) totalcount100, sum(A.totalluckcount500) totalcount500, sum(A.totalluckcount1000) totalcount1000");
			sql.append(" from");
			sql.append(" (");
			sql.append(" select");
			sql.append(" if(t.multiples = 10, sum(t.luckyCount), 0)totalluckcount10,");
			sql.append(" if(t.multiples = 100, sum(t.luckyCount), 0)totalluckcount100,");
			sql.append(" if(t.multiples = 500, sum(t.luckyCount), 0)totalluckcount500,");
			sql.append(" if(t.multiples = 1000, sum(t.luckyCount), 0)totalluckcount1000,");
			sql.append(" FROM_UNIXTIME(t.createAt, '%Y-%m-%d') createdate");
			sql.append(" from bill_prize_list t");
			sql.append(" where 1=1");
			//幸运礼物判断
			if(StringUtil.isNotEmpty(gid)){
				sql.append(" and t.gid=");
				sql.append(gid);
			}
			//起始日期判断
			if(StringUtil.isNotEmpty(startdate)){
				sql.append(" and FROM_UNIXTIME(t.createAt, '%Y-%m-%d')>='");
				sql.append(startdate);
				sql.append(" '");
			}
			//结束日期判断
			if(StringUtil.isNotEmpty(enddate)){
				sql.append(" and FROM_UNIXTIME(t.createAt, '%Y-%m-%d')<='");
				sql.append(enddate);
				sql.append(" '");
			}
			sql.append(" GROUP BY createdate, t.multiples");
			sql.append(" )");
			sql.append(" A");
			sql.append(" GROUP BY A.createdate");
			sql.append(" )");
			sql.append(" B,");
			sql.append(" (");
			sql.append(" SELECT sum(t.sendPrice) totalsendprice, sum(t.sendCount) totalsendcount, sum(t.priceTotal) totalprize, sum(t.sendPrice)-sum(t.priceTotal) totalprofit, FROM_UNIXTIME(t.createAt, '%Y-%m-%d') createdate");
			sql.append(" FROM bill_prize_list t");
			sql.append(" where 1=1");
			//幸运礼物判断
			if(StringUtil.isNotEmpty(gid)){
				sql.append(" and t.gid=");
				sql.append(gid);
			}
			//起始日期判断
			if(StringUtil.isNotEmpty(startdate)){
				sql.append(" and FROM_UNIXTIME(t.createAt, '%Y-%m-%d')>='");
				sql.append(startdate);
				sql.append(" '");
			}
			//结束日期判断
			if(StringUtil.isNotEmpty(enddate)){
				sql.append(" and FROM_UNIXTIME(t.createAt, '%Y-%m-%d')<='");
				sql.append(enddate);
				sql.append(" '");
			}
			sql.append(" GROUP BY createdate");
			sql.append(" )");
			sql.append(" C");
			sql.append(" where B.createdate = C.createdate");

			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			if (null != rs) {
				while (rs.next()) {
					total = rs.getInt(1);
				}
			}
			resultMap.put("total", total);
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

			}
		}


		return resultMap;
	}

	//获取所有下拉列表，提供给前端下拉列表使用，不分页
	public List<HashMap<String, Object>> getAllLuckyGiftList(){
		List<HashMap<String, Object>>  list =  new ArrayList<HashMap<String, Object>>();

		String sql = "SELECT cg.gid, cg.gname FROM config_giftlist cg, config_gift_activity cga WHERE cg.gid = cga.gid AND cga.atype = 2";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_config,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("gid", rs.getString("gid"));
					map.put("gname", rs.getString("gname"));
					list.add(map);
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
					rs.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				System.out.println("login->finally->exception:" + e.getMessage());
			}
		}
		return list;
	}

	public HashMap<String, Object> getPrizeDetailList(String page, String rows, String createdate, String multiple, String gid){

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		HashMap<String, Object> resultMap = new HashMap<String, Object>(); 

		//list-查询分页数据
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		int begin = (Integer.valueOf(page) - 1) * Integer.valueOf(rows);
		sql.append("select FROM_UNIXTIME(t.createAt, '%Y-%m-%d') createdate, t.uid, ");
		sql.append(" (select u.nickname from zhu_admin.user_base_info u where u.uid = t.uid) usernickname,");
		sql.append(" (select u.userLevel from zhu_admin.user_base_info u where u.uid = t.uid) userlevel,");
		sql.append(" t.gid, g.gname, t.anchoruid, ");
		sql.append(" (select u.nickname from zhu_admin.user_base_info u where u.uid = t.anchoruid) anchornickname,");
		sql.append(" (select u.userLevel from zhu_admin.user_base_info u where u.uid = t.anchoruid) anchorlevel,");
		sql.append(" t.luckyCount ");
		sql.append(" from zhu_bill.bill_prize_list t, zhu_config.config_giftlist g");
		sql.append(" where t.gid = g.gid");
		sql.append(" and t.multiples = ");
		sql.append(multiple);
		//判断gid
		if(StringUtil.isNotEmpty(gid)){
			sql.append(" and t.gid = ");
			sql.append(gid);
		}
		//判断日期
		if(StringUtil.isNotEmpty(createdate)){
			sql.append(" and FROM_UNIXTIME(t.createAt, '%Y-%m-%d') = ");
			sql.append(" '");
			sql.append(createdate);
			sql.append(" '");
		}
		sql.append(" ORDER BY createdate, t.uid");
		sql.append(" limit ");
		sql.append(begin);
		sql.append(", ");
		sql.append(Integer.valueOf(rows));
		System.out.println(sql.toString());

		try {
			con = DbUtil.instance().getCon("slave");
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			if (null != rs) {
				try {
					while (rs.next()) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("createdate", rs.getString("createdate"));
						map.put("uid", rs.getInt("uid"));
						map.put("usernickname", rs.getString("usernickname"));
						map.put("userlevel", rs.getInt("userlevel"));
						map.put("gid", rs.getInt("gid"));
						map.put("gname", rs.getString("gname"));
						map.put("anchoruid", rs.getInt("anchoruid"));
						map.put("anchornickname", rs.getString("anchornickname"));
						map.put("luckycount", rs.getInt("luckyCount"));

						list.add(map);
					}
				} catch (SQLException e) {
					e.printStackTrace();
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

			}
		}
		resultMap.put("rows", list);

		//total-查询总数
		int total = 0;
		sql = new StringBuffer();
		sql.append("select count(t.uid) ");
		sql.append(" from zhu_bill.bill_prize_list t, zhu_config.config_giftlist g");
		sql.append(" where t.gid = g.gid");
		sql.append(" and t.multiples = ");
		sql.append(multiple);
		//判断gid
		if(StringUtil.isNotEmpty(gid)){
			sql.append(" and t.gid = ");
			sql.append(gid);
		}
		//判断日期
		if(StringUtil.isNotEmpty(createdate)){
			sql.append(" and FROM_UNIXTIME(t.createAt, '%Y-%m-%d') = ");
			sql.append(" '");
			sql.append(createdate);
			sql.append(" '");
		}


		try {
			con = DbUtil.instance().getCon("slave");
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			if (null != rs) {
				try {
					while (rs.next()) {
						total = rs.getInt(1);
					}
				} catch (SQLException e) {
					e.printStackTrace();
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

			}
		}
		resultMap.put("total", total);

		return resultMap;

	}
}
