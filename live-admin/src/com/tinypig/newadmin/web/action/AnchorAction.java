package com.tinypig.newadmin.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tinypig.admin.util.DateUtil;
import com.tinypig.newadmin.web.service.IAnchorService;

@Controller
@RequestMapping("/anchor")
public class AnchorAction {

	@Autowired
	private IAnchorService anchorService;
	
	@RequestMapping(value = "/list")
	@ResponseBody
	public Map<String, Object> getAnchorListInUnion(HttpServletRequest request,HttpServletResponse response){
		
		String type = request.getParameter("type");
		String values = request.getParameter("val");
		
		String times = request.getParameter("times");
		
		
		int stime = 0;
		int etime = 0;
		
		if (StringUtils.isNotEmpty(times)) {
			
			stime = DateUtil.dateToLong(times, "yyyy-MM", "day", 0).intValue();
			etime = DateUtil.dateToLong(times, "yyyy-MM", "day", 1).intValue();
		}
		
		List<Map<String, Object>> anchorList = new ArrayList<Map<String,Object>>();
		
		if ("unionName".equalsIgnoreCase(type)) {
			anchorList = anchorService.getPunishAnchorListByUnionName(values,stime,etime);
		}else if ("uid".equalsIgnoreCase(type)) {
			anchorList = anchorService.getPunishAnchorListByUid(values.substring(values.length()-2, values.length()), Integer.valueOf(values), stime, etime);
		}

		HashMap<String, Object> map = new HashMap<String,Object>();
		map.put("rows", anchorList);
		map.put("total", anchorList.size());
		
		return map;
	}
}
