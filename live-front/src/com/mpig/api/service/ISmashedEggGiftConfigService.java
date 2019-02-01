package com.mpig.api.service;

import java.util.List;
import java.util.Map;

public interface ISmashedEggGiftConfigService {

	/**
	 * 获取头奖信息+锤子金币信息
	 * @return [{giftname:'跑车',price:'123'},{giftname:'跑车',price:'123'},{giftname:'跑车',price:'123'}]
	 */
	List<Map<String, Object>> getSmashedEggFirstPrize();

}
