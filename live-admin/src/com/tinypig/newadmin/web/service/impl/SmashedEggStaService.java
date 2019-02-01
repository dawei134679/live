package com.tinypig.newadmin.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinypig.newadmin.web.dao.SmashedEggStaDao;
import com.tinypig.newadmin.web.entity.SmashedEggStaDto;
import com.tinypig.newadmin.web.service.ISmashedEggStaService;
import com.tinypig.newadmin.web.vo.SmashedEggVo;

@Service
public class SmashedEggStaService implements ISmashedEggStaService {

	@Autowired
	private SmashedEggStaDao smashedEggStaDao;

	@Override
	public Map<String, Object> smashedEggList(SmashedEggStaDto smashedEggStaDto) {
		Integer startIndex = (smashedEggStaDto.getPage() - 1) * smashedEggStaDto.getRows();
		smashedEggStaDto.setStartIndex(startIndex);
		smashedEggStaDto.setPageSize(smashedEggStaDto.getRows());
		Map<String, Object> map = new HashMap<String, Object>();
		List<SmashedEggVo> list = smashedEggStaDao.smashedEggSta(smashedEggStaDto);
		Long total = smashedEggStaDao.smashedEggCount(smashedEggStaDto);
		map.put("total", total);
		map.put("rows", list);
		return map;
	}

	@Override
	public List<Map<String, Object>> getSmashedEggTotal(SmashedEggStaDto smashedEggStaDto) {
		return smashedEggStaDao.smashedEggTotalCount(smashedEggStaDto);
	}

}
