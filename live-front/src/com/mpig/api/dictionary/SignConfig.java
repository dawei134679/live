package com.mpig.api.dictionary;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mpig.api.dictionary.lib.SignConfigLib.SignForSign;
import com.mpig.api.model.PopulateTemplate;

/**
 * 
 * @author jackzhang
 *
 */
public final class SignConfig implements PopulateTemplate<SignConfig>,Serializable {
	private static final long serialVersionUID = 1L;
	private int day;
	private int exp;
	/**
	 *  item1,3;item2,2
	 */
	private String item;
	
	/**
	 * 经验图标 
	 */
	private String expIcon;
	private SignForSign phase = SignForSign.SignForSign7;
	private String desc;
	
	public SignConfig initWith(int day,int exp,SignForSign signFor,String desc) {
		this.setDay(day);
		this.exp = exp;
		this.setPhase(signFor);
		this.setDesc(desc);
		return this;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	@Override
	public SignConfig populateFromResultSet(ResultSet rs) {
		try {
			this.day = rs.getInt("day");
			this.exp = rs.getInt("exp");
			this.item = rs.getString("item");
			this.expIcon = rs.getString("expIcon");
			this.phase = rs.getInt("phase") == 0?SignForSign.SignForSign7:rs.getInt("phase") == 1?SignForSign.SignForSign15:SignForSign.SignForSignAfter8;
			this.desc = rs.getString("desc");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public SignForSign getPhase() {
		return phase;
	}

	public void setPhase(SignForSign phase) {
		this.phase = phase;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getExpIcon() {
		return expIcon;
	}

	public void setExpIcon(String expIcon) {
		this.expIcon = expIcon;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
