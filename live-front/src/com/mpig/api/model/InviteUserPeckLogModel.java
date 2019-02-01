package com.mpig.api.model;

import java.sql.ResultSet;

public class InviteUserPeckLogModel implements PopulateTemplate<InviteUserPeckLogModel>{
	
	private Integer id;
	private Integer uid;
	private Integer invitationId;
	private Integer status;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getInvitationId() {
		return invitationId;
	}
	public void setInvitationId(Integer invitationId) {
		this.invitationId = invitationId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	@Override
	public InviteUserPeckLogModel populateFromResultSet(ResultSet rs) {
		try {
			this.id = rs.getInt("id");
			this.invitationId = rs.getInt("invitation_id");
			this.status = rs.getInt("status");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	
}
