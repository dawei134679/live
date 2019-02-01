package com.tinypig.admin.web;

import com.alibaba.fastjson.JSON;
import com.tinypig.admin.dao.PayOrderDao;
import com.tinypig.admin.model.PayOrderModel;
import com.tinypig.admin.util.DateUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by YX on 2016/5/6.
 */
public class PayOrderServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");

		if ("getPayOrderList".equalsIgnoreCase(method)) {
			this.getPayOrderList(req, resp);
		}
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
    
    /**
     * 
     * @Description: 获取订单
     * @param req
     * @param resp   
     * @return void  
     * @throws IOException 
     * @throws
     * @author guojp
     * @date 2016-7-14
     */
    private void getPayOrderList(HttpServletRequest req, HttpServletResponse resp) throws IOException{
    	String strPage = req.getParameter("page");
    	int page = 1;
    	if (StringUtils.isNotEmpty(strPage)) {
    		page = Integer.valueOf(strPage);
    		page = page == 0 ? 1:page ;
		}
    	
        String strRows = req.getParameter("rows");
        int rows = 20;
        if (StringUtils.isNotEmpty(strRows)) {
			rows = Integer.valueOf(strRows);
		}

        String strSrcuid = req.getParameter("srcuid");
        int srcuid = 0;
        if (StringUtils.isNotEmpty(strSrcuid)) {
			srcuid = Integer.valueOf(strSrcuid);
		}
        
        String strDstuid = req.getParameter("dstuid");
        int dstuid = 0;
        if (StringUtils.isNotEmpty(strDstuid)) {
			dstuid = Integer.valueOf(strDstuid);
		}
        
        String strStatus = req.getParameter("status");
        int status = 9;
        if (StringUtils.isNotEmpty(strStatus)) {
			status = Integer.valueOf(strStatus);
		}
        
        String strSTime = req.getParameter("s_time");
        Long sTime = 0L;
        if (StringUtils.isNotEmpty(strSTime)) {
			sTime = DateUtil.dateToLong(strSTime, "yyyy-MM-dd", "other", 0);
		}
        
        String strETime = req.getParameter("e_time");
        Long eTime = 0L;
        if (StringUtils.isNotEmpty(strETime)) {
			eTime = DateUtil.dateToLong(strETime, "yyyy-MM-dd", "other", 0);
		}
        
        String channel = req.getParameter("chnenee");

        List<PayOrderModel> data = new PayOrderDao().getPayOrderList(page, rows, "", "", srcuid, dstuid, status,sTime,eTime, channel);

        resp.setContentType("application/json;charset=utf-8");
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("rows", data);
        map.put("total", new PayOrderDao().countQuery(page, rows, "", "", srcuid, dstuid, status,sTime,eTime, channel));
        resp.getWriter().write(JSON.toJSONString(map));
    }
}
