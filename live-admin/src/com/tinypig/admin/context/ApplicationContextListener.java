package com.tinypig.admin.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import com.tinypig.admin.config.GlobalConfig;
import com.tinypig.admin.filter.IPFilter;
import com.tinypig.admin.service.ScheduleJobService;
import com.tinypig.newadmin.common.HttpServerList;

public class ApplicationContextListener implements ServletContextListener {
	public static Scheduler scheduler = null;

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("ApplicationContextListener contextInitialized");
		try {
			String rootPrefix = this.getClass().getResource("/").getPath();
			rootPrefix = rootPrefix.substring(0,rootPrefix.lastIndexOf("classes"));
			
			GlobalConfig.getInstance().loadFromFile(rootPrefix + "config.properties");
			IPFilter.readAllWhiteIpList(rootPrefix + "whiteIpList.properties");
	        ScheduleJobService.initialize(rootPrefix + "quartz.properties");
	        HttpServerList.getInstance().loadFromFile(rootPrefix + "httpserverlist.properties");
	        
			scheduler = ScheduleJobService.getScheduler();
			scheduler.start();
			scheduler.resumeAll();
			checkJobExist();
		} catch (SchedulerException se) {
			System.out.println(se);
		}
	}

	private void checkJobExist() {
//		try {
//			if (!scheduler.checkExists(new JobKey("WaitPayOrderJob", "SayJobGroup"))) {
//				ScheduleJobService.addJob(WaitPayOrderJob.class, "WaitPayOrderJob", "SayJobGroup", "WaitPayOrderTrigger",
//						"0/10 * * * * ?");
//			}
//		} catch (SchedulerException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("The application stop...");
		try {
			scheduler = ScheduleJobService.getScheduler();
			if(scheduler != null){
				scheduler.shutdown();
				System.out.println("The scheduler shutdown...");
			}
		} catch (SchedulerException se) {
			System.out.println(se);
		}
	}

}