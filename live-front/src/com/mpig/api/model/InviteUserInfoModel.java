package com.mpig.api.model;

import java.sql.ResultSet;

/**
 * 邀请任务的邀请人相关信息
 * @author zyl
 * @date 2016-11-9 下午5:42:00
 */
public class InviteUserInfoModel implements PopulateTemplate<InviteUserInfoModel>{

	private Integer uid;
	private Integer inviteCount;
	private Integer gets;
	private Integer createAt;
	
	public Integer getUid() {
		return uid;
	}


	public void setUid(Integer uid) {
		this.uid = uid;
	}


	public Integer getInviteCount() {
		return inviteCount;
	}


	public void setInviteCount(Integer inviteCount) {
		this.inviteCount = inviteCount;
	}


	public Integer getGets() {
		return gets;
	}


	public void setGets(Integer gets) {
		this.gets = gets;
	}


	public Integer getCreateAt() {
		return createAt;
	}


	public void setCreateAt(Integer createAt) {
		this.createAt = createAt;
	}


	@Override
	public InviteUserInfoModel populateFromResultSet(ResultSet rs) {
		try {
			this.uid = rs.getInt("uid");
			this.inviteCount = rs.getInt("invitecount");
			this.gets = rs.getInt("gets");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	
}
