package com.mpig.api.model;

public class ReturnModel {

	// 返回操作码
	private int code = 200; 
	// 返回
	private String message = "操作成功";	
	// 返回数据
	private Object data;	

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ReturnModel [code=" + code + ", message=" + message + ", data=" + data + "]";
	}
	
}
