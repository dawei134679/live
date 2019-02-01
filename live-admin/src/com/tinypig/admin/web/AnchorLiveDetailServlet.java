package com.tinypig.admin.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.dao.LiveDao;
import com.tinypig.admin.model.LiveDetail;
import com.tinypig.admin.util.ResponseUtil;

/**
 * 获取主播直播时长的明细列表
 */
public class AnchorLiveDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AnchorLiveDetailServlet() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uid = request.getParameter("uid");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		List<LiveDetail> list = LiveDao.getInstance().getLiveDetailByUid(Integer.parseInt(uid), startDate, endDate);
		JSONObject result = new JSONObject();
		result.put("rows", list);
		result.put("total", list.size());
		try {
			ResponseUtil.write(response,result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}