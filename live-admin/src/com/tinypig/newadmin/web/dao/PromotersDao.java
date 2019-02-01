package com.tinypig.newadmin.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tinypig.newadmin.web.entity.Promoters;
import com.tinypig.newadmin.web.entity.PromotersDto;
import com.tinypig.newadmin.web.entity.PromotersParamDto;

public interface PromotersDao {
	/**
	 * 逻辑删除
	 */
	int deleteByPrimaryKey(Long id);

	int insert(Promoters record);

	int insertSelective(Promoters record);

	Promoters selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(Promoters record);

	int updateByPrimaryKey(Promoters record);

	Promoters getPromotersByPhone(@Param("phone") String phone);

	int countPromotersByExtensionCenterId(@Param("extensionCenterId") Long extensionCenterId);

	List<PromotersDto> getPromotersListPage(PromotersParamDto params);

	Integer getPromotersTotal(PromotersParamDto params);

	int updateParentId(Long extensionCenterId);

	void updateParentsByExtensionCenter(@Param("strategicPartnerId") Long strategicPartnerId, @Param("extensionCenterId") Long extensionCenterId);


}