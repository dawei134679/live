package com.tinypig.admin.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.tinypig.admin.dao.GiftInfoDao;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.model.GiftInfoModel;
import com.tinypig.admin.util.ResponseUtil;

public class GiftInfoServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(GiftInfoServlet.class);
	
	private static final long serialVersionUID = 5608058080092934020L;

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String method = request.getParameter("method");

		if ("getList".equalsIgnoreCase(method)) {
			this.getGiftList(request, response);
		}else if ("update".equalsIgnoreCase(method)) {
			this.updGiftInfo(request, response);
		}else if ("add".equalsIgnoreCase(method)) {
			this.addGiftInfo(request, response);
		}else if("dst".equalsIgnoreCase(method)){
			this.dstUser(request, response);
		}else if ("src".equalsIgnoreCase(method)) {
			this.srcUser(request, response);
		}
	}

	protected void updGiftInfo(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		Map<String, Object> map = new HashMap<String, Object>();

		boolean bl = true;
		String errmsg = "";
		
		AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
		Integer loginUid = adminUser.getUid();
		
		if (loginUid <= 0) {
			bl = false;
			errmsg = "请先登录";
		}
		String gname = request.getParameter("gname");
		if (StringUtils.isEmpty(gname) && bl) {
			bl = false;
		}
		
		String type = request.getParameter("type");
		if (StringUtils.isEmpty(type) && bl) {
			bl = false;
			errmsg = "请选择礼物大类";
		}
		
		String subtype = request.getParameter("subtype");
		if (StringUtils.isEmpty(subtype) && bl) {
			bl = false;
			errmsg = "请选择礼物大类";
		}
		
		int gprice = Integer.valueOf(request.getParameter("gprice"));
		int gpriceaudit = Integer.valueOf(request.getParameter("gpriceaudit"));
		int wealth = Integer.valueOf(request.getParameter("wealth"));
		int credit = Integer.valueOf(request.getParameter("credit"));
		int charm = Integer.valueOf(request.getParameter("charm"));
		String gcover = request.getParameter("gcover");
		
//		if (StringUtils.isEmpty(gcover) && bl) {
//			bl = false;
//			errmsg = "封面名称不能为空";
//		}
		
		String gtype = request.getParameter("gtype");
		if (StringUtils.isEmpty(gtype) && bl) {
			System.out.println(StringUtils.isEmpty(gtype));
			bl = false;
			errmsg = "请选择礼物类型";
		}
		
		String gpctype = request.getParameter("gpctype");
		if (StringUtils.isEmpty(gpctype) && bl) {
			bl = false;
			errmsg = "请选择PC效果类型";
		}
		
		String gframeurl = request.getParameter("gframeurl");
		if (StringUtils.isEmpty(gframeurl) && bl) {
			bl = false;
			errmsg = "zip地址不能为空";
		}
		String gframeurlios = request.getParameter("gframeurlios");
		if (StringUtils.isEmpty(gframeurlios) && bl) {
			bl = false;
			errmsg = "ios zip地址不能为空";
		}
		int simgs = Integer.valueOf(request.getParameter("simgs"));
		int bimgs = Integer.valueOf(request.getParameter("bimgs"));
		int pimgs = Integer.valueOf(request.getParameter("pimgs"));
		String gnumtype = request.getParameter("gnumtype");
		if (StringUtils.isEmpty(gnumtype) && bl) {
			bl = false;
			errmsg = "礼物数组为空";
		} else {
			try {
				gnumtype.split(",");
			} catch (Exception ex) {
				bl = false;
				errmsg = "礼物数组格式不对";
			}
		}
		float gduration = Float.valueOf(request.getParameter("gduration"));
		String isshow = request.getParameter("isshow");
		if (StringUtils.isEmpty(isshow) && bl) {
			isshow = "0";
		}
		String isvalid = request.getParameter("isvalid");
		if (StringUtils.isEmpty(isvalid) && bl) {
			isvalid = "0";
		}
		int gsort = Integer.valueOf(request.getParameter("gsort"));
		
		int gid = Integer.valueOf(request.getParameter("gid"));
		if (gid == 0 && bl) {
			bl = false;
			errmsg = "请重新选择礼物修改";
		}
		int skin = Integer.valueOf(request.getParameter("skin"));
		
		String category = request.getParameter("category");
		if (StringUtils.isEmpty(category) && bl) {
			bl =  false;
			errmsg = "请选择PC分类";
		}
		
		String icon =  request.getParameter("icon");
		int useDuration = Integer.valueOf(request.getParameter("useDuration"));
		
		if (!bl) {
			map.put("errorMsg", errmsg);
			
		} else {
			int updGiftInfo = GiftInfoDao.getInstance().updGiftInfo(gid,Integer.valueOf(type),Integer.valueOf(subtype), gname, gprice,gpriceaudit, wealth, credit, charm, gcover, Integer.valueOf(gtype),Integer.valueOf(gpctype), gframeurl,gframeurlios, simgs,
					bimgs, pimgs, gnumtype, gduration, "1".equals(isshow) ? true:false, "1".equals(isvalid) ? true:false, gsort,icon,skin,useDuration,Integer.valueOf(category),loginUid);
			if (updGiftInfo != 1) {
				map.put("errorMsg", "更新失败");
			}
		}
		try {
			response.getWriter().write(JSON.toJSONString(map));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}

	protected void getGiftList(HttpServletRequest request, HttpServletResponse response) {
		int ipage = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int irows = request.getParameter("rows") == null ? 20:Integer.valueOf(request.getParameter("rows"));
		if (ipage == 0) {
			ipage = 1;
		}
		String gname = request.getParameter("gname");
		String types = null;
		if(StringUtils.isNotBlank(request.getParameter("type"))) {
			types = request.getParameter("type");
		}
		String subtypes = null;
		if(StringUtils.isNotBlank(request.getParameter("subtype"))) {
			subtypes = request.getParameter("subtype");
		}
		Integer isvalid = null;
		if(StringUtils.isNotBlank(request.getParameter("isvalid"))) {
			isvalid = Integer.valueOf(request.getParameter("isvalid"));
		}
		Integer isshow = null;
		if(StringUtils.isNotBlank(request.getParameter("isshow"))) {
			isshow = Integer.valueOf(request.getParameter("isshow"));
		}
		
		Map<String, Object> map  = GiftInfoDao.getInstance().getGiftList(ipage,irows,gname,types,subtypes,isvalid,isshow);
		try {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(JSON.toJSONString(map));
		} catch (Exception e) {
			logger.error("getGiftList-exception:", e);
		}
	}
	
	protected void addGiftInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		Map<String, Object> map = new HashMap<String, Object>();
	
		try {

			boolean bl = true;
			String errmsg = "";

			AdminUserModel adminUser = (AdminUserModel)request.getSession().getAttribute("currentUser");
			Integer loginUid = adminUser.getUid();
			
			if (loginUid <= 0) {
				bl = false;
				errmsg = "请先登录";
			}
			
			String gname = request.getParameter("gname");
			if (StringUtils.isEmpty(gname) && bl) {
				bl = false;
			}
			
			int gprice = Integer.valueOf(request.getParameter("gprice"));
			int gpriceaudit = Integer.valueOf(request.getParameter("gpriceaudit"));
			int wealth = Integer.valueOf(request.getParameter("wealth"));
			int credit = Integer.valueOf(request.getParameter("credit"));
			int charm;
			if(StringUtils.isNotBlank(request.getParameter("charm"))) {
				charm = Integer.valueOf(request.getParameter("charm"));
			}else {
				charm = 0;
			}
			
		
			String gcover = request.getParameter("gcover");
			if (StringUtils.isEmpty(gcover) && bl) {
				bl = false;
				errmsg = "封面名称不能为空";
			}
			String gtype = request.getParameter("gtype");
			if (StringUtils.isEmpty(gtype) && bl) {
				bl = false;
				errmsg = "请选择礼物类型";
			}
			
			String gpctype = request.getParameter("gpctype");
			if (StringUtils.isEmpty(gpctype) && bl) {
				bl = false;
				errmsg = "请选择PC效果类型";
			}

			String gframeurl = request.getParameter("gframeurl");
			if (StringUtils.isEmpty(gframeurl) && bl) {
				bl = false;
				errmsg = "zip地址不能为空";
			}
			String gframeurlios = request.getParameter("gframeurlios");
			if (StringUtils.isEmpty(gframeurlios) && bl) {
				bl = false;
				errmsg = "ios zip地址不能为空";
			}
			int simgs = Integer.valueOf(request.getParameter("simgs"));
			int bimgs = Integer.valueOf(request.getParameter("bimgs"));
			int pimgs = Integer.valueOf(request.getParameter("pimgs"));
			
			String type = request.getParameter("type");
			if (StringUtils.isEmpty(type) && bl) {
				bl = false;
				errmsg = "请选择礼物大类";
			}
			
			String subtype = request.getParameter("subtype");
			if (StringUtils.isEmpty(subtype) && bl) {
				bl = false;
				errmsg = "请选择礼物大类";
			}
			
			String gnumtype = request.getParameter("gnumtype");
			if (StringUtils.isEmpty(gnumtype) && bl) {
				bl = false;
				errmsg = "礼物数组为空";
			} else {
				try {
					gnumtype.split(",");
				} catch (Exception ex) {
					bl = false;
					errmsg = "礼物数组格式不对";
				}
			}
			float gduration = Float.valueOf(request.getParameter("gduration"));
			String isshow = request.getParameter("isshow");
			if (StringUtils.isEmpty(isshow) && bl) {
				bl = false;
				errmsg = "请选择是否显示";
			}
			
			String isvalid = request.getParameter("isvalid");
			if (StringUtils.isEmpty(isvalid) && bl) {
				bl = false;
				errmsg = "请选择是否有效";
			}
			int gsort = Integer.valueOf(request.getParameter("gsort"));
			int skin = Integer.valueOf(request.getParameter("skin"));
			
			String category = request.getParameter("category");
			if (StringUtils.isEmpty(category) && bl) {
				bl =  false;
				errmsg = "请选择PC分类";
			}
			
			String icon =  request.getParameter("icon");
			int useDuration = Integer.valueOf(request.getParameter("useDuration"));
			
			if (!bl) {
				map.put("errorMsg", errmsg);
				
			} else {
				int addGiftInfo = GiftInfoDao.getInstance().addGiftInfo(Integer.valueOf(type), Integer.valueOf(subtype), gname, gprice,gpriceaudit, wealth, credit, charm, gcover, Integer.valueOf(gtype), Integer.valueOf(gpctype), gframeurl,gframeurlios, simgs, bimgs, pimgs, gnumtype, gduration, Integer.valueOf(isshow), Integer.valueOf(isvalid), gsort, icon, skin, useDuration, Integer.valueOf(category), loginUid);
						
				if (addGiftInfo <= 0) {
					map.put("errorMsg", "更新失败");
				}
			}
		} catch (Exception e) {
			map.put("errorMsg", "系统异常："+ e.getMessage());
		}

		try {
			response.getWriter().write(JSON.toJSONString(map));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	protected void dstUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uid=request.getParameter("uid");
		String date=request.getParameter("start");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		List<Map<String, Object>> footer = new ArrayList<Map<String,Object>>();
		List<GiftInfoModel> list=GiftInfoDao.getInstance().dstGiftInfo(Integer.parseInt(uid),date,page,rows,"dst");
		try {
			Map<String, Object> dstTotal = GiftInfoDao.getInstance().dstTotal(Integer.parseInt(uid), date,"dst");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("rows", list);
			if (dstTotal != null) {
				map.put("total",dstTotal.get("total"));
				Map<String, Object> mapFoot = new HashMap<String, Object>();
				mapFoot.put("srcuid", "收到声援汇总");
				mapFoot.put("srcnickname", dstTotal.get("amount"));
				footer.add(mapFoot);
				map.put("footer",footer);
			}
			ResponseUtil.writeJSON(response, map);
		} catch (Exception e) {
			logger.error("dstUser-exception:", e);
		}
	}

	protected void srcUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uid=request.getParameter("uid");
		String date=request.getParameter("start");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		List<GiftInfoModel> list=GiftInfoDao.getInstance().dstGiftInfo(Integer.parseInt(uid),date,page,rows,"src");

		List<Map<String, Object>> footer = new ArrayList<Map<String,Object>>();
		try {
			Map<String, Object> dstTotal = GiftInfoDao.getInstance().dstTotal(Integer.parseInt(uid), date,"src");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("rows", list);
			if (dstTotal != null) {
				map.put("total",dstTotal.get("total"));
				Map<String, Object> mapFoot = new HashMap<String, Object>();
				mapFoot.put("srcuid", "送出猪头汇总");
				mapFoot.put("srcnickname", dstTotal.get("amount"));
				footer.add(mapFoot);
				map.put("footer",footer);
			}

			ResponseUtil.writeJSON(response, map);
		} catch (Exception e) {
			logger.error("dstUser-exception:", e);
		}
	}
}