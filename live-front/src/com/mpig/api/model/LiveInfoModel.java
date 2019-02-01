package com.mpig.api.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 主播列表信息
 * @author fang
 *
 */
public class LiveInfoModel implements PopulateTemplate<LiveInfoModel> {

    private Integer id;
    private Integer uid;
    private Integer familyId;
    private String mobileliveimg;
    private Boolean status;
    private Integer opentime;
    private Integer recommend;
    private Integer videoline;
    private Integer sort;

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
    public Integer getFamilyId() {
		return familyId;
	}
	public void setFamilyId(Integer familyId) {
		this.familyId = familyId;
	}
    public String getMobileliveimg() {
        return mobileliveimg;
    }
    public void setMobileliveimg(String mobileliveimg) {
        this.mobileliveimg = mobileliveimg == null ? null : mobileliveimg.trim();
    }
    public Boolean getStatus() {
        return status;
    }
    public void setStatus(Boolean status) {
        this.status = status;
    }
    public Integer getOpentime() {
        return opentime;
    }
    public void setOpentime(Integer opentime) {
        this.opentime = opentime;
    }
    public Integer getRecommend() {
        return recommend;
    }
    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }
    public Integer getVideoline() {
        return videoline;
    }
    public void setVideoline(Integer videoline) {
        this.videoline = videoline;
    }
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
	@Override
	public LiveInfoModel populateFromResultSet(ResultSet rs) {

		try {
			this.id = rs.getInt("id");
			this.uid = rs.getInt("uid");
			this.familyId = rs.getInt("familyId");
			this.mobileliveimg = rs.getString("mobileliveimg");
			this.status = rs.getBoolean("status");
			this.opentime = rs.getInt("opentime");
			this.recommend = rs.getInt("recommend");
			this.videoline = rs.getInt("videoline");
			this.sort = rs.getInt("sort");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}
}