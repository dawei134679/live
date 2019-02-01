package com.hkzb.game.dto;

import java.io.Serializable;

public class GameCarTimeDto implements Serializable {

	private static final long serialVersionUID = 1586463143924707704L;

	private Long time;

	private Long periods;

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Long getPeriods() {
		return periods;
	}

	public void setPeriods(Long periods) {
		this.periods = periods;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}