package com.mpig.api.dictionary;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.List;

import com.mpig.api.model.PopulateTemplate;

public class InvitationConfig implements PopulateTemplate<InvitationConfig>,Serializable{

	private static final long serialVersionUID = 3693002148711819975L;
	
	private Integer id;
	private String name; //邀请任务名称
	private Integer schedule; //任务界限值
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSchedule() {
		return schedule;
	}
	public void setSchedule(Integer schedule) {
		this.schedule = schedule;
	}
	
	@Override
	public InvitationConfig populateFromResultSet(ResultSet rs) {
		try {
			this.id = rs.getInt("id");
			this.name = rs.getString("name");
			this.schedule = rs.getInt("schedule");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
}
