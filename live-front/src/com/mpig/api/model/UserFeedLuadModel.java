package com.mpig.api.model;

import java.sql.ResultSet;

/**
 * 动态点赞相关
 * @author zyl
 * @date 2016-12-6 下午6:25:29
 */
public class UserFeedLuadModel implements PopulateTemplate<UserFeedLuadModel>{

	private Integer uid; //用户id
	private Integer feedid; //动态id
	private Integer createAt; //发布时间
	

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getFeedid() {
		return feedid;
	}

	public void setFeedid(Integer feedid) {
		this.feedid = feedid;
	}

	public Integer getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Integer createAt) {
		this.createAt = createAt;
	}

	@Override
	public UserFeedLuadModel populateFromResultSet(ResultSet rs) {
		try {
			this.uid = rs.getInt("uid");
			this.feedid = rs.getInt("feedid");
			this.createAt = rs.getInt("createAt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
}
