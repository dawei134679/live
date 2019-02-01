package com.mpig.api.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LiveMicTimeModel implements PopulateTemplate<LiveMicTimeModel> {
	
    private Integer id;
    private Integer uid;
    private String slogan;
    private String province;
    private String city;
    private Integer starttime;
    private Integer endtime;
    private Boolean type;
    private Integer audience;
    private Integer views;
    private Integer likes;
    private Integer credit;
    private Integer addtime;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getUid() {
        return uid;
    }
    public void setUid(Integer uid) {
        this.uid = uid;
    }
    public String getSlogan() {
		return slogan;
	}
	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Integer getStarttime() {
        return starttime;
    }
    public void setStarttime(Integer starttime) {
        this.starttime = starttime;
    }
    public Integer getEndtime() {
        return endtime;
    }
    public void setEndtime(Integer endtime) {
        this.endtime = endtime;
    }
    public Boolean getType() {
        return type;
    }
    public void setType(Boolean type) {
        this.type = type;
    }
	public Integer getAudience() {
		return audience;
	}
	public void setAudience(Integer audience) {
		this.audience = audience;
	}
	public Integer getViews() {
		return views;
	}
	public void setViews(Integer views) {
		this.views = views;
	}
	public Integer getLikes() {
		return likes;
	}
	public void setLikes(Integer likes) {
		this.likes = likes;
	}
	public Integer getCredit() {
		return credit;
	}
	public void setCredit(Integer credit) {
		this.credit = credit;
	}
	public Integer getAddtime() {
		return addtime;
	}
	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}
	@Override
	public LiveMicTimeModel populateFromResultSet(ResultSet rs) {
		try {
			this.id = rs.getInt("id");
			this.uid = rs.getInt("uid");
			this.slogan = rs.getString("slogan");
			this.province = rs.getString("province");
			this.city = rs.getString("city");
			this.starttime = rs.getInt("starttime");
			this.endtime = rs.getInt("endtime");
			this.type = rs.getBoolean("type");
			this.audience = rs.getInt("audience");
			this.views = rs.getInt("views");
			this.likes = rs.getInt("likes");
			this.credit = rs.getInt("credit");
			this.addtime = rs.getInt("addtime");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
    
}