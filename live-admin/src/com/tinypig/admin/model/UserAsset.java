package com.tinypig.admin.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAsset implements PopulateTemplate<UserAsset> {
	private int money;
	private int wealth;
	private long credit;
	private long creditTotal;

	public int getMoney() {
		return money;
	}


	public void setMoney(int money) {
		this.money = money;
	}


	public int getWealth() {
		return wealth;
	}


	public void setWealth(int wealth) {
		this.wealth = wealth;
	}


	public long getCredit() {
		return credit;
	}


	public void setCredit(long credit) {
		this.credit = credit;
	}


	public long getCreditTotal() {
		return creditTotal;
	}


	public void setCreditTotal(long creditTotal) {
		this.creditTotal = creditTotal;
	}


	@Override
	public UserAsset populateFromResultSet(ResultSet rs) {
		try {
			money = rs.getInt("money");
			wealth = rs.getInt("wealth");
			credit = rs.getLong("credit");
			creditTotal = rs.getLong("creditTotal");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}

}
