<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
	String base = request.getContextPath();
	
	response.setHeader("Cache-Control","no-cache");   
	response.setDateHeader("Expires",0);   
	response.setHeader("Pragma","No-cache");
	
	Integer defcol = 12;//单独使用改成9 然后解除left注释
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
<meta content="width=device-width,user-scalable=no" name="viewport">
<meta name="format-detection" content="telephone=no">
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<%-- <link rel="shortcut icon" href="<%=base%>/static/images/ico32.ico"> --%>

<script src="<%=base%>/static/jquery/jquery-1.11.1.min.js"></script>
<script src="<%=base%>/static/jquery/jquery.form.js"></script>
<script src="<%=base%>/static/jquery/ajaxfileupload.js"></script>

<%-- bootstrap3 --%>
<script src="<%=base%>/static/bootstrap-3.3.5/js/bootstrap.min.js"></script>
<link href="<%=base%>/static/bootstrap-3.3.5/css/bootstrap.min.css" rel="stylesheet" />
<%-- 时间控件 --%>
<script src="<%=base%>/static/bootstrap-3.3.5/js/bootstrap-datetimepicker.js"></script>
<script src="<%=base%>/static/bootstrap-3.3.5/js/bootstrap-datetimepicker.zh-CN.js"></script>
<link href="<%=base%>/static/bootstrap-3.3.5/css/bootstrap-datetimepicker.min.css" rel="stylesheet" />
<%-- 弹出层 --%>
<script src="<%=base%>/static/bootstrap-3.3.5/js/bootstrap-dialog.min.js"></script>
<link href="<%=base%>/static/bootstrap-3.3.5/css/bootstrap-dialog.min.css" rel="stylesheet" />
<%--文件选择上传 --%>
<script src="<%=base%>/static/bootstrap-3.3.5/js/fileinput.min.js"></script>
<script src="<%=base%>/static/bootstrap-3.3.5/js/fileinput_locale_zh.js"></script>
<link href="<%=base%>/static/bootstrap-3.3.5/css/fileinput.min.css" rel="stylesheet" />
<%--多选下拉列表 --%>
<script src="<%=base%>/static/bootstrap-3.3.5/js/bootstrap-multiselect.js"></script>
<link href="<%=base%>/static/bootstrap-3.3.5/css/bootstrap-multiselect.css" rel="stylesheet" />
<%-- 后台样式都在这里 --%>
<link href="<%=base%>/static/css/main.css" rel="stylesheet" />
<script type="text/javascript" src="<%=base%>/js/common.js"></script>
<script type="text/javascript">
var _basepath = "<%=base%>";
$(document).ready(function(){
	//时间控件
	$('.form_date').datetimepicker({
	    language:  'zh-CN',
	    weekStart: 1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 2,
		format: 'yyyy-mm-dd',
	});
	$('.form_date_ss').datetimepicker({
	    language:  'zh-CN',
	    weekStart: 1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 2,
		format: 'yyyy-mm-dd',
	});
	
	$('.form_date_endDate').datetimepicker({
	    language:  'zh-CN',
	    weekStart: 1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 2,
		format: 'yyyy-mm-dd',
		todayBtn: true,
		endDate: new Date()
	});
});

</script>
<title>麦芽</title>