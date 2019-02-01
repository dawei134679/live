package com.mpig.api.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PayAccountModel implements PopulateTemplate<PayAccountModel> {
    private Integer uid;
    private String wx_openid;
    private String wx_unionid;

    private String alipay;
    private Integer isUse;
    private Integer createat;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }


    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay == null ? null : alipay.trim();
    }

    public Integer getIsUse() {
        return isUse;
    }

    public void setIsUse(Integer isUse) {
        this.isUse = isUse;
    }

    public Integer getCreateat() {
        return createat;
    }

    public void setCreateat(Integer createat) {
        this.createat = createat;
    }

    @Override
    public PayAccountModel populateFromResultSet(ResultSet rs) {
        try {
            this.uid = rs.getInt("uid");
            this.wx_openid = rs.getString("wx_openid");
            this.wx_unionid = rs.getString("wx_unionid");
            this.alipay = rs.getString("alipay");
            this.createat = rs.getInt("createAt");
            this.isUse = rs.getInt("isUse");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return this;
    }

    public String getWx_openid() {
        return wx_openid;
    }

    public void setWx_openid(String wx_openid) {
        this.wx_openid = wx_openid;
    }

    public String getWx_unionid() {
        return wx_unionid;
    }

    public void setWx_unionid(String wx_unionid) {
        this.wx_unionid = wx_unionid;
    }
}