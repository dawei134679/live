package com.tinypig.admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.tinypig.admin.util.DbUtil;

public class BaseDao {

	/**
	 * 新增管理员操作日志
	 * 
	 * @param dbname
	 *            表名
	 * @param db_id
	 *            表key
	 * @param operation_note
	 *            操作类型
	 * @param action
	 *            ＝ 新增 编辑 删除
	 * @param previous_version
	 *            更改前
	 * @param current_version
	 *            更改后
	 * @param admin_id
	 *            操作UID
	 * @return ＝1成功 其他失败
	 */
	public int AddOperationLog(String dbname, String db_id, String operation_note, int action,
			String previous_version, String current_version, int admin_id) {
		int ires = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = "insert into admin_operation_log(dbname,db_id,operation_time,operation_note,action,previous_version,current_version,admin_id)value(?,?,?,?,?,?,?,?)";
		try {
			con = DbUtil.instance().getCon("zhu_admin","master");
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, dbname);
			pstmt.setString(2, db_id);
			pstmt.setInt(3, (int) (System.currentTimeMillis() / 1000));
			pstmt.setString(4, operation_note);
			pstmt.setInt(5, action);
			pstmt.setString(6, previous_version);
			pstmt.setString(7, current_version);
			pstmt.setInt(8, admin_id);

			ires = pstmt.executeUpdate();
		} catch (Exception e) {
			
			System.out.println("dbname=" + dbname + "&admin_id=" + admin_id + "&db_id=" + db_id + "&operation_note="
					+ operation_note + "&action=" + action + "&previous_version=" + previous_version + "&current_version="
					+ current_version);

			System.out.println("addoperationlog->exception:" + e.getMessage());
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception ex) {
				System.out.println("addoperationlog->finally->exception:" + ex.getMessage());
			}
		}
		return ires;
	}

	public Integer valueOf(String str) {
		Integer rt = null;
		try {
			rt = Integer.valueOf(str);
		} catch (NumberFormatException e) {
		}
		return rt;
	}

	/**
	 * 获取表名
	 * 
	 * @param uid
	 * @param predbname
	 * @return
	 */
	public String getDbName(int uid, String predbname) {
		int sufix = uid % 100;
		return predbname + (sufix < 10 ? "0" + sufix : sufix);
	}
}
