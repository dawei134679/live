package com.mpig.api.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ActivityModel implements PopulateTemplate<ActivityModel>,Serializable{

	private boolean on;
	private String pic_bannner;
	private String pic_btn;
	private String pic_main;
	private String act_url;
	private String actName;
	private Long expireat;
	private int position;
	private String pic_ext;
	public boolean isOn() {
		return on;
	}
	public void setOn(boolean on) {
		this.on = on;
	}
	public String getPic_bannner() {
		return pic_bannner;
	}
	public void setPic_bannner(String pic_bannner) {
		this.pic_bannner = pic_bannner;
	}
	public String getPic_btn() {
		return pic_btn;
	}
	public void setPic_btn(String pic_btn) {
		this.pic_btn = pic_btn;
	}
	public String getPic_main() {
		return pic_main;
	}
	public void setPic_main(String pic_main) {
		this.pic_main = pic_main;
	}
	public String getAct_url() {
		return act_url;
	}
	public void setAct_url(String act_url) {
		this.act_url = act_url;
	}
	public String getActName() {
		return actName;
	}
	public void setActName(String actName) {
		this.actName = actName;
	}
	public Long getExpireat() {
		return expireat;
	}
	public void setExpireat(Long expireat) {
		this.expireat = expireat;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getPic_ext() {
		return pic_ext;
	}
	public void setPic_ext(String pic_ext) {
		this.pic_ext = pic_ext;
	}
	@Override
	public ActivityModel populateFromResultSet(ResultSet rs) {
		try {
			this.on = rs.getBoolean("on");
			this.pic_bannner = rs.getString("pic_bannner");
			this.pic_btn = rs.getString("pic_btn");
			this.pic_main = rs.getString("pic_main");
			this.act_url = rs.getString("act_url");
			this.actName = rs.getString("actName");
			this.expireat = rs.getLong("expireat");
			this.position = rs.getInt("position");
			this.pic_ext = rs.getString("pic_ext");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}
}
