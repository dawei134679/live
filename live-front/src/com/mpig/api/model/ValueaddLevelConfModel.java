package com.mpig.api.model;

import java.sql.ResultSet;

public class ValueaddLevelConfModel implements PopulateTemplate<ValueaddLevelConfModel>{

	private Integer id;
	private Integer gid;
	private Integer level;
	private Integer exp;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGid() {
		return gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getExp() {
		return exp;
	}

	public void setExp(Integer exp) {
		this.exp = exp;
	}

	@Override
	public ValueaddLevelConfModel populateFromResultSet(ResultSet rs) {
		try {
			this.id = rs.getInt("id");
			this.gid = rs.getInt("gid");
			this.level = rs.getInt("level");
			this.exp = rs.getInt("exp");
		} catch (Exception e) {
		}
		return this;
	}
	
	
}
