package com.mpig.api.model;

import java.io.Serializable;

/**
 * 砸蛋游戏中奖礼物配置
 */
public class SmashedEggGiftConfigModel implements Serializable {
	private static final long serialVersionUID = -6388616986005123399L;

	private Long id;
	private Long giftId;
	private String giftName;
	private int giftNum;
	private int giftType;
	private Integer hammerType;
	private Double probability;
	private Integer isfirstprize;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGiftId() {
		return giftId;
	}

	public void setGiftId(Long giftId) {
		this.giftId = giftId;
	}

	public String getGiftName() {
		return giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public int getGiftNum() {
		return giftNum;
	}

	public void setGiftNum(int giftNum) {
		this.giftNum = giftNum;
	}
	
	public int getGiftType() {
		return giftType;
	}

	public void setGiftType(int giftType) {
		this.giftType = giftType;
	}

	public Integer getHammerType() {
		return hammerType;
	}

	public void setHammerType(Integer hammerType) {
		this.hammerType = hammerType;
	}

	public Double getProbability() {
		return probability;
	}

	public void setProbability(Double probability) {
		this.probability = probability;
	}

	public Integer getIsfirstprize() {
		return isfirstprize;
	}

	public void setIsfirstprize(Integer isfirstprize) {
		this.isfirstprize = isfirstprize;
	}

	// @Override
	// public SmashedEggGiftConfigModel populateFromResultSet(ResultSet rs) {
	// try {
	// this.id = rs.getLong("id");
	// this.gift_id = rs.getLong("gift_id");
	// this.gift_name = rs.getString("gift_name");
	// this.hammer_type = rs.getInt("hammer_type");
	// this.probability = rs.getDouble("probability");
	// this.isfirstprize = rs.getInt("isfirstprize");
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return this;
	// }

}
