package com.tinypig.admin.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminRoleModel implements PopulateTemplate<AdminRoleModel>{
	private int role_id;
	private String menu_ids;
	private String role_name;
	private int update_time;
	private int record_time;
	
	public int getRole_id() {
		return role_id;
	}
	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}
	public String getMenu_ids() {
		return menu_ids;
	}
	public void setMenu_ids(String menu_ids) {
		this.menu_ids = menu_ids;
	}
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	public int getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(int update_time) {
		this.update_time = update_time;
	}
	public int getRecord_time() {
		return record_time;
	}
	public void setRecord_time(int record_time) {
		this.record_time = record_time;
	}
	@Override
	public AdminRoleModel populateFromResultSet(ResultSet rs) {
		try {
			this.role_id = rs.getInt("role_id");
			this.menu_ids = rs.getString("menu_ids");
			this.role_name = rs.getString("role_name");
			this.update_time = rs.getInt("update_time");
			this.record_time = rs.getInt("record_time");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	

}
