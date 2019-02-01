package com.tinypig.admin.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mashape.unirest.http.Unirest;
import com.tinypig.admin.async.AsyncManager;
import com.tinypig.admin.db.DataSource;
import com.tinypig.admin.util.Constant;
import com.tinypig.admin.util.RedisContant;

/**
 * Servlet implementation class GlobalAware
 */
@WebServlet("/GlobalAwareServlet")
public class GlobalAwareServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GlobalAwareServlet() {
        super();
        
        System.gc();
        Unirest.setConcurrency(2000, 2000);
      
        //准备环境路径 
  		String rootPrefix = this.getClass().getResource("/").getPath();
  		rootPrefix = rootPrefix.substring(0,rootPrefix.lastIndexOf("classes"));
          
  		//构造datasource连接池
        DataSource.read(rootPrefix+"db.properties",rootPrefix+"db.json");
        
        Constant.initialize(rootPrefix+"system.properties");
        RedisContant.initialize(rootPrefix+"redis.properties");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public void destroy(){
		try {
			AsyncManager.getInstance().onDestroyed();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Unirest.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		}
		DataSource.instance.destroy();
	}
}
