package com.mpig.api.model;

import java.sql.ResultSet;

public class UserFeedRewardModel implements PopulateTemplate<UserFeedRewardModel>{

	private Integer id;
	private Integer feedId;
	private Integer feedCUid;
	private Integer rewardUid;
	private Integer zhutou;
	private Integer createAt;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getFeedId() {
		return feedId;
	}
	public void setFeedId(Integer feedId) {
		this.feedId = feedId;
	}
	public Integer getFeedCUid() {
		return feedCUid;
	}
	public void setFeedCUid(Integer feedCUid) {
		this.feedCUid = feedCUid;
	}
	public Integer getRewardUid() {
		return rewardUid;
	}
	public void setRewardUid(Integer rewardUid) {
		this.rewardUid = rewardUid;
	}
	public Integer getZhutou() {
		return zhutou;
	}
	public void setZhutou(Integer zhutou) {
		this.zhutou = zhutou;
	}
	public Integer getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Integer createAt) {
		this.createAt = createAt;
	}
	@Override
	public UserFeedRewardModel populateFromResultSet(ResultSet rs) {
		try {
			this.id = rs.getInt("id");
			this.feedId = rs.getInt("feedId");
			this.feedCUid = rs.getInt("feedCUid");
			this.rewardUid = rs.getInt("rewardUid");
			this.zhutou = rs.getInt("zhutou");
			this.createAt = rs.getInt("createAt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
}
