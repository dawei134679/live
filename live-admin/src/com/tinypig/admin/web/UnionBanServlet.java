package com.tinypig.admin.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.tinypig.admin.dao.UnionDao;
import com.tinypig.admin.model.UnionModel;
import com.tinypig.admin.util.Constant;
import com.tinypig.admin.util.ResponseUtil;

/**
 * 编辑公会
 * 
 * @author weber
 */
public class UnionBanServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UnionBanServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String unionid = request.getParameter("unionid");
		String status = request.getParameter("status");

		// 检测状态是否改变
		boolean changeFlag = false;
		UnionModel union = UnionDao.getIns().getUnionById(Integer.parseInt(unionid));
		if (union.getStatus() != Integer.parseInt(status)) {
			changeFlag = true;
		}

		JSONObject result = new JSONObject();
		boolean isOK = UnionDao.getIns().banUnion(Integer.parseInt(unionid), status);
		if (isOK) {
			if (changeFlag)
				invokeHttpInterface(Integer.parseInt(unionid), Integer.parseInt(status));
			result.put("success", isOK);
		} else {
			result.put("fail", isOK);
		}
		try {
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 禁用启用公会,调用业务接口
	private void invokeHttpInterface(int unionid, int status) {
		List<Integer> anchorids = UnionDao.getIns().getAnchorsFromUnionAnchorRef(unionid);
		if (status == 0) {// 禁用公会下所有主播
			for (int rid : anchorids) {
				try {
					Unirest.get(Constant.business_server_url_shark1 + "/admin/ban?anchoruid=" + rid).asJson();
				} catch (UnirestException e) {
					e.printStackTrace();
				}
			}
		} else if (status == 1) {// 启用公会下所有主播
			for (int rid : anchorids) {
				try {
					Unirest.get(Constant.business_server_url_shark1 + "/admin/unban?anchoruid=" + rid).asJson();
				} catch (UnirestException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
