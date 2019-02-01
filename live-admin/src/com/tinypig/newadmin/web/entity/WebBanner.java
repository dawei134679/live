package com.tinypig.newadmin.web.entity;

import com.tinypig.newadmin.common.BaseModel;

public class WebBanner extends BaseModel{
    private Integer id;

    private String picurl;
    
    private String webPicUrl;

    private String jumpurl;
    
    private Integer anchorUid;
    
    private Integer roomId;

    public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public String getWebPicUrl() {
		return webPicUrl;
	}

	public void setWebPicUrl(String webPicUrl) {
		this.webPicUrl = webPicUrl;
	}

	public Integer getAnchorUid() {
		return anchorUid;
	}

	public void setAnchorUid(Integer anchorUid) {
		this.anchorUid = anchorUid;
	}

	private Integer sort;

    private Long startshow;

    private Long endshow;

    private Integer swi;

    private Integer createat;

    private String name;

    private Integer type;
    
    private Integer platform;
    

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl == null ? null : picurl.trim();
    }

    public String getJumpurl() {
        return jumpurl;
    }

    public void setJumpurl(String jumpurl) {
        this.jumpurl = jumpurl == null ? null : jumpurl.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getStartshow() {
		return startshow;
	}

	public void setStartshow(Long startshow) {
		this.startshow = startshow;
	}

	public Long getEndshow() {
		return endshow;
	}

	public void setEndshow(Long endshow) {
		this.endshow = endshow;
	}

	public Integer getSwi() {
		return swi;
	}

	public void setSwi(Integer swi) {
		this.swi = swi;
	}

	public Integer getCreateat() {
        return createat;
    }

    public void setCreateat(Integer createat) {
        this.createat = createat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    
    public Integer getPlatform() {
		return platform;
	}

	public void setPlatform(Integer platform) {
		this.platform = platform;
	}
}