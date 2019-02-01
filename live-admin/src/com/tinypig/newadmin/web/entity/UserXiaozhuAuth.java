package com.tinypig.newadmin.web.entity;

import com.tinypig.newadmin.common.BaseModel;

public class UserXiaozhuAuth extends BaseModel{
    private Integer id;

    private Integer uid;

    private String nickname;

    private String authcontent;

    private Integer status;

    private String cause;

    private Integer createat;

    private Integer auditat;

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getAuthcontent() {
        return authcontent;
    }

    public void setAuthcontent(String authcontent) {
        this.authcontent = authcontent == null ? null : authcontent.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause == null ? null : cause.trim();
    }

    public Integer getCreateat() {
        return createat;
    }

    public void setCreateat(Integer createat) {
        this.createat = createat;
    }

    public Integer getAuditat() {
        return auditat;
    }

    public void setAuditat(Integer auditat) {
        this.auditat = auditat;
    }
}