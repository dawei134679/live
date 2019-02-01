package com.tinypig.newadmin.web.action;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.redis.service.RedisOperat;
import com.tinypig.admin.util.RedisContant;
import com.tinypig.newadmin.common.RSAUtils;
import com.tinypig.newadmin.web.entity.SysCopyright;
import com.tinypig.newadmin.web.service.ISysCopyrightService;

@Controller
@RequestMapping("/sysCopyright")
public class SysCopyrightAction {

	@Autowired
	private ISysCopyrightService sysCopyrightService;
	
	private static Logger log =  Logger.getLogger(SysCopyrightAction.class);
	
	 private static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCPDdlvvBc8lGxJ3iU5pNqwqIHm/u3MdszVCYrqAF/ZgA3pmk94x9BC3kLr03Ls9kCKkDdWIiTwSV19UNcn7or6o0vNGHXetxI/HtSmHRzG7+9K1F0PY+yvELyYZD4EohnV0v/JTFIrWv45BzCUQj5gTXc4/4HmCTXoJeIVwetdWQIDAQAB";
	
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public void saveSysCopyright(@RequestParam(value = "copyright", required = false) MultipartFile copyright,
							HttpServletRequest request,HttpServletResponse response) {
		Map<String,Object> resMap = new HashMap<String,Object>();
		 PrintWriter out = null;
		try {
			response.setHeader("Content-type", "text/html;charset=UTF-8");  
			out = response.getWriter();
			if(copyright==null||copyright.isEmpty()){
				resMap.put("code", "201");
				resMap.put("msg", "授权文件为空");
			}else {
				SysCopyright sysCopyright = sysCopyrightService.getSysCopyright();
				String copyrightData = new String(copyright.getBytes());
				//保存
				if(sysCopyright==null) {
					sysCopyright = new SysCopyright();
					sysCopyright.setCopyrightData(copyrightData);
					int i = sysCopyrightService.saveSysCopyright(sysCopyright);
					if(i==0) {
						resMap.put("code", "201");
						resMap.put("msg", "更新授权信息异常");
					}else {
						resMap.put("code", "200");
						SysCopyright bean = sysCopyrightService.getSysCopyright();
						if(sysCopyright!=null) {
							String base64Data = bean.getCopyrightData();
					        byte[] decodedData = RSAUtils.decryptByPublicKey(Base64.decodeBase64(base64Data), publicKey);
					        JSONObject json = JSON.parseObject(new String(decodedData));
					        resMap.put("data", json);
						}
					}
				}else {
					sysCopyright.setCopyrightData(copyrightData);
					int i = sysCopyrightService.updateSysCopyright(sysCopyright);
					if(i==0) {
						resMap.put("code", "201");
						resMap.put("msg", "更新授权信息异常");
					}else {
						resMap.put("code", "200");
						SysCopyright bean = sysCopyrightService.getSysCopyright();
						if(sysCopyright!=null) {
							String base64Data = bean.getCopyrightData();
					        byte[] decodedData = RSAUtils.decryptByPublicKey(Base64.decodeBase64(base64Data), publicKey);
					        JSONObject json = JSON.parseObject(new String(decodedData));
					        resMap.put("data", json);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			resMap.put("code", "203");
			resMap.put("msg", "获取授权信息异常");
		}finally {
	         out.println("<script>");//输出script标签
	         out.print("window.parent.callBack("+JSON.toJSONString(resMap)+")");
	         out.println("</script>");//输出script结尾标签
	         if(out!=null) {
	        	 out.flush();//清空缓存
	        	 out.close();
	         }
		}
	}
	
	@RequestMapping(value="/getSysCopyright",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getSysCopyright() {
		Map<String,Object> resMap = new HashMap<String,Object>();
		try {
			SysCopyright sysCopyright = sysCopyrightService.getSysCopyright();
			if(sysCopyright!=null) {
				String base64Data = sysCopyright.getCopyrightData();
		        byte[] decodedData = RSAUtils.decryptByPublicKey(Base64.decodeBase64(base64Data), publicKey);
		        JSONObject json = JSON.parseObject(new String(decodedData));
		        resMap.put("data", json);
			}
			resMap.put("code", "200");
			return resMap;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			resMap.put("code", "203");
			resMap.put("msg", "获取授权信息异常");
			return resMap;
		}
	}
	
	@RequestMapping(value="/reRedis",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> reRedis(){
		Map<String,Object> resMap = new HashMap<String,Object>();
		try {
			SysCopyright sysCopyright = sysCopyrightService.getSysCopyright();
			if(sysCopyright==null) {
				resMap.put("msg",  "授权信息为空，更新缓存失败");
				resMap.put("code", "200");
				return resMap;
			}
			RedisOperat.getInstance().set(RedisContant.host, RedisContant.port6379,RedisContant.sysCopyright,sysCopyright.getCopyrightData()); 
			resMap.put("code", "200");
			return resMap;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			resMap.put("code", "203");
			resMap.put("msg", "获取授权信息异常");
			return resMap;
		}
	}
}
