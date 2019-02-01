package com.tinypig.admin.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChannelModel implements PopulateTemplate<ChannelModel> {
	private String channelName;
	private String channelCode;

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	
	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	@Override
	public ChannelModel populateFromResultSet(ResultSet rs) {
		try {
			this.channelCode = rs.getString("channelCode");
			this.channelName = rs.getString("channelName");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}
	
}
