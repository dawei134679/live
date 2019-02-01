package com.tinypig.admin.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tinypig.admin.dao.BannerListDao;
import com.tinypig.admin.dao.CheckUserDao;
import com.tinypig.admin.model.BannerListModel;
import com.tinypig.admin.model.CheckUserModel;
import com.tinypig.admin.util.ResponseUtil;

public class BannerListServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BannerListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String method=request.getParameter("method");
		if("home".equals(method)){
			this.home(request, response);
		}else if("carousel".equals(method)){
			this.carousel(request, response);
		}
	}
	
	protected void home(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		List<BannerListModel> list = BannerListDao.getInstance().getBannerList();
		int type=0;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("rows", list);
			map.put("total",BannerListDao.getInstance().BannerTotal(type));
			ResponseUtil.writeJSON(response, map);
		} catch (Exception e) {
			System.out.println("getList->exception:"+e.getMessage());
		}
	}
	
	protected void carousel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		List<BannerListModel> list = BannerListDao.getInstance().getCarouselList();
		int type=1;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("rows", list);
			map.put("total", BannerListDao.getInstance().BannerTotal(type));
			ResponseUtil.writeJSON(response, map);
		} catch (Exception e) {
			System.out.println("getList->exception:"+e.getMessage());
		}
	}
	
}

