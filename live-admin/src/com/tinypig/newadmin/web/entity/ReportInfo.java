package com.tinypig.newadmin.web.entity;

public class ReportInfo {
    private Long id;

    private Long uid;

    private String content;

    private Long rid;

    private Integer status;

    private Long createtime;

    private Long handletime;

    private String handlemark;

    private Long handleUid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Long createtime) {
        this.createtime = createtime;
    }

    public Long getHandletime() {
        return handletime;
    }

    public void setHandletime(Long handletime) {
        this.handletime = handletime;
    }

    public String getHandlemark() {
        return handlemark;
    }

    public void setHandlemark(String handlemark) {
        this.handlemark = handlemark == null ? null : handlemark.trim();
    }

    public Long getHandleUid() {
        return handleUid;
    }

    public void setHandleUid(Long handleUid) {
        this.handleUid = handleUid;
    }
}