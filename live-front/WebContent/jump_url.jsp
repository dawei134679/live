<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<script type="text/javascript" src="<%=request.getContextPath() %>/html/register/js/jquery-1.11.0.min.js"></script>
		<title>支付请求中...</title>
		<script type="text/javascript">
		  $(document).ready(function(){
			  var url = '<%=request.getAttribute("url")%>';
			  if(url && (url != 'null')){
				  window.location.href = url;
			  }else{
				  $("#a").html('请求参数错误...');
			  }
		  });
	  </script>
  </head>
  <body>
      	<div id="a">支付请求中...</div>
  </body>
</html>