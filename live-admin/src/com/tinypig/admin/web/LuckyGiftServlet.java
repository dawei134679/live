package com.tinypig.admin.web;

import com.alibaba.fastjson.JSON;
import com.tinypig.admin.dao.AdminRoleDao;
import com.tinypig.admin.dao.LuckyGiftDao;
import com.tinypig.admin.dao.PayOrderDao;
import com.tinypig.admin.model.AdminRoleModel;
import com.tinypig.admin.model.PayOrderModel;
import com.tinypig.admin.util.ResponseUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by YX on 2016/5/6.
 */
public class LuckyGiftServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");

		if ("getPrizeList".equalsIgnoreCase(method)) {
			this.getPrizeList(req, resp);
		}else if("getAllLuckyGiftList".equalsIgnoreCase(method)){
			this.getAllLuckyGiftList(req, resp);
		}else if ("getPrizeDetailList".equalsIgnoreCase(method)){
			this.getPrizeDetailList(req, resp);
		}

        
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
    
    private void getPrizeList(HttpServletRequest req, HttpServletResponse resp) throws IOException{
    	String page = req.getParameter("page");
        String rows = req.getParameter("rows");

        String startdate = req.getParameter("startdate");
        String enddate = req.getParameter("enddate");
        String gid = req.getParameter("gid");

        resp.setContentType("application/json;charset=utf-8");
        HashMap<String, Object> map = new LuckyGiftDao().getPriizeList(page, rows, startdate, enddate, gid);
        resp.getWriter().write(JSON.toJSONString(map));
    }
    
    private void getAllLuckyGiftList(HttpServletRequest request, HttpServletResponse response){
		try {
			List<HashMap<String, Object>> list = new LuckyGiftDao().getAllLuckyGiftList();
			ResponseUtil.writeJSON(response, list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    private void getPrizeDetailList(HttpServletRequest req, HttpServletResponse resp) throws IOException{
    	String page = req.getParameter("page");
        String rows = req.getParameter("rows");

        String createdate = req.getParameter("createdate");
        String multiple = req.getParameter("multiple");
        String gid = req.getParameter("gid");

        resp.setContentType("application/json;charset=utf-8");
        HashMap<String, Object> map = new LuckyGiftDao().getPrizeDetailList(page, rows, createdate, multiple, gid);
        resp.getWriter().write(JSON.toJSONString(map));
    }


}
