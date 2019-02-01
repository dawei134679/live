package com.mpig.api.servlet;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.mashape.unirest.http.Unirest;
import com.mpig.api.async.AsyncManager;
import com.mpig.api.db.DataSource;
import com.mpig.api.redis.core.RedisBucket;
import com.mpig.api.service.impl.SearchServiceImpl;

public class DestroyListener implements ServletContextListener{

	
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("LogicServer......contextDestroyed begin");
		SearchServiceImpl.destroy();
		AsyncManager.getInstance().onDestroyed();
		try {
			Unirest.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		RedisBucket.getInstance().destroy();
		DataSource.instance.destroy();
		
		System.out.println("LogicServer......contextDestroyed end");

        System.gc();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
