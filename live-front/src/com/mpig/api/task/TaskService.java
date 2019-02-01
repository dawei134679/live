package com.mpig.api.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.chainsaw.Main;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mpig.api.dictionary.LevelConfig;
import com.mpig.api.dictionary.SignConfig;
import com.mpig.api.dictionary.lib.LevelConfigLib;
import com.mpig.api.dictionary.lib.SignConfigLib;
import com.mpig.api.dictionary.lib.TaskConfigLib;
import com.mpig.api.dictionary.lib.TaskConfigLib.TaskCommitFor;
import com.mpig.api.dictionary.lib.TaskConfigLib.TaskFor;
import com.mpig.api.dictionary.lib.TaskConfigLib.TaskState;
import com.mpig.api.dictionary.lib.UrlConfigLib;
import com.mpig.api.model.ConfigGiftModel;
import com.mpig.api.model.TaskConfigModel;
import com.mpig.api.model.UserAssetModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.model.UserVipInfoModel;
import com.mpig.api.modelcomet.CModProtocol;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.service.IUserItemService;
import com.mpig.api.service.impl.ConfigServiceImpl;
import com.mpig.api.service.impl.UserItemServiceImpl;
import com.mpig.api.service.impl.UserServiceImpl;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.EncryptUtils;
import com.mpig.api.utils.ValueaddServiceUtil;
import com.mpig.api.utils.VarConfigUtils;

/**
 * @ClassName: TaskManager.java
 * @Description: 任务管理器
 * 
 * @author jackzhang
 * @version V1.0
 * @Date Jun 3, 2016 8:54:03 PM
 */
public class TaskService {
	private static final Logger logger = Logger.getLogger(TaskService.class);
	private final static TaskService instance = new TaskService();

	final static Double COUNT_VIP_RESIGN_10 = 10.0;
	final static Double COUNT_VIP_RESIGN_5 = 5.0;

	public static TaskService getInstance() {
		return instance;
	}

	/**
	 * @param source 1#10#15#105
	 * @param str 10
	 * 获取indexof
	 */
	private int getIndexOfSharp(String source,String str) {
		String[] split = StringUtils.split(source,"#");
		for (int i = 0; i < split.length; i++) {
			if(StringUtils.equals(str, split[i])){
				return i;
			}
		}
		return -1;
	}

	/**
	 * 获取用户任务经验和等级消息
	 * 
	 * @param uid
	 * @return
	 */
	public HashMap<String, Object> getSummaryInfo(int uid) {
		final Long exp = UserServiceImpl.getInstance().getUserExp(uid);
		// 财富值+经验值-->等级
		Long wealth = 0l;
		UserAssetModel userAssetByUid = UserServiceImpl.getInstance().getUserAssetByUid(uid, false);
		if (userAssetByUid != null) {
			wealth = userAssetByUid.getWealth();
		}
		final int rate = 1;
		final Long curTotalExp = exp + wealth * rate;
		final int lv = LevelConfigLib.getSuitableLevel(curTotalExp);
		// final int lv = UserServiceImpl.getInstance().getUserLevel(uid) ;
		LevelConfig levelForCurLv = LevelConfigLib.getLevelForLv(lv);
		final Long displayExp = curTotalExp - levelForCurLv.getExp();
		int nextLv = lv + 1;
		LevelConfig levelForNextLv = LevelConfigLib.getLevelForLv(nextLv);
		if (levelForNextLv == null) {
			nextLv = lv;
			levelForNextLv = LevelConfigLib.getLevelForLv(nextLv);
		}

		final Long nextExp = levelForNextLv.getExp() - levelForCurLv.getExp();
		return new HashMap<String, Object>() {
			{
				this.put("exp", displayExp);// 当前经验值-当前等级经验值
				this.put("lv", lv);
				this.put("nextExp", nextExp);// 2个等级之间的差值
			}
		};
	}

	/**
	 * 初始化指定类型的可接受任务列表
	 * 
	 * @param uidStringfy
	 * @param taskFor
	 */
	public synchronized boolean initTaskAcceptList(String uidStringfy, TaskFor taskFor) {
		try {
			OtherRedisService.getInstance().clearUserAcceptedTask(uidStringfy, taskFor);
			OtherRedisService.getInstance().clearUserFinishedTask(uidStringfy, taskFor);
			OtherRedisService.getInstance().clearUserCommitedTask(uidStringfy, taskFor);
			List<TaskConfigModel> taskConfigListForSomeType = TaskConfigLib.getInstance()
					.getTaskConfigListForType(taskFor);
			StringBuffer taskAcceptStringfy = new StringBuffer();
			int len = taskConfigListForSomeType.size();
			for (Iterator<TaskConfigModel> iterator = taskConfigListForSomeType.iterator(); iterator.hasNext();) {
				TaskConfigModel taskConfigModel = (TaskConfigModel) iterator.next();
				if (taskConfigModel.getIsValid() == 1) {
					taskAcceptStringfy.append(taskConfigModel.getId() + ",0");
					taskAcceptStringfy.append("#");
				}
			}
			taskAcceptStringfy.deleteCharAt(taskAcceptStringfy.length() - 1);

			OtherRedisService.getInstance().updateUserAcceptedTask(uidStringfy, taskFor, taskAcceptStringfy.toString());
			if (taskFor == TaskFor.Newbie) {
				OtherRedisService.getInstance().updateUserAcceptedTaskFlagForNewbie(uidStringfy, len);
			}
		} catch (Exception e) {
			logger.error(e.toString());
			return false;
		}
		return true;
	}

	/**
	 * 保护已绑定手机号的用户任务错误
	 * @param ac	进行中
	 * @param fi	已完成
	 * @param co	已领取
	 */
	protected boolean checkBindPhoneTaks(int uid, List<JSONObject> ac, List<JSONObject> fi, List<JSONObject> co) {
		// tosy add check phone binding task miss
		boolean bNeedReGetStat = false;

		String strPhone = null;
		if (null != ac) {
			for (JSONObject jsonObject : ac) {
				TaskConfigModel acTask = JSONObject.parseObject(jsonObject.toJSONString(), TaskConfigModel.class);
				if (1 == acTask.getId()) {
					// check has phone already...
					UserBaseInfoModel userinfo = UserServiceImpl.getInstance().getUserbaseInfoByUid(uid, false);
					if (null != userinfo) {
						strPhone = userinfo.getPhone();
						// logger.error("checkBindPhoneTaks get error stat recover stat" + strPhone);
					}
					break;
				}
			}
		}

		// logger.error("checkBindPhoneTaks 111" + uid);

		if (null == strPhone || 11 != strPhone.length()) {
			// logger.error("checkBindPhoneTaks 111 return " + uid);

			return bNeedReGetStat;
		}

		// logger.error("checkBindPhoneTaks 222" + uid);
		bNeedReGetStat = true;

		int taskId = 1; // 1 = phone task
		TaskConfigModel taskConfigFor = TaskConfigLib.getInstance().getTaskConfigFor(taskId);
		String taskStr = String.valueOf(taskConfigFor.getId());
		String strUid = String.valueOf(uid);

		// logger.error("checkBindPhoneTaks 333" + uid);
		// check commited ... del ac and fi return
		if (null != co) {
			for (JSONObject jsonObject : co) {
				TaskConfigModel coTask = JSONObject.parseObject(jsonObject.toJSONString(), TaskConfigModel.class);
				if (1 == coTask.getId()) {
					// logger.error("checkBindPhoneTaks removeSomeTaskFromAccepted " + strUid + " " +
					// taskConfigFor.getType() + " " + taskStr);

					// TOSY DEBUG removeSomeTaskFromAccepted ~! 1,0
					removeSomeTaskFromAccepted(strUid, taskConfigFor.getType(), "1,0"); // taskStr

					// logger.error("checkBindPhoneTaks removeSomeTaskFromFinished " + strUid + " " +
					// taskConfigFor.getType() + " " + taskStr);

					removeSomeTaskFromFinished(strUid, taskConfigFor.getType(), taskStr);
					return bNeedReGetStat;
				}
			}
		}

		// logger.error("checkBindPhoneTaks 4444" + uid);
		// check finished ... del ac
		if (null != fi) {
			for (JSONObject jsonObject : fi) {
				TaskConfigModel fiTask = JSONObject.parseObject(jsonObject.toJSONString(), TaskConfigModel.class);
				if (1 == fiTask.getId()) {

					// logger.error("checkBindPhoneTaks removeSomeTaskFromAccepted " + strUid + " " +
					// taskConfigFor.getType() + " " + taskStr);
					removeSomeTaskFromAccepted(strUid, taskConfigFor.getType(), "1,0" /* taskStr */);
					return bNeedReGetStat;
				}
			}
		}

		// logger.error("checkBindPhoneTaks zzz" + uid);

		removeSomeTaskFromAccepted(strUid, taskConfigFor.getType(), "1,0"/* taskStr */);
		taskFinished(strUid, taskConfigFor.getType(), taskId);

		return bNeedReGetStat;
	}

	/**
	 * 获取任务列表
	 * 
	 * @param uid
	 * @param taskFor
	 * @return
	 */
	public Map<String, List<JSONObject>> taskAllList(int uid, TaskFor taskFor) {
		HashMap<String, List<JSONObject>> map = new HashMap<String, List<JSONObject>>();
		List<JSONObject> taskFinishedList = taskFinishedList(uid, taskFor);
		List<JSONObject> taskAcceptedList = taskAcceptedList(String.valueOf(uid), taskFor);
		List<JSONObject> taskCommitedList = taskCommitedList(String.valueOf(uid), taskFor);

		// tosy add check task
		try {
			if (checkBindPhoneTaks(uid, taskAcceptedList, taskFinishedList, taskCommitedList)) {
				taskFinishedList = taskFinishedList(uid, taskFor);
				taskAcceptedList = taskAcceptedList(String.valueOf(uid), taskFor);
				taskCommitedList = taskCommitedList(String.valueOf(uid), taskFor);
			}
		} catch (Exception e) {
			logger.error("checkBindPhoneTaks" + e.toString());
		}

		map.put("task_accepted", taskAcceptedList);
		map.put("task_finished", taskFinishedList);
		map.put("task_commited", taskCommitedList);
		return map;
	}

	/**
	 * 获取动态派发的新任务 日常任务明日生效
	 * 
	 * @param uid
	 * @param taskFor
	 * @return
	 */
	public synchronized Set<Integer> taskAllListDynamicNew(int uid, TaskFor taskFor) {
		List<JSONObject> allHas = new ArrayList<JSONObject>();
		List<JSONObject> taskAcceptedListNewbie = taskAcceptedList(String.valueOf(uid), taskFor);
		if (taskAcceptedListNewbie != null) {
			allHas.addAll(taskAcceptedListNewbie);
		}
		List<JSONObject> taskFinishedListNewbie = taskFinishedList(uid, taskFor);
		if (taskFinishedListNewbie != null) {
			allHas.addAll(taskFinishedListNewbie);
		}
		List<JSONObject> taskCommitedListNewbie = taskCommitedList(String.valueOf(uid), taskFor);
		if (taskCommitedListNewbie != null) {
			allHas.addAll(taskCommitedListNewbie);
		}

		Set<Integer> hasDispatchTask = new HashSet<Integer>();
		for (JSONObject obj : allHas) {
			if (obj.containsKey("id")) {
				Integer taskId = obj.getInteger("id");
				hasDispatchTask.add(taskId);
			}
		}
		Set<Integer> allTaskConfig = TaskConfigLib.getInstance().getAllTaskConfig(taskFor);
		Set<Integer> dynamicSet = new HashSet<Integer>();
		dynamicSet.addAll(allTaskConfig);
		dynamicSet.removeAll(hasDispatchTask);
		return dynamicSet;
	}

	/**
	 * 获取指定类型的尚未完成的任务列表
	 * 
	 * @param uid
	 * @param taskFor
	 */
	public List<JSONObject> taskFinishedList(int uid, TaskFor taskFor) {
		String uidStringfy = String.valueOf(uid);
		String userAcceptedTask = OtherRedisService.getInstance().getUserFinishedTask(uidStringfy, taskFor);
		if (userAcceptedTask == null || StringUtils.isEmpty(userAcceptedTask)) {
			return null;
		}

		String[] taskStringfy = userAcceptedTask.split("#");
		int count = taskStringfy.length;
		if (count > 0) {
			List<JSONObject> result = new ArrayList<JSONObject>();
			for (int i = 0; i < count; i++) {
				int taskId = Integer.parseInt(taskStringfy[i]);
				TaskConfigModel taskConfigForEvent = TaskConfigLib.getInstance().getTaskConfigFor(taskId);
				if (taskConfigForEvent == null) {
					logger.error(String.format("taskFinishedList>%d has no config", taskId));
					continue;
				}

				if (taskConfigForEvent.getIsValid() == 1) {
					taskConfigForEvent.setState(TaskState.Finished);
					taskConfigForEvent.setType(taskFor);
					taskConfigForEvent.setCurrentCount(taskConfigForEvent.getTargetCount());
					result.add(taskConfigForEvent.toJsonObject());
				}
			}
			return result;
		}
		return null;
	}

	/**
	 * 获取已经做完但尚未领取奖励的任务列表
	 * 
	 * @param uid
	 * @param type
	 */
	public List<JSONObject> taskAcceptedList(String uid, TaskConfigLib.TaskFor type) {
		String userFinishedTask = OtherRedisService.getInstance().getUserAcceptedTask(uid, type);
		if (StringUtils.isEmpty(userFinishedTask)) {
			return null;
		}
		String[] taskStringfy = userFinishedTask.split("#");
		int count = taskStringfy.length;
		if (count > 0) {
			List<JSONObject> result = new ArrayList<JSONObject>();
			for (int i = 0; i < count; i++) {
				String[] task = taskStringfy[i].split(",");
				if (task.length == 2) {
					int taskId = Integer.parseInt(task[0]);
					TaskConfigModel taskConfigForEvent = TaskConfigLib.getInstance().getTaskConfigFor(taskId);
					if (taskConfigForEvent == null) {
						logger.error(String.format("taskFinishedList>%d has no config", taskId));
						// TODO 移除异常数据

						continue;
					}
					taskConfigForEvent.setState(TaskState.Accepted);
					taskConfigForEvent.setType(type);
					taskConfigForEvent.setCurrentCount(Integer.parseInt(task[1]));
					result.add(taskConfigForEvent.toJsonObject());
				}
			}
			return result;
		}
		return null;
	}

	/**
	 * 获取已经领取奖励的任务列表
	 * 
	 * @param uid
	 * @param type
	 */
	public List<JSONObject> taskCommitedList(String uid, TaskConfigLib.TaskFor type) {
		String userCommitedTask = OtherRedisService.getInstance().getUserCommitedTask(uid, type);
		if (StringUtils.isEmpty(userCommitedTask)) {
			return null;
		}
		String[] taskStringfy = userCommitedTask.split("#");
		int count = taskStringfy.length;
		if (count > 0) {
			List<JSONObject> result = new ArrayList<JSONObject>();
			for (int i = 0; i < count; i++) {
				if (!StringUtils.isEmpty(taskStringfy[i])) {
					int taskId = Integer.parseInt(taskStringfy[i]);
					TaskConfigModel taskConfigForEvent = TaskConfigLib.getInstance().getTaskConfigFor(taskId);
					if (taskConfigForEvent == null) {
						logger.error(String.format("taskFinishedList>%d has no config", taskId));
						continue;
					}
					taskConfigForEvent.setState(TaskState.Commited);
					taskConfigForEvent.setType(type);
					taskConfigForEvent.setCurrentCount(taskConfigForEvent.getTargetCount());
					result.add(taskConfigForEvent.toJsonObject());
				}
			}
			return result;
		}
		return null;
	}

	/**
	 * 提交任务进度 taskId:what,count#taskId:what,count
	 * 
	 * @param uid
	 *            事件发生者
	 * @param what
	 *            任务关注的事件
	 * @param count
	 *            事件发生次数
	 */
	public synchronized void taskProcess(int uid, final String what, int count) {
		String valueOfUid = String.valueOf(uid);
		String userAcceptedTaskForNewBie = OtherRedisService.getInstance().getUserAcceptedTask(valueOfUid,
				TaskFor.Newbie);
		List<String> splitForNewBie = new ArrayList<String>();
		String[] split2 = userAcceptedTaskForNewBie.split("#");
		for (int i = 0; i < split2.length; i++) {
			splitForNewBie.add(split2[i]);
		}
		String userAcceptedTaskForDaily = OtherRedisService.getInstance().getUserAcceptedTask(valueOfUid,
				TaskFor.Daily);
		List<String> splitForDaily = new ArrayList<String>();
		split2 = userAcceptedTaskForDaily.split("#");
		for (int i = 0; i < split2.length; i++) {
			splitForDaily.add(split2[i]);
		}
		// 用户已经完成当前所有任务
		if (splitForNewBie.size() == 0 && splitForDaily.size() == 0) {
			return;
		}

		// 配置中是否有与此事件相关联的任务
		boolean hasTaskAboutWhat = TaskConfigLib.getInstance().hasTaskAboutWhat(what);
		if (hasTaskAboutWhat) {
			boolean newBieChanged = false;
			boolean dailyChanged = false;

			int counter = 0;
			// 判断是不是位于玩家尚未完成的任务桶中
			for (Iterator<String> iteratorForNewBie = splitForNewBie.iterator(); iteratorForNewBie.hasNext();) {
				String processTask = (String) iteratorForNewBie.next();
				String[] split = processTask.split(",");
				if (split.length == 2) {
					String task = split[0];
					TaskConfigModel taskConfigModel = TaskConfigLib.getInstance()
							.getTaskConfigFor(Integer.valueOf(task));
					if (taskConfigModel == null) {
						logger.error(String.format(
								"taskProcess>>taskConfigModel NewBie is null,uid:%d,what:%s,count:%d taskId:%s", uid,
								what, count, task));
						// 是不是该任务已经下掉了
						TaskConfigModel taskConfigForDown = TaskConfigLib.getInstance()
								.getTaskConfigForDown(Integer.valueOf(task));
						if (taskConfigForDown != null) {
							logger.error(String.format(
									"taskProcess>>taskConfigModel NewBie is null,uid:%d,what:%s,count:%d taskId:%s cause has down",
									uid, what, count, task));
						} else {
							logger.error(String.format(
									"taskProcess>>taskConfigModel NewBie is null,uid:%d,what:%s,count:%d taskId:%s cause no config",
									uid, what, count, task));
						}
					} else {
						if (taskConfigModel.getWhat().equals(what)) {
							int proc = Integer.parseInt(split[1]);
							proc += count;
							if (proc >= taskConfigModel.getTargetCount()) {
								// 从尚未完成的桶中移除
								iteratorForNewBie.remove();
								newBieChanged = true;
								// 放进完成的任务桶中
								taskFinished(valueOfUid, taskConfigModel.getType(), taskConfigModel.getId());
								boolean isCommit = taskConfigModel.getCommitMode() == TaskCommitFor.Auto ? true : false;
								if (isCommit) {
									taskCommit(valueOfUid, taskConfigModel.getType(), taskConfigModel.getId());
								}
							} else {
								split[1] = String.valueOf(proc);
								splitForDaily.set(counter, StringUtils.join(split, ","));
								newBieChanged = true;
							}
						}
					}

				} else {
					iteratorForNewBie.remove();
					newBieChanged = true;
				}

			}

			counter = 0;
			for (Iterator<String> iteratorForDaily = splitForDaily.iterator(); iteratorForDaily.hasNext();) {
				String processTask = (String) iteratorForDaily.next();
				String[] split = processTask.split(",");
				if (split.length == 2) {
					String task = split[0];
					TaskConfigModel taskConfigModel = TaskConfigLib.getInstance()
							.getTaskConfigFor(Integer.valueOf(task));
					if (taskConfigModel == null) {
						logger.error(String.format(
								"taskProcess>>taskConfigModel Daily is null,uid:%d,what:%s,count:%d taskId:%s", uid,
								what, count, task));
						TaskConfigLib.getInstance().dumpTaskInfo();
						// 是不是该任务已经下掉了
						TaskConfigModel taskConfigForDown = TaskConfigLib.getInstance()
								.getTaskConfigForDown(Integer.valueOf(task));
						if (taskConfigForDown != null) {
							logger.error(String.format(
									"taskProcess>>taskConfigModel Daily is null,uid:%d,what:%s,count:%d taskId:%s cause has down",
									uid, what, count, task));
						} else {
							logger.error(String.format(
									"taskProcess>>taskConfigModel Daily is null,uid:%d,what:%s,count:%d taskId:%s cause no config",
									uid, what, count, task));
						}
					} else {
						if (taskConfigModel.getWhat().equals(what)) {
							int proc = Integer.parseInt(split[1]);
							proc += count;
							if (proc >= taskConfigModel.getTargetCount()) {
								// 从尚未完成的桶中移除
								iteratorForDaily.remove();
								dailyChanged = true;
								// 放进完成的任务桶中
								taskFinished(valueOfUid, taskConfigModel.getType(), taskConfigModel.getId());
								boolean isCommit = taskConfigModel.getCommitMode() == TaskCommitFor.Auto ? true : false;
								if (isCommit) {
									taskCommit(valueOfUid, taskConfigModel.getType(), taskConfigModel.getId());
								}
							} else {
								split[1] = String.valueOf(proc);
								splitForDaily.set(counter, StringUtils.join(split, ","));
								dailyChanged = true;
							}
						}
					}
				} else {
					iteratorForDaily.remove();
					dailyChanged = true;
				}
				counter++;
			}

			if (newBieChanged) {
				String joinForNewBie = StringUtils.join(splitForNewBie, "#");
				OtherRedisService.getInstance().updateUserAcceptedTask(valueOfUid, TaskFor.Newbie, joinForNewBie);
			}

			if (dailyChanged) {
				String joinForDaily = StringUtils.join(splitForDaily, "#");
				OtherRedisService.getInstance().updateUserAcceptedTask(valueOfUid, TaskFor.Daily, joinForDaily);
			}
		}
	}

	/**
	 * 通知客户端任务进度发生变更
	 * 
	 * @param uid
	 *            事件发生者
	 * @param taskId
	 *            任务编号
	 * @param process
	 *            进度 比如 1/3
	 */
	public synchronized void notifyTaskProcess(final String uid, final int taskId, final String process) {
		final JSONObject msgBody = new JSONObject();
		msgBody.put("taskId", taskId);
		msgBody.put("process", process);
		msgBody.put("cometProtocol", CModProtocol.TASK_PROCESS);
		final String url = UrlConfigLib.getUrl("url").getAdminrpc_publish_live();

		String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret, "appKey=" + VarConfigUtils.ServiceKey,
				"msgBody=" + JSONObject.toJSONString(msgBody), "users=" + uid);

		Unirest.post(url).field("appKey", VarConfigUtils.ServiceKey).field("msgBody", JSONObject.toJSONString(msgBody))
				.field("users", uid).field("sign", signParams).asJsonAsync(new Callback<JsonNode>() {
					public void completed(HttpResponse<JsonNode> arg0) {
						System.out.println(String.format("notifyTaskProcess url=%s body=%s", url, msgBody.toString()));
					}

					@Override
					public void failed(UnirestException arg0) {
						logger.error(String.format(
								"notifyTaskProcess failed for uid:%s,taskId:%d,process:%s failed cause:%s", uid, taskId,
								process, arg0.toString()));
					}

					@Override
					public void cancelled() {
						logger.error(String.format("notifyTaskProcess canceld for uid:%s,taskId:%d,process:%s", uid,
								taskId, process));
					}
				});
	}

	/**
	 * taskId,1#taskId,0 1:已领取奖励 0:未领取奖励 放进任务已完成列表
	 * 
	 * @param uid
	 * @param taskId
	 */
	private synchronized void taskFinished(String uid, TaskConfigLib.TaskFor type, int taskId) {
		String userFinishedTask = OtherRedisService.getInstance().getUserFinishedTask(uid, type);
		String valueOfTaskId = String.valueOf(taskId);
		List<String> userFinishedTaskList = new ArrayList<String>();
		String[] split = userFinishedTask.split("#");
		for (int i = 0; i < split.length; i++) {
			userFinishedTaskList.add(split[i]);
		}
		if (!userFinishedTaskList.contains(valueOfTaskId)) {
			userFinishedTaskList.add(valueOfTaskId);
		}
		for (Iterator<String> iterator = userFinishedTaskList.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			if (StringUtils.isEmpty(string)) {
				iterator.remove();
			}
		}
		String newUserFinishedTask = StringUtils.join(userFinishedTaskList, "#");
		OtherRedisService.getInstance().updateUserFinishedTask(uid, type, newUserFinishedTask);
	}

	/**
	 * taskId,1#taskId,0 1:已领取奖励 0:未领取奖励 放进任务已完成列表
	 * 
	 * @param uid
	 * @param taskId
	 */
	public synchronized int taskCommit(String uid, TaskConfigLib.TaskFor type, int taskId) {
		String userFinishedTask = OtherRedisService.getInstance().getUserFinishedTask(uid, type);
		if (getIndexOfSharp(userFinishedTask,String.valueOf(taskId)) != -1) {
			return taskReward(uid, type, taskId);
		} else {
			String taskStr = String.valueOf(taskId);
			removeSomeTaskFromAccepted(uid, type, taskStr);
			removeSomeTaskFromFinished(uid, type, taskStr);
			return CodeContant.TaskUnFinishedOrCommited;
		}
	}

	/**
	 * taskId,1#taskId,0 1:已领取奖励 0:未领取奖励 放进任务已完成列表
	 * 
	 * @param uid
	 * @param taskId
	 */
	public synchronized int taskCommitById(String uid, int taskId) {
		TaskConfigModel taskConfigFor = TaskConfigLib.getInstance().getTaskConfigFor(taskId);
		if (taskConfigFor == null) {
			return CodeContant.TaskMissingConfig;
		}
		return taskCommit(uid, taskConfigFor.getType(), taskId);
	}

	/**
	 * 通知客户端任务进度已经完成，可以领取
	 * 
	 * @param uid
	 *            事件发生者
	 * @param taskId
	 *            任务编号
	 */
	public synchronized void notifyTaskFinished(final String uid, final int taskId) {
		final JSONObject msgBody = new JSONObject();
		msgBody.put("taskId", taskId);
		msgBody.put("cometProtocol", CModProtocol.TASK_FINISHED);
		final String url = UrlConfigLib.getUrl("url").getAdminrpc_publish_live();

		String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret, "appKey=" + VarConfigUtils.ServiceKey,
				"msgBody=" + JSONObject.toJSONString(msgBody), "users=" + uid);

		Unirest.post(url).field("appKey", VarConfigUtils.ServiceKey).field("msgBody", JSONObject.toJSONString(msgBody))
				.field("users", uid).field("sign", signParams).asJsonAsync(new Callback<JsonNode>() {
					public void completed(HttpResponse<JsonNode> arg0) {
						System.out.println(String.format("notifyTaskFinished url=%s body=%s", url, msgBody.toString()));
					}

					@Override
					public void failed(UnirestException arg0) {
						logger.error(String.format("notifyTaskFinished failed for uid:%s,taskId:%d cause:%s", uid,
								taskId, arg0.toString()));
					}

					@Override
					public void cancelled() {
						logger.error(String.format("notifyTaskFinished canceld for uid:%s,taskId:%d", uid, taskId));
					}
				});
	}

	private synchronized void removeSomeTaskFromAccepted(String uid, TaskFor type, String taskStr) {
		String userFinishedTask = OtherRedisService.getInstance().getUserAcceptedTask(uid, type);
		String[] split2 = userFinishedTask.split("#");
		List<String> split = new ArrayList<String>();
		for (int i = 0; i < split2.length; i++) {
			split.add(split2[i]);
		}
		// logger.error("checkBindPhoneTaks removeSomeTaskFromAccepted in " + userFinishedTask + " taskStr " + taskStr);
		boolean changed = false;
		if (split.contains(taskStr)) {
			split.remove(taskStr);
			changed = true;
			// logger.error("checkBindPhoneTaks removeSomeTaskFromAccepted in true");
		}

		if (changed) {
			String tasks = StringUtils.join(split, "#");
			OtherRedisService.getInstance().updateUserAcceptedTask(uid, type, tasks);
		}
	}

	private synchronized void removeSomeTaskFromFinished(String uid, TaskFor type, String taskStr) {
		String userFinishedTask = OtherRedisService.getInstance().getUserFinishedTask(uid, type);
		String[] split2 = userFinishedTask.split("#");
		List<String> split = new ArrayList<String>();
		for (int i = 0; i < split2.length; i++) {
			split.add(split2[i]);
		}
		if (split.contains(taskStr)) {
			split.remove(taskStr);
		}
		String tasks = StringUtils.join(split, "#");
		OtherRedisService.getInstance().updateUserFinishedTask(uid, type, tasks);
	}

	/**
	 * 领取任务奖励
	 * 
	 * @param uid
	 * @param taskId
	 */
	public synchronized int taskReward(String uid, TaskConfigLib.TaskFor type, int taskId) {
		// 此任务是否合法
		TaskConfigModel taskConfigForEvent = TaskConfigLib.getInstance().getTaskConfigFor(taskId);
		if (taskConfigForEvent == null) {
			taskConfigForEvent = TaskConfigLib.getInstance().getTaskConfigForDown(taskId);
			if (taskConfigForEvent == null) {
				logger.error(String.format("taskReward>>uid:%s,taskId:%d", uid, taskId));
				return CodeContant.TaskMissingConfig;
			}
		}

		// 是否已经领取
		String userCommitedTask = OtherRedisService.getInstance().getUserCommitedTask(uid, type);
		String taskStr = String.valueOf(taskId);
		int indexOf = getIndexOfSharp(userCommitedTask, taskStr);
		if (indexOf != -1) {
			// 从尚未完成和已完成中移除掉
			removeSomeTaskFromAccepted(uid, type, taskStr);
			removeSomeTaskFromFinished(uid, type, taskStr);

			return CodeContant.TaskRewardHasCommit;
		}

		// 任务是否已经完成
		String userFinishedTask = OtherRedisService.getInstance().getUserFinishedTask(uid, type);
		if (userFinishedTask == null) {
			return CodeContant.TaskUnFinished;
		} else {
			int indexOfUserFinished = getIndexOfSharp(userFinishedTask,String.valueOf(taskId));
			if (indexOfUserFinished == -1) {
				return CodeContant.TaskUnFinished;
			}
		}

		// 领取奖励
		String taskStringfy = String.valueOf(taskId);
		String rewards = taskConfigForEvent.getRewards();
		String[] rewardsStringfy = rewards.split("#");
		if (rewardsStringfy.length > 0) {
			for (int i = 0; i < rewardsStringfy.length; i++) {
				String[] reward = rewardsStringfy[i].split(",");
				if (reward.length >= 2) {
					int count = Integer.parseInt(reward[1]);
					if (reward[0].equalsIgnoreCase("money")) {
						int addWealthReason = 1;
						UserServiceImpl.getInstance().addUserWealth(Integer.parseInt(uid), count, addWealthReason,
								taskId, taskConfigForEvent.getName());
						// 记录送钱的任务
						logger.info(String.format("taskReward>>uid:%s,taskId:%s,money:%s", uid, taskId, count));
					} else if (reward[0].equalsIgnoreCase("exp")) {
						UserServiceImpl.getInstance().addUserExpByTask(Integer.parseInt(uid), count);
					} else if (reward[0].equalsIgnoreCase("item")) {
						// 赠送礼物
						if (reward.length == 3) {
							String itemId = reward[1];
							int itemCount = Integer.parseInt(reward[2]);
							UserItemServiceImpl.insertUserItemStatic(Integer.parseInt(uid), Integer.parseInt(itemId),
									itemCount, IUserItemService.ItemSource.Task);
							logger.info(
									String.format("taskReward>>uid:%s,itemId:%s,itemCount:%d", uid, itemId, itemCount));
						}
					}
				}
			}
		}

		// 放进已经领取的桶中
		String reult = StringUtils.isEmpty(userCommitedTask) ? taskStringfy : userCommitedTask + "#" + taskStringfy;
		OtherRedisService.getInstance().appendUserCommitedTask(uid, type, reult);

		// 从已经完成的任务桶中移除
		String[] split2 = userFinishedTask.split("#");
		List<String> split = new ArrayList<String>();
		for (int i = 0; i < split2.length; i++) {
			split.add(split2[i]);
		}
		if (split.contains(taskStringfy)) {
			split.remove(taskStringfy);
		}

		if (split.size() <= 0) {
			if (type == TaskFor.Newbie) {
				List<JSONObject> taskAcceptedList = taskAcceptedList(uid, TaskFor.Newbie);
				if (taskAcceptedList.size() <= 0) {
					// 标记玩家新手任务已经完成
					boolean hasFinishedNewBieTask = OtherRedisService.getInstance().hasFinishedNewBieTask(uid);// 从存储中获取完成情况
					if (!hasFinishedNewBieTask) {
						// 执行奖励
						TaskConfigModel newBieRewardTask = TaskConfigLib.getInstance().getNewBieRewardTask();
						int rewarCount = 0;
						if (newBieRewardTask != null) {
							String rewards2 = newBieRewardTask.getRewards();
							String[] rewardsStringfy2 = rewards2.split("#");
							if (rewardsStringfy2.length > 0) {
								for (int i = 0; i < rewardsStringfy2.length; i++) {
									String[] reward = rewardsStringfy2[i].split(",");
									if (reward.length == 2) {
										int count = Integer.parseInt(reward[1]);
										if (reward[0].equalsIgnoreCase("money")) {
											int addWealthReason = 1;
											UserServiceImpl.getInstance().addUserWealth(Integer.parseInt(uid), count,
													addWealthReason, taskId, taskConfigForEvent.getName());
											logger.warn(String.format("taskReward>>%d:%s", uid, count));
											rewarCount = count;
										} else if (reward[0].equalsIgnoreCase("exp")) {
											UserServiceImpl.getInstance().addUserExpByTask(Integer.parseInt(uid),
													count);
											rewarCount = count;
										}
									}
								}
							}
						}
						// 存到数据库中
						OtherRedisService.getInstance().updateFinishedNewBieTask(uid, rewarCount);
					}
				}
			}
		}
		String newUserFinishedTask = StringUtils.join(split, "#");
		OtherRedisService.getInstance().updateUserFinishedTask(uid, type, newUserFinishedTask);
		return CodeContant.OK;
	}

	/**
	 * 检查制定类型的指定状态是否有某一任务
	 * 
	 * @param uid
	 * @param taskFor
	 * @param state
	 * @param taskId
	 * @return
	 */
	public boolean hasSomeTaskFor(String uid, TaskFor taskFor, TaskState state, int taskId) {
		String userAcceptedTask = null;
		if (state == TaskState.Accepted) {
			userAcceptedTask = OtherRedisService.getInstance().getUserAcceptedTask(uid, taskFor);
		} else if (state == TaskState.Finished) {
			userAcceptedTask = OtherRedisService.getInstance().getUserFinishedTask(uid, taskFor);
		} else if (state == TaskState.Commited) {
			userAcceptedTask = OtherRedisService.getInstance().getUserCommitedTask(uid, taskFor);
		}

		String taskFy = String.valueOf(taskId);
		if (!StringUtils.isEmpty(userAcceptedTask)) {
			String[] split = userAcceptedTask.split("#");
			for (int i = 0; i < split.length; i++) {
				String task = split[i];
				String[] split2 = task.split(",");
				if (split2.length == 2) {
					if (split2[0].equals(taskFy)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 获取用户指定月份的签到信息
	 * 
	 * @param uid
	 * @param month
	 * @return
	 */
	public Map<String, Object> getSignSummaryInfo(final String uid, final long ts) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(ts);
		String dateKey = String.format("%d:%d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
		final int signSummaryInfo = OtherRedisService.getInstance().getSignSummaryInfoByMonth(uid, dateKey);
		if (signSummaryInfo == -1) {
			logger.error(String.format("getSignSummaryInfo:getSignSummaryInfoByMonth>>[%s,%s]", uid, dateKey));
			result.put("code", CodeContant.SignInternalError);
			result.put("message", "服务器内部错误-getSignSummaryInfoByMonth");
			return result;
		}

		final String continuous = OtherRedisService.getInstance().getSignSummaryInfoByUser(uid);
		if (continuous == null) {
			logger.error(String.format("getSignSummaryInfo:getSignSummaryInfoByUser>>[%s,%d]", uid, continuous));
			result.put("code", CodeContant.SignInternalError);
			result.put("message", "服务器内部错误-getSignSummaryInfoByUser");
			return result;
		}

		int day = calendar.get(Calendar.DAY_OF_MONTH);
		// 是否已经领取
		boolean sign = isSign(signSummaryInfo, day);

		String[] split = continuous.split("#");
		if (split.length == 2) {
			int continuousVal = Integer.parseInt(split[0]);
			int continuousOrignal = continuousVal;

			if (!sign) {
				// 最后一次领取签到时间戳
				long parseLong = Long.parseLong(split[1]);
				if (parseLong == -1) {
					continuousVal = 1;
					continuousOrignal = 0;
				} else {
					boolean areContinuousSameDay = areContinuousSameDay(parseLong, ts);
					if (areContinuousSameDay) {
						continuousVal += 1;
					} else {
						continuousVal = 1;
						continuousOrignal = 0;
					}
				}
			}

			final int exp = calSignToday(continuousVal);

			// 计算今天可以领取的经验值
			result.put("stat", signSummaryInfo);
			result.put("ts", ts);
			result.put("rule", SignConfigLib.ruleExp);
			result.put("ruleItem", SignConfigLib.ruleItem);
			result.put("itemIcons", SignConfigLib.getItemIconObject());
			result.put("ruleDesc", SignConfigLib.getRuleDescList());
			result.put("exp", exp);
			result.put("continuous", continuousOrignal);

			// tosy add resign num;
			try {
				int resignInfo = OtherRedisService.getInstance().getReSignInfoByMonth(String.valueOf(uid), dateKey);
				result.put("statRe", resignInfo);

				int resignleft = 0;
				// check user vip
				UserVipInfoModel userVipInfoModel = ValueaddServiceUtil.getVipInfo(Integer.valueOf(uid));
				if (null != userVipInfoModel) {
					// 43 铂金 44 钻石
					int viptpye = userVipInfoModel.getGid();
					Double maxTime = 0.0;
					if (43 == viptpye) {
						maxTime = COUNT_VIP_RESIGN_5;
					} else if (44 == viptpye) {
						maxTime = COUNT_VIP_RESIGN_10;
					}
					// check vip left resign count
					Double nCountResigned = OtherRedisService.getInstance().getVipReSignCount(Integer.valueOf(uid),
							dateKey);
					if (null == nCountResigned) {
						nCountResigned = 0.0;
					}
					resignleft = (int) (maxTime - nCountResigned);
				}
				result.put("resignleft", resignleft);
			} catch (Exception e) {
				logger.error(String.format("get resignleft >>uid:%s,cause:%s", uid, e.toString()));
			}

			return result;
		} else {
			logger.error(String.format("getSignSummaryInfo:getSignSummaryInfoByUser>>[%s,%d]", uid, continuous));
			result.put("code", CodeContant.SignInternalError);
			result.put("message", "服务器内部错误-getSignSummaryInfoByUser split length is not 2");
			return result;
		}
	}

	/**
	 * 获取用户指定月份的签到信息
	 * 
	 * @param uid
	 * @param month
	 * @return
	 */
	public Map<String, Object> getSignSummaryInfoForAnyOne() {
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("rule", SignConfigLib.ruleExp);
		result.put("ruleItem", SignConfigLib.ruleItem);
		result.put("itemIcons", SignConfigLib.getItemIconObject());
		result.put("ruleDesc", SignConfigLib.getRuleDescList());
		return result;
	}

	/**
	 * tosy
	 * 补签，只给补签日的奖励，并计入连续天数，增加本月补签次数
	 * @param uid
	 * @param dayidx
	 * @return
	 */
	public synchronized Map<String, Object> getReSignTake(int uid, int daynum) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		// redis zsort for vip resigned times.
		if (0 >= daynum) {
			result.put("code", CodeContant.ReSignError);
			result.put("message", "arg error <= 0");
			return result;
		}

		// check user vip
		UserVipInfoModel userVipInfoModel = ValueaddServiceUtil.getVipInfo(uid);
		if (null == userVipInfoModel) {
			result.put("code", CodeContant.ReSignError);
			result.put("message", "非VIP用户");
			return result;
		}
		// 43 铂金 44 钻石
		int viptpye = userVipInfoModel.getGid();
		Double maxTime = 0.0;
		if (43 == viptpye) {
			maxTime = COUNT_VIP_RESIGN_5;
		} else if (44 == viptpye) {
			maxTime = COUNT_VIP_RESIGN_10;
		}

		final long ts = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(ts);
		String month = String.format("%d:%d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);

		// check vip left resign count
		Double nCountResigned = OtherRedisService.getInstance().getVipReSignCount(uid, month);
		if (null == nCountResigned) {
			nCountResigned = 0.0;
		}
		Double nResignLeft = maxTime - nCountResigned;
		if (nResignLeft <= 0) {
			result.put("code", CodeContant.ReSignError);
			result.put("message", "无补签次数");
			return result;
		}

		// check day is smaller than right now
		String dayNow = DateUtils.dateToString(null, "dd");
		if (Integer.valueOf(dayNow) <= daynum) {
			result.put("code", CodeContant.ReSignError);
			result.put("message", "日期错误.");
			return result;
		}

		// check day is taken already.

		int signSummaryInfo = OtherRedisService.getInstance().getSignSummaryInfoByMonth(String.valueOf(uid), month);
		int resignInfo = OtherRedisService.getInstance().getReSignInfoByMonth(String.valueOf(uid), month);
		if (signSummaryInfo == -1) {
			logger.error(String.format("getSignSummaryInfo:getSignSummaryInfoByMonth>>[%s,%s]", uid, month));
			result.put("code", CodeContant.SignInternalError);
			result.put("message", "服务器内部错误-getSignSummaryInfoByMonth");
			return result;
		}

		boolean sign = isSign(signSummaryInfo, daynum) | isSign(resignInfo, daynum);
		if (sign) {
			result.put("code", CodeContant.SignRewardHasCommit);
			result.put("message", "签到奖励已经领取");
			return result;
		}

		// change resign info
		resignInfo = enable(resignInfo, daynum);
		OtherRedisService.getInstance().setReSignInfoByMonth(String.valueOf(uid), month, resignInfo);
		signSummaryInfo = enable(signSummaryInfo, daynum);
		OtherRedisService.getInstance().setSignSummaryInfoByMonth(String.valueOf(uid), month, signSummaryInfo);

		// add resigned count
		OtherRedisService.getInstance().incrVipReSignCount(uid, month);

		// reward exp
		final int continues = 1; // fix 1
		int exp = calSignToday(continues);
		UserServiceImpl.getInstance().addUserExpByTask(uid, exp);

		// reward gift
		List<SignConfig> calSignTodayGift = calSignTodayGift(continues);
		if (calSignTodayGift != null && !calSignTodayGift.isEmpty()) {
			for (SignConfig signConfig : calSignTodayGift) {
				String items = signConfig.getItem();
				if (items != null) {
					String[] split2 = items.split("~");
					for (int i = 0; i < split2.length; i++) {
						String itemString = split2[i];
						String[] split23 = itemString.split("-");
						if (split23.length == 2) {
							String itemId = split23[0];
							int itemCount = Integer.parseInt(split23[1]);
							ConfigGiftModel giftConfigByGidNew = ConfigServiceImpl
									.getGiftInfoByGid(Integer.parseInt(itemId));
							if (giftConfigByGidNew == null) {
								logger.error(String.format("getSignTake>>itemId %s missing gift config", itemId));
							} else {
								UserItemServiceImpl.insertUserItemStatic(uid, Integer.parseInt(itemId), itemCount,
										IUserItemService.ItemSource.Task);
								logger.info(String.format("getReSignTake>>uid:%s,itemId:%s,itemCount:%d", uid, itemId,
										itemCount));
							}
						}
					}
				}
			}
		}

		// return args

		result.put("resignleft", (int) (nResignLeft - 1));
		result.put("stat", signSummaryInfo);
		result.put("statRe", resignInfo);
		result.put("ts", ts);
		result.put("exp", exp);
		result.put("rule", SignConfigLib.ruleExp);
		result.put("ruleItem", SignConfigLib.ruleItem);
		result.put("itemIcons", SignConfigLib.getItemIconObject());
		result.put("ruleDesc", SignConfigLib.getRuleDescList());
		result.put("continuous", continues);
		return result;
	}

	/**
	 * 领取签到奖励
	 * 
	 * @param uid
	 * @param month
	 * @return
	 */
	public synchronized Map<String, Object> getSignTake(String uid) {
		HashMap<String, Object> result = new HashMap<String, Object>();

		// 计算连续值
		final long ts = System.currentTimeMillis();

		// 计算月份日志
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(ts);
		String dateKey = String.format("%d:%d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
		int signSummaryInfo = OtherRedisService.getInstance().getSignSummaryInfoByMonth(uid, dateKey);
		if (signSummaryInfo == -1) {
			logger.error(String.format("getSignSummaryInfo:getSignSummaryInfoByMonth>>[%s,%s]", uid, dateKey));
			result.put("code", CodeContant.SignInternalError);
			result.put("message", "服务器内部错误-getSignSummaryInfoByMonth");
			return result;
		}
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		// 是否已经领取
		boolean sign = isSign(signSummaryInfo, day);
		if (sign) {
			result.put("code", CodeContant.SignRewardHasCommit);
			result.put("message", "签到奖励已经领取");
			return result;
		}

		final String info = OtherRedisService.getInstance().getSignSummaryInfoByUser(uid);
		String[] split = info.split("#");
		if (split.length == 2) {
			int continuous = Integer.parseInt(split[0]);
			if (continuous == -1) {
				logger.error(String.format("getSignSummaryInfo:getSignSummaryInfoByUser>>[%s,%d]", uid, continuous));
				result.put("code", CodeContant.SignInternalError);
				result.put("message", "服务器内部错误-getSignSummaryInfoByUser");
				return result;
			}

			// 第一次签到
			long parseLong = Long.parseLong(split[1]);
			if (parseLong == -1) {
				continuous = 1;
			} else {
				boolean areContinuousSameDay = areContinuousSameDay(parseLong, ts);
				if (areContinuousSameDay) {
					continuous += 1;
				} else {
					continuous = 1;
				}
			}

			// 签到
			signSummaryInfo = enable(signSummaryInfo, day);
			OtherRedisService.getInstance().setSignSummaryInfoByMonth(uid, dateKey, signSummaryInfo);

			// 11.4号做一次修正 如果连续签到值小于本月的签到明细累加值 则采用本月明细累加值
			int signBefore = signBefore(signSummaryInfo, day);
			if (signBefore > continuous) {
				continuous = signBefore;
				logger.error(String.format("getSignTake>>uid:%s,day:%d,signBefore:%d,oldcontinuous:%d", uid, day,
						signBefore, continuous));
				// 暂时先不补发东西 等用户提出再说
			}

			OtherRedisService.getInstance().setSignSummaryInfoByUser(uid, continuous, System.currentTimeMillis());

			// 增加经验
			final int exp = calSignToday(continuous);
			UserServiceImpl.getInstance().addUserExpByTask(Integer.parseInt(uid), exp);

			// 增加送的礼物
			List<SignConfig> calSignTodayGift = calSignTodayGift(continuous);
			if (calSignTodayGift != null && !calSignTodayGift.isEmpty()) {
				for (SignConfig signConfig : calSignTodayGift) {
					String items = signConfig.getItem();
					if (items != null) {
						String[] split2 = items.split("~");
						for (int i = 0; i < split2.length; i++) {
							String itemString = split2[i];
							String[] split23 = itemString.split("-");
							if (split23.length == 2) {
								String itemId = split23[0];
								int itemCount = Integer.parseInt(split23[1]);
								ConfigGiftModel giftConfigByGidNew = ConfigServiceImpl
										.getGiftInfoByGid(Integer.parseInt(itemId));
								if (giftConfigByGidNew == null) {
									logger.error(String.format("getSignTake>>itemId %s missing gift config", itemId));
								} else {
									UserItemServiceImpl.insertUserItemStatic(Integer.parseInt(uid),
											Integer.parseInt(itemId), itemCount, IUserItemService.ItemSource.Task);
									logger.info(String.format("getSignTake>>uid:%s,itemId:%s,itemCount:%d", uid, itemId,
											itemCount));
								}
							}
						}
					}
				}
			}

			final int stat = signSummaryInfo;
			final int conti = continuous;

			// 计算今天可以领取的经验值
			result.put("stat", stat);
			result.put("ts", ts);
			result.put("exp", exp);
			result.put("rule", SignConfigLib.ruleExp);
			result.put("ruleItem", SignConfigLib.ruleItem);
			result.put("itemIcons", SignConfigLib.getItemIconObject());
			result.put("ruleDesc", SignConfigLib.getRuleDescList());
			result.put("continuous", conti);
			return result;
		} else {
			logger.error(String.format("getSignTake>>getSignSummaryInfoByUser:info len is not 2,%s", info));
			result.put("code", CodeContant.SignInternalError);
			result.put("message", "服务器内部错误-getSignSummaryInfoByUser split is not 2");
			return result;
		}
	}

	/**
	 * 计算今日签到奖励
	 * 
	 * @param uid
	 * @param month
	 * @return
	 */
	public int calSignToday(int continuous) {
		if (continuous <= 0) {
			return 0;
		}
		int position1 = (continuous >= SignConfigLib.phase3) ? SignConfigLib.phase3 : continuous;
		int position2 = (continuous % SignConfigLib.phase2 == 0) ? SignConfigLib.phase2 : -1;

		int exp = 0;
		SignConfig levelForLv = SignConfigLib.getLevelForLv(position1);
		if (levelForLv != null) {
			exp += levelForLv.getExp();
		}

		levelForLv = SignConfigLib.getLevelForLv(position2);
		if (levelForLv != null) {
			exp += levelForLv.getExp();
		}
		return exp;
	}

	/**
	 * 计算今日签到奖励
	 * 
	 * @param uid
	 * @param month
	 * @return
	 */
	public List<SignConfig> calSignTodayGift(int continuous) {
		if (continuous <= 0) {
			return null;
		}
		int position1 = (continuous >= SignConfigLib.phase3) ? SignConfigLib.phase3 : continuous;
		int position2 = (continuous % SignConfigLib.phase2 == 0) ? SignConfigLib.phase2 : -1;

		SignConfig levelForLv1 = SignConfigLib.getLevelForLv(position1);
		SignConfig levelForLv2 = SignConfigLib.getLevelForLv(position2);
		List<SignConfig> result = null;
		if (levelForLv1 != null || levelForLv2 != null) {
			result = new ArrayList<SignConfig>();
		}
		if (levelForLv1 != null) {
			result.add(levelForLv1);
		}
		if (levelForLv2 != null) {
			result.add(levelForLv2);
		}
		return result;
	}

	/**
	 * 签到
	 */
	public static int enable(int month, int day) {
		month |= (1 << (day - 1));
		return month;
	}

	/**
	 * 解除签到
	 */
	public static int disable(int month, int day) {
		month &= ~(1 << (day - 1));
		return month;
	}

	/**
	 * 是否已经签到
	 */
	public static boolean isSign(int month, int day) {
		String binaryString = Integer.toBinaryString(month);
		int length = binaryString.length();
		if (day <= binaryString.length()) {
			return binaryString.charAt(length - day) == '1';
		}
		return false;
	}

	/**
	 * 是否已经签到
	 */
	public static int signBefore(int month, int day) {
		int signContinue = 0;
		for (int i = day; i >= 1; i--) {
			if (isSign(month, i)) {
				++signContinue;
			} else {
				break;
			}
		}
		return signContinue;
	}

	/**
	 * 是否尚未签到
	 */
	public static boolean isNotSign(int month, int day) {
		return !isSign(month, day);
	}

	/**
	 * 2天是否是连续的2天
	 * 
	 * @param dateMin
	 * @param dateMax
	 * @return
	 */
	public static boolean areContinuousSameDay(long dateA, long dateB) {
		Calendar calDateA = Calendar.getInstance();
		calDateA.setTimeInMillis(dateA);

		Calendar calDateB = Calendar.getInstance();
		calDateB.setTimeInMillis(dateB);

		calDateA.add(Calendar.DAY_OF_MONTH, 1);
		return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
				&& calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
				&& calDateA.get(Calendar.DAY_OF_MONTH) == calDateB.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 重新派发新增的任务
	 * 
	 * @param taskAllListDynamicNew
	 */
	private boolean reDispatchTasks(String uidStringfy, TaskFor taskFor, Set<Integer> taskAllListDynamicNew) {
		try {
			List<TaskConfigModel> taskConfigListForSomeType = TaskConfigLib.getInstance()
					.getSomeTaskConfigList(taskAllListDynamicNew);
			if (taskConfigListForSomeType != null) {
				StringBuffer taskAcceptStringfy = new StringBuffer();
				int len = taskConfigListForSomeType.size();
				boolean hasValid = false;
				String userAcceptedTask = OtherRedisService.getInstance().getUserAcceptedTask(uidStringfy, taskFor);
				for (Iterator<TaskConfigModel> iterator = taskConfigListForSomeType.iterator(); iterator.hasNext();) {
					TaskConfigModel taskConfigModel = (TaskConfigModel) iterator.next();
					if (taskConfigModel.getIsValid() == 1) {
						if (getIndexOfSharp(userAcceptedTask,String.valueOf(taskConfigModel.getId())) == -1) {
							taskAcceptStringfy.append(taskConfigModel.getId() + ",0");
							taskAcceptStringfy.append("#");
							hasValid = true;
						}
					}
				}
				if (hasValid) {
					taskAcceptStringfy.deleteCharAt(taskAcceptStringfy.length() - 1);
					if (userAcceptedTask.isEmpty()) {
						OtherRedisService.getInstance().updateUserAcceptedTask(uidStringfy, taskFor,
								taskAcceptStringfy.toString());
					} else {
						String[] split = userAcceptedTask.split("#");
						len += split.length;
						OtherRedisService.getInstance().updateUserAcceptedTask(uidStringfy, taskFor,
								userAcceptedTask + "#" + taskAcceptStringfy.toString());
					}

					if (taskFor == TaskFor.Newbie) {
						OtherRedisService.getInstance().updateUserAcceptedTaskFlagForNewbie(uidStringfy, len);
					}
				}
			}
		} catch (Exception e) {
			logger.error(String.format("reDispatchTasks>>uid:%s,taskFor:%d,cause:%s", uidStringfy, taskFor.ordinal(),
					e.toString()));
			return false;
		}
		return true;
	}

	/**
	 * 重新派发新增的任务
	 * 
	 * @param taskAllListDynamicNew
	 */
	public synchronized boolean reDispatchTasks(String uidStringfy, TaskFor taskFor) {
		try {
			Set<Integer> taskAllListDynamicNew = taskAllListDynamicNew(Integer.parseInt(uidStringfy), taskFor);
			if (taskAllListDynamicNew != null && !taskAllListDynamicNew.isEmpty()) {
				reDispatchTasks(uidStringfy, taskFor, taskAllListDynamicNew);
			}
		} catch (Exception e) {
			logger.error(String.format("reDispatchTasks>>uid:%s,taskFor:%d,cause:%s", uidStringfy, taskFor.ordinal(),
					e.toString()));
			return false;
		}
		return true;
	}

	public static void main(String... args) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		String dateKey = String.format("%d:%d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
		String uid = "10102278";
		// 本月签到历史 task:sign:month:2016:11
		String bucketKey = "task:sign:month:" + dateKey;
		System.err.println("hget " + bucketKey + " " + uid);
		// redis_6381 hget bucketKey uid(10102278)

		// 连续签到桶 task:sign:user
		System.err.println("hget " + "task:sign:user" + " " + uid);

		int signBefore = signBefore(15, 4);
		int con = 2;

	}
}
