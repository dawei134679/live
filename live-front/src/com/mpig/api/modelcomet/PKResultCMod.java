package com.mpig.api.modelcomet;

/**
 * 
 *	PK结果
 */
public class PKResultCMod extends BaseCMod{
	public PKResultCMod(){
		this.setCometProtocol(CModProtocol.pk_result);
	}
private Integer winnerUid;
	
	private Integer penaltyTime;

	public Integer getWinnerUid() {
		return winnerUid;
	}

	public void setWinnerUid(Integer winnerUid) {
		this.winnerUid = winnerUid;
	}

	public Integer getPenaltyTime() {
		return penaltyTime;
	}

	public void setPenaltyTime(Integer penaltyTime) {
		this.penaltyTime = penaltyTime;
	}
}
