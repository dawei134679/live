package com.tinypig.newadmin.web.entity;

import com.tinypig.newadmin.common.BaseModel;

public class GiftLuckyProbabilitys extends BaseModel{
    private Integer id;

    private Integer multiples;

    private Integer divisor;

    private Integer dividend;

    private Integer isrunway;

    private Integer maxcount;
    
    private String decoratedword;
    
    private Integer gid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMultiples() {
        return multiples;
    }

    public void setMultiples(Integer multiples) {
        this.multiples = multiples;
    }

    public Integer getDivisor() {
        return divisor;
    }

    public void setDivisor(Integer divisor) {
        this.divisor = divisor;
    }

    public Integer getDividend() {
        return dividend;
    }

    public void setDividend(Integer dividend) {
        this.dividend = dividend;
    }

    public Integer getIsrunway() {
        return isrunway;
    }

    public void setIsrunway(Integer isrunway) {
        this.isrunway = isrunway;
    }

    public Integer getMaxcount() {
        return maxcount;
    }

    public void setMaxcount(Integer maxcount) {
        this.maxcount = maxcount;
    }

	public String getDecoratedword() {
		return decoratedword;
	}

	public void setDecoratedword(String decoratedword) {
		this.decoratedword = decoratedword;
	}

	public Integer getGid() {
		return gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}
    
    
    
}