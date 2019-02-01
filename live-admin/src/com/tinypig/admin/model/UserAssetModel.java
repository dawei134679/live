package com.tinypig.admin.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAssetModel implements PopulateTemplate<UserAssetModel>{
	
    private Integer uid;
    private int money;
    private int wealth;
    private int credit;
    private int creditTotal;

    public Integer getUid() {
        return uid;
    }
    public void setUid(Integer uid) {
        this.uid = uid;
    }
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
    public int getCredit() {
        return credit;
    }
    public void setCredit(int credit) {
        this.credit = credit;
    }

	@Override
	public UserAssetModel populateFromResultSet(ResultSet rs) {
		try {
			uid = rs.getInt("uid");
			money = rs.getInt("money");
			wealth = rs.getInt("wealth");
			credit = rs.getInt("credit");	
			creditTotal = rs.getInt("creditTotal");
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return this;		
	}
	public int getCreditTotal() {
		return creditTotal;
	}
	public void setCreditTotal(int creditTotal) {
		this.creditTotal = creditTotal;
	}
}