package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class SysCopyright implements Serializable {
	
	private static final long serialVersionUID = 2411629582667831646L;

	private Integer id;

    private String copyrightData;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCopyrightData() {
        return copyrightData;
    }

    public void setCopyrightData(String copyrightData) {
        this.copyrightData = copyrightData == null ? null : copyrightData.trim();
    }
}