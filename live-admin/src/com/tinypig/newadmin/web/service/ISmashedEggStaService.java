package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.SmashedEggStaDto;

public interface ISmashedEggStaService {

	Map<String, Object> smashedEggList(SmashedEggStaDto smashedEggStaDto);

	List<Map<String, Object>> getSmashedEggTotal(SmashedEggStaDto smashedEggStaDto);

}
