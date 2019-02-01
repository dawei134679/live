package com.mpig.api.model;

import java.io.Serializable;
import java.util.Date;

public class DemoModel implements Serializable {

	private static final long serialVersionUID = -3069338611365292779L;
	
	public DemoModel(Integer aid, String aname){
		this.aid = aid;
		this.aname = aname;
	}

	private Integer aid;

	private String aname;

	private String aintro;

	private String aicon;

	private String uname;

	private String startdate;

	private String enddate;

	private Date time;

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public String getAname() {
		return aname;
	}

	public void setAname(String aname) {
		this.aname = aname == null ? null : aname.trim();
	}

	public String getAintro() {
		return aintro;
	}

	public void setAintro(String aintro) {
		this.aintro = aintro == null ? null : aintro.trim();
	}

	public String getAicon() {
		return aicon;
	}

	public void setAicon(String aicon) {
		this.aicon = aicon == null ? null : aicon.trim();
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname == null ? null : uname.trim();
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate == null ? null : startdate.trim();
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate == null ? null : enddate.trim();
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}