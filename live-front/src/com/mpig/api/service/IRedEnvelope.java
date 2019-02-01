package com.mpig.api.service;

import java.util.Map;

import com.mpig.api.model.PayGetRedenvelopModel;

public interface IRedEnvelope {
	   
    /**
     * 插入抢红包记录
     * @param objects
     * @return
     */
    int insertPayGetRedenvelop(Object... objects);
    
	/**
	 * 查找获取红包被抢记录
	 * @param envelopeId
	 */
	public Map<String, Object> triedRedEnvelopeList(int envelopeId);
	
	/**
	 * 获取用户抢到的红包
	 * @param envelopeId 红包ID
	 * @param uid 抢到红包uid
	 * @return
	 */
	public PayGetRedenvelopModel getGetRedenvelopInfo(int envelopeId,int uid);

}
