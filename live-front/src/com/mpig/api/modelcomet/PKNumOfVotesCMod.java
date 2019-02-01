package com.mpig.api.modelcomet;

/**
 *票数消息推送
 */
public class PKNumOfVotesCMod extends BaseCMod {

	public PKNumOfVotesCMod(){
		this.setCometProtocol(CModProtocol.pk_num_of_votes);
	}
	
	private Integer firstUid;
	
	private Integer secodUid;
	
	private Long firstUserVotes;
	
	private Long secodUserVotes;

	public Integer getFirstUid() {
		return firstUid;
	}

	public void setFirstUid(Integer firstUid) {
		this.firstUid = firstUid;
	}

	public Integer getSecodUid() {
		return secodUid;
	}

	public void setSecodUid(Integer secodUid) {
		this.secodUid = secodUid;
	}

	public Long getFirstUserVotes() {
		return firstUserVotes;
	}

	public void setFirstUserVotes(Long firstUserVotes) {
		this.firstUserVotes = firstUserVotes;
	}

	public Long getSecodUserVotes() {
		return secodUserVotes;
	}

	public void setSecodUserVotes(Long secodUserVotes) {
		this.secodUserVotes = secodUserVotes;
	}
	
	
}
