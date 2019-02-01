package com.mpig.api.dictionary;

public class PayConfig {

    private String alipay_appid;
    private String alipay_submit;
    private String alipay_service;
    private String alipay_partner;
    private String alipay_sellerId;
    private String alipay_inputCharset;
    private String alipay_paymentType;
    private String alipay_notifyUrl;
    private String alipay_returnUrl;
    private String alipay_signType;
    private String alipay_privateKey;
    private String alipay_publicKey;
    private String alipay_verifyUrl;
    private String ipayNow_notifyUrl;
    private String ipayNow_appKey;
    private String ipayNow_signType;
    private String ipayNow_charset;
    private String weixin_appid;
    private String weixin_appKey;
    private String weixin_mchid;
    private String weixin_mchKey;
    private String apple_sandbox;
    private String apple_buy;
    private String pay_notifyUrl;

    public String getAlipay_appid() {
        return alipay_appid;
    }

    public void setAlipay_appid(String alipay_appid) {
        this.alipay_appid = alipay_appid;
    }

    public String getAlipay_submit() {
        return alipay_submit;
    }

    public void setAlipay_submit(String alipay_submit) {
        this.alipay_submit = alipay_submit;
    }

    public String getAlipay_service() {
        return alipay_service;
    }

    public void setAlipay_service(String alipay_service) {
        this.alipay_service = alipay_service;
    }

    public String getAlipay_partner() {
        return alipay_partner;
    }

    public void setAlipay_partner(String alipay_partner) {
        this.alipay_partner = alipay_partner;
    }

    public String getAlipay_sellerId() {
        return alipay_sellerId;
    }

    public void setAlipay_sellerId(String alipay_sellerId) {
        this.alipay_sellerId = alipay_sellerId;
    }

    public String getAlipay_inputCharset() {
        return alipay_inputCharset;
    }

    public void setAlipay_inputCharset(String alipay_inputCharset) {
        this.alipay_inputCharset = alipay_inputCharset;
    }

    public String getAlipay_paymentType() {
        return alipay_paymentType;
    }

    public void setAlipay_paymentType(String alipay_paymentType) {
        this.alipay_paymentType = alipay_paymentType;
    }

    public String getAlipay_notifyUrl() {
        return alipay_notifyUrl;
    }

    public void setAlipay_notifyUrl(String alipay_notifyUrl) {
        this.alipay_notifyUrl = alipay_notifyUrl;
    }

    public String getAlipay_returnUrl() {
        return alipay_returnUrl;
    }

    public void setAlipay_returnUrl(String alipay_returnUrl) {
        this.alipay_returnUrl = alipay_returnUrl;
    }

    public String getAlipay_signType() {
        return alipay_signType;
    }

    public void setAlipay_signType(String alipay_signType) {
        this.alipay_signType = alipay_signType;
    }

    public String getAlipay_privateKey() {
        return alipay_privateKey;
    }

    public void setAlipay_privateKey(String alipay_privateKey) {
        this.alipay_privateKey = alipay_privateKey;
    }

    public String getAlipay_publicKey() {
        return alipay_publicKey;
    }

    public void setAlipay_publicKey(String alipay_publicKey) {
        this.alipay_publicKey = alipay_publicKey;
    }

    public String getAlipay_verifyUrl() {
        return alipay_verifyUrl;
    }

    public void setAlipay_verifyUrl(String alipay_verifyUrl) {
        this.alipay_verifyUrl = alipay_verifyUrl;
    }

    public String getIpayNow_notifyUrl() {
        return ipayNow_notifyUrl;
    }

    public void setIpayNow_notifyUrl(String ipayNow_notiryUrl) {
        this.ipayNow_notifyUrl = ipayNow_notiryUrl;
    }

    public String getIpayNow_appKey() {
        return ipayNow_appKey;
    }

    public void setIpayNow_appKey(String ipayNow_appKey) {
        this.ipayNow_appKey = ipayNow_appKey;
    }

    public String getIpayNow_signType() {
        return ipayNow_signType;
    }

    public void setIpayNow_signType(String ipayNow_signType) {
        this.ipayNow_signType = ipayNow_signType;
    }

    public String getIpayNow_charset() {
        return ipayNow_charset;
    }

    public void setIpayNow_charset(String ipayNow_charset) {
        this.ipayNow_charset = ipayNow_charset;
    }

    public PayConfig initWith(String alipay_submit, String alipay_service,
                              String alipay_partner, String alipay_sellerId,
                              String alipay_inputCharset, String alipay_paymentType,
                              String alipay_notifyUrl, String alipay_returnUrl, String alipay_signType,
                              String alipay_privateKey, String alipay_publicKey, String alipay_verifyUrl, String ipayNow_notiryUrl,
                              String ipayNow_appKey, String ipayNow_signType, String ipayNow_charset, String alipay_appid,
                              String weixin_appid, String weixin_appKey, String mch_id, String mch_key, String apple_sandbox, String apple_buy,String pay_notifyUrl) {

        this.alipay_appid = alipay_appid;
        this.alipay_submit = alipay_submit;
        this.alipay_service = alipay_service;
        this.alipay_partner = alipay_partner;
        this.alipay_sellerId = alipay_sellerId;
        this.alipay_inputCharset = alipay_inputCharset;
        this.alipay_paymentType = alipay_paymentType;
        this.alipay_notifyUrl = alipay_notifyUrl;
        this.alipay_returnUrl = alipay_returnUrl;
        this.alipay_signType = alipay_signType;
        this.alipay_privateKey = alipay_privateKey;
        this.alipay_publicKey = alipay_publicKey;
        this.alipay_verifyUrl = alipay_verifyUrl;
        this.ipayNow_notifyUrl = ipayNow_notiryUrl;
        this.ipayNow_appKey = ipayNow_appKey;
        this.ipayNow_signType = ipayNow_signType;
        this.ipayNow_charset = ipayNow_charset;

        this.weixin_appid = weixin_appid;
        this.weixin_appKey = weixin_appKey;
        this.weixin_mchid = mch_id;
        this.weixin_mchKey = mch_key;
        this.apple_sandbox = apple_sandbox;
        this.apple_buy = apple_buy;
        this.pay_notifyUrl = pay_notifyUrl;
        return this;
    }

    public String getWeixin_appid() {
        return weixin_appid;
    }

    public void setWeixin_appid(String weixin_appid) {
        this.weixin_appid = weixin_appid;
    }

    public String getWeixin_appKey() {
        return weixin_appKey;
    }

    public void setWeixin_appKey(String weixin_appKey) {
        this.weixin_appKey = weixin_appKey;
    }

    public String getWeixin_mchid() {
        return weixin_mchid;
    }

    public void setWeixin_mchid(String weixin_mchid) {
        this.weixin_mchid = weixin_mchid;
    }

    public String getWeixin_mchKey() {
        return weixin_mchKey;
    }

    public void setWeixin_mchKey(String weixin_mchKey) {
        this.weixin_mchKey = weixin_mchKey;
    }

    public String getApple_sandbox() {
        return apple_sandbox;
    }

    public void setApple_sandbox(String apple_sandbox) {
        this.apple_sandbox = apple_sandbox;
    }

    public String getApple_buy() {
        return apple_buy;
    }

    public void setApple_buy(String apple_buy) {
        this.apple_buy = apple_buy;
    }

	public String getPay_notifyUrl() {
		return pay_notifyUrl;
	}

	public void setPay_notifyUrl(String pay_notifyUrl) {
		this.pay_notifyUrl = pay_notifyUrl;
	}
}
