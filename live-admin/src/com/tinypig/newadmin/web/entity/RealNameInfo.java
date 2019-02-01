package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class RealNameInfo implements Serializable{
	
	private static final long serialVersionUID = -2973356230144174958L;

	private Integer id;

    private Integer uid;

    private String realname;

    private String cardid;

    private String cardno;

    private String bankaccount;

    private String provinceofbank;

    private String cityofbank;

    private String branchbank;

    private String positiveimage;

    private String negativeimage;

    private String handimage;

    private Integer auditstatus;

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

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname == null ? null : realname.trim();
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid == null ? null : cardid.trim();
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno == null ? null : cardno.trim();
    }

    public String getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(String bankaccount) {
        this.bankaccount = bankaccount == null ? null : bankaccount.trim();
    }

    public String getProvinceofbank() {
        return provinceofbank;
    }

    public void setProvinceofbank(String provinceofbank) {
        this.provinceofbank = provinceofbank == null ? null : provinceofbank.trim();
    }

    public String getCityofbank() {
        return cityofbank;
    }

    public void setCityofbank(String cityofbank) {
        this.cityofbank = cityofbank == null ? null : cityofbank.trim();
    }

    public String getBranchbank() {
        return branchbank;
    }

    public void setBranchbank(String branchbank) {
        this.branchbank = branchbank == null ? null : branchbank.trim();
    }

    public String getPositiveimage() {
        return positiveimage;
    }

    public void setPositiveimage(String positiveimage) {
        this.positiveimage = positiveimage == null ? null : positiveimage.trim();
    }

    public String getNegativeimage() {
        return negativeimage;
    }

    public void setNegativeimage(String negativeimage) {
        this.negativeimage = negativeimage == null ? null : negativeimage.trim();
    }

    public String getHandimage() {
        return handimage;
    }

    public void setHandimage(String handimage) {
        this.handimage = handimage == null ? null : handimage.trim();
    }

    public Integer getAuditstatus() {
        return auditstatus;
    }

    public void setAuditstatus(Integer auditstatus) {
        this.auditstatus = auditstatus;
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