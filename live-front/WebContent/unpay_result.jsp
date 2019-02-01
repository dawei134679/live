<%@page import="com.mpig.api.utils.SpringContextUtil"%>
<%@page import="com.mpig.api.utils.MD5Encrypt"%>
<%@page import="com.mpig.api.utils.Constant"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Iterator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
			String sdcustomno = new String(request.getParameter("sdcustomno").getBytes("ISO-8859-1"),"UTF-8");
			String state = new String(request.getParameter("state").getBytes("ISO-8859-1"),"UTF-8");
			String sd51no = new String(request.getParameter("sd51no").getBytes("ISO-8859-1"),"UTF-8");
			String orderMoney = new String(request.getParameter("orderMoney").getBytes("ISO-8859-1"),"UTF-8");
			String sign = new String(request.getParameter("sign").getBytes("ISO-8859-1"),"UTF-8");
			String mySign= MD5Encrypt.encrypt("sdcustomno="+sdcustomno+"&state="+state+"&sd51no="+sd51no+"&orderMoney="+orderMoney+"&key="+Constant.unpay_key).toUpperCase();
			
			StringBuffer sb =  new StringBuffer();
			if(mySign.equals(sign)){
				if("1".equals(state)){
				    sb.append("<div class='head'>");
				    sb.append("<span>订单交易成功</span>");
					sb.append("</div>");
					sb.append("<div class='top'>");
					sb.append("<img src='image/icon_success.png'>");
					sb.append("<span>支付成功</span><br/><br/><br/>");
					sb.append("<form><a href='maiyalivepay://success'><input type='button' value='返回麦芽' /></a></form><br /><br />");
					sb.append("</div>");
				}else{
					sb.append("<div class='head'>");
					sb.append("<span>订单交易处理中</span>");
					sb.append("</div>");
					sb.append("<div class='top'><br/><br/><br/>");
					sb.append("<span>处理中...</span><br/><br/><br/>");
					sb.append("<form><a href='maiyalivepay://fail'><input type='button' value='返回麦芽' /></a></form><br /><br />");
					sb.append("</div>");
				}
			}
		%>
		<%=sb.toString() %>
  </body>
</html>