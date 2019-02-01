package com.mpig.api.service;

import java.util.List;
import java.util.Map;

public interface IBaoFengService {
	
	/**
	 * 获取暴风移动端的列表
	 * @param page
	 * @return
	 */
	List<Map<String, Object>> getLiveListForBF(int page);
	
}
