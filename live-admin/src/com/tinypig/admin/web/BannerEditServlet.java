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

public class BannerEditServlet  extends HttpServlet {
		private static final long serialVersionUID = 1L;
	       
	    /**
	     * @see HttpServlet#HttpServlet()
	     */
	    public BannerEditServlet() {
	        super();
	    }

		/**
		 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			doPost(request, response);
		}

		/**
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			request.setCharacterEncoding("utf-8");
			AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
			if(null == adminUser){
				return;
			}
			JSONObject result=new JSONObject();
			String id= request.getParameter("id");
			String swi= request.getParameter("swi");
			String name = request.getParameter("name");
			String startShow[] = request.getParameter("StartDate").split("[-:]");
			String start=startShow[0]+startShow[1]+startShow[2].replace(" ", "")+startShow[3]+startShow[4];
			String endShow[] =  request.getParameter("EndDate").split("[-:]");
			String end=endShow[0]+endShow[1]+endShow[2].replace(" ", "")+endShow[3]+endShow[4];;
			String picUrl= request.getParameter("picUrl");
			System.out.println(id+swi+name+start+end+picUrl);
			if(null == startShow || null == name){
				try {
					result.put("fail", "缺少参数");
					ResponseUtil.write(response,result);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
			
			Boolean bOk = BannerListDao.getInstance().editBanner(Integer.parseInt(id),Integer.parseInt(swi),name, start, end,picUrl);
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

