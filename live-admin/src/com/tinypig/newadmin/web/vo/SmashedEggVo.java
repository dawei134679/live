package com.tinypig.newadmin.web.vo;

import java.io.Serializable;

public class SmashedEggVo implements Serializable {
	private static final long serialVersionUID = -5826272562696635820L;
	private Long uid;
	private Long roomId;
	private Long hammerPrice;
	private Long rewardGiftTotalPrice;
	private Long createAt;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public Long getHammerPrice() {
		return hammerPrice;
	}

	public void setHammerPrice(Long hammerPrice) {
		this.hammerPrice = hammerPrice;
	}

	public Long getRewardGiftTotalPrice() {
		return rewardGiftTotalPrice;
	}

	public void setRewardGiftTotalPrice(Long rewardGiftTotalPrice) {
		this.rewardGiftTotalPrice = rewardGiftTotalPrice;
	}

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}

}
