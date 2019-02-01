package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class AppControlBtnDto implements Serializable {
	private static final long serialVersionUID = -4664469078448167413L;

	private String pay;
	private String payother;
	private String giftshow;
	private String tixian;
	private String gameshow;
	private String adsshow;

	public String getPay() {
		return pay;
	}

	public void setPay(String pay) {
		this.pay = pay;
	}

	public String getPayother() {
		return payother;
	}

	public void setPayother(String payother) {
		this.payother = payother;
	}

	public String getGiftshow() {
		return giftshow;
	}

	public void setGiftshow(String giftshow) {
		this.giftshow = giftshow;
	}

	public String getTixian() {
		return tixian;
	}

	public void setTixian(String tixian) {
		this.tixian = tixian;
	}

	public String getGameshow() {
		return gameshow;
	}

	public void setGameshow(String gameshow) {
		this.gameshow = gameshow;
	}

	public String getAdsshow() {
		return adsshow;
	}

	public void setAdsshow(String adsshow) {
		this.adsshow = adsshow;
	}

}
