package com.tinypig.newadmin.web.action;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.tinypig.newadmin.common.BaseAction;
import com.tinypig.newadmin.common.ConstantsAction;
import com.tinypig.newadmin.common.HttpServerList;
import com.tinypig.newadmin.web.entity.SystemNotice;
import com.tinypig.newadmin.web.service.SystemNoticeService;

@Controller
@RequestMapping("/notice/system")
public class SystemNoticeAction extends BaseAction{

	@Autowired
	private SystemNoticeService systemNoticeService;
	
	@RequestMapping(value = "/noticeList")
	public String noticeList(){
		return "/notice/noticelist";
	}
	
	@RequestMapping(value = "/listAll")
	@ResponseBody
	public Map<String, Object> getNoticeList() throws ParseException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap = putPageMap(systemNoticeService.selectCount(), systemNoticeService.selectList());
		return resultMap;
	}
	
	@RequestMapping(value = "/editPage")
	public String editPage(@ModelAttribute("notice") SystemNotice notice,Model model){
		if(notice!=null && notice.getIsAddOrEdit()!=null){
			if(ConstantsAction.EDIT.equals(notice.getIsAddOrEdit()) && notice.getId()!=null){//修改页面
				SystemNotice editnotice = systemNoticeService.selectByPrimaryKey(notice.getId());
				if(editnotice!=null){
					editnotice.setIsAddOrEdit(ConstantsAction.EDIT);
					model.addAttribute("notice", editnotice);  
				}
				
			}
		}
		return "/notice/edit";
	}
	
	@RequestMapping(value = "/doedit", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String doedit(@ModelAttribute("notice") SystemNotice notice,Model model) throws ParseException, UnsupportedEncodingException{
		boolean flag = false;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(ConstantsAction.EDIT.equals(notice.getIsAddOrEdit()) && notice.getId()!=null){
			flag = systemNoticeService.updateByObject(notice);
		}else if(ConstantsAction.ADD.equals(notice.getIsAddOrEdit())){
			flag = systemNoticeService.insertByObject(notice);
		}
		
		if(flag)
			flag = notifyHttpServer();
		
		if(flag){
			resultMap.put(ConstantsAction.RESULT, flag);
			resultMap.put(ConstantsAction.MSG, "修改成功!");
		}else{
			resultMap.put(ConstantsAction.RESULT, flag);
			resultMap.put(ConstantsAction.MSG, "修改失败!");
		}
		
		return JSONObject.toJSONString(resultMap);
	}
	
	@RequestMapping(value = "/del")
	@ResponseBody
	public Map<String, Object> delNotice(@ModelAttribute("notice") SystemNotice notice){
		boolean flag = false;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		flag = systemNoticeService.deleteByPrimaryKey(notice.getId());
		resultMap.put(ConstantsAction.RESULT, flag);

		return resultMap;
	}
	
	//系统公告,通知业务服务器
	private boolean notifyHttpServer(){
		//循环tomcat服务器列表, 同步新的开屏广告信息
		List<String> list = HttpServerList.getInstance().getServerList();
		boolean flag = true;
		for(int i=0;i<list.size();i++){
			try {
				String url = list.get(i);
				HttpResponse<JsonNode> res = Unirest.get(url + "admin/updateLiveNotice?adminid=admin").asJson();
				org.json.JSONObject object = res.getBody().getObject();
				if(Integer.parseInt(object.get("code").toString()) == 200){
					//						System.out.println("success-----------------------");
				}else{
					flag = false;
				}
			} catch (UnirestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				flag = false;
			}
		}

		return flag;
	}
}
