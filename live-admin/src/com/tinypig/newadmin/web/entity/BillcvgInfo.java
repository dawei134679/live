package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class BillcvgInfo implements Serializable{
    
	private static final long serialVersionUID = -6757056695845659447L;

	private Integer id;

    private Integer uid;

    private Integer anchorid;

    private Integer gid;

    private String gname;

    private Integer count;

    private Integer addtime;

    private Integer starttime;

    private Integer endtime;

    private Integer type;

    private Integer realpricetotal;

    private Integer gstatus;

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

    public Integer getAnchorid() {
        return anchorid;
    }

    public void setAnchorid(Integer anchorid) {
        this.anchorid = anchorid;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname == null ? null : gname.trim();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getAddtime() {
        return addtime;
    }

    public void setAddtime(Integer addtime) {
        this.addtime = addtime;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

	public Integer getRealpricetotal() {
		return realpricetotal;
	}

	public void setRealpricetotal(Integer realpricetotal) {
		this.realpricetotal = realpricetotal;
	}

	public Integer getGstatus() {
		return gstatus;
	}

	public void setGstatus(Integer gstatus) {
		this.gstatus = gstatus;
	}
}