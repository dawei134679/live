package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class IosVersion implements Serializable {
	private static final long serialVersionUID = 1797310109819130143L;

	private Long id; // 主键
	private String version; // 软件版本号
	private Integer pay; // 支付是否展示 0不展示 1展示
	private Integer payother; // 其他支付是否展示 0不展示 1展示
	private Integer giftshow; // 礼物是否展示 0不展示 1展示
	private Integer tixian; // 提现是否展示 0不展示 1展示
	private Integer gameshow; // 游戏是否展示 0不展示 1展示
	private Integer adsshow; // 广告是否展示 0不展示 1展示
	private Integer audit; // 是否审核版本 0否 1是

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getPay() {
		return pay;
	}

	public void setPay(Integer pay) {
		this.pay = pay;
	}

	public Integer getPayother() {
		return payother;
	}

	public void setPayother(Integer payother) {
		this.payother = payother;
	}

	public Integer getGiftshow() {
		return giftshow;
	}

	public void setGiftshow(Integer giftshow) {
		this.giftshow = giftshow;
	}

	public Integer getTixian() {
		return tixian;
	}

	public void setTixian(Integer tixian) {
		this.tixian = tixian;
	}

	public Integer getGameshow() {
		return gameshow;
	}

	public void setGameshow(Integer gameshow) {
		this.gameshow = gameshow;
	}

	public Integer getAdsshow() {
		return adsshow;
	}

	public void setAdsshow(Integer adsshow) {
		this.adsshow = adsshow;
	}

	public Integer getAudit() {
		return audit;
	}

	public void setAudit(Integer audit) {
		this.audit = audit;
	}


}