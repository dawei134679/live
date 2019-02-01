package com.tinypig.admin.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.dao.BannerListDao;
import com.tinypig.admin.dao.UnionDao;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.util.ResponseUtil;

public class carouselAddServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -863295646213062975L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * unionname
	 * onwerid
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String method=request.getParameter("method");
		if("add".equals(method)){
			this.add(request, response);
		}else if("edit".equals(method)){
			this.edit(request, response);
		}else if("del".equals(method)){
			this.del(request, response);
		}
	}
	
	protected void add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if(null == adminUser){
			return;
		}
		JSONObject result=new JSONObject();
		String jumpUrl=request.getParameter("jumpUrl");
		String name = request.getParameter("name");
		String startShow[] = request.getParameter("StartDate").split("[-:]");
		String start=startShow[0]+startShow[1]+startShow[2].replace(" ", "")+startShow[3]+startShow[4];
		String endShow[] =  request.getParameter("EndDate").split("[-:]");
		String end=endShow[0]+endShow[1]+endShow[2].replace(" ", "")+endShow[3]+endShow[4];;
		String picUrl= request.getParameter("picUrl");
		if(null == startShow || null == name){
			try {
				result.put("fail", "缺少参数");
				ResponseUtil.write(response,result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		
		Boolean bOk = BannerListDao.getInstance().addcarousel(jumpUrl, name, start, end, picUrl);
		if(bOk){
			result.put("success", bOk);
		}else{
			result.put("fail", bOk);
		}
		
		try {
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void edit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if(null == adminUser){
			return;
		}
		JSONObject result=new JSONObject();
		String id=request.getParameter("id");
		String jumpUrl=request.getParameter("jumpUrl");
		String name = request.getParameter("name");
		String startShow[] = request.getParameter("StartDate").split("[-:]");
		String start=startShow[0]+startShow[1]+startShow[2].replace(" ", "")+startShow[3]+startShow[4];
		String endShow[] =  request.getParameter("EndDate").split("[-:]");
		String end=endShow[0]+endShow[1]+endShow[2].replace(" ", "")+endShow[3]+endShow[4];;
		String picUrl= request.getParameter("picUrl");
		if(null == startShow || null == name){
			try {
				result.put("fail", "缺少参数");
				ResponseUtil.write(response,result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		
		Boolean bOk = BannerListDao.getInstance().editcarousel(Integer.parseInt(id),jumpUrl, name, start, end, picUrl);
		if(bOk){
			result.put("success", bOk);
		}else{
			result.put("fail", bOk);
		}
		
		try {
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void del(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		if(null == adminUser){
			return;
		}
		JSONObject result=new JSONObject();
		String id=request.getParameter("id");
		if(null == id ){
			try {
				result.put("fail", "缺少参数");
				ResponseUtil.write(response,result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		
		Boolean bOk = BannerListDao.getInstance().delcarousel(Integer.parseInt(id));
		if(bOk){
			result.put("success", bOk);
		}else{
			result.put("fail", bOk);
		}
		
		try {
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
