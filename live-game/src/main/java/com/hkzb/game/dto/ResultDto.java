package com.hkzb.game.dto;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.hkzb.game.common.utils.DateUtil;

public class ResultDto<T> implements Serializable {

	private static final long serialVersionUID = 2150861474997325836L;

	@JSONField(ordinal=1)
	private String code;
	
	@JSONField(ordinal=2)
	private String msg;
	
	@JSONField(ordinal=3)
	private Long operateTime;
	
	@JSONField(ordinal=4)
	private T data;

	public ResultDto(String code, String msg) {
		this.code = code;
		this.msg = msg;
		this.operateTime = DateUtil.nowTime();
	}

	public ResultDto(String code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
		this.operateTime = DateUtil.nowTime();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Long getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Long operateTime) {
		this.operateTime = operateTime;
	}
}