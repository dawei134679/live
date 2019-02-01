package com.tinypig.admin.web.tag;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.ContextLoader;

import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.newadmin.web.entity.AdminMenu;
import com.tinypig.newadmin.web.entity.AdminRole;
import com.tinypig.newadmin.web.service.IAdminMenuService;
import com.tinypig.newadmin.web.service.IAdminRoleService;

/**
 * 权限标签
 */
public class AuthTag extends SimpleTagSupport {

	private String action;
	private String show;//true:如果有action权限，则展示数据false:如果有action则隐藏
	private IAdminRoleService adminRoleService;
	private IAdminMenuService adminMenuService;

	@Override
	public void doTag() throws JspException, IOException {
		StringWriter sw = new StringWriter();
		getJspBody().invoke(sw);
		JspWriter out = getJspContext().getOut();
		boolean isShow = true;
		if(StringUtils.equals("false", show)) {
			isShow = false;
		}
		if(isShow) {
			if (haveAuth(action)) {//有权限的时候展示
				out.println(sw.toString());	
			}
		}else {
			if (!haveAuth(action)) {//有权限的时候不展示（无权限的时候展示）
				out.println(sw.toString());	
			}
		}
	}

	private boolean haveAuth(String action) {
		HttpSession session = ((PageContext) this.getJspContext()).getSession();
		if (null != session) {
			AdminUserModel adminUser = (AdminUserModel) session.getAttribute("currentUser");
			if (null != adminUser) {
				String username = adminUser.getUsername();
				if(StringUtils.equals("admin", username)) {
					return true;
				}
				adminRoleService = ContextLoader.getCurrentWebApplicationContext().getBean(IAdminRoleService.class);
				adminMenuService = ContextLoader.getCurrentWebApplicationContext().getBean(IAdminMenuService.class);
				byte roleId = adminUser.getRole_id();
				AdminMenu adminMenu = adminMenuService.findByUrl(action);
				if (null != adminMenu) {
					String mid = adminMenu.getMid() + "";
					AdminRole adminRole = adminRoleService.findById(roleId);
					if (null != adminRole) {
						String menuIds = adminRole.getMenuIds();
						String[] splits = StringUtils.split(menuIds, ",");
						if (ArrayUtils.indexOf(splits, mid) > -1) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getShow() {
		return show;
	}

	public void setShow(String show) {
		this.show = show;
	}

}
