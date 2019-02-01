package com.tinypig.admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

import com.tinypig.admin.constant;
import com.tinypig.admin.model.AdminMenuModel;
import com.tinypig.admin.util.DbUtil;

public class AdminMenuDao {
	
	private final static AdminMenuDao instance = new AdminMenuDao();

	public static AdminMenuDao getInstance() {
		return instance;
	}

	/**
	 * 
	 * @Description: 查询角色菜单树
	 * @param rid
	 * @return   
	 * @return JSONArray  
	 * @throws
	 * @author guojp
	 * @date 2016-6-28
	 */
	public static JSONArray getMenuListByMenuIds(String menuids) {
		
		
		List list = new ArrayList();
		
		//生成全的menuids，包括父级菜单
		Set<Integer> menuset = new TreeSet<Integer>();
		getRuleIds(menuids, menuset);
		menuset.remove(Integer.parseInt("0"));
		Iterator<Integer> it=menuset.iterator();
		String newgids = "";
		while(it.hasNext())
		{
			int id = (int)it.next();
			newgids = newgids + id + ",";
		}
		if(!"".equals(newgids)){
			int len = newgids.length();
			newgids = newgids.substring(0, len-1);
		}
		
		//从数据库查询该角色的菜单List<AdminUserModel>  list =  new ArrayList<AdminUserModel>();
		String sql = "select * from admin_menu t where t.mid in (" + newgids + ") and t.`show` = 1 order by t.sort asc,t.pid asc, t.mid asc";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					AdminMenuModel aMenuModel = new AdminMenuModel().populateFromResultSet(rs);
					if (aMenuModel != null) {
						list.add(aMenuModel);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					rs.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				System.out.println("login->finally->exception:" + e.getMessage());
			}
		}
		
		//根据数据库返回结果菜单结果生成json串返回前端
		List listAll = new ArrayList();
		Map map0 = new HashMap();
		int mid0 = 0;
		List list0 = new ArrayList();
		for(int i=0;i<list.size();i++){//根节点
			AdminMenuModel model = (AdminMenuModel)list.get(i);
			if(model.getPid() == 0){
				map0.put("text", model.getMenuname());
				mid0 = model.getMid();
				break;
			}
		}
		for(int j=0;j<list.size();j++){//中间节点
			AdminMenuModel model1 = (AdminMenuModel)list.get(j);
			if(model1.getPid() == mid0 && mid0 != 0){
				Map map1 = new HashMap();
				map1.put("text", model1.getMenuname());
				int mid1 = model1.getMid();
				List list1 = new ArrayList();
				for(int k=0;k<list.size();k++){//子节点
					AdminMenuModel model2 = (AdminMenuModel)list.get(k);
					if(model2.getPid() == mid1){
						Map map2 = new HashMap();
						Map map21 = new HashMap();
						map21.put("url", model2.getUrl());
						map2.put("text", model2.getMenuname());
						map2.put("attributes", map21);
						list1.add(map2);
					}
					
				}
				map1.put("children", list1);
				list0.add(map1);
			}
		}
		if(map0.get("text") != null){
			map0.put("children", list0);
			listAll.add(map0);
		}
			
		JSONArray jsonArray = (JSONArray)JSONSerializer.toJSON(listAll); 
		return jsonArray;
	}
	
	
	/**
	 * 
	 * @Description: 获取所有权限树,并根据用户权限设置多选按钮的选中状态
	 * @param menuids
	 * @return   
	 * @return JSONArray  
	 * @throws
	 * @author guojp
	 * @date 2016-7-1
	 */
	public static JSONArray getAllMenuTree(String menuids) {
		List<AdminMenuModel> list = new ArrayList<AdminMenuModel>();
		
		//从数据库查询所有权限
		String sql = "select * from admin_menu t where t.`show` = 1 order by t.pid asc, t.mid asc";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					AdminMenuModel aMenuModel = new AdminMenuModel().populateFromResultSet(rs);
					if (aMenuModel != null) {
						list.add(aMenuModel);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					rs.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				System.out.println("login->finally->exception:" + e.getMessage());
			}
		}
		
		//根据数据库返回结果菜单结果生成json串返回前端
		AdminMenuModel model = null;
		for(int i=0;i<list.size();i++){//根节点
			model = (AdminMenuModel)list.get(i);
			if(model.getPid() == 0){
				break;
			}
		}
		String[] menuArray = menuids.split(",");
		List<String> menuList = Arrays.asList(menuArray);
		StringBuffer content = new StringBuffer("[");
		printRulelTree(model, menuList, content, list);
		content.append("]");
		String newcontent = content.toString().replaceAll(",]", "]");
		
		
//		List listAll = new ArrayList();
//		Map map0 = new HashMap();
//		int mid0 = 0;
//		List list0 = new ArrayList();
//		for(int i=0;i<list.size();i++){//根节点
//			AdminMenuModel model = (AdminMenuModel)list.get(i);
//			if(model.getPid() == 0){
//				map0.put("text", model.getMenuname());
//				mid0 = model.getMid();
//				break;
//			}
//		}
//		for(int j=0;j<list.size();j++){//中间节点
//			AdminMenuModel model1 = (AdminMenuModel)list.get(j);
//			if(model1.getPid() == mid0 && mid0 != 0){
//				Map map1 = new HashMap();
//				map1.put("text", model1.getMenuname());
//				int mid1 = model1.getMid();
//				List list1 = new ArrayList();
//				for(int k=0;k<list.size();k++){//子节点
//					AdminMenuModel model2 = (AdminMenuModel)list.get(k);
//					if(model2.getPid() == mid1){
//						Map map2 = new HashMap();
//						Map map21 = new HashMap();
//						map21.put("url", model2.getUrl());
//						map2.put("text", model2.getMenuname());
//						map2.put("attributes", map21);
//						list1.add(map2);
//					}
//					
//				}
//				map1.put("children", list1);
//				list0.add(map1);
//			}
//		}
//		if(map0.get("text") != null){
//			map0.put("children", list0);
//			listAll.add(map0);
//		}
		JSONArray jsonArray = (JSONArray)JSONSerializer.toJSON(newcontent); 
		return jsonArray;
	}
	
	private static void printRulelTree(AdminMenuModel model, List menuList, StringBuffer sBuffer, List<AdminMenuModel> adminMenuList) {
		sBuffer.append("{\"id\":").append(model.getMid()).append(",").append("\"text\":\"").append(model.getMenuname()).append("\"");
		if (menuList.contains(String.valueOf(model.getMid())))
			sBuffer.append(",\"checked\":true");
		boolean hasChild = false;
		for(AdminMenuModel amodel : adminMenuList){
			if(model.getMid() == amodel.getPid()){
				hasChild = true;
				break;
			}
		}
		if(hasChild){
			sBuffer.append(",\"children\":[");
			for(AdminMenuModel amodel : adminMenuList){
				if(model.getMid() == amodel.getPid()){
					printRulelTree(amodel, menuList, sBuffer, adminMenuList);
				}
			}
			sBuffer.append("]},");
		}else{
			sBuffer.append("},");
		}
	}
	
	//将所有父级菜单id放入其中
	private static void getRuleIds(String menuids, Set<Integer> menuset){
		String[] ids = menuids.split(",");
		for(String id : ids)
			menuset.add(Integer.parseInt(id));

		String sql = "select distinct pid from admin_menu t where t.mid in ("+menuids+")";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String pids = "";
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_admin,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					pids = pids + rs.getInt("pid") + ",";
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					rs.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				System.out.println("login->finally->exception:" + e.getMessage());
			}
		}

		if(!"".equals(pids)){
			int length = pids.length();
			String newpids = pids.substring(0, length-1);
			getRuleIds(newpids, menuset);
		}
	}
}
