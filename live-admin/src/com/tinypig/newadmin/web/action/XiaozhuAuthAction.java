package com.tinypig.newadmin.web.action;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tinypig.newadmin.common.BaseAction;
import com.tinypig.newadmin.common.ConstantsAction;
import com.tinypig.newadmin.web.entity.UserXiaozhuAuth;
import com.tinypig.newadmin.web.entity.UserXiaozhuAuthWithBLOBs;
import com.tinypig.newadmin.web.service.XiaozhuAuthService;

@Controller
@RequestMapping("/pigauth")
public class XiaozhuAuthAction extends BaseAction{

	@Autowired
	private XiaozhuAuthService authService;

	
	@RequestMapping(value = "/list")
	@ResponseBody
	public Map<String, Object> getList(@ModelAttribute("auth") UserXiaozhuAuth auth) throws ParseException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(auth==null){
			auth = new UserXiaozhuAuth();
		}
		auth.setRowNumStart(getRowNumStart());
		auth.setPageSize(getPageSize());

		resultMap = putPageMap(authService.selectCount(auth), authService.selectList(auth));

		return resultMap;
	}
	
	@RequestMapping(value = "/approve")
	@ResponseBody
	public Map<String, Object> approve(@ModelAttribute("id") Integer id){
		boolean flag = false;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);
		long auditat = new Date().getTime()/1000;
		paramMap.put("auditat", auditat);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//根据id查询
		UserXiaozhuAuthWithBLOBs userXiaozhuAuthWithBLOBs  = authService.selectByPrimaryKey(id);
		//检查昵称是否被占用
		UserXiaozhuAuth auth = authService.checkNickName(userXiaozhuAuthWithBLOBs.getNickname());
		if(auth != null){
			flag = false;
			resultMap.put(ConstantsAction.RESULT, flag);
			resultMap.put(ConstantsAction.MSG, "同时申请该昵称人较多, 该昵称已被占用, 请驳回并给与合理解释.");
			return resultMap;
		}
		
		flag = authService.updateByPrimaryKey(paramMap);
		resultMap.put(ConstantsAction.RESULT, flag);

		return resultMap;
	}
	
	@RequestMapping(value = "/reject")
	@ResponseBody
	public Map<String, Object> reject(@ModelAttribute("id") Integer id, @ModelAttribute("cause") String cause){
		boolean flag = false;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);
		paramMap.put("cause", cause);
		long auditat = new Date().getTime()/1000;
		paramMap.put("auditat", auditat);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		flag = authService.rejectByPrimaryKey(paramMap);
		resultMap.put(ConstantsAction.RESULT, flag);

		return resultMap;
	}

	
	@RequestMapping(value = "/authList")
	public String authList(){
		return "/pigauth/authlist";
	}
	
	@RequestMapping(value = "/getinfo")
	public String getInfo(@ModelAttribute("id") Integer id, Model model){
		if(id != null){
			UserXiaozhuAuthWithBLOBs authinfo = authService.selectByPrimaryKey(id);
			if(authinfo != null){
				model.addAttribute("auth", authinfo);  
			}
				
		}
		return "/pigauth/info";
	}

}
