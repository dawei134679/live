package com.mpig.api.modelcomet;

/**
 *PK邀请結果消息
 */
public class PKInviteResultCMod extends BaseCMod {

	public PKInviteResultCMod(){
		this.setCometProtocol(CModProtocol.pk_invite_result);
	}
	
	private Integer inviteResultStatus;
	
	private Integer anchorId;
	
	private String domain;

	public Integer getInviteResultStatus() {
		return inviteResultStatus;
	}

	public void setInviteResultStatus(Integer inviteResultStatus) {
		this.inviteResultStatus = inviteResultStatus;
	}

	public Integer getAnchorId() {
		return anchorId;
	}

	public void setAnchorId(Integer anchorId) {
		this.anchorId = anchorId;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	
}
