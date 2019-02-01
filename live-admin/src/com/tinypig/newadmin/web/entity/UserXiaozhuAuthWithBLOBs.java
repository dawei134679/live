package com.tinypig.newadmin.web.entity;

public class UserXiaozhuAuthWithBLOBs extends UserXiaozhuAuth {
    private String authpics;

    private String authurls;

    public String getAuthpics() {
        return authpics;
    }

    public void setAuthpics(String authpics) {
        this.authpics = authpics == null ? null : authpics.trim();
    }

    public String getAuthurls() {
        return authurls;
    }

    public void setAuthurls(String authurls) {
        this.authurls = authurls == null ? null : authurls.trim();
    }
}