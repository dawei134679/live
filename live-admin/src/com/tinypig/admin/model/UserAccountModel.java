package com.tinypig.admin.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAccountModel implements PopulateTemplate<UserAccountModel> {

	private Integer uid;
	private Integer accountid;
	private String accountname;
	private String password;
	private String authkey;
	private byte status;
	
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	
	public Integer getAccountid() {
		return accountid;
	}
	public void setAccountid(Integer accountid) {
		this.accountid = accountid;
	}

	public String getAccountname() {
		return accountname;
	}
	public void setAccountname(String accountname) {
		this.accountname = accountname == null ? null : accountname.trim();
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}
    
    /**
     * 挂载的开播房间号
     */
    private String roomId;

	public String getAuthkey() {
		return authkey;
	}
	public void setAuthkey(String authkey) {
		this.authkey = authkey == null ? null : authkey.trim();
	}
	
	@Override
	public UserAccountModel populateFromResultSet(final ResultSet rs) {
		try {
			uid = rs.getInt("uid");
			accountid = rs.getInt("accountid");
			accountname = rs.getString("accountname");
			password = rs.getString("password");
			authkey = rs.getString("authkey");
			this.status = rs.getByte("status");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public byte getStatus() {
		return status;
	}
	public void setStatus(byte status) {
		this.status = status;
	}
}