package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class GameCarSetting implements Serializable {

	private static final long serialVersionUID = 6329873486743928957L;

	private Integer id;

	private Double probability1;

	private Double probability2;

	private Double probability3;

	private Double gameCommission;

	private Long roomInformMoney;

	private Long platformInformMoney;

	private Long createTime;

	private Long createUserId;

	private Long updateTime;

	private Long updateUserId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getProbability1() {
		return probability1;
	}

	public void setProbability1(Double probability1) {
		this.probability1 = probability1;
	}

	public Double getProbability2() {
		return probability2;
	}

	public void setProbability2(Double probability2) {
		this.probability2 = probability2;
	}

	public Double getProbability3() {
		return probability3;
	}

	public void setProbability3(Double probability3) {
		this.probability3 = probability3;
	}

	public Double getGameCommission() {
		return gameCommission;
	}

	public void setGameCommission(Double gameCommission) {
		this.gameCommission = gameCommission;
	}

	public Long getRoomInformMoney() {
		return roomInformMoney;
	}

	public void setRoomInformMoney(Long roomInformMoney) {
		this.roomInformMoney = roomInformMoney;
	}

	public Long getPlatformInformMoney() {
		return platformInformMoney;
	}

	public void setPlatformInformMoney(Long platformInformMoney) {
		this.platformInformMoney = platformInformMoney;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}

	@Override
	public String toString() {
		return "GameCarSetting [id=" + id + ", probability1=" + probability1 + ", probability2=" + probability2
				+ ", probability3=" + probability3 + ", gameCommission=" + gameCommission + ", createTime=" + createTime
				+ ", createUserId=" + createUserId + ", updateTime=" + updateTime + ", updateUserId=" + updateUserId
				+ "]";
	}
}