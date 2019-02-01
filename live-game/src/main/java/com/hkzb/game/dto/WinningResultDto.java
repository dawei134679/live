package com.hkzb.game.dto;

import java.io.Serializable;

public class WinningResultDto implements Serializable {

	private static final long serialVersionUID = -8392718610809602733L;

	private Long carId1;

    private Long carId2;

    private Long carId3;
	    
	private Long totalStakeMoney;
	
	private Long totalDeservedMoney;
	
	public Long getCarId1() {
		return carId1;
	}

	public void setCarId1(Long carId1) {
		this.carId1 = carId1;
	}

	public Long getCarId2() {
		return carId2;
	}

	public void setCarId2(Long carId2) {
		this.carId2 = carId2;
	}

	public Long getCarId3() {
		return carId3;
	}

	public void setCarId3(Long carId3) {
		this.carId3 = carId3;
	}

	public Long getTotalStakeMoney() {
		return totalStakeMoney;
	}

	public void setTotalStakeMoney(Long totalStakeMoney) {
		this.totalStakeMoney = totalStakeMoney;
	}

	public Long getTotalDeservedMoney() {
		return totalDeservedMoney;
	}

	public void setTotalDeservedMoney(Long totalDeservedMoney) {
		this.totalDeservedMoney = totalDeservedMoney;
	}
}
