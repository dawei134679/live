package com.tinypig.newadmin.web.dao;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.SmashedEggStaDto;
import com.tinypig.newadmin.web.vo.SmashedEggVo;

public interface SmashedEggStaDao {

	List<SmashedEggVo> smashedEggSta(SmashedEggStaDto smashedEggStaDto);

	/**
	 * 总数
	 */
	Long smashedEggCount(SmashedEggStaDto smashedEggStaDto);

	/**
	 * 合计总数
	 */
	List<Map<String, Object>> smashedEggTotalCount(SmashedEggStaDto smashedEggStaDto);

}
