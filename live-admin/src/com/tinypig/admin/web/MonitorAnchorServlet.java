package com.tinypig.admin.web;

import com.tinypig.admin.dao.LiveDao;
import com.tinypig.admin.dao.UnionDao;
import com.tinypig.admin.dao.UserDao;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.model.UserBaseInfo;
import com.tinypig.admin.redis.service.OtherRedisService;
import com.tinypig.admin.util.Constant;
import com.tinypig.admin.util.ResponseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by fangwuqing on 16/5/9.
 */
@WebServlet(name = "MonitorAnchorServlet")
public class MonitorAnchorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		response.setHeader("Access-Control-Allow-Origin", "*");
		if ("monitor".equalsIgnoreCase(method)) {
			this.getMonitorAnchor(request, response);
		} else if ("block".equalsIgnoreCase(method)) {
			this.blockAnchor(request, response);
		} else if ("ban".equalsIgnoreCase(method)) {
			this.banAnchor(request, response);
		} else if ("close".equalsIgnoreCase(method)) {
			this.closeRoom(request, response);
		} else if ("unhandle".equalsIgnoreCase(method)) {
			this.getUnHandle(request, response);
		} else if ("test".equalsIgnoreCase(method)) {
			this.getTest(request, response);
		}
	}
	
	protected void getTest(HttpServletRequest request, HttpServletResponse response){
		
		int page = Integer.valueOf(request.getParameter("page"));
		int table = Integer.valueOf(request.getParameter("table"));
		
		List<Map<String, Object>> test = UserDao.getInstance().getTest(page, table);

		try {
			ResponseUtil.writeJSON(response, test);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	protected void getUnHandle(HttpServletRequest request, HttpServletResponse response) {
		try {
			String suid = request.getParameter("uid");
			if (StringUtils.isEmpty(suid)) {
				return;
			}
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			Map<String, Object> mapResult = new HashMap<String, Object>();
			
			Map<String, Object> map = UserDao.getInstance().getUserHandle(Integer.valueOf(suid));
			if (map != null) {
				list.add(map);
			}

			mapResult.put("total", list.size());
			mapResult.put("rows", list);
			ResponseUtil.writeJSON(response, mapResult);
			
		} catch (Exception ex) {
			System.out.println("getUnHandle->Exception:" + ex.getMessage());
		}
	}

	/**
	 * 获取开播列表
	 * 
	 * @param resquest
	 * @param response
	 */
	protected void getMonitorAnchor(HttpServletRequest resquest, HttpServletResponse response) {
		try {
			String recommend = resquest.getParameter("recommend");
			Integer nRecommend = Integer.valueOf(recommend);
			List<Map<String, String>> result = new ArrayList<Map<String, String>>();
			Set<String> set = null;
			if (nRecommend == 0) {
				set = OtherRedisService.getInstance().getBaseRoom(0);
			} else if (nRecommend == 1) {
				set = OtherRedisService.getInstance().getHotRoom(0);
			} else if (nRecommend == 2) {
				set = OtherRedisService.getInstance().getRecommendRoom(0);
			} else if (nRecommend == 3){
				//set = OtherRedisService.getInstance().getHeadRoom(0);
				//业务系统把数据放到头牌的数据放到推荐里面 遍历的时候根据根据Recommend判断
				set = OtherRedisService.getInstance().getRecommendRoom(0);
			}
			if (set != null && set.size() > 0) {
				for (String struid : set) {
					UserBaseInfo info = UnionDao.getUserBaseInfo(Integer.valueOf(struid));
					if (null != info) {
						if(false == info.getLiveStatus()){
							continue;
						}
						if (nRecommend == info.getRecommend()) {
							
							Map<String, String> data = new HashMap<String, String>();
							data.put("uid", info.getUid().toString());
							data.put("nickname", info.getNickname().toString());
							data.put("verified", info.getVerified_reason().toString());
							data.put("videopic", Constant.qn_liveImage_domain + "/" + info.getUid() + ".jpg");
							data.put("recommend", info.getRecommend().toString());
							data.put("anchorlevel", info.getAnchorLevel().toString());
							data.put("report", OtherRedisService.getInstance().getReports(Integer.valueOf(struid)));
							result.add(data);
						}
						if (nRecommend == 1 && info.getRecommend() == 0) {
							
							Map<String, String> data = new HashMap<String, String>();
							data.put("uid", info.getUid().toString());
							data.put("nickname", info.getNickname().toString());
							data.put("verified", info.getVerified_reason().toString());
							data.put("videopic", Constant.qn_liveImage_domain + "/" + info.getUid() + ".jpg");
							data.put("recommend", info.getRecommend().toString());
							data.put("anchorlevel", info.getAnchorLevel().toString());
							data.put("report", OtherRedisService.getInstance().getReports(Integer.valueOf(struid)));
							result.add(data);
						}
					}
				}
			}
			ResponseUtil.writeJSON(response, result);
		} catch (Exception e) {
			System.out.println("getMonitorAnchor->writeJSON:" + e.getMessage());
		}
	}

	/**
	 * 封号
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	protected void blockAnchor(HttpServletRequest request, HttpServletResponse response) throws IOException {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		if (null == adminUser) {
			response.sendRedirect("index.jsp");
		}
		int uid = Integer.valueOf(request.getParameter("uid"));
		int status = Integer.valueOf(request.getParameter("status"));
		String cause = request.getParameter("cause");
		if (StringUtils.isEmpty(cause)) {
			cause = "测试";
		}

		Map<String, Object> map = UserDao.getInstance().setBlockAnchor(uid, status, adminUser.getUid(), cause);
		try {
			ResponseUtil.writeJSON(response, map);
		} catch (Exception e) {
			System.out.println("blockAnchor->writeJSON:" + e.getMessage());
		}
	}

	/**
	 * 禁播
	 * 
	 * @param request
	 * @param response
	 */
	protected void banAnchor(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		if (null == adminUser) {
			response.sendRedirect("index.jsp");
		}

		int uid = Integer.valueOf(request.getParameter("uid"));
		int status = Integer.valueOf(request.getParameter("status")); // =1主播
																		// =2看客
																		// =3超管
		String cause = request.getParameter("cause");
		if (StringUtils.isEmpty(cause)) {
			cause = "测试";
		}
		Map<String, Object> map = UserDao.getInstance().setIdentity(uid, status, adminUser.getUid(), cause);

		try {
			ResponseUtil.writeJSON(response, map);
		} catch (Exception e) {
			System.out.println("banAnchor->Exception:" + e.getMessage());
		}
	}

	/**
	 * 关房间
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	protected void closeRoom(HttpServletRequest request, HttpServletResponse response) throws IOException {
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		if (null == adminUser) {
			response.sendRedirect("index.jsp");
		}
		int uid = Integer.valueOf(request.getParameter("uid"));
		Map<String, Object> map = LiveDao.getInstance().closeRoom(uid, adminUser.getUid());

		try {
			ResponseUtil.writeJSON(response, map);
		} catch (Exception e) {
			System.out.println("colseRoom->writeJson:" + e.getMessage());
		}
	}
}
