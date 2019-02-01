package com.mpig.api.dictionary.lib;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.mpig.api.SqlTemplete;
import com.mpig.api.db.DataSource;
import com.mpig.api.model.TaskConfigModel;
import com.mpig.api.utils.VarConfigUtils;


/**
 *任务配置数据
 *@author jack.zhang
 *@date 2016-3-17
 */
public class TaskConfigLib implements SqlTemplete{
	private static final Logger logger = Logger.getLogger(TaskConfigLib.class);
	private final static TaskConfigLib instance = new TaskConfigLib();
	public static TaskConfigLib getInstance() {
		return instance;
	}
	
	/**
	 * 任务名称
	 */
	public static final String BoundPhone = "BoundPhone";			//绑定手机
	public static final String CompleteData = "CompleteData";		//完善个人资料
	public static final String Authentication = "Authentication";	//身份认证
	public static final String Living15Mins = "Living15Mins";		//开播15分钟
	public static final String Prepaid = "Prepaid";					//充值
	public static final String SendGift = "SendGift";				//送礼
	public static final String SendDanmaku = "SendDanmaku";			//发送弹幕
	public static final String Watching30Mins = "Watching30Mins";	//观看30分钟
	public static final String RoomsEntered = "RoomsEntered";		//20次进房
	public static final String SendLetter = "SendLetter";			//发送私信
	public static final String NewbieFinished = "NewbieFinished";			//新手任务完成
	
	public static final String Charge500 = "Charge500";			//首冲超过500元
	public static final String Charge10 = "Charge10";			//首冲超过10元
	public static final String FollowAnchor = "FollowAnchor";//首次关注一个主播
	public static final String SendPopularGift = "SendPopularGift";//送人气猪礼物
	
	public static final String WatchLive5Mins = "WatchLive5Mins";//观看直播5分钟
	public static final String SendLouder = "SendLouder";//每天发射喇叭
	public static final String ChargeOk = "ChargeOk";//每天充值成功
	public static final String SendPopular5 = "SendPopular5";//每天累计送出5个小萌猪
	public static final String Follow3Anchor = "Follow3Anchor";//每天关注3位主播

	//12.8号新增日常充值
	public static final String Daily_charge30 = "Daily_charge30";
	public static final String Daily_charge298 = "Daily_charge298";
	public static final String Daily_charge500 = "Daily_charge500";
	
	/**
	 * 任务类型 
	 */
	public enum TaskFor{
		Newbie,
		Daily
	}
	
	/**
	 * 任务提交方式 
	 */
	public enum TaskCommitFor{
		Manual,
		Auto
	}
	
	/**
	 * 任务状态 
	 */
	public enum TaskState{
		Accepted,
		Finished,
		Commited
	}
	
	/**
	 * 所有任务表
	 */
	private final Map<Integer,TaskConfigModel> taskPool = new HashMap<Integer,TaskConfigModel>();
	
	
	/**
	 * 所有下掉的任务
	 */
	private final Map<Integer,TaskConfigModel> taskPoolHasDown = new HashMap<Integer,TaskConfigModel>();
	
	/**
	 * 所有任务对应的类型
	 */
	private final Map<Integer,TaskFor> taskTypePool = new HashMap<Integer,TaskFor>();
	
	/**
	 * 日常任务表
	 */
	private final Map<Integer,TaskConfigModel> taskPoolByDaily = new HashMap<Integer,TaskConfigModel>();
	private final Map<Integer,TaskConfigModel> taskPoolByDailyUnModify = Collections.unmodifiableMap(taskPoolByDaily);
	
	/**
	 * 新手任务表
	 */
	private final Map<Integer,TaskConfigModel> taskPoolByNewbie = new HashMap<Integer,TaskConfigModel>();
	private final Map<Integer,TaskConfigModel> taskPoolByNewbieUnModify = Collections.unmodifiableMap(taskPoolByNewbie);
	
	/**
	 * 日常任务事件关联的任务列表
	 */
	private final Map<String,Set<Integer>> taskEventByDaily = new HashMap<String,Set<Integer>>();
	
	/**
	 * 新手任务事件关联的任务列表
	 */
	private final Map<String,Set<Integer>> taskEventByNewbie = new HashMap<String,Set<Integer>>();
	
	/**
	 * 新手任务完成奖励的任务
	 */
	private TaskConfigModel newBieRewardTask;

	/**
	 * 获取任务配置数据
	 * @param type
	 * @return
	 */
	public Collection<TaskConfigModel> getTaskListFor(TaskFor type) {
		if(type == TaskFor.Daily){
			return taskPoolByDailyUnModify.values();
		}else{
			return taskPoolByNewbieUnModify.values();
		}
	}

	
	/**
	 * 获取任务配置数据
	 * @param id
	 * @return
	 */
	public TaskConfigModel getTaskConfigFor(int id) {
		return taskPool.get(id);
	}
	
	/**
	 * 获取已经下掉的任务配置数据
	 * @param id
	 * @return
	 */
	public TaskConfigModel getTaskConfigForDown(int id) {
		return taskPoolHasDown.get(id);
	}
	
	/**
	 * 获取任务配置数据
	 * @param taskFor
	 * @return
	 */
	public Set<Integer> getAllTaskConfig(TaskFor taskFor) {
		return Collections.unmodifiableSet(taskFor == TaskFor.Newbie?taskPoolByNewbie.keySet():taskPoolByDaily.keySet());
	}
	
	public void dumpTaskInfo(){
		Collection<TaskConfigModel> values = taskPool.values();
		for (Iterator<TaskConfigModel> iterator = values.iterator(); iterator.hasNext();) {
			TaskConfigModel taskConfigModel = (TaskConfigModel) iterator.next();
			logger.info(String.format("id:%d,name:%s,valid:%d,reward:%s", taskConfigModel.getId(),taskConfigModel.getName(),
					taskConfigModel.getIsValid(),taskConfigModel.getRewards()));
		}
	}
	
	/**
	 * 获取任务数据依据发生的事件
	 * @param type
	 * @param what
	 * @return
	 */
	public List<TaskConfigModel> getTaskConfigForEventWithoutCount(TaskFor type, final String what) {
		Set<Integer> taskList = type == TaskFor.Daily?taskEventByDaily.get(what):taskEventByNewbie.get(what);
		List<TaskConfigModel> result = null;
		if(taskList.size() > 0){
			for (Iterator<Integer> iterator = taskList.iterator(); iterator.hasNext();) {
				Integer id = (Integer) iterator.next();
				TaskConfigModel taskConfigFor = getTaskConfigFor(id);
				if(result == null){
					result = new ArrayList<TaskConfigModel>();
				}
				result.add(taskConfigFor);
			}
		}
		return result;
	}
	
	/**
	 * 依据触发的事件获取任务数据 
	 * @param what
	 * @return
	 */
	public List<TaskConfigModel> getTaskConfigForEventWithoutCount(final String what) {
		List<TaskConfigModel> taskConfigForDaily = getTaskConfigForEventWithoutCount(TaskFor.Daily,what);
		List<TaskConfigModel> taskConfigForNewBie = getTaskConfigForEventWithoutCount(TaskFor.Newbie,what);
		if(taskConfigForDaily == null && taskConfigForNewBie == null){
			return null;
		}
		List<TaskConfigModel> result = new ArrayList<TaskConfigModel>();
		if(taskConfigForDaily != null){
			result.addAll(taskConfigForDaily);
		}
		if(taskConfigForNewBie != null){
			result.addAll(taskConfigForNewBie);
		}
		return result;
	}
	
	/**
	 * 是否有关于某事件的任务
	 * @param what
	 * @return
	 */
	public boolean hasTaskAboutWhat(final String what) {
		return taskEventByDaily.containsKey(what)||taskEventByNewbie.containsKey(what);
	}
	
	/**
	 * 获取任务数据依据发生的事件
	 * @param type
	 * @param what
	 * @param count
	 * @return
	 */
	public List<TaskConfigModel> getTaskConfigForEvent(TaskFor type, final String what, int count) {
		Set<Integer> taskList = type == TaskFor.Daily?taskEventByDaily.get(what):taskEventByNewbie.get(what);
		List<TaskConfigModel> result = null;
		if(taskList.size() > 0){
			for (Iterator<Integer> iterator = taskList.iterator(); iterator.hasNext();) {
				Integer id = (Integer) iterator.next();
				TaskConfigModel taskConfigFor = getTaskConfigFor(id);
				if(taskConfigFor.getTargetCount() <= count){
					if(result == null){
						result = new ArrayList<TaskConfigModel>();
					}
					result.add(taskConfigFor);
				}
			}
		}
		return result;
	}
	
	/**
	 * 依据触发的事件获取任务数据 
	 * @param what
	 * @param count
	 * @return
	 */
	public List<TaskConfigModel> getTaskConfigForEvent(final String what, int count) {
		List<TaskConfigModel> taskConfigForDaily = getTaskConfigForEvent(TaskFor.Daily,what,count);
		List<TaskConfigModel> taskConfigForNewBie = getTaskConfigForEvent(TaskFor.Newbie,what,count);
		if(taskConfigForDaily == null && taskConfigForNewBie == null){
			return null;
		}
		List<TaskConfigModel> result = new ArrayList<TaskConfigModel>();
		if(taskConfigForDaily != null){
			result.addAll(taskConfigForDaily);
		}
		if(taskConfigForNewBie != null){
			result.addAll(taskConfigForNewBie);
		}
		return result;
	}
	
	/**
	 * 获取任务编号依据发生的事件
	 * @param type
	 * @return
	 */
	public List<TaskConfigModel> getTaskConfigListForType(final TaskFor type) {
		return new ArrayList<TaskConfigModel>((Collection<? extends TaskConfigModel>) (type == TaskFor.Daily ? taskPoolByDaily.values() : taskPoolByNewbie.values()));
	}
	
	/**
	 * 获取一些配置数据
	 * @param taskAllListDynamicNew
	 * @return
	 */
	public List<TaskConfigModel> getSomeTaskConfigList(final Set<Integer> taskAllListDynamicNew) {
		if(taskAllListDynamicNew.isEmpty()){
			return null;
		}
		List<TaskConfigModel> result = new ArrayList<TaskConfigModel>();
		for (Integer id : taskAllListDynamicNew) {
			TaskConfigModel taskConfigFor = getTaskConfigFor(id);
			if(taskConfigFor != null){
				result.add(taskConfigFor);
			}
		}
		return result;
	}
	
	
	
	public synchronized boolean loadTaskConfigFromDb(){
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		boolean bupdateOk = false;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuConfig).getConnection();
			statement = conn.prepareStatement(SQL_GetTaskConfigAll);

			rs = statement.executeQuery();
			if (rs != null) {
				taskPoolByDaily.clear();
				taskPoolByNewbie.clear();
				taskEventByDaily.clear();
				taskEventByNewbie.clear();
				taskTypePool.clear();
				taskPool.clear();
				taskPoolHasDown.clear();
				logger.info("+++++++++++++++++taskPool clear+++++++++++++++++");
				int nCount = 0;
				while (rs.next()) {
					TaskConfigModel taskModel = new TaskConfigModel();
					logger.info(">>>>>>>>><updateTask one:>"+ rs.getInt("id")+ ":" + rs.getString("name")+">>"+rs.getInt("isValid"));
					taskModel.populateFromResultSet(rs);
					
					//记录下掉的任务列表
					if(taskModel.getIsValid() != 1){
						taskPoolHasDown.put(new Integer(taskModel.getId()), taskModel);
					}
					taskPool.put(new Integer(taskModel.getId()), taskModel);
					
					if(taskModel.getType() == TaskFor.Daily){
						taskPoolByDaily.put(taskModel.getId(), taskModel);
						Set<Integer> relativeTasklist = taskEventByDaily.get(taskModel.eventKey());
						if(relativeTasklist == null){
							relativeTasklist = new HashSet<Integer>();
							taskEventByDaily.put(taskModel.eventKey(), relativeTasklist);
						}
						relativeTasklist.add(taskModel.getId());
						taskEventByDaily.put(taskModel.eventKey(), relativeTasklist);
					}else if(taskModel.getType() == TaskFor.Newbie){
						taskPoolByNewbie.put(taskModel.getId(), taskModel);
						Set<Integer> relativeTasklist = taskEventByNewbie.get(taskModel.eventKey());
						if(relativeTasklist == null){
							relativeTasklist = new HashSet<Integer>();
							taskEventByNewbie.put(taskModel.eventKey(), relativeTasklist);
						}
						relativeTasklist.add(taskModel.getId());
						taskEventByNewbie.put(taskModel.eventKey(), relativeTasklist);
					}
					taskTypePool.put(taskModel.getId(), taskModel.getType());
					if(taskModel.getWhat().equals(NewbieFinished)){
						TaskConfigLib.getInstance().setNewBieRewardTask(taskModel);
					}
					nCount++;
				}
				bupdateOk = true;
				logger.info("<loadTaskConfigFromDb->OK: count:>" + taskPool.size()+", down task count:"+taskPoolHasDown.size());
			} else {
				logger.info("<loadTaskConfigFromDb->SQL RETURN NULL>");
			}
		} catch (SQLException e) {
			logger.error("<loadTaskConfigFromDb->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<loadTaskConfigFromDb->Exception>" + e.getMessage());
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
			} catch (SQLException e) {
				logger.error("<loadTaskConfigFromDb->finally->Exception>" + e.getMessage());
			}
		}

		return bupdateOk;
	}

	public TaskConfigModel getNewBieRewardTask() {
		return newBieRewardTask;
	}

	public void setNewBieRewardTask(TaskConfigModel newBieRewardTask) {
		this.newBieRewardTask = newBieRewardTask;
	}
}
