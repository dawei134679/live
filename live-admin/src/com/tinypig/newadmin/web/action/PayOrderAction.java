package com.tinypig.newadmin.web.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.dao.UserDao;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.model.UserBaseInfoModel;
import com.tinypig.admin.util.DateUtil;
import com.tinypig.newadmin.web.entity.PayOrder;
import com.tinypig.newadmin.web.service.PayOrderService;

@Controller
@RequestMapping("payOrder")
public class PayOrderAction {

	private static Logger log = Logger.getLogger(PayOrderAction.class);

	@Autowired
	private PayOrderService payOrderService;
	
	@RequestMapping(value = "/savePayOrder" , produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String savePayOrder(HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
			Integer createUserId = adminUser == null ? 0 : adminUser.getUid();
			Long createTime = new Date().getTime();
			int srcuid = Integer.parseInt(request.getParameter("uid"));
			UserBaseInfoModel userBaseInfo = UserDao.getInstance().getUserBaseInfo(srcuid, false);
			if(null==userBaseInfo) {
				map.put("code", "201");
				map.put("message", "用户不存在");
				return JSONObject.toJSONString(map);
			}
			int os = 3;
			String orderId = os+""+DateUtil.datetime2String(createTime, "yyyyMMddHHmmssSSS");
			double amount = Float.valueOf(request.getParameter("amount"));
			String paytype = request.getParameter("paytype");
			int zhutou  = (int) (amount*100);
			String detail = "充值金币"+zhutou+"个金币_"+amount+""+paytype;
			String userSource = "official";
			int dataType = 2;
			PayOrder payOrder = new PayOrder();
			payOrder.setOrderId(orderId);
			payOrder.setOs(os);
			payOrder.setStatus(2);//成功
			payOrder.setSrcuid(srcuid);
			payOrder.setDstuid(srcuid);
			payOrder.setZhutou(zhutou);
			payOrder.setPaytype(paytype);
			payOrder.setAmount(amount);
			payOrder.setDataType(dataType);
			payOrder.setCreateTime(createTime);
			payOrder.setCreateUserId(createUserId);
			payOrder.setDetails(detail);
			payOrder.setDataType(dataType);
			payOrder.setUsersource(userSource);
			payOrder.setInpourNo(getTime3Random());
			String date = request.getParameter("date");
			String dateTime = date +" " +DateUtil.formatDate(new Date(), "HH:mm:ss");
			Long creatAt = DateUtil.dateToLong(dateTime, "yyyy-MM-dd HH:mm:ss");
			payOrder.setCreatat(Integer.parseInt(String.valueOf(creatAt/1000)));
			payOrder.setPaytime(payOrder.getCreatat());
			payOrder.setRegisttime(userBaseInfo.getRegisttime());
			int i = payOrderService.savePayOrder(payOrder);
			if(i==0) {
				map.put("code", "201");
				map.put("msg", "保存虚拟交易失败");
				return JSONObject.toJSONString(map);
			}
			map.put("code", "200");
			map.put("msg", "保存虚拟交易成功");
			return JSONObject.toJSONString(map);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			map.put("code", "203");
			map.put("msg", "保存虚拟交易失败");
			return JSONObject.toJSONString(map);
		}
	}
	
	@RequestMapping(value = "/delPayOrder" , produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String delPayOrder(HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
			Integer userId = adminUser == null ? 0 : adminUser.getUid();
			PayOrder payOrder = new PayOrder();
			payOrder.setFlag(2);
			payOrder.setUpdateTime(new Date().getTime());
			payOrder.setUpdateUserId(userId);
			payOrder.setOrderId(request.getParameter("orderId"));
			int i = payOrderService.delPayOrder(payOrder);
			if(i==0) {
				map.put("code", "201");
				map.put("msg", "删除虚拟交易失败");
				return JSONObject.toJSONString(map);
			}
			map.put("code", "200");
			map.put("msg", "删除虚拟交易成功");
			return JSONObject.toJSONString(map);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			map.put("code", "203");
			map.put("msg", "删除虚拟交易失败");
			return JSONObject.toJSONString(map);
		}
	}
	
	private static String getTime3Random() {
		return System.currentTimeMillis() + RandomStringUtils.randomNumeric(3);
	}
}
