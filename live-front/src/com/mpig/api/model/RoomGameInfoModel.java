package com.mpig.api.model;

import java.io.Serializable;

public class RoomGameInfoModel implements Serializable{

	private static final long serialVersionUID = 8183533483384997609L;

	private Long gameId;

	private String gameKey;
	
    private String name;

    private Integer type;
    
    private String initUrl;

    private String pageUrl;

    private String imgUrl;

    private String destoryUrl;
    
    private String serverUrl;
    
    private String gameIconUrl;

    private Double gameCommission;

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
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
}