package com.mpig.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mpig.api.service.IInterfaceService;

@Controller
@RequestMapping("/interface")
public class InterfaceController {
	private static final Logger logger = Logger.getLogger(InterfaceController.class);
	
	@Resource
	private IInterfaceService interfaceService;

	@RequestMapping(value = "/getLivingList")
	@ResponseBody
	public List<Map<String, Object>> getLivingAnchor(HttpServletRequest request,HttpServletResponse response){
		
		List<Map<String, Object>> listResult = new ArrayList<Map<String,Object>>();
		
		int num = 0;
		String strNum = request.getParameter("num");
		
		if (StringUtils.isNotEmpty(strNum)) {
			try {
				num = Integer.valueOf(strNum);
			} catch (Exception e) {
				logger.error("getLivingAnchor:",e);
			}
		}
		if (num <= 0) {
			return listResult;
		}else {
			return interfaceService.getLivingAnchor(num);
		}
	}
}
