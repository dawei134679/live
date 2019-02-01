package com.mpig.api.perf;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.mpig.api.acl.AclFilter;
import com.mpig.api.perf.vo.CostRequestLog;
import com.mpig.api.redis.service.OtherRedisService;

public class PerfFilter implements Filter {
	/**
	 * 默认耗时较长的警告值
	 */
	private long alarmCostTime = 100;
	/**
	 * 默认存储的最大耗时记录
	 */
	public final static long MaxStoreCount = 5000;
	private static final Logger logger = Logger.getLogger(PerfFilter.class);
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;  
		long t1 = System.currentTimeMillis();  
		chain.doFilter(request, response);  
		long delta = System.currentTimeMillis() - t1; 
		if(delta >= alarmCostTime){
			logger.debug("***request[" + req.getRequestURI() + "] finished with time(ms):"+delta);
			OtherRedisService.getInstance().saveCostRequest(
					CostRequestLog.createWith(
							req.getRequestURI(), delta, AclFilter.getRemoteAddrIp(req), new Date().toString()));
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		alarmCostTime = Long.parseLong(arg0.getInitParameter("alarmCostTime"));
	}
}
