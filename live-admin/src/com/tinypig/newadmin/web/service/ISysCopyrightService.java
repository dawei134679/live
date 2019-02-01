package com.tinypig.newadmin.web.service;

import com.tinypig.newadmin.web.entity.SysCopyright;

public interface ISysCopyrightService {

	public int saveSysCopyright(SysCopyright sysCopyright);
	
	public int updateSysCopyright(SysCopyright sysCopyright);
	
	public SysCopyright getSysCopyright();
}
