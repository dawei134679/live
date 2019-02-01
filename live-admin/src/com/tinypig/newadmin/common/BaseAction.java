package com.tinypig.newadmin.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;


public class BaseAction {

	// 当前页码
	protected int pageCurrent = 1;
	// 每页多少条
	protected int pageSize=10;
	// 数据总条数
	protected int totalNum;
	
	protected HttpServletRequest request;
	
	protected HttpServletResponse response;  
    /**
     * 
        * @Title: setReqAndRes
        * @Description: TODO(通过注解方式获取请求和响应)
        * @param @param request
        * @param @param response    参数
        * @return void    返回类型
        * @throws
     */
    @ModelAttribute  
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response){  
        this.request = request;  
        this.response = response;  
    }  
    
    /**
     * 
        * @Title: initBinder
        * @Description: TODO(指定默认时间转换器)
        * @param @param binder    参数
        * @return void    返回类型
        * @throws
     */
    @InitBinder    
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true)); 
    }
    
	/**
	 * 
	 * @Title: getRowNumStart
	 * @Description: TODO(获取从第几条开始查询的数值)
	 * @param @return 参数
	 * @return int 返回类型
	 * @throws
	 */
	protected int getRowNumStart() {
		if(null != request.getParameter("pageSize") && !request.getParameter("pageSize").equals("")){
			pageSize = Integer.parseInt(request.getParameter("pageSize"));
		}
		if(null != request.getParameter("pageCurrent") && !request.getParameter("pageCurrent").equals("")){
			pageCurrent = Integer.parseInt(request.getParameter("pageCurrent"));
		}
		return pageCurrent * pageSize - pageSize;
	}
	/**
	 * 
	    * @Title: getPageSize
	    * @Description: TODO(获取每页显示条数)
	    * @param @return    参数
	    * @return int    返回类型
	    * @throws
	 */
	protected int getPageSize() {
		if(null != request.getParameter("pageSize") && !request.getParameter("pageSize").equals("")){
			pageSize = Integer.parseInt(request.getParameter("pageSize"));
		}
		return pageSize;
	}
	

	/**
	 * 
	 * @Title: putPageMap
	 * @Description: TODO(封装分页信息)
	 * @param @param totalNum
	 * @param @param list
	 * @param @return 参数
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	protected Map<String, Object> putPageMap(int totalNum, List list) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("DATALIST", list);
		int totalPage = 0;
		if (totalNum % pageSize == 0) {
			totalPage = totalNum / pageSize;
		} else {
			totalPage = totalNum / pageSize + 1;
		}
		map.put("TOTALPAGE", totalPage);
		map.put("TOTALNUM", totalNum);
		map.put("PAGECURRENT", pageCurrent);
		map.put("PAGESIZE", pageSize);
		return map;
	}

}
