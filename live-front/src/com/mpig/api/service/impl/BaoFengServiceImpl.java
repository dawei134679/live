package com.mpig.api.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mpig.api.dictionary.lib.BaoFengLivingListConfigLib;
import com.mpig.api.service.IBaoFengService;

@Service
public class BaoFengServiceImpl implements IBaoFengService{

	
	public List<Map<String, Object>> getLiveListForBF(int page){
		return BaoFengLivingListConfigLib.getLivingList(page);
	}
}
