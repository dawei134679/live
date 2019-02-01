package com.tinypig.newadmin.web.dao;

import java.util.List;

import com.tinypig.newadmin.web.entity.PayOrderSta;
import com.tinypig.newadmin.web.entity.PayOrderStaParam;

public interface PayOrderStaDao {

	 List<PayOrderSta> getPayOrderListPage(PayOrderStaParam param);
	
	 Integer getPayOrderTotal(PayOrderStaParam param);
	 
	 Double getPayOrderTotalAmount(PayOrderStaParam param);
	 
	 List<PayOrderSta> getPayOrderList(PayOrderStaParam param);
}
