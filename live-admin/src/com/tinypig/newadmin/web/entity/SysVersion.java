package com.tinypig.newadmin.web.entity;

public class SysVersion {
    private Integer id;

    private Integer sys;

    private String ver;

    private Integer isforce;

    private String describtion;

    private String uploadUrl;

    private Integer updateTime;

    private Long updateUserId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSys() {
        return sys;
    }

    public void setSys(Integer sys) {
        this.sys = sys;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver == null ? null : ver.trim();
    }

    public Integer getIsforce() {
        return isforce;
    }

    public void setIsforce(Integer isforce) {
        this.isforce = isforce;
    }

    public String getDescribtion() {
        return describtion;
    }

    public void setDescribtion(String describtion) {
        this.describtion = describtion == null ? null : describtion.trim();
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl == null ? null : uploadUrl.trim();
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }
}