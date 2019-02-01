package com.mpig.api.dictionary;

import java.io.Serializable;
import java.sql.ResultSet;

import com.mpig.api.model.PopulateTemplate;

public class InvitationRewardsConfig implements PopulateTemplate<InvitationRewardsConfig>,Serializable{

	private static final long serialVersionUID = 110978497331181564L;
	
	private Integer id;
	private Integer invitationId; //任务id
	private int type; //奖励的类型  1:金币 2：礼物 3：增值服务
	private String rewards; //奖励； gid，num
	
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getRewards() {
		return rewards;
	}
	public void setRewards(String rewards) {
		this.rewards = rewards;
	}
	@Override
	public InvitationRewardsConfig populateFromResultSet(ResultSet rs) {
		try {
			this.id = rs.getInt("id");
			this.invitationId = rs.getInt("invitation_id");
			this.type = rs.getInt("type");
			this.rewards = rs.getString("rewards");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
}
