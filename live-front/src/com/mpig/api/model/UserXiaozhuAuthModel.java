package com.mpig.api.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserXiaozhuAuthModel implements PopulateTemplate<UserXiaozhuAuthModel>,Serializable{
	private int id; 
	private Integer uid;
	private String nickname; //认证昵称
	private String authContent; //认证内容
	private String authPics; //认证图片 以,分隔
	private String authURLs;  //认证连接地址
	private int status; //审核状态 ＝1提交待审核 ＝2驳回 ＝3通过
	private String cause; //驳回原因
	private int createAt; //创建时间
	private int auditAt; //修改时间
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAuthContent() {
		return authContent;
	}
	public void setAuthContent(String authContent) {
		this.authContent = authContent;
	}
	public String getAuthPics() {
		return authPics;
	}
	public void setAuthPics(String authPics) {
		this.authPics = authPics;
	}
	public String getAuthURLs() {
		return authURLs;
	}
	public void setAuthURLs(String authURLs) {
		this.authURLs = authURLs;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public int getCreateAt() {
		return createAt;
	}
	public void setCreateAt(int createAt) {
		this.createAt = createAt;
	}
	public int getAuditAt() {
		return auditAt;
	}
	public void setAuditAt(int auditAt) {
		this.auditAt = auditAt;
	}
	
	@Override
	public UserXiaozhuAuthModel populateFromResultSet(ResultSet rs) {
		try {
			this.id = rs.getInt("id");
			this.nickname = rs.getString("nickname");
			this.authPics = rs.getString("authPics");
			this.authContent = rs.getString("authContent");
			this.authURLs = rs.getString("authURLs");
			this.cause = rs.getString("cause");
			this.status = rs.getInt("status");
			this.createAt = rs.getInt("createAt");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}
}
