package com.tinypig.newadmin.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.newadmin.common.ConstantsAction;
import com.tinypig.newadmin.web.entity.SmashedEggGiftConfig;
import com.tinypig.newadmin.web.service.ISmashedEggGiftConfigService;

@Controller
@RequestMapping("/smashedEggGiftConfig")
public class SmashedEggGiftConfigAction {

	@Autowired
	private ISmashedEggGiftConfigService smashedEggGiftConfigService;
	
	@RequestMapping(value="/smashedEggGiftConfigList")
	@ResponseBody
	public String smashedEggGiftConfigList(HttpServletRequest res) {
		String hammerTypeStr = res.getParameter("hammerType");
		Integer	hammerType = Integer.valueOf(StringUtils.isBlank(hammerTypeStr)?"-1":hammerTypeStr);
		Integer page = Integer.valueOf(res.getParameter("page"));
		Integer rows = Integer.valueOf(res.getParameter("rows"));
		List<SmashedEggGiftConfig> list = smashedEggGiftConfigService.getGiftList(hammerType, page, rows);
		return JSONObject.toJSONString(list);
	}
	
	@RequestMapping(value="/saveSmashedEggGiftConfig",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String saveSmashedEggGiftConfig(HttpServletRequest request,SmashedEggGiftConfig smashedEggGiftConfig) {
		Map<String, Object> resultMap = new HashMap<String,Object>();
		
		if ((Double) smashedEggGiftConfigService.getSumProbability(smashedEggGiftConfig.getHammerType(), 0L).get("sp") + smashedEggGiftConfig.getProbability() <= 1) {
			int row = smashedEggGiftConfigService.saveSmashedEggGiftConfig(smashedEggGiftConfig);
			if(row==1){
				//更新数据的头奖为0,否
				int row1 = smashedEggGiftConfigService.updateNoFirstPrize(smashedEggGiftConfig.getHammerType(),0L);

				resultMap.put(ConstantsAction.RESULT, true);
				resultMap.put(ConstantsAction.MSG, "保存成功");
			}else {
				resultMap.put(ConstantsAction.RESULT, false);
				resultMap.put(ConstantsAction.MSG, "保存失败");
			}
		} else {
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "不同分类中的礼物中奖概率之和不能大于1!");
		}
		return JSONObject.toJSONString(resultMap);
	}
	
	@RequestMapping(value="/updateSmashedEggGiftConfig",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateSmashedEggGiftConfig(HttpServletRequest request,SmashedEggGiftConfig smashedEggGiftConfig) {
		Map<String, Object> resultMap = new HashMap<String,Object>();
		if ((Double) smashedEggGiftConfigService.getSumProbability(smashedEggGiftConfig.getHammerType(),smashedEggGiftConfig.getId()).get("sp") + smashedEggGiftConfig.getProbability() <= 1) {
			int row = smashedEggGiftConfigService.updateByPrimaryKeySelective(smashedEggGiftConfig);
			if(row==1){
				//更新除当前id以外的数据的头奖为0,否
				smashedEggGiftConfigService.updateNoFirstPrize(smashedEggGiftConfig.getHammerType(),smashedEggGiftConfig.getId());
				
				resultMap.put(ConstantsAction.RESULT, true);
				resultMap.put(ConstantsAction.MSG, "保存成功");
			}else {
				resultMap.put(ConstantsAction.RESULT, false);
				resultMap.put(ConstantsAction.MSG, "保存失败");
			}
		}else {
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "不同分类中的礼物中奖概率之和不能大于1!");
		}
		return JSONObject.toJSONString(resultMap);
	}
	
	@RequestMapping(value="/delSmashedEggGiftConfig")
	@ResponseBody
	public Map<String,Object> delSmashedEggGiftConfig(Long id){
		Map<String, Object> resultMap = new HashMap<String,Object>();
		int i = smashedEggGiftConfigService.delSmashedEggGiftConfig(id);
		if(i==0) {
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "删除失败"); 
			return resultMap;
		}
		resultMap.put(ConstantsAction.RESULT, true);
		resultMap.put(ConstantsAction.MSG, "删除成功");
		return resultMap;
	}
	
	@RequestMapping(value="/reSmashedEggGiftConfigRedis",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String reSmashedEggGiftConfigRedis() {
		Map<String, Object> map = smashedEggGiftConfigService.reSmashedEggGiftConfigRedis();
		return JSONObject.toJSONString(map);
	}
}

