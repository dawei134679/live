package com.mpig.api.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mpig.api.dictionary.lib.TaskConfigLib.TaskCommitFor;
import com.mpig.api.dictionary.lib.TaskConfigLib.TaskFor;
import com.mpig.api.dictionary.lib.TaskConfigLib.TaskState;

/**
 * @ClassName:     TaskConfigModel.java
 * @Description:   任务数据
 * 
 * @author         jackzhang
 * @version        V1.0  
 * @Date           Jun 3, 2016 6:30:25 PM
 */
public class TaskConfigModel implements PopulateTemplate<TaskConfigModel>,Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private TaskFor type;
	private String name;
	private String desc;
	private String what;
	private int targetCount;
	private TaskCommitFor commitMode;
	private int enableLv;
	private TaskState state; 
	private int currentCount;
	private int jump;
	/**
	 * 是否有效 1 有效 0 无效
	 */
    private int isValid;
	
	public JSONObject toJsonObject(){
		JSONObject object = new JSONObject();
		object.put("id", id);
		object.put("name", name);
		object.put("type", type.ordinal());
		object.put("desc", desc);
		object.put("enableLv", enableLv);
		object.put("state", state.ordinal());
		object.put("currentCount", currentCount);
		object.put("targetCount", targetCount);
		object.put("jump", jump);
		object.put("rewards", JSONArray.parseArray(rewardsClient.toJSONString()));
		return object;
	}
	
	/*
	 * 奖励
	 * exp,3#money,10
	 */
	private String rewards;
	
	/*
	 * 奖励返回给客户端
	 * [{"unit":"经验值","count":2,"type":"exp"},{"unit":"金币","count":20,"type":"money"}] 
	 */
	private JSONArray rewardsClient = new JSONArray();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getRewards() {
		return rewards;
	}
	public void setRewards(String rewards) {
		this.rewards = rewards;
		String[] split = rewards.split("#");
		for (int i = 0; i < split.length; i++) {
			String reward = split[i];
			String[] split2 = reward.split(",");
			if(split2.length == 2){
				JSONObject object = new JSONObject();
				object.put("type", split2[0]);
				object.put("count", Integer.parseInt(split2[1]));
				String title = "未知";
				if(split2[0].equals("exp")){
					title = "经验";
				}else if(split2[0].equals("money")){
					title = "金币";
				}
				object.put("unit", title);
				rewardsClient.add(object);
			}
		}
	}
	public TaskCommitFor getCommitMode() {
		return commitMode;
	}
	public void setCommitMode(TaskCommitFor commitMode) {
		this.commitMode = commitMode;
	}	

	@Override
	public TaskConfigModel populateFromResultSet(ResultSet rs) {
		try {
			this.id = rs.getInt("id");
			this.jump = rs.getInt("jump");
			this.isValid = rs.getInt("isValid");
			this.type = rs.getInt("type") == 0?TaskFor.Newbie:TaskFor.Daily;
			this.name = rs.getString("name");
			this.desc = rs.getString("desc");
			this.what = rs.getString("what");
			this.targetCount = rs.getInt("count");
			this.setRewards(rs.getString("rewards"));
			this.commitMode = rs.getInt("commitMode") == 0?TaskCommitFor.Manual:TaskCommitFor.Auto;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}
	public TaskFor getType() {
		return type;
	}
	public void setType(TaskFor type) {
		this.type = type;
	}
	
	public String eventKey() {
		return what;
	}
	public int getTargetCount() {
		return targetCount;
	}
	public void setTargetCount(int targetCount) {
		this.targetCount = targetCount;
	}
	public String getWhat() {
		return what;
	}
	public void setWhat(String what) {
		this.what = what;
	}
	public int getEnableLv() {
		return enableLv;
	}
	public void setEnableLv(int enableLv) {
		this.enableLv = enableLv;
	}
	public TaskState getState() {
		return state;
	}
	public void setState(TaskState state) {
		this.state = state;
	}
	public int getCurrentCount() {
		return currentCount;
	}
	public void setCurrentCount(int currentCount) {
		this.currentCount = currentCount;
	}
	public JSONArray getRewardsClient() {
		return rewardsClient;
	}
	public int getJump() {
		return jump;
	}
	public void setJump(int jump) {
		this.jump = jump;
	}
	public int getIsValid() {
		return isValid;
	}
	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}
}