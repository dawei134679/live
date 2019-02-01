<%@page import="com.mpig.api.utils.Constant"%>
<%@page import="com.alipay.api.internal.util.AlipaySignature"%>
<%
/* *
 功能：支付宝页面跳转同步通知页面
 版本：3.2
 日期：2011-03-17
 说明：
 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。

 //***********页面功能说明***********
 该页面可在本机电脑测试
 可放入HTML等美化页面的代码、商户业务逻辑程序代码
 TRADE_FINISHED(表示交易已经成功结束，并不能再对该交易做后续操作);
 TRADE_SUCCESS(表示交易已经成功结束，可以对该交易做后续操作，如：分润、退款等);
 //********************************
 * */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.alipay.config.*"%>
<%@ page import="com.mpig.api.utils.Constant"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>支付结果</title>
		<style type="text/css">
			 body{background-color:#f1f2f6;margin: 0; padding: 0;}
			.head{width: 100%; height: 150px; background-color:#F7aa14; text-align: center; position: relative;}
			.head span{color: #fff; line-height: 150px; font-size: 2.5rem; font-weight: bold;}
			.top{width: 100%;height: 400px;background-color: #fff;margin-top: 8px;position: relative; text-align: center;}
			.top img{width: 100px; height: 100px; margin-top: 120px;margin-left: -30px;}
			.top span{font-size: 3rem;top: -30px;position: relative;}
			.top label{font-size: 4rem; font-weight:bold;}
			.top form input{font-size: 2.5rem; padding: 20px; border-radius: 20px; border:3px solid #F7aa14; background-color: #f7aa14; color: #fff;}
		</style>
  </head>
  <body>
<%
	//获取支付宝GET过来反馈信息
	Map<String,String> params = new HashMap<String,String>();
	Map requestParams = request.getParameterMap();
	for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
		String name = (String) iter.next();
		String[] values = (String[]) requestParams.get(name);
		String valueStr = "";
		for (int i = 0; i < values.length; i++) {
			valueStr = (i == values.length - 1) ? valueStr + values[i]
					: valueStr + values[i] + ",";
		}
		//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
		valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
		params.put(name, valueStr);
	}
	//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
	//商户订单号
	String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

	//支付宝交易号
	String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

	//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
	//计算得出通知验证结果
	boolean verify_result = AlipaySignature.rsaCheckV1(params, Constant.alipay_publicKey, Constant.defaultCharset, "RSA2");
	if(verify_result){
		StringBuffer sb =  new StringBuffer();
	    sb.append("<div class='head'>");
	    sb.append("<span>订单交易成功</span>");
		sb.append("</div>");
		sb.append("<div class='top'>");
		sb.append("<img src='image/icon_success.png'>");
		sb.append("<span>支付成功</span><br/><br/><br/>");
		sb.append("<form><a href='maiyalivepay://success'><input type='button' value='返回麦芽' /></a></form><br /><br />");
		sb.append("</div>");
		%>
		<%=sb.toString() %>
		<%
	}else{
		StringBuffer sb = new StringBuffer();
		sb.append("<div class='head'>");
		sb.append("<span>订单交易失败</span>");
		sb.append("</div>");
		sb.append("<div class='top'>");
		sb.append("<img src='image/icon_fail.png'>");
		sb.append("<span>支付失败</span><br/><br/><br/>");
		sb.append("<form><a href='maiyalivepay://fail'><input type='button' value='返回麦芽' /></a></form><br /><br />");
		sb.append("</div>");
		 %>
		<%=sb.toString() %>
		<%
	}
%>
  </body>
</html>