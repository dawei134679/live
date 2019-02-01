<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="html" uri="/WEB-INF/auth.tld"%>
<html>
<head>
<title>验证码管理</title>
<link rel="stylesheet" type="text/css" href="../../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../../easyui/themes/icon.css">
<script type="text/javascript" src="../../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../../easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script>
	var dataGrid;
	var url;

	$(function() {
		dataGrid = $("#gridList").datagrid({
			url : '../../verifyCode/getVerifyCodeList',
			width : getWidth(0.97),
			height : getHeight(0.97),
			title : '验证码列表',
			queryParams: {},
			pagination : true,
	        rownumbers: true,
			striped : true,
	        singleSelect: true,
	        fit:true,
	        fitColumns:true,
	        pageSize:20,
	        pageList:[20,50,100],
			toolbar : '#tb',
			columns : [[
				{field : 'id',title : 'ID',align : 'center',width : 50,sortable : false},
				{field : 'mobile',title : '手机号',align : 'center',width : 110,sortable : false},
				{field : 'type',title : '类型',align : 'center',width : 110,sortable : false,formatter:function(data,row){
					var ary = ['','注册','重置密码'];
					return ary[data];
				}},
				{field : 'verifyCode',title : '验证码',align : 'center',width : 110,sortable : false},
				{field : 'takeEffectTime',title : '生效时间',align : 'center',width : 110,sortable : false,formatter:function(data,row){
					return data ? new Date(data * 1000).Format("yyyy-MM-dd HH:mm:ss") : "";
				}},
				{field : 'expiryTime',title : '过期时间',align : 'center',width : 110,sortable : false,formatter:function(data,row){
					return data ? new Date(data * 1000).Format("yyyy-MM-dd HH:mm:ss") : "";
				}}
			]]
		});
		
	});
	
	function searchEvent() {
		$('#gridList').datagrid('load', {
			mobile : $('#s_mobile').textbox("getValue")
		});
	}
	
</script>
</head>
<body style="margin: 5px;">
	<table id="gridList"></table>
	<div id="tb" style="padding: 2px 5px; height: auto">
		<div>
			手机号:&nbsp;<input id="s_mobile" name="s_mobile" class="easyui-textbox" />
			&nbsp;&nbsp;<a href="javascript:searchEvent()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
		</div>
	</div>
</body>
</html>