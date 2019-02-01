package com.mpig.api.reapal;

import java.io.Serializable;

/**
 * 融宝支付退款异步通知DTO
 */
public class RefundNotifyDto implements Serializable {
	private static final long serialVersionUID = 3455096792942824388L;

	private String merchant_id; // 商户ID 商户在融宝的用户ID
	private String order_no; // 客户订单号 商户生成的唯一订单号
	private String refund_order_no; // 商户退款订单号 商户退款订单号
	private String refund_id; // 退款订单号 退款订单号
	private String amount; // 退款金额 退款金额 以"分"为单位
	private String status; // 退款状态 completed – 退款完成 failed – 退款失败
	private String product_type; // 产品类型
	private String notify_id; // 通知ID 通知编号
	private String sign_type; // 签名类型 目前仅支持MD5
	private String sign; // 签名 按照sign_type参数指定的签名算法对待签名数据进行签名。目前仅支持MD5.详见数字签名

	public String getMerchant_id() {
		return merchant_id;
	}

	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getRefund_order_no() {
		return refund_order_no;
	}

	public void setRefund_order_no(String refund_order_no) {
		this.refund_order_no = refund_order_no;
	}

	public String getRefund_id() {
		return refund_id;
	}

	public void setRefund_id(String refund_id) {
		this.refund_id = refund_id;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProduct_type() {
		return product_type;
	}

	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}

	public String getNotify_id() {
		return notify_id;
	}

	public void setNotify_id(String notify_id) {
		this.notify_id = notify_id;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
