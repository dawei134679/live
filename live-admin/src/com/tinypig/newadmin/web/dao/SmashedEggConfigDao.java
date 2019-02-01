package com.tinypig.newadmin.web.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface SmashedEggConfigDao {

	List<Map<String, Object>> getSmashedEggMoneyConfig();

	int insertSmashedEggConfig(@Param("smashed1money") Long smashed1money,@Param("smashed2money") Long smashed2money,@Param("smashed3money") Long smashed3money);

	int updateSmashedEggConfig(@Param("id")Long id,@Param("smashed1money") Long smashed1money,@Param("smashed2money") Long smashed2money,@Param("smashed3money") Long smashed3money);

}
