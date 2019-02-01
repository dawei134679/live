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
<title>活跃列表</title>
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
		dataGrid = $("#summarylist").datagrid(
				{
					url : '../../operat/summary',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '活跃列表',
					pagination : true,
					rownumbers : true,
					singleSelect : true,
					pageSize : 20,
					pageList : [ 20, 50, 100 ],
					toolbar : '#tb',
					columns : [ [
							{
								field : 'times',
								title : '日期',
								align : 'center',
								sortable : false
							},
							{
								field : 'news',
								title : '新增设备数',
								align : 'center',
								sortable : false
							},
							{
								field : 'registers',
								title : '新增注册数',
								align : 'center',
								sortable : false
							},
							{
								field : 'registAccounts',
								title : '新增充值金额',
								align : 'center',
								sortable : false
							},
							{
								field : 'registPayUsers',
								title : '新增充值人数',
								align : 'center',
								sortable : false
							},
							{
								field : 'registPayArpu',
								title : '新增付费ARPU',
								align : 'center',
								sortable : false
							},
							{
								field : 'rate',
								title : '注册转化(%)',
								align : 'center',
								sortable : false
							},
							{
								field : 'equipments',
								title : '活跃设备',
								align : 'center',
								sortable : false
							},
							{
								field : 'actives',
								title : '活跃账号',
								align : 'center',
								sortable : false
							},
							{
								field : 'amounts',
								title : '总充值金额',
								align : 'center',
								sortable : false
							},
							{
								field : 'payUsers',
								title : '总充值人数',
								align : 'center',
								sortable : false
							},
							{
								field : 'payRate',
								title : '付费率(%)',
								align : 'center',
								sortable : false
							},
							{
								field : 'payArpu',
								title : '总ARPU',
								align : 'center',
								sortable : false
							}
							] ]
				})
	})

	function clearQuery() {
		$("#searchos").combobox('reset');
		$("#startdate").textbox('reset');
		$("#channelName").combobox('reset');
		$("#enddate").textbox("reset");
	}
	
	function querySummary(){
		$("#summarylist").datagrid('reload', {
			os : $("#searchos").combobox("getValue"),
			startDate : $("#startdate").textbox('getValue'),
			endDate : $("#enddate").textbox('getValue'),
			channelName : $("#channelName").combobox("getValue")
		})
	}
</script>
</head>
<body>
	<div>
		<table id="summarylist"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				<select id="searchos" class="easyui-combobox" name="searchos"
					style="width:90px;">
					<option value="0">全部</option>
					<option value="1">android</option>
					<option value="2">apple</option>
					<option value="3">H5</option>
					<option value="5">WEB</option>
				</select>
				渠道:<input id=channelName name="channelName" class="easyui-combobox" required="true" 
							data-options="valueField: 'channelCode',
    									textField: 'channelName',
    									url: '../../operat/getChannelForSelect'"
    						/>
				&nbsp;开始时间：&nbsp;<input class="easyui-datebox" style="width:160px;height:24px" name="startdate" id="startdate" size="10" />
				&nbsp;结束时间：&nbsp;<input class="easyui-datebox" style="width:160px;height:24px" name="enddate"  id="enddate" size="10" />
				
				<a href="javascript:querySummary()" class="easyui-linkbutton"
					iconCls="icon-search">搜索</a>&nbsp;&nbsp;&nbsp;&nbsp; <a
					href="javascript:clearQuery();" class="easyui-linkbutton">清空</a>
			</div>
		</div>
	</div>
</body>
</html>
