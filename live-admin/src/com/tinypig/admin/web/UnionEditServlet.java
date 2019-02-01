package com.tinypig.admin.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.dao.UnionDao;
import com.tinypig.admin.util.ResponseUtil;

/**
 * 编辑公会
 * @author weber
 */
public class UnionEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UnionEditServlet() {
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
		request.setCharacterEncoding("utf-8");
		String unionid = request.getParameter("unionid");
		String unionName = request.getParameter("unionname");
		String ownerId = request.getParameter("ownerid");
		String remark = request.getParameter("desc");
		String adminuid = request.getParameter("adminuid");
		
		JSONObject result = new JSONObject();
		boolean isOK = UnionDao.getIns().updateUnion(Integer.parseInt(unionid), unionName, ownerId, remark, adminuid);
		if(isOK){
			result.put("success", isOK);
		}else{
			result.put("fail", isOK);
		}
		try {
			ResponseUtil.write(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
