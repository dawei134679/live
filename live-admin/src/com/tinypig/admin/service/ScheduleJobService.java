package com.tinypig.admin.service;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 定时调度任务
 */
public class ScheduleJobService {
	private static SchedulerFactory gSchedulerFactory;
	private static String JOB_GROUP_NAME = "tinypig_admin_job";
	private static String TRIGGER_GROUP_NAME = "tinypig_admin_trigger";
	
	
	public static void initialize(String cfgPathPrefix) {
		try {
			gSchedulerFactory = new StdSchedulerFactory(cfgPathPrefix);
		} catch (SchedulerException e) {
			e.printStackTrace();
		} 	
	}
	
	public static Scheduler getScheduler(){
		Scheduler sched = null;
		try {
			if(gSchedulerFactory != null){
				sched = gSchedulerFactory.getScheduler();
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return sched;
	}
	
	/**
	 * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
	 * 
	 * @param jobName
	 *            任务名
	 * @param jobClass
	 *            任务
	 * @param time
	 *            时间设置，参考quartz说明文档
	 *            <!-- s m h d m w(?) y(?) -->,   分别对应: 秒>分>小时>日>月>周>年
	 *            0 0 0 29 3 2016  就是 2016年 3月 29日 0点 0分 0秒
	 *            "0 15 10 * * ? 2005" 2005年的每天早上10：15触发 
	 *            
	 *          "0 0 12 * * ?" 每天中午十二点触发 
	 * 			"0 15 10 ? * *" 每天早上10：15触发 
	 * 			"0 15 10 * * ?" 每天早上10：15触发 
	 * 			"0 15 10 * * ? *" 每天早上10：15触发 
	 * 			"0 15 10 * * ? 2005" 2005年的每天早上10：15触发 
	 * 			"0 * 14 * * ?" 每天从下午2点开始到2点59分每分钟一次触发 
	 * 			"0 0/5 14 * * ?" 每天从下午2点开始到2：55分结束每5分钟一次触发 
	 * 			"0 0/5 14,18 * * ?" 每天的下午2点至2：55和6点至6点55分两个时间段内每5分钟一次触发 
	 * 			"0 0-5 14 * * ?" 每天14:00至14:05每分钟一次触发 
	 * 			"0 10,44 14 ? 3 WED" 三月的每周三的14：10和14：44触发 
	 * 			"0 15 10 ? * MON-FRI" 每个周一、周二、周三、周四、周五的10：15触发 
	 * 			"0 15 10 15 * ?" 每月15号的10：15触发 
	 * 			"0 15 10 L * ?" 每月的最后一天的10：15触发 
	 * 			"0 15 10 ? * 6L" 每月最后一个周五的10：15 
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static void addJob(String jobName, String jobClass, String time) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			JobDetail jobDetail = new JobDetail(jobName, JOB_GROUP_NAME, Class.forName(jobClass));
			CronTrigger trigger = new CronTrigger(jobName, TRIGGER_GROUP_NAME);
			trigger.setCronExpression(time);
			sched.scheduleJob(jobDetail, trigger);
			if (!sched.isShutdown()) {
				sched.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 添加一个定时任务
	 * 
	 * @param jobName
	 *            任务名
	 * @param jobGroupName
	 *            任务组名
	 * @param triggerName
	 *            触发器名
	 * @param triggerGroupName
	 *            触发器组名
	 * @param jobClass
	 *            任务
	 * @param time
	 *            时间设置，参考quartz说明文档
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
			String jobClass, String time) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			JobDetail jobDetail = new JobDetail(jobName, jobGroupName, Class.forName(jobClass));
			CronTrigger trigger = new CronTrigger(triggerName, triggerGroupName);
			trigger.setCronExpression(time);
			sched.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
	 * 
	 * @param jobName
	 * @param time
	 */
	public static void modifyJobTime(String jobName, String time) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			CronTrigger trigger = (CronTrigger) sched.getTrigger(jobName, TRIGGER_GROUP_NAME);
			if (trigger == null) {
				return;
			}
			String oldTime = trigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(time)) {
				JobDetail jobDetail = sched.getJobDetail(jobName, JOB_GROUP_NAME);
				Class<?> objJobClass = jobDetail.getJobClass();
				String jobClass = objJobClass.getName();
				removeJob(jobName);

				addJob(jobName, jobClass, time);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 修改一个任务的触发时间
	 * 
	 * @param triggerName
	 * @param triggerGroupName
	 * @param time
	 */
	public static void modifyJobTime(String triggerName, String triggerGroupName, String time) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerName, triggerGroupName);
			if (trigger == null) {
				return;
			}
			String oldTime = trigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(time)) {
				CronTrigger ct = (CronTrigger) trigger;
				ct.setCronExpression(time);
				sched.resumeTrigger(triggerName, triggerGroupName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
	 * 
	 * @param jobName
	 */
	public static void removeJob(String jobName) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.pauseTrigger(jobName, TRIGGER_GROUP_NAME);
			sched.unscheduleJob(jobName, TRIGGER_GROUP_NAME);
			sched.deleteJob(jobName, JOB_GROUP_NAME);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 移除一个任务
	 * 
	 * @param jobName
	 * @param jobGroupName
	 * @param triggerName
	 * @param triggerGroupName
	 */
	public static void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.pauseTrigger(triggerName, triggerGroupName);
			sched.unscheduleJob(triggerName, triggerGroupName);
			sched.deleteJob(jobName, jobGroupName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	
	/**
	 * 移除一个任务
	 * 
	 * @param jobName
	 * @param jobGroupName
	 * @param triggerName
	 * @param triggerGroupName
	 */
	public static boolean existJob(String jobName, String jobGroupName) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			JobDetail jobDetail = sched.getJobDetail(jobName, jobGroupName);
			return jobDetail != null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 移除一个任务
	 * 
	 * @param jobName
	 * @param jobGroupName
	 * @param triggerName
	 * @param triggerGroupName
	 */
	public static boolean existTrigger(String triggerName, String triggerGroupName) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			Trigger trigger = sched.getTrigger(triggerName, triggerGroupName);
			return trigger != null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	
	/**
	 * 启动所有定时任务
	 */
	public static void startJobs() {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.start();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 关闭所有定时任务
	 */
	public static void shutdownJobs() {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			if (!sched.isShutdown()) {
				sched.shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/** 
     * 启动一个调度对象 
     * @throws SchedulerException 
     */  
    public  void start() throws SchedulerException  
    {   
    	try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			if (!sched.isStarted()) {
				sched.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
    }  
      
    /** 
     * 检查调度是否启动 
     * @return 
     * @throws SchedulerException 
     */  
    public boolean isStarted() throws SchedulerException  
    {  
    	try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			return sched.isStarted();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
    }  
    
    /** 
     * 暂停调度中所有的job任务 
     * @throws SchedulerException 
     */  
    public void pauseAll() throws SchedulerException  
    {  
    	try {
			Scheduler scheduler = gSchedulerFactory.getScheduler();
			scheduler.pauseAll();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
    }  
    
    /** 
     * 恢复调度中所有的job的任务 
     * @throws SchedulerException 
     */  
    public  void resumeAll() throws SchedulerException  
    {  
    	try {
			Scheduler scheduler = gSchedulerFactory.getScheduler();
			scheduler.resumeAll();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
    }  
	
	  
	public static class TestJob implements Job {  
	  
	    @SuppressWarnings("deprecation")  
	    public void execute(JobExecutionContext arg0) throws JobExecutionException {  
	        System.out.println(Calendar.getInstance().getTime().toLocaleString()+ "★★★★★★★★★★★");  
	    }  
	  
	}  
	
	public static void main(String[] args) throws InterruptedException{
        ScheduleJobService.TestJob job = new ScheduleJobService.TestJob();  
        String job_name ="test"+new Date();  
        
		System.out.println("【系统启动】开始(每1秒输出一次)...");  
		ScheduleJobService.addJob(job_name, job.getClass().getName(), "0/1 * * * * ?");  
		  
		Thread.sleep(5000);  
		System.out.println("【修改时间】开始(每2秒输出一次)...");  
		ScheduleJobService.modifyJobTime(job_name, "10/2 * * * * ?");  
		Thread.sleep(6000);  
		System.out.println("【移除定时】开始...");  
		ScheduleJobService.removeJob(job_name);  
		System.out.println("【移除定时】成功");  
		  
		System.out.println("/n【再次添加定时任务】开始(每10秒输出一次)...");  
		ScheduleJobService.addJob(job_name, job.getClass().getName(), "*/10 * * * * ?");  
		Thread.sleep(60000);  
		System.out.println("【移除定时】开始...");  
		ScheduleJobService.removeJob(job_name);  
		System.out.println("【移除定时】成功");  
	}

}
