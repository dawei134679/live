package com.mpig.api.model;

import java.io.Serializable;

public class PKRecordModel implements Serializable{
	
	private static final long serialVersionUID = 6282344298337549445L;
	
	private Integer id ; 
	
	private Integer firstUid;
	
	private Integer secodUid;
	
	private Integer pkTime;
	
	private Integer penaltyTime;
	
	private Long firstUserVotes;
	
	private Long secodUserVotes;
	
	private Integer winnerUid;
	
	private Long createTime;
	
	private Long updateTIme;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public Integer getWinnerUid() {
		return winnerUid;
	}

	public void setWinnerUid(Integer winnerUid) {
		this.winnerUid = winnerUid;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTIme() {
		return updateTIme;
	}

	public void setUpdateTIme(Long updateTIme) {
		this.updateTIme = updateTIme;
	}
}