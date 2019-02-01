package com.mpig.api.model;

import java.sql.ResultSet;

/**
 * 邀请任务的奖励列表
 * @author zyl
 * @date 2016-11-9 下午5:41:46
 */
public class InviteUserRewardInfoModel implements PopulateTemplate<InviteUserRewardInfoModel>{

	private Integer uid;
	private Integer inviteUid;
	private int status;
	private Integer createAt;
	private Integer updateAt;
	
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Integer getInviteUid() {
		return inviteUid;
	}
	public void setInviteUid(Integer inviteUid) {
		this.inviteUid = inviteUid;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public Integer getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Integer createAt) {
		this.createAt = createAt;
	}
	public Integer getUpdateAt() {
		return updateAt;
	}
	public void setUpdateAt(Integer updateAt) {
		this.updateAt = updateAt;
	}
	@Override
	public InviteUserRewardInfoModel populateFromResultSet(ResultSet rs) {
		try {
			this.uid = rs.getInt("uid");
			this.inviteUid = rs.getInt("invite_uid");
			this.status = rs.getInt("status");
			this.createAt = rs.getInt("createAt");
			this.updateAt = rs.getInt("updateAt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	
}
