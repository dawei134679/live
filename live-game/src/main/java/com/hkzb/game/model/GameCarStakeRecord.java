package com.hkzb.game.model;

import java.io.Serializable;

public class GameCarStakeRecord implements Serializable {

	private static final long serialVersionUID = 6675546905713927573L;

	private Long id;

    private Long roomId;

    private Long anchorId;

    private Long uid;

    private Long carId;

    private Long money;

    private Long stakeTime;

    private Integer status;

    private Long refId;

    private Long awardedMoney;

    private Long deservedMoney;

    private Long commission;

    private Double commissionRate;
    
    private Double multiple;

	private Long periods;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public Long getAnchorId() {
		return anchorId;
	}

	public void setAnchorId(Long anchorId) {
		this.anchorId = anchorId;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

	public Long getMoney() {
		return money;
	}

	public void setMoney(Long money) {
		this.money = money;
	}

	public Long getStakeTime() {
		return stakeTime;
	}

	public void setStakeTime(Long stakeTime) {
		this.stakeTime = stakeTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
	}

	public Long getAwardedMoney() {
		return awardedMoney;
	}

	public void setAwardedMoney(Long awardedMoney) {
		this.awardedMoney = awardedMoney;
	}

	public Long getDeservedMoney() {
		return deservedMoney;
	}

	public void setDeservedMoney(Long deservedMoney) {
		this.deservedMoney = deservedMoney;
	}

	public Long getCommission() {
		return commission;
	}

	public void setCommission(Long commission) {
		this.commission = commission;
	}

	public Double getCommissionRate() {
		return commissionRate;
	}

	public void setCommissionRate(Double commissionRate) {
		this.commissionRate = commissionRate;
	}

	public Double getMultiple() {
		return multiple;
	}
	
	public void setMultiple(Double multiple) {
		this.multiple = multiple;
	}
	
	public Long getPeriods() {
		return periods;
	}
	public void setPeriods(Long periods) {
		this.periods = periods;
	}
}