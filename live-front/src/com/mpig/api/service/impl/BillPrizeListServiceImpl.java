package com.mpig.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mpig.api.dao.IBillPrizeListDao;
import com.mpig.api.model.BillPrizeListModel;
import com.mpig.api.service.BillPrizeListService;

@Service
public class BillPrizeListServiceImpl implements BillPrizeListService{
	@Autowired
	private IBillPrizeListDao billPrizeListDao;
	@Override
	public List<BillPrizeListModel> getListByUser(int uid) {
		return billPrizeListDao.getListByUser(uid);
	}

	
}
