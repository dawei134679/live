package com.mpig.api.dictionary;

/**
 * 中奖概率及倍数的配置
 * @author fangwuqing
 *
 */
public class GameBetConfig {

	private int id;
	private String name;
	private double times;
	private int rate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getTimes() {
		return times;
	}
	public void setTimes(double times) {
		this.times = times;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	
	public GameBetConfig initWith(int id,String name,double times,int rate){
		
		this.id = id;
		this.name = name;
		this.times = times;
		this.rate = rate;
		return this;
	}
}
