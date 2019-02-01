package com.mpig.api.reapal;

import java.io.Serializable;

/**
 * 融宝支付充值异步通知DTO
 */
public class RechargeNotifyDto implements Serializable {
	private static final long serialVersionUID = 2284734445303674075L;

	private String order_time;	// 通知时间
	private String order_no;	// 客户订单号 商户生成的唯一订单号
	private String trade_no;	// 融宝交易流水号 版本号传4.0.1时返回
	private String total_fee;	// 支付金额 单位：分
	private String status;		// 状态 TRADE_FAILURE支付失败 TRADE_FINISHED支付成功
	private String result_code;	// 错误码
	private String result_msg;	// 错误内容
	private String notify_id;	// 通知ID
	private String sign;		// 签名

	public String getOrder_time() {
		return order_time;
	}

	public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getResult_msg() {
		return result_msg;
	}

	public void setResult_msg(String result_msg) {
		this.result_msg = result_msg;
	}

	public String getNotify_id() {
		return notify_id;
	}

	public void setNotify_id(String notify_id) {
		this.notify_id = notify_id;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
