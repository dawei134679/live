package com.tinypig.newadmin.web.entity;

public class WebFeedback {
    private Integer id;

    private Integer uid;

    private Integer cls;

    private String des;

    private String mobile;

    private Integer createat;

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

    public Integer getCls() {
        return cls;
    }

    public void setCls(Integer cls) {
        this.cls = cls;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des == null ? null : des.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Integer getCreateat() {
        return createat;
    }

    public void setCreateat(Integer createat) {
        this.createat = createat;
    }
}