package com.mpig.api.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BillPrizeListModel implements PopulateTemplate<BillPrizeListModel>{
	private Integer id;
	private Integer uid;
	private Integer anchoruid;
	private Integer multiples;
	private Integer luckyCount;
	private Integer gprice;
	private Integer priceTotal;
	private Integer wealthTotal;
	private Integer creditTotal;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Integer getAnchoruid() {
		return anchoruid;
	}
	public void setAnchoruid(Integer anchoruid) {
		this.anchoruid = anchoruid;
	}
	public Integer getMultiples() {
		return multiples;
	}
	public void setMultiples(Integer multiples) {
		this.multiples = multiples;
	}
	public Integer getLuckyCount() {
		return luckyCount;
	}
	public void setLuckyCount(Integer luckyCount) {
		this.luckyCount = luckyCount;
	}
	public Integer getGprice() {
		return gprice;
	}
	public void setGprice(Integer gprice) {
		this.gprice = gprice;
	}
	public Integer getPriceTotal() {
		return priceTotal;
	}
	public void setPriceTotal(Integer priceTotal) {
		this.priceTotal = priceTotal;
	}
	public Integer getWealthTotal() {
		return wealthTotal;
	}
	public void setWealthTotal(Integer wealthTotal) {
		this.wealthTotal = wealthTotal;
	}
	public Integer getCreditTotal() {
		return creditTotal;
	}
	public void setCreditTotal(Integer creditTotal) {
		this.creditTotal = creditTotal;
	}
	
	@Override
	public BillPrizeListModel populateFromResultSet(ResultSet rs) {
		try {
			this.id = rs.getInt("id");
			this.uid = rs.getInt("uid");
			this.anchoruid = rs.getInt("anchoruid");
			this.multiples = rs.getInt("multiples");
			this.luckyCount = rs.getInt("luckyCount");
			this.gprice = rs.getInt("gprice");
			this.priceTotal = rs.getInt("priceTotal");
			this.wealthTotal = rs.getInt("wealthTotal");
			this.creditTotal = rs.getInt("creditTotal");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}
}
