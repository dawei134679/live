package com.mpig.api.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.Logger;

/** 
 * 数据库操作辅助类 
 */  
public class DBHelper {

	private static final Logger logger = Logger.getLogger(DBHelper.class);
    /** 
     * 该语句必须是一个 SQL INSERT、UPDATE 或 DELETE 语句 
     * @param sql 
     * @param paramList：参数，与SQL语句中的占位符一一对应 
     * @return 
     * @throws Exception 
     */  
    public static int execute(final String dsName,final String sql,Boolean bl, Object... paramList) throws Exception {  
    	
        if(sql == null || sql.trim().equals("") || dsName == "") {  
            return 0;
        }  
        Connection conn = null;  
        PreparedStatement pstmt = null;  
        int result = 0;  
        try {
            conn = DataSource.instance.getPool(dsName).getConnection();
            
            pstmt = DBHelper.getPreparedStatement(conn, sql);  
            setPreparedStatementParam(pstmt, paramList);
            if(pstmt == null) {  
                return 0;  
            }  
            result = pstmt.executeUpdate();
            if (result > 0 && bl) {
				ResultSet rs = pstmt.executeQuery("SELECT LAST_INSERT_ID() as id");
				if (rs.next()) {
					result = rs.getInt("id");
				}
			}
        } catch (Exception e) {
        	logger.error("execute sql="+sql+" params="+Arrays.toString(paramList), e);
        } finally {
        	if (pstmt != null) {
				pstmt.close();
			}
        	if (conn != null) {
				conn.close();
			} 
        }  
        return result;  
    }  
      
    public static void setPreparedStatementParam(PreparedStatement pstmt, Object... paramList) throws Exception {
    	 if(pstmt == null || paramList == null ) {
            return;  
        }  
        int iLen = paramList.length;
        Object obj = null;
        
        for (int i = 0; i < iLen; i++) {
        	obj = paramList[i];
        	
            if(obj instanceof Integer) {
                pstmt.setInt(i+1, ((Integer)obj).intValue());
            } else if(obj instanceof Float) {
                pstmt.setFloat(i+1, ((Float)obj).floatValue());
            } else if(obj instanceof Double) {
                pstmt.setDouble(i+1, ((Double)obj).doubleValue());
            } else if(obj instanceof Date) {
            	DateFormat df = DateFormat.getDateTimeInstance();
                pstmt.setString(i+1, df.format((Date)obj));
            } else if(obj instanceof Long) {
                pstmt.setLong(i+1, ((Long)obj).longValue());
            } else if(obj instanceof String) {
                pstmt.setString(i+1, (String)obj);
            } else if(obj instanceof Boolean) {
				pstmt.setBoolean(i+1, ((Boolean)obj).booleanValue());
			} else if(obj instanceof Byte){
				pstmt.setByte(i+1, ((Byte)obj).byteValue());
			}
        }
    } 

    public static PreparedStatement getPreparedStatement(Connection conn, String sql) throws Exception {  
        if(conn == null || sql == null || sql.trim().equals("")) {
            return null;
        }
        PreparedStatement pstmt = null;
	    pstmt = conn.prepareStatement(sql.trim());
        return pstmt;
    }
}