package com.tinypig.newadmin.web.entity;

import com.tinypig.newadmin.common.BaseModel;

public class SystemNotice extends BaseModel{
    private Byte id;

    private String content;

    private Integer utime;

    public Byte getId() {
        return id;
    }

    public void setId(Byte id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getUtime() {
        return utime;
    }

    public void setUtime(Integer utime) {
        this.utime = utime;
    }
}