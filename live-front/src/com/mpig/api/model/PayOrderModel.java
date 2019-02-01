package com.mpig.api.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PayOrderModel implements PopulateTemplate<PayOrderModel> {
    private String orderId;

    private Integer srcuid;

    private Integer dstuid;

    private Double amount;

    private Integer creatat;

    private Integer paytime;

    private Integer os;

    private String paytype;

    private Integer status;

    private String inpourNo;

    private String details;
    
    private String userSource;

    public String getUserSource() {
		return userSource;
	}

	public void setUserSource(String userSource) {
		this.userSource = userSource;
	}

	public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public Integer getSrcuid() {
        return srcuid;
    }

    public void setSrcuid(Integer srcuid) {
        this.srcuid = srcuid;
    }

    public Integer getDstuid() {
        return dstuid;
    }

    public void setDstuid(Integer dstuid) {
        this.dstuid = dstuid;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getCreatat() {
        return creatat;
    }

    public void setCreatat(Integer creatat) {
        this.creatat = creatat;
    }

    public Integer getPaytime() {
        return paytime;
    }

    public void setPaytime(Integer paytime) {
        this.paytime = paytime;
    }

    public Integer getOs() {
        return os;
    }

    public void setOs(Integer os) {
        this.os = os;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype == null ? null : paytype.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getInpourNo() {
        return inpourNo;
    }

    public void setInpourNo(String inpourNo) {
        this.inpourNo = inpourNo == null ? null : inpourNo.trim();
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details == null ? null : details.trim();
    }

    @Override
    public PayOrderModel populateFromResultSet(ResultSet rs) {
        try {
            this.orderId = rs.getString("order_id");
            this.srcuid = rs.getInt("srcuid");
            this.dstuid = rs.getInt("dstuid");
            this.amount = rs.getDouble("amount");
            this.creatat = rs.getInt("creatAt");
            this.paytime = rs.getInt("paytime");
            this.os = rs.getInt("os");
            this.paytype = rs.getString("paytype");
            this.status = rs.getInt("status");
            this.inpourNo = rs.getString("inpour_no");
            this.details = rs.getString("details");
            this.userSource = rs.getString("userSource");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }
}