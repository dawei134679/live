package com.tinypig.newadmin.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tinypig.admin.constant;
import com.tinypig.admin.util.DbUtil;

public class GameDao {
	
	private final static GameDao instance = new GameDao();
	
	public static GameDao getInstance(){
		return instance;
	}
	

	/**
	 * 获取下注及中奖数
	 * @param istime
	 * @param ietime
	 * @param uid
	 * @return map(times,bets,wins,deduct_money,add_price)
	 */
	public Map<String, Object> getbetwinlistOfTime(Long istime,Long ietime,int uid,int page,int size){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Object> mapResult = new HashMap<String, Object>();
		
		try {
			String sql = "select from_unixtime(add_time,'%Y-%m-%d') as add_time,count(*) as bets,sum(add_price) as add_price,count(distinct uid) as persons,"
					+ "sum(case when add_price>0 then 1 else 0 end) as wins,sum(deduct_money) as deduct_money,"
					+" sum(case when nid=2 then deduct_money else 0 end) as nid2,"
					+" sum(case when nid=3 then deduct_money else 0 end) as nid3,"
					+" sum(case when nid=5 then deduct_money else 0 end) as nid5,"
					+" sum(case when nid=7 then deduct_money else 0 end) as nid7,"
					+" sum(case when nid=8 then deduct_money else 0 end) as nid8,"
					+" sum(case when nid=11 then deduct_money else 0 end) as nid11"
					+ " from zhu_game.game_user_bet_log where add_time >= ? and add_time < ?";
			if (uid > 0) {
				sql = sql + " and uid = ? ";
			}
			sql = sql + " group by from_unixtime(add_time,'%Y-%m-%d') order by add_time desc limit "+(page-1)*size+","+ size;
			
			con = DbUtil.instance().getCon(constant.db_zhu_game,"slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, istime);
			pstmt.setLong(2, ietime);
			if (uid > 0 ) {
				pstmt.setInt(3, uid);
			}
			
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				Map<String, Object> mapFoot = new HashMap<String, Object>();
				
				int allPerson = 0;
				int allBets = 0;
				int allWins = 0;
				int allNid7 = 0;
				int allNid11 = 0;
				int allNid8 = 0;
				int allNid2 = 0;
				int allNid3 = 0;
				int allNid5 = 0;
				int allDeduct_money = 0;
				int allAdd_price = 0;
				
				while (rs.next()) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("add_time", rs.getObject("add_time"));
					map.put("persons", rs.getObject("persons"));
					allPerson += rs.getInt("persons");
					
					map.put("bets", rs.getObject("bets"));
					allBets += rs.getInt("bets");
							
					map.put("wins", rs.getObject("wins"));
					allWins += rs.getInt("wins");
					
					if (rs.getInt("bets") > 0) {
						map.put("rate",String.format("%.2f",(rs.getInt("wins")*100.0)/rs.getInt("bets")));
					}else {
						map.put("rate", 0);
					}
					map.put("nid7", rs.getObject("nid7"));
					allNid7 += rs.getInt("nid7");
					
					map.put("nid11", rs.getObject("nid11"));
					allNid11 += rs.getInt("nid11");

					map.put("nid8", rs.getObject("nid8"));
					allNid8 += rs.getInt("nid8");
					
					map.put("nid2", rs.getObject("nid2"));
					allNid2 += rs.getInt("nid2");
					
					map.put("nid3", rs.getObject("nid3"));
					allNid3 += rs.getInt("nid3");
					
					map.put("nid5", rs.getObject("nid5"));
					allNid5 += rs.getInt("nid5");
					
					map.put("deduct_money", rs.getObject("deduct_money"));
					allDeduct_money += rs.getInt("deduct_money");
					
					map.put("add_price", rs.getObject("add_price"));
					allAdd_price += rs.getInt("add_price");
					
					map.put("profit", rs.getInt("deduct_money")-rs.getInt("add_price"));
					list.add(map);
				}
				mapResult.put("rows", list);
				
				mapFoot.put("add_time", "合计");
				mapFoot.put("persons", allPerson);
				mapFoot.put("bets", allBets);
				mapFoot.put("wins", allWins);
				mapFoot.put("rate", String.format("%.2f",(allWins*100.0)/allBets));
				mapFoot.put("nid7", allNid7);
				mapFoot.put("nid11", allNid11);
				mapFoot.put("nid8", allNid8);
				mapFoot.put("nid2", allNid2);
				mapFoot.put("nid3", allNid3);
				mapFoot.put("nid5", allNid5);
				mapFoot.put("deduct_money", allDeduct_money);
				mapFoot.put("add_price", allAdd_price);
				mapFoot.put("profit", allDeduct_money-allAdd_price);

				list = new ArrayList<Map<String,Object>>();
				list.add(mapFoot);
				mapResult.put("footer", list);
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
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return mapResult;
	}
	
	public Integer getbetwinlistsOfTime(Long istime,Long ietime,int uid){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int results = 0;
		
		try {
			String sql = "select from_unixtime(add_time,'%Y-%m-%d') as times "
					+ "from zhu_game.game_user_bet_log where add_time >= ? and add_time < ?";
			if (uid > 0) {
				sql = sql + " and uid = ? ";
			}
			sql = sql + " group by times ";

			con = DbUtil.instance().getCon(constant.db_zhu_game,"slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, istime);
			pstmt.setLong(2, ietime);
			if (uid > 0 ) {
				pstmt.setInt(3, uid);
			}
			
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				
				while (rs.next()) {
					results += 1;
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
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		
		return results;
	}
	
	public List<Map<String, Object>> getNidBetWinlist(Long istime,Long ietime,int nid){

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			String sql = "select from_unixtime(add_time,'%Y-%m-%d') as add_time,count(*) as bets,sum(case when add_price>0 then 1 else 0 end) as wins,sum(deduct_money) as deduct_money,sum(add_price) as add_price "
					+ "from zhu_game.game_user_bet_log where add_time >= ? and add_time < ? ";
			if (nid > 0) {
				sql = sql + " and nid = ? ";
			}
			sql = sql + " group by from_unixtime(add_time,'%Y-%m-%d') order by add_time desc ";
			
			con = DbUtil.instance().getCon(constant.db_zhu_game,"slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, istime);
			pstmt.setLong(2, ietime);
			if (nid > 0 ) {
				pstmt.setInt(3, nid);
			}
			
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				while (rs.next()) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("add_time", rs.getObject("add_time"));
					map.put("bets", rs.getObject("bets"));
					map.put("wins", rs.getObject("wins"));
					if (rs.getInt("bets") > 0) {
						map.put("rate",String.format("%.2f",(rs.getInt("wins")*100)/rs.getInt("bets")));
					}else {
						map.put("rate", 0);
					}
					map.put("deduct_money", rs.getObject("deduct_money"));
					map.put("add_price", rs.getObject("add_price"));
					
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
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return list;
	}
	
	public Integer getNidBetWins(Long istime,Long ietime,int nid){

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int results = 0;
		
		try {
			String sql = "select count(*) as cnts "
					+ "from zhu_game.game_user_bet_log where add_time >= ? and add_time < ? ";
			if (nid > 0) {
				sql = sql + " and nid = ? ";
			}
			sql = sql + " from_unixtime(add_time,'%Y-%m-%d') ";
			
			con = DbUtil.instance().getCon(constant.db_zhu_game,"slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, istime);
			pstmt.setLong(2, ietime);
			if (nid > 0 ) {
				pstmt.setInt(3, nid);
			}
			
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				while (rs.next()) {
					results = rs.getInt("cnts");
					break;
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
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return results;
	}
}
