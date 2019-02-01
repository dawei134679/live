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
import com.tinypig.newadmin.web.entity.GiftLuckyProbabilitys;
import com.tinypig.newadmin.web.entity.SystemNotice;
import com.tinypig.newadmin.web.service.LuckyGiftService;

@Controller
@RequestMapping("/luckygift")
public class LuckyGiftAction extends BaseAction{

	@Autowired
	private LuckyGiftService luckyGiftService;
	
	@RequestMapping(value = "/prizeList")
	public String prizeList(){
		return "/gift/prizelist";
	}
	
	@RequestMapping(value = "/listAll")
	@ResponseBody
	public Map<String, Object> getPrizeList() throws ParseException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap = putPageMap(luckyGiftService.selectCount(), luckyGiftService.selectList());
		return resultMap;
	}
	
	@RequestMapping(value = "/editPage")
	public String editPage(@ModelAttribute("gift") GiftLuckyProbabilitys gift, Model model){
		if(gift!=null && gift.getIsAddOrEdit()!=null){
			if(ConstantsAction.EDIT.equals(gift.getIsAddOrEdit()) && gift.getId()!=null){//修改页面
				GiftLuckyProbabilitys editgift = luckyGiftService.selectByPrimaryKey(gift.getId());
				if(editgift!=null){
					editgift.setIsAddOrEdit(ConstantsAction.EDIT);
					model.addAttribute("gift", editgift);  
				}
				
			}
		}
		return "/gift/prizedit";
	}
	
	@RequestMapping(value = "/doedit", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String doedit(@ModelAttribute("gift") GiftLuckyProbabilitys gift, Model model) throws ParseException, UnsupportedEncodingException{
		boolean flag = false;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		//中奖概率不能大于15/1000, 中奖倍数不大于1000,不小于0
		if(gift.getMultiples() > 1000){
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "中奖倍数不大于1000!");
			return JSONObject.toJSONString(resultMap);
		}else if(gift.getMultiples() <= 0){
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "中奖倍数必须大于0!");
			return JSONObject.toJSONString(resultMap);
		}
		if(Double.parseDouble(gift.getDivisor()+"")/Double.parseDouble(gift.getDividend()+"") > Double.parseDouble(15+"")/Double.parseDouble(1000+"")){
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "中奖概率不能大于15/1000!");
			return JSONObject.toJSONString(resultMap);
		}
		
		
		if(ConstantsAction.EDIT.equals(gift.getIsAddOrEdit()) && gift.getId()!=null){
			flag = luckyGiftService.updateByObject(gift);
		}else if(ConstantsAction.ADD.equals(gift.getIsAddOrEdit())){
			//新增倍数不能重复
			if(luckyGiftService.checkMultiples(gift.getMultiples())){
				resultMap.put(ConstantsAction.RESULT, false);
				resultMap.put(ConstantsAction.MSG, "该中奖倍数已存在!");
				return JSONObject.toJSONString(resultMap);
			}
			
			flag = luckyGiftService.insertByObject(gift);
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
	public Map<String, Object> delGiftLuckyProbabilitys(@ModelAttribute("gift") GiftLuckyProbabilitys gift){
		boolean flag = false;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		flag = luckyGiftService.deleteByPrimaryKey(gift.getId());
		resultMap.put(ConstantsAction.RESULT, flag);

		return resultMap;
	}
	
	//幸运礼物中奖概率配置,通知业务服务器
	private boolean notifyHttpServer(){
		//循环tomcat服务器列表, 同步新的开屏幸运礼物配置信息
		List<String> list = HttpServerList.getInstance().getServerList();
		boolean flag = true;
		for(int i=0;i<list.size();i++){
			try {
				String url = list.get(i);
				HttpResponse<JsonNode> res = Unirest.get(url + "admin/updateProbabilitys?adminid=admin").asJson();
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
