package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.GiftHistoryStaDto;

public interface IGiftStaService {

	Map<String, Object> giftHistorySta(GiftHistoryStaDto giftHistorySta);

	List<Map<String, Object>> getGiftTotal(GiftHistoryStaDto giftHistorySta);

	List<Map<String, Object>> getAllHistorySta(GiftHistoryStaDto giftHistorySta);
}
