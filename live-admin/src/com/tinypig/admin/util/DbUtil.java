package com.tinypig.admin.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tinypig.admin.config.GlobalConfig;
import com.tinypig.admin.db.DataSource;

public class DbUtil {
	private String dbUrl;
	private String dbUserName;
	private String dbPassword;
	
	private static DbUtil instance = null;
	private static Object lock = new Object();
	private DbUtil(){
		dbUrl = GlobalConfig.getInstance().getDbUrl();
		dbUserName = GlobalConfig.getInstance().getDbUserName();
		dbPassword = GlobalConfig.getInstance().getDbPassword();
	}
	
	public static DbUtil instance(){
		synchronized (lock) {
			if(instance == null){
				instance = new DbUtil();
			}
		}
		return instance;
	}
	
	static{
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取链接
	 * @return
	 * @throws Exception
	 */
	public Connection getCon(String dbName,String type) throws Exception{
		return getConFromPool(type, dbName);
		
//		Connection con = null;
//		if ("master".equals(type)) {
//			con = DriverManager.getConnection(dbUrl+dbName,dbUserName,dbPassword);
//		}else {
//			
//			String dbUrl2 = GlobalConfig.getInstance().getDbUrl2();
//			System.out.println("dburl2:"+dbUrl2);
////			String dbUrl2 = dbUrl;
//			con = DriverManager.getConnection(dbUrl2+dbName,dbUserName,dbPassword);
//		}
//		return con;
	}
	
	public Connection getConFromPool(String type,String dbname){
		try {
			if ("master".equals(type)) {
				return DataSource.instance.getMasterPool(dbname).getConnection();
			}else {
				return DataSource.instance.getSlavePool(dbname).getConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * 获取链接
	 * @return
	 * @throws Exception
	 */
//	public Connection getCon(String dbName) throws Exception{
//		Connection con = DriverManager.getConnection(dbUrl+dbName,dbUserName,dbPassword);
//		return con;
//	}
	
	/**
	 * 获取链接,不填某个库名,可以跨库查询
	 * @return
	 * @throws Exception
	 */
	public Connection getCon(String type) throws Exception{
		Connection con = null;
		if ("master".equals(type)) {
			con=DriverManager.getConnection(dbUrl,dbUserName,dbPassword);
		}else {
//			String dbUrl2 = GlobalConfig.getInstance().getDbUrl2();
			String dbUrl2 = dbUrl;
			con=DriverManager.getConnection(dbUrl2,dbUserName,dbPassword);
		}
		
		return con;
	}
	
	/**
	 * �ر���ݿ�����
	 * @param con
	 * @throws Exception
	 */
	public void closeCon(Connection con) throws Exception{
		if(con!=null){
			con.close();
		}
	}
	
	public static void main(String[] args) {
		try {
			DbUtil.instance().getCon("","");
			System.out.println("��ݿ����ӳɹ�");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//简单的Query
	static public class DbSimpleQuery{
		public Connection con = null;
		public PreparedStatement pstmt = null;
		public ResultSet rs = null;
		
		public ResultSet simpleQuery(String Db,String sql){
			release();
			try {
				con = DbUtil.instance().getCon(Db,"slave");
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return rs;
		}
		public void release(){
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
