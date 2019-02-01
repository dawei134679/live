package com.mpig.api.model;

public class Am0AnchorResultUserValueTopModel {
    int     anchorid;
    long    star;       //总star
    long    more;       //多送star        star-more = 正常奖励
    long	allstars;	//所有star

    String  aheadpic;
    String  anickname;
    String  atitle;

    int     uid;
    long    value;

    String  uheadpic;
    String  unickname;

    long    tm;
    

    public long getAllstars() {
		return allstars;
	}

	public void setAllstars(long allstars) {
		this.allstars = allstars;
	}

	public String getAheadpic() {
    	return aheadpic;
	}

	public void setAheadpic(String aheadpic) {
		this.aheadpic = aheadpic;
	}

	public String getAnickname() {
		return anickname;
	}

	public void setAnickname(String anickname) {
		this.anickname = anickname;
	}

	public String getAtitle() {
		return atitle;
	}

	public void setAtitle(String atitle) {
		this.atitle = atitle;
	}

	public String getUheadpic() {
		return uheadpic;
	}

	public void setUheadpic(String uheadpic) {
		this.uheadpic = uheadpic;
	}

	public String getUnickname() {
		return unickname;
	}

	public void setUnickname(String unickname) {
		this.unickname = unickname;
	}

	public long getMore() {
        return more;
    }

    public void setMore(long more) {
        this.more = more;
    }

    public int getAnchorid() {
        return anchorid;
    }

    public void setAnchorid(int anchorid) {
        this.anchorid = anchorid;
    }

    public long getStar() {
        return star;
    }

    public void setStar(long star) {
        this.star = star;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getTm() {
        return tm;
    }

    public void setTm(long tm) {
        this.tm = tm;
    }
}
