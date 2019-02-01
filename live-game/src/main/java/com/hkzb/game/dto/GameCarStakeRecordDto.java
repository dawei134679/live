package com.hkzb.game.dto;

import java.io.Serializable;

public class GameCarStakeRecordDto implements Serializable {

	private static final long serialVersionUID = 4858371593002971855L;

	private String carId;
	
	private String img;

	private String money;

	private String status;

	private Long periods;

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getPeriods() {
		return periods;
	}

	public void setPeriods(Long periods) {
		this.periods = periods;
	}
}
