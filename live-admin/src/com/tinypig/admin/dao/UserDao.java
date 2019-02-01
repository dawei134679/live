package com.tinypig.admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mysql.jdbc.Statement;
import com.tinypig.admin.constant;
import com.tinypig.admin.model.UserAccountModel;
import com.tinypig.admin.model.UserAsset;
import com.tinypig.admin.model.UserBaseInfo;
import com.tinypig.admin.model.UserBaseInfoModel;
import com.tinypig.admin.model.UserInfoModel;
import com.tinypig.admin.model.UserMannerModel;
import com.tinypig.admin.redis.service.RedisOperat;
import com.tinypig.admin.util.Constant;
import com.tinypig.admin.util.DateUtil;
import com.tinypig.admin.util.DbUtil;
import com.tinypig.admin.util.RedisContant;
import com.tinypig.admin.util.StringUtil;

import redis.clients.jedis.Tuple;

public class UserDao extends BaseDao {

	private final static UserDao instance = new UserDao();
	int time = (int) (System.currentTimeMillis() / 1000);

	public static UserDao getInstance() {
		return instance;
	}
	
	/**
	 * 获取扶持号  剩余金币数
	 * @param uid
	 * @param unionid
	 * @param stime
	 * @param etime
	 */
	public void getSupportSurplus(int uid,int unionid,Long stime){
		
		String strYMD = DateUtil.convert2String(stime*1000, "yyyyMMdd");
		
		String fix = "";
		int sufix = uid % 100;
		if (sufix < 10) {
			fix = "0"+sufix;
		}else {
			fix = String.valueOf(sufix);
		}
		String tablename = "user_asset_"+fix;
		
		String sql = " select money from " + tablename + " where uid = " + uid;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user, constant.db_slave);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				while(rs.next()){
					PayOrderDao.getInstance().addSupportSurplus(Integer.valueOf(strYMD), uid, unionid, rs.getInt("money"));
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
	
	public int editCover(int uid,String livimage,String pcimg1,String pcimg2,int adminid){
		
		int iResult = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		String fix = "";
		int sufix = uid % 100;
		if (sufix < 10) {
			fix = "0"+sufix;
		}else {
			fix = String.valueOf(sufix);
		}
		
		String tablename = "user_base_info_"+fix;
		String sql = " update "+ tablename;
		Boolean bl = false;
		if (StringUtils.isNotEmpty(livimage)) {
			sql = sql + " set livimage =  '" + livimage + "'";
			bl = true;
		}
		
		if (StringUtils.isNotEmpty(pcimg1)) {
			if (bl) {
				sql = sql + ",pcimg1 = '" + pcimg1 + "'";
			}else {
				sql = sql + " set pcimg1 = '" + pcimg1 + "'";
				bl = true;
			}
		}
		
		if (StringUtils.isNotEmpty(pcimg2)) {
			if (bl) {
				sql = sql + ",pcimg2 = '" + pcimg2 + "'";
			}else {
				sql = sql + " set pcimg2 = '" + pcimg2 + "'";
				bl = true;
			}
		}
		
		if (bl) {
			sql = sql + " where uid= " + uid;
			UserBaseInfoModel userBaseInfo = getUserBaseInfo(uid, false);
			
			try {
				con = DbUtil.instance().getCon(constant.db_zhu_user, constant.db_master);
				pstmt = con.prepareStatement(sql);
				
				iResult = pstmt.executeUpdate();
				
				if (iResult > 0 ) {
					
					Unirest.get(Constant.business_server_url + "/admin/refreshUserBaseInfo?uid=" + uid).asString();
					
					String previous_version = "";
					String current_version = "{livimage:"+livimage+",pcimg1:"+pcimg1+",pcimg2:"+pcimg2+"}";
					if (userBaseInfo != null) {
						previous_version = "{livimage:"+userBaseInfo.getLivimage()+",pcimg1:"+userBaseInfo.getPcimg1()+",pcimg2:"+userBaseInfo.getPcimg2()+"}";
					}
					
					AddOperationLog(tablename, String.valueOf(uid), "更新封面", 2, previous_version, current_version, adminid);
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
		}
		return iResult;
	}
	
	public List<Map<String, Object>> getTest(int page,int table){

		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String fix = "";
		if (table < 9) {
			fix = "0"+table;
		}else {
			fix = String.valueOf(table);
		}
		String tablename = "user_base_info_"+fix;
		
		try {
			String sql = "select uid,headimage,nickname from "+tablename+" limit "+(page-1)*140+",140";

			con = DbUtil.instance().getCon(constant.db_zhu_user,"slave");
			pstmt = con.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("uid", rs.getInt("uid"));
					map.put("headimage", rs.getString("headimage"));
					map.put("nickname", rs.getString("nickname"));
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 封号
	 * 
	 * @param uid
	 * @param cause
	 * @param admin
	 * @return
	 */
	public Map<String, Object> forbidAccount(int uid, String cause, int admin, int status) {

		Map<String, Object> map = new HashMap<String, Object>();

		HttpResponse<JsonNode> result = null;

		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			String sql = "";
			if (status == 1) {
				sql = "update user_handle set status=1 where uid=? and status=0";
				con = DbUtil.instance().getCon(constant.db_zhu_user,"master");
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, uid);

				pstmt.executeUpdate();
				// 接禁封号
				result = Unirest.get(Constant.business_server_url + "/admin/unforbid?uid=" + uid).asJson();
				
			} else if (status == 0) {
				sql = "insert into user_handle(uid,handle,cause,source,adminid,creatAt) values(?,?,?,?,?,?) ";
				con = DbUtil.instance().getCon(constant.db_zhu_user,"master");
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, uid);
				pstmt.setInt(2, 2);
				pstmt.setString(3, cause);
				pstmt.setInt(4, 2);
				pstmt.setInt(5, admin);
				pstmt.setLong(6, System.currentTimeMillis() / 1000);

				int ires = pstmt.executeUpdate();
				if (ires > 0) {
					// 封号
					result = Unirest.get(Constant.business_server_url + "/admin/forbid?uid=" + uid).asJson();
				}
			}
			if (result != null) {
				JSONObject object = result.getBody().getObject();
				if (object.getInt("code") != 200) {
					map.put("code", 400);
					map.put("errorMsg", "interface设置失败：" + object.getString("message"));
				} else {
					// 设置成功
					if (status == 1) {
						this.AddOperationLog("zhu_user.user_account_", String.valueOf(uid), "修改用户身份", 2, "status=0",
								"status=1", admin);
					}else if (status == 0) {
						this.AddOperationLog("zhu_user.user_account_", String.valueOf(uid), "修改用户身份", 2, "status=1",
								"status=0", admin);
					}
					map.put("code", 200);
				}
			} else {
				map.put("code", 202);
				map.put("errorMsg", "202 interface接口调用失败");
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
		return map;
	}

	/**
	 * 禁播
	 * 
	 * @param uid
	 * @param cause
	 * @param admin
	 * @return
	 */
	public Map<String, Object> banIdentity(int uid, String cause, int admin, int identity) {

		Map<String, Object> map = new HashMap<String, Object>();

		HttpResponse<JsonNode> result = null;
		
		String sql = "";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			if (identity == 1) {
				// 允许开播

				sql = "update user_handle set status=1 where uid=? and status=0";
				con = DbUtil.instance().getCon(constant.db_zhu_user,"master");
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, uid);

				pstmt.executeUpdate();
				// 接禁封号
				result = Unirest.get(Constant.business_server_url + "/admin/unban?anchoruid=" + uid).asJson();
				
			}else if (identity == 2) {
				// 禁播
				sql = "insert into user_handle(uid,handle,cause,source,adminid,creatAt) values(?,?,?,?,?,?) ";
				con = DbUtil.instance().getCon(constant.db_zhu_user,"master");
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, uid);
				pstmt.setInt(2, 1);
				pstmt.setString(3, cause);
				pstmt.setInt(4, 2);
				pstmt.setInt(5, admin);
				pstmt.setLong(6, System.currentTimeMillis() / 1000);

				int ires = pstmt.executeUpdate();
				if (ires > 0) {
					result = Unirest.get(Constant.business_server_url + "/admin/ban?anchoruid=" + uid).asJson();
				}
				
			}
			
			if (result != null) {
				JSONObject object = result.getBody().getObject();
				if (object.getInt("code") != 200) {
					map.put("code", 400);
					map.put("errorMsg", "interface设置失败：" + object.getString("message"));
				} else {
					// 设置成功
					this.AddOperationLog("zhu_user.user_base_info_", String.valueOf(uid), "修改用户身份", 2,
							"identity=" + (identity == 1 ? 2 : 1), "identity=" + identity, admin);
					map.put("code", 200);
				}
			} else {
				map.put("code", 202);
				map.put("errorMsg", "202 interface接口调用失败");
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
		return map;
	}

	/**
	 * 
	 * @param id
	 * @param cause
	 * @param uid
	 * @param admin
	 * @return
	 * @throws SQLException
	 *             1 封号 3禁终端 5禁播
	 */
	public Map<String, Object> edithandle(int id, String cause, int uid, int admin) {
		if (id == 1) {
			// 封号
			return this.forbidAccount(uid, cause, admin, 0);
		} else if (id == 2) {
			// 解封号
			return this.forbidAccount(uid, cause, admin, 1);
		} else if (id == 5) {
			// 禁播
			return this.banIdentity(uid, cause, admin, 2);
		} else if (id == 6) {
			// 解禁播
			return this.banIdentity(uid, cause, admin, 1);
		}
		return null;
	}

	/**
	 * 获取开播的总人数
	 * 
	 * @param type
	 * @param value
	 * @param recommend
	 * @return
	 */
	public int getAnchorTotal(String type, Object value, int recommend) {
		int ires = 0;
		String sql = "select count(*) as cnts from user_base_info WHERE liveStatus = 1";
		if ("uid".equals(type)) {
			sql = sql + " AND uid = ? ";
		} else if ("nickname".equals(type)) {
			sql = sql + " AND nickname = ? ";
		}
		if (recommend >= 0 && recommend <= 9) {
			sql = sql + " AND recommend =" + recommend;
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			con = DbUtil.instance().getCon(constant.db_zhu_admin,"slave");
			pstmt = con.prepareStatement(sql);
			if ("uid".equals(type)) {
				pstmt.setInt(1, (Integer) value);
			} else if ("nickname".equals(type)) {
				pstmt.setString(1, value.toString());
			}
			rs = pstmt.executeQuery();

			if (rs != null) {
				while (rs.next()) {
					ires = rs.getInt("cnts");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
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
				ex.printStackTrace();
			}
		}
		return ires;
	}

	/**
	 * 修改用户身份
	 * 
	 * @param uid
	 * @param identity
	 *            =1主播 =2看客 =3超管
	 * @param adminId
	 * @return
	 */
	public Map<String, Object> setIdentity(int uid, int identity, int adminId, String cause) {

		Map<String, Object> map = new HashMap<String, Object>();
		int sufix = uid % 100;
		String dbname = "user_base_info_" + (sufix < 10 ? "0" + sufix : sufix);
		String selectSql = "select identity from " + dbname + " where uid=" + uid;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int oldIdentity = 0;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user,"slave");
			pstmt = con.prepareStatement(selectSql);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				// 更新前 identity的值
				oldIdentity = rs.getInt("identity");

				HttpResponse<JsonNode> result = null;
				if (identity == 1) {
					// 解禁
					result = Unirest.get(Constant.business_server_url_shark1 + "/admin/unban?anchoruid=" + uid).asJson();
				} else if (identity == 2) {
					// 禁播
					result = Unirest.get(Constant.business_server_url_shark1 + "/admin/ban?anchoruid=" + uid).asJson();
				} else if (identity == 3) {
					// 设置超管
				}

				if (result != null) {
					JSONObject object = result.getBody().getObject();
					if (object.getInt("code") != 200) {
						map.put("code", 201);
						map.put("errorMsg", "201 设置失败：" + object.getString("message"));
					} else {
						if (identity == 2) {
							this.insertUserHandle(uid, 1, cause, 2, adminId);
						}
						// 设置成功
						this.AddOperationLog(dbname, String.valueOf(uid), "修改用户身份", 2, "identity=" + oldIdentity,
								"identity=" + identity, adminId);
						map.put("code", 200);
					}
				} else {
					map.put("code", 202);
					map.put("errorMsg", "202 接口调用失败，identity=" + identity);
				}
			} else {
				map.put("code", 205);
				map.put("errorMsg", "205 接口调用失败，identity=" + identity);
			}
		} catch (Exception ex) {
			map.put("code", 203);
			map.put("errorMsg", "203 eception:" + ex.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();

				if (pstmt != null)
					pstmt.close();

				if (con != null)
					con.close();

			} catch (Exception e) {
				map.put("code", 204);
				map.put("errorMsg", "204 eception:" + e.getMessage());
			}
		}
		return map;
	}

	/**
	 * 修改用户的开播 类别
	 * 
	 * @param uid
	 * @param recommmend
	 *            =0 正常开播 ＝1 热门主播 ＝2 推荐主播
	 * @return =1 表示更新成功 ＝0 表示更新失败
	 */
	public Map<String, Object> setUserCommend(int uid, int recommmend, int contrRq,int grade,int adminId) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		UserBaseInfoModel userBaseInfo = UserDao.getInstance().getUserBaseInfo(uid, false);
		if (userBaseInfo == null) {
			return map;
		}

		if (uid <= 0) {
		} else if (recommmend < 0 || recommmend > 5) {
		} else {
			int sufix = uid % 100;
			String dbname = "user_base_info_" + (sufix < 10 ? "0" + sufix : sufix);
			String selectSql = "select recommend,opentime,contrRq,grade from " + dbname + " where uid=" + uid;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			int oldRecommend = 0;
			int oldRq = 0;
			int oldGrade =0;
			try {
				con = DbUtil.instance().getCon(constant.db_zhu_user,"slave");
				pstmt = con.prepareStatement(selectSql);
				rs = pstmt.executeQuery();
				if (rs != null && rs.next()) {
					oldRecommend = rs.getInt("recommend");
					oldRq = rs.getInt("contrRq");
					oldGrade = rs.getInt("grade");
					if (oldRecommend == recommmend && oldRq == contrRq&&oldGrade==grade && oldGrade==grade) {

					} else {
						HttpResponse<JsonNode> result = Unirest.get(Constant.business_server_url + "/admin/recommend_rq?uid="
								+ uid + "&recommend=" + recommmend + "&rq=" + contrRq + "&grade=" + grade).asJson();
						if (result != null) {
							JSONObject object = result.getBody().getObject();
							if (object.getInt("code") != 200) {
								map.put("code", 201);
								map.put("msg", "201 设置失败：" + object.getString("message"));
							} else {
								if (userBaseInfo.getRecommend() == 0 && recommmend > 0 ) {
									RedisOperat.getInstance().zadd(RedisContant.host, RedisContant.port6381, RedisContant.newAnchorAll, System.currentTimeMillis()/1000, String.valueOf(uid));
									Long zcard = RedisOperat.getInstance().zcard(RedisContant.host, RedisContant.port6381, RedisContant.newAnchorAll);
									
									if (zcard > 200) {
										Set<Tuple> zrevrangeWithScores = RedisOperat.getInstance().zrevrangeWithScores(RedisContant.host, RedisContant.port6381, RedisContant.newAnchorAll,200,zcard.intValue());
										for(Tuple tuple:zrevrangeWithScores){
											RedisOperat.getInstance().zrem(RedisContant.host, RedisContant.port6381, RedisContant.newAnchorAll, tuple.getElement());
											RedisOperat.getInstance().zrem(RedisContant.host, RedisContant.port6381, RedisContant.newAnchor, tuple.getElement());
										}
									}
									
								}else if (userBaseInfo.getRecommend() > 0 && recommmend == 0) {
									RedisOperat.getInstance().zrem(RedisContant.host, RedisContant.port6381, RedisContant.newAnchorAll, String.valueOf(uid));
									RedisOperat.getInstance().zrem(RedisContant.host, RedisContant.port6381, RedisContant.newAnchor, String.valueOf(uid));
								}
								// 设置成功
								this.AddOperationLog(dbname, String.valueOf(uid), "修改推荐人气", 2,
										"oldrecommend=" + oldRecommend + " oldcontrRq=" + oldRq,
										"recommend=" + recommmend + " contrRq=" + contrRq, adminId);
								map.put("code", 200);
								map.put("msg", "设置成功");
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (rs != null)
						rs.close();

					if (pstmt != null)
						pstmt.close();

					if (con != null)
						con.close();

				} catch (Exception e2) {
					System.out.println("setUserCommend->exception:" + e2.getMessage());
				}
			}
		}
		return map;
	}

	/**
	 * 帐号状态变更
	 * 
	 * @param uid
	 * @param status
	 *            =1 正常 =0 封号
	 * @return
	 */
	public Map<String, Object> setBlockAnchor(int uid, int status, int adminId, String cause) {
		Map<String, Object> map = new HashMap<String, Object>();

		if (uid > 0 && (status == 0 || status == 1)) {
			int sufix = uid % 100;
			String dbname = "user_account_" + (sufix < 10 ? "0" + sufix : sufix);
			String selectSql = "select status from " + dbname + " where uid=" + uid;

			Connection con = null;
			PreparedStatement pstmt = null;
			int oldStatus = 0;
			try {
				con = DbUtil.instance().getCon(constant.db_zhu_user,"slave");
				pstmt = con.prepareStatement(selectSql);
				ResultSet rs = pstmt.executeQuery();
				if (rs != null && rs.next()) {
					if (status == rs.getInt("status")) {
						map.put("code", 200);
						map.put("msg", "设置成功");
					} else {
						oldStatus = rs.getInt("status");
						HttpResponse<JsonNode> asJson = null;
						if (status == 1) {
							asJson = Unirest.get(Constant.business_server_url_shark1 + "/admin/unforbid?uid=" + uid).asJson();
						} else {
							asJson = Unirest.get(Constant.business_server_url_shark1 + "/admin/forbid?uid=" + uid).asJson();
						}

						if (asJson != null) {
							JSONObject object = asJson.getBody().getObject();
							if (object.getInt("code") == 200) {
								map.put("code", 200);
								map.put("msg", "设置成功");
								this.insertUserHandle(uid, 2, cause, 2, adminId);
								this.AddOperationLog(dbname, String.valueOf(uid), "修改账户状态", 2, "status=" + oldStatus,
										"status=" + status, adminId);
							} else {
								map.put("code", 201);
								map.put("msg", "201 设置失败：" + object.getString("message"));
							}
						} else {
							map.put("code", 202);
							map.put("msg", "202 设置失败,接口调用失败");
						}
					}
				} else {
					map.put("code", 203);
					map.put("msg", "203 获取该用户失败");
				}
			} catch (Exception e) {
				map.put("code", 204);
				map.put("msg", "204 Exception:" + e.getMessage());
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
		}
		return map;
	}
	
	/**
	 * 获取用户状态
	 * 
	 * @param uid
	 * @param status
	 *            =1 正常 =0 封号 -1未知
	 * @return
	 */
	public int getAnchorStatus(int uid) {

		int sufix = uid % 100;
		String dbname = "user_account_" + (sufix < 10 ? "0" + sufix : sufix);
		String selectSql = "select status from " + dbname + " where uid=" + uid;

		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user,"slave");
			pstmt = con.prepareStatement(selectSql);
			ResultSet rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				return rs.getInt("status");
			} else {
				return -1;
			}
		} catch (Exception e) {
			return -1;
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
	}

	public void delUserHandle(int uid) {
		String sql = "delete from user_handle where uid=" + uid;
		Connection con = null;
		PreparedStatement pstmt = null;
		int ires = 0;
		try {
			con = DbUtil.instance().getCon("zhu_user","master");
			pstmt = con.prepareStatement(sql);
			ires = pstmt.executeUpdate();
			if (ires != 1) {
				System.out.println("delUserHandle--delete is err:" + sql);
			}
		} catch (Exception e) {
			System.out.println("delUserHandle--exception:" + e.getMessage());
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				System.out.println("delUserHandle->finally->exception:" + e2.getMessage());
			}
		}
	}

	/**
	 * 添加禁播等操作
	 * 
	 * @param uid
	 * @param handle
	 *            =1禁播 ＝2封号
	 * @param cause
	 *            原因
	 * @param source
	 *            ＝1客户端 ＝2后台
	 * @param adminid
	 *            操作者uid
	 */
	public void insertUserHandle(int uid, int handle, String cause, int source, int adminid) {
		String sql = "insert into user_handle(uid,handle,cause,source,adminid,creatAt)value(?,?,?,?,?,?)";
		int ires = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbUtil.instance().getCon("zhu_user","master");
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, uid);
			pstmt.setInt(2, handle);
			pstmt.setString(3, cause);
			pstmt.setInt(4, source);
			pstmt.setInt(5, adminid);
			pstmt.setInt(6, (int) (System.currentTimeMillis() / 1000));
			ires = pstmt.executeUpdate();
			if (ires != 1) {
				System.out.println("insertUserHandle->插入user_handle失败");
			}
		} catch (Exception e) {
			System.out.println("insertUserHandle->Exception：" + e.getMessage());
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				System.out.println("insertUserHandle->finally->Exception：" + e2.getMessage());
			}
		}
	}

	public Map<String, Object> getUserHandle(int uid) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "SELECT uid,handle,cause,source,adminid FROM user_handle WHERE uid=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int sufix = 0;
		String dbname = "";

		try {
			con = DbUtil.instance().getCon("zhu_user","slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, uid);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				map.put("cause", rs.getString("cause"));
				map.put("source", rs.getInt("source"));
			} else {
				return null;
			}
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}

			sufix = uid % 100;
			dbname = "user_base_info_" + (sufix < 10 ? "0" + sufix : sufix);
			sql = "select uid,nickname,familyId,anchorLevel,userLevel,identity,recommend from " + dbname + " where uid="
					+ uid;
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				map.put("uid", uid);
				map.put("nickname", rs.getString("nickname"));
				map.put("familyId", rs.getInt("familyId"));
				map.put("anchorLevel", rs.getInt("anchorLevel"));
				map.put("userLevel", rs.getInt("userLevel"));
				map.put("identity", rs.getInt("identity"));
				map.put("recommend", rs.getInt("recommend"));
			} else {
				return null;
			}

			if (Integer.valueOf(map.get("familyId").toString()) > 0) {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}

				sql = "select unionname from zhu_union.union_info where unionid=" + map.get("familyId");

				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
				if (rs != null && rs.next()) {
					map.put("familyName", rs.getString("unionname"));
				} else {
					map.put("familyName", "数据异常");
				}
			} else {
				map.put("familyName", "自由人");
			}
		} catch (Exception e) {
			System.out.println("getUserHandle->exception:" + e.getMessage());
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
				System.out.println("getUserHandle->finally->exception:" + ex.getMessage());
			}
		}

		return map;
	}

	public UserBaseInfo/* Map<String, Object> */ getUserPhone(int uid,boolean isSuper) {
		// Map<String, Object> map = new HashMap<String, Object>();

		String dbname = getDbName(uid, "user_base_info_");

		String sql = "select * from " + dbname + " where uid=" + uid;

		Connection con = null;
		PreparedStatement pstmt = null;
		UserBaseInfo userInfo = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user,"slave");
			// System.out.println("sql === " + sql);
			pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			if (rs != null && rs.next()) {

				userInfo = new UserBaseInfo().populateFromResultSet(rs);
				if(false == isSuper){
					String strPhone = userInfo.getPhone();
					if(null != strPhone && 11 == strPhone.length()){
						userInfo.setPhone(strPhone.substring(0, 3) + "****"+strPhone.substring(7));	//tosy add protection for super admin	
					}
				}
			}

		} catch (Exception e) {
			System.out.println("UserBaseInfo->Exception:" + e.getMessage());
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				System.out.println("getUserBaseInfo->Exception:" + e2.getMessage());
			}
		}
		return userInfo;
	}
	
	public int updateUserBaseInfo(int uid, String nickname, String reason) {
		int result = 0;
		String dbname = getDbName(uid, "user_base_info_");
		String sql = "update " + dbname + " set nickname = '" + nickname + "' where uid=" + uid;

		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user,"master");
			// System.out.println("sql === " + sql);
			pstmt = con.prepareStatement(sql);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("updateUserBaseInfo->Exception:" + e.getMessage());
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				System.out.println("updateUserBaseInfo->Exception:" + e2.getMessage());
			}
		}
		return result;
	}

	/**
	 * 获取用户信息
	 * 
	 * @param uid
	 * @return
	 */
	public UserBaseInfoModel getUserBaseInfo(int uid,Boolean readMysql) {
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		UserBaseInfoModel userBaseInfoModel = null;

		if (!readMysql) {
			// 读缓存
			String userbaseinfo = RedisOperat.getInstance().hget(RedisContant.host, RedisContant.port6379, RedisContant.keyBaseInfoList, String.valueOf(uid));
			if (org.apache.commons.lang.StringUtils.isNotEmpty(userbaseinfo)) {
				userBaseInfoModel = (UserBaseInfoModel) com.alibaba.fastjson.JSONObject.parseObject(userbaseinfo, UserBaseInfoModel.class);
			}
		}
		try {
			if (userBaseInfoModel == null) {
				String dbname = getDbName(uid, "user_base_info_");
				String SQL_GetUserBaseInfoByUid = "SELECT uid,nickname,sex,anchorLevel,userLevel,exp,identity,headimage,livimage,pcimg1,pcimg2,birthday,phone,province,city,signature,registip,registtime,registchannel,subregistchannel,registos,registimei,liveStatus,opentime,recommend,videoline,familyId,verified,verified_reason,contrRq,constellation,hobby,grade"
						+ " FROM %s WHERE uid = " + uid;
				// 获取用户基本信息
				conn = DbUtil.instance().getCon(constant.db_zhu_user, constant.db_slave);
				statement = conn.prepareStatement(String.format(SQL_GetUserBaseInfoByUid, dbname));
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						userBaseInfoModel = new UserBaseInfoModel().populateFromResultSet(rs);
						break;
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
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return userBaseInfoModel;
	}
	
	/**
	 * 获取审核用户封面信息
	 * 
	 * @param uid	主播编号
	 * @return
	 */
	public UserBaseInfo getUserCoverInfo(int userId,int uid,int unionid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		UserBaseInfo userBaseInfoModel = null;
		try {
			String dbname = getDbName(uid, "user_base_info_");
			String sql = "SELECT ub.uid,ub.nickname,ub.sex,ub.anchorLevel,ub.userLevel,ub.exp,ub.identity,ub.headimage,ub.birthday,ub.phone,ub.province,ub.city,ub.signature,ub.registip,ub.registtime,ub.registchannel,ub.subregistchannel,ub.registos,ub.registimei,ub.liveStatus,ub.opentime,ub.recommend,ub.videoline,ub.familyId,ub.verified,ub.verified_reason,ub.contrRq,ub.constellation,ub.hobby,ub.livimage,ub.pcimg1,ub.pcimg2"
					+ " FROM "+dbname+" ub";
			// 获取用户基本信息
			conn = DbUtil.instance().getCon(constant.db_zhu_user, constant.db_slave);
			statement = conn.prepareStatement(sql);
			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					userBaseInfoModel = new UserBaseInfo().populateFromResultSet(rs);
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
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return userBaseInfoModel;
	}

	public List<UserInfoModel> getUserInfo(int uid,boolean isSuper) {

		List<UserInfoModel> list = new ArrayList<UserInfoModel>();
		String dbname = getDbName(uid, "user_base_info_");
		String dbname2 = getDbName(uid, "user_asset_");
		String sql = "SELECT i.* ,a.wealth,a.money,a.creditTotal,a.credit,u.addtime,ifnull((SELECT handle from user_handle WHERE uid="
				+ uid + " and status=0),0)handle from " + dbname + " i," + dbname2 + " a,user_id u  where i.uid=a.uid and i.uid=" + uid
				+ " and u.id=" + uid;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					UserInfoModel cUserModel = new UserInfoModel().populateFromResultSet(rs);
					if (cUserModel != null) {
						if(false == isSuper){
							String strPhone = cUserModel.getPhone();
							if(null != strPhone && 11 == strPhone.length()){
								cUserModel.setPhone(strPhone.substring(0, 3) + "****"+strPhone.substring(7));	//tosy add protection for super admin	
							}
						}
						list.add(cUserModel);
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	// 查找用户信息
	public List<Map<String, Object>> getManagerUser(int uid) {
		List<Map<String, Object>> listUsers = new ArrayList<Map<String, Object>>();
		
		String dbname = getDbName(uid, "user_base_info_");
		String sql = "select i.uid,i.nickname,i.anchorLevel,h.creatAt,h.source,i.userLevel,h.adminid,h.handle,h.cause from " + dbname
				+ " i left join user_handle h on i.uid=h.uid and h.status=0 where i.uid=" + uid;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("uid", rs.getInt("uid"));
					map.put("handle", rs.getInt("handle"));
					map.put("cause", rs.getString("cause"));
					map.put("source", rs.getInt("source"));
					map.put("creatAt", rs.getInt("creatAt"));
					map.put("adminid", rs.getInt("adminid"));
					
					map.put("nickname", rs.getString("nickname"));
					map.put("anchorLevel", rs.getInt("anchorLevel"));
					map.put("userLevel", rs.getInt("userLevel"));
					
					listUsers.add(map);
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listUsers;
	}

	public List<UserMannerModel>/* Map<String, Object> */ getManagerUserStatus(List<Integer> info, String page,
			String rows) {
		List<UserMannerModel> list = new ArrayList<UserMannerModel>();
		String sq = "";
		String sql = "";
		for (int i = 0; i < info.size(); i++) {
			int uid = info.get(i);
			String dbname = getDbName(uid, "user_base_info_");
			sq = sq + "select i.uid,i.nickname,i.anchorLevel,i.userLevel,h.adminid,h.handle,h.cause,(select count(*) from zhu_live.live_report where anchorUid="
					+ uid + ") num from " + dbname + " i left join user_handle h on i.uid=h.uid where i.uid=" + uid
					+ " and h.`status` = 0"
					+ " union ";
		}
		if (sq.length() > 7) {
			sql = sq.substring(0, sq.length() - 6);
		}
		if (StringUtils.isNotEmpty(page)) {
			sql = sql + " limit " + (Integer.valueOf(page) - 1) * Integer.valueOf(rows) + "," + Integer.valueOf(rows);
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
					UserMannerModel cUserModel = new UserMannerModel().populateFromResultSet(rs);
					if (cUserModel != null) {
						list.add(cUserModel);
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public List<UserInfoModel>/* Map<String, Object> */ getUserStatus(List<Integer> info, String page, String rows) {
		List<UserInfoModel> list = new ArrayList<UserInfoModel>();
		String sq = "";
		String sql = "";
		for (int i = 0; i < info.size(); i++) {
			int uid = info.get(i);
			String dbname = getDbName(uid, "user_base_info_");
			String dbname2 = getDbName(uid, "user_asset_");
			sq = sq + "SELECT i.* ,a.wealth,a.money,a.creditTotal,a.credit,u.addtime,(SELECT handle from user_handle WHERE uid="
					+ uid + " and status = 0)handle from " + dbname + " i," + dbname2 + " a,user_id u  where i.uid=a.uid and i.uid="
					+ uid + " and u.id=" + uid + " union ";
		}
		if (sq.length() > 7) {
			sql = sq.substring(0, sq.length() - 6);
		}
		if (StringUtils.isNotEmpty(page)) {
			sql = sql + " limit " + (Integer.valueOf(page) - 1) * Integer.valueOf(rows) + "," + Integer.valueOf(rows);
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
					UserInfoModel cUserModel = new UserInfoModel().populateFromResultSet(rs);
					if (cUserModel != null) {
						list.add(cUserModel);
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public List<UserInfoModel> statusUserBaseInfo(int status, String page, String rows) {
		List<UserInfoModel> list = new ArrayList<UserInfoModel>();
		List<Integer> info = new ArrayList<Integer>();
		String sql = "select uid from user_handle where handle=" + status;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					info.add(Integer.parseInt(rs.getString(1)));
				}
				return getUserStatus(info, page, rows);
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
		return list;
	}

	public int statusUserBaseTotal(int status) {
		
		int cnts = 0;
		String sql = "select count(*) as cnts from user_handle where handle=" + status+" and status=0 ";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					cnts = rs.getInt("cnts");
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cnts;
	}

	// 根据状态查找用户
	public List<Map<String, Object>> statusUserManager(int status, Integer page, Integer rows) {
		
		List<Map<String, Object>> listUsers = new ArrayList<Map<String,Object>>();
		String sql = "select uid,handle,cause,source,adminid,creatAt from user_handle where handle=" + status+" and status=0 order by creatAt desc limit "+(page-1)*rows+","+rows;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			
			con = DbUtil.instance().getCon(constant.db_zhu_user,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("uid", rs.getInt("uid"));
					map.put("handle", rs.getInt("handle"));
					map.put("cause", rs.getString("cause"));
					map.put("source", rs.getInt("source"));
					map.put("creatAt", rs.getInt("creatAt"));
					map.put("adminid", rs.getInt("adminid"));
					
					String hget = RedisOperat.getInstance().hget(RedisContant.host, RedisContant.port6379, RedisContant.keyBaseInfoList, String.valueOf(rs.getInt("uid")));
					UserBaseInfoModel userBaseInfoModel = (UserBaseInfoModel) com.alibaba.fastjson.JSONObject.parseObject(hget, UserBaseInfoModel.class);
					if (userBaseInfoModel != null) {
						map.put("nickname", userBaseInfoModel.getNickname());
						map.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
						map.put("userLevel", userBaseInfoModel.getUserLevel());
					}
					
					listUsers.add(map);
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listUsers;
	}

	public UserAsset getUserAsset(int uid,Boolean readMysql) {
		
		String tableName = getDbName(uid, constant.tb_user_asset);
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		UserAsset userAssetModel = null;
		if (!readMysql) {
			// 读缓存
			String userAsset = RedisOperat.getInstance().hget(RedisContant.host, RedisContant.port6379, RedisContant.keyAssetList, String.valueOf(uid));

			if (userAsset != null && !"".equals(userAsset)) {
				userAssetModel = (UserAsset) com.alibaba.fastjson.JSONObject.parseObject(userAsset, UserAsset.class);
			}
		}
		try {
			if (userAssetModel == null) {
				String SQL_GetUserAssetByUid = "SELECT uid,money,wealth,credit,creditTotal FROM %s WHERE uid="+uid;
				conn = DbUtil.instance().getCon(constant.db_zhu_user, constant.db_slave);
				statement = conn.prepareStatement(String.format(SQL_GetUserAssetByUid, tableName));
				rs = statement.executeQuery();
				
				if (rs != null) {
					while (rs.next()) {
						userAssetModel = new UserAsset().populateFromResultSet(rs);
						break;
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
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return userAssetModel;
	}
	
	public UserAccountModel getUserAccountByUid(int uid, Boolean readMysql) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		UserAccountModel userAccountModel = null;

		if (!readMysql) {
			// 读缓存
			String userAccount = RedisOperat.getInstance().hget(RedisContant.host, RedisContant.port6379, RedisContant.keyAccountList, String.valueOf(uid));
			if (org.apache.commons.lang.StringUtils.isNotEmpty(userAccount)) {
				userAccountModel = (UserAccountModel) com.alibaba.fastjson.JSONObject.parseObject(userAccount, UserAccountModel.class);
			}
		}
		try {
			if (userAccountModel == null) {

				String tableName = getDbName(uid, constant.tb_user_account);
				String SQL_GetUserAccountByUid = "SELECT uid,accountid,accountname,password,authkey,status,unionId FROM %s WHERE uid = "+uid;
				
				conn = DbUtil.instance().getCon(constant.db_zhu_user, constant.db_slave);
				statement = conn.prepareStatement(String.format(SQL_GetUserAccountByUid, tableName));
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						userAccountModel = new UserAccountModel().populateFromResultSet(rs);
						break;
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
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return userAccountModel;
	}
	
	public Map<String, Object> getGrant(int uid,int operateUid,int page,int size,String startDate,String endDate,String gStatus,String gsid){
		Long startTime = null;
		Long endTime = null;
		Integer gsStatus = null;
		Integer gsId = null;
		
		Map<String, Object> mapResult = new HashMap<String, Object>();
		String sql = " select a.uid,a.oldzhutou,a.oldcredit,a.zhutou,a.credit,a.descrip,b.username,a.addtime,"
				+ " c.salesman_id salesmanId,"
				+ " d.name salesmanName,"
				+ " d.contacts_phone salesmanContactsPhone,"
				+ " c.agent_user_id agentUserId,"
				+ " e.name agentUserName,"
				+ " e.contacts agentUserContactsName,"
				+ " e.contacts_phone agentUserContactsPhone,"
				+ " c.promoters_id promotersId,"
				+ " f.name promotersName,"
				+ " f.contacts promotersContactsName,"
				+ " f.contacts_phone promotersContactsPhone,"
				+ " c.extension_center_id extensionCenterId,"
				+ " g.name extensionCenterName,"
				+ " g.contacts extensionCenterContactsName,"
				+ " g.contacts_phone extensionCenterContactsPhone,"
				+ " c.strategic_partner_id strategicPartnerId,"	
				+ " h.name strategicPartnerName,"
				+ " h.contacts strategicPartnerContactsName,"
				+ " h.contacts_phone strategicPartnerContactsPhone "
				+ "from pay_grant a,"
				+ "zhu_admin.admin_user b,"
				+ "zhu_admin.t_user_org_relation c," 
				+ "zhu_admin.t_salesman d," 
				+ "zhu_admin.t_agent_user e," 
				+ "zhu_admin.t_promoters f," 
				+ "zhu_admin.t_extension_center g,"  
				+ "zhu_admin.t_strategic_partner h "
				+ "where a.adminuid=b.uid "
				+ "and a.uid = c.uid and c.salesman_id=d.id and c.agent_user_id = e.id and c.promoters_id = f.id and c.extension_center_id = g.id  and c.strategic_partner_id = h.id"
				;
		
		if (uid > 0) {
			sql = sql + " and a.uid = " + uid;
		}
		if (operateUid > 0) {
			sql = sql + " and a.adminuid = " + operateUid;
		}
		
		if(StringUtils.isNotEmpty(gStatus)) {
			gsStatus = Integer.valueOf(gStatus);
		}
		if(StringUtils.isNotEmpty(gsid)) {
			gsId = Integer.valueOf(gsid);
		}
		if(StringUtils.isNotEmpty(gStatus) && StringUtils.isNotEmpty(gsid)) {
			if(gsStatus == 1) {
				sql = sql + " and c.strategic_partner_id ="+gsId;
			}else if(gsStatus == 2) {
				sql = sql + " and c.extension_center_id ="+gsId;
			}else if(gsStatus == 3) {
				sql = sql + " and c.promoters_id ="+gsId;
			}else if(gsStatus == 4) {
				sql = sql + " and c.agent_user_id ="+gsId;
			}else if(gsStatus == 5) {
				sql = sql + " and c.salesman_id ="+gsId;
			}
		}
		
		if(StringUtils.isNotEmpty(startDate)) {
			startTime = DateUtil.dateToLong(startDate, "yyyy-MM-dd", "other", 0);
			sql = sql + " and a.addtime >= "+startTime;
		}
		if(StringUtils.isNotEmpty(endDate)) {
			endTime = DateUtil.dateToLong(endDate, "yyyy-MM-dd", "day", 1);
			sql = sql + " and a.addtime <= "+endTime;
		}
		String sqlList = sql + " order by a.addtime desc limit " + (page - 1) * size + "," + size;
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
					map.put("uid", rs.getInt("uid"));
					UserBaseInfoModel userBaseInfo = UserDao.getInstance().getUserBaseInfo(rs.getInt("uid"), false);
					if (userBaseInfo != null) {
						map.put("nickname", userBaseInfo.getNickname());
					}else{
						map.put("nickname", "未知");
					}
					map.put("oldzhutou", rs.getInt("oldzhutou"));
					map.put("oldcredit", rs.getInt("oldcredit"));
					map.put("zhutou", rs.getInt("zhutou"));
					map.put("credit", rs.getInt("credit"));
					map.put("descrip", rs.getString("descrip"));
					map.put("username", rs.getString("username"));
					map.put("addtime", rs.getLong("addtime"));
					map.put("salesmanId", rs.getLong("salesmanId"));
					map.put("salesmanName", rs.getString("salesmanName"));
					map.put("salesmanContactsPhone", rs.getString("salesmanContactsPhone"));
					map.put("agentUserId", rs.getLong("agentUserId"));
					map.put("agentUserName", rs.getString("agentUserName"));
					map.put("agentUserContactsName", rs.getString("agentUserContactsName"));
					map.put("agentUserContactsPhone", rs.getString("agentUserContactsPhone"));
					map.put("promotersId", rs.getLong("promotersId"));
					map.put("promotersName", rs.getString("promotersName"));
					map.put("promotersContactsName", rs.getString("promotersContactsName"));
					map.put("promotersContactsPhone", rs.getString("promotersContactsPhone"));
					map.put("extensionCenterId", rs.getLong("extensionCenterId"));
					map.put("extensionCenterName", rs.getString("extensionCenterName"));
					map.put("extensionCenterContactsName", rs.getString("extensionCenterContactsName"));
					map.put("extensionCenterContactsPhone", rs.getString("extensionCenterContactsPhone"));
					map.put("strategicPartnerId", rs.getLong("strategicPartnerId"));
					map.put("strategicPartnerName", rs.getString("strategicPartnerName"));
					map.put("strategicPartnerContactsName", rs.getString("strategicPartnerContactsName"));
					map.put("strategicPartnerContactsPhone", rs.getString("strategicPartnerContactsPhone"));
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
	
	public List<Map<String, Object>> getAllGrant(int uid,int operateUid,String startDate,String endDate,String gStatus,String gsid){
		Long startTime = null;
		Long endTime = null;
		Integer gsStatus = null;
		Integer gsId = null;
		
		String sql = " select a.uid,a.oldzhutou,a.oldcredit,a.zhutou,a.credit,a.descrip,b.username,a.addtime,"
				+ "CASE WHEN a.addtime = 0 THEN '' ELSE FROM_UNIXTIME(a.addtime,'%Y-%m-%d %H:%i:%s') END addtimeStr,"
				+ " c.salesman_id salesmanId,"
				+ " d.name salesmanName,"
				+ " d.contacts_phone salesmanContactsPhone,"
				+ " c.agent_user_id agentUserId,"
				+ " e.name agentUserName,"
				+ " e.contacts agentUserContactsName,"
				+ " e.contacts_phone agentUserContactsPhone,"
				+ " c.promoters_id promotersId,"
				+ " f.name promotersName,"
				+ " f.contacts promotersContactsName,"
				+ " f.contacts_phone promotersContactsPhone,"
				+ " c.extension_center_id extensionCenterId,"
				+ " g.name extensionCenterName,"
				+ " g.contacts extensionCenterContactsName,"
				+ " g.contacts_phone extensionCenterContactsPhone,"
				+ " c.strategic_partner_id strategicPartnerId,"	
				+ " h.name strategicPartnerName,"
				+ " h.contacts strategicPartnerContactsName,"
				+ " h.contacts_phone strategicPartnerContactsPhone "
				+ "from pay_grant a,"
				+ "zhu_admin.admin_user b,"
				+ "zhu_admin.t_user_org_relation c," 
				+ "zhu_admin.t_salesman d," 
				+ "zhu_admin.t_agent_user e," 
				+ "zhu_admin.t_promoters f," 
				+ "zhu_admin.t_extension_center g,"  
				+ "zhu_admin.t_strategic_partner h "
				+ "where a.adminuid=b.uid "
				+ "and a.uid = c.uid and c.salesman_id=d.id and c.agent_user_id = e.id and c.promoters_id = f.id and c.extension_center_id = g.id  and c.strategic_partner_id = h.id";
		
		if (uid > 0) {
			sql = sql + " and a.uid = " + uid;
		}
		if (operateUid > 0) {
			sql = sql + " and a.adminuid = " + operateUid;
		}
		
		if(StringUtils.isNotEmpty(gStatus)) {
			gsStatus = Integer.valueOf(gStatus);
		}
		if(StringUtils.isNotEmpty(gsid)) {
			gsId = Integer.valueOf(gsid);
		}
		if(StringUtils.isNotEmpty(gStatus) && StringUtils.isNotEmpty(gsid)) {
			if(gsStatus == 1) {
				sql = sql + " and c.strategic_partner_id ="+gsId;
			}else if(gsStatus == 2) {
				sql = sql + " and c.extension_center_id ="+gsId;
			}else if(gsStatus == 3) {
				sql = sql + " and c.promoters_id ="+gsId;
			}else if(gsStatus == 4) {
				sql = sql + " and c.agent_user_id ="+gsId;
			}else if(gsStatus == 5) {
				sql = sql + " and c.salesman_id ="+gsId;
			}
		}
		
		if(StringUtils.isNotEmpty(startDate)) {
			startTime = DateUtil.dateToLong(startDate, "yyyy-MM-dd", "other", 0);
			sql = sql + " and a.addtime >= "+startTime;
		}
		if(StringUtils.isNotEmpty(endDate)) {
			endTime = DateUtil.dateToLong(endDate, "yyyy-MM-dd", "day", 1);
			sql = sql + " and a.addtime <= "+endTime;
		}
		String sqlList = sql + " order by a.addtime desc";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_pay, constant.db_slave);
			pstmt = con.prepareStatement(sqlList);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while(rs.next()){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("uid", rs.getInt("uid"));
					UserBaseInfoModel userBaseInfo = UserDao.getInstance().getUserBaseInfo(rs.getInt("uid"), false);
					if (userBaseInfo != null) {
						map.put("nickname", userBaseInfo.getNickname());
					}else{
						map.put("nickname", "未知");
					}
					map.put("oldzhutou", rs.getInt("oldzhutou"));
					map.put("oldcredit", rs.getInt("oldcredit"));
					map.put("zhutou", rs.getInt("zhutou"));
					map.put("credit", rs.getInt("credit"));
					map.put("descrip", rs.getString("descrip"));
					map.put("username", rs.getString("username"));
					map.put("addtime", rs.getLong("addtime"));
					map.put("addtimeStr", rs.getString("addtimeStr"));
					map.put("salesmanId", rs.getLong("salesmanId"));
					map.put("salesmanName", rs.getString("salesmanName"));
					map.put("salesmanContactsPhone", rs.getString("salesmanContactsPhone"));
					map.put("agentUserId", rs.getLong("agentUserId"));
					map.put("agentUserName", rs.getString("agentUserName"));
					map.put("agentUserContactsName", rs.getString("agentUserContactsName"));
					map.put("agentUserContactsPhone", rs.getString("agentUserContactsPhone"));
					map.put("promotersId", rs.getLong("promotersId"));
					map.put("promotersName", rs.getString("promotersName"));
					map.put("promotersContactsName", rs.getString("promotersContactsName"));
					map.put("promotersContactsPhone", rs.getString("promotersContactsPhone"));
					map.put("extensionCenterId", rs.getLong("extensionCenterId"));
					map.put("extensionCenterName", rs.getString("extensionCenterName"));
					map.put("extensionCenterContactsName", rs.getString("extensionCenterContactsName"));
					map.put("extensionCenterContactsPhone", rs.getString("extensionCenterContactsPhone"));
					map.put("strategicPartnerId", rs.getLong("strategicPartnerId"));
					map.put("strategicPartnerName", rs.getString("strategicPartnerName"));
					map.put("strategicPartnerContactsName", rs.getString("strategicPartnerContactsName"));
					map.put("strategicPartnerContactsPhone", rs.getString("strategicPartnerContactsPhone"));
					list.add(map);
				}
				rs.close();
			}
			if (pstmt != null) {
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
		return list;
	}
	
	public int addGrant(int uid,int zhutou,int credit,String descrip,int adminid){
		
		int iResult = 0;
		
		String tableName = "zhu_user."+getDbName(uid, constant.tb_user_asset);
		
		String addSql = "  insert into pay_grant(uid,oldzhutou,oldcredit,zhutou,credit,descrip,adminuid,addtime)select uid,money,credit,?,?,?,?,? from " + tableName + " where uid = " + uid;
		String updSql = " update " + tableName + " set money = money + " +zhutou +",credit = credit + " + credit + ",creditTotal = creditTotal + " + credit + " where uid = " + uid;
		if (zhutou < 0) {
			updSql = updSql + " and (money + " + zhutou +") >= 0 ";
		}
		if (credit < 0) {
			updSql = updSql + " and (credit + " + credit +") >= 0 ";
		}
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_pay, constant.db_master);
			con.setAutoCommit(false);
			
			pstmt = con.prepareStatement(addSql);
			pstmt.setInt(1, zhutou);
			pstmt.setInt(2, credit);
			pstmt.setString(3, descrip);
			pstmt.setInt(4, adminid);
			pstmt.setLong(5, System.currentTimeMillis()/1000);
			
			pstmt.addBatch();
			pstmt.addBatch(updSql);
			
			int[] executeBatch = pstmt.executeBatch();
			
			con.commit();
			for (int i = 0; i < executeBatch.length; i++) {
				iResult += executeBatch[i];
			}
			if (iResult == 2) {
				
				Unirest.get(Constant.business_server_url+"/admin/refreshUserAsset?uid="+uid).asString();
				
				String current_version = "{uid:" + uid + ",zhutou:" + zhutou + ",credit:" + credit + ",descrip:" + descrip + "}";
				AddOperationLog("pay_grant", "0", "补金币|声援", 1, "", current_version, adminid);
				
				Unirest.post(Constant.business_server_url+"/admin/addCredit?addCredit=crdeitdda&uid="+uid+"&credit="+credit).asString();
			}
			
		} catch (Exception e) {
			try {
				if (con != null) {
					con.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
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
	
	public Map<String, Object> getUsedExpList(int uid,Long stime,Long etime,int page,int size){

		Map<String, Object> mapResult = new HashMap<String, Object>();
		
		String sql = " select a.id,a.uid,a.pre_exp,a.exp,b.username,a.addtime,a.remarks "
				+ " from admin_user_exp a,admin_user b "
				+ " where a.adminid=b.uid and a.addtime >= " + stime + " and a.addtime < " + etime;
		if (uid > 0) {
			sql = sql + " and a.uid = " + uid;
		}
		
		String sqlList = sql + " order by addtime desc limit " + (page-1)*size + "," + size;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, constant.db_slave);
			pstmt = con.prepareStatement(sqlList);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				while(rs.next()){
					Map<String, Object> map = new HashMap<String, Object>();
					UserBaseInfoModel userBaseInfo = UserDao.getInstance().getUserBaseInfo(rs.getInt("uid"), false);
					if (userBaseInfo != null) {
						map.put("nickname", userBaseInfo.getNickname());
					}else {
						map.put("nickname", "未知");
					}
					map.put("id", rs.getInt("id"));
					map.put("uid", rs.getInt("uid"));
					map.put("pre_exp", rs.getLong("pre_exp"));
					map.put("exp", rs.getLong("exp"));
					map.put("username", rs.getString("username"));
					
					map.put("addtime", rs.getLong("addtime"));
					map.put("remarks", rs.getString("remarks"));
					
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
			System.out.println("Dao getUsedExpList-Exception:" + e.getMessage());
			System.out.println("sql:" + sqlList);
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
				System.out.println("Dao getUsedExpList-finally-Exception:" + e2.getMessage());
			}
		}
		return mapResult;
	}
	
	/**
	 * 添加成功 返回自增ID，失败则返回 0 
	 * @param uid
	 * @param exp
	 * @param remarks
	 * @param adminid
	 * @return
	 */
	public int addUserExp(int uid,Long exp,String remarks,int adminid){
		
		int iResult = 0;
		UserBaseInfoModel userBaseInfo = getUserBaseInfo(uid, false);
		
		String sql = "insert into admin_user_exp(uid,pre_exp,exp,remarks,adminid,addtime)value(?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, constant.db_master);
			pstmt = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, uid);
			pstmt.setLong(2, userBaseInfo.getExp());
			pstmt.setLong(3, exp);
			pstmt.setString(4, remarks);
			pstmt.setInt(5, adminid);
			pstmt.setLong(6, System.currentTimeMillis()/1000);
			
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				iResult = rs.getInt(1);
			}
			if (iResult > 0) {
				// 添加成功
				String current_version = "{uid:"+uid+",exp:"+exp+",remarks:"+remarks+"}";
				String pre_version = "{pre_exp:" + userBaseInfo.getExp() + "}";
				OperateDao.getInstance().AddOperationLog("admin_user_exp", "0", "添加经验值", 1, pre_version, current_version, adminid);
			}
			
		} catch (Exception e) {
			System.out.println("addUserExp-Exception:" + e.getMessage());
			System.out.println("sql : " + sql);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				System.out.println("addUserExp-finally-Exception:" + e2.getMessage());
			}
		}
		return iResult;
	}

	/**
	 * 添加经验值后 修改状态
	 * @param id
	 * @param status
	 * @return
	 */
	public int updUserExpStatus(int id,int status){
		
		int iResult = 0;
		
		String sql = "update admin_user_exp set isvalid = " +status + " where id= " + id;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, constant.db_master);
			pstmt = con.prepareStatement(sql);
			
			iResult = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("updUserExpStatus-Exception:" + e.getMessage());
			System.out.println("sql : " + sql);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				System.out.println("updUserExpStatus-finally-Exception:" + e2.getMessage());
			}
		}
		return iResult;
	}
	
	/**
	 * 获取艺管 要审核的兑现记录
	 * @param uid
	 * @param stime
	 * @param etime
	 * @param page
	 * @param size
	 * @return
	 */
	public Map<String, Object> getAnchorCashOfArttube(int unionid,Integer uid,Long stime,Long etime,int page,int size){
		Map<String, Object> mapResult = new HashMap<String, Object>();
		
		String sql = "select a.id,a.uid,a.unionid,a.credits,a.rate,a.operatebak,a.status,a.addtime,b.unionname "
				+ " from pay_anchor_credit a,zhu_union.union_info b "
				+ " where a.unionid=b.unionid and a.status in(0,4) ";
		if (uid > 0) {
			sql = sql + " and a.uid = " + uid;
		}
		
		if ( stime > 0) {
			sql = sql + " and a.addtime >= " + stime;
		}
		if (etime > 0 ) {
			sql = sql + " and a.addtime < " + etime;
		}
		if (unionid > 0) {
			sql = sql + " and a.unionid = "+ unionid;
		}
		
		String sqlList = sql +" order by a.addtime limit " + (page - 1)* size + "," + size;
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
					
					map.put("id", rs.getInt("id"));
					map.put("times", rs.getLong("addtime"));
					map.put("unionid", rs.getInt("unionid"));
					map.put("uname", rs.getString("unionname"));
					map.put("uid", rs.getInt("uid"));
					UserBaseInfoModel userBaseInfo = UserDao.instance.getUserBaseInfo(rs.getInt("uid"), false);
					if (userBaseInfo != null) {
						map.put("nickname", userBaseInfo.getNickname());
					}else {
						map.put("nickname", "未知");
					}
					map.put("credits", rs.getInt("credits"));
					map.put("rate", rs.getObject("rate"));
					map.put("amount", rs.getInt("credits") * rs.getFloat("rate"));
					map.put("operatebak", rs.getString("operatebak"));
					map.put("addtime", rs.getLong("addtime"));
					map.put("status", rs.getInt("status"));
					
					list.add(map);
					
				}
				mapResult.put("rows", list);
				

				rs.close();
				pstmt.close();
			}else {
				mapResult.put("rows", null);
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
			System.out.println("sql:" + sqlList);
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
	 * 艺管审核兑换单
	 * @param type =1通过 =0拒绝
	 * @param id
	 * @param uid
	 * @param adminid
	 * @return
	 */
	public int verifyByArttube(int type,int id,int uid,int adminid){
		
		int iResult = 0;
		String sql = "update pay_anchor_credit set status = ? where id= " + id + " and uid= " + uid + " and status in (0,4) ";

		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_pay, constant.db_master);
			pstmt = con.prepareStatement(sql);
			if (type == 1) {
				pstmt.setInt(1, 1);
			}else {
				pstmt.setInt(1, 2);
			}
			
			iResult = pstmt.executeUpdate();
			if (iResult > 0) {
				String current_version = "艺管审核兑换单 ID:"+id+",主播UID:"+uid+" 状态：" + (type==1?"通过":"拒绝");
				AddOperationLog("pay_anchor_credit", String.valueOf(id), "审核兑换单", 2, "", current_version, adminid);
			}
		} catch (Exception e) {
			System.out.println("sql:" + sql);
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
	 * 获取运营 要审核的兑现记录
	 * @param uid
	 * @param stime
	 * @param etime
	 * @param page
	 * @param size
	 * @return
	 */
	public Map<String, Object> getAnchorCashOfOperate(int uid,int unionid,int times,Long stime,Long etime,int page,int size){
		Map<String, Object> mapResult = new HashMap<String, Object>();
		
		String sql = "select a.uid,a.unionid,sum(a.credits) as credits,a.rate,a.status,a.addtime,b.unionname,b.adminname "
				+ " from pay_anchor_credit a,zhu_union.union_info b "
				+ " where a.unionid=b.unionid and a.status =1 ";
		
		if ( stime > 0) {
			sql = sql + " and a.addtime >= " + stime;
		}
		if (etime > 0 ) {
			sql = sql + " and a.addtime < " + etime;
		}
		if (uid > 0) {
			sql = sql + " and a.uid="+ uid;
		}
		if (unionid > 0) {
			sql = sql + " and a.unionid=" + unionid;
		}
		sql = sql + " group by a.uid ";
		
		String sqlList = sql +" limit " + (page - 1)* size + "," + size;
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
					
					map.put("times", times);
					map.put("adminname", rs.getString("adminname"));
					map.put("unionid", rs.getInt("unionid"));
					map.put("uname", rs.getString("unionname"));
					map.put("uid", rs.getInt("uid"));
					UserBaseInfoModel userBaseInfo = UserDao.instance.getUserBaseInfo(rs.getInt("uid"), false);
					if (userBaseInfo != null) {
						map.put("nickname", userBaseInfo.getNickname());
					}else {
						map.put("nickname", "未知");
					}
					map.put("credits", rs.getInt("credits"));
					map.put("rate", rs.getObject("rate"));
					map.put("amount", rs.getInt("credits") * rs.getFloat("rate"));

					map.put("money", (int)(rs.getInt("credits") * rs.getFloat("rate")/100));
					map.put("status", rs.getInt("status"));
					
					list.add(map);
					
				}
				mapResult.put("rows", list);
				

				rs.close();
				pstmt.close();
			}else {
				mapResult.put("rows", null);
			}
			
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			List<Map<String, Object>> footer = new ArrayList<Map<String,Object>>();
			int amount = 0;
			
			int total = 0;
			if (rs != null) {
				Map<String, Object> mapFooter = new HashMap<String, Object>();
				
				while(rs.next()){
					amount = amount + (int)(rs.getInt("credits") * rs.getFloat("rate")/100);
					total++;
				}
				mapFooter.put("uname", "金额合计");
				mapFooter.put("adminname", amount);
				
				footer.add(mapFooter);
			}
			mapResult.put("total", total);
			mapResult.put("footer", footer);
			
		} catch (Exception e) {
			System.out.println("sql:" + sqlList);
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
	 * 运营审核兑换单
	 * @param type =1通过 =0拒绝
	 * @param id
	 * @param uid
	 * @param adminid
	 * @return
	 */
	public int verifyByOperate(int type,Long stime,Long etime,int uid,int adminid){
		
		int iResult = 0;
		String sql = "update pay_anchor_credit set status = ? where addtime >= " + stime + " and addtime < " + etime + " and uid= " + uid + " and status = 1 ";

		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_pay, constant.db_master);
			pstmt = con.prepareStatement(sql);
			if (type == 1) {
				pstmt.setInt(1, 3);
			}else {
				pstmt.setInt(1, 4);
			}
			
			iResult = pstmt.executeUpdate();
			if (iResult > 0) {
				String current_version = "运营审核兑换单 开始时间:"+DateUtil.convert2String(stime*1000, "yyyy-MM-dd")+", 结束时间:"+DateUtil.convert2String(etime*1000, "yyyy-MM-dd")+",主播UID:"+uid+" 状态：" + (type==1?"通过":"拒绝");
				AddOperationLog("pay_anchor_credit", "", "审核兑换单", 2, "", current_version, adminid);
			}
		} catch (Exception e) {
			System.out.println("sql:" + sql);
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
	 * 获取兑现记录
	 * @param times
	 * @param stime
	 * @param etime
	 * @param uid
	 * @param status =9全部  其他则具体状态
	 * @param page
	 * @param size
	 * @return
	 */
	public Map<String, Object> getCashCreditList(Long stime,Long etime,int uid,int status,int page,int size){
		Map<String, Object> mapResult = new HashMap<String, Object>();
		
		String sql = "select a.uid,a.unionid,a.credits,a.rate,a.status,a.addtime,a.operatebak,b.unionname,b.adminname "
				+ " from pay_anchor_credit a,zhu_union.union_info b "
				+ " where a.unionid=b.unionid ";
		
		if ( stime > 0) {
			sql = sql + " and a.addtime >= " + stime;
		}
		if (etime > 0 ) {
			sql = sql + " and a.addtime < " + etime;
		}
		if (status != 9) {
			sql = sql + " and a.status = " + status;
		}
		if (uid > 0) {
			sql = sql + " and a.uid = " + uid;
		}
		
		String sqlList = sql +" order by a.addtime desc limit " + (page - 1)* size + "," + size;
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
					
					map.put("times", rs.getLong("addtime"));
					map.put("adminname", rs.getString("adminname"));
					map.put("unionid", rs.getInt("unionid"));
					map.put("uname", rs.getString("unionname"));
					map.put("uid", rs.getInt("uid"));
					UserBaseInfoModel userBaseInfo = UserDao.instance.getUserBaseInfo(rs.getInt("uid"), false);
					if (userBaseInfo != null) {
						map.put("nickname", userBaseInfo.getNickname());
					}else {
						map.put("nickname", "未知");
					}
					map.put("credits", rs.getInt("credits"));
					map.put("rate", rs.getObject("rate"));
					map.put("amount", rs.getInt("credits") * rs.getFloat("rate"));
					map.put("status", rs.getInt("status"));
					map.put("addtime", rs.getLong("addtime"));
					map.put("operatebak", rs.getString("operatebak"));
					
					list.add(map);
					
				}
				mapResult.put("rows", list);
				

				rs.close();
				pstmt.close();
			}else {
				mapResult.put("rows", null);
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
			System.out.println("sql:" + sqlList);
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
	
	public int updUserCover(int id,int uId,String picCover,String picCover1,String picCover2){
		int iResult = 0;
		String tablename = getDbName(uId, "user_base_info_");
		String sql = "update "+tablename+" set livimage=?,pcimg1=?,pcimg2=? where uid=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user, constant.db_master);
			pstmt = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, picCover);
			pstmt.setString(2, picCover1);
			pstmt.setString(3, picCover2);
			pstmt.setInt(4, uId);
			iResult = pstmt.executeUpdate();
			if (iResult > 0 ) {
				//刷新用户基本信息缓存
				Unirest.get(Constant.business_server_url + "/admin/refreshUserBaseInfo?uid=" + uId).asString();
			}
		} catch (Exception e) {
			System.out.println("addUserExp-Exception:" + e.getMessage());
			System.out.println("sql : " + sql);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				System.out.println("addUserExp-finally-Exception:" + e2.getMessage());
			}
		}
		return iResult;
	}
	public int updUserCoverStatus(int uId){
		int iResult = 0;
		String sql = "update user_cover_check set status=1 where id =?";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_union, constant.db_master);
			pstmt = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, uId);
			iResult = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("addUserExp-Exception:" + e.getMessage());
			System.out.println("sql : " + sql);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				System.out.println("updUserCoverExp-finally-Exception:" + e2.getMessage());
			}
		}
		return iResult;
	}
	
	public int updUserCover(int id,String cause){
		int iResult = 0;
		String sql = "update user_cover_check set status=2,cause=? where id =?";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_union, constant.db_master);
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, cause);
			pstmt.setInt(2,id);
			iResult = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("updUserCoverExp-Exception:" + e.getMessage());
			System.out.println("sql : " + sql);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				System.out.println("updUserCoverExp-finally-Exception:" + e2.getMessage());
			}
		}
		return iResult;
	}
}