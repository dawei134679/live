package com.tinypig.newadmin.web.entity;

public class WebVer {
    private String iosver;

    private Integer iosat;

    private String androidver;

    private Integer androidat;

    public String getIosver() {
        return iosver;
    }

    public void setIosver(String iosver) {
        this.iosver = iosver == null ? null : iosver.trim();
    }

    public Integer getIosat() {
        return iosat;
    }

    public void setIosat(Integer iosat) {
        this.iosat = iosat;
    }

    public String getAndroidver() {
        return androidver;
    }

    public void setAndroidver(String androidver) {
        this.androidver = androidver == null ? null : androidver.trim();
    }

    public Integer getAndroidat() {
        return androidat;
    }

    public void setAndroidat(Integer androidat) {
        this.androidat = androidat;
    }
}