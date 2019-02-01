package com.tinypig.admin.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 工会信息
 * @author tosy
 *
 */
public class UnionModel  implements PopulateTemplate<UnionModel> {
	
	//DB
	int unionid;
	String unionname;
	int createtime;
	int anchorcount;
	long profit;
	String desc;
	int ownerid;
	int credit;
	int adminuid;
	int operatoruid;
	long totalmoney;
	int status;
	
	//返回需要，不是表内的字段
	String ownername;
	String adminname;
	String operatorname;
	
	public String getAdminname() {
		return adminname;
	}
	public void setAdminname(String adminname) {
		this.adminname = adminname;
	}
	public String getOperatorname() {
		return operatorname;
	}
	public void setOperatorname(String operatorname) {
		this.operatorname = operatorname;
	}
	public int getOwnerid() {
		return ownerid;
	}
	public void setOwnerid(int ownerid) {
		this.ownerid = ownerid;
	}
	public String getOwnername() {
		return ownername;
	}
	public void setOwnername(String ownername) {
		this.ownername = ownername;
	}
	public int getUnionid() {
		return unionid;
	}
	public void setUnionid(int unionid) {
		this.unionid = unionid;
	}
	public String getUnionname() {
		return unionname;
	}
	public void setUnionname(String unionname) {
		this.unionname = unionname;
	}
	
	public int getAnchorcount() {
		return anchorcount;
	}
	public void setAnchorcount(int anchorcount) {
		this.anchorcount = anchorcount;
	}
	public long getProfit() {
		return profit;
	}
	public void setProfit(long profit) {
		this.profit = profit;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getCredit() {
		return credit;
	}
	public void setCredit(int credit) {
		this.credit = credit;
	}
	public int getAdminuid() {
		return adminuid;
	}
	public void setAdminuid(int adminuid) {
		this.adminuid = adminuid;
	}
	public int getOperatoruid() {
		return operatoruid;
	}
	public void setOperatoruid(int operatoruid) {
		this.operatoruid = operatoruid;
	}
	public long getTotalmoney() {
		return totalmoney;
	}
	public void setTotalmoney(long totalmoney) {
		this.totalmoney = totalmoney;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public UnionModel populateFromResultSet(ResultSet rs) {
		try {
			unionid = rs.getInt("unionid");
			unionname = rs.getString("unionname");
			createtime = rs.getInt("createtime");
			anchorcount = rs.getInt("anchorcount");
			profit = rs.getInt("profit");
			desc = rs.getString("remarks");
			ownerid = rs.getInt("ownerid");
			adminuid = rs.getInt("adminuid");
			operatoruid = rs.getInt("operatoruid");
			totalmoney = rs.getLong("totalmoney");
			status = rs.getInt("status");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
	public int getCreatetime() {
		return createtime;
	}
	public void setCreatetime(int createtime) {
		this.createtime = createtime;
	}	
}
