package com.mpig.api.dictionary;

public final class LevelsConfig {

	private Integer levelCode;
	private String levelName;
	private Long number;
	private Integer rq;
	private Integer rate;
	
	public Integer getLevelCode() {
		return levelCode;
	}
	public void setLevelCode(Integer levelCode) {
		this.levelCode = levelCode;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	public Long getNumber() {
		return number;
	}
	public void setNumber(Long number) {
		this.number = number;
	}
	
	public Integer getRq() {
		return rq;
	}
	public void setRq(Integer rq) {
		this.rq = rq;
	}
	public Integer getRate() {
		return rate;
	}
	public void setRate(Integer rate) {
		this.rate = rate;
	}
	public LevelsConfig initWith(Integer code,String name,Long iNumber,Integer iRq,Integer rate) {
		this.levelCode = code;
		this.levelName = name;
		this.number = iNumber;
		this.rq = iRq;
		this.rate = rate;
		return this;
	}	
}
