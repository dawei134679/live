package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class GameDollSetting implements Serializable {

	private static final long serialVersionUID = -1775871693078245144L;

	private Integer id;

	private Integer claw1;

	private Integer claw2;

	private Integer claw3;

	private Integer roomInformMoney;

	private Integer platformInformMoney;

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

	public Integer getClaw1() {
		return claw1;
	}

	public void setClaw1(Integer claw1) {
		this.claw1 = claw1;
	}

	public Integer getClaw2() {
		return claw2;
	}

	public void setClaw2(Integer claw2) {
		this.claw2 = claw2;
	}

	public Integer getClaw3() {
		return claw3;
	}

	public void setClaw3(Integer claw3) {
		this.claw3 = claw3;
	}

	public Integer getRoomInformMoney() {
		return roomInformMoney;
	}

	public void setRoomInformMoney(Integer roomInformMoney) {
		this.roomInformMoney = roomInformMoney;
	}

	public Integer getPlatformInformMoney() {
		return platformInformMoney;
	}

	public void setPlatformInformMoney(Integer platformInformMoney) {
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
}