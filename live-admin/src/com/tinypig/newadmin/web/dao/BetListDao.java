package com.tinypig.newadmin.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tinypig.newadmin.web.entity.BetRecord;

public interface BetListDao {
	
	List<BetRecord> getBetList(@Param("periods") Long periods, @Param("anchorId") Long anchorId,@Param("uid") Long uid,
								   @Param("startIndex") Integer startIndex, @Param("rows") Integer rows);
	
	BetRecord getBetTotal(@Param("periods") Long periods, @Param("anchorId") Long anchorId,@Param("uid") Long uid);
	
	Long getBetTotalCount(@Param("periods") Long periods, @Param("anchorId") Long anchorId,@Param("uid") Long uid);
}
