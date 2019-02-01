<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<script type="text/javascript" src="<%=request.getContextPath() %>/html/register/js/jquery-1.11.0.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/html/common/api.js"></script>
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
			.hide{display: none;}
		</style>
		<script type="text/javascript">
		  $(document).ready(function(){
			  var orderNo = '${param.orderNo}';
			  setTimeout(function(){
				  $.ajax({
					  url:api.getOrderByNoNew,
					  data:{"orderno":orderNo},
					  success:function(res){
						  if(res==null){
							 $("#processDiv").show();
							 $("#successDiv").hide();
		 					 return;
						  }
						  var d = res.data;
						  if(res.code!=200){
							  $("#processDiv").show();
							  $("#successDiv").hide();
		 					 return;
						  }
						  if(d.data.status!=2){
							  $("#processDiv").show();
							  $("#successDiv").hide();
						  }else{
							  $("#processDiv").hide();
							  $("#successDiv").show();
						  }
					  },
					   error:function(jqXHR, textStatus, errorThrown){
						   $("#processDiv").show();
						   $("#successDiv").hide();
						   return;
					  }
				  });
			  },1000);
		  });
	  </script>
  </head>
  <body>
      <div id="processDiv" class="">
		  <div class='head'>
				<span>订单交易处理中</span>
		  </div>
		  <div class='top'><br/><br/><br/>
				<span>处理中...</span><br/><br/><br/>
				<form><a href='maiyalivepay://success'><input type='button' value='返回麦芽' /></a></form><br /><br />
		  </div>
	  </div>
	  <div id="successDiv" class="hide">
		  <div class='head'>
				<span>订单交易</span>
		  </div>
		  <div class='top'><br/><br/><br/>
				<span>支付成功</span><br/><br/><br/>
				<form><a href='maiyalivepay://success'><input type='button' value='返回麦芽' /></a></form><br /><br />
		  </div>
	  </div>
  </body>
</html>