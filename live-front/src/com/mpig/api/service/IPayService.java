package com.mpig.api.service;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * Created by YX on 2016/4/12.
 */
public interface IPayService {

    /**
     * 生成签名
     *
     */
    String buildSign(Map<String, String> param);

    /**
     * 回调验证
     *
     * @param req HttpServletRequest
     * @return check
     */
    boolean checkNotify(HttpServletRequest req);
}