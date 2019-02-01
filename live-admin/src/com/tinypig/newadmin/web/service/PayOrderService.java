package com.tinypig.newadmin.web.service;

import com.tinypig.newadmin.web.entity.PayOrder;

public interface PayOrderService {

	public int savePayOrder(PayOrder payOrder);

	public int delPayOrder(PayOrder payOrder);
}
