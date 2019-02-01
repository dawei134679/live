<%--
  Created by IntelliJ IDEA.
  User: fangwuqing
  Date: 16/5/6
  Time: 23:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>礼物送出列表</title>
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
		dataGrid = $("#sendGiftlist").datagrid(
				{
					url : '../../operat/sendgiftlist',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '礼物送出列表',
					pagination : true,
					idField : "times",
					sortName : 'times',
					sortOrder : 'desc',
					rownumbers : true,
					singleSelect : true,
					pageSize : 30,
					pageList : [ 30, 50, 100 ],
					toolbar : '#tb',
					columns : [ [
							{
								field : 'gid',
								title : '礼物ID',
								align : 'center',
								width : 100,
								sortable : false
							},{
								field : 'gname',
								title : '礼物名称',
								align : 'center',
								width : 300,
								sortable : false
							},
							{
								field : 'counts',
								title : '送出数量',
								align : 'center',
								width : 200,
								sortable : false
							},
							{
								field : 'amount',
								title : '金币数',
								align : 'center',
								width : 200,
								sortable : false
							}
							] ]
				})
	})

	function clearQuery() {
		$("#startdate").textbox('reset');
		$("#enddate").textbox('reset');
	}
	
	function querySendGift(){
		$("#sendGiftlist").datagrid('reload', {
			startDate : $("#startdate").textbox('getValue'),
			endDate : $("#enddate").textbox('getValue')
		})
	}
</script>
</head>
<body>
	<div>
		<table id="sendGiftlist"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				开始时间：&nbsp;<input class="easyui-datebox" style="width:160px;height:24px" name="startdate" id="startdate" size="10" />
				&nbsp;结束时间：&nbsp;<input class="easyui-datebox" style="width:160px;height:24px" name="enddate"  id="enddate" size="10" />
				&nbsp;&nbsp; 
				<a href="javascript:querySendGift()" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
				&nbsp;&nbsp;&nbsp;&nbsp; 
				<a href="javascript:clearQuery();" class="easyui-linkbutton">清空</a> (<font color="red">包含开始时间和结束时间</font>)
			</div>
		</div>
	</div>
</body>
</html>
