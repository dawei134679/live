<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>融宝快捷支付结果页</title>
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
	<c:choose>
	   <c:when test="${success eq 'success'}">  
	     <div class='head'>
		    <span>订单交易成功</span>
			</div>
			<div class='top'>
			<img src='<%=request.getContextPath() %>/image/icon_success.png'>
			<span>支付成功</span><br/><br/><br/>
			<form><a href='maiyalivepay://success'><input type='button' value='返回麦芽' /></a></form><br /><br />
		 </div>           
	   </c:when>
	   <c:otherwise> 
	     <div class='head'>
			<span>订单交易失败</span>
			</div>
			<div class='top'>
			<img src='<%=request.getContextPath() %>/image/icon_fail.png'>
			<span>支付失败</span><br/><br/><br/>
			<form><a href='maiyalivepay://fail'><input type='button' value='返回麦芽' /></a></form><br /><br />
		 </div>
	   </c:otherwise>
	</c:choose>
</body>
</html>