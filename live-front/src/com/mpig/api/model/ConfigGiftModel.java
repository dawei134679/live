package com.mpig.api.model;

import java.sql.ResultSet;

public class ConfigGiftModel implements PopulateTemplate<ConfigGiftModel> {
	int gid;
	String gname;
	int type;
	int subtype;
	int gprice;
	int gpriceaudit;
	int wealth;
	int credit;
	int charm;
	String gcover;
	int gtype;
	String gframeurl;
	String gframeurlios;
	int simgs;
	int bimgs;
	int pimgs;
	String gnumtype;
	float gduration;
	int gver;
	int sver;
	boolean isshow;
	boolean isvalid;
	int gsort;
	int createAt;

	private String icon;
	private String skin;
	int useDuration;
	int category;
	int gpctype;
	int act;

	
	public int getCharm() {
		return charm;
	}

	public void setCharm(int charm) {
		this.charm = charm;
	}

	public int getGpctype() {
		return gpctype;
	}

	public void setGpctype(int gpctype) {
		this.gpctype = gpctype;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getAct() {
		return act;
	}

	public void setAct(int act) {
		this.act = act;
	}

	public int getSubtype() {
		return subtype;
	}

	public void setSubtype(int subtype) {
		this.subtype = subtype;
	}

	public int getType() {
		return type;
	}

	public int getUseDuration() {
		return useDuration;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setUseDuration(int useDuration) {
		this.useDuration = useDuration;
	}

	public int getGid() {
		return gid;
	}

	public String getGname() {
		return gname;
	}

	public int getGprice() {
		return gprice;
	}

	public int getGpriceaudit() {
		return gpriceaudit;
	}

	public void setGpriceaudit(int gpriceaudit) {
		this.gpriceaudit = gpriceaudit;
	}

	public int getWealth() {
		return wealth;
	}

	public int getCredit() {
		return credit;
	}

	public String getGcover() {
		return gcover;
	}

	public int getGtype() {
		return gtype;
	}

	public String getGframeurl() {
		return gframeurl;
	}

	public String getGframeurlios() {
		return gframeurlios;
	}

	public void setGframeurlios(String gframeurlios) {
		this.gframeurlios = gframeurlios;
	}

	public int getSimgs() {
		return simgs;
	}

	public int getBimgs() {
		return bimgs;
	}

	public int getPimgs() {
		return pimgs;
	}

	public String getGnumtype() {
		return gnumtype;
	}

	public float getGduration() {
		return gduration;
	}

	public int getGver() {
		return gver;
	}

	public int getSver() {
		return sver;
	}

	public boolean getIsshow() {
		return isshow;
	}

	public boolean getIsvalid() {
		return isvalid;
	}

	public int getGsort() {
		return gsort;
	}

	public int getCreateAt() {
		return createAt;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public void setGprice(int gprice) {
		this.gprice = gprice;
	}

	public void setWealth(int wealth) {
		this.wealth = wealth;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public void setGcover(String gcover) {
		this.gcover = gcover;
	}

	public void setGtype(int gtype) {
		this.gtype = gtype;
	}

	public void setGframeurl(String gframeurl) {
		this.gframeurl = gframeurl;
	}

	public void setSimgs(int simgs) {
		this.simgs = simgs;
	}

	public void setBimgs(int bimgs) {
		this.bimgs = bimgs;
	}

	public void setPimgs(int pimgs) {
		this.pimgs = pimgs;
	}

	public void setGnumtype(String gnumtype) {
		this.gnumtype = gnumtype;
	}

	public void setGduration(float gduration) {
		this.gduration = gduration;
	}

	public void setGver(int gver) {
		this.gver = gver;
	}

	public void setSver(int sver) {
		this.sver = sver;
	}

	public void setIsshow(boolean isshow) {
		this.isshow = isshow;
	}

	public void setIsvalid(boolean isvalid) {
		this.isvalid = isvalid;
	}

	public void setGsort(int gsort) {
		this.gsort = gsort;
	}

	public void setCreateAt(int createAt) {
		this.createAt = createAt;
	}

	@Override
	public ConfigGiftModel populateFromResultSet(ResultSet rs) {
		try {
			this.gid = rs.getInt("gid");
			this.gname = rs.getString("gname");
			this.gprice = rs.getInt("gprice");
			this.gpriceaudit = rs.getInt("gpriceaudit");
			this.wealth = rs.getInt("wealth");
			this.credit = rs.getInt("credit");
			this.charm = rs.getInt("charm");
			this.gcover = rs.getString("gcover");
			this.gtype = rs.getInt("gtype");
			this.gframeurl = rs.getString("gframeurl");
			this.gframeurlios = rs.getString("gframeurlios");
			this.simgs = rs.getInt("simgs");
			this.bimgs = rs.getInt("bimgs");
			this.pimgs = rs.getInt("pimgs");
			this.gnumtype = rs.getString("gnumtype");
			this.gduration = rs.getFloat("gduration");
			this.gver = rs.getInt("gver");
			this.sver = rs.getInt("sver");
			this.isshow = rs.getBoolean("isshow");
			this.isvalid = rs.getBoolean("isvalid");
			this.gsort = rs.getInt("gsort");
			this.createAt = rs.getInt("createAt");

			this.icon = rs.getString("icon");
			this.skin = String.valueOf(rs.getInt("skin"));
			this.useDuration = rs.getInt("useDuration");
			this.type = rs.getInt("type");
			this.subtype = rs.getInt("subtype");
			this.category = rs.getInt("category");
			this.gpctype = rs.getInt("gpctype");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
