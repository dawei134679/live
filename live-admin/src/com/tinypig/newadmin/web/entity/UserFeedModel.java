package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

/**
 * 动态相关
 * @author zyl
 * @date 2016-12-6 下午6:25:19
 */
public class UserFeedModel implements Serializable {
	private static final long serialVersionUID = -593330004549088586L;

	private Integer id;
	private Integer uid; // 用户id
	private String content; // 动态内容
	private String imgs; // 动态图片
	private Integer createAt; // 发布时间
	private Integer status; // 0删除 1有效

	// VO
	private String nickname;
	private String headimage;
	private String city;
	private Boolean sex;
	private Boolean isLaud;
	private boolean verified;
	private Integer laudCount;
	private Integer replyCount;
	private Integer rewardCount;

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

	public String getContent() {
		return content;
	}

	public Boolean getIsLaud() {
		return isLaud;
	}

	public void setIsLaud(Boolean isLaud) {
		this.isLaud = isLaud;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadimage() {
		return headimage;
	}

	public Boolean getSex() {
		return sex;
	}

	public void setSex(Boolean sex) {
		this.sex = sex;
	}

	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public String getImgs() {
		return imgs;
	}

	public void setImgs(String imgs) {
		this.imgs = imgs;
	}

	public Integer getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Integer createAt) {
		this.createAt = createAt;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getLaudCount() {
		return laudCount;
	}

	public void setLaudCount(Integer laudCount) {
		this.laudCount = laudCount;
	}

	public Integer getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(Integer replyCount) {
		this.replyCount = replyCount;
	}

	public Integer getRewardCount() {
		return rewardCount;
	}

	public void setRewardCount(Integer rewardCount) {
		this.rewardCount = rewardCount;
	}
}
