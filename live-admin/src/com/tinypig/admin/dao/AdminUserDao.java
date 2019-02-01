package com.tinypig.admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.tinypig.admin.constant;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.util.DbUtil;
import com.tinypig.admin.util.EncryptUtils;

public class AdminUserDao {

	private final static AdminUserDao instance = new AdminUserDao();

	public static AdminUserDao getInstance() {
		return instance;
	}

	/**
	 * 管理员登录
	 * 
	 * @param uname
	 * @param pword
	 * @return
	 */
	public AdminUserModel login(String uname, String pword) {
		AdminUserModel adminUser = null;
		if (StringUtils.isEmpty(uname) || StringUtils.isEmpty(pword)) {
			return adminUser;
		} else {
			String sql = "select uid,type,username,password,role_id,reg_time,login_time,createUid,isvalid from admin_user where username=? and isvalid = 1";
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				con = DbUtil.instance().getCon(constant.db_zhu_admin, "master");
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, uname);
				rs = pstmt.executeQuery();
				if (rs != null) {
					while (rs.next()) {
						if (pword.toUpperCase().equals(rs.getString("password"))) {
							adminUser = new AdminUserModel().populateFromResultSet(rs);
						}
					}
				} else {
					System.out.println("login->resultset is null");
				}
				if (adminUser != null) {
					sql = "update admin_user set login_time=? where uid=?";
					pstmt = null;
					pstmt = con.prepareStatement(sql);
					pstmt.setInt(1, (int) (System.currentTimeMillis() / 1000));
					pstmt.setInt(2, adminUser.getUid());
					pstmt.executeUpdate();
				}
			} catch (Exception e) {
				System.out.println("login->exception:" + e.getMessage());
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
					System.out.println("login->finally->exception:" + e.getMessage());
				}
			}
		}
		return adminUser;
	}

	/**
	 * 修改密码
	 * 
	 * @param uid
	 * @param oldPwd
	 * @param newPwd
	 * @return true 表示修改成功 false 表示修改失败
	 */
	public Boolean updPwd(int uid, String oldPwd, String newPwd) {
		boolean bl = false;
		
		String sql = "select password from admin_user where uid=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "master");
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, uid);
			rs = pstmt.executeQuery();
			if (rs != null) {
				String oldPwdMd5 = EncryptUtils.md5Encrypt(oldPwd);
				String newPwdMd5 = EncryptUtils.md5Encrypt(newPwd);
				while (rs.next()) {
					if (oldPwdMd5.equals(rs.getString("password"))) {
						if (oldPwd.equals(newPwd)) {
							bl = true;
						} else {
							sql = "update admin_user set password=? where uid=?";
							pstmt = con.prepareStatement(sql);
							pstmt.setString(1, newPwdMd5);
							pstmt.setInt(2, uid);
							if (pstmt.executeUpdate() == 1) {
								bl = true;
							}
						}
					} else {
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
		return bl;
	}

	/**
	 * 强制修改密码
	 * @param uid
	 * @param newPwd
	 * @return
	 */
	public Boolean updPwd(int uid, String newPwd) {
		boolean bl = false;
		String newPwdMd5 = EncryptUtils.md5Encrypt(newPwd);
		String sql = "update admin_user set password=? where uid=?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "master");
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, newPwdMd5);
			pstmt.setInt(2, uid);
			if (pstmt.executeUpdate() == 1) {
				bl = true;
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
		return bl;
	}

	/**
	 * 改变用户名 密码
	 * @param uid
	 * @param newPwd
	 * @return
	 */
	public Boolean updUsernamePwd(int uid, String username, String newPwd) {
		boolean bl = false;
		String newPwdMd5 = EncryptUtils.md5Encrypt(newPwd);
		String sql = "update admin_user set password=?,username=? where uid=?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "master");
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, newPwdMd5);
			pstmt.setString(2, username);
			pstmt.setInt(3, uid);
			if (pstmt.executeUpdate() == 1) {
				bl = true;
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
		return bl;
	}

	public Boolean updUsername(int uid, String username) {
		boolean bl = false;
		String sql = "update admin_user set username=? where uid=?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "master");
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, username);
			pstmt.setInt(2, uid);
			if (pstmt.executeUpdate() == 1) {
				bl = true;
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
		return bl;
	}

	/**
	 * 添加管理员
	 * 
	 * @param username
	 * @param password
	 * @param role_id
	 * @param createUid
	 * @return 大于 0 则是添加成功后uid ＝0 添加失败
	 */
	public int addAdminUser(String username, String password, int role_id, int createUid, int type) {
		int ires = 0;
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || role_id == 0) {
		} else {
			String sql = "insert into admin_user(username,password,role_id,reg_time,login_time,createUid,type)value(?,?,?,?,?,?,?)";
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				String pwdMd5 = EncryptUtils.md5Encrypt(password);

				con = DbUtil.instance().getCon(constant.db_zhu_admin, "master");
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, username);
				pstmt.setString(2, pwdMd5);
				pstmt.setInt(3, role_id);
				pstmt.setInt(4, (int) (System.currentTimeMillis() / 1000));
				pstmt.setInt(5, (int) (System.currentTimeMillis() / 1000));
				pstmt.setInt(6, createUid);
				pstmt.setInt(7, type);
				ires = pstmt.executeUpdate();
				if (ires > 0) {
					rs = pstmt.executeQuery("SELECT LAST_INSERT_ID() as id");
					if (rs != null) {
						if (rs.next()) {
							ires = rs.getInt("id");
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
		return ires;
	}

	/**
	 * 获取所有后台管理人员列表
	 * @param username 
	 * @return
	 */
	public List<Map<String, Object>> getAdminList(String username,Integer pageSize,Integer startIndex) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String sql = "select au.uid, au.username,au.type, au.role_id, ar.role_name, au.reg_time, au.login_time, au.isvalid, au.createUid from admin_user au, admin_role ar where au.role_id = ar.role_id";
		if(StringUtils.isNotBlank(username)) {
			sql  += " and au.username=?";
		}
		sql += " limit "+startIndex+","+pageSize;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "master");
			pstmt = con.prepareStatement(sql);
			if(StringUtils.isNotBlank(username)) {
				pstmt.setString(1, username);
			}
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("uid", rs.getInt("uid"));
					map.put("username", rs.getString("username"));
					map.put("type", rs.getInt("type"));
					map.put("role_id", rs.getInt("role_id"));
					map.put("role_name", rs.getString("role_name"));
					map.put("reg_time", rs.getInt("reg_time"));
					map.put("login_time", rs.getInt("login_time"));
					map.put("isvalid", rs.getInt("isvalid"));
					map.put("createUid", rs.getInt("createUid"));

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
	
	/**
	 * 获取分页列表下面的条数
	 * @param username
	 * @return
	 */
	public Integer getAdminListTotal(String username) {
		int count = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select count(uid) from admin_user where 1=1";
		if(StringUtils.isNotBlank(username)) {
			sql  += " and username=?";
		}
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "master");
			if (con != null) {
				pstmt = con.prepareStatement(sql);
				if(StringUtils.isNotBlank(username)) {
					pstmt.setString(1, username);
				}
				rs = pstmt.executeQuery();
				if (rs.next()) {
					count = rs.getInt(1);
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
		return count;
	}

	/**
	 * 获取所有后台管理人员姓名列表
	 * @return
	 */
	public List<Map<String, Object>> getAdminNameList() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String sql = "select au.uid, au.username from admin_user au";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "master");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("uid", rs.getInt("uid"));
					map.put("username", rs.getString("username"));

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

	/**
	 * 获取所有后台管理人员列表
	 * @return
	 */
	public AdminUserModel getAdminInfo(int uid) {
		AdminUserModel aUserModel = null;

		String sql = "select uid,username,password,role_id,reg_time,login_time,isvalid,createUid from admin_user where uid=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "master");
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, uid);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					aUserModel = new AdminUserModel().populateFromResultSet(rs);
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

		return aUserModel;
	}

	public AdminUserModel getAdminInfoByUsername(String username) {
		AdminUserModel aUserModel = null;

		String sql = "select * from admin_user where username=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "master");
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					aUserModel = new AdminUserModel().populateFromResultSet(rs);
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

		return aUserModel;
	}

	/**
	 * 设置用户是否有效
	 * 
	 * @param uid
	 * @param isvalid
	 *            ＝false无效 ＝true有效
	 * @return
	 */
	public int setIsValid(int uid, boolean isvalid) {
		int ires = 0;
		String sql = "update admin_user set isvalid=? where uid=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "master");
			pstmt = con.prepareStatement(sql);
			pstmt.setBoolean(1, isvalid);
			pstmt.setInt(2, uid);
			ires = pstmt.executeUpdate();
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
		return ires;
	}

	/**
	 * 修改角色
	 * @param uid
	 * @param roleid
	 * @return
	 */
	public int setRole(int uid, int roleid) {
		int ires = 0;
		String sql = "update admin_user set role_id=? where uid=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "master");
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, roleid);
			pstmt.setInt(2, uid);
			ires = pstmt.executeUpdate();
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
		return ires;
	}

	/**
	 * 修改密码
	 * 
	 */
	public int setPassword(int uid, String password) {
		int ires = 0;
		String sql = "update admin_user set password=? where uid=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		String pwdMd5 = EncryptUtils.md5Encrypt(password);
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "master");
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, pwdMd5);
			pstmt.setInt(2, uid);
			ires = pstmt.executeUpdate();
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
		return ires;
	}

	/**
	 * 修改用户名
	 */
	public int setUsername(int uid, String username) {
		int ires = 0;
		String sql = "update admin_user set username=? where uid=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "master");
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, username);
			pstmt.setInt(2, uid);
			ires = pstmt.executeUpdate();
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
		return ires;
	}

	/**
	 * 获取管理员填充下拉框
	 * @param isvalid =9全部
	 * @return
	 */
	public List<Map<String, Object>> getAdminForSelect(int isvalid) {

		List<Map<String, Object>> listResult = new ArrayList<Map<String, Object>>();

		Map<String, Object> _map = new HashMap<String, Object>();
		_map.put("uid", 0);
		_map.put("username", "全部");
		listResult.add(_map);

		String sql = " select uid,username from admin_user ";
		if (isvalid != 9) {
			sql = sql + " and isvalid = " + isvalid;
		}

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, constant.db_master);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs != null) {
				while (rs.next()) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("uid", rs.getInt("uid"));
					map.put("username", rs.getString("username"));
					listResult.add(map);
				}
			}
		} catch (Exception e) {
			System.out.println("getAdminForSelect-Exception:" + e.getMessage());
			System.out.println("sql:" + sql);
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
				System.out.println("getAdminForSelect-finally-Exception:" + e2.getMessage());
			}
		}

		return listResult;
	}

	/**
	 * 获取管理员操作日志
	 * @param acton
	 * @param uid
	 * @param stime
	 * @param etime
	 * @return
	 */
	public Map<String, Object> getOperatLogs(int action, int uid, Long stime, Long etime, int page, int size) {

		Map<String, Object> mapResult = new HashMap<String, Object>();
		String sql = "select a.log_id,a.dbname,a.db_id,a.operation_note,a.action,a.previous_version,a.current_version,a.operation_time,a.admin_id,b.username "
				+ " from admin_operation_log a,admin_user b " + " where a.admin_id=b.uid and a.operation_time >= "
				+ stime + " and a.operation_time <" + etime;
		if (action > 0) {
			sql = sql + " and a.action = " + action;
		}
		if (uid > 0) {
			sql = sql + " and a.admin_id = " + uid;
		}

		String sqlList = sql + " order by a.operation_time desc limit " + (page - 1) * size + "," + size;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, constant.db_master);
			pstmt = con.prepareStatement(sqlList);
			rs = pstmt.executeQuery();

			if (rs != null) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				while (rs.next()) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("dbname", rs.getString("dbname"));
					map.put("db_id", rs.getObject("db_id"));
					map.put("operation_note", rs.getObject("operation_note"));
					map.put("action", rs.getInt("action"));
					map.put("previous_version", rs.getString("previous_version"));
					map.put("current_version", rs.getString("current_version"));
					map.put("operation_time", rs.getLong("operation_time"));
					map.put("username", rs.getString("username"));

					list.add(map);
				}
				mapResult.put("rows", list);
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
		} catch (Exception e) {
			System.out.println("getOperatLogs-Exception:" + e.getMessage());
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
				System.out.println("getOperatLogs-finally-Exception:" + e2.getMessage());
			}
		}
		return mapResult;
	}

	public int countByUserName(String username) {
		int count = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select count(*) from admin_user where username=?";
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "master");
			if (con != null) {
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, username);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					count = rs.getInt(1);
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
		return count;
	}

	public int deleteByUserName(String contactsPhone) {
		int bl = 0;
		String sql = "delete from admin_user where username=?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "master");
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, contactsPhone);
			bl = pstmt.executeUpdate();
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
		return bl;
	}

	public int deleteByUid(int uid) {
		int bl = 0;
		String sql = "delete from admin_user where uid=?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "master");
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, uid);
			bl = pstmt.executeUpdate();
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
		return bl;
	}

	public int updateUserByUid(int uid, int roleId, int isvalid, int type, String password) {
		int bl = 0;
		StringBuilder sql = new StringBuilder();
		sql.append("update admin_user set role_id=?,type=?,isvalid=?");
		if(StringUtils.isNotBlank(password)) {
			password = EncryptUtils.md5Encrypt(password);
			sql.append(",password=?");
		}
		sql.append(" where uid=?");
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "master");
			pstmt = con.prepareStatement(sql.toString());
			int i = 1;
			pstmt.setInt(i++, roleId);
			pstmt.setInt(i++, type);
			pstmt.setInt(i++, isvalid);
			if(StringUtils.isNotBlank(password)) {
				pstmt.setString(i++, password);
			}
			pstmt.setInt(i++, uid);
			if (pstmt.executeUpdate() == 1) {
				bl = 1;
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
		return bl;
	}

}
