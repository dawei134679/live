package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class RoomGameManagement implements Serializable{

	private static final long serialVersionUID = 7217475364963496623L;

	private Long id;

	private String gameKey;
	
    private String name;

    private Integer type;

    private Integer status;

    private String initUrl;

    private String pageUrl;

    private String imgUrl;

    private String destoryUrl;
    
    private String serverUrl;
    
    private String gameIconUrl;

    private Double gameCommission;

    private Long createTime;

    private Long createUserId;

    private Long updateTime;
    
    private Long updateUserId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGameKey() {
		return gameKey;
	}

	public void setGameKey(String gameKey) {
		this.gameKey = gameKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getInitUrl() {
		return initUrl;
	}

	public void setInitUrl(String initUrl) {
		this.initUrl = initUrl;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getDestoryUrl() {
		return destoryUrl;
	}

	public void setDestoryUrl(String destoryUrl) {
		this.destoryUrl = destoryUrl;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getGameIconUrl() {
		return gameIconUrl;
	}

	public void setGameIconUrl(String gameIconUrl) {
		this.gameIconUrl = gameIconUrl;
	}

	public Double getGameCommission() {
		return gameCommission;
	}

	public void setGameCommission(Double gameCommission) {
		this.gameCommission = gameCommission;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}
}