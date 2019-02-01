package com.tinypig.newadmin.web.action;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.qiniu.common.BannerConfig;
import com.tinypig.admin.util.QiniuUpUtils;
import com.tinypig.newadmin.common.BaseAction;
import com.tinypig.newadmin.common.ConstantsAction;
import com.tinypig.newadmin.common.HttpServerList;
import com.tinypig.newadmin.web.entity.WebBanner;
import com.tinypig.newadmin.web.service.WebBannerService;
import com.tinypig.newadmin.web.vo.WebBannerVo;

@Controller
@RequestMapping("/banner")
public class WebBannerAction extends BaseAction{

	@Autowired
	private WebBannerService bannerService;

	
	@RequestMapping(value = "/listAll")
	@ResponseBody
	public Map<String, Object> getBannerList(@ModelAttribute("banner") WebBannerVo banner) throws ParseException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(banner==null){
			banner = new WebBannerVo();
		}
		banner.setRowNumStart(getRowNumStart());
		banner.setPageSize(getPageSize());
		if(banner.getStartDate()!=null && !banner.getStartDate().isEmpty()){
			String startShow[] = banner.getStartDate().split("[-:]");
			String start=startShow[0]+startShow[1]+startShow[2].replace(" ", "")+startShow[3]+startShow[4];
			Calendar c = Calendar.getInstance();
			c.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(start));
			banner.setStartshow(c.getTimeInMillis()/1000);
		}
		if(banner.getEndDate()!=null && !banner.getEndDate().isEmpty()){
			String endShow[] = banner.getEndDate().split("[-:]");
			String end=endShow[0]+endShow[1]+endShow[2].replace(" ", "")+endShow[3]+endShow[4];
			Calendar c = Calendar.getInstance();
			c.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(end));
			banner.setEndshow(c.getTimeInMillis()/1000);
		}
		resultMap = putPageMap(bannerService.selectCount(banner), bannerService.selectList(banner));

		return resultMap;
	}
	
	@RequestMapping(value = "/del")
	@ResponseBody
	public Map<String, Object> delbanner(@ModelAttribute("banner") WebBanner banner){
		boolean flag = false;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		WebBannerVo webBannerVo = bannerService.selectByPrimaryKey(banner.getId());
		if(null != webBannerVo) {
			Integer type = webBannerVo.getType();
			flag = bannerService.deleteByPrimaryKey(banner.getId());
			if (flag) {
				if(type == 0){//开屏广告
					flag =  notifyHttpServer();
				}else if (type == 1) {
					flag = notifyBannerList();
				}
			}
		}
		resultMap.put(ConstantsAction.RESULT, flag);
		return resultMap;
	}
	@RequestMapping(value = "/doedit", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String doedit(@ModelAttribute("banner") WebBannerVo banner,Model model,
			@RequestParam(value = "fileapp", required = false) MultipartFile fileapp,
			@RequestParam(value = "fileweb", required = false) MultipartFile fileweb) throws ParseException, UnsupportedEncodingException{
		boolean flag = false;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(banner!=null){
			if(banner.getStartDate()!=null && !"".equals(banner.getStartDate())){
				String start= banner.getStartDate();
				Calendar c = Calendar.getInstance();
				c.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(start));
				banner.setStartshow(c.getTimeInMillis()/1000);
				//开屏广告，需要时间不与其他开屏广告冲突
				if(banner.getType() == 0){
					boolean status = bannerService.checkSheduleTime(banner);
					if(!status){//时间冲突
						resultMap.put(ConstantsAction.RESULT, false);
						resultMap.put(ConstantsAction.MSG, "时间冲突，请重新设置时间！");
						return JSONObject.toJSONString(resultMap);
					}
				}
				
			}
			if(banner.getEndDate()!=null && !"".equals(banner.getEndDate())){
				String end= banner.getEndDate();
				Calendar c = Calendar.getInstance();
				c.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(end));
				banner.setEndshow(c.getTimeInMillis()/1000);
			}
			String url = "";
			
			if(fileapp!=null && !fileapp.isEmpty()){
				//先删除文件
				if(ConstantsAction.EDIT.equals(banner.getIsAddOrEdit())){//只有编辑的时候删
					String urlapp[] = banner.getPicurl().split("/");
					QiniuUpUtils.delete(urlapp[banner.getPicurl().split("/").length-1], BannerConfig.BANNER);
				}
				//再上传
				url = QiniuUpUtils.uploadfile(fileapp,BannerConfig.BANNER,BannerConfig.DOMAIL);
				banner.setPicurl(url);
			}
			if(fileweb!=null && !fileweb.isEmpty()){
				//先删除文件
				if(ConstantsAction.EDIT.equals(banner.getIsAddOrEdit())){//只有编辑的时候删
					String urlweb[] = banner.getWebPicUrl().split("/");
					QiniuUpUtils.delete(urlweb[banner.getWebPicUrl().split("/").length-1], BannerConfig.BANNER);
				}
				//再上传
				url = QiniuUpUtils.uploadfile(fileweb,BannerConfig.BANNER,BannerConfig.DOMAIL);
				banner.setWebPicUrl(url);
			}
		}
		
		if(ConstantsAction.EDIT.equals(banner.getIsAddOrEdit()) && banner.getId()!=null){
			flag = bannerService.updateByObject(banner);
			if(flag){
				if(banner.getType() == 0){//开屏广告
					flag =  notifyHttpServer();
				}else if (banner.getType() == 1) {
					flag = notifyBannerList();
				}
			}
		}else if(ConstantsAction.ADD.equals(banner.getIsAddOrEdit())){
			flag = bannerService.insertByObject(banner);
			if(flag){
				if(banner.getType() == 0){//开屏广告
					flag = notifyHttpServer();
				}else if (banner.getType() == 1) {
					flag = notifyBannerList();
				}
			}
		}
		
		resultMap.put(ConstantsAction.RESULT, flag);
		resultMap.put(ConstantsAction.MSG, "修改成功!");
		return JSONObject.toJSONString(resultMap);
	}

	
	@RequestMapping(value = "/bannerList")
	public String bannerList(@ModelAttribute("type") String type){
		return "/banner/bannerlist";
	}
	
	@RequestMapping(value = "/editPage")
	public String editPage(@ModelAttribute("banner") WebBannerVo banner,Model model,@ModelAttribute("type") String type){
		if(banner!=null && banner.getIsAddOrEdit()!=null){
			if(ConstantsAction.EDIT.equals(banner.getIsAddOrEdit()) && banner.getId()!=null){//修改页面
				WebBannerVo editbanner = bannerService.selectByPrimaryKey(banner.getId());
				if(editbanner!=null){
					editbanner.setIsAddOrEdit(ConstantsAction.EDIT);
					model.addAttribute("banner", editbanner);  
				}
				
			}
		}
		return "/banner/edit";
	}
	
	//开屏广告,通知http服务器
	private boolean notifyHttpServer(){
		//循环tomcat服务器列表, 同步新的开屏广告信息
		List<String> list = HttpServerList.getInstance().getServerList();
		boolean flag = true;
		for(int i=0;i<list.size();i++){
			try {
				String url = list.get(i);
				HttpResponse<JsonNode> res = Unirest.get(url + "admin/updateOpenScreem?adminid=admin").asJson();
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

	//轮播列表通知
	private boolean notifyBannerList(){
		//循环tomcat服务器列表, 同步新的开屏广告信息
		List<String> list = HttpServerList.getInstance().getServerList();
		boolean flag = true;
		for(int i=0;i<list.size();i++){
			try {
				String url = list.get(i);
				HttpResponse<JsonNode> res = Unirest.get(url + "admin/updateBanner?adminid=admin").asJson();
				org.json.JSONObject object = res.getBody().getObject();
				if(Integer.parseInt(object.get("code").toString()) == 200){
					//						System.out.println("success-----------------------");
				}else{
					flag = false;
				}
			} catch (UnirestException e) {
				
				e.printStackTrace();
				flag = false;
			}
		}

		return flag;
	}
}
