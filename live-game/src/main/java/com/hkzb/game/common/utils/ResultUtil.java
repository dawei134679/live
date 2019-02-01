package com.hkzb.game.common.utils;

import com.hkzb.game.dto.ResultDto;

public class ResultUtil {

	public static final String SUCCESS_CODE = "200";
	public static final String SUCCESS_MSG = "处理成功";

	public static final String FAIL_CODE = "201";
	public static final String FAIL_MSG = "处理失败";

	public static final String ERROR_CODE = "203";
	public static final String ERROR_MSG = "系统异常";

	public static <T> ResultDto<T> success() {
		ResultDto<T> result = new ResultDto<T>(SUCCESS_CODE, SUCCESS_MSG);
		return result;
	}

	public static <T> ResultDto<T> success(String msg) {
		ResultDto<T> result = new ResultDto<T>(SUCCESS_CODE, msg);
		return result;
	}

	public static <T> ResultDto<T> success(T data) {
		ResultDto<T> result = new ResultDto<T>(SUCCESS_CODE, SUCCESS_MSG, data);
		return result;
	}

	public static <T> ResultDto<T> success(String msg, T data) {
		ResultDto<T> result = new ResultDto<T>(SUCCESS_CODE, msg, data);
		return result;
	}

	public static <T> ResultDto<T> fail() {
		ResultDto<T> result = new ResultDto<T>(FAIL_CODE, FAIL_MSG);
		return result;
	}

	public static <T> ResultDto<T> fail(String msg) {
		ResultDto<T> result = new ResultDto<T>(FAIL_CODE, msg);
		return result;
	}

	public static <T> ResultDto<T> fail(String code, String msg) {
		ResultDto<T> result = new ResultDto<T>(code, msg);
		return result;
	}

	public static <T> ResultDto<T> error() {
		ResultDto<T> result = new ResultDto<T>(ERROR_CODE, ERROR_MSG);
		return result;
	}

	public static <T> ResultDto<T> error(String msg) {
		ResultDto<T> result = new ResultDto<T>(ERROR_CODE, msg);
		return result;
	}

	public static <T> ResultDto<T> error(String code, String msg) {
		ResultDto<T> result = new ResultDto<T>(code, msg);
		return result;
	}
	
	public static <T> ResultDto<T> result(String code, String msg) {
		ResultDto<T> result = new ResultDto<T>(code, msg);
		return result;
	} 
}