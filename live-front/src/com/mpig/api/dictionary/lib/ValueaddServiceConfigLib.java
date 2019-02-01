package com.mpig.api.dictionary.lib;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.mpig.api.SqlTemplete;
import com.mpig.api.dao.IValueaddPrivilegeDao;
import com.mpig.api.db.DataSource;
import com.mpig.api.model.ValueaddPrivilegeModel;
import com.mpig.api.utils.VarConfigUtils;

public class ValueaddServiceConfigLib {
	private static final Logger log = Logger.getLogger(ValueaddServiceConfigLib.class);
    private static final Map<String, ValueaddPrivilegeModel> privilege = new HashMap<String, ValueaddPrivilegeModel>();
    private static final Map<Integer, Map<String,Object>> addScoreConfig = new HashMap<Integer, Map<String,Object>>();
    
    public static synchronized boolean getList() {
    	List<ValueaddPrivilegeModel> privilegeModels = getPrivilegeList();
    	for(ValueaddPrivilegeModel privilegeModel :privilegeModels){
    		privilege.put(privilegeModel.getGid()+":"+privilegeModel.getLevel(), privilegeModel);
    	}
    	setAddScoreConfig();
    	return true;
	}
    
	public static Map<String, ValueaddPrivilegeModel> getValueaddPrivilege(){
		return privilege;
	}
	
	public static Map<Integer, Map<String,Object>> getAddSocreConfig(){
		return addScoreConfig;
	}
	
	/**
	 * 设置默认的排序规则讯息
	 */
	public static void setAddScoreConfig(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("gid", 43);
		map.put("level", 1);
		addScoreConfig.put(5, map);
		
		map = new HashMap<String,Object>();
		map.put("gid", 44);
		map.put("level", 1);
		addScoreConfig.put(10, map);
		
		map = new HashMap<String,Object>();
		map.put("gid", 45);
		map.put("level", 1);
		addScoreConfig.put(7, map);
		
		map = new HashMap<String,Object>();
		map.put("gid", 45);
		map.put("level", 2);
		addScoreConfig.put(9, map);
		
		map = new HashMap<String,Object>();
		map.put("gid", 45);
		map.put("level", 3);
		addScoreConfig.put(11, map);
		
		map = new HashMap<String,Object>();
		map.put("gid", 46);
		map.put("level", 1);
		addScoreConfig.put(15, map);
		
		map = new HashMap<String,Object>();
		map.put("gid", 46);
		map.put("level", 2);
		addScoreConfig.put(17, map);
		
		map = new HashMap<String,Object>();
		map.put("gid", 46);
		map.put("level", 3);
		addScoreConfig.put(19, map);
	}
	
	/**
	 * 获取增值服务特权的配置
	 * @return
	 */
	public static List<ValueaddPrivilegeModel> getPrivilegeList() {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		List<ValueaddPrivilegeModel> valueaddPrivilegeModels = new ArrayList<ValueaddPrivilegeModel>();
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuConfig).getConnection();
			statement = conn.prepareStatement(SqlTemplete.SQL_valueaddPrivilegeList);
			rs = statement.executeQuery();
			if (rs != null) {
				while(rs.next()){
					ValueaddPrivilegeModel valueaddPrivilegeModel = new ValueaddPrivilegeModel().populateFromResultSet(rs);
					valueaddPrivilegeModels.add(valueaddPrivilegeModel);
				}
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				log.error(e2.getMessage());
			}
		}
		return valueaddPrivilegeModels;
	}
}
