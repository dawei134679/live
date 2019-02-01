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
<title>商城统计</title>
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
		dataGrid = $("#staticlist").datagrid(
				{
					url : '../../operat/mallStatic',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '商城统计',
					pagination : true,
					rownumbers : true,
					singleSelect : true,
					pageSize : 20,
					pageList : [ 20, 50, 100 ],
					toolbar : '#tb',
					columns : [ [
							{
								field : 'gid',
								title : '礼物ID',
								align : 'center',
								width : 50,
								sortable : false
							},
							{
								field : 'gname',
								title : '礼物名称',
								align : 'center',
								width : 200,
								sortable : false
							},
							{
								field : 'exist',
								title : '现存人数',
								align : 'center',
								width : 150,
								sortable : false
							},
							{
								field : 'cushions',
								title : '缓存人数',
								width : 150,
								align : 'center',
								sortable : false
							},
							{
								field : 'counts',
								title : '历史人数',
								width : 150,
								align : 'center',
								sortable : false
							},
							{
								field : 'rate',
								title : '流失率(%)',
								width : 150,
								align : 'center',
								sortable : false
							}
							] ]
				})
	})

	function clearQuery() {
		$("#searchos").combobox('reset');
		$("#startdate").textbox('reset');
		$("#enddate").textbox("reset");
	}
	
	function queryStatic(){
		$("#staticlist").datagrid('reload', {
			searchos : $("#searchos").combobox("getValue"),
			startDate : $("#startdate").textbox('getValue'),
			endDate : $("#enddate").textbox('getValue')
		})
	}
</script>
</head>
<body>
	<div>
		<table id="staticlist"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				<select id="searchos" class="easyui-combobox" name="searchos"
					style="width:90px;">
					<option value="0">全部</option>
					<option value="43">白金VIP</option>
					<option value="44">钻石VIP</option>
					<option value="45">骑士守护</option>
					<option value="46">王子守护</option>
					<option value="60">萌猪卡</option>
				</select>
				&nbsp;开始时间：&nbsp;<input class="easyui-datebox" style="width:120px;height:24px" name="startdate" id="startdate" size="10" />
				&nbsp;结束时间：&nbsp;<input class="easyui-datebox" style="width:120px;height:24px" name="enddate"  id="enddate" size="10" />
				&nbsp;&nbsp;
				<a href="javascript:queryStatic()" class="easyui-linkbutton"
					iconCls="icon-search">搜索</a>&nbsp;&nbsp;&nbsp;&nbsp; <a
					href="javascript:clearQuery();" class="easyui-linkbutton">清空</a>
			</div>
		</div>
	</div>
</body>
</html>
