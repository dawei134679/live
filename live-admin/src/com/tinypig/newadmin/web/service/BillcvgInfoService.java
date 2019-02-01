package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.vo.BillcvgParamVo;

public interface BillcvgInfoService {
	Map<String, Object> getcvgStaList(BillcvgParamVo billcvgParamVo);

	List<Map<String, Object>> getAllCvgStaList(BillcvgParamVo param);
}
