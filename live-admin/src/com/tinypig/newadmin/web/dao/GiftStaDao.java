package com.tinypig.newadmin.web.dao;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.GiftHistoryStaDto;
import com.tinypig.newadmin.web.vo.GiftStaHistoryVo;

public interface GiftStaDao {

	List<GiftStaHistoryVo> giftHistorySta(GiftHistoryStaDto giftHistorySta);

	List<Map<String, Object>> getGiftTotal(GiftHistoryStaDto giftHistorySta);

	Long giftHistoryTotalCount(GiftHistoryStaDto giftHistorySta);

	List<Map<String, Object>> giftAllHistorySta(GiftHistoryStaDto giftHistorySta);

}
