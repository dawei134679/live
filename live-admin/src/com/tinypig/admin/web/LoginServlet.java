package com.tinypig.admin.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tinypig.admin.dao.AdminUserDao;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.util.EncryptUtils;
import com.tinypig.admin.util.StringUtil;

@Controller
@RequestMapping("/login")
public class LoginServlet {

	AdminUserDao adminUserDao = new AdminUserDao();

	@RequestMapping(value = "/login")
	@ResponseBody
	public Map<String, Object> userLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Map<String, Object> mapResult = new HashMap<String, Object>();
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		request.setAttribute("userName", userName);

		if (StringUtil.isEmpty(userName) || StringUtil.isEmpty(password)) {
			mapResult.put("success", 201);
			mapResult.put("msg", "用户名和密码不能为空");
		} else {

			AdminUserModel adminUser = adminUserDao.login(userName, password);
			if (adminUser == null) {
				mapResult.put("success", 201);
				mapResult.put("msg", "用户名或密码不正确");
			} else {
				HttpSession session = request.getSession();
				session.setAttribute("currentUser", adminUser);

				mapResult.put("success", 200);
				mapResult.put("msg", "main.jsp");
			}
		}
		return mapResult;
	}

	@RequestMapping(value = "/quit")
	@ResponseBody
	public Map<String, Object> userQuit(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> mapResult = new HashMap<String, Object>();

		try {
			request.getSession().removeAttribute("currentUser");
		} catch (Exception e) {
			mapResult.put("errMsg", "退出异常：" + e.getMessage());
		}
		return mapResult;
	}

	// 修改当前登录用户密码
	@RequestMapping(value = "/editPwd")
	@ResponseBody
	public Map<String, Object> editPwd(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> mapResult = new HashMap<String, Object>();
		try {
			String passwordOld = request.getParameter("passwordOld");
			String passwordNew = request.getParameter("passwordNew");
			if (StringUtil.isEmpty(passwordOld) || StringUtil.isEmpty(passwordNew)) {
				mapResult.put("success", 201);
				mapResult.put("msg", "新密码和旧密码不能为空");
				return mapResult;
			}

			AdminUserModel sessionAdminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
			if (sessionAdminUser == null) {
				mapResult.put("success", 202);
				mapResult.put("msg", "用户不存在");
				return mapResult;
			}
			Boolean updPwd = AdminUserDao.getInstance().updPwd(sessionAdminUser.getUid(), passwordOld, passwordNew);
			if (updPwd) {
				mapResult.put("success", 200);
				mapResult.put("msg", "修改成功");
				sessionAdminUser.setPassword(passwordNew);
				request.getSession().setAttribute("currentUser",sessionAdminUser);
			} else {
				mapResult.put("success", 203);
				mapResult.put("msg", "修改失败");
			}
		} catch (Exception e) {
			mapResult.put("success", 500);
			mapResult.put("msg", e.getMessage());
		}
		return mapResult;
	}

	public static void main(String[] srgs) {
		// monitor
		String pass = "gao!@#123qiang";
		String md5Pwd = EncryptUtils.md5Encrypt(pass);

		System.out.println("md5=" + md5Pwd);

	}
}
