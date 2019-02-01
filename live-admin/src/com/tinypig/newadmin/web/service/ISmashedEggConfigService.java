package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

public interface ISmashedEggConfigService {

	Map<String, Object> reSmashedEggConfigRedis();

	List<Map<String, Object>> getSmashedEggMoneyConfig();

	int saveSmashedEggConfig(Long id, Long smashed1money, Long smashed2money, Long smashed3money);

}
