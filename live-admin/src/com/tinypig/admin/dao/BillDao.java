package com.tinypig.admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.tinypig.admin.constant;
import com.tinypig.admin.util.DateUtil;
import com.tinypig.admin.util.DbUtil;

public class BillDao {
	private static final BillDao billDao = new BillDao();
	
	public static BillDao getInstance(){
		return billDao;
	}
	
	/*
	 * 商城消费
	 */
	public void getMallConsume(int uid,int unionid,Long stime,Long etime,String YM){
		
		String strYMD = DateUtil.datetime2String(stime*1000, "yyyyMMdd");
		int iYMD = Integer.valueOf(strYMD);
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			// 商城购买
			String sqlMall = "SELECT sum(realpricetotal) as zhutou from pay_mall_list "
					+ " where srcuid=" + uid + " and type != 1 and createAt>=" + stime + " and createAt<"+ etime;
			con = DbUtil.instance().getCon(constant.db_zhu_pay, constant.db_slave);
			pstmt = con.prepareStatement(sqlMall);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				while(rs.next()){
					if (rs.getInt("zhutou") > 0 ) {
						PayOrderDao.getInstance().addSupportConsume(iYMD, uid, unionid, 0, rs.getInt("zhutou"));
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
	
	/*
	 * 砸蛋游戏
	 */
	public void getEggConsume(int uid,int unionid,Long stime,Long etime,String YM){
		
		String strYMD = DateUtil.datetime2String(stime*1000, "yyyyMMdd");
		int iYMD = Integer.valueOf(strYMD);
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			// 商城购买
			String sqlEgg = "SELECT sum(hammer_price*hammer_count) as zhutou from game_smashed_egg_log "
					+ " where uid="+ uid +" and createAt>="+ stime +" and createAt<"+ etime;
			con = DbUtil.instance().getCon(constant.db_zhu_game, constant.db_slave);
			pstmt = con.prepareStatement(sqlEgg);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				while(rs.next()){
					if (rs.getInt("zhutou") > 0 ) {
						PayOrderDao.getInstance().addSupportConsume(iYMD, uid, unionid, 0, rs.getInt("zhutou"));
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

	
	/*
	 * 抽奖消费
	 */
	public void getLottoryConsume(int uid,int unionid,Long stime,Long etime,String YM){
		
		String strYMD = DateUtil.datetime2String(stime*1000, "yyyyMMdd");
		int iYMD = Integer.valueOf(strYMD);
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sqlLottory = "SELECT sum(consume) as zhutou from lottory_consume_list "
					+ " where uid="+ uid +" and addtime>="+ stime +" and addtime<"+ etime;
			con = DbUtil.instance().getCon(constant.db_zhu_pay, constant.db_slave);
			pstmt = con.prepareStatement(sqlLottory);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				while(rs.next()){
					if (rs.getInt("zhutou") > 0 ) {
						PayOrderDao.getInstance().addSupportConsume(iYMD, uid, unionid, 0, rs.getInt("zhutou"));
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
	
	/*
	 * 打赏消费
	 */
	public void getFeedConsume(int uid,int unionid,Long stime,Long etime,String YM){
		
		String strYMD = DateUtil.datetime2String(stime*1000, "yyyyMMdd");
		int iYMD = Integer.valueOf(strYMD);
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sqlFeed ="SELECT sum(zhutou) as zhutou from zhu_user.user_feed_reward "
					+ " where rewardUid="+ uid +" and createAt>="+ stime +" and createAt<"+ etime;
			con = DbUtil.instance().getCon(constant.db_zhu_pay, constant.db_slave);
			pstmt = con.prepareStatement(sqlFeed);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				while(rs.next()){
					if (rs.getInt("zhutou") > 0 ) {
						PayOrderDao.getInstance().addSupportConsume(iYMD, uid, unionid, 0, rs.getInt("zhutou"));
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
	
	/**
	 * 生成扶持号 消费数据
	 * @param uid
	 * @param unionid
	 * @param stime
	 * @param etime
	 * @param YM
	 */
	public void getSupportConsume(int uid,int unionid,Long stime,Long etime,String YM){
		
		String strYMD = DateUtil.datetime2String(stime*1000, "yyyyMMdd");
		
		int iYMD = Integer.valueOf(strYMD);

		String strTableName = "bill_" + YM;
		// 送礼
		String sqlGift = " select sum(case when a.familyid != " + unionid + " and dstuid > 0 then a.count*a.price else 0 end) as support,sum(a.count*a.price) as amount "
				+ " from " + strTableName + " a,zhu_config.config_giftlist b where a.gid=b.gid and a.srcuid = " + uid + " and addtime >= " + stime + " and addtime < " + etime;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_bill, constant.db_slave);
			pstmt = con.prepareStatement(sqlGift);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				while(rs.next()){
					if (rs.getInt("support") > 0 || rs.getInt("amount") > 0) {
						
						PayOrderDao.getInstance().addSupportConsume(iYMD, uid, unionid, rs.getInt("support"), rs.getInt("amount"));
					}
				}
				rs.close();
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
	}
	
	public long getCredit(int uid,String startDate,String endDate){
		long endCredit = getCreditByDate(uid, endDate, false, startDate);
		if(endCredit == 0){
			String[] splits = startDate.split("-");
			if(splits != null && splits.length == 3){
				endDate = splits[0]+"-"+splits[1] + "-" + getday(splits[1]); 
				endCredit = getCreditByDate(uid, endDate, false, startDate);
			}
		}
		long startCredit = getCreditByDate(uid, startDate, true, startDate);
		if(startCredit == endCredit)
			return endCredit;
		return  endCredit - startCredit < 0 ? 0 : endCredit - startCredit;
	}
	

	/**
	 * 生成扶持号，非扶持消费，红包
	 * @param uid
	 * @param unionid
	 * @param stime
	 * @param etime
	 * @param YM
	 */
	public void getSupportRed(int uid,int unionid,Long stime,Long etime,String YM){
		
		String strYMD = DateUtil.datetime2String(stime*1000, "yyyyMMdd");
		
		int iYMD = Integer.valueOf(strYMD);

		String strTableName = "bill_redenvelope";
		// 收红包
		String sql = "select sum(case when srcuid > 0 and srcuid != dstuid then sendmoney else 0 end) as support,sum(sendmoney) as amount "
				+ " from " + strTableName + " where srcuid = " + uid + " and sendtime >= " + stime + " and sendtime < " + etime;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_bill, constant.db_slave);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				while(rs.next()){
					if (rs.getInt("support") > 0 || rs.getInt("amount") > 0) {
						PayOrderDao.getInstance().addSupportConsume(iYMD, uid, unionid, rs.getInt("support"), rs.getInt("amount"));
					}
				}
				rs.close();
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
	}
	
	
	/**
	 * 
	 * @param date 日期边界
	 * @param isStart 标示开始日期或结束日期
	 * @return
	 */
	private long getCreditByDate(int uid, String date,boolean isStart,String startDate){
		long credit = 0;
		int dateSec = 0;
		String tableName = getTableName(date);
		String where = " where dstuid=" + uid;
		String orderBy = isStart ? " order by addtime asc ":" order by addtime desc ";
		if(tableName == null){
			return 0;
		}
		if(isStart){
			date+=" 00:00:00";
			try {
				dateSec = DateUtil.getTimeStamp(DateUtil.formatString(date, "yyyy-MM-dd HH:mm:ss"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			orderBy = " order by addtime desc ";
			where += " and gid<>1 and addtime<" + dateSec;
		}else{
			date+=" 23:59:59";
			startDate +=" 00:00:00";
			int startSec = 0;
			try {
				startSec = DateUtil.getTimeStamp(DateUtil.formatString(startDate, "yyyy-MM-dd HH:mm:ss"));
				dateSec = DateUtil.getTimeStamp(DateUtil.formatString(date, "yyyy-MM-dd HH:mm:ss"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			orderBy = " order by addtime desc ";
			where += " and gid<>1 and addtime >="+startSec + " and addtime<=" + dateSec;
		}
		String sql="select dstcredit from "+ tableName + where + orderBy + "limit 0,1;";
//		System.out.println("bill sql:" + sql);
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_bill,"master");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				credit = rs.getLong(1);
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
		return credit;
	}
	
	private int getday(String month){
		if(month.equals("01") || month.equals("03") || month.equals("05") || month.equals("07") || month.equals("08") || month.equals("10") || month.equals("12")){
			return 31;
		}else{
			return 30;
		}
	}
	
	private String getTableName(String date){
		if(date != null){
			String[] splits = date.split("-");
			if(splits != null && splits.length == 3){
				return constant.tb_bill + splits[0] + splits[1];
			}
		}
		return null;
	}

}
