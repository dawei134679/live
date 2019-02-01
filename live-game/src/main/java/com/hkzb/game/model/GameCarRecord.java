package com.hkzb.game.model;

import java.io.Serializable;

public class GameCarRecord implements Serializable {

	private static final long serialVersionUID = 3641773255019642293L;

	private Long id;

    private Long roomId;

    private Long anchorId;

    private Long carId1;

    private Long carId2;

    private Long carId3;

    private Long lotteryTime;

    private Long periods;
    
    private Integer lotteryType;

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

    public Long getLotteryTime() {
        return lotteryTime;
    }

    public void setLotteryTime(Long lotteryTime) {
        this.lotteryTime = lotteryTime;
    }

    public Long getPeriods() {
        return periods;
    }

    public void setPeriods(Long periods) {
        this.periods = periods;
    }

	public Integer getLotteryType() {
		return lotteryType;
	}

	public void setLotteryType(Integer lotteryType) {
		this.lotteryType = lotteryType;
	}
}