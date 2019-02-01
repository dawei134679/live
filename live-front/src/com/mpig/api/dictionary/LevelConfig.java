package com.mpig.api.dictionary;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mpig.api.model.PopulateTemplate;

/**
 * 
 * @author jackzhang
 *
 */
public final class LevelConfig implements PopulateTemplate<LevelConfig>,Serializable {
	private int lv;
	private Long exp;
	
	public LevelConfig initWith(int lv,Long exp) {
		this.lv = lv;
		this.exp = exp;
		return this;
	}

	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}

	public Long getExp() {
		return exp;
	}

	public void setExp(Long exp) {
		this.exp = exp;
	}

	@Override
	public LevelConfig populateFromResultSet(ResultSet rs) {
		try {
			this.lv = rs.getInt("lv");
			this.exp = rs.getLong("exp");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}
}
