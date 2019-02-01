package com.mpig.api.modelcomet;

/**
 *PK邀请消息
 */
public class PKInviteCMod extends BaseCMod {

	public PKInviteCMod(){
		this.setCometProtocol(CModProtocol.pk_invite);
	}
	
	private Integer pkTime;
	
	private Integer penaltyTime;
	
	private Integer inviteStatus;

	public Integer getPkTime() {
		return pkTime;
	}

	public void setPkTime(Integer pkTime) {
		this.pkTime = pkTime;
	}

	public Integer getPenaltyTime() {
		return penaltyTime;
	}

	public void setPenaltyTime(Integer penaltyTime) {
		this.penaltyTime = penaltyTime;
	}

	public Integer getInviteStatus() {
		return inviteStatus;
	}

	public void setInviteStatus(Integer inviteStatus) {
		this.inviteStatus = inviteStatus;
	}
}
