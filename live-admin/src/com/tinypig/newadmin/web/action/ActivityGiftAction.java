package com.tinypig.newadmin.web.action;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.tinypig.admin.util.DateUtil;
import com.tinypig.newadmin.common.BaseAction;
import com.tinypig.newadmin.common.ConstantsAction;
import com.tinypig.newadmin.common.HttpServerList;
import com.tinypig.newadmin.web.service.ActivityGiftService;
import com.tinypig.newadmin.web.vo.ActivityGiftVo;

@Controller
@RequestMapping("/actgift")
public class ActivityGiftAction extends BaseAction{

	@Autowired
	private ActivityGiftService activityGiftService;
	
	@RequestMapping(value = "/actList")
	public String prizeList(){
		return "/gift/activitygiftlist";
	}
	
	@RequestMapping(value = "/listAll")
	@ResponseBody
	public Map<String, Object> getList(@ModelAttribute("gift") ActivityGiftVo gift) throws ParseException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(gift==null){
			gift = new ActivityGiftVo();
		}
		gift.setRowNumStart(getRowNumStart());
		gift.setPageSize(getPageSize());
		resultMap = putPageMap(activityGiftService.selectCount(gift), activityGiftService.selectList(gift));
		return resultMap;
	}
	
	@RequestMapping(value = "/editPage")
	public String editPage(@ModelAttribute("gift") ActivityGiftVo gift, Model model){
		if(gift!=null && gift.getIsAddOrEdit()!=null){
			if(ConstantsAction.EDIT.equals(gift.getIsAddOrEdit()) && gift.getId()!=null){//修改页面
				ActivityGiftVo editgift = activityGiftService.selectByPrimaryKey(gift.getId());
				if(editgift!=null){
					editgift.setIsAddOrEdit(ConstantsAction.EDIT);
					model.addAttribute("gift", editgift);
				}
				
			}
		}
		return "/gift/activitygiftedit";
	}
	
	@RequestMapping(value = "/getGiftList", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getGiftList(){
		ArrayList<HashMap<String, Object>> list = activityGiftService.getGiftList();
		return JSONObject.toJSONString(list);
	}
	
	@RequestMapping(value = "/getAllGiftList", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getAllGiftList(){
		ArrayList<HashMap<String, Object>> list = activityGiftService.getAllGiftList();
		return JSONObject.toJSONString(list);
	}
	
	@RequestMapping(value = "/doedit", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String doedit(@ModelAttribute("gift") ActivityGiftVo gift, Model model) throws ParseException, UnsupportedEncodingException{
		boolean flag = false;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		long stime = 0l;
		long etime = 0l;
		
		if(gift.getStarttime()!=null && !gift.getStarttime().isEmpty()){
//			String startShow[] = gift.getStarttime().split("[-:]");
//			String start=startShow[0]+startShow[1]+startShow[2].replace(" ", "")+startShow[3]+startShow[4];
			Calendar c = Calendar.getInstance();
			c.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(gift.getStarttime()));
			gift.setStime(c.getTimeInMillis()/1000);
			stime = c.getTimeInMillis();
		}
		if(gift.getEndtime()!=null && !gift.getEndtime().isEmpty()){
//			String endShow[] = gift.getEndtime().split("[-:]");
//			String end=endShow[0]+endShow[1]+endShow[2].replace(" ", "")+endShow[3]+endShow[4];
			Calendar c = Calendar.getInstance();
			c.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(gift.getEndtime()));
			gift.setEtime(c.getTimeInMillis()/1000);
			etime = c.getTimeInMillis();
		}
		
		//校验时间,结束时间必须大于开始时间
		if(etime <= stime){
			resultMap.put(ConstantsAction.RESULT, flag);
			resultMap.put(ConstantsAction.MSG, "结束时间必须大于开始时间!");
			return JSONObject.toJSONString(resultMap);
		}
		
		if(ConstantsAction.EDIT.equals(gift.getIsAddOrEdit()) && gift.getId()!=null){
			flag = activityGiftService.updateByObject(gift);
		}else if(ConstantsAction.ADD.equals(gift.getIsAddOrEdit())){
			//开始时间必须小于当前时间,结束时间必须大于当前时间
//			if(stime >= new Date().getTime()){
//				resultMap.put(ConstantsAction.RESULT, flag);
//				resultMap.put(ConstantsAction.MSG, "开始时间必须小于当前时间,保证活动礼物当前有效!");
//				return JSONObject.toJSONString(resultMap);
//			}else if(etime <= new Date().getTime()){
//				resultMap.put(ConstantsAction.RESULT, flag);
//				resultMap.put(ConstantsAction.MSG, "结束时间已过期!");
//				return JSONObject.toJSONString(resultMap);
//			}
			if(etime <= new Date().getTime()){
				resultMap.put(ConstantsAction.RESULT, flag);
				resultMap.put(ConstantsAction.MSG, "结束时间已过期! etime:"+etime+" now:"+new Date().getTime());
				return JSONObject.toJSONString(resultMap);
			}
			flag = activityGiftService.insertByObject(gift);
		}
		
		if(flag){//活动礼物增加修改,通知业务服务器
			flag = notifyHttpServer();
		}
			
		
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
	public Map<String, Object> delActivityGift(Integer id){
		boolean flag = false;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		flag = activityGiftService.deleteByPrimaryKey(id);
		
		if(flag){//活动礼物增加修改,通知业务服务器
			flag = notifyHttpServer();
		}
		resultMap.put(ConstantsAction.RESULT, flag);

		return resultMap;
	}
	
	//活动礼物增加修改,通知业务服务器
	private boolean notifyHttpServer(){
		//循环tomcat服务器列表, 同步新的开屏幸运礼物配置信息
		List<String> list = HttpServerList.getInstance().getServerList();
		boolean flag = true;
		for(int i=0;i<list.size();i++){
			try {
				String url = list.get(i);
				HttpResponse<JsonNode> res = Unirest.get(url + "admin/updateGiftAct?adminid=admin").asJson();
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

	public static void main(String[] args) {
		
		Long lg = 1473609600000L;
		String formatDate = null;
		try {
			formatDate = DateUtil.formatDate(new Date(lg), "yyyyMMdd");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("for:"+formatDate);
	}
}
