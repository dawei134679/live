package com.tinypig.newadmin.web.dao;

import com.tinypig.newadmin.web.entity.PayOrder;

public interface PayOrderMapper {
    int deleteByPrimaryKey(String orderId);

    int insert(PayOrder record);

    int insertSelective(PayOrder record);

    PayOrder selectByPrimaryKey(String orderId);

    int updateByPrimaryKeySelective(PayOrder record);

    int updateByPrimaryKey(PayOrder record);

	int delPayOrder(PayOrder payOrder);
}