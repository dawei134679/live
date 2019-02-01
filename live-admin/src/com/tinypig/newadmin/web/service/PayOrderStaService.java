package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.PayOrderSta;
import com.tinypig.newadmin.web.entity.PayOrderStaParam;

public interface PayOrderStaService {
	
	public Map<String, Object> getPayOrderListPage(PayOrderStaParam param);
	
	public Double getPayOrderTotalAmount(PayOrderStaParam param);
	
	public List<PayOrderSta> getPayOrderList(PayOrderStaParam param);
}
