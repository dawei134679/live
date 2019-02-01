package com.tinypig.admin.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminUser implements PopulateTemplate<AdminUser> {
	private int uid;
	private String username;
	private String password;
	private int role_id;
	private int type;
	private int reg_time;
	private int login_time;
	private int createUid;

	public int getUid() {
		return uid;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public int getReg_time() {
		return reg_time;
	}

	public int getLogin_time() {
		return login_time;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRole_id(byte role_id) {
		this.role_id = role_id;
	}

	public void setReg_time(int reg_time) {
		this.reg_time = reg_time;
	}

	public void setLogin_time(int login_time) {
		this.login_time = login_time;
	}

	public int getCreateUid() {
		return createUid;
	}

	public void setCreateUid(int createUid) {
		this.createUid = createUid;
	}

	public int getRole_id() {
		return role_id;
	}

	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public AdminUser populateFromResultSet(ResultSet rs) {
		try {
			this.uid = rs.getInt("uid");
			this.username = rs.getString("username");
			this.password = rs.getString("password");
			this.role_id = rs.getInt("role_id");
			this.type = rs.getInt("type");
			this.reg_time = rs.getInt("reg_time");
			this.login_time = rs.getInt("login_time");
			this.createUid = rs.getInt("createUid");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return this;
	}
}
