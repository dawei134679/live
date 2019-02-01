package com.tinypig.admin.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.tinypig.admin.dao.AdminMenuDao;
import com.tinypig.admin.dao.AdminRoleDao;
import com.tinypig.admin.model.AdminRoleModel;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.util.ResponseUtil;

public class AdminRoleServlet extends HttpServlet{


	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminRoleServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");

		if ("getRoleList".equalsIgnoreCase(method)) {
			this.getRoleList(request, response);
		}else if("saveRole".equalsIgnoreCase(method)){
			this.saveRole(request, response);
		}else if("getRuleTree".equalsIgnoreCase(method)){
			this.getRuleTree(request, response);
		}else if("saveRuleTree".equalsIgnoreCase(method)){
			this.saveRuleTree(request, response);
		}else if("loadRuleById".equalsIgnoreCase(method)){
			this.loadRuleById(request, response);
		}else if("delRule".equalsIgnoreCase(method)){
			this.delRule(request, response);
		}else if("getAllRoleList".equalsIgnoreCase(method)){
			this.getAllRoleList(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}


	/**
	 * 
	 * @Description: 获取角色列表
	 * @param request
	 * @param response   
	 * @return void  
	 * @throws
	 * @author guojp
	 * @date 2016-6-29
	 */
	protected void getRoleList(HttpServletRequest request, HttpServletResponse response){
		int page = Integer.parseInt(request.getParameter("page").toString());
		int rows = Integer.parseInt(request.getParameter("rows").toString());
		try {
			Map<String, Object> map = AdminRoleDao.getInstance().getRoleList(page, rows);
			ResponseUtil.writeJSON(response, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	/**
	 * 
	 * @Description: 保存角色
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @return String
	 * @throws
	 * @author guojp
	 * @date 2016-7-1
	 */
	public String saveRole(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		
		String rolename = request.getParameter("role_name").toString();
		int roleid = (request.getParameter("role_id")==null || "".equals(request.getParameter("role_id")))?0:Integer.parseInt(request.getParameter("role_id").toString());
		
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			response.getWriter().write("<body><pre>{\"message\":\"<br/>请先登录！\",\"success\":false}</pre></body>");
			response.getWriter().flush();
			response.getWriter().close();
			return null;
		}

		if (roleid == 0) {//新增角色
//			Usergroup oldUsergroup = usergroupService.getLoaded(usergroup.getId());
//			if (!usergroupService.isPropertyUnique("groupname", usergroup.getGroupname(), oldUsergroup.getGroupname())) {
//				response.getWriter().write("<body><pre>{\"message\":\"<br/>修改角色失败！角色名称已存在！\",\"success\":false}</pre></body>");
//				response.getWriter().flush();
//				response.getWriter().close();
//				return null;
//			}
//			usergroupService.update(usergroup);
			boolean flag = AdminRoleDao.getInstance().saveRole(request, response, rolename,adminUser.getUid());
			if(!flag){
				response.getWriter().write("<body><pre>{\"message\":\"<br/>新增角色失败！角色名称已存在！\",\"success\":false}</pre></body>");
				response.getWriter().flush();
				response.getWriter().close();
				return null;
			}
		}else{//修改角色
			boolean flag = AdminRoleDao.getInstance().editRole(request, response, rolename, roleid,adminUser.getUid());
			if(!flag){
				response.getWriter().write("<body><pre>{\"message\":\"修改角色失败！角色名称已存在！\",\"success\":false}</pre></body>");
				response.getWriter().flush();
				response.getWriter().close();
				return null;
			}

		}

		response.getWriter().write("<body><pre>{\"message\":\"保存成功\",\"success\":true}</pre></body>");
		response.getWriter().flush();
		response.getWriter().close();
		return null;
	}
	
	/**
	 * 
	 * @Description: 获取权限列表
	 * @param request
	 * @param response
	 * @throws IOException   
	 * @return void  
	 * @throws
	 * @author guojp
	 * @date 2016-7-1
	 */
	public void getRuleTree(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
			if (adminUser == null) {
				ResponseUtil.write(response, null);
				return;
			}
			
			int rid = Integer.valueOf(request.getParameter("rid"));
			AdminRoleModel adminRoleModel = AdminRoleDao.getInstance().getRoleByRid(rid,adminUser.getUid());
			String menuids = adminRoleModel.getMenu_ids();
			JSONArray jsonArray = AdminMenuDao.getAllMenuTree(menuids);
		
			ResponseUtil.write(response, jsonArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @Description: 保存角色权限
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException   
	 * @return String  
	 * @throws
	 * @author guojp
	 * @date 2016-7-4
	 */
	public void saveRuleTree(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("text/html;charset=utf-8");
		try {
			AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
			if (adminUser == null) {
				ResponseUtil.write(response, null);
				return;
			}

			String grule = request.getParameter("grule").toString();
			int gid = (request.getParameter("gid")==null || "".equals(request.getParameter("gid")))?0:Integer.parseInt(request.getParameter("gid").toString());
	
			JSONObject jobj = new JSONObject();
			if (gid != 0) {//新增角色
				boolean flag = AdminRoleDao.getInstance().saveRuleTree(gid, grule,adminUser.getUid());
				if(flag){
					jobj.put("success", true);
				}else{
					jobj.put("success", false);
				}
			}else{
				jobj.put("success", false);
			}

			ResponseUtil.write(response, jobj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @Description: 加载角色
	 * @param request
	 * @param response   
	 * @return void  
	 * @throws
	 * @author guojp
	 * @date 2016-7-4
	 */
	public void loadRuleById(HttpServletRequest request, HttpServletResponse response){
		
		int rid = (request.getParameter("rid")==null || "".equals(request.getParameter("rid")))?0:Integer.parseInt(request.getParameter("rid").toString());

		AdminRoleModel adminRoleModel = null;
		if (rid != 0) {//新增角色
			adminRoleModel = AdminRoleDao.getInstance().loadRuleById(rid);
		}

		try {
			ResponseUtil.writeJSON(response, adminRoleModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @Description: 删除角色
	 * @param request
	 * @param response
	 * @throws IOException   
	 * @return void  
	 * @throws
	 * @author guojp
	 * @date 2016-7-4
	 */
	public void delRule(HttpServletRequest request, HttpServletResponse response) throws IOException {

		try {
			AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
			if (adminUser == null) {
				ResponseUtil.write(response, null);
				return;
			}
			
			int rid = (request.getParameter("rid")==null || "".equals(request.getParameter("rid")))?0:Integer.parseInt(request.getParameter("rid").toString());
	
			JSONObject jobj = new JSONObject();
			if (rid != 0) {//新增角色
				boolean flag = AdminRoleDao.getInstance().delRule(rid,adminUser.getUid());
				if(flag){
					jobj.put("success", true);
				}else{
					jobj.put("success", false);
				}
			}else{
				jobj.put("success", false);
			}

			ResponseUtil.write(response, jobj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * 
	 * @Description: 获取所有角色，供前端下拉列表使用
	 * @param request
	 * @param response   
	 * @return void  
	 * @throws
	 * @author guojp
	 * @date 2016-7-5
	 */
	protected void getAllRoleList(HttpServletRequest request, HttpServletResponse response){
		try {
			List<AdminRoleModel> list = AdminRoleDao.getInstance().getAllRoleList();
			ResponseUtil.writeJSON(response, list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
