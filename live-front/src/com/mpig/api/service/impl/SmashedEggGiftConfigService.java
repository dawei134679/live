package com.mpig.api.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.mpig.api.SqlTemplete;
import com.mpig.api.model.SmashedEggGiftConfigModel;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.service.ISmashedEggGiftConfigService;
import com.mpig.api.utils.JsonUtil;
import com.mpig.api.utils.RedisContant;

@Service
public class SmashedEggGiftConfigService implements ISmashedEggGiftConfigService, SqlTemplete {
	//private static final Logger logger = Logger.getLogger(SmashedEggGiftConfigService.class);

	/**
	 * 获取头奖信息+锤子金币信息
	 * @return [{giftname:'跑车',price:'123'},{giftname:'跑车',price:'123'},{giftname:'跑车',price:'123'}]
	 */
	@Override
	public List<Map<String, Object>> getSmashedEggFirstPrize() {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		int i = 1;
		while(i <= 3) {
			String smash = OtherRedisService.getInstance().hget(RedisContant.SmashedEggGiftCfgList, ""+i);//礼物列表
			String smashMoneys = OtherRedisService.getInstance().hget(RedisContant.SmashedEggMoneyCfgList, ""+i);//金额数据
			Map<String, Object> m = new HashMap<String,Object>();
			String giftName = "";//头奖礼物名称
			if(StringUtils.isNotBlank(smash)) {
				List<SmashedEggGiftConfigModel> smashs = JsonUtil.toListBean(smash, SmashedEggGiftConfigModel.class);
				if(null != smashs && smashs.size() > 0) {
					for (SmashedEggGiftConfigModel smashedEggGiftConfigModel : smashs) {
						if(smashedEggGiftConfigModel.getIsfirstprize() == 1) {//头奖
							giftName = smashedEggGiftConfigModel.getGiftName();
							break;
						}
					}
				}
			}
			m.put("giftname",giftName);
			m.put("price", smashMoneys);
			result.add(m);
			i++;
		}
		return result;
	}

	
}
