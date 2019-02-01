package com.mpig.api.model;

import java.sql.ResultSet;

public class PayExchangeModel implements PopulateTemplate<PayExchangeModel>{

	private int id;
	private int uid;
	private int credit;
	private int money;
	private int zhutou;
	private int createAT;
	
	public int getId() {
		return id;
	}
	public int getUid() {
		return uid;
	}
	public int getCredit() {
		return credit;
	}
	public int getMoney() {
		return money;
	}
	public int getZhutou() {
		return zhutou;
	}
	public int getCreateAT() {
		return createAT;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public void setCredit(int credit) {
		this.credit = credit;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public void setZhutou(int zhutou) {
		this.zhutou = zhutou;
	}
	public void setCreateAT(int createAT) {
		this.createAT = createAT;
	}
	
	@Override
	public PayExchangeModel populateFromResultSet(ResultSet rs) {
		try {
			this.id = rs.getInt("id");
			this.uid = rs.getInt("uid");
			this.credit = rs.getInt("credit");
			this.money = rs.getInt("money");
			this.zhutou = rs.getInt("zhutou");
			this.createAT = rs.getInt("createAT");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
}
