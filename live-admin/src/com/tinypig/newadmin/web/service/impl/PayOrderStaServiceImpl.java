package com.tinypig.newadmin.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinypig.newadmin.web.dao.PayOrderStaDao;
import com.tinypig.newadmin.web.entity.PayOrderSta;
import com.tinypig.newadmin.web.entity.PayOrderStaParam;
import com.tinypig.newadmin.web.service.PayOrderStaService;

@Service
public class PayOrderStaServiceImpl implements PayOrderStaService {

	@Autowired
	private PayOrderStaDao payOrderStaDao;
	
	@Override
	public Map<String, Object> getPayOrderListPage(PayOrderStaParam param) {
		Map<String, Object> resultmap = new HashMap<String, Object>();
		
		List<PayOrderSta> list = payOrderStaDao.getPayOrderListPage(param);
		resultmap.put("rows", list);
		resultmap.put("total", payOrderStaDao.getPayOrderTotal(param));
		return resultmap;
	}

	@Override
	public Double getPayOrderTotalAmount(PayOrderStaParam param) {
		return payOrderStaDao.getPayOrderTotalAmount(param);
	}

	@Override
	public List<PayOrderSta> getPayOrderList(PayOrderStaParam param) {
		
		List<PayOrderSta> list = payOrderStaDao.getPayOrderList(param);
		return list;
	}
}
