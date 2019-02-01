package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class AdminRole implements Serializable {
	private static final long serialVersionUID = 3846080847682056492L;

	private Byte roleId;

	private String menuIds;

	private String roleName;

	private Integer updateTime;

	private Integer recordTime;

	public Byte getRoleId() {
		return roleId;
	}

	public void setRoleId(Byte roleId) {
		this.roleId = roleId;
	}

	public String getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds == null ? null : menuIds.trim();
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName == null ? null : roleName.trim();
	}

	public Integer getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Integer updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Integer recordTime) {
		this.recordTime = recordTime;
	}
}