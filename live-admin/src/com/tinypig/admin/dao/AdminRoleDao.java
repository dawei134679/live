package com.tinypig.admin.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.constant;
import com.tinypig.admin.model.AdminRoleModel;
import com.tinypig.admin.util.DbUtil;

public class AdminRoleDao extends BaseDao {
	private final static AdminRoleDao instance = new AdminRoleDao();

	public static AdminRoleDao getInstance() {
		return instance;
	}

	/**
	 * @Description: 根据用户角色名称获取一个角色
	 * @return AdminRoleModel
	 */
	public AdminRoleModel getRoleByRoleName(String roleName) {
		AdminRoleModel aRoleModel = null;

		String sql = "select ar.* from admin_role ar where ar.role_name = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, roleName);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					aRoleModel = new AdminRoleModel().populateFromResultSet(rs);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
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
				System.out.println("login->finally->exception:" + e.getMessage());
			}
		}
		return aRoleModel;

	}

	/**
	 * 
	 * @Description: 根据用户uid获取角色
	 * @return   
	 * @return AdminRoleModel  
	 * @throws
	 * @author guojp
	 * @date 2016-6-28
	 */
	public AdminRoleModel getRoleByUid(int uid) {
		AdminRoleModel aRoleModel = null;

		String sql = "select ar.* from admin_user au, admin_role ar where au.role_id = ar.role_id and au.uid = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, uid);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					aRoleModel = new AdminRoleModel().populateFromResultSet(rs);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
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
				System.out.println("login->finally->exception:" + e.getMessage());
			}
		}
		return aRoleModel;

	}

	/**
	 * 
	 * @Description: 根据角色rid获取角色
	 * @return   
	 * @return AdminRoleModel  
	 * @throws
	 * @author guojp
	 * @date 2016-6-28
	 */
	public AdminRoleModel getRoleByRid(int rid, int adminid) {
		AdminRoleModel aRoleModel = null;

		String sql = "select * from admin_role ar where ar.role_id = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, rid);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					aRoleModel = new AdminRoleModel().populateFromResultSet(rs);
				}
			}
			AddOperationLog("admin_role", String.valueOf(rid), "查询角色", 3, "", "{role_id:" + rid + "}", adminid);
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
				System.out.println("login->finally->exception:" + e.getMessage());
			}
		}
		return aRoleModel;
	}

	/**
	 * 
	 * @Description: 获取角色列表
	 * @return   
	 * @return List<AdminRoleModel>  
	 * @throws
	 * @author guojp
	 * @date 2016-6-30
	 */
	public Map<String, Object> getRoleList(int page, int rows) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<AdminRoleModel> list = new ArrayList<AdminRoleModel>();

		String sql = "select t.role_id, t.menu_ids, t.role_name, t.record_time, t.update_time from admin_role t order by t.role_id limit "
				+ (page - 1) * rows + "," + rows;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					AdminRoleModel aRoleModel = new AdminRoleModel().populateFromResultSet(rs);
					if (aRoleModel != null) {
						list.add(aRoleModel);
					}
				}
			}
			map.put("rows", list);
			// 查询角色总数
			String sqlcount = "select count(t.role_id) count from admin_role t order by t.role_id";
			pstmt = con.prepareStatement(sqlcount);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				map.put("total", rs.getInt(1));
			}

		} catch (Exception e) {
			// TODO: handle exception
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
				System.out.println("login->finally->exception:" + e.getMessage());
			}
		}
		return map;
	}

	/**
	 * 
	 * @Description: 增加角色
	 * @param request
	 * @param response
	 * @param rolename
	 * @return
	 * @throws IOException   
	 * @return String  
	 * @throws
	 * @author guojp
	 * @date 2016-6-30
	 */
	public boolean saveRole(HttpServletRequest request, HttpServletResponse response, String rolename, int adminid)
			throws IOException {
		response.setContentType("text/html;charset=utf-8");

		// 查询该角色是否存在
		String sql = "select t.role_id from admin_role t where t.role_name = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, rolename);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return false;
			} else {// 如果不存在则插入该角色
				String sqlrole = "insert into admin_role(menu_ids, role_name, record_time, update_time) values(?,?,?,?)";

				pstmt = con.prepareStatement(sqlrole, Statement.RETURN_GENERATED_KEYS);
				pstmt.setString(1, "");
				pstmt.setString(2, rolename);
				pstmt.setLong(3, (int) (System.currentTimeMillis() / 1000));
				pstmt.setLong(4, (int) (System.currentTimeMillis() / 1000));
				pstmt.executeUpdate();

				rs = pstmt.getGeneratedKeys();
				int rc = 0;
				if (rs != null && rs.next()) {
					rc = rs.getInt(1);
				}

				if (rc > 0) {
					AddOperationLog("admin_role", String.valueOf(rc), "新增角色", 1, "", "{role_name:" + rolename + "}",
							adminid);
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
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
				System.out.println("login->finally->exception:" + e.getMessage());
			}
		}
		return false;

	}

	public boolean saveRuleTree(int gid, String grule, int adminid) {
		// 查询该角色是否存在
		AdminRoleModel loadRuleById = loadRuleById(gid);
		if (loadRuleById == null) {
			return false;
		}

		String sql = "update admin_role t set t.menu_ids = ? where t.role_id = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, grule);
			pstmt.setInt(2, gid);
			int rc = pstmt.executeUpdate();

			if (rc > 0) {
				AddOperationLog("admin_role", String.valueOf(gid), "设置权限", 2, JSONObject.toJSONString(loadRuleById),
						"{menu_ids:" + grule + "}", adminid);
				return true;
			} else {
				return false;
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
				System.out.println("login->finally->exception:" + e.getMessage());
			}
		}
		return false;
	}

	// 加载角色
	public AdminRoleModel loadRuleById(int rid) {
		AdminRoleModel aRoleModel = null;

		String sql = "select * from admin_role t where t.role_id = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, rid);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					aRoleModel = new AdminRoleModel().populateFromResultSet(rs);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
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
				System.out.println("login->finally->exception:" + e.getMessage());
			}
		}
		return aRoleModel;
	}

	// 修改角色
	public boolean editRole(HttpServletRequest request, HttpServletResponse response, String rolename, int roleid,
			int adminid) throws IOException {
		response.setContentType("text/html;charset=utf-8");

		AdminRoleModel ruleModel = loadRuleById(roleid);

		if (ruleModel == null) {
			return false;
		}

		String sql = "UPDATE admin_role t SET t.role_name = ?, t.update_time = ? WHERE t.role_id = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "master");
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, rolename);
			pstmt.setLong(2, (int) (System.currentTimeMillis() / 1000));
			pstmt.setInt(3, roleid);
			int rc = pstmt.executeUpdate();

			if (rc > 0) {
				AddOperationLog("admin_role", String.valueOf(rc), "修改角色", 2, JSONObject.toJSONString(ruleModel),
						"{role_name:" + rolename + "}", adminid);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
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
		return false;

	}

	// 删除角色
	public boolean delRule(int roleid, int adminid) throws IOException {

		AdminRoleModel ruleModel = loadRuleById(roleid);
		if (ruleModel == null) {
			return false;
		}

		String sql = "delete from admin_role where role_id = ?";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "master");
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, roleid);
			int rc = pstmt.executeUpdate();

			if (rc > 0) {
				AddOperationLog("admin_role", String.valueOf(roleid), "删除角色", 2, JSONObject.toJSONString(ruleModel), "",
						adminid);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
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
		return false;
	}

	// 获取所有角色，提供给前端下拉列表使用，不分页
	public List<AdminRoleModel> getAllRoleList() {
		List<AdminRoleModel> list = new ArrayList<AdminRoleModel>();

		String sql = "select t.role_id, t.menu_ids, t.role_name, t.record_time, t.update_time from admin_role t order by t.role_id";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin, "slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					AdminRoleModel aRoleModel = new AdminRoleModel().populateFromResultSet(rs);
					if (aRoleModel != null) {
						list.add(aRoleModel);
					}
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
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
}
