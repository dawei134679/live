package com.hkzb.game.dto;

import java.io.Serializable;

public class GameCarMessageDto implements Serializable{

	private static final long serialVersionUID = 4350366121303693349L;

	private Long uid;
	
	private Long anchorId;
	
	private String uname;
	
	private Long money;
	
	private int mesgType;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public Long getMoney() {
		return money;
	}

	public void setMoney(Long money) {
		this.money = money;
	}

	public int getMesgType() {
		return mesgType;
	}

	public void setMesgType(int mesgType) {
		this.mesgType = mesgType;
	}

	public Long getAnchorId() {
		return anchorId;
	}

	public void setAnchorId(Long anchorId) {
		this.anchorId = anchorId;
	}
}
