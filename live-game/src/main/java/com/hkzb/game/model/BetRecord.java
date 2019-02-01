package com.hkzb.game.model;

import java.io.Serializable;

public class BetRecord implements Serializable {

	private static final long serialVersionUID = -384012041808050457L;

	private Long id;

	private Long anchorId;

	private Long uid;

	private Long betTotal;

	private Long capitalTotal;

	private Long awTotal;

	private Double commission;

	private Long commissionTotal;

	private Long deservedTotal;

	private Long periods;

	private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getBetTotal() {
		return betTotal;
	}

	public void setBetTotal(Long betTotal) {
		this.betTotal = betTotal;
	}

	public Long getCapitalTotal() {
		return capitalTotal;
	}

	public void setCapitalTotal(Long capitalTotal) {
		this.capitalTotal = capitalTotal;
	}

	public Long getAwTotal() {
		return awTotal;
	}

	public void setAwTotal(Long awTotal) {
		this.awTotal = awTotal;
	}

	public Double getCommission() {
		return commission;
	}

	public void setCommission(Double commission) {
		this.commission = commission;
	}

	public Long getCommissionTotal() {
		return commissionTotal;
	}

	public void setCommissionTotal(Long commissionTotal) {
		this.commissionTotal = commissionTotal;
	}

	public Long getDeservedTotal() {
		return deservedTotal;
	}

	public void setDeservedTotal(Long deservedTotal) {
		this.deservedTotal = deservedTotal;
	}

	public Long getPeriods() {
		return periods;
	}

	public void setPeriods(Long periods) {
		this.periods = periods;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
