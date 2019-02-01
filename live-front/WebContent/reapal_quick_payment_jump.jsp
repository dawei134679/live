<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>融宝快捷支付跳转页</title>
	<script type="text/javascript" src="<%=request.getContextPath() %>/html/register/js/jquery-1.11.0.min.js"></script>
	<style type="text/css">
		 body{background-color:#f1f2f6;margin: 0; padding: 0;}
		.head{width: 100%; height: 150px; background-color:#F7aa14; text-align: center; position: relative;}
		.head span{color: #fff; line-height: 150px; font-size: 2.5rem; font-weight: bold;}
		.top{width: 100%;height: 400px;background-color: #fff;margin-top: 8px;position: relative; text-align: center;}
		.top .warp{display: inline-block;min-width: 90%;font-size:2.5rem;text-align:left;color:#2a2a2f;margin-bottom:2rem;}
		.top .button_p2p{display: inline-block;min-width: 90%;height:5rem;line-height:5rem;margin: 1rem auto;text-align: center;font-size: 3rem;color: #fff;background: #F7aa14;border: none;border-radius: 5px;}
	</style>
	<script type="text/javascript">
	$(document).ready(function() {
		$("#rongpaysubmit").submit();
	});
</script>
  </head>
  <body>
		<div class='head'>
	    <span>麦芽充值订单确认</span>
		</div>
		<div class='top'>
			<br/><br/><br/>
			<!-- 
			<div class="warp">&nbsp;&nbsp;&nbsp;订单号：0</div>
			<div class="warp">订单金额：0</div>
			 -->
			<div>
				${submitForm}
			</div>
		</div>
  </body>
</html>