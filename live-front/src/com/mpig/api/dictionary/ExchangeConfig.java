package com.mpig.api.dictionary;

/**
 * 内兑配置
 * @author fangwuqing
 *
 */
public class ExchangeConfig {

	private int money;
	private int zhutou;
	public int getMoney() {
		return money;
	}
	public int getZhutou() {
		return zhutou;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public void setZhutou(int zhutou) {
		this.zhutou = zhutou;
	}

	public ExchangeConfig initWith(int money,int zhutou){
		this.money = money;
		this.zhutou = zhutou;
		return this;
	}
}
