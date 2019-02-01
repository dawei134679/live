package com.tinypig.newadmin.web.entity;

public class SupportUser {
    private Long id;

    private Integer uid;

    private String notes;

    private Integer status;

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

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes == null ? null : notes.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

	@Override
	public String toString() {
		return "SupportUser [id=" + id + ", uid=" + uid + ", notes=" + notes + ", status=" + status + ", createTime="
				+ createTime + ", createUserId=" + createUserId + ", updateTime=" + updateTime + ", updateUserId="
				+ updateUserId + "]";
	}
    
}