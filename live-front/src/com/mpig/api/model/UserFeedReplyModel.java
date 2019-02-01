package com.mpig.api.model;

import java.sql.ResultSet;

/**
 * 动态回复相关
 * @author zyl
 * @date 2016-12-6 下午6:25:08
 */
public class UserFeedReplyModel implements PopulateTemplate<UserFeedReplyModel>{

	private Integer id;
	private Integer feedid; //动态id
	private Integer fromUid; //回复人id
	private String fromNickName;
	private String fromHeadImage;
	private boolean fromSex;
	private Integer fromUserLevel;
	private Integer toUid; //被回复人id
	private String toNickName;
	private String content; //回复内容
	private Integer createAt; //发布时间
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFromUid() {
		return fromUid;
	}

	public void setFromUid(Integer fromUid) {
		this.fromUid = fromUid;
	}

	public Integer getToUid() {
		return toUid;
	}

	public void setToUid(Integer toUid) {
		this.toUid = toUid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getFromNickName() {
		return fromNickName;
	}

	public void setFromNickName(String fromNickName) {
		this.fromNickName = fromNickName;
	}

	public String getFromHeadImage() {
		return fromHeadImage;
	}

	public void setFromHeadImage(String fromHeadImage) {
		this.fromHeadImage = fromHeadImage;
	}

	public boolean isFromSex() {
		return fromSex;
	}

	public void setFromSex(boolean fromSex) {
		this.fromSex = fromSex;
	}
	
	public Integer getFromUserLevel() {
		return fromUserLevel;
	}

	public void setFromUserLevel(Integer fromUserLevel) {
		this.fromUserLevel = fromUserLevel;
	}

	public String getToNickName() {
		return toNickName;
	}

	public void setToNickName(String toNickName) {
		this.toNickName = toNickName;
	}

	@Override
	public UserFeedReplyModel populateFromResultSet(ResultSet rs) {
		try {
			this.id = rs.getInt("id");
			this.feedid = rs.getInt("feedid");
			this.fromUid = rs.getInt("fromUid");
			this.toUid = rs.getInt("toUid");
			this.content = rs.getString("content");
			this.createAt = rs.getInt("createAt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
}
